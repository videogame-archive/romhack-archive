package com.github.videogamearchive.hack2arch;

import com.github.videogamearchive.model.*;
import com.github.videogamearchive.rompatcher.MarcFile;
import com.github.videogamearchive.rompatcher.formats.BPS;
import com.github.videogamearchive.util.Hashes;
import com.github.videogamearchive.util.PathUtil;
import com.github.videogamearchive.util.StringUtil;
import com.github.videogamearchive.util.Zip;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

public class Romhack2Archive {
    public static void main(String[] args) throws Exception {
        if (args.length != 3) {
            help();
        } else {
            processSystem(Path.of(args[2]), Path.of(args[0]), Path.of(args[1]));
        }
    }

    private static void help() {
        System.out.println("usage: ");
        System.out.println("\t\t java -jar romhack2archive.jar \"parentRom\" \"romhackRom\" \"outputDir\"");
    }

    static private long MAX_MB_FOR_DELTA = 50331648;
    private static void processSystem(Path outDir, Path pathToParentRom, Path pathToRomhackRom) throws IOException, NoSuchAlgorithmException, ReflectiveOperationException {
        System.out.println("maxMemory: " + Runtime.getRuntime().maxMemory());
        // Create directory tree
        Path out = outDir;
        if (!Files.exists(out)) {
            Files.createDirectories(out);
        }
        String parentRomName = null;
        if (PathUtil.isZip(pathToParentRom)) {
            parentRomName = Zip.readAllBytes(pathToParentRom).keySet().stream().iterator().next();
        } else {
            parentRomName = PathUtil.getName(pathToParentRom);
        }
        out = out.resolve(parentRomName);
        if (!Files.exists(out)) {
            Files.createDirectories(out);
        }
        String romhackRomName = null;
        if (PathUtil.isZip(pathToRomhackRom)) {
            romhackRomName = Zip.readAllBytes(pathToRomhackRom).keySet().stream().iterator().next();
        } else {
            romhackRomName = PathUtil.getName(pathToRomhackRom);
        }
        out = out.resolve(romhackRomName);
        if (!Files.exists(out)) {
            Files.createDirectories(out);
        }

        // Create romhack.json
        Info info = new Info(null, null, null, null, null, null, null);
        Provenance provenance = new Provenance("Unknown", "YYYY-MM-DD", null);
        byte[] romhackRomBytes = getBytes(pathToRomhackRom);
        Rom rom = new Rom((long) romhackRomBytes.length, Hashes.getCrc32(romhackRomBytes), Hashes.getMd5(romhackRomBytes), Hashes.getSha1(romhackRomBytes));
        String romhackName = PathUtil.getName(pathToRomhackRom);
        List<Patch> patches = new ArrayList<>();
        for (String patchAsString: StringUtil.substrings(romhackName, "[", "]", true)) {
            String[] collection2NameVersionAlt = patchAsString.split(" by ");
            String label = collection2NameVersionAlt[0];
            String nameVersionAlt = collection2NameVersionAlt[1];
            int indexOfVersion = nameVersionAlt.indexOf(" (v");
            String authors = nameVersionAlt.substring(0, indexOfVersion);
            String version = StringUtil.substring(nameVersionAlt, "(v", ")", true);
            String alternative = StringUtil.substring(nameVersionAlt, "(Alt", ")", true);
            List<String> authorsAsList = new ArrayList<>();
            for (String author:authors.split(",")) {
                authorsAsList.add(author.trim());
            }
            Patch patch = new Patch(authorsAsList, null, version, null, alternative, List.of(label));
            patches.add(patch);
        }
        Romhack romhack = new Romhack(info, provenance, rom, patches);
        RomhackReaderWriter romhackReaderWriter = new RomhackReaderWriter();
        String json = romhackReaderWriter.write(romhack);
        json = json.replace("\"status\": null,", "\"status\": \"Fully Playable | Unfinished\",");
        json = json.replace("\"source\": null", "\"source\": \"Trusted | null\"");

        Files.write(out.resolve("romhack.json"), json.getBytes(StandardCharsets.UTF_8));

        // Create romhack.bps
        byte[] parentRomBytes = getBytes(pathToParentRom);
        MarcFile parentRom = new MarcFile(parentRomBytes);
        MarcFile romhackRom = new MarcFile(romhackRomBytes);
        boolean useDeltaMode = (parentRomBytes.length < MAX_MB_FOR_DELTA && romhackRomBytes.length < MAX_MB_FOR_DELTA);
        BPS bps = BPS.createBPSFromFiles(parentRom, romhackRom, useDeltaMode);
        MarcFile romhackBPS = bps.export();
        romhackBPS.save(out.resolve("romhack.bps"));

        //Create romhack-original
        Files.createDirectories(out.resolve("romhack-original"));
    }

    private static byte[] getBytes(Path path) throws IOException {
        byte[] bytes = null;
        if (PathUtil.isZip(path)) {
            bytes = Zip.readAllBytesOneFile(path);
        } else {
            bytes = Files.readAllBytes(path);
        }
        return bytes;
    }
}
