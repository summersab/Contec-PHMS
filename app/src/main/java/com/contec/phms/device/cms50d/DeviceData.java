package com.contec.phms.device.cms50d;

public class DeviceData extends com.contec.phms.device.template.DeviceData {
    private static final long serialVersionUID = 1;

    public DeviceData(byte[] pack) {
        super(pack);
    }

    public DeviceData() {
    }

    public void initInfo() {
        this.mUploadType = "trend";
        this.mDataType = "sp0208";
    }

    public void setSaveDate() {
    }
}
