package dokapon.enums;

import java.util.HashMap;
import java.util.Map;

import static dokapon.enums.FontColor.*;

public class PaletteSalesman extends OldPalette {

    Map<String, FontColor> mapGameColors = new HashMap<String, FontColor>(){{
        put("000000", MAP_COLOR_01);
        put("e7c684", MAP_COLOR_02);
        put("ce8c39", MAP_COLOR_03);
        put("844a18", MAP_COLOR_04);
        put("522908", MAP_COLOR_05);
        put("00bd29", MAP_COLOR_06);
        put("39e794", MAP_COLOR_07);
        put("004a18", MAP_COLOR_08);
        put("008421", MAP_COLOR_09);
        put("b55221", MAP_COLOR_10);
        put("216bef", MAP_COLOR_11);
        put("0042c6", MAP_COLOR_12);
        put("428cf7", MAP_COLOR_13);
        put("102994", MAP_COLOR_14);
        put("211008", MAP_COLOR_15);
        put("f7efef", MAP_COLOR_16);
    }};

    public FontColor getFontColor(int i) {
        if (i==-16777216) return BLACK;
        else if (i==-1) return WHITE;
        else if (i==-16777126) return DARK_BLUE;
        else return DARK_GREY;
    }

    @Override
    public FontColor getFontColor(String hexa) {
        return mapGameColors.get(hexa);
    }

}
