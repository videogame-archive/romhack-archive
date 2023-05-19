package com.github.videogamearchive.model;

public enum Status {
    FullyPlayable("Fully Playable"),
    Incomplete("Incomplete");
    private final String displayName;

    Status(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}