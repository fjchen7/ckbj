package org.ckbj.crypto;

import org.bouncycastle.crypto.digests.Blake2bDigest;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class Blake2b {
    private static final byte[] CKB_HASH_PERSONALIZATION = "ckb-default-hash".getBytes(StandardCharsets.UTF_8);
    private Blake2bDigest blake2bDigest;

    public Blake2b() {
        blake2bDigest = new Blake2bDigest(null, 32, null, CKB_HASH_PERSONALIZATION);
    }

    public void update(byte[] input) {
        blake2bDigest.update(input, 0, input.length);
    }

    public void update(byte[] input, int offset, int len) {
        blake2bDigest.update(input, offset, len);
    }

    public byte[] doFinal256() {
        byte[] out = new byte[32];
        blake2bDigest.doFinal(out, 0);
        return out;
    }

    public byte[] doFinal160() {
        return Arrays.copyOfRange(doFinal256(), 0, 20);
    }

    public static byte[] digest256(byte[] input) {
        Blake2b blake2b = new Blake2b();
        blake2b.update(input);
        return blake2b.doFinal256();
    }

    public static byte[] digest160(byte[] input) {
        Blake2b blake2b = new Blake2b();
        blake2b.update(input);
        return blake2b.doFinal160();
    }
}
