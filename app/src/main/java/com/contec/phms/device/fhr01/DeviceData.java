package com.contec.phms.device.fhr01;

import java.util.ArrayList;
import java.util.Date;

public class DeviceData extends com.contec.phms.device.template.DeviceData {
    private static final long serialVersionUID = 1;
    public ArrayList<byte[]> m_fetal_heart;
    public int m_iDay;
    public int m_iHour;
    public int m_iMinute;
    public int m_iMonth;
    public int m_iSec;
    public int m_iYear;

    public void initInfo() {
        this.mUploadType = "case";
        this.mDataType = "FHR01";
    }

    public void setSaveDate() {
        this.mSaveDate = new Date(this.m_iYear, this.m_iMonth, this.m_iDay, this.m_iHour, this.m_iMinute, this.m_iSec);
    }
}
