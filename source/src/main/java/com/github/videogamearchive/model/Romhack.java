package com.github.videogamearchive.model;

import java.util.List;

public record Romhack(
        Long id,
        Info info,
        Provenance provenance,
        Rom rom,
        List<Patch> patches) implements Identifiable {
}
