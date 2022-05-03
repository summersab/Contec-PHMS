package com.contec.phms.device.temp01;

import java.util.Date;

public class DeviceData extends com.contec.phms.device.template.DeviceData {
    private static final long serialVersionUID = 1;
    public double m_data;
    public String m_receiveDate;

    public void init() {
        this.mDate = new int[6];
        String[] _date = this.m_receiveDate.replace(" ", "-").replace(":", "-").split("-");
        for (int j = 0; j < this.mDate.length; j++) {
            this.mDate[j] = Integer.parseInt(_date[j].toString());
        }
        setSaveDate();
    }

    public void initInfo() {
        this.mUploadType = "trend";
        this.mDataType = "temperature";
    }

    public void setSaveDate() {
        this.mSaveDate = new Date(this.mDate[0], this.mDate[1], this.mDate[2], this.mDate[3], this.mDate[4], this.mDate[5]);
    }
}
