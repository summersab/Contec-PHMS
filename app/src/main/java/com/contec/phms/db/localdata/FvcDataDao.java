package com.contec.phms.db.localdata;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "LDFVC")
public class FvcDataDao {
    public static final String FEF25 = "fef25";
    public static final String FEF2575 = "fef2575";
    public static final String FEF75 = "fef75";
    public static final String FEV1 = "Fev1";
    public static final String FEV1RATE = "fev1rate";
    public static final String FLAG = "flag";
    public static final String FVC = "fvc";
    public static final String PEF = "pef";
    public static final String TIME = "time";
    public static final String UNIQUE = "unique";
    @DatabaseField(columnName = "fef25", id = false)
    public String mFef25;
    @DatabaseField(columnName = "fef2575", id = false)
    public String mFef2575;
    @DatabaseField(columnName = "fef75", id = false)
    public String mFef75;
    @DatabaseField(columnName = "Fev1", id = false)
    public String mFev1;
    @DatabaseField(columnName = "fev1rate", id = false)
    public String mFev1Rate;
    @DatabaseField(columnName = "flag", id = false)
    public String mFlag;
    @DatabaseField(columnName = "fvc", id = false)
    public String mFvc;
    @DatabaseField(generatedId = true)
    public int mID;
    @DatabaseField(columnName = "pef", id = false)
    public String mPef;
    @DatabaseField(columnName = "time", id = false)
    public String mTime;
    @DatabaseField(columnName = "unique", id = false)
    public String mUnique;
}
