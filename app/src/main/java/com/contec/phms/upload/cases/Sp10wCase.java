package com.contec.phms.upload.cases;

import com.contec.jni.HVCallBack;
import com.contec.jni.HVnative;
import com.contec.phms.App_phms;
import com.contec.phms.device.sp10w.DeviceData;
import com.contec.phms.manager.device.DeviceManager;
import com.contec.phms.manager.device.ServiceBean;
import com.contec.phms.upload.cases.common.NEW_CASE;
import com.contec.phms.upload.cases.spir.PatientINI;
import com.contec.phms.upload.cases.spir.SaveHelper;
import com.contec.phms.util.CLog;
import com.contec.phms.util.Constants;
import com.contec.phms.util.FileOperation;
import com.contec.phms.util.PageUtil;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.commons.httpclient.cookie.CookieSpec;
import cn.com.contec.jar.sp10w.DeviceDataJar;
import u.aly.bs;

public class Sp10wCase {
    final String TAG = "Sp10w_SaveCase";
    String mBasePath;
    String mCasePath;
    DeviceData mDeviceData;
    String mDtPath;
    String mFileName;
    String mName;
    String mPatientID;
    String mPhotoPath;
    String mSendDate;
    String mTempDir;
    DeviceDataJar mUserDatas;
    com.contec.sp10.code.DeviceDataJar mUserDatas2;

    public Sp10wCase(com.contec.phms.device.template.DeviceData data) {
        this.mDeviceData = (DeviceData) data;
        this.mTempDir = ServiceBean.getInstance().mTempPath;
        this.mDtPath = String.valueOf(this.mTempDir) + CookieSpec.PATH_DELIM + this.mFileName + ".dt";
        this.mCasePath = String.valueOf(this.mTempDir) + "/temp.dat";
        makePath(this.mTempDir);
    }

    public NEW_CASE process() {
        makeCaseFile();
        return makeCase();
    }

    public void makeCaseFile() {
        new PatientINI().createINI(String.valueOf(this.mTempDir) + "/__temp.ini");
        int _progress = 0;
        for (int i = 0; i < this.mDeviceData.mDataList.size(); i++) {
            _progress += 35 / this.mDeviceData.mDataList.size();
            if (DeviceManager.m_DeviceBean.getmBluetoothType().equalsIgnoreCase(Constants.DEVICE_BLUEBOOTH_TYPE_CLASSIC)) {
                this.mUserDatas = (DeviceDataJar) this.mDeviceData.mDataList.get(i);
                makeDtFile(this.mUserDatas);
            } else {
                this.mUserDatas2 = (com.contec.sp10.code.DeviceDataJar) this.mDeviceData.mDataList.get(i);
                makeDtFile(this.mUserDatas2);
            }
            ex_lzma(String.valueOf(this.mDtPath) + ".lzma", this.mDtPath, (HVCallBack) null);
            if (Constants.Detail_Progress && DeviceManager.mDeviceBeanList != null) {
                DeviceManager.m_DeviceBean.mProgress = _progress + 10;
                App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(DeviceManager.m_DeviceBean);
            }
        }
        ex_lzma(String.valueOf(this.mTempDir) + "/__temp.ini.lzma", String.valueOf(this.mTempDir) + "/__temp.ini", (HVCallBack) null);
        ex_DAT();
        if (Constants.Detail_Progress && DeviceManager.mDeviceBeanList != null) {
            DeviceManager.m_DeviceBean.mProgress = 50;
            App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(DeviceManager.m_DeviceBean);
        }
    }

    public void makeDtFile(DeviceDataJar userDatas) {
        this.mFileName = new StringBuilder(String.valueOf(userDatas.mPatientInfo.mCaseID)).toString();
        this.mDtPath = String.valueOf(this.mTempDir) + CookieSpec.PATH_DELIM + this.mFileName + ".dt";
        FileOperation.createFile(this.mDtPath);
        try {
            FileOutputStream _os = new FileOutputStream(new File(this.mDtPath));
            userDatas.mPatientInfo.save(_os);
            userDatas.mParamInfo.save(_os);
            SaveHelper.saveInt(_os, 0);
            for (int i = 0; i < userDatas.mDataList.size(); i++) {
                userDatas.mDataList.get(i).save(_os);
            }
            _os.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void makeDtFile(com.contec.sp10.code.DeviceDataJar userDatas) {
        this.mFileName = new StringBuilder(String.valueOf(userDatas.mPatientInfo.mCaseID)).toString();
        this.mDtPath = String.valueOf(this.mTempDir) + CookieSpec.PATH_DELIM + this.mFileName + ".dt";
        FileOperation.createFile(this.mDtPath);
        try {
            FileOutputStream _os = new FileOutputStream(new File(this.mDtPath));
            userDatas.mPatientInfo.save(_os);
            userDatas.mParamInfo.save(_os);
            SaveHelper.saveInt(_os, 0);
            for (int i = 0; i < userDatas.mDataList.size(); i++) {
                userDatas.mDataList.get(i).save(_os);
            }
            _os.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public NEW_CASE makeCase() {
        this.mSendDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        this.mName = "Contec";
        this.mPatientID = "1";
        this.mPhotoPath = bs.b;
        if (Constants.Detail_Progress && DeviceManager.mDeviceBeanList != null) {
            DeviceManager.m_DeviceBean.mProgress = 65;
            App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(DeviceManager.m_DeviceBean);
        }
        ex_lzma(String.valueOf(this.mCasePath) + ".dat", this.mCasePath, (HVCallBack) null);
        NEW_CASE _case = new NEW_CASE(this.mSendDate, this.mName, this.mPatientID, PageUtil.addUUID(String.valueOf(this.mCasePath) + ".dat"), this.mBasePath, this.mPhotoPath, 0);
        _case.setCaseName(Constants.SP10W_NAME);
        _case.setCaseType(5);
        return _case;
    }

    public boolean ex_lzma(String strDes, String strSrc, HVCallBack lzmaCallback) {
        boolean ret = HVnative.lzmaEn(strDes, strSrc, lzmaCallback);
        CLog.i("lzma is ok", "lzma is ok?::" + ret);
        return ret;
    }

    private void ex_DAT() {
        String _dtPath;
        String _dtPath2;
        File _caseFile = new File(this.mCasePath);
        File _tempINI = new File(String.valueOf(this.mTempDir) + "/__temp.ini");
        byte[] _b = new byte[2048];
        byte[] _temp = new byte[4];
        try {
            new File(String.valueOf(_tempINI.getPath()) + ".lzma");
            OutputStream _out = new FileOutputStream(_caseFile);
            _out.write((this.mDeviceData.mDataList.size() + 1) & 255);
            for (int i = 0; i < this.mDeviceData.mDataList.size(); i++) {
                _temp[0] = 1;
                _temp[1] = 0;
                _temp[2] = 0;
                _temp[3] = 0;
                _out.write(_temp);
                if (DeviceManager.m_DeviceBean.getmBluetoothType().equalsIgnoreCase(Constants.DEVICE_BLUEBOOTH_TYPE_CLASSIC)) {
                    _dtPath2 = String.valueOf(this.mTempDir) + CookieSpec.PATH_DELIM + ((DeviceDataJar) this.mDeviceData.mDataList.get(i)).mPatientInfo.mCaseName;
                } else {
                    _dtPath2 = String.valueOf(this.mTempDir) + CookieSpec.PATH_DELIM + ((com.contec.sp10.code.DeviceDataJar) this.mDeviceData.mDataList.get(i)).mPatientInfo.mCaseName;
                }
                File file = new File(String.valueOf(_dtPath2) + ".lzma");
                _temp[0] = (byte) ((int) (file.length() & 255));
                _temp[1] = (byte) ((int) ((file.length() >> 8) & 255));
                _temp[2] = (byte) ((int) ((file.length() >> 16) & 255));
                _temp[3] = (byte) ((int) ((file.length() >> 24) & 255));
                _out.write(_temp);
            }
            _temp[0] = 0;
            _temp[1] = 0;
            _temp[2] = 0;
            _temp[3] = 0;
            _out.write(_temp);
            File file2 = new File(this.mTempDir, "__temp.ini.lzma");
            _temp[0] = (byte) ((int) (file2.length() & 255));
            _temp[1] = (byte) ((int) ((file2.length() >> 8) & 255));
            _temp[2] = (byte) ((int) ((file2.length() >> 16) & 255));
            _temp[3] = (byte) ((int) ((file2.length() >> 24) & 255));
            _out.write(_temp);
            for (int j = 0; j < this.mDeviceData.mDataList.size(); j++) {
                if (DeviceManager.m_DeviceBean.getmBluetoothType().equalsIgnoreCase(Constants.DEVICE_BLUEBOOTH_TYPE_CLASSIC)) {
                    _dtPath = String.valueOf(this.mTempDir) + CookieSpec.PATH_DELIM + ((DeviceDataJar) this.mDeviceData.mDataList.get(j)).mPatientInfo.mCaseName;
                } else {
                    _dtPath = String.valueOf(this.mTempDir) + CookieSpec.PATH_DELIM + ((com.contec.sp10.code.DeviceDataJar) this.mDeviceData.mDataList.get(j)).mPatientInfo.mCaseName;
                }
                InputStream _in = new FileInputStream(String.valueOf(_dtPath) + ".lzma");
                while (true) {
                    int _n = _in.read(_b);
                    if (_n == -1) {
                        break;
                    }
                    _out.write(_b, 0, _n);
                }
                _in.close();
            }
            InputStream _in2 = new FileInputStream(new File(String.valueOf(_tempINI.getPath()) + ".lzma"));
            while (true) {
                int _n2 = _in2.read(_b);
                if (_n2 == -1) {
                    _in2.close();
                    _out.close();
                    return;
                }
                _out.write(_b, 0, _n2);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e2) {
            e2.printStackTrace();
        }
    }

    public void makePath(String path) {
        File _file = new File(path);
        if (!_file.exists()) {
            _file.mkdirs();
        }
    }
}
