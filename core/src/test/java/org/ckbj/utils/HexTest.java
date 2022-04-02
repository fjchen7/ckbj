package org.ckbj.utils;

import org.ckbj.utils.Hex;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class HexTest {
    @Test
    public void testEncode() {
        Assertions.assertEquals("0x1234", Hex.encode(new byte[]{0x12, 0x34}));
        Assertions.assertEquals("0x0123456789abcdef",
                Hex.encode(new byte[]{0x01, 0x23, 0x45, 0x67, (byte) 0x89, (byte) 0xab, (byte) 0xcd, (byte) 0xef}));

        Assertions.assertEquals(null, Hex.encode(null));
        Assertions.assertEquals("0x", Hex.encode(new byte[]{}));
        Assertions.assertEquals("0x00", Hex.encode(new byte[]{0x0}));
        Assertions.assertEquals("0x000000", Hex.encode(new byte[]{0x0, 0x0, 0x0}));
        Assertions.assertEquals("0x0234", Hex.encode(new byte[]{0x02, 0x34}));

        Assertions.assertEquals("0x0", Hex.encode(new byte[]{0x0}, true, false));
        Assertions.assertEquals("0x00000", Hex.encode(new byte[]{0x0, 0x0, 0x0}, true, false));
        Assertions.assertEquals("0x234", Hex.encode(new byte[]{0x02, 0x34}, true, false));
        Assertions.assertEquals("234", Hex.encode(new byte[]{0x02, 0x34}, false, false));
    }

    @Test
    public void testDecode() {
        byte[] bytes = new byte[]{0x01, 0x23, 0x45, 0x67, (byte) 0x89, (byte) 0xab, (byte) 0xcd, (byte) 0xef};
        Assertions.assertArrayEquals(bytes, Hex.decode("0x0123456789abcdef"));
        Assertions.assertArrayEquals(bytes, Hex.decode("0x0123456789AbcDeF"));

        Assertions.assertArrayEquals(new byte[]{0x1}, Hex.decode("0x1"));
        Assertions.assertArrayEquals(new byte[]{0x1}, Hex.decode("0x01"));
        Assertions.assertArrayEquals(new byte[]{0x0, 0x1}, Hex.decode("0x001"));
        Assertions.assertArrayEquals(new byte[]{0x1, 0x23}, Hex.decode("0x123"));

        Assertions.assertArrayEquals(new byte[]{0}, Hex.decode("0x0"));
        Assertions.assertArrayEquals(new byte[]{0}, Hex.decode("0x00"));
        Assertions.assertArrayEquals(new byte[]{0, 0, 0}, Hex.decode("0x00000"));

        Assertions.assertArrayEquals(new byte[]{}, Hex.decode("0x"));
        Assertions.assertArrayEquals(new byte[]{}, Hex.decode(""));
        Assertions.assertArrayEquals(null, Hex.decode(null));

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            Hex.decode("0x01234akf");
        });
    }
}
