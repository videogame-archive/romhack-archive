package dokapon.services;


import dokapon.characters.*;
import dokapon.entities.*;
import dokapon.enums.CharSide;
import dokapon.enums.CharType;
import org.json.JSONArray;
import org.json.JSONObject;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.*;

import static dokapon.Constants.TRANSLATION_KEY_ENG;
import static dokapon.Constants.TRANSLATION_KEY_JAP;
import static dokapon.services.Utils.bytesToHex;

public class JsonLoader {

    static String file = "config.json";

    private static String loadJson() {
        InputStream is =
                JsonLoader.class.getClassLoader().getResourceAsStream(file);
        String jsonTxt = null;
        try {
            jsonTxt = IOUtils.toString( is );
        } catch (IOException e) {
            e.printStackTrace();
        }
        return jsonTxt;
    }

    public static List<String> loadTranslationFiles() {

        List<String> files = new ArrayList<>();
        JSONObject json = new JSONObject(loadJson());

        JSONArray c = json.getJSONArray("translations-files");
        for (Object o : c) {
            String next = (String) o;
            files.add(next);
        }

        return files;
    }

    public static Config loadConfig() {

        Config config = new Config();

        JSONObject json = new JSONObject(loadJson());

        JSONObject c = json.getJSONObject("config");
        config.setRomInput(c.getString("rom-input"));
        config.setRomOutput(c.getString("rom-output"));
        config.setBpsPatchOutput(c.getString("bps-patch-output"));
        config.setFileDicoJap(c.getString("file.jap"));
        config.setFileDicoLatin(c.getString("file.latin"));
        config.setFileDicoNames(c.getString("file.names"));

        return config;
    }

    public static Map<String, SpecialChar> loadSpecialChars() {
        Map<String, SpecialChar> specialCharMap = new HashMap<>();

        JSONObject json = new JSONObject(loadJson());

        JSONArray array = json.getJSONArray("specials");

        for (Object o : array) {
            JSONObject next = (JSONObject) o;
            String code = next.getString("code");
            int inGameLength = next.getInt("in-game-length");
            int dataLength = next.getInt("data-length");
            SpecialChar c = new SpecialChar(code);
            c.setInGameLength(inGameLength);
            c.setDataLength(dataLength);
            specialCharMap.put(c.getCode(), c);
        }
        return specialCharMap;
    }

    public static List<LatinChar> loadLatin() {
        List<LatinChar> latinChars = new ArrayList<>();

        JSONObject json = new JSONObject(loadJson());

        JSONArray array = json.getJSONArray("latin");
        for (Object o : array) {
            JSONObject next = (JSONObject) o;
            LatinChar c = new LatinChar();
            String value = next.getString("value");
            if (next.has("code")) {
                c.setCode(next.getString("code"));
            }
            String type = next.getString("type");
            c.setValue(value);
            c.setType(CharType.valueOf(type));
            if (next.has("sprite")) {
                JSONObject sprite = next.getJSONObject("sprite");
                if (sprite.has("image")) {
                    c.setSprite(new LatinSprite(sprite.getString("image")));
                } else {
                    c.setSprite(new LatinSprite(sprite.getString("image-top"), sprite.getString("image-bot")));
                }
            }
            if (next.has("location")) {
                JSONObject location = next.getJSONObject("location");
                c.setSpriteLocation(new SpriteLocation(Integer.parseInt(location.getString("offset"), 16), CharSide.valueOf(location.getString("side"))));
            }
            latinChars.add(c);
        }
        return latinChars;
    }

    public static List<CodePatch> loadCodePatches() {
        List<CodePatch> codePatches = new ArrayList<>();

        JSONObject json = new JSONObject(loadJson());

        JSONArray array = json.getJSONArray("code-patches");
        for (Object o : array) {
            JSONObject next = (JSONObject) o;
            String code = "";
            int offset = Integer.parseInt(next.getString("offset"), 16);
            if (next.has("file") && next.getBoolean("file")) {
                try {
                    System.out.println("Loading code patch "+"src/main/resources/data/output/" + next.getString("offset") + ".data");
                    byte[] bytes = Files.readAllBytes(new File("src/main/resources/data/output/" + next.getString("offset") + ".data").toPath());
                    code = bytesToHex(bytes);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                code = next.getString("code");
            }
            CodePatch codePatch = new CodePatch(code, offset);
            if (next.has("debug")) codePatch.setDebug(next.getBoolean("debug"));
            codePatches.add(codePatch);
        }
        return codePatches;
    }

    public static List<InputPatch> loadInputPatches() {
        List<InputPatch> patches = new ArrayList<>();

        JSONObject json = new JSONObject(loadJson());

        JSONArray array = json.getJSONArray("input-patches");
        for (Object o : array) {
            JSONObject next = (JSONObject) o;
            int offset = Integer.parseInt(next.getString("offset"), 16);
            InputPatch patch = new InputPatch(offset);
            patch.setLatin(next.getString("latin"));
            patch.setType(InputPatch.InputPatchType.valueOf(next.getString("type")));
            if (next.has("debug")) patch.setDebug(next.getBoolean("debug"));
            patches.add(patch);
        }
        return patches;
    }

    public static List<PointerTable> loadTables() {
        List<PointerTable> tables = new ArrayList<>();

        JSONObject json = new JSONObject(loadJson());

        JSONArray array = json.getJSONArray("tables");
        for (Object o : array) {
            JSONObject next = (JSONObject) o;
            int id = next.getInt("id");
            PointerTable table = new PointerTable(id);
            table.setNewDataStart(next.getInt("new-data-start"));
            table.setNewDataShift(next.getInt("new-data-shift"));
            JSONArray pointersArray = next.getJSONArray("pointers");
            for (Object value : pointersArray) {
                JSONObject pointerObject = (JSONObject) value;
                PointerRange range = new PointerRange(
                        pointerObject.getInt("start"),
                        pointerObject.getInt("end"),
                        pointerObject.getInt("shift"));
                table.addPointerRange(range);
            }
            if (next.has("menu")) {
                table.setMenu(next.getBoolean("menu"));
            }
            if (next.has("counter")) {
                table.setCounter(next.getBoolean("counter"));
            }
            if (next.has("even-length")) {
                table.setEvenLength(next.getBoolean("even-length"));
            }
            if (next.has("overflow")) {
                JSONObject overflow = next.getJSONObject("overflow");
                Overflow ow = new Overflow();
                ow.setLimit(overflow.getInt("limit"));
                ow.setDataStart(overflow.getInt("data-start"));
                ow.setDataShift(overflow.getInt("data-shift"));
                table.setOverflow(ow);
            }
            tables.add(table);
        }
        return tables;
    }

    public static List<JapaneseChar> loadJap() {
        List<JapaneseChar> chars = new ArrayList<>();
        JSONObject json = new JSONObject(loadJson());

        JSONArray array = json.getJSONArray("japanese");
        for (Object o : array) {
            JSONObject next = (JSONObject) o;
            JapaneseChar c = new JapaneseChar();
            String value = next.getString("value");
            if (next.has("code")) {
                c.setCode(next.getString("code"));
            }
            c.setValue(value);
            chars.add(c);
        }
        return chars;
    }

    public static List<TownSign> loadTownSigns() {
        List<TownSign> list = new ArrayList<>();

        JSONObject json = new JSONObject(loadJson());

        JSONArray array = json.getJSONArray("town-signs");
        for (Object o : array) {
            JSONObject next = (JSONObject) o;
            String town = next.getString("town");
            String value = next.getString("value");
            int offset = Integer.parseInt(next.getString("offset"), 16);
            TownSign ts = new TownSign(offset, town, value);
            list.add(ts);
        }
        return list;
    }

}
