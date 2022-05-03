package com.contec.phms.db.localdata.opration;

import android.content.Context;
import com.conect.json.CLog;
import com.contec.phms.db.DatabaseHelper;
import com.contec.phms.db.localdata.FvcDataDao;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

public class FvcDataDaoOperation {
    public static String TAG = FvcDataDaoOperation.class.getSimpleName();
    private static FvcDataDaoOperation mOperation;
    private Dao<FvcDataDao, String> mFvcDataDao;
    private DatabaseHelper mHelper;

    public static FvcDataDaoOperation getInstance(Context pContext) {
        if (mOperation == null) {
            mOperation = new FvcDataDaoOperation(pContext);
        }
        return mOperation;
    }

    private FvcDataDaoOperation(Context pContext) {
        this.mHelper = (DatabaseHelper) OpenHelperManager.getHelper(pContext, DatabaseHelper.class);
    }

    public void insertFvcDao(FvcDataDao pDao) {
        try {
            this.mFvcDataDao = this.mHelper.getFvcDataDao();
            this.mFvcDataDao.create(pDao);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateFvcDataDao(String unique) {
        try {
            this.mFvcDataDao = this.mHelper.getFvcDataDao();
            List<FvcDataDao> _listbeanDao = this.mFvcDataDao.queryBuilder().where().eq("unique", unique).query();
            if (_listbeanDao != null && _listbeanDao.size() > 0) {
                for (FvcDataDao dao : _listbeanDao) {
                    dao.mFlag = "1";
                    this.mFvcDataDao.update(dao);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean querySql(String mUnique) {
        this.mFvcDataDao = this.mHelper.getFvcDataDao();
        boolean ret = false;
        try {
            List<FvcDataDao> rawResults = this.mFvcDataDao.queryBuilder().where().eq("time", mUnique).query();
            if (rawResults.size() != 0) {
                ret = true;
            }
            CLog.e("tag", new StringBuilder(String.valueOf(rawResults.size())).toString());
        } catch (SQLException e1) {
            e1.printStackTrace();
        }
        return ret;
    }

    public FvcDataDao queryLast() {
        CLog.e("aaaaa", "aaaaaa");
        this.mFvcDataDao = this.mHelper.getFvcDataDao();
        try {
            List<FvcDataDao> rawResults = this.mFvcDataDao.queryBuilder().where().eq("flag", "0").query();
            if (rawResults.size() == 0) {
                return null;
            }
            FvcDataDao ret = rawResults.get(rawResults.size() - 1);
            CLog.e("aaaaa", "aaaaaa" + ret.mTime);
            return ret;
        } catch (SQLException e1) {
            e1.printStackTrace();
            return null;
        }
    }

    public List<FvcDataDao> queryAll() {
        CLog.e("aaaaa", "aaaaaa");
        this.mFvcDataDao = this.mHelper.getFvcDataDao();
        try {
            List<FvcDataDao> rawResults = this.mFvcDataDao.queryBuilder().where().eq("flag", "0").query();
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
