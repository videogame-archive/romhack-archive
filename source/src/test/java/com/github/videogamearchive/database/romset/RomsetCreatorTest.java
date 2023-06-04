package com.github.videogamearchive.database.romset;

import org.junit.jupiter.api.Test;

public class RomsetCreatorTest {
    @Test
    public void romsetCreatorTest() throws Exception {
        RomsetCreator.main(new String[]{ "../database", "../roms", "../out", "--validate"});
    }
}
