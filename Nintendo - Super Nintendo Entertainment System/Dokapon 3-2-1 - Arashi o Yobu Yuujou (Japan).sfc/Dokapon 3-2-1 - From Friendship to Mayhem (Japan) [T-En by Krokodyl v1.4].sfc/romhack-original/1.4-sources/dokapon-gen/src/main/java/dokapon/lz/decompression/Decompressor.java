package dokapon.lz.decompression;

import dokapon.lz.entities.DataByte;
import dokapon.lz.entities.FlagByte;
import dokapon.lz.entities.Header;
import dokapon.lz.entities.RepeatByte;
import dokapon.services.Utils;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

public class Decompressor {

    byte[] input;

    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

    Header header;

    int start;
    int end;

    static boolean verbose = false;

    public Decompressor(byte[] data, int start) {
        this.start = start;
        header = new Header(data[start-3], data[start-2], data[start-1]);
        this.input = data;
    }

    public Decompressor(byte[] data, int start, Header header) {
        this.start = start;
        this.header = header;
        this.input = data;
    }

    public byte[] getDecompressedBytes() {
        return outputStream.toByteArray();
    }

    public void decompress() {
        try {
            decompressData();
            printOutput();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void decompressData() {
        int index = start;
        int dataRemaining = header.getDecompressedLength();

        while (dataRemaining>0) {
            FlagByte flagByte = new FlagByte(input[index++]);
            if (verbose) System.out.println(Utils.toHexString(index-1,6)+" "+flagByte+"\t"+Utils.toHexString((header.getDecompressedLength()-dataRemaining),6));
            int cursor = 0;
            while (cursor<header.getFlagLength()) {
                if (flagByte.isNewData(cursor)) {
                    if (getDecompressedBytes().length < header.getDecompressedLength()) {
                        DataByte dataByte = new DataByte(input[index++]);
                        outputStream.write(dataByte.getValue());
                        dataRemaining--;
                        if (verbose) System.out.println(dataByte);
                    }
                } else {
                    if (getDecompressedBytes().length+1 < header.getDecompressedLength()) {
                        RepeatByte repeatByte = new RepeatByte(header, input[index++], input[index++]);
                        if (verbose) System.out.println(repeatByte);
                        int written = decompressRepeatByte(repeatByte);
                        dataRemaining -= written;
                    }
                }
                cursor++;
            }
        }
        end = index;
    }

    public int decompressRepeatByte(RepeatByte repeatByte) {
        byte value = 0;
        int size = repeatByte.getSize();
        if (size==33) {
            //System.out.println();
        }
        int position = repeatByte.getPosition();
        int windowIndex = getDecompressedBytes().length-1-position;
        byte[] written = new byte[size];
        int indexWritten = 0;

        while (size-->0) {
            if (windowIndex<0) {
                value = 0;
                windowIndex++;
            }
            else value = getDecompressedBytes()[windowIndex++];
            outputStream.write(value);
            written[indexWritten++] = value;
        }
        if (verbose) System.out.println("Written : "+Utils.bytesToHex(written));
        return written.length;
    }

    private void printOutput() throws FileNotFoundException, UnsupportedEncodingException {
        PrintWriter writer = new PrintWriter("D:/git/dokapon-english/sprites/output.txt", "UTF-8");

        byte[] bytes = outputStream.toByteArray();

        int offset = 0;
        for (int i=0;i<bytes.length;i++) {
            if (i%16==0) {
                if (verbose) System.out.println();
                writer.println();
                if (verbose) System.out.print(Utils.toHexString(offset,4)+"   ");
                offset+=16;
            }
            writer.print(Utils.toHexString(bytes[i] & 0xFF,2).toLowerCase());
            if (verbose) System.out.print(Utils.toHexString(bytes[i] & 0xFF,2)+" ");
        }
        writer.close();
    }



    public void test() {
        for (byte b:input) {
            FlagByte flagByte = new FlagByte(b);
            for (int i=0;i<8;i++)
                System.out.println(flagByte.isNewData(i));
        }
    }

    public int getEnd() {
        return end;
    }

    public Header getHeader() {
        return header;
    }
}
