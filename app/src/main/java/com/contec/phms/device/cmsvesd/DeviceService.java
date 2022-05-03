package com.contec.phms.device.cmsvesd;

import com.contec.phms.device.template.DataFilter;

public class DeviceService extends com.contec.phms.device.template.DeviceService {
    public void initObjects() {
        this.mReceiveThread = new ReceiveThread(this);
        this.mPackManager = new PackManager();
        this.mDataFilter = new DataFilter();
    }

    public void sendCommand() {
    }

    public void saveSDCard() {
        nextStep();
    }
}
