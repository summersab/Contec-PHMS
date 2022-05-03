package com.contec.phms.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class GetCurrentTimeMillis {
    private int day;
    private int hour;
    private int minute;
    private int month;
    private int seconds;
    private int year;

    public GetCurrentTimeMillis(int year2, int month2, int day2, int hour2, int minute2, int seconds2) {
        this.year = year2;
        this.month = month2;
        this.day = day2;
        this.hour = hour2;
        this.minute = minute2;
        this.seconds = seconds2;
    }

    public String getTimeMillis() {
        long time = 0;
        try {
            time = new SimpleDateFormat("yyyyMMddHHmmss").parse(getTime()).getTime() / 1000;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return Long.toString(time);
    }

    public String getTime() {
        String y = getFormat(this.year + 2000);
        String m = getFormat(this.month);
        String d = getFormat(this.day);
        String h = getFormat(this.hour);
        String min = getFormat(this.minute);
        return String.valueOf(y) + m + d + h + min + getFormat(this.seconds);
    }

    private String getFormat(int i) {
        if (i < 10) {
            return "0" + i;
        }
        return new StringBuilder(String.valueOf(i)).toString();
    }
}
