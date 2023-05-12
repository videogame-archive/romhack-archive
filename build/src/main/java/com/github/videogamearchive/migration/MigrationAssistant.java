package com.github.videogamearchive.migration;

import com.github.videogamearchive.model.Romhack;
import com.github.videogamearchive.model.RomhackReaderWriter;
import com.github.videogamearchive.model.RomhackValidator;
import com.github.videogamearchive.util.CSV;
import com.github.videogamearchive.util.PathUtil;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

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

    private static Map<String, Long> assignedIds = new HashMap<>();
    private static Long lastFoundId = null;

    private static Romhack updateLastFoundId(boolean dryRun, String filename, Romhack romhack) {
        // Update assigned ids, two romhack folders belong to the same romhack if the share name and authors
        String romhackHash = filename.substring(0, filename.indexOf('[')) + CSV.toString(romhack.patches().get(0).authors());

        if (romhack.id() == null && !dryRun) { // Update Romhack
            Long assignedValue = assignedIds.get(romhackHash);
            if (assignedValue == null) {
                if (lastFoundId == null) {
                    assignedValue = 1L; // First value
                } else {
                    assignedValue = lastFoundId + 1;
                }
            }
            romhack = new Romhack(assignedValue, romhack.info(), romhack.provenance(), romhack.rom(), romhack.patches());
        }

        // Update stored id
        if (romhack.id() != null) {
            if (lastFoundId == null || lastFoundId < romhack.id()) {
                lastFoundId = romhack.id();
            }
        }
        // Update assigned ids
        if (!assignedIds.containsKey(romhackHash) && romhack.id() != null) {
            assignedIds.put(romhackHash, romhack.id());
        }
        return romhack;
    }

    public static void migrate(boolean dryRun, File root) throws ReflectiveOperationException, IOException {

        RomhackReaderWriter romhackReaderWriter = new RomhackReaderWriter();

        for (File system:root.listFiles()) {
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
                    Path romhackPath = romhackFolder.toPath().resolve("romhack.json");
                    if (!Files.exists(romhackPath)) {
                        ignored(romhackFolder);
                        continue;
                    }
                    String json = Files.readString(romhackPath, StandardCharsets.UTF_8);
                    Romhack romhack = romhackReaderWriter.read(json);
                    romhack = updateLastFoundId(dryRun, romhackFolder.getName(), romhack);
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
            }
        }
    }

    private static void ignored(File file) {
        System.out.println("WARNING - Ignored folder: " + file.getPath());
    }

}
