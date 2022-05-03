package com.contec.sp10.code;

import android.util.Log;
import com.alibaba.cchannel.CloudChannel;
import com.contec.phms.util.Constants;
import java.util.ArrayList;
import u.aly.dp;

public class PackManagerSpirometer {
    private static String TAG = PackManagerSpirometer.class.getSimpleName();
    static int WaveLength;
    private static boolean ifWave = false;
    static byte[] mWaveData;
    int _packCount = 0;
    private boolean bGetPackId = false;
    int byte1_num;
    private byte[] curPack = new byte[64];
    int i;
    int k = 0;
    int len = 0;
    int mCount;
    int mCount_ECG;
    int mDataLength = 0;
    public ArrayList<StructData> mDataList;
    byte mDay;
    public String mDeviceData;
    public ArrayList<DeviceDataJar> mDeviceDataJarsList = new ArrayList<>();
    byte mHour;
    int mIndex;
    boolean mIsWaveData = false;
    byte mMinu;
    byte mMonth;
    int mPackCount;
    public ParamInfo mParamInfo;
    public Sp10PatientInfo mPatientInfo;
    byte mSeco;
    int mSegmentLen;
    public SerialNumber mSerial;
    public byte[] mWaveLengthSize;
    int mWaveLengthnum;
    byte mYear;
    public ArrayList<Object> minDatas = new ArrayList<>();
    public int scaler;
    public byte[] storage;
    public long[] timeData;
    byte value;

    public PackManagerSpirometer() {
        init();
    }

    public byte arrangeMessage(byte[] buf, int length) {
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
            case -43:
                return 12;
            case Constants.UPDATEXML_UPLOAD_FAIL:
                return 6;
            case Constants.GENERATE_XML_FAIL:
                return 33;
            case Constants.THREAD_OUT:
                return this.mWaveLengthSize.length;
            case Constants.UPDATE_XML_FAIL:
                return 3;
            case -14:
                return 8;
            case -13:
                return 3;
            case -10:
                return 3;
            case -8:
                return 10;
            case -7:
                return 10;
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
                Log.e("肺活量数据存储空间操作成功", "---------------");
                this.storage = new byte[4];
                this.storage[0] = (byte) ((((pack[2] & 255) << 7) | (pack[1] & 255)) & 2047);
                this.storage[1] = (byte) ((((pack[4] & 255) << 7) | (pack[3] & 255)) & 2047);
                System.out.println(String.valueOf(this.storage[0]) + "****" + this.storage[1] + "****" + this.storage[2] + "****" + this.storage[3]);
                return 19;
            case Constants.GENERATE_XML_FAIL:
                Log.e("肺活量基本信息部分操作成功", "---------------");
                WaveLength = (((((byte) (pack[31] | ((pack[25] & 32) << 2))) & 255) << 8) | (((byte) (pack[30] | ((pack[25] & dp.n) << 3))) & 255)) & 65535;
                WaveLength *= 4;
                if (WaveLength % 7 == 0) {
                    this.byte1_num = WaveLength / 7;
                } else {
                    this.byte1_num = (WaveLength / 7) + 1;
                }
                this.mWaveLengthnum = WaveLength + this.byte1_num + 2;
                this.mWaveLengthSize = new byte[this.mWaveLengthnum];
                this.mPatientInfo.initInfo(pack);
                this.mPatientInfo.initOtherInfo(pack);
                this.mPatientInfo.mYear = pack[1] & 255;
                this.mPatientInfo.mMonth = pack[2] & 255;
                this.mPatientInfo.mDay = pack[3] & 255;
                this.mPatientInfo.mHour = pack[4] & 255;
                this.mPatientInfo.mMin = pack[5] & 255;
                this.mPatientInfo.mSecond = pack[6] & 255;
                Log.e("current time: ", "year: " + this.mPatientInfo.mYear + " month: " + this.mPatientInfo.mMonth + " day: " + this.mPatientInfo.mDay + " hour: " + this.mPatientInfo.mHour + " minute: " + this.mPatientInfo.mMin + " second: " + this.mPatientInfo.mSecond);
                this.mDeviceData = this.mParamInfo.initInfo1(pack);
                if (this.mParamInfo.mFVC > 10.0d || this.mParamInfo.mPEF > 16.0d) {
                    this.mPatientInfo.mCaseFlag = 0;
                } else {
                    this.mPatientInfo.mCaseFlag = 1;
                }
                this.mParamInfo.initInfo2(pack);
                return 20;
            case Constants.THREAD_OUT:
                Log.e("肺活量波形数据部分操作成功", "---------------");
                dealWaveData(pack, pack.length);
                return 21;
            case Constants.UPDATE_XML_FAIL:
                if (!Check(pack)) {
                    Log.e("肺活量删除数据操作失败", "---------------");
                    return 33;
                } else if ((pack[1] & 255) == 0) {
                    Log.e("肺活量删除数据操作成功", "---------------");
                    return 32;
                } else {
                    Log.e("肺活量删除数据操作失败", "---------------");
                    return 33;
                }
            case -13:
                if (!Check(pack)) {
                    return CloudChannel.SDK_VERSION;
                }
                if ((pack[1] & 255) == 0) {
                    Log.e("肺活量对时操作成功", "---------------");
                    return dp.n;
                }
                Log.e("肺活量对时操作失败", "---------------");
                return CloudChannel.SDK_VERSION;
            default:
                return 0;
        }
    }

    private void dealWaveData(byte[] pack, int count) {
        byte[] wareData = unPackWaveData(pack, count);
        int[] waveAray = new int[(mWaveData.length / 4)];
        int j = 0;
        for (int i = 0; i < wareData.length; i++) {
            if (i % 4 == 0 && i <= wareData.length - 4) {
                waveAray[j] = ((wareData[i + 3] & 255) << 24) | ((wareData[i + 2] & 255) << dp.n) | ((wareData[i + 1] & 255) << 8) | (wareData[i] & 255 & -1);
                j++;
            }
        }
        for (int AIRFLOW_STREN2 : waveAray) {
            StructData sd = new StructData();
            sd.mData = AIRFLOW_STREN2 - 80000;
            int i2 = this.mIndex;
            this.mIndex = i2 + 1;
            sd.mIndex = i2;
            this.mDataList.add(sd);
        }
        saveUserCase();
        this.mWaveLengthSize = null;
        this._packCount = 0;
        this.mDataLength = 0;
    }

    private long getTimeData(byte[] pack) {
        byte[] data = new byte[6];
        for (int i = 0; i < 6; i++) {
            data[i] = pack[i];
        }
        return JNI.GetData(data, 6);
    }

    public void saveUserCase() {
        DeviceDataJar mDeviceDataJar = new DeviceDataJar();
        mDeviceDataJar.mPatientInfo = this.mPatientInfo;
        mDeviceDataJar.mParamInfo = this.mParamInfo;
        mDeviceDataJar.mDataList = this.mDataList;
        mDeviceDataJar.mSerial = this.mSerial;
        mDeviceDataJar.mScaler = this.scaler;
        mDeviceDataJar.mData = this.timeData;
        this.mDeviceDataJarsList.add(mDeviceDataJar);
        init();
    }

    private void init() {
        this.mSerial = new SerialNumber();
        this.mParamInfo = new ParamInfo();
        this.mPatientInfo = new Sp10PatientInfo();
        this.mDataList = new ArrayList<>();
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

    static byte[] unPackWaveData(byte[] pack, int len2) {
        mWaveData = new byte[WaveLength];
        int mNum = (len2 - 2) % 8;
        int j = 0;
        for (int i = 0; i <= len2 - 2; i++) {
            if (i % 8 == 0 && i <= (len2 - 2) - 8) {
                byte IndexHeight = pack[i + 1];
                mWaveData[j] = (byte) ((pack[i + 2] | ((IndexHeight & 1) << 7)) & 255);
                mWaveData[j + 1] = (byte) ((pack[i + 3] | ((IndexHeight & 2) << 6)) & 255);
                mWaveData[j + 2] = (byte) ((pack[i + 4] | ((IndexHeight & 4) << 5)) & 255);
                mWaveData[j + 3] = (byte) (pack[i + 5] | (((IndexHeight & 8) << 4) & 255));
                mWaveData[j + 4] = (byte) ((pack[i + 6] | ((IndexHeight & dp.n) << 3)) & 255);
                mWaveData[j + 5] = (byte) ((pack[i + 7] | ((IndexHeight & 32) << 2)) & 255);
                mWaveData[j + 6] = (byte) ((pack[i + 8] | ((IndexHeight & 64) << 1)) & 255);
                j += 7;
            } else if ((len2 - 2) - i == mNum && i <= (len2 - 2) - mNum) {
                if (mNum == 1) {
                    byte IndexHeight2 = pack[i + 1];
                } else if (mNum == 2) {
                    mWaveData[j] = (byte) (pack[i + 2] | ((pack[i + 1] & 1) << 7));
                } else if (mNum == 3) {
                    byte IndexHeight3 = pack[i + 1];
                    mWaveData[j] = (byte) (pack[i + 2] | ((IndexHeight3 & 1) << 7));
                    mWaveData[j + 1] = (byte) (pack[i + 3] | ((IndexHeight3 & 2) << 6));
                } else if (mNum == 4) {
                    byte IndexHeight4 = pack[i + 1];
                    mWaveData[j] = (byte) (pack[i + 2] | ((IndexHeight4 & 1) << 7));
                    mWaveData[j + 1] = (byte) (pack[i + 3] | ((IndexHeight4 & 2) << 6));
                    mWaveData[j + 2] = (byte) (pack[i + 4] | ((IndexHeight4 & 4) << 5));
                } else if (mNum == 5) {
                    byte IndexHeight5 = pack[i + 1];
                    mWaveData[j] = (byte) (pack[i + 2] | ((IndexHeight5 & 1) << 7));
                    mWaveData[j + 1] = (byte) (pack[i + 3] | ((IndexHeight5 & 2) << 6));
                    mWaveData[j + 2] = (byte) (pack[i + 4] | ((IndexHeight5 & 4) << 5));
                    mWaveData[j + 3] = (byte) (pack[i + 5] | ((IndexHeight5 & 8) << 4));
                } else if (mNum == 6) {
                    byte IndexHeight6 = pack[i + 1];
                    mWaveData[j] = (byte) (pack[i + 2] | ((IndexHeight6 & 1) << 7));
                    mWaveData[j + 1] = (byte) (pack[i + 3] | ((IndexHeight6 & 2) << 6));
                    mWaveData[j + 2] = (byte) (pack[i + 4] | ((IndexHeight6 & 4) << 5));
                    mWaveData[j + 3] = (byte) (pack[i + 5] | ((IndexHeight6 & 8) << 4));
                    mWaveData[j + 4] = (byte) (pack[i + 6] | ((IndexHeight6 & dp.n) << 3));
                } else if (mNum == 7) {
                    byte IndexHeight7 = pack[i + 1];
                    mWaveData[j] = (byte) (pack[i + 2] | ((IndexHeight7 & 1) << 7));
                    mWaveData[j + 1] = (byte) (pack[i + 3] | ((IndexHeight7 & 2) << 6));
                    mWaveData[j + 2] = (byte) (pack[i + 4] | ((IndexHeight7 & 4) << 5));
                    mWaveData[j + 3] = (byte) (pack[i + 5] | ((IndexHeight7 & 8) << 4));
                    mWaveData[j + 4] = (byte) (pack[i + 6] | ((IndexHeight7 & dp.n) << 3));
                    mWaveData[j + 5] = (byte) (pack[i + 7] | ((IndexHeight7 & 32) << 2));
                }
            }
        }
        for (int n = 0; n < mWaveData.length; n++) {
        }
        return mWaveData;
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
