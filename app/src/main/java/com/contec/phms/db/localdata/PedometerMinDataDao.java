package com.contec.phms.db.localdata;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "LDPEDOMETERMIN")
public class PedometerMinDataDao {
    public static final String CAL = "cal";
    public static final String CALTARGET = "caltarget";
    public static final String FLAG = "flag";
    public static final String STEPS = "steps";
    public static final String TIME = "time";
    public static final String UNIQUE = "unique";
    @DatabaseField(columnName = "cal", id = false)
    public String mCal;
    @DatabaseField(columnName = "caltarget", id = false)
    public String mCaltarget;
    @DatabaseField(columnName = "flag", id = false)
    public String mFlag;
    @DatabaseField(generatedId = true)
    public int mID;
    @DatabaseField(columnName = "steps", id = false)
    public String mSteps;
    @DatabaseField(columnName = "time", id = false)
    public String mTime;
    @DatabaseField(columnName = "unique", id = false)
    public String mUnique;
}
