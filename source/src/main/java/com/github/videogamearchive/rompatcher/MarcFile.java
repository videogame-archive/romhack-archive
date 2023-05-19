/* Apache 2 License, Copyright (c) 2023 Juan Fuentes, based on Rom Patcher JS by Marc Robledo */
package com.github.videogamearchive.rompatcher;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
public class MarcFile {
    public boolean littleEndian=false;
    public int offset=0;
    public Object _lastRead = null;
    public int[] _u8array;
    public int fileSize;
    public MarcFile(Path source) throws IOException {
        this(Files.readAllBytes(source));
    }
    public MarcFile(byte[] bytes) throws IOException {
        this._u8array = new int[bytes.length];
        for (int i = 0; i < bytes.length; i++) {
            this._u8array[i] = Byte.toUnsignedInt(bytes[i]);
        }
        this.fileSize = bytes.length;
    }

    public MarcFile(int size) {
        this._u8array=new int[size];
        this.fileSize=size;
    }


























































    public void seek(int offset){
        this.offset=offset;
    }
    public void skip(int nBytes){
        this.offset+=nBytes;
    }
    public boolean isEOF(){
        return !(this.offset<this.fileSize);
    }

    public MarcFile slice(int offset, Integer len){
        if(len==null||len==0)
            len=this.fileSize-offset;

        MarcFile newFile;

//        if(typeof this._u8array.buffer.slice!=='undefined'){
//            newFile=new MarcFile(0);
//            newFile.fileSize=len;
//            newFile._u8array=new Uint8Array(this._u8array.buffer.slice(offset, offset+len));
//        }else{
            newFile=new MarcFile(len);
            this.copyToFile(newFile, offset, len, 0);
//        }
//        newFile.fileName=this.fileName;
//        newFile.fileType=this.fileType;
        newFile.littleEndian=this.littleEndian;
        return newFile;
    }


    public void copyToFile(MarcFile target, int offsetSource){
        copyToFile(target, offsetSource, null, null);
    }

    public void copyToFile(MarcFile target, int offsetSource, Integer len){
        copyToFile(target, offsetSource, len, null);
    }

    public void copyToFile(MarcFile target, int offsetSource, Integer len, Integer offsetTarget){
        if(offsetTarget==null)
            offsetTarget=offsetSource;
        if(len==null||len==0)
            len=(this.fileSize-offsetSource);

        for(var i=0; i<len; i++){
            target._u8array[offsetTarget+i]=this._u8array[offsetSource+i];
        }
    }

    public byte[] save() {
        byte[] bytes = new byte[this._u8array.length];
        for (int i = 0; i < this._u8array.length; i++) {
            bytes[i] = (byte) this._u8array[i];
        }
        return bytes;
    }
    public void save(Path path) throws IOException {
        Files.write(path, save());
    }













    public int readU8() {
        this._lastRead=this._u8array[this.offset];

        this.offset++;
        return (int) this._lastRead;
    }
    public int readU16() {
        if(this.littleEndian)
            this._lastRead=this._u8array[this.offset] + (this._u8array[this.offset+1] << 8);
        else
            this._lastRead=(this._u8array[this.offset] << 8) + this._u8array[this.offset+1];

        this.offset+=2;
        return (int) this._lastRead >>> 0;
    }
    public int readU24() {
        if(this.littleEndian)
            this._lastRead=this._u8array[this.offset] + (this._u8array[this.offset+1] << 8) + (this._u8array[this.offset+2] << 16);
        else
            this._lastRead=(this._u8array[this.offset] << 16) + (this._u8array[this.offset+1] << 8) + this._u8array[this.offset+2];

        this.offset+=3;
        return (int) this._lastRead >>> 0;
    }
    public int readU32() {
        if(this.littleEndian)
            this._lastRead=this._u8array[this.offset] + (this._u8array[this.offset+1] << 8) + (this._u8array[this.offset+2] << 16) + (this._u8array[this.offset+3] << 24);
        else
            this._lastRead=(this._u8array[this.offset] << 24) + (this._u8array[this.offset+1] << 16) + (this._u8array[this.offset+2] << 8) + this._u8array[this.offset+3];

        this.offset+=4;
        return (int) this._lastRead >>> 0;
    }



    public List<Integer> readBytes(int len){
        this._lastRead=new ArrayList<Integer>(len);
        for(var i=0; i<len; i++){
            ((ArrayList<Integer>) this._lastRead).add(this._u8array[this.offset+i]);
        }

        this.offset+=len;
        return (ArrayList<Integer>) this._lastRead;
    }

    public String readString(int len){
        this._lastRead="";
        for(var i=0;i<len && (this.offset+i)<this.fileSize && this._u8array[this.offset+i]>0;i++)
            this._lastRead=(String)this._lastRead+(char)(this._u8array[this.offset+i]);

        this.offset+=len;
        return (String) this._lastRead;
    }

    public void writeU8(int u8){
        this._u8array[this.offset]=u8;

        this.offset++;
    }
    public void writeU16(int u16){
        if(this.littleEndian){
            this._u8array[this.offset]=u16 & 0xff;
            this._u8array[this.offset+1]=u16 >> 8;
        }else{
            this._u8array[this.offset]=u16 >> 8;
            this._u8array[this.offset+1]=u16 & 0xff;
        }

        this.offset+=2;
    }
    public void writeU24(int u24){
        if(this.littleEndian){
            this._u8array[this.offset]=u24 & 0x0000ff;
            this._u8array[this.offset+1]=(u24 & 0x00ff00) >> 8;
            this._u8array[this.offset+2]=(u24 & 0xff0000) >> 16;
        }else{
            this._u8array[this.offset]=(u24 & 0xff0000) >> 16;
            this._u8array[this.offset+1]=(u24 & 0x00ff00) >> 8;
            this._u8array[this.offset+2]=u24 & 0x0000ff;
        }

        this.offset+=3;
    }
    public void writeU32(long u32){
        if(this.littleEndian){
            this._u8array[this.offset]= (int) (u32 & 0x000000ff);
            this._u8array[this.offset+1]=(int) ((u32 & 0x0000ff00) >> 8);
            this._u8array[this.offset+2]=(int) ((u32 & 0x00ff0000) >> 16);
            this._u8array[this.offset+3]=(int) ((u32 & 0xff000000) >> 24);
        }else{
            this._u8array[this.offset]=(int) ((u32 & 0xff000000) >> 24);
            this._u8array[this.offset+1]=(int) ((u32 & 0x00ff0000) >> 16);
            this._u8array[this.offset+2]=(int) ((u32 & 0x0000ff00) >> 8);
            this._u8array[this.offset+3]=(int) (u32 & 0x000000ff);
        }

        this.offset+=4;
    }


    public void writeBytes(List<Integer> a){
        for(var i=0;i<a.size();i++)
            this._u8array[this.offset+i]=a.get(i);

        this.offset+=a.size();
    }

    public void writeString(String str){
        for(var i=0;i<str.length();i++)
            this._u8array[this.offset+i]=str.charAt(i);

        this.offset+=str.length();
    }

}
