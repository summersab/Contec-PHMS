package cn.com.contec.net.util;

import java.security.MessageDigest;
import u.aly.dp;

public class MD5_encoding {
    public static final String MD5(String s) {
        char[] hexDigits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        try {
            byte[] strTemp = s.getBytes();
            MessageDigest mdTemp = MessageDigest.getInstance("MD5");
            mdTemp.update(strTemp);
            int j = 1;
            char[] str = new char[(j * 2)];
            int k = 0;
            for (byte byte0 : mdTemp.digest()) {
                int k2 = k + 1;
                str[k] = hexDigits[(byte0 >>> 4) & 15];
                k = k2 + 1;
                str[k2] = hexDigits[byte0 & dp.m];
            }
            return new String(str);
        } catch (Exception e) {
            return null;
        }
    }

    public static String MD5(byte[] strTemp) {
        char[] hexDigits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        try {
            MessageDigest mdTemp = MessageDigest.getInstance("MD5");
            mdTemp.update(strTemp);
            int j = 1;
            char[] str = new char[(j * 2)];
            int k = 0;
            for (byte byte0 : mdTemp.digest()) {
                int k2 = k + 1;
                str[k] = hexDigits[(byte0 >>> 4) & 15];
                k = k2 + 1;
                str[k2] = hexDigits[byte0 & dp.m];
            }
            return new String(str);
        } catch (Exception e) {
            return null;
        }
    }
}
