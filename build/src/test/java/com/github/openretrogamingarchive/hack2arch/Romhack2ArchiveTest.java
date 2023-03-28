package com.github.openretrogamingarchive.hack2arch;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

public class Romhack2ArchiveTest {

    Path original = Path.of("test-data","Final Fantasy V (Japan).zip");
    Path modified = Path.of("test-data","Final Fantasy V - Legend of the Crystals (Japan) [T-En by RPGe v1.10] [Add by Spooniest v2.1] [Add by noisecross v1.0].zip");

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
}
