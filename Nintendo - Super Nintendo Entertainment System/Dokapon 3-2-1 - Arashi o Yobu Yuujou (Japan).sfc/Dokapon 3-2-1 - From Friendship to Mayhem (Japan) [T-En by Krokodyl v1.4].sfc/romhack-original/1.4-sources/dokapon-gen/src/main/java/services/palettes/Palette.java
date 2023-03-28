package services.palettes;

public abstract class Palette {

    public abstract FontColor getFontColor(String hexa);
    public abstract String getHexaValue(FontColor fontColor);

}
