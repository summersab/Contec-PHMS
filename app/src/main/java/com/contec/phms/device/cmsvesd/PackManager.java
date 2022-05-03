package com.contec.phms.device.cmsvesd;

import com.contec.phms.device.template.SendCommand;
import com.contec.phms.util.PrintBytes;

public class PackManager extends com.contec.phms.device.template.PackManager {
    public static boolean mDataFinished = false;
    private final int ORDER_ECG = 2;
    private final int ORDER_SOUND = 4;
    private final int ORDER_SPO2 = 3;
    private final int ORDER_UPLOAD = 1;
    private final int ORDER_UPLOAD_DATA = 5;
    private final int ORDER_UPLOAD_FINISH = 6;
    DeviceData mDeviceData;

    public void processPack(byte[] pack, int count) {
        PrintBytes.printData(pack, count);
        switch (checkOrder(pack[1], pack[0])) {
            case 1:
                this.mDeviceData = new DeviceData(pack);
                SendCommand.send(DeviceOrder.UPLOAD_FILE_CONFIRM);
                new ConfirmThread().start();
                return;
            case 2:
            case 3:
            case 4:
                this.mDeviceData.addData(pack);
                return;
            case 5:
                this.mDeviceData.addData(unPack(pack));
                return;
            case 6:
                this.mDeviceData.makeInfos();
                uploadFinished();
                return;
            default:
                return;
        }
    }

    public byte[] unPack(byte[] pack) {
        byte[] temp = new byte[pack.length];
        for (int i = 0; i < temp.length; i++) {
            if (i % 2 == 1) {
                temp[i - 1] = pack[i];
                temp[i] = pack[i - 1];
            }
        }
        return temp;
    }

    public byte[] doPack(byte[] pack) {
        byte[] temp = new byte[2];
        for (int i = 0; i < 2; i++) {
            temp[i] = pack[1 - i];
        }
        return temp;
    }

    public void processData(byte[] pack) {
    }

    public void initCmdPosition() {
    }

    public int checkOrder(byte temp1, byte temp2) {
        if (temp1 == DeviceOrder.UPLOAD_FILE[0] && temp2 == DeviceOrder.UPLOAD_FILE[1]) {
            return 1;
        }
        if (temp1 == DeviceOrder.ORDER_TYPE_ECG[0] && temp2 == DeviceOrder.ORDER_TYPE_ECG[1]) {
            return 2;
        }
        if (temp1 == DeviceOrder.ORDER_TYPE_SPO2[0] && temp2 == DeviceOrder.ORDER_TYPE_SPO2[1]) {
            return 3;
        }
        if (temp1 == DeviceOrder.ORDER_TYPE_SOUND[0] && temp2 == DeviceOrder.ORDER_TYPE_SOUND[1]) {
            return 4;
        }
        if (temp1 == -1 && temp2 == -1) {
            return 5;
        }
        return (temp1 == -65 && temp2 == -1) ? 6 : 0;
    }

    void uploadFinished() {
        mDataFinished = true;
        DeviceService.mReceiveFinished = true;
        addData(this.mDeviceData);
    }
}
