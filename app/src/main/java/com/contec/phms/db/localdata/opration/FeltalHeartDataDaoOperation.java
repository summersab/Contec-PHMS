package com.contec.phms.db.localdata.opration;

import android.content.Context;
import com.conect.json.CLog;
import com.contec.phms.db.DatabaseHelper;
import com.contec.phms.db.localdata.FetalHeartDataDao;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

public class FeltalHeartDataDaoOperation {
    private static FeltalHeartDataDaoOperation mOperation;
    private Dao<FetalHeartDataDao, String> mFetalHeartDataDao;
    private DatabaseHelper mHelper;

    public static FeltalHeartDataDaoOperation getInstance(Context pContext) {
        if (mOperation == null) {
            mOperation = new FeltalHeartDataDaoOperation(pContext);
        }
        return mOperation;
    }

    private FeltalHeartDataDaoOperation(Context pContext) {
        this.mHelper = (DatabaseHelper) OpenHelperManager.getHelper(pContext, DatabaseHelper.class);
    }

    public void insertFetalHeartDataDao(FetalHeartDataDao pDao) {
        try {
            this.mFetalHeartDataDao = this.mHelper.getFetalHeartDataDao();
            this.mFetalHeartDataDao.create(pDao);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateFetalHeartData(String unique) {
        try {
            this.mFetalHeartDataDao = this.mHelper.getFetalHeartDataDao();
            List<FetalHeartDataDao> _listbeanDao = this.mFetalHeartDataDao.queryBuilder().where().eq("unique", unique).query();
            if (_listbeanDao != null && _listbeanDao.size() > 0) {
                for (FetalHeartDataDao dao : _listbeanDao) {
                    dao.mFlag = "1";
                    this.mFetalHeartDataDao.update(dao);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean querySql(String mUnique) {
        this.mFetalHeartDataDao = this.mHelper.getFetalHeartDataDao();
        boolean ret = false;
        try {
            List<FetalHeartDataDao> rawResults = this.mFetalHeartDataDao.queryBuilder().where().eq("time", mUnique).query();
            if (rawResults.size() != 0) {
                ret = true;
            }
            CLog.e("tag", new StringBuilder(String.valueOf(rawResults.size())).toString());
        } catch (SQLException e1) {
            e1.printStackTrace();
        }
        return ret;
    }

    public FetalHeartDataDao queryLast() {
        CLog.e("aaaaa", "aaaaaa");
        this.mFetalHeartDataDao = this.mHelper.getFetalHeartDataDao();
        try {
            List<FetalHeartDataDao> rawResults = this.mFetalHeartDataDao.queryBuilder().where().eq("flag", "0").query();
            if (rawResults.size() == 0) {
                return null;
            }
            FetalHeartDataDao ret = rawResults.get(rawResults.size() - 1);
            CLog.e("aaaaa", "aaaaaa" + ret.mTime);
            return ret;
        } catch (SQLException e1) {
            e1.printStackTrace();
            return null;
        }
    }

    public List<FetalHeartDataDao> queryAll() {
        CLog.e("aaaaa", "aaaaaa");
        this.mFetalHeartDataDao = this.mHelper.getFetalHeartDataDao();
        try {
            List<FetalHeartDataDao> rawResults = this.mFetalHeartDataDao.queryBuilder().where().eq("flag", "0").query();
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
