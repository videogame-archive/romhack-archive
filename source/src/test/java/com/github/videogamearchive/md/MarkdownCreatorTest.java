package com.github.videogamearchive.md;

import org.junit.jupiter.api.Test;

public class MarkdownCreatorTest {
    @Test
    public void markdownCreatorTest() throws Exception {
        MarkdownCreator.main(new String[]{ "../database" });
    }

}
