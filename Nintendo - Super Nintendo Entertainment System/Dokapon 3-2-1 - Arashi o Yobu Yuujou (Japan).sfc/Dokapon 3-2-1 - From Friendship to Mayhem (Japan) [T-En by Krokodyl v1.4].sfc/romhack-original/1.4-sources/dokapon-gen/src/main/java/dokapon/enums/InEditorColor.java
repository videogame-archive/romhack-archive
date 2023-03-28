package dokapon.enums;

public enum InEditorColor {

    BLACK(1),
    DARK_BROWN(2),
    LIGHT_BROWN(3),
    SAND(4),
    RED(5),
    DARK_BLUE(6),
    YELLOW(7),
    SKY_BLUE(8),
    DEEP_BLUE(9),
    LIME_GREEN(10),
    LIGHT_BLUE(11),
    LIGHT_PINK(12),
    OTHER_BLACK(13),
    SALMON(14),
    DARK_PINK(15),
    SOFT_GREEN(16);

    private int color;

    InEditorColor(int color) {
        this.color = color;
    }

    public static int getColor(InGameColor sideZero, InGameColor sizeOne) {
        if (sideZero==InGameColor.TRANSPARENT) {
            if (sizeOne==InGameColor.TRANSPARENT) {
                return BLACK.color;
            } else if (sizeOne==InGameColor.GREY) {
                return RED.color;
            } else if (sizeOne==InGameColor.BLUE) {
                return DEEP_BLUE.color;
            }
        }
        else if (sideZero==InGameColor.GREY) {
            if (sizeOne==InGameColor.TRANSPARENT) {
                return DARK_BROWN.color;
            } else if (sizeOne==InGameColor.GREY) {
                return DARK_BLUE.color;
            } else if (sizeOne==InGameColor.BLUE) {
                return YELLOW.color;
            } else if (sizeOne==InGameColor.WHITE) {
                return SKY_BLUE.color;
            }
        }
        else if (sideZero==InGameColor.BLUE) {
            if (sizeOne==InGameColor.TRANSPARENT) {
                return LIGHT_BROWN.color;
            } else if (sizeOne==InGameColor.GREY) {
                return LIME_GREEN.color;
            } else if (sizeOne==InGameColor.BLUE) {
                return LIGHT_BLUE.color;
            } else if (sizeOne==InGameColor.WHITE) {
                return LIGHT_PINK.color;
            }
        }
        else {
            if (sizeOne==InGameColor.TRANSPARENT) {
                return SAND.color;
            } else if (sizeOne==InGameColor.GREY) {
                return SALMON.color;
            } else if (sizeOne==InGameColor.BLUE) {
                return DARK_PINK.color;
            } else if (sizeOne==InGameColor.WHITE) {
                return SOFT_GREEN.color;
            }
        }
        return BLACK.color;
    }
}
