package com.contec.phms.db;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import java.util.Date;

@DatabaseTable(tableName = "userinfo")
public class UserInfoDao {
    public static final String AUTOLOGIN = "autologin";
    public static final String BluetoothState = "BluetoothState";
    public static final String DayNum = "dayNum";
    public static final String EndTime = "endtime";
    public static final String LASTLOGINDATE = "lastlogindate";
    public static final String Language = "Language";
    public static final String PSW = "psw";
    public static final String SAVINGMODE = "savingmode";
    public static final String SearchInterval = "SearchInterval";
    public static final String Sex = "Sex";
    public static final String StartTime = "StartTime";
    public static final String UserId = "UserId";
    public static final String UserIsClickSearch = "UserIsClickSearch";
    public static final String UserName = "UserName";
    @DatabaseField(canBeNull = false, columnName = "autologin")
    private boolean autologin;
    @DatabaseField(canBeNull = false, columnName = "lastlogindate")
    private Date lastLoginDate;
    @DatabaseField(canBeNull = false, columnName = "BluetoothState")
    private int mBluetoothState;
    @DatabaseField(canBeNull = false, columnName = "endtime")
    private String mEndtime = "22";
    @DatabaseField(canBeNull = false, columnName = "Language")
    private String mLanguage;
    @DatabaseField(canBeNull = false, columnName = "SearchInterval")
    private int mSearchInterval;
    @DatabaseField(canBeNull = false, columnName = "Sex")
    private String mSex;
    @DatabaseField(canBeNull = false, columnName = "StartTime")
    private String mStartTime = "6";
    @DatabaseField(canBeNull = false, columnName = "dayNum")
    private String mSumDay = "1";
    @DatabaseField(columnName = "UserId", id = true)
    private String mUserId;
    @DatabaseField(canBeNull = false, columnName = "UserIsClickSearch")
    private boolean mUserIsClickSearch;
    @DatabaseField(canBeNull = false, columnName = "UserName")
    private String mUserName;
    @DatabaseField(canBeNull = false, columnName = "psw")
    private String psw;
    @DatabaseField(canBeNull = false, columnName = "savingmode")
    private boolean savingMode;

    public boolean ismUserIsClickSearch() {
        return this.mUserIsClickSearch;
    }

    public void setmUserIsClickSearch(boolean mUserIsClickSearch2) {
        this.mUserIsClickSearch = mUserIsClickSearch2;
    }

    public void setmLanguage(String mLanguage2) {
        this.mLanguage = mLanguage2;
    }

    public String getmLanguage() {
        return this.mLanguage;
    }

    public void setmBluetoothState(int mBluetoothState2) {
        this.mBluetoothState = mBluetoothState2;
    }

    public int getBluetoothstate() {
        return this.mBluetoothState;
    }

    public String getmUserId() {
        return this.mUserId;
    }

    public void setmUserId(String uin) {
        this.mUserId = uin;
    }

    public int getmSearchInterval() {
        return this.mSearchInterval;
    }

    public void setmSearchInterval(int mSearchInterval2) {
        this.mSearchInterval = mSearchInterval2;
    }

    public String getPsw() {
        return this.psw;
    }

    public void setPsw(String psw2) {
        this.psw = psw2;
    }

    public void setmUserName(String mUserName2) {
        this.mUserName = mUserName2;
    }

    public String getmUserName() {
        return this.mUserName;
    }

    public void setmSex(String mSex2) {
        this.mSex = mSex2;
    }

    public String getmSex() {
        return this.mSex;
    }

    public boolean getAutologin() {
        return this.autologin;
    }

    public void setAutologin(boolean autologin2) {
        this.autologin = autologin2;
    }

    public boolean getSavingMode() {
        return this.savingMode;
    }

    public void setSavingMode(boolean savingMode2) {
        this.savingMode = savingMode2;
    }

    public Date getLastLoginData() {
        return this.lastLoginDate;
    }

    public void setLastLoginData(Date lastLoginDate2) {
        this.lastLoginDate = lastLoginDate2;
    }

    public String getmStartTime() {
        return this.mStartTime;
    }

    public String getmEndtime() {
        return this.mEndtime;
    }

    public void setmStartTime(String mStartTime2) {
        this.mStartTime = mStartTime2;
    }

    public void setmEndtime(String mEndtime2) {
        this.mEndtime = mEndtime2;
    }

    public void setmSumDay(String mSumDay2) {
        this.mSumDay = mSumDay2;
    }

    public String getmSumDay() {
        return this.mSumDay;
    }
}
