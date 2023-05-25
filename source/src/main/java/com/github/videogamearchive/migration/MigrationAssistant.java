package com.github.videogamearchive.migration;

import com.github.videogamearchive.model.System_;
import com.github.videogamearchive.model.Parent;
import com.github.videogamearchive.model.Patch;
import com.github.videogamearchive.model.Romhack;
import com.github.videogamearchive.model.json.RomhackMapper;
import com.github.videogamearchive.model.validator.RomhackValidator;
import com.github.videogamearchive.util.PathUtil;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class MigrationAssistant {

    private static void help() {
        System.out.println("usage: ");
        System.out.println("\t\t java -jar migration-assistant.jar [--dry-run] \"archiveRoot\"");
    }

    public static void main(String[] args) throws ReflectiveOperationException, IOException {
        if (args.length == 1 || args.length == 2) {
            File root = null;
            boolean dryRun = false;
            for (String arg:args) {
                if (arg.equals("--dry-run")) {
                    dryRun = true;
                } else if (Files.exists(Path.of(arg))) {
                    root = new File(arg);
                } else {
                    System.out.println("Given argument '" + arg + "' is neither a flag or an existing folder.");
                }
            }
            if (root != null) {
                System.out.println("migrate - dry run");
                migrate(true, root); // first pass is used to find out the last id on the database
                System.out.println("An existing folder is required to continue, aborting.");
                if (!dryRun) {
                    migrate(false, root); // second pass makes changes if requested
                }
            } else {
                System.out.println("An existing folder is required to continue, aborting.");
            }
        } else {
            help();
        }
    }

    private static IdentifiableCache<System_> systemIdentifiableCache = new IdentifiableCache<>();
    private static IdentifiableCache<Parent> gameIdentifiableCache = new IdentifiableCache<>();
    private static IdentifiableCache<Romhack> romhackIdentifiableCache = new IdentifiableCache<>();
    private static IdentifiableCache<Patch> patchIdentifiableCache = new IdentifiableCache<>();

    public static void migrate(boolean dryRun, File root) throws ReflectiveOperationException, IOException {

        RomhackMapper romhackReaderWriter = new RomhackMapper();

        for (File system:root.listFiles()) {
            processSystem(system);
            if (system.isFile()) {
                ignored(system);
                continue;
            }
            for (File parentFolder:system.listFiles()) {
                if (system.isFile()) {
                    ignored(parentFolder);
                    continue;
                }
                File[] folders = parentFolder.listFiles();
                if (folders == null) {
                    ignored(system);
                    continue;
                }
                for (File romhackFolder:folders) {
                    processRomhack(dryRun, romhackReaderWriter, romhackFolder);
                }
            }
        }
    }

    private static void processSystem(File systemFolder) {
        Path systemPath = systemFolder.toPath().resolve("system.json");
        System_ system = null;
        if (Files.exists(systemPath)) {

        }
    }

    private static void processRomhack(boolean dryRun, RomhackMapper romhackReaderWriter, File romhackFolder) throws IOException, ReflectiveOperationException {
        Path romhackPath = romhackFolder.toPath().resolve("romhack.json");
        if (!Files.exists(romhackPath)) {
            ignored(romhackFolder);
            return;
        }
        String json = Files.readString(romhackPath, StandardCharsets.UTF_8);
        Romhack romhack = romhackReaderWriter.read(json);
        romhack = romhackIdentifiableCache.updateLastId(dryRun, romhackFolder.getName(), romhack);
        String expectedFolderNamePostfix = RomhackValidator.getExpectedFolderNamePostfix(romhack);
        String updatedJson = romhackReaderWriter.write(romhack);
        if (!json.equals(updatedJson)) {
            System.out.println("UPDATE\tjson\t" + PathUtil.getName(romhackPath));
        }
        if (!dryRun) {
            Files.writeString(romhackPath, updatedJson, StandardCharsets.UTF_8);
        }
        String extension = PathUtil.getExtension(romhackFolder.getName());
        int indexOfPostFix = romhackFolder.getName().indexOf(" [");
        String correctName = romhackFolder.getName().substring(0, indexOfPostFix)+ expectedFolderNamePostfix + "." + extension;
        if (!romhackFolder.getName().equals(correctName)) {
            File corrected = romhackFolder.getParentFile().toPath().resolve(correctName).toFile();
            System.out.println("UPDATE\tfolder\t" + romhackFolder.getName() + "\t" + corrected.getName());
            if (!dryRun) {
                romhackFolder.renameTo(corrected);
            }
        }
    }

    private static void ignored(File file) {
        System.out.println("WARNING - Ignored folder: " + file.getPath());
    }

}
