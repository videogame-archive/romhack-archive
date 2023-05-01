/* Apache 2 License, Copyright (c) 2023 Juan Fuentes, based on Rom Patcher JS by Marc Robledo */
package com.github.videogamearchive.rompatcher.formats;

import com.github.videogamearchive.rompatcher.MarcFile;
import java.util.ArrayList;
import java.util.List;

import static com.github.videogamearchive.rompatcher.CRC.crc32;

public class UPS {
    public static String UPS_MAGIC="UPS1";
    private List<UpsRecord> records;
    private int sizeInput, sizeOutput;
    private long checksumInput, checksumOutput;

    public UPS(){
        this.records=new ArrayList<>();
        this.sizeInput=0; this.sizeOutput=0;
        this.checksumInput=0; this.checksumOutput=0;
    }
    public void addRecord(int relativeOffset,List<Integer> d){
        this.records.add(new UpsRecord(relativeOffset, d));
    }
    public MarcFile export(){
        var patchFileSize=UPS_MAGIC.length();//UPS1 string
        patchFileSize+=UPS_getVLVLength(this.sizeInput); //input file size
        patchFileSize+=UPS_getVLVLength(this.sizeOutput); //output file size
        for(var i=0; i<this.records.size(); i++){
            patchFileSize+=UPS_getVLVLength(this.records.get(i).offset);
            patchFileSize+= this.records.get(i).XORdata.size()+1;
        }
        patchFileSize+=12; //input/output/patch checksums

        MarcFile tempFile=new MarcFile(patchFileSize);
//        tempFile.writeVLV=UPS_writeVLV;
//        tempFile.fileName=fileName+'.ups';
        tempFile.writeString(UPS_MAGIC);

        UPS_writeVLV(tempFile, this.sizeInput);
        UPS_writeVLV(tempFile, this.sizeOutput);

        for(var i=0; i<this.records.size(); i++){
            UPS_writeVLV(tempFile, this.records.get(i).offset);
            tempFile.writeBytes(this.records.get(i).XORdata);
            tempFile.writeU8(0x00);
        }
        tempFile.littleEndian=true;
        tempFile.writeU32(this.checksumInput);
        tempFile.writeU32(this.checksumOutput);
        tempFile.writeU32(crc32(tempFile, 0, true));

        return tempFile;
    }
    public boolean validateSource(MarcFile romFile){return crc32(romFile)==this.checksumInput;}
    public MarcFile apply(MarcFile romFile,boolean validate){
        if(validate && !this.validateSource(romFile)){
            throw new Error("error_crc_input");
        }

        /* fix the glitch that cut the end of the file if it's larger than the changed file patch was originally created with */
        /* more info: https://github.com/marcrobledo/RomPatcher.js/pull/40#issuecomment-1069087423 */
        int sizeOutput = this.sizeOutput;
        int sizeInput = this.sizeInput;
        if(!validate && sizeInput < romFile.fileSize){
            sizeInput = romFile.fileSize;
            if(sizeOutput < sizeInput){
                sizeOutput = sizeInput;
            }
        }

        /* copy original file */
        MarcFile tempFile=new MarcFile(sizeOutput);
        romFile.copyToFile(tempFile, 0, sizeInput);

        romFile.seek(0);


        var nextOffset=0;
        for(var i=0; i<this.records.size(); i++){
            var record= this.records.get(i);
            tempFile.skip(record.offset);
            romFile.skip(record.offset);

            for(var j=0; j<record.XORdata.size(); j++){
                tempFile.writeU8((romFile.isEOF()?0x00:romFile.readU8()) ^ record.XORdata.get(j));
            }
            tempFile.skip(1);
            romFile.skip(1);
        }

        if(validate && crc32(tempFile)!=this.checksumOutput){
            throw new Error("error_crc_output");
        }

        return tempFile;
    }


    /* encode/decode variable length values, used by UPS file structure */
    public static void UPS_writeVLV(MarcFile file, int data){
        while(true){
            var x=data & 0x7f;
            data=data>>7;
            if(data==0){
                file.writeU8(0x80 | x);
                break;
            }
            file.writeU8(x);
            data=data-1;
        }
    }
    public static int UPS_readVLV(MarcFile file){
        int data=0;

        int shift=1;
        while(true){
            var x=file.readU8();

            if(x==-1)
                throw new Error("Can't read UPS VLV at 0x"+(file.offset-1));

            data+=(x&0x7f)*shift;
            if((x&0x80)!=0)
                break;
            shift=shift<<7;
            data+=shift;
        }
        return data;
    }
    public static int UPS_getVLVLength(int data){
        int len=0;
        while(true){
            var x=data & 0x7f;
            data=data>>7;
            len++;
            if(data==0){
                break;
            }
            data=data-1;
        }
        return len;
    }

    public static UPS parseUPSFile(MarcFile file){
        var patch=new UPS();


        file.seek(UPS_MAGIC.length());

        patch.sizeInput=UPS_readVLV(file);
        patch.sizeOutput=UPS_readVLV(file);


        int nextOffset=0;
        while(file.offset<(file.fileSize-12)){
            int relativeOffset=UPS_readVLV(file);


            List<Integer> XORdifferences=new ArrayList<>();
            while(file.readU8() > 0){
                XORdifferences.add((Integer) file._lastRead);
            }
            patch.addRecord(relativeOffset, XORdifferences);
        }

        file.littleEndian=true;
        patch.checksumInput=file.readU32();
        patch.checksumOutput=file.readU32();

        if(file.readU32()!=crc32(file, 0, true)){
            throw new Error("error_crc_patch");
        }

        file.littleEndian=false;
        return patch;
    }



    public static UPS createUPSFromFiles(MarcFile original,MarcFile modified){
        var patch=new UPS();
        patch.sizeInput=original.fileSize;
        patch.sizeOutput=modified.fileSize;


        var previousSeek=1;
        while(!modified.isEOF()){
            int b1=original.isEOF()?0x00:original.readU8();
            int b2=modified.readU8();

            if(b1!=b2){
                int currentSeek=modified.offset;
                List<Integer> XORdata=new ArrayList<>();

                while(b1!=b2){
                    XORdata.add(b1 ^ b2);

                    if(modified.isEOF())
                        break;
                    b1=original.isEOF()?0x00:original.readU8();
                    b2=modified.readU8();
                }

                patch.addRecord(currentSeek-previousSeek, XORdata);
                previousSeek=currentSeek+XORdata.size()+1;
            }
        }


        patch.checksumInput=crc32(original);
        patch.checksumOutput=crc32(modified);
        return patch;
    }

    private static class UpsRecord {
        int offset;
        List<Integer> XORdata;

        public UpsRecord(int offset, List<Integer> XORdata) {
            this.offset = offset;
            this.XORdata = XORdata;
        }
    }
}
