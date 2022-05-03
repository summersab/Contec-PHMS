package com.contec.phms.upload.cases.spir;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;

public class SaveHelper implements Serializable {
    private static final long serialVersionUID = 1;

    public static void saveInt(OutputStream out, int data) {
        try {
            out.write(new byte[]{(byte) (data & 255), (byte) ((data >> 8) & 255), (byte) ((data >> 16) & 255), (byte) ((data >> 24) & 255)});
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveBoolean(OutputStream out, boolean data) {
        if (data) {
            saveInt(out, 1);
        } else {
            saveInt(out, 0);
        }
    }

    public static void saveByteArray(OutputStream out, byte[] data) {
        try {
            out.write(data);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveDouble(OutputStream out, double data) {
        byte[] arr = new byte[8];
        Long l = Long.valueOf(Double.doubleToLongBits(data));
        for (int i = 0; i < 8; i++) {
            arr[i] = (byte) ((int) (l.longValue() >> (i * 8)));
        }
        try {
            out.write(arr);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveByte(OutputStream out, int data) {
        try {
            out.write(data);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveString(OutputStream out, String data, int i) {
        byte[] b = new byte[i];
        if (data != null) {
            byte[] bData = data.getBytes();
            int n = bData.length;
            int m = b.length;
            if (m >= n) {
                for (int j = 0; j < n; j++) {
                    b[j] = bData[j];
                }
            } else {
                for (int j2 = 0; j2 < m; j2++) {
                    b[j2] = bData[j2];
                }
            }
        }
        try {
            out.write(b, 0, i);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveChar(OutputStream out, char pdata) {
        byte[] _pdata = new byte[4];
        _pdata[0] = (byte) ((pdata >> 8) & 255);
        _pdata[1] = (byte) (pdata & 255);
        try {
            out.write(_pdata);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
