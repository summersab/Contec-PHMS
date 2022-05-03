package com.contec.phms.device.cms50k;

import com.contec.phms.device.template.DeviceData;
import com.contec.phms.db.localdata.Spo2DataDao;

public class DeviceDataSpo2Point extends DeviceData {
    private static final long serialVersionUID = 1;

    public void initInfo() {
        this.mUploadType = "trend";
        this.mDataType = Spo2DataDao.SPO2;
    }

    public void setSaveDate() {
    }
}
