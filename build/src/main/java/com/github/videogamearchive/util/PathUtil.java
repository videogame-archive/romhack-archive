package com.github.videogamearchive.util;

import java.nio.file.Files;
import java.nio.file.Path;

public class PathUtil {
    public static String getName(Path path) {
        return path.getName(path.getNameCount() - 1).toString();
    }

    public static String getNameWithoutExtension(Path path) {
        String name = path.getName(path.getNameCount() - 1).toString();
        return getNameWithoutExtension(name);
    }

    public static String getNameWithoutExtension(String name) {
        int indexOfDot = name.lastIndexOf('.');
        if (indexOfDot > -1) {
            return name.substring(0, indexOfDot);
        } else {
            return name;
        }
    }

    public static String getExtension(Path path) {
        String name = path.getName(path.getNameCount() - 1).toString();
        return getExtension(name);
    }

    public static String getExtension(String name) {
        int indexOfDot = name.lastIndexOf('.');
        if (indexOfDot > -1) {
            return name.substring(indexOfDot + 1);
        } else {
            return null;
        }
    }

    public static boolean isZip(Path path) {
        return isExtension(path, "zip");
    }
    public static boolean isExtension(Path path, String extension) {
        return !Files.isDirectory(path) &&
                path.toString().toLowerCase().endsWith("." + extension.toLowerCase());
    }
}
