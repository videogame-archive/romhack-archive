package dokapon.enums;

import java.util.HashMap;
import java.util.Map;

import static dokapon.enums.FontColor.*;

public class PaletteRico extends OldPalette {

    Map<String, FontColor> mapGameColors = new HashMap<String, FontColor>(){{
        put("000000", MAP_COLOR_01);
        put("212129", MAP_COLOR_02);
        put("dea58c", MAP_COLOR_03);
        put("ad6b5a", MAP_COLOR_04);
        put("8c4239", MAP_COLOR_05);
        put("bd5229", MAP_COLOR_06);
        put("941829", MAP_COLOR_07);
        put("6b1829", MAP_COLOR_08);
        put("00a5d6", MAP_COLOR_09);
        put("00639c", MAP_COLOR_10);
        put("003963", MAP_COLOR_11);
        put("524a52", MAP_COLOR_12);
        put("847384", MAP_COLOR_13);
        put("b59cbd", MAP_COLOR_14);
        put("180810", MAP_COLOR_15);
        put("dedede", MAP_COLOR_16);
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
