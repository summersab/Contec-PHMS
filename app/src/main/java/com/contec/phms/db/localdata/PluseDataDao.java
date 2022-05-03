package com.contec.phms.db.localdata;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "LDPLUSE")
public class PluseDataDao {
    public static final String FLAG = "flag";
    public static final String PLUSE = "pluse";
    public static final String RESULT = "result";
    public static final String TIME = "time";
    public static final String UNIQUE = "unique";
    @DatabaseField(columnName = "flag", id = false)
    public String mFlag;
    @DatabaseField(generatedId = true)
    public int mID;
    @DatabaseField(columnName = "pluse", id = false)
    public String mPluse;
    @DatabaseField(columnName = "result", id = false)
    public String mResult;
    @DatabaseField(columnName = "time", id = false)
    public String mTime;
    @DatabaseField(columnName = "unique", id = false)
    public String mUnique;
}
