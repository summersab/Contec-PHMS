package com.contec.sp10.code;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;

public class CommandSpirometer {
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

    public static byte[] GetCode(byte[] data) {
        byte[] code = new byte[8];
        try {
            byte[] md5_code = MessageDigest.getInstance("MD5").digest(data);
            for (int i = 0; i < 8; i++) {
                code[i] = (byte) ((md5_code[i] ^ md5_code[i + 8]) & Byte.MAX_VALUE);
            }
            byte[] _emtrypt = new byte[10];
            _emtrypt[0] = -65;
            _emtrypt[1] = code[0];
            _emtrypt[2] = code[1];
            _emtrypt[3] = code[2];
            _emtrypt[4] = code[3];
            _emtrypt[5] = code[4];
            _emtrypt[6] = code[5];
            _emtrypt[7] = code[6];
            _emtrypt[8] = code[7];
            return sum_Check(_emtrypt);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
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

    public static byte[] GET_BASEINFOR(int mType) {
        byte[] _baseinformation = new byte[3];
        _baseinformation[0] = -111;
        _baseinformation[1] = (byte) mType;
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
