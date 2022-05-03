package com.contec.phms.device.cmsvesd;

import com.contec.phms.device.template.SendCommand;

/* compiled from: PackManager */
class ConfirmThread extends Thread {
    ConfirmThread() {
    }

    public void run() {
        while (!PackManager.mDataFinished) {
            try {
                SendCommand.send(DeviceOrder.CONFIRM_TRANSFER);
                sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
