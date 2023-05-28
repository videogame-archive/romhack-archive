package com.github.videogamearchive.database.romset;

import com.github.videogamearchive.database.romset.RomsetCreator;
import org.junit.jupiter.api.Test;

public class RomsetCreatorTest {
    @Test
    public void romsetCreatorTest() throws Exception {
        RomsetCreator.main(new String[]{ "archiveRoot", "inputRomRoot", "outputRootRoot"});
    }
}
