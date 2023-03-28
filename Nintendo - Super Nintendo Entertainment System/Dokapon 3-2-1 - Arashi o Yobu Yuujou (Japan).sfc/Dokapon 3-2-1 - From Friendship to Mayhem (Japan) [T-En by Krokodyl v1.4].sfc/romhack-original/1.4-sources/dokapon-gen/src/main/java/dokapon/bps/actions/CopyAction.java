package dokapon.bps.actions;

import dokapon.bps.utils.Utils;

public class CopyAction extends Action{

    int shift;

    private static int sourceRelativeOffset = 0;
    private static int targetRelativeOffset = 0;

    public CopyAction(ActionType type, int length) {
        super(type);
        this.length = length;
    }

    public int getShift() {
        return shift;
    }

    public void setShift(int shift) {
        this.shift = shift;
    }

    public static int getSourceRelativeOffset() {
        return sourceRelativeOffset;
    }

    public static void setSourceRelativeOffset(int sourceRelativeOffset) {
        CopyAction.sourceRelativeOffset = sourceRelativeOffset;
    }

    public static int getTargetRelativeOffset() {
        return targetRelativeOffset;
    }

    public static void setTargetRelativeOffset(int targetRelativeOffset) {
        CopyAction.targetRelativeOffset = targetRelativeOffset;
    }

    @Override
    public String toString() {
        return "CopyAction{" +
                "length=" + length +
                ", shift=" + shift +
                ", type=" + type +
                '}';
    }

    @Override
    public int applyAction(byte[] patchData, byte[] sourceData, int offset, byte[] outputData) {
        int index = offset;
        if (type==ActionType.SOURCE_COPY) {
            sourceRelativeOffset += shift;
            for (index = offset;index<offset+length;index++) {
                outputData[index] = sourceData[sourceRelativeOffset++];
            }
        }
        if (type==ActionType.TARGET_COPY) {
            targetRelativeOffset += shift;
            for (index = offset;index<offset+length;index++) {
                outputData[index] = outputData[targetRelativeOffset++];
            }
        }
        return index;
    }


    @Override
    public byte[] getBytes() {
        long res = type.ordinal();
        res += ((length-1) << 2);
        byte[] command = Utils.decode(res);
        long offset = 0;
        if (shift<0) offset = 1;
        offset += (shift<<1);
        byte[] data = Utils.decode(offset);
        byte[] bytes = new byte[command.length + data.length];
        System.arraycopy(command, 0, bytes, 0, command.length);
        System.arraycopy(data, 0, bytes, command.length, data.length);
        return bytes;
    }
}
