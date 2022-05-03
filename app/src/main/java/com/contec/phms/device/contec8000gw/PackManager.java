package com.contec.phms.device.contec8000gw;

import com.contec.phms.device.template.DeviceData;
import com.contec.phms.manager.datas.DatasContainer;
import com.contec.phms.manager.device.DeviceManager;

public class PackManager extends com.contec.phms.device.template.PackManager {
    public static DeviceData mDeviceData;

    public PackManager() {
        mDeviceData = new DeviceData((byte[]) null);
    }

    public void processData(byte[] pack) {
    }

    public byte[] doPack(byte[] pack) {
        return null;
    }

    public void addData(DeviceData deviceData) {
        mDeviceData.setUniquenes(DeviceManager.m_DeviceBean.getDeivceUniqueness());
        mDeviceData.setmUploadType("case");
        DatasContainer.mDeviceDatas.add(mDeviceData);
        mDeviceData.setmUploadType("trend");
        DatasContainer.mDeviceDatas.add(mDeviceData);
    }

    public void processPack(byte[] pack, int count) {
    }

    public void initCmdPosition() {
    }

    public byte[] unPack(byte[] pack) {
        return null;
    }
}
