package com.github.videogamearchive.model;

public enum Source {
    Trusted("Trusted"), Unreliable("Unreliable");
    private final String displayName;

    Source(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}