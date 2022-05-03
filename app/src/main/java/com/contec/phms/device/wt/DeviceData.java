package com.contec.phms.device.wt;

import com.contec.phms.util.CLog;
import java.math.BigDecimal;
import java.util.Date;
import u.aly.bs;

public class DeviceData extends com.contec.phms.device.template.DeviceData {
    private static final long serialVersionUID = 1;
    public byte mDay;
    public byte mHour;
    public byte mMin;
    public byte mMonth;
    public byte mNum;
    public byte mResult_H;
    public byte mResult_L;
    public byte mSec;
    public byte mYear;
    public String m_receiveDate;

    public void init() {
        this.mDate = new int[6];
        String[] _date = this.m_receiveDate.replace(" ", "-").replace(":", "-").split("-");
        for (int j = 0; j < this.mDate.length; j++) {
            this.mDate[j] = Integer.parseInt(_date[j].toString());
        }
        this.mSaveDate = new Date(this.mDate[0], this.mDate[1], this.mDate[2], this.mDate[3], this.mDate[4], this.mDate[5]);
    }

    public DeviceData() {
    }

    public DeviceData(byte[] pack) {
        super(pack);
        if (pack.length == 8) {
            this.mYear = pack[0];
            this.mMonth = pack[1];
            this.mDay = pack[2];
            this.mHour = pack[3];
            this.mMin = pack[4];
            this.mDate = new int[5];
            for (int i = 0; i < this.mDate.length; i++) {
                this.mDate[i] = pack[i];
            }
            this.mSec = 0;
            setSaveDate();
            this.mNum = pack[5];
            this.mResult_H = pack[6];
            this.mResult_L = pack[7];
            this.mSaveInfo = pack;
        } else if (pack.length == 9) {
            this.mYear = pack[0];
            this.mMonth = pack[1];
            this.mDay = pack[2];
            this.mHour = pack[3];
            this.mMin = pack[4];
            this.mSec = pack[5];
            this.mDate = new int[6];
            for (int i2 = 0; i2 < this.mDate.length; i2++) {
                this.mDate[i2] = pack[i2];
            }
            setSaveDate();
            this.mNum = pack[6];
            this.mResult_H = pack[7];
            this.mResult_L = pack[8];
            this.mSaveInfo = pack;
        }
    }

    public void printData() {
        CLog.i("WT-Datas", String.valueOf(String.valueOf(String.valueOf(String.valueOf(String.valueOf(String.valueOf(String.valueOf(String.valueOf(String.valueOf(String.valueOf(String.valueOf(String.valueOf(String.valueOf(String.valueOf(String.valueOf(String.valueOf(bs.b) + Integer.toHexString(this.mYear)) + " ") + Integer.toHexString(this.mMonth)) + " ") + Integer.toHexString(this.mDay)) + " ") + Integer.toHexString(this.mHour)) + " ") + Integer.toHexString(this.mMin)) + " ") + Integer.toHexString(this.mNum)) + " ") + Integer.toHexString(this.mResult_H)) + " ") + Integer.toHexString(this.mResult_L)) + " ");
    }

    public void initInfo() {
        this.mUploadType = "trend";
        this.mDataType = "wt";
    }

    public void setSaveDate() {
        this.mSaveDate = new Date(this.mYear, this.mMonth, this.mDay, this.mHour, this.mMin, this.mSec);
    }

    public float getUserMeasureWeigth() {
        return new BigDecimal((double) (((float) (((this.mResult_H & 255) << 8) | (this.mResult_L & 255))) / 100.0f)).setScale(2, 4).floatValue();
    }
}
