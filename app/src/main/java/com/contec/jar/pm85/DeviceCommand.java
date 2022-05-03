package com.contec.jar.pm85;

import java.util.Calendar;

public class DeviceCommand {
    public static byte Detect_Communicate = -13;
    public static byte REQUEST_Account = -16;
    public static byte REQUEST_DATA_COUNT = -8;
    public static byte REQUEST_Data = -15;
    public static byte REQUEST_Time = -12;
    public static byte RESPONSE_0B = -14;
    public static byte Send_Account_Info = -7;
    public static byte Stop_UploadData = -9;

    public static byte[] SET_DATE() {
        int mYear = Calendar.getInstance().get(1);
        int mMonth = Calendar.getInstance().get(2) + 1;
        int mDay = Calendar.getInstance().get(5);
        byte[] SET_DATE = new byte[6];
        SET_DATE[0] = -10;
        SET_DATE[2] = (byte) (mYear & 255);
        SET_DATE[3] = (byte) ((65280 & mYear) >> 8);
        SET_DATE[4] = (byte) mMonth;
        SET_DATE[5] = (byte) mDay;
        return SET_DATE;
    }

    public static byte[] SET_TIME() {
        int mHours = Calendar.getInstance().get(11);
        int mMinutes = Calendar.getInstance().get(12);
        int mSeconds = Calendar.getInstance().get(13);
        byte[] SET_TIME = new byte[5];
        SET_TIME[0] = -11;
        SET_TIME[2] = (byte) mHours;
        SET_TIME[3] = (byte) mMinutes;
        SET_TIME[4] = (byte) mSeconds;
        return SET_TIME;
    }

    public static byte[] Match_Number(String pAccount) {
        byte[] match_number = new byte[24];
        match_number[0] = -7;
        byte[] number = pAccount.getBytes();
        int _count = number.length;
        for (int i = 0; i < _count; i++) {
            match_number[i + 4] = number[i];
        }
        return match_number;
    }
}
