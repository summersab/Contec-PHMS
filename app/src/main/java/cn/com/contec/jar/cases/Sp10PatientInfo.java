package cn.com.contec.jar.cases;

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
        int i;
        this.mCaseID = (pack[2] & 255) + 1;
        this.mUserName = bs.b;
        this.mAge = pack[3] & 255;
        this.mHeight = pack[4] & 255;
        this.mWeight = pack[5] & 255;
        this.mScaler = ((pack[7] & 255) << 8) | (pack[6] & 255);
        this.mNation = pack[8] & 63;
        if (((pack[8] & 255) >> 4) == 0) {
            i = 1;
        } else {
            i = 0;
        }
        this.mGender = i;
        this.mStandard = 0;
        this.mCaseName = String.valueOf(this.mCaseID) + ".dt";
    }

    public void initOtherInfo(byte[] pack) {
        boolean z;
        boolean z2 = true;
        if ((pack[2] & dp.m) == 1) {
            z = true;
        } else {
            z = false;
        }
        this.mSmoke = z;
        if (((pack[2] & 255) >> 4) != 1) {
            z2 = false;
        }
        this.mMedication = z2;
        this.mTime = getDateStr(pack);
        Log.e("测量日期是：", String.valueOf(this.mTime) + "&**************************");
    }

    public String getDateStr(byte[] pack) {
        this.mYear = (pack[3] & 255) | ((pack[4] & 255) << 8);
        if (this.mYear > 2572) {
            this.mYear = pack[3];
            this.mSecond = pack[4] - 10;
        } else {
            this.mSecond = 0;
        }
        this.mMonth = (byte) (pack[5] & 255);
        this.mDay = pack[6] & 255;
        this.mHour = (byte) (pack[7] & 255);
        this.mMin = (byte) (pack[8] & 255);
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
