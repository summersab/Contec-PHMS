package com.contec.phms.device.contec8000gw;

public class DeviceData extends com.contec.phms.device.template.DeviceData {
    private static final long serialVersionUID = 1;
    public String mFilePath;

    public DeviceData(byte[] pack) {
        super(pack);
    }

    public DeviceData() {
    }

    public void initInfo() {
        this.mUploadType = "case";
        this.mDataType = "contec8000gw";
    }

    public void setSaveDate() {
    }
}
