package com.contec.jar.fhr01;

import com.contec.phms.manager.datas.DataList;

import java.util.ArrayList;
import u.aly.bs;

public class DeviceData {
    public ArrayList<byte[]> mDatas = new ArrayList<>();
    public String m_Device_ID = bs.b;
    public int[] m_end_time = new int[6];
    public int[] m_start_time = new int[6];
    public DataList mDataList;
    public ArrayList<byte[]> m_fetal_heart;
    public int m_iDay;
    public int m_iHour;
    public int m_iMinute;
    public int m_iMonth;
    public int m_iYear;
    public int m_iSec;
    public int[] mDate;
}
