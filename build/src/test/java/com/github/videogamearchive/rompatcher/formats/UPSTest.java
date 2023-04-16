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
    Path original = Path.of("..","..", "romhack-archive-private", "test-data","3x3 Eyes - Juuma Houkan (Japan).sfc");
    Path modified = Path.of("..","..", "romhack-archive-private", "test-data","3x3 Eyes - Juuma Houkan (Japan) [T-En by Atomizer_Zero (v1.01)].sfc");
    Path patch = Path.of("..","..", "romhack-archive-private", "test-data","3x3 Eyes - Juuma Houkan (Japan) [T-En by Atomizer_Zero (v1.01)].ups");

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
