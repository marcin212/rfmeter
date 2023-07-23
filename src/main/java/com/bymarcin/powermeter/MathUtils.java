package com.bymarcin.powermeter;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;

public class MathUtils {
    public static String addSI(long value, String postunit) {
        int unit = 1000;
        if (value < unit) return value + " " + postunit;
        int exp = (int) (Math.log(value) / Math.log(unit));
        char pre = "kMGTPE".charAt(exp - 1);
        return String.format("%.2f %s%s", value / Math.pow(unit, exp), pre, postunit);
    }

    public static String encryptPassword(String password) {
        String sha1 = "";
        try {
            MessageDigest crypt = MessageDigest.getInstance("SHA-1");
            crypt.reset();
            crypt.update(password.getBytes("UTF-8"));
            sha1 = byteToHex(crypt.digest());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return sha1;
    }

    private static String byteToHex(final byte[] hash) {
        Formatter formatter = new Formatter();
        for (byte b : hash) {
            formatter.format("%02x", b);
        }
        String result = formatter.toString();
        formatter.close();
        return result;

    }

    public static int toBGRA(float red, float green, float blue, float alfa) {
        int r = (int) (red * 255);
        int g = (int) (green * 255);
        int b = (int) (blue * 255);
        int a = (int) (alfa * 255);
        return b + ((g&255) << 8) + ((r&255) << 16) + ((a&255) << 24);
    }

    public static int toRGB(float red, float green, float blue) {
        int r = (int) (red * 255);
        int g = (int) (green * 255);
        int b = (int) (blue * 255);
        return b + ((g&255) << 8) + ((r&255) << 16);
    }

    public static int toBGRA(float red, float green, float blue) {
        return toBGRA(red, green, blue, 1.0f);
    }

    public static int fromLong(long l) {
        if(l > Integer.MAX_VALUE) {
            return Integer.MAX_VALUE;
        }
       return (int) l;
    }
}
