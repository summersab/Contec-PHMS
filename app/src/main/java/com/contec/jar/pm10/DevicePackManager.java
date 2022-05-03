package com.contec.jar.pm10;

import android.util.Log;
import com.contec.phms.util.Constants;
import u.aly.dp;

public class DevicePackManager {
    public static final byte e_back_caseinfo = -32;
    public static final byte e_back_deletedata = -64;
    public static final byte e_back_settime = -14;
    public static final byte e_back_single_caseinfo = -31;
    public static final byte e_back_single_data = -48;
    public int PackLen = 64;
    int _caseLen = 0;
    int _dataCount = 0;
    int _revDataCount = 0;
    boolean bGetPackId = false;
    byte[] curPack = new byte[64];
    int i;
    int k = 0;
    int len = 0;
    int m = 0;
    public int mCount = 0;
    public DeviceDataECG mDeviceData = new DeviceDataECG();
    byte value;

    public byte[] arrangeMessage(byte[] buf, int length) {
        byte[] _return = null;
        this.i = 0;
        while (this.i < length) {
            this.value = buf[this.i];
            if (this.bGetPackId) {
                byte[] bArr = this.curPack;
                int i = this.k;
                this.k = i + 1;
                bArr[i] = this.value;
                if (this.k >= this.len) {
                    this.bGetPackId = false;
                    _return = processData(this.curPack);
                }
            } else {
                this.bGetPackId = true;
                this.k = 0;
                this.len = this.PackLen;
                this.curPack = new byte[this.len];
                byte[] bArr2 = this.curPack;
                int i2 = this.k;
                this.k = i2 + 1;
                bArr2[i2] = this.value;
                if (this.len == 1) {
                    _return = processData(this.curPack);
                    this.bGetPackId = false;
                }
            }
            this.i++;
        }
        return _return;
    }

    public byte[] processData(byte[] pack) {
        switch (pack[0]) {
            case -48:
                unPack(pack);
                for (int i = 0; i < 50; i += 2) {
                    this.mDeviceData.CaseData[(this._revDataCount * 50) + i] = pack[i + 10 + 1 + 1];
                    this.mDeviceData.CaseData[(this._revDataCount * 50) + i + 1] = pack[i + 10 + 1];
                }
                this._revDataCount++;
                if (this._revDataCount != this._dataCount) {
                    return pack;
                }
                byte[] _return = new byte[64];
                _return[0] = -1;
                return _return;
            case Constants.UPDATEXML_UPLOAD_FAIL /*-32*/:
                this.mCount = ((pack[2] << 7) | (pack[1] & 255)) & 65535;
                return pack;
            case Constants.GENERATE_XML_FAIL /*-31*/:
                this.mDeviceData.mYear = ((pack[3] << 7) | (pack[4] & 255)) & 65535;
                this.mDeviceData.mMonth = pack[5] & Byte.MAX_VALUE;
                this.mDeviceData.mDay = pack[6] & Byte.MAX_VALUE;
                this.mDeviceData.mHour = pack[7] & Byte.MAX_VALUE;
                this.mDeviceData.mMin = pack[8] & Byte.MAX_VALUE;
                this.mDeviceData.mSec = pack[9] & Byte.MAX_VALUE;
                this.mDeviceData.Plus = (((pack[14] & 255) << 7) | (pack[15] & 255)) & 2047;
                this.mDeviceData.mResult[0] = pack[16];
                this.mDeviceData.mResult[0] = pack[17];
                this.mDeviceData.mResult[0] = pack[18];
                this.mDeviceData.mResult[0] = pack[19];
                this.mDeviceData.mResult[0] = pack[20];
                this.mDeviceData.mResult[0] = pack[21];
                this._caseLen = (pack[10] << 21) | (pack[11] << dp.l) | (pack[12] << 7) | pack[13];
                this._dataCount = this._caseLen / 25;
                this._revDataCount = 0;
                this.mDeviceData.CaseData = new byte[(this._caseLen * 2)];
                Log.e(Constants.PM10_NAME, new StringBuilder().append(this._caseLen).toString());
                return pack;
            default:
                return pack;
        }
    }

    static byte[] unPack(byte[] pack) {
        for (int i = 0; i < 8; i++) {
            if (i == 7) {
                int i2 = (i * 7) + 11;
                pack[i2] = (byte) (pack[i2] | ((pack[i + 3] << 7) & 128));
            } else {
                for (int j = 0; j < 7; j++) {
                    int i3 = (i * 7) + 11 + j;
                    pack[i3] = (byte) (pack[i3] | ((pack[i + 3] << (7 - j)) & 128));
                }
            }
        }
        return pack;
    }

    public static void doPack(byte[] pack) {
    }
}
