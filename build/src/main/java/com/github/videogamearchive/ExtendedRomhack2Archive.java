package com.github.videogamearchive;

import com.github.videogamearchive.hack2arch.Romhack2Archive;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class ExtendedRomhack2Archive {

    public static List<Path> noIntro = List.of(Path.of("../Nintendo - Super Nintendo Entertainment System (no-intro)"),
                                                Path.of("../Nintendo - Satellaview (no-intro)"));
    public static Path chadMasterSet = Path.of("../Nintendo - Super Famicom [T-En] Collection (15-04-2023)");
    public static Path raSet = Path.of("../Nintendo - Super Nintendo Entertainment System");
    public static Path rhdnDump = Path.of("../../romhack-archive-crawler/community-romhacking.net/raw/www.romhacking.net");

    public static void main(String[] args) throws Exception {
        String domain = "https://www.romhacking.net/";
        List<String> domainPatches = List.of("https://www.romhacking.net/translations/304/", "https://www.romhacking.net/translations/6723/");
        //String domain = null;
        //List<String> domainPatches = List.of();
        // https://patreon.com/kandowontu and twitter.com/kandowontu
        String parent = "Cyber Knight (Japan).zip";
        String romhack = "Cyber Knight (Japan) [T-En by Aeon Genesis, Gideon Zhi, Sogabe, Cidolfas, Taskforce (v1.01)] [Fix by mteam (v1.0) (Opt Easy Mode)].zip";
        String username = "JuMaFuSe";
        process(domain, domainPatches, parent, romhack, username);
    }

    private static void process(String domain, List<String> domainPatches, String parent, String romhack, String userName) throws Exception {
        Path parentRom = null;
        for (Path possibleParent:noIntro) {
            Path possibleParentRom = possibleParent.resolve(parent);
            if (Files.exists(possibleParentRom)) {
                parentRom = possibleParentRom;
            }
        }

        Path romhackFolder = Romhack2Archive.process(
                false,
                parentRom,
                chadMasterSet.resolve(romhack),
                raSet,
                userName,
                domainPatches
        );

        for (int patchIndex = 0; patchIndex < domainPatches.size(); patchIndex++) {
            String patch = domainPatches.get(patchIndex);
            if (patch.startsWith(domain)) {
                File downloadFolder = rhdnDump.resolve("download").resolve(patch.substring(domain.length())).toFile();
                File[] files = downloadFolder.listFiles();
                for (File file : files) {
                    Files.copy(file.toPath(), romhackFolder.resolve("romhack-original").resolve(Integer.toString(patchIndex + 1)).resolve(file.getName()));
                }
            }
        }
    }


}
