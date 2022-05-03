package com.contec.phms.db;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "HISTORY")
public class HistoryDao {
    public static final String Content = "Content";
    public static final String DB_TABLE = "HISTORY";
    public static final String Date = "Date";
    public static final String User = "User";
    @DatabaseField(columnName = "Content")
    private String content;
    @DatabaseField(columnName = "Date")
    private String date;
    @DatabaseField(generatedId = true)
    private int mId;
    @DatabaseField(columnName = "User")
    private String user;

    public int getmId() {
        return this.mId;
    }

    public void setmId(int mId2) {
        this.mId = mId2;
    }

    public String getUser() {
        return this.user;
    }

    public void setUser(String user2) {
        this.user = user2;
    }

    public String getDate() {
        return this.date;
    }

    public void setDate(String date2) {
        this.date = date2;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content2) {
        this.content = content2;
    }
}
