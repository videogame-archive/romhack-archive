package dokapon.enums;

import java.util.HashMap;
import java.util.Map;

import static dokapon.enums.FontColor.*;
import static dokapon.enums.FontColor.DARK_GREY;

public class PaletteGame extends OldPalette {

    Map<String, FontColor> mapGameColors = new HashMap<String, FontColor>(){{
        put("638c73", MAP_COLOR_01);
        put("ffe7b5", MAP_COLOR_02);
        put("dec694", MAP_COLOR_03);
        put("ce9c52", MAP_COLOR_04);
        put("b5ff94", MAP_COLOR_05);
        put("94ef5a", MAP_COLOR_06);
        put("7bbd42", MAP_COLOR_07);
        put("217b08", MAP_COLOR_08);
        put("105a10", MAP_COLOR_09);
        put("003910", MAP_COLOR_10);
        put("efff8c", MAP_COLOR_11);
        put("e7e76b", MAP_COLOR_12);
        put("ad7b42", MAP_COLOR_13);
        put("734a31", MAP_COLOR_14);
        put("212118", MAP_COLOR_15);
        put("fff7de", MAP_COLOR_16);
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
