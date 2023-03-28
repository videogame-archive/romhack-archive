/* Apache 2 License, Copyright (c) 2023 Juan Fuentes, based on Rom Patcher JS by Marc Robledo */
package com.github.openretrogamingarchive.rompatcher.formats;

import com.github.openretrogamingarchive.rompatcher.MarcFile;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class IPS {
    public static final String IPS_MAGIC="PATCH";
    public static final int IPS_MAX_SIZE=0x1000000; //16 megabytes
    public static final int IPS_RECORD_RLE=0x0000;
    public static final int IPS_RECORD_SIMPLE=0x01;
    private List<IpsRecord> records;
    private int truncate;
    public IPS(){
        this.records=new ArrayList<>();
        // NOTE FOR MARC
        // Truncate default value is used, been this one 0, setting its value boolean seems incorrect
        this.truncate=0; //=false
    }
    public void addSimpleRecord(int o, List<Integer> d){
        this.records.add(new IpsRecord(o, null, IPS_RECORD_SIMPLE, d.size(), d, null));
    }
    public void addRLERecord(int o,int l,int b){
        this.records.add(new IpsRecord(o, null, IPS_RECORD_RLE, l, null, b));
    }









    public MarcFile export(){
        var patchFileSize=5; //PATCH string
        for(var i=0; i<this.records.size(); i++){
            if(this.records.get(i).type==IPS_RECORD_RLE)
                patchFileSize+=(3+2+2+1); //offset+0x0000+length+RLE byte to be written
            else
                patchFileSize+=(3+2+ this.records.get(i).data.size()); //offset+length+data
        }
        patchFileSize+=3; //EOF string
        if(this.truncate > 0)
            patchFileSize+=3; //truncate

        MarcFile tempFile=new MarcFile(patchFileSize);
        tempFile.writeString(IPS_MAGIC);
        for(var i=0; i<this.records.size(); i++){
            IpsRecord rec= this.records.get(i);
            tempFile.writeU24(rec.offset);
            if(rec.type==IPS_RECORD_RLE){
                tempFile.writeU16(0x0000);
                tempFile.writeU16(rec.length);
                tempFile.writeU8(rec.oneByte);
            }else{
                tempFile.writeU16(rec.data.size());
                tempFile.writeBytes(rec.data);
            }
        }

        tempFile.writeString("EOF");
// NOTE FOR MARC T
// These statements seem unreachable, the rec is declared outside of scope
// and even if a "var" persists outside the scope avoiding an error, a truncate field is never set on rec.
//        if(rec.truncate)
//            tempFile.writeU24(rec.truncate);
        return tempFile;
    }
    public MarcFile apply(MarcFile romFile){
        MarcFile tempFile;
        if(this.truncate > 0){
            if(this.truncate>romFile.fileSize){ //expand (discussed here: https://github.com/marcrobledo/RomPatcher.js/pull/46)
                tempFile=new MarcFile(this.truncate);
                romFile.copyToFile(tempFile, 0, romFile.fileSize, 0);
            }else{ //truncate
                tempFile=romFile.slice(0, this.truncate);
            }
        }else{
            //calculate target ROM size, expanding it if any record offset is beyond target ROM size
            var newFileSize=romFile.fileSize;
            for(var i=0; i<this.records.size(); i++){
                IpsRecord rec=this.records.get(i);
                if(rec.type==IPS_RECORD_RLE){
                    if(rec.offset+rec.length>newFileSize){
                        newFileSize=rec.offset+rec.length;
                    }
                }else{
                    if(rec.offset+rec.data.size()>newFileSize){
                        newFileSize=rec.offset+rec.data.size();
                    }
                }
            }

            if(newFileSize==romFile.fileSize){
                tempFile=romFile.slice(0, romFile.fileSize);
            }else{
                tempFile=new MarcFile(newFileSize);
                romFile.copyToFile(tempFile,0);
            }
        }


        romFile.seek(0);

        for(var i=0; i<this.records.size(); i++){
            tempFile.seek(this.records.get(i).offset);
            if(this.records.get(i).type==IPS_RECORD_RLE){
                for(var j=0; j<this.records.get(i).length; j++)
                    tempFile.writeU8(this.records.get(i).oneByte);
            }else{
                tempFile.writeBytes(this.records.get(i).data);
            }
        }

        return tempFile;
    }




    public static IPS parseIPSFile(MarcFile file){
        var patchFile=new IPS();
        file.seek(5);

        while(!file.isEOF()){
            var offset=file.readU24();

            if(offset==0x454f46){ /* EOF */
                if(file.isEOF()){
                    break;
                }else if((file.offset+3)==file.fileSize){
                    patchFile.truncate=file.readU24();
                    break;
                }
            }

            var length=file.readU16();

            if(length==IPS_RECORD_RLE){
                patchFile.addRLERecord(offset, file.readU16(), file.readU8());
            }else{
                patchFile.addSimpleRecord(offset, file.readBytes(length));
            }
        }
        return patchFile;
    }


    public static IPS createIPSFromFiles(MarcFile original, MarcFile modified){
        var patch=new IPS();

        if(modified.fileSize<original.fileSize){
            patch.truncate=modified.fileSize;
        }

        //solucion: guardar startOffset y endOffset (ir mirando de 6 en 6 hacia atrás)
        IpsRecord previousRecord = new IpsRecord(0, 0, 0xdeadbeef, 0, null, null);
        while(!modified.isEOF()){
            int b1=original.isEOF()?0x00:original.readU8();
            int b2=modified.readU8();

            if(b1!=b2){
                boolean RLEmode=true;
                List<Integer> differentData=new ArrayList<>();
                var startOffset=modified.offset-1;

                while(b1!=b2 && differentData.size()<0xffff){
                    differentData.add(b2);
                    if(b2!=differentData.get(0))
                        RLEmode=false;

                    if(modified.isEOF() || differentData.size()==0xffff)
                        break;

                    b1=original.isEOF()?0x00:original.readU8();
                    b2=modified.readU8();
                }


                //check if this record is near the previous one
                var distance=startOffset-(previousRecord.offset+previousRecord.length);
                if(
                        previousRecord.type==IPS_RECORD_SIMPLE &&
                                distance<6 && (previousRecord.length+distance+differentData.size())<0xffff
                ){
                    if(RLEmode && differentData.size()>6){
                        // separate a potential RLE record
                        original.seek(startOffset);
                        modified.seek(startOffset);
                        previousRecord = new IpsRecord(0, 0, 0xdeadbeef, 0, null, null);
                    }else{
                        // merge both records
                        while(distance-- > 0){
                            previousRecord.data.add(modified._u8array[previousRecord.offset+previousRecord.length]);
                            previousRecord.length++;
                        }
                        previousRecord.data.addAll(differentData);
                        previousRecord.length=previousRecord.data.size();
                    }
                }else{
                    if(startOffset>=IPS_MAX_SIZE){
                        throw new Error("files are too big for IPS format");
                        //return null;
                    }

                    if(RLEmode && differentData.size()>2){
                        patch.addRLERecord(startOffset, differentData.size(), differentData.get(0));
                    }else{
                        patch.addSimpleRecord(startOffset, differentData);
                    }
                    previousRecord=patch.records.get(patch.records.size() - 1);
                }
            }
        }




        if(modified.fileSize>original.fileSize){
            IpsRecord lastRecord=patch.records.get(patch.records.size() - 1);
            var lastOffset=lastRecord.offset+lastRecord.length;

            if(lastOffset<modified.fileSize){
                patch.addSimpleRecord(modified.fileSize-1, new ArrayList<Integer>(Arrays.asList(0x00)));
            }
        }


        return patch;
    }

    private static class IpsRecord {
        public int offset;
        public Integer startOffset;
        public int type;
        public int length;
        public List<Integer> data;
        public Integer oneByte;

        public IpsRecord(int offset, Integer startOffset, int type, int length, List<Integer> data, Integer oneByte) {
            this.offset = offset;
            this.startOffset = startOffset;
            this.type = type;
            this.length = length;
            this.data = data;
            this.oneByte = oneByte;
        }
    }
}
