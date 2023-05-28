package com.github.videogamearchive.database.md;

import com.github.videogamearchive.database.md.MarkdownCreator;
import org.junit.jupiter.api.Test;

public class MarkdownCreatorTest {
    @Test
    public void markdownCreatorTest() throws Exception {
        MarkdownCreator.main(new String[]{ "../database" });
    }

}
