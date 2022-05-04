package com.contec.jar.pm85;

import android.bluetooth.BluetoothSocket;
import java.util.ArrayList;

import u.aly.dp;

public class DevicePackManager {
    public static final int[] PackLength;
    public static final int e_pack_DataCount = 7;
    public static final int e_pack_ECGData_Date = 4;
    public static final int e_pack_ECGData_time = 3;
    public static final int e_pack_ECG_Data = 2;
    public static final int e_pack_accountInfo = 1;
    public static final int e_pack_account_re = 10;
    public static final int e_pack_check_signal = 12;
    public static final int e_pack_clock_invalid = 13;
    public static final int e_pack_date = 6;
    public static final int e_pack_date_re = 15;
    public static final int e_pack_ecg_re = 11;
    public static final int e_pack_flag = 18;
    public static final int e_pack_noData = 31;
    public static final int e_pack_time = 5;
    public static final int e_pack_time_re = 14;
    boolean bGetPackId = false;
    byte[] curPack = new byte[9];
    int i;
    int k = 0;
    int len = 0;
    int m = 0;
    public int mCount = 0;
    public DeviceData mDeviceData = new DeviceData();
    public ArrayList<DeviceData> mDeviceDatas = new ArrayList<>();
    BluetoothSocket mSocket;
    byte value;

    static {
        int[] iArr = new int[16];
        iArr[1] = 25;
        iArr[2] = 9;
        iArr[3] = 5;
        iArr[4] = 6;
        iArr[5] = 5;
        iArr[6] = 6;
        iArr[7] = 3;
        iArr[9] = 1;
        iArr[10] = 1;
        iArr[11] = 1;
        iArr[14] = 1;
        iArr[15] = 1;
        PackLength = iArr;
    }

    public static int packlength(byte pHead) {
        int _p = pHead & 255;
        if (_p == 31 || _p == 17 || _p == 18) {
            return 1;
        }
        if (_p == 245) {
            return 5;
        }
        if (_p == 246) {
            return 6;
        }
        return PackLength[_p];
    }

    public DevicePackManager(BluetoothSocket pSocket) {
        this.mSocket = pSocket;
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
            } else if (this.value >= 0 && packlength(this.value) > 0) {
                this.bGetPackId = true;
                this.k = 0;
                this.len = packlength(this.value);
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

    public byte processData(byte[] pack) {
        if (pack.length != 1) {
            unPack(pack);
        }
        switch (pack[0]) {
            case 2:
                this.m++;
                this.mDeviceData.mDatas.add(getEcg(pack));
                break;
            case 3:
                this.mDeviceData = new DeviceData();
                this.mDeviceData._time[0] = pack[2];
                this.mDeviceData._time[1] = pack[3];
                this.mDeviceData._time[2] = pack[4];
                break;
            case 4:
                this.mDeviceData._date[0] = pack[2];
                this.mDeviceData._date[1] = pack[3];
                this.mDeviceData._date[2] = pack[4];
                this.mDeviceData._date[3] = pack[5];
                break;
            case 7:
                this.mCount = ((pack[1] << 8) | pack[2]) & 255;
                System.out.println("fsdfsdf");
                break;
            case 11:
                this.mDeviceDatas.add(this.mDeviceData);
                break;
            case 18:
                this.mDeviceData.mFlags.add(Integer.valueOf(this.m));
                break;
        }
        return pack[0];
    }

    public static short[] getEcg(byte[] pack) {
        short V_L = (short) (pack[3] & 255);
        short LA_L = (short) (pack[4] & 255);
        short LA_H = (short) ((pack[5] >> 4) & 15);
        short V_H = (short) (pack[5] & dp.m);
        short RA_L = (short) (pack[6] & 255);
        short QB_L = (short) (pack[7] & 255);
        short QB_H = (short) ((pack[8] >> 4) & 255);
        short RA_H = (short) (pack[8] & dp.m);
        short LA = (short) (((LA_H << 8) & 4095) | LA_L);
        short RA = (short) (((RA_H << 8) & 4095) | RA_L);
        short s = (short) (((QB_H << 8) & 4095) | QB_L);
        short V = (short) (((V_H << 8) & 4095) | V_L);
        short II = (short) (4096 - RA);
        short III = (short) (4096 - LA);
        short I = (short) ((LA - RA) + 2048);
        short AVR = (short) ((RA - (LA / 2)) + 1024);
        short AVL = (short) ((LA - (RA / 2)) + 1024);
        short AVF = (short) (4096 - ((RA + LA) / 2));
        return new short[]{I, II, III, AVR, AVL, AVF, (short) (V - (((RA + LA) - 4096) / 3))};
    }

    public static byte[] unPack(byte[] pack) {
        int len2 = packlength(pack[0]);
        if (pack[0] != 1) {
            for (int i = 2; i < len2; i++) {
                int tmp21_20 = i;
                pack[tmp21_20] = (byte) (pack[tmp21_20] & ((pack[1] << (9 - i)) | Byte.MAX_VALUE));
            }
            pack[1] = 80;
        } else {
            unPack_01(pack);
        }
        return pack;
    }

    public static byte[] doPack(byte[] pack) {
        if (pack == null) {
            return null;
        }
        int len2 = packlength(pack[0]);
        if (len2 <= 0) {
            return pack;
        }
        pack[1] = 0;
        for (int i = 2; i < len2; i++) {
            pack[1] = (byte) (pack[1] | ((pack[i] & 128) >> (9 - i)));
            int tmp47_46 = i;
            pack[tmp47_46] = (byte) (pack[tmp47_46] & Byte.MAX_VALUE);
        }
        return pack;
    }

    public static byte[] doPack_F9(byte[] pack) {
        for (int i = 1; i <= 2; i++) {
            for (int j = 1; j < 8; j++) {
                int tmp12_11 = i;
                pack[tmp12_11] = (byte) (pack[tmp12_11] | ((pack[(((i - 1) * 7) + 3) + j] & 128) >> (8 - j)));
            }
        }
        for (int i2 = 1; i2 < 7; i2++) {
            pack[3] = (byte) (pack[3] | ((pack[i2 + 17] & 128) >> (8 - i2)));
        }
        return pack;
    }

    public static byte[] unPack_01(byte[] pack) {
        for (int i = 1; i <= 3; i++) {
            for (int j = 1; j < 8; j++) {
                int tmp21_20 = ((i - 1) * 7) + 3 + j;
                pack[tmp21_20] = (byte) (pack[tmp21_20] & ((pack[i] << (8 - j)) | Byte.MAX_VALUE));
            }
        }
        return pack;
    }
}
