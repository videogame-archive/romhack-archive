package com.github.openretrogamingarchive.model;

public record Info(
        String name,
        Boolean translatedTitle,
        Status status,
        Boolean adult,
        Boolean offensive
        //Boolean excluded
        ) {

}
