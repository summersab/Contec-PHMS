package com.contec.phms.device.cms50d;

import com.contec.phms.device.template.DeviceData;

public class DeviceDataPedometerDay extends DeviceData {
    private static final long serialVersionUID = 1;

    public DeviceDataPedometerDay(byte[] pack) {
        super(pack);
    }

    public DeviceDataPedometerDay() {
    }

    public void initInfo() {
        this.mUploadType = "trend";
        this.mDataType = "pedometerDay";
    }

    public void setSaveDate() {
    }
}
