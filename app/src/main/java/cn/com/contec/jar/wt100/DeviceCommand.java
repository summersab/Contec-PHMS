package cn.com.contec.jar.wt100;

import android.util.Log;
import com.alibaba.cchannel.CloudChannel;
import java.util.Calendar;
import u.aly.dp;

public class DeviceCommand {
    static final String TAG = "cn.com.contec.jar.WT100.DeviceCommand";

    public static byte[] command_VerifyTime() {
        byte[] p_verrfytime = getcureentbytetime();
        byte[] _cmd = new byte[13];
        _cmd[0] = -109;
        _cmd[1] = -114;
        _cmd[2] = 10;
        _cmd[4] = 5;
        _cmd[5] = 5;
        _cmd[6] = p_verrfytime[0];
        _cmd[7] = p_verrfytime[1];
        _cmd[8] = p_verrfytime[2];
        _cmd[9] = p_verrfytime[3];
        _cmd[10] = p_verrfytime[4];
        _cmd[11] = p_verrfytime[5];
        _cmd[12] = (byte) (p_verrfytime[0] + 20 + p_verrfytime[1] + p_verrfytime[2] + p_verrfytime[3] + p_verrfytime[4] + p_verrfytime[5]);
        for (int i = 0; i < 12; i++) {
            Log.i(TAG, new StringBuilder(String.valueOf(_cmd[i])).toString());
        }
        DevicePackManager.mSynchronizationTime = p_verrfytime;
        return _cmd;
    }

    private static byte[] getcureentbytetime() {
        String[] strArr = new String[7];
        Calendar _c = Calendar.getInstance();
        return new byte[]{(byte) Integer.parseInt(new StringBuilder(String.valueOf(_c.get(1))).toString().substring(2, 4)), (byte) (_c.get(2) + 1), (byte) _c.get(5), (byte) _c.get(11), (byte) _c.get(12), (byte) _c.get(13)};
    }

    public static byte[] command_requestData(String wt_version) {
        if (wt_version.equals("0")) {
            byte[] bArr = new byte[7];
            bArr[0] = -109;
            bArr[1] = -114;
            bArr[2] = 4;
            bArr[4] = 5;
            bArr[5] = 8;
            bArr[6] = CloudChannel.SDK_VERSION;
            return bArr;
        } else if (!wt_version.equals("1")) {
            return null;
        } else {
            byte[] bArr2 = new byte[7];
            bArr2[0] = -109;
            bArr2[1] = -114;
            bArr2[2] = 4;
            bArr2[4] = 5;
            bArr2[5] = 11;
            bArr2[6] = 20;
            return bArr2;
        }
    }

    public static byte[] command_delData() {
        byte[] _cmd = new byte[7];
        _cmd[0] = -109;
        _cmd[1] = -114;
        _cmd[2] = 4;
        _cmd[4] = 5;
        _cmd[5] = 7;
        _cmd[6] = dp.n;
        return _cmd;
    }

    public static byte[] processTimeStrings(boolean pHaveSecond) {
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
        Log.i("cn.com.contec.jar.WT100.DeviceCommand@getTimeArray", _dateStr);
        return _dateStr.split(" ");
    }

    private static int getWeekDay(int p_WeekDay) {
        if (p_WeekDay == 1) {
            return 7;
        }
        return p_WeekDay - 1;
    }
}
