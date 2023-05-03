package com.github.videogamearchive.util;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.zip.CRC32;

public class Hashes {

    private Hashes() {
        // Private constructor to make clear that is a non-instantiable utility class
    }

    public static String getMd5(byte[] bytes) throws NoSuchAlgorithmException {
        // Static getInstance method is called with hashing MD5
        MessageDigest md = MessageDigest.getInstance("MD5");

        // digest() method is called to calculate message digest
        // of an input digest() return array of byte
        byte[] messageDigest = md.digest(bytes);

        // Convert byte array into signum representation
        BigInteger no = new BigInteger(1, messageDigest);

        // Convert message digest into hex value
        String hashtext = no.toString(16);
        while (hashtext.length() < 32) {
            hashtext = "0" + hashtext;
        }
        return hashtext;
    }

    public static String getSha1(byte[] bytes) throws NoSuchAlgorithmException {
        // Static getInstance method is called with hashing MD5
        MessageDigest md = MessageDigest.getInstance("SHA-1");

        // digest() method is called to calculate message digest
        // of an input digest() return array of byte
        byte[] messageDigest = md.digest(bytes);

        // Convert byte array into signum representation
        BigInteger no = new BigInteger(1, messageDigest);

        // Convert message digest into hex value
        String hashtext = no.toString(16);
        while (hashtext.length() < 40) {
            hashtext = "0" + hashtext;
        }
        return hashtext;
    }

    public static String getCrc32(byte[] bytes) {
        CRC32 crc32 = new CRC32();
        crc32.update(bytes);
        return getCrc32String(crc32.getValue());
    }

    public static String getCrc32String(long value) {
        String hexString = Integer.toHexString((int) value);
        while (hexString.length() < 8) {
            hexString = "0" + hexString;
        }
        return hexString;
    }

}
