package com.github.videogamearchive.model;

import java.util.List;

public record Hack(
        Long id,
        String name,
        List<String> authors,
        String shortAuthors,
        String url,
        List<String> otherUrls,
        String version,
        String releaseDate,
        List<String> options,
        String shortOptions,
        List<Label> labels,
        List<Media> medias
        ) implements Identifiable<Hack> {

        @Override
        public Hack withId(Long id) {
                return new Hack(id, name(), authors(), shortAuthors(), url(), otherUrls(), version(), releaseDate(), options(), shortOptions(), labels(), medias());
        }
}
