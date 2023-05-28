package com.github.videogamearchive.database.dat;

import com.github.videogamearchive.dat.DatCreator;
import org.junit.jupiter.api.Test;

public class DatCreatorTest {
    @Test
    public void datCreatorTest() throws Exception {
        DatCreator.main(new String[]{ "../database", "--validate"});
    }
}
