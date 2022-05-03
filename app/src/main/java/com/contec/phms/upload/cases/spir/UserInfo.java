package com.contec.phms.upload.cases.spir;

import android.util.Log;
import java.io.OutputStream;
import java.io.Serializable;

public class UserInfo implements Serializable {
    private static final long serialVersionUID = 1;
    private int mAge;
    private int mCaseId;
    private String mCaseName;
    private int mDrug;
    private int mEffective;
    private int mGender;
    private int mHeight;
    private int mNation;
    private int mScaler;
    private int mSmoke;
    private int mStandard;
    private String mTime;
    private String mUserName;
    private int mWeight;

    public int getmStandard() {
        return this.mStandard;
    }

    public void setmStandard(int mStandard2) {
        this.mStandard = mStandard2;
    }

    public String getmUserName() {
        return this.mUserName;
    }

    public String getmTime() {
        return this.mTime;
    }

    public String getmCaseName() {
        return this.mCaseName;
    }

    public int getmEffective() {
        return this.mEffective;
    }

    public void setmUserName(String mUserName2) {
        this.mUserName = mUserName2;
    }

    public void setmTime(String mTime2) {
        this.mTime = mTime2;
    }

    public void setmCaseName(String mCaseName2) {
        this.mCaseName = mCaseName2;
    }

    public void setmEffective(int mEffective2) {
        this.mEffective = mEffective2;
    }

    public int getmCaseId() {
        return this.mCaseId;
    }

    public void setmCaseId(int mCaseId2) {
        this.mCaseId = mCaseId2;
    }

    public int getmGender() {
        return this.mGender;
    }

    public void setmGender(int mGender2) {
        this.mGender = mGender2;
    }

    public int getmNation() {
        return this.mNation;
    }

    public void setmNation(int mNation2) {
        this.mNation = mNation2;
    }

    public int getmAge() {
        return this.mAge;
    }

    public void setmAge(int mAge2) {
        this.mAge = mAge2;
    }

    public int getmHeight() {
        return this.mHeight;
    }

    public void setmHeight(int mHeight2) {
        this.mHeight = mHeight2;
    }

    public int getmWeight() {
        return this.mWeight;
    }

    public void setmWeight(int mWeight2) {
        this.mWeight = mWeight2;
    }

    public int getmScaler() {
        return this.mScaler;
    }

    public void setmScaler(int mScaler2) {
        this.mScaler = mScaler2;
    }

    public int getmDrug() {
        return this.mDrug;
    }

    public void setmDrug(int mDrug2) {
        this.mDrug = mDrug2;
    }

    public int getmSmoke() {
        return this.mSmoke;
    }

    public void setmSmoke(int mSmoke2) {
        this.mSmoke = mSmoke2;
    }

    public void write(OutputStream out, UserInfo info) {
        SaveHelper.saveInt(out, info.mEffective);
        SaveHelper.saveInt(out, info.mCaseId);
        SaveHelper.saveString(out, this.mUserName, 20);
        SaveHelper.saveInt(out, info.mGender);
        SaveHelper.saveInt(out, info.mNation);
        SaveHelper.saveInt(out, this.mAge);
        SaveHelper.saveInt(out, info.mHeight);
        SaveHelper.saveInt(out, info.mWeight);
        SaveHelper.saveString(out, info.mTime, 20);
        SaveHelper.saveInt(out, info.mScaler);
        SaveHelper.saveString(out, info.mCaseName, 100);
        SaveHelper.saveInt(out, info.mDrug);
        SaveHelper.saveInt(out, info.mSmoke);
        Log.i("�Ƿ�������ҩ", "����" + this.mSmoke + "��ҩ" + this.mDrug);
    }
}
