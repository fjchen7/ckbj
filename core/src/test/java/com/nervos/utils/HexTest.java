package com.nervos.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.nervos.utils.Hex;

public class HexTest {
    @Test
    public void testEncode() {
        byte[] bytes = new byte[]{0x01, 0x23, 0x45, 0x67, (byte) 0x89, (byte) 0xab, (byte) 0xcd, (byte) 0xef};
        String hex = "0x0123456789abcdef";
        Assertions.assertEquals(Hex.encode(bytes), hex);
    }

    @Test
    public void testDecode() {
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
