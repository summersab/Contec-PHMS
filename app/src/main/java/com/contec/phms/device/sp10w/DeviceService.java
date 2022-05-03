package com.contec.phms.device.sp10w;

import com.contec.phms.App_phms;
import com.contec.phms.device.template.DataFilter;
import com.contec.phms.device.template.SendCommand;
import com.contec.phms.manager.device.DeviceManager;
import cn.com.contec.jar.sp10w.DeviceCommand;

public class DeviceService extends com.contec.phms.device.template.DeviceService {
    public void initObjects() {
        this.mReceiveThread = new ReceiveThread(this);
        this.mDataFilter = new DataFilter();
    }

    public void sendCommand() {
        DeviceManager.m_DeviceBean.mProgress = 0;
        App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(DeviceManager.m_DeviceBean);
        SendCommand.send(DeviceCommand.command_Date());
    }

    public void saveSDCard() {
    }

    public void processResult() {
    }
}
