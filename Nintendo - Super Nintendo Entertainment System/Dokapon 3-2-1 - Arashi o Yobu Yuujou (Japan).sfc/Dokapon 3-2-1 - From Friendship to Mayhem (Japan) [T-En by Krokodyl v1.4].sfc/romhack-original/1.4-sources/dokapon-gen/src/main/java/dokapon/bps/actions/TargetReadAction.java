package dokapon.bps.actions;

import dokapon.bps.utils.Utils;

import java.util.Arrays;

public class TargetReadAction extends Action{

    byte[] data;

    public TargetReadAction(int length) {
        super(ActionType.TARGET_READ);
        this.length = length;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "TargetReadAction{" +
                "length=" + length +
                ", data=" + Arrays.toString(data) +
                ", type=" + type +
                '}';
    }

    @Override
    public int applyAction(byte[] patchData, byte[] sourceData, int offset, byte[] outputData) {
        int index = offset;
        for (byte b:data) {
            outputData[index++] = b;
        }
        return index;
    }

    public void appendData(byte[] bytes) {
        byte[] tmp = new byte[data.length + bytes.length];
        System.arraycopy(data, 0, tmp, 0, data.length);
        System.arraycopy(bytes, 0, tmp, data.length, bytes.length);
        data = tmp;
        length = data.length;
    }

    @Override
    public byte[] getBytes() {
        long res = type.ordinal();
        res += ((length-1) << 2);
        byte[] command = Utils.decode(res);
        byte[] bytes = new byte[command.length + data.length];
        System.arraycopy(command, 0, bytes, 0, command.length);
        System.arraycopy(data, 0, bytes, command.length, data.length);
        return bytes;
    }
}
