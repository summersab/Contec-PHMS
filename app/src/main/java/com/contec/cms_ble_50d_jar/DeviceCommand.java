package com.contec.cms_ble_50d_jar;

import java.util.Calendar;

public class DeviceCommand {
    public static byte[] getCompareVesion() {
        byte[] set_sj = new byte[2];
        set_sj[0] = -126;
        return sum_Check(set_sj);
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

    public static byte[] SET_STEP_TIME(int startTime, int endTime) {
        byte[] STEP_TIME = new byte[4];
        STEP_TIME[0] = -124;
        STEP_TIME[1] = (byte) startTime;
        STEP_TIME[2] = (byte) endTime;
        return sum_Check(STEP_TIME);
    }

    public static byte[] SET_WEIGHT(double pWeight) {
        int _Weight = (int) (1000.0d * pWeight);
        byte[] _weightCommand = new byte[5];
        _weightCommand[0] = -123;
        _weightCommand[1] = (byte) (_Weight & 127);
        _weightCommand[2] = (byte) ((_Weight >> 7) & 127);
        _weightCommand[3] = (byte) ((_Weight >> 14) & 127);
        return sum_Check(_weightCommand);
    }

    public static byte[] SET_HEIGHT(double height) {
        int _height = (int) Math.round(height);
        byte[] _weightCommand = new byte[4];
        _weightCommand[0] = -118;
        _weightCommand[1] = (byte) (_height & 127);
        _weightCommand[2] = (byte) ((_height >> 7) & 1);
        return sum_Check(_weightCommand);
    }

    public static byte[] SET_CALORIE(int calorie, int storageDays, int sensitivity) {
        byte[] set_calorie = new byte[6];
        set_calorie[0] = -117;
        set_calorie[1] = (byte) (calorie & 127);
        set_calorie[2] = (byte) ((calorie >> 7) & 127);
        set_calorie[3] = (byte) (storageDays & 127);
        set_calorie[4] = (byte) (((byte) ((storageDays >> 7) & 1)) | ((byte) ((sensitivity & 3) << 1)));
        return sum_Check(set_calorie);
    }

    public static byte[] GET_DATA_SIZE(int pType) {
        byte[] _getCommand = new byte[3];
        _getCommand[0] = -112;
        _getCommand[1] = (byte) pType;
        return sum_Check(_getCommand);
    }

    public static byte[] GET_SPO2_POINT(int pType) {
        byte[] getData = new byte[3];
        getData[0] = -111;
        getData[1] = (byte) pType;
        return sum_Check(getData);
    }

    public static byte[] GET_STEP_DAY(int pType) {
        byte[] getData = new byte[3];
        getData[0] = -110;
        getData[1] = (byte) pType;
        return sum_Check(getData);
    }

    public static byte[] GET_STEP_BASE_MINDATA(int pType) {
        byte[] getData = new byte[3];
        getData[0] = -109;
        getData[1] = (byte) pType;
        return sum_Check(getData);
    }

    public static byte[] GET_STEP_MIN_DATA(int pType) {
        byte[] getData = new byte[3];
        getData[0] = -108;
        getData[1] = (byte) pType;
        return sum_Check(getData);
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

    public static byte[] SEND_COMMUNICATION_RESET() {
        byte[] set_sj = new byte[2];
        set_sj[0] = Byte.MIN_VALUE;
        return sum_Check(set_sj);
    }

    private static byte[] test() {
        byte[] getData = new byte[2];
        getData[0] = -119;
        return sum_Check(getData);
    }

    private static byte[] SET_TIME(int mYear, int mMonth, int mDay, int mHours, int mMinutes, int mSeconds) {
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
}
