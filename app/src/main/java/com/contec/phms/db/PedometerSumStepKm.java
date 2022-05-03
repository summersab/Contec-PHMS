package com.contec.phms.db;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import java.util.List;

@DatabaseTable(tableName = "PEDOMETER_HISTORY_SUM")
public class PedometerSumStepKm {
    public static final String Cal = "Cal";
    public static final String DB_TABLE = "PEDOMETER_HISTORY_SUM";
    public static final String Date = "Date";
    public static final String Distance = "SumDistance";
    public static final String Steps = "SumSteps";
    public static final String SumTime = "SumTime";
    public static final String UserID = "UserID";
    @DatabaseField(columnName = "Date")
    private String date;
    @DatabaseField(columnName = "Cal")
    private float mCal;
    @DatabaseField(generatedId = true)
    private int mId;
    @DatabaseField(columnName = "SumTime")
    private int mSecond;
    @DatabaseField(columnName = "SumDistance")
    private int mSumDistance;
    @DatabaseField(columnName = "SumSteps")
    private int mSumStep;
    @DatabaseField(columnName = "UserID")
    private String mUserID;
    public List<PedometerHistoryDao> mlistDaos;

    public float getmCal() {
        return this.mCal;
    }

    public void setmCal(float mCal2) {
        this.mCal = mCal2;
    }

    public int getmSecond() {
        return this.mSecond;
    }

    public void setmSecond(int mSecond2) {
        this.mSecond = mSecond2;
    }

    public void setmUserID(String mUserID2) {
        this.mUserID = mUserID2;
    }

    public String getmUserID() {
        return this.mUserID;
    }

    public int getmId() {
        return this.mId;
    }

    public void setmSumDistance(int mSumDistance2) {
        this.mSumDistance = mSumDistance2;
    }

    public void setmSumStep(int mSumStep2) {
        this.mSumStep = mSumStep2;
    }

    public int getmSumDistance() {
        return this.mSumDistance;
    }

    public int getmSumStep() {
        return this.mSumStep;
    }

    public void setmId(int mId2) {
        this.mId = mId2;
    }

    public String getDate() {
        return this.date;
    }

    public void setDate(String date2) {
        this.date = date2;
    }
}
