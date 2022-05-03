package com.contec.jar.cms50k;

import com.contec.phms.device.pm10.ReceiveThread;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;

public class DeviceCommand {
    public static byte[] GET_FRAGMENT_SIZE(int pType) {
        byte[] _getCommand = new byte[3];
        _getCommand[0] = ReceiveThread.e_back_dateresponse;
        _getCommand[1] = (byte) pType;
        return sum_Check(_getCommand);
    }

    public static byte[] GET_SPO2_POINT(int pType) {
        byte[] getData = new byte[3];
        getData[0] = -111;
        getData[1] = (byte) pType;
        return sum_Check(getData);
    }

    public static byte[] GET_PULSE_FRAGMENT(int pType) {
        byte[] getData = new byte[5];
        getData[0] = -94;
        getData[1] = (byte) pType;
        return sum_Check(getData);
    }

    public static byte[] GET_SPO2_FRAGMENT(int pType) {
        byte[] getData = new byte[5];
        getData[0] = -93;
        getData[1] = (byte) pType;
        return sum_Check(getData);
    }

    public static byte[] GET_MOVE_FRAGMENT(int pType) {
        byte[] getData = new byte[5];
        getData[0] = -92;
        getData[1] = (byte) pType;
        return sum_Check(getData);
    }

    public static byte[] GET_RR_DATA(int mType) {
        byte[] getRrdata = new byte[5];
        getRrdata[0] = -91;
        return sum_Check(getRrdata);
    }

    public static byte[] GET_STEP_DAY(int pType) {
        byte[] getData = new byte[3];
        getData[0] = -110;
        getData[1] = (byte) pType;
        return sum_Check(getData);
    }

    public static byte[] GET_STEP_MIN(int pType) {
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

    public static byte[] GET_ECG_INFO(int pType) {
        byte[] getData = new byte[3];
        getData[0] = -107;
        getData[1] = (byte) pType;
        return sum_Check(getData);
    }

    public static byte[] GET_ECG_DATA(int pType) {
        byte[] getData = new byte[3];
        getData[0] = -106;
        getData[1] = (byte) pType;
        return sum_Check(getData);
    }

    public static byte[] GET_DATA_SIZE(int pType) {
        byte[] _getCommand = new byte[3];
        _getCommand[0] = -112;
        _getCommand[1] = (byte) pType;
        return sum_Check(_getCommand);
    }

    public static byte[] DEL_CONTINUOUS_DATA(int pType) {
        byte[] _getCommand = new byte[3];
        _getCommand[0] = -95;
        _getCommand[1] = (byte) pType;
        return sum_Check(_getCommand);
    }

    public static byte[] SET_STEP_TIME(int startTime, int endTime) {
        byte[] STEP_TIME = new byte[4];
        STEP_TIME[0] = -124;
        STEP_TIME[1] = (byte) startTime;
        STEP_TIME[2] = (byte) endTime;
        return sum_Check(STEP_TIME);
    }

    public static byte[] SET_WEIGHT(int pWeight) {
        byte[] _weightCommand = new byte[5];
        _weightCommand[0] = -123;
        _weightCommand[1] = (byte) (pWeight & 7);
        _weightCommand[2] = (byte) ((pWeight >> 7) & 7);
        _weightCommand[3] = (byte) ((pWeight >> 14) & 7);
        return sum_Check(_weightCommand);
    }

    public static byte[] SET_CALORIE(int calorie, int starttime, int endtime) {
        int time = endtime - starttime;
        byte[] set_calorie = new byte[6];
        set_calorie[0] = -117;
        set_calorie[1] = (byte) (calorie & 127);
        set_calorie[2] = (byte) ((calorie >> 7) & 127);
        set_calorie[3] = (byte) (time & 128);
        set_calorie[4] = (byte) (time >> 1);
        return sum_Check(set_calorie);
    }

    public static byte[] SET_TARGET_CALORIE(int calorie) {
        byte[] target_calorie = new byte[6];
        target_calorie[0] = -117;
        target_calorie[1] = (byte) (calorie & 127);
        target_calorie[2] = (byte) ((calorie >> 7) & 127);
        return sum_Check(target_calorie);
    }

    public static byte[] SET_INIT(int pulsescreen, int ecglrhand) {
        byte[] SET_INIT = new byte[16];
        SET_INIT[0] = -116;
        SET_INIT[7] = (byte) pulsescreen;
        SET_INIT[12] = (byte) ecglrhand;
        return sum_Check(SET_INIT);
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

    public static byte[] sum_Check(byte[] pack) {
        int CHECK_SUM = 0;
        int _size = pack.length - 1;
        for (int i = 0; i < _size; i++) {
            CHECK_SUM += pack[i] & 255;
        }
        pack[pack.length - 1] = (byte) (CHECK_SUM & 127);
        return pack;
    }

    public static byte[] sum_Calorie(byte[] pack) {
        int SUM_CALORIE = 0;
        int _size = pack.length - 2;
        for (int i = 0; i < _size; i++) {
            SUM_CALORIE += pack[i] & Byte.MAX_VALUE;
        }
        return pack;
    }

    public static byte[] setUpdate(byte[] SET_TIME) {
        return sum_Check(SET_TIME);
    }

    public static byte[] setInitDate(byte[] SET_TIME) {
        return sum_Check(SET_TIME);
    }

    public static byte[] set_SJ() {
        byte[] set_sj = new byte[2];
        set_sj[0] = -72;
        return sum_Check(set_sj);
    }

    public static byte[] getCompareVesion() {
        byte[] set_sj = new byte[2];
        set_sj[0] = -126;
        return sum_Check(set_sj);
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

    public static byte[] getProductIdentification() {
        byte[] getInentification = new byte[2];
        getInentification[0] = -127;
        return sum_Check(getInentification);
    }
}
