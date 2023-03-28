package dokapon.sprites;

import dokapon.entities.TownSign;
import dokapon.enums.Sprite;
import dokapon.services.Utils;

import javax.xml.bind.DatatypeConverter;
import java.io.*;
import java.nio.file.Files;
import java.util.*;

public class SpriteMaker {

    byte[] data;

    String[] names = {
            "TOKYO]",
            "(SHANGHAI)",
            "[MACAU]-",
            "[HARBIN]",
            "(IRKUTSK)-",
            "-[MOSCOW]-",
            "(ASTANA)",
            "(LHASA)-",
            "[KABUL]-",
            "(AMMAN}d",
            "-(SEVILLA)",
            "[ROME]",
            "(PARIS)-",
            "[VIENNA]",
            "(PRAGUE)",
            "-[RIGA]---",
            "(LONDON)",
            "(TALLINNyt",
            "[POZNAŃ]",
            "(AKUREYRI)wxcv",
            "[NUUK]",
            "[DUBLIN]",
            "[OSLO]--",
            "(DERRY)-",
            "(SIDNEY)",
            "[ADELAIDE]",
            "[PERTH]-",
            "[ALICE]-",
            "(CAIRNS)",
            "(AUCKLAND)",
            "-[ULURU]",
            "jkPRETORIAlm",
            "-(RABAT)",
            "[CAIRO]-",
            "bnNIAMEY,;",
            "-(BAMAKO)-",
            "[LOME]--",
            "-(NAIROBI)",
            "[BOGOTA]",
            "-(CARACAS)",
            "-[MANAUS]-",
            "(LIMA)",
            "[LA_PAZ]",
            "(BRASILIA)",
            "-[RIO]--",
            "(SANTIAGO)",
            "[JUNEAU]",
            "(SEATTLE)-",
            "-(PORTLANDyt",
            "[DENVER]",
            "(DALLAS)",
            "(NEW_YORK)",
            "[HOUSTON]-",
            "(ANAHEIM)-"
    };

    Set<Integer> skip = new HashSet<>(Arrays.asList(
            Integer.parseInt("40",16),
            Integer.parseInt("46",16),
            Integer.parseInt("4A",16),
            Integer.parseInt("57",16),
            Integer.parseInt("79",16),
            Integer.parseInt("7A",16),
            Integer.parseInt("85",16),
            Integer.parseInt("BA",16),
            Integer.parseInt("BB",16),
            Integer.parseInt("BC",16),
            Integer.parseInt("C0",16),
            Integer.parseInt("D2",16),
            Integer.parseInt("D8",16),
            Integer.parseInt("E0",16),
            Integer.parseInt("E1",16),
            Integer.parseInt("E2",16),
            Integer.parseInt("E3",16),
            Integer.parseInt("E4",16),
            Integer.parseInt("E5",16),
            Integer.parseInt("E6",16),
            Integer.parseInt("E7",16),
            Integer.parseInt("E8",16)
    ));

    byte[] skipValue = {
            0, 0, 0, 0, 0, 0, 0, 0
    };

    Map<String, String> pairValue = new HashMap<>();

    public SpriteMaker(byte[] data) {
        this.data = data;
    }

    public static void main(String[] args)
    {
        /*int value = Integer.parseInt("40",16);
        while (value<Integer.parseInt("E0",16)) {
            for (int k = 0; k < 16; k++) {
                System.out.print(
                        String.format(
                                "%s %s ",
                                Utils.toHexString(value++),
                                "00"
                        )
                );
            }
            System.out.println();
        }*/
        //new SpriteMaker(null).generateJson();

    }

    public Set<String> printMap(String sourceFile) throws IOException {
        byte[] data = Files.readAllBytes(new File(sourceFile).toPath());
        PrintWriter pw = new PrintWriter(new File("src/main/resources/data/jpn/map.txt"));
        int start = 4;
        int index = 0;
        Set<String> goodValues = new TreeSet<>();
        Set<String> badValues = new TreeSet<>();
        List<String> removed = new ArrayList<String>(Arrays.asList(
                "5203", "7503", "7603", "7703", "4503",
                "5202", "6903", "6A03", "6B03"
        ));
        List<String> empty = new ArrayList<String>(Arrays.asList("0200 ",
                "0300 ",
                "0A00 ",
                "0800 ",
                "0500 ",
                "0400 "));
        List<String> vpaths = new ArrayList<String>(Arrays.asList(
                "1100 ", "EA00 ", "D900 ", "EB00 ", "A601 ", "FE00 ", "BE00 ", "9801 ", "5900 ", "FF00 ",
                "7300 ", "5D00 ", "5C00 ", "7D00 ", "9900 ", "8100 ", "5100 ", "4C00 ", "4D00 ", "D000 ",
                "9700 ", "FA00 ", "A400 ", "A500 ", "F900 ", "B500 ", "AD00 ", "FB00 ", "A701 ", "9D01 ",
                "4300 ", "9600 "
        ));
        List<String> hpaths = new ArrayList<String>(Arrays.asList(
                "9A01 ", "6500 ", "1000 ", "DA00 ", "EC00 ", "ED00 ", "5B00 ", "5800 ", "5E00 ", "BF00 ",
                "FC00 ", "1D00 ", "1E00 ", "9E01 ", "9F01 ", "5A00 ", "9B01 ", "FD00 ", "8300 ", "8400 ",
                "8600 ", "7400 ", "4900 ", "4E00 ", "5600 ", "9D00 ", "D100 ", "6200 ", "A000 ", "9F00 ",
                "6300 ", "9800 ", "BD00 ", "F000 ", "8A00 ", "9E00 "
        ));
        List<String> crossroads = new ArrayList<String>(Arrays.asList(
                "1200 ", "D800 ", "5700 ", "C000 ","8500 ", "4A00 ", "D200 ", "BC00 "
        ));
        List<String> chests = new ArrayList<String>(Arrays.asList(
                "1900 ", "1A00 ", "A201 ", "A101 ", "1800 ", "A001 "
        ));
        List<String> towns = new ArrayList<String>(Arrays.asList(
                "EE01 ", "ED01 ", "E901 ", "EA01 ", "EF01 ", "EC01 ", "E801 ",
                "E601 ", "E701 ", "E501 ", "F401 ", "E000 ", "EB01 ", "E201 ",
                "E401 ", "E100 ", "F601 ", "F501 ", "F701 ", "D901 ", "DA01 ",
                "DB01 ", "DC01 ", "E001 ", "F101 ", "F001 ", "F301 ", "F201 ",
                "DF01 ", "DE01 ", "DD01 ", "D601 ", "D801 ", "D501 ", "2D00 ",
                "D301 ", "D201 ", "D101 ", "D401 ", "D701 ", "E400 ", "E300 ",
                "E200 ", "E700 ", "E600 ", "E500 "
        ));
        List<String> castles = new ArrayList<String>(Arrays.asList(
                "B901 ", "BB01 ", "B801 ", "BA01 ", "B701 ", "3E00 ", "3000 ", "BC01 "
        ));
        List<String> churches = new ArrayList<String>(Arrays.asList(
                "BE01 ", "C001 ", "BD01 ", "BF01 ", "1B00 ", "C101 "
        ));
        List<String> entrances = new ArrayList<String>(Arrays.asList(
                "1A01 ", "4000 ", "3800 ", "B301 ", "B001 ", "B401 ", "B501 ", "B201 ", "B101 ", "BA00 ",
                "3B00 ", "B601 ", "9701 "
        ));
        List<String> shops = new ArrayList<String>(Arrays.asList(
                "C501 ", "CA01 ", "CE01 ", "C401 ", "CD01 ", "C901 ", "C601 ", "CF01 ",
                "CC01 ", "C301 ", "C801 ", "4600 ", "3900 ", "2C00 ", "C201 ", "CB01 ",
                "C701 ", "D001 "
        ));
        List<String> tax = new ArrayList<String>(Arrays.asList(
                "7900 ", "7A00 ", "A301 "
        ));
        for (String s:vpaths) badValues.add(s.substring(0,2));
        for (String s:hpaths) badValues.add(s.substring(0,2));
        for (String s:crossroads) badValues.add(s.substring(0,2));
        for (String s:chests) badValues.add(s.substring(0,2));
        for (String s:towns) badValues.add(s.substring(0,2));
        for (String s:castles) badValues.add(s.substring(0,2));
        for (String s:churches) badValues.add(s.substring(0,2));
        for (String s:entrances) badValues.add(s.substring(0,2));
        for (String s:shops) badValues.add(s.substring(0,2));
        for (String s:tax) badValues.add(s.substring(0,2));
        for (String s:removed) badValues.add(s.substring(0,2));

        String pair = "";
        for (byte b:data) {
            if ((index-start)%(Integer.parseInt("100",16))==0) {
                //System.out.println();
                pw.println("");
            }
            if (index>=start) {
                if (index%2==0) pair += (Utils.toHexString(b));
                else pair += (Utils.toHexString(b)+" ");
            }
            if (pair.length()==5) {
                String code = pair;
                if (empty.contains(pair)) {
                    pair = ("     ");
                } else if (vpaths.contains(pair)) {
                    pair = (" ||  ");
                } else if (hpaths.contains(pair)) {
                    pair = ("---- ");
                } else if (crossroads.contains(pair)) {
                    pair = ("(  ) ");
                } else if (chests.contains(pair)) {
                    pair = (" []  ");
                } else if (towns.contains(pair)) {
                    pair = (" ##  ");
                } else if (entrances.contains(pair)) {
                    pair = (" @@  ");
                } else if (shops.contains(pair)) {
                    pair = (" $$  ");
                } else if (tax.contains(pair)) {
                    pair = (" ££  ");
                } else if (churches.contains(pair)) {
                    pair = (" ++  ");
                } else if (castles.contains(pair)) {
                    pair = (" /\\  ");
                }
                //System.out.print(pair);
                if (
                        !badValues.contains(code.substring(0,2))
                ) {
                    //goodValues.add(code.substring(0,2));
                }
                pw.print(pair);
                pair="";
            }
            index++;
        }
        int count = 0;
        for (int k=Integer.parseInt("40",16);k<=Integer.parseInt("FF",16);k++) {
            if (!badValues.contains(Utils.toHexString(k))) {
                count++;
                goodValues.add(Utils.toHexString(k));
            }
        }
        System.out.println("Good values "+goodValues.size());
        System.out.println("Count "+count);
        pw.close();
        return goodValues;
    }

    public void generateSigns(byte[] data, List<TownSign> signs, String filename) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        int index = 0;
        //int value = Integer.parseInt("40",16);
        //int mutliplier = Integer.parseInt("200",16);
        int offset = 0;

        Set<String> goodValues = printMap("src/main/resources/data/jpn/BBA39.data");
        LinkedList<String> acceptableCodes = new LinkedList<>();
        for (String s:goodValues) acceptableCodes.offerLast(s+"02");
        for (String s:goodValues) acceptableCodes.offerLast(s+"03");
        for (String s:goodValues) acceptableCodes.offerLast(s+"04");

        for (TownSign sign : signs) {
            String name = sign.getValue();
            String[] pairs = name.split("(?<=\\G.{2})");
            for (String pair:pairs) {
                if (!pairValue.containsKey(pair)) {
                    Sprite left = Sprite.getSprite(pair.charAt(0) + "");
                    Sprite right = Sprite.getSprite(pair.charAt(1) + "");
                    String encodedSprite = String.format("%s %s %s %s",
                            left.getValueTop(),
                            right.getValueTop(),
                            left.getValueBot(),
                            right.getValueBot()
                    );
                    String code = acceptableCodes.pollFirst();
                    /* 4102
                    8
                     */
                    String hex = code.substring(2) + code.substring(0, 2);
                    int i = Integer.parseInt(hex, 16);
                    i = i << 3;
                    i += Integer.parseInt("4DDC", 16);
                    i -= Integer.parseInt("5FDC", 16);

                    while (index<i) {
                        outputStream.write(skipValue);
                        index += 8;
                        //value++;
                    }
                    byte[] bytes = DatatypeConverter.parseHexBinary(encodedSprite.replaceAll(" ", ""));
                    outputStream.write(bytes);
                    index += 8;
                    //System.out.println(pair + "\t" + code);
                    pairValue.put(pair, code);
                    //value++;
                } else {
                    String s = pairValue.get(pair);
                    //System.out.println(pair + "\t" + s);
                }
            }
        }
        Files.write(new File(filename).toPath(), outputStream.toByteArray());
        //String suffix = "02";
        for (TownSign sign : signs) {
            String name = sign.getValue();
            String[] pairs = name.split("(?<=\\G.{2})");
            String hex = "";
            for (String pair : pairs) {
                String code = pairValue.get(pair);
                hex += code +" ";
                //hex += "02 00 ";
            }
            //System.out.println(String.format("%-12s",name)+"\t\t"+hex);
            sign.setHex(hex.trim());


        }
    }
/*
    public void generateDataFile(String filename) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        int index = 100;
        int value = Integer.parseInt("40",16);
        for (String name:names) {
            String[] pairs = name.split("(?<=\\G.{2})");
            for (String pair:pairs) {
                if (!pairValue.containsKey(pair)) {
                    Sprite left = Sprite.getSprite(pair.charAt(0) + "");
                    Sprite right = Sprite.getSprite(pair.charAt(1) + "");
                    String encodedSprite = String.format("%s %s %s %s",
                            left.getValueTop(),
                            right.getValueTop(),
                            left.getValueBot(),
                            right.getValueBot()
                    );
                    //System.out.println(pair +" "+encodedSprite);
                    while (skip.contains(value)) {
                        outputStream.write(skipValue);
                        index += 8;
                        value++;
                    }
                    byte[] bytes = DatatypeConverter.parseHexBinary(encodedSprite.replaceAll(" ", ""));
                    outputStream.write(bytes);
                    index += 8;
                    System.out.println(pair + "\t" + Utils.toHexString(value));
                    pairValue.put(pair, value);
                    value++;
                } else {
                    Integer integer = pairValue.get(pair);
                    System.out.println(pair + "\t" + Utils.toHexString(integer));
                }
            }

        }
        Files.write(new File(filename).toPath(), outputStream.toByteArray());
        for (String name:names) {
            String[] pairs = name.split("(?<=\\G.{2})");
            String hex = "";
            for (String pair : pairs) {
                Integer integer = pairValue.get(pair);
                hex += Utils.toHexString(integer) + " 02 ";
            }
            System.out.println(String.format("%-12s",name)+"\t\t"+hex);
        }
    }*/

    private void generateJson() {
        String offsets = "TOKYO;35DC\n" +
                "SHANGHAI;33C0\n" +
                "MACAU;3dba\n" +
                "HARBIN;19e0\n" +
                "IRKUTSK;0CEA\n" +
                "MOSCOW;18AE\n" +
                "ASTANA;26AC\n" +
                "LHASA;30B2\n" +
                "KABUL;359E\n" +
                "AMMAN;3C8C\n" +
                "SEVILLA;3A64\n" +
                "ROME;317E\n" +
                "PARIS;2674\n" +
                "VIENNA;2A92\n" +
                "PRAGUE;2596\n" +
                "RIGA;1C80\n" +
                "LONDON;2064\n" +
                "TALLINN;1486\n" +
                "POZNAN;1C96\n" +
                "AKUREYRI;0948\n" +
                "NUUK;1254\n" +
                "DUBLIN;0F78\n" +
                "OSLO;0976\n" +
                "DERRY;0D6C\n" +
                "SIDNEY;67D8\n" +
                "ADELAIDE;63CC\n" +
                "PERTH;66A8\n" +
                "ALICE;4FC8\n" +
                "CAIRNS;53E0\n" +
                "AUCKLAND;67EA\n" +
                "ULURU;56C8\n" +
                "BAMAKO;4F60\n" +
                "LOME;5676\n" +
                "CAIRO;4584\n" +
                "RABAT;4662\n" +
                "NAIROBI;4E8C\n" +
                "NIAMEY;4E6C\n" +
                "PRETORIA;6670\n" +
                "BOGOTA;471A\n" +
                "CARACAS;472C\n" +
                "MANAUS;4D20\n" +
                "LIMA;5418\n" +
                "LA PAZ;5622\n" +
                "BRASILIA;4F30\n" +
                "RIO;4F40\n" +
                "SANTIAGO;621C\n" +
                "JUNEAU;0A16\n" +
                "SEATTLE;1D12\n" +
                "PORTLAND;2916\n" +
                "DENVER;1C30\n" +
                "DALLAS;2130\n" +
                "NEW YORK;2748\n" +
                "HOUSTON;332C\n" +
                "ANAHEIM;3018";
        TreeMap<Integer, String> offs = new TreeMap<>();
        String[] split = offsets.split("\n");
        for (String s : split) {
            if (s.trim().contains(";")) {
                String[] pair = s.split(";");
                offs.put(Integer.parseInt(pair[1],16), pair[0]);
            }
        }
        for (Map.Entry<Integer, String> e : offs.entrySet()) {
            String value = "";
            for (String name:names) {
                if (name.contains(e.getValue())) value = name;
            }
            System.out.println(
String.format(
                    "{\n" +
                            "      \"offset\": \"%s\",\n" +
                            "      \"town\": \"%s\",\n" +
                            "      \"value\": \"%s\"\n" +
                            "    },", Utils.toHexString(e.getKey(),4)
        , e.getValue(),
        value)
            );
            //System.out.println(e);
        }


    }

    public void generateMapOrder(List<TownSign> townSigns, String sourceFile, String targetFile) throws IOException {
        byte[] data = Files.readAllBytes(new File(sourceFile).toPath());
        int keep = 100;
        for (TownSign townSign : townSigns) {
            if (
                    keep-->0
            )
            {
                int offset = townSign.getOffset();
                String hex = townSign.getHex();
                byte[] bytes = DatatypeConverter.parseHexBinary(hex.replaceAll(" ", ""));
                for (byte b : bytes) {
                    /*if (offset%2==0) data[offset++] = 11;
                    else data[offset++] = 0;*/
                    data[offset++] = b;
                }
                //System.out.println(townSign.getTown());
            }
        }
        Files.write(new File(targetFile).toPath(), data);
    }
}
