package com.example.bm77_bc_code;

import java.util.Calendar;

public class DeviceCommand {
    public static byte[] doPack(byte[] pbyte) {
        int _size = pbyte.length;
        int _checkSum = 0;
        for (int i = 2; i < _size - 1; i++) {
            _checkSum += pbyte[i];
        }
        pbyte[_size - 1] = (byte) _checkSum;
        return pbyte;
    }

    public static byte[] Request_AllData() {
        byte[] _request = new byte[7];
        _request[0] = -109;
        _request[1] = -114;
        _request[2] = 4;
        _request[4] = 9;
        _request[5] = 5;
        _request[6] = 18;
        return _request;
    }

    public static byte[] Request_AllData_all() {
        byte[] _request = new byte[7];
        _request[0] = -109;
        _request[1] = -114;
        _request[2] = 4;
        _request[4] = 9;
        _request[5] = 21;
        _request[6] = 34;
        return _request;
    }

    public static byte[] Delete_AllData() {
        byte[] _request = new byte[7];
        _request[0] = -109;
        _request[1] = -114;
        _request[2] = 4;
        _request[4] = 9;
        _request[5] = 6;
        _request[6] = 19;
        return _request;
    }

    public static byte[] Synchronous_Time() {
        int mYear = Calendar.getInstance().get(1) - 2000;
        int mMonth = Calendar.getInstance().get(2) + 1;
        int mDay = Calendar.getInstance().get(5);
        int mHours = Calendar.getInstance().get(11);
        int mMinutes = Calendar.getInstance().get(12);
        byte[] _times = new byte[12];
        _times[0] = -109;
        _times[1] = -114;
        _times[2] = 9;
        _times[4] = 9;
        _times[5] = 2;
        _times[6] = (byte) mYear;
        _times[7] = (byte) mMonth;
        _times[8] = (byte) mDay;
        _times[9] = (byte) mHours;
        _times[10] = (byte) mMinutes;
        return doPack(_times);
    }

    public static byte[] Synchronous_Time_NEW() {
        int mYear = Calendar.getInstance().get(1) - 2000;
        int mMonth = Calendar.getInstance().get(2) + 1;
        int mDay = Calendar.getInstance().get(5);
        int mHours = Calendar.getInstance().get(11);
        int mMinutes = Calendar.getInstance().get(12);
        int mSec = Calendar.getInstance().get(13);
        byte[] _times = new byte[13];
        _times[0] = -109;
        _times[1] = -114;
        _times[2] = 10;
        _times[4] = 9;
        _times[5] = 2;
        _times[6] = (byte) mYear;
        _times[7] = (byte) mMonth;
        _times[8] = (byte) mDay;
        _times[9] = (byte) mHours;
        _times[10] = (byte) mMinutes;
        _times[11] = (byte) mSec;
        return doPack(_times);
    }
}
