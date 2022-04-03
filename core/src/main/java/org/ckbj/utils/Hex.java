package org.ckbj.utils;

import java.math.BigInteger;

public class Hex {
    private static final char[] HEX_CHARS = "0123456789abcdef".toCharArray();
    private static final String HEX_PREFIX = "0x";

    /**
     * Convert byte array to hex string with 0x
     */
    public static String toHexString(byte[] in) {
        return toHexString(in, true);
    }

    /**
     * Convert byte array to hex string
     *
     * @param in           byte array.
     * @param appendPrefix whether add "0x" prefix to the returned hex string.
     * @return hex string
     */
    public static String toHexString(byte[] in, boolean appendPrefix) {
        char[] hexChars;
        hexChars = new char[in.length * 2];
        for (int i = 0; i < in.length; i++) {
            int hex = in[i] & 0xFF;
            hexChars[i * 2] = HEX_CHARS[hex >>> 4];
            hexChars[i * 2 + 1] = HEX_CHARS[hex & 0x0F];
        }
        return (appendPrefix ? HEX_PREFIX : "") + String.valueOf(hexChars);
    }

    /**
     * Convert BigInteger to hex string
     *
     * @param in           BigInteger
     * @param appendPrefix whether add "0x" prefix to the returned hex string.
     * @return hex string
     */
    public static String toHexString(BigInteger in, boolean appendPrefix) {
        return toHexString(toByteArray(in), appendPrefix);
    }

    /**
     * Only keep significant hex in hex string. For example, it returns "0x123" for input "0x0123".
     *
     * @param in hex string with or without prefix "0x"
     * @return hex string that only keeps significant hex
     */
    public static String onlyKeepSignificantHex(String in) {
        StringBuilder builder = new StringBuilder();
        int i = 0;
        if (in.startsWith(HEX_PREFIX)) {
            builder.append(HEX_PREFIX);
            i = 2;
        }
        while (i < in.length() - 1 && in.charAt(i) == '0') {
            i++;
        }
        builder.append(in.substring(i));
        return builder.toString();
    }

    /**
     * Convert hex string to byte array.
     *
     * @param in hex string with or without prefix "0x"
     * @return byte array
     */
    public static byte[] toByteArray(String in) {
        in = formatHexString(in);
        int length = in.length();
        byte[] bytes = new byte[(length + 1) / 2];
        int i = 0, j = 0;
        if (length % 2 != 0) {
            bytes[i] = (byte) Character.digit(in.charAt(j), 16);
            i++;
            j++;
        }
        while (j < length) {
            bytes[i] = (byte) ((Character.digit(in.charAt(j), 16) << 4)
                    + Character.digit(in.charAt(j + 1), 16));
            i++;
            j += 2;
        }
        return bytes;
    }

    /**
     * Convert BigInteger to byte array.
     */
    public static byte[] toByteArray(BigInteger in) {
        return toByteArray(in, -1);
    }

    /**
     * Convert BigInteger to byte array with fixed length.
     *
     * @param in     BigInteger
     * @param length of the returned byte array. No fix length if length is -1.
     */
    public static byte[] toByteArray(BigInteger in, int length) {
        byte[] array = in.toByteArray();
        if (array[0] == 0 && array.length > 1) {
            byte[] tmp = new byte[array.length - 1];
            System.arraycopy(array, 1, tmp, 0, tmp.length);
            array = tmp;
        }
        if (length == -1) {
            return array;
        } else {
            byte[] out = new byte[length];
            System.arraycopy(array, 0, out, out.length - array.length, array.length);
            return out;
        }
    }

    /**
     * Convert hex string to BigInteger
     *
     * @param in hex string with or without prefix "0x"
     * @return BigInteger
     */
    public static BigInteger toBigInteger(String in) {
        in = formatHexString(in);
        return new BigInteger(in, 16);
    }

    /**
     * Convert byte array string to BigInteger
     *
     * @param in byte array without sign bit
     * @return BigInteger
     */
    public static BigInteger toBigInteger(byte[] in) {
        return new BigInteger(1, in);
    }

    /**
     * Check if the given string is a hex string.
     *
     * @param in hex string with or without prefix "0x"
     * @return true if the input is a hex string, otherwise false
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

    private static String formatHexString(String in) {
        if (!isHexString(in)) {
            throw new IllegalArgumentException("Illegal hex string");
        }
        if (in.startsWith(HEX_PREFIX)) {
            in = in.substring(2);
        }
        in = in.toLowerCase();
        return in;
    }
}
