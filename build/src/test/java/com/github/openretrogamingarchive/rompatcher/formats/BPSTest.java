/* Apache 2 License, Copyright (c) 2023 Juan Fuentes, based on Rom Patcher JS by Marc Robledo */
package com.github.openretrogamingarchive.rompatcher.formats;

import com.github.openretrogamingarchive.rompatcher.MarcFile;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BPSTest {
    Path original = Path.of("test-data","3x3 Eyes - Juuma Houkan (Japan).sfc");
    Path modified = Path.of("test-data","3x3 Eyes - Juuma Houkan (Japan) [T-En by Atomizer_Zero v1.01].sfc");
    Path linearPatch = Path.of("test-data","3x3 Eyes - Juuma Houkan (Japan) [T-En by Atomizer_Zero v1.01]-linear.bps");
    Path deltaPatch = Path.of("test-data","3x3 Eyes - Juuma Houkan (Japan) [T-En by Atomizer_Zero v1.01]-delta.bps");

    Path tempFile = Path.of("temp-" + System.currentTimeMillis());
    @AfterEach
    public void cleanup() throws IOException {
        if (Files.exists(tempFile)) {
            Files.delete(tempFile);
        }
    }

    @Test
    public void testCreateLinear() throws Exception {
        BPS bps = BPS.createBPSFromFiles(new MarcFile(original), new MarcFile(modified), false);
        MarcFile export = bps.export();
        Path testPath = tempFile;
        export.save(testPath);

        byte[] expected = Files.readAllBytes(linearPatch);
        byte[] actual = Files.readAllBytes(testPath);

        assertArrayEquals(expected, actual);
    }
    @Test
    public void testParseLinear() throws Exception {
        BPS bps = BPS.parseBPSFile(new MarcFile(linearPatch));
        MarcFile export = bps.export();
        Path testPath = tempFile;
        export.save(testPath);

        byte[] expected = Files.readAllBytes(linearPatch);
        byte[] actual = Files.readAllBytes(testPath);

        assertArrayEquals(expected, actual);
    }
    @Test
    public void testApplyLinear() throws Exception {
        BPS bps = BPS.parseBPSFile(new MarcFile(linearPatch));

        assertTrue(bps.validateSource(new MarcFile(original)));
        MarcFile actualModified = bps.apply(new MarcFile(original), true);
        Path actualModifiedPath = tempFile;
        actualModified.save(actualModifiedPath);

        byte[] expected = Files.readAllBytes(modified);
        byte[] actual = Files.readAllBytes(actualModifiedPath);

        assertArrayEquals(expected, actual);
    }
    @Test
    public void testCreateDelta() throws Exception {
        BPS bps = BPS.createBPSFromFiles(new MarcFile(original), new MarcFile(modified), true);
        MarcFile export = bps.export();
        Path testPath = tempFile;
        export.save(testPath);

        byte[] expected = Files.readAllBytes(deltaPatch);
        byte[] actual = Files.readAllBytes(testPath);

        assertArrayEquals(expected, actual);
    }
    @Test
    public void testParseDelta() throws Exception {
        BPS bps = BPS.parseBPSFile(new MarcFile(deltaPatch));
        MarcFile export = bps.export();
        Path testPath = tempFile;
        export.save(testPath);

        byte[] expected = Files.readAllBytes(deltaPatch);
        byte[] actual = Files.readAllBytes(testPath);

        assertArrayEquals(expected, actual);
    }
    @Test
    public void testApplyDelta() throws Exception {
        BPS bps = BPS.parseBPSFile(new MarcFile(deltaPatch));

        assertTrue(bps.validateSource(new MarcFile(original)));
        MarcFile actualModified = bps.apply(new MarcFile(original), true);
        Path actualModifiedPath = tempFile;
        actualModified.save(actualModifiedPath);

        byte[] expected = Files.readAllBytes(modified);
        byte[] actual = Files.readAllBytes(actualModifiedPath);

        assertArrayEquals(expected, actual);
    }
}
