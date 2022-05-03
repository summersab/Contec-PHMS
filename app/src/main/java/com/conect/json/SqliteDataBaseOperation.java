package com.conect.json;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.contec.phms.util.Constants;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SqliteDataBaseOperation {
    private Context mContext;
    private SQLiteDatabase mSqliteData;

    public SqliteDataBaseOperation(Context pContext) {
        this.mContext = pContext;
    }

    private void openDb() {
        if (this.mSqliteData == null) {
            this.mSqliteData = new SqliteDataBaseHelper(this.mContext).getWritableDatabase();
        }
    }

    private void closeDb() {
        if (this.mSqliteData != null) {
            this.mSqliteData.close();
            this.mSqliteData = null;
        }
    }

    public boolean insertData(AppFlowInfo pApp) {
        openDb();
        SimpleDateFormat _formatStr = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat _formatInt = new SimpleDateFormat("yyyyMMdd");
        Date _date = new Date();
        String _strStr = _formatStr.format(_date);
        int _currentTime = 0;
        try {
            _currentTime = Integer.parseInt(_formatInt.format(_date));
        } catch (Exception e) {
            e.printStackTrace();
        }
        long[] jArr = new long[2];
        long[] _lastRecord = selectLastData();
        if (_lastRecord == null) {
            _lastRecord = new long[]{0, 0};
        }
        ContentValues _values = new ContentValues();
        _values.put(SqliteConst.KEY_DATEINT, Integer.valueOf(_currentTime));
        _values.put(SqliteConst.KEY_DATESTR, _strStr);
        CLog.e("SqliteDataBaseOperation", String.valueOf(_strStr) + _currentTime + " ORIGDATDOW: " + _lastRecord[0] + " ORIGDATUP: " + _lastRecord[1]);
        _values.put(SqliteConst.KEY_DOWNLOAD, Long.valueOf(pApp.getDownLoad() - _lastRecord[0] > 0 ? pApp.getDownLoad() - _lastRecord[0] : pApp.getDownLoad()));
        _values.put(SqliteConst.KEY_UPLOAD, Long.valueOf(pApp.getUpLoad() - _lastRecord[1] > 0 ? pApp.getUpLoad() - _lastRecord[1] : pApp.getUpLoad()));
        CLog.e("insertData:pApp.getDownLoad()   pLastRecord[0]", "pApp.getDownLoad(): " + pApp.getDownLoad() + "  pLastRecord[0]: " + _lastRecord[0]);
        CLog.e("insertData:pApp.getUpLoad()   pLastRecord[1]", " pApp.getUpLoad(): " + pApp.getUpLoad() + "  pLastRecord[1]: " + _lastRecord[1]);
        _values.put(SqliteConst.KEY_ORIGDATDOW, Long.valueOf(pApp.getDownLoad()));
        _values.put(SqliteConst.KEY_ORIGDATUP, Long.valueOf(pApp.getUpLoad()));
        Cursor _cursor = this.mSqliteData.rawQuery("select * from " + SqliteConst.TABLE_NAME + " where " + SqliteConst.KEY_DATEINT + " = " + _currentTime, (String[]) null);
        if (_cursor == null || _cursor.getCount() == 0) {
            CLog.e("SqliteDataBaseOperation", "Database operation: insertData");
            this.mSqliteData.insert(SqliteConst.TABLE_NAME, (String) null, _values);
            _cursor.close();
            return true;
        }
        upData(pApp, _currentTime, _cursor, _lastRecord);
        return true;
    }

    public List<AppFlowInfo> selectDataWintInDate(int pStart, int pEnd) {
        CLog.e("SqliteDataBaseOperation", "Database operation: selectDataWintInDate");
        List<AppFlowInfo> _listApp = new ArrayList<>();
        openDb();
        Cursor _cursor = this.mSqliteData.rawQuery("select * from " + SqliteConst.TABLE_NAME + " where " + SqliteConst.KEY_DATEINT + " between " + pStart + " and " + pEnd, (String[]) null);
        if (!(_cursor == null || _cursor.getCount() == 0)) {
            while (_cursor.moveToNext()) {
                _listApp.add(new AppFlowInfo(Long.parseLong(_cursor.getString(3)), Long.parseLong(_cursor.getString(4)), _cursor.getString(1), _cursor.getInt(2)));
            }
        }
        _cursor.close();
        closeDb();
        return _listApp;
    }

    public List<AppFlowInfo> selectDataCustom(String pWhere) {
        CLog.e("SqliteDataBaseOperation", "Database operation: selectDataWintInDate");
        List<AppFlowInfo> _listApp = new ArrayList<>();
        openDb();
        Cursor _cursor = this.mSqliteData.rawQuery("select * from " + SqliteConst.TABLE_NAME + " where " + pWhere, (String[]) null);
        if (!(_cursor == null || _cursor.getCount() == 0)) {
            while (_cursor.moveToNext()) {
                _listApp.add(new AppFlowInfo(Long.parseLong(_cursor.getString(3)), Long.parseLong(_cursor.getString(4)), _cursor.getString(1), _cursor.getInt(2)));
            }
        }
        _cursor.close();
        closeDb();
        return _listApp;
    }

    public ArrayList<AppFlowInfo> selectDataForAll() {
        CLog.e("SqliteDataBaseOperation", "Database operation: selectDataForAll");
        ArrayList<AppFlowInfo> _listApp = new ArrayList<>();
        openDb();
        Cursor _cursor = this.mSqliteData.rawQuery("select * from " + SqliteConst.TABLE_NAME, (String[]) null);
        if (!(_cursor == null || _cursor.getCount() == 0)) {
            while (_cursor.moveToNext()) {
                _listApp.add(new AppFlowInfo(Long.parseLong(_cursor.getString(3)), Long.parseLong(_cursor.getString(4)), _cursor.getString(1), _cursor.getInt(2)));
            }
        }
        _cursor.close();
        closeDb();
        return _listApp;
    }

    public ArrayList<AppFlowInfo> selectDataWithInId(int pIndex, int pCount) {
        CLog.e("SqliteDataBaseOperation", "Database operation: selectDataWithInId");
        ArrayList<AppFlowInfo> _listApp = new ArrayList<>();
        openDb();
        Cursor _cursor = this.mSqliteData.rawQuery("select * from " + SqliteConst.TABLE_NAME + " where " + SqliteConst.KEY_ID + " between " + pIndex + " and " + (pIndex + pCount), (String[]) null);
        if (!(_cursor == null || _cursor.getCount() == 0)) {
            while (_cursor.moveToNext()) {
                _listApp.add(new AppFlowInfo(Long.parseLong(_cursor.getString(3)), Long.parseLong(_cursor.getString(4)), _cursor.getString(1), _cursor.getInt(2)));
            }
        }
        _cursor.close();
        closeDb();
        return _listApp;
    }

    public ArrayList<AppFlowInfo> select(int start, int count) {
        CLog.e("SqliteDataBaseOperation", "Database operation: select(start,end)");
        ArrayList<AppFlowInfo> _listApp = new ArrayList<>();
        openDb();
        Cursor _cursor = this.mSqliteData.rawQuery("select * from " + SqliteConst.TABLE_NAME + " order by " + SqliteConst.KEY_DATEINT + " desc " + " limit " + start + Constants.DOUHAO + count, (String[]) null);
        if (!(_cursor == null || _cursor.getCount() == 0)) {
            while (_cursor.moveToNext()) {
                _listApp.add(new AppFlowInfo(Long.parseLong(_cursor.getString(3)), Long.parseLong(_cursor.getString(4)), _cursor.getString(1), _cursor.getInt(2)));
            }
        }
        _cursor.close();
        closeDb();
        return _listApp;
    }

    public boolean deleData() {
        CLog.e("SqliteDataBaseOperation", "Database operation: deleData");
        openDb();
        this.mSqliteData.delete(SqliteConst.TABLE_NAME, (String) null, (String[]) null);
        closeDb();
        return true;
    }

    public int deleData(String pWhere) {
        CLog.e("SqliteDataBaseOperation", "Database operation: deleData");
        int _row = 0;
        openDb();
        Cursor _cursor = this.mSqliteData.rawQuery("select * from " + SqliteConst.TABLE_NAME + " where " + pWhere, (String[]) null);
        if (!(_cursor == null || _cursor.getCount() == 0)) {
            while (_cursor.moveToNext()) {
                this.mSqliteData.delete(SqliteConst.TABLE_NAME, pWhere, (String[]) null);
                _row++;
            }
        }
        _cursor.close();
        closeDb();
        return _row;
    }

    public long[] selectLastData() {
        CLog.e("SqliteDataBaseOperation", "Database operation: selectLastData");
        long[] _lastRecord = new long[2];
        openDb();
        Cursor _cursor = this.mSqliteData.rawQuery("select * from " + SqliteConst.TABLE_NAME, (String[]) null);
        if (_cursor == null || _cursor.getCount() == 0) {
            _cursor.close();
            return null;
        }
        _cursor.moveToLast();
        try {
            _lastRecord[0] = Long.parseLong(_cursor.getString(5).trim());
            _lastRecord[1] = Long.parseLong(_cursor.getString(6).trim());
            _cursor.close();
            return _lastRecord;
        } catch (Exception e) {
            e.printStackTrace();
            _cursor.close();
            return null;
        }
    }

    public boolean upData(AppFlowInfo pApp, int pNow, Cursor pCursor, long[] pLastRecord) {
        CLog.e("SqliteDataBaseOperation", "Database operation: upData");
        openDb();
        while (pCursor.moveToNext()) {
            ContentValues _values = new ContentValues();
            _values.put(SqliteConst.KEY_DATEINT, Integer.valueOf(pNow));
            _values.put(SqliteConst.KEY_DATESTR, String.valueOf(new StringBuilder(String.valueOf(pNow)).toString().substring(0, 4)) + "-" + new StringBuilder(String.valueOf(pNow)).toString().substring(4, 6) + "-" + new StringBuilder(String.valueOf(pNow)).toString().substring(6, 8));
            long _down = pApp.getDownLoad() - pLastRecord[0];
            long _up = pApp.getUpLoad() - pLastRecord[1];
            CLog.e("upData:pApp.getDownLoad()   pLastRecord[0]", "pApp.getDownLoad(): " + pApp.getDownLoad() + "  pLastRecord[0]: " + pLastRecord[0]);
            CLog.e("upData:pApp.getUpLoad()   pLastRecord[1]", " pApp.getUpLoad(): " + pApp.getUpLoad() + "  pLastRecord[1]: " + pLastRecord[1]);
            if (pApp.getDownLoad() < pLastRecord[0] && pApp.getUpLoad() < pLastRecord[1]) {
                _values.put(SqliteConst.KEY_DOWNLOAD, new StringBuilder(String.valueOf(pApp.getDownLoad() + Long.parseLong(pCursor.getString(4)))).toString());
                _values.put(SqliteConst.KEY_UPLOAD, new StringBuilder(String.valueOf(pApp.getUpLoad() + Long.parseLong(pCursor.getString(3)))).toString());
            } else if (pApp.getDownLoad() >= pLastRecord[0] && pApp.getUpLoad() >= pLastRecord[1]) {
                _values.put(SqliteConst.KEY_DOWNLOAD, new StringBuilder(String.valueOf(Long.parseLong(pCursor.getString(4)) + _down)).toString());
                _values.put(SqliteConst.KEY_UPLOAD, new StringBuilder(String.valueOf(Long.parseLong(pCursor.getString(3)) + _up)).toString());
            }
            _values.put(SqliteConst.KEY_ORIGDATDOW, Long.valueOf(pApp.getDownLoad()));
            _values.put(SqliteConst.KEY_ORIGDATUP, Long.valueOf(pApp.getUpLoad()));
            this.mSqliteData.update(SqliteConst.TABLE_NAME, _values, "dateint = " + pNow, (String[]) null);
        }
        pCursor.close();
        closeDb();
        return true;
    }
}
