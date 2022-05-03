package com.contec.phms.db;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "LOCALLOGIN")
public class LocalLoginInfoDao {
    public static final String CardNb = "CardNb";
    public static final String Password = "Password";
    public static final String ThirdCode = "thirdcode";
    @DatabaseField(columnName = "CardNb", id = false)
    public String mCardNb;
    @DatabaseField(generatedId = true)
    private int mId;
    @DatabaseField(columnName = "Password", id = false)
    public String mPassword;
    @DatabaseField(columnName = "thirdcode", id = false)
    public String mThirdCode;
}
