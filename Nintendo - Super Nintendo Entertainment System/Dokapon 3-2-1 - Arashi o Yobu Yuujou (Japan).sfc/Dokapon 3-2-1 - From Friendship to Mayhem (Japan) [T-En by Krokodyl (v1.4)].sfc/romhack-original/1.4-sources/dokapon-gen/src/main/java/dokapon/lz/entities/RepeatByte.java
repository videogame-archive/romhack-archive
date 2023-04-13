package dokapon.lz.entities;

import dokapon.services.Utils;

public class RepeatByte extends CompressedByte {

    Header header;

    byte leftByte;
    byte rightByte;

    /**
     * size
     * ----
     * 01101101 10101100
     *     ---- --------
     *     position
     */

    public RepeatByte(Header header, byte leftByte, byte rightByte)
    {
        this.header = header;
        this.leftByte = leftByte;
        this.rightByte = rightByte;
    }

    public RepeatByte(Header header, int size, int position) {
        this.header = header;
        this.rightByte = (byte)(position);
        byte rightPart = (byte)(position >> 8);
        byte leftPart = (byte)((size - 2) << header.getSizeShift());
        this.leftByte = (byte) (leftPart | rightPart);
    }

    /*public RepeatByte(int size, int position) {
        this.rightByte = (byte)(position);
        byte rightPart = (byte)(position >> 8);
        byte leftPart = (byte)(size << 3);
        this.leftByte = (byte) (leftPart | rightPart);
    }*/

    public int getPosition() {
        int position = (leftByte & 0xFF) << 8;
        position = (position & header.getPositionMask()) | (rightByte & 0xFF);
        return position;
    }

    public int getSize() {
        int size = leftByte & 0xFF;
        size = size >> header.getSizeShift();
        return size + 2;
    }

    /*public int getLeftByte() {
        int i = leftByte & 0xFF;
        return i >> 3;
    }

    public int getRightByte() {
        return getExactPosition();
    }

    public int getExactPosition() {
        int exactPosition = (leftByte & 0xFF) << 8;
        //System.out.println(Integer.toBinaryString(exactPosition & 0xFFF)+" "+Integer.toBinaryString(position & 0xFF));
        exactPosition = (exactPosition & 0x7FF) | (rightByte & 0xFF);
        //System.out.println(Integer.toBinaryString(exactPosition));
        return exactPosition;
    }*/

    public String toString() {
        return "REPEAT : position "+ getPosition()+"("+ Utils.toBinaryString(rightByte)+") size "+ getSize()+" ("+Utils.toBinaryString(leftByte)+")";
    }

    @Override
    public byte[] getBytes() {
        byte[] bytes = new byte[2];
        bytes[0] = leftByte;
        bytes[1] = rightByte;
        return bytes;
    }
}
