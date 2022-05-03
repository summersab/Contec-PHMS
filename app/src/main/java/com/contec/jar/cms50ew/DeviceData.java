package com.contec.jar.cms50ew;

import com.contec.phms.manager.datas.DataList;

import java.util.ArrayList;

public class DeviceData extends com.contec.phms.device.template.DeviceData {
    public String mUserName;
    public ArrayList<byte[]> valueList = new ArrayList<>();
    public DataList mDataList;
    public int[] mDate;
    public long mAge;
    public long mHeight;
    public long mWeight;
    public long mSex;
    public String mAddress;
    public String mPhone;
    public int mPI;
}
