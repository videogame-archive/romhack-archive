package services.lz;

import services.Utils;

public class RepeatCommand extends Command {
    
    int shift;
    int length;
    
    REPEAT_ALGORITHM algorithm;

    public RepeatCommand(int shift, int length, REPEAT_ALGORITHM algorithm) {
        this.shift = shift;
        this.length = length;
        this.algorithm = algorithm;
    }

    public byte[] getBytes() {
        byte[] bytes = new byte[2];
        int a = (length-2 << algorithm.getShift() | ((shift & 0xFF00) >> 8));
        /*byte a = (byte) ((shift >>> 8) & algorithm.getMask());
        a = (byte) (a | ((length-3 & 0xFF) * algorithm.getMultiplier()));
        byte b = (byte) (shift & 0xFF);*/
        byte b = (byte) (shift & 0xFF);
        bytes[0] = (byte) (a & 0xFF);
        bytes[1] = b;
        return bytes;
    }

    public int getShift() {
        return shift;
    }

    public void setShift(int shift) {
        this.shift = shift;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    @Override
    public String toString() {
        return "RepeatCommand{" +
                "shift=" + shift +
                ", length=" + length +
                ", bytes=" + Utils.bytesToHex(getBytes()) +
                '}';
    }
}
