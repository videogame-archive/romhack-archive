package com.github.videogamearchive.util;

import java.util.ArrayList;
import java.util.List;

public class StringUtil {

    private StringUtil() {
        // Private constructor to make clear that is a non-instantiable utility class
    }

    public static boolean isBlankString(String string) {
        return string == null || string.isBlank();
    }

    public static List<String> substrings(String string, String start, String end, boolean excluding) {
        int offset = 0;
        List<String> items = new ArrayList<>();
        while (offset < string.length()) {
            int startIndex = string.indexOf(start, offset);
            int endIndex = -1;
            if (startIndex > -1) {
                endIndex = string.indexOf(end, startIndex + start.length());
            }
            if (endIndex > -1) { // substring
                String item = null;
                if (excluding) {
                    item = string.substring(startIndex + start.length(), endIndex);
                } else {
                    item = string.substring(startIndex, endIndex + end.length());
                }
                items.add(item);
                offset = endIndex + end.length();
            } else {
                offset = string.length();
            }
        }

        return items;
    }

    public static String substring(String string, String start, String end, boolean excluding) {
        List<String> substring = substrings(string, start, end, excluding);
        if (substring.size() > 0) {
            return substring.get(0);
        } else {
            return null;
        }
    }

}