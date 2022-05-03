package com.contec.jar.fhr01;

import android.util.Log;
import com.alibaba.fastjson.asm.Opcodes;
import u.aly.bs;

public class DevicePackManager {
    boolean bGetPackId = false;
    byte[] curPack = new byte[512];
    int i;
    int k = 0;
    int len = 0;
    private DeviceData mDeviceData;
    public DeviceDatas mDeviceDatas = new DeviceDatas();
    boolean mReviceOver = false;
    private int m_Data_Count;
    private String m_device_id = bs.b;
    byte value;

    public int commandSize(byte pByte) {
        if (pByte == 10) {
            return 7;
        }
        if (pByte == -12) {
            return 1;
        }
        if (pByte == 9) {
            return 12;
        }
        return 512;
    }

    public int arrangeMessage1(byte[] buf, int length) {
        int _return = 1;
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
            } else if (commandSize(this.value) > 0) {
                this.bGetPackId = true;
                this.k = 0;
                this.len = commandSize(this.value);
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

    public int processData(byte[] pPackData) {
        int _order = pPackData[0] & 255;
        switch (_order) {
            case 9:
                this.mDeviceData = new DeviceData();
                byte _Location = pPackData[2];
                int _Year = pPackData[3] + 2000;
                int _Month = pPackData[4] - 1;
                byte _Day = pPackData[5];
                byte _startHour = pPackData[6];
                int[] dateEndTime = endTime(_Year, _Month, _Day, pPackData[9], pPackData[10], pPackData[11], _startHour);
                this.mDeviceData.m_start_time = new int[]{_Year, _Month, _Day, _startHour, pPackData[7], pPackData[8]};
                this.mDeviceData.m_end_time = dateEndTime;
                this.mDeviceData.m_Device_ID = this.m_device_id;
                Log.e("******0x09*******", "_Location:" + _Location + "      " + "m_Data_Count:" + this.m_Data_Count);
                if (_Location == this.m_Data_Count) {
                    this.mReviceOver = true;
                }
                return 9;
            case 10:
                if (pPackData[1] == 0) {// == false || !true) {
                    return Opcodes.IF_ICMPLT;
                }
                this.m_Data_Count = pPackData[2] & 255;
                this.mDeviceDatas.m_Data_Count = this.m_Data_Count;
                int j = 0;
                while (true) {
                    int j2 = j;
                    if (j2 >= 8) {
                        Log.e("******0x0A*******", new StringBuilder().append(this.m_Data_Count).toString());
                        return 10;
                    }
                    int j3 = j2 + 1;
                    this.m_device_id = String.valueOf(this.m_device_id) + Integer.toHexString((pPackData[(j2 / 2) + 3] & 255) >> 4);
                    this.m_device_id = String.valueOf(this.m_device_id) + Integer.toHexString(pPackData[(j3 / 2) + 3] & 15);
                    j = j3 + 1;
                }
            case 244:
                this.mDeviceDatas.mDatas.add(this.mDeviceData);
                if (this.mReviceOver) {
                    return Opcodes.I2B;
                }
                return 244;
            default:
                if (_order <= 30 || _order >= 250) {
                    return 1;
                }
                byte[] _mData = new byte[512];
                for (int i = 0; i < 512; i++) {
                    _mData[i] = pPackData[i];
                }
                this.mDeviceData.mDatas.add(_mData);
                return 0;
        }
    }

    public int[] endTime(int pYear, int pMonth, int pDay, int pHour, int pMinute, int pSecond, int p_startHour) {
        if (p_startHour > pHour) {
            if (pDay < new int[]{31, (pYear % 4 == 0 && pYear % 100 != 0) || pYear % 400 == 0 ? 29 : 28, 31, 30, 31, 30, 31, 31, 30, 21, 30, 31}[pMonth - 1]) {
                pDay++;
            } else {
                pDay = 1;
            }
            if (pDay != 1 || pMonth >= 12) {
                pMonth = 1;
            } else {
                pMonth++;
            }
            if (pMonth == 1) {
                pYear++;
            }
        }
        return new int[]{pYear, pMonth, pDay, pHour, pMinute, pSecond};
    }
}
