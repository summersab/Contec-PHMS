package com.contec.phms.util;

import u.aly.bs;

public class PrintBytes {
    public static void printData(byte[] pack) {
        CLog.i("***********************", "************************");
        String _temp = bs.b;
        for (int i = 0; i < pack.length; i++) {
            if (i >= 3 && (i - 2) % 7 == 1) {
                CLog.i("Data", _temp);
                _temp = bs.b;
            }
            _temp = String.valueOf(_temp) + " " + Integer.toHexString(pack[i]);
        }
        CLog.i("Data", _temp);
        CLog.i("***********************", "************************");
    }

    public static void printData(byte[] pack, int count) {
        CLog.i("***********************", "************************");
        String _temp = bs.b;
        for (int i = 0; i < count; i++) {
            _temp = String.valueOf(_temp) + " " + Integer.toHexString(pack[i]);
        }
        CLog.i("Data", _temp);
        CLog.i("***********************", "************************");
    }
}
