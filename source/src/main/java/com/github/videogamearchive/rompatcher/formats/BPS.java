/* Apache 2 License, Copyright (c) 2023 Juan Fuentes, based on Rom Patcher JS by Marc Robledo */
package com.github.videogamearchive.rompatcher.formats;

import com.github.videogamearchive.rompatcher.MarcFile;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.github.videogamearchive.rompatcher.CRC.crc32;

public class BPS {
    private static final String BPS_MAGIC = "BPS1";
    private static final int BPS_ACTION_SOURCE_READ=0;
    private static final int  BPS_ACTION_TARGET_READ=1;
    private static final int  BPS_ACTION_SOURCE_COPY=2;
    private static final int  BPS_ACTION_TARGET_COPY=3;
    public int sourceSize, targetSize;
    public String metaData;
    public List<BPSAction> actions;
    public long sourceChecksum, targetChecksum, patchChecksum;
    private BPS() {
        this.sourceSize = 0; this.targetSize = 0;
        this.metaData = "";
        this.actions = new ArrayList<>();
        this.sourceChecksum = 0; this.targetChecksum = 0; this.patchChecksum = 0;
    }
    public boolean validateSource(MarcFile romFile){return this.sourceChecksum==crc32(romFile);}
    public MarcFile apply(MarcFile romFile, boolean validate){
        if(validate && !this.validateSource(romFile)){
            throw new Error("error_crc_input");
        }


        MarcFile tempFile=new MarcFile(this.targetSize);


        //patch
        int sourceRelativeOffset=0;
        int targetRelativeOffset=0;
        for(int i=0; i<this.actions.size(); i++){
            BPSAction action= this.actions.get(i);

            if(action.type==BPS_ACTION_SOURCE_READ){
                romFile.copyToFile(tempFile, tempFile.offset, action.length, null);
                tempFile.skip(action.length);

            }else if(action.type==BPS_ACTION_TARGET_READ){
                tempFile.writeBytes(action.bytes);

            }else if(action.type==BPS_ACTION_SOURCE_COPY){
                sourceRelativeOffset+=action.relativeOffset;
                int actionLength=action.length;
                while(actionLength-- != 0){
                    tempFile.writeU8(romFile._u8array[sourceRelativeOffset]);
                    sourceRelativeOffset++;
                }
            }else if(action.type==BPS_ACTION_TARGET_COPY){
                targetRelativeOffset+=action.relativeOffset;
                int actionLength=action.length;
                while(actionLength-- != 0) {
                    tempFile.writeU8(tempFile._u8array[targetRelativeOffset]);
                    targetRelativeOffset++;
                }
            }
        }

        if(validate && this.targetChecksum!=crc32(tempFile)){
            throw new Error("error_crc_output");
        }

        return tempFile;
    }



    public static BPS parseBPSFile(MarcFile file){


        file.littleEndian=true;
        BPS patch=new BPS();


        file.seek(4); //skip BPS1

        patch.sourceSize=BPS_readVLV(file);
        patch.targetSize=BPS_readVLV(file);

        int metaDataLength=BPS_readVLV(file);
        if(metaDataLength > 0){
            patch.metaData=file.readString(metaDataLength);
        }


        int endActionsOffset=file.fileSize-12;
        while(file.offset<endActionsOffset){
            int data=BPS_readVLV(file);
            BPSAction action = new BPSAction( data & 3, (data >> 2)+1, null, null);

            if(action.type==BPS_ACTION_TARGET_READ){
                action.bytes=file.readBytes(action.length);

            }else if(action.type==BPS_ACTION_SOURCE_COPY || action.type==BPS_ACTION_TARGET_COPY){
                int relativeOffset=BPS_readVLV(file);
                action.relativeOffset=((relativeOffset & 1) > 0? -1 : +1) * (relativeOffset >> 1);
            }

            patch.actions.add(action);
        }

        //file.seek(endActionsOffset);
        patch.sourceChecksum=file.readU32();
        patch.targetChecksum=file.readU32();
        patch.patchChecksum=file.readU32();

        if(patch.patchChecksum!=crc32(file, 0, true)){
            throw new Error("error_crc_patch");
        }


        return patch;
    }



    private static int BPS_readVLV(MarcFile file){
        int data=0, shift=1;
        while(true){
            int x = file.readU8();
            data += (x & 0x7f) * shift;
            if((x & 0x80) > 0)
                break;
            shift <<= 7;
            data += shift;
        }

        file._lastRead=data;
        return data;
    }
    private static void BPS_writeVLV(MarcFile file, int data){
        while(true){
            int x = data & 0x7f;
            data >>= 7;
            if(data == 0){
                file.writeU8(0x80 | x);
                break;
            }
            file.writeU8(x);
            data--;
        }
    }
    private static int BPS_getVLVLen(int data){
        if (data < 0) {
            throw new RuntimeException("VLV is negative: This is more likely an overflow due to a BPS_ACTION_SOURCE_READ length being too big for this tool to handle. Please use other tool. Sorry.");
        }
        int len=0;
        while(true){
            int x = data & 0x7f;
            data >>= 7;
            if(data == 0){
                len++;
                break;
            }
            len++;
            data--;
        }
        return len;
    }


    public MarcFile export() throws IOException {
        int patchFileSize = BPS_MAGIC.length();
        patchFileSize += BPS_getVLVLen(this.sourceSize);
        patchFileSize += BPS_getVLVLen(this.targetSize);
        patchFileSize += BPS_getVLVLen(this.metaData.length());
        patchFileSize += metaData.length();
        for(int i=0; i < this.actions.size(); i++){
            BPSAction action= this.actions.get(i);
            patchFileSize+=BPS_getVLVLen(((action.length-1)<<2) + action.type);

            if(action.type==BPS_ACTION_TARGET_READ){
                patchFileSize+=action.length;
            }else if(action.type==BPS_ACTION_SOURCE_COPY || action.type==BPS_ACTION_TARGET_COPY){
                patchFileSize+=BPS_getVLVLen((Math.abs(action.relativeOffset)<<1)+(action.relativeOffset<0?1:0));
            }
        }
        patchFileSize+=12;

        MarcFile patchFile = new MarcFile(patchFileSize);

        patchFile.littleEndian=true;


        patchFile.writeString(BPS_MAGIC);
        BPS_writeVLV(patchFile, this.sourceSize);
        BPS_writeVLV(patchFile, this.targetSize);
        BPS_writeVLV(patchFile, this.metaData.length());
        patchFile.writeString(this.metaData);

        for(int i=0; i<this.actions.size(); i++){
            BPSAction action= this.actions.get(i);
            BPS_writeVLV(patchFile, ((action.length-1)<<2) + action.type);
            if(action.type == BPS_ACTION_TARGET_READ) {
                patchFile.writeBytes(action.bytes);
            }else if(action.type == BPS_ACTION_SOURCE_COPY || action.type == BPS_ACTION_TARGET_COPY){
                BPS_writeVLV(patchFile, (Math.abs(action.relativeOffset)<<1)+(action.relativeOffset<0?1:0));
            }
        }

        patchFile.writeU32(this.sourceChecksum);
        patchFile.writeU32(this.targetChecksum);
        patchFile.writeU32(this.patchChecksum);

        return patchFile;
    }

    public static class BPS_Node {
        public int offset=0;
        public BPS_Node next=null;
        public void delete() {
            if(this.next != null)
                this.next = null;
        }
    }
    public static BPS createBPSFromFiles(MarcFile original, MarcFile modified, boolean deltaMode) throws IOException {
        BPS patch=new BPS();
        patch.sourceSize = original.fileSize;
        patch.targetSize = modified.fileSize;

//        if(original.fileSize <= 4194304) {
        if(deltaMode) {
            patch.actions = createBPSFromFilesDelta(original, modified);
        } else {
            patch.actions = createBPSFromFilesLinear(original, modified);
        }

        patch.sourceChecksum= crc32(original);
        patch.targetChecksum= crc32(modified);
        patch.patchChecksum= crc32(patch.export(), 0, true);
        return patch;
    }

/* delta implementation from https://github.com/chiya/beat/blob/master/nall/beat/linear.hpp */
public static List<BPSAction> createBPSFromFilesLinear(MarcFile original, MarcFile modified){
        List<BPSAction> patchActions = new ArrayList<>();

        /* references to match original beat code */
        int[] sourceData= original._u8array;
        int[] targetData= modified._u8array;
        int sourceSize = original.fileSize;
        int targetSize = modified.fileSize;
        final int Granularity=1;



        int targetRelativeOffset=0;
        int outputOffset=0;
        int targetReadLength=0;















        while(outputOffset < targetSize) {
            int sourceLength = 0;
            for(int n = 0; outputOffset + n < Math.min(sourceSize, targetSize); n++) {
                if(sourceData[outputOffset + n] != targetData[outputOffset + n]) break;
                sourceLength++;
            }

            int rleLength = 0;
            for(int n = 1; outputOffset + n < targetSize; n++) {
                if(targetData[outputOffset] != targetData[outputOffset + n]) break;
                rleLength++;
            }

            if(rleLength >= 4) {
                //write byte to repeat
                targetReadLength++;
                outputOffset++;
                targetReadFlush(targetReadLength, outputOffset, targetData, patchActions); targetReadLength = 0;

                //copy starting from repetition byte
                //encode(TargetCopy | ((rleLength - 1) << 2));
                int relativeOffset = (outputOffset - 1) - targetRelativeOffset;
                //encode(relativeOffset << 1);
                patchActions.add(new BPSAction(BPS_ACTION_TARGET_COPY, rleLength, null, relativeOffset));
                outputOffset += rleLength;
                targetRelativeOffset = outputOffset - 1;
            } else if(sourceLength >= 4) {
                targetReadFlush(targetReadLength, outputOffset, targetData, patchActions); targetReadLength = 0;
                //encode(SourceRead | ((sourceLength - 1) << 2));
                patchActions.add(new BPSAction(BPS_ACTION_SOURCE_READ, sourceLength, null, null));
                outputOffset += sourceLength;
            } else {
                targetReadLength += Granularity;
                outputOffset += Granularity;
            }
        }

        targetReadFlush(targetReadLength, outputOffset, targetData, patchActions); targetReadLength = 0;



        return patchActions;
    }

    /* delta implementation from https://github.com/chiya/beat/blob/master/nall/beat/delta.hpp */
    public static List<BPSAction> createBPSFromFilesDelta(MarcFile original, MarcFile modified) {
        List<BPSAction> patchActions = new ArrayList<>();


        /* references to match original beat code */
        int[] sourceData= original._u8array;
        int[] targetData= modified._u8array;
        int sourceSize = original.fileSize;
        int targetSize = modified.fileSize;
        final int Granularity=1;



        int sourceRelativeOffset=0;
        int targetRelativeOffset=0;
        int outputOffset=0;



        BPS_Node[] sourceTree = new BPS_Node[65536];
        BPS_Node[] targetTree = new BPS_Node[65536];
        for(int n=0; n<65536; n++){
            sourceTree[n] = null;
            targetTree[n] = null;
        }

        //source tree creation
        for(int offset=0; offset < sourceSize; offset++) {
            int symbol = sourceData[offset + 0];
            //sourceChecksum = crc32_adjust(sourceChecksum, symbol);
            if(offset < sourceSize - 1)
                symbol |= sourceData[offset + 1] << 8;
            BPS_Node node=new BPS_Node();
            node.offset=offset;
            node.next=sourceTree[symbol];
            sourceTree[symbol] = node;
        }

        int targetReadLength = 0;

















        while(outputOffset<modified.fileSize){
            int maxLength = 0, maxOffset = 0, mode = BPS_ACTION_TARGET_READ;

            int symbol = targetData[outputOffset + 0];
            if(outputOffset < targetSize - 1) symbol |= targetData[outputOffset + 1] << 8;

            { //source read
                int length = 0, offset = outputOffset;
                while(offset < sourceSize && offset < targetSize && sourceData[offset] == targetData[offset]) {
                    length++;
                    offset++;
                }
                if(length > maxLength) { maxLength = length; mode = BPS_ACTION_SOURCE_READ; }
            }

            { //source copy
                BPS_Node node = sourceTree[symbol];
                while(node != null) {
                    int length = 0, x = node.offset, y = outputOffset;
                    while(x < sourceSize && y < targetSize && sourceData[x++] == targetData[y++]) length++;
                    if(length > maxLength) { maxLength = length; maxOffset = node.offset; mode = BPS_ACTION_SOURCE_COPY; }
                    node = node.next;
                }
            }

            { //target copy
                BPS_Node node = targetTree[symbol];
                while(node != null) {
                    int length = 0, x = node.offset, y = outputOffset;
                    while(y < targetSize && targetData[x++] == targetData[y++]) length++;
                    if(length > maxLength) { maxLength = length; maxOffset = node.offset; mode = BPS_ACTION_TARGET_COPY; }
                    node = node.next;
                }

                //target tree append
                node = new BPS_Node();
                node.offset = outputOffset;
                node.next = targetTree[symbol];
                targetTree[symbol] = node;
            }

            { //target read
                if(maxLength < 4) {
                    maxLength = Math.min(Granularity, targetSize - outputOffset);
                    mode = BPS_ACTION_TARGET_READ;
                }
            }

            if(mode != BPS_ACTION_TARGET_READ) { targetReadFlush(targetReadLength, outputOffset, targetData, patchActions); targetReadLength = 0; }

            switch(mode) {
                case BPS_ACTION_SOURCE_READ:
                    //encode(BPS_ACTION_SOURCE_READ | ((maxLength - 1) << 2));
                    patchActions.add(new BPSAction(BPS_ACTION_SOURCE_READ, maxLength, null, null));
                    break;
                case BPS_ACTION_TARGET_READ:
                    //delay write to group sequential TargetRead commands into one
                    targetReadLength += maxLength;
                    break;
                case BPS_ACTION_SOURCE_COPY:
                case BPS_ACTION_TARGET_COPY:
                    //encode(mode | ((maxLength - 1) << 2));
                    int relativeOffset;
                    if(mode == BPS_ACTION_SOURCE_COPY) {
                        relativeOffset = maxOffset - sourceRelativeOffset;
                        sourceRelativeOffset = maxOffset + maxLength;
                    } else {
                        relativeOffset = maxOffset - targetRelativeOffset;
                        targetRelativeOffset = maxOffset + maxLength;
                    }
                    //encode((relativeOffset < 0) | (abs(relativeOffset) << 1));
                    patchActions.add(new BPSAction(mode, maxLength, null, relativeOffset));
                    break;
            }

            outputOffset += maxLength;
        }

        targetReadFlush(targetReadLength, outputOffset, targetData, patchActions); targetReadLength = 0;


        return patchActions;
    }
    private static void targetReadFlush(int targetReadLength, int outputOffset, int[] targetData, List<BPSAction> patchActions) {
        if(targetReadLength > 0) {
            //encode(TargetRead | ((targetReadLength - 1) << 2));
            BPSAction action = new BPSAction(BPS_ACTION_TARGET_READ, targetReadLength, new ArrayList<>(), null);
            patchActions.add(action);
            int offset = outputOffset - targetReadLength;
            while(targetReadLength > 0) {
                //write(targetData[offset++]);
                action.bytes.add(targetData[offset++]);
                targetReadLength--;
            }
        }
    }
    public static class BPSAction {
        public Integer type;
        public Integer length;
        public List<Integer> bytes;
        public Integer relativeOffset;
        public BPSAction(int type, int length, List<Integer> bytes, Integer relativeOffset) {
            this.type = type;
            this.length = length;
            this.bytes = bytes;
            this.relativeOffset = relativeOffset;
        }
    }
}