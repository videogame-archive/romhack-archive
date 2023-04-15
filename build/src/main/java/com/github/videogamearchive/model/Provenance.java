package com.github.videogamearchive.model;

public record Provenance(
        String retrievedBy,
        String retrievedDate,
        Source source,
        String notes) {

}
