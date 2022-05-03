package com.contec.phms.device.fhr01;

public class DataHead {
    public int dwFileLength;
    public short mDay;
    public short mHour;
    public short mMilliseconds;
    public short mMinute;
    public short mMonth;
    public short mSecond;
    public short mWeekOfMonth;
    public short mYear;

    public DataHead() {
    }

    public DataHead(short mYear2, short mMonth2, short mWeekOfMonth2, short mDay2, short mHour2, short mMinute2, short mSecond2, short mMilliseconds2, int dwFileLength2) {
        this.mYear = mYear2;
        this.mMonth = mMonth2;
        this.mDay = mDay2;
        this.mHour = mHour2;
        this.mMinute = mMinute2;
        this.mSecond = mSecond2;
        this.mMilliseconds = mMilliseconds2;
        this.dwFileLength = dwFileLength2;
        this.mWeekOfMonth = mWeekOfMonth2;
    }

    public byte[] toByteArray() {
        return new byte[]{(byte) (this.mYear & 255), (byte) ((this.mYear >> 8) & 255), (byte) (this.mMonth & 255), (byte) ((this.mMonth >> 8) & 255), (byte) (this.mWeekOfMonth & 255), (byte) ((this.mWeekOfMonth >> 8) & 255), (byte) (this.mDay & 255), (byte) ((this.mDay >> 8) & 255), (byte) (this.mHour & 255), (byte) ((this.mHour >> 8) & 255), (byte) (this.mMinute & 255), (byte) ((this.mMinute >> 8) & 255), (byte) (this.mSecond & 255), (byte) ((this.mSecond >> 8) & 255), (byte) (this.mMilliseconds & 255), (byte) ((this.mMilliseconds >> 8) & 255), (byte) (this.dwFileLength & 255), (byte) ((this.dwFileLength >> 8) & 255), (byte) ((this.dwFileLength >> 16) & 255), (byte) ((this.dwFileLength >> 24) & 255)};
    }
}
