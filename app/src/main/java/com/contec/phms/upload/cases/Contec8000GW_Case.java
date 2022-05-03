package com.contec.phms.upload.cases;

import com.contec.jni.HVCallBack;
import com.contec.jni.HVnative;
import com.contec.phms.device.template.DeviceData;
import com.contec.phms.manager.device.DeviceBeanList;
import com.contec.phms.manager.device.DeviceManager;
import com.contec.phms.manager.device.ServiceBean;
import com.contec.phms.upload.cases.common.NEW_CASE;
import com.contec.phms.util.CLog;
import com.contec.phms.util.Constants;
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
import u.aly.bs;

public class Contec8000GW_Case {
    final String TAG = "Contec8000GW_Case";
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

    public Contec8000GW_Case(DeviceData data) {
        this.mDeviceData = data;
        this.mTempDir = ServiceBean.getInstance().mTempPath;
        this.mCasePath = String.valueOf(this.mTempDir) + "/temp.dat";
        makePath(this.mTempDir);
    }

    public NEW_CASE process() {
        makeCaseFile();
        return makeCase();
    }

    public void makeCaseFile() {
        this.mDtPath = String.valueOf(this.mTempDir) + CookieSpec.PATH_DELIM + this.mDeviceData.mFileName;
        ex_lzma(String.valueOf(this.mDtPath) + ".lzma", this.mDeviceData.mFilePath, (HVCallBack) null);
        ex_DAT();
        if (Constants.Detail_Progress) {
            DeviceBeanList deviceBeanList = DeviceManager.mDeviceBeanList;
        }
    }

    public NEW_CASE makeCase() {
        String time = this.mDeviceData.mFileName;
        CLog.dT("Contec8000GW_Case", "Case start time: " + time);
        if (this.mDeviceData.mFileName == null || this.mDeviceData.mFileName.length() <= 14) {
            this.mSendDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        } else {
            this.mSendDate = String.valueOf(time.substring(0, 4)) + "-" + time.substring(4, 6) + "-" + time.substring(6, 8) + " " + time.substring(8, 10) + ":" + time.substring(10, 12) + ":" + time.substring(12, 14);
        }
        this.mName = "Contec";
        this.mPatientID = "1";
        this.mPhotoPath = bs.b;
        ex_lzma(String.valueOf(this.mCasePath) + ".dat", this.mCasePath, (HVCallBack) null);
        NEW_CASE _case = new NEW_CASE(this.mSendDate, this.mName, this.mPatientID, PageUtil.addUUID(String.valueOf(this.mCasePath) + ".dat"), this.mBasePath, this.mPhotoPath, 0);
        _case.setCaseName(Constants.Contec8000GW_Name);
        _case.setCaseType(4);
        return _case;
    }

    public boolean ex_lzma(String strDes, String strSrc, HVCallBack lzmaCallback) {
        boolean ret = HVnative.lzmaEn(strDes, strSrc, lzmaCallback);
        CLog.eT("lzma is ok", "lzma is ok?::" + ret + "  strDes: " + strDes + "  strSrc:" + strSrc);
        return ret;
    }

    private void ex_DAT() {
        byte[] _b = new byte[2048];
        byte[] _temp = new byte[4];
        IOException e;
        try {
            OutputStream _out = new FileOutputStream(new File(this.mCasePath));
            _out.write(1);
            _temp[0] = 1;
            _temp[1] = 0;
            _temp[2] = 0;
            _temp[3] = 0;
            _out.write(_temp);
            File file = new File(String.valueOf(String.valueOf(this.mTempDir) + CookieSpec.PATH_DELIM + this.mDeviceData.mFileName) + ".lzma");
            try {
                _temp[0] = (byte) ((int) (file.length() & 255));
                _temp[1] = (byte) ((int) ((file.length() >> 8) & 255));
                _temp[2] = (byte) ((int) ((file.length() >> 16) & 255));
                _temp[3] = (byte) ((int) ((file.length() >> 24) & 255));
                _out.write(_temp);
                InputStream _in = new FileInputStream(String.valueOf(String.valueOf(this.mTempDir) + CookieSpec.PATH_DELIM + this.mDeviceData.mFileName) + ".lzma");
                while (true) {
                    int _n = _in.read(_b);
                    if (_n == -1) {
                        _in.close();
                        _out.close();
                        return;
                    }
                    _out.write(_b, 0, _n);
                }
            } catch (FileNotFoundException ex) {
                e = ex;
                File file2 = file;
                e.printStackTrace();
            } catch (IOException e2) {
                e = e2;
                File file3 = file;
                e.printStackTrace();
            }
        } catch (FileNotFoundException e3) {
            e = e3;
            e.printStackTrace();
        } catch (IOException e4) {
            e = e4;
            e.printStackTrace();
        }
    }

    public void makePath(String path) {
        File _file = new File(path);
        if (!_file.exists()) {
            _file.mkdirs();
        }
    }
}
