package dokapon.enums;

import java.util.Objects;

import static dokapon.services.Utils.toHexString;

public enum Sprite {

    A("A", "01 14","11 14"),
    B("B", "02 14","12 14"),
    C("C", "03 14","13 14"),
    D("D", "04 14","14 14"),
    E("E", "05 14","15 14"),
    F("F", "06 14","16 14"),
    G("G", "07 14","17 14"),
    H("H", "08 14","18 14"),
    I("I", "09 14","19 14"),
    J("J", "0A 14","1A 14"),
    K("K", "0B 14","1B 14"),
    L("L", "0C 14","1C 14"),
    M("M", "0D 14","1D 14"),
    N("N", "0E 14","1E 14"),
    O("O", "0F 14","1F 14"),
    P("P", "20 14","30 14"),
    Q("Q", "21 14","31 14"),
    R("R", "22 14","32 14"),
    S("S", "23 14","33 14"),
    T("T", "24 14","34 14"),
    U("U", "25 14","35 14"),
    V("V", "26 14","36 14"),
    W("W", "27 14","37 14"),
    X("X", "28 14","38 14"),
    Y("Y", "29 14","39 14"),
    Z("Z", "2A 14","3A 14"),
    N_POLISH("Ń", "2E 14","3E 14"),
    GRASS("-", "53 14","63 14"),
    DESERT("d", "59 0C","69 0C"),
    TREE("t", "2E 15","90 15"),
    SIGN_TREE("y", "2C 54","3C 54"),
    EMPTY("_", "44 14","4F 14"),
    LEFT("[", "4C 14","4D 14"),
    RIGHT("]", "4C 54","4D 54"),
    LEFT_S("(", "7E 56","7D 56"),
    RIGHT_S(")", "7E 16","7D 16"),
    LEFT_DESERT("{", "2B 14","3B 14"),
    RIGHT_DESERT("}", "2B 54","3B 54"),

    LEFT_SIGN_MUD("k", "2D 54","3D 54"),
    RIGHT_SIGN_MUD("l", "2D 14","3D 14"),
    LEFT_MUD("j", "21 16","29 16"),
    RIGHT_MUD("m", "22 16","2A 16"),

    NIAM_LEFT_01("b", "38 0E","68 0C"),
    NIAM_LEFT_02("n", "4C 14","2B 94"),
    NIAM_RIGHT_01(",", "4C 54","2B 54"),
    NIAM_RIGHT_02(";", "31 0E","41 0E"),

    SNOW_AFTER_01("w", "5F 0A","56 0A"),
    SNOW_AFTER_02("x", "55 08","65 08"),
    SNOW_UNDER_01("c", "52 14","5F 0A"),
    SNOW_UNDER_02("v", "56 0A","65 08");

    String letter;
    String valueTop;
    String valueBot;

    Sprite(String letter, String valueTop, String valueBot) {
        this.letter = letter;
        this.valueTop = valueTop;
        this.valueBot = valueBot;
    }

    public static Sprite getSprite(String letter) {
        for (Sprite s:Sprite.values()) {
            if (Objects.equals(s.letter, letter)) return s;
        }
        return null;
    }

    public String getLetter() {
        return letter;
    }

    public void setLetter(String letter) {
        this.letter = letter;
    }

    public String getValueTop() {
        return valueTop;
    }

    public void setValueTop(String valueTop) {
        this.valueTop = valueTop;
    }

    public String getValueBot() {
        return valueBot;
    }

    public void setValueBot(String valueBot) {
        this.valueBot = valueBot;
    }

    public static void main(String[] args) {
        int i = 2;
        for (char c='B';c<='Z';c++) {
            if (i==16) i=32;
            System.out.println(
                    String.format(
                    "%s(\"%s\", \"%s 14\",\"%s 14\"),",
                    c+"",
                    c+"",
                            toHexString(i),toHexString((i++)+16)
                    )
            );
        }
    }
}
