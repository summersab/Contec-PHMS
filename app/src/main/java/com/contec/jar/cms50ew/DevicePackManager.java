package com.contec.jar.cms50ew;

import android.util.Log;
import java.util.ArrayList;
import u.aly.dp;

public class DevicePackManager {
    public static boolean IS_SET_DATE_SUCCESS = false;
    static int PILowLimit = 0;
    static int PIUpLimit = 2200;
    static int PULSELowLimit = 0;
    static int PULSEUpLimit = 254;
    public static final int[] PackLength;
    static int SPO2LowLimit = 0;
    static int SPO2UpLimit = 100;
    public static final int e_PI_exclude = 65535;
    public static final int e_pack_check_sum = 24;
    public static final int e_pack_datacount = 10;
    public static final int e_pack_datalen = 8;
    public static final int e_pack_device_date = 22;
    public static final int e_pack_feedback = 11;
    public static final int e_pack_pitype = 14;
    public static final int e_pack_realTimeData = 1;
    public static final int e_pack_savedata = 15;
    public static final int e_pack_savedatapi = 9;
    public static final int e_pack_savedate = 7;
    public static final int e_pack_savetime = 18;
    public static final int e_pack_username = 5;
    public static final int e_pack_usernum = 16;
    public static final int e_pulse_exclude = 255;
    public static final int e_spO2_exclude = 127;
    public int CHECK_SUM = 0;
    boolean bGetPackId = false;
    byte[] curPack = new byte[9];
    int i;
    int k = 0;
    int len = 0;
    public int mDATALEN = 0;
    public int mDataCount = 0;
    private int mDataLen = 0;
    private byte mDay = 0;
    public DeviceData mDeviceData = new DeviceData();
    public DeviceDataIW mDeviceDataIW;
    public ArrayList<DeviceDataIW> mDeviceDataIWs = new ArrayList<>();
    private byte mHour = 0;
    private byte mMin = 0;
    private byte mMonth = 0;
    public int mPI = 1;
    public DeviceDataIW mSaveDeviceDataIW;
    private byte mSec = 0;
    private String mUserName;
    private int mYear = 0;
    private int mpiDataindex = 0;
    byte value;

    static {
        int[] iArr = new int[128];
        iArr[1] = 9;
        iArr[2] = 9;
        iArr[3] = 9;
        iArr[4] = 9;
        iArr[5] = 9;
        iArr[6] = 4;
        iArr[7] = 8;
        iArr[8] = 8;
        iArr[9] = 6;
        iArr[10] = 4;
        iArr[11] = 4;
        iArr[12] = 2;
        iArr[13] = 3;
        iArr[14] = 3;
        iArr[15] = 8;
        iArr[16] = 3;
        iArr[17] = 9;
        iArr[18] = 8;
        iArr[19] = 9;
        iArr[20] = 9;
        iArr[21] = 9;
        iArr[22] = 5;
        iArr[23] = 7;
        iArr[24] = 4;
        iArr[123] = 9;
        iArr[124] = 9;
        iArr[125] = 9;
        PackLength = iArr;
    }

    public byte arrangeMessage(byte[] buf, int length) {
        byte _return = 0;
        this.i = 0;
        while (this.i < length) {
            this.value = buf[this.i];
            if (this.bGetPackId) {
                if (this.value < 0) {
                    byte[] bArr = this.curPack;
                    int i = this.k;
                    this.k = i + 1;
                    bArr[i] = this.value;
                    if (this.k >= this.len) {
                        this.bGetPackId = false;
                        _return = processData(this.curPack);
                    }
                } else {
                    this.bGetPackId = false;
                }
            } else if (this.value >= 0 && PackLength[this.value] > 0) {
                this.bGetPackId = true;
                this.k = 0;
                this.len = PackLength[this.value];
                this.curPack = new byte[this.len];
                byte[] bArr2 = this.curPack;
                int i2 = this.k;
                this.k = i2 + 1;
                bArr2[i2] = this.value;
            }
            this.i++;
        }
        return _return;
    }

    public void sum_Check(byte[] pack) {
        int _size = pack.length;
        for (int i = 0; i < _size; i++) {
            this.CHECK_SUM += pack[i] & 255;
        }
    }

    public static byte[] unPack(byte[] pack) {
        int len2 = PackLength[pack[0]];
        for (int i = 2; i < len2; i++) {
            pack[i] = (byte) (pack[i] & ((pack[1] << (9 - i)) | Byte.MAX_VALUE));
        }
        pack[1] = Byte.MIN_VALUE;
        return pack;
    }

    public static byte[] doPack(byte[] pack) {
        if (pack == null) {
            return null;
        }
        int len2 = PackLength[pack[0]];
        if (len2 <= 0) {
            return pack;
        }
        pack[1] = Byte.MIN_VALUE;
        for (int i = 2; i < len2; i++) {
            pack[1] = (byte) (pack[1] | ((pack[i] & 128) >> (9 - i)));
            pack[i] = (byte) (pack[i] | 128);
        }
        return pack;
    }

    public byte processData(byte[] pack) {
        unPack(pack);
        PrintBytes.printData(pack);
        switch (pack[0]) {
            case 1:
                return 1;
            case 5:
                int[] tmp = new int[6];
                for (int i = 0; i < 6; i++) {
                    tmp[i] = pack[i + 2];
                    this.mUserName = new String(tmp, 0, 6).trim();
                }
                return 5;
            case 7:
                sum_Check(pack);
                this.mYear = pack[5] + 2000;
                this.mMonth = pack[6];
                this.mDay = pack[7];
                if (this.mPI == 0) {
                    if (this.mDeviceDataIW != null) {
                        this.mSaveDeviceDataIW = this.mDeviceDataIW;
                    }
                    this.mDeviceDataIW = new DeviceDataIW();
                    this.mDeviceDataIW._Year = this.mYear;
                    this.mDeviceDataIW._Month = this.mMonth;
                    this.mDeviceDataIW._Day = this.mDay;
                }
                return 7;
            case 8:
                this.mDataLen = (pack[4] & 255) | ((pack[5] & 255) << 8) | ((pack[6] & 255) << 16) | ((pack[7] & 255) << 24);
                if (this.mPI == 0) {
                    this.mDataLen /= 4;
                } else {
                    this.mDataLen /= 2;
                }
                this.mDATALEN = this.mDataLen;
                this.mDeviceDataIW = new DeviceDataIW();
                this.mDeviceDataIW._DataLen = this.mDataLen;
                return 8;
            case 9:
                sum_Check(pack);
                this.mDeviceDataIW.valueList.add(new byte[]{pack[2], pack[3], pack[4], pack[5]});
                Log.e("*****", "****: 9");
                return 9;
            case 10:
                this.mDataCount = pack[3];
                return 10;
            case 11:
                byte _back = pack[2];
                byte _back_info = pack[3];
                switch (_back) {
                    case -82:
                        if (_back_info == 0) {
                            return -82;
                        }
                        return -94;
                    case -79:
                        if (_back_info == 0) {
                            return -79;
                        }
                        return 33;
                    case -78:
                        if (_back_info == 0) {
                            return -78;
                        }
                        return 34;
                    case -76:
                        return -76;
                    case -75:
                        return -75;
                    case -73:
                        return -73;
                }
            case 14:
                this.mPI = pack[2];
                return dp.l;
            case 15:
                sum_Check(pack);
                this.mDeviceData.valueList.add(new byte[]{(byte) (this.mYear - 2000), this.mMonth, this.mDay, this.mHour, this.mMin, this.mSec, pack[2], pack[3], (byte) this.mPI});
                return dp.m;
            case 18:
                sum_Check(pack);
                this.mHour = pack[4];
                this.mMin = pack[5];
                this.mSec = pack[6];
                if (this.mPI == 0) {
                    this.mDeviceDataIW._Hour = this.mHour;
                    this.mDeviceDataIW._Min = this.mMin;
                    this.mDeviceDataIW._Sec = this.mSec;
                }
                return 18;
            case 22:
                return 22;
            case 24:
                if (this.mPI == 0) {
                    this.mSaveDeviceDataIW = this.mDeviceDataIW;
                    this.mDeviceDataIWs.add(this.mDeviceDataIW);
                }
                int low_eight = this.CHECK_SUM & 255;
                int hight_eight = (this.CHECK_SUM >> 8) & 255;
                this.CHECK_SUM = 0;
                int _low_eight = pack[3] & 255;
                int _hight_eight = pack[2] & 255;
                if (low_eight != _low_eight || _hight_eight == hight_eight) {
                    return 24;
                }
                return 24;
        }
        return 0;
    }
}
