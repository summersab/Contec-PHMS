package com.contec.phms.device.temp01;

import com.contec.phms.App_phms;
import com.contec.phms.device.template.DataFilter;
import com.contec.phms.device.template.SendCommand;
import com.contec.phms.manager.device.DeviceManager;
import com.example.temp.bm77_code.DeviceCommand;

public class DeviceService extends com.contec.phms.device.template.DeviceService {
    public void sendCommand() {
        DeviceManager.m_DeviceBean.mProgress = 0;
        App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(DeviceManager.m_DeviceBean);
        new Thread() {
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (Exception e) {
                }
                SendCommand.send(DeviceCommand.commandConfirmEquipment());
            }
        }.start();
    }

    public void saveSDCard() {
    }

    public void initObjects() {
        this.mReceiveThread = new ReceiveThread(this);
        this.mDataFilter = new DataFilter();
    }
}
