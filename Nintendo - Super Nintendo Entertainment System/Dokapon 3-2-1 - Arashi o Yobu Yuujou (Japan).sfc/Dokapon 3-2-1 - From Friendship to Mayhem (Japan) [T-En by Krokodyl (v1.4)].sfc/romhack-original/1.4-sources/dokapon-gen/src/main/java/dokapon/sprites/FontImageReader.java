package dokapon.sprites;

import dokapon.enums.*;
import dokapon.lz.entities.Header;
import dokapon.services.Utils;
import services.FontImageReaderRevised;
import services.palettes.Palette;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import static services.Utils.h2;

public class FontImageReader {

    BufferedImage image;

    byte[] imageData;

    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

    boolean verbose = false;

    public FontImageReader() {

    }

    public static void main(String[] args) throws IOException {
        FontImageReader imageReader = new FontImageReader();
        //imageReader.introQuote();
        //imageReader.introQuoteOrder();
        //imageReader.introTexts();
        //imageReader.score();
        //imageReader.map();
        imageReader.trainingMap();
        //imageReader.mapOrder();
        //imageReader.titleScreen();
    }

    public void chapterStart() throws IOException {
        generateSpriteDataFromImage(
                "src/main/resources/images/sprites/chapter-start.png",
                "src/main/resources/data/sprite-uncompressed.data",
                new PaletteChapterStart(),
                4
        );
        String uncomp = "src/main/resources/data/sprite-uncompressed.data";
        String outputFile = "src/main/resources/data/output/1F3000.data";
        CompressedSpriteManager compressedSpriteManager = new CompressedSpriteManager(null);
        compressedSpriteManager.compressCopyFile(uncomp, Header.CHAPTER_START_SPRITES_HEADER, outputFile);
        //compressedSpriteManager.decompressFile(outputFile, "src/main/resources/data/decomp-1B8000.data");
    }

    public void chapterStartOrder() throws IOException {
        CompressedSpriteManager compressedSpriteManager = new CompressedSpriteManager(null);
        compressedSpriteManager.compressCopyFile(
                "src/main/resources/data/input/chapter-1-start-order-uncompressed.data",
                Header.CHAPTER_START_SPRITES_ORDER_HEADER,
                "src/main/resources/data/output/1E8020.data");
        compressedSpriteManager.compressCopyFile(
                "src/main/resources/data/input/chapter-2-start-order-uncompressed.data",
                Header.CHAPTER_START_SPRITES_ORDER_HEADER,
                "src/main/resources/data/output/1E9000.data");
        compressedSpriteManager.compressCopyFile(
                "src/main/resources/data/input/chapter-3-start-order-uncompressed.data",
                Header.CHAPTER_START_SPRITES_ORDER_HEADER,
                "src/main/resources/data/output/1EA000.data");
        compressedSpriteManager.compressCopyFile(
                "src/main/resources/data/input/chapter-4-start-order-uncompressed.data",
                Header.CHAPTER_START_SPRITES_ORDER_HEADER,
                "src/main/resources/data/output/1EB000.data");
        compressedSpriteManager.compressCopyFile(
                "src/main/resources/data/input/chapter-5-start-order-uncompressed.data",
                Header.CHAPTER_START_SPRITES_ORDER_HEADER,
                "src/main/resources/data/output/1EC000.data");
        compressedSpriteManager.compressCopyFile(
                "src/main/resources/data/input/chapter-6-start-order-uncompressed.data",
                Header.CHAPTER_START_SPRITES_ORDER_HEADER,
                "src/main/resources/data/output/1ED000.data");
        compressedSpriteManager.compressCopyFile(
                "src/main/resources/data/input/chapter-7-start-order-uncompressed.data",
                Header.CHAPTER_START_SPRITES_ORDER_HEADER,
                "src/main/resources/data/output/1EE000.data");
        compressedSpriteManager.compressCopyFile(
                "src/main/resources/data/input/chapter-8-start-order-uncompressed.data",
                Header.CHAPTER_START_SPRITES_ORDER_HEADER,
                "src/main/resources/data/output/1EF000.data");
        //compressedSpriteManager.decompressFile(outputFile, "src/main/resources/data/decomp-1B8000.data");
    }

    public void bill() throws IOException {
        generateSpriteDataFromImage(
                "src/main/resources/images/sprites/bill.png",
                "src/main/resources/data/sprite-uncompressed.data",
                new PaletteBill(),
                4
        );
        String uncomp = "src/main/resources/data/sprite-uncompressed.data";
        String outputFile = "src/main/resources/data/output/1DC000.data";
        CompressedSpriteManager compressedSpriteManager = new CompressedSpriteManager(null);
        compressedSpriteManager.compressCopyFile(uncomp, Header.BILL_SPRITES_HEADER, outputFile);
    }

    public void battleCards() throws IOException {
        generateSpriteDataFromImage(
                "src/main/resources/images/sprites/battle-cards.png",
                "src/main/resources/data/sprite-uncompressed.data",
                new PaletteBattleCards(),
                4
        );
        String uncomp = "src/main/resources/data/sprite-uncompressed.data";
        String outputFile = "src/main/resources/data/output/1C0000.data";
        CompressedSpriteManager compressedSpriteManager = new CompressedSpriteManager(null);
        compressedSpriteManager.compressCopyFile(uncomp, Header.BATTLE_CARDS_SPRITES_HEADER, outputFile);
    }

    public void rico() throws IOException {
        generateSpriteDataFromImage(
                "src/main/resources/images/sprites/rico.png",
                "src/main/resources/data/sprite-uncompressed.data",
                new PaletteRico(),
                4
        );
        String uncomp = "src/main/resources/data/sprite-uncompressed.data";
        String outputFile = "src/main/resources/data/output/1BC500.data";
        CompressedSpriteManager compressedSpriteManager = new CompressedSpriteManager(null);
        compressedSpriteManager.compressCopyFile(uncomp, Header.RICO_SPRITES_HEADER, outputFile);
    }

    public void chapterEnd() throws IOException {
        generateSpriteDataFromImage(
                "src/main/resources/images/sprites/chapter-end.png",
                "src/main/resources/data/sprite-uncompressed.data",
                new PaletteChapterEnd(),
                4
        );
        String uncomp = "src/main/resources/data/sprite-uncompressed.data";
        String outputFile = "src/main/resources/data/output/1FC000.data";
        CompressedSpriteManager compressedSpriteManager = new CompressedSpriteManager(null);
        compressedSpriteManager.compressCopyFile(uncomp, Header.CHAPTER_END_SPRITES_HEADER, outputFile);
        //compressedSpriteManager.decompressFile(outputFile, "src/main/resources/data/decomp-1B8000.data");
    }

    public void pennyOmg() throws IOException {
        generateSpriteDataFromImage(
                "src/main/resources/images/sprites/penny-omg.png",
                "src/main/resources/data/sprite-uncompressed.data",
                new PalettePenny(),
                4
        );
        String uncomp = "src/main/resources/data/sprite-uncompressed.data";
        String outputFile = "src/main/resources/data/output/1FA000.data";
        CompressedSpriteManager compressedSpriteManager = new CompressedSpriteManager(null);
        compressedSpriteManager.compressCopyFile(uncomp, Header.PENNY_OMG_SPRITES_HEADER, outputFile);
        //compressedSpriteManager.decompressFile(outputFile, "src/main/resources/data/decomp-1B8000.data");
    }

    public void roshambo() throws IOException {
        generateSpriteDataFromImage(
                "src/main/resources/images/sprites/roshambo.png",
                "src/main/resources/data/sprite-uncompressed.data",
                new PaletteRoshambo(),
                4
        );
        String uncomp = "src/main/resources/data/sprite-uncompressed.data";
        String outputFile = "src/main/resources/data/output/1DA000.data";
        CompressedSpriteManager compressedSpriteManager = new CompressedSpriteManager(null);
        compressedSpriteManager.compressCopyFile(uncomp, Header.ROSHAMBO_SPRITES_HEADER, outputFile);
        //compressedSpriteManager.decompressFile(outputFile, "src/main/resources/data/decomp-1B8000.data");
    }

    public void salesman() throws IOException {
        generateSpriteDataFromImage(
                "src/main/resources/images/sprites/salesman.png",
                "src/main/resources/data/sprite-uncompressed.data",
                new PaletteSalesman(),
                4
        );
        String uncomp = "src/main/resources/data/sprite-uncompressed.data";
        String outputFile = "src/main/resources/data/output/1CC000.data";
        CompressedSpriteManager compressedSpriteManager = new CompressedSpriteManager(null);
        compressedSpriteManager.compressCopyFile(uncomp, Header.SALESMAN_SPRITES_HEADER, outputFile);
        //compressedSpriteManager.decompressFile(outputFile, "src/main/resources/data/decomp-1B8000.data");
    }

    public void doc() throws IOException {
        generateSpriteDataFromImage(
                "src/main/resources/images/sprites/doc.png",
                "src/main/resources/data/sprite-uncompressed.data",
                new PaletteDoc(),
                4
        );
        String uncomp = "src/main/resources/data/sprite-uncompressed.data";
        String outputFile = "src/main/resources/data/output/1CA000.data";
        CompressedSpriteManager compressedSpriteManager = new CompressedSpriteManager(null);
        compressedSpriteManager.compressCopyFile(uncomp, Header.DOC_SPRITES_HEADER, outputFile);
        //compressedSpriteManager.decompressFile(outputFile, "src/main/resources/data/decomp-1B8000.data");
    }

    public void freeTownBanner() throws IOException {
        generateSpriteDataFromImage(
                "src/main/resources/images/sprites/banner.png",
                "src/main/resources/data/sprite-uncompressed.data",
                new PaletteBanner(),
                4
        );
        String uncomp = "src/main/resources/data/sprite-uncompressed.data";
        String outputFile = "src/main/resources/data/output/1C8000.data";
        CompressedSpriteManager compressedSpriteManager = new CompressedSpriteManager(null);
        compressedSpriteManager.compressCopyFile(uncomp, Header.FREETOWN_BANNER_SPRITES_HEADER, outputFile);
        //compressedSpriteManager.decompressFile(outputFile, "src/main/resources/data/decomp-1B8000.data");
    }

    public void titleScreen() throws IOException {
        generateSpriteDataFromImage(
                "src/main/resources/images/sprites/title-screen.png",
                "src/main/resources/data/sprite-uncompressed.data",
                new PaletteTitle(),
                4
        );
        String uncomp = "src/main/resources/data/sprite-uncompressed.data";
        String outputFile = "src/main/resources/data/output/1F0000.data";
        CompressedSpriteManager compressedSpriteManager = new CompressedSpriteManager(null);
        compressedSpriteManager.compressCopyFile(uncomp, Header.TITLE_SCREEN_SPRITES_HEADER, outputFile);
        //compressedSpriteManager.decompressFile(outputFile, "src/main/resources/data/decomp-1B8000.data");
    }

    public void titleScreenOrder() throws IOException {
        String uncomp = "src/main/resources/data/input/title-order-uncompressed.data";
        String outputFile = "src/main/resources/data/output/1F8000.data";
        CompressedSpriteManager compressedSpriteManager = new CompressedSpriteManager(null);
        compressedSpriteManager.compressCopyFile(uncomp, Header.TITLE_SCREEN_SPRITES_ORDER_HEADER, outputFile);
        //compressedSpriteManager.decompressFile(outputFile, "src/main/resources/data/decomp-1B8000.data");
    }

    public void introQuote() throws IOException {
        generateSpriteDataFromImage(
                "src/main/resources/images/sprites/intro-quote-sprite.png",
                "src/main/resources/data/sprite-uncompressed.data",
                new PaletteIntro(),
                2
        );
        String uncomp = "src/main/resources/data/sprite-uncompressed.data";
        String outputFile = "src/main/resources/data/output/1B8000.data";
        CompressedSpriteManager compressedSpriteManager = new CompressedSpriteManager(null);
        compressedSpriteManager.compressFile(uncomp, Header.INTRO_QUOTE_SPRITE_HEADER, outputFile);
        compressedSpriteManager.decompressFile(outputFile, "src/main/resources/data/decomp-1B8000.data");
    }

    public void introQuoteOrder() throws IOException {
        FontImageReader imageReader = new FontImageReader();
        String uncomp = "src/main/resources/data/input/intro-quote-uncompressed.data";
        String outputFile = "src/main/resources/data/output/1B8800.data";
        CompressedSpriteManager compressedSpriteManager = new CompressedSpriteManager(null);
        compressedSpriteManager.compressFile(uncomp, Header.INTRO_QUOTE_SPRITE_ORDER_HEADER, outputFile);
        compressedSpriteManager.decompressFile(outputFile, "src/main/resources/data/decomp-1B8800.data");
    }

    public void introTexts() throws IOException {
        generateSpriteDataFromImage(
                "src/main/resources/images/sprites/intro-texts-sprite.png",
                "src/main/resources/data/sprite-uncompressed.data",
                new PaletteIntro(),
                2
        );
        String uncomp = "src/main/resources/data/sprite-uncompressed.data";
        String outputFile = "src/main/resources/data/output/1712E2.data";
        CompressedSpriteManager compressedSpriteManager = new CompressedSpriteManager(null);
        compressedSpriteManager.compressFile(uncomp, Header.INTRO_TEXTS_SPRITE_HEADER, outputFile);
    }

    public void score() throws IOException {
        generateSpriteDataFromImage(
                "src/main/resources/images/sprites/score-screen-title-sprite.png",
                "src/main/resources/data/sprite-uncompressed.data",
                new PaletteIntro(),
                2
        );
        String uncomp = "src/main/resources/data/sprite-uncompressed.data";
        String decomp = "src/main/resources/data/sprite-decompressed.data";
        String outputFile = "src/main/resources/data/output/1CDD63.data";
        CompressedSpriteManager compressedSpriteManager = new CompressedSpriteManager(null);
        compressedSpriteManager.compressFile(uncomp, Header.SCORE_TITLE_SPRITE_HEADER, outputFile);
        compressedSpriteManager.decompressFile(outputFile, decomp);
    }

    public void trainingMap() throws IOException {
        generateSpriteDataFromImage(
                "src/main/resources/images/sprites/takama.png",
                "src/main/resources/data/sprite-uncompressed.data",
                new PaletteGame(),
                4
        );
        String uncomp = "src/main/resources/data/sprite-uncompressed.data";
        String outputFile = "src/main/resources/data/output/1B3000.data";
        CompressedSpriteManager compressedSpriteManager = new CompressedSpriteManager(null);
        compressedSpriteManager.compressCopyFile(uncomp, Header.TRAINING_MAP_SPRITE_HEADER, outputFile);
    }

    public void trainingMapOrder() throws IOException {
        FontImageReader imageReader = new FontImageReader();
        String uncomp = "src/main/resources/data/input/training-map-order-uncompressed.data";
        String outputFile = "src/main/resources/data/output/1B9000.data";
        CompressedSpriteManager compressedSpriteManager = new CompressedSpriteManager(null);
        compressedSpriteManager.compressCopyFile(uncomp, Header.TRAINING_MAP_SPRITE_ORDER_HEADER, outputFile);
    }

    public void trainingMapTilesDefinition() throws IOException {
        FontImageReader imageReader = new FontImageReader();
        String uncomp = "src/main/resources/data/input/training-map-tiles-uncompressed.data";
        String outputFile = "src/main/resources/data/output/1BB000.data";
        CompressedSpriteManager compressedSpriteManager = new CompressedSpriteManager(null);
        compressedSpriteManager.compressCopyFile(uncomp, Header.TRAINING_MAP_SPRITE_TILES_HEADER, outputFile);
    }

    public void map() throws IOException {
        generateSpriteDataFromImage(
                "src/main/resources/images/sprites/map-sprite.png",
                "src/main/resources/data/sprite-uncompressed.data",
                new PaletteGame(),
                4
        );
        String uncomp = "src/main/resources/data/sprite-uncompressed.data";
        String outputFile = "src/main/resources/data/output/1D0000.data";
        CompressedSpriteManager compressedSpriteManager = new CompressedSpriteManager(null);
        compressedSpriteManager.compressFile(uncomp, Header.MAP_SPRITE_HEADER, outputFile);
    }

    /**
     * 04 70 04     29855       690s
     * 04 70 04     30580       132s
     * 04 70 05     30617       129s
     * 04 70 06     30693       135s
     * 04 70 07
     * 04 70 08
     *
     */
    public void mapOrder() throws IOException {
        FontImageReader imageReader = new FontImageReader();
        String uncomp = "src/main/resources/data/input/map-order-uncompressed.data";
        String outputFile = "src/main/resources/data/output/1E0000.data";
        CompressedSpriteManager compressedSpriteManager = new CompressedSpriteManager(null);
        compressedSpriteManager.compressCopyFile(uncomp, Header.MAP_ORDER_HEADER, outputFile);
    }

    protected static String generateSpriteDataFromImage(String image, String output, OldPalette palette, int bpp) throws IOException {
        System.out.println("Generating Sprite Data from image "+image);
        FontImageReader fontImageReader = new FontImageReader();
        String s = "";
        if (bpp==2) s = fontImageReader.loadFontImage2bpp(image, palette);
        else s = fontImageReader.loadFontImage4bpp(image, palette);
        byte[] bytes = fontImageReader.getBytes();

        try (FileOutputStream fos = new FileOutputStream(output)) {
            fos.write(bytes);
            fos.close();
            //There is no more need for this line since you had created the instance of "fos" inside the try. And this will automatically close the OutputStream
        }

        return s;
    }

    protected String generateSpriteDataFromImage(String image, String output, services.palettes.Palette palette, int bpp) throws IOException {
        outputStream = new ByteArrayOutputStream();
        System.out.println("Generating Sprite Data from image "+image);
        String s = "";
        if (bpp==4) s = loadFontImage4bpp(image, palette);
        else if (bpp==2) s = loadFontImage2bpp(image, palette);
        //else s = loadFontImage4bpp(image, palette);
        byte[] bytes = getBytes();

        try (FileOutputStream fos = new FileOutputStream(output)) {
            fos.write(bytes);
            fos.close();
            //There is no more need for this line since you had created the instance of "fos" inside the try. And this will automatically close the OutputStream
        }

        return s;
    }

    public byte[] getBytes() {
        return outputStream.toByteArray();
    }

    public String loadFontImage2bpp(String file, Palette palette) {
        StringBuffer sb = new StringBuffer();
        try {
            image = ImageIO.read(new File(file));
        } catch (IOException e) {

        }
        boolean stop = false;
        int tileX = 0, tileY = 0, x = 0, y = 0;
        while (tileY++<image.getHeight()/8) {
            tileX=0;
            while (tileX++<image.getWidth()/8) {
                y=0;
                while (y++<8) {
                    x=0;
                    int encodedLine = 0;
                    while (x++<8) {
                        int rgb = image.getRGB(((tileX - 1) * 8 + (x - 1)), ((tileY - 1) * 8 + (y - 1)));
                        if (rgb==0) {
                            stop = true;
                            break;
                        }
                        String color = services.Utils.getColorAsHex(rgb).toLowerCase();
                        services.palettes.FontColor fontColor = palette.getFontColor(color);
                        int mask = fontColor.getMask();
                        mask = mask >> (x-1);
                        encodedLine = encodedLine | mask;
                    }
                    if (stop) break;
                    int leftByte = encodedLine >> 8;
                    int rightByte = encodedLine & 0x00FF;
                    outputStream.write(leftByte);
                    outputStream.write(rightByte);
                    String hex = services.Utils.toHexString(leftByte, 2)+" "+ services.Utils.toHexString(rightByte,2);
                    sb.append(hex.replaceAll(" ",""));
                    //System.out.print(hex+" ");
                }
                if (stop) break;
                //System.out.println();
            }
            if (stop) break;
        }
        return sb.toString();
    }

    public String loadFontImage4bpp(String file, Palette palette) {
        StringBuffer sb = new StringBuffer();
        byte[] output = new byte[0];
        int indexOutput = 0;
        try {
            image = ImageIO.read(new File(file));
            output = new byte[image.getHeight()*image.getWidth()/2];
        } catch (IOException e) {

        }
        boolean stop = false;
        int tileX = 0, tileY = 0, x = 0, y = 0;
        while (tileY++<image.getHeight()/8) {
            tileX=0;
            while (tileX++<image.getWidth()/8) {
                y=0;
                while (y++<8) {
                    x=0;
                    long encodedLine = 0;
                    while (x++<8) {
                        int rgb = image.getRGB(((tileX - 1) * 8 + (x - 1)), ((tileY - 1) * 8 + (y - 1)));
                        String color = services.Utils.getColorAsHex(rgb).toLowerCase();
                        if (rgb==0) {
                            stop = true;
                            break;
                        }
                        services.palettes.FontColor fontColor = palette.getFontColor(color);
                        long mask = fontColor.getLongMask();
                        mask = mask >> (x-1);
                        encodedLine = encodedLine | mask;
                    }
                    if (stop) break;
                    //int leftByte = encodedLine >> 8;
                    //int rightByte = encodedLine & 0x00FF;
                    long byte1 = encodedLine >> 24;
                    long byte2 = (encodedLine >> 16) & 0x00FF;
                    long byte3 = (encodedLine >> 8) & 0x00FF;
                    long byte4 = (encodedLine) & 0x00FF;

                    output[indexOutput] = (byte) ((byte1) & 0xFF);
                    output[indexOutput+1] = (byte) ((byte2) & 0xFF);
                    output[indexOutput+16] = (byte) ((byte3) & 0xFF);
                    output[indexOutput+17] = (byte) ((byte4) & 0xFF);
                    indexOutput += 2;
                }
                indexOutput += 16;
                if (stop) break;
            }
            if (stop) break;
        }
        int k = 0;
        for (byte b:output) {
            //if (k++%16==0) System.out.println();
            String s = h2(b) + " ";
            sb.append(s);
            outputStream.write(b);
            //System.out.print(s);
        }
        return sb.toString();
    }
    
    public String loadFontImage2bpp(String file, OldPalette palette) {
        StringBuffer sb = new StringBuffer();
        try {
            image = ImageIO.read(new File(file));
        } catch (IOException e) {

        }
        boolean stop = false;
        int tileX = 0, tileY = 0, x = 0, y = 0;
        while (tileY++<image.getHeight()/8) {
            tileX=0;
            while (tileX++<image.getWidth()/8) {
                y=0;
                while (y++<8) {
                    x=0;
                    int encodedLine = 0;
                    while (x++<8) {
                        int rgb = image.getRGB(((tileX - 1) * 8 + (x - 1)), ((tileY - 1) * 8 + (y - 1)));
                        if (rgb==0) {
                            stop = true;
                            break;
                        }
                        FontColor fontColor = palette.getFontColor(rgb);
                        int mask = fontColor.getMask();
                        mask = mask >> (x-1);
                        encodedLine = encodedLine | mask;
                    }
                    if (stop) break;
                    int leftByte = encodedLine >> 8;
                    int rightByte = encodedLine & 0x00FF;
                    outputStream.write(leftByte);
                    outputStream.write(rightByte);
                    String hex = Utils.toHexString(leftByte, 2)+" "+Utils.toHexString(rightByte,2);
                    sb.append(hex.replaceAll(" ",""));
                    if (verbose) System.out.print(hex+" ");
                }
                if (stop) break;
                if (verbose) System.out.println();
            }
            if (stop) break;
        }
        return sb.toString();
    }

    public String loadFontImage4bpp(String file, OldPalette palette) {
        StringBuffer sb = new StringBuffer();
        byte[] output = new byte[0];
        int indexOutput = 0;
        try {
            image = ImageIO.read(new File(file));
            output = new byte[image.getHeight()*image.getWidth()/2];
        } catch (IOException e) {

        }
        boolean stop = false;
        int tileX = 0, tileY = 0, x = 0, y = 0;
        while (tileY++<image.getHeight()/8) {
            tileX=0;
            while (tileX++<image.getWidth()/8) {
                y=0;
                while (y++<8) {
                    x=0;
                    long encodedLine = 0;
                    while (x++<8) {
                        int rgb = image.getRGB(((tileX - 1) * 8 + (x - 1)), ((tileY - 1) * 8 + (y - 1)));
                        String color = Utils.getColorAsHex(rgb).toLowerCase();
                        if (rgb==0) {
                            stop = true;
                            break;
                        }
                        FontColor fontColor = palette.getFontColor(color);
                        long mask = fontColor.getLongMask();
                        mask = mask >> (x-1);
                        encodedLine = encodedLine | mask;
                    }
                    if (stop) break;
                    //int leftByte = encodedLine >> 8;
                    //int rightByte = encodedLine & 0x00FF;
                    long byte1 = encodedLine >> 24;
                    long byte2 = (encodedLine >> 16) & 0x00FF;
                    long byte3 = (encodedLine >> 8) & 0x00FF;
                    long byte4 = (encodedLine) & 0x00FF;

                    output[indexOutput] = (byte) ((byte1) & 0xFF);
                    output[indexOutput+1] = (byte) ((byte2) & 0xFF);
                    output[indexOutput+16] = (byte) ((byte3) & 0xFF);
                    output[indexOutput+17] = (byte) ((byte4) & 0xFF);
                    indexOutput += 2;
                }
                indexOutput += 16;
                if (stop) break;
            }
            if (stop) break;
        }
        int k = 0;
        for (byte b:output) {
            if (k++%16==0) if (verbose) System.out.println();
            String s = Utils.toHexString(b) + " ";
            sb.append(s);
            outputStream.write(b);
            if (verbose) System.out.print(s);
        }
        return sb.toString();
    }


}
