package io.github.fjchen7.ckbj.crypto;

import io.github.fjchen7.ckbj.utils.Hex;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

public class Blake2bTest {
    @Test
    public void testDigest() {
        byte[] input = new byte[]{0x1, 0x2};
        assertArrayEquals(
                Hex.toByteArray("0x4f0e79094368f9b3e9fbffa7d5098c39cc3744f0031b5c0aefea31a5bd8aba76"),
                Blake2b.digest(input));
    }
}
