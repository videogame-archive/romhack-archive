package dokapon.services;


import dokapon.Constants;
import dokapon.characters.JapaneseChar;
import dokapon.entities.CodePatch;
import dokapon.entities.PointerData;
import dokapon.entities.PointerTable;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import static dokapon.Dokapon.EXTRA_DATA_BANK_REQUIRED;

public class DataWriter {

    public static byte[] fillDataWithPlaceHolders(byte[] data) {
        int fullLength = Integer.parseInt("180000", 16)+Integer.parseInt("8000", 16)*EXTRA_DATA_BANK_REQUIRED;
        if (data.length<fullLength) {
            byte[] dummy = new byte[fullLength];
            for (int k=0;k<data.length;k++) dummy[k] = data[k];
            for (int k=Integer.parseInt("180000", 16);k<fullLength;k++) dummy[k] = 0;
            data = dummy;
        }
        for (int i=0;i<EXTRA_DATA_BANK_REQUIRED;i++) {
            int k = Integer.parseInt("180000", 16)+Integer.parseInt("8000", 16)*i;
            for (int j=0;j<Integer.parseInt("8000", 16);j++) {
                //data[k+j] = (byte)(16 + i);
                data[k+j] =0;
            }
        }
        return data;
    }

    public static byte[] writeCodePatches(List<CodePatch> patchList, byte[] data, boolean debug) {
        System.out.println("Write Patches (debug="+debug+")");
        for (CodePatch cp:patchList) {
            if (cp.isDebug()==debug)
            cp.writePatch(data);
        }
        return data;
    }

    public static byte[] writeEnglish(PointerTable table, byte[] data) {
        System.out.println("Write English for table "+table.getId());
        for (PointerData p : table.getDataEng()) {
            int offset = p.getOffset();
            data[offset] = (byte) (p.getValue() % 256);
            data[offset + 1] = (byte) (p.getValue() / 256);
            int offsetData = p.getOffsetData();
            for (String s : p.getData()) {
                int a = Integer.parseInt(s.substring(0, 2), 16);
                int b = Integer.parseInt(s.substring(2, 4), 16);
                data[offsetData] = (byte) a;
                data[offsetData + 1] = (byte) b;
                offsetData += 2;
            }
            String[] menuData = p.getMenuData();
            if (menuData != null) {
                int i = p.getOffsetOldMenuData();
                for (String s : menuData) {
                    int a = Integer.parseInt(s.substring(0, 2), 16);
                    int b = Integer.parseInt(s.substring(2, 4), 16);
                    data[i] = (byte) a;
                    data[i + 1] = (byte) b;
                    i += 2;
                }
            }
        }
        return data;
    }

    public static String[] extractMenuData(PointerData pointer, String english) {
        String[] data = pointer.getData();
        String[] menuData = new String[2];
        menuData[0] = data[0];
        menuData[1] = data[1];
        pointer.setMenuData(menuData);
        String[] newData = new String[data.length-2];
        for (int k=2;k<data.length;k++) {
            newData[k-2] = data[k];
        }
        pointer.setData(newData);
        return menuData;
    }

    public static String getPrintableString(PointerData pointer, Translator translator, List<JapaneseChar> japaneseChars, String english){
        String s = "";
        s +=    Constants.TRANSLATION_KEY_OFFSET
                +Constants.TRANSLATION_KEY_VALUE_SEPARATOR
                +Integer.toHexString(pointer.getOffset())
                +"\n";
        String value = Utils.padLeft(Integer.toHexString(pointer.getValue()),'0',4);
        s +=    Constants.TRANSLATION_KEY_VALUE
                +Constants.TRANSLATION_KEY_VALUE_SEPARATOR
                + value+"("+value.substring(2,4)+value.substring(0,2)+")"
                +"\n";
        s +=    Constants.TRANSLATION_KEY_OFFSETDATA
                +Constants.TRANSLATION_KEY_VALUE_SEPARATOR
                +Integer.toHexString(pointer.getOffsetData())
                +"\n";
        if (pointer.getMenuData()!=null && pointer.getMenuData().length>0) {
            s +=    Constants.TRANSLATION_KEY_MENUDATA
                    +Constants.TRANSLATION_KEY_VALUE_SEPARATOR
                    +Utils.concat(pointer.getMenuData())
                    +"\n";
        }
        s +=    Constants.TRANSLATION_KEY_DATA
                +Constants.TRANSLATION_KEY_VALUE_SEPARATOR
                +(Utils.concat(pointer.getData()))
                +"\n";
        String japanese = translator.getJapanese(
                Utils.concat(pointer.getData()),
                JsonLoader.loadJap());
        Pattern pattern = Pattern.compile("([0-9a-f]{4})}\\{([0-9a-f]{4})");
        japanese = pattern.matcher(japanese).replaceAll("$1 $2");
        english = pattern.matcher(english).replaceAll("$1 $2");
        s +=    Constants.TRANSLATION_KEY_JAP
                +Constants.TRANSLATION_KEY_VALUE_SEPARATOR
                +japanese
                +"\n";
        s +=    Constants.TRANSLATION_KEY_ENG
                +Constants.TRANSLATION_KEY_VALUE_SEPARATOR
                +english
                +"\n";
        return s;
    }

    public static void saveData(String romOutput, byte[] data) {
        System.out.println("Save Data "+romOutput);
        FileOutputStream stream = null;
        try {
            stream = new FileOutputStream(new File(romOutput));
            stream.write(data);
            stream.close();
        } catch (IOException ex) {
            Logger.getLogger(DataWriter.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException ex) {
                    Logger.getLogger(DataWriter.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    public static String removeMenuData(String english, String[] menuData) {
        String nospace = "{"+menuData[0]+"}"+"{"+menuData[1]+"}";
        String space = "{"+menuData[0]+" "+menuData[1]+"}";
        if (english.startsWith(nospace)) {
            return english.replace(nospace,"");
        }
        else if (english.startsWith(space)) {
            return english.replace(space,"");
        }
        return english;
    }

}
