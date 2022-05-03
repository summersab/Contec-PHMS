package com.contec.phms.db.localdata.opration;

import android.content.Context;
import com.conect.json.CLog;
import com.contec.phms.db.DatabaseHelper;
import com.contec.phms.db.localdata.PedometerMinDataDao;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

public class PedometerMinDataDaoOperation {
    public static String TAG = PedometerMinDataDaoOperation.class.getSimpleName();
    private static PedometerMinDataDaoOperation mOperation;
    private DatabaseHelper mHelper;
    private Dao<PedometerMinDataDao, String> mPedometerMinDataDao;

    public static PedometerMinDataDaoOperation getInstance(Context pContext) {
        if (mOperation == null) {
            mOperation = new PedometerMinDataDaoOperation(pContext);
        }
        return mOperation;
    }

    private PedometerMinDataDaoOperation(Context pContext) {
        this.mHelper = (DatabaseHelper) OpenHelperManager.getHelper(pContext, DatabaseHelper.class);
    }

    public void insertPedometerMinDao(PedometerMinDataDao pDao) {
        try {
            this.mPedometerMinDataDao = this.mHelper.getPedometerMinDataDao();
            this.mPedometerMinDataDao.create(pDao);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updatePedometerMinData(String unique) {
        try {
            this.mPedometerMinDataDao = this.mHelper.getPedometerMinDataDao();
            List<PedometerMinDataDao> _listbeanDao = this.mPedometerMinDataDao.queryBuilder().where().eq("unique", unique).query();
            if (_listbeanDao != null && _listbeanDao.size() > 0) {
                for (PedometerMinDataDao dao : _listbeanDao) {
                    dao.mFlag = "1";
                    this.mPedometerMinDataDao.update(dao);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean querySql(String mUnique) {
        this.mPedometerMinDataDao = this.mHelper.getPedometerMinDataDao();
        boolean ret = false;
        try {
            List<PedometerMinDataDao> rawResults = this.mPedometerMinDataDao.queryBuilder().where().eq("time", mUnique).query();
            if (rawResults.size() != 0) {
                ret = true;
            }
            CLog.e("tag", new StringBuilder(String.valueOf(rawResults.size())).toString());
        } catch (SQLException e1) {
            e1.printStackTrace();
        }
        return ret;
    }

    public PedometerMinDataDao queryLast() {
        CLog.e("aaaaa", "aaaaaa");
        this.mPedometerMinDataDao = this.mHelper.getPedometerMinDataDao();
        try {
            List<PedometerMinDataDao> rawResults = this.mPedometerMinDataDao.queryBuilder().where().eq("flag", "0").query();
            if (rawResults.size() == 0) {
                return null;
            }
            PedometerMinDataDao ret = rawResults.get(rawResults.size() - 1);
            CLog.e("aaaaa", "aaaaaa" + ret.mTime);
            return ret;
        } catch (SQLException e1) {
            e1.printStackTrace();
            return null;
        }
    }

    public List<PedometerMinDataDao> queryAll() {
        CLog.e("aaaaa", "aaaaaa");
        this.mPedometerMinDataDao = this.mHelper.getPedometerMinDataDao();
        try {
            List<PedometerMinDataDao> rawResults = this.mPedometerMinDataDao.queryBuilder().where().eq("flag", "0").query();
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
