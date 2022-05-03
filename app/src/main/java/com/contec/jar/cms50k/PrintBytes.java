package com.contec.jar.cms50k;

import u.aly.bs;

public class PrintBytes {
    public static void printData(byte[] pack) {
        DebugLog.i("***********************", "************************");
        String _temp = bs.b;
        for (int i = 0; i < pack.length; i++) {
            if (i >= 3 && (i - 2) % 7 == 1) {
                DebugLog.i("Data", _temp);
                _temp = bs.b;
            }
            _temp = String.valueOf(_temp) + " " + Integer.toHexString(pack[i]);
        }
        DebugLog.e("Data", _temp);
    }

    public static void printData(byte[] pack, int count) {
        DebugLog.e("***********************", "************************");
        String _temp = bs.b;
        for (int i = 0; i < count; i++) {
            _temp = String.valueOf(_temp) + " " + Integer.toHexString(pack[i]);
        }
        DebugLog.e("Data", _temp);
        DebugLog.e("***********************", "************************");
    }

    public static void printDatai(byte[] pack, int count) {
        DebugLog.i("***********************", "************************");
        String _temp = bs.b;
        for (int i = 0; i < count; i++) {
            _temp = String.valueOf(_temp) + " " + Integer.toHexString(pack[i]);
        }
        DebugLog.i("Data_Data", _temp);
        DebugLog.i("***********************", "************************");
    }
}
