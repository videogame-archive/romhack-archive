package com.github.openretrogamingarchive.model;

import java.util.List;

public record Patch(
        List<String> authors,
        String url,
        String version,
        String releaseDate,
        String alternative,
        List<String> labels
        ) {

}
