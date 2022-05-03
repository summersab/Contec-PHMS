package cn.com.contec.jar.sp10w;

import android.util.Log;
import java.util.Calendar;
import u.aly.bs;

public class DeviceCommand {
    static final String TAG = "SP10W.DeviceCommand";

    public static byte[] commandNumberOfData() {
        byte[] _cmd = new byte[9];
        _cmd[0] = 88;
        return doPack(_cmd, _cmd.length);
    }

    public static byte[] command_requestData() {
        return new byte[]{64, Byte.MIN_VALUE, Byte.MIN_VALUE, Byte.MIN_VALUE, Byte.MIN_VALUE, Byte.MIN_VALUE, Byte.MIN_VALUE, Byte.MIN_VALUE, Byte.MIN_VALUE};
    }

    public static byte[] doPack(byte[] packd, int len) {
        if (packd == null) {
            return null;
        }
        if (len <= 0) {
            return packd;
        }
        packd[1] = Byte.MIN_VALUE;
        for (int i = 2; i < len; i++) {
            packd[1] = (byte) (packd[1] | ((packd[i] & 128) >> (9 - i)));
            packd[i] = (byte) (packd[i] | 128);
        }
        return packd;
    }

    public static byte[] command_delData() {
        return new byte[]{66, Byte.MIN_VALUE, Byte.MIN_VALUE, Byte.MIN_VALUE, Byte.MIN_VALUE, Byte.MIN_VALUE, Byte.MIN_VALUE, Byte.MIN_VALUE, Byte.MIN_VALUE};
    }

    public static byte[] command_Time() {
        byte[] p_time = getSp10wTimeStrings();
        byte[] _cmd = new byte[8];
        _cmd[0] = 83;
        _cmd[2] = p_time[0];
        _cmd[3] = p_time[1];
        _cmd[4] = p_time[2];
        byte[] _cmdD = doPack(_cmd, _cmd.length);
        DevicePackManager.mSynchronizationTime = p_time;
        return _cmdD;
    }

    public static byte[] command_Date() {
        byte[] p_date = getSp10wDateStrings();
        byte[] _cmd = new byte[9];
        _cmd[0] = 85;
        _cmd[2] = p_date[0];
        _cmd[3] = p_date[1];
        _cmd[4] = p_date[2];
        _cmd[5] = p_date[3];
        _cmd[6] = p_date[4];
        byte[] _cmdD = doPack(_cmd, _cmd.length);
        DevicePackManager.mSynchronizationDate = p_date;
        return _cmdD;
    }

    public static byte[] getSp10wTimeStrings() {
        String[] _timeArray = getTimeArray();
        return new byte[]{(byte) Integer.parseInt(_timeArray[3]), (byte) Integer.parseInt(_timeArray[4]), (byte) Integer.parseInt(_timeArray[5])};
    }

    public static String[] getTimeArray() {
        String[] strArr = new String[7];
        Calendar _c = Calendar.getInstance();
        int _year = _c.get(1);
        int _month = _c.get(2) + 1;
        int _day = _c.get(5);
        int _hour = _c.get(11);
        int _minute = _c.get(12);
        int _second = _c.get(13);
        String _dateStr = (String.valueOf(_year) + "-" + _month + "-" + _day + " " + _hour + ":" + _minute + ":" + _second + " " + getWeekDay(_c.get(7))).replace("-", " ").replace(":", " ");
        Log.i("SP10W.DeviceCommand@getTimeArray", _dateStr);
        return _dateStr.split(" ");
    }

    private static int getWeekDay(int p_WeekDay) {
        if (p_WeekDay == 1) {
            return 7;
        }
        return p_WeekDay - 1;
    }

    public static byte[] getSp10wDateStrings() {
        String[] _timeArray = getTimeArray();
        String _year = Integer.toBinaryString(Integer.parseInt(_timeArray[0]));
        String _yearChange = bs.b;
        if (_year.length() < 16) {
            for (int i = 0; i < 16 - _year.length(); i++) {
                _yearChange = String.valueOf(_yearChange) + "0";
            }
            _yearChange = String.valueOf(_yearChange) + _year;
        }
        return new byte[]{(byte) Integer.parseInt(Integer.valueOf(_yearChange.substring(8, 16), 2).toString()), (byte) Integer.parseInt(Integer.valueOf(_yearChange.substring(0, 8), 2).toString()), (byte) Integer.parseInt(_timeArray[1]), (byte) Integer.parseInt(_timeArray[2]), (byte) Integer.parseInt(_timeArray[6])};
    }

    public static byte[] DELETE_DATA() {
        byte[] _delete = new byte[2];
        _delete[0] = -109;
        return sum_Check(_delete);
    }

    public static byte[] GET_WAVE_DATA(int mType) {
        byte[] getData = new byte[3];
        getData[0] = -110;
        getData[1] = (byte) mType;
        return sum_Check(getData);
    }

    public static byte[] GET_TIME() {
        byte[] _time = new byte[2];
        _time[0] = -119;
        return sum_Check(_time);
    }

    public static byte[] SET_TIME() {
        int mYear = Calendar.getInstance().get(1) - 2000;
        int mMonth = Calendar.getInstance().get(2) + 1;
        int mDay = Calendar.getInstance().get(5);
        int mHours = Calendar.getInstance().get(11);
        int mMinutes = Calendar.getInstance().get(12);
        int mSeconds = Calendar.getInstance().get(13);
        byte[] SET_TIME = new byte[10];
        SET_TIME[0] = -125;
        SET_TIME[1] = (byte) mYear;
        SET_TIME[2] = (byte) mMonth;
        SET_TIME[3] = (byte) mDay;
        SET_TIME[4] = (byte) mHours;
        SET_TIME[5] = (byte) mMinutes;
        SET_TIME[6] = (byte) mSeconds;
        return sum_Check(SET_TIME);
    }

    public static byte[] GET_VERSION() {
        byte[] _version = new byte[2];
        _version[0] = -126;
        return sum_Check(_version);
    }

    public static byte[] GET_STORAGE_INFOR() {
        byte[] _storage = new byte[2];
        _storage[0] = -112;
        return sum_Check(_storage);
    }

    public static byte[] GET_BASEINFOR() {
        byte[] _baseinformation = new byte[2];
        _baseinformation[0] = -111;
        return sum_Check(_baseinformation);
    }

    public static byte[] sum_Check(byte[] pack) {
        int CHECK_SUM = 0;
        int _size = pack.length - 1;
        for (int i = 0; i < _size; i++) {
            CHECK_SUM += pack[i] & 255;
        }
        pack[pack.length - 1] = (byte) (CHECK_SUM & 127);
        return pack;
    }
}
