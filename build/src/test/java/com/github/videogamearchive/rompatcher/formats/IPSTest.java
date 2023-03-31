/* Apache 2 License, Copyright (c) 2023 Juan Fuentes, based on Rom Patcher JS by Marc Robledo */
package com.github.videogamearchive.rompatcher.formats;

import com.github.videogamearchive.rompatcher.MarcFile;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

public class IPSTest {
    Path original = Path.of("test-data","3x3 Eyes - Juuma Houkan (Japan).sfc");
    Path modified = Path.of("test-data","3x3 Eyes - Juuma Houkan (Japan) [T-En by Atomizer_Zero v1.01].sfc");
    Path patch = Path.of("test-data","3x3 Eyes - Juuma Houkan (Japan) [T-En by Atomizer_Zero v1.01].ips");

    Path tempFile = Path.of("temp-" + System.currentTimeMillis());
    @AfterEach
    public void cleanup() throws IOException {
        if (Files.exists(tempFile)) {
            Files.delete(tempFile);
        }
    }
    @Test
    public void testCreate() throws Exception {
        IPS ips = IPS.createIPSFromFiles(new MarcFile(original), new MarcFile(modified));
        MarcFile export = ips.export();
        Path testPath = tempFile;
        export.save(testPath);

        byte[] expected = Files.readAllBytes(patch);
        byte[] actual = Files.readAllBytes(testPath);

        assertArrayEquals(expected, actual);
    }
    @Test
    public void testParse() throws Exception {
        IPS ips = IPS.parseIPSFile(new MarcFile(patch));
        MarcFile export = ips.export();
        Path testPath = tempFile;
        export.save(testPath);

        byte[] expected = Files.readAllBytes(patch);
        byte[] actual = Files.readAllBytes(testPath);

        assertArrayEquals(expected, actual);
    }

    @Test
    public void testApply() throws Exception {
        IPS ips = IPS.parseIPSFile(new MarcFile(patch));

        MarcFile actualModified = ips.apply(new MarcFile(original));
        Path actualModifiedPath = tempFile;
        actualModified.save(actualModifiedPath);

        byte[] expected = Files.readAllBytes(modified);
        byte[] actual = Files.readAllBytes(actualModifiedPath);

        assertArrayEquals(expected, actual);
    }
}
