package org.ckbj.utils;

public class Hex {
    private static final char[] HEX_ARRAY = "0123456789abcdef".toCharArray();

    /**
     * Convert byte array to hex string with 0x prefix.
     *
     * @param in byte array
     * @return hex string
     */
    public static String encode(byte[] in) {
        if (in == null || in.length == 0) {
            return "0x";
        }
        char[] hexChars;
        int i = 0, j = 0;
        if (in[0] >>> 4 == 0) {
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
        return "0x" + String.valueOf(hexChars);
    }

    /**
     * Convert hex string to byte array.
     *
     * @param in hex string with or without 0x
     * @return byte array
     */
    public static byte[] decode(String in) {
        in = formatHexString(in);
        if (!isHexString(in)) {
            throw new IllegalArgumentException("Illegal hex string");
        }
        int len = in.length();
        byte[] data = new byte[(len + 1) / 2];
        int i = 0, j = 0;
        if (len % 2 != 0) {
            data[i] = (byte) Character.digit(in.charAt(j), 16);
            i++;
            j++;
        }
        for (; j < len; i++, j += 2) {
            data[i] = (byte) ((Character.digit(in.charAt(j), 16) << 4)
                    + Character.digit(in.charAt(j + 1), 16));
        }
        return data;
    }

    /**
     * Check if the given string is a hex string.
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
        if (in == null) {
            return "";
        }
        if (in.startsWith("0x")) {
            in = in.substring(2);
        }
        return in.toLowerCase();
    }
}
