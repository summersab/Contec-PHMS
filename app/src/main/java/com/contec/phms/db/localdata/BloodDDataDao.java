package com.contec.phms.db.localdata;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "LDBLOODD")
public class BloodDDataDao {
    public static final String AVERAGE = "average";
    public static final String FLAG = "flag";
    public static final String HIGH = "high";
    public static final String LOW = "low";
    public static final String TIME = "time";
    public static final String UNIQUE = "unique";
    @DatabaseField(columnName = "average", id = false)
    public String mAverage;
    @DatabaseField(columnName = "flag", id = false)
    public String mFlag;
    @DatabaseField(columnName = "high", id = false)
    public String mHigh;
    @DatabaseField(generatedId = true)
    public int mID;
    @DatabaseField(columnName = "low", id = false)
    public String mLow;
    @DatabaseField(columnName = "time", id = false)
    public String mTime;
    @DatabaseField(columnName = "unique", id = false)
    public String mUnique;
}
