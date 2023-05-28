package com.github.videogamearchive.database.index;

import com.github.videogamearchive.database.index.IndexCreator;
import org.junit.jupiter.api.Test;

public class IndexCreatorTest {
    @Test
    public void csvIndexCreatorTest() throws Exception {
        IndexCreator.main(new String[]{ "../database" });
    }

}
