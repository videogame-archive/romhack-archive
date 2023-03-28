package dokapon.enums;

import java.util.HashMap;
import java.util.Map;

import static dokapon.enums.FontColor.*;

public class PaletteBattleCards extends OldPalette {

    Map<String, FontColor> mapGameColors = new HashMap<String, FontColor>(){{
        put("00b573", MAP_COLOR_01);
        put("4a0000", MAP_COLOR_02);
        put("842900", MAP_COLOR_03);
        put("bd5200", MAP_COLOR_04);
        put("000039", MAP_COLOR_05);
        put("007b00", MAP_COLOR_06);
        put("009c00", MAP_COLOR_07);
        put("6bc66b", MAP_COLOR_08);
        put("636300", MAP_COLOR_09);
        put("d6b500", MAP_COLOR_10);
        put("ffef00", MAP_COLOR_11);
        put("292929", MAP_COLOR_12);
        put("7b7b7b", MAP_COLOR_13);
        put("adadad", MAP_COLOR_14);
        put("000000", MAP_COLOR_15);
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
