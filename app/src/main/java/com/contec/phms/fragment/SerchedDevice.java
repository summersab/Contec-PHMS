package com.contec.phms.fragment;

import android.bluetooth.BluetoothDevice;

public class SerchedDevice {
    private int device_image;
    private String device_name;
    private String mBluetoothType;
    private String mCode;
    private BluetoothDevice mDevice;
    private String mDeviceName;
    private String mMac;
    private byte[] scanRecord;

    public SerchedDevice(int device_image2, String device_name2, String pCode, String pMac, String pNameDB, BluetoothDevice pDevice, String pBluetoothType, byte[] pScanRecord) {
        this.mDeviceName = pNameDB;
        this.device_image = device_image2;
        this.device_name = device_name2;
        this.mCode = pCode;
        this.mMac = pMac;
        this.mDevice = pDevice;
        this.mBluetoothType = pBluetoothType;
        this.scanRecord = pScanRecord;
    }

    public BluetoothDevice getmDevice() {
        return this.mDevice;
    }

    public void setmDevice(BluetoothDevice mDevice2) {
        this.mDevice = mDevice2;
    }

    public String getmDeviceName() {
        return this.mDeviceName;
    }

    public void setmDeviceName(String mDeviceName2) {
        this.mDeviceName = mDeviceName2;
    }

    public String getmMac() {
        return this.mMac;
    }

    public void setmMac(String mMac2) {
        this.mMac = mMac2;
    }

    public String getmCode() {
        return this.mCode;
    }

    public void setmCode(String mCode2) {
        this.mCode = mCode2;
    }

    public int getDevice_image() {
        return this.device_image;
    }

    public void setDevice_image(int device_image2) {
        this.device_image = device_image2;
    }

    public String getDevice_name() {
        return this.device_name;
    }

    public void setDevice_name(String device_name2) {
        this.device_name = device_name2;
    }

    public String getmBluetoothType() {
        return this.mBluetoothType;
    }

    public void setmBluetoothType(String mBluetoothType2) {
        this.mBluetoothType = mBluetoothType2;
    }

    public byte[] getScanRecord() {
        return this.scanRecord;
    }

    public void setScanRecord(byte[] scanRecord2) {
        this.scanRecord = scanRecord2;
    }

    public String toString() {
        return "device_name:" + this.device_name + " mCode:" + this.mCode + "  mDeviceName:" + this.mDeviceName;
    }
}
