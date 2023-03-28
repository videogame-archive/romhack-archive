package services;

import dokapon.Dokapon;
import dokapon.enums.*;
import dokapon.lz.compression.CopyCompressor;
import dokapon.lz.entities.Header;
import dokapon.sprites.CompressedSpriteManager;
import dokapon.sprites.FontImageReader;
import services.lz.LzCompressor;
import services.lz.REPEAT_ALGORITHM;
import services.palettes.Palette4bpp;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

import static services.Utils.h;

public class FontImageReaderRevised extends FontImageReader {

    boolean enabled = true;
    
    @Override
    public void trainingMap() throws IOException {
        generateSpriteDataFromImage(
                "src/main/resources/images/sprites/takama.png",
                "src/main/resources/data/sprite-uncompressed.data",
                new PaletteGame(),
                4
        );
        String uncomp = "src/main/resources/data/sprite-uncompressed.data";
        String outputFile = "src/main/resources/data/output/1B3000.data";
        optimizedCompression(uncomp, outputFile);
    }

    @Override
    public void trainingMapOrder() throws IOException {
        String uncomp = "src/main/resources/data/input/training-map-order-uncompressed.data";
        String outputFile = "src/main/resources/data/output/1B9000.data";
        optimizedCompression(uncomp, outputFile);
    }

    @Override
    public void trainingMapTilesDefinition() throws IOException {
        String uncomp = "src/main/resources/data/input/training-map-tiles-uncompressed.data";
        String outputFile = "src/main/resources/data/output/1BB000.data";
        optimizedCompression(uncomp, outputFile);
    }

    @Override
    public void introQuote() throws IOException {
        generateSpriteDataFromImage(
                "src/main/resources/images/sprites/intro-quote-sprite.png",
                "src/main/resources/data/sprite-uncompressed.data",
                new PaletteIntro(),
                2
        );
        String uncomp = "src/main/resources/data/sprite-uncompressed.data";
        String outputFile = "src/main/resources/data/output/1B8000.data";
        optimizedCompression(uncomp, outputFile);
    }

    @Override
    public void introQuoteOrder() throws IOException {
        FontImageReader imageReader = new FontImageReader();
        String uncomp = "src/main/resources/data/input/intro-quote-uncompressed.data";
        String outputFile = "src/main/resources/data/output/1B8800.data";
        optimizedCompression(uncomp, outputFile);
    }

    @Override
    public void introTexts() throws IOException {
        generateSpriteDataFromImage(
                "src/main/resources/images/sprites/intro-texts-sprite.png",
                "src/main/resources/data/sprite-uncompressed.data",
                new PaletteIntro(),
                2
        );
        String uncomp = "src/main/resources/data/sprite-uncompressed.data";
        String outputFile = "src/main/resources/data/output/1712E2.data";
        optimizedCompression(uncomp, outputFile);
    }

    public void score() throws IOException {
        generateSpriteDataFromImage(
                "src/main/resources/images/sprites/score-screen-title-sprite.png",
                "src/main/resources/data/sprite-uncompressed.data",
                new PaletteIntro(),
                2
        );
        String uncomp = "src/main/resources/data/sprite-uncompressed.data";
        String outputFile = "src/main/resources/data/output/1CDD63.data";
        optimizedCompression(uncomp, outputFile);
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
        optimizedCompression(uncomp, outputFile);
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
        optimizedCompression(uncomp, outputFile);
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
        optimizedCompression(uncomp, outputFile);
    }

    public void titleScreenOrder() throws IOException {
        String uncomp = "src/main/resources/data/input/title-order-uncompressed.data";
        String outputFile = "src/main/resources/data/output/1F8000.data";
        optimizedCompression(uncomp, outputFile);
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
        optimizedCompression(uncomp, outputFile);
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
        optimizedCompression(uncomp, outputFile);
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
        optimizedCompression(uncomp, outputFile);
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
        optimizedCompression(uncomp, outputFile);
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
        optimizedCompression(uncomp, outputFile);
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
        optimizedCompression(uncomp, outputFile);
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
        optimizedCompression(uncomp, outputFile);
    }

    public void chapterStartOrder() throws IOException {
        CompressedSpriteManager compressedSpriteManager = new CompressedSpriteManager(null);
        optimizedCompression(
                "src/main/resources/data/input/chapter-1-start-order-uncompressed.data",
                "src/main/resources/data/output/1E8020.data");
        optimizedCompression(
                "src/main/resources/data/input/chapter-2-start-order-uncompressed.data",
                "src/main/resources/data/output/1E9000.data");
        optimizedCompression(
                "src/main/resources/data/input/chapter-3-start-order-uncompressed.data",
                "src/main/resources/data/output/1EA000.data");
        optimizedCompression(
                "src/main/resources/data/input/chapter-4-start-order-uncompressed.data",
                "src/main/resources/data/output/1EB000.data");
        optimizedCompression(
                "src/main/resources/data/input/chapter-5-start-order-uncompressed.data",
                "src/main/resources/data/output/1EC000.data");
        optimizedCompression(
                "src/main/resources/data/input/chapter-6-start-order-uncompressed.data",
                "src/main/resources/data/output/1ED000.data");
        optimizedCompression(
                "src/main/resources/data/input/chapter-7-start-order-uncompressed.data",
                "src/main/resources/data/output/1EE000.data");
        optimizedCompression(
                "src/main/resources/data/input/chapter-8-start-order-uncompressed.data",
                "src/main/resources/data/output/1EF000.data");
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
        optimizedCompression(uncomp, outputFile);
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
        optimizedCompression(uncomp, outputFile);
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
        optimizedCompression(uncomp, outputFile);
    }

    /*public void monsterGremlin() throws IOException {
        generateSpriteDataFromImage(
                "src/main/resources/images/sprites/monster-gremlin.png",
                "src/main/resources/data/sprite-uncompressed.data",
                new Palette4bpp("/palettes/monster-gremlin.png"),
                4
        );
        String uncomp = "src/main/resources/data/sprite-uncompressed.data";
        String outputFile = "src/main/resources/data/output/192000.data";
        optimizedCompression(uncomp, outputFile);
    }*/

    void optimizedCompression(String input, String output) throws IOException {
        
        if (!enabled) return;
        //Map<LzCompressor, byte[]> compressorMap = new HashMap<>();
        byte[] bestCompression = null; 

        byte[] data = Files.readAllBytes(new File(input).toPath());
        
        for (REPEAT_ALGORITHM algorithm : REPEAT_ALGORITHM.values()) {
            LzCompressor compressor = new LzCompressor(algorithm);
            byte[] compressedBytes = compressor.compressData(data, false);
            System.out.println("Compression "+algorithm.name()+" : "+h(compressedBytes.length));
            if (bestCompression==null) bestCompression = compressedBytes;
            else if (compressedBytes.length<bestCompression.length) {
                System.out.println("New best compression "+algorithm.name());
                bestCompression = compressedBytes;
            }
            //compressorMap.put(compressor, compressedBytes);
        }



        Dokapon.resetTime();

        //CopyCompressor compressor = new CopyCompressor(header, data);

        System.out.println();
        System.out.println("Optimized Compressed length : "+ h(bestCompression.length));
        System.out.println();
        //System.out.println("Compressed bytes : "+Utils.bytesToHex(compressedBytes));
        System.out.println("Optimized Time : "+(Dokapon.getTime()));

        System.out.println("Optimized Compressing data - output : "+output);

        Files.write(new File(output).toPath(), bestCompression);
    }

    void compressCopyFile(LzCompressor compressor, String input, String output) throws IOException {
        byte[] data = Files.readAllBytes(new File(input).toPath());

        Dokapon.resetTime();

        //CopyCompressor compressor = new CopyCompressor(header, data);
        byte[] compressedBytes = compressor.compressData(data, false);

        System.out.println();
        System.out.println("Revised Compressed length : "+ h(compressedBytes.length));
        System.out.println();
        //System.out.println("Compressed bytes : "+Utils.bytesToHex(compressedBytes));
        System.out.println("Revised Time : "+(Dokapon.getTime()));

        System.out.println("Revised Compressing data - output : "+output);

        Files.write(new File(output).toPath(), compressedBytes);
    }

    public void disable() {
        enabled = false;
    }

    public void enable() {
        enabled = true;
    }
}
