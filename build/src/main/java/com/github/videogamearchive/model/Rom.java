package com.github.videogamearchive.model;

public record Rom(
        Long size,
        String crc32,
        String md5,
        String sha1) {

}
