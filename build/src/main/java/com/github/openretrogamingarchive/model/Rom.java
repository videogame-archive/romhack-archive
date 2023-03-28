package com.github.openretrogamingarchive.model;

public record Rom(
        Long size,
        String crc32,
        String md5,
        String sha1) {

}
