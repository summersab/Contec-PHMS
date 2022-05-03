package com.contec.phms.manager.device;

import android.bluetooth.BluetoothSocket;
import com.contec.phms.device.template.BluetoothLeDeviceService;
import com.contec.phms.device.template.DeviceService;
import com.contec.phms.util.Constants;
import com.contec.phms.util.DeviceNameUtils;
import com.contec.phms.util.JiXinTestUtils;
import java.util.Locale;

public class ServiceBean {
    public static ServiceBean mServiceBean;
    BluetoothLeDeviceService mBLEService;
    String mDeviceName;
    public String mDevicePackage;
    public String mDirPath;
    public String mFailedPath;
    String mMacAddr;
    public int mProgress;
    DeviceService mService;
    public BluetoothSocket mSocket;
    public String mTempPath;
    public String mUploadedPath;

    private ServiceBean() {
        initPath();
    }

    public void init(DeviceBean mBean) {
        this.mDeviceName = mBean.mDeviceName;
        this.mMacAddr = mBean.mMacAddr;
        if (this.mDeviceName.equalsIgnoreCase("CMS50IW")) {
            this.mDevicePackage = "com.contec.phms.device." + Constants.CMS50EW.toLowerCase();
            this.mProgress = 31;
            matchService(Constants.CMS50EW);
            initPath();
        } else if (this.mDeviceName.equalsIgnoreCase("CONTEC08C")) {
            this.mDevicePackage = "com.contec.phms.device." + "contec08aw".toLowerCase();
            this.mProgress = 31;
            matchService("contec08aw");
            initPath();
        } else if (this.mDeviceName.equalsIgnoreCase(DeviceNameUtils.TEMP03.toUpperCase(Locale.ENGLISH))) {
            this.mDevicePackage = "com.contec.phms.device." + "temp01".toLowerCase();
            this.mProgress = 31;
            matchService("temp01");
            initPath();
        } else {
            if (this.mDeviceName.equalsIgnoreCase(Constants.CMS50K1_NAME)) {
                this.mDeviceName = Constants.CMS50K_NAME;
            }
            this.mDevicePackage = "com.contec.phms.device." + this.mDeviceName.toLowerCase();
            this.mProgress = 31;
            matchService(this.mDeviceName);
            initPath();
        }
    }

    void initPath() {
        this.mDirPath = Constants.ContecPath;
        this.mTempPath = String.valueOf(this.mDirPath) + "/temp";
        this.mUploadedPath = String.valueOf(this.mDirPath) + "/uploaded";
        this.mFailedPath = String.valueOf(this.mDirPath) + "/failed";
    }

    public static ServiceBean getInstance() {
        if (mServiceBean == null) {
            mServiceBean = new ServiceBean();
        }
        return mServiceBean;
    }

    public DeviceService getmService() {
        return this.mService;
    }

    public int getProgress() {
        return this.mProgress;
    }

    public void setProgress(int mProgress2) {
        this.mProgress = mProgress2;
    }

    public String getmDeviceName() {
        return this.mDeviceName;
    }

    public void setmDeviceName(String mDeviceName2) {
        this.mDeviceName = mDeviceName2;
    }

    public String getmMacAddr() {
        return this.mMacAddr;
    }

    public void setmMacAddr(String mMacAddr2) {
        this.mMacAddr = mMacAddr2;
    }

    public void setmService(DeviceService mService2) {
        this.mService = mService2;
    }

    void matchService(String mDeviceName2) {
        try {
            this.mService = (DeviceService) Class.forName(String.valueOf(this.mDevicePackage) + ".DeviceService").newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void matchBLEService(String mDeviceName2) {
        if (mDeviceName2.equalsIgnoreCase("cms50k1")) {
            mDeviceName2 = "cms50k";
        }
        if (mDeviceName2.equalsIgnoreCase("CONTEC08C")) {
            mDeviceName2 = "contec08aw";
        }
        this.mDevicePackage = "com.contec.phms.device." + mDeviceName2.toLowerCase();
        JiXinTestUtils.LogE("mDevicePackage:" + this.mDevicePackage);
        try {
            this.mBLEService = (BluetoothLeDeviceService) Class.forName(String.valueOf(this.mDevicePackage) + ".BluetoothLeDeviceService").newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public BluetoothLeDeviceService getmBLEService() {
        return this.mBLEService;
    }
}
