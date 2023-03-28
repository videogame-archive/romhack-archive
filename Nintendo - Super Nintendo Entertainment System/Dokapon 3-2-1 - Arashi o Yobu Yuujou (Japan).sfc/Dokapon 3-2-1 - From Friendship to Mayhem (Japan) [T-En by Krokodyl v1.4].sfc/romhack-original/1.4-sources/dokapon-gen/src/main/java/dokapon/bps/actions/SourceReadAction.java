package dokapon.bps.actions;

public class SourceReadAction extends Action{

    public SourceReadAction(int length) {
        super(ActionType.SOURCE_READ);
        this.length = length;
    }

    @Override
    public int applyAction(byte[] patchData, byte[] sourceData, int offset, byte[] outputData) {
        int index = offset;
        for (index=offset;index<offset+length;index++) {
            outputData[index] = sourceData[index];
        }
        return index;
    }

    @Override
    public String toString() {
        return "SourceReadAction{" +
                "length=" + length +
                ", type=" + type +
                '}';
    }

}
