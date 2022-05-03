package com.contec.phms.device.cms50d;

import com.contec.phms.device.template.DeviceData;

public class DeviceDataPedometerMin extends DeviceData {
    private static final long serialVersionUID = 1;

    public DeviceDataPedometerMin(byte[] pack) {
        super(pack);
    }

    public DeviceDataPedometerMin() {
    }

    public void initInfo() {
        this.mUploadType = "trend";
        this.mDataType = "pedometerMin";
    }

    public void setSaveDate() {
    }
}
