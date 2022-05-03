package com.contec.phms.upload.cases;

import android.util.Log;
import com.conect.json.CLog;
import com.contec.jni.HVCallBack;
import com.contec.jni.HVnative;
import com.contec.phms.App_phms;
import com.contec.phms.device.pm85.DeviceData;
import com.contec.phms.manager.device.DeviceManager;
import com.contec.phms.manager.device.ServiceBean;
import com.contec.phms.db.LoginUserDao;
import com.contec.phms.upload.cases.common.NEW_CASE;
import com.contec.phms.upload.cases.pm85.MakeBaseCase;
import com.contec.phms.upload.cases.pm85.PM85_ini;
import com.contec.phms.util.Constants;
import com.contec.phms.util.FileOperation;
import com.contec.phms.util.PageUtil;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import org.apache.commons.httpclient.cookie.CookieSpec;
import u.aly.bs;

public class PM85Case {
    DeviceData _mData;
    String mBasePath;
    String mCasePath;
    String mDtPath;
    String mFileName;
    String mFlagPath;
    LoginUserDao mLoginUserInfo = PageUtil.getLoginUserInfo();
    String mName;
    String mPatientID;
    String mPhotoPath;
    String mSendDate;
    String mTempDir;

    public PM85Case(com.contec.phms.device.template.DeviceData data) {
        this.mFileName = data.dateToString();
        this.mTempDir = ServiceBean.getInstance().mTempPath;
        this.mDtPath = String.valueOf(this.mTempDir) + CookieSpec.PATH_DELIM + this.mFileName + ".dt";
        this.mFlagPath = String.valueOf(this.mTempDir) + CookieSpec.PATH_DELIM + "flag" + ".flag";
        this.mCasePath = String.valueOf(this.mTempDir) + "/temp.dat";
        this._mData = (DeviceData) data;
        makePath(this.mTempDir);
    }

    public NEW_CASE process() {
        makeBaseCase();
        makeDtFile(this._mData);
        makeDatFile();
        App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(DeviceManager.m_DeviceBean);
        return makeCase();
    }

    public void makeBaseCase() {
        MakeBaseCase baseCase = new MakeBaseCase();
        if (this.mLoginUserInfo.mBirthday != null && !this.mLoginUserInfo.mBirthday.equalsIgnoreCase(bs.b)) {
            this.mLoginUserInfo.mAge = new StringBuilder().append(Calendar.getInstance().get(1) - Integer.parseInt(this.mLoginUserInfo.mBirthday.substring(0, 4))).toString();
        }
        if (this.mLoginUserInfo.mAge != null && !this.mLoginUserInfo.mAge.equalsIgnoreCase(bs.b)) {
            baseCase.Age = this.mLoginUserInfo.mAge;
        }
        if (this.mLoginUserInfo.mHeight != null && !this.mLoginUserInfo.mHeight.equalsIgnoreCase(bs.b)) {
            baseCase.height = this.mLoginUserInfo.mHeight;
        }
        if (this.mLoginUserInfo.mWeight == null || !this.mLoginUserInfo.mWeight.equalsIgnoreCase(bs.b)) {
        }
        baseCase.weight = this.mLoginUserInfo.mWeight;
        if (this.mLoginUserInfo.mSex == null || !this.mLoginUserInfo.mSex.equalsIgnoreCase(bs.b)) {
        }
        baseCase.sex = new StringBuilder().append(Long.parseLong(this.mLoginUserInfo.mSex) + 1).toString();
        if (this.mLoginUserInfo.mUserName != null) {
            baseCase.username = this.mLoginUserInfo.mUserName;
        }
        if (this.mLoginUserInfo.mPhone != null) {
            baseCase.Tel = this.mLoginUserInfo.mPhone;
        }
        baseCase.EventTime = makeDate();
        baseCase.datatype = 11;
        this.mBasePath = String.valueOf(this.mTempDir) + CookieSpec.PATH_DELIM + this.mFileName + ".bas";
        baseCase.ToDo(this.mBasePath);
    }

    public String makeDate() {
        String _date = bs.b;
        String data = String.valueOf(this._mData.mDate[0]) + "-" + this._mData.mDate[1] + "-" + this._mData.mDate[2] + " " + this._mData.mDate[3] + ":" + this._mData.mDate[4] + ":" + this._mData.mDate[5];
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            _date = format.format(format.parse(data));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        CLog.e("PM85Case", _date);
        return _date;
    }

    public NEW_CASE makeCase() {
        this.mSendDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        this.mName = "Contec";
        this.mPatientID = "1";
        this.mPhotoPath = bs.b;
        lzmaFile(this.mCasePath, String.valueOf(this.mCasePath) + ".dat");
        NEW_CASE _case = new NEW_CASE(this.mSendDate, "lisan", "1", PageUtil.addUUID(String.valueOf(this.mCasePath) + ".dat"), this.mBasePath, this.mPhotoPath, 0);
        _case.setCaseType(11);
        _case.setCaseName(Constants.PM85_NAME);
        return _case;
    }

    public void makeDtFile(com.contec.phms.device.template.DeviceData pData) {
        this.mDtPath = String.valueOf(this.mTempDir) + CookieSpec.PATH_DELIM + this.mFileName + ".dt";
        FileOperation.createFile(this.mDtPath);
        try {
            DataOutputStream outputStream = new DataOutputStream(new FileOutputStream(new File(this.mDtPath)));
            ArrayList<Object> mDataList = pData.mDataList;
            int _size = mDataList.size();
            for (int i = 0; i < _size; i++) {
                short[] _data = (short[]) mDataList.get(i);
                for (int j = 0; j < _data.length; j++) {
                    outputStream.writeByte((_data[j] >>> 0) & 255);
                    outputStream.writeByte((_data[j] >>> 8) & 255);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        FileOperation.createFile(this.mFlagPath);
        try {
            DataOutputStream _flagDataOutputStream = new DataOutputStream(new FileOutputStream(new File(this.mFlagPath)));
            ArrayList<Integer> _flagData = this._mData.mFlag;
            int _flagsize = _flagData.size();
            byte[] _temp = new byte[4];
            for (int i2 = 0; i2 < _flagsize; i2++) {
                _temp[0] = (byte) ((_flagData.get(i2).intValue() >> 0) & 255);
                _temp[1] = (byte) ((_flagData.get(i2).intValue() >> 8) & 255);
                _temp[2] = (byte) ((_flagData.get(i2).intValue() >> 16) & 255);
                _temp[3] = (byte) ((_flagData.get(i2).intValue() >> 24) & 255);
                _flagDataOutputStream.write(_temp);
            }
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    public void makeDatFile() {
        if (this.mLoginUserInfo.mBirthday != null && !this.mLoginUserInfo.mBirthday.equalsIgnoreCase(bs.b)) {
            this.mLoginUserInfo.mAge = new StringBuilder().append(Calendar.getInstance().get(1) - Integer.parseInt(this.mLoginUserInfo.mBirthday.substring(0, 4))).toString();
        }
        lzmaFile(this.mDtPath, String.valueOf(this.mDtPath) + ".lzma");
        File _dt = new File(String.valueOf(this.mDtPath) + ".lzma");
        lzmaFile(this.mFlagPath, String.valueOf(this.mFlagPath) + ".lzma");
        File _flag = new File(String.valueOf(this.mFlagPath) + ".lzma");
        PM85_ini _ini = new PM85_ini();
        if (this.mLoginUserInfo.mAge != null && !this.mLoginUserInfo.mAge.equalsIgnoreCase(bs.b)) {
            _ini.ACG = this.mLoginUserInfo.mAge;
        }
        if (this.mLoginUserInfo.mHeight != null && !this.mLoginUserInfo.mHeight.equalsIgnoreCase(bs.b)) {
            _ini.HEIGHT = this.mLoginUserInfo.mHeight;
        }
        if (this.mLoginUserInfo.mWeight == null || !this.mLoginUserInfo.mWeight.equalsIgnoreCase(bs.b)) {
        }
        _ini.WEIGHT = this.mLoginUserInfo.mWeight;
        if (this.mLoginUserInfo.mSex == null || !this.mLoginUserInfo.mSex.equalsIgnoreCase(bs.b)) {
        }
        _ini.SEX = Integer.parseInt(this.mLoginUserInfo.mSex) + 1;
        if (this.mLoginUserInfo.mUserName != null) {
            _ini.NAME = this.mLoginUserInfo.mUserName;
        }
        if (this.mLoginUserInfo.mPhone != null) {
            _ini.PHONE = this.mLoginUserInfo.mPhone;
        }
        _ini.DATETIME = makeDate();
        _ini.SmpTime = makeDate();
        String _iniFile = String.valueOf(this.mTempDir) + CookieSpec.PATH_DELIM + "pm85.ini";
        _ini.SaveIni(_iniFile);
        lzmaFile(_iniFile, String.valueOf(_iniFile) + ".lzma");
        File _inilzma = new File(String.valueOf(_iniFile) + ".lzma");
        byte[] _b = new byte[2014];
        byte[] _temp = new byte[4];
        try {
            OutputStream _out = new FileOutputStream(new File(this.mCasePath));
            _out.write(3);
            _temp[0] = 1;
            _temp[1] = 0;
            _temp[2] = 0;
            _temp[3] = 0;
            _out.write(_temp);
            _temp[0] = (byte) ((int) (_dt.length() & 255));
            _temp[1] = (byte) ((int) ((_dt.length() >> 8) & 255));
            _temp[2] = (byte) ((int) ((_dt.length() >> 16) & 255));
            _temp[3] = (byte) ((int) ((_dt.length() >> 24) & 255));
            _out.write(_temp);
            _temp[0] = 0;
            _temp[1] = 0;
            _temp[2] = 0;
            _temp[3] = 0;
            _out.write(_temp);
            _temp[0] = (byte) ((int) (_inilzma.length() & 255));
            _temp[1] = (byte) ((int) ((_inilzma.length() >> 8) & 255));
            _temp[2] = (byte) ((int) ((_inilzma.length() >> 16) & 255));
            _temp[3] = (byte) ((int) ((_inilzma.length() >> 24) & 255));
            _out.write(_temp);
            _temp[0] = 5;
            _temp[1] = 0;
            _temp[2] = 0;
            _temp[3] = 0;
            _out.write(_temp);
            _temp[0] = (byte) ((int) (_flag.length() & 255));
            _temp[1] = (byte) ((int) ((_flag.length() >> 8) & 255));
            _temp[2] = (byte) ((int) ((_flag.length() >> 16) & 255));
            _temp[3] = (byte) ((int) ((_flag.length() >> 24) & 255));
            _out.write(_temp);
            InputStream _in = new FileInputStream(String.valueOf(this.mDtPath) + ".lzma");
            while (true) {
                int _n = _in.read(_b);
                if (_n == -1) {
                    break;
                }
                _out.write(_b, 0, _n);
            }
            InputStream _in2 = new FileInputStream(String.valueOf(_iniFile) + ".lzma");
            while (true) {
                int _n2 = _in2.read(_b);
                if (_n2 == -1) {
                    break;
                }
                _out.write(_b, 0, _n2);
            }
            InputStream _in3 = new FileInputStream(String.valueOf(this.mFlagPath) + ".lzma");
            while (true) {
                int _n3 = _in3.read(_b);
                if (_n3 == -1) {
                    _in3.close();
                    _out.close();
                    return;
                }
                _out.write(_b, 0, _n3);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean lzmaFile(String srcFile, String dstFile) {
        Log.e("sdfd", "lzmaFile");
        if (!new File(srcFile.substring(0, srcFile.lastIndexOf(CookieSpec.PATH_DELIM))).exists()) {
            return false;
        }
        File _file = new File(dstFile.substring(0, dstFile.lastIndexOf(CookieSpec.PATH_DELIM)));
        if (!_file.exists()) {
            _file.mkdirs();
        }
        if (HVnative.lzmaEn(dstFile, srcFile, (HVCallBack) null)) {
            return true;
        }
        return false;
    }

    public void makePath(String path) {
        File _file = new File(path);
        if (!_file.exists()) {
            _file.mkdirs();
        }
    }

    public void copyFile(String src, String dst) {
        File _src = new File(src);
        File _dst = new File(dst.substring(0, dst.lastIndexOf(CookieSpec.PATH_DELIM)));
        if (_src.exists()) {
            if (_dst.exists()) {
                _dst.mkdirs();
            }
            byte[] _b = new byte[2014];
            try {
                InputStream _in = new FileInputStream(_src);
                OutputStream _out = new FileOutputStream(dst);
                while (true) {
                    int _n = _in.read(_b);
                    if (_n == -1) {
                        _in.close();
                        _out.close();
                        return;
                    }
                    _out.write(_b, 0, _n);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
