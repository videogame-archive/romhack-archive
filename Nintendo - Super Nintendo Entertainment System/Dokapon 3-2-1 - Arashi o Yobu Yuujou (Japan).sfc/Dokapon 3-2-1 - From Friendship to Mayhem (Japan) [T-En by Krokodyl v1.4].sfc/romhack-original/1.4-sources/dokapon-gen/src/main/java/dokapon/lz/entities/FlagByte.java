package dokapon.lz.entities;

import dokapon.services.Utils;

public class FlagByte extends CompressedByte {

    byte value;

    public FlagByte(byte value) {
        this.value = value;
    }

    /**
     * index between 0 and 7
     */
    public boolean isNewData(int index) {
        String binaryString = Utils.toBinaryString(value);
        return binaryString.charAt(8-index-1)=='1';
    }

    @Override
    public String toString() {
        return "FLAG : "+Utils.toHexString(value)+" "+Utils.toBinaryString(value);
    }

    @Override
    public byte[] getBytes() {
        byte[] bytes = new byte[1];
        bytes[0] = value;
        return bytes;
    }
}
