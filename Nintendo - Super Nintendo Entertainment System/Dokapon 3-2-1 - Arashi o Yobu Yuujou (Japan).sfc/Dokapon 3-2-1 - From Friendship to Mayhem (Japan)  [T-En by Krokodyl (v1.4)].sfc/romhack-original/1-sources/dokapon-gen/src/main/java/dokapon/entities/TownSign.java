package dokapon.entities;

public class TownSign {

    int offset;
    String town;
    String value;
    private String hex;

    public TownSign(int offset, String town, String value) {
        this.offset = offset;
        this.town = town;
        this.value = value;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public String getValue() {
        /*String name = "("+getTown().replaceAll(" ","_")+")";
        if (name.length()%2==1) name += "-";
        return name;*/
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setHex(String hex) {
        this.hex = hex;
    }

    public String getHex() {
        return hex;
    }
}
