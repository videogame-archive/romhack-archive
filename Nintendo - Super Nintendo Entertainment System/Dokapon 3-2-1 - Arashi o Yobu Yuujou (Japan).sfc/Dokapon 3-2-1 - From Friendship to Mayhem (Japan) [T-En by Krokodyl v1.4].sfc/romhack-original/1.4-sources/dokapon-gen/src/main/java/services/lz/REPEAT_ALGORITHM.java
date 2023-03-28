package services.lz;

import static services.Utils.b;
import static services.Utils.x;

public enum REPEAT_ALGORITHM {

    REPEAT_ALGORITHM_SIZE_8BITS(0, (byte) 0x00, b("10")),
    REPEAT_ALGORITHM_SIZE_7BITS(1, (byte) 0x01, b("10")),
    REPEAT_ALGORITHM_SIZE_6BITS(2, (byte) 0x03, b("10")),
    REPEAT_ALGORITHM_SIZE_5BITS(3, (byte) 0x07, b("10")),
    REPEAT_ALGORITHM_SIZE_4BITS(4, (byte) 0x0F, b("10")),
    REPEAT_ALGORITHM_SIZE_3BITS(5, (byte) 0x1F, b("10"));
    
    int shift;
    byte mask;
    byte multiplier;
    int windowSize;
    int maxBufferSize;

    REPEAT_ALGORITHM(int value, byte mask, byte multiplier) {
        this.shift = value;
        this.mask = mask;
        this.multiplier = multiplier;
        windowSize = mask*0x100 + 0xFF;
        maxBufferSize = 0xFF >>> shift;
    }

    public RepeatCommand buildRepeatCommand(byte a, byte b) {
        int length =  ((a & 0xFF) >> getShift()) + 2;
        //int length = ((b & 0xFF) >>> algorithm.getShift()) + 3;
        int shift = (((a & getMask())* x("100")) | ((b & 0xFF)));
        RepeatCommand repeatCommand = new RepeatCommand(shift, length, this);
        return repeatCommand;
    }

    public int getShift() {
        return shift;
    }

    public byte getMask() {
        return mask;
    }

    public byte getMultiplier() {
        return multiplier;
    }

    public int getWindowSize() {
        return windowSize;
    }

    public int getMaxBufferSize() {
        return maxBufferSize;
    }
}
