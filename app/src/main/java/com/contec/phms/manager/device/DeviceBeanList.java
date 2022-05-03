package com.contec.phms.manager.device;

import java.util.ArrayList;

public class DeviceBeanList {
    public ArrayList<DeviceBean> mBeanList = new ArrayList<>();
    public String mDeviceName;
    public int mProgress;
    public int mState;
    public boolean misShowDelBtn = false;

    public void clearPath() {
        for (int i = 0; i < this.mBeanList.size(); i++) {
            this.mBeanList.get(i).clearDataPath();
        }
    }
}
