package com.contec.phms.device.cms50d;

import com.contec.cms50dj_jar.DeviceCommand;
import com.contec.phms.App_phms;
import com.contec.phms.device.template.DataFilter;
import com.contec.phms.device.template.SendCommand;
import com.contec.phms.manager.device.DeviceManager;
import com.contec.phms.util.CLog;

public class DeviceService extends com.contec.phms.device.template.DeviceService {
    public void initObjects() {
        this.mReceiveThread = new ReceiveThread(this);
        this.mDataFilter = new DataFilter();
    }

    public void sendCommand() {
        DeviceManager.m_DeviceBean.mProgress = 0;
        App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(DeviceManager.m_DeviceBean);
        new Thread() {
            public void run() {
                try {
                    Thread.sleep(100);
                    CLog.d(com.contec.phms.device.template.DeviceService.TAG, "50dj: 对时索要设备号!!!!111111******************");
                    SendCommand.send(DeviceCommand.deviceConfirmCommand());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    public void saveSDCard() {
    }
}
