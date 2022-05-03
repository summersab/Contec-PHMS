package com.contec.phms.device.contec8000gw;

import com.contec.phms.device.template.DataFilter;

public class DeviceService extends com.contec.phms.device.template.DeviceService {
    public void initObjects() {
        this.mReceiveThread = new ReceiveThread(this);
        this.mDataFilter = new DataFilter();
    }

    public void sendCommand() {
    }

    public void saveSDCard() {
    }

    public void processResult() {
    }
}
