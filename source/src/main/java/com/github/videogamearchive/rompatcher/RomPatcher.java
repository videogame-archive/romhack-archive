/* Apache 2 License, Copyright (c) 2023 Juan Fuentes, based on Rom Patcher JS by Marc Robledo */
package com.github.videogamearchive.rompatcher;

import com.github.videogamearchive.rompatcher.formats.BPS;
import com.github.videogamearchive.rompatcher.formats.IPS;
import com.github.videogamearchive.rompatcher.formats.UPS;
import com.github.videogamearchive.util.PathUtil;
import com.github.videogamearchive.util.Zip;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

public class RomPatcher {
    public static void main(String[] args) throws IOException {
        if (args.length != 3) {
            help();
        } else {
            Path patch = Path.of(args[0]);
            String extension = PathUtil.getExtension(patch);
            Path input = Path.of(args[1]);
            String inputName;
            byte[] inputBytes;
            if (PathUtil.isZip(input)) {
                Map<String, byte[]> zipContent = Zip.readAllBytes(input);
                if (zipContent.size() != 1) {
                    throw new IllegalArgumentException("zip contains " + zipContent.size() + " files.");
                }
                inputName = zipContent.keySet().iterator().next();
                inputBytes = zipContent.get(inputName);
            } else {
                inputName = PathUtil.getName(input);
                inputBytes = Files.readAllBytes(input);
            }
            Path output = Path.of(args[2]);
            String outputName = null;
            byte[] outputBytes = null;
            switch (extension) {
                case "ips":
                    IPS ips = IPS.parseIPSFile(new MarcFile(patch));
                    MarcFile ipsOut = ips.apply(new MarcFile(inputBytes));
                    outputBytes = ipsOut.save();
                    break;
                case "ups":
                    UPS ups = UPS.parseUPSFile(new MarcFile(patch));
                    MarcFile upsOut = ups.apply(new MarcFile(inputBytes), true);
                    outputBytes = upsOut.save();
                    break;
                case "bps":
                    BPS bps = BPS.parseBPSFile(new MarcFile(patch));
                    MarcFile bpsOut = bps.apply(new MarcFile(inputBytes), true);
                    outputBytes = bpsOut.save();
                    break;
                default:
                    throw new IllegalArgumentException("Unsupported patch format.");
            }
            if (PathUtil.isZip(output)) {
                String originalExtension = PathUtil.getExtension(inputName);
                if (originalExtension != null) {
                    outputName = PathUtil.getNameWithoutExtension(output) + "." + PathUtil.getExtension(inputName);
                } else {
                    outputName =  PathUtil.getNameWithoutExtension(output);
                }
                Zip.write(output, Map.of(outputName, outputBytes));
            } else {
                Files.write(output, outputBytes);
            }
        }
    }

    public static String helpMsg() {
        return "usage: \n" +
                "\t\t java -jar rom-patcher.jar \"patch\" \"inputRom\" \"outputRom\" \n";
    }
    public static void help() {
        System.out.print(helpMsg());
    }
}
