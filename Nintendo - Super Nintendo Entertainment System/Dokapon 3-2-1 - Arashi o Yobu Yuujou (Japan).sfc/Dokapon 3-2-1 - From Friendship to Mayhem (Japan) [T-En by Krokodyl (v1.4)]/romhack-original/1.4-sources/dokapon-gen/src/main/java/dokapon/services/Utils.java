package dokapon.services;

import dokapon.enums.CharSide;

import static dokapon.Constants.*;

public class Utils {

    public static String toHexString(byte value) {
        return toHexString(((int)value) & 0xFF, 2);
    }

    public static String toHexString(int value) {
        return toHexString(value & 0xFF, 2);
    }

    public static String toHexString(int value, int padding) {
        return String.format("%0"+padding+"x",value).toUpperCase();
    }

    /**
     * return a binary string padded on 8 chars with 0.
     */
    public static String toBinaryString(byte value) {
        String binaryString = Integer.toBinaryString(value & 0xFF);
        binaryString = String.format("%08d", Integer.parseInt(binaryString));
        return binaryString;
    }

    private static final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();
    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 3];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 3] = HEX_ARRAY[v >>> 4];
            hexChars[j * 3 + 1] = HEX_ARRAY[v & 0x0F];
            hexChars[j * 3 + 2] = ' ';
        }
        return new String(hexChars);
    }

    public static String padRight(String s,char c,int length) {
        while (s.length()<length) {
            s+=c;
        }
        return s;
    }
    public static String padLeft(String s,char c,int length) {
        while (s.length()<length) {
            s=c+s;
        }
        return s;
    }

    public static String concat(String[] array) {
        String res = "";
        for (String s:array) res+=s+" ";
        return res.trim();
    }

    public static String getCharCodeFromOffset(int offset, CharSide side) {
        String suffix = "00";
        int left,right;
        int offsetRef = OFFSET_FIRST_CHAR_00;
        if (offset>=OFFSET_FIRST_CHAR_01) {
            offsetRef = OFFSET_FIRST_CHAR_01;
            suffix = "01";
        }
        if (offset>=OFFSET_FIRST_CHAR_02) {
            offsetRef = OFFSET_FIRST_CHAR_02;
            suffix = "02";
            int pos = (offset - offsetRef);
            left = pos / Integer.parseInt("400",16);
            pos = pos - (left*Integer.parseInt("400",16));
            if ((pos >= Integer.parseInt("200",16))) {
                pos = pos - Integer.parseInt("100",16);
                right = pos / Integer.parseInt("20",16);
            }
            else right = pos / Integer.parseInt("20",16);
            String res = left +""+ right;
            if (side==CharSide.ONE) {
                return Integer.toHexString(left) + "" + Integer.toHexString(right) + suffix;
            }
        } else {
            int pos = (offset - offsetRef);
            left = pos / Integer.parseInt("200",16);
            pos = pos - (left*Integer.parseInt("200",16));
            right = pos / Integer.parseInt("20",16);
            String res = left +""+ right;
        }
        if (side==CharSide.ONE) {
            return Integer.toHexString(left) +""+ Integer.toHexString(right*2) + suffix;
        } else {
            return Integer.toHexString(left) +""+ Integer.toHexString(right*2+1) + suffix;
        }
    }

    public static String getColorAsHex(int rgb) {
        int blue = rgb & 0xff;
        int green = (rgb & 0xff00) >> 8;
        int red = (rgb & 0xff0000) >> 16;
        return toHexString(red,2)+toHexString(green,2)+toHexString(blue,2);
    }
}
