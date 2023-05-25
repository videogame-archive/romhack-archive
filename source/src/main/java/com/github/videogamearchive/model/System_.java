package com.github.videogamearchive.model;

public record System_(Long id) implements Identifiable<System_> {
    @Override
    public System_ withId(Long id) {
        return new System_(id);
    }
}
