package com.contec.phms.db.localdata.opration;

import android.content.Context;
import com.conect.json.CLog;
import com.contec.phms.db.DatabaseHelper;
import com.contec.phms.db.localdata.CmssxtDataDao;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

public class CmssxtDataDaoOperation {
    private static CmssxtDataDaoOperation mOperation;
    private Dao<CmssxtDataDao, String> mCmssxtDataDao;
    private DatabaseHelper mHelper;

    public static CmssxtDataDaoOperation getInstance(Context pContext) {
        if (mOperation == null) {
            mOperation = new CmssxtDataDaoOperation(pContext);
        }
        return mOperation;
    }

    private CmssxtDataDaoOperation(Context pContext) {
        this.mHelper = (DatabaseHelper) OpenHelperManager.getHelper(pContext, DatabaseHelper.class);
    }

    public void insertCmssxtDataDao(CmssxtDataDao pDao) {
        try {
            this.mCmssxtDataDao = this.mHelper.getCmssxtDataDao();
            this.mCmssxtDataDao.create(pDao);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateCmssxtData(String unique) {
        try {
            this.mCmssxtDataDao = this.mHelper.getCmssxtDataDao();
            List<CmssxtDataDao> _listbeanDao = this.mCmssxtDataDao.queryBuilder().where().eq("unique", unique).query();
            if (_listbeanDao != null && _listbeanDao.size() > 0) {
                for (CmssxtDataDao dao : _listbeanDao) {
                    dao.mFlag = "1";
                    this.mCmssxtDataDao.update(dao);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean querySql(String mUnique) {
        this.mCmssxtDataDao = this.mHelper.getCmssxtDataDao();
        boolean ret = false;
        try {
            List<CmssxtDataDao> rawResults = this.mCmssxtDataDao.queryBuilder().where().eq("time", mUnique).query();
            if (rawResults.size() != 0) {
                ret = true;
            }
            CLog.e("tag", new StringBuilder(String.valueOf(rawResults.size())).toString());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ret;
    }

    public CmssxtDataDao queryLast() {
        CLog.e("aaaaa", "aaaaaa");
        this.mCmssxtDataDao = this.mHelper.getCmssxtDataDao();
        try {
            List<CmssxtDataDao> rawResults = this.mCmssxtDataDao.queryBuilder().where().eq("flag", "0").query();
            if (rawResults.size() == 0) {
                return null;
            }
            CmssxtDataDao ret = rawResults.get(rawResults.size() - 1);
            CLog.e("aaaaa", "aaaaaa" + ret.mTime);
            return ret;
        } catch (SQLException e1) {
            e1.printStackTrace();
            return null;
        }
    }

    public List<CmssxtDataDao> queryAll() {
        CLog.e("aaaaa", "aaaaaa");
        this.mCmssxtDataDao = this.mHelper.getCmssxtDataDao();
        try {
            List<CmssxtDataDao> rawResults = this.mCmssxtDataDao.queryBuilder().where().eq("flag", "0").query();
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
