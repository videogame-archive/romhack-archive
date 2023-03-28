package dokapon.enums;

import java.util.HashMap;
import java.util.Map;

import static dokapon.enums.FontColor.*;

public class PaletteBill extends OldPalette {

    Map<String, FontColor> mapGameColors = new HashMap<String, FontColor>(){{
        put("529c8c", MAP_COLOR_01);
        put("a56310", MAP_COLOR_02);
        put("bd7b39", MAP_COLOR_03);
        put("e79c52", MAP_COLOR_04);
        put("efc663", MAP_COLOR_05);
        put("f7de8c", MAP_COLOR_06);
        put("f7efbd", MAP_COLOR_07);
        put("8c4a00", MAP_COLOR_08);
        put("d67300", MAP_COLOR_09);
        put("efad5a", MAP_COLOR_10);
        put("f7e7ad", MAP_COLOR_11);
        put("ce8c4a", MAP_COLOR_12);
        put("f7ad63", MAP_COLOR_13);
        put("f7d673", MAP_COLOR_14);
        put("f7ef9c", MAP_COLOR_15);
        put("f7f7ce", MAP_COLOR_16);
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
