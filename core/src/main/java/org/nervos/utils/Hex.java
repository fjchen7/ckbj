package org.nervos.utils;

import java.math.BigInteger;

public class Hex {
    private static final char[] HEX_ARRAY = "0123456789abcdef".toCharArray();

    public static String encode(byte[] in) {
        char[] hexChars = new char[in.length * 2];
        for (int j = 0; j < in.length; j++) {
            int v = in[j] & 0xFF;
            hexChars[j * 2] = HEX_ARRAY[v >>> 4];
            hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
        }
        return "0x" + String.valueOf(hexChars);
    }

    public static String encode(BigInteger in) {
        return encode(in.toByteArray());
    }

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
