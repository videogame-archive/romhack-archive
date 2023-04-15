package com.github.videogamearchive.model;

import com.github.videogamearchive.rompatcher.MarcFile;
import com.github.videogamearchive.rompatcher.formats.BPS;
import com.github.videogamearchive.util.Hashes;
import com.github.videogamearchive.util.PathUtil;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class RomhackValidator {

    private RomhackValidator() {}
    public static void validateRomHashLength(Romhack romhack) {
        // Rom hashes validation
        if (romhack.rom().crc32().length() != 8) {
            throw new RuntimeException("romhack.json rom crc32 is not 8 - Actual: " + romhack.rom().crc32());
        }
        if (romhack.rom().sha1().length() != 40) {
            throw new RuntimeException("romhack.json rom sha1 is not 40 - Actual: " + romhack.rom().sha1());
        }
        if (romhack.rom().md5().length() != 32) {
            throw new RuntimeException("romhack.json rom md5 is not 32 - Actual: " + romhack.rom().md5());
        }
    }

    public static void validateBPS(Romhack romhack, Path romhackBPS) throws IOException {
        BPS bps = BPS.parseBPSFile(new MarcFile(romhackBPS));
        if (romhack.rom().size() != bps.targetSize) {
            throw new RuntimeException("romhack rom size and bps patch target size differ - Actual: " + bps.targetSize + " Expected: " + romhack.rom().size());
        }
        String expectedRomCrc = romhack.rom().crc32();
        String foundRomCrc = Hashes.getCrc32String(bps.targetChecksum);
        if (!expectedRomCrc.equals(foundRomCrc)) {
            throw new RuntimeException("romhack rom crc32 and bps patch target crc32 differ - Actual: " + foundRomCrc + " Expected: " + expectedRomCrc);
        }
    }

    public static void validateFolder(Romhack romhack, Path romFolder) {
        String folderName = PathUtil.getName(romFolder);
        String extension = PathUtil.getExtension(folderName);

        // romhack folder naming convention validation
        if (extension == null) {
            throw new RuntimeException("Missing rom extension");
        }

        String expectedFolderPostfix = RomhackValidator.getExpectedFolderNamePostfix(romhack) + "." + extension;
        if (!folderName.endsWith(expectedFolderPostfix)) {
            throw new RuntimeException("romhack rom folder name missmatch - Actual: '" + folderName + "' Expected: '" + expectedFolderPostfix + "'");
        }

        // romhack-original - ensure only folders are found
        Path originalFolder = romFolder.resolve("romhack-original");
        for (File version:originalFolder.toFile().listFiles()) {
            if (!version.isDirectory()) {
                throw new RuntimeException("file in romhack-original folder when it can only contain directories");
            }
        }

        // romhack-original - ensure folders with number of patches are found
        HashSet<String> folderNames = new HashSet<>();
        folderNames.addAll(Arrays.asList(originalFolder.toFile().list()));

        for (int i = 1; i < romhack.patches().size(); i++) {
            String name = "" + i;
            if (!folderNames.contains(name)) {
                throw new RuntimeException("romhack-original misses a version - Actual: " + name);
            }
            folderNames.remove(name);
            String nameSources = "" + i + "-source";
            folderNames.remove(nameSources);
        }

        if (!folderNames.isEmpty()) {
            throw new RuntimeException("romhack-original has unexpected contents - Actual: " + folderNames);
        }

    }

    public static void validateRom(Romhack romhack, byte[] bytes) throws NoSuchAlgorithmException {
        String crc32 = Hashes.getCrc32(bytes);
        if (!romhack.rom().crc32().equals(crc32)) {
            throw new RuntimeException("romhack rom crc32 differ - Actual: " + crc32 + " Expected: " + romhack.rom().crc32());
        }
        String md5 = Hashes.getMd5(bytes);
        if (!romhack.rom().md5().equals(md5)) {
            throw new RuntimeException("romhack rom md5 differ - Actual: " + md5 + " Expected: " + romhack.rom().md5());
        }
        String sha1 = Hashes.getSha1(bytes);
        if (!romhack.rom().sha1().equals(sha1)) {
            throw new RuntimeException("romhack rom sha1 differ - Actual: " + sha1 + " Expected: " + romhack.rom().sha1());
        }
        if (romhack.rom().size() != bytes.length) {
            throw new RuntimeException("romhack rom size differ - Actual: " + bytes.length + " Expected: " + romhack.rom().size());
        }
    }

    public static String getExpectedFolderNamePostfix(Romhack romhack) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < romhack.patches().size(); i++) {
            Patch patch = romhack.patches().get(i);

            // Validation
            if (patch.alternative() != null && patch.alternative().isBlank()) {
                throw new RuntimeException(i +" alternative string CANNOT BE empty");
            }

            // Building name
            builder.append(' ');
            builder.append('[');

            builder.append(patch.labels().get(0) + " by " + getAuthor(patch.authors()) + " (v" + patch.version() + ")");
            if (patch.alternative() != null) {
                builder.append(" (Alt " + patch.alternative() + ")");
            }
            builder.append(']');
        }
        return builder.toString();
    }

    private static String getAuthor(List<String> authors) {
        StringBuilder builder = new StringBuilder();
        for (String author:authors) {
            if (!builder.isEmpty()) {
                builder.append(", ");
            }
            builder.append(author);
        }
        return builder.toString();
    }
}
