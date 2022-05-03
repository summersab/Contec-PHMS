package com.contec.phms.upload.cases;

import android.util.Log;
import com.contec.jni.HVCallBack;
import com.contec.jni.HVnative;
import com.contec.phms.device.template.DeviceData;
import com.contec.phms.manager.device.ServiceBean;
import com.contec.phms.upload.cases.common.NEW_CASE;
import com.contec.phms.upload.cases.spo2.SpO2PulsePack;
import com.contec.phms.upload.cases.spo2.Util;
import com.contec.phms.util.CLog;
import com.contec.phms.util.Constants;
import com.contec.phms.util.PageUtil;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.commons.httpclient.cookie.CookieSpec;
import u.aly.bs;

public class Spo2Case {
    static int PILowLimit = 0;
    static int PIUpLimit = 2200;
    int PULSELowLimit = 0;
    int PULSEUpLimit = 254;
    int SPO2LowLimit = 0;
    int SPO2UpLimit = 100;
    final String TAG = "Spo2_SaveCase";
    public int e_PI_exclude = 65535;
    public final int e_pulse_exclude = 255;
    public final int e_spO2_exclude = 127;
    String mBasePath;
    String mCasePath;
    DeviceData mData;
    String mFileName;
    String mName;
    String mPatientID;
    String mPhotoPath;
    String mSendDate;
    String mTempDir;

    public Spo2Case(DeviceData data) {
        this.mData = data;
        this.mTempDir = ServiceBean.getInstance().mTempPath;
        if (this.mData.mFileInfo != null) {
            this.mFileName = String.valueOf(this.mData.mFileInfo[2]);
        } else {
            this.mFileName = String.valueOf(this.mData.mDataType) + this.mData.dateToString();
        }
    }

    public NEW_CASE process() {
        makeSPO2File();
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

    public void makeSPO2File() {
        FileOutputStream mOut = null;
        File _target = new File(this.mTempDir, CookieSpec.PATH_DELIM + this.mFileName + ".SpO2");
        this.mCasePath = _target.getPath();
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
            int n = this.mData.mDataList.size();
            this.mData.mUserInfo.writeToFile(mOut);
            this.mData.mDeviceInfo.writeToFile(mOut);
            this.mData.mSaveTime.writeToFile(mOut);
            Util.writeLong((OutputStream) mOut, (long) (n * 5));
            for (int i = 0; i < n; i++) {
                byte[] _pack = (byte[]) this.mData.mDataList.get(i);
                SpO2PulsePack _packSp02 = new SpO2PulsePack();
                _packSp02.mSpO2 = _pack[0];
                _packSp02.mPulse = _pack[1];
                _packSp02.mPI = (_pack[2] & 255) | ((_pack[3] & 255) << 8);
                if (this.mData.mDeviceType.equalsIgnoreCase("CMS50F")) {
                    _packSp02.mPI_Type = 1;
                } else {
                    _packSp02.mPI_Type = 0;
                }
                if (_packSp02.mSpO2 > this.SPO2UpLimit || _packSp02.mSpO2 <= this.SPO2LowLimit) {
                    _packSp02.mSpO2 = Byte.MAX_VALUE;
                }
                if ((_packSp02.mPulse & 255) >= this.PULSEUpLimit || (_packSp02.mPulse & 255) <= this.PULSELowLimit) {
                    _packSp02.mPulse = -1;
                }
                if (_packSp02.mPI > PIUpLimit || _packSp02.mPI == PILowLimit) {
                    _packSp02.mPI = this.e_PI_exclude;
                }
                _packSp02.writeToFile(mOut);
            }
        } else {
            CLog.i("Spo2_SaveCase", "Get OutputStream Failed");
        }
        CLog.i("Spo2_SaveCase", "Make Spo2 file finished");
    }

    public NEW_CASE makeCase() {
        this.mSendDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        this.mName = "Contec";
        this.mPatientID = "1";
        this.mPhotoPath = bs.b;
        CLog.i("Spo2_SaveCase", "Make NEW_CASE finished");
        String _DatFile = String.valueOf(this.mTempDir) + CookieSpec.PATH_DELIM + this.mFileName + ".dat";
        lzmaFile(this.mCasePath, _DatFile);
        NEW_CASE _cCase = new NEW_CASE(this.mSendDate, this.mName, this.mPatientID, PageUtil.addUUID(_DatFile), bs.b, this.mPhotoPath, 0);
        _cCase.setCaseName(Constants.CMS50IW_NAME);
        _cCase.setCaseType(3);
        return _cCase;
    }
}
