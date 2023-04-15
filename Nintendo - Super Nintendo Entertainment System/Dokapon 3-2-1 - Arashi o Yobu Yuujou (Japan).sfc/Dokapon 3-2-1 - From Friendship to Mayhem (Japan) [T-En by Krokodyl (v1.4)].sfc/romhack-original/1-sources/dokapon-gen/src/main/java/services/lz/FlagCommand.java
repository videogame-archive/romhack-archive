package services.lz;

import static services.Utils.h2;

public class FlagCommand extends Command {
    
    byte flags;

    FlagCommand(byte a) {
        flags = a;
    }

    /**
     * 
     * @param offset 0-7
     * @return
     */
    public int getBit(int offset) {
        byte tmp = flags;
        tmp = (byte) ((flags >> (offset)) & 0x01);
        return tmp;
    }

    @Override
    public byte[] getBytes() {
        byte[] bytes = new byte[1];
        bytes[0] = flags;
        return bytes;
    }

    @Override
    public String toString() {
        return "FlagCommand{" +
                "flags=" + flags + "(" + h2(flags & 0xFF) + ")" +
                '}';
    }
}
