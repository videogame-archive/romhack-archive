package com.github.videogamearchive.md;

import com.github.videogamearchive.index.IndexCreator;
import org.junit.jupiter.api.Test;

public class MarkdownGeneratorTest {
    @Test
    public void csvIndexCreatorTest() throws Exception {
        MarkdownGenerator.main(new String[]{ "../database" });
    }

}
