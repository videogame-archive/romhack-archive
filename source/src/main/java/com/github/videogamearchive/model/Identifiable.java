package com.github.videogamearchive.model;

public interface Identifiable<RECORD extends Identifiable<RECORD>> {
    Long id();
    RECORD withId(Long id);
}
