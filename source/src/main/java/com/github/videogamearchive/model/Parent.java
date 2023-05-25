package com.github.videogamearchive.model;

public record Parent(Long id) implements Identifiable<Parent> {
    @Override
    public Parent withId(Long id) {
        return new Parent(id);
    }
}
