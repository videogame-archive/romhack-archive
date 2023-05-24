package com.github.videogamearchive.model;

import java.util.List;

public record Romhack(
        Long id,
        Info info,
        Provenance provenance,
        Rom rom,
        List<Patch> patches) implements Identifiable<Romhack> {
    @Override
    public Romhack withId(Long id) {
        return new Romhack(id, info(), provenance(), rom(), patches());
    }
}
