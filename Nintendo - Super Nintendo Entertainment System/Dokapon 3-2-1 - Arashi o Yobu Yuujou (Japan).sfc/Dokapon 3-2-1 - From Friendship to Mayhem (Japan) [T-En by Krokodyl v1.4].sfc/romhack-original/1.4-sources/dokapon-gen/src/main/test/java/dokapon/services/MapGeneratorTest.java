package dokapon.services;

import org.junit.jupiter.api.Test;
import services.palettes.ColorDepth;
import services.palettes.Palette;
import services.palettes.PaletteLoader;

import java.io.IOException;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class MapGeneratorTest {

    @Test
    public void testPalettes() throws IOException {
        Map<Integer, Palette> integerPaletteMap = PaletteLoader.loadPalettes("/maps/overworld/palettes.png", ColorDepth._4BPP);
        System.out.println();
    }
    
}