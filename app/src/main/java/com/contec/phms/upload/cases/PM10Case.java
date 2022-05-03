package com.contec.phms.upload.cases;

import android.util.Log;
import com.contec.jni.HVCallBack;
import com.contec.jni.HVnative;
import com.contec.phms.device.pm10.DeviceData;
import com.contec.phms.manager.device.ServiceBean;
import com.contec.phms.db.LoginUserDao;
import com.contec.phms.upload.cases.common.NEW_CASE;
import com.contec.phms.util.Constants;
import com.contec.phms.util.PageUtil;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.commons.httpclient.cookie.CookieSpec;
import u.aly.bs;

public class PM10Case {
    String mCasePath;
    DeviceData mDeviceData;
    String mFileName;
    LoginUserDao mLoginUserInfo = PageUtil.getLoginUserInfo();
    String mTempDir;

    public PM10Case(com.contec.phms.device.template.DeviceData data) {
        this.mDeviceData = (DeviceData) data;
        this.mFileName = data.dateToString();
        this.mTempDir = ServiceBean.getInstance().mTempPath;
        this.mCasePath = String.valueOf(this.mTempDir) + CookieSpec.PATH_DELIM + this.mFileName + ".dat";
    }

    public NEW_CASE process() {
        NEW_CASE _Case = null;
        if (makeDtFile()) {
            String mSendDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            if (lzmaFile(this.mCasePath, String.valueOf(this.mCasePath) + ".case")) {
                _Case = new NEW_CASE(mSendDate, "lisan", "1", PageUtil.addUUID(String.valueOf(this.mCasePath) + ".case"), bs.b, bs.b, 0);
                if (this.mDeviceData.mDataType.equalsIgnoreCase("ECG(CMS50K)")) {
                    _Case.setCaseName("ECG(CMS50K)");
                } else if (this.mDeviceData.mDataType.equalsIgnoreCase("ECG(CMS50K1)")) {
                    _Case.setCaseName("ECG(CMS50K1)");
                } else if (this.mDeviceData.mDataType.equalsIgnoreCase("ECG(PM10)")) {
                    _Case.setCaseName("ECG(PM10)");
                } else {
                    _Case.setCaseName(Constants.PM10_NAME);
                }
                _Case.setCaseType(19);
            }
        }
        return _Case;
    }

    public boolean makeDtFile() {
        if (this.mDeviceData.CaseData == null || this.mDeviceData.CaseData.length == 0) {
            return false;
        }
        byte[] _temp = new byte[4];
        try {
            OutputStream _out = new FileOutputStream(new File(this.mCasePath));
            _out.write(1);
            _temp[0] = (byte) (this.mDeviceData.CaseData.length & 255);
            _temp[1] = (byte) ((this.mDeviceData.CaseData.length >> 8) & 255);
            _temp[2] = (byte) ((this.mDeviceData.CaseData.length >> 16) & 255);
            _temp[3] = (byte) ((this.mDeviceData.CaseData.length >> 24) & 255);
            _out.write(_temp);
            _out.write(this.mDeviceData.CaseData);
            _out.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
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
}
