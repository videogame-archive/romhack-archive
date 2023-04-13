package dokapon;

import dokapon.characters.LatinChar;
import dokapon.entities.*;
import dokapon.lz.entities.Header;
import dokapon.services.*;
import dokapon.sprites.FontImageReader;
import dokapon.sprites.SpriteMaker;
import services.FontImageReaderRevised;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static dokapon.services.Utils.toHexString;

public class Dokapon {

    static byte[] data;
    static Translator translator;
    static LatinLoader latinLoader;
    static SpriteWriter spriteWriter;
    static List<PointerTable> tables;
    static Config config;

    public static int EXTRA_DATA_BANK_REQUIRED = 16;
    public static boolean DEBUG = false;
    //f200 f300 b300 f400
    public static void main(String[] args) throws IOException {
        latinLoader = new LatinLoader();
        spriteWriter = new SpriteWriter();
        translator = new Translator(latinLoader);
        config = JsonLoader.loadConfig();
        JsonLoader.loadCodePatches();

        System.out.println("Loading config");
        System.out.println("rom-input="+config.getRomInput());
        System.out.println("rom-output="+config.getRomOutput());
        System.out.println("bps-patch-output="+config.getBpsPatchOutput());

        try {
            data = Files.readAllBytes(new File(config.getRomInput()).toPath());
        } catch (IOException ex) {
            Logger.getLogger(Dokapon.class.getName()).log(Level.SEVERE, null, ex);
        }
        data = DataWriter.fillDataWithPlaceHolders(data);

        if (DEBUG) {
            Header headerSprites = new Header(
                    (byte) Integer.parseInt("00", 16),
                    (byte) Integer.parseInt("10", 16),
                    (byte) Integer.parseInt("05", 16)
            );
            Header headerOrder = new Header(
                    (byte) Integer.parseInt("00", 16),
                    (byte) Integer.parseInt("08", 16),
                    (byte) Integer.parseInt("07", 16)
            );
            System.out.println(headerOrder.getMaxPosition());
            System.out.println(headerOrder.getMaxSize());
            System.out.println(headerSprites.getMaxPosition());
            System.out.println(headerSprites.getMaxSize());

            tables = JsonLoader.loadTables();
            for (PointerTable table:tables) {
                List<PointerRange> ranges = table.getRanges();
                for (PointerRange range : ranges) {
                    System.out.println("table "+table.getId()+"  "+toHexString(range.getStart(),6)+" - "+toHexString(range.getEnd(),6));
                }

            }



            return;
        }

        List<TownSign> townSigns = JsonLoader.loadTownSigns();
        SpriteMaker spriteMaker = new SpriteMaker(data);
        //spriteMaker.generateSigns(data, townSigns, "src/main/resources/data/190000.data");
        //spriteMaker.generateMapOrder(townSigns, "src/main/resources/data/jpn/BBA39.data", "src/main/resources/data/map-order-uncompressed.data");
        //spriteMaker.printMap("src/main/resources/data/jpn/BBA39.data");
        FontImageReaderRevised imageReader = new FontImageReaderRevised();
        imageReader.disable();
        
        imageReader.trainingMap();
        imageReader.trainingMapOrder();
        imageReader.trainingMapTilesDefinition();
        
        imageReader.introQuote();
        imageReader.introQuoteOrder();
        imageReader.introTexts();
        imageReader.score();
        
        imageReader.map();
        imageReader.mapOrder();
        
        imageReader.titleScreen();
        imageReader.titleScreenOrder();
        imageReader.freeTownBanner();
        imageReader.doc();
        imageReader.salesman();
        imageReader.roshambo();
        imageReader.pennyOmg();
        imageReader.chapterEnd();
        imageReader.chapterStart();;
        imageReader.chapterStartOrder();
        imageReader.rico();
        imageReader.battleCards();
        imageReader.bill();

        //imageReader.enable();
        //imageReader.monsterGremlin();
        
        //spriteMaker.generateDataFile("src/main/resources/data/190000.data");

        latinLoader.loadLatin();
        spriteWriter.writeLatinChars(latinLoader.getLatinChars(), data);

        DataWriter.writeCodePatches(JsonLoader.loadCodePatches(), data, false);

        tables = JsonLoader.loadTables();
        for (String s:JsonLoader.loadTranslationFiles()) {
            try {
                translator.loadTranslationFile(s);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        translator.setSpecialCharMap(JsonLoader.loadSpecialChars());
        translator.checkTranslations(data);
        for (PointerTable table:tables) {
            DataReader.readTable(table, data);
        }
        for (PointerTable table:tables) {
            DataReader.generateEnglish(translator, table, data);
        }
        /*System.out.println(Translation.test.size());
        for (String s: Translation.test) {
            System.out.println("test:"+s);
        }*/
        for (PointerTable table:tables) {
            DataWriter.writeEnglish(table, data);
        }

        for (PointerTable table:tables) {
            if (table.getId()==7) {
                //DataReader.collectSpecialChars(translator, table, data);
            }
            if(table.getId()==2) {
                //DataReader.collectSpecialChars(translator, table, data);
            }
        }
        for (PointerTable table:tables) {
            /*if (table.getId()==7) {
                DataReader.generateEnglishPointer(translator, table, data);
            }
            else */
            if (table.getId()==2) {
                //DataReader.checkMenuData(table);
            }
        }
        //translator.showSpecialChars();


        for (InputPatch ip:JsonLoader.loadInputPatches()) {
            ip.generateCode(latinLoader.getLatinChars());
            if (!ip.isDebug()) ip.writePatch(data);
        }

        for (PointerTable table:tables) {
            List<PointerData> dataJap = table.getDataJap();
            /*if (table.getId()==2) {
                for (PointerData pd:dataJap) {
                    String english = translator.getEnglish(pd);
                    String[] menuData = DataWriter.extractMenuData(pd, english);
                    english = DataWriter.removeMenuData(english, menuData);
                    String printableString = DataWriter.getPrintableString(pd, translator, JsonLoader.loadJap(), english);
                    System.out.println(printableString);
                }
            }*/
            /*if (table.getId()==7) {
                int totalDataLength = 0;
                for (PointerData pd:dataJap) {
                    String english = translator.getEnglish(pd);
                    translator.checkInGameLength(english);
                    //totalDataLength += translator.checkDataLength(english);
                    String printableString = DataWriter.getPrintableString(pd, translator, JsonLoader.loadJap(), english);
                    //System.out.println(printableString);
                }
                List<Integer> values = new ArrayList<>();
                for (PointerData p:table.getDataEng()) {
                    int value = p.getValue();
                    String[] data = p.getData();
                    if (!values.contains(value)) {
                        values.add(value);
                        totalDataLength += data.length;
                    }
                }
                System.out.println(totalDataLength*2);
            }*/
        }

        /*try {
            DataReader.generateCredits();
        } catch (IOException e) {
            e.printStackTrace();
        }*/

        // PRINT DIALOG JAPANESE NAMES
        List<String> names = new ArrayList<>();
        List<String> lines = new ArrayList<>();
        for (PointerTable table:tables) {
            //if (table.getId()==5) {
            List<PointerData> dataJap = table.getDataJap();
            for (PointerData pd:dataJap) {
                if (Utils.concat(pd.getData()).contains("b600")) {
                    String japanese = translator.getJapanese(Utils.concat(pd.getData()), JsonLoader.loadJap());
                    //String printableString = DataWriter.getPrintableString(pd, translator, JsonLoader.loadJap());
                    //System.out.println(printableString);
                    String name = japanese.substring(0, japanese.indexOf("┌"));
                    if (!names.contains(name)) {
                        names.add(name);
                        lines.add(japanese);
                    }
                }
                //System.out.println(printableString);
            }
            //}
        }
        //for (String s:lines) System.out.println(s);

        /*for (String s:new String[]{
                "fc00 fd00 bf00 bf00 bf00 bf00 bf00 bf00 bf00 bf00 4210 7585 0030 bf00 0030 0201 0301 bf00 bf00 bf00 bf00 8010 8085 0030 bf00 0030 9700 8400 9600 bf00 bf00 bf00 bf00 bf00 bf00 bf00 bf00 bf00 bf00 bf00 bf00 1110 9085 0030 ee00 ef00 bf00 bf00 7310 7785 0030 f000 f100 bf00 bf00 bf00 bf00 bf00 4310 7b85 c100 0030 f200 f300 bf00 bf00 bf00 6310 7d85 c100 0030 c500 c000 c400 c600 bf00 bf00 bf00 bf00 5310 9185 c100 ffff"
        })
        System.out.println(
                translator.getJapanese(
                        s,
                        JsonLoader.loadJap()));*/

        System.out.println("Saving rom-output...");
        DataWriter.saveData(config.getRomOutput(), data);
        System.out.println("Saving bps-patch-output...");
        //Patcher.generatePatch(new File(config.getRomInput()), new File(config.getRomOutput()), new File(config.getBpsPatchOutput()), "https://github.com/Krokodyl/dokapon-english");
        System.out.println("Process complete");
    }

    public static long start = -1;

    public static void resetTime() {
        start = System.currentTimeMillis();
    }

    public static long getTime() {
        if (start<0) start = System.currentTimeMillis();
        return (System.currentTimeMillis()-start)/1000;
    }
}
