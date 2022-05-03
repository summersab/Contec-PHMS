package cn.com.contec.jar.util;

import android.util.Log;
import java.util.Calendar;
import u.aly.bs;

public class TimeUtil {
    static final String TAG = "cn.com.contec.jar.util.TimeUtil";

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
        Log.i("cn.com.contec.jar.util.TimeUtil@getTimeArray", _dateStr);
        return _dateStr.split(" ");
    }

    private static int getWeekDay(int p_WeekDay) {
        if (p_WeekDay == 1) {
            return 7;
        }
        return p_WeekDay - 1;
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

    public static byte[] getSp10wTimeStrings() {
        String[] _timeArray = getTimeArray();
        return new byte[]{(byte) Integer.parseInt(_timeArray[3]), (byte) Integer.parseInt(_timeArray[4]), (byte) Integer.parseInt(_timeArray[5])};
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
}
