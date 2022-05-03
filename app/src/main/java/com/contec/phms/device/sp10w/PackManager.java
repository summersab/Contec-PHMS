package com.contec.phms.device.sp10w;

import android.util.Log;
import com.contec.phms.device.template.DeviceData;
import com.contec.phms.manager.datas.DataList;
import com.contec.phms.manager.datas.DatasContainer;
import com.contec.phms.manager.device.DeviceManager;
import com.contec.phms.db.DeviceListDaoOperation;
import com.contec.phms.db.LoginUserDao;
import com.contec.phms.util.CLog;
import com.contec.phms.util.Constants;
import com.contec.phms.util.PageUtil;
import com.contec.sp10.code.PackManagerSpirometer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import cn.com.contec.jar.cases.ParamInfo;
import cn.com.contec.jar.cases.SerialNumber;
import cn.com.contec.jar.cases.Sp10PatientInfo;
import cn.com.contec.jar.cases.StructData;
import cn.com.contec.jar.sp10w.DeviceDataJar;
import cn.com.contec.jar.sp10w.DevicePackManager;
import u.aly.bs;

public class PackManager extends com.contec.phms.device.template.PackManager {
    public static DeviceData mDeviceData;
    ArrayList<StructData> mDataList;
    int mIndex;
    ParamInfo mParamInfo;
    Sp10PatientInfo mPatientInfo;
    SerialNumber mSerial;

    public PackManager() {
        mDeviceData = new DeviceData((byte[]) null);
        mDeviceData.setUniquenes(DeviceManager.m_DeviceBean.getDeivceUniqueness());
    }

    public byte[] trimPack(byte[] pack) {
        int _count = 0;
        int i = 0;
        while (i < pack.length && pack[i] != 0) {
            _count++;
            i++;
        }
        byte[] _trimedPack = new byte[_count];
        for (int i2 = 0; i2 < _trimedPack.length; i2++) {
            _trimedPack[i2] = pack[i2];
        }
        return _trimedPack;
    }

    public byte[] unPack(byte[] pack) {
        int n = pack.length;
        for (int i = 2; i < n; i++) {
            pack[i] = (byte) (pack[i] & ((byte) ((pack[1] << (9 - i)) | 127)));
        }
        return pack;
    }

    public void processData(byte[] pack) {
    }

    public byte[] doPack(byte[] pack) {
        return null;
    }

    public void saveDeviceData(DevicePackManager m_DevicePackManager) {
        LoginUserDao _loginUserInfo = PageUtil.getLoginUserInfo();
        if (m_DevicePackManager.mDeviceDataJarsList.size() > 9) {
            int _saveNum = 0;
            for (int i = 0; i < m_DevicePackManager.mDeviceDataJarsList.size(); i++) {
                _saveNum++;
                DeviceDataJar _jarData = m_DevicePackManager.mDeviceDataJarsList.get(i);
                _jarData.mPatientInfo.mUserName = _loginUserInfo.mUserName;
                if (_loginUserInfo.mBirthday != null && !_loginUserInfo.mBirthday.equalsIgnoreCase(bs.b)) {
                    _loginUserInfo.mAge = new StringBuilder().append(Calendar.getInstance().get(1) - Integer.parseInt(_loginUserInfo.mBirthday.substring(0, 4))).toString();
                }
                if (_loginUserInfo.mAge != null && !_loginUserInfo.mAge.equals(bs.b)) {
                    _jarData.mPatientInfo.mAge = Integer.parseInt(_loginUserInfo.mAge);
                }
                if (_loginUserInfo.mHeight != null && !_loginUserInfo.mHeight.equals(bs.b) && _loginUserInfo.mHeight.length() > 2) {
                    _jarData.mPatientInfo.mHeight = Integer.parseInt(_loginUserInfo.mHeight.substring(0, _loginUserInfo.mHeight.length() - 3));
                }
                if (_loginUserInfo.mWeight != null && !_loginUserInfo.mWeight.equals(bs.b) && _loginUserInfo.mWeight.length() > 2) {
                    _jarData.mPatientInfo.mWeight = Integer.parseInt(_loginUserInfo.mWeight.substring(0, _loginUserInfo.mWeight.length() - 3));
                }
                if (_loginUserInfo.mSex != null && !_loginUserInfo.mSex.equals(bs.b)) {
                    _jarData.mPatientInfo.mGender = Integer.parseInt(_loginUserInfo.mSex) == 0 ? 1 : 0;
                }
                _jarData.mPatientInfo.mStandard = 0;
                mDeviceData.mSaveDate = new Date(_jarData.mPatientInfo.mYear - 1900, _jarData.mPatientInfo.mMonth - 1, _jarData.mPatientInfo.mDay, _jarData.mPatientInfo.mHour, _jarData.mPatientInfo.mMin, _jarData.mPatientInfo.mSecond);
                mDeviceData.mDataList.add(_jarData);
                if (_saveNum == 9) {
                    CLog.d(TAG, "满2条了，开始存储*************");
                    addData((DeviceData) null);
                    mDeviceData.mDataList.clear();
                    _saveNum = 0;
                } else if (i == m_DevicePackManager.mDeviceDataJarsList.size() - 1) {
                    addData((DeviceData) null);
                    CLog.d(TAG, "结束了 ，存储所有的数据 *************");
                }
            }
            return;
        }
        for (int i2 = 0; i2 < m_DevicePackManager.mDeviceDataJarsList.size(); i2++) {
            DeviceDataJar _jarData2 = m_DevicePackManager.mDeviceDataJarsList.get(i2);
            _jarData2.mPatientInfo.mUserName = _loginUserInfo.mUserName;
            if (_loginUserInfo.mBirthday != null && !_loginUserInfo.mBirthday.equalsIgnoreCase(bs.b)) {
                _jarData2.mPatientInfo.mAge = Calendar.getInstance().get(1) - Integer.parseInt(_loginUserInfo.mBirthday.substring(0, 4));
            }
            if (_loginUserInfo.mHeight != null && !_loginUserInfo.mHeight.equals(bs.b) && _loginUserInfo.mHeight.length() > 2) {
                _jarData2.mPatientInfo.mHeight = Integer.parseInt(_loginUserInfo.mHeight.substring(0, _loginUserInfo.mHeight.length() - 3));
            }
            if (_loginUserInfo.mWeight != null && !_loginUserInfo.mWeight.equals(bs.b) && _loginUserInfo.mWeight.length() > 2) {
                _jarData2.mPatientInfo.mWeight = Integer.parseInt(_loginUserInfo.mWeight.substring(0, _loginUserInfo.mWeight.length() - 3));
            }
            if (_loginUserInfo.mSex != null && !_loginUserInfo.mSex.equals(bs.b)) {
                _jarData2.mPatientInfo.mGender = Integer.parseInt(_loginUserInfo.mSex) == 0 ? 1 : 0;
            }
            _jarData2.mPatientInfo.mStandard = 0;
            mDeviceData.mSaveDate = new Date(_jarData2.mPatientInfo.mYear - 1900, _jarData2.mPatientInfo.mMonth - 1, _jarData2.mPatientInfo.mDay, _jarData2.mPatientInfo.mHour, _jarData2.mPatientInfo.mMin);
            mDeviceData.mDataList.add(_jarData2);
        }
        addData((DeviceData) null);
    }

    public void saveDeviceData_new(DevicePackManager m_DevicePackManager) {
        int size = m_DevicePackManager.mDeviceDataJarsList.size();
        if (size > 0) {
            LoginUserDao _loginUserInfo = PageUtil.getLoginUserInfo();
            for (int i = 0; i < size; i++) {
                DeviceDataJar _jarData = m_DevicePackManager.mDeviceDataJarsList.get(i);
                _jarData.mPatientInfo.mUserName = _loginUserInfo.mUserName;
                init_DeviceDataJar(_loginUserInfo, _jarData);
                _jarData.mPatientInfo.mStandard = 0;
                LogI("年：" + _jarData.mPatientInfo.mYear + " 月：" + (_jarData.mPatientInfo.mMonth - 1) + " 日:" + _jarData.mPatientInfo.mDay);
                LogI("时：" + _jarData.mPatientInfo.mHour + " 分：" + (_jarData.mPatientInfo.mMin - 1) + " 秒:" + _jarData.mPatientInfo.mSecond);
                int mYear = _jarData.mPatientInfo.mYear;
                if (mYear < 99 && mYear > 0) {
                    _jarData.mPatientInfo.mYear = mYear + 2000;
                }
                mDeviceData.mSaveDate = new Date(_jarData.mPatientInfo.mYear - 1900, _jarData.mPatientInfo.mMonth - 1, _jarData.mPatientInfo.mDay, _jarData.mPatientInfo.mHour, _jarData.mPatientInfo.mMin, _jarData.mPatientInfo.mSecond);
                mDeviceData.mDataList.add(_jarData);
                if ((i + 1) % 15 == 0 || (i == size - 1 && (i + 1) % 15 != 0)) {
                    addData(mDeviceData);
                    mDeviceData.mDataList.clear();
                }
            }
        }
    }

    public void saveDeviceData_new(PackManagerSpirometer m_DevicePackManager) {
        int size = m_DevicePackManager.mDeviceDataJarsList.size();
        if (size > 0) {
            LoginUserDao _loginUserInfo = PageUtil.getLoginUserInfo();
            for (int i = 0; i < size; i++) {
                com.contec.sp10.code.DeviceDataJar _jarData = m_DevicePackManager.mDeviceDataJarsList.get(i);
                Log.e("jxxkk", "caseName" + i + ":" + _jarData.mPatientInfo.mCaseName);
                _jarData.mPatientInfo.mUserName = _loginUserInfo.mUserName;
                init_DeviceDataJarBle(_loginUserInfo, _jarData);
                _jarData.mPatientInfo.mStandard = 0;
                LogI("年：" + _jarData.mPatientInfo.mYear + " 月：" + _jarData.mPatientInfo.mMonth + " 日:" + _jarData.mPatientInfo.mDay);
                LogI("时：" + _jarData.mPatientInfo.mHour + " 分：" + _jarData.mPatientInfo.mMin + " 秒:" + _jarData.mPatientInfo.mSecond);
                int mYear = _jarData.mPatientInfo.mYear;
                if (mYear < 99 && mYear > 0) {
                    _jarData.mPatientInfo.mYear = mYear + 2000;
                }
                mDeviceData.mSaveDate = new Date(_jarData.mPatientInfo.mYear - 1900, _jarData.mPatientInfo.mMonth - 1, _jarData.mPatientInfo.mDay, _jarData.mPatientInfo.mHour, _jarData.mPatientInfo.mMin, _jarData.mPatientInfo.mSecond);
                mDeviceData.mDataList.add(_jarData);
                if ((i + 1) % 15 == 0 || (i == size - 1 && (i + 1) % 15 != 0)) {
                    addData(mDeviceData);
                    mDeviceData.mDataList.clear();
                }
            }
        }
    }

    private void init_DeviceDataJarBle(LoginUserDao _loginUserInfo, com.contec.sp10.code.DeviceDataJar _jarData) {
        int i = 1;
        if (_loginUserInfo.mBirthday != null && !_loginUserInfo.mBirthday.equalsIgnoreCase(bs.b)) {
            _loginUserInfo.mAge = new StringBuilder().append(Calendar.getInstance().get(1) - Integer.parseInt(_loginUserInfo.mBirthday.substring(0, 4))).toString();
        }
        if (_loginUserInfo.mAge != null && !_loginUserInfo.mAge.equals(bs.b)) {
            _jarData.mPatientInfo.mAge = Integer.parseInt(_loginUserInfo.mAge);
        }
        if (_loginUserInfo.mHeight != null && !_loginUserInfo.mHeight.equals(bs.b) && _loginUserInfo.mHeight.length() > 2) {
            _jarData.mPatientInfo.mHeight = Integer.parseInt(_loginUserInfo.mHeight.substring(0, _loginUserInfo.mHeight.length() - 3));
        }
        if (_loginUserInfo.mWeight != null && !_loginUserInfo.mWeight.equals(bs.b) && _loginUserInfo.mWeight.length() > 2) {
            _jarData.mPatientInfo.mWeight = Integer.parseInt(_loginUserInfo.mWeight.substring(0, _loginUserInfo.mWeight.length() - 3));
        }
        if (_loginUserInfo.mSex != null && !_loginUserInfo.mSex.equals(bs.b)) {
            com.contec.sp10.code.Sp10PatientInfo sp10PatientInfo = _jarData.mPatientInfo;
            if (Integer.parseInt(_loginUserInfo.mSex) != 0) {
                i = 0;
            }
            sp10PatientInfo.mGender = i;
        }
    }

    private void init_DeviceDataJar(LoginUserDao _loginUserInfo, DeviceDataJar _jarData) {
        int i = 1;
        if (_loginUserInfo.mBirthday != null && !_loginUserInfo.mBirthday.equalsIgnoreCase(bs.b)) {
            _loginUserInfo.mAge = new StringBuilder().append(Calendar.getInstance().get(1) - Integer.parseInt(_loginUserInfo.mBirthday.substring(0, 4))).toString();
        }
        if (_loginUserInfo.mAge != null && !_loginUserInfo.mAge.equals(bs.b)) {
            _jarData.mPatientInfo.mAge = Integer.parseInt(_loginUserInfo.mAge);
        }
        if (_loginUserInfo.mHeight != null && !_loginUserInfo.mHeight.equals(bs.b) && _loginUserInfo.mHeight.length() > 2) {
            _jarData.mPatientInfo.mHeight = Integer.parseInt(_loginUserInfo.mHeight.substring(0, _loginUserInfo.mHeight.length() - 3));
        }
        if (_loginUserInfo.mWeight != null && !_loginUserInfo.mWeight.equals(bs.b) && _loginUserInfo.mWeight.length() > 2) {
            _jarData.mPatientInfo.mWeight = Integer.parseInt(_loginUserInfo.mWeight.substring(0, _loginUserInfo.mWeight.length() - 3));
        }
        if (_loginUserInfo.mSex != null && !_loginUserInfo.mSex.equals(bs.b)) {
            Sp10PatientInfo sp10PatientInfo = _jarData.mPatientInfo;
            if (Integer.parseInt(_loginUserInfo.mSex) != 0) {
                i = 0;
            }
            sp10PatientInfo.mGender = i;
        }
    }

    public void addData(DeviceData deviceData) {
        DeviceData _deviceData = (DeviceData) deviceData;
        _deviceData.setmUploadType("case");
        if (DatasContainer.mDeviceDatas == null) {
            DatasContainer.mDeviceDatas = new DataList();
        }
        DatasContainer.mDeviceDatas.add(_deviceData);
        DatasContainer.mDeviceDatas.addCase(_deviceData);
        _deviceData.setmUploadType("trend");
        update_db(_deviceData);
        DatasContainer.mDeviceDatas.add(_deviceData);
    }

    private void update_db(DeviceData _deviceData) {
        DeviceListDaoOperation.getInstance().updateReceiveDataStr(DeviceManager.m_DeviceBean.mMacAddr, String.valueOf(new SimpleDateFormat("yyyy").format(new Date()).toString()) + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(_deviceData.mSaveDate).substring(4), bs.b);
    }

    public void processPack(byte[] pack, int count) {
    }

    public void initCmdPosition() {
    }

    public void LogI(String msg) {
        if (Constants.mTestFlag) {
            Log.i(TAG, msg);
        }
    }
}
