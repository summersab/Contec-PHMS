package com.example.bm77_contec08A_code;

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
        Log.i("Data", _temp);
        Log.i("***********************", "************************");
    }

    public static void printData(byte[] pack, int count) {
        String _temp = bs.b;
        for (int i = 0; i < count; i++) {
            _temp = String.valueOf(_temp) + " " + Integer.toHexString(pack[i] & 255);
        }
        Log.i("Data", _temp);
    }
}
