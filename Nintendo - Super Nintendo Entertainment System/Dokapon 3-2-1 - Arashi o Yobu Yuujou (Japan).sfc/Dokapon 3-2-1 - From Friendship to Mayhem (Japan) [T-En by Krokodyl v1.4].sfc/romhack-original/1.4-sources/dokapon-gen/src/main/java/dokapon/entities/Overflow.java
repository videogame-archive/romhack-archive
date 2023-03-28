package dokapon.entities;

public class Overflow {

    int limit;
    int dataStart;
    int dataShift;

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public int getDataStart() {
        return dataStart;
    }

    public void setDataStart(int dataStart) {
        this.dataStart = dataStart;
    }

    public int getDataShift() {
        return dataShift;
    }

    public void setDataShift(int dataShift) {
        this.dataShift = dataShift;
    }
}
