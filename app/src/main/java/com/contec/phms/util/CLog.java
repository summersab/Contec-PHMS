package com.contec.phms.util;

import android.util.Log;

public class CLog {
    private static final String APPLOG = "PHMSLOG";
    private static int count = 0;
    private static final boolean isWriteToFile = false;

    public static void i(String tag, String msg) {
        if (Constants.mTestFlag) {
            Log.i(APPLOG, String.valueOf(tag) + "    " + msg);
        }
        writeToFile(tag, msg);
    }

    public static void e(String tag, String msg) {
        if (Constants.mTestFlag) {
            Log.e(APPLOG, String.valueOf(tag) + "  " + msg);
        }
        writeToFile(tag, msg);
    }

    public static void d(String tag, String msg) {
        if (Constants.mTestFlag) {
            Log.d(APPLOG, String.valueOf(tag) + "    " + msg);
        }
        writeToFile(tag, msg);
    }

    public static void iT(String tag, String msg) {
        if (Constants.mTestFlag) {
            Log.i(tag, msg);
        }
        writeToFile(tag, msg);
    }

    public static void eT(String tag, String msg) {
        if (Constants.mTestFlag) {
            Log.e(tag, msg);
            writeToFile(tag, msg);
        }
    }

    public static void dT(String tag, String msg) {
        if (Constants.mTestFlag) {
            Log.d(tag, msg);
            writeToFile(tag, msg);
        }
    }

    public static void v(String tag, String msg) {
        if (Constants.mTestFlag) {
            Log.d(tag, msg);
            writeToFile(tag, msg);
        }
    }

    private static void writeToFile(String tag, String msg) {
    }
}
