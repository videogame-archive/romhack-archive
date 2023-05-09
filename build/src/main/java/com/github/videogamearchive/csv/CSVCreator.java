package com.github.videogamearchive.csv;

import com.github.videogamearchive.model.Romhack;
import com.github.videogamearchive.model.RomhackReaderWriter;
import com.github.videogamearchive.util.CSV;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.*;

public class CSVCreator {
    private static final String ROMHACK_JSON = "romhack.json";

    private static final String ROMHACK_BPS = "romhack.bps";

    private static final String ROMHACK_ORIGINAL = "romhack-original";
    private static SimpleDateFormat TIMESTAMP_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
    private static String NOW = TIMESTAMP_FORMAT.format(new Date());
    private static List<RomhackRow> romhacks = new ArrayList<>();
    private static RomhackReaderWriter romhackReader = new RomhackReaderWriter();

    public static void main(String[] args) throws Exception {
        if (args.length != 1) {
            help();
        } else {
            File root = new File(args[0]);
            if (root.exists() && root.isDirectory()) {
                for (File systemFolder:root.listFiles()) {
                    processSystem(systemFolder);
                }
                List<String[]> rows = new ArrayList<>(romhacks.size());
                for (RomhackRow row:romhacks) {
                    rows.add(row.row());
                }
                CSV.write(Path.of("romhacks.csv"), RomhackRow.headers(), rows);
            } else {
                help();
            }
        }
    }
    private static void help() {
        System.out.println("usage: ");
        System.out.println("\t\t java -jar csv-creator.jar \"pathToArchiveRoot\"");
    }

    private static void processSystem(File system) throws IOException, ReflectiveOperationException {
        if (!system.isDirectory()) {
            ignored(system);
            return;
        } else {
            processing(system);
        }
        for (File parent:system.listFiles()) {
            if (!parent.isDirectory()) {
                ignored(parent);
                continue;
            } else {
                processing(parent);
            }
            for (File clone:parent.listFiles()) {
                if (!clone.isDirectory()) {
                    ignored(clone);
                    continue;
                }
                File romhackJSON = null;
                File romhackBPS = null;
                File romhackOriginal = null;
                for (File file:clone.listFiles()) {
                    if (file.getName().equals(ROMHACK_JSON) && file.isFile()) {
                        romhackJSON = file;
                    } else if (file.getName().equals(ROMHACK_BPS) && file.isFile()) {
                        romhackBPS = file;
                    } else if(file.getName().equals(ROMHACK_ORIGINAL) && file.isDirectory() && file.listFiles().length > 0) {
                        romhackOriginal = file;
                    }
                }
                if (romhackJSON == null || romhackBPS == null || romhackOriginal == null) {
                    ignored(clone);
                    continue;
                } else {
                    processing(clone);
                }

                Romhack romhack = romhackReader.read(romhackJSON.toPath());
                RomhackRow extendedRomhack = new RomhackRow(system.getName(), parent.getName(), clone.getName(), romhack);
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

    private static void addGame(RomhackRow romhack) {
        romhacks.add(romhack);
    }

}
