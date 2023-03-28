package services;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Utils {

    public static int x(String s) {
        return Integer.parseInt(s,16);
    }

    public static String h(int i) {
        return padLeft(Integer.toHexString(i).toUpperCase(), '0',5);
    }

    public static String h2(int i) {
        return padLeft(Integer.toHexString(i & 0xFF).toUpperCase(), '0',2);
    }

    public static String h4(int i) {
        return padLeft(Integer.toHexString(i & 0xFFFF).toUpperCase(), '0',4);
    }

    public static String h(byte b) {
        return padLeft(Integer.toHexString(b).toUpperCase(), '0',2);
    }

    public static byte b(String s) {
        return (byte) Integer.parseInt(s,16);
    }
    
    public static String padLeft(String s,char c,int length) {
        while (s.length()<length) {
            s=c+s;
        }
        return s;
    }

    private static final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();
    public static String bytesToHex(byte[] bytes) {
        if (bytes==null) return "";
        char[] hexChars = new char[bytes.length * 3];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 3] = HEX_ARRAY[v >>> 4];
            hexChars[j * 3 + 1] = HEX_ARRAY[v & 0x0F];
            hexChars[j * 3 + 2] = ' ';
        }
        return new String(hexChars);
    }

    public static byte[] hexStringToByteArray(String s) {
        String[] s1 = s.split(" ");
        byte[] bytes = new byte[s1.length];
        for (int i = 0; i < s1.length; i++) {
            bytes[i] = (byte) (Integer.parseInt(s1[i],16) & 0xFF);
        }
        return bytes;
    }

    public static String getColorAsHex(int rgb) {
        int blue = rgb & 0xff;
        int green = (rgb & 0xff00) >> 8;
        int red = (rgb & 0xff0000) >> 16;
        return toHexString(red,2)+toHexString(green,2)+toHexString(blue,2);
    }

    public static String toHexString(int value, int padding) {
        return String.format("%0"+padding+"x",value).toUpperCase();
    }

    public static int lastIndexOf(byte[] array, byte[] target) {
        if (target.length == 0) {
            return array.length - 1;
        } else if (target.length > array.length) {
            return -1;
        }

        int lastIndexOf = -1;
        boolean differentValue;

        for (int i = 0; i <= array.length - target.length; i++) {
            differentValue = false;
            for (int j = 0; j < target.length; j++) {
                if (array[i + j] != target[j]) {
                    differentValue = true;
                    break;
                }
            }
            if (!differentValue) {
                lastIndexOf = i;
            }
        }

        return lastIndexOf;
    }

    public static byte[] codeBytes(String code) {
        byte[] data = new byte[2];
        int a = Integer.parseInt(code.substring(0, 2), 16);
        int b = Integer.parseInt(code.substring(2, 4), 16);
        data[0] = (byte) a;
        data[1] = (byte) b;
        return data;
    }

    public static byte[] getPointer(int value) {
        String code = h4(value);
        byte[] data = new byte[2];
        int a = Integer.parseInt(code.substring(0, 2), 16);
        int b = Integer.parseInt(code.substring(2, 4), 16);
        data[0] = (byte) b;
        data[1] = (byte) a;
        return data;
    }

    /**
    from www  .j  a  v  a2  s .  c  om
    * Flips an image horizontally and/or vertically.
    *
    * @param image      The image to be flipped.
    * @param horizontal Whether the image should be flipped horizontally.
    * @param vertical   Whether the image should be flipped vertically.
    * @return           The given image, flipped horizontally and/or vertically.
    */
    public static BufferedImage flipImage(final BufferedImage image, final boolean horizontal,
                                          final boolean vertical) {
        int x = 0;
        int y = 0;
        int w = image.getWidth();
        int h = image.getHeight();

        final BufferedImage out = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        final Graphics2D g2d = out.createGraphics();

        if (horizontal) {
            x = w;
            w *= -1;
        }

        if (vertical) {
            y = h;
            h *= -1;
        }

        g2d.drawImage(image, x, y, w, h, null);
        g2d.dispose();

        return out;
    }
}
