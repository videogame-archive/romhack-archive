package com.github.openretrogamingarchive.dat;

import org.junit.jupiter.api.Test;

public class DatCreatorTest {
    @Test
    public void datCreatorTest() throws Exception {
        DatCreator.main(new String[]{ "archiveRoot", "validate"});
    }
}
