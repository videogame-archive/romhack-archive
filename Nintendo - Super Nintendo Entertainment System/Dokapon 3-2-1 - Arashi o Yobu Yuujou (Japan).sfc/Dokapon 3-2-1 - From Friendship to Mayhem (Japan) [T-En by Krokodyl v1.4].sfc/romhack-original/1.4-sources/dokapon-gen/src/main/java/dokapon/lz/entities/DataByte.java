package dokapon.lz.entities;

import dokapon.services.Utils;

public class DataByte extends CompressedByte {

    byte value;

    public DataByte(byte b) {
        value = b;
    }

    public DataByte(int b) {
        value = (byte) b;
    }

    public byte getValue() {
        return value;
    }

    public String toString() {
        return "DATA : "+ Utils.toHexString(value);
    }

    @Override
    public byte[] getBytes() {
        byte[] bytes = new byte[1];
        bytes[0] = value;
        return bytes;
    }
}
