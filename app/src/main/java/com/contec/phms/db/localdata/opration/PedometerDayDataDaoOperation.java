package com.contec.phms.db.localdata.opration;

import android.content.Context;
import com.conect.json.CLog;
import com.contec.phms.db.DatabaseHelper;
import com.contec.phms.db.localdata.PedometerDayDataDao;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

public class PedometerDayDataDaoOperation {
    public static String TAG = PedometerDayDataDaoOperation.class.getSimpleName();
    private static PedometerDayDataDaoOperation mOperation;
    private DatabaseHelper mHelper;
    private Dao<PedometerDayDataDao, String> mPedometerDayDataDao;

    public static PedometerDayDataDaoOperation getInstance(Context pContext) {
        if (mOperation == null) {
            mOperation = new PedometerDayDataDaoOperation(pContext);
        }
        return mOperation;
    }

    private PedometerDayDataDaoOperation(Context pContext) {
        this.mHelper = (DatabaseHelper) OpenHelperManager.getHelper(pContext, DatabaseHelper.class);
    }

    public void insertPedometerDayDataDao(PedometerDayDataDao pDao) {
        try {
            this.mPedometerDayDataDao = this.mHelper.getPedometerDayDataDao();
            this.mPedometerDayDataDao.create(pDao);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updatePedometerDayData(String unique) {
        try {
            this.mPedometerDayDataDao = this.mHelper.getPedometerDayDataDao();
            List<PedometerDayDataDao> _listbeanDao = this.mPedometerDayDataDao.queryBuilder().where().eq("unique", unique).query();
            if (_listbeanDao != null && _listbeanDao.size() > 0) {
                for (PedometerDayDataDao dao : _listbeanDao) {
                    dao.mFlag = "1";
                    this.mPedometerDayDataDao.update(dao);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean querySql(String mUnique) {
        this.mPedometerDayDataDao = this.mHelper.getPedometerDayDataDao();
        boolean ret = false;
        try {
            List<PedometerDayDataDao> rawResults = this.mPedometerDayDataDao.queryBuilder().where().eq("time", mUnique).query();
            if (rawResults.size() != 0) {
                ret = true;
            }
            CLog.e("tag", new StringBuilder(String.valueOf(rawResults.size())).toString());
        } catch (SQLException e1) {
            e1.printStackTrace();
        }
        return ret;
    }

    public PedometerDayDataDao queryLast() {
        this.mPedometerDayDataDao = this.mHelper.getPedometerDayDataDao();
        CLog.e("aaaaa", "aaaaaa");
        try {
            List<PedometerDayDataDao> rawResults = this.mPedometerDayDataDao.queryBuilder().where().eq("flag", "0").query();
            if (rawResults.size() == 0) {
                return null;
            }
            PedometerDayDataDao ret = rawResults.get(rawResults.size() - 1);
            CLog.e("aaaaa", "aaaaaa" + ret.mTime);
            return ret;
        } catch (SQLException e1) {
            e1.printStackTrace();
            return null;
        }
    }

    public List<PedometerDayDataDao> queryAll() {
        this.mPedometerDayDataDao = this.mHelper.getPedometerDayDataDao();
        CLog.e("aaaaa", "aaaaaa");
        try {
            List<PedometerDayDataDao> rawResults = this.mPedometerDayDataDao.queryBuilder().where().eq("flag", "0").query();
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
