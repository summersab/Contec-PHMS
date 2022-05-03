package com.conect.json;

import android.graphics.drawable.Drawable;

public class AppFlowInfo {
    private String mDate;
    private long mDownLoad;
    private Drawable mDraw;
    private int mIntDate;
    private String mLable;
    private long mUpLoad;

    public AppFlowInfo(String pLable, long pUpLoad, long pDownLoad, Drawable pDraw) {
        this.mLable = pLable;
        this.mUpLoad = pUpLoad;
        this.mDownLoad = pDownLoad;
        this.mDraw = pDraw;
    }

    public AppFlowInfo(long pUpLoad, long pDownLoad, String pDate, int pIntDate) {
        this.mUpLoad = pUpLoad;
        this.mDownLoad = pDownLoad;
        this.mDate = pDate;
        this.mIntDate = pIntDate;
    }

    public String getDate() {
        return this.mDate;
    }

    public void setDate(String pDate) {
        this.mDate = pDate;
    }

    public int getIntDate() {
        return this.mIntDate;
    }

    public void setIntDate(int mIntDate2) {
        this.mIntDate = mIntDate2;
    }

    public String getLable() {
        return this.mLable;
    }

    public void setLable(String pLable) {
        this.mLable = pLable;
    }

    public Drawable getDraw() {
        return this.mDraw;
    }

    public void setDraw(Drawable pDraw) {
        this.mDraw = pDraw;
    }

    public long getUpLoad() {
        return this.mUpLoad;
    }

    public void setUpLoad(long pUpLoad) {
        this.mUpLoad = pUpLoad;
    }

    public long getDownLoad() {
        return this.mDownLoad;
    }

    public void setDownLoad(long pDownLoad) {
        this.mDownLoad = pDownLoad;
    }
}
