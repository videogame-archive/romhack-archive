package com.github.videogamearchive.model;

import java.util.Arrays;

public enum Status {
    FullyPlayable("Fully Playable"),
    Incomplete("Incomplete");
    private final String label;

    Status(String displayName) {
        this.label = displayName;
    }
    @Override public String toString() { return label; }

    public static Status valueOfLabel(String value) {
        if (value == null) {
            return null;
        }
        for (Status status: Status.values()) {
            if (status.label.equals(value)) {
                return status;
            }
        }
        throw new IllegalArgumentException("value '" + value + "' is not a valid " + Status.class.getSimpleName() + ", valid values are: " + Arrays.toString(Status.values()));
    }
}