package com.contec.phms.db.localdata.opration;

import android.content.Context;
import com.contec.phms.db.DatabaseHelper;
import com.contec.phms.db.localdata.UrineDataDao;
import com.contec.phms.util.CLog;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

public class UrineDataDaoOperation {
    public static String TAG = UrineDataDaoOperation.class.getSimpleName();
    private static UrineDataDaoOperation mOperation;
    private DatabaseHelper mHelper;
    private Dao<UrineDataDao, String> mUrineDataDao;

    public static UrineDataDaoOperation getInstance(Context pContext) {
        if (mOperation == null) {
            mOperation = new UrineDataDaoOperation(pContext);
        }
        return mOperation;
    }

    private UrineDataDaoOperation(Context pContext) {
        this.mHelper = (DatabaseHelper) OpenHelperManager.getHelper(pContext, DatabaseHelper.class);
    }

    public void insertUrineDataDao(UrineDataDao pDao) {
        try {
            CLog.e("UrineDataDaoOperation", "insertUrineDataDao");
            this.mUrineDataDao = this.mHelper.getUrineDataDao();
            this.mUrineDataDao.create(pDao);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateUrineData(String unique) {
        try {
            this.mUrineDataDao = this.mHelper.getUrineDataDao();
            List<UrineDataDao> _listbeanDao = this.mUrineDataDao.queryBuilder().where().eq("unique", unique).query();
            if (_listbeanDao != null && _listbeanDao.size() > 0) {
                for (UrineDataDao dao : _listbeanDao) {
                    dao.mFlag = "1";
                    this.mUrineDataDao.update(dao);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean querySql(String mTime) {
        this.mUrineDataDao = this.mHelper.getUrineDataDao();
        boolean ret = false;
        try {
            List<UrineDataDao> rawResults = this.mUrineDataDao.queryBuilder().where().eq("time", mTime).query();
            if (rawResults.size() != 0) {
                ret = true;
            }
            com.conect.json.CLog.e("tag++++++querySql", new StringBuilder(String.valueOf(rawResults.size())).toString());
        } catch (SQLException e1) {
            e1.printStackTrace();
        }
        return ret;
    }

    public UrineDataDao queryLast() {
        com.conect.json.CLog.e("aaaaa", "aaaaaa");
        this.mUrineDataDao = this.mHelper.getUrineDataDao();
        try {
            List<UrineDataDao> rawResults = this.mUrineDataDao.queryBuilder().where().eq("flag", "0").query();
            if (rawResults.size() == 0) {
                return null;
            }
            UrineDataDao ret = rawResults.get(rawResults.size() - 1);
            com.conect.json.CLog.e("aaaaa", "aaaaaa" + ret.mTime);
            return ret;
        } catch (SQLException e1) {
            e1.printStackTrace();
            return null;
        }
    }

    public List<UrineDataDao> queryAll() {
        com.conect.json.CLog.e("aaaaa", "aaaaaa");
        this.mUrineDataDao = this.mHelper.getUrineDataDao();
        try {
            List<UrineDataDao> rawResults = this.mUrineDataDao.queryBuilder().where().eq("flag", "0").query();
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
