package com.contec.phms.db.localdata.opration;

import android.content.Context;
import com.conect.json.CLog;
import com.contec.phms.db.DatabaseHelper;
import com.contec.phms.db.localdata.Spo2DataDao;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

public class Spo2DataDaoOperation {
    public static String TAG = Spo2DataDaoOperation.class.getSimpleName();
    private static Spo2DataDaoOperation mOperation;
    private DatabaseHelper mHelper;
    private Dao<Spo2DataDao, String> mSpo2DataDao;

    public static Spo2DataDaoOperation getInstance(Context pContext) {
        if (mOperation == null) {
            mOperation = new Spo2DataDaoOperation(pContext);
        }
        return mOperation;
    }

    private Spo2DataDaoOperation(Context pContext) {
        this.mHelper = (DatabaseHelper) OpenHelperManager.getHelper(pContext, DatabaseHelper.class);
    }

    public void insertSpo2DataDao(Spo2DataDao pDao) {
        try {
            this.mSpo2DataDao = this.mHelper.getSpo2DataDao();
            this.mSpo2DataDao.create(pDao);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateSpo2Data(String unique) {
        try {
            this.mSpo2DataDao = this.mHelper.getSpo2DataDao();
            List<Spo2DataDao> _listbeanDao = this.mSpo2DataDao.queryBuilder().where().eq("unique", unique).query();
            if (_listbeanDao != null && _listbeanDao.size() > 0) {
                for (Spo2DataDao dao : _listbeanDao) {
                    dao.mFlag = "1";
                    this.mSpo2DataDao.update(dao);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean querySql(String mUnique) {
        this.mSpo2DataDao = this.mHelper.getSpo2DataDao();
        boolean ret = false;
        CLog.e("tag", String.valueOf(mUnique) + "==mUnique");
        try {
            List<Spo2DataDao> rawResults = this.mSpo2DataDao.queryBuilder().where().eq("time", mUnique).query();
            if (rawResults.size() != 0) {
                ret = true;
            }
            CLog.e("tag", new StringBuilder(String.valueOf(rawResults.size())).toString());
        } catch (SQLException e1) {
            e1.printStackTrace();
        }
        return ret;
    }

    public Spo2DataDao queryLast() {
        CLog.e("aaaaa", "aaaaaa");
        this.mSpo2DataDao = this.mHelper.getSpo2DataDao();
        try {
            List<Spo2DataDao> rawResults = this.mSpo2DataDao.queryBuilder().where().eq("flag", "0").query();
            if (rawResults.size() == 0) {
                return null;
            }
            Spo2DataDao ret = rawResults.get(rawResults.size() - 1);
            CLog.e("aaaaa", "aaaaaa" + ret.mTime);
            return ret;
        } catch (SQLException e1) {
            e1.printStackTrace();
            return null;
        }
    }

    public List<Spo2DataDao> queryAll() {
        CLog.e("aaaaa", "aaaaaa");
        this.mSpo2DataDao = this.mHelper.getSpo2DataDao();
        try {
            List<Spo2DataDao> rawResults = this.mSpo2DataDao.queryBuilder().where().eq("flag", "0").query();
            if (rawResults.size() == 0) {
                return null;
            }
            Collections.reverse(rawResults);
            return rawResults;
        } catch (SQLException e1) {
            e1.printStackTrace();
            return null;
        }
    }
}
