package com.contec.phms.db;

import com.conect.json.CLog;
import com.contec.phms.App_phms;
import com.contec.phms.manager.device.DeviceBean;
import com.contec.phms.manager.device.DeviceBeanList;
import com.contec.phms.manager.device.DeviceListItem;
import com.contec.phms.util.Constants;
import com.contec.phms.util.PageUtil;
import com.j256.ormlite.dao.Dao;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import u.aly.bs;

public class DeviceListDaoOperation {
    public static String TAG = DeviceListDaoOperation.class.getSimpleName();
    private static Dao<DeviceListItemBeanDao, String> mDao;
    private static DeviceListDaoOperation mOperation;
    private String mSqlStr = "select DeviceName , sum(UseNum)as sumUseNum from DEVICE_LIST_ITEM_BEAN group by DeviceName  order by sumUseNum desc";

    public static DeviceListDaoOperation getInstance() {
        if (mOperation == null) {
            mOperation = new DeviceListDaoOperation();
        }
        mDao = App_phms.getInstance().mHelper.getDeviceListItemDao();
        return mOperation;
    }

    public void updateReceiveDataStr(String pMac, String pDataTime, String pReceiveDataStr) {
        try {
            List<DeviceListItemBeanDao> _listbeanDao = mDao.queryBuilder().where().eq("UserName", App_phms.getInstance().mUserInfo.mUserID).and().eq(DeviceListItemBeanDao.DeviceMAC, pMac).query();
            if (_listbeanDao != null && _listbeanDao.size() > 0) {
                DeviceListItemBeanDao pDao = _listbeanDao.get(0);
                pDao.mReceiveDataStr = pReceiveDataStr;
                pDao.mDataTime = pDataTime;
                mDao.update(pDao);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public DeviceListItemBeanDao getReceiveDataStr(String pMac) {
        try {
            List<DeviceListItemBeanDao> _listbeanDao = mDao.queryBuilder().where().eq("UserName", App_phms.getInstance().mUserInfo.mUserID).and().eq(DeviceListItemBeanDao.DeviceMAC, pMac).query();
            if (_listbeanDao == null || _listbeanDao.size() <= 0) {
                return null;
            }
            return _listbeanDao.get(0);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public DeviceListItem getDeviceListItem() {
        DeviceListItem _deviceListItem = new DeviceListItem();
        try {
            List<DeviceListItemBeanDao> _listbeanDao = mDao.queryBuilder().groupBy(DeviceListItemBeanDao.DeviceName).where().eq("UserName", App_phms.getInstance().mUserInfo.mUserID).query();
            if (_listbeanDao != null) {
                for (int i = 0; i < _listbeanDao.size(); i++) {
                    DeviceBeanList _DeviceBeanList = new DeviceBeanList();
                    DeviceBeanList _List = null;
                    int _position = 0;
                    String _devicename = _listbeanDao.get(i).mDeviceName;
                    _DeviceBeanList.mDeviceName = _devicename;
                    boolean _Sequence = false;
                    for (int n = 0; n < i; n++) {
                        if (_deviceListItem.getDevice(n).mBeanList.get(0).mState == 27 && !_Sequence) {
                            _position = n;
                            _List = _deviceListItem.getDevice(n);
                            _Sequence = true;
                        }
                    }
                    _DeviceBeanList.mState = 27;
                    List<DeviceListItemBeanDao> _listbean = mDao.queryBuilder().where().eq("UserName", App_phms.getInstance().mUserInfo.mUserID).and().eq(DeviceListItemBeanDao.DeviceName, _devicename).query();
                    if (_listbean != null) {
                        for (int j = 0; j < _listbean.size(); j++) {
                            DeviceBean _Bean = null;
                            int _pos = 0;
                            DeviceListItemBeanDao _bean = _listbean.get(j);
                            DeviceBean _devicebean = new DeviceBean(_bean.mDeviceName, _bean.mDeviceMac, _bean.mDeviceCode, _bean.mId, _bean.mReceiveDataStr);
                            _devicebean.setmBluetoothType(_bean.mBluetoothType);
                            _devicebean.setmBroadcastPacketFiled(_bean.mBroadcastPacketFiled);
                            if (PageUtil.getFailedTimes(Constants.DataPath, _devicebean.getDeivceUniqueness()) > 0) {
                                _devicebean.mState = 27;
                                _DeviceBeanList.mState = 27;
                                boolean _Sequencee = false;
                                for (int m = 0; m < j; m++) {
                                    if (_DeviceBeanList.mBeanList.get(m).mState == 27 && !_Sequencee) {
                                        _Bean = _DeviceBeanList.mBeanList.get(m);
                                        _Sequencee = true;
                                        _pos = m;
                                    }
                                }
                            } else {
                                _devicebean.mState = 27;
                            }
                            if (_Bean != null) {
                                _DeviceBeanList.mBeanList.set(_pos, _devicebean);
                                _DeviceBeanList.mBeanList.add(_Bean);
                            } else {
                                _DeviceBeanList.mBeanList.add(_devicebean);
                            }
                        }
                    }
                    if (_List != null) {
                        _deviceListItem.setObject(_position, _DeviceBeanList);
                        _deviceListItem.addDevice(_List);
                    } else {
                        _deviceListItem.addDevice(_DeviceBeanList);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (_deviceListItem.size() > 0 && _deviceListItem.getDevice(0).mBeanList.get(0).mState == 7) {
            for (int m2 = 0; m2 < _deviceListItem.size(); m2++) {
                DeviceBeanList mBeanList = _deviceListItem.getDevice(m2);
                if (mBeanList.mState == 7) {
                    mBeanList.mState = 27;
                }
                for (int n2 = 0; n2 < mBeanList.mBeanList.size(); n2++) {
                    if (mBeanList.mBeanList.get(n2).mState == 7) {
                        mBeanList.mBeanList.get(n2).mState = 27;
                    }
                }
            }
            _deviceListItem.getDevice(0).mState = 7;
            _deviceListItem.getDevice(0).mBeanList.get(0).mState = 7;
        }
        return _deviceListItem;
    }

    public ArrayList<DeviceBean> getDevice() {
        ArrayList<DeviceBean> mBeans = new ArrayList<>();
        try {
            List<DeviceListItemBeanDao> _listbeanDao = mDao.queryBuilder().where().eq("UserName", App_phms.getInstance().mUserInfo.mUserID).query();
            if (_listbeanDao != null) {
                for (int i = 0; i < _listbeanDao.size(); i++) {
                    DeviceBean _Bean = new DeviceBean(bs.b, bs.b);
                    _Bean.mCode = _listbeanDao.get(i).mDeviceCode;
                    _Bean.mDeviceName = _listbeanDao.get(i).mDeviceName;
                    _Bean.mMacAddr = _listbeanDao.get(i).mDeviceMac;
                    _Bean.mReceiveDataStr = _listbeanDao.get(i).mReceiveDataStr;
                    _Bean.mBluetoothType = _listbeanDao.get(i).mBluetoothType;
                    _Bean.mState = 27;
                    mBeans.add(_Bean);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return mBeans;
    }

    public List<DeviceListItemBeanDao> querySql(String pDeviceName) {
        String _sql = "select * from DEVICE_LIST_ITEM_BEAN where  DeviceName = '" + pDeviceName + "' and UserName = '" + App_phms.getInstance().mUserInfo.mUserID + "'";
        List<DeviceListItemBeanDao> _list = new ArrayList<>();
        try {
            List<String[]> _listre = mDao.queryRaw(_sql, new String[0]).getResults();
            for (int i = 0; i < _listre.size(); i++) {
                DeviceListItemBeanDao _listDao = new DeviceListItemBeanDao();
                String[] _resBean = _listre.get(i);
                String _userName = _resBean[0];
                String _deviceCode = _resBean[1];
                String _mac = _resBean[2];
                String _deviceName = _resBean[3];
                int _id = Integer.parseInt(_resBean[4]);
                int _useNum = Integer.parseInt(_resBean[5]);
                boolean _isNew = Boolean.getBoolean(_resBean[6]);
                _listDao.mUserName = _userName;
                _listDao.mDeviceName = _deviceName;
                _listDao.mDeviceCode = _deviceCode;
                _listDao.mDeviceMac = _mac;
                _listDao.mId = _id;
                _listDao.mUseNum = _useNum;
                _listDao.isNew = _isNew;
                _list.add(_listDao);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return _list;
    }

    /* JADX WARNING: Removed duplicated region for block: B:5:0x0037 A[Catch:{ Exception -> 0x004a }] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean isHasMac() {
        /*
            r11 = this;
            com.contec.phms.App_phms r8 = com.contec.phms.App_phms.getInstance()
            com.contec.phms.infos.UserInfo r8 = r8.mUserInfo
            java.lang.String r2 = r8.mUserID
            java.lang.StringBuilder r8 = new java.lang.StringBuilder
            java.lang.String r9 = "select * from DEVICE_LIST_ITEM_BEAN where UserName = '"
            r8.<init>(r9)
            java.lang.StringBuilder r8 = r8.append(r2)
            java.lang.String r9 = "'"
            java.lang.StringBuilder r8 = r8.append(r9)
            java.lang.String r1 = r8.toString()
            r0 = 1
            com.j256.ormlite.dao.Dao<com.contec.phms.db.DeviceListItemBeanDao, java.lang.String> r8 = mDao     // Catch:{ Exception -> 0x004a }
            com.contec.phms.db.DeviceListDaoOperation$1 r9 = new com.contec.phms.db.DeviceListDaoOperation$1     // Catch:{ Exception -> 0x004a }
            r9.<init>()     // Catch:{ Exception -> 0x004a }
            r10 = 0
            java.lang.String[] r10 = new java.lang.String[r10]     // Catch:{ Exception -> 0x004a }
            com.j256.ormlite.dao.GenericRawResults r7 = r8.queryRaw((java.lang.String) r1, r9, (java.lang.String[]) r10)     // Catch:{ Exception -> 0x004a }
            java.util.Iterator r5 = r7.iterator()     // Catch:{ Exception -> 0x004a }
        L_0x0030:
            boolean r8 = r5.hasNext()     // Catch:{ Exception -> 0x004a }
            if (r8 != 0) goto L_0x0037
        L_0x0036:
            return r0
        L_0x0037:
            java.lang.Object r3 = r5.next()     // Catch:{ Exception -> 0x004a }
            com.contec.phms.db.DeviceListItemBeanDao r3 = (com.contec.phms.db.DeviceListItemBeanDao) r3     // Catch:{ Exception -> 0x004a }
            java.lang.String r6 = r3.mDeviceMac     // Catch:{ Exception -> 0x004a }
            if (r6 == 0) goto L_0x0048
            int r8 = r6.length()     // Catch:{ Exception -> 0x004a }
            r9 = 1
            if (r8 >= r9) goto L_0x0030
        L_0x0048:
            r0 = 0
            goto L_0x0036
        L_0x004a:
            r4 = move-exception
            r4.printStackTrace()
            goto L_0x0036
        */
        throw new UnsupportedOperationException("Method not decompiled: com.contec.phms.db.DeviceListDaoOperation.isHasMac():boolean");
    }

    public void insertDevice(DeviceListItemBeanDao pDao) {
        try {
            mDao.create(pDao);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void insertDevice(DeviceBean pBean) {
        if (pBean != null) {
            try {
                DeviceListItemBeanDao _dao = new DeviceListItemBeanDao();
                _dao.mDeviceName = pBean.mDeviceName;
                _dao.mDeviceMac = pBean.mMacAddr;
                _dao.mDeviceCode = pBean.mCode;
                mDao.create(_dao);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void delectDevice(DeviceBean pBean) {
        try {
            DeviceListItemBeanDao pDao = new DeviceListItemBeanDao();
            if (pBean != null) {
                pDao.mId = pBean.mId;
                pDao.mDeviceCode = pBean.mCode;
                pDao.mDeviceName = pBean.mDeviceName;
                mDao.delete(pDao);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delectDevice(DeviceListItemBeanDao pDao) {
        if (pDao != null) {
            try {
                mDao.delete(pDao);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void delectAllDevice() {
        try {
            mDao.delete(mDao.queryForAll());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateDevice(DeviceBean pBean) {
        try {
            DeviceListItemBeanDao pDao = new DeviceListItemBeanDao();
            if (pBean != null) {
                pDao.mId = pBean.mId;
                pDao.mDeviceCode = pBean.mCode;
                pDao.mDeviceName = pBean.mDeviceName;
                pDao.mDeviceMac = pBean.mMacAddr;
                pDao.mUseNum = pBean.mUseNum;
                mDao.update(pDao);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateDevice(DeviceListItemBeanDao pDao) {
        if (pDao != null) {
            try {
                mDao.update(pDao);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void updateDeviceCode(String pId, String pDeviceMac) {
        if (pId != null) {
            try {
                if (!pId.equals(bs.b)) {
                    DeviceListItemBeanDao pDao = mDao.queryForId(pId);
                    CLog.e(TAG, "update mac  befor judge");
                    if (pDao.mDeviceMac == null || pDao.mDeviceMac.equals(bs.b)) {
                        pDao.mDeviceMac = pDeviceMac;
                        CLog.e(TAG, pDao.toString());
                        if (pDao != null) {
                            mDao.update(pDao);
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
