package dokapon.services;

import dokapon.Dokapon;
import dokapon.characters.LatinChar;
import dokapon.characters.LatinSprite;
import dokapon.characters.SpriteLocation;
import dokapon.enums.CharSide;
import dokapon.enums.CharType;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static dokapon.Constants.*;

public class LatinLoader {


    private List<LatinChar> latinChars = new ArrayList<>();

    public List<LatinChar> getLatinChars() {
        return latinChars;
    }

    boolean verbose = false;

    public void loadLatin() {
        System.out.println("Load Latin");
        latinChars.addAll(JsonLoader.loadLatin());


        String japArmor = "JAP=な  し    {NL}{ARMOR}ぬののふく  {NL}{ARMOR}ローブ    {NL}{ARMOR}あさのふく  {NL}{ARMOR}マント    {NL}{ARMOR}パディット  {NL}{ARMOR}レザーアーマー{NL}{ARMOR}キルボアール {NL}{ARMOR}リングメイル {NL}{ARMOR}スケイルメイル{NL}{ARMOR}ライトアーマー{NL}{ARMOR}くさりかたびら{NL}{ARMOR}スプリント  {NL}{ARMOR}ホーバーク  {NL}{ARMOR}ハーフプレート{NL}{ARMOR}プレートメイル{NL}{ARMOR}ナイトアーマー{NL}{ARMOR}バトルスーツ {NL}{ARMOR}オーラアーマー{NL}{ARMOR}マジックローブ{NL}{ARMOR}チキンマント {NL}{ARMOR}エルブンマント{NL}{ARMOR}ライフガード {NL}{ARMOR}リフレクス  {NL}{ARMOR}ダークアーマー{NL}{ARMOR}シルバーメイル{NL}{ARMOR}デモンアーマー{NL}{ARMOR}ケイオスメイル{NL}{ARMOR}{NL}{ARMOR}{NL}{ARMOR}{NL}{ARMOR}{NL}{ARMOR}{NL}{ARMOR}{EL}";
        List<LatinChar> armorDoubleLatinChars = generateLatinCharTypeDouble(japArmor, "tables/armors-jap.txt", LENGTH_ARMOR_NAMES, CharType.DOUBLE_STRAIGHT);
        for (LatinChar lc:armorDoubleLatinChars) {
            if (!latinChars.contains(lc)) {
                latinChars.add(lc);
                if (verbose) System.out.println(lc.toPrintString());
            }
        }
        String japWeapon = "JAP=な  し    {NL}{WEAP}ナイフ    {NL}{WEAP}ダガー    {NL}{WEAP}グラディウス {NL}{WEAP}ハンドアクス {NL}{WEAP}レイピア   {NL}{WEAP}スピア    {NL}{WEAP}せスタス   {NL}{WEAP}メイス    {NL}{WEAP}フレイル   {NL}{WEAP}サーベル   {NL}{WEAP}トライデント {NL}{WEAP}バトルフィスト{NL}{WEAP}シミター   {NL}{WEAP}ロングソード {NL}{WEAP}バトルアクス {NL}{WEAP}ロングスピア {NL}{WEAP}せルフボウ  {NL}{WEAP}クロスボウ  {NL}{WEAP}ウォーハンマー{NL}{WEAP}エストック  {NL}{WEAP}ロングボウ  {NL}{WEAP}リピーター  {NL}{WEAP}ヌンチャク  {NL}{WEAP}シャムシール {NL}{WEAP}グレートアクス{NL}{WEAP}モール    {NL}{WEAP}グレートソード{NL}{WEAP}アーバレスト {NL}{WEAP}パルチザン  {NL}{WEAP}ハルベルト  {NL}{WEAP}エルブンボウ {NL}{WEAP}きこうけん  {NL}{WEAP}フレイムソード{NL}{WEAP}らんぶのつるぎ{NL}{WEAP}サンダーアクス{NL}{WEAP}シルバーソード{NL}{WEAP}たいまのやり {NL}{WEAP}スリープアロー{NL}{WEAP}げんむのつえ {NL}{WEAP}ポイズンブロウ{NL}{WEAP}ソウルイーター{NL}{WEAP}オーラナックル{NL}{WEAP}ごうりゅうけん{NL}{WEAP}あんこくけん {NL}{WEAP}{NL}{WEAP}{NL}{WEAP}{EL}";
        List<LatinChar> weaponDoubleLatinChars = generateLatinCharTypeDouble(japWeapon, "tables/weapons-jap.txt", LENGTH_WEAPON_NAMES, CharType.DOUBLE_STRAIGHT);
        for (LatinChar lc:weaponDoubleLatinChars) {
            if (!latinChars.contains(lc)) {
                latinChars.add(lc);
                if (verbose) System.out.println(lc.toPrintString());
            }
        }
        String japShield = "JAP=な  し    {NL}{SHIELD}ウッドシールド{NL}{SHIELD}かわのたて  {NL}{SHIELD}バックラー  {NL}{SHIELD}せいどうのたて{NL}{SHIELD}なまりのたて {NL}{SHIELD}どうのたて  {NL}{SHIELD}てつのたて  {NL}{SHIELD}ラージシールド{NL}{SHIELD}はがねのたて {NL}{SHIELD}ナイツシールド{NL}{SHIELD}タワーシールド{NL}{SHIELD}オーラシールド{NL}{SHIELD}せんしのたて {NL}{SHIELD}ぎんのたて  {NL}{SHIELD}ミラーシールド{NL}{SHIELD}アテナのたて {NL}{SHIELD}りゅうのたて {NL}{SHIELD}ダークシールド{NL}{SHIELD}メタルガーダー{NL}{SHIELD}バインダー  {NL}{SHIELD}デモンシールド{NL}{SHIELD}のろいのたて {EL}";
        List<LatinChar> shieldsDoubleLatinChars = generateLatinCharTypeDouble(japShield, "tables/shields-jap.txt", LENGTH_SHIELD_NAMES, CharType.DOUBLE_STRAIGHT);
        for (LatinChar lc:shieldsDoubleLatinChars) {
            if (!latinChars.contains(lc)) {
                latinChars.add(lc);
                if (verbose) System.out.println(lc.toPrintString());
            }
        }
        String japCities = "JAP=    {NL}トキオ {NL}キンペ {NL}ウール {NL}シンガ {NL}モンゴー{NL}ハバロフ{NL}スクワモ{NL}ライク {NL}シバーリ{NL}サラム {NL}ホルトー{NL}マーロ {NL}オフラン{NL}アテーネ{NL}ウイン {NL}ゲルマン{NL}バリテン{NL}ストック{NL}ラスカー{NL}シアトー{NL}ヨーク {NL}ゴシカ {NL}ヒュース{NL}ペガス {NL}アステカ{NL}パマナ {NL}バランシ{NL}サンバ {NL}コルバト{NL}ナスカ {NL}リッチ {NL}パラス {NL}チュール{NL}ゴット {NL}キビック{NL}ビースン{NL}ザイルー{NL}イジップ{NL}ロッコ {NL}ケーニア{NL}タッカル{NL}ケープ {NL}シドーニ{NL}ホルン {NL}パース {NL}ターウイ{NL}フリッツ{NL}リントン{EL}";
        List<LatinChar> citiesDoubleLatinChars = generateLatinCharTypeDouble(japCities, "tables/cities-straight.txt", LENGTH_CIY_NAMES, CharType.DOUBLE_STRAIGHT);
        for (LatinChar lc:citiesDoubleLatinChars) {
            if (!latinChars.contains(lc)) {
                latinChars.add(lc);
                if (verbose) System.out.println(lc.toPrintString());
            }
        }

        Collections.sort(latinChars, new Comparator<LatinChar>() {
            @Override
            public int compare(LatinChar latinChar, LatinChar t1) {
                return latinChar.getType().compareTo(t1.getType());
            }
        });

        initializeCodesAndLocations();

    }

    private void initializeCodesAndLocations() {
        List<SpriteLocation> locations = generateAllPossibleSpriteLocations();
        for (LatinChar lc:latinChars) {
            if (locations.contains(lc.getSpriteLocation())) {
                locations.remove(lc.getSpriteLocation());
            }
        }
        for (LatinChar lc:latinChars) {
            if (lc.getType()!= CharType.NO_SPRITE && lc.getSpriteLocation()==null && !locations.isEmpty()) {
                SpriteLocation spriteLocation = locations.get(0);
                lc.setSpriteLocation(spriteLocation);
                locations.remove(spriteLocation);
            }
            if (lc.getCode()==null) {
                String code = Utils.getCharCodeFromOffset(lc.getSpriteLocation().getOffset(), lc.getSpriteLocation().getSide());
                lc.setCode(code);
            }
        }
        Collections.sort(latinChars, new Comparator<LatinChar>() {
            @Override
            public int compare(LatinChar latinChar, LatinChar t1) {
                if (latinChar.getSpriteLocation() == null || t1.getSpriteLocation()==null) return 0;
                return Integer.compare(latinChar.getSpriteLocation().getOffset(), t1.getSpriteLocation().getOffset());
            }
        });

        for (LatinChar l:latinChars) {
            String s = "";
            if (l.getSpriteLocation()!=null) s = Integer.toHexString(l.getSpriteLocation().getOffset());
            if (Dokapon.DEBUG) System.out.println(s + " " + l.getCode()+" "+l.getIgValue()+" ("+l.getValue()+")");
        }
    }

    private List<SpriteLocation> generateAllPossibleSpriteLocations() {
        List<SpriteLocation> locations = new ArrayList<>();
        int off = OFFSET_FIRST_CHAR_00;
        int line = 0;
        while (off < Integer.parseInt("e0000",16)) {
            if (off>=OFFSET_FIRST_CHAR_02) {
                locations.add(new SpriteLocation(off, CharSide.ONE));
            }
            else {
                locations.add(new SpriteLocation(off, CharSide.ONE));
                locations.add(new SpriteLocation(off, CharSide.TWO));
            }
            off += 32;
            line++;
            if (line%8==0) off += 256;
        }
        return locations;
    }

    private List<LatinChar> generateLatinCharTypeDouble(String japanese, String filename, int maxLength, CharType type) {
        List<LatinChar> res = new ArrayList<>();
        Properties properties = new Properties();
        try {
            properties.load(new InputStreamReader(Objects.requireNonNull(ClassLoader.getSystemClassLoader().getResourceAsStream(filename)), StandardCharsets.UTF_8));
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (Dokapon.DEBUG) System.out.println(japanese);
        japanese = japanese.replace(TRANSLATION_KEY_JAP,TRANSLATION_KEY_ENG);
        for (Map.Entry<Object, Object> entry : properties.entrySet()) {
            String key = entry.getKey().toString();
            key = Utils.padRight(key,' ',maxLength);
            String val = entry.getValue().toString();
            if (val.length()<maxLength) {
                val = Utils.padRight(val,' ',maxLength);
            }
            if (val.length()==maxLength) {
                japanese = japanese.replace(key,val);
            }
            else {
                String doubleChar = generateDoubleCharName(val, maxLength, type);
                japanese = japanese.replace(key,doubleChar);
                res.addAll(generateDoubleChars(val, maxLength, type));
            }
        }
        if (Dokapon.DEBUG) System.out.println(japanese);
        return res;
    }

    private List<LatinChar> generateDoubleChars(String name, int maxLength, CharType type) {
        List<LatinChar> res = new ArrayList<>();
        name = name.trim().toUpperCase();
        if (type==CharType.DOUBLE_STRAIGHT) {
            String[] split = name.split(" ");
            String top = split[0];
            String bot = split[1];
            if (top.length() > maxLength || bot.length() > maxLength) {
                System.out.println("ERROR");
            } else {
                int length = top.length();
                if (length < bot.length()) length = bot.length();
                for (int k = 0; k < length; k++) {
                    String topChar = " ";
                    if (k < top.length()) topChar = top.charAt(k) + "";
                    String botChar = " ";
                    if (k < bot.length()) botChar = bot.charAt(k) + "";
                    String pair = topChar + botChar;
                    LatinChar dc = new LatinChar();
                    dc.setValue("{DC-" + pair + "}");
                    dc.setIgValue(pair);
                    initDoubleCharInfo(dc, type);
                    res.add(dc);
                }
            }
        } else if (type == CharType.DOUBLE_SLANTED) {
            if (name.length()%2!=0) name+=" ";
            for (int k=0;k<name.length()-1;k=k+2) {
                LatinChar dc = new LatinChar();
                String topChar = name.charAt(k) + "";
                String botChar = name.charAt(k+1) + "";
                String pair = topChar + botChar;
                dc.setValue("{DCS-" + pair + "}");
                dc.setIgValue(pair);
                initDoubleCharInfo(dc, type);
                res.add(dc);
            }
        }
        return res;
    }

    // Assuming s contains a space
    private static String generateDoubleCharName(String name, int maxLength, CharType type) {
        String res = "";
        name = name.trim().toUpperCase();
        if (type==CharType.DOUBLE_STRAIGHT) {
            String[] split = name.split(" ");
            String top = split[0];
            String bot = split[1];
            if (top.length() > maxLength || bot.length() > maxLength) {
                System.out.println("ERROR");
            } else {
                int length = top.length();
                if (length < bot.length()) length = bot.length();
                for (int k = 0; k < length; k++) {
                    String topChar = " ";
                    if (k < top.length()) topChar = top.charAt(k) + "";
                    String botChar = " ";
                    if (k < bot.length()) botChar = bot.charAt(k) + "";
                    String pair = topChar + botChar;
                    res += "{DC-" + pair + "}";
                }
                for (int k = 0; k < maxLength - length; k++) res += " ";
            }
        }
        else if (type==CharType.DOUBLE_SLANTED) {
            if (name.length()%2!=0) name+=" ";
            int charCount = 0;
            for (int k=0;k<name.length()-1;k=k+2) {
                charCount++;
                res += "{DCS-" + name.charAt(k) + name.charAt(k+1) + "}";
            }
            for (int k=charCount;k<maxLength;k++) res+=" ";
        }
        return res;
    }

    private void initDoubleCharInfo(LatinChar latinChar, CharType type) {
        latinChar.setType(type);
        if (type==CharType.DOUBLE_STRAIGHT) {
            char c = latinChar.getIgValue().charAt(0);
            int val = c - 'A';
            String prefix = "";
            if (val < 10) prefix = "0";
            String top = "images/double-chars/straight/char-" + prefix + val + ".png";
            c = latinChar.getIgValue().charAt(1);
            val = c - 'A';
            prefix = "";
            if (val < 10) prefix = "0";
            String bot = "images/double-chars/straight/char-" + prefix + val + ".png";
            latinChar.setSprite(new LatinSprite(top, bot));
        }
        if (type==CharType.DOUBLE_SLANTED) {
            char c = latinChar.getIgValue().charAt(0);
            int val = c - 'A';
            String prefix = "";
            if (val < 10) prefix = "0";
            String top = "images/double-chars/slanted/top-" + prefix + val + ".png";
            c = latinChar.getIgValue().charAt(1);
            val = c - 'A';
            prefix = "";
            if (val < 10) prefix = "0";
            String bot = "images/double-chars/slanted/bot-" + prefix + val + ".png";
            latinChar.setSprite(new LatinSprite(top, bot));
        }
    }
}
