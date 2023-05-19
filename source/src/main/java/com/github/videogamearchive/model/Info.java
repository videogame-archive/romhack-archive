package com.github.videogamearchive.model;

public record Info(
        String name,
        Boolean translatedTitle,
        Status status,
        Boolean adult,
        Boolean offensive,
        Boolean obsoleteVersion,
        Boolean backCatalog
        ) {

}
