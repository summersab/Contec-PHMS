package com.contect.jar.blewt;

import android.util.Log;
import com.alibaba.cchannel.CloudChannel;
import com.contec.phms.util.Constants;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import cn.com.contec.jar.wt100.WTDeviceDataJar;
import u.aly.dp;

public class PackManagerHealthy {
    protected static int mCount;
    protected static int mPackLen;
    public static byte[] mSynchronizationTime = new byte[5];
    int _packCount = 0;
    private HashMap<String, String> _resultmap = new HashMap<>();
    private boolean bGetPackId = false;
    byte[] curPack = new byte[64];
    int i;
    int k = 0;
    int len = 0;
    protected byte[] mPack = new byte[1024];
    private ArrayList<String> mReceiveContainer = new ArrayList<>();
    public WTDeviceDataJar mWeight;
    public ArrayList<WTDeviceDataJar> m_DeviceDatas = new ArrayList<>();
    byte value;

    public byte arrangeMessage(byte[] buf, int length) {
        Log.e("arrangeMessage", "-------����--------");
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
                return 5;
            case Constants.GENERATE_XML_FAIL:
                return 12;
            case Constants.THREAD_OUT:
                return 12;
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
                this.mWeight = new WTDeviceDataJar();
                this.mWeight.datanumber = new byte[5];
                this.mWeight.datanumber[0] = pack[1];
                Log.e("0xE00xE00xE00xE00xE00xE0", "mWeight.datanumber[0]" + this.mWeight.datanumber[0]);
                this.mWeight.datanumber[1] = pack[2];
                this.mWeight.datanumber[2] = pack[3];
                return 18;
            case Constants.GENERATE_XML_FAIL:
                Log.e("===������������", "###########������������");
                if ((pack[1] & 64) == 64) {
                    return 19;
                }
                return 21;
            case Constants.THREAD_OUT:
                Log.e("===ȫ����������", "###########ȫ����������");
                this._packCount++;
                float allweight = new BigDecimal((double) (((float) (((pack[9] | (((pack[3] & 255) & 32) << 2)) << 8) | (pack[10] | (((pack[3] & 255) & 64) << 1)))) / 100.0f)).setScale(2, 4).floatValue();
                String str = String.valueOf((byte) (pack[2] & 255)) + "-" + ((byte) (pack[3] & dp.m)) + "-" + ((byte) (pack[4] & 255)) + " " + ((byte) (pack[5] & 255)) + ":" + ((byte) (pack[6] & 255)) + ":" + ((byte) (pack[7] & 255)) + "  " + allweight;
                WTDeviceDataJar mWtDevice = new WTDeviceDataJar();
                mWtDevice.m_iYear = (byte) (pack[2] & 255);
                mWtDevice.m_iMonth = (byte) (pack[3] & dp.m);
                mWtDevice.m_iDay = (byte) (pack[4] & 255);
                mWtDevice.m_iHour = (byte) (pack[5] & 255);
                mWtDevice.m_iMinute = (byte) (pack[6] & 255);
                mWtDevice.m_iSecond = (byte) (pack[7] & 255);
                mWtDevice.mNum = (byte) (pack[8] & 255);
                mWtDevice.m_data = new StringBuilder().append(allweight).toString();
                mWtDevice.m_saveDate = getDateFormByte(mWtDevice.m_iYear, mWtDevice.m_iMonth, mWtDevice.m_iDay, mWtDevice.m_iHour, mWtDevice.m_iMinute, mWtDevice.m_iSecond);
                this.m_DeviceDatas.add(mWtDevice);
                if ((pack[1] & 64) == 64) {
                    this._packCount = 0;
                    return 23;
                } else if (this._packCount != 10) {
                    return 24;
                } else {
                    this._packCount = 0;
                    return 22;
                }
            case Constants.UPDATE_XML_FAIL:
                Log.e("===ɾ����������", "###########ɾ����������");
                if (!Check(pack) || (pack[1] & 255) != 0) {
                    return 33;
                }
                return 32;
            case -13:
                if (!Check(pack)) {
                    Log.e("===���ж�ʱ����ʧ��", "###########ʱ����ʧ��");
                    return CloudChannel.SDK_VERSION;
                } else if ((pack[1] & 255) == 0) {
                    Log.e("���ж�ʱ�����ɹ�", "###########��ʱ�����ɹ�");
                    return dp.n;
                } else {
                    Log.e("���ж�ʱ����ʧ��", "##########ʱ����ʧ��");
                    return CloudChannel.SDK_VERSION;
                }
            default:
                return 0;
        }
    }

    public static String getDateFormByte(byte pyear, byte pmonth, byte pday, byte phour, byte pmin, byte pss) {
        StringBuffer _date = new StringBuffer();
        if (pyear < 10) {
            _date.append("200" + pyear);
        } else {
            _date.append("20" + pyear);
        }
        _date.append("-");
        if (pmonth < 10) {
            _date.append("0" + pmonth);
        } else {
            _date.append(pmonth);
        }
        _date.append("-");
        if (pday < 10) {
            _date.append("0" + pday);
        } else {
            _date.append(pday);
        }
        _date.append(" ");
        if (phour < 10) {
            _date.append("0" + phour);
        } else {
            _date.append(phour);
        }
        _date.append(":");
        if (pmin < 10) {
            _date.append("0" + pmin);
        } else {
            _date.append(pmin);
        }
        _date.append(":");
        if (pss < 10) {
            _date.append("0" + pss);
        } else {
            _date.append(pss);
        }
        return _date.toString();
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
            int i = j + 4;
            pack[i] = (byte) (pack[i] | ((pack[2] << (7 - j)) & 128));
        }
        for (int j2 = 0; j2 < 5; j2++) {
            int i2 = j2 + 11;
            pack[i2] = (byte) (pack[i2] | ((pack[3] << (7 - j2)) & 128));
        }
        return pack;
    }
}
