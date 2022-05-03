package cn.com.contec.jar.util;

import android.util.Log;
import u.aly.bs;

public class PrintBytes {
    public static void printData(byte[] pack) {
        Log.i("***********************", "************************");
        if (pack == null) {
            Log.i("***********************", "param is null");
            return;
        }
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
        Log.i("***********************", "************************");
        if (pack == null) {
            Log.i("****************", "param pack is null");
            return;
        }
        String _temp = bs.b;
        for (int i = 0; i < count; i++) {
            _temp = String.valueOf(_temp) + " " + Integer.toHexString(pack[i]);
        }
        Log.i("Data", _temp);
        Log.i("***********************", "************************");
    }
}
