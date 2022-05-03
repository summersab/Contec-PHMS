package cn.com.contec.net.util;

public class Base64_encoding {
    static final char[] intToBase64 = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '*', '/'};

    public static String encoding(String pMessage) {
        if (pMessage == null) {
            return null;
        }
        byte[] _MessageByte = pMessage.getBytes();
        int aLen = _MessageByte.length;
        int numFullGroups = aLen / 3;
        int numBytesInPartialGroup = aLen - (numFullGroups * 3);
        StringBuffer result = new StringBuffer(((aLen + 2) / 3) * 4);
        int i = 0;
        int inCursor = 0;
        while (i < numFullGroups) {
            int inCursor2 = inCursor + 1;
            int byte0 = _MessageByte[inCursor] & 255;
            int inCursor3 = inCursor2 + 1;
            int byte1 = _MessageByte[inCursor2] & 255;
            int byte2 = _MessageByte[inCursor3] & 255;
            result.append(intToBase64[byte0 >> 2]);
            result.append(intToBase64[((byte0 << 4) & 63) | (byte1 >> 4)]);
            result.append(intToBase64[((byte1 << 2) & 63) | (byte2 >> 6)]);
            result.append(intToBase64[byte2 & 63]);
            i++;
            inCursor = inCursor3 + 1;
        }
        if (numBytesInPartialGroup != 0) {
            int inCursor4 = inCursor + 1;
            int byte02 = _MessageByte[inCursor] & 255;
            result.append(intToBase64[byte02 >> 2]);
            if (numBytesInPartialGroup == 1) {
                result.append(intToBase64[(byte02 << 4) & 63]);
                result.append("==");
                return result.toString();
            }
            inCursor = inCursor4 + 1;
            int byte12 = _MessageByte[inCursor4] & 255;
            result.append(intToBase64[((byte02 << 4) & 63) | (byte12 >> 4)]);
            result.append(intToBase64[(byte12 << 2) & 63]);
            result.append('=');
        }
        int i2 = inCursor;
        return result.toString();
    }
}
