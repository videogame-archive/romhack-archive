package dokapon.enums;

import java.util.HashMap;
import java.util.Map;

import static dokapon.enums.FontColor.*;

public class PaletteChapterStart extends OldPalette {

    Map<String, FontColor> mapGameColors = new HashMap<String, FontColor>(){{
        put("948ce7", MAP_COLOR_01);
        put("f70000", MAP_COLOR_02);
        put("f7a500", MAP_COLOR_03);
        put("f7f700", MAP_COLOR_04);
        put("a5f700", MAP_COLOR_05);
        put("00f700", MAP_COLOR_06);
        put("00f7a5", MAP_COLOR_07);
        put("00f7f7", MAP_COLOR_08);
        put("00a5f7", MAP_COLOR_09);
        put("0000f7", MAP_COLOR_10);
        put("0000f7", MAP_COLOR_11);
        put("849c84", MAP_COLOR_12);
        put("6b8cd6", MAP_COLOR_13);
        put("4a4a84", MAP_COLOR_14);
        put("4263b5", MAP_COLOR_15);
        put("adc6ff", MAP_COLOR_16);
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
