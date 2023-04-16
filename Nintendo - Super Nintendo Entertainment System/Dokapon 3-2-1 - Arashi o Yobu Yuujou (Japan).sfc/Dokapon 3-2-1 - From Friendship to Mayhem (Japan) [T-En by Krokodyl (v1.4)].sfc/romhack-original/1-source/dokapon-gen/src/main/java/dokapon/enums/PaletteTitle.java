package dokapon.enums;

import java.util.HashMap;
import java.util.Map;

import static dokapon.enums.FontColor.*;

public class PaletteTitle extends OldPalette {

    Map<String, FontColor> mapGameColors = new HashMap<String, FontColor>(){{
        put("000000", MAP_COLOR_01);
        put("ff8c18", MAP_COLOR_02);
        put("945210", MAP_COLOR_03);
        put("c6b594", MAP_COLOR_04);
        put("cea55a", MAP_COLOR_05);
        put("f7f700", MAP_COLOR_06);
        put("8c4239", MAP_COLOR_07);
        put("94d639", MAP_COLOR_08);
        put("00a5e7", MAP_COLOR_09);
        put("d60000", MAP_COLOR_10);
        put("739442", MAP_COLOR_11);
        put("21739c", MAP_COLOR_12);
        put("5a6b73", MAP_COLOR_13);
        put("424a52", MAP_COLOR_14);
        put("181821", MAP_COLOR_15);
        put("eff7ff", MAP_COLOR_16);
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
