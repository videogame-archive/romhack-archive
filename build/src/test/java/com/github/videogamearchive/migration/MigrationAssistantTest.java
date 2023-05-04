package com.github.videogamearchive.migration;

import org.junit.jupiter.api.Test;

public class MigrationAssistantTest {
    @Test
    public void romhackCreatorTest() throws Exception {
        MigrationAssistant.main(new String[]{
                "--dry-run", "../"});
    }

    @Test
    public void romhackCreatorTest2() throws Exception {
        MigrationAssistant.main(new String[]{
                "../"});
    }

    @Test
    public void romhackCreatorHelp() throws Exception {
        MigrationAssistant.main(new String[]{});
    }

    @Test
    public void romhackCreatorError1() throws Exception {
        MigrationAssistant.main(new String[]{"--dry-run"});
    }

    @Test
    public void romhackCreatorError2() throws Exception {
        MigrationAssistant.main(new String[]{"non-existing-path"});
    }
}
