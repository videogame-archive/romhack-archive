package com.github.videogamearchive.database;

import com.github.videogamearchive.model.Identifiable;
import com.github.videogamearchive.model.Patch;
import com.github.videogamearchive.model.Romhack;
import com.github.videogamearchive.model.System_;
import com.github.videogamearchive.model.Game;
import com.github.videogamearchive.util.CSV;

import java.util.ArrayList;
import java.util.List;

public record ExtendedRomhack(
        String systemFolderName,
        System_ system,
        String parentFolderName,
        Game game,
        String romhackFolderName,
        Romhack romhack) implements Comparable<ExtendedRomhack>, Identifiable<ExtendedRomhack> {

    public static String[] headers() {
        String[] indexHeaders = new String[] {
                // ## System
                "System Folder Name", "System Id", "System Name (original)",
                // ## Parent
                "Parent Folder Name", "Game Id", "Parent Name (original)",
                // ## Romhack
                "Romhack Folder Name",
                // Id
                "Romhack Id",
                // Info
                "Name (original)", "Translated Title", "Status", "Adult", "Offensive", "Obsolete Version", "Back Catalog",
                // Provenance
                "Retrieved By", "Retrieved Date", "Source", "Notes",
                // Rom
                "Size", "CRC32", "MD5", "SHA-1",
                // Patch 1
                "Id", "Authors", "Short Authors", "Url", "Other Urls", "Version", "Release Date", "Options", "Labels",
                // Patch 2-5
        };

        List<String> indexHeadersAsList = new ArrayList<>(List.of(indexHeaders));

        for (int i = 2; i <= 5; i++) {
            indexHeadersAsList.addAll(List.of(
                    "Id (" + i + ")",
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

        return indexHeadersAsList.toArray(new String[] {});
    }

    public String[] row() {
        String[] index = new String[] {
                // ## System
                CSV.toString(systemFolderName), CSV.toString(system.id()), CSV.toString(system.name()),
                // ## Parent
                CSV.toString(parentFolderName), CSV.toString(game.id()), CSV.toString(game.name()),
                // ## Romhack
                CSV.toString(romhackFolderName),
                // Id
                CSV.toString(romhack.id()),
                // Info
                CSV.toString(romhack.info().name()), CSV.toString(romhack.info().translatedTitle()), CSV.toString(romhack.info().status()), CSV.toString(romhack.info().adult()), CSV.toString(romhack.info().offensive()), CSV.toString(romhack.info().obsoleteVersion()), CSV.toString(romhack.info().backCatalog()),
                // Provenance
                CSV.toString(romhack.provenance().retrievedBy()), CSV.toString(romhack.provenance().retrievedDate()), CSV.toString(romhack.provenance().source()), CSV.toString(romhack.provenance().notes()),
                // Rom
                CSV.toString(romhack.rom().size()), CSV.toString(romhack.rom().crc32()), CSV.toString(romhack.rom().md5()), CSV.toString(romhack.rom().sha1()),
        };

        List<String> romAsList = new ArrayList<>(List.of(index));

        int numPatches = 0;
        for (Patch patch:romhack.patches()) {
            romAsList.add(CSV.toString(patch.id()));
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

        for (int i = numPatches; i <= 5; i++) {
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

    //
    //
    //

    @Override
    public int compareTo(ExtendedRomhack o) {
        return romhackFolderName.compareTo(o.romhackFolderName);
    }

    //
    //
    //

    @Override
    public Long id() {
        return romhack.id();
    }

    @Override
    public ExtendedRomhack withId(Long id) {
        return new ExtendedRomhack(systemFolderName, system, parentFolderName, game, romhackFolderName, romhack.withId(id));
    }

    //
    //
    //

    public String getDownload() {
        return CSV.toString("https://github.com/videogame-archive/romhack-archive/raw/main/database/" + systemFolderName + "/" + parentFolderName + "/" + romhackFolderName + "/romhack.bps");
    }
}