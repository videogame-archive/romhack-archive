package com.github.videogamearchive.csv;

import com.github.videogamearchive.model.Patch;
import com.github.videogamearchive.model.Romhack;
import com.github.videogamearchive.util.CSV;

import java.util.ArrayList;
import java.util.List;

public record CSVRomhack(
        String system,
        String parent,
        String name,
        Romhack romhack) implements Comparable<CSVRomhack> {

    public static String[] headers() {
        String[] csvHeaders = new String[] {
                // Folder names info
                "Parent", "Name", "System",
                // Id
                "id",
                // Info
                "Name (original)", "Translated Title", "Status", "Adult", "Offensive", "Obsolete Version", "Back Catalog",
                // Provenance
                "Retrieved By", "Retrieved Date", "Source", "Notes",
                // Rom
                "Size", "CRC32", "MD5", "SHA-1",
                // Patch 1
                "Authors", "Short Authors", "Url", "Other Urls", "Version", "Release Date", "Options", "Labels",
                // Patch 2-5
        };

        List<String> csvHeadersAsList = new ArrayList<>(List.of(csvHeaders));

        for (int i = 2; i <= 5; i++) {
            csvHeadersAsList.addAll(List.of(
                    "Authors (" + i + ")",
                    "Short Authors (" + i + ")",
                    "Url (" + i + ")",
                    "Other Urls (" + i + ")",
                    "Version (" + i + ")",
                    "Release Date (" + i + ")",
                    "Options (" + i + ")",
                    "Labels (" + i + ")"
            ));
        }

        return csvHeadersAsList.toArray(new String[] {});
    }

    public String[] row() {
        String[] csv = new String[] {
                // Folder names info
                CSV.toString(parent), CSV.toString(name), CSV.toString(system),
                // Id
                CSV.toString(romhack.id()),
                // Info
                CSV.toString(romhack.info().name()), CSV.toString(romhack.info().translatedTitle()), CSV.toString(romhack.info().status()), CSV.toString(romhack.info().adult()), CSV.toString(romhack.info().offensive()), CSV.toString(romhack.info().obsoleteVersion()), CSV.toString(romhack.info().backCatalog()),
                // Provenance
                CSV.toString(romhack.provenance().retrievedBy()), CSV.toString(romhack.provenance().retrievedDate()), CSV.toString(romhack.provenance().source()), CSV.toString(romhack.provenance().notes()),
                // Rom
                CSV.toString(romhack.rom().size()), CSV.toString(romhack.rom().crc32()), CSV.toString(romhack.rom().md5()), CSV.toString(romhack.rom().sha1()),
        };

        List<String> romAsList = new ArrayList<>(List.of(csv));

        int numPatches = 0;
        for (Patch patch:romhack.patches()) {
            romAsList.add(CSV.toString(patch.authors()));
            romAsList.add(CSV.toString(patch.shortAuthors()));
            romAsList.add(CSV.toString(patch.url()));
            romAsList.add(CSV.toString(patch.otherUrls()));
            romAsList.add(CSV.toString(patch.version()));
            romAsList.add(CSV.toString(patch.releaseDate()));
            romAsList.add(CSV.toString(patch.options()));
            romAsList.add(CSV.toString(patch.labels()));
            numPatches++;
        }

        for (int i = numPatches; i < 5; i++) {
            romAsList.add("");
            romAsList.add("");
            romAsList.add("");
            romAsList.add("");
            romAsList.add("");
            romAsList.add("");
            romAsList.add("");
            romAsList.add("");
        }

        return romAsList.toArray(new String[] {});
    }
    @Override
    public int compareTo(CSVRomhack o) {
        return name.compareTo(o.name);
    }
}