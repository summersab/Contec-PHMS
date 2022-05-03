package com.contec.sdk;

import android.util.Log;
import u.aly.bs;

public class PrintBytes {
    public static void printData(byte[] pack) {
        Log.i("***********************", "************************");
        String _temp = bs.b;
        for (int i = 0; i < pack.length; i++) {
            if (i >= 3 && (i - 2) % 7 == 1) {
                Log.i("Data", _temp);
                _temp = bs.b;
            }
            _temp = String.valueOf(_temp) + " " + Integer.toHexString(pack[i]);
        }
        Log.e("Data", _temp);
    }

    public static void printData(byte[] pack, int count) {
        Log.e("***********************", "************************");
        String _temp = bs.b;
        for (int i = 0; i < count; i++) {
            _temp = String.valueOf(_temp) + " " + Integer.toHexString(pack[i]);
        }
        Log.e("Data", _temp);
        Log.e("***********************", "************************");
    }

    public static void printDatai(byte[] pack, int count) {
        Log.i("***********************", "************************");
        String _temp = bs.b;
        for (int i = 0; i < count; i++) {
            _temp = String.valueOf(_temp) + " " + Integer.toHexString(pack[i]);
        }
        Log.i("Data", _temp);
        Log.i("***********************", "************************");
    }
}
