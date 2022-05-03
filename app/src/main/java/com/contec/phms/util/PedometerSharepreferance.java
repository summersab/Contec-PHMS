package com.contec.phms.util;

import android.content.Context;
import android.content.SharedPreferences;
import cn.com.contec.net.util.Constants_jar;
import u.aly.bs;

public class PedometerSharepreferance {
    private static final String TAG = "com.contec.sharepreferance.Sharepreferance";
    private CallBack mCallBack;
    private int mDistance = 0;
    private SharedPreferences.Editor mEditor;
    private SharedPreferences mPreferences;
    private float mSensitivity = 9.0f;
    private int mStep_length = 60;
    private int mSteps = 0;
    private int mTarget = Constants_jar.PROCESS_NEED_DOWN_CASE_XML_END;
    private int mUnit = 1;
    private int mWeight = 60;

    public interface CallBack {
        void setSensitivity(float f);

        void setStepLength(int i);

        void setTarget(int i);

        void setUnit(int i);

        void setWeight(int i);
    }

    public void addCallBack(CallBack pShareCallBack) {
        this.mCallBack = pShareCallBack;
    }

    public PedometerSharepreferance(Context pContext) {
        this.mPreferences = pContext.getSharedPreferences(Constants.SHARE_NAME, 0);
    }

    public int getSteps() {
        this.mSteps = this.mPreferences.getInt(Constants.SHARE_KEY_STEPS, this.mSteps);
        return this.mSteps;
    }

    public void setSteps(int mSteps2) {
        if (this.mEditor == null) {
            open();
        }
        this.mEditor.putInt(Constants.SHARE_KEY_STEPS, mSteps2);
        this.mSteps = mSteps2;
    }

    public boolean getExit() {
        return this.mPreferences.getBoolean("exit_app", false);
    }

    public void setExit(boolean exit) {
        if (this.mEditor == null) {
            open();
        }
        this.mEditor.putBoolean("exit_app", exit);
        this.mEditor.commit();
    }

    public boolean getBackPedometer() {
        return this.mPreferences.getBoolean("back_pedometer", false);
    }

    public void setBackPedometer(boolean back_pedometer) {
        if (this.mEditor == null) {
            open();
        }
        this.mEditor.putBoolean("back_pedometer", back_pedometer);
        this.mEditor.commit();
    }

    public int getStep_length() {
        this.mStep_length = this.mPreferences.getInt(Constants.SHARE_KEY_STEP_LENGTH, this.mStep_length);
        return this.mStep_length;
    }

    public void setUserID(String pUserID) {
        if (this.mEditor == null) {
            open();
        }
        this.mEditor.putString(Constants.SHARE_KEY_USERID, pUserID);
        this.mEditor.commit();
    }

    public void setUserName(String pUserName) {
        if (this.mEditor == null) {
            open();
        }
        this.mEditor.putString(Constants.SHARE_KEY_USERNAME, pUserName);
        this.mEditor.commit();
    }

    public String getUserID() {
        return this.mPreferences.getString(Constants.SHARE_KEY_USERID, bs.b);
    }

    public String getUserName() {
        return this.mPreferences.getString(Constants.SHARE_KEY_USERNAME, bs.b);
    }

    public void setStep_length(int mStep_length2) {
        if (this.mEditor == null) {
            open();
        }
        this.mEditor.putInt(Constants.SHARE_KEY_STEP_LENGTH, mStep_length2);
        if (this.mCallBack != null) {
            this.mCallBack.setStepLength(mStep_length2);
        }
        this.mStep_length = mStep_length2;
    }

    public int getDistance() {
        this.mDistance = this.mPreferences.getInt(Constants.SHARE_KEY_DISTANCE, this.mDistance);
        return this.mDistance;
    }

    public void setDistance(int mDistance2) {
        if (this.mEditor == null) {
            open();
        }
        this.mEditor.putInt(Constants.SHARE_KEY_DISTANCE, mDistance2);
        this.mDistance = mDistance2;
    }

    public int getWeight() {
        this.mWeight = this.mPreferences.getInt("weight", this.mWeight);
        return this.mWeight;
    }

    public void setWeight(int mWeight2) {
        if (this.mEditor == null) {
            open();
        }
        this.mEditor.putInt("weight", mWeight2);
        if (this.mCallBack != null) {
            this.mCallBack.setWeight(mWeight2);
        }
        this.mWeight = mWeight2;
    }

    public float getSensitivity() {
        this.mSensitivity = this.mPreferences.getFloat("sensitivity", this.mSensitivity);
        return this.mSensitivity;
    }

    public void setSensitivity(float mSensitivity2) {
        if (this.mEditor == null) {
            open();
        }
        this.mEditor.putFloat("sensitivity", mSensitivity2);
        if (this.mCallBack != null) {
            this.mCallBack.setSensitivity(mSensitivity2);
        }
        this.mSensitivity = mSensitivity2;
    }

    public int getUnit() {
        this.mUnit = this.mPreferences.getInt(Constants.SHARE_KEY_UNIT, this.mUnit);
        return this.mUnit;
    }

    public void setUnit(int mUnit2) {
        if (this.mEditor == null) {
            open();
        }
        this.mEditor.putInt(Constants.SHARE_KEY_UNIT, mUnit2);
        if (this.mCallBack != null) {
            this.mCallBack.setUnit(mUnit2);
        }
        this.mUnit = mUnit2;
    }

    public int getTarget() {
        this.mTarget = this.mPreferences.getInt(Constants.SHARE_KEY_TARGET, this.mTarget);
        return this.mTarget;
    }

    public void setTarget(int mTarget2) {
        if (this.mEditor == null) {
            open();
        }
        this.mEditor.putInt(Constants.SHARE_KEY_TARGET, mTarget2);
        if (this.mCallBack != null) {
            this.mCallBack.setTarget(mTarget2);
        }
        this.mTarget = mTarget2;
    }

    public void open() {
        this.mEditor = this.mPreferences.edit();
    }

    public void close() {
        this.mEditor.commit();
    }
}
