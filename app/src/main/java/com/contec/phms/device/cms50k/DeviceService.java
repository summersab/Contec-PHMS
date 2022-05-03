package com.contec.phms.device.cms50k;

import com.contec.jar.cms50k.DeviceCommand;
import com.contec.phms.device.template.DataFilter;
import com.contec.phms.device.template.SendCommand;
import com.contec.phms.manager.device.DeviceManager;

public class DeviceService extends com.contec.phms.device.template.DeviceService {
    public void initObjects() {
        this.mReceiveThread = new ReceiveThread(this);
        this.mDataFilter = new DataFilter();
    }

    public void sendCommand() {
        DeviceManager.mDeviceBeanList.mState = 4;
        DeviceManager.m_DeviceBean.mState = 4;
        DeviceManager.m_DeviceBean.mProgress = 0;
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        SendCommand.send(DeviceCommand.SET_TIME());
    }

    public void saveSDCard() {
    }
}
