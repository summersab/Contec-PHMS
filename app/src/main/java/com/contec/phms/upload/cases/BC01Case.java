package com.contec.phms.upload.cases;

import android.util.Log;
import com.contec.jni.HVCallBack;
import com.contec.jni.HVnative;
import com.contec.phms.device.bc01.DeviceData;
import com.contec.phms.manager.device.ServiceBean;
import com.contec.phms.db.LoginUserDao;
import com.contec.phms.upload.cases.BC01.MakeBaseCase_BC01;
import com.contec.phms.upload.cases.BC01.XML_BC01;
import com.contec.phms.upload.cases.common.NEW_CASE;
import com.contec.phms.util.Constants;
import com.contec.phms.util.PageUtil;
import com.example.bm77_bc_code.BC401_Struct;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.commons.httpclient.cookie.CookieSpec;
import u.aly.bs;

public class BC01Case {
    String mBasePath;
    String mCasePath;
    String mCaseXML;
    DeviceData mDeviceData;
    String mFileName;
    LoginUserDao mLoginUserInfo = PageUtil.getLoginUserInfo();
    String mTempDir;

    public BC01Case(com.contec.phms.device.template.DeviceData data) {
        this.mDeviceData = (DeviceData) data;
        this.mFileName = data.dateToString();
        this.mTempDir = ServiceBean.getInstance().mTempPath;
        this.mBasePath = String.valueOf(this.mTempDir) + CookieSpec.PATH_DELIM + this.mFileName + ".base";
        this.mCaseXML = String.valueOf(this.mTempDir) + CookieSpec.PATH_DELIM + this.mFileName + ".xml";
        this.mCasePath = String.valueOf(this.mTempDir) + CookieSpec.PATH_DELIM + this.mFileName + ".dat";
        Log.e("###################", "###################");
        Log.e("mFileName" + this.mFileName, "mTempDir" + this.mTempDir);
        Log.e("mBasePath" + this.mBasePath, "mCasePath" + this.mCasePath);
        Log.e("###################", "###################");
    }

    public void makeBaseCase() {
        MakeBaseCase_BC01 basecase = new MakeBaseCase_BC01();
        basecase.age = this.mLoginUserInfo.mAge;
        basecase.birthday = this.mLoginUserInfo.mBirthday;
        basecase.bloodsugar = bs.b;
        basecase.bloodtype = bs.b;
        basecase.checktime = bs.b;
        basecase.datatype = "1";
        basecase.description = bs.b;
        basecase.device = "ECG7(ECG12、CM300)";
        basecase.height = this.mLoginUserInfo.mHeight;
        basecase.language = "Chinese";
        basecase.mobile = this.mLoginUserInfo.mPhone;
        basecase.nation = bs.b;
        basecase.personid = this.mLoginUserInfo.mPersonID;
        basecase.sex = this.mLoginUserInfo.mSex;
        basecase.username = this.mLoginUserInfo.mUserName;
        basecase.weight = this.mLoginUserInfo.mWeight;
        basecase.ToDo(this.mBasePath);
    }

    public void makeXmlCase() {
        BC401_Struct _Struct = this.mDeviceData.mDataList.get(0);
        XML_BC01 _Xml_BC01 = new XML_BC01();
        _Xml_BC01.setBIL(_Struct.BIL);
        _Xml_BC01.setBLD(_Struct.BLD);
        _Xml_BC01.setGLU(_Struct.GLU);
        _Xml_BC01.setKET(_Struct.KET);
        _Xml_BC01.setLEU(_Struct.LEU);
        _Xml_BC01.setNIT(_Struct.NIT);
        _Xml_BC01.setPH(_Struct.PH);
        _Xml_BC01.setPRO(_Struct.PRO);
        _Xml_BC01.setSG(_Struct.SG);
        _Xml_BC01.setURO(_Struct.URO);
        _Xml_BC01.setVC(_Struct.VC);
        _Xml_BC01.TOXML(this.mCaseXML);
    }

    public NEW_CASE process() {
        makeBaseCase();
        makeXmlCase();
        makeDtFile();
        String mSendDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        lzmaFile(this.mCasePath, String.valueOf(this.mCasePath) + ".case");
        NEW_CASE _Case = new NEW_CASE(mSendDate, "lisan", "1", PageUtil.addUUID(String.valueOf(this.mCasePath) + ".case"), this.mBasePath, bs.b, 0);
        _Case.setCaseName(Constants.BC01_NAME);
        _Case.setCaseType(1);
        return _Case;
    }

    public void makeDtFile() {
        lzmaFile(this.mCaseXML, String.valueOf(this.mCaseXML) + ".lzma");
        File _xmlFlie = new File(String.valueOf(this.mCaseXML) + ".lzma");
        File _baseFile = new File(this.mBasePath);
        byte[] _b = new byte[2014];
        byte[] _temp = new byte[4];
        try {
            OutputStream _out = new FileOutputStream(new File(this.mCasePath));
            _temp[0] = (byte) ((int) (_baseFile.length() & 255));
            _temp[1] = (byte) ((int) ((_baseFile.length() >> 8) & 255));
            _temp[2] = (byte) ((int) ((_baseFile.length() >> 16) & 255));
            _temp[3] = (byte) ((int) ((_baseFile.length() >> 24) & 255));
            _out.write(_temp);
            int _dataLen = (int) (9 + _xmlFlie.length());
            _temp[0] = (byte) (_dataLen & 255);
            _temp[1] = (byte) ((_dataLen >> 8) & 255);
            _temp[2] = (byte) ((_dataLen >> 16) & 255);
            _temp[3] = (byte) ((_dataLen >> 24) & 255);
            _out.write(_temp);
            InputStream _in = new FileInputStream(this.mBasePath);
            while (true) {
                int _n = _in.read(_b);
                if (_n == -1) {
                    break;
                }
                _out.write(_b, 0, _n);
            }
            _out.write(3);
            _temp[0] = 3;
            _temp[1] = 0;
            _temp[2] = 0;
            _temp[3] = 0;
            _out.write(_temp);
            _temp[0] = (byte) ((int) (_xmlFlie.length() & 255));
            _temp[1] = (byte) ((int) ((_xmlFlie.length() >> 8) & 255));
            _temp[2] = (byte) ((int) ((_xmlFlie.length() >> 16) & 255));
            _temp[3] = (byte) ((int) ((_xmlFlie.length() >> 24) & 255));
            _out.write(_temp);
            InputStream _in2 = new FileInputStream(String.valueOf(this.mCaseXML) + ".lzma");
            while (true) {
                int _n2 = _in2.read(_b);
                if (_n2 == -1) {
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
