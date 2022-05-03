package com.contec.phms.util;

import android.content.Context;
import android.widget.Toast;
import u.aly.bs;

public class ToastUtils {
    public static boolean isShow = true;
    public static Toast toast;

    private ToastUtils() {
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    public static void show(Context context, String content) {
        Toast.makeText(context, content, Toast.LENGTH_SHORT).show();
    }

    public static void showShort(Context context, CharSequence message) {
        if (isShow) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        }
    }

    public static void showShort(Context context, int message) {
        if (isShow) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        }
    }

    public static void showLong(Context context, CharSequence message) {
        if (isShow) {
            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
        }
    }

    public static void showLong(Context context, int message) {
        if (isShow) {
            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
        }
    }

    public static void show(Context context, CharSequence message, int duration) {
        if (isShow) {
            Toast.makeText(context, message, duration).show();
        }
    }

    public static void show(Context context, int message, int duration) {
        if (isShow) {
            Toast.makeText(context, message, duration).show();
        }
    }

    public static void showToast(Context mContext, String msg) {
        if (toast == null) {
            toast = Toast.makeText(mContext, bs.b, Toast.LENGTH_SHORT);
        }
        toast.setText(msg);
        toast.show();
    }
}
