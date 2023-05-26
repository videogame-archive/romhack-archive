package com.github.videogamearchive.index;

import com.github.videogamearchive.model.Game;
import com.github.videogamearchive.model.Identifiable;
import com.github.videogamearchive.model.Romhack;
import com.github.videogamearchive.model.System_;
import com.github.videogamearchive.model.json.GameMapper;
import com.github.videogamearchive.model.json.RomhackMapper;
import com.github.videogamearchive.model.json.SystemMapper;
import com.github.videogamearchive.util.CSV;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.*;

public class IndexCreator {

    private static final String SYSTEM_JSON = "system.json";

    private static final String GAME_JSON = "game.json";
    private static final String ROMHACK_JSON = "romhack.json";

    private static final String ROMHACK_BPS = "romhack.bps";

    private static final String ROMHACK_ORIGINAL = "romhack-original";
    private static SimpleDateFormat TIMESTAMP_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
    private static String NOW = TIMESTAMP_FORMAT.format(new Date());
    private static List<IndexRomhack> romhacks = new ArrayList<>();
    private static SystemMapper systemMapper = new SystemMapper();
    private static GameMapper gameMapper = new GameMapper();
    private static RomhackMapper romhackMapper = new RomhackMapper();

    public static void main(String[] args) throws Exception {
        if (args.length != 1) {
            help();
        } else {
            File root = new File(args[0]);
            if (root.exists() && root.isDirectory()) {
                processDatabase(root, new IdentifiableVisitor() {
                    @Override
                    public void walk(File identifiableFolder, Identifiable identifiable) {
                        // Do nothing
                    }
                });
                Collections.sort(romhacks);
                List<String[]> rows = new ArrayList<>(romhacks.size());
                for (IndexRomhack row:romhacks) {
                    rows.add(row.row());
                }
                CSV.write(Path.of("../docs/database/database.csv"), IndexRomhack.headers(), rows);
            } else {
                help();
            }
        }
    }

    private static void help() {
        System.out.println("usage: ");
        System.out.println("\t\t java -jar index-creator.jar \"pathToDatabaseRoot\"");
    }

    public interface IdentifiableVisitor {
        void walk(File identifiableFolder, Identifiable identifiable);
    }

    public static void processDatabase(File databaseFolder, IdentifiableVisitor identifiableVisitor) throws IOException, ReflectiveOperationException {
        for (File systemFolder: databaseFolder.listFiles()) {
            processSystem(systemFolder, identifiableVisitor);
        }
    }

    private static void processSystem(File systemFolder, IdentifiableVisitor identifiableVisitor) throws IOException, ReflectiveOperationException {
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
                File romhackJSON = null;
                File romhackBPS = null;
                File romhackOriginal = null;
                for (File file:cloneFolder.listFiles()) {
                    if (file.getName().equals(ROMHACK_JSON) && file.isFile()) {
                        romhackJSON = file;
                    } else if (file.getName().equals(ROMHACK_BPS) && file.isFile()) {
                        romhackBPS = file;
                    } else if(file.getName().equals(ROMHACK_ORIGINAL) && file.isDirectory() && file.listFiles().length > 0) {
                        romhackOriginal = file;
                    }
                }
                if (romhackJSON == null || romhackBPS == null || romhackOriginal == null) {
                    ignored(cloneFolder);
                    continue;
                } else {
                    processing(cloneFolder);
                }

                Romhack romhack = romhackMapper.read(romhackJSON.toPath());
                IndexRomhack extendedRomhack = new IndexRomhack(system.id(), systemFolder.getName(), game.id(), parentFolder.getName(), cloneFolder.getName(), romhack);
                identifiableVisitor.walk(cloneFolder, extendedRomhack);
                addGame(extendedRomhack);
            }
        }
    }

    private static void processing(File file) {
        System.out.println("Processing folder: " + file.getPath());
    }
    private static void ignored(File file) {
        System.out.println("WARNING - Ignored folder: " + file.getPath());
    }

    private static void addGame(IndexRomhack romhack) {
        romhacks.add(romhack);
    }

}
