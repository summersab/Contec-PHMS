package cn.com.contec.jar.cmssxt;

import android.util.Log;
import com.alibaba.cchannel.CloudChannel;
import java.util.ArrayList;
import java.util.HashMap;
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
    private static final String TAG = "cn.com.contec.jar.CMSSXT.DevicePackManager";
    public static byte[] m_SetTime = new byte[5];
    private byte mCommandData;
    private byte mCommandDel;
    private byte mCommandTime;
    private int mCount;
    private byte[] mPack = new byte[1024];
    private int mPackLen;
    private int mPosition;
    private byte m_CheckData;
    public ArrayList<CmssxtDataJar> m_DeviceDatas = new ArrayList<>();
    private Map<String, Integer> m_WCmd;
    public byte[] m_returnTimeByteArry;

    public DevicePackManager() {
        initCmdPosition();
    }

    public int arrangeMessage(byte[] buf, int length) {
        byte m_PageNum;
        int _isover = 0;
        for (int i = 0; i < length; i++) {
            Log.d("receiveData", "bytes received: " + Integer.toHexString(buf[i] & 255));
            byte[] bArr = this.mPack;
            int i2 = this.mCount;
            this.mCount = i2 + 1;
            bArr[i2] = buf[i];
            if (this.mCount == 3) {
                this.mPackLen = buf[i];
            }
            if (this.mCount == 7 && (m_PageNum = buf[i]) == 0 && this.mPack[5] == 5) {
                Log.e(TAG, "Have: " + m_PageNum + "  package;");
                return 7;
            }
            if (this.mCount == 8) {
                Log.e(TAG, "This is the: " + buf[i] + "  package；");
            }
            if (this.mCount > 2 && this.mCount > this.mPackLen + 2) {
                this.m_CheckData = (byte) (this.m_CheckData + buf[i]);
                _isover = processPack(this.mPack);
                if (_isover == 1) {
                    Log.d("receiveData", "返回值为1判断校验****************");
                    _isover = (this.m_CheckData & 255) == (this.mPack[this.mPackLen + 2] & 255) ? 1 : 2;
                } else if (_isover == 0) {
                    Log.d("receiveData", "返回值为0判断校验****************" + (this.m_CheckData & 255) + "   " + (this.mPack[this.mPackLen + 2] & 255));
                    if ((this.m_CheckData & 255) != (this.mPack[this.mPackLen + 2] & 255)) {
                        _isover = 2;
                    } else {
                        this.mCount = 0;
                        this.mPack = new byte[1024];
                        this.m_CheckData = 0;
                    }
                }
                String v_check_str = Integer.toHexString(this.m_CheckData & 255);
                String v_check_str_r = Integer.toHexString(this.mPack[this.mPackLen + 2] & 255);
                Log.d("receiveData", "*****checksum: " + v_check_str);
                Log.i(TAG, "del or not " + _isover + "  m_CheckData: " + v_check_str + "  mPack[mPackLen +2] " + v_check_str_r);
                this.mCount = 0;
                this.m_CheckData = 0;
            }
        }
        return _isover;
    }

    public int processData(byte[] pack) {
        byte _num = pack[8];
        byte[] _tempPack = new byte[8];
        Log.e(TAG, "Num______" + _num);
        for (int i = 0; i < _num; i++) {
            int _count = (i * 8) + 8 + 1;
            int j = 0;
            while (j < _tempPack.length) {
                _tempPack[j] = pack[_count];
                j++;
                _count++;
            }
            addData(new CmssxtDataJar(_tempPack));
        }
        Log.i(TAG, "_tempPack[7]" + _tempPack[7] + "    pack[6]" + _tempPack[6] + "  pack[4]" + _tempPack[4] + "pack[5]" + _tempPack[5] + "pack[3]" + _tempPack[3] + "pack[2]" + _tempPack[2] + "pack[1]" + _tempPack[1] + "pack[0]" + pack[0]);
        if (pack[7] == pack[6]) {
            return 1;
        }
        return 0;
    }

    public int processPack(byte[] pack) {
        if (!checkPack(pack)) {
            return 0;
        }
        switch (this.m_WCmd.get("Cmd").intValue()) {
            case 5:
                Log.i(TAG, "receive data++++++++++++++++++++++++++");
                if (pack == null || pack.length < 5) {
                    return 2;
                }
                if (pack[6] != 0) {
                    return processData(pack);
                }
                Log.i(TAG, "pack[6] == 0");
                return 7;
            case 7:
                if (DeviceCommand.RECEIVE_ID) {
                    Log.i(TAG, "索要设备的id: " + this.mPackLen);
                    if (this.mPackLen == 16) {
                        Log.i(TAG, "该设备是旧设备 对时不带秒");
                        return 8;
                    } else if (this.mPackLen <= 16) {
                        return 0;
                    } else {
                        Log.i(TAG, "该设备是新设备 对时带秒");
                        return 9;
                    }
                } else {
                    Log.i(TAG, "check  time: " + this.mPackLen);
                    return processReturnTime(pack);
                }
            case 8:
                if (pack.length <= 6) {
                    return 0;
                }
                if (pack[8] == 17) {
                    return 5;
                }
                return 6;
            default:
                return 0;
        }
    }

    private int processReturnTime(byte[] pack) {
        int _result;
        if (this.mPackLen == 16) {
            Log.i(TAG, "该设备是旧设备 时间校正不带秒");
            byte[] _Array = new byte[5];
            if (pack.length > 10) {
                for (int i = 0; i < pack.length; i++) {
                    if (i == 13) {
                        _Array[0] = pack[i];
                        Log.e(TAG, new StringBuilder(String.valueOf(pack[i])).toString());
                    }
                    if (i == 14) {
                        _Array[1] = pack[i];
                        Log.e(TAG, new StringBuilder(String.valueOf(pack[i])).toString());
                    }
                    if (i == 15) {
                        _Array[2] = pack[i];
                        Log.e(TAG, new StringBuilder(String.valueOf(pack[i])).toString());
                    }
                    if (i == 16) {
                        _Array[3] = pack[i];
                        Log.e(TAG, new StringBuilder(String.valueOf(pack[i])).toString());
                    }
                    if (i == 17) {
                        _Array[4] = pack[i];
                        Log.e(TAG, new StringBuilder(String.valueOf(pack[i])).toString());
                        this.m_returnTimeByteArry = _Array;
                    }
                }
            }
        } else if (this.mPackLen > 16) {
            Log.i(TAG, "该设备是新设备 时间校正带秒");
            byte[] _Array2 = new byte[6];
            if (pack.length > 10) {
                for (int i2 = 0; i2 < pack.length; i2++) {
                    if (i2 == 13) {
                        _Array2[0] = pack[i2];
                        Log.e(TAG, new StringBuilder(String.valueOf(pack[i2])).toString());
                    }
                    if (i2 == 14) {
                        _Array2[1] = pack[i2];
                        Log.e(TAG, new StringBuilder(String.valueOf(pack[i2])).toString());
                    }
                    if (i2 == 15) {
                        _Array2[2] = pack[i2];
                        Log.e(TAG, new StringBuilder(String.valueOf(pack[i2])).toString());
                    }
                    if (i2 == 16) {
                        _Array2[3] = pack[i2];
                        Log.e(TAG, new StringBuilder(String.valueOf(pack[i2])).toString());
                    }
                    if (i2 == 17) {
                        _Array2[4] = pack[i2];
                        Log.e(TAG, new StringBuilder(String.valueOf(pack[i2])).toString());
                    }
                    if (i2 == 18) {
                        _Array2[5] = pack[i2];
                        Log.e(TAG, new StringBuilder(String.valueOf(pack[i2])).toString());
                        this.m_returnTimeByteArry = _Array2;
                    }
                }
            }
        }
        if (checkDeviceTime(m_SetTime, this.m_returnTimeByteArry)) {
            _result = 3;
        } else {
            _result = 4;
        }
        return _result;
    }

    private boolean checkPack(byte[] pack) {
        if (pack[this.mPosition] == this.mCommandData) {
            this.m_WCmd.put("Cmd", 5);
            return true;
        } else if (pack[this.mPosition] == this.mCommandTime) {
            this.m_WCmd.put("Cmd", 7);
            return true;
        } else if (pack[this.mPosition] != this.mCommandDel) {
            return false;
        } else {
            this.m_WCmd.put("Cmd", 8);
            return true;
        }
    }

    public void addData(CmssxtDataJar deviceData) {
        this.m_DeviceDatas.add(deviceData);
    }

    public void initCmdPosition() {
        this.m_DeviceDatas = new ArrayList<>();
        this.mPosition = 5;
        this.mCommandData = 5;
        this.mCommandDel = 8;
        this.mCommandTime = 7;
        this.m_WCmd = new HashMap();
    }

    public boolean checkDeviceTime(byte[] p_setTime, byte[] p_receiveByte) {
        printPack(p_receiveByte, p_receiveByte.length);
        byte[] _timeArray = p_receiveByte;
        for (int i = 0; i < _timeArray.length; i++) {
            Log.i(TAG, "p_setTime  :" + p_setTime[i] + "  p_receiveByte" + p_receiveByte[i]);
            if (p_setTime[i] != _timeArray[i]) {
                return false;
            }
        }
        Log.i(TAG, "++++Cmssxt set time successfuly++++");
        return true;
    }

    public boolean checkDeviceDel(byte[] p_receiveByte) {
        byte[] _success = new byte[9];
        _success[0] = 83;
        _success[1] = 78;
        _success[2] = 6;
        _success[4] = 2;
        _success[5] = 8;
        _success[7] = 1;
        _success[8] = CloudChannel.SDK_VERSION;
        byte[] bArr = new byte[9];
        bArr[0] = 83;
        bArr[1] = 78;
        bArr[2] = 6;
        bArr[4] = 2;
        bArr[5] = 8;
        bArr[8] = dp.n;
        int i = 0;
        while (i < _success.length && _success[i] == p_receiveByte[i]) {
            i++;
        }
        return false;
    }

    static void printPack(byte[] pack, int p_length) {
        if (pack == null) {
            Log.i(TAG, "param pack is null");
            return;
        }
        String packStr = bs.b;
        for (int i = 0; i < p_length; i++) {
            packStr = String.valueOf(String.valueOf(packStr) + Integer.toHexString(pack[i])) + " ";
        }
        Log.i(TAG, "this is printpack :" + packStr);
    }
}
