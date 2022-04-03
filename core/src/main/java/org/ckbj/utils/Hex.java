package org.ckbj.utils;

import java.math.BigInteger;

public class Hex {
    private static final char[] HEX_ARRAY = "0123456789abcdef".toCharArray();
    private static final String HEX_PREFIX = "0x";

    /**
     * Convert byte array to hex string with 0x and with 0 padded
     */
    public static String encode(byte[] in) {
        return encode(in, true, true);
    }

    /**
     * Convert byte array to hex string
     *
     * @param in        byte array.
     * @param hasPrefix whether the returned hex string has "0x" prefix.
     * @param isPadded  whether the returned hex string has "0" padded in the beginning if needed.
     *                  For example, it returns "0x0123" for [0x01, 0x23] when isPadded is true, but "0x123" when false;
     * @return hex string
     */
    public static String encode(byte[] in, boolean hasPrefix, boolean isPadded) {
        if (in == null) {
            return null;
        }
        String hex;
        if (in.length == 0) {
            hex = "";
        } else {
            char[] hexChars;
            int i = 0, j = 0;
            if (!isPadded && in[0] >>> 4 == 0) {
                hexChars = new char[in.length * 2 - 1];
                hexChars[0] = HEX_ARRAY[in[0] & 0x0F];
                i = j = 1;
            } else {
                hexChars = new char[in.length * 2];
            }

            for (; j < in.length; i += 2, j++) {
                int v = in[j] & 0xFF;
                hexChars[i] = HEX_ARRAY[v >>> 4];
                hexChars[i + 1] = HEX_ARRAY[v & 0x0F];
            }
            hex = String.valueOf(hexChars);
        }
        return (hasPrefix ? HEX_PREFIX : "") + hex;
    }

    /**
     * Convert hex string to byte array.
     *
     * @param in hex string with or without 0x
     * @return byte array
     */
    public static byte[] decode(String in) {
        if (in == null) {
            return null;
        }
        in = formatHexString(in);
        if (!isHexString(in)) {
            throw new IllegalArgumentException("Illegal hex string");
        }
        int len = in.length();
        byte[] bytes = new byte[(len + 1) / 2];
        int i = 0, j = 0;
        if (len % 2 != 0) {
            bytes[i] = (byte) Character.digit(in.charAt(j), 16);
            i++;
            j++;
        }
        for (; j < len; i++, j += 2) {
            bytes[i] = (byte) ((Character.digit(in.charAt(j), 16) << 4)
                    + Character.digit(in.charAt(j + 1), 16));
        }
        return bytes;
    }

    public static String toHexString(BigInteger in, boolean hasPrefix, boolean isPadded) {
        return encode(decode(in), hasPrefix, isPadded);
    }

    public static byte[] decode(BigInteger in) {
        byte[] array = in.toByteArray();
        if (array[0] == 0 && array.length > 1) {
            byte[] tmp = new byte[array.length - 1];
            System.arraycopy(array, 1, tmp, 0, tmp.length);
            array = tmp;
        }
        return array;
    }

    public static BigInteger hexStringToBigInteger(String in) {
        in = formatHexString(in);
        return new BigInteger(in, 16);
    }

    /**
     * Check if the given string is a hex string.
     *
     * @return true if {@code in} is a hex string
     */
    public static boolean isHexString(String in) {
        if (in == null) {
            return false;
        }
        in = formatHexString(in);
        for (int i = 0; i < in.length(); i++) {
            char ch = Character.toLowerCase(in.charAt(i));
            if (!(ch >= '0' && ch <= '9' || ch >= 'a' && ch <= 'f')) {
                return false;
            }
        }
        return true;
    }

    private static String formatHexString(String in) {
        if (in.startsWith(HEX_PREFIX)) {
            in = in.substring(2);
        }
        return in.toLowerCase();
    }
}
