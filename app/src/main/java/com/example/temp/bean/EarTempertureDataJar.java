package com.example.temp.bean;

import android.util.Log;
import com.example.temp.bm77_code.DevicePackManager;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import u.aly.dp;

public class EarTempertureDataJar implements Serializable {
    private static final String TAG = "EarTempertureDataJar";
    private static final long serialVersionUID = 1;
    public double m_data;
    public String m_saveDate;

    public EarTempertureDataJar() {
    }

    public EarTempertureDataJar(byte[] pack) {
        DevicePackManager.printPack(pack, pack.length);
        this.m_data = processValue(pack) / 10.0d;
        this.m_saveDate = processTimeStrings(pack);
        Log.e(TAG, "EarTemperture Data: " + this.m_data + " EarTemperture Date :" + this.m_saveDate);
    }

    private static String processTimeStrings(byte[] pack) {
        Calendar cl = Calendar.getInstance();
        cl.set(2000, 0, 1, 0, 0, 0);
        Date startDate = cl.getTime();
        long _benchmarkSecond = startDate.getTime();
        long timeMillis = (long) ((((byte) (((pack[1] & Byte.MAX_VALUE) >> 2) & 1)) << 7) | (pack[4] & Byte.MAX_VALUE) | (((((byte) (((pack[1] & Byte.MAX_VALUE) >> 3) & 1)) << 7) | (pack[5] & Byte.MAX_VALUE)) << 8) | (((((byte) (((pack[1] & Byte.MAX_VALUE) >> 4) & 1)) << 7) | (pack[6] & Byte.MAX_VALUE)) << dp.n) | (((((byte) (((pack[1] & Byte.MAX_VALUE) >> 5) & 1)) << 7) | (pack[7] & Byte.MAX_VALUE)) << 24));
        long _scond = (1000 * timeMillis) + _benchmarkSecond;
        Calendar c2 = Calendar.getInstance();
        c2.setTimeInMillis(_scond);
        Date startDate2 = c2.getTime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dataDate = simpleDateFormat.format(startDate);
        String dataDate2 = simpleDateFormat.format(startDate2);
        Log.d(TAG, "send second  _scond: " + _scond + "  timeMillis:" + timeMillis + "  _benchmarkSecond:" + _benchmarkSecond + " start date :" + dataDate + "  dataDate2:" + dataDate2);
        return dataDate2;
    }

    public double processValue(byte[] pData) {
        int _data;
        byte a = (byte) (pData[1] & 1);
        byte b = (byte) ((pData[1] >> 1) & 1);
        String _dataType = Integer.toHexString((pData[2] >> 4) & 15);
        if (_dataType != null && (_dataType.equals("8") || _dataType.equals("9"))) {
            int _data2 = ((((a << 8) | (pData[2] & Byte.MAX_VALUE)) << 8) | (b << 7) | (pData[3] & 127)) & 65535;
            Log.e(TAG, "Celsius*******************a:" + a + "  b:" + b + "  _data:" + _data2);
            _data = _data2 / 10;
        } else if (_dataType == null || !_dataType.equalsIgnoreCase("C")) {
            _data = 500;
        } else {
            int _data3 = ((((a << 8) | (pData[2] & dp.m)) << 8) | (b << 7) | (pData[3] & 127)) & 65535;
            Log.e(TAG, "Fahrenheit Celsius Mode*******************a:" + a + "  b:" + b + "  Celsius for _data:" + _data3);
            _data = _data3 / 10;
        }
        return (double) _data;
    }

    public String toString() {
        return "m_saveDate:" + this.m_saveDate + "  m_data:" + this.m_data;
    }
}
