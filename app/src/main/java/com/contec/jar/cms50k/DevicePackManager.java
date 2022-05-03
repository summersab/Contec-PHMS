package com.contec.jar.cms50k;

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

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v193, resolved type: java.lang.Object[]} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v303, resolved type: java.lang.Object[]} */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public byte processData(byte[] r18) {
        /*
            r17 = this;
            r8 = 0
            r14 = 0
            byte r14 = r18[r14]
            switch(r14) {
                case -60: goto L_0x0008;
                case -59: goto L_0x001d;
                case -58: goto L_0x0007;
                case -57: goto L_0x0007;
                case -56: goto L_0x003b;
                case -55: goto L_0x0007;
                case -54: goto L_0x0007;
                case -53: goto L_0x0007;
                case -52: goto L_0x0007;
                case -51: goto L_0x0007;
                case -50: goto L_0x0007;
                case -49: goto L_0x0eb8;
                case -48: goto L_0x06e4;
                case -47: goto L_0x08c3;
                case -46: goto L_0x08e0;
                case -45: goto L_0x0a0e;
                case -44: goto L_0x0b8a;
                case -43: goto L_0x0ce8;
                case -42: goto L_0x0d65;
                case -41: goto L_0x0007;
                case -40: goto L_0x0007;
                case -39: goto L_0x0007;
                case -38: goto L_0x0007;
                case -37: goto L_0x0007;
                case -36: goto L_0x0007;
                case -35: goto L_0x0007;
                case -34: goto L_0x0007;
                case -33: goto L_0x0007;
                case -32: goto L_0x00d6;
                case -31: goto L_0x01ae;
                case -30: goto L_0x0244;
                case -29: goto L_0x02b5;
                case -28: goto L_0x0315;
                case -27: goto L_0x039b;
                case -26: goto L_0x0559;
                case -25: goto L_0x06d2;
                case -24: goto L_0x06db;
                case -23: goto L_0x0007;
                case -22: goto L_0x0007;
                case -21: goto L_0x0007;
                case -20: goto L_0x0007;
                case -19: goto L_0x0007;
                case -18: goto L_0x0007;
                case -17: goto L_0x0007;
                case -16: goto L_0x0007;
                case -15: goto L_0x0efe;
                case -14: goto L_0x003e;
                case -13: goto L_0x004a;
                case -12: goto L_0x0067;
                case -11: goto L_0x0084;
                case -10: goto L_0x0007;
                case -9: goto L_0x0007;
                case -8: goto L_0x0007;
                case -7: goto L_0x0007;
                case -6: goto L_0x0007;
                case -5: goto L_0x00a4;
                case -4: goto L_0x00bd;
                default: goto L_0x0007;
            }
        L_0x0007:
            return r8
        L_0x0008:
            boolean r14 = r17.Check(r18)
            if (r14 == 0) goto L_0x001a
            r14 = 5
            byte r14 = r18[r14]
            r15 = 1
            if (r14 != r15) goto L_0x0017
            r8 = 21
            goto L_0x0007
        L_0x0017:
            r8 = 22
            goto L_0x0007
        L_0x001a:
            r8 = 22
            goto L_0x0007
        L_0x001d:
            boolean r14 = r17.Check(r18)
            if (r14 == 0) goto L_0x0038
            r14 = 1
            byte r14 = r18[r14]
            r15 = 1
            if (r14 != r15) goto L_0x002c
            r8 = 37
            goto L_0x0007
        L_0x002c:
            r14 = 1
            byte r14 = r18[r14]
            r15 = 2
            if (r14 != r15) goto L_0x0035
            r8 = 38
            goto L_0x0007
        L_0x0035:
            r8 = 39
            goto L_0x0007
        L_0x0038:
            r8 = 39
            goto L_0x0007
        L_0x003b:
            r8 = 40
            goto L_0x0007
        L_0x003e:
            boolean r14 = r17.Check(r18)
            if (r14 == 0) goto L_0x0047
            r8 = 69
            goto L_0x0007
        L_0x0047:
            r8 = 71
            goto L_0x0007
        L_0x004a:
            java.lang.String r14 = "DevicePackManager"
            java.lang.String r15 = "-------校时反馈--------"
            com.contec.jar.cms50k.DebugLog.e(r14, r15)
            boolean r14 = r17.Check(r18)
            if (r14 == 0) goto L_0x0064
            r14 = 1
            byte r14 = r18[r14]
            r14 = r14 & 255(0xff, float:3.57E-43)
            if (r14 != 0) goto L_0x0061
            r8 = 16
            goto L_0x0007
        L_0x0061:
            r8 = 17
            goto L_0x0007
        L_0x0064:
            r8 = 17
            goto L_0x0007
        L_0x0067:
            java.lang.String r14 = "DevicePackManager"
            java.lang.String r15 = "-------设置计步反馈--------"
            com.contec.jar.cms50k.DebugLog.e(r14, r15)
            boolean r14 = r17.Check(r18)
            if (r14 == 0) goto L_0x0081
            r14 = 1
            byte r14 = r18[r14]
            r14 = r14 & 255(0xff, float:3.57E-43)
            if (r14 != 0) goto L_0x007e
            r8 = 32
            goto L_0x0007
        L_0x007e:
            r8 = 33
            goto L_0x0007
        L_0x0081:
            r8 = 33
            goto L_0x0007
        L_0x0084:
            java.lang.String r14 = "DevicePackManager"
            java.lang.String r15 = "-------体重参数设置--------"
            com.contec.jar.cms50k.DebugLog.e(r14, r15)
            boolean r14 = r17.Check(r18)
            if (r14 == 0) goto L_0x00a0
            r14 = 1
            byte r14 = r18[r14]
            r14 = r14 & 255(0xff, float:3.57E-43)
            if (r14 != 0) goto L_0x009c
            r8 = 48
            goto L_0x0007
        L_0x009c:
            r8 = 49
            goto L_0x0007
        L_0x00a0:
            r8 = 49
            goto L_0x0007
        L_0x00a4:
            boolean r14 = r17.Check(r18)
            if (r14 == 0) goto L_0x00b9
            r14 = 1
            byte r14 = r18[r14]
            r14 = r14 & 255(0xff, float:3.57E-43)
            if (r14 != 0) goto L_0x00b5
            r8 = -112(0xffffffffffffff90, float:NaN)
            goto L_0x0007
        L_0x00b5:
            r8 = -111(0xffffffffffffff91, float:NaN)
            goto L_0x0007
        L_0x00b9:
            r8 = -112(0xffffffffffffff90, float:NaN)
            goto L_0x0007
        L_0x00bd:
            boolean r14 = r17.Check(r18)
            if (r14 == 0) goto L_0x00d2
            r14 = 1
            byte r14 = r18[r14]
            r14 = r14 & 255(0xff, float:3.57E-43)
            if (r14 != 0) goto L_0x00ce
            r8 = 24
            goto L_0x0007
        L_0x00ce:
            r8 = 25
            goto L_0x0007
        L_0x00d2:
            r8 = 25
            goto L_0x0007
        L_0x00d6:
            r0 = r18
            int r14 = r0.length
            r0 = r18
            com.contec.jar.cms50k.PrintBytes.printDatai(r0, r14)
            java.lang.String r14 = "DevicePackManager"
            java.lang.String r15 = "-------数据存储空间--------"
            com.contec.jar.cms50k.DebugLog.e(r14, r15)
            r14 = 5
            byte r14 = r18[r14]
            int r14 = r14 << 7
            r15 = 4
            byte r15 = r18[r15]
            r15 = r15 & 255(0xff, float:3.57E-43)
            r14 = r14 | r15
            r15 = 65535(0xffff, float:9.1834E-41)
            r14 = r14 & r15
            r0 = r17
            r0.mCount = r14
            r14 = 1
            byte r14 = r18[r14]
            switch(r14) {
                case 0: goto L_0x0100;
                case 1: goto L_0x0128;
                case 2: goto L_0x0150;
                case 3: goto L_0x0178;
                case 4: goto L_0x01a0;
                default: goto L_0x00fe;
            }
        L_0x00fe:
            goto L_0x0007
        L_0x0100:
            java.lang.String r14 = "DevicePackManager"
            java.lang.StringBuilder r15 = new java.lang.StringBuilder
            java.lang.String r16 = "单次血氧数据个数："
            r15.<init>(r16)
            r0 = r17
            int r0 = r0.mCount
            r16 = r0
            java.lang.StringBuilder r15 = r15.append(r16)
            java.lang.String r15 = r15.toString()
            com.contec.jar.cms50k.DebugLog.d(r14, r15)
            r0 = r17
            int r14 = r0.mCount
            if (r14 != 0) goto L_0x0124
            r8 = 65
            goto L_0x0007
        L_0x0124:
            r8 = 64
            goto L_0x0007
        L_0x0128:
            java.lang.String r14 = "DevicePackManager"
            java.lang.StringBuilder r15 = new java.lang.StringBuilder
            java.lang.String r16 = "全天计步器个数："
            r15.<init>(r16)
            r0 = r17
            int r0 = r0.mCount
            r16 = r0
            java.lang.StringBuilder r15 = r15.append(r16)
            java.lang.String r15 = r15.toString()
            com.contec.jar.cms50k.DebugLog.d(r14, r15)
            r0 = r17
            int r14 = r0.mCount
            if (r14 != 0) goto L_0x014c
            r8 = 81
            goto L_0x0007
        L_0x014c:
            r8 = 80
            goto L_0x0007
        L_0x0150:
            java.lang.String r14 = "DevicePackManager"
            java.lang.StringBuilder r15 = new java.lang.StringBuilder
            java.lang.String r16 = "5分钟计步器个数："
            r15.<init>(r16)
            r0 = r17
            int r0 = r0.mCount
            r16 = r0
            java.lang.StringBuilder r15 = r15.append(r16)
            java.lang.String r15 = r15.toString()
            com.contec.jar.cms50k.DebugLog.d(r14, r15)
            r0 = r17
            int r14 = r0.mCount
            if (r14 != 0) goto L_0x0174
            r8 = 97
            goto L_0x0007
        L_0x0174:
            r8 = 96
            goto L_0x0007
        L_0x0178:
            java.lang.String r14 = "DevicePackManager"
            java.lang.StringBuilder r15 = new java.lang.StringBuilder
            java.lang.String r16 = "心电数据个数："
            r15.<init>(r16)
            r0 = r17
            int r0 = r0.mCount
            r16 = r0
            java.lang.StringBuilder r15 = r15.append(r16)
            java.lang.String r15 = r15.toString()
            com.contec.jar.cms50k.DebugLog.d(r14, r15)
            r0 = r17
            int r14 = r0.mCount
            if (r14 != 0) goto L_0x019c
            r8 = 113(0x71, float:1.58E-43)
            goto L_0x0007
        L_0x019c:
            r8 = 112(0x70, float:1.57E-43)
            goto L_0x0007
        L_0x01a0:
            r0 = r17
            int r14 = r0.mCount
            if (r14 != 0) goto L_0x01aa
            r8 = -127(0xffffffffffffff81, float:NaN)
            goto L_0x0007
        L_0x01aa:
            r8 = -128(0xffffffffffffff80, float:NaN)
            goto L_0x0007
        L_0x01ae:
            java.lang.String r14 = "DevicePackManager"
            java.lang.String r15 = "-------单次血氧脉率数据--------"
            com.contec.jar.cms50k.DebugLog.e(r14, r15)
            r0 = r17
            int r14 = r0._packCount
            int r14 = r14 + 1
            r0 = r17
            r0._packCount = r14
            r14 = 8
            byte[] r7 = new byte[r14]
            r14 = 0
            r15 = 2
            byte r15 = r18[r15]
            r15 = r15 & 255(0xff, float:3.57E-43)
            byte r15 = (byte) r15
            r7[r14] = r15
            r14 = 1
            r15 = 3
            byte r15 = r18[r15]
            r15 = r15 & 15
            byte r15 = (byte) r15
            r7[r14] = r15
            r14 = 2
            r15 = 4
            byte r15 = r18[r15]
            r15 = r15 & 255(0xff, float:3.57E-43)
            byte r15 = (byte) r15
            r7[r14] = r15
            r14 = 3
            r15 = 5
            byte r15 = r18[r15]
            r15 = r15 & 255(0xff, float:3.57E-43)
            byte r15 = (byte) r15
            r7[r14] = r15
            r14 = 4
            r15 = 6
            byte r15 = r18[r15]
            r15 = r15 & 255(0xff, float:3.57E-43)
            byte r15 = (byte) r15
            r7[r14] = r15
            r14 = 5
            r15 = 7
            byte r15 = r18[r15]
            r15 = r15 & 255(0xff, float:3.57E-43)
            byte r15 = (byte) r15
            r7[r14] = r15
            r14 = 6
            r15 = 8
            byte r15 = r18[r15]
            r15 = r15 & 255(0xff, float:3.57E-43)
            byte r15 = (byte) r15
            r7[r14] = r15
            r14 = 7
            r15 = 3
            byte r15 = r18[r15]
            r15 = r15 & 64
            int r15 = r15 << 1
            r16 = 9
            byte r16 = r18[r16]
            r15 = r15 | r16
            byte r15 = (byte) r15
            r7[r14] = r15
            r0 = r17
            com.contec.jar.cms50k.DeviceDataSPO2H r14 = r0.mSpo2h
            java.util.ArrayList<java.lang.Object> r14 = r14.Spo2Point
            r14.add(r7)
            r14 = 1
            byte r14 = r18[r14]
            r14 = r14 & 64
            r15 = 64
            if (r14 != r15) goto L_0x022f
            r8 = 67
            r14 = 0
            r0 = r17
            r0._packCount = r14
            goto L_0x0007
        L_0x022f:
            r0 = r17
            int r14 = r0._packCount
            r15 = 10
            if (r14 != r15) goto L_0x0240
            r8 = 66
            r14 = 0
            r0 = r17
            r0._packCount = r14
            goto L_0x0007
        L_0x0240:
            r8 = -120(0xffffffffffffff88, float:NaN)
            goto L_0x0007
        L_0x0244:
            java.lang.String r14 = "DevicePackManager"
            java.lang.String r15 = "-------全天总步数数据返回--------"
            com.contec.jar.cms50k.DebugLog.e(r14, r15)
            r0 = r17
            int r14 = r0._packCount
            int r14 = r14 + 1
            r0 = r17
            r0._packCount = r14
            unPackPedometer(r18)
            r14 = 7
            byte[] r2 = new byte[r14]
            r14 = 0
            r15 = 2
            byte r15 = r18[r15]
            r2[r14] = r15
            r14 = 1
            r15 = 3
            byte r15 = r18[r15]
            r2[r14] = r15
            r14 = 2
            r15 = 4
            byte r15 = r18[r15]
            r2[r14] = r15
            r14 = 3
            r15 = 6
            byte r15 = r18[r15]
            r2[r14] = r15
            r14 = 4
            r15 = 7
            byte r15 = r18[r15]
            r2[r14] = r15
            r14 = 5
            r15 = 8
            byte r15 = r18[r15]
            r2[r14] = r15
            r14 = 6
            r15 = 9
            byte r15 = r18[r15]
            r2[r14] = r15
            r0 = r17
            com.contec.jar.cms50k.DeviceDataPedometerJar r14 = r0.mPedometerJar
            r14.addDayPedometerData(r2)
            r14 = 1
            byte r14 = r18[r14]
            r14 = r14 & 64
            r15 = 64
            if (r14 != r15) goto L_0x02a0
            r8 = 83
            r14 = 0
            r0 = r17
            r0._packCount = r14
            goto L_0x0007
        L_0x02a0:
            r0 = r17
            int r14 = r0._packCount
            r15 = 10
            if (r14 != r15) goto L_0x02b1
            r14 = 0
            r0 = r17
            r0._packCount = r14
            r8 = 82
            goto L_0x0007
        L_0x02b1:
            r8 = -120(0xffffffffffffff88, float:NaN)
            goto L_0x0007
        L_0x02b5:
            java.lang.String r14 = "DevicePackManager"
            java.lang.String r15 = "-------全天每5分钟步数基本信息--------"
            com.contec.jar.cms50k.DebugLog.e(r14, r15)
            r8 = 100
            r14 = 1
            byte r14 = r18[r14]
            r0 = r17
            r0.mYear = r14
            r14 = 2
            byte r14 = r18[r14]
            r0 = r17
            r0.mMonth = r14
            r14 = 3
            byte r14 = r18[r14]
            r0 = r17
            r0.mDay = r14
            r14 = 4
            byte r14 = r18[r14]
            r0 = r17
            r0.mHour = r14
            com.contec.jar.cms50k.MinData r14 = new com.contec.jar.cms50k.MinData
            r14.<init>()
            r0 = r17
            r0.mMinData = r14
            r14 = 5
            byte[] r5 = new byte[r14]
            r14 = 0
            r15 = 1
            byte r15 = r18[r15]
            r5[r14] = r15
            r14 = 1
            r15 = 2
            byte r15 = r18[r15]
            r5[r14] = r15
            r14 = 2
            r15 = 3
            byte r15 = r18[r15]
            r5[r14] = r15
            r14 = 3
            r15 = 4
            byte r15 = r18[r15]
            r5[r14] = r15
            r14 = 4
            r15 = 24
            r5[r14] = r15
            r0 = r17
            com.contec.jar.cms50k.MinData r14 = r0.mMinData
            r14.mStartDate = r5
            r0 = r17
            int r14 = r0.mCount
            int r14 = r14 + -1
            r0 = r17
            r0.mCount = r14
            goto L_0x0007
        L_0x0315:
            java.lang.String r14 = "DevicePackManager"
            java.lang.String r15 = "-------全天每5分钟步数具体数据--------"
            com.contec.jar.cms50k.DebugLog.e(r14, r15)
            r0 = r17
            int r14 = r0._packCount
            int r14 = r14 + 1
            r0 = r17
            r0._packCount = r14
            unECG_Pack(r18)
            r10 = 0
        L_0x032a:
            r14 = 3
            if (r10 < r14) goto L_0x0350
            r14 = 1
            byte r14 = r18[r14]
            r14 = r14 & 64
            r15 = 64
            if (r14 != r15) goto L_0x0386
            r0 = r17
            java.util.ArrayList<java.lang.Object> r14 = r0.minDatas
            r0 = r17
            com.contec.jar.cms50k.MinData r15 = r0.mMinData
            r14.add(r15)
            r0 = r17
            int r14 = r0.mCount
            if (r14 != 0) goto L_0x0383
            r8 = 101(0x65, float:1.42E-43)
        L_0x0349:
            r14 = 0
            r0 = r17
            r0._packCount = r14
            goto L_0x0007
        L_0x0350:
            r14 = 4
            byte[] r4 = new byte[r14]
            r14 = 0
            int r15 = r10 * 4
            int r15 = r15 + 4
            byte r15 = r18[r15]
            r4[r14] = r15
            r14 = 1
            int r15 = r10 * 4
            int r15 = r15 + 5
            byte r15 = r18[r15]
            r4[r14] = r15
            r14 = 2
            int r15 = r10 * 4
            int r15 = r15 + 6
            byte r15 = r18[r15]
            r4[r14] = r15
            r14 = 3
            int r15 = r10 * 4
            int r15 = r15 + 7
            byte r15 = r18[r15]
            r4[r14] = r15
            r0 = r17
            com.contec.jar.cms50k.MinData r14 = r0.mMinData
            java.util.List<byte[]> r14 = r14.mMinDataList
            r14.add(r4)
            int r10 = r10 + 1
            goto L_0x032a
        L_0x0383:
            r8 = 99
            goto L_0x0349
        L_0x0386:
            r0 = r17
            int r14 = r0._packCount
            r15 = 10
            if (r14 != r15) goto L_0x0397
            r14 = 0
            r0 = r17
            r0._packCount = r14
            r8 = 98
            goto L_0x0007
        L_0x0397:
            r8 = -120(0xffffffffffffff88, float:NaN)
            goto L_0x0007
        L_0x039b:
            java.lang.String r14 = "DevicePackManager"
            java.lang.String r15 = "-------心电图片段基本信息--------"
            com.contec.jar.cms50k.DebugLog.e(r14, r15)
            r14 = 0
            r0 = r17
            r0._packCount = r14
            r8 = 117(0x75, float:1.64E-43)
            com.contec.jar.cms50k.DeviceDataECG r14 = new com.contec.jar.cms50k.DeviceDataECG
            r14.<init>()
            r0 = r17
            r0.mEcg = r14
            r0 = r17
            com.contec.jar.cms50k.DeviceDataECG r14 = r0.mEcg
            r15 = 22
            byte[] r15 = new byte[r15]
            r14.Point_data = r15
            r0 = r17
            com.contec.jar.cms50k.DeviceDataECG r14 = r0.mEcg
            byte[] r14 = r14.Point_data
            r15 = 0
            r16 = 1
            byte r16 = r18[r16]
            r14[r15] = r16
            r0 = r17
            com.contec.jar.cms50k.DeviceDataECG r14 = r0.mEcg
            byte[] r14 = r14.Point_data
            r15 = 1
            r16 = 2
            byte r16 = r18[r16]
            r14[r15] = r16
            r0 = r17
            com.contec.jar.cms50k.DeviceDataECG r14 = r0.mEcg
            byte[] r14 = r14.Point_data
            r15 = 2
            r16 = 3
            byte r16 = r18[r16]
            r14[r15] = r16
            r0 = r17
            com.contec.jar.cms50k.DeviceDataECG r14 = r0.mEcg
            byte[] r14 = r14.Point_data
            r15 = 3
            r16 = 4
            byte r16 = r18[r16]
            r14[r15] = r16
            r0 = r17
            com.contec.jar.cms50k.DeviceDataECG r14 = r0.mEcg
            byte[] r14 = r14.Point_data
            r15 = 4
            r16 = 5
            byte r16 = r18[r16]
            r14[r15] = r16
            r0 = r17
            com.contec.jar.cms50k.DeviceDataECG r14 = r0.mEcg
            byte[] r14 = r14.Point_data
            r15 = 5
            r16 = 6
            byte r16 = r18[r16]
            r14[r15] = r16
            r0 = r17
            com.contec.jar.cms50k.DeviceDataECG r14 = r0.mEcg
            byte[] r14 = r14.Point_data
            r15 = 6
            r16 = 8
            byte r16 = r18[r16]
            r14[r15] = r16
            r0 = r17
            com.contec.jar.cms50k.DeviceDataECG r14 = r0.mEcg
            byte[] r14 = r14.Point_data
            r15 = 7
            r16 = 7
            byte r16 = r18[r16]
            r14[r15] = r16
            r0 = r17
            com.contec.jar.cms50k.DeviceDataECG r14 = r0.mEcg
            byte[] r14 = r14.Point_data
            r15 = 9
            byte r15 = r18[r15]
            r0 = r17
            r0.dealData(r14, r15)
            r0 = r17
            com.contec.jar.cms50k.DeviceDataECG r14 = r0.mEcg
            byte[] r14 = r14.Point_data
            r15 = 10
            byte r15 = r18[r15]
            r0 = r17
            r0.dealData(r14, r15)
            r0 = r17
            com.contec.jar.cms50k.DeviceDataECG r14 = r0.mEcg
            byte[] r14 = r14.Point_data
            r15 = 11
            byte r15 = r18[r15]
            r0 = r17
            r0.dealData(r14, r15)
            r0 = r17
            com.contec.jar.cms50k.DeviceDataECG r14 = r0.mEcg
            byte[] r14 = r14.Point_data
            r15 = 12
            byte r15 = r18[r15]
            r0 = r17
            r0.dealData(r14, r15)
            r0 = r17
            com.contec.jar.cms50k.DeviceDataECG r14 = r0.mEcg
            byte[] r14 = r14.Point_data
            r15 = 13
            byte r15 = r18[r15]
            r0 = r17
            r0.dealData(r14, r15)
            r0 = r17
            com.contec.jar.cms50k.DeviceDataECG r14 = r0.mEcg
            byte[] r14 = r14.Point_data
            r15 = 14
            byte r15 = r18[r15]
            r0 = r17
            r0.dealData(r14, r15)
            r0 = r17
            com.contec.jar.cms50k.DeviceDataECG r14 = r0.mEcg
            byte[] r14 = r14.Point_data
            r15 = 9
            byte r14 = r14[r15]
            r0 = r17
            com.contec.jar.cms50k.DeviceDataECG r15 = r0.mEcg
            byte[] r15 = r15.Point_data
            r16 = 10
            byte r15 = r15[r16]
            r14 = r14 | r15
            r0 = r17
            com.contec.jar.cms50k.DeviceDataECG r15 = r0.mEcg
            byte[] r15 = r15.Point_data
            r16 = 11
            byte r15 = r15[r16]
            r14 = r14 | r15
            r0 = r17
            com.contec.jar.cms50k.DeviceDataECG r15 = r0.mEcg
            byte[] r15 = r15.Point_data
            r16 = 12
            byte r15 = r15[r16]
            r14 = r14 | r15
            r0 = r17
            com.contec.jar.cms50k.DeviceDataECG r15 = r0.mEcg
            byte[] r15 = r15.Point_data
            r16 = 13
            byte r15 = r15[r16]
            r14 = r14 | r15
            r0 = r17
            com.contec.jar.cms50k.DeviceDataECG r15 = r0.mEcg
            byte[] r15 = r15.Point_data
            r16 = 14
            byte r15 = r15[r16]
            r14 = r14 | r15
            r0 = r17
            com.contec.jar.cms50k.DeviceDataECG r15 = r0.mEcg
            byte[] r15 = r15.Point_data
            r16 = 15
            byte r15 = r15[r16]
            r14 = r14 | r15
            r0 = r17
            com.contec.jar.cms50k.DeviceDataECG r15 = r0.mEcg
            byte[] r15 = r15.Point_data
            r16 = 16
            byte r15 = r15[r16]
            r14 = r14 | r15
            r0 = r17
            com.contec.jar.cms50k.DeviceDataECG r15 = r0.mEcg
            byte[] r15 = r15.Point_data
            r16 = 17
            byte r15 = r15[r16]
            r14 = r14 | r15
            r0 = r17
            com.contec.jar.cms50k.DeviceDataECG r15 = r0.mEcg
            byte[] r15 = r15.Point_data
            r16 = 18
            byte r15 = r15[r16]
            r14 = r14 | r15
            r0 = r17
            com.contec.jar.cms50k.DeviceDataECG r15 = r0.mEcg
            byte[] r15 = r15.Point_data
            r16 = 19
            byte r15 = r15[r16]
            r14 = r14 | r15
            r0 = r17
            com.contec.jar.cms50k.DeviceDataECG r15 = r0.mEcg
            byte[] r15 = r15.Point_data
            r16 = 20
            byte r15 = r15[r16]
            r14 = r14 | r15
            r0 = r17
            com.contec.jar.cms50k.DeviceDataECG r15 = r0.mEcg
            byte[] r15 = r15.Point_data
            r16 = 21
            byte r15 = r15[r16]
            r14 = r14 | r15
            if (r14 <= 0) goto L_0x051a
            r0 = r17
            com.contec.jar.cms50k.DeviceDataECG r14 = r0.mEcg
            byte[] r14 = r14.Point_data
            r15 = 8
            r16 = 0
            r14[r15] = r16
        L_0x051a:
            r14 = 16
            byte r14 = r18[r14]
            int r14 = r14 << 7
            r15 = 15
            byte r15 = r18[r15]
            r15 = r15 & 255(0xff, float:3.57E-43)
            r14 = r14 | r15
            r15 = 65535(0xffff, float:9.1834E-41)
            r3 = r14 & r15
            java.lang.String r14 = "info"
            java.lang.StringBuilder r15 = new java.lang.StringBuilder
            r15.<init>()
            java.lang.StringBuilder r15 = r15.append(r3)
            java.lang.String r15 = r15.toString()
            com.contec.jar.cms50k.DebugLog.i(r14, r15)
            r0 = r17
            com.contec.jar.cms50k.DeviceDataECG r14 = r0.mEcg
            int r15 = r3 * 2
            byte[] r15 = new byte[r15]
            r14.ECG_Data = r15
            r0 = r17
            int r14 = r0.mCount
            int r14 = r14 + -1
            r0 = r17
            r0.mCount = r14
            r14 = 0
            r0 = r17
            r0.mCount_ECG = r14
            goto L_0x0007
        L_0x0559:
            r0 = r18
            int r14 = r0.length
            r0 = r18
            com.contec.jar.cms50k.PrintBytes.printData(r0, r14)
            java.lang.String r14 = "DevicePackManager"
            java.lang.String r15 = "-------心电片段--------"
            com.contec.jar.cms50k.DebugLog.e(r14, r15)
            unECG_Pack(r18)
            r0 = r17
            int r14 = r0._packCount
            int r14 = r14 + 1
            r0 = r17
            r0._packCount = r14
            r14 = 1
            byte r14 = r18[r14]
            r14 = r14 & 64
            r15 = 64
            if (r14 != r15) goto L_0x05cd
            r0 = r17
            com.contec.jar.cms50k.DeviceDataECG r14 = r0.mEcg
            byte[] r14 = r14.ECG_Data
            int r14 = r14.length
            r0 = r17
            int r15 = r0.mCount_ECG
            int r15 = r15 * 12
            int r6 = r14 - r15
            r10 = 0
        L_0x058e:
            if (r10 < r6) goto L_0x05b4
            r0 = r17
            java.util.ArrayList<com.contec.jar.cms50k.DeviceDataECG> r14 = r0.mEcgs
            r0 = r17
            com.contec.jar.cms50k.DeviceDataECG r15 = r0.mEcg
            r14.add(r15)
            r14 = 0
            r0 = r17
            r0._packCount = r14
            r0 = r17
            int r14 = r0.mCount
            if (r14 != 0) goto L_0x05ca
            r8 = 116(0x74, float:1.63E-43)
        L_0x05a8:
            r0 = r17
            int r14 = r0.mCount_ECG
            int r14 = r14 + 1
            r0 = r17
            r0.mCount_ECG = r14
            goto L_0x0007
        L_0x05b4:
            r0 = r17
            com.contec.jar.cms50k.DeviceDataECG r14 = r0.mEcg
            byte[] r14 = r14.ECG_Data
            r0 = r17
            int r15 = r0.mCount_ECG
            int r15 = r15 * 12
            int r15 = r15 + r10
            int r16 = r10 + 4
            byte r16 = r18[r16]
            r14[r15] = r16
            int r10 = r10 + 1
            goto L_0x058e
        L_0x05ca:
            r8 = 115(0x73, float:1.61E-43)
            goto L_0x05a8
        L_0x05cd:
            r0 = r17
            com.contec.jar.cms50k.DeviceDataECG r14 = r0.mEcg
            byte[] r14 = r14.ECG_Data
            r0 = r17
            int r15 = r0.mCount_ECG
            int r15 = r15 * 12
            int r15 = r15 + 0
            r16 = 4
            byte r16 = r18[r16]
            r14[r15] = r16
            r0 = r17
            com.contec.jar.cms50k.DeviceDataECG r14 = r0.mEcg
            byte[] r14 = r14.ECG_Data
            r0 = r17
            int r15 = r0.mCount_ECG
            int r15 = r15 * 12
            int r15 = r15 + 1
            r16 = 5
            byte r16 = r18[r16]
            r14[r15] = r16
            r0 = r17
            com.contec.jar.cms50k.DeviceDataECG r14 = r0.mEcg
            byte[] r14 = r14.ECG_Data
            r0 = r17
            int r15 = r0.mCount_ECG
            int r15 = r15 * 12
            int r15 = r15 + 2
            r16 = 6
            byte r16 = r18[r16]
            r14[r15] = r16
            r0 = r17
            com.contec.jar.cms50k.DeviceDataECG r14 = r0.mEcg
            byte[] r14 = r14.ECG_Data
            r0 = r17
            int r15 = r0.mCount_ECG
            int r15 = r15 * 12
            int r15 = r15 + 3
            r16 = 7
            byte r16 = r18[r16]
            r14[r15] = r16
            r0 = r17
            com.contec.jar.cms50k.DeviceDataECG r14 = r0.mEcg
            byte[] r14 = r14.ECG_Data
            r0 = r17
            int r15 = r0.mCount_ECG
            int r15 = r15 * 12
            int r15 = r15 + 4
            r16 = 8
            byte r16 = r18[r16]
            r14[r15] = r16
            r0 = r17
            com.contec.jar.cms50k.DeviceDataECG r14 = r0.mEcg
            byte[] r14 = r14.ECG_Data
            r0 = r17
            int r15 = r0.mCount_ECG
            int r15 = r15 * 12
            int r15 = r15 + 5
            r16 = 9
            byte r16 = r18[r16]
            r14[r15] = r16
            r0 = r17
            com.contec.jar.cms50k.DeviceDataECG r14 = r0.mEcg
            byte[] r14 = r14.ECG_Data
            r0 = r17
            int r15 = r0.mCount_ECG
            int r15 = r15 * 12
            int r15 = r15 + 6
            r16 = 10
            byte r16 = r18[r16]
            r14[r15] = r16
            r0 = r17
            com.contec.jar.cms50k.DeviceDataECG r14 = r0.mEcg
            byte[] r14 = r14.ECG_Data
            r0 = r17
            int r15 = r0.mCount_ECG
            int r15 = r15 * 12
            int r15 = r15 + 7
            r16 = 11
            byte r16 = r18[r16]
            r14[r15] = r16
            r0 = r17
            com.contec.jar.cms50k.DeviceDataECG r14 = r0.mEcg
            byte[] r14 = r14.ECG_Data
            r0 = r17
            int r15 = r0.mCount_ECG
            int r15 = r15 * 12
            int r15 = r15 + 8
            r16 = 12
            byte r16 = r18[r16]
            r14[r15] = r16
            r0 = r17
            com.contec.jar.cms50k.DeviceDataECG r14 = r0.mEcg
            byte[] r14 = r14.ECG_Data
            r0 = r17
            int r15 = r0.mCount_ECG
            int r15 = r15 * 12
            int r15 = r15 + 9
            r16 = 13
            byte r16 = r18[r16]
            r14[r15] = r16
            r0 = r17
            com.contec.jar.cms50k.DeviceDataECG r14 = r0.mEcg
            byte[] r14 = r14.ECG_Data
            r0 = r17
            int r15 = r0.mCount_ECG
            int r15 = r15 * 12
            int r15 = r15 + 10
            r16 = 14
            byte r16 = r18[r16]
            r14[r15] = r16
            r0 = r17
            com.contec.jar.cms50k.DeviceDataECG r14 = r0.mEcg
            byte[] r14 = r14.ECG_Data
            r0 = r17
            int r15 = r0.mCount_ECG
            int r15 = r15 * 12
            int r15 = r15 + 11
            r16 = 15
            byte r16 = r18[r16]
            r14[r15] = r16
            r0 = r17
            int r14 = r0._packCount
            r15 = 10
            if (r14 != r15) goto L_0x06ce
            r14 = 0
            r0 = r17
            r0._packCount = r14
            r8 = 114(0x72, float:1.6E-43)
            goto L_0x05a8
        L_0x06ce:
            r8 = -120(0xffffffffffffff88, float:NaN)
            goto L_0x05a8
        L_0x06d2:
            java.lang.String r14 = "DevicePackManager"
            java.lang.String r15 = "-------脉搏波基本信息--------"
            com.contec.jar.cms50k.DebugLog.e(r14, r15)
            goto L_0x0007
        L_0x06db:
            java.lang.String r14 = "DevicePackManager"
            java.lang.String r15 = "-------脉搏波数据信息--------"
            com.contec.jar.cms50k.DebugLog.e(r14, r15)
            goto L_0x0007
        L_0x06e4:
            java.lang.String r14 = "DevicePackManager"
            java.lang.String r15 = "-------查询连续数据基本信息--------"
            com.contec.jar.cms50k.DebugLog.e(r14, r15)
            r14 = 12
            byte r14 = r18[r14]
            r14 = r14 & 127(0x7f, float:1.78E-43)
            int r14 = r14 << 14
            r15 = 11
            byte r15 = r18[r15]
            r15 = r15 & 127(0x7f, float:1.78E-43)
            int r15 = r15 << 7
            r14 = r14 | r15
            r15 = 10
            byte r15 = r18[r15]
            r15 = r15 & 127(0x7f, float:1.78E-43)
            r14 = r14 | r15
            r0 = r17
            r0.mSegmentLen = r14
            r0 = r17
            int r14 = r0.mSegmentLen
            if (r14 != 0) goto L_0x072a
            r14 = 0
            r0 = r17
            r0.mPackCount = r14
            r14 = 1
            byte r14 = r18[r14]
            switch(r14) {
                case 0: goto L_0x071a;
                case 1: goto L_0x071e;
                case 2: goto L_0x0722;
                case 3: goto L_0x0726;
                default: goto L_0x0718;
            }
        L_0x0718:
            goto L_0x0007
        L_0x071a:
            r8 = -79
            goto L_0x0007
        L_0x071e:
            r8 = -77
            goto L_0x0007
        L_0x0722:
            r8 = -75
            goto L_0x0007
        L_0x0726:
            r8 = -71
            goto L_0x0007
        L_0x072a:
            r14 = 0
            r0 = r17
            r0.mPackCount = r14
            r14 = 1
            byte r14 = r18[r14]
            switch(r14) {
                case 0: goto L_0x0737;
                case 1: goto L_0x0795;
                case 2: goto L_0x07f3;
                case 3: goto L_0x0865;
                default: goto L_0x0735;
            }
        L_0x0735:
            goto L_0x0007
        L_0x0737:
            r8 = -80
            r0 = r17
            com.contec.jar.cms50k.DeviceDataSPO2H r14 = r0.mSpo2h
            int[] r14 = r14.PulseTime
            r15 = 0
            r16 = 2
            byte r16 = r18[r16]
            r14[r15] = r16
            r0 = r17
            com.contec.jar.cms50k.DeviceDataSPO2H r14 = r0.mSpo2h
            int[] r14 = r14.PulseTime
            r15 = 1
            r16 = 3
            byte r16 = r18[r16]
            r14[r15] = r16
            r0 = r17
            com.contec.jar.cms50k.DeviceDataSPO2H r14 = r0.mSpo2h
            int[] r14 = r14.PulseTime
            r15 = 2
            r16 = 4
            byte r16 = r18[r16]
            r14[r15] = r16
            r0 = r17
            com.contec.jar.cms50k.DeviceDataSPO2H r14 = r0.mSpo2h
            int[] r14 = r14.PulseTime
            r15 = 3
            r16 = 5
            byte r16 = r18[r16]
            r14[r15] = r16
            r0 = r17
            com.contec.jar.cms50k.DeviceDataSPO2H r14 = r0.mSpo2h
            int[] r14 = r14.PulseTime
            r15 = 4
            r16 = 6
            byte r16 = r18[r16]
            r14[r15] = r16
            r0 = r17
            com.contec.jar.cms50k.DeviceDataSPO2H r14 = r0.mSpo2h
            int[] r14 = r14.PulseTime
            r15 = 5
            r16 = 7
            byte r16 = r18[r16]
            r14[r15] = r16
            r0 = r17
            com.contec.jar.cms50k.DeviceDataSPO2H r14 = r0.mSpo2h
            r0 = r17
            int r15 = r0.mSegmentLen
            byte[] r15 = new byte[r15]
            r14.PulseSegment = r15
            goto L_0x0007
        L_0x0795:
            r8 = -78
            r0 = r17
            com.contec.jar.cms50k.DeviceDataSPO2H r14 = r0.mSpo2h
            int[] r14 = r14.Spo2Time
            r15 = 0
            r16 = 2
            byte r16 = r18[r16]
            r14[r15] = r16
            r0 = r17
            com.contec.jar.cms50k.DeviceDataSPO2H r14 = r0.mSpo2h
            int[] r14 = r14.Spo2Time
            r15 = 1
            r16 = 3
            byte r16 = r18[r16]
            r14[r15] = r16
            r0 = r17
            com.contec.jar.cms50k.DeviceDataSPO2H r14 = r0.mSpo2h
            int[] r14 = r14.Spo2Time
            r15 = 2
            r16 = 4
            byte r16 = r18[r16]
            r14[r15] = r16
            r0 = r17
            com.contec.jar.cms50k.DeviceDataSPO2H r14 = r0.mSpo2h
            int[] r14 = r14.Spo2Time
            r15 = 3
            r16 = 5
            byte r16 = r18[r16]
            r14[r15] = r16
            r0 = r17
            com.contec.jar.cms50k.DeviceDataSPO2H r14 = r0.mSpo2h
            int[] r14 = r14.Spo2Time
            r15 = 4
            r16 = 6
            byte r16 = r18[r16]
            r14[r15] = r16
            r0 = r17
            com.contec.jar.cms50k.DeviceDataSPO2H r14 = r0.mSpo2h
            int[] r14 = r14.Spo2Time
            r15 = 5
            r16 = 7
            byte r16 = r18[r16]
            r14[r15] = r16
            r0 = r17
            com.contec.jar.cms50k.DeviceDataSPO2H r14 = r0.mSpo2h
            r0 = r17
            int r15 = r0.mSegmentLen
            byte[] r15 = new byte[r15]
            r14.Spo2Segment = r15
            goto L_0x0007
        L_0x07f3:
            r8 = -76
            r0 = r17
            com.contec.jar.cms50k.DeviceDataSPO2H r14 = r0.mSpo2h
            int[] r14 = r14.MovementTime
            r15 = 0
            r16 = 2
            byte r16 = r18[r16]
            r14[r15] = r16
            r0 = r17
            com.contec.jar.cms50k.DeviceDataSPO2H r14 = r0.mSpo2h
            int[] r14 = r14.MovementTime
            r15 = 1
            r16 = 3
            byte r16 = r18[r16]
            r14[r15] = r16
            r0 = r17
            com.contec.jar.cms50k.DeviceDataSPO2H r14 = r0.mSpo2h
            int[] r14 = r14.MovementTime
            r15 = 2
            r16 = 4
            byte r16 = r18[r16]
            r14[r15] = r16
            r0 = r17
            com.contec.jar.cms50k.DeviceDataSPO2H r14 = r0.mSpo2h
            int[] r14 = r14.MovementTime
            r15 = 3
            r16 = 5
            byte r16 = r18[r16]
            r14[r15] = r16
            r0 = r17
            com.contec.jar.cms50k.DeviceDataSPO2H r14 = r0.mSpo2h
            int[] r14 = r14.MovementTime
            r15 = 4
            r16 = 6
            byte r16 = r18[r16]
            r14[r15] = r16
            r0 = r17
            com.contec.jar.cms50k.DeviceDataSPO2H r14 = r0.mSpo2h
            int[] r14 = r14.MovementTime
            r15 = 5
            r16 = 7
            byte r16 = r18[r16]
            r14[r15] = r16
            r0 = r17
            com.contec.jar.cms50k.DeviceDataSPO2H r14 = r0.mSpo2h
            r0 = r17
            int r15 = r0.mSegmentLen
            byte[] r15 = new byte[r15]
            r14.MovementPoint = r15
            r0 = r17
            com.contec.jar.cms50k.DeviceDataSPO2H r14 = r0.mSpo2h
            r15 = 8
            byte r15 = r18[r15]
            r14.MovementStart = r15
            r0 = r17
            com.contec.jar.cms50k.DeviceDataSPO2H r14 = r0.mSpo2h
            r15 = 9
            byte r15 = r18[r15]
            r14.MovementEnd = r15
            goto L_0x0007
        L_0x0865:
            r8 = -72
            r0 = r17
            com.contec.jar.cms50k.DeviceDataSPO2H r14 = r0.mSpo2h
            int[] r14 = r14.RrTime
            r15 = 0
            r16 = 2
            byte r16 = r18[r16]
            r14[r15] = r16
            r0 = r17
            com.contec.jar.cms50k.DeviceDataSPO2H r14 = r0.mSpo2h
            int[] r14 = r14.RrTime
            r15 = 1
            r16 = 3
            byte r16 = r18[r16]
            r14[r15] = r16
            r0 = r17
            com.contec.jar.cms50k.DeviceDataSPO2H r14 = r0.mSpo2h
            int[] r14 = r14.RrTime
            r15 = 2
            r16 = 4
            byte r16 = r18[r16]
            r14[r15] = r16
            r0 = r17
            com.contec.jar.cms50k.DeviceDataSPO2H r14 = r0.mSpo2h
            int[] r14 = r14.RrTime
            r15 = 3
            r16 = 5
            byte r16 = r18[r16]
            r14[r15] = r16
            r0 = r17
            com.contec.jar.cms50k.DeviceDataSPO2H r14 = r0.mSpo2h
            int[] r14 = r14.RrTime
            r15 = 4
            r16 = 6
            byte r16 = r18[r16]
            r14[r15] = r16
            r0 = r17
            com.contec.jar.cms50k.DeviceDataSPO2H r14 = r0.mSpo2h
            int[] r14 = r14.RrTime
            r15 = 5
            r16 = 7
            byte r16 = r18[r16]
            r14[r15] = r16
            r0 = r17
            com.contec.jar.cms50k.DeviceDataSPO2H r14 = r0.mSpo2h
            r0 = r17
            int r15 = r0.mSegmentLen
            byte[] r15 = new byte[r15]
            r14.RRPoint = r15
            goto L_0x0007
        L_0x08c3:
            r14 = 2
            byte r14 = r18[r14]
            if (r14 != 0) goto L_0x0007
            r14 = 1
            byte r14 = r18[r14]
            switch(r14) {
                case 0: goto L_0x08d0;
                case 1: goto L_0x08d4;
                case 2: goto L_0x08d8;
                case 3: goto L_0x08dc;
                default: goto L_0x08ce;
            }
        L_0x08ce:
            goto L_0x0007
        L_0x08d0:
            r8 = -64
            goto L_0x0007
        L_0x08d4:
            r8 = -63
            goto L_0x0007
        L_0x08d8:
            r8 = -62
            goto L_0x0007
        L_0x08dc:
            r8 = -61
            goto L_0x0007
        L_0x08e0:
            java.lang.String r14 = "DevicePackManager"
            java.lang.StringBuilder r15 = new java.lang.StringBuilder
            java.lang.String r16 = "-------脉率值--------"
            r15.<init>(r16)
            r0 = r18
            int r0 = r0.length
            r16 = r0
            java.lang.StringBuilder r15 = r15.append(r16)
            java.lang.String r15 = r15.toString()
            com.contec.jar.cms50k.DebugLog.e(r14, r15)
            byte[] r18 = unNewPack(r18)
            r0 = r17
            int r14 = r0.mPackCount
            r0 = r17
            int r15 = r0.mSegmentLen
            int r15 = r15 / 27
            if (r14 != r15) goto L_0x09a6
            r0 = r17
            int r14 = r0.mSegmentLen
            int r11 = r14 % 27
            r14 = 5
            byte r14 = r18[r14]
            r0 = r17
            r0.pluse = r14
            r0 = r17
            com.contec.jar.cms50k.DeviceDataSPO2H r14 = r0.mSpo2h
            byte[] r14 = r14.PulseSegment
            r0 = r17
            int r15 = r0.mPackCount
            int r15 = r15 * 27
            r0 = r17
            byte r0 = r0.pluse
            r16 = r0
            r14[r15] = r16
            r10 = 1
        L_0x092b:
            int r14 = r11 / 2
            if (r10 <= r14) goto L_0x0953
            r8 = -125(0xffffffffffffff83, float:NaN)
        L_0x0931:
            r0 = r17
            int r14 = r0.mPackCount
            int r14 = r14 + 1
            r0 = r17
            r0.mPackCount = r14
            r0 = r17
            int r14 = r0.mPackCount
            r0 = r17
            int r15 = r0.mSegmentLen
            int r15 = r15 / 27
            if (r14 != r15) goto L_0x0007
            r0 = r17
            int r14 = r0.mSegmentLen
            int r14 = r14 % 27
            if (r14 != 0) goto L_0x0007
            r8 = -125(0xffffffffffffff83, float:NaN)
            goto L_0x0007
        L_0x0953:
            r0 = r17
            byte r14 = r0.pluse
            int r15 = r10 + 5
            byte r15 = r18[r15]
            byte[] r12 = unContinuousData(r14, r15)
            r0 = r17
            com.contec.jar.cms50k.DeviceDataSPO2H r14 = r0.mSpo2h
            byte[] r14 = r14.PulseSegment
            r0 = r17
            int r15 = r0.mPackCount
            int r15 = r15 * 27
            int r16 = r10 * 2
            int r15 = r15 + r16
            int r15 = r15 + -1
            r16 = 0
            byte r16 = r12[r16]
            r14[r15] = r16
            r0 = r17
            int r14 = r0.mPackCount
            int r14 = r14 * 27
            int r15 = r10 * 2
            int r14 = r14 + r15
            r0 = r17
            int r15 = r0.mSegmentLen
            if (r14 >= r15) goto L_0x099c
            r0 = r17
            com.contec.jar.cms50k.DeviceDataSPO2H r14 = r0.mSpo2h
            byte[] r14 = r14.PulseSegment
            r0 = r17
            int r15 = r0.mPackCount
            int r15 = r15 * 27
            int r16 = r10 * 2
            int r15 = r15 + r16
            r16 = 1
            byte r16 = r12[r16]
            r14[r15] = r16
        L_0x099c:
            r14 = 1
            byte r14 = r12[r14]
            r0 = r17
            r0.pluse = r14
            int r10 = r10 + 1
            goto L_0x092b
        L_0x09a6:
            r14 = 5
            byte r14 = r18[r14]
            r0 = r17
            r0.pluse = r14
            r0 = r17
            com.contec.jar.cms50k.DeviceDataSPO2H r14 = r0.mSpo2h
            byte[] r14 = r14.PulseSegment
            r0 = r17
            int r15 = r0.mPackCount
            int r15 = r15 * 27
            r0 = r17
            byte r0 = r0.pluse
            r16 = r0
            r14[r15] = r16
            r10 = 1
        L_0x09c2:
            r14 = 14
            if (r10 < r14) goto L_0x09ca
            r8 = -126(0xffffffffffffff82, float:NaN)
            goto L_0x0931
        L_0x09ca:
            r0 = r17
            byte r14 = r0.pluse
            int r15 = r10 + 5
            byte r15 = r18[r15]
            byte[] r12 = unContinuousData(r14, r15)
            r0 = r17
            com.contec.jar.cms50k.DeviceDataSPO2H r14 = r0.mSpo2h
            byte[] r14 = r14.PulseSegment
            r0 = r17
            int r15 = r0.mPackCount
            int r15 = r15 * 27
            int r16 = r10 * 2
            int r15 = r15 + r16
            int r15 = r15 + -1
            r16 = 0
            byte r16 = r12[r16]
            r14[r15] = r16
            r0 = r17
            com.contec.jar.cms50k.DeviceDataSPO2H r14 = r0.mSpo2h
            byte[] r14 = r14.PulseSegment
            r0 = r17
            int r15 = r0.mPackCount
            int r15 = r15 * 27
            int r16 = r10 * 2
            int r15 = r15 + r16
            r16 = 1
            byte r16 = r12[r16]
            r14[r15] = r16
            r14 = 1
            byte r14 = r12[r14]
            r0 = r17
            r0.pluse = r14
            int r10 = r10 + 1
            goto L_0x09c2
        L_0x0a0e:
            java.lang.String r14 = "DevicePackManager"
            java.lang.String r15 = "-------血氧--------"
            com.contec.jar.cms50k.DebugLog.e(r14, r15)
            byte[] r18 = unNewPack(r18)
            r0 = r17
            int r14 = r0.mPackCount
            r0 = r17
            int r15 = r0.mSegmentLen
            int r15 = r15 / 27
            if (r14 != r15) goto L_0x0af6
            java.lang.String r14 = "cms50k"
            java.lang.String r15 = "连续数据包结束"
            com.contec.jar.cms50k.DebugLog.e(r14, r15)
            r0 = r17
            int r14 = r0.mSegmentLen
            int r11 = r14 % 27
            r14 = 5
            byte r14 = r18[r14]
            r0 = r17
            r0.spo2 = r14
            r0 = r17
            byte r14 = r0.spo2
            r14 = r14 & 255(0xff, float:3.57E-43)
            r15 = 255(0xff, float:3.57E-43)
            if (r14 != r15) goto L_0x0a49
            r14 = 127(0x7f, float:1.78E-43)
            r0 = r17
            r0.spo2 = r14
        L_0x0a49:
            r0 = r17
            com.contec.jar.cms50k.DeviceDataSPO2H r14 = r0.mSpo2h
            byte[] r14 = r14.Spo2Segment
            r0 = r17
            int r15 = r0.mPackCount
            int r15 = r15 * 27
            r0 = r17
            byte r0 = r0.spo2
            r16 = r0
            r14[r15] = r16
            r10 = 1
        L_0x0a5e:
            int r14 = r11 / 2
            if (r10 <= r14) goto L_0x0a86
            r8 = -109(0xffffffffffffff93, float:NaN)
        L_0x0a64:
            r0 = r17
            int r14 = r0.mPackCount
            int r14 = r14 + 1
            r0 = r17
            r0.mPackCount = r14
            r0 = r17
            int r14 = r0.mPackCount
            r0 = r17
            int r15 = r0.mSegmentLen
            int r15 = r15 / 27
            if (r14 != r15) goto L_0x0007
            r0 = r17
            int r14 = r0.mSegmentLen
            int r14 = r14 % 27
            if (r14 != 0) goto L_0x0007
            r8 = -109(0xffffffffffffff93, float:NaN)
            goto L_0x0007
        L_0x0a86:
            r0 = r17
            byte r14 = r0.spo2
            int r15 = r10 + 5
            byte r15 = r18[r15]
            byte[] r13 = unContinuousData(r14, r15)
            r14 = 0
            byte r14 = r13[r14]
            r14 = r14 & 255(0xff, float:3.57E-43)
            r15 = 255(0xff, float:3.57E-43)
            if (r14 != r15) goto L_0x0aa0
            r14 = 0
            r15 = 127(0x7f, float:1.78E-43)
            r13[r14] = r15
        L_0x0aa0:
            r14 = 1
            byte r14 = r13[r14]
            r14 = r14 & 255(0xff, float:3.57E-43)
            r15 = 255(0xff, float:3.57E-43)
            if (r14 != r15) goto L_0x0aae
            r14 = 1
            r15 = 127(0x7f, float:1.78E-43)
            r13[r14] = r15
        L_0x0aae:
            r0 = r17
            com.contec.jar.cms50k.DeviceDataSPO2H r14 = r0.mSpo2h
            byte[] r14 = r14.Spo2Segment
            r0 = r17
            int r15 = r0.mPackCount
            int r15 = r15 * 27
            int r16 = r10 * 2
            int r15 = r15 + r16
            int r15 = r15 + -1
            r16 = 0
            byte r16 = r13[r16]
            r14[r15] = r16
            r0 = r17
            int r14 = r0.mPackCount
            int r14 = r14 * 27
            int r15 = r10 * 2
            int r14 = r14 + r15
            r0 = r17
            int r15 = r0.mSegmentLen
            if (r14 >= r15) goto L_0x0aeb
            r0 = r17
            com.contec.jar.cms50k.DeviceDataSPO2H r14 = r0.mSpo2h
            byte[] r14 = r14.Spo2Segment
            r0 = r17
            int r15 = r0.mPackCount
            int r15 = r15 * 27
            int r16 = r10 * 2
            int r15 = r15 + r16
            r16 = 1
            byte r16 = r13[r16]
            r14[r15] = r16
        L_0x0aeb:
            r14 = 1
            byte r14 = r13[r14]
            r0 = r17
            r0.spo2 = r14
            int r10 = r10 + 1
            goto L_0x0a5e
        L_0x0af6:
            r14 = 5
            byte r14 = r18[r14]
            r0 = r17
            r0.spo2 = r14
            r0 = r17
            byte r14 = r0.spo2
            r14 = r14 & 255(0xff, float:3.57E-43)
            r15 = 255(0xff, float:3.57E-43)
            if (r14 != r15) goto L_0x0b0d
            r14 = 127(0x7f, float:1.78E-43)
            r0 = r17
            r0.spo2 = r14
        L_0x0b0d:
            r0 = r17
            com.contec.jar.cms50k.DeviceDataSPO2H r14 = r0.mSpo2h
            byte[] r14 = r14.Spo2Segment
            r0 = r17
            int r15 = r0.mPackCount
            int r15 = r15 * 27
            r0 = r17
            byte r0 = r0.spo2
            r16 = r0
            r14[r15] = r16
            r10 = 1
        L_0x0b22:
            r14 = 14
            if (r10 < r14) goto L_0x0b2a
            r8 = -110(0xffffffffffffff92, float:NaN)
            goto L_0x0a64
        L_0x0b2a:
            r0 = r17
            byte r14 = r0.spo2
            int r15 = r10 + 5
            byte r15 = r18[r15]
            byte[] r13 = unContinuousData(r14, r15)
            r14 = 0
            byte r14 = r13[r14]
            r14 = r14 & 255(0xff, float:3.57E-43)
            r15 = 255(0xff, float:3.57E-43)
            if (r14 != r15) goto L_0x0b44
            r14 = 0
            r15 = 127(0x7f, float:1.78E-43)
            r13[r14] = r15
        L_0x0b44:
            r14 = 1
            byte r14 = r13[r14]
            r14 = r14 & 255(0xff, float:3.57E-43)
            r15 = 255(0xff, float:3.57E-43)
            if (r14 != r15) goto L_0x0b52
            r14 = 1
            r15 = 127(0x7f, float:1.78E-43)
            r13[r14] = r15
        L_0x0b52:
            r0 = r17
            com.contec.jar.cms50k.DeviceDataSPO2H r14 = r0.mSpo2h
            byte[] r14 = r14.Spo2Segment
            r0 = r17
            int r15 = r0.mPackCount
            int r15 = r15 * 27
            int r16 = r10 * 2
            int r15 = r15 + r16
            int r15 = r15 + -1
            r16 = 0
            byte r16 = r13[r16]
            r14[r15] = r16
            r0 = r17
            com.contec.jar.cms50k.DeviceDataSPO2H r14 = r0.mSpo2h
            byte[] r14 = r14.Spo2Segment
            r0 = r17
            int r15 = r0.mPackCount
            int r15 = r15 * 27
            int r16 = r10 * 2
            int r15 = r15 + r16
            r16 = 1
            byte r16 = r13[r16]
            r14[r15] = r16
            r14 = 1
            byte r14 = r13[r14]
            r0 = r17
            r0.spo2 = r14
            int r10 = r10 + 1
            goto L_0x0b22
        L_0x0b8a:
            java.lang.String r14 = "DevicePackManager"
            java.lang.String r15 = "-------体动--------"
            com.contec.jar.cms50k.DebugLog.e(r14, r15)
            unPack(r18)
            r0 = r17
            int r14 = r0.mPackCount
            r0 = r17
            int r15 = r0.mSegmentLen
            int r15 = r15 / 12
            if (r14 != r15) goto L_0x0bf6
            r0 = r17
            com.contec.jar.cms50k.DeviceDataSPO2H r14 = r0.mSpo2h
            byte[] r14 = r14.MovementPoint
            int r14 = r14.length
            r0 = r17
            int r15 = r0.mPackCount
            int r15 = r15 * 12
            int r6 = r14 - r15
            r10 = 0
        L_0x0bb0:
            if (r10 < r6) goto L_0x0be0
            r8 = -74
            r14 = 0
            r0 = r17
            r0.mPackCount = r14
        L_0x0bb9:
            r0 = r17
            int r14 = r0.mPackCount
            int r14 = r14 + 1
            r0 = r17
            r0.mPackCount = r14
            r0 = r17
            int r14 = r0.mPackCount
            r0 = r17
            int r15 = r0.mSegmentLen
            int r15 = r15 / 12
            if (r14 != r15) goto L_0x0007
            r0 = r17
            int r14 = r0.mSegmentLen
            int r14 = r14 % 12
            if (r14 != 0) goto L_0x0007
            r8 = -74
            r14 = 0
            r0 = r17
            r0.mPackCount = r14
            goto L_0x0007
        L_0x0be0:
            r0 = r17
            com.contec.jar.cms50k.DeviceDataSPO2H r14 = r0.mSpo2h
            byte[] r14 = r14.MovementPoint
            r0 = r17
            int r15 = r0.mPackCount
            int r15 = r15 * 12
            int r15 = r15 + r10
            int r16 = r10 + 5
            byte r16 = r18[r16]
            r14[r15] = r16
            int r10 = r10 + 1
            goto L_0x0bb0
        L_0x0bf6:
            r0 = r17
            com.contec.jar.cms50k.DeviceDataSPO2H r14 = r0.mSpo2h
            byte[] r14 = r14.MovementPoint
            r0 = r17
            int r15 = r0.mPackCount
            int r15 = r15 * 12
            r16 = 5
            byte r16 = r18[r16]
            r14[r15] = r16
            r0 = r17
            com.contec.jar.cms50k.DeviceDataSPO2H r14 = r0.mSpo2h
            byte[] r14 = r14.MovementPoint
            r0 = r17
            int r15 = r0.mPackCount
            int r15 = r15 * 12
            int r15 = r15 + 1
            r16 = 6
            byte r16 = r18[r16]
            r14[r15] = r16
            r0 = r17
            com.contec.jar.cms50k.DeviceDataSPO2H r14 = r0.mSpo2h
            byte[] r14 = r14.MovementPoint
            r0 = r17
            int r15 = r0.mPackCount
            int r15 = r15 * 12
            int r15 = r15 + 2
            r16 = 7
            byte r16 = r18[r16]
            r14[r15] = r16
            r0 = r17
            com.contec.jar.cms50k.DeviceDataSPO2H r14 = r0.mSpo2h
            byte[] r14 = r14.MovementPoint
            r0 = r17
            int r15 = r0.mPackCount
            int r15 = r15 * 12
            int r15 = r15 + 3
            r16 = 8
            byte r16 = r18[r16]
            r14[r15] = r16
            r0 = r17
            com.contec.jar.cms50k.DeviceDataSPO2H r14 = r0.mSpo2h
            byte[] r14 = r14.MovementPoint
            r0 = r17
            int r15 = r0.mPackCount
            int r15 = r15 * 12
            int r15 = r15 + 4
            r16 = 9
            byte r16 = r18[r16]
            r14[r15] = r16
            r0 = r17
            com.contec.jar.cms50k.DeviceDataSPO2H r14 = r0.mSpo2h
            byte[] r14 = r14.MovementPoint
            r0 = r17
            int r15 = r0.mPackCount
            int r15 = r15 * 12
            int r15 = r15 + 5
            r16 = 10
            byte r16 = r18[r16]
            r14[r15] = r16
            r0 = r17
            com.contec.jar.cms50k.DeviceDataSPO2H r14 = r0.mSpo2h
            byte[] r14 = r14.MovementPoint
            r0 = r17
            int r15 = r0.mPackCount
            int r15 = r15 * 12
            int r15 = r15 + 6
            r16 = 11
            byte r16 = r18[r16]
            r14[r15] = r16
            r0 = r17
            com.contec.jar.cms50k.DeviceDataSPO2H r14 = r0.mSpo2h
            byte[] r14 = r14.MovementPoint
            r0 = r17
            int r15 = r0.mPackCount
            int r15 = r15 * 12
            int r15 = r15 + 7
            r16 = 12
            byte r16 = r18[r16]
            r14[r15] = r16
            r0 = r17
            com.contec.jar.cms50k.DeviceDataSPO2H r14 = r0.mSpo2h
            byte[] r14 = r14.MovementPoint
            r0 = r17
            int r15 = r0.mPackCount
            int r15 = r15 * 12
            int r15 = r15 + 8
            r16 = 13
            byte r16 = r18[r16]
            r14[r15] = r16
            r0 = r17
            com.contec.jar.cms50k.DeviceDataSPO2H r14 = r0.mSpo2h
            byte[] r14 = r14.MovementPoint
            r0 = r17
            int r15 = r0.mPackCount
            int r15 = r15 * 12
            int r15 = r15 + 9
            r16 = 14
            byte r16 = r18[r16]
            r14[r15] = r16
            r0 = r17
            com.contec.jar.cms50k.DeviceDataSPO2H r14 = r0.mSpo2h
            byte[] r14 = r14.MovementPoint
            r0 = r17
            int r15 = r0.mPackCount
            int r15 = r15 * 12
            int r15 = r15 + 10
            r16 = 15
            byte r16 = r18[r16]
            r14[r15] = r16
            r0 = r17
            com.contec.jar.cms50k.DeviceDataSPO2H r14 = r0.mSpo2h
            byte[] r14 = r14.MovementPoint
            r0 = r17
            int r15 = r0.mPackCount
            int r15 = r15 * 12
            int r15 = r15 + 11
            r16 = 16
            byte r16 = r18[r16]
            r14[r15] = r16
            r8 = -73
            goto L_0x0bb9
        L_0x0ce8:
            java.lang.String r14 = "DevicePackManager"
            java.lang.String r15 = "-------连续五分钟RR间期数据的基本信息--------"
            com.contec.jar.cms50k.DebugLog.e(r14, r15)
            r8 = -56
            r14 = 1
            byte r14 = r18[r14]
            r0 = r17
            r0.mYear = r14
            r14 = 2
            byte r14 = r18[r14]
            r0 = r17
            r0.mMonth = r14
            r14 = 3
            byte r14 = r18[r14]
            r0 = r17
            r0.mDay = r14
            r14 = 4
            byte r14 = r18[r14]
            r0 = r17
            r0.mHour = r14
            r14 = 5
            byte r14 = r18[r14]
            r0 = r17
            r0.mMinu = r14
            r14 = 6
            byte r14 = r18[r14]
            r0 = r17
            r0.mSeco = r14
            com.contec.jar.cms50k.MinData r14 = new com.contec.jar.cms50k.MinData
            r14.<init>()
            r0 = r17
            r0.mMinData = r14
            r14 = 6
            byte[] r1 = new byte[r14]
            r14 = 0
            r15 = 1
            byte r15 = r18[r15]
            r1[r14] = r15
            r14 = 0
            r15 = 2
            byte r15 = r18[r15]
            r1[r14] = r15
            r14 = 0
            r15 = 3
            byte r15 = r18[r15]
            r1[r14] = r15
            r14 = 0
            r15 = 4
            byte r15 = r18[r15]
            r1[r14] = r15
            r14 = 0
            r15 = 5
            byte r15 = r18[r15]
            r1[r14] = r15
            r14 = 0
            r15 = 6
            byte r15 = r18[r15]
            r1[r14] = r15
            r14 = 0
            r15 = 0
            r1[r14] = r15
            r14 = 0
            r15 = 0
            r1[r14] = r15
            r0 = r17
            com.contec.jar.cms50k.MinData r14 = r0.mMinData
            r14.mStartDate = r1
            r0 = r17
            int r14 = r0.mCount
            int r14 = r14 + -1
            r0 = r17
            r0.mCount = r14
            goto L_0x0007
        L_0x0d65:
            java.lang.String r14 = "DevicePackManager"
            java.lang.String r15 = "-------连续RR间数据--------"
            com.contec.jar.cms50k.DebugLog.e(r14, r15)
            unPack(r18)
            r0 = r17
            int r14 = r0._packCount
            int r14 = r14 + 1
            r0 = r17
            r0._packCount = r14
            r14 = 1
            byte r14 = r18[r14]
            r14 = r14 & 64
            r15 = 64
            if (r14 != r15) goto L_0x0dc4
            java.lang.String r14 = "cms50k"
            java.lang.String r15 = "数据包结束"
            com.contec.jar.cms50k.DebugLog.e(r14, r15)
            r0 = r17
            com.contec.jar.cms50k.DeviceDataSPO2H r14 = r0.mSpo2h
            byte[] r14 = r14.PulseSegment
            int r14 = r14.length
            r0 = r17
            int r15 = r0.mPackCount
            int r15 = r15 * 12
            int r6 = r14 - r15
            r10 = 0
        L_0x0d99:
            if (r10 < r6) goto L_0x0dae
            r8 = -62
            r14 = 0
            r0 = r17
            r0._packCount = r14
        L_0x0da2:
            r0 = r17
            int r14 = r0.mPackCount
            int r14 = r14 + 1
            r0 = r17
            r0.mPackCount = r14
            goto L_0x0007
        L_0x0dae:
            r0 = r17
            com.contec.jar.cms50k.DeviceDataSPO2H r14 = r0.mSpo2h
            byte[] r14 = r14.PulseSegment
            r0 = r17
            int r15 = r0.mPackCount
            int r15 = r15 * 12
            int r15 = r15 + r10
            int r16 = r10 + 5
            byte r16 = r18[r16]
            r14[r15] = r16
            int r10 = r10 + 1
            goto L_0x0d99
        L_0x0dc4:
            r0 = r17
            com.contec.jar.cms50k.DeviceDataSPO2H r14 = r0.mSpo2h
            byte[] r14 = r14.RRPoint
            r0 = r17
            int r15 = r0.mPackCount
            int r15 = r15 * 12
            int r15 = r15 + 0
            r16 = 5
            byte r16 = r18[r16]
            r14[r15] = r16
            r0 = r17
            com.contec.jar.cms50k.DeviceDataSPO2H r14 = r0.mSpo2h
            byte[] r14 = r14.RRPoint
            r0 = r17
            int r15 = r0.mPackCount
            int r15 = r15 * 12
            int r15 = r15 + 1
            r16 = 6
            byte r16 = r18[r16]
            r14[r15] = r16
            r0 = r17
            com.contec.jar.cms50k.DeviceDataSPO2H r14 = r0.mSpo2h
            byte[] r14 = r14.RRPoint
            r0 = r17
            int r15 = r0.mPackCount
            int r15 = r15 * 12
            int r15 = r15 + 2
            r16 = 7
            byte r16 = r18[r16]
            r14[r15] = r16
            r0 = r17
            com.contec.jar.cms50k.DeviceDataSPO2H r14 = r0.mSpo2h
            byte[] r14 = r14.RRPoint
            r0 = r17
            int r15 = r0.mPackCount
            int r15 = r15 * 12
            int r15 = r15 + 3
            r16 = 8
            byte r16 = r18[r16]
            r14[r15] = r16
            r0 = r17
            com.contec.jar.cms50k.DeviceDataSPO2H r14 = r0.mSpo2h
            byte[] r14 = r14.RRPoint
            r0 = r17
            int r15 = r0.mPackCount
            int r15 = r15 * 12
            int r15 = r15 + 4
            r16 = 9
            byte r16 = r18[r16]
            r14[r15] = r16
            r0 = r17
            com.contec.jar.cms50k.DeviceDataSPO2H r14 = r0.mSpo2h
            byte[] r14 = r14.RRPoint
            r0 = r17
            int r15 = r0.mPackCount
            int r15 = r15 * 12
            int r15 = r15 + 5
            r16 = 10
            byte r16 = r18[r16]
            r14[r15] = r16
            r0 = r17
            com.contec.jar.cms50k.DeviceDataSPO2H r14 = r0.mSpo2h
            byte[] r14 = r14.RRPoint
            r0 = r17
            int r15 = r0.mPackCount
            int r15 = r15 * 12
            int r15 = r15 + 6
            r16 = 11
            byte r16 = r18[r16]
            r14[r15] = r16
            r0 = r17
            com.contec.jar.cms50k.DeviceDataSPO2H r14 = r0.mSpo2h
            byte[] r14 = r14.RRPoint
            r0 = r17
            int r15 = r0.mPackCount
            int r15 = r15 * 12
            int r15 = r15 + 7
            r16 = 12
            byte r16 = r18[r16]
            r14[r15] = r16
            r0 = r17
            com.contec.jar.cms50k.DeviceDataSPO2H r14 = r0.mSpo2h
            byte[] r14 = r14.RRPoint
            r0 = r17
            int r15 = r0.mPackCount
            int r15 = r15 * 12
            int r15 = r15 + 8
            r16 = 13
            byte r16 = r18[r16]
            r14[r15] = r16
            r0 = r17
            com.contec.jar.cms50k.DeviceDataSPO2H r14 = r0.mSpo2h
            byte[] r14 = r14.RRPoint
            r0 = r17
            int r15 = r0.mPackCount
            int r15 = r15 * 12
            int r15 = r15 + 9
            r16 = 14
            byte r16 = r18[r16]
            r14[r15] = r16
            r0 = r17
            com.contec.jar.cms50k.DeviceDataSPO2H r14 = r0.mSpo2h
            byte[] r14 = r14.RRPoint
            r0 = r17
            int r15 = r0.mPackCount
            int r15 = r15 * 12
            int r15 = r15 + 10
            r16 = 15
            byte r16 = r18[r16]
            r14[r15] = r16
            r0 = r17
            com.contec.jar.cms50k.DeviceDataSPO2H r14 = r0.mSpo2h
            byte[] r14 = r14.RRPoint
            r0 = r17
            int r15 = r0.mPackCount
            int r15 = r15 * 12
            int r15 = r15 + 11
            r16 = 16
            byte r16 = r18[r16]
            r14[r15] = r16
            r8 = -61
            goto L_0x0da2
        L_0x0eb8:
            java.lang.String r14 = "DevicePackManager"
            java.lang.String r15 = "-------数据封装结果--------"
            com.contec.jar.cms50k.DebugLog.e(r14, r15)
            r14 = 8
            byte[] r9 = new byte[r14]
            r14 = 0
            r15 = 1
            byte r15 = r18[r15]
            r9[r14] = r15
            r14 = 1
            r15 = 2
            byte r15 = r18[r15]
            r9[r14] = r15
            r14 = 2
            r15 = 3
            byte r15 = r18[r15]
            r9[r14] = r15
            r14 = 3
            r15 = 4
            byte r15 = r18[r15]
            r9[r14] = r15
            r14 = 4
            r15 = 5
            byte r15 = r18[r15]
            r9[r14] = r15
            r14 = 5
            r15 = 6
            byte r15 = r18[r15]
            r9[r14] = r15
            r14 = 6
            r15 = 7
            byte r15 = r18[r15]
            r9[r14] = r15
            r14 = 7
            r15 = 8
            byte r15 = r18[r15]
            r9[r14] = r15
            r0 = r17
            com.contec.jar.cms50k.DeviceDataSPO2H r14 = r0.mSpo2h
            r14.mCodedata = r9
            r8 = -49
            goto L_0x0007
        L_0x0efe:
            r14 = 7
            byte r14 = r18[r14]
            r15 = 48
            if (r14 != r15) goto L_0x0f26
            r14 = 8
            byte r14 = r18[r14]
            r15 = 48
            if (r14 != r15) goto L_0x0f17
            r14 = 2
            r0 = r17
            r0.setProduct(r14)
        L_0x0f13:
            r8 = -15
            goto L_0x0007
        L_0x0f17:
            r14 = 8
            byte r14 = r18[r14]
            r15 = 49
            if (r14 != r15) goto L_0x0f13
            r14 = 3
            r0 = r17
            r0.setProduct(r14)
            goto L_0x0f13
        L_0x0f26:
            r14 = 7
            byte r14 = r18[r14]
            r15 = 49
            if (r14 != r15) goto L_0x0f13
            r14 = 8
            byte r14 = r18[r14]
            r15 = 48
            if (r14 != r15) goto L_0x0f3c
            r14 = 0
            r0 = r17
            r0.setProduct(r14)
            goto L_0x0f13
        L_0x0f3c:
            r14 = 8
            byte r14 = r18[r14]
            r15 = 49
            if (r14 != r15) goto L_0x0f13
            r14 = 1
            r0 = r17
            r0.setProduct(r14)
            goto L_0x0f13
        */
        throw new UnsupportedOperationException("Method not decompiled: com.contec.jar.cms50k.DevicePackManager.processData(byte[]):byte");
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
