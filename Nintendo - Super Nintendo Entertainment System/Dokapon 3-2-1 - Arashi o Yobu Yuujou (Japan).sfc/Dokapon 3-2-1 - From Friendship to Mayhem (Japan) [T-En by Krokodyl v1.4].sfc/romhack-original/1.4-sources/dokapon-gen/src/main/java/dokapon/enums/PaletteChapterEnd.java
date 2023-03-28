package dokapon.enums;

import java.util.HashMap;
import java.util.Map;

import static dokapon.enums.FontColor.*;

public class PaletteChapterEnd extends OldPalette {

    Map<String, FontColor> mapGameColors = new HashMap<String, FontColor>(){{
        put("944a00", MAP_COLOR_01);
        put("ef0000", MAP_COLOR_02);
        put("ef9c00", MAP_COLOR_03);
        put("efef00", MAP_COLOR_04);
        put("9cef00", MAP_COLOR_05);
        put("00ef00", MAP_COLOR_06);
        put("00ef9c", MAP_COLOR_07);
        put("00efef", MAP_COLOR_08);
        put("009cef", MAP_COLOR_09);
        put("0000ef", MAP_COLOR_10);
        put("9c00ef", MAP_COLOR_11);
        put("a55a10", MAP_COLOR_12);
        put("c6c6c6", MAP_COLOR_13);
        put("6b6b6b", MAP_COLOR_14);
        put("8c8c8c", MAP_COLOR_15);
        put("e7e7e7", MAP_COLOR_16);
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
