package com.contec.phms.util;

import android.content.Context;
import android.content.SharedPreferences;
import u.aly.bs;

public class PhmsSharedPreferences {
    private static SharedPreferences.Editor mEditor;
    private static PhmsSharedPreferences mPhmsSharedPreferences;
    private static SharedPreferences mshaPreferences;

    public static PhmsSharedPreferences getInstance(Context context) {
        if (mPhmsSharedPreferences == null) {
            mPhmsSharedPreferences = new PhmsSharedPreferences();
        }
        mshaPreferences = context.getSharedPreferences("PhmsShared", 0);
        mEditor = mshaPreferences.edit();
        return mPhmsSharedPreferences;
    }

    public void saveColume(String key, String pvalue) {
        mEditor.putString(key, pvalue);
        mEditor.commit();
    }

    public void saveLong(String key, long pvalue) {
        mEditor.putLong(key, pvalue);
        mEditor.commit();
    }

    public void saveInt(String key, int pvalue) {
        mEditor.putInt(key, pvalue);
        mEditor.commit();
    }

    public void saveFloat(String key, float pvalue) {
        mEditor.putFloat(key, pvalue);
        mEditor.commit();
    }

    public long getLong(String key) {
        return mshaPreferences.getLong(key, 0);
    }

    public float getFloat(String key) {
        return mshaPreferences.getFloat(key, 0.0f);
    }

    public String getString(String key) {
        return mshaPreferences.getString(key, bs.b);
    }

    public String getString(String key, String value) {
        return mshaPreferences.getString(key, value);
    }

    public int getInt(String key) {
        return mshaPreferences.getInt(key, 1);
    }

    public int getInt(String key, int value) {
        return mshaPreferences.getInt(key, value);
    }

    public void saveBoolean(String key, boolean pvalue) {
        mEditor.putBoolean(key, pvalue);
        mEditor.commit();
    }

    public boolean getBoolean(String key) {
        return mshaPreferences.getBoolean(key, false);
    }

    public boolean getBoolean(String key, Boolean value) {
        return mshaPreferences.getBoolean(key, value.booleanValue());
    }

    public void clearSh() {
        mEditor.clear();
        mEditor.commit();
    }
}
