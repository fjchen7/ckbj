package org.ckbj.crypto;

import org.ckbj.utils.Hex;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class Blake2bTest {
    @Test
    public void testDigest() {
        byte[] input = new byte[]{0x1, 0x2};
        Assertions.assertArrayEquals(
                Hex.decode("0x4f0e79094368f9b3e9fbffa7d5098c39cc3744f0031b5c0aefea31a5bd8aba76"),
                Blake2b.digest256(input));
        Assertions.assertArrayEquals(
                Hex.decode("0x4f0e79094368f9b3e9fbffa7d5098c39cc3744f0"),
                Blake2b.digest160(input));
    }
}
