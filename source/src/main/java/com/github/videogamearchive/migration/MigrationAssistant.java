package com.github.videogamearchive.migration;

import com.github.videogamearchive.model.System_;
import com.github.videogamearchive.model.Game;
import com.github.videogamearchive.model.Patch;
import com.github.videogamearchive.model.Romhack;
import com.github.videogamearchive.model.json.GameMapper;
import com.github.videogamearchive.model.json.RomhackMapper;
import com.github.videogamearchive.model.json.SystemMapper;
import com.github.videogamearchive.model.validator.RomhackValidator;
import com.github.videogamearchive.util.CSV;
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
    private static IdentifiableCache<Game> parentIdentifiableCache = new IdentifiableCache<>();
    private static IdentifiableCache<Romhack> romhackIdentifiableCache = new IdentifiableCache<>();
    private static IdentifiableCache<Patch> patchIdentifiableCache = new IdentifiableCache<>();

    public static void migrate(boolean dryRun, File root) throws ReflectiveOperationException, IOException {
        SystemMapper systemMapper = new SystemMapper();
        GameMapper parentMapper = new GameMapper();
        RomhackMapper romhackMapper = new RomhackMapper();

        for (File systemFolder:root.listFiles()) {
            if (systemFolder.isFile()) {
                ignored(systemFolder);
                continue;
            }
            processSystem(dryRun, systemMapper, systemFolder);
            for (File parentFolder:systemFolder.listFiles()) {
                if (systemFolder.isFile()) {
                    ignored(parentFolder);
                    continue;
                }
                File[] folders = parentFolder.listFiles();
                if (folders == null) {
                    ignored(parentFolder);
                    continue;
                }
                processGame(dryRun, parentMapper, parentFolder);
                for (File romhackFolder:folders) {
                    processRomhack(dryRun, romhackMapper, romhackFolder);
                }
            }
        }
    }

    private static void processSystem(boolean dryRun, SystemMapper systemMapper, File systemFolder) throws IOException, ReflectiveOperationException {
        Path systemPath = systemFolder.toPath().resolve("system.json");
        String json = null;
        System_ system = null;
        if (Files.exists(systemPath)) {
            json = Files.readString(systemPath, StandardCharsets.UTF_8);
            system = systemMapper.read(json);
        } else {
            json = "{}";
            system = new System_(null);
        }
        system = systemIdentifiableCache.updateLastId(dryRun, systemFolder.getName(), system);
        String updatedJson = systemMapper.write(system);
        if (!json.equals(updatedJson)) {
            System.out.println("UPDATE\tjson\t" + PathUtil.getName(systemPath));
        }
        if (!dryRun) {
            Files.writeString(systemPath, updatedJson, StandardCharsets.UTF_8);
        }
    }

    private static void processGame(boolean dryRun, GameMapper parentMapper, File gameFolder) throws IOException, ReflectiveOperationException {
        Path parentPath = gameFolder.toPath().resolve("parent.json");
        String json = null;
        Game game = null;
        if (Files.exists(parentPath)) {
            json = Files.readString(parentPath, StandardCharsets.UTF_8);
            game = parentMapper.read(json);
        } else {
            json = "{}";
            game = new Game(null);
        }
        game = parentIdentifiableCache.updateLastId(dryRun, gameFolder.getName(), game);
        String updatedJson = parentMapper.write(game);
        if (!json.equals(updatedJson)) {
            System.out.println("UPDATE\tjson\t" + PathUtil.getName(parentPath));
        }
        if (!dryRun) {
            Files.writeString(parentPath, updatedJson, StandardCharsets.UTF_8);
        }
    }

    private static void processRomhack(boolean dryRun, RomhackMapper romhackMapper, File romhackFolder) throws IOException, ReflectiveOperationException {
        Path romhackPath = romhackFolder.toPath().resolve("romhack.json");
        if (!Files.exists(romhackPath)) {
            ignored(romhackFolder);
            return;
        }
        String json = Files.readString(romhackPath, StandardCharsets.UTF_8);
        Romhack romhack = romhackMapper.read(json);
        romhack = romhackIdentifiableCache.updateLastId(dryRun, romhackFolder.getName(), romhack);
        String expectedFolderNamePostfix = RomhackValidator.getExpectedFolderNamePostfix(romhack);

        for (int i = 0; i < romhack.patches().size(); i++) {
            Patch patch = romhack.patches().get(i);
            Patch newPatch = patchIdentifiableCache.updateLastId(dryRun, patch.name() + " " + CSV.toString(patch.authors()), patch);
            romhack.patches().set(i, newPatch);
        }

        String updatedJson = romhackMapper.write(romhack);
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
