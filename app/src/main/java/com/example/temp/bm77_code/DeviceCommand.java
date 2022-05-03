package com.example.temp.bm77_code;

import android.util.Log;
import com.alibaba.cchannel.CloudChannel;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DeviceCommand {
    static final String TAG = "jar.EarTemperture.DeviceCommand";

    public static byte[] commandConfirmEquipment() {
        byte[] otherByte = new byte[2];
        otherByte[1] = 1;
        processByte(otherByte);
        byte[] _cmd = {CloudChannel.SDK_VERSION, otherByte[0], otherByte[1]};
        DevicePackManager.printPack(_cmd, _cmd.length);
        Log.i(TAG, "Send Command to confirm the equipment �?" + _cmd[0] + "  " + _cmd[1] + "  " + _cmd[2]);
        return _cmd;
    }

    private static void processByte(byte[] value) {
        byte head = 0;
        for (int i = value.length - 1; i > 0; i--) {
            head = (byte) (((value[i] & 128) >> 7) | ((byte) (head << 1)));
            value[i] = (byte) (value[i] | 128);
        }
        value[0] = (byte) (head | 128);
    }

    public static byte[] command_VerifyTime() {
        byte[] otherByte = processTimeStrings();
        processByte(otherByte);
        byte[] _cmd = {18, otherByte[0], otherByte[1], otherByte[2], otherByte[3], otherByte[4]};
        Log.i(TAG, "**********发�?�对时命令！18 " + otherByte[0] + "  " + otherByte[1] + "  " + otherByte[2] + "  " + otherByte[3] + "  " + otherByte[4]);
        DevicePackManager.printPack(_cmd, _cmd.length);
        return _cmd;
    }

    private static byte[] processTimeStrings() {
        byte[] timeArray = new byte[5];
        Calendar cl = Calendar.getInstance();
        cl.set(2000, 0, 1, 0, 0, 0);
        Date startDate = cl.getTime();
        long _nowTimeMillis = System.currentTimeMillis();
        long _benchmarkSecond = startDate.getTime();
        long _scond = (_nowTimeMillis - _benchmarkSecond) / 1000;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        cl.setTimeInMillis(_nowTimeMillis);
        Log.i(TAG, "send second �?" + _scond + "  _nowTimeMillis:" + _nowTimeMillis + "  _benchmarkSecond:" + _benchmarkSecond + " now date :" + format.format(cl.getTime()));
        timeArray[1] = (byte) ((int) (255 & _scond));
        timeArray[2] = (byte) ((int) ((_scond >> 8) & 255));
        timeArray[3] = (byte) ((int) ((_scond >> 16) & 255));
        timeArray[4] = (byte) ((int) ((_scond >> 24) & 255));
        return timeArray;
    }

    public static byte[] command_queryDataNum() {
        byte[] otherByte = new byte[2];
        otherByte[1] = 1;
        processByte(otherByte);
        byte[] _cmd = {23, otherByte[0], otherByte[1]};
        DevicePackManager.printPack(_cmd, _cmd.length);
        Log.i(TAG, "Query data command: 23  " + otherByte[0] + "  " + otherByte[1]);
        return _cmd;
    }

    public static byte[] command_readDeviceTime() {
        byte[] otherByte = new byte[2];
        otherByte[1] = 1;
        processByte(otherByte);
        byte[] _cmd = {19, otherByte[0], otherByte[1]};
        DevicePackManager.printPack(_cmd, _cmd.length);
        Log.i(TAG, "Query device time command: 19  " + otherByte[0] + "  " + otherByte[1]);
        return _cmd;
    }

    public static byte[] command_requestDataOlnyOne() {
        byte[] otherByte = new byte[2];
        otherByte[1] = 1;
        processByte(otherByte);
        byte[] _cmd = {24, otherByte[0], otherByte[1]};
        DevicePackManager.printPack(_cmd, _cmd.length);
        Log.i(TAG, "request single data command�?24  " + otherByte[0] + "  " + otherByte[1]);
        return _cmd;
    }

    public static byte[] command_requestAllData() {
        byte[] otherByte = new byte[2];
        otherByte[1] = 1;
        processByte(otherByte);
        byte[] _cmd = {25, otherByte[0], otherByte[1]};
        DevicePackManager.printPack(_cmd, _cmd.length);
        Log.i(TAG, "request All data command�?25  " + otherByte[0] + "  " + otherByte[1]);
        return _cmd;
    }

    public static byte[] command_delData() {
        byte[] otherByte = new byte[2];
        otherByte[1] = 1;
        processByte(otherByte);
        byte[] _cmd = {20, otherByte[0], otherByte[1]};
        DevicePackManager.printPack(_cmd, _cmd.length);
        Log.i(TAG, " del all data command�?20  " + otherByte[0] + "  " + otherByte[1]);
        return _cmd;
    }

    public static byte[] command_closeBluetooth() {
        byte[] otherByte = new byte[2];
        otherByte[1] = 1;
        processByte(otherByte);
        byte[] _cmd = {21, otherByte[0], otherByte[1]};
        DevicePackManager.printPack(_cmd, _cmd.length);
        Log.i(TAG, " close bluetooth command�?21  " + otherByte[0] + "  " + otherByte[1]);
        return _cmd;
    }

    public static byte[] command_DisconnectBluetooth() {
        byte[] otherByte = new byte[2];
        otherByte[1] = 1;
        processByte(otherByte);
        byte[] _cmd = {22, otherByte[0], otherByte[1]};
        DevicePackManager.printPack(_cmd, _cmd.length);
        Log.i(TAG, " disconnect bluetooth command�?22  " + otherByte[0] + "  " + otherByte[1]);
        return _cmd;
    }
}
