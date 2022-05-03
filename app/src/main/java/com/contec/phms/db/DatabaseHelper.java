package com.contec.phms.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import com.contec.phms.db.localdata.BloodDDataDao;
import com.contec.phms.db.localdata.CmssxtDataDao;
import com.contec.phms.db.localdata.FetalHeartDataDao;
import com.contec.phms.db.localdata.FvcDataDao;
import com.contec.phms.db.localdata.PedometerDayDataDao;
import com.contec.phms.db.localdata.PedometerMinDataDao;
import com.contec.phms.db.localdata.PluseDataDao;
import com.contec.phms.db.localdata.Spo2DataDao;
import com.contec.phms.db.localdata.TempertureDataDao;
import com.contec.phms.db.localdata.UrineDataDao;
import com.contec.phms.db.localdata.WeightDataDao;
import com.contec.phms.util.CLog;
import com.contec.phms.util.Constants;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.UpdateBuilder;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import java.sql.SQLException;

public class DatabaseHelper extends OrmLiteSqliteOpenHelper {
    private static final String DATABASE_NAME = "database.db";
    private static final int DATABASE_VERSION = 9;
    private final String LOG_NAME = getClass().getName();
    private Dao<DeviceListItemBeanDao, String> deviceListItemDao;
    private Dao<HistoryDao, String> historyDao;
    private Dao<LocalLoginInfoDao, String> localLoginInfoDao;
    private Dao<BloodDDataDao, String> mBloodDDataDao;
    private Dao<CmssxtDataDao, String> mCmssxtDataDao;
    private Dao<FetalHeartDataDao, String> mFetalHeartDataDao;
    private Dao<FvcDataDao, String> mFvcDataDao;
    private Dao<LoginUserDao, String> mLoginUserDao;
    private Dao<PedometerDayDataDao, String> mPedometerDayDataDao;
    private Dao<PedometerMinDataDao, String> mPedometerMinDataDao;
    private Dao<PluseDataDao, String> mPluseDataDao;
    private Dao<Spo2DataDao, String> mSpo2DataDao;
    private Dao<TempertureDataDao, String> mTempertureDataDao;
    private Dao<UrineDataDao, String> mUrineDataDao;
    private Dao<WeightDataDao, String> mWeightDataDao;
    private Dao<PedometerHistoryDao, String> pedometerhistoryDao;
    private Dao<PedometerSumStepKm, String> pedometersumstepkmDao;
    private Dao<UserInfoDao, String> userDao;

    public Dao<LocalLoginInfoDao, String> getLocalLoginInfoDao() throws SQLException {
        if (this.localLoginInfoDao == null) {
            this.localLoginInfoDao = getDao(LocalLoginInfoDao.class);
        }
        return this.localLoginInfoDao;
    }

    public Dao<LoginUserDao, String> getLoginUserDao() throws SQLException {
        if (this.mLoginUserDao == null) {
            this.mLoginUserDao = getDao(LoginUserDao.class);
        }
        return this.mLoginUserDao;
    }

    public Dao<UserInfoDao, String> getUserDao() throws SQLException {
        if (this.userDao == null) {
            this.userDao = getDao(UserInfoDao.class);
        }
        return this.userDao;
    }

    public Dao<HistoryDao, String> getHistoryDao() throws SQLException {
        if (this.historyDao == null) {
            this.historyDao = getDao(HistoryDao.class);
        }
        return this.historyDao;
    }

    public Dao<PedometerHistoryDao, String> getPedometerhistoryDao() {
        if (this.pedometerhistoryDao == null) {
            try {
                this.pedometerhistoryDao = getDao(PedometerHistoryDao.class);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return this.pedometerhistoryDao;
    }

    public Dao<PedometerSumStepKm, String> getPedometerSumStepKmDao() {
        if (this.pedometersumstepkmDao == null) {
            try {
                this.pedometersumstepkmDao = getDao(PedometerSumStepKm.class);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return this.pedometersumstepkmDao;
    }

    public Dao<DeviceListItemBeanDao, String> getDeviceListItemDao() {
        if (this.deviceListItemDao == null) {
            try {
                this.deviceListItemDao = getDao(DeviceListItemBeanDao.class);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return this.deviceListItemDao;
    }

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, (SQLiteDatabase.CursorFactory) null, 9);
        CLog.e("jxx", "call constructor");
    }

    public Dao<BloodDDataDao, String> getBloodDDataDao() {
        if (this.mBloodDDataDao == null) {
            try {
                this.mBloodDDataDao = getDao(BloodDDataDao.class);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return this.mBloodDDataDao;
    }

    public Dao<CmssxtDataDao, String> getCmssxtDataDao() {
        if (this.mCmssxtDataDao == null) {
            try {
                this.mCmssxtDataDao = getDao(CmssxtDataDao.class);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return this.mCmssxtDataDao;
    }

    public Dao<FetalHeartDataDao, String> getFetalHeartDataDao() {
        if (this.mFetalHeartDataDao == null) {
            try {
                this.mFetalHeartDataDao = getDao(FetalHeartDataDao.class);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return this.mFetalHeartDataDao;
    }

    public Dao<FvcDataDao, String> getFvcDataDao() {
        if (this.mFvcDataDao == null) {
            try {
                this.mFvcDataDao = getDao(FvcDataDao.class);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return this.mFvcDataDao;
    }

    public Dao<WeightDataDao, String> getWeightDataDao() {
        if (this.mWeightDataDao == null) {
            try {
                this.mWeightDataDao = getDao(WeightDataDao.class);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return this.mWeightDataDao;
    }

    public Dao<PedometerMinDataDao, String> getPedometerMinDataDao() {
        if (this.mPedometerMinDataDao == null) {
            try {
                this.mPedometerMinDataDao = getDao(PedometerMinDataDao.class);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return this.mPedometerMinDataDao;
    }

    public Dao<PluseDataDao, String> getPluseDataDao() {
        if (this.mPluseDataDao == null) {
            try {
                this.mPluseDataDao = getDao(PluseDataDao.class);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return this.mPluseDataDao;
    }

    public Dao<Spo2DataDao, String> getSpo2DataDao() {
        if (this.mSpo2DataDao == null) {
            try {
                this.mSpo2DataDao = getDao(Spo2DataDao.class);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return this.mSpo2DataDao;
    }

    public Dao<TempertureDataDao, String> getTempertureDataDao() {
        if (this.mTempertureDataDao == null) {
            try {
                this.mTempertureDataDao = getDao(TempertureDataDao.class);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return this.mTempertureDataDao;
    }

    public Dao<UrineDataDao, String> getUrineDataDao() {
        if (this.mUrineDataDao == null) {
            try {
                this.mUrineDataDao = getDao(UrineDataDao.class);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return this.mUrineDataDao;
    }

    public Dao<PedometerDayDataDao, String> getPedometerDayDataDao() {
        if (this.mPedometerDayDataDao == null) {
            try {
                this.mPedometerDayDataDao = getDao(PedometerDayDataDao.class);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return this.mPedometerDayDataDao;
    }

    public void onCreate(SQLiteDatabase arg0, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, UserInfoDao.class);
            TableUtils.createTable(connectionSource, HistoryDao.class);
            TableUtils.createTable(connectionSource, PedometerHistoryDao.class);
            TableUtils.createTable(connectionSource, PedometerSumStepKm.class);
            TableUtils.createTable(connectionSource, DeviceListItemBeanDao.class);
            TableUtils.createTable(connectionSource, LoginUserDao.class);
            TableUtils.createTable(connectionSource, LocalLoginInfoDao.class);
            TableUtils.createTable(connectionSource, CmssxtDataDao.class);
            TableUtils.createTable(connectionSource, FetalHeartDataDao.class);
            TableUtils.createTable(connectionSource, FvcDataDao.class);
            TableUtils.createTable(connectionSource, PedometerMinDataDao.class);
            TableUtils.createTable(connectionSource, PluseDataDao.class);
            TableUtils.createTable(connectionSource, Spo2DataDao.class);
            TableUtils.createTable(connectionSource, TempertureDataDao.class);
            TableUtils.createTable(connectionSource, UrineDataDao.class);
            TableUtils.createTable(connectionSource, WeightDataDao.class);
            TableUtils.createTable(connectionSource, PedometerDayDataDao.class);
            TableUtils.createTable(connectionSource, BloodDDataDao.class);
        } catch (SQLException e) {
            Log.e(this.LOG_NAME, "Could not create new table.", e);
        }
    }

    public void onUpgrade(SQLiteDatabase arg0, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        CLog.e("jxx", "call upgrade method. oldVersion: " + oldVersion + " newVersion: " + newVersion);
        try {
            CLog.e("SQLiteDatabase", "onUpgrade oldversion======" + oldVersion + "  newVersion:" + newVersion);
            if (oldVersion == 2 && newVersion == 3) {
                if (this.deviceListItemDao == null) {
                    getDeviceListItemDao();
                }
                this.deviceListItemDao.executeRaw("ALTER TABLE `DEVICE_LIST_ITEM_BEAN` ADD COLUMN ReceiveData varchar(20);", new String[0]);
                this.deviceListItemDao.executeRaw("ALTER TABLE `DEVICE_LIST_ITEM_BEAN` ADD COLUMN DataTime varchar(20);", new String[0]);
            } else if (oldVersion == 2 && newVersion == 4) {
                if (this.deviceListItemDao == null) {
                    getDeviceListItemDao();
                }
                this.deviceListItemDao.executeRaw("ALTER TABLE `DEVICE_LIST_ITEM_BEAN` ADD COLUMN ReceiveData varchar(20);", new String[0]);
                this.deviceListItemDao.executeRaw("ALTER TABLE `DEVICE_LIST_ITEM_BEAN` ADD COLUMN DataTime varchar(20);", new String[0]);
                if (this.mLoginUserDao == null) {
                    getLoginUserDao();
                }
                this.mLoginUserDao.executeRaw("ALTER TABLE `loginuserinfo` ADD COLUMN height varchar(20);", new String[0]);
                this.mLoginUserDao.executeRaw("ALTER TABLE `loginuserinfo` ADD COLUMN weight varchar(20);", new String[0]);
                this.mLoginUserDao.executeRaw("ALTER TABLE `loginuserinfo` ADD COLUMN datetime varchar(20);", new String[0]);
            } else if (oldVersion == 2 && newVersion == 5) {
                if (this.deviceListItemDao == null) {
                    getDeviceListItemDao();
                }
                this.deviceListItemDao.executeRaw("ALTER TABLE `DEVICE_LIST_ITEM_BEAN` ADD COLUMN ReceiveData varchar(20);", new String[0]);
                this.deviceListItemDao.executeRaw("ALTER TABLE `DEVICE_LIST_ITEM_BEAN` ADD COLUMN DataTime varchar(20);", new String[0]);
                if (this.mLoginUserDao == null) {
                    getLoginUserDao();
                }
                this.mLoginUserDao.executeRaw("ALTER TABLE `loginuserinfo` ADD COLUMN height varchar(20);", new String[0]);
                this.mLoginUserDao.executeRaw("ALTER TABLE `loginuserinfo` ADD COLUMN weight varchar(20);", new String[0]);
                this.mLoginUserDao.executeRaw("ALTER TABLE `loginuserinfo` ADD COLUMN datetime varchar(20);", new String[0]);
                this.mLoginUserDao.executeRaw("ALTER TABLE `loginuserinfo` ADD COLUMN sporttarget varchar(20);", new String[0]);
                if (this.userDao == null) {
                    getUserDao();
                }
                this.userDao.executeRaw("ALTER TABLE `userinfo` ADD COLUMN StartTime varchar(20);", new String[0]);
                this.userDao.executeRaw("ALTER TABLE `userinfo` ADD COLUMN endtime varchar(20);", new String[0]);
                this.userDao.executeRaw("ALTER TABLE `userinfo` ADD COLUMN dayNum varchar(20);", new String[0]);
            } else if (oldVersion == 2 && newVersion == 6) {
                if (this.deviceListItemDao == null) {
                    getDeviceListItemDao();
                }
                this.deviceListItemDao.executeRaw("ALTER TABLE `DEVICE_LIST_ITEM_BEAN` ADD COLUMN ReceiveData varchar(20);", new String[0]);
                this.deviceListItemDao.executeRaw("ALTER TABLE `DEVICE_LIST_ITEM_BEAN` ADD COLUMN DataTime varchar(20);", new String[0]);
                if (this.mLoginUserDao == null) {
                    getLoginUserDao();
                }
                this.mLoginUserDao.executeRaw("ALTER TABLE `loginuserinfo` ADD COLUMN height varchar(20);", new String[0]);
                this.mLoginUserDao.executeRaw("ALTER TABLE `loginuserinfo` ADD COLUMN weight varchar(20);", new String[0]);
                this.mLoginUserDao.executeRaw("ALTER TABLE `loginuserinfo` ADD COLUMN datetime varchar(20);", new String[0]);
                this.mLoginUserDao.executeRaw("ALTER TABLE `loginuserinfo` ADD COLUMN sporttarget varchar(20);", new String[0]);
                this.mLoginUserDao.executeRaw("ALTER TABLE `loginuserinfo` ADD COLUMN amactivity varchar(20);", new String[0]);
                this.mLoginUserDao.executeRaw("ALTER TABLE `loginuserinfo` ADD COLUMN pmactivity varchar(20);", new String[0]);
                if (this.userDao == null) {
                    getUserDao();
                }
                this.userDao.executeRaw("ALTER TABLE `userinfo` ADD COLUMN StartTime varchar(20);", new String[0]);
                this.userDao.executeRaw("ALTER TABLE `userinfo` ADD COLUMN endtime varchar(20);", new String[0]);
                this.userDao.executeRaw("ALTER TABLE `userinfo` ADD COLUMN dayNum varchar(20);", new String[0]);
            } else if (oldVersion == 2 && newVersion == 7) {
                if (this.deviceListItemDao == null) {
                    getDeviceListItemDao();
                }
                this.deviceListItemDao.executeRaw("ALTER TABLE `DEVICE_LIST_ITEM_BEAN` ADD COLUMN ReceiveData varchar(20);", new String[0]);
                this.deviceListItemDao.executeRaw("ALTER TABLE `DEVICE_LIST_ITEM_BEAN` ADD COLUMN DataTime varchar(20);", new String[0]);
                if (this.mLoginUserDao == null) {
                    getLoginUserDao();
                }
                this.mLoginUserDao.executeRaw("ALTER TABLE `loginuserinfo` ADD COLUMN height varchar(20);", new String[0]);
                this.mLoginUserDao.executeRaw("ALTER TABLE `loginuserinfo` ADD COLUMN weight varchar(20);", new String[0]);
                this.mLoginUserDao.executeRaw("ALTER TABLE `loginuserinfo` ADD COLUMN datetime varchar(20);", new String[0]);
                this.mLoginUserDao.executeRaw("ALTER TABLE `loginuserinfo` ADD COLUMN sporttarget varchar(20);", new String[0]);
                this.mLoginUserDao.executeRaw("ALTER TABLE `loginuserinfo` ADD COLUMN amactivity varchar(20);", new String[0]);
                this.mLoginUserDao.executeRaw("ALTER TABLE `loginuserinfo` ADD COLUMN pmactivity varchar(20);", new String[0]);
                if (this.userDao == null) {
                    getUserDao();
                }
                this.userDao.executeRaw("ALTER TABLE `userinfo` ADD COLUMN StartTime varchar(20);", new String[0]);
                this.userDao.executeRaw("ALTER TABLE `userinfo` ADD COLUMN endtime varchar(20);", new String[0]);
                this.userDao.executeRaw("ALTER TABLE `userinfo` ADD COLUMN dayNum varchar(20);", new String[0]);
                add_colum_version_7();
            } else if (oldVersion == 2 && newVersion == 8) {
                if (this.deviceListItemDao == null) {
                    getDeviceListItemDao();
                }
                this.deviceListItemDao.executeRaw("ALTER TABLE `DEVICE_LIST_ITEM_BEAN` ADD COLUMN ReceiveData varchar(20);", new String[0]);
                this.deviceListItemDao.executeRaw("ALTER TABLE `DEVICE_LIST_ITEM_BEAN` ADD COLUMN DataTime varchar(20);", new String[0]);
                if (this.mLoginUserDao == null) {
                    getLoginUserDao();
                }
                this.mLoginUserDao.executeRaw("ALTER TABLE `loginuserinfo` ADD COLUMN height varchar(20);", new String[0]);
                this.mLoginUserDao.executeRaw("ALTER TABLE `loginuserinfo` ADD COLUMN weight varchar(20);", new String[0]);
                this.mLoginUserDao.executeRaw("ALTER TABLE `loginuserinfo` ADD COLUMN datetime varchar(20);", new String[0]);
                this.mLoginUserDao.executeRaw("ALTER TABLE `loginuserinfo` ADD COLUMN sporttarget varchar(20);", new String[0]);
                this.mLoginUserDao.executeRaw("ALTER TABLE `loginuserinfo` ADD COLUMN amactivity varchar(20);", new String[0]);
                this.mLoginUserDao.executeRaw("ALTER TABLE `loginuserinfo` ADD COLUMN pmactivity varchar(20);", new String[0]);
                if (this.userDao == null) {
                    getUserDao();
                }
                this.userDao.executeRaw("ALTER TABLE `userinfo` ADD COLUMN StartTime varchar(20);", new String[0]);
                this.userDao.executeRaw("ALTER TABLE `userinfo` ADD COLUMN endtime varchar(20);", new String[0]);
                this.userDao.executeRaw("ALTER TABLE `userinfo` ADD COLUMN dayNum varchar(20);", new String[0]);
                add_colum_version_7();
                add_colum_version_8();
            } else if (oldVersion == 2 && newVersion == 9) {
                if (this.deviceListItemDao == null) {
                    getDeviceListItemDao();
                }
                this.deviceListItemDao.executeRaw("ALTER TABLE `DEVICE_LIST_ITEM_BEAN` ADD COLUMN ReceiveData varchar(20);", new String[0]);
                this.deviceListItemDao.executeRaw("ALTER TABLE `DEVICE_LIST_ITEM_BEAN` ADD COLUMN DataTime varchar(20);", new String[0]);
                if (this.mLoginUserDao == null) {
                    getLoginUserDao();
                }
                this.mLoginUserDao.executeRaw("ALTER TABLE `loginuserinfo` ADD COLUMN height varchar(20);", new String[0]);
                this.mLoginUserDao.executeRaw("ALTER TABLE `loginuserinfo` ADD COLUMN weight varchar(20);", new String[0]);
                this.mLoginUserDao.executeRaw("ALTER TABLE `loginuserinfo` ADD COLUMN datetime varchar(20);", new String[0]);
                this.mLoginUserDao.executeRaw("ALTER TABLE `loginuserinfo` ADD COLUMN sporttarget varchar(20);", new String[0]);
                this.mLoginUserDao.executeRaw("ALTER TABLE `loginuserinfo` ADD COLUMN amactivity varchar(20);", new String[0]);
                this.mLoginUserDao.executeRaw("ALTER TABLE `loginuserinfo` ADD COLUMN pmactivity varchar(20);", new String[0]);
                if (this.userDao == null) {
                    getUserDao();
                }
                this.userDao.executeRaw("ALTER TABLE `userinfo` ADD COLUMN StartTime varchar(20);", new String[0]);
                this.userDao.executeRaw("ALTER TABLE `userinfo` ADD COLUMN endtime varchar(20);", new String[0]);
                this.userDao.executeRaw("ALTER TABLE `userinfo` ADD COLUMN dayNum varchar(20);", new String[0]);
                add_colum_version_7();
                add_colum_version_8();
                add_colum_version_9();
            } else if (oldVersion == 3 && newVersion == 4) {
                if (this.mLoginUserDao == null) {
                    getLoginUserDao();
                }
                this.mLoginUserDao.executeRaw("ALTER TABLE `loginuserinfo` ADD COLUMN height varchar(20);", new String[0]);
                this.mLoginUserDao.executeRaw("ALTER TABLE `loginuserinfo` ADD COLUMN weight varchar(20);", new String[0]);
                this.mLoginUserDao.executeRaw("ALTER TABLE `loginuserinfo` ADD COLUMN datetime varchar(20);", new String[0]);
            } else if (oldVersion == 3 && newVersion == 5) {
                if (this.mLoginUserDao == null) {
                    getLoginUserDao();
                }
                this.mLoginUserDao.executeRaw("ALTER TABLE `loginuserinfo` ADD COLUMN height varchar(20);", new String[0]);
                this.mLoginUserDao.executeRaw("ALTER TABLE `loginuserinfo` ADD COLUMN weight varchar(20);", new String[0]);
                this.mLoginUserDao.executeRaw("ALTER TABLE `loginuserinfo` ADD COLUMN datetime varchar(20);", new String[0]);
                this.mLoginUserDao.executeRaw("ALTER TABLE `loginuserinfo` ADD COLUMN sporttarget varchar(20);", new String[0]);
                if (this.userDao == null) {
                    getUserDao();
                }
                this.userDao.executeRaw("ALTER TABLE `userinfo` ADD COLUMN StartTime varchar(20);", new String[0]);
                this.userDao.executeRaw("ALTER TABLE `userinfo` ADD COLUMN endtime varchar(20);", new String[0]);
                this.userDao.executeRaw("ALTER TABLE `userinfo` ADD COLUMN dayNum varchar(20);", new String[0]);
            } else if (oldVersion == 3 && newVersion == 6) {
                if (this.mLoginUserDao == null) {
                    getLoginUserDao();
                }
                this.mLoginUserDao.executeRaw("ALTER TABLE `loginuserinfo` ADD COLUMN height varchar(20);", new String[0]);
                this.mLoginUserDao.executeRaw("ALTER TABLE `loginuserinfo` ADD COLUMN weight varchar(20);", new String[0]);
                this.mLoginUserDao.executeRaw("ALTER TABLE `loginuserinfo` ADD COLUMN datetime varchar(20);", new String[0]);
                this.mLoginUserDao.executeRaw("ALTER TABLE `loginuserinfo` ADD COLUMN sporttarget varchar(20);", new String[0]);
                this.mLoginUserDao.executeRaw("ALTER TABLE `loginuserinfo` ADD COLUMN amactivity varchar(20);", new String[0]);
                this.mLoginUserDao.executeRaw("ALTER TABLE `loginuserinfo` ADD COLUMN pmactivity varchar(20);", new String[0]);
                if (this.userDao == null) {
                    getUserDao();
                }
                this.userDao.executeRaw("ALTER TABLE `userinfo` ADD COLUMN StartTime varchar(20);", new String[0]);
                this.userDao.executeRaw("ALTER TABLE `userinfo` ADD COLUMN endtime varchar(20);", new String[0]);
                this.userDao.executeRaw("ALTER TABLE `userinfo` ADD COLUMN dayNum varchar(20);", new String[0]);
            } else if (oldVersion == 3 && newVersion == 7) {
                if (this.mLoginUserDao == null) {
                    getLoginUserDao();
                }
                this.mLoginUserDao.executeRaw("ALTER TABLE `loginuserinfo` ADD COLUMN height varchar(20);", new String[0]);
                this.mLoginUserDao.executeRaw("ALTER TABLE `loginuserinfo` ADD COLUMN weight varchar(20);", new String[0]);
                this.mLoginUserDao.executeRaw("ALTER TABLE `loginuserinfo` ADD COLUMN datetime varchar(20);", new String[0]);
                this.mLoginUserDao.executeRaw("ALTER TABLE `loginuserinfo` ADD COLUMN sporttarget varchar(20);", new String[0]);
                this.mLoginUserDao.executeRaw("ALTER TABLE `loginuserinfo` ADD COLUMN amactivity varchar(20);", new String[0]);
                this.mLoginUserDao.executeRaw("ALTER TABLE `loginuserinfo` ADD COLUMN pmactivity varchar(20);", new String[0]);
                if (this.userDao == null) {
                    getUserDao();
                }
                this.userDao.executeRaw("ALTER TABLE `userinfo` ADD COLUMN StartTime varchar(20);", new String[0]);
                this.userDao.executeRaw("ALTER TABLE `userinfo` ADD COLUMN endtime varchar(20);", new String[0]);
                this.userDao.executeRaw("ALTER TABLE `userinfo` ADD COLUMN dayNum varchar(20);", new String[0]);
                add_colum_version_7();
            } else if (oldVersion == 3 && newVersion == 8) {
                if (this.mLoginUserDao == null) {
                    getLoginUserDao();
                }
                this.mLoginUserDao.executeRaw("ALTER TABLE `loginuserinfo` ADD COLUMN height varchar(20);", new String[0]);
                this.mLoginUserDao.executeRaw("ALTER TABLE `loginuserinfo` ADD COLUMN weight varchar(20);", new String[0]);
                this.mLoginUserDao.executeRaw("ALTER TABLE `loginuserinfo` ADD COLUMN datetime varchar(20);", new String[0]);
                this.mLoginUserDao.executeRaw("ALTER TABLE `loginuserinfo` ADD COLUMN sporttarget varchar(20);", new String[0]);
                this.mLoginUserDao.executeRaw("ALTER TABLE `loginuserinfo` ADD COLUMN amactivity varchar(20);", new String[0]);
                this.mLoginUserDao.executeRaw("ALTER TABLE `loginuserinfo` ADD COLUMN pmactivity varchar(20);", new String[0]);
                if (this.userDao == null) {
                    getUserDao();
                }
                this.userDao.executeRaw("ALTER TABLE `userinfo` ADD COLUMN StartTime varchar(20);", new String[0]);
                this.userDao.executeRaw("ALTER TABLE `userinfo` ADD COLUMN endtime varchar(20);", new String[0]);
                this.userDao.executeRaw("ALTER TABLE `userinfo` ADD COLUMN dayNum varchar(20);", new String[0]);
                add_colum_version_7();
                add_colum_version_8();
            } else if (oldVersion == 3 && newVersion == 9) {
                if (this.mLoginUserDao == null) {
                    getLoginUserDao();
                }
                this.mLoginUserDao.executeRaw("ALTER TABLE `loginuserinfo` ADD COLUMN height varchar(20);", new String[0]);
                this.mLoginUserDao.executeRaw("ALTER TABLE `loginuserinfo` ADD COLUMN weight varchar(20);", new String[0]);
                this.mLoginUserDao.executeRaw("ALTER TABLE `loginuserinfo` ADD COLUMN datetime varchar(20);", new String[0]);
                this.mLoginUserDao.executeRaw("ALTER TABLE `loginuserinfo` ADD COLUMN sporttarget varchar(20);", new String[0]);
                this.mLoginUserDao.executeRaw("ALTER TABLE `loginuserinfo` ADD COLUMN amactivity varchar(20);", new String[0]);
                this.mLoginUserDao.executeRaw("ALTER TABLE `loginuserinfo` ADD COLUMN pmactivity varchar(20);", new String[0]);
                if (this.userDao == null) {
                    getUserDao();
                }
                this.userDao.executeRaw("ALTER TABLE `userinfo` ADD COLUMN StartTime varchar(20);", new String[0]);
                this.userDao.executeRaw("ALTER TABLE `userinfo` ADD COLUMN endtime varchar(20);", new String[0]);
                this.userDao.executeRaw("ALTER TABLE `userinfo` ADD COLUMN dayNum varchar(20);", new String[0]);
                add_colum_version_7();
                add_colum_version_8();
                add_colum_version_9();
            } else if (oldVersion == 4 && newVersion == 5) {
                if (this.mLoginUserDao == null) {
                    getLoginUserDao();
                }
                this.mLoginUserDao.executeRaw("ALTER TABLE `loginuserinfo` ADD COLUMN sporttarget varchar(20);", new String[0]);
                if (this.userDao == null) {
                    getUserDao();
                }
                this.userDao.executeRaw("ALTER TABLE `userinfo` ADD COLUMN StartTime varchar(20);", new String[0]);
                this.userDao.executeRaw("ALTER TABLE `userinfo` ADD COLUMN endtime varchar(20);", new String[0]);
                this.userDao.executeRaw("ALTER TABLE `userinfo` ADD COLUMN dayNum varchar(20);", new String[0]);
            } else if (oldVersion == 4 && newVersion == 6) {
                if (this.mLoginUserDao == null) {
                    getLoginUserDao();
                }
                this.mLoginUserDao.executeRaw("ALTER TABLE `loginuserinfo` ADD COLUMN sporttarget varchar(20);", new String[0]);
                this.mLoginUserDao.executeRaw("ALTER TABLE `loginuserinfo` ADD COLUMN amactivity varchar(20);", new String[0]);
                this.mLoginUserDao.executeRaw("ALTER TABLE `loginuserinfo` ADD COLUMN pmactivity varchar(20);", new String[0]);
                if (this.userDao == null) {
                    getUserDao();
                }
                this.userDao.executeRaw("ALTER TABLE `userinfo` ADD COLUMN StartTime varchar(20);", new String[0]);
                this.userDao.executeRaw("ALTER TABLE `userinfo` ADD COLUMN endtime varchar(20);", new String[0]);
                this.userDao.executeRaw("ALTER TABLE `userinfo` ADD COLUMN dayNum varchar(20);", new String[0]);
            } else if (oldVersion == 4 && newVersion == 7) {
                if (this.mLoginUserDao == null) {
                    getLoginUserDao();
                }
                this.mLoginUserDao.executeRaw("ALTER TABLE `loginuserinfo` ADD COLUMN sporttarget varchar(20);", new String[0]);
                this.mLoginUserDao.executeRaw("ALTER TABLE `loginuserinfo` ADD COLUMN amactivity varchar(20);", new String[0]);
                this.mLoginUserDao.executeRaw("ALTER TABLE `loginuserinfo` ADD COLUMN pmactivity varchar(20);", new String[0]);
                if (this.userDao == null) {
                    getUserDao();
                }
                this.userDao.executeRaw("ALTER TABLE `userinfo` ADD COLUMN StartTime varchar(20);", new String[0]);
                this.userDao.executeRaw("ALTER TABLE `userinfo` ADD COLUMN endtime varchar(20);", new String[0]);
                this.userDao.executeRaw("ALTER TABLE `userinfo` ADD COLUMN dayNum varchar(20);", new String[0]);
                add_colum_version_7();
            } else if (oldVersion == 4 && newVersion == 8) {
                if (this.mLoginUserDao == null) {
                    getLoginUserDao();
                }
                this.mLoginUserDao.executeRaw("ALTER TABLE `loginuserinfo` ADD COLUMN sporttarget varchar(20);", new String[0]);
                this.mLoginUserDao.executeRaw("ALTER TABLE `loginuserinfo` ADD COLUMN amactivity varchar(20);", new String[0]);
                this.mLoginUserDao.executeRaw("ALTER TABLE `loginuserinfo` ADD COLUMN pmactivity varchar(20);", new String[0]);
                if (this.userDao == null) {
                    getUserDao();
                }
                this.userDao.executeRaw("ALTER TABLE `userinfo` ADD COLUMN StartTime varchar(20);", new String[0]);
                this.userDao.executeRaw("ALTER TABLE `userinfo` ADD COLUMN endtime varchar(20);", new String[0]);
                this.userDao.executeRaw("ALTER TABLE `userinfo` ADD COLUMN dayNum varchar(20);", new String[0]);
                add_colum_version_7();
                add_colum_version_8();
            } else if (oldVersion == 4 && newVersion == 9) {
                if (this.mLoginUserDao == null) {
                    getLoginUserDao();
                }
                this.mLoginUserDao.executeRaw("ALTER TABLE `loginuserinfo` ADD COLUMN sporttarget varchar(20);", new String[0]);
                this.mLoginUserDao.executeRaw("ALTER TABLE `loginuserinfo` ADD COLUMN amactivity varchar(20);", new String[0]);
                this.mLoginUserDao.executeRaw("ALTER TABLE `loginuserinfo` ADD COLUMN pmactivity varchar(20);", new String[0]);
                if (this.userDao == null) {
                    getUserDao();
                }
                this.userDao.executeRaw("ALTER TABLE `userinfo` ADD COLUMN StartTime varchar(20);", new String[0]);
                this.userDao.executeRaw("ALTER TABLE `userinfo` ADD COLUMN endtime varchar(20);", new String[0]);
                this.userDao.executeRaw("ALTER TABLE `userinfo` ADD COLUMN dayNum varchar(20);", new String[0]);
                add_colum_version_7();
                add_colum_version_8();
                add_colum_version_9();
            } else if (oldVersion == 5 && newVersion == 6) {
                if (this.mLoginUserDao == null) {
                    getLoginUserDao();
                }
                this.mLoginUserDao.executeRaw("ALTER TABLE `loginuserinfo` ADD COLUMN amactivity varchar(20);", new String[0]);
                this.mLoginUserDao.executeRaw("ALTER TABLE `loginuserinfo` ADD COLUMN pmactivity varchar(20);", new String[0]);
            } else if (oldVersion == 5 && newVersion == 7) {
                if (this.mLoginUserDao == null) {
                    getLoginUserDao();
                }
                this.mLoginUserDao.executeRaw("ALTER TABLE `loginuserinfo` ADD COLUMN amactivity varchar(20);", new String[0]);
                this.mLoginUserDao.executeRaw("ALTER TABLE `loginuserinfo` ADD COLUMN pmactivity varchar(20);", new String[0]);
                add_colum_version_7();
            } else if (oldVersion == 5 && newVersion == 8) {
                if (this.mLoginUserDao == null) {
                    getLoginUserDao();
                }
                this.mLoginUserDao.executeRaw("ALTER TABLE `loginuserinfo` ADD COLUMN amactivity varchar(20);", new String[0]);
                this.mLoginUserDao.executeRaw("ALTER TABLE `loginuserinfo` ADD COLUMN pmactivity varchar(20);", new String[0]);
                add_colum_version_7();
                add_colum_version_8();
            } else if (oldVersion == 5 && newVersion == 9) {
                if (this.mLoginUserDao == null) {
                    getLoginUserDao();
                }
                this.mLoginUserDao.executeRaw("ALTER TABLE `loginuserinfo` ADD COLUMN amactivity varchar(20);", new String[0]);
                this.mLoginUserDao.executeRaw("ALTER TABLE `loginuserinfo` ADD COLUMN pmactivity varchar(20);", new String[0]);
                add_colum_version_7();
                add_colum_version_8();
                add_colum_version_9();
            } else if (oldVersion == 6 && newVersion == 7) {
                CLog.i("jxx", "Add table fields----6---7");
                add_colum_version_7();
            } else if (oldVersion == 6 && newVersion == 8) {
                CLog.i("jxx", "Add table fields----6---8");
                add_colum_version_7();
                add_colum_version_8();
            } else if (oldVersion == 6 && newVersion == 9) {
                CLog.i("jxx", "Add table fields----6---9");
                add_colum_version_7();
                add_colum_version_8();
                add_colum_version_9();
            } else if (oldVersion == 7 && newVersion == 8) {
                CLog.i("jxx", "Add table fields----7---8");
                add_colum_version_8();
            } else if (oldVersion == 7 && newVersion == 9) {
                CLog.i("jxx", "Add table fields----7---9");
                add_colum_version_8();
                add_colum_version_9();
            } else if (oldVersion == 8 && newVersion == 9) {
                CLog.i("jxx", "Add table fields----8---9");
                add_colum_version_9();
            } else {
                try {
                    TableUtils.dropTable(connectionSource, UserInfoDao.class, true);
                    TableUtils.dropTable(connectionSource, HistoryDao.class, true);
                    TableUtils.dropTable(connectionSource, PedometerHistoryDao.class, true);
                    TableUtils.dropTable(connectionSource, PedometerSumStepKm.class, true);
                    TableUtils.dropTable(connectionSource, DeviceListItemBeanDao.class, true);
                    TableUtils.dropTable(connectionSource, LoginUserDao.class, true);
                    onCreate(arg0, connectionSource);
                } catch (SQLException e) {
                    Log.e(this.LOG_NAME, "Could not upgrade the table.", e);
                }
            }
        } catch (Exception e2) {
            e2.printStackTrace();
            CLog.e("jxx", "错误" + e2.toString());
        }
    }

    private void add_colum_version_7() throws SQLException {
        if (this.deviceListItemDao == null) {
            getDeviceListItemDao();
        }
        this.deviceListItemDao.executeRaw("ALTER TABLE `DEVICE_LIST_ITEM_BEAN` ADD COLUMN BluetoothType varchar(20);", new String[0]);
        UpdateBuilder<DeviceListItemBeanDao, String> updateBuilder = this.deviceListItemDao.updateBuilder();
        updateBuilder.updateColumnValue(DeviceListItemBeanDao.BluetoothType, Constants.DEVICE_BLUEBOOTH_TYPE_CLASSIC);
        updateBuilder.update();
        updateBuilder.updateColumnValue(DeviceListItemBeanDao.BluetoothType, Constants.DEVICE_BLUEBOOTH_TYPE_BLE);
        updateBuilder.where().eq(DeviceListItemBeanDao.DeviceName, Constants.CMS50K_NAME);
        updateBuilder.update();
    }

    private void add_colum_version_8() throws SQLException {
        CLog.e("jxx", "call add_colum_version_8 method");
        if (this.deviceListItemDao == null) {
            getDeviceListItemDao();
        }
        this.deviceListItemDao.executeRaw("ALTER TABLE `DEVICE_LIST_ITEM_BEAN` ADD COLUMN BroadcastPacketFiled varchar(20);", new String[0]);
        UpdateBuilder<DeviceListItemBeanDao, String> updateBuilder = this.deviceListItemDao.updateBuilder();
        updateBuilder.updateColumnValue(DeviceListItemBeanDao.BroadcastPacketFiled, "BroadcastPacketNoFiled");
        updateBuilder.update();
        updateBuilder.updateColumnValue(DeviceListItemBeanDao.BroadcastPacketFiled, "BroadcastPacketHasFiled");
        updateBuilder.where().eq(DeviceListItemBeanDao.DeviceName, Constants.CMS50K_NAME);
    }

    private void add_colum_version_9() throws SQLException {
        CLog.e("jxx", "call add_colum_version_9 method");
        if (this.mLoginUserDao == null) {
            getLoginUserDao();
        }
        this.mLoginUserDao.executeRaw("ALTER TABLE `loginuserinfo` ADD COLUMN sportdays varchar(20);", new String[0]);
    }
}
