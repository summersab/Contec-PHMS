package com.contec.phms.device.cmsvesd;

import com.contec.phms.device.template.DeviceService;

public class ReceiveThread extends com.contec.phms.device.template.ReceiveThread {
    int mCurLen = 2;
    int mDataLen;

    public ReceiveThread(DeviceService mDeviceService) {
        super(mDeviceService);
    }

    public void arrangeMessage(byte[] buf, int length) {
        for (int i = 0; i < length; i++) {
            byte[] bArr = this.mPack;
            int i2 = this.mCount;
            this.mCount = i2 + 1;
            bArr[i2] = buf[i];
            if (this.mCount == 2) {
                this.mPackLen = getLength(this.mPack[0], this.mPack[1]);
            }
            if (this.mCount > 1 && this.mCount >= this.mPackLen) {
                this.mDeviceService.mPackManager.processPack(this.mPack, this.mCount);
                this.mCount = 0;
            }
        }
    }

    int getLength(byte cmd1, byte cmd2) {
        int _len = 0;
        if (cmd1 == 0 && cmd2 == -80) {
            _len = 0;
        }
        if (cmd1 == 0 && cmd2 == -64) {
            _len = 9;
            this.mDataLen = 404;
        }
        if (cmd1 == 0 && cmd2 == -65) {
            _len = 9;
            this.mDataLen = 248;
        }
        if (cmd1 == 0 && cmd2 == -81) {
            _len = 13;
            this.mDataLen = 65535;
        }
        if (cmd1 == -1 && cmd2 == -65) {
            _len = 0;
            this.mDataLen = 0;
        }
        if (cmd1 == -1 && cmd2 == -1) {
            return this.mDataLen;
        }
        return _len;
    }
}
