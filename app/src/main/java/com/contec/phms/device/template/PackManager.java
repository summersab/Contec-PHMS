package com.contec.phms.device.template;

import com.contec.phms.device.cmsvesd.DeviceService;
import com.contec.phms.manager.datas.DatasContainer;
import com.contec.phms.manager.device.ServiceBean;

public abstract class PackManager {
    public static final String TAG = ("PackManager_" + ServiceBean.getInstance().getmDeviceName());
    public byte mCommand;
    public int mPosition;

    public abstract byte[] doPack(byte[] bArr);

    public abstract void initCmdPosition();

    public abstract void processData(byte[] bArr);

    public abstract void processPack(byte[] bArr, int i);

    public abstract byte[] unPack(byte[] bArr);

    public PackManager() {
        initCmdPosition();
    }

    public byte[] trimPack(byte[] pack) {
        int _count = 0;
        int i = 0;
        while (i < pack.length && pack[i] != 0) {
            _count++;
            i++;
        }
        byte[] _trimedPack = new byte[_count];
        for (int i2 = 0; i2 < _trimedPack.length; i2++) {
            _trimedPack[i2] = pack[i2];
        }
        return _trimedPack;
    }

    public void noData() {
        datasFinished();
    }

    public boolean checkPack(byte[] pack) {
        if (pack[this.mPosition] == this.mCommand) {
            return true;
        }
        return false;
    }

    public void addData(DeviceData deviceData) {
        DatasContainer.mDeviceDatas.add(deviceData);
    }

    public void datasFinished() {
        DeviceService.mReceiveFinished = true;
    }
}
