package com.contec.jar.blepm10;

import android.util.Log;
import com.alibaba.cchannel.CloudChannel;
import com.contec.jar.pm10.DeviceDataECG;
import com.contec.jar.pm10.PrintBytes;
import com.contec.phms.util.Constants;
import java.util.ArrayList;
import u.aly.dp;

public class PackManagerEcg {
    int _packCount = 0;
    private boolean bGetPackId = false;
    byte[] curPack = new byte[64];
    int datapack = 0;
    int i;
    int k = 0;
    int len = 0;
    int mCount;
    int mCount_ECG;
    public DeviceDataECG mEcg;
    public ArrayList<DeviceDataECG> mEcgs = new ArrayList<>();
    int uploadCount;
    byte value;

    public byte arrangeMessage(byte[] buf, int length) {
        byte _return = 0;
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
                this.len = PackLen(this.value);
                if (this.len == 0) {
                    _return = 0;
                    this.bGetPackId = false;
                } else {
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
            }
            this.i++;
        }
        return _return;
    }

    public int PackLen(byte pHead) {
        switch (pHead) {
            case -60:
                return 7;
            case -59:
                return 3;
            case -56:
                return 2;
            case Constants.UPDATEXML_UPLOAD_FAIL /*-32*/:
                return 7;
            case Constants.NO_COMPLETE /*-27*/:
                return 18;
            case Constants.LAST_CHECKOUT_FAIL /*-26*/:
                return 17;
            case -14:
                return 8;
            case -13:
                return 3;
            default:
                return 0;
        }
    }

    public boolean Check(byte[] pack) {
        int CHECK_SUM = 0;
        int _size = pack.length - 1;
        for (int i = 0; i < _size; i++) {
            CHECK_SUM += pack[i] & 255;
        }
        if ((CHECK_SUM & Byte.MAX_VALUE) == (pack[pack.length - 1] & Byte.MAX_VALUE)) {
            return true;
        }
        return false;
    }

    public byte processData(byte[] pack) {
        byte _return;
        switch (pack[0]) {
            case Constants.UPDATEXML_UPLOAD_FAIL /*-32*/:
                this.mEcg = new DeviceDataECG();
                this.mEcg.UpLoad_Data = new byte[5];
                this.mEcg.UpLoad_Data[0] = (byte) (pack[2] & 255);
                this.mEcg.UpLoad_Data[1] = (byte) (pack[3] & 255);
                this.mEcg.UpLoad_Data[2] = (byte) (pack[4] & 255);
                this.mEcg.UpLoad_Data[3] = (byte) (pack[5] & 255);
                this.mCount = ((pack[5] << 7) | (pack[4] & 255)) & 65535;
                this.uploadCount = ((pack[3] << 7) | (pack[2] & 255)) & 65535;
                Log.d("心电返回的数据未上传的数据", new StringBuilder().append(this.mCount).toString());
                Log.d("心电返回的数据上传的数据", new StringBuilder().append(this.uploadCount).toString());
                switch (pack[1]) {
                    case 3:
                        if (this.mCount == 0) {
                            return 113;
                        }
                        return 112;
                    default:
                        return 0;
                }
            case Constants.NO_COMPLETE /*-27*/:
                Log.e("DevicePackManager", "-------心电图片段基本信息--------");
                this._packCount = 0;
                this.mEcg = new DeviceDataECG();
                this.mEcg.Point_data = new byte[22];
                this.mEcg.Point_data[0] = pack[1];
                this.mEcg.Point_data[1] = pack[2];
                this.mEcg.Point_data[2] = pack[3];
                this.mEcg.Point_data[3] = pack[4];
                this.mEcg.Point_data[4] = pack[5];
                this.mEcg.Point_data[5] = pack[6];
                this.mEcg.Point_data[6] = pack[8];
                this.mEcg.Point_data[7] = pack[7];
                dealData(this.mEcg.Point_data, pack[9]);
                dealData(this.mEcg.Point_data, pack[10]);
                dealData(this.mEcg.Point_data, pack[11]);
                dealData(this.mEcg.Point_data, pack[12]);
                dealData(this.mEcg.Point_data, pack[13]);
                dealData(this.mEcg.Point_data, pack[14]);
                if ((this.mEcg.Point_data[9] | this.mEcg.Point_data[10] | this.mEcg.Point_data[11] | this.mEcg.Point_data[12] | this.mEcg.Point_data[13] | this.mEcg.Point_data[14] | this.mEcg.Point_data[15] | this.mEcg.Point_data[16] | this.mEcg.Point_data[17] | this.mEcg.Point_data[18] | this.mEcg.Point_data[19] | this.mEcg.Point_data[20] | this.mEcg.Point_data[21]) > 0) {
                    this.mEcg.Point_data[8] = 0;
                }
                int _dataLen = ((pack[16] << 7) | (pack[15] & 255)) & 65535;
                Log.i("info", new StringBuilder().append(_dataLen).toString());
                this.mEcg.ECG_Data = new byte[(_dataLen * 2)];
                this.mCount--;
                this.mCount_ECG = 0;
                return 117;
            case Constants.LAST_CHECKOUT_FAIL /*-26*/:
                PrintBytes.printData(pack, pack.length);
                Log.e("DevicePackManager", "-------心电片段--------");
                unECG_Pack(pack);
                this._packCount++;
                if ((pack[1] & 64) == 64) {
                    int _len = this.mEcg.ECG_Data.length - (this.mCount_ECG * 12);
                    for (int i = 0; i < _len; i++) {
                        this.mEcg.ECG_Data[(this.mCount_ECG * 12) + i] = pack[i + 4];
                    }
                    this.mEcgs.add(this.mEcg);
                    this._packCount = 0;
                    if (this.mCount == 0) {
                        _return = 116;
                    } else {
                        _return = 115;
                    }
                } else {
                    this.mEcg.ECG_Data[(this.mCount_ECG * 12) + 0] = pack[4];
                    this.mEcg.ECG_Data[(this.mCount_ECG * 12) + 1] = pack[5];
                    this.mEcg.ECG_Data[(this.mCount_ECG * 12) + 2] = pack[6];
                    this.mEcg.ECG_Data[(this.mCount_ECG * 12) + 3] = pack[7];
                    this.mEcg.ECG_Data[(this.mCount_ECG * 12) + 4] = pack[8];
                    this.mEcg.ECG_Data[(this.mCount_ECG * 12) + 5] = pack[9];
                    this.mEcg.ECG_Data[(this.mCount_ECG * 12) + 6] = pack[10];
                    this.mEcg.ECG_Data[(this.mCount_ECG * 12) + 7] = pack[11];
                    this.mEcg.ECG_Data[(this.mCount_ECG * 12) + 8] = pack[12];
                    this.mEcg.ECG_Data[(this.mCount_ECG * 12) + 10] = pack[14];
                    this.mEcg.ECG_Data[(this.mCount_ECG * 12) + 11] = pack[15];
                    if (this._packCount == 10) {
                        this._packCount = 0;
                        _return = 114;
                    } else {
                        _return = -120;
                    }
                }
                this.mCount_ECG++;
                Log.i("记录接收到心电数据包的个数", new StringBuilder().append(this.mCount_ECG).toString());
                return _return;
            case -13:
                if (!Check(pack) || (pack[1] & 255) != 0) {
                    return CloudChannel.SDK_VERSION;
                }
                return dp.n;
            default:
                return 0;
        }
    }

    static byte[] unPackPedometer(byte[] pack) {
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 4; j++) {
                int i2 = j + 6;
                pack[i2] = (byte) (pack[i2] | ((pack[5] << (7 - j)) & 128));
            }
        }
        return pack;
    }

    static byte[] unECG_Pack(byte[] pack) {
        for (int j = 0; j < 7; j++) {
            int i = j + 4;
            pack[i] = (byte) (pack[i] | ((pack[2] << (7 - j)) & 128));
        }
        for (int j2 = 0; j2 < 5; j2++) {
            int i2 = j2 + 11;
            pack[i2] = (byte) (pack[i2] | ((pack[3] << (7 - j2)) & 128));
        }
        return pack;
    }

    static byte[] unPack(byte[] pack) {
        for (int j = 0; j < 7; j++) {
            int i = j + 4;
            pack[i] = (byte) (pack[i] | ((pack[2] << (7 - j)) & 128));
        }
        for (int j2 = 0; j2 < 5; j2++) {
            int i2 = j2 + 11;
            pack[i2] = (byte) (pack[i2] | ((pack[3] << (7 - j2)) & 128));
        }
        return pack;
    }

    void dealData(byte[] pdata, byte pDal) {
        switch (pDal) {
            case 0:
                pdata[8] = 1;
                return;
            case 1:
                pdata[8] = 0;
                pdata[9] = 1;
                return;
            case 2:
                pdata[8] = 0;
                pdata[10] = 1;
                return;
            case 3:
                pdata[8] = 0;
                pdata[11] = 1;
                return;
            case 4:
                pdata[8] = 0;
                pdata[12] = 1;
                return;
            case 5:
                pdata[8] = 0;
                pdata[13] = 1;
                return;
            case 6:
                pdata[8] = 0;
                pdata[14] = 1;
                return;
            case 7:
                pdata[8] = 0;
                pdata[15] = 1;
                return;
            case 8:
                pdata[8] = 0;
                pdata[16] = 1;
                return;
            case 9:
                pdata[8] = 0;
                pdata[17] = 1;
                return;
            case 10:
                pdata[8] = 0;
                pdata[18] = 1;
                return;
            case 11:
                pdata[8] = 0;
                pdata[19] = 1;
                return;
            case 12:
                pdata[8] = 0;
                pdata[20] = 1;
                return;
            case 13:
                pdata[8] = 0;
                pdata[21] = 1;
                return;
            default:
                return;
        }
    }
}
