package com.contec.cms50ew.code;

import android.util.Log;
import com.alibaba.cchannel.CloudChannel;
import com.contec.phms.util.Constants;
import com.contec.sdk.PrintBytes;
import java.util.ArrayList;
import u.aly.dp;

public class DevicePackManager {
    int _packCount = 0;
    private boolean bGetPackId = false;
    byte[] curPack = new byte[64];
    int i;
    int k = 0;
    int len = 0;
    public int mCount;
    int mCount_ECG;
    byte mDay;
    public DeviceDataIW mDeviceDataIW;
    public ArrayList<DeviceDataIW> mDeviceDataIWs = new ArrayList<>();
    byte mHour;
    byte mMinu;
    byte mMonth;
    public int mNoUpLoadCount;
    int mPackCount;
    private ArrayList<String> mReceiveContainer = new ArrayList<>();
    byte mSeco;
    int mSegmentLen;
    byte mYear;
    public ArrayList<Object> minDatas = new ArrayList<>();
    byte pluse;
    private int product;
    byte spo2;
    byte value;

    public int getProduct() {
        return this.product;
    }

    public void setProduct(int product2) {
        this.product = product2;
    }

    public byte arrangeMessage(byte[] buf, int length) {
        Log.e("$$$$$$$$$$$$$$$$$$$$$$", "BLE Bluetooth bufbufbufbufbufbufbufbufbuf");
        PrintBytes.printData(buf, length);
        Log.e("$$$$$$$$$$$$$$$$$$$$$$", "BLE Bluetooth bufbufbufbufbufbufbufbufbuf");
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
            case -49:
                return 10;
            case -48:
                return 14;
            case -47:
                return 4;
            case -46:
                return 20;
            case -45:
                return 20;
            case -44:
                return 18;
            case -43:
                return 12;
            case Constants.UPDATEXML_UPLOAD_FAIL /*-32*/:
                return 7;
            case Constants.GENERATE_XML_FAIL /*-31*/:
                return 11;
            case Constants.THREAD_OUT /*-30*/:
                return 11;
            case Constants.UPDATE_XML_FAIL /*-29*/:
                return 9;
            case Constants.ALL_FILE_ERROR /*-28*/:
                return 17;
            case Constants.NO_COMPLETE /*-27*/:
                return 18;
            case Constants.LAST_CHECKOUT_FAIL /*-26*/:
                return 17;
            case Constants.NET_INIT_FAIL /*-25*/:
                return 10;
            case Constants.UNITE_FILE_ERROR /*-24*/:
                return 17;
            case Constants.MALLOC_ERROR /*-23*/:
                return 13;
            case -15:
                return 10;
            case -14:
                return 8;
            case -13:
                return 3;
            case -12:
                return 3;
            case -11:
                return 3;
            case -5:
                return 3;
            case -4:
                return 3;
            case 1:
                return 1;
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
        switch (pack[0]) {
            case Constants.UPDATEXML_UPLOAD_FAIL /*-32*/:
                PrintBytes.printDatai(pack, pack.length);
                Log.e("DevicePackManager", "--------0xE0-------");
                this.mCount = ((pack[3] << 7) | (pack[2] & 255)) & 65535;
                this.mNoUpLoadCount = ((pack[5] << 7) | (pack[4] & 255)) & 65535;
                switch (pack[1]) {
                    case 0:
                        Log.d("wsd1", new StringBuilder().append(this.mCount).toString());
                        if (this.mNoUpLoadCount == 0) {
                            return 65;
                        }
                        return 64;
                    default:
                        return 0;
                }
            case Constants.GENERATE_XML_FAIL /*-31*/:
                Log.e("DevicePackManager", "-----0xE1----------");
                this._packCount++;
                this.mDeviceDataIW = new DeviceDataIW();
                this.mDeviceDataIW._value_ew = new byte[8];
                this.mDeviceDataIW._Year = (byte) (pack[2] & 255);
                this.mDeviceDataIW._Month = (byte) (pack[3] & dp.m);
                this.mDeviceDataIW._Day = (byte) (pack[4] & 255);
                this.mDeviceDataIW._Hour = (byte) (pack[5] & 255);
                this.mDeviceDataIW._Min = (byte) (pack[6] & 255);
                this.mDeviceDataIW._Sec = (byte) (pack[7] & 255);
                this.mDeviceDataIW._value_ew[6] = (byte) (pack[8] & 255);
                this.mDeviceDataIW._value_ew[7] = (byte) (pack[9] & 255);
                String _cms50ew = new StringBuilder().append(this.mDeviceDataIW._Year).append(this.mDeviceDataIW._Month).append(this.mDeviceDataIW._Day).append(this.mDeviceDataIW._Hour).append(this.mDeviceDataIW._Min).append(this.mDeviceDataIW._Sec).append(this.mDeviceDataIW._value_ew[6]).append(this.mDeviceDataIW._value_ew[7]).toString();
                Log.e("BLE Bluetooth Data", "_cms50ew========================");
                PrintBytes.printData(pack);
                Log.e("BLE Bluetooth Data", "_cms50ew" + _cms50ew);
                Log.e("BLE Bluetooth Data", "_cms50ew========================");
                this.mDeviceDataIWs.add(this.mDeviceDataIW);
                if ((pack[1] & 64) == 64) {
                    this._packCount = 0;
                    return 67;
                } else if (this._packCount != 10) {
                    return -120;
                } else {
                    this._packCount = 0;
                    return 66;
                }
            case -13:
                Log.e("DevicePackManager", "------0xF3---------");
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
            int i = j + 5;
            pack[i] = (byte) (pack[i] | ((pack[3] << (7 - j)) & 128));
        }
        for (int j2 = 0; j2 < 5; j2++) {
            int i2 = j2 + 12;
            pack[i2] = (byte) (pack[i2] | ((pack[4] << (7 - j2)) & 128));
        }
        return pack;
    }

    protected static byte[] unNewPack(byte[] pack) {
        for (int i = 0; i < 7; i++) {
            int i2 = i + 5;
            pack[i2] = (byte) (pack[i2] | ((pack[3] << (7 - i)) & 128));
        }
        for (int j = 0; j < 7; j++) {
            int i3 = j + 12;
            pack[i3] = (byte) (pack[i3] | ((pack[4] << (7 - j)) & 128));
        }
        return pack;
    }

    protected static byte[] unContinuousData(byte data, byte pack) {
        byte frist;
        byte second;
        byte high4 = (byte) ((pack & 240) >> 4);
        byte low4 = (byte) (pack & dp.m);
        byte high = (byte) (high4 & 7);
        byte low = (byte) (low4 & 7);
        byte highOne = (byte) ((pack & 128) >> 7);
        byte lowOne = (byte) ((pack & 8) >> 3);
        if (high4 == 15) {
            frist = -1;
            second = -1;
        } else {
            if (highOne == 0) {
                frist = (byte) (data + high);
            } else {
                frist = (byte) (data - high);
            }
            if (low4 == 15) {
                second = -1;
            } else if (lowOne == 0) {
                second = (byte) (frist + low);
            } else {
                second = (byte) (frist - low);
            }
        }
        return new byte[]{frist, second};
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
