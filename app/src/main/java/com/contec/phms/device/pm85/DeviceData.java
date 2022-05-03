package com.contec.phms.device.pm85;

import java.util.ArrayList;
import java.util.Date;

public class DeviceData extends com.contec.phms.device.template.DeviceData {
    public ArrayList<Integer> mFlag = new ArrayList<>();

    public void initInfo() {
        this.mUploadType = "case";
        this.mDataType = "pm85";
    }

    public void setSaveDate() {
        this.mSaveDate = new Date(this.mDate[0], this.mDate[1], this.mDate[2], this.mDate[3], this.mDate[4], this.mDate[5]);
    }
}
