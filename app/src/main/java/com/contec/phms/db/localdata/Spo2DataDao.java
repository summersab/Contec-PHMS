package com.contec.phms.db.localdata;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "LDSPO2")
public class Spo2DataDao {
    public static final String FLAG = "flag";
    public static final String PR = "pr";
    public static final String SPO2 = "spo2";
    public static final String TIME = "time";
    public static final String UNIQUE = "unique";
    @DatabaseField(columnName = "flag", id = false)
    public String mFlag;
    @DatabaseField(generatedId = true)
    public int mID;
    @DatabaseField(columnName = "pr", id = false)
    public String mPr;
    @DatabaseField(columnName = "spo2", id = false)
    public String mSpo2;
    @DatabaseField(columnName = "time", id = false)
    public String mTime;
    @DatabaseField(columnName = "unique", id = false)
    public String mUnique;
}
