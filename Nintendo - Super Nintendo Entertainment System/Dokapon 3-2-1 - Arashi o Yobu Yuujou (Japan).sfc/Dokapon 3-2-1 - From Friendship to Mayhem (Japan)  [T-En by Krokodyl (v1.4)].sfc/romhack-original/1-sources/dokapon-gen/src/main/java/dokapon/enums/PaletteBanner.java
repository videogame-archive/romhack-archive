package dokapon.enums;

import java.util.HashMap;
import java.util.Map;

import static dokapon.enums.FontColor.*;

public class PaletteBanner extends OldPalette {

    Map<String, FontColor> mapGameColors = new HashMap<String, FontColor>(){{
        put("000000", MAP_COLOR_01);
        put("f7f7a5", MAP_COLOR_02);
        put("dec6bd", MAP_COLOR_03);
        put("633984", MAP_COLOR_04);
        put("4a214a", MAP_COLOR_05);
        put("7384e7", MAP_COLOR_06);
        put("ff6b42", MAP_COLOR_07);
        put("7b52b5", MAP_COLOR_08);
        put("525a63", MAP_COLOR_09);
        put("42424a", MAP_COLOR_10);
        put("392921", MAP_COLOR_11);
        put("7b8494", MAP_COLOR_12);
        put("cece7b", MAP_COLOR_13);
        put("94a5b5", MAP_COLOR_14);
        put("636b7b", MAP_COLOR_15);
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
