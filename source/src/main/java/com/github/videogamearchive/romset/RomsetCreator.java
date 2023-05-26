package com.github.videogamearchive.romset;

import com.github.videogamearchive.model.Romhack;
import com.github.videogamearchive.model.json.RomhackMapper;
import com.github.videogamearchive.model.validator.RomhackValidator;
import com.github.videogamearchive.rompatcher.MarcFile;
import com.github.videogamearchive.rompatcher.formats.BPS;
import com.github.videogamearchive.util.PathUtil;
import com.github.videogamearchive.util.Zip;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

public class RomsetCreator {
    private static final String ROMHACK_JSON = "romhack.json";

    private static final String ROMHACK_BPS = "romhack.bps";

    private static final String ROMHACK_ORIGINAL = "romhack-original";

    private static RomhackMapper romhackReader = new RomhackMapper();
    public static void main(String[] args) throws Exception {
        if (args.length != 3 && args.length != 4) {
            help();
        } else {
            boolean validate = false;
            if (args.length == 4) {
                validate = args[3].equals("validate");
            }
            File patchesRoot = new File(args[0]);
            if (patchesRoot.exists() && patchesRoot.isDirectory()) {
                for (File systemFolder:patchesRoot.listFiles()) {
                    processSystem(systemFolder, new File(args[1]), new File(args[2]), validate);
                }
            } else {
                help();
            }
        }
    }
    private static void help() {
        System.out.println("usage: ");
        System.out.println("\t\t java -jar romset-creator.jar \"pathToArchiveRoot\" \"pathToInputRomRoot\" \"pathToOutputRomRoot\" [\"validate\"]");
    }
    private static void processSystem(File system, File inputRomRoot, File outputRomRoot, boolean validate) throws IOException, ReflectiveOperationException, NoSuchAlgorithmException {
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
                if (romhackJSON != null && romhackBPS != null && romhackOriginal == null) {
                    throw new RuntimeException("Missing romhack-original folder");
                } else if (romhackJSON == null || romhackBPS == null || romhackOriginal == null) {
                    ignored(clone);
                    continue;
                } else {
                    processing(clone);
                }

                Romhack romhack = romhackReader.read(romhackJSON.toPath());
                File pathToInputRom = getInputRom(inputRomRoot, system, parent);
                if (pathToInputRom == null) {
                    ignored(clone);
                    continue;
                }
                String romhackFileName = clone.getName();
                String zipName = PathUtil.getNameWithoutExtension(romhackFileName) + ".zip";
                File pathToOutputRomZip = getOutputRom(outputRomRoot, system, zipName);
                createRomhack(romhackFileName, romhack, romhackBPS, pathToInputRom, pathToOutputRomZip, validate);
            }
        }
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

    private static DocumentBuilder getDocumentBuilder() throws ParserConfigurationException {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        return documentBuilderFactory.newDocumentBuilder();
    }

    private static void processing(File file) {
        System.out.println("Processing folder: " + file.getPath());
    }

    private static void ignored(File file) {
        System.out.println("WARNING - Ignored folder: " + file.getPath());
    }
}
