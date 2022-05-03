package com.example.bm77_contec08A_code;

import com.alibaba.cchannel.CloudChannel;
import java.util.Calendar;

public class DeviceCommand {
    public static byte BOLD_DELETE_1 = 37;
    public static byte BOLD_DELETE_2 = 38;
    public static byte BOLD_DELETE_3 = 39;
    public static byte BOLD_DELETE_ALL = 40;
    public static byte BOLD_USER_1 = 33;
    public static byte BOLD_USER_2 = 34;
    public static byte BOLD_USER_3 = 35;
    public static byte BOLD_USER_ALL = 36;
    public static byte BP_DELETE_1 = 21;
    public static byte BP_DELETE_2 = 22;
    public static byte BP_DELETE_3 = 23;
    public static byte BP_DELETE_ALL = 24;
    public static byte BP_USER_1 = CloudChannel.SDK_VERSION;
    public static byte BP_USER_2 = 18;
    public static byte BP_USER_3 = 19;
    public static byte BP_USER_ALL = 20;
    public static byte[] correct_time_notice;

    public static byte[] REQUEST_IS_NEW_DEVICES() {
        return new byte[]{64, -113, -1, -2, -3, -4};
    }

    public static byte[] REPLAY_CONTEC08A() {
        byte[] bArr = new byte[4];
        bArr[0] = 74;
        bArr[1] = 70;
        bArr[2] = 1;
        return bArr;
    }

    public static byte[] REPLAY_ABPM50() {
        byte[] bArr = new byte[4];
        bArr[0] = 74;
        bArr[1] = 67;
        bArr[2] = 1;
        return bArr;
    }

    public static byte[] REQUEST_HANDSHAKE() {
        return new byte[]{66, -113, -1, -2, -3, -4};
    }

    public static byte[] REQUEST_BLOOD_PRESSURE() {
        byte[] _commd_blood_pressure = new byte[6];
        _commd_blood_pressure[0] = 67;
        _commd_blood_pressure[1] = 66;
        _commd_blood_pressure[2] = 1;
        _commd_blood_pressure[3] = 7;
        _commd_blood_pressure[4] = 5;
        return _commd_blood_pressure;
    }

    public static byte[] REQUEST_BLOOD_PRESSURE_NEW_Version() {
        byte[] _commd_blood_pressure = new byte[6];
        _commd_blood_pressure[0] = 67;
        _commd_blood_pressure[1] = 66;
        _commd_blood_pressure[2] = 1;
        _commd_blood_pressure[3] = 7;
        _commd_blood_pressure[4] = 5;
        return _commd_blood_pressure;
    }

    public static byte[] REQUEST_NORMAL_BLOOD_PRESSURE() {
        byte[] _commd_normal_blood_pressure = new byte[6];
        _commd_normal_blood_pressure[0] = 67;
        _commd_normal_blood_pressure[2] = 1;
        _commd_normal_blood_pressure[3] = 3;
        _commd_normal_blood_pressure[4] = 5;
        return _commd_normal_blood_pressure;
    }

    public static byte[] REQUEST_AMBULATORY_BLOOD_PRESSURE() {
        byte[] _commd_ambulatory_blood_pressure = new byte[6];
        _commd_ambulatory_blood_pressure[0] = 67;
        _commd_ambulatory_blood_pressure[1] = 4;
        _commd_ambulatory_blood_pressure[2] = 1;
        _commd_ambulatory_blood_pressure[3] = 1;
        _commd_ambulatory_blood_pressure[4] = 2;
        return _commd_ambulatory_blood_pressure;
    }

    public static byte[] REQUEST_OXYGEN() {
        byte[] _commd_oxygen = new byte[6];
        _commd_oxygen[0] = 67;
        _commd_oxygen[1] = 66;
        _commd_oxygen[2] = 1;
        _commd_oxygen[3] = 7;
        _commd_oxygen[4] = 3;
        return _commd_oxygen;
    }

    static {
        byte[] bArr = new byte[6];
        bArr[0] = 67;
        bArr[1] = 66;
        bArr[2] = 2;
        bArr[3] = -1;
        bArr[4] = 4;
        correct_time_notice = bArr;
    }

    public static byte[] Correct_Time() {
        int mYear = Calendar.getInstance().get(1) - 2000;
        int mMonth = Calendar.getInstance().get(2) + 1;
        int mDay = Calendar.getInstance().get(5);
        int mHours = Calendar.getInstance().get(11);
        int mMinutes = Calendar.getInstance().get(12);
        int mSeconds = Calendar.getInstance().get(13);
        byte[] SET_DATE = new byte[10];
        SET_DATE[0] = 76;
        SET_DATE[1] = 66;
        SET_DATE[2] = Byte.MIN_VALUE;
        SET_DATE[3] = (byte) mYear;
        SET_DATE[4] = (byte) mMonth;
        SET_DATE[5] = (byte) mDay;
        SET_DATE[6] = (byte) mHours;
        SET_DATE[7] = (byte) mMinutes;
        SET_DATE[8] = (byte) mSeconds;
        DevicePackManager.doPack(SET_DATE);
        return SET_DATE;
    }

    public static byte[] Correct_Time_Abpm50() {
        int mYear = Calendar.getInstance().get(1) - 2000;
        int mMonth = Calendar.getInstance().get(2) + 1;
        int mDay = Calendar.getInstance().get(5);
        int mHours = Calendar.getInstance().get(11);
        int mMinutes = Calendar.getInstance().get(12);
        int mSeconds = Calendar.getInstance().get(13);
        byte[] SET_DATE = new byte[10];
        SET_DATE[0] = 76;
        SET_DATE[1] = -124;
        SET_DATE[2] = Byte.MIN_VALUE;
        SET_DATE[3] = (byte) mYear;
        SET_DATE[4] = (byte) mMonth;
        SET_DATE[5] = (byte) mDay;
        SET_DATE[6] = (byte) mHours;
        SET_DATE[7] = (byte) mMinutes;
        SET_DATE[8] = (byte) mSeconds;
        DevicePackManager.doPack(SET_DATE);
        return SET_DATE;
    }

    public static byte[] DELETE_OXYGEN() {
        byte[] _delete_oxygen = new byte[6];
        _delete_oxygen[0] = 67;
        _delete_oxygen[1] = 66;
        _delete_oxygen[2] = 3;
        _delete_oxygen[3] = 7;
        _delete_oxygen[4] = 3;
        return _delete_oxygen;
    }

    public static byte[] DELETE_BP() {
        byte[] _delete_bp = new byte[6];
        _delete_bp[0] = 67;
        _delete_bp[1] = 66;
        _delete_bp[2] = 3;
        _delete_bp[3] = 7;
        _delete_bp[4] = 2;
        return _delete_bp;
    }

    public static byte[] command_synchronizationTime(byte[] pTime) {
        if (pTime.length != 6) {
            return null;
        }
        return new byte[]{25, pTime[0], pTime[1], pTime[2], pTime[3], pTime[4], pTime[5]};
    }
}
