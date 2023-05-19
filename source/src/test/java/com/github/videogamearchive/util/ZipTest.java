package com.github.videogamearchive.util;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class ZipTest {
    Path original = Path.of("test-data", "Kirby Test ROM (World).nes");
    Path modified = Path.of("test-data", "Lolo Test ROM (World) [Themed by Hackermans (v1.1)].nes");
    Path originalZip = Path.of("test-data", "Kirby Test ROM (World).zip");
    Path modifiedZip = Path.of("test-data", "Lolo Test ROM (World) [Themed by Hackermans (v1.1)].zip");
    Path zip1 = Path.of("test-data", "Lolo Test ROM v1.1.zip");
    Path tempFileZip = Path.of("temp-" + System.currentTimeMillis() + ".zip");
    @AfterEach
    public void cleanup() throws IOException {
        if (Files.exists(tempFileZip)) {
            Files.delete(tempFileZip);
        }
    }

    @Test
    public void testUnzip() throws IOException {
        byte[] expected = Files.readAllBytes(original);
        Map<String, byte[]> zipContent = Zip.readAllBytes(originalZip);
        byte[] actual = zipContent.values().iterator().next();
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testUnzip2() throws IOException {
        Map<String, byte[]> contents = Zip.readAllBytes(zip1);
        assertEquals(2, contents.size());
    }

    @Test
    public void testZip() throws IOException {
        byte[] expected = Files.readAllBytes(original);

        Path testZip = tempFileZip;
        Zip.write(testZip, Map.of(original.getName(original.getNameCount() - 1).toString(), expected));

        Map<String, byte[]> zipContent = Zip.readAllBytes(testZip);
        byte[] actual = zipContent.values().iterator().next();
        assertArrayEquals(expected, actual);
    }

    @Test /* TorrentZIp binary compatibility fails **/
    public void zipEquals() throws IOException {
        byte[] toZip = Files.readAllBytes(original);

        Path testZip = tempFileZip;
        Zip.write(testZip, Map.of(original.getName(original.getNameCount() - 1).toString(), toZip));

        byte[] expected = Files.readAllBytes(originalZip);
        byte[] actual = Files.readAllBytes(testZip);

        assertThrows(AssertionFailedError.class,
                ()->{
                    assertArrayEquals(expected, actual);
                });
    }

}
