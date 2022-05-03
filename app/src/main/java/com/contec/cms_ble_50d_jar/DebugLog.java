package com.contec.cms_ble_50d_jar;

import android.util.Log;

public class DebugLog {
    public static boolean isDebug = false;

    public static void i(String tag, String debug) {
        if (isDebug) {
            Log.i(tag, debug);
        }
    }

    public static void e(String tag, String debug) {
        if (isDebug) {
            Log.e(tag, debug);
        }
    }

    public static void d(String tag, String debug) {
        if (isDebug) {
            Log.d(tag, debug);
        }
    }
}
