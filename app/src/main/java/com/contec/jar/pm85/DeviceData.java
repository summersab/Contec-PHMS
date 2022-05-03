package com.contec.jar.pm85;

import java.util.ArrayList;

public class DeviceData extends com.contec.phms.device.template.DeviceData {
    public byte[] _date = new byte[4];
    public byte[] _time = new byte[3];
    public ArrayList<Object> mDatas = new ArrayList<>();
    public ArrayList<Integer> mFlags = new ArrayList<>();
    public ArrayList<Object> mDataList;
    public ArrayList<Integer> mFlag;
    public int[] mDate;
}
