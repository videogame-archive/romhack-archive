package dokapon.lz.compression;

import dokapon.Dokapon;
import dokapon.lz.entities.*;
import dokapon.services.Utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CopyCompressor {

    Header header;
    byte[] data;
    String dataHex;
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

    boolean verbose = false;

    public CopyCompressor(Header header, byte[] data) {
        this.header = header;
        this.data = data;
    }

    public void compress() throws IOException {
        dataHex = Utils.bytesToHex(data).replaceAll(" ","");
        if (verbose) System.out.println("Compressing : "+dataHex);
        Dokapon.getTime();
        Node root = new Node(dataHex, null);
        if (verbose) System.out.println("dataSize="+dataHex.length());
        List<CompressedByte> compressedBytes = root.getCompressedByte(header);

        int offset = 0;
        for (CompressedByte compressedByte : compressedBytes) {
            if (compressedByte instanceof DataByte) offset++;
            else if (compressedByte instanceof RepeatByte) {
                RepeatByte rb = (RepeatByte) compressedByte;
                offset += rb.getSize();
            }
        }

        outputStream.write(header.getBytes());

        CompressedByte[] buffer = new CompressedByte[8];
        int k = 0;
        for (CompressedByte compressedByte : compressedBytes) {
            buffer[k++] = compressedByte;
            if (k==buffer.length) {
                String bits = "";
                for (CompressedByte cb:buffer) {
                    if (cb == null || cb instanceof DataByte) bits = "1" + bits;
                    else bits = "0" + bits;
                }
                FlagByte f = new FlagByte((byte) (Integer.parseInt(bits,2) & 0xFF));
                if (verbose) System.out.print(f.getBytesHex()+" ");
                outputStream.write(f.getBytes());
                for (CompressedByte cb:buffer) {
                    if (verbose) System.out.print(cb.getBytesHex()+" ");
                    outputStream.write(cb.getBytes());
                }
                k=0;
                buffer = new CompressedByte[8];
            }
        }
        if (k!=0) {
            String bits = "";
            for (CompressedByte cb:buffer) {
                if (cb == null || cb instanceof DataByte) bits = "1" + bits;
                else bits = "0" + bits;
            }
            FlagByte f = new FlagByte((byte) (Integer.parseInt(bits,2) & 0xFF));
            if (verbose) System.out.print(f.getBytesHex()+" ");
            outputStream.write(f.getBytes());
            for (CompressedByte cb:buffer)
                if (cb!=null) {
                    if (verbose) System.out.print(cb.getBytesHex()+" ");
                    outputStream.write(cb.getBytes());
                }
        }

    }

    public byte[] getCompressedBytes() {
        return outputStream.toByteArray();
    }
}
