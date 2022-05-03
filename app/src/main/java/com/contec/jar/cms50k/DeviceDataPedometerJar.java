package com.contec.jar.cms50k;

import java.util.ArrayList;

public class DeviceDataPedometerJar {
    private String TAG = "DeviceDataPedometer";
    private ArrayList<Object> mPedometerDataDayList = new ArrayList<>();

    public void addDayPedometerData(byte[] pdayData) {
        this.mPedometerDataDayList.add(pdayData);
        DebugLog.d(this.TAG, getDayString(pdayData));
    }

    public ArrayList<Object> getmPedometerDataDayList() {
        return this.mPedometerDataDayList;
    }

    public void setmPedometerDataDayList(ArrayList<Object> mPedometerDataDayList2) {
        this.mPedometerDataDayList = mPedometerDataDayList2;
    }

    public String getMinString(byte[] pminData) {
        return " cal:" + ((pminData[1] & 255) | ((pminData[0] & 255) << 8)) + "  steps:" + ((pminData[3] & 255) | ((pminData[2] & 255) << 8));
    }

    public String getDayString(byte[] pdayData) {
        return "year:" + pdayData[0] + " month:" + pdayData[1] + "  day:" + pdayData[2] + " steps:" + ((pdayData[5] & 255) | ((pdayData[6] & 255) << 8)) + "  cal:" + ((pdayData[3] & 255) | ((pdayData[4] & 255) << 8));
    }
}
