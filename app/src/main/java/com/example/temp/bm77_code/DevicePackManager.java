package com.example.temp.bm77_code;

import android.util.Log;
import com.contec.phms.manager.message.OrderList;
import com.example.temp.bean.EarTempertureDataJar;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import u.aly.bs;
import u.aly.dp;

public class DevicePackManager {
    private static final int DEL_FA = 6;
    private static final int DEL_SU = 5;
    private static final int NEW_DEVICE = 9;
    private static final int NO_DATA = 7;
    private static final int OLD_DEVICE = 8;
    private static final int RECEIVE_DATA_FA = 2;
    private static final int RECEIVE_DATA_SU = 1;
    private static final int SET_TIME_FA = 4;
    private static final int SET_TIME_SU = 3;
    private static final String TAG = "jar.EarTemperture.DevicePackManager";
    public static byte[] m_SetTime = new byte[5];
    boolean bGetPackId;
    byte[] curPack;
    int i;
    int k;
    int len;
    private byte mCommandData;
    private byte mCommandDel;
    private byte mCommandTime;
    private int mCount;
    public String mDeviceTime;
    private int mEarDataNum;
    private byte[] mPack;
    private int mPackLen;
    private int mPosition;
    private byte m_CheckData;
    public ArrayList<EarTempertureDataJar> m_DeviceDatas;
    private Map<String, Integer> m_WCmd;
    public byte[] m_returnTimeByteArry;
    byte value;

    public DevicePackManager() {
        this.mPack = new byte[1024];
        this.m_DeviceDatas = new ArrayList<>();
        this.bGetPackId = false;
        this.curPack = new byte[64];
        this.k = 0;
        this.len = 0;
        this.m_DeviceDatas = new ArrayList<>();
    }

    public int arrangeMessage(byte[] buf, int length) {
        int _return = 0;
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
                        _return = processData1(this.curPack);
                    }
                } else {
                    this.bGetPackId = false;
                }
            } else if (this.value >= 0 && PackLen(this.value) > 0) {
                this.bGetPackId = true;
                this.k = 0;
                this.len = PackLen(this.value);
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

    public int processData1(byte[] pack) {
        switch (pack[0] & 255) {
            case 32:
                Log.e(TAG, "--------------------->>:packet received");
                return processData2(pack);
            case 33:
                Log.d(TAG, "********receive ear tempture type: " + (pack[2] & 127));
                return 8;
            case 34:
                int mEarType = pack[2] & 127;
                if (mEarType == 0) {
                    Log.d(TAG, "********set time  success: " + mEarType);
                    return 3;
                } else if (mEarType != 1) {
                    return 0;
                } else {
                    Log.d(TAG, "********set time  failed: " + mEarType);
                    return 4;
                }
            case OrderList.DS_FILTER_DATAS:
                this.mDeviceTime = processTimeStrings(pack, this.mCount);
                return 10;
            case OrderList.DS_SAVE_SDCARD:
                int mEarType2 = pack[2] & 127;
                if (mEarType2 == 0) {
                    Log.d(TAG, "********delete success: " + mEarType2);
                    return 5;
                } else if (mEarType2 != 1) {
                    return 0;
                } else {
                    Log.d(TAG, "********delete  failed: " + mEarType2);
                    return 6;
                }
            case 37:
                int mEarType3 = pack[2] & 127;
                if (mEarType3 == 0) {
                    Log.d(TAG, "********delete success: " + mEarType3);
                    return 13;
                } else if (mEarType3 != 1) {
                    return 0;
                } else {
                    Log.d(TAG, "********delete  failed: " + mEarType3);
                    return 14;
                }
            case OrderList.DS_PROCCESS_RESULT:
                int mEarType4 = pack[2] & 127;
                if (mEarType4 == 0) {
                    Log.d(TAG, "********delete success: " + mEarType4);
                    return 11;
                } else if (mEarType4 != 1) {
                    return 0;
                } else {
                    Log.d(TAG, "********delete  failed: " + mEarType4);
                    return 12;
                }
            case OrderList.DS_FINISHED:
                this.mEarDataNum = ((((byte) (pack[1] & 1)) << 7) | (pack[2] & Byte.MAX_VALUE)) & 255;
                Log.d(TAG, "********mEarDataNum: " + this.mEarDataNum);
                Log.e(TAG, "********mEarDataNum: " + this.mEarDataNum);
                if (this.mEarDataNum == 0) {
                    return 7;
                }
                if (this.mEarDataNum > 0) {
                    return 9;
                }
                return 0;
            default:
                return 0;
        }
    }

    public int PackLen(int pHead) {
        switch (pHead) {
            case 32:
                return 9;
            case 33:
                return 3;
            case 34:
                return 3;
            case OrderList.DS_FILTER_DATAS:
                return 6;
            case OrderList.DS_SAVE_SDCARD:
                return 3;
            case 37:
                return 3;
            case OrderList.DS_PROCCESS_RESULT:
                return 3;
            case OrderList.DS_FINISHED:
                return 3;
            default:
                return 0;
        }
    }

    public int arrangeMessage1(byte[] buf, int length) {
        int _isover = 0;
        for (int i = 0; i < length; i++) {
            switch (buf[0] & 255) {
                case 32:
                    Log.e(TAG, "--------------------->>packet received");
                    byte[] bArr = this.mPack;
                    int i2 = this.mCount;
                    this.mCount = i2 + 1;
                    bArr[i2] = buf[i];
                    if (this.mCount != this.mEarDataNum * 9) {
                        break;
                    } else {
                        _isover = processData(this.mPack);
                        this.mCount = 0;
                        break;
                    }
                case 33:
                    byte[] bArr2 = this.mPack;
                    int i3 = this.mCount;
                    this.mCount = i3 + 1;
                    bArr2[i3] = buf[i];
                    if (this.mCount != 3) {
                        break;
                    } else {
                        Log.d(TAG, "********receive ear tempture type: " + (this.mPack[2] & 127));
                        _isover = 8;
                        this.mCount = 0;
                        break;
                    }
                case 34:
                    byte[] bArr3 = this.mPack;
                    int i4 = this.mCount;
                    this.mCount = i4 + 1;
                    bArr3[i4] = buf[i];
                    if (this.mCount == 3) {
                        int mEarType = this.mPack[2] & 127;
                        if (mEarType == 0) {
                            Log.d(TAG, "********set time  success: " + mEarType);
                            _isover = 3;
                        } else if (mEarType == 1) {
                            _isover = 4;
                            Log.d(TAG, "********set time  failed: " + mEarType);
                        }
                        this.mCount = 0;
                        break;
                    } else {
                        break;
                    }
                case OrderList.DS_FILTER_DATAS:
                    byte[] bArr4 = this.mPack;
                    int i5 = this.mCount;
                    this.mCount = i5 + 1;
                    bArr4[i5] = buf[i];
                    if (this.mCount != 6) {
                        break;
                    } else {
                        this.mDeviceTime = processTimeStrings(this.mPack, this.mCount);
                        this.mCount = 0;
                        _isover = 10;
                        break;
                    }
                case OrderList.DS_SAVE_SDCARD:
                    byte[] bArr5 = this.mPack;
                    int i6 = this.mCount;
                    this.mCount = i6 + 1;
                    bArr5[i6] = buf[i];
                    if (this.mCount == 3) {
                        int mEarType2 = this.mPack[2] & 127;
                        if (mEarType2 == 0) {
                            Log.d(TAG, "********delete success: " + mEarType2);
                            _isover = 5;
                        } else if (mEarType2 == 1) {
                            _isover = 6;
                            Log.d(TAG, "********delete  failed: " + mEarType2);
                        }
                        this.mCount = 0;
                        break;
                    } else {
                        break;
                    }
                case 37:
                    byte[] bArr6 = this.mPack;
                    int i7 = this.mCount;
                    this.mCount = i7 + 1;
                    bArr6[i7] = buf[i];
                    if (this.mCount == 3) {
                        int mEarType3 = this.mPack[2] & 127;
                        if (mEarType3 == 0) {
                            Log.d(TAG, "********delete success: " + mEarType3);
                            _isover = 13;
                        } else if (mEarType3 == 1) {
                            _isover = 14;
                            Log.d(TAG, "********delete  failed: " + mEarType3);
                        }
                        this.mCount = 0;
                        break;
                    } else {
                        break;
                    }
                case OrderList.DS_PROCCESS_RESULT:
                    byte[] bArr7 = this.mPack;
                    int i8 = this.mCount;
                    this.mCount = i8 + 1;
                    bArr7[i8] = buf[i];
                    if (this.mCount == 3) {
                        int mEarType4 = this.mPack[2] & 127;
                        if (mEarType4 == 0) {
                            Log.d(TAG, "********delete success: " + mEarType4);
                            _isover = 11;
                        } else if (mEarType4 == 1) {
                            _isover = 12;
                            Log.d(TAG, "********delete  failed: " + mEarType4);
                        }
                        this.mCount = 0;
                        break;
                    } else {
                        break;
                    }
                case OrderList.DS_FINISHED:
                    byte[] bArr8 = this.mPack;
                    int i9 = this.mCount;
                    this.mCount = i9 + 1;
                    bArr8[i9] = buf[i];
                    if (this.mCount != 3) {
                        break;
                    } else {
                        this.mEarDataNum = this.mPack[2] & Byte.MAX_VALUE;
                        Log.d(TAG, "********mEarDataNum: " + this.mEarDataNum);
                        Log.e(TAG, "********mEarDataNum: " + this.mEarDataNum);
                        if (this.mEarDataNum == 0) {
                            _isover = 7;
                        } else if (this.mEarDataNum > 0) {
                            _isover = 9;
                        }
                        this.mCount = 0;
                        break;
                    }
            }
        }
        return _isover;
    }

    private static String processTimeStrings(byte[] pack, int length) {
        printPack(pack, length);
        Calendar cl = Calendar.getInstance();
        cl.set(2000, 0, 1, 0, 0, 0);
        Date startDate = cl.getTime();
        long _benchmarkSecond = startDate.getTime();
        long timeMillis = (long) ((((byte) ((pack[1] & Byte.MAX_VALUE) & 1)) << 7) | (pack[2] & Byte.MAX_VALUE) | (((((byte) (((pack[1] & Byte.MAX_VALUE) >> 1) & 1)) << 7) | (pack[3] & Byte.MAX_VALUE)) << 8) | (((((byte) (((pack[1] & Byte.MAX_VALUE) >> 2) & 1)) << 7) | (pack[4] & Byte.MAX_VALUE)) << dp.n) | (((((byte) (((pack[1] & Byte.MAX_VALUE) >> 3) & 1)) << 7) | (pack[5] & Byte.MAX_VALUE)) << 24));
        long _scond = (1000 * timeMillis) + _benchmarkSecond;
        Calendar c2 = Calendar.getInstance();
        c2.setTimeInMillis(_scond);
        Date startDate2 = c2.getTime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dataDate = simpleDateFormat.format(startDate);
        String dataDate2 = simpleDateFormat.format(startDate2);
        Log.d(TAG, "send second  _scond: " + _scond + "  timeMillis:" + timeMillis + "  _benchmarkSecond:" + _benchmarkSecond + " start date :" + dataDate + "  dataDate2:" + dataDate2);
        return dataDate2;
    }

    public int processData(byte[] pack) {
        byte[] _tempPack = new byte[9];
        int _count = 0;
        for (int i = 0; i < this.mEarDataNum; i++) {
            int j = 0;
            while (j < _tempPack.length) {
                _tempPack[j] = pack[_count];
                j++;
                _count++;
            }
            _count = (i * 9) + 9;
            addData(new EarTempertureDataJar(_tempPack));
        }
        if (this.m_DeviceDatas.size() == this.mEarDataNum) {
            return 1;
        }
        return 2;
    }

    public int processData2(byte[] pack) {
        addData(new EarTempertureDataJar(pack));
        if (this.m_DeviceDatas.size() == this.mEarDataNum) {
            return 1;
        }
        return 2;
    }

    public void addData(EarTempertureDataJar deviceData) {
        this.m_DeviceDatas.add(deviceData);
        Log.e(TAG, "****************----->>: " + this.m_DeviceDatas.size());
    }

    static void printPack(byte pack) {
        Log.i(TAG, "this is printpack :" + (String.valueOf(String.valueOf(bs.b) + Integer.toHexString(pack & 255)) + " "));
    }

    public static void printPack(byte[] pack, int p_length) {
        if (pack == null) {
            Log.i(TAG, "param pack is null");
            return;
        }
        String packStr = bs.b;
        for (int i = 0; i < p_length; i++) {
            packStr = String.valueOf(String.valueOf(packStr) + Integer.toHexString(pack[i])) + " ";
        }
        Log.i(TAG, "this is printpack :" + packStr + "\n=======================");
    }
}
