package com.example.ble_bc_code;

import android.util.Log;
import com.alibaba.cchannel.CloudChannel;
import com.contec.phms.util.Constants;
import com.example.bm77_bc_code.BC401_Data;
import com.example.bm77_bc_code.BC401_Struct;
import com.example.bm77_bc_code.PrintBytes;
import java.util.ArrayList;
import java.util.List;
import u.aly.dp;

public class PackManagerUran {
    int _packCount = 0;
    private boolean bGetPackId = false;
    byte[] curPack = new byte[64];
    int i;
    int k = 0;
    int len = 0;
    public BC401_Data mBc401_Data = new BC401_Data();
    int mCount;
    int mCount_ECG;
    byte mDay;
    byte mHour;
    byte mMinu;
    byte mMonth;
    int mPackCount;
    byte mSeco;
    int mSegmentLen;
    public DeviceUran mUran;
    byte mYear;
    public ArrayList<Object> minDatas = new ArrayList<>();
    public List<DeviceUran> mlistUran = new ArrayList();
    byte value;

    public byte arrangeMessage(byte[] buf, int length) {
        Log.e("arrangeMessage", "-------测试--------");
        Log.e("arrangeMessage", "*****************************");
        PrintBytes.printData(buf, length);
        Log.e("arrangeMessage", "*****************************");
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
            case Constants.UPDATEXML_UPLOAD_FAIL:
                return 7;
            case Constants.THREAD_OUT:
                return 16;
            case Constants.UPDATE_XML_FAIL:
                return 3;
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
        switch (pack[0]) {
            case Constants.UPDATEXML_UPLOAD_FAIL:
                this.mUran = new DeviceUran();
                this.mUran.mDataNumber = new byte[7];
                this.mUran.mStrongNUmber = new int[3];
                this.mUran.mDataNumber[0] = pack[2];
                this.mUran.mDataNumber[1] = (byte) (pack[3] & 255);
                this.mUran.mDataNumber[2] = pack[4];
                this.mUran.mDataNumber[3] = (byte) (pack[5] & 255);
                this.mUran.mStrongNUmber[0] = ((this.mUran.mDataNumber[1] & 32640) | (this.mUran.mDataNumber[0] & 255)) & 65535;
                this.mUran.mStrongNUmber[1] = ((this.mUran.mDataNumber[3] & 32640) | (this.mUran.mDataNumber[2] & 255)) & 65535;
                Log.e("==============", "======================");
                Log.e("==============", "======================" + this.mUran.mStrongNUmber[0] + this.mUran.mStrongNUmber[1]);
                Log.e("==============", "======================");
                return 18;
            case Constants.THREAD_OUT:
                Log.e("+++++++++++++++", "+++++++++++++++");
                Log.e("+++++++++++++++", "全部尿常规数�?");
                Log.e("+++++++++++++++", "+++++++++++++++");
                this._packCount++;
                this.mUran = new DeviceUran();
                this.mUran.mUranData = new byte[20];
                this.mUran.mUranData[0] = (byte) (pack[2] & 255);
                this.mUran.mUranData[1] = (byte) (pack[3] & 255);
                this.mUran.mUranData[2] = (byte) (pack[4] & 255);
                this.mUran.mUranData[3] = (byte) (pack[5] & 255);
                this.mUran.mUranData[4] = (byte) (pack[6] & 255);
                this.mUran.mUranData[5] = (byte) (pack[7] & 255);
                this.mUran.mUranData[6] = (byte) (pack[8] & 255);
                this.mUran.mUranData[7] = (byte) (pack[9] & 255);
                this.mUran.mUranData[8] = (byte) (pack[10] & 255);
                this.mUran.mUranData[9] = (byte) (pack[11] & 255);
                this.mUran.mUranData[10] = (byte) (pack[12] & 255);
                this.mUran.mUranData[11] = (byte) (pack[13] & 255);
                this.mUran.mUranData[12] = (byte) (pack[14] & 255);
                this.mUran.mUranData[6] = (byte) (pack[8] & 255 & 7);
                this.mUran.mUranData[7] = (byte) (((pack[8] & 255) & 56) >> 3);
                this.mUran.mUranData[8] = (byte) (pack[9] & 255 & 7);
                this.mUran.mUranData[9] = (byte) ((((pack[9] & 255) & 255) & 56) >> 3);
                this.mUran.mUranData[10] = (byte) (pack[10] & 255 & 7);
                this.mUran.mUranData[11] = (byte) ((((pack[10] & 255) & 255) & 56) >> 3);
                this.mUran.mUranData[12] = (byte) (pack[11] & 255 & 255 & 7);
                this.mUran.mUranData[13] = (byte) ((((pack[11] & 255) & 255) & 56) >> 3);
                this.mUran.mUranData[14] = (byte) (pack[12] & 255 & 255 & 7);
                this.mUran.mUranData[15] = (byte) ((((pack[12] & 255) & 255) & 56) >> 3);
                this.mUran.mUranData[16] = (byte) (pack[13] & 255 & 255 & 7);
                this.mUran.mUranData[17] = (byte) ((((pack[13] & 255) & 255) & 56) >> 3);
                this.mUran.mUranData[18] = (byte) (pack[14] & 255 & 255 & 7);
                this.mUran.mUranData[19] = (byte) ((((pack[14] & 255) & 255) & 56) >> 3);
                this.mBc401_Data.Structs.add(unPack(pack));
                Log.e("�?有数据的条数", "GGGGGGGGGG" + this.mlistUran.size());
                if ((pack[1] & 64) == 64) {
                    this._packCount = 0;
                    return 20;
                } else if (this._packCount != 10) {
                    return -120;
                } else {
                    this._packCount = 0;
                    return 19;
                }
            case Constants.UPDATE_XML_FAIL:
                Log.e("+++++++++++++++", "+++++++++++++++");
                Log.e("+++++++++++++++", "删除数据成功");
                Log.e("+++++++++++++++", "+++++++++++++++");
                if (!Check(pack) || (pack[1] & 255) != 0) {
                    return 33;
                }
                return 32;
            case -13:
                Log.e("+++++++++++++++", "+++++++++++++++");
                Log.e("+++++++++++++++", "对时操作成功");
                Log.e("+++++++++++++++", "+++++++++++++++");
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

    public BC401_Struct unPack(byte[] pData) {
        byte[] _data = pData;
        BC401_Struct _BC01 = new BC401_Struct();
        _BC01.Year = _data[2] & 255;
        _BC01.Month = _data[3] & 255;
        _BC01.Date = _data[4] & 255;
        _BC01.Hour = _data[5] & 255;
        _BC01.Min = _data[6] & 255;
        _BC01.Sec = _data[7] & 255;
        _BC01.URO = (byte) (_data[8] & 255 & 7);
        _BC01.BLD = (byte) (((_data[8] & 255) & 56) >> 3);
        _BC01.BIL = (byte) (_data[9] & 255 & 7);
        _BC01.KET = (byte) ((((_data[9] & 255) & 255) & 56) >> 3);
        _BC01.GLU = (byte) (_data[10] & 255 & 7);
        _BC01.PRO = (byte) ((((_data[10] & 255) & 255) & 56) >> 3);
        _BC01.PH = (byte) (_data[11] & 255 & 255 & 7);
        _BC01.NIT = (byte) ((((_data[11] & 255) & 255) & 56) >> 3);
        _BC01.LEU = (byte) (_data[12] & 255 & 255 & 7);
        _BC01.SG = (byte) ((((_data[12] & 255) & 255) & 56) >> 3);
        _BC01.VC = (byte) (_data[13] & 255 & 255 & 7);
        _BC01.MAL = -8;
        _BC01.CR = -8;
        _BC01.UCA = -8;
        return _BC01;
    }
}
