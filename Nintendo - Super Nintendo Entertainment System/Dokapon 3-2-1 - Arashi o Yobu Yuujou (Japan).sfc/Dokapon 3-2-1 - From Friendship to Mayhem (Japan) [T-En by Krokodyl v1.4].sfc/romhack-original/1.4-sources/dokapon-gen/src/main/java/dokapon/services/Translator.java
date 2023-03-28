package dokapon.services;

import dokapon.Constants;
import dokapon.characters.JapaneseChar;
import dokapon.characters.LatinChar;
import dokapon.characters.SpecialChar;
import dokapon.entities.PointerData;
import dokapon.entities.PointerTable;
import dokapon.entities.Translation;
import org.apache.commons.lang.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static dokapon.Constants.LENGTH_DIALOG_LINE;

public class Translator {

    private List<Translation> translations = new ArrayList<>();

    private Map<String, SpecialChar> specialCharMap = new HashMap<>();

    private LatinLoader latinLoader;

    public Translator(LatinLoader latinLoader) {
        this.latinLoader = latinLoader;
    }

    public void setSpecialCharMap(Map<String, SpecialChar> specialCharMap) {
        this.specialCharMap = specialCharMap;
    }

    public void loadTranslationFile(String name) throws IOException {
        System.out.println("Load Translation File : "+name);
        BufferedReader br = new BufferedReader(
                new InputStreamReader(
                        Objects.requireNonNull(Translator.class.getClassLoader().getResourceAsStream(name)), StandardCharsets.UTF_8));
        String line = br.readLine();
        Translation t = new Translation();
        while (line != null) {
            if (line.contains(Constants.TRANSLATION_KEY_VALUE_SEPARATOR)) {
                String[] split = line.split(Constants.TRANSLATION_KEY_VALUE_SEPARATOR);
                if (split.length>0) {
                    if (split[0].equals(Constants.TRANSLATION_KEY_OFFSETDATA)) {
                        t.setOffsetData(Integer.parseInt(split[1], 16));
                    }
                    if (split[0].equals(Constants.TRANSLATION_KEY_OFFSET)) {
                        t.setOffset(Integer.parseInt(split[1], 16));
                    }
                    if (split[0].equals(Constants.TRANSLATION_KEY_MENUDATA)) {
                        if (split.length>1) t.setMenuData(split[1]);
                    }
                    if (split[0].equals(Constants.TRANSLATION_KEY_VALUE)) {
                        t.setValue(split[1]);
                    }
                    if (split[0].equals(Constants.TRANSLATION_KEY_ENG)) {
                        t.setTranslation(split[1]);
                    }
                }
            } else {
                if (t.getTranslation() != null && !t.getTranslation().trim().isEmpty()) {
                    if (!name.contains("Table 7") && name.contains("Table 6")) {
                        /*if (t.getTranslation().length()>=40 && t.getTranslation().length()<=48) {
                            System.out.println("pb:"+t.getTranslation());
                            //t.setTranslation("Z{EL}");
                        }*/
                        /*System.out.println(t.getOffset());
                        System.out.println(t.getValue().length());*/
                    }

                    translations.add(t);
                }
                else {
                    System.out.println("MISSING TRANSLATIONS : "+Integer.toHexString(t.getOffset()));
                }
                t = new Translation();
            }
            line = br.readLine();
        }
    }

    public String[] getTranslation(PointerData p, boolean evenLength) {
        for (Translation t : translations) {
            String translation = t.getTranslation();
            if (t.getOffsetData() == p.getOffsetData() && translation != null && !translation.isEmpty()) {
                int dataLength = checkDataLength(translation);
                if (evenLength && dataLength%2!=0) {
                    translation+="{EL}";
                }
                String menuData = t.getMenuData();
                String eng = getCodesFromEnglish(translation);
                if (menuData != null && !menuData.isEmpty()) {
                    eng = menuData + " " + eng;
                }
                return eng.split(" ");
            }
        }
        return null;
    }

    public String getEnglish(PointerData p) {
        for (Translation t : translations) {
            if (t.getOffsetData() == p.getOffsetData() && t.getTranslation() != null && !t.getTranslation().isEmpty()) {
                String menuData = t.getMenuData();
                String eng = t.getTranslation();
                return eng;
            }
        }
        return null;
    }

    public String[] getMenuData(PointerData p) {
        for (Translation t : translations) {
            if (t.getOffsetData() == p.getOffsetData() && t.getTranslation() != null && !t.getTranslation().isEmpty()) {
                String menuData = t.getMenuData();
                if (menuData == null) {
                    return null;
                }
                return menuData.trim().split(" ");
            }
        }
        return null;
    }

    private String getCodesFromEnglish(String eng) {
        String res = "";
        boolean skip = false;
        String skipped = "";
        boolean openQuote = true;
        checkLineLength(eng.split("\\{NL\\}"));
        for (char c : eng.toCharArray()) {
            if (c == '{') {
                skip = true;
                skipped = "";
            }
            if (!skip) {
                LatinChar e = getLatinChar(c + "");
                if (e == null) {
                    if (c=='"' && openQuote) {
                        e = getLatinChar("{O-QUOTES}");
                        openQuote = false;
                    }
                    if (c=='"' && !openQuote) {
                        e = getLatinChar("{C-QUOTES}");
                        openQuote = true;
                    }
                }
                if (e == null) {
                    res = res += e.getCode() + " ";
                }
                res = res += e.getCode() + " ";

            }
            if (skip) {
                skipped += c;
            }
            if (c == '}') {
                skip = false;
                if (containsTranslation(skipped)) {
                    res += getLatinChar(skipped).getCode() + " ";
                } else {
                    res += skipped;
                }
            }
        }
        res = res.replace("{NL}", Constants.NEW_LINE_CHARACTER_HEXA+" ");
        res = res.replace("{EL}", Constants.END_OF_LINE_CHARACTER_HEXA+" ");
        res = res.replace("{", "");
        res = res.replace("}", " ");
        return res.trim();
    }

    private void checkLineLength(String[] lines) {
        for (String eng:lines) {
            String res = "";
            boolean skip = false;
            for (char c : eng.toCharArray()) {
                if (c == '{') {
                    skip = true;
                } else if (c == '}') {
                    skip = false;
                } else {
                    if (!skip) res += c;
                }
            }
            if (res.length()>LENGTH_DIALOG_LINE) {
                System.out.println("LINE TOO LONG "+res+" ("+res.length()+")");
                System.out.println(insertLineBreak(res));
            }
        }
    }

    public List<String> collectSpecialChars(String eng) {
        List<String> res = new ArrayList<>();
        boolean skip = false;
        String spe = "";
        for (char c : eng.toCharArray()) {
            if (c == '{') {
                skip = true;
                spe += c;
            } else if (c == '}') {
                skip = false;
                spe += c;
                if (!specialCharMap.containsKey(spe) && !res.contains(spe)) res.add(spe);
                spe = "";
            } else {
                if (skip) spe += c;
            }
        }
        return res;
    }

    public void showSpecialChars() {
        /*System.out.println("SPECIAL");
        Collections.sort(specials);
        for (String s:specials) System.out.println(s);*/
    }

    private String insertLineBreak(String line) {
        String[] split = line.split(" ");
        String res = "";
        String segment = "";
        for (String s:split) {
            if ((segment+" "+s).length()>LENGTH_DIALOG_LINE) {
                segment+="{NL}";
                res += segment;
                segment = s;
            } else {
                segment += " "+s;
            }
        }
        res+=segment;
        return res.trim()+"{EL}";
    }

    public String getJapanese(String codes, List<JapaneseChar> japaneseChars) {
        String[] split = codes.split(" ");
        String res = "";
        for (String s:split) {
            boolean found = false;
            for (JapaneseChar jc:japaneseChars) {
                if (jc.getCode().equals(s)) {
                    found = true;
                    res+=jc.getValue();
                }
            }
            if (!found) {
                res+="{"+s+"}";
            }
        }
        return res;
    }

    private LatinChar getLatinChar(String c) {
        for (LatinChar e : latinLoader.getLatinChars()) {
            if (e.getValue().equals(c)) {
                return e;
            }
        }
        return null;
    }

    private boolean containsTranslation(String s) {
        for (LatinChar e : latinLoader.getLatinChars()) {
            if (e.getValue().equals(s)) {
                return true;
            }
        }
        return false;
    }

    public void checkTranslations(byte[] data) {
        int count = 0;
        for (Translation t:translations) {
            int a = (data[t.getOffset()] & 0xFF);
            int b = (data[t.getOffset()+1] & 0xFF);
            String val = Integer.toHexString(a)+Integer.toHexString(b);
            if (!t.getValue().contains(val))
            {
                count++;
                System.out.println(Integer.toHexString(t.getOffset())+" "+t.getValue()+" "+val);
            }
        }
    }

    public String[] getTranslationPointer(PointerData p, PointerTable table) {
        String eng = getCodesFromEnglish(
                Utils.padLeft(Integer.toHexString(p.getValue()),'0',4)
                        +" "
                        +(Integer.toHexString(p.getOffsetData()))
                        +"{EL}");
        return eng.split(" ");

    }

    public void checkInGameLength(String english) {
        String[] lines = english.split("\\{NL\\}");
        for (String line:lines) {
            int length = 0;
            for (Map.Entry<String, SpecialChar> specialCharEntry : specialCharMap.entrySet()) {
                int matches = StringUtils.countMatches(line, specialCharEntry.getKey());
                length+=specialCharEntry.getValue().getInGameLength()*matches;
                line = line.replace(specialCharEntry.getKey(),"");
            }
            length+=line.length();
            if (length> LENGTH_DIALOG_LINE)
                System.out.println("LINE TOO LONG "+line+" ("+length+")");
        }
    }

    public int getInGameLength(String english) {
        String[] lines = english.split("\\{NL\\}");
        int length = 0;
        for (String line:lines) {
            for (Map.Entry<String, SpecialChar> specialCharEntry : specialCharMap.entrySet()) {
                int matches = StringUtils.countMatches(line, specialCharEntry.getKey());
                length+=specialCharEntry.getValue().getInGameLength()*matches;
                line = line.replace(specialCharEntry.getKey(),"");
            }
            length+=line.length();
        }
        return length;
    }

    public int checkDataLength(String english) {
        String line = english;
        int length = 0;
        for (Map.Entry<String, SpecialChar> specialCharEntry : specialCharMap.entrySet()) {
            int matches = StringUtils.countMatches(line, specialCharEntry.getKey());
            length+=specialCharEntry.getValue().getDataLength()*matches;
            line = line.replace(specialCharEntry.getKey(),"");
        }
        length+=line.length();
        return length;
    }

}
