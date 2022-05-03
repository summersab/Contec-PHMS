package com.contec.phms.db.localdata.opration;

import android.content.Context;
import com.conect.json.CLog;
import com.contec.phms.db.DatabaseHelper;
import com.contec.phms.db.localdata.TempertureDataDao;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

public class TempertureDataDaoOperation {
    public static String TAG = TempertureDataDaoOperation.class.getSimpleName();
    private static TempertureDataDaoOperation mOperation;
    private DatabaseHelper mHelper;
    private Dao<TempertureDataDao, String> mTempertureDataDao;

    public static TempertureDataDaoOperation getInstance(Context pContext) {
        if (mOperation == null) {
            mOperation = new TempertureDataDaoOperation(pContext);
        }
        return mOperation;
    }

    private TempertureDataDaoOperation(Context pContext) {
        this.mHelper = (DatabaseHelper) OpenHelperManager.getHelper(pContext, DatabaseHelper.class);
    }

    public void insertTempertureDataDao(TempertureDataDao pDao) {
        try {
            this.mTempertureDataDao = this.mHelper.getTempertureDataDao();
            this.mTempertureDataDao.create(pDao);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateTempertureData(String unique) {
        try {
            this.mTempertureDataDao = this.mHelper.getTempertureDataDao();
            List<TempertureDataDao> _listbeanDao = this.mTempertureDataDao.queryBuilder().where().eq("unique", unique).query();
            if (_listbeanDao != null && _listbeanDao.size() > 0) {
                for (TempertureDataDao dao : _listbeanDao) {
                    dao.mFlag = "1";
                    this.mTempertureDataDao.update(dao);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean querySql(String mUnique) {
        this.mTempertureDataDao = this.mHelper.getTempertureDataDao();
        boolean ret = false;
        try {
            List<TempertureDataDao> rawResults = this.mTempertureDataDao.queryBuilder().where().eq("time", mUnique).query();
            if (rawResults.size() != 0) {
                ret = true;
            }
            CLog.e("tag", new StringBuilder(String.valueOf(rawResults.size())).toString());
        } catch (SQLException e1) {
            e1.printStackTrace();
        }
        return ret;
    }

    public TempertureDataDao queryLast() {
        CLog.e("aaaaa", "aaaaaa");
        this.mTempertureDataDao = this.mHelper.getTempertureDataDao();
        try {
            List<TempertureDataDao> rawResults = this.mTempertureDataDao.queryBuilder().where().eq("flag", "0").query();
            if (rawResults.size() == 0) {
                return null;
            }
            TempertureDataDao ret = rawResults.get(rawResults.size() - 1);
            CLog.e("aaaaa", "aaaaaa" + ret.mTime);
            return ret;
        } catch (SQLException e1) {
            e1.printStackTrace();
            return null;
        }
    }

    public List<TempertureDataDao> queryAll() {
        CLog.e("aaaaa", "aaaaaa");
        this.mTempertureDataDao = this.mHelper.getTempertureDataDao();
        try {
            List<TempertureDataDao> rawResults = this.mTempertureDataDao.queryBuilder().where().eq("flag", "0").query();
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
