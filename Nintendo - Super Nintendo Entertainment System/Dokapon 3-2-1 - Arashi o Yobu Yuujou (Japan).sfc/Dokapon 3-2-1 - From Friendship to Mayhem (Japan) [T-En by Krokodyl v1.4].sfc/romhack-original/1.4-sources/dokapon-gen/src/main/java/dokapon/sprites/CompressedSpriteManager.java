package dokapon.sprites;

import dokapon.Dokapon;
import dokapon.entities.Config;
import dokapon.lz.compression.Compressor;
import dokapon.lz.compression.CopyCompressor;
import dokapon.lz.decompression.Decompressor;
import dokapon.lz.entities.Header;
import dokapon.services.JsonLoader;
import dokapon.services.Utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import static services.Utils.h;

public class CompressedSpriteManager {

    byte[] data;

    public CompressedSpriteManager(byte[] data){
        this.data = data;
    }

    public static void main(String[] args) {
        Config config = JsonLoader.loadConfig();
        try {
            byte[] data = Files.readAllBytes(new File(config.getRomInput()).toPath());
            new CompressedSpriteManager(data).decompressStuff();
        } catch (IOException ex) {
            Logger.getLogger(Dokapon.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void testCopyCompressor() throws IOException {
        String uncomp = "src/main/resources/data/jpn/BBA39.data";
        String outputFile = "src/main/resources/data/output/1E0000.data";
        CompressedSpriteManager compressedSpriteManager = new CompressedSpriteManager(null);
        compressedSpriteManager.compressCopyFile(uncomp, Header.MAP_ORDER_HEADER, outputFile);
    }

    private void compressTownNames() throws IOException {
        System.out.println();
        Header header = new Header(
                (byte) Integer.parseInt("00", 16),
                (byte) Integer.parseInt("5A", 16),
                (byte) Integer.parseInt("03", 16)
        );
        System.out.println(header.getMaxPosition());

        byte[] data = Files.readAllBytes(new File("D:/git/dokapon-english/sprites/town-names/sprite-uncompressed.data").toPath());

        Compressor compressor = new Compressor(data, header);
        compressor.compress();
        byte[] compressedBytes = compressor.getCompressedBytes();

        System.out.println();
        System.out.println("Compressed length : "+ compressedBytes.length);
        System.out.println();
        System.out.println("Compressed bytes : "+ Utils.bytesToHex(compressedBytes));

        System.out.println(System.currentTimeMillis());

        try (FileOutputStream fos = new FileOutputStream(
                //"D:/git/dokapon-english/sprites/intro-01/english-sprite-order-01-decompressed.data"
                "D:/git/dokapon-english/sprites/town-names/sprite-compressed.data"
        )) {
            fos.write(compressedBytes);
            fos.close();
            //There is no more need for this line since you had created the instance of "fos" inside the try. And this will automatically close the OutputStream
        }
    }

    private void compressMapTiles() throws IOException {
        System.out.println();
        Header header = new Header(
                (byte) Integer.parseInt("00", 16),
                (byte) Integer.parseInt("12", 16),
                (byte) Integer.parseInt("04", 16)
        );
        System.out.println(header.getMaxPosition());

        byte[] data = Files.readAllBytes(new File("D:/git/dokapon-english/sprites/town-names/sprite-decompressed-BDC95.data").toPath());

        Compressor compressor = new Compressor(data, header);
        compressor.compress();
        byte[] compressedBytes = compressor.getCompressedBytes();

        System.out.println();
        System.out.println("Compressed length : "+ compressedBytes.length);
        System.out.println();
        System.out.println("Compressed bytes : "+Utils.bytesToHex(compressedBytes));

        System.out.println(System.currentTimeMillis());

        try (FileOutputStream fos = new FileOutputStream(
                //"D:/git/dokapon-english/sprites/intro-01/english-sprite-order-01-decompressed.data"
                "D:/git/dokapon-english/sprites/town-names/sprite-compressed-BDC95.data"
        )) {
            fos.write(compressedBytes);
            fos.close();
            //There is no more need for this line since you had created the instance of "fos" inside the try. And this will automatically close the OutputStream
        }
    }

    private void compressMapReference() throws IOException {
        System.out.println();
        Header header = new Header(
                (byte) Integer.parseInt("04", 16),
                (byte) Integer.parseInt("70", 16),
                (byte) Integer.parseInt("04", 16)
        );
        System.out.println(header.getMaxPosition());
        Dokapon.getTime();

        byte[] data = Files.readAllBytes(new File("D:/git/dokapon-english/sprites/town-names/map-order-uncompressed.data").toPath());

        Compressor compressor = new Compressor(data, header);
        compressor.compress();
        byte[] compressedBytes = compressor.getCompressedBytes();

        System.out.println();
        System.out.println("Compressed length : "+ compressedBytes.length);
        System.out.println();
        System.out.println("Compressed bytes : "+Utils.bytesToHex(compressedBytes));

        System.out.println(Dokapon.getTime());

        try (FileOutputStream fos = new FileOutputStream(
                //"D:/git/dokapon-english/sprites/intro-01/english-sprite-order-01-decompressed.data"
                "D:/git/dokapon-english/sprites/town-names/sprite-compressed-BBA39.data"
        )) {
            fos.write(compressedBytes);
            fos.close();
            //There is no more need for this line since you had created the instance of "fos" inside the try. And this will automatically close the OutputStream
        }
    }

    void compressFile(String input, Header header, String output) throws IOException {
        byte[] data = Files.readAllBytes(new File(input).toPath());

        Dokapon.resetTime();

        Compressor compressor = new Compressor(data, header);
        compressor.compress();
        byte[] compressedBytes = compressor.getCompressedBytes();

        System.out.println();
        System.out.println("Compressed length : "+ compressedBytes.length);
        System.out.println();
        System.out.println("Compressed bytes : "+Utils.bytesToHex(compressedBytes));
        System.out.println("Time : "+(Dokapon.getTime()));

        Files.write(new File(output).toPath(), compressedBytes);
    }

    void compressCopyFile(String input, Header header, String output) throws IOException {
        byte[] data = Files.readAllBytes(new File(input).toPath());

        Dokapon.resetTime();

        CopyCompressor compressor = new CopyCompressor(header, data);
        compressor.compress();
        byte[] compressedBytes = compressor.getCompressedBytes();

        System.out.println();
        System.out.println("Compressed length : "+ h(compressedBytes.length));
        System.out.println();
        //System.out.println("Compressed bytes : "+Utils.bytesToHex(compressedBytes));
        System.out.println("Time : "+(Dokapon.getTime()));

        System.out.println("Compressing data - output : "+output);

        Files.write(new File(output).toPath(), compressedBytes);
    }

    private void compressScore() throws IOException {
        byte[] data = Files.readAllBytes(new File("D:/git/dokapon-english/sprites/score/sprite-uncompressed.data").toPath());

        Header header = new Header(
                (byte) Integer.parseInt("00", 16),
                (byte) Integer.parseInt("20", 16),
                (byte) Integer.parseInt("06", 16)
        );

        Compressor compressor = new Compressor(data, header);
        compressor.compress();
        byte[] compressedBytes = compressor.getCompressedBytes();

        System.out.println();
        System.out.println("Compressed length : "+ compressedBytes.length);
        System.out.println();
        System.out.println("Compressed bytes : "+Utils.bytesToHex(compressedBytes));
    }

    public void decompressFile(String inputFile, String outputFile) throws IOException {
        byte[] data = Files.readAllBytes(new File(inputFile).toPath());
        Decompressor decompressor = new Decompressor(data, 3);
        decompressor.decompressData();
        byte[] decompressedBytes = decompressor.getDecompressedBytes();
        System.out.println();
        System.out.println("Decompressed bytes : "+Utils.bytesToHex(decompressedBytes));
        Files.write(new File(outputFile).toPath(), decompressedBytes);
    }

    private void decompressIntro2() throws IOException {
        int start = Integer.parseInt("1712E5", 16);
        Header headerSprites = new Header(
                (byte) Integer.parseInt("00", 16),
                (byte) Integer.parseInt("20", 16),
                (byte) Integer.parseInt("05", 16)
        );
        Decompressor decompressor = new Decompressor(data, start);
        decompressor.decompressData();
        byte[] decompressedBytes = decompressor.getDecompressedBytes();


        System.out.println();
        System.out.println("Decompressed bytes : "+Utils.bytesToHex(decompressedBytes));

        try (FileOutputStream fos = new FileOutputStream(
                //"D:/git/dokapon-english/sprites/intro-01/english-sprite-order-01-decompressed.data"
                "D:/git/dokapon-english/sprites/intro-02/sprite-decompressed.data"
        )) {
            fos.write(decompressedBytes);
            fos.close();
            //There is no more need for this line since you had created the instance of "fos" inside the try. And this will automatically close the OutputStream
        }
    }

    private void decompressTownNames() throws IOException {
        int start = Integer.parseInt("B2528", 16)+3;
        start = Integer.parseInt("1D0000", 16)+3;
        Decompressor decompressor = new Decompressor(data, start);
        decompressor.decompressData();
        byte[] decompressedBytes = decompressor.getDecompressedBytes();


        System.out.println();
        System.out.println("Decompressed bytes : "+Utils.bytesToHex(decompressedBytes));

        try (FileOutputStream fos = new FileOutputStream(
                //"D:/git/dokapon-english/sprites/intro-01/english-sprite-order-01-decompressed.data"
                "D:/git/dokapon-english/sprites/town-names/sprite-decompressed.data"
        )) {
            fos.write(decompressedBytes);
            fos.close();
            //There is no more need for this line since you had created the instance of "fos" inside the try. And this will automatically close the OutputStream
        }
    }

    private void decompressStuff() throws IOException {
        String[] addresses = new String[]{
                /*"D4BE4",
                "16D58F",
                "16EA3F",*/
                //"585AA"

                /*"16CD62",
                "16CE67",
                "16CF6C",
                "16D071",
                "16D176",
                "16D27B",
                "16D380",
                "16D485"*/

                /*"BF746",
                "D4DE2",
                "100FC4",
                "101755"*/

                "14EE47"

                //"175C9E"
                //"140AD5"
                /*"BBA39",
                "BDC95",
                "B2528"*/
                /*"BBA39",
                "1D0000",
                "D4DE2",
                "1712E2",
                "BEA18"*/
        };
        for (String s:addresses) {
            int start = Integer.parseInt(s, 16)+3;
            Decompressor decompressor = new Decompressor(data, start);
            decompressor.decompressData();
            byte[] decompressedBytes = decompressor.getDecompressedBytes();
            System.out.println("From "+Utils.toHexString(start-3,6)+" to "+Utils.toHexString(decompressor.getEnd(),6));
            System.out.println("Header expected size "+decompressor.getHeader().getDecompressedLength());
            System.out.println("Decompressed bytes : "+Utils.bytesToHex(decompressedBytes));
            if (decompressor.getHeader().getDecompressedLength()==2048)
            {
                PrintWriter pw = new PrintWriter(new File("src/main/resources/data/jpn/chapter-start/"+s+".txt"));
                String[] split = Utils.bytesToHex(decompressedBytes).split("(?<=\\G.{192})");
                for (String s1 : split) {
                    pw.write(s1+"\n");
                    System.out.println(s1);
                }
                pw.close();
            }
            if (decompressor.getHeader().getDecompressedLength()==6592)
            {
                String[] split = Utils.bytesToHex(decompressedBytes).split("(?<=\\G.{618})");
                for (String s1 : split) {
                    System.out.println(s1);
                }

            }
            if (decompressor.getHeader().getDecompressedLength()==4608)
            {
                String[] split = Utils.bytesToHex(decompressedBytes).split("(?<=\\G.{24})");
                for (String s1 : split) {
                    System.out.println(s1);
                }

            }
            try (FileOutputStream fos = new FileOutputStream(
                    "src/main/resources/data/jpn/chapter-start/"+s+".data"
            )) {
                fos.write(decompressedBytes);
            }
        }
    }

    private void decompressTownNamesOrder() throws IOException {
        int start = Integer.parseInt("B2528", 16)+3;
        start = Integer.parseInt("BBA39", 16)+3;
        Decompressor decompressor = new Decompressor(data, start);
        decompressor.decompressData();
        byte[] decompressedBytes = decompressor.getDecompressedBytes();


        System.out.println("From "+Utils.toHexString(start-3,6)+" to "+Utils.toHexString(decompressor.getEnd(),6));
        System.out.println("Header expected size "+decompressor.getHeader().getDecompressedLength());
        System.out.println("Decompressed bytes : "+Utils.bytesToHex(decompressedBytes));


        try (FileOutputStream fos = new FileOutputStream(
                //"D:/git/dokapon-english/sprites/intro-01/english-sprite-order-01-decompressed.data"
                "D:/git/dokapon-english/sprites/town-names/sprite-decompressed-"+Utils.toHexString(start-3,6)+".data"
        )) {
            fos.write(decompressedBytes);
            fos.close();
            //There is no more need for this line since you had created the instance of "fos" inside the try. And this will automatically close the OutputStream
        }
    }

    private void decompressScore() throws IOException {
        int start = Integer.parseInt("CE92D", 16)+3;
        Decompressor decompressor = new Decompressor(data, start);
        decompressor.decompressData();
        byte[] decompressedBytes = decompressor.getDecompressedBytes();


        System.out.println();
        System.out.println("Decompressed bytes : "+Utils.bytesToHex(decompressedBytes));

        try (FileOutputStream fos = new FileOutputStream(
                //"D:/git/dokapon-english/sprites/intro-01/english-sprite-order-01-decompressed.data"
                "D:/git/dokapon-english/sprites/score/sprite-decompressed.data"
        )) {
            fos.write(decompressedBytes);
            fos.close();
            //There is no more need for this line since you had created the instance of "fos" inside the try. And this will automatically close the OutputStream
        }
    }

    private void decompressScoreOrder() throws IOException {
        int start = Integer.parseInt("CE92D", 16)+3;
        Decompressor decompressor = new Decompressor(data, start);
        decompressor.decompressData();
        byte[] decompressedBytes = decompressor.getDecompressedBytes();


        System.out.println();
        System.out.println("Decompressed bytes : "+Utils.bytesToHex(decompressedBytes));

        try (FileOutputStream fos = new FileOutputStream(
                //"D:/git/dokapon-english/sprites/intro-01/english-sprite-order-01-decompressed.data"
                "D:/git/dokapon-english/sprites/score/sprite-decompressed.data"
        )) {
            fos.write(decompressedBytes);
            fos.close();
            //There is no more need for this line since you had created the instance of "fos" inside the try. And this will automatically close the OutputStream
        }
    }

    public void cityNames() throws IOException {
        List<String> names = Files.readAllLines(Paths.get("D:/git/dokapon-english/sprites/town-names/names-02.txt"), StandardCharsets.UTF_8);
        List<City> cities = new ArrayList<>();
        for (String name : names) {
            String nameA = "";
            String nameB = "";
            if (name.length()%2==0) {
                nameA = name;
                nameB = "|"+name+"|";
            } else {
                nameA = "|" +name;
                nameB = name+"|";
            }
            String[] splitA = nameA.toUpperCase().split("(?<=\\G.{2})");
            String[] splitB = nameB.toUpperCase().split("(?<=\\G.{2})");
            City c = new City(name,
                    new HashSet<String>(Arrays.asList(splitA)),
                    new HashSet<String>(Arrays.asList(splitB))
                    );
            cities.add(c);
        }
        Map<String, Integer> pairs = new HashMap<>();
        String pattern = "";
        int best = 200;
        int loop = 0;
        String data = "";
        while (true) {
            for (City c : cities) {
                boolean b = new Random().nextBoolean();
                Set<String> setPairs;
                if (b) setPairs = c.pairsA;
                else setPairs = c.pairsB;
                for (String setPair : setPairs) {
                    if (!pairs.containsKey(setPair)) pairs.put(setPair, 1);
                    else pairs.put(setPair, pairs.get(setPair)+1);
                }
                if (b) pattern += "A";
                else pattern += "B";
            }
            if (pairs.size()<best) {
                best = pairs.size();
                System.out.println(best+"  "+pattern);
                loop = 0;
            } else {
                loop++;
            }
            if (loop==1000000) {

                System.out.println(pairs);
            }
            pattern = "";
            pairs.clear();
        }

    }

    class City {
        String name;
        Set<String> pairsA;
        Set<String> pairsB;

        public City(String name, Set<String> pairsA, Set<String> pairsB) {
            this.name = name;
            this.pairsA = pairsA;
            this.pairsB = pairsB;
        }

        @Override
        public String toString() {
            return "City{" +
                    "name='" + name + '\'' +
                    ", pairsA=" + pairsA +
                    ", pairsB=" + pairsB +
                    '}';
        }
    }

    public int countOccurrences(String haystack, String needle) {
        int count = 0, offset = 0, index;
        // As long as there is another occurrence...
        while((index = haystack.indexOf(needle, offset)) != -1) {
            // Skip already matched parts the next time
            offset = index + needle.length();
            // Increment counter
            count++;
        }
        return count;
    }
}
