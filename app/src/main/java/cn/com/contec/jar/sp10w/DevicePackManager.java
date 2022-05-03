package cn.com.contec.jar.sp10w;

import android.util.Log;
import com.alibaba.cchannel.CloudChannel;
import com.contec.phms.util.Constants;
import java.util.ArrayList;
import cn.com.contec.jar.cases.ParamInfo;
import cn.com.contec.jar.cases.SerialNumber;
import cn.com.contec.jar.cases.Sp10PatientInfo;
import cn.com.contec.jar.cases.StructData;
import u.aly.dp;

public class DevicePackManager {
    private static final int CHECK_DATE = 13;
    private static final int CHECK_TIME = 12;
    private static final int DEL_FA = 6;
    private static final int DEL_SU = 5;
    private static final int RECEIVE_DATA_SU = 1;
    private static final int RECEIVE_NO_DATA = 2;
    private static final int SET_DATE_FA = 8;
    private static final int SET_DATE_SU = 7;
    private static final int SET_TIME_FA = 4;
    private static final int SET_TIME_SU = 3;
    private static final String TAG = "cn.com.contec.jar.Sp10w.DevicePackManager";
    public static byte[] mSynchronizationDate = new byte[5];
    public static byte[] mSynchronizationTime = new byte[3];
    private boolean bGetPackId = false;
    byte[] curPack = new byte[64];
    int i;
    int k = 0;
    int len = 0;
    private int mCount;
    public ArrayList<StructData> mDataList;
    public ArrayList<DeviceDataJar> mDeviceDataJarsList = new ArrayList<>();
    int mIndex;
    private byte[] mPack = new byte[1024];
    public ParamInfo mParamInfo;
    public Sp10PatientInfo mPatientInfo;
    public SerialNumber mSerial;
    private int m_num;
    byte[] unPackParamInfo;
    byte value;

    public DevicePackManager() {
        init();
    }

    public int arrangeMessage(byte[] buf, int length, boolean isBle) {
        if (!isBle) {
            int _isover = 0;
            if (buf[0] == 19) {
                this.m_num = 13;
            } else if (buf[0] == 18) {
                this.m_num = 12;
            }
            if (this.m_num != 0) {
                return processDateTime(buf, length, 0);
            }
            for (int i = 0; i < length; i++) {
                byte _value = buf[i];
                if (_value >= 0 && this.mCount > 0) {
                    processData(this.mPack);
                    this.mCount = 0;
                    if (_value == 7) {
                        _isover = 1;
                    }
                } else if (_value == 7 && this.mCount == 0) {
                    _isover = 2;
                }
                byte[] bArr = this.mPack;
                int i2 = this.mCount;
                this.mCount = i2 + 1;
                bArr[i2] = _value;
                if (_value == 10) {
                    this.mCount = 0;
                    _isover = 5;
                }
            }
            return _isover;
        }
        byte _return = 0;
        this.i = 0;
        while (this.i < length) {
            this.value = buf[this.i];
            if (this.bGetPackId) {
                byte[] bArr2 = this.curPack;
                int i3 = this.k;
                this.k = i3 + 1;
                bArr2[i3] = this.value;
                if (this.k >= this.len) {
                    this.bGetPackId = false;
                    _return = processBleData(this.curPack);
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
                    byte[] bArr3 = this.curPack;
                    int i4 = this.k;
                    this.k = i4 + 1;
                    bArr3[i4] = this.value;
                    if (this.len == 1) {
                        _return = processBleData(this.curPack);
                        this.bGetPackId = false;
                    }
                }
            }
            this.i++;
        }
        return _return;
    }

    private int processDateTime(byte[] buf, int length, int _isover) {
        if (this.m_num == 13) {
            for (int i = 0; i < length; i++) {
                byte _value = buf[i];
                byte[] bArr = this.mPack;
                int i2 = this.mCount;
                this.mCount = i2 + 1;
                bArr[i2] = _value;
                if (this.mCount == 7) {
                    Log.i(TAG, "process date data");
                    _isover = processData(this.mPack, length);
                    this.mCount = 0;
                }
            }
        } else {
            for (int i3 = 0; i3 < length; i3++) {
                byte _value2 = buf[i3];
                byte[] bArr2 = this.mPack;
                int i4 = this.mCount;
                this.mCount = i4 + 1;
                bArr2[i4] = _value2;
                if (this.mCount == 5) {
                    Log.i(TAG, "process time data ");
                    _isover = processData(this.mPack, length);
                    this.mCount = 0;
                    this.m_num = 0;
                }
            }
        }
        return _isover;
    }

    public void processData(byte[] pack) {
        byte[] pack2 = unPack(trimPack(pack));
        switch (pack2[0]) {
            case 1:
                this.mIndex = 0;
                Log.i("CaseID", "strpack**************" + pack2[2]);
                this.mPatientInfo.initInfo(pack2);
                return;
            case 2:
                this.mPatientInfo.initOtherInfo(pack2);
                this.mPatientInfo.mYear = (pack2[3] & 255) | ((pack2[4] & 255) << 8);
                if (this.mPatientInfo.mYear > 2572) {
                    this.mPatientInfo.mYear = pack2[3];
                    this.mPatientInfo.mSecond = pack2[4] - 10;
                } else {
                    this.mPatientInfo.mSecond = 0;
                }
                this.mPatientInfo.mMonth = (byte) (pack2[5] & 255);
                this.mPatientInfo.mDay = pack2[6] & 255;
                this.mPatientInfo.mHour = (byte) (pack2[7] & 255);
                this.mPatientInfo.mMin = (byte) (pack2[8] & 255);
                return;
            case 3:
                this.mParamInfo.initInfo1(pack2);
                if (this.mParamInfo.mFVC > 10.0d || this.mParamInfo.mPEF > 16.0d) {
                    this.mPatientInfo.mCaseFlag = 0;
                    return;
                } else {
                    this.mPatientInfo.mCaseFlag = 1;
                    return;
                }
            case 4:
                this.mParamInfo.initInfo2(pack2);
                return;
            case 5:
                StructData sd = new StructData();
                sd.mData = ((((pack2[2] & 255) | ((pack2[3] & 255) << 8)) | ((pack2[5] & 255) << 24)) | ((pack2[4] & 255) << dp.n)) - 80000;
                int i = this.mIndex;
                this.mIndex = i + 1;
                sd.mIndex = i;
                this.mDataList.add(sd);
                return;
            case 6:
                saveUserCase();
                return;
            case 8:
                int _count = (pack2[2] - 1) * 6;
                int i2 = 3;
                while (i2 < pack2.length) {
                    this.mSerial.mSerial[_count] = pack2[i2];
                    i2++;
                    _count++;
                }
                return;
            case 11:
                this.mParamInfo.mFEF50 = ((double) ((pack2[2] & 255) | ((pack2[3] & 255) << 8))) / 100.0d;
                Log.e("0x0b:", "*0x0b *********************" + this.mParamInfo.mFEF50);
                return;
            default:
                return;
        }
    }

    private String processDate(int mYear, int mMonth, int mDay, int mHour, int mMinute) {
        String _month;
        String _day;
        String _hour;
        String _minute;
        if (mMonth < 10) {
            _month = "0" + mMonth;
        } else {
            _month = new StringBuilder(String.valueOf(mMonth)).toString();
        }
        if (mDay < 10) {
            _day = "0" + mDay;
        } else {
            _day = new StringBuilder(String.valueOf(mDay)).toString();
        }
        if (mHour < 10) {
            _hour = "0" + mHour;
        } else {
            _hour = new StringBuilder(String.valueOf(mHour)).toString();
        }
        if (mHour < 10) {
            _minute = "0" + mMinute;
        } else {
            _minute = new StringBuilder(String.valueOf(mMinute)).toString();
        }
        return String.valueOf(mYear) + "-" + _month + "-" + _day + " " + _hour + ":" + _minute + ":00";
    }

    public void saveUserCase() {
        DeviceDataJar mDeviceDataJar = new DeviceDataJar();
        mDeviceDataJar.mPatientInfo = this.mPatientInfo;
        mDeviceDataJar.mParamInfo = this.mParamInfo;
        mDeviceDataJar.mDataList = this.mDataList;
        mDeviceDataJar.mSerial = this.mSerial;
        this.mDeviceDataJarsList.add(mDeviceDataJar);
        init();
    }

    private void init() {
        this.mSerial = new SerialNumber();
        this.mParamInfo = new ParamInfo();
        this.mPatientInfo = new Sp10PatientInfo();
        this.mDataList = new ArrayList<>();
    }

    public byte[] unPack(byte[] pack) {
        int n = pack.length;
        for (int i = 2; i < n; i++) {
            pack[i] = (byte) (pack[i] & ((byte) ((pack[1] << (9 - i)) | 127)));
        }
        return pack;
    }

    public int processData(byte[] p_pack, int p_lenght) {
        byte[] p_pack2 = trimPack(p_pack);
        if (p_pack2.length > 0) {
            p_pack2 = unPack(p_pack2);
        }
        switch (p_pack2[0]) {
            case 18:
                if (mSynchronizationTime[0] == p_pack2[2] && mSynchronizationTime[1] == p_pack2[3] && mSynchronizationTime[2] == p_pack2[4]) {
                    return 3;
                }
                return 4;
            case 19:
                if (mSynchronizationDate[0] == p_pack2[2] && mSynchronizationDate[1] == p_pack2[3] && mSynchronizationDate[2] == p_pack2[4] && mSynchronizationDate[3] == p_pack2[5] && mSynchronizationDate[4] == p_pack2[6]) {
                    return 7;
                }
                return 8;
            default:
                return 0;
        }
    }

    public byte[] trimPack(byte[] pack) {
        int _count = 0;
        int i = 0;
        while (i < pack.length && pack[i] != 0) {
            _count++;
            i++;
        }
        byte[] _trimedPack = new byte[_count];
        for (int i2 = 0; i2 < _trimedPack.length; i2++) {
            _trimedPack[i2] = pack[i2];
        }
        return _trimedPack;
    }

    public byte processBleData(byte[] pack) {
        switch (pack[0]) {
            case Constants.UPDATEXML_UPLOAD_FAIL /*-32*/:
                Log.e("肺活量数据存储空间操作成功", "---------------");
                byte[] storage = {(byte) (pack[1] & 255), (byte) ((pack[2] << 7) & 255), (byte) (pack[3] & 255), (byte) ((pack[4] << 7) & 255)};
                System.out.println(String.valueOf(storage[0]) + "****" + storage[1] + "****" + storage[2] + "****" + storage[3]);
                return 19;
            case Constants.GENERATE_XML_FAIL /*-31*/:
                Log.e("肺活量基本信息部分操作成功", "---------------");
                this.mPatientInfo.mYear = (byte) (pack[1] & 255);
                this.mPatientInfo.mMonth = (byte) (pack[2] & 255);
                this.mPatientInfo.mDay = (byte) (pack[3] & 255);
                this.mPatientInfo.mHour = (byte) (pack[4] & 255);
                this.mPatientInfo.mMin = (byte) (pack[5] & 255);
                this.mPatientInfo.mSecond = (byte) (pack[6] & 255);
                this.unPackParamInfo = unPackParamInfo(pack);
                this.mParamInfo.mFVC = ((double) (((this.unPackParamInfo[1] & 255) << 8) | ((this.unPackParamInfo[0] & 255) & 65535))) / 100.0d;
                this.mParamInfo.mFEV1 = ((double) (((this.unPackParamInfo[3] & 255) << 8) | ((this.unPackParamInfo[2] & 255) & 65535))) / 100.0d;
                this.mParamInfo.mFEV1Per = (double) (this.unPackParamInfo[4] & 255);
                this.mParamInfo.mPEF = ((double) (((this.unPackParamInfo[6] & 255) << 8) | ((this.unPackParamInfo[5] & 255) & 65535))) / 100.0d;
                this.mParamInfo.mFEF25 = ((double) (((this.unPackParamInfo[8] & 255) << 8) | ((this.unPackParamInfo[7] & 255) & 65535))) / 100.0d;
                this.mParamInfo.mFEF75 = ((double) (((this.unPackParamInfo[10] & 255) << 8) | ((this.unPackParamInfo[9] & 255) & 65535))) / 100.0d;
                this.mParamInfo.mFEF2575 = ((double) (((this.unPackParamInfo[12] & 255) << 8) | ((this.unPackParamInfo[11] & 255) & 65535))) / 100.0d;
                this.mParamInfo.mLength = ((this.unPackParamInfo[13] & 255) << 8) | (this.unPackParamInfo[14] & 255 & 65535);
                return 20;
            case Constants.THREAD_OUT /*-30*/:
                Log.e("肺活量波形数据部分操作成功", "---------------");
                int lowInt = ((((pack[7] | (pack[2] & 8)) & 255) << 24) | (((pack[6] | (pack[2] & 4)) & 255) << dp.n) | (((pack[5] | (pack[2] & 2)) & 255) << 8) | ((pack[4] | (pack[2] & 1)) & 255)) & -1;
                int midInt = ((((pack[11] | (pack[3] & 2)) & 255) << 24) | (((pack[10] | (pack[2] & 1)) & 255) << dp.n) | (((pack[9] | (pack[2] & 32)) & 255) << 8) | ((pack[8] | (pack[2] & 16)) & 255)) & -1;
                int highInt = ((((pack[15] | (pack[3] & 32)) & 255) << 24) | (((pack[14] | (pack[3] & dp.n)) & 255) << dp.n) | (((pack[13] | (pack[3] & 8)) & 255) << 8) | ((pack[12] | (pack[3] & 4)) & 255)) & -1;
                Log.e("肺活量波形数据部分", new StringBuilder().append(lowInt).toString());
                Log.e("肺活量波形数据部分", new StringBuilder().append(midInt).toString());
                Log.e("肺活量波形数据部分", new StringBuilder().append(highInt).toString());
                int AIRFLOW_STREN2 = 0;
                for (int i = 0; i < 3; i++) {
                    switch (i) {
                        case 0:
                            AIRFLOW_STREN2 = lowInt;
                            break;
                        case 1:
                            AIRFLOW_STREN2 = midInt;
                            break;
                        case 2:
                            AIRFLOW_STREN2 = highInt;
                            break;
                    }
                    StructData sd = new StructData();
                    sd.mData = AIRFLOW_STREN2 - 80000;
                    int i2 = this.mIndex;
                    this.mIndex = i2 + 1;
                    sd.mIndex = i2;
                    this.mDataList.add(sd);
                }
                return 21;
            case Constants.UPDATE_XML_FAIL /*-29*/:
                if (!Check(pack)) {
                    Log.e("肺活量删除数据操作失败", "---------------");
                    return 33;
                } else if ((pack[1] & 255) == 0) {
                    Log.e("肺活量删除数据操作成功", "---------------");
                    saveUserCase();
                    return 32;
                } else {
                    Log.e("肺活量删除数据操作失败", "---------------");
                    return 33;
                }
            case -13:
                this.mIndex = 0;
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
            case Constants.UPDATEXML_UPLOAD_FAIL /*-32*/:
                return 6;
            case Constants.GENERATE_XML_FAIL /*-31*/:
                return 33;
            case Constants.THREAD_OUT /*-30*/:
                return 17;
            case Constants.UPDATE_XML_FAIL /*-29*/:
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

    private byte[] unPackParamInfo(byte[] pack) {
        return new byte[]{(byte) (pack[15] | ((pack[9] & 32) << 2)), (byte) (pack[16] | ((pack[9] & 64) << 1)), (byte) (pack[18] | ((pack[17] & 1) << 7)), (byte) (pack[19] | ((pack[17] & 2) << 6)), (byte) (pack[20] | ((pack[17] & 4) << 5)), (byte) (pack[21] | ((pack[17] & 8) << 4)), (byte) (pack[22] | ((pack[17] & dp.n) << 3)), (byte) (pack[23] | ((pack[17] & 32) << 2)), (byte) (pack[24] | ((pack[17] & 64) << 1)), (byte) (pack[26] | ((pack[25] & 1) << 7)), (byte) (pack[27] | ((pack[25] & 2) << 6)), (byte) (pack[28] | ((pack[25] & 4) << 5)), (byte) (pack[29] | ((pack[25] & 8) << 4)), (byte) (pack[30] | ((pack[25] & dp.n) << 3)), (byte) (pack[31] | ((pack[25] & 32) << 2))};
    }
}
