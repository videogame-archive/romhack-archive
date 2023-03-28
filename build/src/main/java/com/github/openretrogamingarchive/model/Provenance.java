package com.github.openretrogamingarchive.model;

public record Provenance(
        String retrievedBy,
        String retrievedDate,
        Source source) {

}
