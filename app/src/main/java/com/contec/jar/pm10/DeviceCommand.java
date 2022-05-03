package com.contec.jar.pm10;

import com.contec.phms.device.pm10.ReceiveThread;
import java.util.Calendar;

public class DeviceCommand {
    public static byte[] CONFIRM;
    public static byte[] SET;

    public static byte[] DELETE_DATA(int pType, int pIndex) {
        byte[] _delete = new byte[64];
        _delete[0] = -80;
        _delete[1] = (byte) pType;
        _delete[2] = (byte) pIndex;
        return _delete;
    }

    static {
        byte[] bArr = new byte[64];
        bArr[0] = -2;
        CONFIRM = bArr;
        byte[] bArr2 = new byte[64];
        bArr2[0] = -125;
        bArr2[1] = 1;
        bArr2[3] = 1;
        bArr2[4] = 1;
        bArr2[5] = 1;
        SET = bArr2;
    }

    public static byte[] GET_DATA_INFO(int pType, int pIndex) {
        byte[] _delete = new byte[64];
        _delete[0] = -112;
        _delete[1] = (byte) pType;
        _delete[2] = (byte) (pIndex & 127);
        _delete[3] = (byte) ((pIndex >> 7) & 127);
        return _delete;
    }

    public static byte[] GET_DATA(int pIndex) {
        byte[] _delete = new byte[64];
        _delete[0] = ReceiveThread.e_back_dateresponse;
        _delete[1] = (byte) (pIndex & 127);
        _delete[2] = (byte) ((pIndex >> 7) & 127);
        return _delete;
    }

    public static byte[] GET_DATA_RE(int pIndex) {
        byte[] _delete = new byte[64];
        _delete[0] = ReceiveThread.e_back_dateresponse;
        _delete[1] = Byte.MAX_VALUE;
        _delete[2] = Byte.MAX_VALUE;
        return _delete;
    }

    public static byte[] SET_TIME() {
        int mYear = Calendar.getInstance().get(1);
        int mMonth = Calendar.getInstance().get(2) + 1;
        int mDay = Calendar.getInstance().get(5);
        int mHours = Calendar.getInstance().get(11);
        int mMinutes = Calendar.getInstance().get(12);
        int mSeconds = Calendar.getInstance().get(13);
        byte[] SET_TIME = new byte[64];
        SET_TIME[0] = -126;
        SET_TIME[1] = (byte) ((mYear >> 7) & 255);
        SET_TIME[2] = (byte) (mYear & 127);
        SET_TIME[3] = (byte) mMonth;
        SET_TIME[4] = (byte) mDay;
        SET_TIME[5] = (byte) mHours;
        SET_TIME[6] = (byte) mMinutes;
        SET_TIME[7] = (byte) mSeconds;
        return SET_TIME;
    }
}
