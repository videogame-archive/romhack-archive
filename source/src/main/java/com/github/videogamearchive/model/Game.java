package com.github.videogamearchive.model;

public record Game(String name, Long id) implements Identifiable<Game> {
    @Override
    public Game withId(Long id) {
        return new Game(name, id);
    }
}
