package org.ckbj.utils;

import java.util.Arrays;

public class Hex {
    private static final char[] HEX_ARRAY = "0123456789abcdef".toCharArray();

    /**
     * Convert byte array to hex string
     *
     * @param in byte array
     * @return hex string
     */
    public static String encode(byte[] in) {
        if (in == null) {
            return "0x";
        }
        char[] hexChars = new char[in.length * 2];
        for (int j = 0; j < in.length; j++) {
            int v = in[j] & 0xFF;
            hexChars[j * 2] = HEX_ARRAY[v >>> 4];
            hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
        }
        int i = 0;
        while (i < hexChars.length - 1 && hexChars[i] == HEX_ARRAY[0]) {
            i++;
        }
        hexChars = Arrays.copyOfRange(hexChars, i, hexChars.length);
        return "0x" + String.valueOf(hexChars);
    }


    /**
     * Convert hex string to byte array
     *
     * @param in hex string
     * @return byte array
     */
    public static byte[] decode(String in) {
        in = formatHexString(in);
        if (!isHexString(in)) {
            throw new IllegalArgumentException("No a valid hex string");
        }
        int len = in.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(in.charAt(i), 16) << 4)
                    + Character.digit(in.charAt(i + 1), 16));
        }
        return data;
    }

    /**
     * Check if the given string is a hex string
     *
     * @return true if {@code in} is a hex string
     */
    public static boolean isHexString(String in) {
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
        if (in.startsWith("0x")) {
            in = in.substring(2);
        }
        return in.toLowerCase();
    }
}
