package com.ckbj.utils;

import org.ckbj.utils.Hex;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class HexTest {
    @Test
    public void testEncode() {
        byte[] bytes = new byte[]{0x01, 0x23, 0x45, 0x67, (byte) 0x89, (byte) 0xab, (byte) 0xcd, (byte) 0xef};
        Assertions.assertEquals("0x123456789abcdef", Hex.encode(bytes));

        Assertions.assertEquals("0x1234", Hex.encode(new byte[]{0x12, 0x34}));

        Assertions.assertEquals("0x", Hex.encode(null));
        Assertions.assertEquals("0x", Hex.encode(new byte[]{}));
        Assertions.assertEquals("0x0", Hex.encode(new byte[]{0x0}));
        Assertions.assertEquals("0x00000", Hex.encode(new byte[]{0x0, 0x0, 0x0}));
        Assertions.assertEquals("0x234", Hex.encode(new byte[]{0x02, 0x34}));
    }

    @Test
    public void testDecode() {
        byte[] bytes = new byte[]{0x01, 0x23, 0x45, 0x67, (byte) 0x89, (byte) 0xab, (byte) 0xcd, (byte) 0xef};
        Assertions.assertArrayEquals(bytes, Hex.decode("0x0123456789Abcdef"));
        Assertions.assertArrayEquals(bytes, Hex.decode("0x0123456789AbcDeF"));

        Assertions.assertArrayEquals(new byte[]{0x1}, Hex.decode("0x1"));
        Assertions.assertArrayEquals(new byte[]{0x1}, Hex.decode("0x01"));
        Assertions.assertArrayEquals(new byte[]{0x0, 0x1}, Hex.decode("0x001"));

        Assertions.assertArrayEquals(new byte[]{0}, Hex.decode("0x0"));
        Assertions.assertArrayEquals(new byte[]{0}, Hex.decode("0x00"));
        Assertions.assertArrayEquals(new byte[]{0, 0, 0}, Hex.decode("0x00000"));

        bytes = new byte[]{};
        Assertions.assertArrayEquals(bytes, Hex.decode("0x"));
        Assertions.assertArrayEquals(bytes, Hex.decode(""));
        Assertions.assertArrayEquals(bytes, Hex.decode(null));

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            Hex.decode("0x01234akf");
        });
    }
}
