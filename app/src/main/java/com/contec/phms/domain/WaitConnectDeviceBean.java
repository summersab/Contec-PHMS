package com.contec.phms.domain;

import com.contec.phms.manager.device.DeviceBean;

public class WaitConnectDeviceBean {
    private int mConnectedCount;
    private DeviceBean mDevicebean;
    private boolean mIfFirstAddOrActiveClick = false;

    public WaitConnectDeviceBean(DeviceBean mDevicebean2, int mConnectedCount2, boolean ifFirstAddOrActiveClick) {
        this.mDevicebean = mDevicebean2;
        this.mConnectedCount = mConnectedCount2;
        this.mIfFirstAddOrActiveClick = ifFirstAddOrActiveClick;
    }

    public int getmConnectedCount() {
        return this.mConnectedCount;
    }

    public void setmConnectedCount(int mConnectedCount2) {
        this.mConnectedCount = mConnectedCount2;
    }

    public DeviceBean getmDevicebean() {
        return this.mDevicebean;
    }

    public void setmDevicebean(DeviceBean mDevicebean2) {
        this.mDevicebean = mDevicebean2;
    }

    public boolean ismIfFirstAddOrActiveClick() {
        return this.mIfFirstAddOrActiveClick;
    }

    public void setmIfFirstAddOrActiveClick(boolean mIfFirstAddOrActiveClick2) {
        this.mIfFirstAddOrActiveClick = mIfFirstAddOrActiveClick2;
    }
}
