package org.ckbj.utils;

import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.nio.ByteOrder;

import static org.junit.jupiter.api.Assertions.*;

public class HexTest {
    @Test
    public void testToHexString() {
        assertEquals("0x1234", Hex.toHexString(new byte[]{0x12, 0x34}));
        assertEquals("0x0123456789abcdef",
                     Hex.toHexString(new byte[]{0x01, 0x23, 0x45, 0x67,
                                                (byte) 0x89, (byte) 0xab,
                                                (byte) 0xcd, (byte) 0xef}));
        assertEquals("0x", Hex.toHexString(new byte[]{}));
        assertEquals("0x00", Hex.toHexString(new byte[]{0x0}));
        assertEquals("0x000000", Hex.toHexString(new byte[]{0x0, 0x0, 0x0}));
        assertEquals("0x0234", Hex.toHexString(new byte[]{0x02, 0x34}));

        assertEquals("0x00", Hex.toHexString(new byte[]{0x0}, true));
        assertEquals("0x000000", Hex.toHexString(new byte[]{0x0, 0x0, 0x0}, true));
        assertEquals("0x0234", Hex.toHexString(new byte[]{0x02, 0x34}, true));
        assertEquals("0234", Hex.toHexString(new byte[]{0x02, 0x34}, false));
    }

    @Test
    public void testToByteArray() {
        byte[] bytes = new byte[]{0x01, 0x23, 0x45, 0x67, (byte) 0x89, (byte) 0xab, (byte) 0xcd, (byte) 0xef};
        assertArrayEquals(bytes, Hex.toByteArray("0x0123456789abcdef"));
        assertArrayEquals(bytes, Hex.toByteArray("0x0123456789AbcDeF"));

        assertArrayEquals(new byte[]{0x1}, Hex.toByteArray("0x1"));
        assertArrayEquals(new byte[]{0x1}, Hex.toByteArray("0x01"));
        assertArrayEquals(new byte[]{0x0, 0x1}, Hex.toByteArray("0x001"));
        assertArrayEquals(new byte[]{0x1, 0x23}, Hex.toByteArray("0x123"));

        assertArrayEquals(new byte[]{0}, Hex.toByteArray("0x0"));
        assertArrayEquals(new byte[]{0}, Hex.toByteArray("0x00"));
        assertArrayEquals(new byte[]{0, 0, 0}, Hex.toByteArray("0x00000"));

        assertArrayEquals(new byte[]{}, Hex.toByteArray("0x"));
        assertArrayEquals(new byte[]{}, Hex.toByteArray(""));

        assertThrows(IllegalArgumentException.class, () -> {
            Hex.toByteArray("0x01234akf");
        });

        assertArrayEquals(new byte[]{0x30, 0x39}, Hex.toByteArray(new BigInteger("12345")));
        assertArrayEquals(new byte[]{0x30, 0x39}, Hex.toByteArray(new BigInteger("12345"), -1));
        assertArrayEquals(new byte[]{0x0, 0x0, 0x30, 0x39}, Hex.toByteArray(new BigInteger("12345"), 4));
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> {
            Hex.toByteArray(new BigInteger("12345"), 1);
        });

    }

    @Test
    public void testToBigInteger() {
        assertEquals(new BigInteger("234807695289199110200921978552702164240"),
                     Hex.toBigInteger("0xb0a65130f06c64071d3c47901a375d10"));
    }

    @Test
    public void testLittleEndian() {
        ByteOrder order = ByteOrder.LITTLE_ENDIAN;
        assertArrayEquals(new byte[]{0x39, 0x30}, Hex.toByteArray(new BigInteger("12345"), -1, order));
        assertArrayEquals(new byte[]{0x39, 0x30, 0x0, 0x0}, Hex.toByteArray(new BigInteger("12345"), 4, order));

        assertEquals(new BigInteger("1684861104"), Hex.toBigInteger("0xb0f06c64", order));
    }
}
