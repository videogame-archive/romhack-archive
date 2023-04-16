package com.github.videogamearchive.model;

import java.util.List;

public record Patch(
        List<String> authors,
        String shortAuthors,
        String url,
        String version,
        String releaseDate,
        String options,
        List<String> labels
        ) {

}
