package com.contec.phms.device.pm10;

public class DeviceData extends com.contec.phms.device.template.DeviceData {
    public byte[] CaseData;
    public byte[] TrendData;
    public String mCaseID;

    public void initInfo() {
        this.mUploadType = "case";
        this.mDataType = "pm10";
    }

    public void setSaveDate() {
    }
}
