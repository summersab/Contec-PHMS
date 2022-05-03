package com.contec.phms.upload.cases;

import android.util.Log;
import com.contec.jni.HVCallBack;
import com.contec.jni.HVnative;
import com.contec.phms.device.cms50k.SpO2Fragment;
import com.contec.phms.device.template.DeviceData;
import com.contec.phms.manager.device.ServiceBean;
import com.contec.phms.upload.cases.common.NEW_CASE;
import com.contec.phms.upload.cases.spo2.DeviceInfo;
import com.contec.phms.upload.cases.spo2.SaveTime_T;
import com.contec.phms.upload.cases.spo2.SpO2PulsePack;
import com.contec.phms.upload.cases.spo2.UserInfo;
import com.contec.phms.upload.cases.spo2.Util;
import com.contec.phms.util.CLog;
import com.contec.phms.util.Constants;
import com.contec.phms.util.PageUtil;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.commons.httpclient.cookie.CookieSpec;
import u.aly.bs;

public class CMS50KCase {
    static int PILowLimit = 0;
    static int PIUpLimit = 2200;
    int PULSELowLimit = 0;
    int PULSEUpLimit = 254;
    int SPO2LowLimit = 0;
    int SPO2UpLimit = 100;
    final String TAG = "Spo2_SaveCase";
    SpO2Fragment _Fragment;
    public int e_PI_exclude = 65535;
    public final int e_pulse_exclude = 255;
    public final int e_spO2_exclude = 127;
    String mBasePath;
    String mCasePath;
    String mCasePath_Move;
    String mCasePath_SpO2;
    DeviceData mData;
    public DeviceInfo mDeviceInfo;
    String mFileName;
    String mName;
    String mPatientID;
    String mPhotoPath;
    public SaveTime_T mSaveTime;
    String mSendDate;
    String mTempDir;
    public UserInfo mUserInfo;

    public CMS50KCase(DeviceData data) {
        this.mData = data;
        this._Fragment = (SpO2Fragment) this.mData.mDataList.get(0);
        this.mTempDir = ServiceBean.getInstance().mTempPath;
        if (this.mData.mFileInfo != null) {
            this.mFileName = String.valueOf(this.mData.mFileInfo[2]);
        } else {
            this.mFileName = String.valueOf(this.mData.mDataType) + this.mData.dateToString();
        }
    }

    public NEW_CASE process() {
        makeSPO2File();
        makeMoveFile();
        makeDatFile1();
        return makeCase();
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

    public void makeMoveFile() {
        FileOutputStream mOut = null;
        if (this._Fragment.MovementPoint == null) {
            File _target = new File(this.mTempDir, CookieSpec.PATH_DELIM + this.mFileName + ".activity");
            this.mCasePath_Move = _target.getPath();
            try {
                if (!_target.exists()) {
                    _target.getParentFile().mkdirs();
                    _target.createNewFile();
                }
                mOut = new FileOutputStream(_target);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (mOut != null) {
                byte[] bArr = this._Fragment.MovementPoint;
                int[] _MoveTime = this._Fragment.MovementTime;
                Util.writeLong((OutputStream) mOut, (long) _MoveTime[0]);
                Util.writeLong((OutputStream) mOut, (long) _MoveTime[1]);
                Util.writeLong((OutputStream) mOut, (long) _MoveTime[2]);
                Util.writeLong((OutputStream) mOut, (long) _MoveTime[3]);
                Util.writeLong((OutputStream) mOut, (long) _MoveTime[4]);
                Util.writeLong((OutputStream) mOut, (long) _MoveTime[5]);
                Util.writeInt(mOut, 0);
                Util.writeInt(mOut, 30);
                Util.writeInt(mOut, this._Fragment.MovementEnd);
                Util.writeInt(mOut, (24 - (this._Fragment.MovementEnd - this._Fragment.MovementStart)) * 60);
                return;
            }
            CLog.i("Spo2_SaveCase", "Get OutputStream Failed");
            return;
        }
        File _target2 = new File(this.mTempDir, CookieSpec.PATH_DELIM + this.mFileName + ".activity");
        this.mCasePath_Move = _target2.getPath();
        try {
            if (!_target2.exists()) {
                _target2.getParentFile().mkdirs();
                _target2.createNewFile();
            }
            mOut = new FileOutputStream(_target2);
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        if (mOut != null) {
            byte[] _MoveFragment = this._Fragment.MovementPoint;
            int[] _MoveTime2 = this._Fragment.MovementTime;
            Util.writeLong((OutputStream) mOut, (long) _MoveTime2[0]);
            Util.writeLong((OutputStream) mOut, (long) _MoveTime2[1]);
            Util.writeLong((OutputStream) mOut, (long) _MoveTime2[2]);
            Util.writeLong((OutputStream) mOut, (long) _MoveTime2[3]);
            Util.writeLong((OutputStream) mOut, (long) _MoveTime2[4]);
            Util.writeLong((OutputStream) mOut, (long) _MoveTime2[5]);
            Util.writeInt(mOut, _MoveFragment.length);
            Util.writeInt(mOut, 30);
            Util.writeInt(mOut, this._Fragment.MovementEnd);
            Util.writeInt(mOut, (24 - (this._Fragment.MovementEnd - this._Fragment.MovementStart)) * 60);
            try {
                mOut.write(_MoveFragment);
            } catch (IOException e3) {
                e3.printStackTrace();
            }
            for (int i = 0; i < _MoveFragment.length; i++) {
                Log.e("wsd", "Physical activity date: " + _MoveFragment[i]);
            }
            return;
        }
        CLog.i("Spo2_SaveCase", "Get OutputStream Failed");
    }

    public void makeSPO2File() {
        FileOutputStream mOut = null;
        File _target = new File(this.mTempDir, CookieSpec.PATH_DELIM + this.mFileName + ".SpO2");
        this.mCasePath_SpO2 = _target.getPath();
        try {
            if (!_target.exists()) {
                _target.getParentFile().mkdirs();
                _target.createNewFile();
            }
            mOut = new FileOutputStream(_target);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (mOut != null) {
            byte[] _SpO2 = this._Fragment.Spo2Segment;
            byte[] _Pulse = this._Fragment.PulseSegment;
            int n = _SpO2.length;
            this.mData.mUserInfo.writeToFile(mOut);
            this.mData.mDeviceInfo.writeToFile(mOut);
            this.mData.mSaveTime.writeToFile(mOut);
            Util.writeLong((OutputStream) mOut, (long) n);
            for (int i = 0; i < n; i++) {
                SpO2PulsePack _packSp02 = new SpO2PulsePack();
                _packSp02.mSpO2 = _SpO2[i];
                _packSp02.mPulse = _Pulse[i];
                _packSp02.mPI_Type = 1;
                if (_packSp02.mSpO2 > this.SPO2UpLimit || _packSp02.mSpO2 <= this.SPO2LowLimit) {
                    _packSp02.mSpO2 = Byte.MAX_VALUE;
                }
                if ((_packSp02.mPulse & 255) >= this.PULSEUpLimit || (_packSp02.mPulse & 255) <= this.PULSELowLimit) {
                    _packSp02.mPulse = -1;
                }
                _packSp02.writeToFile(mOut);
            }
        } else {
            CLog.i("Spo2_SaveCase", "Get OutputStream Failed");
        }
        CLog.i("Spo2_SaveCase", "Make Spo2 file finished");
    }

    public void makeDatFile_movenull() {
        File _dtSpO2 = new File(this.mCasePath_SpO2);
        this.mCasePath = String.valueOf(this.mTempDir) + CookieSpec.PATH_DELIM + this.mFileName + ".dat";
        byte[] _b = new byte[2014];
        byte[] _temp = new byte[4];
        try {
            OutputStream _out = new FileOutputStream(new File(this.mCasePath));
            _temp[0] = 2;
            _temp[1] = 0;
            _temp[2] = 0;
            _temp[3] = 0;
            _out.write(_temp);
            _out.write(new byte[]{2});
            _temp[0] = (byte) ((int) (_dtSpO2.length() & 255));
            _temp[1] = (byte) ((int) ((_dtSpO2.length() >> 8) & 255));
            _temp[2] = (byte) ((int) ((_dtSpO2.length() >> 16) & 255));
            _temp[3] = (byte) ((int) ((_dtSpO2.length() >> 24) & 255));
            _out.write(_temp);
            _out.write(new byte[]{4});
            _temp[0] = 8;
            _temp[1] = 0;
            _temp[2] = 0;
            _temp[3] = 0;
            _out.write(_temp);
            InputStream _in = new FileInputStream(_dtSpO2);
            while (true) {
                int _n = _in.read(_b);
                if (_n == -1) {
                    _out.write(this._Fragment.mCode);
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

    public void makeDatFile1() {
        File _dtSpO2 = new File(this.mCasePath_SpO2);
        File _dtMove = new File(this.mCasePath_Move);
        this.mCasePath = String.valueOf(this.mTempDir) + CookieSpec.PATH_DELIM + this.mFileName + ".dat";
        byte[] _b = new byte[2014];
        byte[] _temp = new byte[4];
        try {
            OutputStream _out = new FileOutputStream(new File(this.mCasePath));
            _temp[0] = 3;
            _temp[1] = 0;
            _temp[2] = 0;
            _temp[3] = 0;
            _out.write(_temp);
            _out.write(new byte[]{1});
            _temp[0] = (byte) ((int) (_dtMove.length() & 255));
            _temp[1] = (byte) ((int) ((_dtMove.length() >> 8) & 255));
            _temp[2] = (byte) ((int) ((_dtMove.length() >> 16) & 255));
            _temp[3] = (byte) ((int) ((_dtMove.length() >> 24) & 255));
            _out.write(_temp);
            _out.write(new byte[]{2});
            _temp[0] = (byte) ((int) (_dtSpO2.length() & 255));
            _temp[1] = (byte) ((int) ((_dtSpO2.length() >> 8) & 255));
            _temp[2] = (byte) ((int) ((_dtSpO2.length() >> 16) & 255));
            _temp[3] = (byte) ((int) ((_dtSpO2.length() >> 24) & 255));
            _out.write(_temp);
            _out.write(new byte[]{4});
            _temp[0] = 8;
            _temp[1] = 0;
            _temp[2] = 0;
            _temp[3] = 0;
            _out.write(_temp);
            InputStream _in = new FileInputStream(_dtMove);
            while (true) {
                int _n = _in.read(_b);
                if (_n == -1) {
                    break;
                }
                _out.write(_b, 0, _n);
            }
            InputStream _in2 = new FileInputStream(_dtSpO2);
            while (true) {
                int _n2 = _in2.read(_b);
                if (_n2 == -1) {
                    _out.write(this._Fragment.mCode);
                    _in2.close();
                    _out.close();
                    return;
                }
                _out.write(_b, 0, _n2);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public NEW_CASE makeCase() {
        this.mSendDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(((int) this.mData.mSaveTime.m_year) - 1900, ((int) this.mData.mSaveTime.m_month) - 1, (int) this.mData.mSaveTime.m_day, (int) this.mData.mSaveTime.m_hour, (int) this.mData.mSaveTime.m_minute, (int) this.mData.mSaveTime.m_second));
        this.mName = "Contec";
        this.mPatientID = "1";
        this.mPhotoPath = bs.b;
        CLog.i("Spo2_SaveCase", "Make NEW_CASE finished");
        lzmaFile(this.mCasePath, String.valueOf(this.mCasePath) + "lzma");
        NEW_CASE _cCase = new NEW_CASE(this.mSendDate, this.mName, this.mPatientID, PageUtil.addUUID(String.valueOf(this.mCasePath) + "lzma"), bs.b, this.mPhotoPath, 0);
        _cCase.setCaseName(Constants.CMS50K_NAME);
        _cCase.setCaseType(20);
        return _cCase;
    }
}
