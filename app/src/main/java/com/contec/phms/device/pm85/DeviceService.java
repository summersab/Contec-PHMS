package com.contec.phms.device.pm85;

import com.contec.jar.pm85.DeviceCommand;
import com.contec.jar.pm85.DevicePackManager;
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
        App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(DeviceManager.m_DeviceBean);
        byte[] _setDate = DeviceCommand.SET_DATE();
        DevicePackManager.doPack(_setDate);
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        SendCommand.send(_setDate);
    }

    public void saveSDCard() {
    }
}
