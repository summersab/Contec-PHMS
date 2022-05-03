package com.contec.phms.SearchDevice;

import u.aly.bs;

public class SortDeviceContainer {
    private String mDeviceNameCode = bs.b;
    private boolean mhadsort = false;

    public String getmDeviceNameCode() {
        return this.mDeviceNameCode;
    }

    public void setmDeviceNameCode(String mDeviceNameCode2) {
        this.mDeviceNameCode = mDeviceNameCode2;
    }

    public boolean isMhadsort() {
        return this.mhadsort;
    }

    public void setMhadsort(boolean mhadsort2) {
        this.mhadsort = mhadsort2;
    }

    public SortDeviceContainer(String mDeviceNameCode2) {
        setmDeviceNameCode(mDeviceNameCode2);
    }
}
