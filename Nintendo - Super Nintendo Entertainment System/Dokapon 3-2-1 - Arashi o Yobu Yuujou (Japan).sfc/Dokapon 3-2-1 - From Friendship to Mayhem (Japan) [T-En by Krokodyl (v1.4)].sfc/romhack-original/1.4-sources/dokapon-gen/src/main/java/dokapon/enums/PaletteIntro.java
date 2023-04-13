package dokapon.enums;

import static dokapon.enums.FontColor.*;

public class PaletteIntro extends OldPalette {

    public FontColor getFontColor(int i) {
        if (i==-16777216) return BLACK;
        else if (i==-1) return WHITE;
        else if (i==-5592406) return LIGHT_GREY;
        else return DARK_GREY;
    }

    @Override
    public FontColor getFontColor(String hexa) {
        return null;
    }

}
