package com.contec.phms.manager.device;

import android.util.Log;
import com.contec.phms.App_phms;
import java.util.ArrayList;
import u.aly.bs;

public class DeviceBean {
    public static final int CONNECT_FA = 3;
    public static final int CONNECT_ING = 1;
    public static final int CONNECT_SU = 2;
    public static final int NO_NEW_DATA = 10;
    public static final int RECEIVE_FA = 5;
    public static final int RECEIVE_ING = 4;
    public static final int RECEIVE_SU = 6;
    public static final int UPLOADED_DATA = 11;
    public static final int UPLOAD_FA = 9;
    public static final int UPLOAD_SU = 8;
    public static final int WATE_CONNECT = 0;
    public static final int WATE_UPLOAD = 7;
    public boolean ifAddNew = false;
    public String mBluetoothType = bs.b;
    public String mBroadcastPacketFiled = bs.b;
    public String mCode;
    public ArrayList<String> mDataPath;
    public String mDeviceName = bs.b;
    public int mFailedReasons = 0;
    public int mId;
    public String mMacAddr;
    public int mProgress;
    public String mPromptStr;
    public String mReceiveDataStr;
    public int mState;
    public int mUseNum;

    public DeviceBean(String pDeviceName, String pMacAddr) {
        this.mDeviceName = pDeviceName;
        this.mMacAddr = pMacAddr;
        this.mDataPath = new ArrayList<>();
    }

    public DeviceBean(String pDeviceName, String pMacAddr, String pCode, int pId, int pUseNum) {
        this.mDeviceName = pDeviceName;
        this.mMacAddr = pMacAddr;
        this.mCode = pCode;
        this.mId = pId;
        this.mUseNum = pUseNum;
        this.mDataPath = new ArrayList<>();
    }

    public DeviceBean(String pDeviceName, String pMacAddr, String pCode, int pId, String pReceiveData) {
        this.mDeviceName = pDeviceName;
        this.mMacAddr = pMacAddr;
        this.mCode = pCode;
        this.mId = pId;
        this.mDataPath = new ArrayList<>();
        this.mReceiveDataStr = pReceiveData;
    }

    public String getMacString() {
        String[] _macs = this.mMacAddr.split(":");
        String _return = bs.b;
        for (int i = 0; i < _macs.length; i++) {
            _return = String.valueOf(_return) + _macs[i];
        }
        return _return;
    }

    public String getDeivceUniqueness() {
        return String.valueOf(getMacString()) + this.mDeviceName + App_phms.getInstance().GetUserInfoNAME();
    }

    public void addDataPath(String mDataPath2) {
        if (mDataPath2 == null || mDataPath2 == null) {
            Log.e("Get the current path: ", mDataPath2);
            Log.e("Init array locally", "::::" + this.mDataPath);
            return;
        }
        this.mDataPath.add(mDataPath2);
    }

    public void deleteDataPath(int pPosition) {
        this.mDataPath.remove(pPosition);
    }

    public void clearDataPath() {
        this.mDataPath.clear();
    }

    public String getmBluetoothType() {
        return this.mBluetoothType;
    }

    public void setmBluetoothType(String mBluetoothType2) {
        this.mBluetoothType = mBluetoothType2;
    }

    public String getmBroadcastPacketFiled() {
        return this.mBroadcastPacketFiled;
    }

    public void setmBroadcastPacketFiled(String mBroadcastPacketFiled2) {
        this.mBroadcastPacketFiled = mBroadcastPacketFiled2;
    }
}
