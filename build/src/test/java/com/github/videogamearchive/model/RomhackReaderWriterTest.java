package com.github.videogamearchive.model;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class RomhackReaderWriterTest {
    @Test
    public void testRead() throws IOException, ReflectiveOperationException {
        Path pathToJson = Path.of("..",
                "Nintendo - Super Nintendo Entertainment System",
                "3x3 Eyes - Juuma Houkan (Japan).sfc",
                "3x3 Eyes - Juuma Houkan (Japan) [T-En by Atomizer_Zero, FamilyGuy, AkiHizirino, mkwong98 (v1.01)].sfc",
                "romhack.json");
        RomhackReaderWriter romhackReader = new RomhackReaderWriter();
        Romhack romhack = romhackReader.read(pathToJson);
        assertNotNull(romhack);
    }

    @Test
    public void testReadWrite() throws IOException, ReflectiveOperationException {
        Path pathToJson = Path.of("..",
                "Nintendo - Super Nintendo Entertainment System",
                "3x3 Eyes - Juuma Houkan (Japan).sfc",
                "3x3 Eyes - Juuma Houkan (Japan) [T-En by Atomizer_Zero, FamilyGuy, AkiHizirino, mkwong98 (v1.01)].sfc",
                "romhack.json");
        RomhackReaderWriter romhackReaderWriter = new RomhackReaderWriter();
        Romhack romhack = romhackReaderWriter.read(pathToJson);
        assertNotNull(romhack);
        romhackReaderWriter.write(romhack, pathToJson);
        Romhack romhackB = romhackReaderWriter.read(pathToJson);
        assertEquals(romhack, romhackB);
    }
}
