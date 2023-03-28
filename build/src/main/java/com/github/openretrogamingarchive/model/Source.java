package com.github.openretrogamingarchive.model;

import java.util.Arrays;

public enum Source {
    Trusted("Trusted");
    private final String label;

    Source(String displayName) {
        this.label = displayName;
    }
    @Override public String toString() { return label; }

    public static Source valueOfLabel(String value) {
        if (value == null) {
            return null;
        }
        for (Source status: Source.values()) {
            if (status.label.equals(value)) {
                return status;
            }
        }
        throw new IllegalArgumentException("value '" + value + "' is not a valid " + Source.class.getSimpleName() + ", valid values are: " + Arrays.toString(Source.values()));
    }
}