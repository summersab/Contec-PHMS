package com.contec.phms.device.fhr01;

import com.contec.jar.fhr01.DeviceCommand;
import com.contec.phms.App_phms;
import com.contec.phms.device.template.DataFilter;
import com.contec.phms.device.template.SendCommand;
import com.contec.phms.manager.device.DeviceManager;
import cn.com.contec.jar.util.TimeUtil;

public class DeviceService extends com.contec.phms.device.template.DeviceService {
    public void initObjects() {
        this.mReceiveThread = new ReceiveThread(this);
        this.mDataFilter = new DataFilter();
    }

    public void sendCommand() {
        App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(DeviceManager.m_DeviceBean);
        SendCommand.send(DeviceCommand.CONNECTION_REQUEST(TimeUtil.processTimeStrings(true)));
    }

    public void saveSDCard() {
    }
}
