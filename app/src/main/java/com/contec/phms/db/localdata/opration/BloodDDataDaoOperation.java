package com.contec.phms.db.localdata.opration;

import android.content.Context;
import com.conect.json.CLog;
import com.contec.phms.db.DatabaseHelper;
import com.contec.phms.db.localdata.BloodDDataDao;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

public class BloodDDataDaoOperation {
    private static BloodDDataDaoOperation mOperation;
    private Dao<BloodDDataDao, String> mDao;
    private DatabaseHelper mHelper;

    public static BloodDDataDaoOperation getInstance(Context mContext) {
        if (mOperation == null) {
            mOperation = new BloodDDataDaoOperation(mContext);
        }
        return mOperation;
    }

    private BloodDDataDaoOperation(Context mContext) {
        this.mHelper = (DatabaseHelper) OpenHelperManager.getHelper(mContext, DatabaseHelper.class);
    }

    public void insertBloodDDataDao(BloodDDataDao pDao) {
        try {
            this.mDao = this.mHelper.getBloodDDataDao();
            this.mDao.create(pDao);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateBloodDDataDao(String unique) {
        try {
            this.mDao = this.mHelper.getBloodDDataDao();
            List<BloodDDataDao> _listbeanDao = this.mDao.queryBuilder().where().eq("unique", unique).query();
            if (_listbeanDao != null && _listbeanDao.size() > 0) {
                for (BloodDDataDao dao : _listbeanDao) {
                    dao.mFlag = "1";
                    this.mDao.update(dao);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean querySql(String mUnique) {
        this.mDao = this.mHelper.getBloodDDataDao();
        boolean ret = false;
        try {
            List<BloodDDataDao> rawResults = this.mDao.queryBuilder().where().eq("time", mUnique).query();
            if (rawResults.size() != 0) {
                ret = true;
            }
            CLog.e("tag", new StringBuilder(String.valueOf(rawResults.size())).toString());
        } catch (SQLException e1) {
            e1.printStackTrace();
        }
        return ret;
    }

    public BloodDDataDao queryLast() {
        CLog.e("aaaaa", "aaaaaa");
        this.mDao = this.mHelper.getBloodDDataDao();
        try {
            List<BloodDDataDao> rawResults = this.mDao.queryBuilder().where().eq("flag", "0").query();
            if (rawResults.size() == 0) {
                return null;
            }
            BloodDDataDao ret = rawResults.get(rawResults.size() - 1);
            CLog.e("aaaaa", "aaaaaa" + ret.mTime);
            return ret;
        } catch (SQLException e1) {
            e1.printStackTrace();
            return null;
        }
    }

    public List<BloodDDataDao> queryAll() {
        CLog.e("aaaaa", "aaaaaa");
        this.mDao = this.mHelper.getBloodDDataDao();
        try {
            List<BloodDDataDao> rawResults = this.mDao.queryBuilder().where().eq("flag", "0").query();
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
