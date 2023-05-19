package com.github.videogamearchive.util;

import org.tukaani.xz.LZMA2Options;
import org.tukaani.xz.XZInputStream;
import org.tukaani.xz.XZOutputStream;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;

public class XZ {
    
    private XZ() {
        // Private constructor to make clear that is a non-instantiable utility class
    }

    public static byte[] readAllBytes(Path path) throws IOException {
        InputStream infile = new FileInputStream(path.toFile());
        XZInputStream inxz = new XZInputStream(infile);
        byte[] bytes = inxz.readAllBytes();
        inxz.close();
        return bytes;
    }

    public static void write(Path path, byte[] content) throws IOException {
        FileOutputStream outfile = new FileOutputStream(path.toFile());
        XZOutputStream outxz = new XZOutputStream(outfile, new LZMA2Options(LZMA2Options.PRESET_MAX));
        outxz.write(content);
        outxz.close();
    }

}