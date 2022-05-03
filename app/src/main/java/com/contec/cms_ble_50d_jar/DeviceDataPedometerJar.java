package com.contec.cms_ble_50d_jar;

import java.util.ArrayList;
import java.util.List;

public class DeviceDataPedometerJar {
    private String TAG = DeviceDataPedometerJar.class.getSimpleName();
    private ArrayList<Object> mPedometerDataDayList = new ArrayList<>();
    private List<MinData> mPedometerDataMinList = new ArrayList();

    protected void addMinPedometerData(MinData pminData) {
        this.mPedometerDataMinList.add(pminData);
    }

    protected void addDayPedometerData(byte[] pdayData) {
        this.mPedometerDataDayList.add(pdayData);
    }

    public List<MinData> getmPedometerDataMinList() {
        return this.mPedometerDataMinList;
    }

    public ArrayList<Object> getmPedometerDataDayList() {
        return this.mPedometerDataDayList;
    }

    private String getMinString(byte[] pminData) {
        return " cal:" + ((pminData[1] & 255) | ((pminData[0] & 255) << 8)) + "  steps:" + ((pminData[3] & 255) | ((pminData[2] & 255) << 8));
    }

    private String getDayString(byte[] pdayData) {
        return "year:" + pdayData[0] + " month:" + pdayData[1] + "  day:" + pdayData[2] + " steps:" + ((pdayData[5] & 255) | ((pdayData[6] & 255) << 8)) + "  cal:" + ((pdayData[3] & 255) | ((pdayData[4] & 255) << 8));
    }
}
