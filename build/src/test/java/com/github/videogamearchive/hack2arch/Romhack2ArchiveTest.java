package com.github.videogamearchive.hack2arch;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

public class Romhack2ArchiveTest {

    Path original = Path.of("..","..", "romhack-archive-private", "test-data","Final Fantasy V (Japan).zip");
    Path modified = Path.of("..","..", "romhack-archive-private", "test-data","Final Fantasy V - Legend of the Crystals (Japan) [T-En by RPGe v1.10] [Add by Spooniest v2.1] [Add by noisecross v1.0].zip");

    Path original2 = Path.of("..","..", "romhack-archive-private", "test-data-2","Super_Mario_Land_2_-_6_Golden_Coins_USA_Europe.gb");
    Path modified2 = Path.of("..","..", "romhack-archive-private", "test-data-2","Super_Mario_Land_2_-_6_Golden_Coins_USA_Europe_patched.gb");

    Path originalBig = Path.of("..","..", "romhack-archive-private", "test-data-big","Yu-Gi-Oh! Forbidden Memories (USA).bin");
    Path modifiedBig = Path.of("..","..", "romhack-archive-private", "test-data-big","Yu-Gi-Oh! Forbidden Memories (USA) - 15 Cards drop mod  [Add by Unknown v2019.04.05].bin");

    Path originalBig2 = Path.of("..","..", "romhack-archive-private", "test-data-big-2","Tomb Raider - The Last Revelation (USA) (Rev 1).bin");
    Path modifiedBig2 = Path.of("..","..", "romhack-archive-private", "test-data-big-2","Tomb Raider - The Last Revelation (USA) (Rev 1) - TR4 Improved [Fix by Farglior v1.0].bin");

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
    public void datCreatorTest() throws Exception {
        Romhack2Archive.main(new String[]{
                original.toString(),
                modified.toString(),
                tempFile.toString()});
    }

    @Test
    public void dat2CreatorTest() throws Exception {
        Romhack2Archive.main(new String[]{
                original2.toString(),
                modified2.toString(),
                tempFile.toString()});
    }

    @Test
    public void datBigCreatorTest() throws Exception {
        Romhack2Archive.main(new String[]{
                originalBig.toString(),
                modifiedBig.toString(),
                tempFile.toString()});
    }

    @Test
    public void datBig2CreatorTest() throws Exception {
        Romhack2Archive.main(new String[]{
                originalBig2.toString(),
                modifiedBig2.toString(),
                tempFile.toString()});
    }
}
