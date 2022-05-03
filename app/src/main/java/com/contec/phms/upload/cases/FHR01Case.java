package com.contec.phms.upload.cases;

import android.util.Log;
import com.contec.jni.HVCallBack;
import com.contec.jni.HVnative;
import com.contec.phms.device.fhr01.BasInfo;
import com.contec.phms.device.fhr01.DataHead;
import com.contec.phms.device.fhr01.DeviceData;
import com.contec.phms.device.fhr01.FHR01_ini;
import com.contec.phms.device.fhr01.FHRInfo;
import com.contec.phms.device.pm10.ReceiveThread;
import com.contec.phms.manager.device.ServiceBean;
import com.contec.phms.db.LoginUserDao;
import com.contec.phms.upload.cases.common.NEW_CASE;
import com.contec.phms.util.Constants;
import com.contec.phms.util.FileOperation;
import com.contec.phms.util.PageUtil;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import org.apache.commons.httpclient.cookie.CookieSpec;
import u.aly.bs;
import u.aly.dp;

public class FHR01Case {
    private static int sampleRateInHz = 4000;
    DeviceData _mData;
    private int bufferSizeInBytes = 1024;
    String mBasePath;
    String mCasePath;
    String mDtPath;
    String mFileName;
    LoginUserDao mLoginUserInfo = PageUtil.getLoginUserInfo();
    String mName;
    String mPatientID;
    String mPhotoPath;
    String mRawPath;
    String mSendDate;
    String mTempDir;
    String mWAVPath;

    public FHR01Case(com.contec.phms.device.template.DeviceData data) {
        this.mFileName = data.dateToString();
        this.mTempDir = ServiceBean.getInstance().mTempPath;
        this.mDtPath = String.valueOf(this.mTempDir) + CookieSpec.PATH_DELIM + this.mFileName + ".dt";
        this.mRawPath = String.valueOf(this.mTempDir) + CookieSpec.PATH_DELIM + this.mFileName + ".raw";
        this.mWAVPath = String.valueOf(this.mTempDir) + CookieSpec.PATH_DELIM + this.mFileName + ".wav";
        this.mCasePath = String.valueOf(this.mTempDir) + "/temp.dat";
        this._mData = (DeviceData) data;
        makePath(this.mTempDir);
    }

    public NEW_CASE process() {
        makeBaseCase();
        makeWavFile(this._mData);
        makeDtFile(this._mData);
        makeDatFile();
        copyFile(this.mCasePath, "/sdcard/fhr01.dat");
        return makeCase();
    }

    public void makeBaseCase() {
        if (this.mLoginUserInfo.mBirthday != null && !this.mLoginUserInfo.mBirthday.equalsIgnoreCase(bs.b)) {
            this.mLoginUserInfo.mAge = new StringBuilder().append(Calendar.getInstance().get(1) - Integer.parseInt(this.mLoginUserInfo.mBirthday.substring(0, 4))).toString();
        }
        BasInfo baseCase = new BasInfo();
        baseCase.username = this.mLoginUserInfo.mUserName;
        baseCase.datatype = "14";
        baseCase.sex = this.mLoginUserInfo.mSex;
        baseCase.nAge = this.mLoginUserInfo.mAge;
        baseCase.height = this.mLoginUserInfo.mHeight;
        baseCase.weight = this.mLoginUserInfo.mWeight;
        baseCase.DATATIME = this.mLoginUserInfo.mDateTime;
        baseCase.personid = this.mLoginUserInfo.mPersonID;
        baseCase.phone = this.mLoginUserInfo.mPhone;
        this.mBasePath = String.valueOf(this.mTempDir) + CookieSpec.PATH_DELIM + this.mFileName + ".bas";
        baseCase.write(this.mTempDir, String.valueOf(this.mFileName) + ".bas");
    }

    public NEW_CASE makeCase() {
        this.mSendDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        this.mName = "Contec";
        this.mPatientID = "1";
        this.mPhotoPath = bs.b;
        lzmaFile(this.mCasePath, String.valueOf(this.mCasePath) + ".dat");
        NEW_CASE _Case = new NEW_CASE(this.mSendDate, "lisan", "1", PageUtil.addUUID(String.valueOf(this.mCasePath) + ".dat"), this.mBasePath, this.mPhotoPath, 0);
        _Case.setCaseName(Constants.FHR01_NAME);
        _Case.setCaseType(14);
        return _Case;
    }

    public void makeWavFile(com.contec.phms.device.template.DeviceData pData) {
        this.mRawPath = String.valueOf(this.mTempDir) + CookieSpec.PATH_DELIM + this.mFileName + ".raw";
        FileOperation.createFile(this.mRawPath);
        try {
            DataOutputStream _dos = new DataOutputStream(new FileOutputStream(new File(this.mRawPath)));
            for (int i = 0; i < this._mData.m_fetal_heart.size(); i++) {
                _dos.write(this._mData.m_fetal_heart.get(i), 12, 500);
            }
            _dos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void makeDtFile(com.contec.phms.device.template.DeviceData pData) {
        this.mDtPath = String.valueOf(this.mTempDir) + CookieSpec.PATH_DELIM + this.mFileName + ".dt";
        FileOperation.createFile(this.mDtPath);
        File _dt = new File(this.mDtPath);
        DataHead _DataHead = new DataHead((short) this._mData.m_iYear, (short) this._mData.m_iMonth, (short) (this._mData.m_iDay / 7), (short) this._mData.m_iDay, (short) this._mData.m_iHour, (short) this._mData.m_iMinute, (short) this._mData.m_iSec, (short) 0, 1);
        int _dataSize = (int) Math.ceil((((double) this._mData.m_fetal_heart.size()) * 1.0d) / 8.0d);
        FHRInfo[] _FHRInfo = new FHRInfo[_dataSize];
        for (int i = 0; i < _dataSize; i++) {
            _FHRInfo[i] = new FHRInfo((short) this._mData.m_fetal_heart.get(i * 8)[0], 0, 0, 0, i);
        }
        _DataHead.dwFileLength = (_dataSize * 12) + 18;
        Log.e("_DataHead.dwFileLength", "_DataHead.dwFileLength= " + _DataHead.dwFileLength);
        try {
            DataOutputStream _dos = new DataOutputStream(new FileOutputStream(_dt));
            byte[] _buf = _DataHead.toByteArray();
            _dos.write(_buf, 0, _buf.length);
            _dos.write(_buf, 0, _buf.length);
            _dos.write(_buf, 0, _buf.length);
            for (FHRInfo byteArray : _FHRInfo) {
                byte[] _buf2 = byteArray.toByteArray();
                _dos.write(_buf2, 0, _buf2.length);
            }
            _dos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void makeDatFile() {
        if (this.mLoginUserInfo.mBirthday != null && !this.mLoginUserInfo.mBirthday.equalsIgnoreCase(bs.b)) {
            this.mLoginUserInfo.mAge = new StringBuilder().append(Calendar.getInstance().get(1) - Integer.parseInt(this.mLoginUserInfo.mBirthday.substring(0, 4))).toString();
        }
        lzmaFile(this.mDtPath, String.valueOf(this.mDtPath) + ".lzma");
        File _dt = new File(String.valueOf(this.mDtPath) + ".lzma");
        copyWaveFile(this.mRawPath, this.mWAVPath);
        lzmaFile(this.mWAVPath, String.valueOf(this.mWAVPath) + ".lzma");
        File _wav_dt = new File(String.valueOf(this.mWAVPath) + ".lzma");
        FHR01_ini _ini = new FHR01_ini();
        _ini.CaseName = this.mLoginUserInfo.mUserName;
        _ini.CaseAge = this.mLoginUserInfo.mAge;
        _ini.CaseHeight = this.mLoginUserInfo.mHeight;
        _ini.CaseWeight = this.mLoginUserInfo.mWeight;
        _ini.CheckTime = this.mLoginUserInfo.mDateTime;
        _ini.CaseIDCard = this.mLoginUserInfo.mPersonID;
        _ini.CasePhone = this.mLoginUserInfo.mPhone;
        String _iniFile = String.valueOf(this.mTempDir) + CookieSpec.PATH_DELIM + "fhr01.ini";
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
            _temp[0] = 2;
            _temp[1] = 0;
            _temp[2] = 0;
            _temp[3] = 0;
            _out.write(_temp);
            _temp[0] = (byte) ((int) (_wav_dt.length() & 255));
            _temp[1] = (byte) ((int) ((_wav_dt.length() >> 8) & 255));
            _temp[2] = (byte) ((int) ((_wav_dt.length() >> 16) & 255));
            _temp[3] = (byte) ((int) ((_wav_dt.length() >> 24) & 255));
            _out.write(_temp);
            _temp[0] = 3;
            _temp[1] = 0;
            _temp[2] = 0;
            _temp[3] = 0;
            _out.write(_temp);
            _temp[0] = (byte) ((int) (_inilzma.length() & 255));
            _temp[1] = (byte) ((int) ((_inilzma.length() >> 8) & 255));
            _temp[2] = (byte) ((int) ((_inilzma.length() >> 16) & 255));
            _temp[3] = (byte) ((int) ((_inilzma.length() >> 24) & 255));
            _out.write(_temp);
            InputStream _in = new FileInputStream(String.valueOf(this.mDtPath) + ".lzma");
            while (true) {
                int _n = _in.read(_b);
                if (_n == -1) {
                    break;
                }
                _out.write(_b, 0, _n);
            }
            InputStream _in2 = new FileInputStream(String.valueOf(this.mWAVPath) + ".lzma");
            while (true) {
                int _n2 = _in2.read(_b);
                if (_n2 == -1) {
                    break;
                }
                _out.write(_b, 0, _n2);
            }
            InputStream _in3 = new FileInputStream(String.valueOf(_iniFile) + ".lzma");
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

    private void copyWaveFile(String inFilename, String outFilename) {
        long j = 0 + 36;
        long longSampleRate = (long) sampleRateInHz;
        byte[] data = new byte[this.bufferSizeInBytes];
        IOException e;
        try {
            FileInputStream in = new FileInputStream(inFilename);
            try {
                FileOutputStream out = new FileOutputStream(outFilename);
                try {
                    long totalAudioLen = in.getChannel().size();
                    WriteWaveFileHeader(out, totalAudioLen, totalAudioLen + 36, longSampleRate, 1, 4000);
                    while (in.read(data) != -1) {
                        out.write(data);
                    }
                    in.close();
                    out.close();
                    FileInputStream fileInputStream = in;
                } catch (FileNotFoundException ex) {
                    e = ex;
                    FileInputStream fileInputStream2 = in;
                    e.printStackTrace();
                } catch (IOException e2) {
                    e = e2;
                    FileInputStream fileInputStream3 = in;
                    e.printStackTrace();
                }
            } catch (FileNotFoundException e3) {
                e = e3;
                FileInputStream fileInputStream4 = in;
                e.printStackTrace();
            }
        } catch (FileNotFoundException e5) {
            e = e5;
            e.printStackTrace();
        }
    }

    private void WriteWaveFileHeader(FileOutputStream out, long totalAudioLen, long totalDataLen, long longSampleRate, int channels, long byteRate) throws IOException {
        out.write(new byte[]{82, 73, 70, 70, (byte) ((int) (255 & totalDataLen)), (byte) ((int) ((totalDataLen >> 8) & 255)), (byte) ((int) ((totalDataLen >> 16) & 255)), (byte) ((int) ((totalDataLen >> 24) & 255)), 87, 65, 86, 69, 102, 109, 116, 32, dp.n, 0, 0, 0, 1, 0, 1, 0, ReceiveThread.e_back_dateresponse, dp.m, 0, 0, ReceiveThread.e_back_dateresponse, dp.m, 0, 0, 1, 0, 8, 0, 100, 97, 116, 97, (byte) ((int) (255 & totalAudioLen)), (byte) ((int) ((totalAudioLen >> 8) & 255)), (byte) ((int) ((totalAudioLen >> 16) & 255)), (byte) ((int) ((totalAudioLen >> 24) & 255))}, 0, 44);
    }
}
