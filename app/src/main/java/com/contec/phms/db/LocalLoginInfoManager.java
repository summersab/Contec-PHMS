package com.contec.phms.db;

import com.conect.json.CLog;
import com.contec.phms.App_phms;
import com.j256.ormlite.dao.Dao;
import java.sql.SQLException;
import java.util.List;

public class LocalLoginInfoManager {
    public static String TAG = LocalLoginInfoManager.class.getSimpleName();
    private static Dao<LocalLoginInfoDao, String> mDao;
    private static LocalLoginInfoManager mOperation;

    public static LocalLoginInfoManager getInstance() {
        if (mOperation == null) {
            mOperation = new LocalLoginInfoManager();
        }
        try {
            mDao = App_phms.getInstance().mHelper.getLocalLoginInfoDao();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return mOperation;
    }

    public boolean querySql(String mThirdCode) {
        boolean ret = false;
        try {
            List<String[]> listResult = mDao.queryRaw("select * from LOCALLOGIN where  ThirdCode = '" + mThirdCode + "'", new String[0]).getResults();
            if (listResult.size() != 0) {
                ret = true;
            }
            CLog.e(TAG, new StringBuilder(String.valueOf(listResult.size())).toString());
        } catch (SQLException e1) {
            e1.printStackTrace();
        }
        return ret;
    }

    public void add(LocalLoginInfoDao dao) {
        try {
            mDao.createOrUpdate(dao);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public LocalLoginInfoDao findByThirdCode(String thirdCode) {
        try {
            return mDao.queryBuilder().where().eq(LocalLoginInfoDao.ThirdCode, thirdCode).query().get(0);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean querySqlThirdCode(String mCardId) {
        boolean ret = false;
        try {
            List<String[]> listResult = mDao.queryRaw("select * from LOCALLOGIN where  CardNb = '" + mCardId + "'", new String[0]).getResults();
            if (listResult.size() != 0) {
                ret = true;
            }
            CLog.e(TAG, new StringBuilder(String.valueOf(listResult.size())).toString());
        } catch (SQLException e1) {
            e1.printStackTrace();
        }
        return ret;
    }

    public LocalLoginInfoDao findByCardId(String mCardId) {
        try {
            return mDao.queryBuilder().where().eq(LocalLoginInfoDao.CardNb, mCardId).query().get(0);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
