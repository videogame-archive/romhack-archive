package com.github.videogamearchive.romset;

import com.github.videogamearchive.database.DatabaseVisitor;
import com.github.videogamearchive.database.DatabaseWalker;
import com.github.videogamearchive.index.ExtendedRomhack;
import com.github.videogamearchive.model.Identifiable;
import com.github.videogamearchive.model.Romhack;
import com.github.videogamearchive.model.validator.RomhackValidator;
import com.github.videogamearchive.rompatcher.MarcFile;
import com.github.videogamearchive.rompatcher.formats.BPS;
import com.github.videogamearchive.util.PathUtil;
import com.github.videogamearchive.util.Zip;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

public class RomsetCreator {
    public static void main(String[] args) throws Exception {
        if (args.length != 3 && args.length != 4) {
            help();
        } else {
            boolean validate;
            if (args.length == 4) {
                validate = args[3].equals("--validate");
            } else {
                validate = false;
            }
            File patchesRoot = new File(args[0]);
            if (patchesRoot.exists() && patchesRoot.isDirectory()) {
                File roms = new File(args[1]);
                File romsOutput = new File(args[2]);
                DatabaseVisitor visitor = new DatabaseVisitor() {
                    @Override
                    public boolean validate() {
                        return validate;
                    }

                    @Override
                    public void walk(File identifiableFolder, Identifiable identifiable) {
                        if (identifiable instanceof ExtendedRomhack) {
                            ExtendedRomhack indexRomhack = (ExtendedRomhack) identifiable;
                            try {
                                File system = identifiableFolder.getParentFile().getParentFile();
                                File parent = identifiableFolder.getParentFile();
                                File pathToInputRom = getInputRom(roms,
                                        system,
                                        parent);
                                File romhackBPS = identifiableFolder.toPath().resolve(DatabaseWalker.ROMHACK_BPS).toFile();
                                String romhackFileName = indexRomhack.parent();
                                String zipName = PathUtil.getNameWithoutExtension(romhackFileName) + ".zip";
                                File pathToOutputRomZip = getOutputRom(romsOutput, system, zipName);
                                createRomhack(romhackFileName, indexRomhack.romhack(), romhackBPS, pathToInputRom, pathToOutputRomZip, validate);
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }
                };

                DatabaseWalker.processDatabase(patchesRoot, visitor);
            } else {
                help();
            }
        }
    }
    private static void help() {
        System.out.println("usage: ");
        System.out.println("\t\t java -jar romset-creator.jar \"database\" \"roms\" \"romsOutput\" [--validate]");
    }

    private static void createRomhack(String romhackFileName, Romhack romhack, File romhackBPS, File pathToInputRom, File pathToOutputRomZip, boolean validate) throws IOException, NoSuchAlgorithmException {
        BPS bps = BPS.parseBPSFile(new MarcFile(romhackBPS.toPath()));
        byte[] inputRomAsBytes;
        if (PathUtil.isZip(pathToInputRom.toPath())) {
            inputRomAsBytes = Zip.readAllBytesOneFile(pathToInputRom.toPath());
        } else {
            inputRomAsBytes = Files.readAllBytes(pathToInputRom.toPath());
        }
        MarcFile output = bps.apply(new MarcFile(inputRomAsBytes), true);
        byte[] bytes = output.save();
        Zip.write(pathToOutputRomZip.toPath(), Map.of(romhackFileName, bytes));
        if (validate) {
            RomhackValidator.validateRom(romhack, bytes);
        }
    }

    private static File getOutputRom(File outputRomRoot, File system, String name) throws IOException {
        Path romDir = outputRomRoot.toPath().resolve(system.getName());
        Files.createDirectories(romDir);
        Path romPath = romDir.resolve(name);
        return romPath.toFile();
    }
    private static File getInputRom(File inputRomRoot, File system, File parent) {
        File[] inputRomSystemAsArray = inputRomRoot.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return pathname.getName().equals(system.getName());
            }
        });
        File inputRomSystem = null;
        if (inputRomSystemAsArray.length == 1 && inputRomSystemAsArray[0].isDirectory()) {
            inputRomSystem = inputRomSystemAsArray[0];
        }
        if (inputRomSystem != null) {
            String parentNameWithoutExtension = PathUtil.getNameWithoutExtension(parent.getName()) + "."; // The dot is to avoid collisions
            File[] inputRomAsArray = inputRomSystem.listFiles(new FileFilter() {
                @Override
                public boolean accept(File pathname) {
                    return pathname.getName().startsWith(parentNameWithoutExtension);
                }
            });
            if (inputRomAsArray.length == 1 && inputRomAsArray[0].isFile()) {
                return inputRomAsArray[0];
            }
        }
        return null;
    }

}
