package com.github.videogamearchive.database.migration;

import com.github.videogamearchive.database.DatabaseVisitor;
import com.github.videogamearchive.database.DatabaseWalker;
import com.github.videogamearchive.database.ExtendedRomhack;
import com.github.videogamearchive.model.*;
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
        System.out.println("\t\t java -jar migration-assistant.jar [--dry-run][--validate] \"database\"");
    }

    public static void main(String[] args) throws ReflectiveOperationException, IOException {
        if (args.length == 2 || args.length == 3) {
            File root = null;
            boolean dryRun = false;
            boolean validate = false;
            for (String arg:args) {
                if (arg.equals("--dryr-un")) {
                    dryRun = true;
                } else if (arg.equals("--validate")) {
                    validate = true;
                } else if (Files.exists(Path.of(arg))) {
                    root = new File(arg);
                } else {
                    System.out.println("Given argument '" + arg + "' is neither a flag or an existing folder.");
                }
            }
            if (root != null) {
                System.out.println("migrate - dry run");
                migrate(true, validate, root); // first pass is used to find out the last id on the database
                System.out.println("An existing folder is required to continue, aborting.");
                if (!dryRun) {
                    migrate(false, false, root); // second pass makes changes if requested
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

    public static void migrate(boolean dryRun, boolean validate, File root) throws ReflectiveOperationException, IOException {
        SystemMapper systemMapper = new SystemMapper();
        GameMapper gameMapper = new GameMapper();
        RomhackMapper romhackMapper = new RomhackMapper();

        DatabaseWalker.processDatabase(root, new DatabaseVisitor() {
            @Override
            public boolean validate() {
                return validate;
            }

            @Override
            public void walk(File identifiableFolder, Identifiable identifiable) {
                try {
                    if (identifiable instanceof System_) {
                        processSystem(dryRun, (System_) identifiable, systemMapper, identifiableFolder);
                    } else if (identifiable instanceof Game) {
                        processGame(dryRun, (Game) identifiable, gameMapper, identifiableFolder);
                    } else if (identifiable instanceof ExtendedRomhack) {
                        processRomhack(dryRun, (ExtendedRomhack) identifiable, romhackMapper, identifiableFolder);
                    }
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
    }

    private static void processSystem(boolean dryRun, System_ system, SystemMapper systemMapper, File systemFolder) throws IOException, ReflectiveOperationException {
        Path systemPath = systemFolder.toPath().resolve(DatabaseWalker.SYSTEM_JSON);
        String json = systemMapper.write(system);
        system = systemIdentifiableCache.updateLastId(dryRun, systemFolder.getName(), system);
        String updatedJson = systemMapper.write(system);
        if (!json.equals(updatedJson)) {
            System.out.println("UPDATE\tjson\t" + PathUtil.getName(systemPath));
        }
        if (!dryRun) {
            Files.writeString(systemPath, updatedJson, StandardCharsets.UTF_8);
        }
    }

    private static void processGame(boolean dryRun, Game game, GameMapper gameMapper, File gameFolder) throws IOException, ReflectiveOperationException {
        Path parentPath = gameFolder.toPath().resolve(DatabaseWalker.GAME_JSON);
        String json = gameMapper.write(game);
        game = parentIdentifiableCache.updateLastId(dryRun, gameFolder.getName(), game);
        String updatedJson = gameMapper.write(game);
        if (!json.equals(updatedJson)) {
            System.out.println("UPDATE\tjson\t" + PathUtil.getName(parentPath));
        }
        if (!dryRun) {
            Files.writeString(parentPath, updatedJson, StandardCharsets.UTF_8);
        }
    }

    private static void processRomhack(boolean dryRun, ExtendedRomhack indexRomhack, RomhackMapper romhackMapper, File romhackFolder) throws IOException, ReflectiveOperationException {
        Path romhackPath = romhackFolder.toPath().resolve(DatabaseWalker.ROMHACK_JSON);
        String json = romhackMapper.write(indexRomhack.romhack());
        Romhack romhack = indexRomhack.romhack();
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
