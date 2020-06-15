package itba.edu.ar.utils.criptography;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Hashing {

    public static byte[] md5(byte[] bytes) throws NoSuchAlgorithmException {
        return hash(bytes, "MD5");
    }

    public static byte[] sha1(byte[] bytes) throws NoSuchAlgorithmException {
        return hash(bytes, "SHA-1");
    }

    public static byte[] sha256(byte[] bytes) throws NoSuchAlgorithmException {
        return hash(bytes, "SHA-256");
    }

    private static byte[] hash(byte[] bytes, String method) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance(method);
        return md.digest(bytes);
    }

}
