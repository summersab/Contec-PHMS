package com.contec.phms.device.cms50k;

import com.contec.phms.device.template.DeviceData;

public class DeviceDataPedometer extends DeviceData {
    private static final long serialVersionUID = 1;

    public void initInfo() {
        this.mUploadType = "trend";
        this.mDataType = "pedometerDayK";
    }

    public void setSaveDate() {
    }
}
