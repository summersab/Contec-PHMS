package com.contec.jar.pm85;

import android.bluetooth.BluetoothSocket;
import java.util.ArrayList;

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

    /* JADX WARNING: type inference failed for: r19v26, types: [int] */
    /* JADX WARNING: type inference failed for: r19v29, types: [int] */
    /* JADX WARNING: type inference failed for: r19v32, types: [int] */
    /* JADX WARNING: type inference failed for: r19v35, types: [int] */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static short[] getEcg(byte[] r21) {
        /*
            r19 = 3
            byte r19 = r21[r19]
            r0 = r19
            r0 = r0 & 255(0xff, float:3.57E-43)
            r19 = r0
            r0 = r19
            short r0 = (short) r0
            r18 = r0
            r19 = 4
            byte r19 = r21[r19]
            r0 = r19
            r0 = r0 & 255(0xff, float:3.57E-43)
            r19 = r0
            r0 = r19
            short r9 = (short) r0
            r19 = 5
            byte r19 = r21[r19]
            int r19 = r19 >> 4
            r19 = r19 & 15
            r0 = r19
            short r8 = (short) r0
            r19 = 5
            byte r19 = r21[r19]
            r19 = r19 & 15
            r0 = r19
            short r0 = (short) r0
            r17 = r0
            r19 = 6
            byte r19 = r21[r19]
            r0 = r19
            r0 = r0 & 255(0xff, float:3.57E-43)
            r19 = r0
            r0 = r19
            short r15 = (short) r0
            r19 = 7
            byte r19 = r21[r19]
            r0 = r19
            r0 = r0 & 255(0xff, float:3.57E-43)
            r19 = r0
            r0 = r19
            short r12 = (short) r0
            r19 = 8
            byte r19 = r21[r19]
            int r19 = r19 >> 4
            r0 = r19
            r0 = r0 & 255(0xff, float:3.57E-43)
            r19 = r0
            r0 = r19
            short r11 = (short) r0
            r19 = 8
            byte r19 = r21[r19]
            r19 = r19 & 15
            r0 = r19
            short r14 = (short) r0
            int r19 = r8 << 8
            r0 = r19
            r0 = r0 & 4095(0xfff, float:5.738E-42)
            r19 = r0
            r19 = r19 | r9
            r0 = r19
            short r7 = (short) r0
            int r19 = r14 << 8
            r0 = r19
            r0 = r0 & 4095(0xfff, float:5.738E-42)
            r19 = r0
            r19 = r19 | r15
            r0 = r19
            short r13 = (short) r0
            int r19 = r11 << 8
            r0 = r19
            r0 = r0 & 4095(0xfff, float:5.738E-42)
            r19 = r0
            r19 = r19 | r12
            r0 = r19
            short r10 = (short) r0
            int r19 = r17 << 8
            r0 = r19
            r0 = r0 & 4095(0xfff, float:5.738E-42)
            r19 = r0
            r19 = r19 | r18
            r0 = r19
            short r0 = (short) r0
            r16 = r0
            int r0 = 4096 - r13
            r19 = r0
            r0 = r19
            short r5 = (short) r0
            int r0 = 4096 - r7
            r19 = r0
            r0 = r19
            short r6 = (short) r0
            int r19 = r7 - r13
            r0 = r19
            int r0 = r0 + 2048
            r19 = r0
            r0 = r19
            short r4 = (short) r0
            int r19 = r7 / 2
            int r19 = r13 - r19
            r0 = r19
            int r0 = r0 + 1024
            r19 = r0
            r0 = r19
            short r3 = (short) r0
            int r19 = r13 / 2
            int r19 = r7 - r19
            r0 = r19
            int r0 = r0 + 1024
            r19 = r0
            r0 = r19
            short r2 = (short) r0
            int r19 = r13 + r7
            int r19 = r19 / 2
            r0 = r19
            int r0 = 4096 - r0
            r19 = r0
            r0 = r19
            short r1 = (short) r0
            int r19 = r13 + r7
            r0 = r19
            int r0 = r0 + -4096
            r19 = r0
            int r19 = r19 / 3
            int r19 = r16 - r19
            r0 = r19
            short r0 = (short) r0
            r16 = r0
            r19 = 7
            r0 = r19
            short[] r0 = new short[r0]
            r19 = r0
            r20 = 0
            r19[r20] = r4
            r20 = 1
            r19[r20] = r5
            r20 = 2
            r19[r20] = r6
            r20 = 3
            r19[r20] = r3
            r20 = 4
            r19[r20] = r2
            r20 = 5
            r19[r20] = r1
            r20 = 6
            r19[r20] = r16
            return r19
        */
        throw new UnsupportedOperationException("Method not decompiled: com.contec.jar.pm85.DevicePackManager.getEcg(byte[]):short[]");
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
