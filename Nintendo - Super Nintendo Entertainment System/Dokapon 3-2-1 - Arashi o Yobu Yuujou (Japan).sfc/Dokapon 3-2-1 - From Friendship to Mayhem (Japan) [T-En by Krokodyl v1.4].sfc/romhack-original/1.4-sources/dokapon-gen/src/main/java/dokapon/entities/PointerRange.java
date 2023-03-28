package dokapon.entities;

public class PointerRange {

    private int start;
    private int end;
    private int shift;

    public PointerRange(int start, int end, int shift) {
        this.start = start;
        this.end = end;
        this.shift = shift;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    public int getShift() {
        return shift;
    }

    public void setShift(int shift) {
        this.shift = shift;
    }
}
