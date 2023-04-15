package com.github.videogamearchive;

import com.github.videogamearchive.community.rhdn.Resource;
import com.github.videogamearchive.model.Patch;
import com.github.videogamearchive.model.Romhack;
import com.github.videogamearchive.model.RomhackReaderWriter;
import com.github.videogamearchive.model.RomhackValidator;
import com.github.videogamearchive.util.CSV;
import com.github.videogamearchive.util.PathUtil;
import com.github.videogamearchive.util.StringUtil;
import org.apache.commons.csv.CSVRecord;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class Migration {
    public static File getDownload(String pathToDownload) {
        File dir = Path.of(pathToDownload).toFile();
        File[] files = dir.listFiles();
        for (File file:files) {
            if (file.isFile() && !PathUtil.isExtension(file.toPath(), "png")) {
                return file;
            }
        }
        return null;
    }

    public static void main(String[] args) throws ReflectiveOperationException, IOException {
        File rhdn = Path.of("../../romhack-archive-crawler/community-romhacking.net/raw/www.romhacking.net").toFile();

        RomhackReaderWriter romhackReaderWriter = new RomhackReaderWriter();
        File root = Path.of("../Nintendo - Super Nintendo Entertainment System").toFile();
        for (File parent:root.listFiles()) {
            for (File hack:parent.listFiles()) {
                System.out.println(hack.getName());
                Path json = hack.toPath().resolve("romhack.json");
                Romhack romhack = romhackReaderWriter.read(json);
                String expectedFolderNamePostfix = RomhackValidator.getExpectedFolderNamePostfix(romhack);
                String currentName = hack.getName();
                String extension = PathUtil.getExtension(currentName);
                int indexOfEnd = currentName.indexOf("[");
                String newName = currentName.substring(0, indexOfEnd) + expectedFolderNamePostfix + "." + extension;
                File newHack = parent.toPath().resolve(newName).toFile();
                hack.renameTo(newHack);
                //System.out.println(hack + " -> " + newHack);
//                List<Patch> patches = new ArrayList<>();
//                for (int i = 0; i < romhack.patches().size(); i++) {
//                    Patch patch = romhack.patches().get(i);
//                    CSVRecord info = Resource.getInfo(patch.url());
//                    if (patch.url().equals("https://www.romhacking.net/translations/341/")) {
//                        int breakpoint = 0;
//                    }
////                    String relativeDownloadPath = info.get("Download");
////                    String absoluteDownloadPath = rhdn.getCanonicalPath() + relativeDownloadPath;
////                    System.out.println(absoluteDownloadPath);
////                    File file = getDownload(absoluteDownloadPath);
////                    File patchDir = hack.toPath().resolve("romhack-original").resolve("" + (i + 1)).toFile();
////                    if (patchDir.exists()) {
////                        System.out.println(patchDir.getCanonicalPath());
////                    } else {
////                        Files.createDirectory(patchDir.toPath());
////                        Files.copy(file.toPath(), patchDir.toPath().resolve(file.getName()));
////                    }
//                    List<String> authors = new ArrayList<>();
//                    String releasedByAsString = info.get("Released By");
//                    if (releasedByAsString != null && !releasedByAsString.isBlank()) {
//                        List<String> releasedBy = StringUtil.substrings(releasedByAsString, "[", "]", true);
//                        authors.addAll(releasedBy);
//                    }
//                    String creditsAsString = info.get("Credits");
//                    if (creditsAsString != null && !creditsAsString.isBlank()) {
//                        List<String> credits = StringUtil.substrings(creditsAsString, "[", "]", true);
//                        for (String credit:credits) {
//                            if (!authors.contains(credit)) {
//                                authors.add(credit);
//                            }
//                        }
//                    }
//
//                    Patch patchFix = new Patch(
//                            new ArrayList<>(authors),
//                            patch.url(),
//                            patch.version(),
//                            patch.releaseDate(),
//                            patch.alternative(),
//                            patch.labels());
//
//                    patches.add(patchFix);
//                }
//
//                romhack.patches().clear();
//                romhack.patches().addAll(patches);
//                romhackReaderWriter.write(romhack, json);
            }
        }
    }
}
