package dokapon.services;

import dokapon.Constants;
import dokapon.Dokapon;
import dokapon.entities.PointerData;
import dokapon.entities.PointerRange;
import dokapon.entities.PointerTable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static dokapon.Constants.END_OF_LINE_CHARACTER_HEXA;
import static dokapon.Constants.LENGTH_DIALOG_LINE;

public class DataReader {

    public static PointerTable readTable(PointerTable table, byte[] data) {
        for (PointerRange range : table.getRanges()) {
            if (!table.isCounter()) {
                for (int i = range.getStart(); i <= range.getEnd(); i = i + 2) {
                    PointerData p = new PointerData();
                    int a = (data[i] & 0xFF);
                    int b = (data[i + 1] & 0xFF);
                    int c = b * 256 + a;
                    p.setValue(c);
                    p.setOffset(i);
                    String[] readData = readPointerData(c + range.getShift(), data);
                    p.setData(readData);
                    p.setOffsetData(c + range.getShift());
                    table.addPointerDataJap(p);
                }
            } else {
                int i = range.getStart();
                while (i <= range.getEnd()) {
                    int nb = data[i];
                    for (int k = 1; k <= nb * 2; k = k + 2) {
                        PointerData p = new PointerData();
                        int a = (data[i + k] & 0xFF);
                        int b = (data[i + k + 1] & 0xFF);
                        int c = b * 256 + a;
                        p.setValue(c);
                        p.setOffset(i + k);
                        String[] readData = readPointerData(c + range.getShift(), data);
                        p.setData(readData);
                        p.setOffsetData(c + range.getShift());
                        table.addPointerDataJap(p);
                    }
                    i = i + 1 + nb * 2;
                }
            }
        }
        return table;
    }

    public static PointerTable generateEnglish(Translator translator, PointerTable table, byte[] data) {
        System.out.println("Generate English for table "+table.getId());
        Map<Integer, Integer> mapValues = new HashMap<Integer, Integer>();
        Map<String, Integer> mapLegnths = new HashMap<String, Integer>();
        mapLegnths.put("ODD", 0);
        mapLegnths.put("EVEN", 0);
        int tableDataLength = 0;
        int offsetData = table.getNewDataStart();
        int newDataShift = table.getNewDataShift();
        if (table.getNewDataStart() == 0) return table;
        boolean overflowActive = false;
        for (PointerData p : table.getDataJap()) {
            if (!overflowActive && table.hasOverflow() && offsetData > table.getOverflow().getLimit()) {
                offsetData = table.getOverflow().getDataStart();
                newDataShift = table.getOverflow().getDataShift();
                overflowActive = true;
            }
            PointerData newP = new PointerData();
            newP.setOldPointer(p);
            String[] translation = translator.getTranslation(p, table.isEvenLength());
            if (table.getId()==6) {
                String english = translator.getEnglish(p);
                //System.out.println(english.length()+" "+english);
                if (p.getData().length != translation.length) {
                    System.out.println("pb:"+p.getData().length +"-"+ translation.length);
                }
                String[] split = english.replace("\\{EL\\}","").split("\\{NL\\}");
                //for (String s:split) System.out.println(s.length()+" "+s);


            }
            if (translation != null && translation.length % 2 == 0) mapLegnths.put("EVEN", mapLegnths.get("EVEN") + 1);
            else {
                mapLegnths.put("ODD", mapLegnths.get("ODD") + 1);
            }
            String[] menuData = translator.getMenuData(p);
            if (translation != null && translation.length > 0) {
                newP.setData(translation);
            } else {
                newP.setData(p.getData());
            }
            if (menuData != null) {
                newP.setMenuData(menuData);
            }
            newP.setOffset(p.getOffset());
            newP.setOffsetData(offsetData);
            newP.setOffsetOldMenuData(p.getOffsetData());
            int oldValue = p.getValue();
            if (!mapValues.containsKey(oldValue)) {
                int value = offsetData - newDataShift;
                newP.setValue(value);
                mapValues.put(oldValue, value);
                double l = newP.getData().length;
                tableDataLength += l;
                int longueur = (int) l * 2;
                if (table.getId() == 4) {
                    longueur = (int) ((Math.ceil(l / 8)) * 16);
                    if (longueur % 32 == 16) longueur += 16;
                }
                offsetData += longueur;
                if (table.getId() == 5) {
                    if (offsetData % 16 == 0) offsetData += 2;
                }
            } else {
                newP.setValue(mapValues.get(oldValue));
            }
            table.addPointerDataEng(newP);
        }
        /*System.out.println("TABLE "+table.getId());
        System.out.println("TABLE DATA LENGTH "+tableDataLength);
        for (Map.Entry<Integer, Integer> e : mapValues.entrySet()) {
            System.out.println(Integer.toHexString(e.getKey())+" -> "+Integer.toHexString(e.getValue()));
        }
        System.out.println("EVEN="+mapLegnths.get("EVEN"));
        System.out.println("ODD="+mapLegnths.get("ODD"));*/
        return table;
    }

    private static String[] readPointerData(int offset, byte[] data) {
        boolean end = false;
        List<String> res = new ArrayList<String>();
        int i = offset;
        while (!end) {
            int a = (data[i] & 0xFF);
            int b = (data[i + 1] & 0xFF);
            String s = Integer.toHexString(b);
            s = Utils.padLeft(s, '0', 2);
            s = Integer.toHexString(a) + s;
            s = Utils.padLeft(s, '0', 4);
            if (s.equals(END_OF_LINE_CHARACTER_HEXA)) {
                end = true;
            }
            res.add(s);
            i = i + 2;
        }
        return res.toArray(new String[0]);
    }

    public static void generateCredits() throws IOException {
        List<String> nicknames = Arrays.asList(
                "Krokodyl",
                "everyone",
                "Porsche",
                "Tired",
                "Horse",
                "Opti",
                "Dual-Use",
                "Newly-wed",
                "Messenger",
                "Host",
                "Brave");
        String finalLine = "";
        BufferedReader br = new BufferedReader(
                new InputStreamReader(
                        Objects.requireNonNull(Translator.class.getClassLoader().getResourceAsStream("tables/credits.txt")), StandardCharsets.UTF_8));
        String line = br.readLine();
        while (line != null) {
            String res = "";
            boolean skip = false;
            for (char c : line.toCharArray()) {
                if (c == '{') {
                    skip = true;
                } else if (c == '}') {
                    skip = false;
                } else {
                    if (!skip) res += c;
                }
            }
            res = res.trim();
            if (res.length() < LENGTH_DIALOG_LINE) {
                int i = LENGTH_DIALOG_LINE - res.length();
                int left = i / 2;
                int right = i - left;
                res = Utils.padLeft(res, ' ', res.length() + left);
                res = Utils.padRight(res, ' ', res.length() + right);
            }
            for (String name : nicknames) {
                res = res.replace(name, "{CG}" + name + "{CW}");
            }
            finalLine += res + "{NL}";
            line = br.readLine();
        }
        System.out.println(finalLine + "{EL}");
    }

    public static PointerTable generateEnglishPointer(Translator translator, PointerTable table, byte[] data) {
        Map<Integer, Integer> mapValues = new HashMap<Integer, Integer>();
        int offsetData = table.getNewDataStart();
        if (table.getNewDataStart() == 0) return table;
        for (PointerData p : table.getDataJap()) {
            PointerData newP = new PointerData();
            newP.setOldPointer(p);
            newP.setOffset(p.getOffset());
            newP.setOffsetData(offsetData);
            int value = offsetData - table.getNewDataShift();
            newP.setValue(value);
            String[] translation = translator.getTranslationPointer(newP, table);
            String[] menuData = translator.getMenuData(p);
            if (translation != null && translation.length > 0) {
                newP.setData(translation);
            } else {
                newP.setData(p.getData());
            }
            if (menuData != null) {
                newP.setMenuData(menuData);
            }
            newP.setOffsetOldMenuData(p.getOffsetData());
            int oldValue = p.getValue();
            if (!mapValues.containsKey(oldValue)) {
                value = offsetData - table.getNewDataShift();
                newP.setValue(value);
                mapValues.put(oldValue, value);
                double l = newP.getData().length;
                int longueur = (int) l * 2;
                if (table.getId() == 4) {
                    longueur = (int) ((Math.ceil(l / 8)) * 16);
                    if (longueur % 32 == 16) longueur += 16;
                }
                offsetData += longueur;
                if (table.getId() == 5) {
                    if (offsetData % 16 == 0) offsetData += 2;
                }
            } else {
                newP.setValue(mapValues.get(oldValue));
            }
            table.addPointerDataEng(newP);
        }
        System.out.println("TABLE " + table.getId());
        for (Map.Entry<Integer, Integer> e : mapValues.entrySet()) {
            System.out.println(Integer.toHexString(e.getKey()) + " -> " + Integer.toHexString(e.getValue()));
        }
        return table;
    }

    /**
     * Print out all the special characters from the table
     * A special character is a value representing a memory variable (player name, health points, gold...)
     * Useful to populate the "specials" in config.json
     */
    public static void collectSpecialChars(Translator translator, PointerTable table, byte[] data) {
        List<String> specialChars = new ArrayList<>();
        for (PointerData p : table.getDataJap()) {
            String english = translator.getEnglish(p);
            for (String s : translator.collectSpecialChars(english)) {
                if (!specialChars.contains(s)) specialChars.add(s);
            }
        }
        Collections.sort(specialChars);
        for (String s : specialChars) if (Dokapon.DEBUG) System.out.println(s);
    }

    /**
     * Checks that the dimensions of the menu are not out of bound.
     */
    public static void checkMenuData(PointerTable table) {
        for (PointerData p : table.getDataEng()) {
            String[] menuData = p.getMenuData();
            int x = Integer.parseInt(menuData[0].substring(0, 2), 16);
            int y = Integer.parseInt(menuData[0].substring(2, 4), 16);
            int width = Integer.parseInt(menuData[1].substring(0, 2), 16);
            int height = Integer.parseInt(menuData[1].substring(2, 4), 16);
            if (x + width > Constants.MENU_RIGHT_EDGE) {
                if (Dokapon.DEBUG) System.err.println("MENU OFFSCREEN RIGHT EDGE : " + Integer.toHexString(p.getOffset()) + "(" + (width + x) + ")");
            }
            if (2 * y + height > Constants.MENU_BOTTOM_EDGE && height < 255) {
                if (Dokapon.DEBUG) System.err.println("MENU OFFSCREEN BOTTOM EDGE : " + Integer.toHexString(p.getOffset()) + "(" + (2 * y + height) + ")");
            }
        }
    }
}
