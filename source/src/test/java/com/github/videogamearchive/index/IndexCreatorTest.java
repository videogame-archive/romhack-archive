package com.github.videogamearchive.index;

import org.junit.jupiter.api.Test;

public class IndexCreatorTest {
    @Test
    public void csvIndexCreatorTest() throws Exception {
        IndexCreator.main(new String[]{ "csv", "../database" });
    }

    @Test
    public void mdIndexCreatorTest() throws Exception {
        IndexCreator.main(new String[]{ "md", "../database" });
    }
}
