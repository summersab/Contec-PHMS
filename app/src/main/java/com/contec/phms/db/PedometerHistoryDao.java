package com.contec.phms.db;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "PEDOMETER_HISTORY")
public class PedometerHistoryDao {
    public static final String Calories = "Calories";
    public static final String DB_TABLE = "PEDOMETER_HISTORY";
    public static final String Date = "Date";
    public static final String Distance = "Distance";
    public static final String IsUploaded = "IsUploaded";
    public static final String OverTime = "Over_Time";
    public static final String StartTime = "Start_Time";
    public static final String Steps = "Steps";
    public static final String SumCalories = "SumCalories";
    public static final String SumSteps = "SumSteps";
    public static final String SumTime = "SumTime";
    public static final String UploadDate = "UploadDate";
    public static final String UserID = "UserID";
    @DatabaseField(columnName = "Date")
    private String date;
    @DatabaseField(columnName = "Calories")
    private int mCalories;
    @DatabaseField(columnName = "Distance")
    private int mDistance;
    @DatabaseField(generatedId = true)
    private int mId;
    @DatabaseField(columnName = "IsUploaded")
    private int mIsUploaded;
    @DatabaseField(columnName = "Over_Time")
    private String mOverTime;
    @DatabaseField(columnName = "SumTime")
    private int mSecond;
    @DatabaseField(columnName = "Start_Time")
    private String mStartTime;
    @DatabaseField(columnName = "Steps")
    private int mStep;
    @DatabaseField(columnName = "SumCalories")
    private float mSumCalories;
    @DatabaseField(columnName = "SumSteps")
    private int mSumStep;
    @DatabaseField(columnName = "UploadDate")
    private String mUploadDate;
    @DatabaseField(columnName = "UserID")
    private String mUserID;

    public int getmSumStep() {
        return this.mSumStep;
    }

    public void setmSumStep(int mSumStep2) {
        this.mSumStep = mSumStep2;
    }

    public float getmSumCalories() {
        return this.mSumCalories;
    }

    public void setmSumCalories(int mSumCalories2) {
        this.mSumCalories = (float) mSumCalories2;
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

    public void setmUploadDate(String mUploadDate2) {
        this.mUploadDate = mUploadDate2;
    }

    public String getmUploadDate() {
        return this.mUploadDate;
    }

    public int getmId() {
        return this.mId;
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

    public void setmStartTime(String mStartTime2) {
        this.mStartTime = mStartTime2;
    }

    public String getmOverTime() {
        return this.mOverTime;
    }

    public String getmStartTime() {
        return this.mStartTime;
    }

    public void setmOverTime(String mOverTime2) {
        this.mOverTime = mOverTime2;
    }

    public void setmStep(int mStep2) {
        this.mStep = mStep2;
    }

    public int getmStep() {
        return this.mStep;
    }

    public int getmCalories() {
        return this.mCalories;
    }

    public void setmCalories(int mCalories2) {
        this.mCalories = mCalories2;
    }

    public int getmDistance() {
        return this.mDistance;
    }

    public void setmDistance(int mDistance2) {
        this.mDistance = mDistance2;
    }

    public int getmIsUploaded() {
        return this.mIsUploaded;
    }

    public void setmIsUploaded(int mIsUploaded2) {
        this.mIsUploaded = mIsUploaded2;
    }

    public String toString() {
        return new String("mStartTime :" + this.mStartTime + "   mOverTime:" + this.mOverTime + "   mStep:" + this.mStep + "   mSumStep:" + this.mSumStep + "    mCalories:" + this.mCalories + "    mSumCalories:" + this.mSumCalories);
    }
}
