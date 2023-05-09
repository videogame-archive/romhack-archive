package com.github.videogamearchive.csv;

import com.github.videogamearchive.model.Patch;
import com.github.videogamearchive.model.Romhack;
import com.github.videogamearchive.util.CSV;

import java.util.ArrayList;
import java.util.List;

public record RomhackRow(
        String system,
        String parent,
        String name,
        Romhack romhack) implements Comparable<RomhackRow> {

    public static String[] headers() {
        return new String[] {
                // Folder names info
                "system", "parent", "name",
                // Info
                "name", "translatedTitle", "status", "adult", "offensive", "obsoleteVersion", "backCatalog",
                // Provenance
                "retrievedBy", "retrievedDate", "source", "notes",
                // Rom
                "size", "crc32", "md5", "sha1",
                // Patch 1
                "authors", "shortAuthors", "url", "otherUrls", "version", "releaseDate", "options", "labels",
                // Patch 2
                "authors", "shortAuthors", "url", "otherUrls", "version", "releaseDate", "options", "labels",
                // Patch 3
                "authors", "shortAuthors", "url", "otherUrls", "version", "releaseDate", "options", "labels",
                // Patch 4
                "authors", "shortAuthors", "url", "otherUrls", "version", "releaseDate", "options", "labels",
                // Patch 5
                "authors", "shortAuthors", "url", "otherUrls", "version", "releaseDate", "options", "labels",
        };
    }

    public String[] row() {
        String[] csv = new String[] {
                // Folder names info
                CSV.toString(system), CSV.toString(parent), CSV.toString(name),
                // Info
                CSV.toString(romhack.info().name()), CSV.toString(romhack.info().translatedTitle()), CSV.toString(romhack.info().status()), CSV.toString(romhack.info().adult()), CSV.toString(romhack.info().offensive()), CSV.toString(romhack.info().obsoleteVersion()), CSV.toString(romhack.info().backCatalog()),
                // Provenance
                CSV.toString(romhack.provenance().retrievedBy()), CSV.toString(romhack.provenance().retrievedDate()), CSV.toString(romhack.provenance().source()), CSV.toString(romhack.provenance().notes()),
                // Rom
                CSV.toString(romhack.rom().size()), CSV.toString(romhack.rom().crc32()), CSV.toString(romhack.rom().md5()), CSV.toString(romhack.rom().sha1()),
        };

        List<String> romAsList = new ArrayList<>(List.of(csv));

        for (Patch patch:romhack.patches()) {
            romAsList.add(CSV.toString(patch.authors()));
            romAsList.add(CSV.toString(patch.shortAuthors()));
            romAsList.add(CSV.toString(patch.url()));
            romAsList.add(CSV.toString(patch.otherUrls()));
            romAsList.add(CSV.toString(patch.version()));
            romAsList.add(CSV.toString(patch.releaseDate()));
            romAsList.add(CSV.toString(patch.options()));
            romAsList.add(CSV.toString(patch.labels()));
        }

        return romAsList.toArray(new String[] {});
    }
    @Override
    public int compareTo(RomhackRow o) {
        return name.compareTo(o.name);
    }
}