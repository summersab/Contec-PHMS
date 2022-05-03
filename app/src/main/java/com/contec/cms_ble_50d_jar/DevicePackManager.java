package com.contec.cms_ble_50d_jar;

import android.util.Log;
import com.alibaba.cchannel.CloudChannel;
import com.contec.phms.util.Constants;
import u.aly.dp;

public class DevicePackManager {
    private int _packCount = 0;
    private boolean bGetPackId = false;
    private byte[] curPack = new byte[64];
    private int i;
    private int k = 0;
    private int len = 0;
    private int mCount;
    private MinData mMinData;
    private DeviceDataPedometerJar mPedometerJar = new DeviceDataPedometerJar();
    private DeviceDataSPO2H mSpo2h = new DeviceDataSPO2H();
    private int product;
    private byte value;

    public byte arrangeMessage(byte[] buf, int length) {
        PrintBytes.printData(buf, length);
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
            case Constants.CREATE_THREAD_ERROR /*-22*/:
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
            case -6:
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
        byte _return;
        switch (pack[0]) {
            case -60:
                if (!Check(pack) || pack[5] != 1) {
                    return 22;
                }
                return 21;
            case -59:
                if (!Check(pack)) {
                    return 39;
                }
                if (pack[1] == 1) {
                    return 37;
                }
                if (pack[1] == 2) {
                    return 38;
                }
                return 39;
            case -56:
                return 40;
            case -47:
                if (pack[2] != 0) {
                    return 0;
                }
                switch (pack[1]) {
                    case 0:
                        return -64;
                    case 1:
                        return -63;
                    default:
                        return 0;
                }
            case Constants.UPDATEXML_UPLOAD_FAIL /*-32*/:
                PrintBytes.printDatai(pack, pack.length);
                DebugLog.e("DevicePackManager", "-------数据存储空间--------");
                this.mCount = ((pack[5] << 7) | (pack[4] & 255)) & 65535;
                switch (pack[1]) {
                    case 0:
                        DebugLog.d("DevicePackManager", "单次血氧数据个数：" + this.mCount);
                        if (this.mCount == 0) {
                            return 65;
                        }
                        return 64;
                    case 1:
                        DebugLog.d("DevicePackManager", "全天计步器个数：" + this.mCount);
                        if (this.mCount == 0) {
                            return 81;
                        }
                        return 80;
                    case 2:
                        DebugLog.d("DevicePackManager", "5分钟计步器个数：" + this.mCount);
                        if (this.mCount == 0) {
                            return 97;
                        }
                        return 96;
                    default:
                        return 0;
                }
            case Constants.GENERATE_XML_FAIL /*-31*/:
                DebugLog.e("DevicePackManager", "-------单次血氧脉率数据--------");
                this._packCount++;
                byte[] _point = {(byte) (pack[2] & 255), (byte) (pack[3] & dp.m), (byte) (pack[4] & 255), (byte) (pack[5] & 255), (byte) (pack[6] & 255), (byte) (pack[7] & 255), (byte) (pack[8] & 255), (byte) (((pack[3] & 64) << 1) | pack[9])};
                Log.e("jxx", "年：" + _point[0] + "月:" + _point[1] + " 日：" + _point[2] + " 时：" + _point[3] + " 分：" + _point[4] + " 秒：" + _point[5]);
                Log.e("jxx", "血氧：" + _point[6] + " 脉率:" + _point[7]);
                this.mSpo2h.getmSpo2PointList().add(_point);
                if ((pack[1] & 64) == 64) {
                    this._packCount = 0;
                    return 67;
                } else if (this._packCount != 10) {
                    return -120;
                } else {
                    this._packCount = 0;
                    return 66;
                }
            case Constants.THREAD_OUT /*-30*/:
                DebugLog.e("DevicePackManager", "-------全天总步数数据返回--------");
                this._packCount++;
                unPackPedometer(pack);
                this.mPedometerJar.addDayPedometerData(new byte[]{pack[2], pack[3], pack[4], pack[6], pack[7], pack[8], pack[9]});
                if ((pack[1] & 64) == 64) {
                    this._packCount = 0;
                    return 83;
                } else if (this._packCount != 10) {
                    return -120;
                } else {
                    this._packCount = 0;
                    return 82;
                }
            case Constants.UPDATE_XML_FAIL /*-29*/:
                DebugLog.e("DevicePackManager", "-------全天每5分钟步数基本信息--------");
                this.mMinData = new MinData();
                byte[] _date = new byte[5];
                _date[0] = pack[1];
                _date[1] = pack[2];
                _date[2] = pack[3];
                _date[3] = pack[4];
                this.mMinData.setmStartDate(_date);
                this.mCount--;
                return 100;
            case Constants.ALL_FILE_ERROR /*-28*/:
                DebugLog.e("DevicePackManager", "-------全天每5分钟步数具体数据--------");
                this._packCount++;
                unECG_Pack(pack);
                for (int i = 0; i < 3; i++) {
                    this.mMinData.getmMinDataList().add(new byte[]{pack[(i * 4) + 4], pack[(i * 4) + 5], pack[(i * 4) + 6], pack[(i * 4) + 7]});
                }
                if ((pack[1] & 64) == 64) {
                    this.mPedometerJar.addMinPedometerData(this.mMinData);
                    if (this.mCount == 0) {
                        _return = 101;
                    } else {
                        _return = 99;
                    }
                    this._packCount = 0;
                    return _return;
                } else if (this._packCount != 10) {
                    return -120;
                } else {
                    this._packCount = 0;
                    return 98;
                }
            case Constants.CREATE_THREAD_ERROR /*-22*/:
                DebugLog.e("DevicePackManager", "-------全天总步数数据返回2--------");
                this._packCount++;
                unPackPedometer2(pack);
                this.mPedometerJar.addDayPedometerData(new byte[]{pack[2], pack[3], pack[4], pack[6], pack[7], pack[8], pack[9], pack[10], pack[11]});
                if ((pack[1] & 64) == 64) {
                    this._packCount = 0;
                    return 83;
                } else if (this._packCount != 10) {
                    return -120;
                } else {
                    this._packCount = 0;
                    return 82;
                }
            case -15:
                if (pack[7] == 48) {
                    if (pack[8] == 48) {
                        setProduct(2);
                    } else if (pack[8] == 49) {
                        setProduct(3);
                    }
                } else if (pack[7] == 49) {
                    if (pack[8] == 48) {
                        setProduct(0);
                    } else if (pack[8] == 49) {
                        setProduct(1);
                    }
                }
                return -15;
            case -14:
                if (Check(pack)) {
                    return 69;
                }
                return 71;
            case -13:
                DebugLog.e("DevicePackManager", "-------校时反馈--------");
                if (!Check(pack) || (pack[1] & 255) != 0) {
                    return CloudChannel.SDK_VERSION;
                }
                return dp.n;
            case -12:
                DebugLog.e("DevicePackManager", "-------设置计步反馈--------");
                if (!Check(pack) || (pack[1] & 255) != 0) {
                    return 33;
                }
                return 32;
            case -11:
                DebugLog.e("DevicePackManager", "-------体重参数设置--------");
                if (!Check(pack) || (pack[1] & 255) != 0) {
                    return 49;
                }
                return 48;
            case -6:
                DebugLog.e("DevicePackManager", "-------身高参数设置--------");
                if (!Check(pack) || (pack[1] & 255) != 0) {
                    return 51;
                }
                return 50;
            case -5:
                if (!Check(pack) || (pack[1] & 255) == 0) {
                    return -112;
                }
                return -111;
            case -4:
                if (!Check(pack) || (pack[1] & 255) != 0) {
                    return 25;
                }
                return 24;
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

    static byte[] unPackPedometer2(byte[] pack) {
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 6; j++) {
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

    public DeviceDataPedometerJar getmPedometerJar() {
        return this.mPedometerJar;
    }

    public DeviceDataSPO2H getmSpo2h() {
        return this.mSpo2h;
    }

    public int getProduct() {
        return this.product;
    }

    public void setProduct(int product2) {
        this.product = product2;
    }
}
