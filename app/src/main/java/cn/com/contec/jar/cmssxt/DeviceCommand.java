package cn.com.contec.jar.cmssxt;

import android.util.Log;
import com.alibaba.cchannel.CloudChannel;
import java.util.Calendar;
import u.aly.dp;

public class DeviceCommand {
    public static boolean RECEIVE_ID = false;
    static final String TAG = "cn.com.contec.jar.CMSSXT.DeviceCommand";

    public static byte[] command_ReadID() {
        RECEIVE_ID = true;
        byte[] _cmd = new byte[9];
        _cmd[0] = 83;
        _cmd[1] = 78;
        _cmd[2] = 6;
        _cmd[4] = 2;
        _cmd[5] = 7;
        _cmd[8] = dp.m;
        Log.i(TAG, "Send command: 83  78  6  0  2  7  0  0  15");
        return _cmd;
    }

    public static byte[] command_VerifyTime() {
        RECEIVE_ID = false;
        byte[] p_verrfytime = processTimeStrings(false);
        byte[] _cmd = new byte[12];
        _cmd[0] = 83;
        _cmd[1] = 78;
        _cmd[2] = 9;
        _cmd[4] = 2;
        _cmd[5] = 6;
        _cmd[6] = p_verrfytime[0];
        _cmd[7] = p_verrfytime[1];
        _cmd[8] = p_verrfytime[2];
        _cmd[9] = p_verrfytime[3];
        _cmd[10] = p_verrfytime[4];
        _cmd[11] = (byte) (p_verrfytime[0] + CloudChannel.SDK_VERSION + p_verrfytime[1] + p_verrfytime[2] + p_verrfytime[3] + p_verrfytime[4]);
        Log.i(TAG, _cmd + "发送不带秒的对时命令！");
        DevicePackManager.m_SetTime = p_verrfytime;
        return _cmd;
    }

    public static byte[] command_VerifyTimeSS() {
        RECEIVE_ID = false;
        byte[] p_verrfytime = processTimeStrings(true);
        byte[] _cmd = new byte[13];
        _cmd[0] = 83;
        _cmd[1] = 78;
        _cmd[2] = 10;
        _cmd[4] = 2;
        _cmd[5] = 6;
        _cmd[6] = p_verrfytime[0];
        _cmd[7] = p_verrfytime[1];
        _cmd[8] = p_verrfytime[2];
        _cmd[9] = p_verrfytime[3];
        _cmd[10] = p_verrfytime[4];
        _cmd[11] = p_verrfytime[5];
        _cmd[12] = (byte) (p_verrfytime[0] + 18 + p_verrfytime[1] + p_verrfytime[2] + p_verrfytime[3] + p_verrfytime[4] + p_verrfytime[5]);
        Log.i(TAG, _cmd + "发送带秒的对时命令！");
        DevicePackManager.m_SetTime = p_verrfytime;
        return _cmd;
    }

    public static byte[] command_requestData() {
        byte[] _cmd = new byte[9];
        _cmd[0] = 83;
        _cmd[1] = 78;
        _cmd[2] = 6;
        _cmd[4] = 2;
        _cmd[5] = 5;
        _cmd[8] = dp.k;
        Log.i(TAG, "Send command: 83  78  6  0  2  5  0  0  13");
        return _cmd;
    }

    public static byte[] command_delData() {
        byte[] _cmd = new byte[9];
        _cmd[0] = 83;
        _cmd[1] = 78;
        _cmd[2] = 6;
        _cmd[4] = 2;
        _cmd[5] = 8;
        _cmd[8] = dp.n;
        return _cmd;
    }

    private static byte[] processTimeStrings(boolean pHaveSecond) {
        String[] _timeArray = getTimeArray();
        byte _year = (byte) Integer.parseInt(_timeArray[0].substring(2, 4));
        byte _month = (byte) Integer.parseInt(_timeArray[1]);
        byte _day = (byte) Integer.parseInt(_timeArray[2]);
        byte _hh = (byte) Integer.parseInt(_timeArray[3]);
        byte _mm = (byte) Integer.parseInt(_timeArray[4]);
        byte _ss = (byte) Integer.parseInt(_timeArray[5]);
        if (pHaveSecond) {
            return new byte[]{_year, _month, _day, _hh, _mm, _ss};
        }
        return new byte[]{_year, _month, _day, _hh, _mm};
    }

    private static String[] getTimeArray() {
        String[] strArr = new String[7];
        Calendar _c = Calendar.getInstance();
        int _year = _c.get(1);
        int _month = _c.get(2) + 1;
        int _day = _c.get(5);
        int _hour = _c.get(11);
        int _minute = _c.get(12);
        int _second = _c.get(13);
        String _dateStr = (String.valueOf(_year) + "-" + _month + "-" + _day + " " + _hour + ":" + _minute + ":" + _second + " " + getWeekDay(_c.get(7))).replace("-", " ").replace(":", " ");
        Log.i("cn.com.contec.jar.CMSSXT.DeviceCommand@getTimeArray", _dateStr);
        return _dateStr.split(" ");
    }

    private static int getWeekDay(int p_WeekDay) {
        if (p_WeekDay == 1) {
            return 7;
        }
        return p_WeekDay - 1;
    }
}
