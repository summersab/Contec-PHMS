package com.contec.phms.upload.cases.spo2;

import java.io.OutputStream;
import java.io.Serializable;

public class SaveTime_T implements Serializable {
    private static final long serialVersionUID = 1;
    public long m_day = 0;
    public long m_hour = 0;
    public long m_minute = 0;
    public long m_month = 0;
    public long m_second = 0;
    public long m_year = 0;

    public long Year() {
        return this.m_year;
    }

    public long Month() {
        return this.m_month;
    }

    public long Day() {
        return this.m_day;
    }

    public long Hour() {
        return this.m_hour;
    }

    public long Minute() {
        return this.m_minute;
    }

    public long Second() {
        return this.m_second;
    }

    public void SetTime(long y, long mon, long d, long h, long min, long s) {
        this.m_year = y;
        this.m_month = mon;
        this.m_day = d;
        this.m_hour = h;
        this.m_minute = min;
        this.m_second = s;
    }

    void SetSaveDate(long y, long mon, long d) {
        this.m_year = y;
        this.m_month = mon;
        this.m_day = d;
    }

    void SetSaveTime(long h, long min, long s) {
        this.m_hour = h;
        this.m_minute = min;
        this.m_second = s;
    }

    public boolean writeToFile(OutputStream out) {
        Util.writeLong(out, this.m_year);
        Util.writeLong(out, this.m_month);
        Util.writeLong(out, this.m_day);
        Util.writeLong(out, this.m_hour);
        Util.writeLong(out, this.m_minute);
        Util.writeLong(out, this.m_second);
        return true;
    }
}
