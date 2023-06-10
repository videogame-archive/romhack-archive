package com.github.videogamearchive.hack2release;

import com.github.videogamearchive.community.rhdn.RHDNResource;
import com.github.videogamearchive.model.*;
import com.github.videogamearchive.model.json.ReleaseMapper;
import com.github.videogamearchive.model.validator.ReleaseValidator;
import com.github.videogamearchive.rompatcher.MarcFile;
import com.github.videogamearchive.rompatcher.formats.BPS;
import com.github.videogamearchive.util.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Romhack2Release {
    public static DateTimeFormatter archiveFormat = DateTimeFormatter.ISO_LOCAL_DATE.withLocale(Locale.ENGLISH).withZone(ZoneId.systemDefault());

    private static final Map<String, PatchResource> supportedResources = Map.of("https://www.romhacking.net/", new RHDNResource());
    public static void main(String[] args) throws Exception {
        if (args.length < 4 ||
                (args[0].equals("--no-bps") && args.length < 5)) {
            help();
        } else {
            int index = (args[0].equals("--no-bps"))?1:0;
            boolean disableBPS = index == 1;
            String retrievedBy = args[index + 0];
            Path parent = Path.of(args[index + 1]);
            Path romhack = Path.of(args[index + 2]);
            Path outputFolder = Path.of(args[index + 3]);
            List<String> urls = new ArrayList<>();
            for (int i = index + 4; i < args.length; i++) {
                if (args[i].startsWith("http://") || args[i].startsWith("https://")) {
                    urls.add(args[i]);
                }
            }
            if (Files.size(parent) > MAX_MB_FOR_LINEAR || Files.size(romhack) > MAX_MB_FOR_LINEAR) {
                throw new RuntimeException("rom is too big for this tool to handle. Please use this tool with --no-bps flag and flips to generate the bps.");
            }
            process(disableBPS, parent, romhack, outputFolder, retrievedBy, urls, false);
        }
    }

    private static void help() {
        System.out.println("usage: ");
        System.out.println("\t\t java -jar romhack2archive.jar [--no-bps] \"retrievedBy\" \"parentRom\" \"romhackRom\" \"outputDir\" [\"patchURL1\"] ... [\"patchURLN\"]");
        System.out.println("- Currently only romhacking.net urls are supported, other don't retrieve extra information.");
        System.out.println("- URL information takes precedence over filename information.");
    }

    static private long MAX_MB_FOR_DELTA = 16777216; // 16 Mb
    static private long MAX_MB_FOR_LINEAR = 536870911; // 512 Mb

    public static Path process(boolean disableBPS, Path pathToParentRom, Path pathToRomhackRom, Path outDir, String retrievedBy, List<String> urls, boolean keepGivenFilename) throws Exception {
        System.out.println("maxMemory: " + Runtime.getRuntime().maxMemory());
        // Create romhack.json
        Info info = new Info(null, null, null, null, null, null, null);
        Provenance provenance = new Provenance(retrievedBy, archiveFormat.format(Instant.now()), null, null);
        byte[] romhackRomBytes = getBytes(pathToRomhackRom);
        Rom rom = new Rom((long) romhackRomBytes.length, Hashes.getCrc32(romhackRomBytes), Hashes.getMd5(romhackRomBytes), Hashes.getSha1(romhackRomBytes));
        String romhackName = PathUtil.getName(pathToRomhackRom);
        List<Hack> patches = new ArrayList<>();
        for (String patchAsString: StringUtil.substrings(romhackName, "[", "]", true)) {
            String[] collection2NameVersionOpt = patchAsString.split(" by ");
            List<Label> labels = new ArrayList<>();
            for (String labelAsString : Arrays.asList(collection2NameVersionOpt[0].split("\\s*,\\s*"))) {
                labels.add(Enum.valueOf(Label.class, labelAsString.replace(" ", "").replace("-", "")));
            }
            String nameVersionOpt = collection2NameVersionOpt[1];
            int indexOfVersion = nameVersionOpt.indexOf(" (v");
            String authors = nameVersionOpt.substring(0, indexOfVersion);
            String version = StringUtil.substring(nameVersionOpt, "(v", ")", true);
            String shortOptions = StringUtil.substring(nameVersionOpt, "(Opt ", ")", true);
            List<String> authorsAsList = new ArrayList<>();
            for (String author:authors.split(",")) {
                authorsAsList.add(author.trim());
            }
            Hack patch = null;
            String url = null;
            if (urls.size() > patches.size()) {
                url = urls.get(patches.size());
                Hack urlPatch = null;
                for (String domain:supportedResources.keySet()) {
                    if (url.startsWith(domain)) {
                        PatchResource patchResource = supportedResources.get(domain);
                        urlPatch = patchResource.getPatch(url);
                    }
                }
                if (urlPatch != null) {
                    patch = new Hack(null, urlPatch.name(), urlPatch.authors(), urlPatch.shortAuthors(), urlPatch.url(), null, urlPatch.version(), urlPatch.releaseDate(), null, shortOptions, labels, null);
                }
            }
            if (patch == null) {
                patch = new Hack(null, null, authorsAsList, null,url, List.of(), version, null, null, shortOptions, labels, List.of());
            }
            patches.add(patch);
        }
        Release romhack = new Release(null, info, provenance, rom, patches);
        ReleaseMapper romhackReaderWriter = new ReleaseMapper();
        String json = romhackReaderWriter.write(romhack);
        json = json.replace("\"status\": null,", "\"status\": \"Fully Playable | Incomplete\",");
        if (!urls.isEmpty()) {
            json = json.replace("\"source\": null", "\"source\": \"Trusted\"");
        } else {
            json = json.replace("\"source\": null", "\"source\": \"Trusted | null\"");
        }
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

        String expectedFolderNamePostfix = ReleaseValidator.getExpectedFolderNamePostfix(romhack);

        if (keepGivenFilename) {
            romhackRomName = romhackName;
        } else {
            int indexOfName = romhackName.indexOf(" [");
            romhackRomName = romhackName.substring(0, indexOfName) + expectedFolderNamePostfix + "." + PathUtil.getExtension(romhackRomName);
        }

        out = out.resolve(romhackRomName);
        if (!Files.exists(out)) {
            Files.createDirectories(out);
        }
        //
        // Finish - Create directory tree
        //

        Files.write(out.resolve("romhack.json"), json.getBytes(StandardCharsets.UTF_8));

        // Create romhack.bps
        if (!disableBPS) {
            byte[] parentRomBytes = getBytes(pathToParentRom);
            MarcFile parentRom = new MarcFile(parentRomBytes);
            MarcFile romhackRom = new MarcFile(romhackRomBytes);
            boolean useDeltaMode = (parentRomBytes.length < MAX_MB_FOR_DELTA && romhackRomBytes.length < MAX_MB_FOR_DELTA);
            BPS bps = BPS.createBPSFromFiles(parentRom, romhackRom, useDeltaMode);
            MarcFile romhackBPS = bps.export();
            romhackBPS.save(out.resolve("romhack.bps"));
        }
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
