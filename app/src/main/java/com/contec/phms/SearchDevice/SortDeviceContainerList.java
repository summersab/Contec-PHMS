package com.contec.phms.SearchDevice;

import java.util.ArrayList;

public class SortDeviceContainerList {
    private static SortDeviceContainerList mSortDeviceContainerList = null;
    ArrayList<SortDeviceContainer> mSortDeviceList = new ArrayList<>();

    private SortDeviceContainerList() {
    }

    public static SortDeviceContainerList getInstance() {
        if (mSortDeviceContainerList == null) {
            mSortDeviceContainerList = new SortDeviceContainerList();
        }
        return mSortDeviceContainerList;
    }

    public ArrayList<SortDeviceContainer> getmSortDeviceList() {
        return this.mSortDeviceList;
    }

    public void setmSortDeviceList(ArrayList<SortDeviceContainer> mSortDeviceList2) {
        this.mSortDeviceList = mSortDeviceList2;
    }

    public void addelement(SortDeviceContainer pSortDeviceContainer) {
        boolean _cansave = true;
        int i = 0;
        while (true) {
            if (i >= this.mSortDeviceList.size()) {
                break;
            } else if (this.mSortDeviceList.get(i).getmDeviceNameCode().equals(pSortDeviceContainer.getmDeviceNameCode())) {
                _cansave = false;
                break;
            } else {
                i++;
            }
        }
        if (_cansave) {
            this.mSortDeviceList.add(pSortDeviceContainer);
        }
    }

    public void clearSortlist() {
        this.mSortDeviceList.clear();
    }
}
