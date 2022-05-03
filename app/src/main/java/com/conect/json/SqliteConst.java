package com.conect.json;

public class SqliteConst {
    public static String TABLE_NAME = "data";
    public static String DATEBASE_NAME = "flow.db";
    public static final String KEY_DATEINT = "dateint";
    public static final String KEY_DATESTR = "datestr";
    public static final String KEY_DOWNLOAD = "download";
    public static final String KEY_ID = "id";
    public static final String KEY_LASTMODTIME = "lastmodtime";
    public static final String KEY_ORIGDATDOW = "Originaldatadownload";
    public static final String KEY_ORIGDATUP = "Originaldataupload";
    public static final String KEY_UPLOAD = "upload";
    public static boolean Log = true;
    public static final int VISION = 1;
    public static final String CREATE_TABLE = ("CREATE TABLE " + TABLE_NAME + "(" + KEY_ID + " integer primary key autoincrement," + KEY_DATESTR + " text not null," + KEY_DATEINT + " integer," + KEY_UPLOAD + " text not null," + KEY_DOWNLOAD + " text not null," + KEY_ORIGDATDOW + " text not null," + KEY_ORIGDATUP + " text not null)");
}
