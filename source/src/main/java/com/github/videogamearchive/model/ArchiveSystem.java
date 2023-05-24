package com.github.videogamearchive.model;

public record ArchiveSystem(Long id) implements Identifiable<ArchiveSystem> {
    @Override
    public ArchiveSystem withId(Long id) {
        return new ArchiveSystem(id);
    }
}
