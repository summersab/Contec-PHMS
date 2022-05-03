package com.conect.json;

import android.util.Log;

public class CLog {
    public static void i(String tag, String msg) {
        if (SqliteConst.Log) {
            Log.i(tag, msg);
        }
    }

    public static void e(String tag, String msg) {
        if (SqliteConst.Log) {
            Log.e(tag, msg);
        }
    }
}
