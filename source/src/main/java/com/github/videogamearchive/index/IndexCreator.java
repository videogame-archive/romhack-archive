package com.github.videogamearchive.index;

import com.github.videogamearchive.model.Romhack;
import com.github.videogamearchive.model.RomhackReaderWriter;
import com.github.videogamearchive.util.CSV;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.*;

public class IndexCreator {
    private static final String ROMHACK_JSON = "romhack.json";

    private static final String ROMHACK_BPS = "romhack.bps";

    private static final String ROMHACK_ORIGINAL = "romhack-original";
    private static SimpleDateFormat TIMESTAMP_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
    private static String NOW = TIMESTAMP_FORMAT.format(new Date());
    private static List<IndexRomhack> romhacks = new ArrayList<>();
    private static RomhackReaderWriter romhackReader = new RomhackReaderWriter();

    public enum Format {csv, md}

    public static void main(String[] args) throws Exception {
        if (args.length != 2) {
            help();
        } else {
            Format format = Format.valueOf(args[0]);
            File root = new File(args[1]);
            if (root.exists() && root.isDirectory()) {
                for (File systemFolder:root.listFiles()) {
                    processSystem(systemFolder);
                }

                //
                Collections.sort(romhacks);
                List<String[]> rows = new ArrayList<>(romhacks.size());
                for (IndexRomhack row:romhacks) {
                    rows.add(row.row());
                }
                switch (format) {
                    case csv -> {
                        CSV.write(Path.of("rom-file-index.csv"), IndexRomhack.headers(), rows);
                    }
                    case md -> {
                        StringBuilder builder = new StringBuilder();
                        builder.append("![videogame archive](./brand/videogame-archive-(alt).png \"Videogame Archive\")").append("\n").append("\n");
                        builder.append("# Romhack Archive: Rom File Index").append("\n").append("\n");
                        builder.append("This table can be also downloaded as a standard Comma Separated Value (CSV) format file, as for RFC4180: [Download](./rom-file-index.csv)").append("\n").append("\n");

                        StringBuilder headerBuilder = new StringBuilder();
                        String[] headers = IndexRomhack.headers();
                        int downloadIndex = -1;
                        for (int i = 0; i < headers.length;i++) {
                            String header = headers[i];
                            if (header.equals("Download")) {
                                downloadIndex = i;
                            }
                            headerBuilder.append("|");
                            headerBuilder.append("**").append(header).append("**");
                        }
                        headerBuilder.append("|").append("\n");

                        int separatorLength = headerBuilder.length() - 1;
                        for (int i = 0; i < separatorLength; i++) {
                            if (headerBuilder.charAt(i) == '|') {
                                headerBuilder.append("|");
                            } else {
                                headerBuilder.append("-");
                            }
                        }
                        headerBuilder.append("\n");

                        builder.append(headerBuilder);
                        for (String[] row:rows) {
                            for (int i = 0; i < row.length; i++) {
                                String field = row[i];
                                builder.append("|");

                                if (i == downloadIndex) {
                                    builder.append("[Download](").append(field).append(")");
                                } else {
                                    builder.append(field);
                                }
                            }
                            builder.append("|").append("\n");
                        }
                        Files.write(Path.of("rom-file-index.md"), builder.toString().getBytes(StandardCharsets.UTF_8));
                    }
                }
            } else {
                help();
            }
        }
    }
    private static void help() {
        System.out.println("usage: ");
        System.out.println("\t\t java -jar [csv|md] index-creator.jar \"pathToArchiveRoot\"");
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
                IndexRomhack extendedRomhack = new IndexRomhack(system.getName(), parent.getName(), clone.getName(), romhack);
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
