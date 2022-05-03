package com.contec.jar.blepm10;

import java.util.Calendar;

public class CommandEcg {
    public static byte[] GET_VERSION() {
        byte[] _version = new byte[2];
        _version[0] = -126;
        return sum_Check(_version);
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

    public static byte[] GET_TIME() {
        byte[] _time = new byte[2];
        _time[0] = -119;
        return sum_Check(_time);
    }

    public static byte[] GET_SET_PARAMETER() {
        byte[] set_parameter = new byte[2];
        set_parameter[0] = -114;
        return sum_Check(set_parameter);
    }

    public static byte[] GET_FRAGMENT_INFOR(int mType) {
        byte[] fragment_infor = new byte[3];
        fragment_infor[0] = -112;
        fragment_infor[1] = (byte) mType;
        return sum_Check(fragment_infor);
    }

    public static byte[] GET_BASE_INFOR(int mType) {
        byte[] base_infor = new byte[3];
        base_infor[0] = -107;
        base_infor[1] = (byte) mType;
        return sum_Check(base_infor);
    }

    public static byte[] GET_GROUP_DATA(int mType) {
        byte[] group_data = new byte[3];
        group_data[0] = -106;
        group_data[1] = (byte) mType;
        return sum_Check(group_data);
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
