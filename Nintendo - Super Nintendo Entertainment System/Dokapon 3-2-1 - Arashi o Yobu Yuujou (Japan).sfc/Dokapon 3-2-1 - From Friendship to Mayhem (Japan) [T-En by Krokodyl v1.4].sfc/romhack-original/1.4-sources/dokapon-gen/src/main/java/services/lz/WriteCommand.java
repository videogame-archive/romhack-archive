package services.lz;

import com.google.common.primitives.Bytes;
import services.Utils;

public class WriteCommand extends Command {
    
    byte[] data;

    public WriteCommand(byte b) {
        this.data = new byte[1];
        data[0] = b;
    }

    public WriteCommand(byte[] b) {
        this.data = b;
    }
    
    public byte[] getBytes() {
        return data;
    }

    public byte[] getData() {
        return data;
    }

    public void appendData(byte[] data) {
        this.data = Bytes.concat(this.data, data);
    }

    @Override
    public String toString() {
        return "WriteCommand{" +
                "data=" + Utils.bytesToHex(data) +
                '}';
    }
}
