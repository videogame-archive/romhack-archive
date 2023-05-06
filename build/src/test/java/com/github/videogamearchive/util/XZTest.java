package com.github.videogamearchive.util;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

public class XZTest {
    Path linearPatch = Path.of("test-data", "Lolo Test ROM v1.1.linear.bps");

    Path tempFile = Path.of("temp-" + System.currentTimeMillis());
    @AfterEach
    public void cleanup() throws IOException {
        if (Files.exists(tempFile)) {
            Files.delete(tempFile);
        }
    }

    @Test
    public void write() throws IOException {
        byte[] bytes = Files.readAllBytes(linearPatch);
        XZ.write(tempFile, bytes);
        byte[] bytesXZ = Files.readAllBytes(tempFile);
        assertTrue(bytesXZ.length < bytes.length);
    }

    @Test
    public void read() throws IOException {
        write();
        byte[] bytes = Files.readAllBytes(linearPatch);
        byte[] bytesFromXZ = XZ.readAllBytes(tempFile);
        assertArrayEquals(bytes, bytesFromXZ);
    }
}
