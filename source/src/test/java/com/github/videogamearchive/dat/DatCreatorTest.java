package com.github.videogamearchive.dat;

import org.junit.jupiter.api.Test;

public class DatCreatorTest {
    @Test
    public void datCreatorTest() throws Exception {
        DatCreator.main(new String[]{ "../database", "--validate"});
    }
}
