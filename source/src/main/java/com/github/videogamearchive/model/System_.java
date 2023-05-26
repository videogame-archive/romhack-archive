package com.github.videogamearchive.model;

public record System_(String name, Long id) implements Identifiable<System_> {
    @Override
    public System_ withId(Long id) {
        return new System_(name, id);
    }
}
