package com.contec.phms.db.localdata.opration;

import android.content.Context;
import com.conect.json.CLog;
import com.contec.phms.db.DatabaseHelper;
import com.contec.phms.db.localdata.WeightDataDao;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

public class WeightDataDaoOperation {
    public static String TAG = WeightDataDaoOperation.class.getSimpleName();
    private static WeightDataDaoOperation mOperation;
    private DatabaseHelper mHelper;
    private Dao<WeightDataDao, String> mWeightDataDao;

    public static WeightDataDaoOperation getInstance(Context pContext) {
        if (mOperation == null) {
            mOperation = new WeightDataDaoOperation(pContext);
        }
        return mOperation;
    }

    private WeightDataDaoOperation(Context pContext) {
        this.mHelper = (DatabaseHelper) OpenHelperManager.getHelper(pContext, DatabaseHelper.class);
    }

    public void insertWeightDataDao(WeightDataDao pDao) {
        try {
            this.mWeightDataDao = this.mHelper.getWeightDataDao();
            this.mWeightDataDao.create(pDao);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateWeightData(String unique) {
        try {
            this.mWeightDataDao = this.mHelper.getWeightDataDao();
            List<WeightDataDao> _listbeanDao = this.mWeightDataDao.queryBuilder().where().eq("unique", unique).query();
            if (_listbeanDao != null && _listbeanDao.size() > 0) {
                for (WeightDataDao dao : _listbeanDao) {
                    dao.mFlag = "1";
                    this.mWeightDataDao.update(dao);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean querySql(String mUnique) {
        this.mWeightDataDao = this.mHelper.getWeightDataDao();
        boolean ret = false;
        try {
            List<WeightDataDao> rawResults = this.mWeightDataDao.queryBuilder().where().eq("time", mUnique).query();
            if (rawResults.size() != 0) {
                ret = true;
            }
            CLog.e("tag", new StringBuilder(String.valueOf(rawResults.size())).toString());
        } catch (SQLException e1) {
            e1.printStackTrace();
        }
        return ret;
    }

    public WeightDataDao queryLast() {
        CLog.e("aaaaa", "aaaaaa");
        this.mWeightDataDao = this.mHelper.getWeightDataDao();
        try {
            List<WeightDataDao> rawResults = this.mWeightDataDao.queryBuilder().where().eq("flag", "0").query();
            if (rawResults.size() == 0) {
                return null;
            }
            WeightDataDao ret = rawResults.get(rawResults.size() - 1);
            CLog.e("aaaaa", "aaaaaa" + ret.mTime);
            return ret;
        } catch (SQLException e1) {
            e1.printStackTrace();
            return null;
        }
    }

    public List<WeightDataDao> queryAll() {
        CLog.e("aaaaa", "aaaaaa");
        this.mWeightDataDao = this.mHelper.getWeightDataDao();
        try {
            List<WeightDataDao> rawResults = this.mWeightDataDao.queryBuilder().where().eq("flag", "0").query();
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
