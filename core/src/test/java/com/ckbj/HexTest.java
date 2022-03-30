package com.ckbj;

import org.ckbj.utils.Hex;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class HexTest {
    @Test
    public void encodeTest() {
        byte[] bytes = new byte[]{0x01, 0x23, 0x45, 0x67, (byte) 0x89, (byte) 0xab, (byte) 0xcd, (byte) 0xef};
        Assertions.assertEquals("0x123456789abcdef", Hex.encode(bytes));

        bytes = new byte[]{0x12, 0x34};
        Assertions.assertEquals("0x1234", Hex.encode(bytes));

        Assertions.assertEquals("0x", Hex.encode(null));
        Assertions.assertEquals("0x", Hex.encode(new byte[]{}));
        Assertions.assertEquals("0x0", Hex.encode(new byte[]{0x0}));
        Assertions.assertEquals("0x0", Hex.encode(new byte[]{0x0, 0x0, 0x0}));
    }

    @Test
    public void decodeTest() {
        byte[] bytes = new byte[]{0x01, 0x23, 0x45, 0x67, (byte) 0x89, (byte) 0xab, (byte) 0xcd, (byte) 0xef};
        byte[] bytesFromHex = Hex.decode("0x0123456789Abcdef");
        Assertions.assertEquals(bytes.length, bytesFromHex.length);
        for (int i = 0; i < bytes.length; i++) {
            Assertions.assertEquals(bytes[i], bytesFromHex[i]);
        }
        bytesFromHex = Hex.decode("0x0123456789AbcDeF");
        Assertions.assertEquals(bytes.length, bytesFromHex.length);
        for (int i = 0; i < bytes.length; i++) {
            Assertions.assertEquals(bytes[i], bytesFromHex[i]);
        }

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            Hex.decode("0x01234akf");
        });
    }
}
