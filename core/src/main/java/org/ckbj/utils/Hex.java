package org.ckbj.utils;

import java.math.BigInteger;
import java.nio.ByteOrder;

import static java.nio.ByteOrder.BIG_ENDIAN;
import static java.nio.ByteOrder.LITTLE_ENDIAN;

public class Hex {
    private static final char[] HEX_CHARS = "0123456789abcdef".toCharArray();
    private static final String HEX_PREFIX = "0x";

    /**
     * Convert byte array to hex string with 0x
     */
    public static String toHexString(byte[] hex) {
        return toHexString(hex, true);
    }

    /**
     * Convert byte array to hex string
     *
     * @param hex   byte array.
     * @param add0x whether add "0x" prefix to the returned hex string.
     * @return hex string
     */
    public static String toHexString(byte[] hex, boolean add0x) {
        char[] hexChars;
        hexChars = new char[hex.length * 2];
        for (int i = 0; i < hex.length; i++) {
            int b = hex[i] & 0xFF;
            hexChars[i * 2] = HEX_CHARS[b >>> 4];
            hexChars[i * 2 + 1] = HEX_CHARS[b & 0x0F];
        }
        return (add0x ? HEX_PREFIX : "") + String.valueOf(hexChars);
    }

    /**
     * Convert hex string to byte array.
     *
     * @param hex hex string with or without prefix "0x"
     * @return byte array
     */
    public static byte[] toByteArray(String hex) {
        hex = formatHexString(hex);
        int length = hex.length();
        byte[] bytes = new byte[(length + 1) / 2];
        int i = 0, j = 0;
        if (length % 2 != 0) {
            bytes[i] = (byte) Character.digit(hex.charAt(j), 16);
            i++;
            j++;
        }
        while (j < length) {
            bytes[i] = (byte) ((Character.digit(hex.charAt(j), 16) << 4)
                    + Character.digit(hex.charAt(j + 1), 16));
            i++;
            j += 2;
        }
        return bytes;
    }

    /**
     * Convert BigInteger to byte array by big endian.
     */
    public static byte[] toByteArray(BigInteger in) {
        return toByteArray(in, -1);
    }

    /**
     * Convert BigInteger to zero-padded byte array.
     *
     * @param in     BigInteger
     * @param length of the returned byte array. It pads zero at beginning of
     *               the byte array until its length is `length`.
     *               -1 means no padding.
     * @param order  the order of byte array, little or big endian.
     * @return byte array
     */
    public static byte[] toByteArray(BigInteger in, int length, ByteOrder order) {
        byte[] arr = in.toByteArray();
        if (arr[0] == 0 && arr.length > 1) {
            byte[] tmp = new byte[arr.length - 1];
            System.arraycopy(arr, 1, tmp, 0, tmp.length);
            arr = tmp;
        }
        // if length is specified, pad with 0
        if (length != -1) {
            byte[] src = arr;
            arr = new byte[length];
            System.arraycopy(src, 0, arr, arr.length - src.length, src.length);
        }
        if (order == BIG_ENDIAN) {
            return arr;
        } else if (order == LITTLE_ENDIAN) {
            return reverse(arr);
        } else {
            throw new IllegalArgumentException("Unknown order: " + order);
        }
    }

    /**
     * Convert BigInteger to zero-padded byte array by big endian.
     */
    public static byte[] toByteArray(BigInteger in, int length) {
        return toByteArray(in, length, BIG_ENDIAN);
    }

    /**
     * Convert hex string to BigInteger.
     *
     * @param hex hex string with or without prefix "0x"
     * @return BigInteger
     */
    public static BigInteger toBigInteger(String hex, ByteOrder order) {
        hex = formatHexString(hex);
        return toBigInteger(toByteArray(hex), order);
    }


    /**
     * Convert hex string to BigInteger.
     *
     * @param hex hex string with or without prefix "0x"
     * @return BigInteger
     */
    public static BigInteger toBigInteger(String hex) {
        return toBigInteger(hex, BIG_ENDIAN);
    }

    /**
     * Convert byte array string to BigInteger by big endian.
     *
     * @param hex byte array without sign bit
     * @return BigInteger
     */
    public static BigInteger toBigInteger(byte[] hex) {
        return toBigInteger(hex, BIG_ENDIAN);
    }

    /**
     * Convert byte array string to BigInteger.
     *
     * @param hex   byte array without sign bit
     * @param order the order of byte array, little or big endian.
     * @return BigInteger
     */
    public static BigInteger toBigInteger(byte[] hex, ByteOrder order) {
        if (order == BIG_ENDIAN) {
            return new BigInteger(1, hex);
        } else if (order == LITTLE_ENDIAN) {
            return new BigInteger(1, reverse(hex));
        } else {
            throw new IllegalArgumentException("Unknown order: " + order);
        }
    }

    /**
     * Check if the given string is a hex string.
     */
    public static boolean isHexString(String in) {
        if (in == null) {
            return false;
        }
        int i = 0;
        if (in.startsWith(HEX_PREFIX)) {
            i = 2;
        }
        for (; i < in.length(); i++) {
            char ch = Character.toLowerCase(in.charAt(i));
            if (!(ch >= '0' && ch <= '9' || ch >= 'a' && ch <= 'f' || ch >= 'A' && ch <= 'F')) {
                return false;
            }
        }
        return true;
    }

    private static String formatHexString(String hex) {
        if (!isHexString(hex)) {
            throw new IllegalArgumentException("Illegal hex string");
        }
        if (hex.startsWith(HEX_PREFIX)) {
            hex = hex.substring(2);
        }
        hex = hex.toLowerCase();
        return hex;
    }

    public static byte[] reverse(byte[] hex) {
        byte[] out = new byte[hex.length];
        for (int i = 0; i < hex.length; i++) {
            out[i] = hex[hex.length - i - 1];
        }
        return out;
    }
}
