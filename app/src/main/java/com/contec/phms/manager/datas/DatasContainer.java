package com.contec.phms.manager.datas;

import com.contec.phms.device.template.DeviceData;

public class DatasContainer {
    public static DeviceData mCurData;
    public static DataList mDeviceDatas;
    public static DataList mFailedDatas;
    public static DataList mUploadedDatas;

    public DatasContainer() {
        init();
        clearAll();
    }

    public void setCurData(DeviceData data) {
        mCurData = data;
    }

    public void init() {
        mDeviceDatas = new DataList();
        mFailedDatas = new DataList();
        mUploadedDatas = new DataList();
    }

    public void clearAll() {
        mDeviceDatas.clear();
        mFailedDatas.clear();
        mUploadedDatas.clear();
    }
}
