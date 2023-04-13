package dokapon.bps.actions;

import dokapon.bps.utils.Utils;

public abstract class Action {

    int length;
    ActionType type;

    public Action(ActionType type) {
        this.type = type;
    }

    public ActionType getType() {
        return type;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public abstract int applyAction(byte[] patchData, byte[] sourceData, int offset, byte[] outputData);

    public byte[] getBytes() {
        long res = type.ordinal();
        res += ((length-1) << 2);
        byte[] bytes = Utils.decode(res);
        return bytes;
    }
}
