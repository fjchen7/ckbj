package io.github.fjchen7.ckbj.utils;

public class Hash {
    public static boolean isHash(byte[] hash) {
        if (hash == null) {
            return false;
        }
        return hash.length == 32;
    }
}
