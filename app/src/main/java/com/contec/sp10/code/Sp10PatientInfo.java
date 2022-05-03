package com.contec.sp10.code;

import android.util.Log;
import java.io.OutputStream;
import java.io.Serializable;
import u.aly.bs;
import u.aly.dp;

public class Sp10PatientInfo implements Serializable {
    private static final long serialVersionUID = 1;
    public int mAge;
    public int mCaseFlag;
    public int mCaseID;
    public String mCaseName;
    public int mDay;
    public int mGender;
    public int mHeight;
    public int mHour;
    public boolean mMedication;
    public int mMin;
    public int mMonth;
    public int mNation;
    public int mScaler;
    public int mSecond;
    public boolean mSmoke;
    public int mStandard;
    public String mTime;
    public String mUserName;
    public int mWeight;
    public int mYear;

    public void initInfo(byte[] pack) {
        this.mCaseID = (byte) (pack[8] | (pack[7] & 32));
        this.mUserName = bs.b;
        this.mAge = (byte) (pack[12] | ((pack[9] & 4) << 5));
        this.mHeight = (byte) (pack[13] | ((pack[9] & 8) << 4));
        this.mWeight = (byte) (pack[14] | ((pack[9] & dp.n) << 3));
        this.mScaler = (((((byte) (pack[11] | ((pack[9] & 2) << 6))) & 255) << 8) | (((byte) (pack[10] | ((pack[9] & 1) << 7))) & 255)) & 65535;
        this.mNation = (byte) (pack[7] & 3);
        this.mGender = (byte) (pack[7] & 4);
        this.mStandard = pack[7] & dp.m;
        this.mCaseName = String.valueOf(this.mCaseID) + ".dt";
        Log.e("Basic information===", "CaseID: " + this.mCaseID + " Age: " + this.mAge + " Height: " + this.mHeight + " Weight: " + this.mWeight + "定位系数：" + this.mScaler + " Nationality: " + this.mNation + " Gender: " + this.mGender + " Case Name: " + this.mCaseName);
    }

    public void initOtherInfo(byte[] pack) {
        boolean z = true;
        this.mSmoke = (pack[7] & 8) == 1;
        if ((pack[7] & dp.n) != 1) {
            z = false;
        }
        this.mMedication = z;
        Log.e("Basic Information=====", "smoker: " + this.mSmoke + " medication: " + this.mMedication);
        this.mTime = getDateStr(pack);
        Log.e("Time: ", String.valueOf(this.mTime) + "&**************************");
    }

    public String getDateStr(byte[] pack) {
        this.mYear = pack[1] & 255;
        this.mMonth = pack[2] & 255;
        this.mDay = pack[3] & 255;
        this.mHour = pack[4] & 255;
        this.mMin = pack[5] & 255;
        this.mSecond = pack[6] & 255;
        StringBuffer strBuffer = new StringBuffer();
        if (new StringBuilder(String.valueOf(this.mYear)).toString().length() > 3) {
            strBuffer.append(this.mYear);
        } else if (this.mYear < 10) {
            strBuffer.append("200" + this.mYear);
        } else {
            strBuffer.append("20" + this.mYear);
        }
        strBuffer.append("-");
        if (this.mMonth < 10) {
            strBuffer.append("0");
            strBuffer.append(this.mMonth);
        } else {
            strBuffer.append(this.mMonth);
        }
        strBuffer.append("-");
        if (this.mDay < 10) {
            strBuffer.append("0");
            strBuffer.append(this.mDay);
        } else {
            strBuffer.append(this.mDay);
        }
        strBuffer.append(" ");
        if (this.mHour < 10) {
            strBuffer.append("0");
            strBuffer.append(this.mHour);
        } else {
            strBuffer.append(this.mHour);
        }
        strBuffer.append(":");
        if (this.mMin < 10) {
            strBuffer.append("0");
            strBuffer.append(this.mMin);
        } else {
            strBuffer.append(this.mMin);
        }
        strBuffer.append(":");
        if (this.mSecond < 10) {
            strBuffer.append("0");
            strBuffer.append(this.mSecond);
        } else {
            strBuffer.append(this.mSecond);
        }
        return strBuffer.toString();
    }

    public void save(OutputStream out) {
        SaveHelper.saveInt(out, this.mCaseFlag);
        SaveHelper.saveInt(out, this.mCaseID);
        SaveHelper.saveString(out, this.mUserName, 20);
        SaveHelper.saveInt(out, this.mGender);
        SaveHelper.saveInt(out, this.mNation);
        SaveHelper.saveInt(out, this.mAge);
        SaveHelper.saveInt(out, this.mHeight);
        SaveHelper.saveInt(out, this.mWeight);
        SaveHelper.saveString(out, this.mTime, 20);
        SaveHelper.saveInt(out, this.mScaler);
        SaveHelper.saveString(out, this.mCaseName, 100);
        SaveHelper.saveBoolean(out, this.mMedication);
        SaveHelper.saveBoolean(out, this.mSmoke);
    }
}
