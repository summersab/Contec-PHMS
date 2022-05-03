package cn.com.contec.jar.wt100;

import android.util.Log;
import java.io.Serializable;
import java.math.BigDecimal;

public class WTDeviceDataJar implements Serializable {
    private static final long serialVersionUID = 1;
    final String TAG = "cn.com.contec.jar.WT100.DeviceData";
    public byte[] datanumber;
    public byte mNum;
    public byte[] mPack;
    public byte mResult_H;
    public byte mResult_L;
    public String m_data;
    public byte m_iDay;
    public byte m_iHour;
    public byte m_iMinute;
    public byte m_iMonth;
    public byte m_iSecond;
    public byte m_iYear;
    public String m_saveDate;
    public byte[] weight;

    public WTDeviceDataJar(byte[] pack) {
        this.mPack = pack;
        if (pack.length == 8) {
            this.m_iYear = pack[0];
            this.m_iMonth = pack[1];
            this.m_iDay = pack[2];
            this.m_iHour = pack[3];
            this.m_iMinute = pack[4];
            this.m_iSecond = 0;
            this.mNum = pack[5];
            this.mResult_H = pack[6];
            this.mResult_L = pack[7];
            this.m_data = new StringBuilder(String.valueOf(getUserMeasureWeigth())).toString();
            this.m_saveDate = getUserMeasureTime();
        } else if (pack.length == 9) {
            this.m_iYear = pack[0];
            this.m_iMonth = pack[1];
            this.m_iDay = pack[2];
            this.m_iHour = pack[3];
            this.m_iMinute = pack[4];
            this.m_iSecond = pack[5];
            this.mNum = pack[6];
            this.mResult_H = pack[7];
            this.mResult_L = pack[8];
            this.m_data = new StringBuilder(String.valueOf(getUserMeasureWeigth())).toString();
            this.m_saveDate = getUserMeasureTime();
        }
    }

    public WTDeviceDataJar() {
    }

    public String toString() {
        Log.i("cn.com.contec.jar.WT100.DeviceData", String.valueOf(this.m_iYear) + "-" + this.m_iMonth + "-" + this.m_iDay + " " + this.m_iHour + ":" + this.m_iMinute + "  " + this.m_iSecond + "  " + this.mResult_H + " " + this.mResult_L);
        String str = String.valueOf(this.m_iYear) + "-" + this.m_iMonth + "-" + this.m_iDay + " " + this.m_iHour + ":" + this.m_iMinute + this.m_iSecond + "  " + this.mResult_H + " " + this.mResult_L;
        return super.toString();
    }

    public String getobj() {
        return String.valueOf(this.m_iYear) + "-" + this.m_iMonth + "-" + this.m_iDay + " " + this.m_iHour + ":" + this.m_iMinute + this.m_iSecond + "  " + new BigDecimal((double) (((float) (((this.mResult_H & 255) << 8) | (this.mResult_L & 255))) / 100.0f)).setScale(2, 4).floatValue();
    }

    public String getUserMeasureResult() {
        return String.valueOf(this.m_iYear) + "-" + this.m_iMonth + "-" + this.m_iDay + " " + this.m_iHour + ":" + this.m_iMinute + "   " + this.m_iSecond + "  " + this.mResult_H + " " + this.mResult_L + "  " + new BigDecimal((double) (((float) (((this.mResult_H & 255) << 8) | (this.mResult_L & 255))) / 100.0f)).setScale(2, 4).floatValue() + "kg";
    }

    public float getUserMeasureWeigth() {
        return new BigDecimal((double) (((float) (((this.mResult_H & 255) << 8) | (this.mResult_L & 255))) / 100.0f)).setScale(2, 4).floatValue();
    }

    public String getUserMeasureTime() {
        String _realyear = new StringBuilder(String.valueOf(this.m_iYear & 255)).toString();
        if (_realyear.length() == 2) {
            _realyear = "20" + _realyear;
        }
        return String.valueOf(_realyear) + "-" + this.m_iMonth + "-" + this.m_iDay + " " + this.m_iHour + ":" + this.m_iMinute + ":" + this.m_iSecond;
    }
}
