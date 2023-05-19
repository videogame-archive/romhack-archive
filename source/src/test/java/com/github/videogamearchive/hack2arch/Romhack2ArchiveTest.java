package com.github.videogamearchive.hack2arch;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

public class Romhack2ArchiveTest {

    Path original = Path.of("test-data", "Kirby Test ROM (World).nes");
    Path modified = Path.of("test-data", "Lolo Test ROM (World) [Themed by Hackermans (v1.1)].nes");
    Path modifiedMultiLabel = Path.of("test-data", "Lolo Test ROM - Cool Font (World) [Themed, T-Ja by Hackermans (v1.1)] [Reskin , T-En ,T-Es by Unknown (vUnknown).nes");

    Path original2 = Path.of("test-data", "Kirby Test ROM (World).zip");
    Path modified2 = Path.of("test-data", "Lolo Test ROM - Cool Font (World) [Themed by Hackermans (v1.1)] [Reskin by Unknown (vUnknown)].zip");

    Path originalBig = Path.of("..","..", "romhack-archive-private", "test-data-big","Yu-Gi-Oh! Forbidden Memories (USA).bin");
    Path modifiedBig = Path.of("..","..", "romhack-archive-private", "test-data-big","Yu-Gi-Oh! Forbidden Memories (USA) - 15 Cards drop mod  [Add by Unknown (v2019.04.05)].bin");

    Path originalBig2 = Path.of("..","..", "romhack-archive-private", "test-data-big-2","Tomb Raider - The Last Revelation (USA) (Rev 1).bin");
    Path modifiedBig2 = Path.of("..","..", "romhack-archive-private", "test-data-big-2","Tomb Raider - The Last Revelation (USA) (Rev 1) - TR4 Improved [Fix by Farglior (v1.0)].bin");

    Path tempFile = Path.of("temp-" + System.currentTimeMillis());

    private void deleteDir(File file) {
        File[] contents = file.listFiles();
        if (contents != null) {
            for (File f : contents) {
                if (! Files.isSymbolicLink(f.toPath())) {
                    deleteDir(f);
                }
            }
        }
        file.delete();
    }

    @AfterEach
    public void cleanup() {
        if (Files.exists(tempFile)) {
            deleteDir(tempFile.toFile());
        }
    }
    @Test
    public void romhackCreatorTestInfo() throws Exception {
        Romhack2Archive.main(new String[]{});
    }
    @Test
    public void romhackCreatorTest() throws Exception {
        Romhack2Archive.main(new String[]{
                "test",
                original.toString(),
                modified.toString(),
                tempFile.toString(),
                "https://www.romhacking.net/translations/353/",
                "https://www.romhacking.net/translations/2600/",
                "https://www.romhacking.net/translations/3499/"});
    }
    @Test
    public void romhackCreatorTestMulti() throws Exception {
        Romhack2Archive.main(new String[]{
                "test",
                original.toString(),
                modifiedMultiLabel.toString(),
                tempFile.toString(),
                "https://www.romhacking.net/translations/353/",
                "https://www.romhacking.net/translations/2600/",
                "https://www.romhacking.net/translations/3499/"});
    }

    @Test
    public void romhack2CreatorTest() throws Exception {
        Romhack2Archive.main(new String[]{
                "test",
                original2.toString(),
                modified2.toString(),
                tempFile.toString(),
                "https://www.romhacking.net/hacks/3784/"
        });
    }

//    @Test
//    public void romhackBigCreatorTest() throws Exception {
//        Romhack2Archive.main(new String[]{
//                "test",
//                originalBig.toString(),
//                modifiedBig.toString(),
//                tempFile.toString()});
//    }
//
//    @Test
//    public void romhackBig2CreatorTest() throws Exception {
//        Romhack2Archive.main(new String[]{
//                "test",
//                originalBig2.toString(),
//                modifiedBig2.toString(),
//                tempFile.toString()});
//    }
//
//    @Test
//    public void romhackBig2CreatorTestNoBPS() throws Exception {
//        Romhack2Archive.main(new String[]{
//                "--no-bps",
//                "test",
//                originalBig2.toString(),
//                modifiedBig2.toString(),
//                tempFile.toString()});
//    }
}
