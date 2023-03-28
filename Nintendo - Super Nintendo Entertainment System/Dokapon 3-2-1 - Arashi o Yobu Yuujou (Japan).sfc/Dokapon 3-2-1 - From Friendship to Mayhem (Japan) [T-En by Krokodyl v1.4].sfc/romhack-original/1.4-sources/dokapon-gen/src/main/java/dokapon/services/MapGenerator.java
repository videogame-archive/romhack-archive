package dokapon.services;

import org.apache.commons.lang.ArrayUtils;
import services.Utils;
import services.palettes.ColorDepth;
import services.palettes.FontColor;
import services.palettes.Palette;
import services.palettes.PaletteLoader;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static services.Utils.h;
import static services.Utils.x;

/**
 * Generates a png of the map based on input data (tiles, sprites, sprite order) 
 * 
 */
public class MapGenerator {

    byte[] spriteMap;
    byte[] spriteDefinition;
    byte[] spriteDefinitionExtra;
    int spritePerLine = 0;
    
    BufferedImage tiles;

    Palette defaultPalette = null;
    Map<Integer, Palette> palettes = new HashMap<>();
    
    boolean verbose = false;

    public void loadSpriteMap(String spriteMapFile, int spritePerLine) {
        try {
            spriteMap = Files.readAllBytes(
                    Paths.get(MapGenerator.class.getResource(spriteMapFile).toURI())
            );
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
        this.spritePerLine = spritePerLine;
    }

    public void loadSpriteDefinition(String spriteDefFile) {
        try {
            spriteDefinition = Files.readAllBytes(
                    Paths.get(MapGenerator.class.getResource(spriteDefFile).toURI())
            );
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public void loadSpriteDefinitionExtra(String spriteDefFile) {
        try {
            spriteDefinitionExtra = Files.readAllBytes(
                    Paths.get(MapGenerator.class.getResource(spriteDefFile).toURI())
            );
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public void loadTiles(String tilesFile) {
        try {
            tiles = ImageIO.read(Objects.requireNonNull(MapGenerator.class.getResource(tilesFile)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadPalettes(String palettesFile, ColorDepth cd, int defaultPaletteId) {
        try {
            palettes = PaletteLoader.loadPalettes(palettesFile, cd);
            defaultPalette = palettes.get(defaultPaletteId);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 2 bytes from the tilemap referencing a tile
     * 
     * In the tilemap: 01 00
     *
     * 1st byte: 	00000001		Tile 01		Page: 0 (x8000)
     *
     * 2nd byte:	00000000		Palette 0 Page 0 (x8000)
     * 				00000001		Palette 0 Page 1 (xA000)
     * 				00000010		Palette 0 Page 2 (xC000)
     * 				00000011		Palette 0 Page 3 (xE000)
     *
     * 				00000100		Palette 1
     * 				00001000		Palette 2
     * 				00001100		Palette 3
     * 				00010000		Palette 4
     * 				00010100		Palette 5
     * 				00011000		Palette 6
     * 				00011100		Palette 7
     *
     * 				10010100		Palette 5	V-Flip
     * 				01010100		Palette 5	H-Flip
     * 				11010100		Palette 5	V-Flip & H-Flip
     *
     */
    public BufferedImage getTile(byte[] bytes) {
        if (verbose) System.out.println("Get Tile "+ Utils.bytesToHex(bytes));
        byte a = bytes[0];
        byte b = bytes[1];
        int page = (b & 0xFF) & 0x03;
        int row = (a & 0xFF) / 16;
        int col = (a & 0xFF) % 16;

        boolean hFlip = ((b & 0x40) == 0x40);
        boolean vFlip = (b & 0x80) == 0x80;

        if (verbose) System.out.println(String.format("Get Tile SubImage\t%s\t%s\t%s\t%s\t%s", col, row, page, hFlip, vFlip));
        BufferedImage tile = copyImage(tiles.getSubimage(col * 8, row * 8 + page * 16 * 8, 8, 8));

        if (hFlip || vFlip) tile = Utils.flipImage(tile, hFlip, vFlip);
        /*if (vFlip==-1 || hFlip==-1) {

            AffineTransform tx = AffineTransform.getScaleInstance(hFlip, vFlip);
            tx.translate(0, -tile.getHeight(null));
            AffineTransformOp op = new AffineTransformOp(tx,
                    AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
            tile = op.filter(tile, null);
        }*/
        /*String s = Utils.bytesToHex(bytes);
        try {
            ImageIO.write(tile, "png", new File("src/main/resources/maps/"+s+".png"));
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        
        int paletteId = ((b & 0xFF) >> 2) & 0x0F;
        Palette palette = palettes.get(paletteId);
        if (palette != defaultPalette) switchPalette(tile, palette);

        return tile;
    }

    /**
     * 8 bytes referencing 4 tiles (2/tile, in order: top left, top right, bottom left, bottom right)
     */
    public BufferedImage getSprite(byte[] bytes) {
        BufferedImage spriteImage = new BufferedImage(16, 16, BufferedImage.TYPE_INT_RGB);
        if (verbose) System.out.println("tile TL");
        writeImage(
                getTile(ArrayUtils.subarray(bytes, 0, 2)),
                spriteImage,
                0,0
        );
        if (verbose) System.out.println("tile TR");
        writeImage(
                getTile(ArrayUtils.subarray(bytes, 2, 4)),
                spriteImage,
                8,0
        );
        if (verbose) System.out.println("tile BL");
        writeImage(
                getTile(ArrayUtils.subarray(bytes, 4, 6)),
                spriteImage,
                0,8
        );
        if (verbose) System.out.println("tile BR");
        writeImage(
                getTile(ArrayUtils.subarray(bytes, 6, 8)),
                spriteImage,
                8,8
        );
        return spriteImage;
    }

    /**
     * prints the contents of image a on image b with an opaque value of 1.
     */
    public void writeImage(BufferedImage a, BufferedImage b,
                                int x, int y) {
        Graphics2D g2d = b.createGraphics();
        g2d.setComposite(
                AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1));
        g2d.drawImage(a, x, y, null);
        g2d.dispose();
    }

    public static BufferedImage copyImage(BufferedImage source){
        BufferedImage b = new BufferedImage(source.getWidth(), source.getHeight(), source.getType());
        Graphics2D g = b.createGraphics();
        g.drawImage(source, 0, 0, null);
        g.dispose();
        return b;
    }

    /**
     * Applies a palette to the image if the palette is different from the default one.
     */
    public void switchPalette(BufferedImage image, Palette palette) {
        if (palette == defaultPalette) return;
        for (int y=0;y<image.getHeight();y++) {
            for (int x=0;x<image.getWidth();x++) {
                if (verbose) System.out.println(x+" "+y);
                int rgb = image.getRGB(x, y);
                String colorAsHex = Utils.getColorAsHex(rgb).toLowerCase();
                FontColor fontColor = defaultPalette.getFontColor(colorAsHex);
                String hexaValue = palette.getHexaValue(fontColor);
                Color color = Color.decode("#" + hexaValue);
                image.setRGB(x, y, color.getRGB());
            }
        }
    }
    
    public int convertSpriteMapBytesToSpriteDefOffset(byte[] bytes) {
        int value = (bytes[1] & 0xFF)*x("100")+(bytes[0] & 0xFF);
        return value*8;
    }
    
    public void generateMap(String output) {
        
        int spriteWidth = 16;
        int spriteHeight = 16;
        int lineCount = (spriteMap.length/2) / (spritePerLine);

        BufferedImage outputImage = new BufferedImage(spriteWidth*spritePerLine, lineCount*spriteHeight, BufferedImage.TYPE_INT_ARGB);

        int col = 0;
        int row = 0;
        int index = 0;
        while (index<spriteMap.length) {
            byte[] spriteBytes = ArrayUtils.subarray(spriteMap, index, index + 2);
            int offset = convertSpriteMapBytesToSpriteDefOffset(spriteBytes);
            byte[] spriteDefBytes = null;
            if (offset<spriteDefinition.length-8) {
                spriteDefBytes = ArrayUtils.subarray(spriteDefinition, offset, offset + 8);
                if (verbose) System.out.println("Sprite\t"+(index/2+1)+"\t"+Utils.bytesToHex(spriteDefBytes));
            } else {
                offset = offset - 0x1200;
                spriteDefBytes = ArrayUtils.subarray(spriteDefinitionExtra, offset, offset + 8);
            }
            BufferedImage sprite = getSprite(spriteDefBytes);
            writeImage(sprite, outputImage, col * spriteWidth, row * spriteHeight);
            
            col++;
            if (col>=spritePerLine) {
                row++;
                col=0;
            }
            
            index += 2;
        }
        
        //byte[] bytes = Utils.hexStringToByteArray("01 0d 02 0d 11 0d 12 0d");
        
        //BufferedImage tile = getTile(new byte[]{0x4C, (byte) 0x52});
        //BufferedImage sprite = getSprite(bytes);
        //writeImage(tile, outputImage, 0, 0);

        try {
            ImageIO.write(outputImage, "png", new File(output));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    //80 16 81 16 90 16 91 16
    public void testSprite() {
        byte[] bytes = Utils.hexStringToByteArray("7E 56 01 14 7D 56 11 14");

        //BufferedImage tile = getTile(new byte[]{0x4C, (byte) 0x52});
        BufferedImage sprite = getSprite(bytes);

        try {
            ImageIO.write(sprite, "png", new File("src/main/resources/maps/test.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args) {
        MapGenerator mapGenerator = new MapGenerator();
        mapGenerator.loadSpriteMap("/maps/overworld/map-order-uncompressed.data", 128);
        mapGenerator.loadSpriteDefinition("/maps/overworld/map-sprites-uncompressed.data");
        mapGenerator.loadSpriteDefinitionExtra("/maps/overworld/map-sprites-town-names-uncompressed.data");
        mapGenerator.loadPalettes("/maps/overworld/palettes.png", ColorDepth._4BPP, 5);
        mapGenerator.loadTiles("/maps/overworld/map-sprite.png");
        mapGenerator.generateMap("src/main/resources/maps/map.png");
        //mapGenerator.testSprite();
    }
}
