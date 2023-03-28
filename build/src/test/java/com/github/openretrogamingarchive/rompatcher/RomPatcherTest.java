package com.github.openretrogamingarchive.rompatcher;

import com.github.openretrogamingarchive.util.PathUtil;
import com.github.openretrogamingarchive.util.Zip;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class RomPatcherTest {

    Path deltaPatch = Path.of("test-data","3x3 Eyes - Juuma Houkan (Japan) [T-En by Atomizer_Zero v1.01]-delta.bps");
    Path original = Path.of("test-data","3x3 Eyes - Juuma Houkan (Japan).sfc");
    Path modified = Path.of("test-data","3x3 Eyes - Juuma Houkan (Japan) [T-En by Atomizer_Zero v1.01].sfc");
    Path originalZip = Path.of("test-data","3x3 Eyes - Juuma Houkan (Japan).zip");
    Path modifiedZip = Path.of("test-data","3x3 Eyes - Juuma Houkan (Japan) [T-En by Atomizer_Zero v1.01].zip");
    Path tempFile = Path.of("temp-" + System.currentTimeMillis() + ".sfc");
    Path tempFileZip = Path.of("temp-" + System.currentTimeMillis() + ".zip");
    @AfterEach
    public void cleanup() throws IOException {
        if (Files.exists(tempFile)) {
            Files.delete(tempFile);
        }
        if (Files.exists(tempFileZip)) {
            Files.delete(tempFileZip);
        }
    }

    @Test
    public void testHelp() throws IOException {
        String expected = RomPatcher.helpMsg();

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(byteArrayOutputStream);
        System.setOut(printStream);
        RomPatcher.main(new String[] {});
        String actual = new String(byteArrayOutputStream.toByteArray());
        assertTrue(expected.length() > 0);
        assertEquals(expected, actual);
    }

    @Test
    public void testPatch() throws IOException {
        Path out = tempFile;
        RomPatcher.main(new String[] {deltaPatch.toString(), original.toString(), out.toString()});

        byte[] expected = Files.readAllBytes(modified);
        byte[] actual = Files.readAllBytes(out);
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testPatchInputZip() throws IOException {
        Path out = tempFile;
        RomPatcher.main(new String[] {deltaPatch.toString(), originalZip.toString(), out.toString()});

        byte[] expected = Files.readAllBytes(modified);
        byte[] actual = Files.readAllBytes(out);
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testPatchOutputZip() throws IOException {
        Path out = tempFileZip;
        RomPatcher.main(new String[] {deltaPatch.toString(), original.toString(), out.toString()});

        byte[] expected = Files.readAllBytes(modified);
        Map<String, byte[]> zipContent = Zip.readAllBytes(out);
        String actualName = zipContent.keySet().iterator().next();
        assertEquals("sfc", PathUtil.getExtension(actualName));
        byte[] actual = zipContent.values().iterator().next();
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testPatchInputOutputZip() throws IOException {
        Path out = tempFileZip;
        RomPatcher.main(new String[] {deltaPatch.toString(), originalZip.toString(), out.toString()});

        byte[] expected = Files.readAllBytes(modified);
        Map<String, byte[]> zipContent = Zip.readAllBytes(out);
        String actualName = zipContent.keySet().iterator().next();
        assertEquals("sfc", PathUtil.getExtension(actualName));
        byte[] actual = zipContent.values().iterator().next();
        assertArrayEquals(expected, actual);
    }
}
