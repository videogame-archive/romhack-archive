package com.github.videogamearchive.model;

import java.util.List;

public record Patch(
        List<String> authors,
        String shortAuthors,
        String url,
        List<String> otherUrls,
        String version,
        String releaseDate,
        String options,
        List<Label> labels
        ) {

}
