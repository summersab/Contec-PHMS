package com.contec.phms.db.localdata.opration;

import android.content.Context;
import com.conect.json.CLog;
import com.contec.phms.db.DatabaseHelper;
import com.contec.phms.db.localdata.PluseDataDao;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

public class PluseDataDaoOperation {
    public static String TAG = PluseDataDaoOperation.class.getSimpleName();
    private static PluseDataDaoOperation mOperation;
    private DatabaseHelper mHelper;
    private Dao<PluseDataDao, String> mPluseDataDao;

    public static PluseDataDaoOperation getInstance(Context pContext) {
        if (mOperation == null) {
            mOperation = new PluseDataDaoOperation(pContext);
        }
        return mOperation;
    }

    private PluseDataDaoOperation(Context pContext) {
        this.mHelper = (DatabaseHelper) OpenHelperManager.getHelper(pContext, DatabaseHelper.class);
    }

    public void insertPluseDao(PluseDataDao pDao) {
        try {
            this.mPluseDataDao = this.mHelper.getPluseDataDao();
            this.mPluseDataDao.create(pDao);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updatePluseData(String unique) {
        try {
            this.mPluseDataDao = this.mHelper.getPluseDataDao();
            List<PluseDataDao> _listbeanDao = this.mPluseDataDao.queryBuilder().where().eq("unique", unique).query();
            if (_listbeanDao != null && _listbeanDao.size() > 0) {
                for (PluseDataDao dao : _listbeanDao) {
                    dao.mFlag = "1";
                    this.mPluseDataDao.update(dao);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean querySql(String mUnique) {
        this.mPluseDataDao = this.mHelper.getPluseDataDao();
        boolean ret = false;
        try {
            List<PluseDataDao> rawResults = this.mPluseDataDao.queryBuilder().where().eq("time", mUnique).query();
            if (rawResults != null) {
                if (rawResults.size() != 0) {
                    ret = true;
                }
                CLog.e("tag", new StringBuilder(String.valueOf(rawResults.size())).toString());
            }
        } catch (SQLException e1) {
            e1.printStackTrace();
        }
        return ret;
    }

    public PluseDataDao queryLast() {
        CLog.e("aaaaa", "aaaaaa");
        this.mPluseDataDao = this.mHelper.getPluseDataDao();
        try {
            List<PluseDataDao> rawResults = this.mPluseDataDao.queryBuilder().where().eq("flag", "0").query();
            if (rawResults.size() == 0) {
                return null;
            }
            PluseDataDao ret = rawResults.get(rawResults.size() - 1);
            CLog.e("aaaaa", "aaaaaa" + ret.mTime);
            return ret;
        } catch (SQLException e1) {
            e1.printStackTrace();
            return null;
        }
    }

    public List<PluseDataDao> queryAll() {
        CLog.e("aaaaa", "aaaaaa");
        this.mPluseDataDao = this.mHelper.getPluseDataDao();
        try {
            List<PluseDataDao> rawResults = this.mPluseDataDao.queryBuilder().where().eq("flag", "0").query();
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
