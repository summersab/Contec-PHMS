package com.contec.phms.device.contec8000gw;

import com.contec.phms.device.template.DeviceService;

public class ReceiveThread extends com.contec.phms.device.template.ReceiveThread {
    public ReceiveThread(DeviceService mDeviceService) {
        super(mDeviceService);
    }

    public void arrangeMessage(byte[] buf, int length) {
    }
}
