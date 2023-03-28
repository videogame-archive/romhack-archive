package dokapon.enums;

import java.util.HashMap;
import java.util.Map;

import static dokapon.enums.FontColor.*;

public class PalettePenny extends OldPalette {

    Map<String, FontColor> mapGameColors = new HashMap<String, FontColor>(){{
        put("635a5a", MAP_COLOR_01);
        put("6b737b", MAP_COLOR_02);
        put("4a525a", MAP_COLOR_03);
        put("6b3100", MAP_COLOR_04);
        put("945a10", MAP_COLOR_05);
        put("bd8418", MAP_COLOR_06);
        put("e7ad5a", MAP_COLOR_07);
        put("efd694", MAP_COLOR_08);
        put("dee742", MAP_COLOR_09);
        put("c64231", MAP_COLOR_10);
        put("f76352", MAP_COLOR_11);
        put("ff9c73", MAP_COLOR_12);
        put("9c8c29", MAP_COLOR_13);
        put("eff7ad", MAP_COLOR_14);
        put("393131", MAP_COLOR_15);
        put("efdeef", MAP_COLOR_16);
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
