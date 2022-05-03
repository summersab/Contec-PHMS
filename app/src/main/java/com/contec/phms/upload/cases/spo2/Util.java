package com.contec.phms.upload.cases.spo2;

import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;

public class Util {
    public static boolean writeLong(OutputStream out, long l) {
        try {
            out.write(new byte[]{(byte) ((int) (255 & l)), (byte) ((int) ((65280 & l) >> 8)), (byte) ((int) ((16711680 & l) >> 16)), (byte) ((int) ((-16777216 & l) >> 24))}, 0, 4);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } catch (Exception e2) {
            e2.printStackTrace();
            return false;
        }
    }

    public static boolean writeInt(OutputStream out, int l) {
        try {
            out.write(new byte[]{(byte) ((l >> 0) & 255), (byte) ((l >> 8) & 255), (byte) ((l >> 16) & 255), (byte) ((l >> 24) & 255)});
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } catch (Exception e2) {
            e2.printStackTrace();
            return false;
        }
    }

    public static boolean writeString(OutputStream out, String str, int len) {
        if (str != null) {
            str.getBytes();
            int i = 0;
            while (i < str.length() && i < len) {
                System.out.println(str.charAt(i));
                i++;
            }
        }
        byte[] temp = new byte[(len * 2)];
        byte[] t = null;
        if (str != null) {
            try {
                t = str.getBytes("UTF-16");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        if (t != null) {
            int i2 = 0;
            while (i2 < t.length && i2 < len * 2) {
                temp[i2] = t[i2];
                i2++;
            }
        } else {
            for (int i3 = 0; i3 < len * 2; i3++) {
                temp[i3] = 0;
            }
        }
        temp[(len * 2) - 1] = 0;
        try {
            out.write(temp, 0, len * 2);
            return true;
        } catch (IOException e2) {
            e2.printStackTrace();
            return false;
        } catch (Exception e3) {
            e3.printStackTrace();
            return false;
        }
    }

    public static boolean writeShort(OutputStream out, int s) {
        try {
            out.write(new byte[]{(byte) (s & 255), (byte) ((65280 & s) >> 8)}, 0, 2);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } catch (Exception e2) {
            e2.printStackTrace();
            return false;
        }
    }

    public static boolean writeChar(OutputStream out, int c) {
        try {
            out.write(new byte[]{(byte) c});
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean writeLong(RandomAccessFile file, long l) {
        try {
            file.write(new byte[]{(byte) ((int) (255 & l)), (byte) ((int) ((65280 & l) >> 8)), (byte) ((int) ((16711680 & l) >> 16)), (byte) ((int) ((-16777216 & l) >> 24))}, 0, 4);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } catch (Exception e2) {
            e2.printStackTrace();
            return false;
        }
    }

    public static boolean writeString(RandomAccessFile file, String str, int len) {
        byte[] temp = new byte[(len * 2)];
        byte[] t = null;
        if (str != null) {
            t = str.getBytes();
        }
        if (t != null) {
            int i = 0;
            while (i < t.length && i < len * 2) {
                temp[i] = t[i];
                i++;
            }
        } else {
            for (int i2 = 0; i2 < len * 2; i2++) {
                temp[i2] = 0;
            }
        }
        temp[(len * 2) - 1] = 0;
        try {
            file.write(temp, 0, len * 2);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } catch (Exception e2) {
            e2.printStackTrace();
            return false;
        }
    }

    public static boolean writeShort(RandomAccessFile file, int s) {
        try {
            file.write(new byte[]{(byte) (s & 255), (byte) ((65280 & s) >> 8)}, 0, 2);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } catch (Exception e2) {
            e2.printStackTrace();
            return false;
        }
    }

    public static boolean writeChar(RandomAccessFile file, int c) {
        try {
            file.write(new byte[]{(byte) c});
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
