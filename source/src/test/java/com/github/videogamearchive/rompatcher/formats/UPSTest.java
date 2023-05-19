/* Apache 2 License, Copyright (c) 2023 Juan Fuentes, based on Rom Patcher JS by Marc Robledo */
package com.github.videogamearchive.rompatcher.formats;

import com.github.videogamearchive.rompatcher.MarcFile;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

public class UPSTest {
    Path original = Path.of("test-data", "Kirby Test ROM (World).nes");
    Path modified = Path.of("test-data", "Lolo Test ROM (World) [Themed by Hackermans (v1.1)].nes");
    Path patch = Path.of("test-data", "Lolo Test ROM v1.1.ups");

    Path tempFile = Path.of("temp-" + System.currentTimeMillis());
    @AfterEach
    public void cleanup() throws IOException {
        if (Files.exists(tempFile)) {
            Files.delete(tempFile);
        }
    }

    @Test
    public void testCreate() throws Exception {
        UPS ups = UPS.createUPSFromFiles(new MarcFile(original), new MarcFile(modified));
        MarcFile export = ups.export();
        Path testPath = tempFile;
        export.save(testPath);

        byte[] expected = Files.readAllBytes(patch);
        byte[] actual = Files.readAllBytes(testPath);

        assertArrayEquals(expected, actual);
    }
    @Test
    public void testParse() throws Exception {
        UPS ups = UPS.parseUPSFile(new MarcFile(patch));
        MarcFile export = ups.export();
        Path testPath = tempFile;
        export.save(testPath);

        byte[] expected = Files.readAllBytes(patch);
        byte[] actual = Files.readAllBytes(testPath);

        assertArrayEquals(expected, actual);
    }

    @Test
    public void testApply() throws Exception {
        UPS ups = UPS.parseUPSFile(new MarcFile(patch));

        MarcFile actualModified = ups.apply(new MarcFile(original), true);
        Path actualModifiedPath = tempFile;
        actualModified.save(actualModifiedPath);

        byte[] expected = Files.readAllBytes(modified);
        byte[] actual = Files.readAllBytes(actualModifiedPath);

        assertArrayEquals(expected, actual);
    }
}
