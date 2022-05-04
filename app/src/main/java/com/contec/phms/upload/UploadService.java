package com.contec.phms.upload;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.os.Message;
import com.contec.phms.App_phms;
import com.contec.phms.R;
import com.contec.phms.SearchDevice.SearchDevice;
import com.contec.phms.SearchDevice.SortDeviceContainerMap;
import com.contec.phms.Server_Main;
import com.contec.phms.device.template.DeviceData;
import com.contec.phms.device.template.DeviceService;
import com.contec.phms.eventbus.EventReLoadReport;
import com.contec.phms.login.LoginActivity;
import com.contec.phms.manager.datas.DataObject;
import com.contec.phms.manager.device.DeviceBean;
import com.contec.phms.manager.device.DeviceManager;
import com.contec.phms.manager.message.MessageManager;
import com.contec.phms.db.HistoryDao;
import com.contec.phms.db.localdata.Spo2DataDao;
import com.contec.phms.db.localdata.opration.BloodDDataDaoOperation;
import com.contec.phms.db.localdata.opration.CmssxtDataDaoOperation;
import com.contec.phms.db.localdata.opration.FeltalHeartDataDaoOperation;
import com.contec.phms.db.localdata.opration.FvcDataDaoOperation;
import com.contec.phms.db.localdata.opration.PedometerDayDataDaoOperation;
import com.contec.phms.db.localdata.opration.PedometerMinDataDaoOperation;
import com.contec.phms.db.localdata.opration.PluseDataDaoOperation;
import com.contec.phms.db.localdata.opration.Spo2DataDaoOperation;
import com.contec.phms.db.localdata.opration.TempertureDataDaoOperation;
import com.contec.phms.db.localdata.opration.UrineDataDaoOperation;
import com.contec.phms.db.localdata.opration.WeightDataDaoOperation;
import com.contec.phms.util.CLog;
import com.contec.phms.util.Constants;
import com.contec.phms.util.DeviceNameUtils;
import com.contec.phms.util.PageUtil;
import com.contec.phms.util.TimerUtil;
import com.j256.ormlite.dao.Dao;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.commons.httpclient.cookie.CookieSpec;
import u.aly.bs;

public class UploadService extends Service {
    public static final String TAG = "UploadService";
    public static int mFaildRe = 0;
    public static boolean mIsUploading = false;
    public IntentFilter mFilter;
    LoginActivity mLoginActivity;
    private String misalive;

    public IBinder onBind(Intent arg0) {
        return null;
    }

    public static void stopServer(Context pContext) {
        CLog.e(TAG, "stopServer*********UploadService");
        pContext.stopService(new Intent(pContext, UploadService.class));
    }

    public void onCreate() {
        super.onCreate();
        CLog.e(TAG, "Starting UploadService*****  onCreate");
        App_phms.getInstance().mEventBus.register(this);
    }

    public void onDestroy() {
        super.onDestroy();
        CLog.d(TAG, "onDestroy  UploadService");
        App_phms.getInstance().mEventBus.unregister(this);
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        CLog.e(TAG, "Starting UploadService*****  onStartCommand");
        this.mLoginActivity = LoginActivity.getInterface();
        return super.onStartCommand(intent, flags, startId);
    }

    public static String[] getFileList(String path, String pFileMAC) {
        List<String> _listStr = new ArrayList<>();
        File[] files = new File(path).listFiles();
        if (files == null) {
            return new String[0];
        }
        for (int i = 0; i < files.length; i++) {
            if (files[i].getName().contains(pFileMAC)) {
                _listStr.add(files[i].getPath());
            }
        }
        String[] _arrayString = new String[_listStr.size()];
        for (int i2 = 0; i2 < _listStr.size(); i2++) {
            _arrayString[i2] = _listStr.get(i2);
            CLog.d(TAG, "file path: " + _arrayString[i2]);
        }
        return _arrayString;
    }

    public static String[] getFileList(String path) {
        File[] files = new File(path).listFiles();
        if (files == null) {
            return new String[0];
        }
        String[] lisStrings = new String[files.length];
        for (int i = 0; i < files.length; i++) {
            lisStrings[i] = files[i].getPath();
        }
        return lisStrings;
    }

    public static ArrayList<DeviceData> getDeviceData(String pFileMAC) {
        String[] _fileList;
        DeviceData _deviceData;
        CLog.d(TAG, "pFileMAC:" + pFileMAC);
        ArrayList<DeviceData> mDeviceDatas = new ArrayList<>();
        if (pFileMAC.length() > 12) {
            if (pFileMAC.contains(Constants.DEVICE_8000GW_NAME)) {
                _fileList = getFileList(Constants.UploadedDatas_8000GW, pFileMAC);
            } else {
                _fileList = getFileList(Constants.DataPath, pFileMAC);
            }
            ArrayList<String> _waitUpload = new ArrayList<>();
            int _size_file = _fileList.length;
            for (int j = 0; j < _size_file; j++) {
                if (_fileList[j].contains(pFileMAC)) {
                    _waitUpload.add(_fileList[j]);
                }
            }
            String[] _fileListFailed = getFileList(Constants.UPLOAD_FIAIL_DADT, pFileMAC);
            int _size_fileFailed = _fileListFailed.length;
            for (int j2 = 0; j2 < _size_fileFailed; j2++) {
                if (_fileListFailed[j2].contains(pFileMAC)) {
                    _waitUpload.add(_fileListFailed[j2]);
                }
            }
            if (_waitUpload != null) {
                int _size = _waitUpload.size();
                for (int i = 0; i < _size; i++) {
                    File _File = new File(_waitUpload.get(i));
                    if (_File.exists()) {
                        if (_waitUpload.get(i).contains(Constants.DEVICE_8000GW_NAME)) {
                            _deviceData = new com.contec.phms.device.contec8000gw.DeviceData();
                            _deviceData.initInfo();
                            _deviceData.mFilePath = _waitUpload.get(i);
                            _deviceData.mFileName = _File.getName();
                            _deviceData.setUniquenes(DeviceManager.m_DeviceBean.getDeivceUniqueness());
                        } else {
                            _deviceData = (DeviceData) DataObject.readObject(_File);
                        }
                        if (_deviceData != null) {
                            mDeviceDatas.add(_deviceData);
                        }
                    }
                }
            }
        }
        return mDeviceDatas;
    }

    public void startUpload() {
        Constants.NOVIP_NOTICE = true;
        Constants.SP10W_UPLOAD_PROCESS = 0;
        List<DeviceBean> _BeanList = DeviceManager.mDeviceBeanList.mBeanList;
        for (int m = 0; m < _BeanList.size(); m++) {
            DeviceManager.m_DeviceBean = _BeanList.get(m);
            ArrayList<DeviceData> _dataDatasAll = getDeviceData(DeviceManager.m_DeviceBean.getDeivceUniqueness());
            if (_dataDatasAll.size() > 0) {
                if (_dataDatasAll.get(0).mDataType.contains("contec08")) {
                    int _size = _dataDatasAll.size();
                    ArrayList<DeviceData> _dataContec08 = new ArrayList<>();
                    ArrayList<DeviceData> _dataContec08SpO2 = new ArrayList<>();
                    for (int j = 0; j < _size; j++) {
                        if (_dataDatasAll.get(j).mDataType.equalsIgnoreCase("contec08aw")) {
                            _dataContec08.add(_dataDatasAll.get(j));
                        } else {
                            _dataContec08SpO2.add(_dataDatasAll.get(j));
                        }
                    }
                    if (_dataContec08.size() > 0) {
                        upload(_dataContec08);
                    }
                    if (_dataContec08SpO2.size() > 0) {
                        upload(_dataContec08SpO2);
                    }
                } else if (_dataDatasAll.get(0).mDataType.contains("abpm50")) {
                    int _size2 = _dataDatasAll.size();
                    ArrayList<DeviceData> _dataABPM50Case = new ArrayList<>();
                    ArrayList<DeviceData> _dataABPM50Trend = new ArrayList<>();
                    for (int j2 = 0; j2 < _size2; j2++) {
                        if (_dataDatasAll.get(j2).mDataType.equalsIgnoreCase("abpm50w")) {
                            _dataABPM50Case.add(_dataDatasAll.get(j2));
                        } else {
                            _dataABPM50Trend.add(_dataDatasAll.get(j2));
                        }
                    }
                    if (_dataABPM50Case.size() > 0) {
                        upload(_dataABPM50Case);
                    }
                    if (_dataABPM50Trend.size() > 0) {
                        upload(_dataABPM50Trend);
                    }
                } else if (_dataDatasAll.size() > 0) {
                    upload(_dataDatasAll);
                }
            }
        }
    }

    public void startUploadDeviceList() {
        Constants.NOVIP_NOTICE = true;
        Constants.SP10W_UPLOAD_PROCESS = 0;
        if (DeviceManager.mDeviceList != null) {
            for (int n = 0; n < DeviceManager.mDeviceList.size(); n++) {
                DeviceManager.mDeviceBeanList = DeviceManager.mDeviceList.getDevice(n);
                List<DeviceBean> _BeanList = DeviceManager.mDeviceBeanList.mBeanList;
                for (int m = 0; m < _BeanList.size(); m++) {
                    DeviceManager.m_DeviceBean = _BeanList.get(m);
                    ArrayList<DeviceData> _dataDatasAll = getDeviceData(DeviceManager.m_DeviceBean.getDeivceUniqueness());
                    if (_dataDatasAll.size() > 0) {
                        if (_dataDatasAll.get(0).mDataType.contains("contec08")) {
                            int _size = _dataDatasAll.size();
                            ArrayList<DeviceData> _dataContec08 = new ArrayList<>();
                            ArrayList<DeviceData> _dataContec08SpO2 = new ArrayList<>();
                            for (int j = 0; j < _size; j++) {
                                if (_dataDatasAll.get(j).mDataType.equalsIgnoreCase("contec08aw")) {
                                    _dataContec08.add(_dataDatasAll.get(j));
                                } else {
                                    _dataContec08SpO2.add(_dataDatasAll.get(j));
                                }
                            }
                            if (_dataContec08.size() > 0) {
                                upload(_dataContec08);
                            }
                            if (_dataContec08SpO2.size() > 0) {
                                upload(_dataContec08SpO2);
                            }
                        } else if (_dataDatasAll.get(0).mDataType.contains("abpm50")) {
                            int _size2 = _dataDatasAll.size();
                            ArrayList<DeviceData> _dataABPM50Case = new ArrayList<>();
                            ArrayList<DeviceData> _dataABPM50Trend = new ArrayList<>();
                            for (int j2 = 0; j2 < _size2; j2++) {
                                if (_dataDatasAll.get(j2).mDataType.equalsIgnoreCase("abpm50w")) {
                                    _dataABPM50Case.add(_dataDatasAll.get(j2));
                                } else {
                                    _dataABPM50Trend.add(_dataDatasAll.get(j2));
                                }
                            }
                            if (_dataABPM50Case.size() > 0) {
                                upload(_dataABPM50Case);
                            }
                            if (_dataABPM50Trend.size() > 0) {
                                upload(_dataABPM50Trend);
                            }
                        } else if (_dataDatasAll.size() > 0) {
                            upload(_dataDatasAll);
                        }
                    }
                }
            }
        }
    }

    public void upload(ArrayList<DeviceData> pDatas) {
        UploadTask _task;
        int _pro;
        String _type = null;
        if (pDatas.size() > 0) {
            _type = pDatas.get(0).mDataType;
        }
        Exception e;
        if (pDatas.size() > 0 && (_type.equalsIgnoreCase("contec08spo2") || _type.equalsIgnoreCase("abpm50wtrend"))) {
            DeviceManager.m_DeviceBean.mProgress = 0;
            DeviceManager.mDeviceBeanList.mState = 7;
            DeviceManager.m_DeviceBean.mState = 7;
            App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(DeviceManager.m_DeviceBean);
            try {
                UploadTask _task2 = new UploadTask(pDatas);
                try {
                    if (_task2.execute()) {
                        App_phms.getInstance().mEventBus.post(new EventReLoadReport());
                        CLog.i(TAG, "Upload  Data Successful");
                        sendMessage_DataUploadSucceedNotificationService();
                        addUploadeds(pDatas, DeviceManager.m_DeviceBean.mDeviceName);
                        if (DeviceManager.mDeviceBeanList != null) {
                            DeviceManager.mDeviceBeanList.mState = 8;
                            DeviceManager.m_DeviceBean.mProgress = 100;
                            DeviceManager.m_DeviceBean.mState = 8;
                            App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(DeviceManager.m_DeviceBean);
                            DeviceManager.m_DeviceBean.clearDataPath();
                        }
                        DeviceManager.m_DeviceBean.mProgress = 100;
                        App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(DeviceManager.m_DeviceBean);
                        DeviceManager.m_DeviceBean.clearDataPath();
                        UploadTask uploadTask = _task2;
                    } else {
                        if (mFaildRe == 1) {
                            DeviceManager.mDeviceBeanList.mState = 16;
                            DeviceManager.m_DeviceBean.mState = 16;
                            DeviceManager.m_DeviceBean.mProgress = 0;
                            DeviceManager.m_DeviceBean.mFailedReasons = 1;
                        } else {
                            DeviceManager.mDeviceBeanList.mState = 9;
                            DeviceManager.m_DeviceBean.mState = 9;
                            DeviceManager.m_DeviceBean.mProgress = 0;
                            DeviceManager.m_DeviceBean.mFailedReasons = 0;
                        }
                        DeviceManager.m_DeviceBean.mProgress = 0;
                        DeviceManager.m_DeviceBean.clearDataPath();
                        DeviceManager.m_DeviceBean.clearDataPath();
                        CLog.i(TAG, "Upload Data Failed");
                        addFaileds(pDatas);
                        App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(DeviceManager.m_DeviceBean);
                        UploadTask uploadTask2 = _task2;
                    }
                } catch (Exception ex) {
                    e = ex;
                    UploadTask uploadTask3 = _task2;
                    CLog.i(TAG, "Error Data Object");
                    e.printStackTrace();
                    TimerUtil.getinstance().startTimer();
                }
            } catch (Exception e2) {
                e = e2;
                CLog.i(TAG, "Error Data Object");
                e.printStackTrace();
                TimerUtil.getinstance().startTimer();
            }
        } else if (pDatas.size() > 0) {
            int successCount = 0;
            int _size = pDatas.size();
            CLog.i(TAG, "上传数据的pathlist.size()" + _size);
            int j = 0;
            UploadTask _task3 = null;
            while (j < _size) {
                if (DeviceManager.mDeviceBeanList != null) {
                    if (_size == 1) {
                        Constants.Detail_Progress = true;
                    } else {
                        Constants.Detail_Progress = false;
                    }
                }
                try {
                    DeviceData _data = pDatas.get(j);
                    if (_data == null) {
                        CLog.i(TAG, "Error Data Object");
                        _task = _task3;
                    } else {
                        CLog.i(TAG, "设备类型" + _data.mDataType);
                        _task = new UploadTask(_data);
                        try {
                            _data.upLoadTimes++;
                            if (_task.execute()) {
                                successCount++;
                                App_phms.getInstance().mEventBus.post(new EventReLoadReport());
                                CLog.i(TAG, "Upload Data Successful");
                                if (DeviceManager.mDeviceBeanList != null && DeviceManager.mDeviceBeanList.mState != 9 && !_data.mDataType.equalsIgnoreCase("sp10w")) {
                                    if (!_data.mDataType.equalsIgnoreCase(DeviceNameUtils.ABPM50W)) {
                                        _pro = ((j + 1) * 100) / _size;
                                    } else {
                                        _pro = (((j + 1) * 50) / _size) + 50;
                                    }
                                    if (_pro > DeviceManager.m_DeviceBean.mProgress) {
                                        DeviceManager.m_DeviceBean.mProgress = _pro;
                                    }
                                    if (_pro == 100) {
                                        DeviceManager.mDeviceBeanList.mState = 8;
                                        DeviceManager.m_DeviceBean.mState = 8;
                                    }
                                } else if (_data.mDataType.equalsIgnoreCase("sp10w") && !_data.mUploadType.equalsIgnoreCase("trend")) {
                                    CLog.e(TAG, "上传肺活量病例成功");
                                    DeviceManager.mDeviceBeanList.mState = 8;
                                    DeviceManager.m_DeviceBean.mState = 8;
                                    Constants.SP10W_UPLOAD_PROCESS += 15;
                                    DeviceManager.m_DeviceBean.mProgress = Constants.SP10W_UPLOAD_PROCESS;
                                } else if (_data.mDataType.equalsIgnoreCase("sp10w") && _data.mUploadType.equalsIgnoreCase("trend")) {
                                    CLog.e(TAG, "上传肺活量趋势成功");
                                    DeviceManager.mDeviceBeanList.mState = 8;
                                    DeviceManager.m_DeviceBean.mState = 8;
                                    Constants.SP10W_UPLOAD_PROCESS += 15;
                                    DeviceManager.m_DeviceBean.mProgress = Constants.SP10W_UPLOAD_PROCESS;
                                }
                                App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(DeviceManager.m_DeviceBean);
                                DeviceManager.m_DeviceBean.clearDataPath();
                                addUploaded(_data, DeviceManager.m_DeviceBean.mDeviceName);
                                if (_data.mFileName.contains(Constants.DEVICE_8000GW_NAME)) {
                                    DataObject.remove(_data.mFilePath);
                                }
                                DataObject.remove(String.valueOf(Constants.DataPath) + CookieSpec.PATH_DELIM + _data.mFileName);
                                DataObject.remove(String.valueOf(Constants.UPLOAD_FIAIL_DADT) + CookieSpec.PATH_DELIM + _data.mFileName);
                            } else {
                                if (DeviceManager.mDeviceBeanList.mState != 16) {
                                    DeviceManager.mDeviceBeanList.mState = 9;
                                    DeviceManager.m_DeviceBean.mState = 9;
                                    DeviceManager.m_DeviceBean.mProgress = 0;
                                    DeviceManager.m_DeviceBean.mFailedReasons = 0;
                                }
                                DeviceManager.m_DeviceBean.mProgress = 0;
                                App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(DeviceManager.m_DeviceBean);
                                DeviceManager.m_DeviceBean.clearDataPath();
                                DeviceManager.m_DeviceBean.clearDataPath();
                                CLog.i(TAG, "Upload Data Failed");
                                addFailed(_data);
                            }
                            if (j == _size - 1 && successCount > 0) {
                                sendMessage_DataUploadSucceedNotificationService();
                            }
                        } catch (Exception e3) {
                            e = e3;
                            CLog.i(TAG, "Error Data Object");
                            e.printStackTrace();
                            j++;
                            _task3 = _task;
                        }
                    }
                } catch (Exception e4) {
                    e = e4;
                    _task = _task3;
                    CLog.i(TAG, "Error Data Object");
                    e.printStackTrace();
                    j++;
                    _task3 = _task;
                }
                j++;
                _task3 = _task;
            }
            UploadTask uploadTask4 = _task3;
        }
        TimerUtil.getinstance().startTimer();
    }

    private void sendMessage_DataUploadSucceedNotificationService() {
        Message msg = new Message();
        msg.what = 15;
        if (App_phms.getInstance().isBackground(App_phms.getInstance().getApplicationContext())) {
            msg.arg2 = 15;
        } else {
            msg.arg2 = 16;
        }
        App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(msg);
    }

    public void addUploadeds(ArrayList<DeviceData> datas, String pDeviceName) {
        int _size = datas.size();
        for (int i = 0; i < _size; i++) {
            addUploaded(datas.get(i), pDeviceName);
            DataObject.remove(String.valueOf(Constants.DataPath) + CookieSpec.PATH_DELIM + datas.get(i).mFileName);
            DataObject.remove(String.valueOf(Constants.UPLOAD_FIAIL_DADT) + CookieSpec.PATH_DELIM + datas.get(i).mFileName);
        }
        CLog.e("jxx", "代码执行到这里。。adduploades");
    }

    public void addFaileds(ArrayList<DeviceData> datas) {
        int _size = datas.size();
        for (int i = 0; i < _size; i++) {
            addFailed(datas.get(i));
        }
    }

    public void addUploaded(DeviceData data, String pDeviceName) {
        changeFlag(data);
        String _HisDate = new SimpleDateFormat("yyyy-MM-dd  HH:mm").format(new Date());
        String _HisContent = bs.b;
        if ("TEMP01".equalsIgnoreCase(pDeviceName)) {
            _HisContent = getResources().getString(R.string.device_productname_hc06);
        } else if (Constants.DEVICE_8000GW_NAME.contains(pDeviceName)) {
            _HisContent = getResources().getString(R.string.str_8000G);
        } else if (pDeviceName.equalsIgnoreCase("CMS50D")) {
            _HisContent = getResources().getString(R.string.upload_base_50D);
        } else if (pDeviceName.equalsIgnoreCase(Constants.CMS50EW)) {
            _HisContent = getResources().getString(R.string.upload_base_50EW);
        } else if (pDeviceName.equalsIgnoreCase("CMS50IW")) {
            _HisContent = getResources().getString(R.string.upload_base_50EW);
        } else if (pDeviceName.equalsIgnoreCase("SpO206")) {
            _HisContent = getResources().getString(R.string.upload_base_50EW);
        } else if (pDeviceName.equalsIgnoreCase("SpO201")) {
            _HisContent = getResources().getString(R.string.upload_base_50EW);
        } else if (pDeviceName.equalsIgnoreCase("CMSVESD")) {
            _HisContent = getResources().getString(R.string.upload_base_50EW);
        } else if (pDeviceName.equalsIgnoreCase("SP10W")) {
            _HisContent = getResources().getString(R.string.upload_base_10W);
        } else if (pDeviceName.equalsIgnoreCase("CMSSXT")) {
            _HisContent = getResources().getString(R.string.upload_base_SXT);
        } else if (pDeviceName.equalsIgnoreCase("ABPM50W")) {
            _HisContent = getResources().getString(R.string.upload_base_50W);
        } else if (pDeviceName.equalsIgnoreCase("CONTEC08AW") || pDeviceName.equalsIgnoreCase("CONTEC08C")) {
            _HisContent = getResources().getString(R.string.upload_base_8AW);
        } else if (pDeviceName.equalsIgnoreCase("WT")) {
            _HisContent = getResources().getString(R.string.upload_base_WT);
        } else if (pDeviceName.equalsIgnoreCase("FHR01")) {
            _HisContent = getResources().getString(R.string.upload_base_FHR01);
        } else if (pDeviceName.equalsIgnoreCase(Constants.PM85_NAME)) {
            _HisContent = getResources().getString(R.string.upload_base_PM85);
        }
        PageUtil.checkUserinfo(App_phms.getInstance().mUserInfo, App_phms.getInstance().getApplicationContext());
        try {
            Dao<HistoryDao, String> mDao = App_phms.getInstance().mHelper.getHistoryDao();
            HistoryDao mHistoryDao = new HistoryDao();
            mHistoryDao.setContent(_HisContent);
            mHistoryDao.setDate(_HisDate);
            mHistoryDao.setUser(App_phms.getInstance().mUserInfo.mUserID);
            mDao.create(mHistoryDao);
        } catch (Exception e) {
            e.printStackTrace();
        }
        saveUploaded(data.getFileName());
    }

    public void addFailed(DeviceData data) {
        if (data.mCheckTimeIllegal) {
            Message msg = new Message();
            msg.arg2 = 1;
            msg.what = Constants.CHECK_TIME_ILLEGAL;
            App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(msg);
            DataObject.remove(String.valueOf(Constants.DataPath) + CookieSpec.PATH_DELIM + data.mFileName);
            return;
        }
        if (!data.mDataType.equalsIgnoreCase("contec8000gw")) {
            DataObject.makeObjectFile_failed(data, Constants.UPLOAD_FIAIL_DADT);
        }
        DataObject.remove(String.valueOf(Constants.DataPath) + CookieSpec.PATH_DELIM + data.mFileName);
        Constants.isSuccessOperationDevice = false;
    }

    public void saveUploaded(String save) {
        IOException e;
        FileWriter _fw = null;
        File _file = null;
        File _file2 = new File(Constants.UploadedDatas);
        try {
            if (_file2.exists()) {
            }
            FileWriter _fw2 = new FileWriter(Constants.UploadedDatas, true);
            try {
                _fw2.write(";" + save);
                _fw2.flush();
                _fw2.close();
            } catch (IOException e2) {
                e = e2;
                _file = _file2;
                _fw = _fw2;
                if (_fw != null) {
                    try {
                        _fw.close();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                        e.printStackTrace();
                    }
                }
                if (_file != null) {
                }
                e.printStackTrace();
            }
        } catch (IOException e3) {
            e = e3;
            _file = _file2;
        }
    }

    public void onEvent(Message msg) {
        String[] _fileList;
        if (msg.arg2 == 5) {
            switch (msg.what) {
                case 51:
                    if (msg.obj != null) {
                        this.misalive = (String) msg.obj;
                    }
                    CLog.e(TAG, "US_START_UPLOAD*********************通知准备上传" + this.misalive + "  msg.obj:" + msg.obj + "  " + Constants.BLUETOOTHSTAT);
                    if (Constants.BLUETOOTHSTAT == 2 || Constants.BLUETOOTHSTAT == 3) {
                        CLog.e("lzerror", "真的很生气，你到底在哪里执行了上传 ，你妈知道你犯错了吗？ ————  蓝牙正在搜索或者连接设备时数据上传");
                        CLog.i("jxx", "代码只想到这里3");
                        Message msgManager = new Message();
                        msgManager.what = 66;
                        msgManager.arg2 = 14;
                        msgManager.obj = true;
                        App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(msgManager);
                        return;
                    }
                    String _fileMAC = DeviceManager.m_DeviceBean.getDeivceUniqueness();
                    CLog.e(TAG, "准备上传 8000G的fileMAC:" + _fileMAC);
                    if (_fileMAC.contains(Constants.DEVICE_8000GW_NAME)) {
                        _fileList = getFileList(Constants.UploadedDatas_8000GW, _fileMAC);
                    } else {
                        _fileList = getFileList(Constants.DataPath, _fileMAC);
                    }
                    String[] _fileListFailed = getFileList(Constants.UPLOAD_FIAIL_DADT, _fileMAC);
                    CLog.i(TAG, "Start Upload" + (_fileList.length + _fileListFailed.length));
                    if (_fileList.length + _fileListFailed.length == 0) {
                        CLog.d(TAG, "上传8000g的文件pFileMAC:" + _fileMAC + "  " + Constants.DEVICE_8000GW_NAME);
                        if (_fileMAC.contains(Constants.DEVICE_8000GW_NAME)) {
                            DeviceManager.m_DeviceBean.mProgress = 0;
                            DeviceManager.mDeviceBeanList.mState = 0;
                            DeviceManager.m_DeviceBean.mState = 0;
                            App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(DeviceManager.m_DeviceBean);
                            CLog.d(TAG, "上传8000g的文件数量为0*****************************");
                        }
                        Message msgus = new Message();
                        msgus.what = 53;
                        msgus.arg2 = 5;
                        App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(msgus);
                        CLog.i(TAG, "上传文件的个数为=====结束上传");
                        return;
                    }
                    Message msgus2 = new Message();
                    msgus2.what = 52;
                    msgus2.arg2 = 5;
                    App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(msgus2);
                    return;
                case 52:
                    CLog.i(TAG, "Uploading Datas********************************");
                    new Thread() {
                        public void run() {
                            if (UploadService.this.misalive == null || !UploadService.this.misalive.equals(Constants.DEVICE_UPLOAD_TIMER)) {
                                UploadService.this.startUpload();
                                CLog.i("jxx2", "代码执行到这里1");
                            } else {
                                CLog.i("jxx", "代码执行到这里1");
                                UploadService.this.startUploadDeviceList();
                            }
                            Message msgManager = new Message();
                            msgManager.what = 53;
                            msgManager.arg2 = 5;
                            App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(msgManager);
                        }
                    }.start();
                    return;
                case 53:
                    CLog.i("jxx2", "结束上传啦啊");
                    CLog.i("lzerror", "end Upload");
                    Constants.BLUETOOTHSTAT = 1;
                    SortDeviceContainerMap.getInstance().getmSortDeviceMap().clear();
                    if (Constants.REPORT) {
                        Constants.ADD_DEVICE = false;
                        Message _msg = new Message();
                        _msg.what = 532;
                        _msg.arg2 = 6;
                        App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(_msg);
                        Constants.REPORT = false;
                        Message msgss = new Message();
                        msgss.what = Constants.V_NOTIFY_SEARCH_TIME_LONG_CANCLE;
                        msgss.arg2 = 1;
                        App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(msgss);
                        CLog.e(TAG, "----------Upload Finished-------------");
                        SearchDevice.stopServer(this);
                        DeviceManager.stopServer(this);
                        stopServer(this);
                        MessageManager.stopServer(this);
                        DeviceService.stopServer(this);
                        Server_Main.stopServer(this);
                        Message msgs = new Message();
                        msgs.what = Constants.V_START_REPORT;
                        msgs.arg2 = 1;
                        App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(msgs);
                        return;
                    } else if (Constants.GO_TO_ADD_DEVICE) {
                        Constants.GO_TO_ADD_DEVICE = false;
                        Message msgss2 = new Message();
                        msgss2.what = Constants.V_NOTIFY_SEARCH_TIME_LONG_CANCLE;
                        msgss2.arg2 = 1;
                        App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(msgss2);
                        CLog.e(TAG, "----------Upload Finished GO TO  ADD DEVICE-------------");
                        SearchDevice.stopServer(this);
                        DeviceManager.stopServer(this);
                        stopServer(this);
                        MessageManager.stopServer(this);
                        DeviceService.stopServer(this);
                        Server_Main.stopServer(this);
                        Message msgs2 = new Message();
                        msgs2.what = Constants.START_ADD_DEVICE;
                        msgs2.arg2 = 1;
                        App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(msgs2);
                        return;
                    } else {
                        if (this.misalive != null && (this.misalive.equals(Constants.DEVICE_UPLOAD_CALL) || this.misalive.equals(Constants.DEVICE_UPLOAD_TIMER))) {
                            CLog.e(TAG, "设备依次执行完成，开启回联*****或者是回联成功 开启下一次回联");
                            Message msgManager2 = new Message();
                            msgManager2.what = 66;
                            msgManager2.arg2 = 14;
                            msgManager2.obj = false;
                            App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(msgManager2);
                        } else if (this.misalive != null && this.misalive.equals(Constants.DEVICE_UPLOAD)) {
                            CLog.e(TAG, "开启下一个设备*************");
                            Message msgManager3 = new Message();
                            msgManager3.what = 21;
                            msgManager3.arg2 = 4;
                            App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(msgManager3);
                        }
                        mIsUploading = false;
                        return;
                    }
                default:
                    return;
            }
        }
    }

    public void changeFlag(DeviceData mData) {
        if (mData.mDataType.equals("ECG(PM10)") || mData.mDataType.equals("ECG(CMS50K)") || mData.mDataType.equals("ECG(CMS50K1)")) {
            PluseDataDaoOperation.getInstance(App_phms.getInstance()).updatePluseData(mData.mFileName);
            CLog.e("changeFlag==============", "Upload ECG(PM10) Trend=========");
        }
        if (mData.mUploadType.equals("trend")) {
            String unique = mData.mFileName;
            if (mData.mDataType.equals("ECG(PM10)") || mData.mDataType.equals("ECG(CMS50K)") || mData.mDataType.equals("ECG(CMS50K1)")) {
                PluseDataDaoOperation.getInstance(App_phms.getInstance()).updatePluseData(unique);
                CLog.e("changeFlag==============-----------------------------", "Upload ECG(PM10) Trend");
            } else if (mData.mDataType.equals("bc01")) {
                CLog.e("GotYou", "Upload bc01 Trend");
                UrineDataDaoOperation.getInstance(App_phms.getInstance().getApplicationContext()).updateUrineData(unique);
            } else if (mData.mDataType.equals("wt")) {
                CLog.e("GotYou", "Upload WT Trend");
                WeightDataDaoOperation.getInstance(App_phms.getInstance().getApplicationContext()).updateWeightData(unique);
            } else if (mData.mDataType.equals("cmssxt")) {
                CLog.e("GotYou", "Upload CMSSXT Trend");
                CmssxtDataDaoOperation.getInstance(App_phms.getInstance().getApplicationContext()).updateCmssxtData(unique);
            } else if (mData.mDataType.equals("contec08aw")) {
                CLog.e("GotYou", "Upload contec08aw =====");
                BloodDDataDaoOperation.getInstance(App_phms.getInstance().getApplicationContext()).updateBloodDDataDao(unique);
            } else if (mData.mDataType.equals(Spo2DataDao.SPO2)) {
                Spo2DataDaoOperation.getInstance(App_phms.getInstance().getApplicationContext()).updateSpo2Data(unique);
                CLog.e("GotYou", "Upload spo2 =====" + unique);
            } else if (mData.mDataType.equalsIgnoreCase("FHR01")) {
                FeltalHeartDataDaoOperation.getInstance(App_phms.getInstance().getApplicationContext()).updateFetalHeartData(unique);
                CLog.e("GotYou", "Upload FHR01 =====");
            } else if (mData.mDataType.equals("contec08spo2")) {
                CLog.e("GotYou", "Upload contec08spo2 ===Cms50ew_08A_Trend==");
            } else if (mData.mDataType.equalsIgnoreCase("sp10w")) {
                FvcDataDaoOperation.getInstance(App_phms.getInstance().getApplicationContext()).updateFvcDataDao(unique);
                CLog.e("GotYou", "Upload sp10w =====");
            } else if (mData.mDataType.equalsIgnoreCase("sp0208")) {
                Spo2DataDaoOperation.getInstance(App_phms.getInstance().getApplicationContext()).updateSpo2Data(unique);
                CLog.e("GotYou", "Upload sp0208 =====");
            } else if (mData.mDataType.equalsIgnoreCase("pedometerDay")) {
                PedometerDayDataDaoOperation.getInstance(App_phms.getInstance().getApplicationContext()).updatePedometerDayData(unique);
                CLog.e("GotYou", "Upload pedometerDay =====");
            } else if (mData.mDataType.equalsIgnoreCase("pedometerMin")) {
                PedometerMinDataDaoOperation.getInstance(App_phms.getInstance().getApplicationContext()).updatePedometerMinData(unique);
                CLog.e("GotYou", "Upload pedometerMin =====");
            } else if (mData.mDataType.equalsIgnoreCase("temperature")) {
                TempertureDataDaoOperation.getInstance(App_phms.getInstance()).updateTempertureData(unique);
                CLog.e("GotYou", "Upload temperature =====");
            } else if (mData.mDataType.equalsIgnoreCase("pedometerDayK")) {
                PedometerDayDataDaoOperation.getInstance(App_phms.getInstance().getApplicationContext()).updatePedometerDayData(unique);
                CLog.e("GotYou", "Upload pedometerDayK =====");
            } else if (mData.mDataType.equalsIgnoreCase("pedometerMinK")) {
                PedometerMinDataDaoOperation.getInstance(App_phms.getInstance().getApplicationContext()).updatePedometerMinData(unique);
                CLog.e("GotYou", "Upload pedometerMinK =====");
            }
        }
    }
}
