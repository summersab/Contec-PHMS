package com.contec.phms.SearchDevice;

import java.util.HashMap;

public class SortDeviceContainerMap {
    private static SortDeviceContainerMap mSortDeviceContainerMap = null;
    HashMap<String, SortDeviceContainer> mSortDeviceMap = new HashMap<>();

    private SortDeviceContainerMap() {
    }

    public static SortDeviceContainerMap getInstance() {
        if (mSortDeviceContainerMap == null) {
            mSortDeviceContainerMap = new SortDeviceContainerMap();
        }
        return mSortDeviceContainerMap;
    }

    public HashMap<String, SortDeviceContainer> getmSortDeviceMap() {
        return this.mSortDeviceMap;
    }

    public void setmSortDeviceMap(HashMap<String, SortDeviceContainer> mSortDeviceList) {
        this.mSortDeviceMap = mSortDeviceList;
    }

    public void addelement(String pkey, SortDeviceContainer pSortDeviceContainer) {
        this.mSortDeviceMap.put(pkey, pSortDeviceContainer);
    }

    public void clearSortlist() {
        this.mSortDeviceMap.clear();
    }
}
