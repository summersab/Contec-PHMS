package com.contec.phms.device.sp10w;

public class DeviceData extends com.contec.phms.device.template.DeviceData {
    private static final long serialVersionUID = 1;

    public DeviceData(byte[] pack) {
        super(pack);
    }

    public void initInfo() {
        this.mUploadType = "case";
        this.mDataType = "sp10w";
    }

    public void setSaveDate() {
    }
}
