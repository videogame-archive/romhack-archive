package services.lz;

import static services.Utils.x;

public class HeaderCommand extends Command {

    int decompressedLength;
    int repeatLengthBits;
    //int compressedLength;
    
    public HeaderCommand(byte a, byte b, byte c) {
        decompressedLength = (b & 0xFF)*x("100")+(a & 0xFF);
        repeatLengthBits = c;
    }
    
    public HeaderCommand(int decompressedLength, int repeatLengthBits) {
        this.decompressedLength = decompressedLength;
        this.repeatLengthBits = repeatLengthBits;
    }
    
    public REPEAT_ALGORITHM getRepeatAlgorithm() {
        switch (repeatLengthBits) {
            case 3: return REPEAT_ALGORITHM.REPEAT_ALGORITHM_SIZE_3BITS;
            case 4: return REPEAT_ALGORITHM.REPEAT_ALGORITHM_SIZE_4BITS;
            case 6: return REPEAT_ALGORITHM.REPEAT_ALGORITHM_SIZE_6BITS;
            case 7: return REPEAT_ALGORITHM.REPEAT_ALGORITHM_SIZE_7BITS;
            case 8: return REPEAT_ALGORITHM.REPEAT_ALGORITHM_SIZE_8BITS;
            default:
                return REPEAT_ALGORITHM.REPEAT_ALGORITHM_SIZE_5BITS;
        }
    }
    
    @Override
    public byte[] getBytes() {
        byte[] bytes = new byte[3];
        int a = decompressedLength % x("100");
        int b = decompressedLength / x("100");
        int c = repeatLengthBits;
        bytes[0] = (byte) (a & 0xFF);
        bytes[1] = (byte) (b & 0xFF);
        bytes[2] = (byte) (c & 0xFF);
        return bytes;
    }

    public int getRepeatLengthBits() {
        return repeatLengthBits;
    }

    public int getDecompressedLength() {
        return decompressedLength;
    }

    @Override
    public String toString() {
        return "HeaderCommand{" +
                "decompressedLength=" + decompressedLength +
                ", repeatLengthBits=" + repeatLengthBits +
                '}';
    }
}
