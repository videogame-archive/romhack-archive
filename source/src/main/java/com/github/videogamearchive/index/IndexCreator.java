package com.github.videogamearchive.index;

import com.github.videogamearchive.database.DatabaseVisitor;
import com.github.videogamearchive.database.DatabaseWalker;
import com.github.videogamearchive.model.Identifiable;
import com.github.videogamearchive.util.CSV;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class IndexCreator {
    private static List<ExtendedRomhack> romhacks = new ArrayList<>();
    public static void main(String[] args) throws Exception {
        if (args.length != 1 && args.length != 2) {
            help();
        } else {
            boolean validate;
            if (args.length == 2) {
                validate = args[1].equals("--validate");
            } else {
                validate = false;
            }
            File root = new File(args[0]);
            if (root.exists() && root.isDirectory()) {
                DatabaseWalker.processDatabase(root, new DatabaseVisitor() {
                    @Override
                    public boolean validate() {
                        return validate;
                    }

                    @Override
                    public void walk(File identifiableFolder, Identifiable identifiable) {
                        if (identifiable instanceof ExtendedRomhack) {
                            romhacks.add((ExtendedRomhack) identifiable);
                        }
                    }
                });
                Collections.sort(romhacks);
                List<String[]> rows = new ArrayList<>(romhacks.size());
                for (ExtendedRomhack row:romhacks) {
                    rows.add(row.row());
                }
                CSV.write(Path.of("../docs/database/database.csv"), ExtendedRomhack.headers(), rows);
            } else {
                help();
            }
        }
    }

    private static void help() {
        System.out.println("usage: ");
        System.out.println("\t\t java -jar index-creator.jar \"database\" [--validate]");
    }

}
