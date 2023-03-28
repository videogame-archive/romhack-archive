package services.palettes;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class PaletteLoader {
    
    public static Map<Integer, Palette> loadPalettes(String file, ColorDepth depth) throws IOException {
        Map<Integer, Palette> paletteMap = new HashMap<>();
        BufferedImage image = ImageIO.read(Objects.requireNonNull(PaletteLoader.class.getResource(file)));
        int paletteHeight = 12;
        int id = 0;
        int y = 0;
        while (y < image.getHeight()) {
            BufferedImage subimage = image.getSubimage(0, y, image.getWidth(), paletteHeight);
            if (depth==ColorDepth._4BPP) {
                Palette4bpp palette4bpp = new Palette4bpp(subimage);
                paletteMap.put(id++, palette4bpp);
            }
            y += paletteHeight;
        }
        return paletteMap;
    }
    
}
