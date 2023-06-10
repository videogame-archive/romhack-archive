package com.github.videogamearchive.model;

import java.util.List;

public record Release(
        Long id,
        Info info,
        Provenance provenance,
        Rom rom,
        List<Hack> hacks) implements Identifiable<Release> {
    @Override
    public Release withId(Long id) {
        return new Release(id, info(), provenance(), rom(), hacks());
    }
}
