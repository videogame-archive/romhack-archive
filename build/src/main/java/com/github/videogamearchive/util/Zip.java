package com.github.videogamearchive.util;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.attribute.FileTime;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

/*
 * This class applies the torrentZip standard as close as it can be done using the built-in JDK Zip classes.
 */
public class Zip {

    public static byte[] readAllBytesOneFile(Path path) throws IOException {
        Map<String, byte[]> zipContent = Zip.readAllBytes(path);
        if (zipContent.size() != 1) {
            throw new IllegalArgumentException("zip contains " + zipContent.size() + " files.");
        }
        return zipContent.values().iterator().next();
    }

    public static Map<String, byte[]> readAllBytes(Path path) throws IOException {
        Map<String, byte[]> inMemory = new HashMap<>();
        try(ZipFile zipFile = new ZipFile(path.toString())) {
            Enumeration<? extends ZipEntry> entries = zipFile.entries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = entries.nextElement();
                if (!entry.isDirectory()) {
                    String name = entry.getName().replace('\'', '/');
                    InputStream fileAsInput = zipFile.getInputStream(entry);
                    inMemory.put(name, fileAsInput.readAllBytes());
                }
            }
        }
        return inMemory;
    }
    private static Instant torrentZipModificationTime = Instant.parse("1996-12-24T22:32:00.00Z");

    public static void write(Path path,Map<String, byte[]>  content) throws IOException {
        ZipOutputStream out = new ZipOutputStream(new FileOutputStream(path.toFile()));
        out.setLevel(9);
        out.setMethod(ZipOutputStream.DEFLATED);
        for (String key:getTorrentZipEntryOrder(content.keySet())) {
            byte[] bytes = content.get(key);
            String name = key.replace('\'', '/');
            ZipEntry e = new ZipEntry(name);
            e.setMethod(ZipEntry.DEFLATED);
            e.setLastModifiedTime(FileTime.from(torrentZipModificationTime));
            if (name.endsWith("/")) { // Is directory
                e.setSize(0);
                e.setCrc(0);
                out.putNextEntry(e);
            } else {
                out.putNextEntry(e);
                out.write(bytes); // writing should also set the crc32
            }
            out.closeEntry();
        }
        out.close();
    }

    private static List<String> getTorrentZipEntryOrder(Collection<String> entryNames) {
        List<String> normalizedEntryNames = new ArrayList<>(entryNames.size());
        Map<String, String> normalizedEntryNames2original = new HashMap<>();
        for (String entryName:entryNames) {
            String normalizedEntryName = entryName.replace('\'', '/').toLowerCase();
            normalizedEntryNames.add(normalizedEntryName);
            normalizedEntryNames2original.put(normalizedEntryName, entryName);
        }
        Collections.sort(normalizedEntryNames);
        List<String> normalizedEntryNamesSorted = normalizedEntryNames; // New variable to indicate that now is sorted
        List<String> originalEntryNamesSorted = new ArrayList<>(entryNames.size());
        for (String normalizedEntryNameSorted:normalizedEntryNamesSorted) {
            originalEntryNamesSorted.add(normalizedEntryNames2original.get(normalizedEntryNameSorted));
        }
        return originalEntryNamesSorted;
    }
}
