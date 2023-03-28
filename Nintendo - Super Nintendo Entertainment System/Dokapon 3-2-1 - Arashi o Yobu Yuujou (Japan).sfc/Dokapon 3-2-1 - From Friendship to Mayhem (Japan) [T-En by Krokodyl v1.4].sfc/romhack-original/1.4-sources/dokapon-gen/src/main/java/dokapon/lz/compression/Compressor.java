package dokapon.lz.compression;

import dokapon.Dokapon;
import dokapon.bps.sections.Data;
import dokapon.lz.decompression.Decompressor;
import dokapon.lz.entities.*;
import dokapon.services.Utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Compressor {

    Header header;

    byte[] data;
    String dataHex;

    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

    /*public Compressor(byte[] data) {
        this(data, new Header(
                (byte) Integer.parseInt("00",16),
                (byte) Integer.parseInt("10",16),
                (byte) Integer.parseInt("05",16)
        ));
    }*/

    public Compressor(byte[] data, Header header) {
        this.header = header;
        this.data = data;
    }

    public static void main(String[] args) throws IOException {

        Compressor compressor = null;
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

        try {
            //byte[] data = Files.readAllBytes(new File("D:/git/dokapon-english/sprites/intro-01/intro-quote-uncompressed.data").toPath());
            byte[] data = Files.readAllBytes(new File("D:/git/dokapon-english/sprites/intro-01/intro-quote-uncompressed.data").toPath());
            data = Files.readAllBytes(new File("D:/git/dokapon-english/sprites/intro-01/english-sprite-text-01-uncompressed.data").toPath());

            compressor = new Compressor(data, headerSprites);
            compressor.compress();

            System.out.println();
            System.out.println("Compressed length : "+ compressor.getCompressedBytes().length);

        } catch (IOException ex) {
            Logger.getLogger(Dokapon.class.getName()).log(Level.SEVERE, null, ex);
        }


        //treeBuilder.test(Utils.bytesToHex(decompressedBytes).replaceAll(" ",""));

        byte[] compressedBytes = compressor.getCompressedBytes();
        System.out.println("Compressed bytes : "+Utils.bytesToHex(compressedBytes));
        Decompressor decompressor = new Decompressor(compressedBytes, 3);
        decompressor.decompress();
        byte[] decompressedBytes = decompressor.getDecompressedBytes();

        System.out.println();
        System.out.println("Compressed bytes : "+Utils.bytesToHex(compressedBytes));
        System.out.println("Decompressed bytes : "+Utils.bytesToHex(decompressedBytes));

        try (FileOutputStream fos = new FileOutputStream(
                //"D:/git/dokapon-english/sprites/intro-01/english-sprite-order-01-decompressed.data"
                "D:/git/dokapon-english/sprites/intro-01/english-sprite-text-01-decompressed.data"
        )) {
            fos.write(decompressedBytes);
            fos.close();
            //There is no more need for this line since you had created the instance of "fos" inside the try. And this will automatically close the OutputStream
        }

    }



    public void compress() throws IOException {
        dataHex = Utils.bytesToHex(data).replaceAll(" ","");
        System.out.println("Compressing : "+dataHex);
        Dokapon.getTime();
        Node root = new Node(dataHex, null);
        List<Node> untreatedNodes = root.getUntreatedNodes();
        String bestPattern = getBestPattern(untreatedNodes);
        //String[] bestPatterns = getBestPatterns(untreatedNodes);
        while (bestPattern!=null && !untreatedNodes.isEmpty()) {
            //for (String bestPattern:bestPatterns) root.split(bestPattern);
            root.split(bestPattern);
            //root.println();
            untreatedNodes = root.getUntreatedNodes();
            //bestPatterns = getBestPatterns(untreatedNodes);
            bestPattern = getBestPattern(untreatedNodes);
        }
        // Update Repeat
        Map<String, Integer> patternOffset = new HashMap<>();
        root.updateRepeat(patternOffset);
        System.out.println();
        System.out.println(Dokapon.getTime());
        //root.println();
        checkTree(dataHex, root);
        System.out.println("dataSize="+dataHex.length());
        getCompressedSize(root);
        List<CompressedByte> compressedBytes = root.getCompressedByte(header);

        int offset = 0;
        for (CompressedByte compressedByte : compressedBytes) {
            System.out.println(offset+" "+compressedByte);
            if (compressedByte instanceof DataByte) offset++;
            else if (compressedByte instanceof RepeatByte) {
                RepeatByte rb = (RepeatByte) compressedByte;
                offset += rb.getSize();
            }
        }

        outputStream.write(header.getBytes());

        CompressedByte[] buffer = new CompressedByte[8];
        int k = 0;
        for (CompressedByte compressedByte : compressedBytes) {
            buffer[k++] = compressedByte;
            if (k==buffer.length) {
                String bits = "";
                for (CompressedByte cb:buffer) {
                    if (cb == null || cb instanceof DataByte) bits = "1" + bits;
                    else bits = "0" + bits;
                }
                FlagByte f = new FlagByte((byte) (Integer.parseInt(bits,2) & 0xFF));
                System.out.print(f.getBytesHex()+" ");
                outputStream.write(f.getBytes());
                for (CompressedByte cb:buffer) {
                    System.out.print(cb.getBytesHex()+" ");
                    outputStream.write(cb.getBytes());
                }
                k=0;
                buffer = new CompressedByte[8];
            }
        }
        if (k!=0) {
            String bits = "";
            for (CompressedByte cb:buffer) {
                if (cb == null || cb instanceof DataByte) bits = "1" + bits;
                else bits = "0" + bits;
            }
            FlagByte f = new FlagByte((byte) (Integer.parseInt(bits,2) & 0xFF));
            System.out.print(f.getBytesHex()+" ");
            outputStream.write(f.getBytes());
            for (CompressedByte cb:buffer)
                if (cb!=null) {
                    System.out.print(cb.getBytesHex()+" ");
                    outputStream.write(cb.getBytes());
                }
        }

    }

    public byte[] getCompressedBytes() {
        return outputStream.toByteArray();
    }

    public int getCompressedSize(Node root) {
        int repeatCount = root.countType(NodeType.REPEAT);
        int untreatedSize = root.getTreeSize(NodeType.UNTREATED);
        System.out.println("repeatCount="+repeatCount);
        System.out.println("untreatedSize="+untreatedSize);
        System.out.println("flagCount="+(repeatCount+untreatedSize)/8);
        System.out.println("compressedSize="+((root.getTreeSize())+(repeatCount+untreatedSize)/8));
        return root.getTreeSize();
    }

    public void checkTree(String data, Node root) {
        String treeData = root.getTreeData();
        System.out.println("Check Tree Data : "+treeData.equals(data));
    }

    public String[] getBestPatterns(List<Node> nodes) {
        List<Tuple> tuples = new ArrayList<>();

        String data = "";
        for (Node node : nodes) {
            data += node.getData() + "  ";
        }

        int length = 32;

        //Set<String> patterns = new HashSet<>();
        Set<CompressionPattern> bests = new TreeSet<>();
        while (length>=2) {
            for (int i = 0; i < data.length() - length*2; i = i + 2) {
                String substring = data.substring(i, i + length*2);
                if (!substring.contains(" ")) {
                    //patterns.add(substring);
                    CompressionPattern compressionPattern = createCompressionPattern(data, substring);
                    if (compressionPattern.getCompressionGain()>0) bests.add(compressionPattern);
                }
            }
            length--;
        }

        if (bests.isEmpty()) return null;
        //System.out.println("best.getPattern() "+best.getPattern());
        System.out.println(Dokapon.getTime()/1000+"s");
        Set<String> patterns = new HashSet<>();
        for (CompressionPattern best : bests) {
            patterns.add(best.getPattern());
        }
        return patterns.toArray(new String[0]);
        //return bests.getPattern();
    }

    public String getBestPattern(List<Node> nodes) {
        List<Tuple> tuples = new ArrayList<>();

        String data = "";
        for (Node node : nodes) {
            data += node.getData() + "  ";
        }

        int length = 32;

        //Set<String> patterns = new HashSet<>();
        CompressionPattern best = null;
        while (length>=2) {
            for (int i = 0; i < data.length() - length*2; i = i + 2) {
                String substring = data.substring(i, i + length*2);
                if (!substring.contains(" ")) {
                    //patterns.add(substring);
                    CompressionPattern compressionPattern = createCompressionPattern(data, substring);
                    if (best == null) best = compressionPattern;
                    else {
                        if (compressionPattern.getCompressionGain()>best.getCompressionGain()) best = compressionPattern;
                    }
                }
            }
            if (header.isOptionStopFirstPattern() && best!=null) {
               //System.out.println("best "+length);
                return best.getPattern();
            }
            length--;
        }

        if (best==null) return null;
        System.out.println("best.getPattern() "+best.getPattern());
        System.out.println(Dokapon.getTime()/1000+"s");

        return best.getPattern();
    }

    public CompressionPattern createCompressionPattern(String data, String pattern) {
        CompressionPattern compressionPattern = new CompressionPattern(pattern);

        int[] countPattern = countPattern(data, pattern);
        int i = countPattern[0];
        int c = countPattern[1];

        compressionPattern.setCountConsecutive(i);
        compressionPattern.setCountNonOverlapping(c);

        return compressionPattern;
    }

    static class Tuple {
        String pattern;
        int maxCount;
        int count;
        int byteCompressed;

        public Tuple(String pattern, int maxCount, int count, int byteCompressed) {
            this.pattern = pattern;
            this.maxCount = maxCount;
            this.count = count;
            this.byteCompressed = byteCompressed;
        }

        public String getPattern() {
            return pattern;
        }

        public void setPattern(String pattern) {
            this.pattern = pattern;
        }

        public int getMaxCount() {
            return maxCount;
        }

        public void setMaxCount(int maxCount) {
            this.maxCount = maxCount;
        }

        public int getByteCompressed() {
            return byteCompressed;
        }

        public void setByteCompressed(int byteCompressed) {
            this.byteCompressed = byteCompressed;
        }

        @Override
        public String toString() {
            return "Tuple{" +
                    "pattern='" + pattern + '\'' +
                    ", maxCount=" + maxCount +
                    ", count=" + count +
                    ", byteCompressed=" + byteCompressed +
                    '}';
        }
    }

    public static void testAlgoSplit() throws IOException {
        byte[] encoded = Files.readAllBytes(Paths.get("../font/uncompressed-latin-bytes.txt"));
        String data = new String(encoded, StandardCharsets.US_ASCII);
        //String data = "0000000099FF99FF99FFB1B1B1B1B1B199FF99FFAAAA00000000B1B1B1";
        int length = 17;

        List<Tuple> tuples = new ArrayList<>();
        while (length>3*2) {
            List<String> patterns = new ArrayList<>();
            for (int i = 0; i < data.length() - length*2; i = i + 2) {
                String substring = data.substring(i, i + length*2);
                //System.out.println(data.substring(i,j));
                if (!patterns.contains(substring)) patterns.add(substring);
            }

            for (String s : patterns) {
                if (s.equals("FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF")) {
                    System.out.println();
                }
                int[] countPattern = countPattern(data, s);
                int i = countPattern[0];
                int c = countPattern[1];
                //System.out.println(i + " " + s);
                if (c>1) {
                    int compression = (s.length() / 2) * (c - 1) - 2 * (c - 1);
                    Tuple t = new Tuple(s, i, c, compression);
                    tuples.add(t);
                }
            }
            length--;
        }
        Collections.sort(tuples, new Comparator<Tuple>() {
            @Override
            public int compare(Tuple o1, Tuple o2) {
                return o2.getByteCompressed()-o1.getByteCompressed();
            }
        });
        int totalBytesCompressed = 0;
        for (Tuple t:tuples) {
            //System.out.println("pattern "+t.getPattern());
            //for (String s:split) System.out.println(s);
            System.out.println(t);
            totalBytesCompressed += t.getByteCompressed();
        }

        System.out.println("bytes = "+data.length()/2);
        System.out.println("totalBytesCompressed = "+totalBytesCompressed);

        System.out.println();
    }

    public static int[] countPattern(String data, String pattern) {
        int lastIndex = data.indexOf(pattern);
        int index = data.indexOf(pattern, lastIndex+pattern.length());
        int maxCount = 1;
        int count = 1;
        int fullCount = 1;
        while (index>0) {
            if (index-pattern.length()==lastIndex) {
                count++;
                fullCount++;
                if (maxCount<count) maxCount = count;
            } else {
                count=1;
            }
            lastIndex = index;
            index = data.indexOf(pattern, lastIndex+pattern.length());
        }
        int[] res = new int[2];
        res[0] = count;
        res[1] = fullCount;
        return res;
    }

    /*private static int countMaxConsecutivePattern(String data, String pattern) {
        int lastIndex = data.indexOf(pattern);
        int index = data.indexOf(pattern, lastIndex+pattern.length());
        int maxCount = 1;
        int count = 1;
        while (index>0) {
            if (index-pattern.length()==lastIndex) {
                count++;
                if (maxCount<count) maxCount = count;
            } else {
                count=1;
            }
            lastIndex = index;
            index = data.indexOf(pattern, lastIndex+pattern.length());
        }
        return maxCount;
    }

    private static int countNonOverlappingPattern(String data, String pattern) {
        int lastIndex = data.indexOf(pattern);
        int index = data.indexOf(pattern, lastIndex+pattern.length());
        int count = 1;
        while (index>0) {
            if (index-pattern.length()>=lastIndex) {
                count++;
            }
            lastIndex = index;
            index = data.indexOf(pattern, lastIndex+pattern.length());
        }
        return count;
    }*/
}
