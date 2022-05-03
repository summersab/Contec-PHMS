package com.example.bm77_bc_code;

import android.util.Log;
import com.contec.phms.util.Constants;
import u.aly.dp;

public class DevicePackManager {
    public int Percent = 0;
    public int PercentAll = 0;
    boolean bGetPackId = false;
    byte[] curPack = new byte[12];
    int i;
    int k = 0;
    int len = 0;
    public BC401_Data mBc401_Data = new BC401_Data();
    int mCurrent_Pack = 0;
    int mDataLen = 0;
    int mPack_Data_Count = 0;
    int mRec_DataCount = 0;
    int mTotal_Pack = 0;
    public int mVersion;
    byte value;

    public int PackLength(byte pOrder) {
        switch (pOrder) {
            case -109:
                return 6;
            case 5:
                break;
            case 6:
                return 7;
            case 8:
                return 8;
            case 21:
                break;
            default:
                return 0;
        }
        return 9;
    }

    public int PackLength(byte pOrder, byte[] pack) {
        switch (pOrder) {
            case 2:
                byte lenght = pack[2];
                Log.e("������ݵĳ���", "��ͳ�豸���ݳ���" + lenght);
                if (lenght == 9) {
                    return 12;
                }
                if (lenght == 10) {
                    return 13;
                }
                return 14;
            default:
                return 0;
        }
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
                    if (this.len == 6) {
                        if (this.curPack[5] == 2) {
                            this.len = PackLength(this.curPack[5], buf);
                        } else {
                            this.len = PackLength(this.curPack[5]);
                        }
                        byte[] _data = new byte[this.curPack.length];
                        for (int j = 0; j < this.curPack.length; j++) {
                            _data[j] = this.curPack[j];
                        }
                        this.curPack = new byte[this.len];
                        for (int j2 = 0; j2 < _data.length; j2++) {
                            this.curPack[j2] = _data[j2];
                        }
                    } else if (this.len == 9) {
                        byte a = buf[5];
                        Log.e("�����ǰ������Ϊ����", new StringBuilder().append(a).toString());
                        if (a == 5) {
                            Log.e("�ɰ洫ͳ�������ص�ֵ", new StringBuilder().append(length).toString());
                            this.len = (this.curPack[8] * dp.l) + 9 + 1;
                        } else {
                            Log.e("�°洫ͳ�������ص�ֵ", new StringBuilder().append(length).toString());
                            this.len = (this.curPack[8] * 30) + 9 + 1;
                        }
                        byte[] _data2 = new byte[this.curPack.length];
                        for (int j3 = 0; j3 < this.curPack.length; j3++) {
                            _data2[j3] = this.curPack[j3];
                        }
                        this.curPack = new byte[this.len];
                        for (int j4 = 0; j4 < _data2.length; j4++) {
                            this.curPack[j4] = _data2[j4];
                        }
                    } else {
                        this.bGetPackId = false;
                        _return = processData(this.curPack);
                    }
                }
            } else if (PackLength(this.value) > 0) {
                this.bGetPackId = true;
                this.k = 0;
                this.len = PackLength(this.value);
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

    public byte processData(byte[] pack) {
        switch (pack[5]) {
            case 2:
                Log.e("���Ͷ�ʱ�������", "��ʱ�ɹ�,��ӡ����汾������");
                int lendata = pack.length;
                Log.e("���Ͷ�ʱ�������", "������ݵĳ���" + lendata);
                if (lendata == 14) {
                    this.mVersion = pack[pack.length - 2] & 255;
                } else {
                    this.mVersion = 0;
                }
                Log.e("��ǰ�İ汾����", "����汾" + this.mVersion);
                return 2;
            case 5:
                Log.e("����ȫ�����ݽ�������", "����һ����Һ�����ǵ�����(�����ڷǶ�����ʾ��������14����ֽ)");
                this.mBc401_Data.Percent = (((pack[7] & 255) + 1) * 100) / (pack[6] & 255);
                this.Percent = (((pack[7] & 255) + 1) * 100) / (pack[6] & 255);
                if (this.mVersion == 1) {
                    Log.e("������صİ汾��1", "���洢������ݴ�������");
                } else {
                    Log.e("������صİ汾����1", "�洢������ݴ�������");
                    dealDPack(pack);
                }
                if (this.Percent == 100) {
                    return 5;
                }
                return 0;
            case 6:
                Log.e("��������ɾ��ָ��", "ɾ���ɹ�");
                return 6;
            case 8:
                return 8;
            case 21:
                Log.e("����ȫ�����ݽ�������", "����һ����Һ�����ǵ�����(�����ڶ�����ʾ������14����ֽ)");
                this.mBc401_Data.PercentAll = ((pack[7] & 255) * 100) / (pack[6] & 255);
                this.PercentAll = (((pack[7] & 255) + 1) * 100) / (pack[6] & 255);
                dealDPackall(pack);
                if (this.PercentAll == 100) {
                    return 21;
                }
                return 0;
            default:
                return 0;
        }
    }

    public void dealDPack(byte[] pPack) {
        int _dataCount = pPack[8] & 255;
        for (int i = 0; i < _dataCount; i++) {
            byte[] _data = new byte[14];
            for (int j = 0; j < 14; j++) {
                _data[j] = pPack[j + 9 + (i * 14)];
            }
            this.mBc401_Data.Structs.add(unPack(_data));
        }
    }

    public void dealDPackall(byte[] pPack) {
        int _dataCount = pPack[8] & 255;
        for (int i = 0; i < _dataCount; i++) {
            byte[] _data = new byte[30];
            for (int j = 0; j < 30; j++) {
                _data[j] = pPack[j + 9 + (i * 30)];
            }
            this.mBc401_Data.Structs.add(unPackall(_data));
        }
    }

    public BC401_Struct unPack(byte[] pData) {
        byte[] _data = pData;
        BC401_Struct _BC01 = new BC401_Struct();
        _BC01.ID = (_data[0] | ((_data[1] & 255) << 8)) & 1023;
        _BC01.User = ((_data[1] & 255) >> 2) & 31;
        _BC01.Year = _data[2] & Byte.MAX_VALUE;
        _BC01.Month = (((_data[2] & 255) >> 7) | ((_data[3] & 255) << 1)) & 15;
        _BC01.Date = ((_data[3] & 255) >> 3) & 31;
        _BC01.Hour = _data[4] & 31;
        _BC01.Min = (((_data[4] & 255) >> 5) | (_data[5] << 3)) & 63;
        _BC01.Sec = _data[6] & Byte.MAX_VALUE;
        _BC01.Item = ((_data[8] & 255) | ((_data[9] & 255) << 8)) & 2047;
        if ((_BC01.Item & 1024) > 0) {
            _BC01.URO = (byte) (((_data[9] & 255) >> 3) & 7);
            _BC01.URO1 = 999;
            _BC01.URO1_Real = 999;
            _BC01.URO_Real = 0;
        } else {
            _BC01.URO = 9;
            _BC01.URO_Real = 9;
            _BC01.URO1 = 999;
            _BC01.URO1_Real = 999;
        }
        if ((_BC01.Item & 512) > 0) {
            _BC01.BLD = (byte) (_data[10] & 7);
            _BC01.BLD1 = 999;
            _BC01.BLD1_Real = 999;
            _BC01.BLD_Real = 0;
        } else {
            _BC01.BLD = 9;
            _BC01.BLD_Real = 9;
            _BC01.BLD1 = 999;
            _BC01.BLD1_Real = 999;
        }
        if ((_BC01.Item & 256) > 0) {
            _BC01.BIL = (byte) (((_data[10] & 255) >> 3) & 7);
            _BC01.BIL1 = 999;
            _BC01.BIL1_Real = 999;
            _BC01.BIL_Real = 0;
        } else {
            _BC01.BIL = 9;
            _BC01.BIL_Real = 9;
            _BC01.BIL1 = 999;
            _BC01.BIL1_Real = 999;
        }
        if ((_BC01.Item & 128) > 0) {
            _BC01.KET = (byte) ((((_data[10] & 255) >> 6) | ((_data[11] & 1) << 2)) & 7);
            _BC01.KET_Real = 0;
            _BC01.KET1 = 999;
            _BC01.KET1_Real = 999;
        } else {
            _BC01.KET = 9;
            _BC01.KET_Real = 9;
            _BC01.KET1 = 999;
            _BC01.KET1_Real = 999;
        }
        if ((_BC01.Item & 64) > 0) {
            _BC01.GLU = (byte) (((_data[11] & 255) >> 1) & 7);
            _BC01.GLU1 = 999;
            _BC01.GLU1_Real = 999;
            _BC01.GLU_Real = 0;
        } else {
            _BC01.GLU = 9;
            _BC01.GLU_Real = 9;
            _BC01.GLU1 = 999;
            _BC01.GLU1_Real = 999;
        }
        if ((_BC01.Item & 32) > 0) {
            _BC01.PRO = (byte) (((_data[11] & 255) >> 4) & 7);
            _BC01.PRO1 = 999;
            _BC01.PRO1_Real = 999;
            _BC01.PRO_Real = 0;
        } else {
            _BC01.PRO = 9;
            _BC01.PRO_Real = 9;
            _BC01.PRO1 = 999;
            _BC01.PRO1_Real = 999;
        }
        if ((_BC01.Item & 16) > 0) {
            _BC01.PH = (byte) (_data[12] & 7);
            _BC01.PH_Real = 0;
            _BC01.PH1 = 999;
            _BC01.PH1_Real = 999;
        } else {
            _BC01.PH = 9;
            _BC01.PH_Real = 9;
            _BC01.PH1 = 999;
            _BC01.PH1_Real = 999;
        }
        if ((_BC01.Item & 8) > 0) {
            _BC01.NIT = (byte) (((_data[12] & 255) >> 3) & 7);
            _BC01.NIT_Real = 0;
            _BC01.NIT1 = 999;
            _BC01.NIT1_Real = 999;
        } else {
            _BC01.NIT = 9;
            _BC01.NIT_Real = 9;
            _BC01.NIT1 = 999;
            _BC01.NIT1_Real = 999;
        }
        if ((_BC01.Item & 4) > 0) {
            _BC01.LEU = (byte) ((((_data[12] & 255) >> 6) | (_data[13] << 2)) & 7);
            _BC01.LEU_Real = 0;
            _BC01.LEU1 = 999;
            _BC01.LEU1_Real = 999;
        } else {
            _BC01.LEU = 9;
            _BC01.LEU_Real = 9;
            _BC01.LEU1 = 999;
            _BC01.LEU1_Real = 999;
        }
        if ((_BC01.Item & 2) > 0) {
            _BC01.SG = (byte) (((_data[13] & 255) >> 1) & 7);
            _BC01.SG_Real = 0;
            _BC01.SG1 = 999;
            _BC01.SG1_Real = 999;
        } else {
            _BC01.SG = 9;
            _BC01.SG_Real = 9;
            _BC01.SG1 = 999;
            _BC01.SG1_Real = 999;
        }
        if ((_BC01.Item & 1) > 0) {
            _BC01.VC = (byte) (((_data[13] & 255) >> 4) & 7);
            _BC01.VC_Real = 0;
            _BC01.VC1 = 999;
            _BC01.VC1_Real = 999;
        } else {
            _BC01.VC = 9;
            _BC01.VC_Real = 9;
            _BC01.VC1 = 999;
            _BC01.VC1_Real = 999;
        }
        _BC01.MAL = -8;
        _BC01.CR = -8;
        _BC01.UCA = -8;
        return _BC01;
    }

    public BC401_Struct unPackall(byte[] pData) {
        Log.e("�����ڶ�����ʾ������14����ֽ", "$$$$$$$$$$$");
        PrintBytes.printData(pData, pData.length);
        Log.e("�����ڶ�����ʾ������14����ֽ", "$$$$$$$$$$$");
        byte[] _data = pData;
        BC401_Struct _BC01 = new BC401_Struct();
        _BC01.ID = (_data[0] | ((_data[1] & 255) << 8)) & 1023;
        _BC01.User = ((_data[1] & 255) >> 2) & 31;
        _BC01.Year = _data[2] & Byte.MAX_VALUE;
        _BC01.Month = ((((_data[2] & 255) >> 7) & 7) | ((_data[3] & 7) << 1)) & 15;
        _BC01.Date = ((_data[3] & 255) >> 3) & 31;
        _BC01.Hour = _data[4] & 31;
        _BC01.Min = (((_data[4] & 255) >> 5) | (_data[5] << 3)) & 63;
        _BC01.Sec = _data[6] & Byte.MAX_VALUE;
        Log.e("ֵ��ʱ��", new StringBuilder().append(_BC01.ID + _BC01.User + _BC01.Year + _BC01.Month + _BC01.Date + _BC01.Hour + _BC01.Min + _BC01.Sec).toString());
        _BC01.Item = ((_data[8] & 255) | ((_data[9] & 255) << 8)) & 16383;
        if ((_BC01.Item & 8192) > 0) {
            _BC01.URO = (byte) (_data[10] & 255 & 7);
            _BC01.URO1 = _data[16] & 255;
            _BC01.URO_Real = 0;
            _BC01.URO1_Real = 0;
        } else {
            _BC01.URO = 9;
            _BC01.URO_Real = 9;
            _BC01.URO1 = 999;
            _BC01.URO1_Real = 999;
        }
        if ((_BC01.Item & 4096) > 0) {
            _BC01.BLD = (byte) (((_data[10] & 255) >> 3) & 7);
            _BC01.BLD1 = _data[17] & 255;
            _BC01.BLD_Real = 0;
            _BC01.BLD1_Real = 0;
        } else {
            _BC01.BLD = 9;
            _BC01.BLD_Real = 9;
            _BC01.BLD1 = 999;
            _BC01.BLD1_Real = 999;
        }
        if ((_BC01.Item & 2048) > 0) {
            _BC01.BIL = (byte) ((((_data[10] & 255) >> 6) | ((_data[11] & 1) << 2)) & 7);
            _BC01.BIL1 = _data[18] & 255 & Byte.MAX_VALUE;
            _BC01.BIL_Real = 0;
            _BC01.BIL1_Real = 0;
        } else {
            _BC01.BIL = 9;
            _BC01.BIL_Real = 9;
            _BC01.BIL1 = 999;
            _BC01.BIL1_Real = 999;
        }
        if ((_BC01.Item & 1024) > 0) {
            _BC01.KET = (byte) (((_data[11] & 255) >> 1) & 7);
            _BC01.KET1 = (((_data[18] & 255) >> 7) | ((_data[19] & 255) << 1)) & 127;
            _BC01.KET1_Real = 0;
            _BC01.KET_Real = 0;
        } else {
            _BC01.KET = 9;
            _BC01.KET_Real = 9;
            _BC01.KET1 = 999;
            _BC01.KET1_Real = 999;
        }
        if ((_BC01.Item & 512) > 0) {
            _BC01.GLU = (byte) (((_data[11] & 255) >> 4) & 7);
            _BC01.GLU1 = ((_data[20] & 255) | ((_data[21] & 255) << 8)) & 1023;
            _BC01.GLU1_Real = 0;
            _BC01.GLU_Real = 0;
        } else {
            _BC01.GLU = 9;
            _BC01.GLU_Real = 9;
            _BC01.GLU1 = 999;
            _BC01.GLU1_Real = 999;
        }
        if ((_BC01.Item & 256) > 0) {
            _BC01.PRO = (byte) (_data[12] & 255 & 7);
            _BC01.PRO1 = ((_data[22] & 255) | ((_data[23] & 255) << 8)) & 511;
            _BC01.PRO1_Real = 0;
            _BC01.PRO_Real = 0;
        } else {
            _BC01.PRO = 9;
            _BC01.PRO_Real = 9;
            _BC01.PRO1 = 999;
            _BC01.PRO1_Real = 999;
        }
        if ((_BC01.Item & 128) > 0) {
            _BC01.PH = (byte) (((_data[12] & 255) >> 3) & 7);
            _BC01.PH1 = ((_data[23] & 255) >> 1) & 63;
            _BC01.PH1_Real = 0;
            _BC01.PH_Real = 0;
        } else {
            _BC01.PH = 9;
            _BC01.PH_Real = 9;
            _BC01.PH1 = 999;
            _BC01.PH1_Real = 999;
        }
        if ((_BC01.Item & 64) > 0) {
            _BC01.NIT = (byte) ((((_data[12] & 255) >> 6) | ((_data[13] & 255) << 2)) & 7);
            _BC01.NIT1 = (byte) (_data[24] & 255 & 31);
            _BC01.NIT1_Real = 0;
            _BC01.NIT_Real = 0;
        } else {
            _BC01.NIT = 9;
            _BC01.NIT_Real = 9;
            _BC01.NIT1 = 999;
            _BC01.NIT1_Real = 999;
        }
        if ((_BC01.Item & 32) > 0) {
            _BC01.LEU = (byte) (((_data[13] & 255) >> 1) & 7);
            _BC01.LEU1 = (((_data[24] & 255) >> 5) | ((_data[25] & 255) << 3)) & Constants.V_CHANGE_USER_SUCCESS;
            _BC01.LEU1_Real = 0;
            _BC01.LEU_Real = 0;
        } else {
            _BC01.LEU = 9;
            _BC01.LEU_Real = 9;
            _BC01.LEU1 = 999;
            _BC01.LEU1_Real = 999;
        }
        if ((_BC01.Item & 16) > 0) {
            _BC01.SG = (byte) (((_data[13] & 255) >> 4) & 7);
            _BC01.SG1 = _data[26] & 255 & 31;
            _BC01.SG1_Real = 0;
            _BC01.SG_Real = 0;
        } else {
            _BC01.SG = 9;
            _BC01.SG_Real = 9;
            _BC01.SG1 = 999;
            _BC01.SG1_Real = 999;
        }
        if ((_BC01.Item & 8) > 0) {
            _BC01.VC = (byte) (_data[14] & 255 & 7);
            _BC01.VC1 = (((_data[26] & 255) >> 5) | ((_data[27] & 255) << 3)) & 63;
            _BC01.VC1_Real = 0;
            _BC01.VC_Real = 0;
        } else {
            _BC01.VC = 9;
            _BC01.VC_Real = 9;
            _BC01.VC1 = 999;
            _BC01.VC1_Real = 999;
        }
        if ((_BC01.Item & 4) > 0) {
            _BC01.MAL = (byte) (((_data[14] & 255) >> 3) & 7);
            _BC01.MAL1 = ((_data[27] & 255) >> 3) & 15;
            _BC01.MAL1_Real = 0;
            _BC01.MAL_Real = 0;
        } else {
            _BC01.MAL = 9;
            _BC01.MAL_Real = 9;
            _BC01.MAL1 = 999;
            _BC01.MAL1_Real = 999;
        }
        if ((_BC01.Item & 2) > 0) {
            _BC01.CR = (byte) ((((_data[14] & 255) >> 6) | ((_data[15] & 255) << 2)) & 7);
            _BC01.CR1 = ((_data[28] & 255) | ((_data[29] & 255) << 8)) & 511;
            _BC01.CR1_Real = 0;
            _BC01.CR_Real = 0;
        } else {
            _BC01.CR = 9;
            _BC01.CR_Real = 9;
            _BC01.CR1 = 999;
            _BC01.CR1_Real = 999;
        }
        if ((_BC01.Item & 1) > 0) {
            _BC01.UCA = (byte) (((_data[15] & 255) >> 1) & 7);
            _BC01.UCA1 = ((_data[29] & 255) >> 1) & 127;
            _BC01.UCA1_Real = 0;
            _BC01.UCA_Real = 0;
        } else {
            _BC01.UCA = 9;
            _BC01.UCA_Real = 9;
            _BC01.UCA1 = 999;
            _BC01.UCA1_Real = 999;
        }
        return _BC01;
    }
}
