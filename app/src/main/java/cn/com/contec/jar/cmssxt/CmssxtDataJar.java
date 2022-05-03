package cn.com.contec.jar.cmssxt;

import android.util.Log;
import java.io.Serializable;

public class CmssxtDataJar implements Serializable {
    private static final long serialVersionUID = 1;
    final String TAG = "cn.com.contec.jar.CMSSXT.DeviceData";
    public double m_data;
    public String m_saveDate;

    public CmssxtDataJar() {
    }

    public CmssxtDataJar(byte[] pack) {
        int[] _date = new int[6];
        byte[] _data = new byte[2];
        for (int j = 0; j < _date.length; j++) {
            _date[j] = pack[j] & 255;
        }
        _data[0] = pack[5];
        _data[1] = pack[6];
        _date[5] = pack[7];
        setSaveDate(_date);
        this.m_data = ((double) processValue(_data)) / 10.0d;
        Log.i("DeviceData  CMSSXT", "CMSSXT Data: " + this.m_data + " CMSSXT Date :" + this.m_saveDate + " 秒pack[7]:" + pack[7]);
    }

    public int processValue(byte[] pData) {
        return ((pData[0] & 255) << 8) | (pData[1] & 255);
    }

    public void setSaveDate(int[] p_date) {
        String _year;
        String _month;
        String _day;
        String _hour;
        String _mm;
        StringBuffer buffer = new StringBuffer();
        String _ss = "00";
        if (p_date[0] < 10) {
            _year = "200" + p_date[0];
        } else {
            _year = "20" + p_date[0];
        }
        if (p_date[1] < 10) {
            _month = "0" + p_date[1];
        } else {
            _month = new StringBuilder(String.valueOf(p_date[1])).toString();
        }
        if (p_date[2] < 10) {
            _day = "0" + p_date[2];
        } else {
            _day = new StringBuilder(String.valueOf(p_date[2])).toString();
        }
        if (p_date[3] < 10) {
            _hour = "0" + p_date[3];
        } else {
            _hour = new StringBuilder(String.valueOf(p_date[3])).toString();
        }
        if (p_date[4] < 10) {
            _mm = "0" + p_date[4];
        } else {
            _mm = new StringBuilder(String.valueOf(p_date[4])).toString();
        }
        if (p_date[5] < 10) {
            _ss = "0" + p_date[5];
        } else if (p_date[5] > 9 && p_date[5] < 60) {
            _ss = new StringBuilder(String.valueOf(p_date[5])).toString();
        }
        buffer.append(_year).append("-").append(_month).append("-").append(_day).append(" ").append(_hour).append(":").append(_mm).append(":").append(_ss);
        this.m_saveDate = buffer.toString();
    }

    public String toString() {
        return "m_saveDate:" + this.m_saveDate + "  m_data:" + this.m_data;
    }
}
