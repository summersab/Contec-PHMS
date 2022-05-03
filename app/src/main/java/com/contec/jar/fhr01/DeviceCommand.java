package com.contec.jar.fhr01;

public class DeviceCommand {
    public static byte[] DELETE_DATA = {122};
    public static byte[] REQUEST_DATA = {125, -90};

    public static byte[] CONNECTION_REQUEST(byte[] pTime) {
        byte[] _order = new byte[9];
        _order[0] = 125;
        _order[1] = -93;
        int _size = pTime.length;
        for (int i = 0; i < _size; i++) {
            _order[i + 2] = pTime[i];
        }
        return _order;
    }
}
