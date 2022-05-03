package com.contec.jar.cms50ew;

import java.util.Calendar;

public class DeviceCommand {
    public static final int e_pack_command = 125;

    public static byte[] REQUEST_USER_INFO() {
        byte[] _commd = new byte[9];
        _commd[0] = 125;
        _commd[1] = Byte.MIN_VALUE;
        _commd[2] = -85;
        return _commd;
    }

    public static byte[] REQUEST_PI_TYPE() {
        byte[] _commd = new byte[9];
        _commd[0] = 125;
        _commd[1] = Byte.MIN_VALUE;
        _commd[2] = -84;
        return _commd;
    }

    public static byte[] REQUEST_DATA_COUNT() {
        byte[] _commd = new byte[9];
        _commd[0] = 125;
        _commd[1] = Byte.MIN_VALUE;
        _commd[2] = -93;
        return _commd;
    }

    public static byte[] REQUEST_DATA_DATE(int position) {
        byte[] comdt = new byte[9];
        comdt[0] = 125;
        comdt[1] = Byte.MIN_VALUE;
        comdt[2] = -91;
        comdt[4] = (byte) position;
        return comdt;
    }

    public static byte[] REQUEST_DATA_LEN(int position) {
        byte[] comdd = new byte[9];
        comdd[0] = 125;
        comdd[1] = Byte.MIN_VALUE;
        comdd[2] = -92;
        comdd[4] = (byte) position;
        return comdd;
    }

    public static byte[] REQUEST_DATA(int position, boolean _PI) {
        if (!_PI) {
            return new byte[]{125, -127, -90, -1, -1};
        }
        byte[] comddata = new byte[9];
        comddata[0] = 125;
        comddata[1] = Byte.MIN_VALUE;
        comddata[2] = -90;
        comddata[4] = (byte) position;
        return comddata;
    }

    public static byte[] DELETE_DATA(int position, boolean all) {
        if (all) {
            return new byte[]{125, -127, -82, -1, -1};
        }
        byte[] comddata = new byte[9];
        comddata[0] = 125;
        comddata[1] = Byte.MIN_VALUE;
        comddata[2] = -82;
        comddata[4] = (byte) position;
        return comddata;
    }

    public static byte[] BACK_COMMAND(int position) {
        byte[] comd = new byte[9];
        comd[0] = 125;
        comd[1] = Byte.MIN_VALUE;
        comd[2] = -71;
        comd[4] = (byte) position;
        return comd;
    }

    public static byte[] SET_DATE() {
        int mYear = Calendar.getInstance().get(1) - 2000;
        int mMonth = Calendar.getInstance().get(2) + 1;
        int mDay = Calendar.getInstance().get(5);
        int mWeek = Calendar.getInstance().get(7) - 1;
        byte[] SET_DATE = new byte[9];
        SET_DATE[0] = 125;
        SET_DATE[1] = Byte.MIN_VALUE;
        SET_DATE[2] = -78;
        SET_DATE[3] = 32;
        SET_DATE[4] = (byte) mYear;
        SET_DATE[5] = (byte) mMonth;
        SET_DATE[6] = (byte) mDay;
        SET_DATE[7] = (byte) mWeek;
        return SET_DATE;
    }

    public static byte[] SET_TIME() {
        int mHours = Calendar.getInstance().get(11);
        int mMinutes = Calendar.getInstance().get(12);
        int mSeconds = Calendar.getInstance().get(13);
        byte[] SET_TIME = new byte[9];
        SET_TIME[0] = 125;
        SET_TIME[1] = Byte.MIN_VALUE;
        SET_TIME[2] = -79;
        SET_TIME[3] = (byte) mHours;
        SET_TIME[4] = (byte) mMinutes;
        SET_TIME[5] = (byte) mSeconds;
        return SET_TIME;
    }
}
