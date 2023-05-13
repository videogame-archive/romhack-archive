package com.github.videogamearchive;

import com.github.videogamearchive.rompatcher.MarcFile;
import com.github.videogamearchive.rompatcher.formats.BPS;
import com.github.videogamearchive.util.PathUtil;
import com.github.videogamearchive.util.Zip;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class GBA {

    private static Map<String, String> parentsTable = new HashMap<>();
    static {
        /*
         * Fire Emblem 6 versions
         */
        parentsTable.put("AFEJ", "0001 - Fire Emblem - Fuuin no Tsurugi (Japan).zip");
        //parentsTable.put("AFEJ", "0009 - Fire Emblem - Rekka no Ken (Japan) (Beta) (06-02-2003).zip");
        //parentsTable.put("AFEJ", "0010 - Fire Emblem - Rekka no Ken (Japan) (Beta) (19-02-2003).zip");
        parentsTable.put("AFEC", "0016 - Fire Emblem - Fuuin no Tsurugi (China)[iQue][CHS].zip");

        /*
         * Fire Emblem 7 versions
         */
        parentsTable.put("AE7J", "0002 - Fire Emblem - Rekka no Ken (Japan).zip");
        parentsTable.put("AE7E", "0003 - Fire Emblem (USA, Australia).zip");
        parentsTable.put("AE7X", "0004 - Fire Emblem (Europe) (En,Fr,De).zip");
        parentsTable.put("AE7Y", "0005 - Fire Emblem (Europe) (En,Es,It).zip");
        //parentsTable.put("AE7J", "0011 - Fire Emblem - Seima no Kouseki (Japan) (Beta).zip");
        //parentsTable.put("AE7E", "0013 - Fire Emblem (USA) (Wii U Virtual Console).zip");
        //parentsTable.put("AE7X", "0015 - Fire Emblem (Europe) (En,Fr,De) (Wii U Virtual Console).zip");

        /*
         * Fire Emblem 8 versions
         */
        parentsTable.put("BE8J", "0006 - Fire Emblem - Seima no Kouseki (Japan).zip");
        parentsTable.put("BE8E", "0007 - Fire Emblem - The Sacred Stones (USA, Australia).zip");
        parentsTable.put("BE8P", "0008 - Fire Emblem - The Sacred Stones (Europe) (En,Fr,De,Es,It).zip");
        //parentsTable.put("BE8E", "0012 - Fire Emblem - The Sacred Stones (USA) (Wii U Virtual Console).zip");
        //parentsTable.put("BE8P", "0014 - Fire Emblem - The Sacred Stones (Europe) (En,Fr,De,Es,It) (Wii U Virtual Console).zip");

        /*
         * Stats
         */
        // AFEJ=135
        // AE7J=38
        // AE7E=218
        // BE8J=261
        // BE8E=562
    }

    private static Map<String, String> parentsTableNoIntro = new HashMap<>();
    static {
        parentsTableNoIntro.put("AFEJ", "../FE-ROM-Hack-Repo/nointro/Fire Emblem - Fuuin no Tsurugi (Japan).zip");
        parentsTableNoIntro.put("AE7J", "../FE-ROM-Hack-Repo/nointro/Fire Emblem - Rekka no Ken (Japan).zip");
        parentsTableNoIntro.put("AE7E", "../FE-ROM-Hack-Repo/nointro/Fire Emblem (USA, Australia).zip");
        parentsTableNoIntro.put("BE8J", "../FE-ROM-Hack-Repo/nointro/Fire Emblem - Seima no Kouseki (Japan).zip");
        parentsTableNoIntro.put("BE8E", "../FE-ROM-Hack-Repo/nointro/Fire Emblem - The Sacred Stones (USA, Australia).zip");
    }

    public static void bpsUnique(String[] args) throws IOException {
        File roms = new File("../FE-ROM-Hack-Repo/Unique");
        for (File rom:roms.listFiles()) {
            for (String pathToParent:parentsTableNoIntro.values()) {
                Path patch = Path.of("../FE-ROM-Hack-Repo/Unique/" + PathUtil.getNameWithoutExtension(rom.getName()) + "-" + PathUtil.getNameWithoutExtension(pathToParent) + ".bps");
                byte[] parent = Zip.readAllBytesOneFile(Path.of(pathToParent));
                byte[] hack = Zip.readAllBytesOneFile(rom.toPath());
                if (!Files.exists(patch)) {
                    BPS createBPS = BPS.createBPSFromFiles(new MarcFile(parent), new MarcFile(hack), false);
                    MarcFile export = createBPS.export();
                    export.save(patch);
                }
            }
        }
    }

    public static void main(String[] args) throws IOException {
        Map<String, Integer> counts = new HashMap<>();
        File roms = new File("../FE-ROM-Hack-Repo/ROMs");
        for (File rom:roms.listFiles()) {
            try {
                String numberAsString = rom.getName().substring(0, 4);
                if (Integer.parseInt(numberAsString) < 19) {
                    continue;
                }
                String gameCode = getGameCode(rom);
                Integer count = counts.get(gameCode);
                if (count == null) {
                    count = 1;
                } else {
                    count++;
                }
                counts.put(gameCode, count);

                if (!parentsTableNoIntro.containsKey(gameCode)) {
                    System.out.println("UNIQUE" + "\t" + rom);
                } else {
                    Path patch = Path.of("../FE-ROM-Hack-Repo-bps-torrentzip/", PathUtil.getNameWithoutExtension(rom.getName()) + ".bps.zip");
                    System.out.println(numberAsString + "\t" + rom + "\t" + gameCode);
                    if (!Files.exists(patch)) {
                        byte[] parent = Zip.readAllBytesOneFile(Path.of(parentsTableNoIntro.get(gameCode)));
                        byte[] hack = Zip.readAllBytesOneFile(rom.toPath());
                        BPS createBPS = BPS.createBPSFromFiles(new MarcFile(parent), new MarcFile(hack), false);
                        MarcFile export = createBPS.export();
                        export.save(patch);
                    }
                    String parentName = PathUtil.getNameWithoutExtension(parentsTableNoIntro.get(gameCode));
                    Path parentPath = Path.of("../FE-ROM-Hack-Repo-bps-torrentzip/" + parentName);
                    Files.createDirectories(parentPath);
                    Path dst = parentPath.resolve(PathUtil.getName(patch));
                    System.out.println(patch + " -> " + dst);
                    Files.move(patch, dst);
//                    BPS testBPS = BPS.parseBPSFile(new MarcFile(patch));
//                    MarcFile hackAsMarc = testBPS.apply(new MarcFile(parent), true);
//                    byte[] hackFromPatchParent = hackAsMarc.save();
//                    boolean equals = Arrays.equals(hack, hackFromPatchParent);
//                    if (!equals) {
//                        System.out.println("NOTEQUALS" + "\t" + rom);
//                    }
                }
            } catch (Exception ex) {
                System.out.println("ERROR" + "\t" + rom);
            }
        }
    }

    private static String getGameCode(File rom) throws IOException {
        byte[] bytes = Zip.readAllBytesOneFile(rom.toPath());
        byte[] gameTitleBytes = Arrays.copyOfRange(bytes, 160, 160 + 12);
        String gameTitle = new String(gameTitleBytes, StandardCharsets.US_ASCII);
        byte[] gameCodeBytes = Arrays.copyOfRange(bytes, 172, 172 + 4);
        String gameCode = new String(gameCodeBytes, StandardCharsets.US_ASCII);
        return gameCode;
    }
}

