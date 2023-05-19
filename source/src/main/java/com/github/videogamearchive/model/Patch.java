package com.github.videogamearchive.model;

import java.util.List;

public record Patch(
        Long id,
        String name,
        List<String> authors,
        String shortAuthors,
        String url,
        List<String> otherUrls,
        String version,
        String releaseDate,
        String options,
        List<Label> labels,
        List<Media> medias
        ) {

}
