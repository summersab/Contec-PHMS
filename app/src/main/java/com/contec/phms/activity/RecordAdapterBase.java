package com.contec.phms.activity;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.BaseAdapter;
import android.widget.Toast;
import com.contec.phms.R;
import com.contec.phms.util.CLog;
import java.util.ArrayList;

public abstract class RecordAdapterBase extends BaseAdapter {
    public Context mContext;
    public int mCount = 100;
    public int mIndex;
    private LayoutInflater mLayoutInflater;
    public int mPageCount;
    private String mToastContent;

    public RecordAdapterBase(Context pContext, int pCount) {
        this.mContext = pContext;
        this.mLayoutInflater = LayoutInflater.from(this.mContext);
    }

    public void listIsNull(ArrayList<?> pList) {
        if (pList == null || pList.size() == 0) {
            Toast.makeText(this.mContext, R.string.no_items, Toast.LENGTH_SHORT).show();
            CLog.e("RecordAdapterBase", "数据库没有流量记录");
            return;
        }
        CLog.e("RecordAdapterBase", "pList.size():" + pList.size());
    }

    public String getmToastContent() {
        return this.mToastContent;
    }

    public void setmToastContent(String mToastContent2) {
        this.mToastContent = mToastContent2;
    }

    public void toastContent() {
        Toast.makeText(this.mContext, getmToastContent(), Toast.LENGTH_LONG).show();
    }

    public int getmIndex() {
        return this.mIndex;
    }

    public void setmIndex(int mIndex2) {
        this.mIndex = mIndex2;
    }

    public int getmCount() {
        return this.mCount;
    }

    public void setmCount(int mCount2) {
        this.mCount = mCount2;
    }

    public Context getmContext() {
        return this.mContext;
    }

    public void setmContext(Context pContext) {
        this.mContext = pContext;
    }

    public LayoutInflater getmLayoutInflater() {
        return this.mLayoutInflater;
    }

    public void setmLayoutInflater(LayoutInflater pLayoutInflater) {
        this.mLayoutInflater = pLayoutInflater;
    }

    public long getItemId(int arg0) {
        return (long) arg0;
    }

    public boolean isEnabled(int position) {
        return false;
    }
}
