package com.contec.jar.cms50k;

import com.alibaba.cchannel.CloudChannel;
import com.contec.phms.util.Constants;
import java.util.ArrayList;
import u.aly.dp;

public class DevicePackManager {
    int _packCount = 0;
    private boolean bGetPackId = false;
    byte[] curPack = new byte[64];
    int i;
    int k = 0;
    int len = 0;
    int mCount;
    int mCount_ECG;
    byte mDay;
    public DeviceDataECG mEcg;
    public ArrayList<DeviceDataECG> mEcgs = new ArrayList<>();
    byte mHour;
    MinData mMinData;
    byte mMinu;
    byte mMonth;
    int mPackCount;
    public DeviceDataPedometerJar mPedometerJar = new DeviceDataPedometerJar();
    byte mSeco;
    int mSegmentLen;
    public DeviceDataSPO2H mSpo2h = new DeviceDataSPO2H();
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
        byte _return;
        byte _return2;
        byte _return3;
        byte _return4;
        byte _return5;
        byte _return6;
        switch (pack[0]) {
            case -60:
                if (!Check(pack) || pack[5] != 1) {
                    return (byte) 22;
                }
                return (byte) 21;
            case -59:
                if (!Check(pack)) {
                    return (byte) 39;
                }
                if (pack[1] == 1) {
                    return (byte) 37;
                }
                if (pack[1] == 2) {
                    return (byte) 38;
                }
                return (byte) 39;
            case -58:
            case -57:
            case -55:
            case -54:
            case -53:
            case -52:
            case -51:
            case -50:
            case -41:
            case -40:
            case -39:
            case Constants.SESSION_INVALID /* -38 */:
            case Constants.CANCEL_DATA_TRANSF /* -37 */:
            case Constants.GET_DOCTOR_DATA_TYPE_ERROR /* -36 */:
            case Constants.FILE_READ_ERROR /* -35 */:
            case Constants.CREATE_DIR_ERROR /* -34 */:
            case Constants.FILE_WRITE_ERROR /* -33 */:
            case Constants.MALLOC_ERROR /* -23 */:
            case Constants.CREATE_THREAD_ERROR /* -22 */:
            case -21:
            case Constants.DOWNLOAD_FILENAME_TOO_LONG /* -20 */:
            case Constants.DOWNLOAD_PARA_ERROR /* -19 */:
            case Constants.DOWNLOAD_COMPLETE_ERROR /* -18 */:
            case Constants.HOLD_PLACE_ERROR /* -17 */:
            case Constants.GET_LIST_ERROR /* -16 */:
            case -10:
            case -9:
            case -8:
            case -7:
            case -6:
            default:
                return (byte) 0;
            case -56:
                return (byte) 40;
            case -49:
                DebugLog.e("DevicePackManager", "-------数据封装结果--------");
                byte[] code = {pack[1], pack[2], pack[3], pack[4], pack[5], pack[6], pack[7], pack[8]};
                this.mSpo2h.mCodedata = code;
                return (byte) -49;
            case -48:
                DebugLog.e("DevicePackManager", "-------查询连续数据基本信息--------");
                this.mSegmentLen = ((pack[12] & Byte.MAX_VALUE) << 14) | ((pack[11] & Byte.MAX_VALUE) << 7) | (pack[10] & Byte.MAX_VALUE);
                if (this.mSegmentLen == 0) {
                    this.mPackCount = 0;
                    switch (pack[1]) {
                        case 0:
                            return (byte) -79;
                        case 1:
                            return (byte) -77;
                        case 2:
                            return (byte) -75;
                        case 3:
                            return (byte) -71;
                        default:
                            return (byte) 0;
                    }
                } else {
                    this.mPackCount = 0;
                    switch (pack[1]) {
                        case 0:
                            this.mSpo2h.PulseTime[0] = pack[2];
                            this.mSpo2h.PulseTime[1] = pack[3];
                            this.mSpo2h.PulseTime[2] = pack[4];
                            this.mSpo2h.PulseTime[3] = pack[5];
                            this.mSpo2h.PulseTime[4] = pack[6];
                            this.mSpo2h.PulseTime[5] = pack[7];
                            this.mSpo2h.PulseSegment = new byte[this.mSegmentLen];
                            return (byte) -80;
                        case 1:
                            this.mSpo2h.Spo2Time[0] = pack[2];
                            this.mSpo2h.Spo2Time[1] = pack[3];
                            this.mSpo2h.Spo2Time[2] = pack[4];
                            this.mSpo2h.Spo2Time[3] = pack[5];
                            this.mSpo2h.Spo2Time[4] = pack[6];
                            this.mSpo2h.Spo2Time[5] = pack[7];
                            this.mSpo2h.Spo2Segment = new byte[this.mSegmentLen];
                            return (byte) -78;
                        case 2:
                            this.mSpo2h.MovementTime[0] = pack[2];
                            this.mSpo2h.MovementTime[1] = pack[3];
                            this.mSpo2h.MovementTime[2] = pack[4];
                            this.mSpo2h.MovementTime[3] = pack[5];
                            this.mSpo2h.MovementTime[4] = pack[6];
                            this.mSpo2h.MovementTime[5] = pack[7];
                            this.mSpo2h.MovementPoint = new byte[this.mSegmentLen];
                            this.mSpo2h.MovementStart = pack[8];
                            this.mSpo2h.MovementEnd = pack[9];
                            return (byte) -76;
                        case 3:
                            this.mSpo2h.RrTime[0] = pack[2];
                            this.mSpo2h.RrTime[1] = pack[3];
                            this.mSpo2h.RrTime[2] = pack[4];
                            this.mSpo2h.RrTime[3] = pack[5];
                            this.mSpo2h.RrTime[4] = pack[6];
                            this.mSpo2h.RrTime[5] = pack[7];
                            this.mSpo2h.RRPoint = new byte[this.mSegmentLen];
                            return (byte) -72;
                        default:
                            return (byte) 0;
                    }
                }
            case -47:
                if (pack[2] != 0) {
                    return (byte) 0;
                }
                switch (pack[1]) {
                    case 0:
                        return (byte) -64;
                    case 1:
                        return (byte) -63;
                    case 2:
                        return (byte) -62;
                    case 3:
                        return (byte) -61;
                    default:
                        return (byte) 0;
                }
            case -46:
                DebugLog.e("DevicePackManager", "-------脉率值--------" + pack.length);
                byte[] pack2 = unNewPack(pack);
                if (this.mPackCount == this.mSegmentLen / 27) {
                    int lastLen = this.mSegmentLen % 27;
                    this.pluse = pack2[5];
                    this.mSpo2h.PulseSegment[this.mPackCount * 27] = this.pluse;
                    for (int i = 1; i <= lastLen / 2; i++) {
                        byte[] pluseData = unContinuousData(this.pluse, pack2[i + 5]);
                        this.mSpo2h.PulseSegment[((this.mPackCount * 27) + (i * 2)) - 1] = pluseData[0];
                        if ((this.mPackCount * 27) + (i * 2) < this.mSegmentLen) {
                            this.mSpo2h.PulseSegment[(this.mPackCount * 27) + (i * 2)] = pluseData[1];
                        }
                        this.pluse = pluseData[1];
                    }
                    _return4 = -125;
                } else {
                    this.pluse = pack2[5];
                    this.mSpo2h.PulseSegment[this.mPackCount * 27] = this.pluse;
                    for (int i2 = 1; i2 < 14; i2++) {
                        byte[] pluseData2 = unContinuousData(this.pluse, pack2[i2 + 5]);
                        this.mSpo2h.PulseSegment[((this.mPackCount * 27) + (i2 * 2)) - 1] = pluseData2[0];
                        this.mSpo2h.PulseSegment[(this.mPackCount * 27) + (i2 * 2)] = pluseData2[1];
                        this.pluse = pluseData2[1];
                    }
                    _return4 = -126;
                }
                this.mPackCount++;
                if (this.mPackCount == this.mSegmentLen / 27 && this.mSegmentLen % 27 == 0) {
                    return (byte) -125;
                }
                return _return4;
            case -45:
                DebugLog.e("DevicePackManager", "-------血氧--------");
                byte[] pack3 = unNewPack(pack);
                if (this.mPackCount == this.mSegmentLen / 27) {
                    DebugLog.e("cms50k", "连续数据包结束");
                    int lastLen2 = this.mSegmentLen % 27;
                    this.spo2 = pack3[5];
                    if ((this.spo2 & 255) == 255) {
                        this.spo2 = Byte.MAX_VALUE;
                    }
                    this.mSpo2h.Spo2Segment[this.mPackCount * 27] = this.spo2;
                    for (int i3 = 1; i3 <= lastLen2 / 2; i3++) {
                        byte[] spo2Data = unContinuousData(this.spo2, pack3[i3 + 5]);
                        if ((spo2Data[0] & 255) == 255) {
                            spo2Data[0] = Byte.MAX_VALUE;
                        }
                        if ((spo2Data[1] & 255) == 255) {
                            spo2Data[1] = Byte.MAX_VALUE;
                        }
                        this.mSpo2h.Spo2Segment[((this.mPackCount * 27) + (i3 * 2)) - 1] = spo2Data[0];
                        if ((this.mPackCount * 27) + (i3 * 2) < this.mSegmentLen) {
                            this.mSpo2h.Spo2Segment[(this.mPackCount * 27) + (i3 * 2)] = spo2Data[1];
                        }
                        this.spo2 = spo2Data[1];
                    }
                    _return3 = -109;
                } else {
                    this.spo2 = pack3[5];
                    if ((this.spo2 & 255) == 255) {
                        this.spo2 = Byte.MAX_VALUE;
                    }
                    this.mSpo2h.Spo2Segment[this.mPackCount * 27] = this.spo2;
                    for (int i4 = 1; i4 < 14; i4++) {
                        byte[] spo2Data2 = unContinuousData(this.spo2, pack3[i4 + 5]);
                        if ((spo2Data2[0] & 255) == 255) {
                            spo2Data2[0] = Byte.MAX_VALUE;
                        }
                        if ((spo2Data2[1] & 255) == 255) {
                            spo2Data2[1] = Byte.MAX_VALUE;
                        }
                        this.mSpo2h.Spo2Segment[((this.mPackCount * 27) + (i4 * 2)) - 1] = spo2Data2[0];
                        this.mSpo2h.Spo2Segment[(this.mPackCount * 27) + (i4 * 2)] = spo2Data2[1];
                        this.spo2 = spo2Data2[1];
                    }
                    _return3 = -110;
                }
                this.mPackCount++;
                if (this.mPackCount == this.mSegmentLen / 27 && this.mSegmentLen % 27 == 0) {
                    return (byte) -109;
                }
                return _return3;
            case -44:
                DebugLog.e("DevicePackManager", "-------体动--------");
                unPack(pack);
                if (this.mPackCount == this.mSegmentLen / 12) {
                    int _len = this.mSpo2h.MovementPoint.length - (this.mPackCount * 12);
                    for (int i5 = 0; i5 < _len; i5++) {
                        this.mSpo2h.MovementPoint[(this.mPackCount * 12) + i5] = pack[i5 + 5];
                    }
                    _return2 = -74;
                    this.mPackCount = 0;
                } else {
                    this.mSpo2h.MovementPoint[this.mPackCount * 12] = pack[5];
                    this.mSpo2h.MovementPoint[(this.mPackCount * 12) + 1] = pack[6];
                    this.mSpo2h.MovementPoint[(this.mPackCount * 12) + 2] = pack[7];
                    this.mSpo2h.MovementPoint[(this.mPackCount * 12) + 3] = pack[8];
                    this.mSpo2h.MovementPoint[(this.mPackCount * 12) + 4] = pack[9];
                    this.mSpo2h.MovementPoint[(this.mPackCount * 12) + 5] = pack[10];
                    this.mSpo2h.MovementPoint[(this.mPackCount * 12) + 6] = pack[11];
                    this.mSpo2h.MovementPoint[(this.mPackCount * 12) + 7] = pack[12];
                    this.mSpo2h.MovementPoint[(this.mPackCount * 12) + 8] = pack[13];
                    this.mSpo2h.MovementPoint[(this.mPackCount * 12) + 9] = pack[14];
                    this.mSpo2h.MovementPoint[(this.mPackCount * 12) + 10] = pack[15];
                    this.mSpo2h.MovementPoint[(this.mPackCount * 12) + 11] = pack[16];
                    _return2 = -73;
                }
                this.mPackCount++;
                if (!(this.mPackCount == this.mSegmentLen / 12 && this.mSegmentLen % 12 == 0)) {
                    return _return2;
                }
                this.mPackCount = 0;
                return (byte) -74;
            case -43:
                DebugLog.e("DevicePackManager", "-------连续五分钟RR间期数据的基本信息--------");
                this.mYear = pack[1];
                this.mMonth = pack[2];
                this.mDay = pack[3];
                this.mHour = pack[4];
                this.mMinu = pack[5];
                this.mSeco = pack[6];
                this.mMinData = new MinData();
                byte[] _RRdate = new byte[6];
                _RRdate[0] = pack[1];
                _RRdate[0] = pack[2];
                _RRdate[0] = pack[3];
                _RRdate[0] = pack[4];
                _RRdate[0] = pack[5];
                _RRdate[0] = pack[6];
                _RRdate[0] = 0;
                _RRdate[0] = 0;
                this.mMinData.mStartDate = _RRdate;
                this.mCount--;
                return (byte) -56;
            case -42:
                DebugLog.e("DevicePackManager", "-------连续RR间数据--------");
                unPack(pack);
                this._packCount++;
                if ((pack[1] & 64) == 64) {
                    DebugLog.e("cms50k", "数据包结束");
                    int _len2 = this.mSpo2h.PulseSegment.length - (this.mPackCount * 12);
                    for (int i6 = 0; i6 < _len2; i6++) {
                        this.mSpo2h.PulseSegment[(this.mPackCount * 12) + i6] = pack[i6 + 5];
                    }
                    _return = -62;
                    this._packCount = 0;
                } else {
                    this.mSpo2h.RRPoint[(this.mPackCount * 12) + 0] = pack[5];
                    this.mSpo2h.RRPoint[(this.mPackCount * 12) + 1] = pack[6];
                    this.mSpo2h.RRPoint[(this.mPackCount * 12) + 2] = pack[7];
                    this.mSpo2h.RRPoint[(this.mPackCount * 12) + 3] = pack[8];
                    this.mSpo2h.RRPoint[(this.mPackCount * 12) + 4] = pack[9];
                    this.mSpo2h.RRPoint[(this.mPackCount * 12) + 5] = pack[10];
                    this.mSpo2h.RRPoint[(this.mPackCount * 12) + 6] = pack[11];
                    this.mSpo2h.RRPoint[(this.mPackCount * 12) + 7] = pack[12];
                    this.mSpo2h.RRPoint[(this.mPackCount * 12) + 8] = pack[13];
                    this.mSpo2h.RRPoint[(this.mPackCount * 12) + 9] = pack[14];
                    this.mSpo2h.RRPoint[(this.mPackCount * 12) + 10] = pack[15];
                    this.mSpo2h.RRPoint[(this.mPackCount * 12) + 11] = pack[16];
                    _return = -61;
                }
                this.mPackCount++;
                return _return;
            case Constants.UPDATEXML_UPLOAD_FAIL /* -32 */:
                PrintBytes.printDatai(pack, pack.length);
                DebugLog.e("DevicePackManager", "-------数据存储空间--------");
                this.mCount = ((pack[5] << 7) | (pack[4] & 255)) & 65535;
                switch (pack[1]) {
                    case 0:
                        DebugLog.d("DevicePackManager", "单次血氧数据个数：" + this.mCount);
                        if (this.mCount == 0) {
                            return (byte) 65;
                        }
                        return (byte) 64;
                    case 1:
                        DebugLog.d("DevicePackManager", "全天计步器个数：" + this.mCount);
                        if (this.mCount == 0) {
                            return (byte) 81;
                        }
                        return (byte) 80;
                    case 2:
                        DebugLog.d("DevicePackManager", "5分钟计步器个数：" + this.mCount);
                        if (this.mCount == 0) {
                            return (byte) 97;
                        }
                        return (byte) 96;
                    case 3:
                        DebugLog.d("DevicePackManager", "心电数据个数：" + this.mCount);
                        if (this.mCount == 0) {
                            return (byte) 113;
                        }
                        return (byte) 112;
                    case 4:
                        if (this.mCount == 0) {
                            return (byte) -127;
                        }
                        return Byte.MIN_VALUE;
                    default:
                        return (byte) 0;
                }
            case Constants.GENERATE_XML_FAIL /* -31 */:
                DebugLog.e("DevicePackManager", "-------单次血氧脉率数据--------");
                this._packCount++;
                byte[] _point = {(byte) (pack[2] & 255), (byte) (pack[3] & dp.m), (byte) (pack[4] & 255), (byte) (pack[5] & 255), (byte) (pack[6] & 255), (byte) (pack[7] & 255), (byte) (pack[8] & 255), (byte) (((pack[3] & 64) << 1) | pack[9])};
                this.mSpo2h.Spo2Point.add(_point);
                if ((pack[1] & 64) == 64) {
                    this._packCount = 0;
                    return (byte) 67;
                } else if (this._packCount != 10) {
                    return (byte) -120;
                } else {
                    this._packCount = 0;
                    return (byte) 66;
                }
            case Constants.THREAD_OUT /* -30 */:
                DebugLog.e("DevicePackManager", "-------全天总步数数据返回--------");
                this._packCount++;
                unPackPedometer(pack);
                byte[] _data = {pack[2], pack[3], pack[4], pack[6], pack[7], pack[8], pack[9]};
                this.mPedometerJar.addDayPedometerData(_data);
                if ((pack[1] & 64) == 64) {
                    this._packCount = 0;
                    return (byte) 83;
                } else if (this._packCount != 10) {
                    return (byte) -120;
                } else {
                    this._packCount = 0;
                    return (byte) 82;
                }
            case Constants.UPDATE_XML_FAIL /* -29 */:
                DebugLog.e("DevicePackManager", "-------全天每5分钟步数基本信息--------");
                this.mYear = pack[1];
                this.mMonth = pack[2];
                this.mDay = pack[3];
                this.mHour = pack[4];
                this.mMinData = new MinData();
                byte[] _date = {pack[1], pack[2], pack[3], pack[4], 24};
                this.mMinData.mStartDate = _date;
                this.mCount--;
                return (byte) 100;
            case Constants.ALL_FILE_ERROR /* -28 */:
                DebugLog.e("DevicePackManager", "-------全天每5分钟步数具体数据--------");
                this._packCount++;
                unECG_Pack(pack);
                for (int i7 = 0; i7 < 3; i7++) {
                    byte[] _dataMin = {pack[(i7 * 4) + 4], pack[(i7 * 4) + 5], pack[(i7 * 4) + 6], pack[(i7 * 4) + 7]};
                    this.mMinData.mMinDataList.add(_dataMin);
                }
                if ((pack[1] & 64) == 64) {
                    this.minDatas.add(this.mMinData);
                    if (this.mCount == 0) {
                        _return6 = 101;
                    } else {
                        _return6 = 99;
                    }
                    this._packCount = 0;
                    return _return6;
                } else if (this._packCount != 10) {
                    return (byte) -120;
                } else {
                    this._packCount = 0;
                    return (byte) 98;
                }
            case Constants.NO_COMPLETE /* -27 */:
                DebugLog.e("DevicePackManager", "-------心电图片段基本信息--------");
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
                DebugLog.i("info", new StringBuilder().append(_dataLen).toString());
                this.mEcg.ECG_Data = new byte[_dataLen * 2];
                this.mCount--;
                this.mCount_ECG = 0;
                return (byte) 117;
            case Constants.LAST_CHECKOUT_FAIL /* -26 */:
                PrintBytes.printData(pack, pack.length);
                DebugLog.e("DevicePackManager", "-------心电片段--------");
                unECG_Pack(pack);
                this._packCount++;
                if ((pack[1] & 64) == 64) {
                    int _len3 = this.mEcg.ECG_Data.length - (this.mCount_ECG * 12);
                    for (int i8 = 0; i8 < _len3; i8++) {
                        this.mEcg.ECG_Data[(this.mCount_ECG * 12) + i8] = pack[i8 + 4];
                    }
                    this.mEcgs.add(this.mEcg);
                    this._packCount = 0;
                    if (this.mCount == 0) {
                        _return5 = 116;
                    } else {
                        _return5 = 115;
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
                    this.mEcg.ECG_Data[(this.mCount_ECG * 12) + 9] = pack[13];
                    this.mEcg.ECG_Data[(this.mCount_ECG * 12) + 10] = pack[14];
                    this.mEcg.ECG_Data[(this.mCount_ECG * 12) + 11] = pack[15];
                    if (this._packCount == 10) {
                        this._packCount = 0;
                        _return5 = 114;
                    } else {
                        _return5 = -120;
                    }
                }
                this.mCount_ECG++;
                return _return5;
            case Constants.NET_INIT_FAIL /* -25 */:
                DebugLog.e("DevicePackManager", "-------脉搏波基本信息--------");
                return (byte) 0;
            case Constants.UNITE_FILE_ERROR /* -24 */:
                DebugLog.e("DevicePackManager", "-------脉搏波数据信息--------");
                return (byte) 0;
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
                return (byte) -15;
            case -14:
                if (Check(pack)) {
                    return (byte) 69;
                }
                return (byte) 71;
            case -13:
                DebugLog.e("DevicePackManager", "-------校时反馈--------");
                if (!Check(pack)) {
                    return CloudChannel.SDK_VERSION;
                }
                if ((pack[1] & 255) == 0) {
                    return dp.n;
                }
                return CloudChannel.SDK_VERSION;
            case -12:
                DebugLog.e("DevicePackManager", "-------设置计步反馈--------");
                if (!Check(pack) || (pack[1] & 255) != 0) {
                    return (byte) 33;
                }
                return (byte) 32;
            case -11:
                DebugLog.e("DevicePackManager", "-------体重参数设置--------");
                if (!Check(pack) || (pack[1] & 255) != 0) {
                    return (byte) 49;
                }
                return (byte) 48;
            case -5:
                if (!Check(pack) || (pack[1] & 255) == 0) {
                    return (byte) -112;
                }
                return (byte) -111;
            case -4:
                if (!Check(pack) || (pack[1] & 255) != 0) {
                    return (byte) 25;
                }
                return (byte) 24;
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
