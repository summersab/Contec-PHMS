package com.example.ble_contec08A_code;

import java.util.Calendar;

public class CommandSphygmomanometer {
    public static byte[] CONTACT_RESTART() {
        byte[] restart = new byte[2];
        restart[0] = Byte.MIN_VALUE;
        return sumCheck(restart);
    }

    public static byte[] GET_MARK() {
        byte[] equipMark = new byte[2];
        equipMark[0] = -127;
        return sumCheck(equipMark);
    }

    public static byte[] GET_VERSION() {
        byte[] version = new byte[2];
        version[0] = -126;
        return sumCheck(version);
    }

    public static byte[] SET_TIME() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(1) - 2000;
        int month = calendar.get(2) + 1;
        int day = calendar.get(5);
        int hours = calendar.get(11);
        int minutes = calendar.get(12);
        int seconds = calendar.get(13);
        int milliSeconds = calendar.get(14);
        byte[] setTime = new byte[10];
        setTime[0] = -125;
        setTime[1] = (byte) year;
        setTime[2] = (byte) month;
        setTime[3] = (byte) day;
        setTime[4] = (byte) hours;
        setTime[5] = (byte) minutes;
        setTime[6] = (byte) seconds;
        setTime[7] = (byte) (milliSeconds & 127);
        setTime[8] = (byte) ((milliSeconds >> 7) & 255);
        return sumCheck(setTime);
    }

    public static byte[] GET_POWERSIZE() {
        byte[] version = new byte[2];
        version[0] = -124;
        return sumCheck(version);
    }

    public static byte[] GET_SERIAL_NUM() {
        byte[] version = new byte[2];
        version[0] = -123;
        return sumCheck(version);
    }

    public static byte[] GET_TIME() {
        byte[] time = new byte[2];
        time[0] = -122;
        return sumCheck(time);
    }

    public static byte[] GET_CLIP_INFO() {
        byte[] clipInfo = new byte[2];
        clipInfo[0] = -112;
        return sumCheck(clipInfo);
    }

    public static byte[] GET_DATA(int type) {
        byte[] data = new byte[3];
        data[0] = -111;
        data[1] = (byte) type;
        return sumCheck(data);
    }

    public static byte[] sumCheck(byte[] pack) {
        int checkSum = 0;
        int num = pack.length - 1;
        for (int i = 0; i < num; i++) {
            checkSum += pack[i] & 255;
        }
        pack[pack.length - 1] = (byte) (checkSum & 127);
        return pack;
    }
}
