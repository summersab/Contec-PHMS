package com.contec.phms.device.pm10;

import com.contec.phms.App_phms;
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
        App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(DeviceManager.m_DeviceBean);
        SendCommand.send(DeviceCommand.SET_TIME());
    }

    public void saveSDCard() {
    }
}
