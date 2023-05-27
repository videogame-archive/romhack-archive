package com.github.videogamearchive.romset;

import org.junit.jupiter.api.Test;

public class RomsetCreatorTest {
    @Test
    public void romsetCreatorTest() throws Exception {
        RomsetCreator.main(new String[]{ "archiveRoot", "inputRomRoot", "outputRootRoot"});
    }
}
