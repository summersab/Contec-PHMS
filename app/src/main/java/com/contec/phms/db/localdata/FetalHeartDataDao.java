package com.contec.phms.db.localdata;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "LDFETALHEART")
public class FetalHeartDataDao {
    public static final String FETALHEARTRATE = "fetal_heart_rate";
    public static final String FLAG = "flag";
    public static final String TIME = "time";
    public static final String UNIQUE = "unique";
    @DatabaseField(columnName = "fetal_heart_rate", id = false)
    public String mFetalHeartRate;
    @DatabaseField(columnName = "flag", id = false)
    public String mFlag;
    @DatabaseField(generatedId = true)
    public int mID;
    @DatabaseField(columnName = "time", id = false)
    public String mTime;
    @DatabaseField(columnName = "unique", id = false)
    public String mUnique;
}
