package dokapon.entities;

public class PointerData {

    private int value;
    private int offset;
    private String[] data;
    private int offsetData;
    private PointerData oldPointer;
    private String[] menuData;
    private int offsetOldMenuData;

    public void setValue(int value) {
        if (this.value>0) System.out.println(this.value+"  ->  "+value);
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getOffset() {
        return offset;
    }

    public void setData(String[] data) {
        this.data = data;
    }

    public String[] getData() {
        return data;
    }

    public void setOffsetData(int offsetData) {

        this.offsetData = offsetData;
    }

    public int getOffsetData() {
        return offsetData;
    }

    public void setOldPointer(PointerData oldPointer) {
        this.oldPointer = oldPointer;
    }

    public PointerData getOldPointer() {
        return oldPointer;
    }

    public void setMenuData(String[] menuData) {
        this.menuData = menuData;
    }

    public String[] getMenuData() {
        return menuData;
    }

    public void setOffsetOldMenuData(int offsetOldMenuData) {
        this.offsetOldMenuData = offsetOldMenuData;
    }

    public int getOffsetOldMenuData() {
        return offsetOldMenuData;
    }

    @Override
    public String toString() {
        String s = "";
        for (String a:data) s = s+a+" ";
        return "Pointeur{" + "offset=" + Integer.toHexString(offset) + ", valeur=" + Integer.toHexString(value) + ", offsetdata="+Integer.toHexString(offsetData)+", data=" + s + '}';
    }
}
