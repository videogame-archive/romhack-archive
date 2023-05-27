package com.github.videogamearchive.database;

import com.github.videogamearchive.index.ExtendedRomhack;
import com.github.videogamearchive.model.Game;
import com.github.videogamearchive.model.Romhack;
import com.github.videogamearchive.model.System_;
import com.github.videogamearchive.model.json.GameMapper;
import com.github.videogamearchive.model.json.RomhackMapper;
import com.github.videogamearchive.model.json.SystemMapper;
import com.github.videogamearchive.model.validator.RomhackValidator;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class DatabaseWalker {
    public static final String SYSTEM_JSON = "system.json";
    public static final String GAME_JSON = "game.json";
    public static final String ROMHACK_JSON = "romhack.json";
    public static final String ROMHACK_BPS = "romhack.bps";
    public static final String ROMHACK_ORIGINAL = "romhack-original";

    public static void processDatabase(File databaseFolder, DatabaseVisitor identifiableVisitor) throws IOException, ReflectiveOperationException {
        for (File systemFolder: databaseFolder.listFiles()) {
            processSystem(systemFolder, identifiableVisitor);
        }
    }

    public static void processSystem(File systemFolder, DatabaseVisitor identifiableVisitor) throws IOException, ReflectiveOperationException {
        SystemMapper systemMapper = new SystemMapper();
        GameMapper gameMapper = new GameMapper();
        RomhackMapper romhackMapper = new RomhackMapper();

        if (!systemFolder.isDirectory()) {
            ignored(systemFolder);
            return;
        } else {
            processing(systemFolder);
        }
        System_ system = null;
        if(Files.exists(systemFolder.toPath().resolve(SYSTEM_JSON))) {
            system = systemMapper.read(systemFolder.toPath().resolve(SYSTEM_JSON));
        } else {
            system = new System_(systemFolder.getName(),null);
        }
        identifiableVisitor.walk(systemFolder, system);

        for (File parentFolder:systemFolder.listFiles()) {
            if (!parentFolder.isDirectory()) {
                ignored(parentFolder);
                continue;
            } else {
                processing(parentFolder);
            }
            Game game = null;
            if(Files.exists(parentFolder.toPath().resolve(GAME_JSON))) {
                game = gameMapper.read(parentFolder.toPath().resolve(GAME_JSON));
            } else {
                game = new Game(parentFolder.getName(), null);
            }
            identifiableVisitor.walk(parentFolder, game);

            for (File cloneFolder:parentFolder.listFiles()) {
                if (!cloneFolder.isDirectory()) {
                    ignored(cloneFolder);
                    continue;
                }
                int romhackFiles = 0;
                File romhackJSON = null;
                File romhackBPS = null;
                File romhackOriginal = null;
                for (File file:cloneFolder.listFiles()) {
                    if (file.getName().equals(ROMHACK_JSON) && file.isFile()) {
                        romhackJSON = file;
                        romhackFiles++;
                    } else if (file.getName().equals(ROMHACK_BPS) && file.isFile()) {
                        romhackBPS = file;
                        romhackFiles++;
                    } else if(file.getName().equals(ROMHACK_ORIGINAL) && file.isDirectory() && file.listFiles().length > 0) {
                        romhackOriginal = file;
                        romhackFiles++;
                    }
                }

                if (romhackFiles == 0) {
                    ignored(cloneFolder);
                    continue;
                } else if (romhackFiles != 3) {
                    throw new RuntimeException("Missing romhack.json, romhack.bps or romhack-original folder");
                } else {
                    processing(cloneFolder);
                }

                Romhack romhack = romhackMapper.read(romhackJSON.toPath());

                if (identifiableVisitor.validate()) {
                    RomhackValidator.validateRomHashLength(romhack);
                    RomhackValidator.validateFolder(romhack, cloneFolder.toPath());
                    RomhackValidator.validateBPS(romhack, cloneFolder.toPath().resolve("romhack.bps"));
                }

                ExtendedRomhack extendedRomhack = new ExtendedRomhack(system.id(), systemFolder.getName(), game.id(), parentFolder.getName(), cloneFolder.getName(), romhack);
                identifiableVisitor.walk(cloneFolder, extendedRomhack);
            }
        }
    }

    public static void processing(File file) {
        System.out.println("Processing folder: " + file.getPath());
    }
    public static void ignored(File file) {
        System.out.println("WARNING - Ignored folder: " + file.getPath());
    }

}
