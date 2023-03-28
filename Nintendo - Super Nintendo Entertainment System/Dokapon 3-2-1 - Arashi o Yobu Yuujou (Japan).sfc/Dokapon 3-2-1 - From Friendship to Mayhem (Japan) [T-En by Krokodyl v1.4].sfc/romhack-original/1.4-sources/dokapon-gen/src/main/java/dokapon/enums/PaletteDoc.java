package dokapon.enums;

import java.util.HashMap;
import java.util.Map;

import static dokapon.enums.FontColor.*;

public class PaletteDoc extends OldPalette {

    Map<String, FontColor> mapGameColors = new HashMap<String, FontColor>(){{
        put("000000", MAP_COLOR_01);
        put("ffd694", MAP_COLOR_02);
        put("d69452", MAP_COLOR_03);
        put("9c5a29", MAP_COLOR_04);
        put("422910", MAP_COLOR_05);
        put("212131", MAP_COLOR_06);
        put("29314a", MAP_COLOR_07);
        put("4a5263", MAP_COLOR_08);
        put("520810", MAP_COLOR_09);
        put("b52110", MAP_COLOR_10);
        put("e76b29", MAP_COLOR_11);
        put("394a5a", MAP_COLOR_12);
        put("292942", MAP_COLOR_13);
        put("9ca5c6", MAP_COLOR_14);
        put("211810", MAP_COLOR_15);
        put("ffffff", MAP_COLOR_16);
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
