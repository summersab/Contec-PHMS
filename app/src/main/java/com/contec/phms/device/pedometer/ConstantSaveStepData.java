package com.contec.phms.device.pedometer;

import com.contec.phms.App_phms;
import com.contec.phms.eventbus.EventFragment;
import com.contec.phms.db.PedometerHistoryDao;
import com.contec.phms.db.PedometerSumStepKm;
import com.contec.phms.util.CLog;
import com.j256.ormlite.dao.Dao;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import u.aly.bs;

public class ConstantSaveStepData {
    private final String TAG = getClass().getSimpleName();
    public boolean isVisibleHistoryBtn;
    public float mCalories;
    public int mChangUserLogin;
    public int mDistance;
    private int mDistanceSumTodayInt;
    public float mHistoryCalories;
    public int mHistoryDaoStep;
    public String mStartTimeStr;
    public int mStepsInt;
    private int mStepsSumInt;
    public float mSumCalories;
    private PedometerSumStepKm mSumKmPedometer;
    public int mSumStepsInt;
    public int mTimeInt;
    private int mTotalSeconds;
    public int mUnit;
    public String mUserID;

    public void saveToDB() {
        List<PedometerSumStepKm> _list;
        String mOverTimeStr = new SimpleDateFormat("HH:mm:ss").format(Long.valueOf(System.currentTimeMillis()));
        String mDateUploadStr = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(Long.valueOf(System.currentTimeMillis()));
        String mDateStrSum = new SimpleDateFormat("yyyy-MM-dd").format(Long.valueOf(System.currentTimeMillis()));
        Dao<PedometerSumStepKm, String> mSumDao = App_phms.getInstance().mHelper.getPedometerSumStepKmDao();
        try {
            new ArrayList();
            if (!this.isVisibleHistoryBtn) {
                CLog.e(this.TAG, "logged in: " + this.mUserID);
                _list = mSumDao.queryBuilder().where().eq("Date", mDateStrSum).and().eq("UserID", this.mUserID).query();
            } else {
                CLog.e(this.TAG, "not logged in: " + this.mUserID);
                _list = mSumDao.queryBuilder().where().eq("Date", mDateStrSum).and().eq("UserID", bs.b).query();
            }
            if (_list != null && _list.size() > 0) {
                this.mSumKmPedometer = _list.get(0);
                this.mDistanceSumTodayInt = this.mSumKmPedometer.getmSumDistance();
                this.mStepsSumInt = this.mSumKmPedometer.getmSumStep();
                this.mTotalSeconds = this.mSumKmPedometer.getmSecond();
            }
        } catch (SQLException e2) {
            e2.printStackTrace();
        }
        if (this.mSumKmPedometer == null) {
            this.mSumKmPedometer = new PedometerSumStepKm();
            this.mSumKmPedometer.setDate(mDateStrSum);
            if (this.mUnit == 1) {
                this.mSumKmPedometer.setmSumDistance(this.mDistanceSumTodayInt + this.mDistance);
            } else {
                this.mSumKmPedometer.setmSumDistance(this.mDistanceSumTodayInt + ((int) (((float) this.mDistance) * 3.2808f)));
            }
            this.mSumKmPedometer.setmUserID(this.mUserID);
            this.mSumKmPedometer.setmSumStep(this.mStepsSumInt + this.mStepsInt);
            this.mSumKmPedometer.setmSecond(this.mTotalSeconds);
            this.mSumKmPedometer.setmCal(this.mCalories);
            try {
                mSumDao.create(this.mSumKmPedometer);
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            CLog.e(this.TAG, "第一次存储：" + toString());
        } else {
            this.mSumKmPedometer.setDate(mDateStrSum);
            if (this.mUnit == 1) {
                this.mSumKmPedometer.setmSumDistance(this.mDistanceSumTodayInt + this.mDistance);
            } else {
                this.mSumKmPedometer.setmSumDistance(this.mDistanceSumTodayInt + ((int) (((float) this.mDistance) * 3.2808f)));
            }
            this.mSumKmPedometer.setmSumStep(this.mStepsSumInt + this.mStepsInt);
            this.mSumKmPedometer.setmUserID(this.mUserID);
            this.mSumKmPedometer.setmSecond(this.mTimeInt + this.mTotalSeconds);
            this.mCalories = this.mSumKmPedometer.getmCal() + this.mCalories;
            this.mSumKmPedometer.setmCal(this.mCalories);
            CLog.e(this.TAG, "第二次存储数据" + toString());
            try {
                mSumDao.update(this.mSumKmPedometer);
            } catch (SQLException e12) {
                e12.printStackTrace();
            }
        }
        PedometerHistoryDao _pDao = new PedometerHistoryDao();
        _pDao.setDate(mDateStrSum);
        _pDao.setmCalories((int) this.mCalories);
        if (this.mUnit == 1) {
            _pDao.setmDistance(this.mDistance);
        } else {
            _pDao.setmDistance((int) (((float) this.mDistance) * 3.2808f));
        }
        _pDao.setmStartTime(App_phms.getInstance().mCurrentloginUserInfo.getString("LastSavePedometerDateTime", bs.b));
        _pDao.setmOverTime(mOverTimeStr);
        _pDao.setmStep(this.mHistoryDaoStep);
        _pDao.setmSumStep(this.mStepsSumInt + this.mStepsInt);
        _pDao.setmCalories((int) (this.mHistoryCalories * 100.0f));
        _pDao.setmSumCalories((int) (this.mCalories * 100.0f));
        _pDao.setmIsUploaded(2);
        _pDao.setmUploadDate(mDateUploadStr);
        _pDao.setmUserID(this.mUserID);
        _pDao.setmSecond(this.mTimeInt);
        CLog.e(this.TAG, "saving data: " + toString());
        CLog.i("more", "db save    mHistoryCalories = " + this.mHistoryCalories + "  mCalories" + this.mCalories);
        try {
            App_phms.getInstance().mHelper.getPedometerhistoryDao().create(_pDao);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        App_phms.getInstance().mCurrentloginUserInfo.edit().putString("LastSavePedometerDateTime", new SimpleDateFormat("yyyy-MM-dd HH:mm").format(Long.valueOf(System.currentTimeMillis()))).commit();
        if (!this.isVisibleHistoryBtn && this.mChangUserLogin == 9) {
            EventFragment _fragment = new EventFragment();
            _fragment.setmWhichCommand(4);
            App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(_fragment);
            CLog.e(this.TAG, "Sending command to upload pedometer data");
        }
    }

    public String toString() {
        return " mUserName:" + this.mUserID + "  mStepsInt:" + this.mStepsInt + "   mStepsSumInt: " + this.mStepsSumInt + "  isVisibleHistoryBtn:" + this.isVisibleHistoryBtn;
    }
}
