package com.example.temp.ble_code;

import android.util.Log;
import com.alibaba.cchannel.CloudChannel;
import com.contec.phms.util.Constants;
import com.example.temp.bean.EarTempertureDataJar;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import u.aly.dp;

public class PackManagerThermometer {
    int _packCount = 0;
    private boolean bGetPackId = false;
    byte[] curPack = new byte[64];
    int i;
    int k = 0;
    int len = 0;
    int mCount;
    int mCountNo;
    public byte[] mSaveDataNumber;
    public List<EarTempertureDataJar> m_DeviceDatas = new ArrayList();
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
            case Constants.UPDATEXML_UPLOAD_FAIL:
                return 7;
            case Constants.THREAD_OUT:
                return 13;
            case Constants.UPDATE_XML_FAIL:
                return 3;
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
            case Constants.UPDATEXML_UPLOAD_FAIL:
                this.mSaveDataNumber = new byte[7];
                this.mSaveDataNumber[0] = pack[2];
                this.mSaveDataNumber[1] = (byte) (pack[3] & 255);
                this.mSaveDataNumber[2] = pack[4];
                this.mSaveDataNumber[3] = (byte) (pack[5] & 255);
                this.mCount = ((pack[5] << 7) | (pack[4] & 255)) & 65535;
                this.mCountNo = ((pack[3] << 7) | (pack[2] & 255)) & 65535;
                if (this.mCount > 0) {
                    _return = -32;
                } else {
                    _return = 18;
                }
                System.out.println("===========:" + this.mCount);
                return _return;
            case Constants.THREAD_OUT:
                this._packCount++;
                EarTempertureDataJar mthermometer = new EarTempertureDataJar();
                byte[] mDataEar = new byte[13];
                mDataEar[0] = (byte) (pack[2] & 255);
                mDataEar[1] = (byte) (pack[3] & dp.m);
                mDataEar[2] = (byte) (pack[4] & 255);
                mDataEar[3] = (byte) (pack[5] & 255);
                mDataEar[4] = (byte) (pack[6] & 255);
                mDataEar[5] = (byte) (pack[7] & 255);
                mDataEar[8] = pack[8];
                mDataEar[9] = pack[9];
                mDataEar[10] = pack[10];
                mDataEar[11] = pack[11];
                mDataEar[12] = pack[12];
                byte x1 = (byte) ((pack[10] & 255) | ((pack[9] & 1) << 7));
                int x2 = (byte) (pack[11] | ((pack[9] & 2) << 6));
                int type = pack[9] & 4;
                int ear = (((x1 & 255) << 8) | (x2 & 255)) & 65535;
                int errorear = (((pack[9] & 112) & 255) >> 4) & 15;
                System.out.println("++++++++type++++++++" + type);
                System.out.println("++++++ear++++++++++" + ear);
                if (type != 0) {
                    System.out.println(">>>>>>>>>ear>>>>>>>>>:" + ear);
                    BigDecimal decimalear = new BigDecimal(((((double) ear) * 1.8d) + 3200.0d) / 100.0d);
                    System.out.println(">>>>>>>>>decimalear>>>>>>>>>:" + decimalear);
                    BigDecimal dataear = decimalear.setScale(1, 1);
                    System.out.println(decimalear);
                    System.out.println(">>>>>>>>>dataear>>>>>>>>>:" + dataear);
                } else if (errorear == 0) {
                    Log.e("================", "++++++decimal22222++++++++++" + new BigDecimal(((double) ear) / 100.0d));
                    BigDecimal decimal = new BigDecimal(new StringBuilder(String.valueOf(((double) ear) / 100.0d)).toString());
                    Log.e("================", "++++++decimal++++++++++" + decimal);
                    BigDecimal x = decimal.setScale(1, 1);
                    mthermometer.m_data = x.doubleValue();
                    mthermometer.m_saveDate = String.valueOf(mDataEar[0] + 2000) + "-" + mDataEar[1] + "-" + mDataEar[2] + " " + mDataEar[3] + ":" + mDataEar[4] + ":" + mDataEar[5];
                    Log.e("================", "++++++ x ++++++++++" + x.doubleValue());
                } else if (errorear == 1) {
                    BigDecimal decimal2 = new BigDecimal(((double) ear) / 100.0d);
                    Log.e("================", "++++++decimal++++++++++" + decimal2);
                    BigDecimal scale = decimal2.setScale(1, 1);
                    mthermometer.m_data = 0.0d;
                    mthermometer.m_saveDate = String.valueOf(mDataEar[0] + 2000) + "-" + mDataEar[1] + "-" + mDataEar[2] + " " + mDataEar[3] + ":" + mDataEar[4] + ":" + mDataEar[5];
                    Log.e("================", "++++++ x ++++++++++" + decimal2);
                } else {
                    BigDecimal decimal3 = new BigDecimal(((double) ear) / 100.0d);
                    Log.e("================", "++++++decimal++++++++++" + decimal3);
                    BigDecimal scale2 = decimal3.setScale(1, 1);
                    mthermometer.m_data = 50.0d;
                    mthermometer.m_saveDate = String.valueOf(mDataEar[0] + 2000) + "-" + mDataEar[1] + "-" + mDataEar[2] + " " + mDataEar[3] + ":" + mDataEar[4] + ":" + mDataEar[5];
                    Log.e("================", "++++++ x ++++++++++" + decimal3);
                }
                this.m_DeviceDatas.add(mthermometer);
                if ((pack[1] & 64) == 64) {
                    this._packCount = 0;
                    return 67;
                } else if (this._packCount != 10) {
                    return -120;
                } else {
                    this._packCount = 0;
                    return 66;
                }
            case Constants.UPDATE_XML_FAIL:
                if (!Check(pack) || (pack[1] & 255) != 0) {
                    return 33;
                }
                return 32;
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
}
