package services.palettes;

import dokapon.enums.OldPalette;
import services.Utils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static services.palettes.FontColor.*;

public class Palette4bpp extends Palette {

    Map<String, FontColor> mapGameColors = new HashMap<String, FontColor>();
    Map<FontColor, String> mapHexaValues = new HashMap<FontColor, String>();

    public Palette4bpp(String file) {
        try {
            BufferedImage image = ImageIO.read(Objects.requireNonNull(getClass().getResource(file)));
            loadPaletteImage(image);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Palette4bpp(BufferedImage image) throws IOException {
        loadPaletteImage(image);
    }
    
    public void loadPaletteImage(BufferedImage image) throws IOException {
        int gap = image.getWidth()/16;
        //System.out.println("Loading Palette "+file);
        for (int index = 0;index<16;index++) {
            int color = image.getRGB(index * gap, 0);
            FontColor fc = null;
            switch (index) {
                case 0:fc = MAP_COLOR_01;break;
                case 1:{fc = MAP_COLOR_02;break;}
                case 2:{fc = MAP_COLOR_03;break;}
                case 3:{fc = MAP_COLOR_04;break;}
                case 4:{fc = MAP_COLOR_05;break;}
                case 5:{fc = MAP_COLOR_06;break;}
                case 6:{fc = MAP_COLOR_07;break;}
                case 7:{fc = MAP_COLOR_08;break;}
                case 8:{fc = MAP_COLOR_09;break;}
                case 9:{fc = MAP_COLOR_10;break;}
                case 10:{fc = MAP_COLOR_11;break;}
                case 11:{fc = MAP_COLOR_12;break;}
                case 12:{fc = MAP_COLOR_13;break;}
                case 13:{fc = MAP_COLOR_14;break;}
                case 14:{fc = MAP_COLOR_15;break;}
                case 15:{fc = MAP_COLOR_16;break;}
            }
            String colorAsHex = Utils.getColorAsHex(color);
            //System.out.println(colorAsHex +"\t"+fc.name());
            if (!mapGameColors.containsKey(colorAsHex)) {
                mapGameColors.put(colorAsHex.toLowerCase(), fc);
                mapHexaValues.put(fc, colorAsHex.toLowerCase());
            } else
                mapHexaValues.put(fc, colorAsHex.toLowerCase());
        }
    }

    @Override
    public FontColor getFontColor(String hexa) {
        return mapGameColors.get(hexa);
    }

    @Override
    public String getHexaValue(FontColor fontColor) {
        return mapHexaValues.get(fontColor);
    }
}
