package com.github.videogamearchive.migration;

import org.junit.jupiter.api.Test;

public class MigrationAssistantTest {
    @Test
    public void migrationAssistantTest() throws Exception {
        MigrationAssistant.main(new String[]{
                "--dry-run", "../"});
    }

    @Test
    public void migrationAssistantTest2() throws Exception {
        MigrationAssistant.main(new String[]{
                "../"});
    }

    @Test
    public void migrationAssistantHelpTest() throws Exception {
        MigrationAssistant.main(new String[]{});
    }

    @Test
    public void migrationAssistantError1Test() throws Exception {
        MigrationAssistant.main(new String[]{"--dry-run"});
    }

    @Test
    public void migrationAssistantError2Test() throws Exception {
        MigrationAssistant.main(new String[]{"non-existing-path"});
    }
}
