package com.github.videogamearchive.hack2arch;

import com.github.videogamearchive.community.rhdn.Resource;
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
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Romhack2Archive {
    public static DateTimeFormatter archiveFormat = DateTimeFormatter.ISO_LOCAL_DATE.withLocale(Locale.ENGLISH).withZone(ZoneId.systemDefault());

    public static void main(String[] args) throws Exception {
        if (args.length < 3) {
            help();
        } else {
            Path parent = Path.of(args[0]);
            Path romhack = Path.of(args[1]);
            Path outputFolder = Path.of(args[2]);
            List<String> urls = new ArrayList<>();
            for (int i = 3; i < args.length; i++) {
                if (args[i].startsWith("https://www.romhacking.net/") && args[i].endsWith("/")) {
                    urls.add(args[i]);
                }
            }
            process(parent, romhack, outputFolder, null, urls);
        }
    }

    private static void help() {
        System.out.println("usage: ");
        System.out.println("\t\t java -jar romhack2archive.jar \"parentRom\" \"romhackRom\" \"outputDir\" [\"patchURL1\"] ... [\"patchURLN\"]");
        System.out.println("- Currently only romhacking.net urls are supported, other are ignored.");
        System.out.println("- URL information takes precedence over filename information.");
    }

    static private long MAX_MB_FOR_DELTA = 50331648;
    public static Path process(Path pathToParentRom, Path pathToRomhackRom, Path outDir, String username, List<String> urls) throws Exception {
        System.out.println("maxMemory: " + Runtime.getRuntime().maxMemory());
        // Create romhack.json
        Info info = new Info(null, null, null, null, null, null, null);
        Provenance provenance = new Provenance(username, archiveFormat.format(Instant.now()), null, null);
        byte[] romhackRomBytes = getBytes(pathToRomhackRom);
        Rom rom = new Rom((long) romhackRomBytes.length, Hashes.getCrc32(romhackRomBytes), Hashes.getMd5(romhackRomBytes), Hashes.getSha1(romhackRomBytes));
        String romhackName = PathUtil.getName(pathToRomhackRom);
        List<Patch> patches = new ArrayList<>();
        for (String patchAsString: StringUtil.substrings(romhackName, "[", "]", true)) {
            String[] collection2NameVersionAlt = patchAsString.split(" by ");
            List<String> labels = Arrays.asList(collection2NameVersionAlt[0].split("\s*,\s*"));
            String nameVersionAlt = collection2NameVersionAlt[1];
            int indexOfVersion = nameVersionAlt.indexOf(" (v");
            String authors = nameVersionAlt.substring(0, indexOfVersion);
            String version = StringUtil.substring(nameVersionAlt, "(v", ")", true);
            String alternative = StringUtil.substring(nameVersionAlt, "(Alt ", ")", true);
            List<String> authorsAsList = new ArrayList<>();
            for (String author:authors.split(",")) {
                authorsAsList.add(author.trim());
            }
            Patch patch = null;
            if (urls.size() > patches.size()) {
                Patch urlPatch = Resource.getPatch(urls.get(patches.size()));
                if (urlPatch != null) {
                    patch = new Patch(urlPatch.authors(), urlPatch.shortAuthors(), urlPatch.url(), List.of(), urlPatch.version(), urlPatch.releaseDate(), alternative, labels);
                }
            }
            if (patch == null) {
                patch = new Patch(authorsAsList, null,null, List.of(), version, null, alternative, labels);
            }
            patches.add(patch);
        }
        Romhack romhack = new Romhack(info, provenance, rom, patches);
        RomhackReaderWriter romhackReaderWriter = new RomhackReaderWriter();
        String json = romhackReaderWriter.write(romhack);
        json = json.replace("\"status\": null,", "\"status\": \"Fully Playable | Unfinished\",");
        json = json.replace("\"source\": null", "\"source\": \"Trusted | null\"");

        //
        // Start - Create directory tree
        //
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

        String expectedFolderNamePostfix = RomhackValidator.getExpectedFolderNamePostfix(romhack);
        int indexOfName = romhackName.indexOf(" [");
        romhackRomName = romhackName.substring(0, indexOfName) + expectedFolderNamePostfix + "." + PathUtil.getExtension(romhackRomName);
        out = out.resolve(romhackRomName);
        if (!Files.exists(out)) {
            Files.createDirectories(out);
        }
        //
        // Finish - Create directory tree
        //

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
        Path romhackOriginal = out.resolve("romhack-original");
        Files.createDirectories(romhackOriginal);
        for (int folderNumber = 1; folderNumber<= patches.size(); folderNumber++) {
            Files.createDirectories(romhackOriginal.resolve(Integer.toString(folderNumber)));
        }

        return out;
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
