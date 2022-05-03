package com.contec.phms.device.cms50f;

import com.contec.jar.cms50ew.DeviceCommand;
import com.contec.jar.cms50ew.DevicePackManager;
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
        DeviceManager.mDeviceBeanList.mState = 4;
        DeviceManager.m_DeviceBean.mState = 4;
        DeviceManager.m_DeviceBean.mProgress = 0;
        App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(DeviceManager.m_DeviceBean);
        byte[] mcomd_date = DeviceCommand.SET_DATE();
        DevicePackManager.doPack(mcomd_date);
        SendCommand.send(mcomd_date);
        CLog.d(com.contec.phms.device.template.DeviceService.TAG, "ew: 对时开始******************");
    }

    public void saveSDCard() {
    }
}
