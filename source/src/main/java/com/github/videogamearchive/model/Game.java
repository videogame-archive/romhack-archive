package com.github.videogamearchive.model;

public record Game(Long id) implements Identifiable<Game> {
    @Override
    public Game withId(Long id) {
        return new Game(id);
    }
}
