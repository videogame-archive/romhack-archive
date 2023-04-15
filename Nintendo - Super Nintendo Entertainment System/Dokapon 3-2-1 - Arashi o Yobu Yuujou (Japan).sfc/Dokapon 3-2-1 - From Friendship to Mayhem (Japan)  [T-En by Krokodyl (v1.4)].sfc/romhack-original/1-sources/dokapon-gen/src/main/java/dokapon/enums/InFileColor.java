package dokapon.enums;

public enum InFileColor {

    WHITE(-1),
    BLACK(-16777216),
    RED(-65536);

    private int color;

    InFileColor(int color) {
        this.color = color;
    }

    public static InFileColor getInFileColor(int rgb) {
        for (InFileColor c:InFileColor.values()) {
            if (c.color==rgb) return c;
        }
        return null;
    }

    public InGameColor getInGameColor() {
        if (this==WHITE) return InGameColor.BLUE;
        else if (this==RED) return InGameColor.GREY;
        else return InGameColor.WHITE;
    }

}
