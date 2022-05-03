package com.contec.cms_ble_50d_jar;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MinData implements Serializable {
    private static final long serialVersionUID = 1;
    private List<byte[]> mMinDataList;
    private byte[] mStartDate;

    protected void setmStartDate(byte[] mStartDate2) {
        this.mStartDate = mStartDate2;
        this.mMinDataList = new ArrayList();
    }

    public byte[] getmStartDate() {
        return this.mStartDate;
    }

    public List<byte[]> getmMinDataList() {
        return this.mMinDataList;
    }
}
