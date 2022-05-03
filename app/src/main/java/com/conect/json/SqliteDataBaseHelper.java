package com.conect.json;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SqliteDataBaseHelper extends SQLiteOpenHelper {
    public SqliteDataBaseHelper(Context context) {
        super(context, SqliteConst.DATEBASE_NAME, (SQLiteDatabase.CursorFactory) null, 1);
    }

    public void onCreate(SQLiteDatabase arg0) {
    //    arg0.execSQL(SqliteConst.CREATE_TABLE);
    }

    public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
    }
}
