package com.contec.phms.upload.cases;

import android.os.Message;
import android.util.Log;
import com.contec.jni.HVCallBack;
import com.contec.jni.HVnative;
import com.contec.phms.App_phms;
import com.contec.phms.device.abpm50w.DeviceData;
import com.contec.phms.device.abpm50w.P_Info;
import com.contec.phms.infos.PatientInfo;
import com.contec.phms.manager.device.DeviceManager;
import com.contec.phms.manager.device.ServiceBean;
import com.contec.phms.db.LoginUserDao;
import com.contec.phms.db.UserInfoDao;
import com.contec.phms.upload.cases.abpm.MemoryTimeFile;
import com.contec.phms.upload.cases.common.MakeBaseCase;
import com.contec.phms.upload.cases.common.NEW_CASE;
import com.contec.phms.util.CLog;
import com.contec.phms.util.Constants;
import com.contec.phms.util.FileOperation;
import com.contec.phms.util.PageUtil;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import org.apache.commons.httpclient.cookie.CookieSpec;
import org.dtools.ini.BasicIniFile;
import org.dtools.ini.IniFile;
import org.dtools.ini.IniFileReader;
import org.dtools.ini.IniFileWriter;
import org.dtools.ini.IniItem;
import org.dtools.ini.IniSection;
import u.aly.bs;

public class Abpm50Case {
    private final App_phms MAPP_PHMS = App_phms.getInstance();
    final String TAG = "Abpm50w SaveCase";
    String mBasePath;
    String mCasePath;
    DeviceData mDeviceData;
    String mIniName;
    String mName;
    String mPatientID;
    String mPhotoPath;
    String mSendDate;
    String mTempDir;
    private P_Info m_MyInfos = new P_Info();

    public Abpm50Case(com.contec.phms.device.template.DeviceData data) {
        this.mDeviceData = (DeviceData) data;
        this.mTempDir = ServiceBean.getInstance().mTempPath;
    }

    public NEW_CASE process() {
        makeAWPFile();
        makeBaseCase();
        return makeCase();
    }

    public void makeAWPFile() {
        this.mSendDate = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        this.mIniName = String.valueOf(this.mTempDir) + CookieSpec.PATH_DELIM + this.mSendDate + ".ini";
        this.mCasePath = String.valueOf(this.mTempDir) + CookieSpec.PATH_DELIM + this.mSendDate + ".awp";
        int m_size = this.mDeviceData.mDataList.size();
        Log.e("Data length: ", new StringBuilder(String.valueOf(m_size)).toString());
        int _progress = 0;
        SaveIni(this.mIniName, "0000000001FF");
        for (int i = 0; i < m_size; i++) {
            _progress += 35 / m_size;
            this.m_MyInfos = this.mDeviceData.getM_savedata().get(i);
            String dateString = this.m_MyInfos.m_iYear + "-" + this.m_MyInfos.m_iMonth + "-" + this.m_MyInfos.m_iDay + " " + this.m_MyInfos.m_iHour + ":" + this.m_MyInfos.m_iMinute;
            Log.e("date: ", dateString);
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            String date = null;
            try {
                date = format.format(format.parse(dateString));
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (SaveUploadData(date)) {
                SaveToFile(this.mCasePath, "0000000001FF");
            }
            if (Constants.Detail_Progress && DeviceManager.mDeviceBeanList != null) {
                DeviceManager.m_DeviceBean.mProgress = _progress + 10;
                Message msg = new Message();
                msg.what = 502;
                msg.arg2 = 1;
                App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(msg);
            }
        }
        byte[] _b = new byte[2014];
        try {
            OutputStream _out = new FileOutputStream(String.valueOf(this.mIniName) + ".awp");
            InputStream _in = new FileInputStream(this.mIniName);
            while (true) {
                int _n = _in.read(_b);
                if (_n == -1) {
                    break;
                }
                _out.write(_b, 0, _n);
            }
            InputStream _in2 = new FileInputStream(this.mCasePath);
            while (true) {
                int _n2 = _in2.read(_b);
                if (_n2 != -1) {
                    _out.write(_b, 0, _n2);
                } else {
                    return;
                }
            }
        } catch (Exception e2) {
            e2.printStackTrace();
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

    public void makeBaseCase() {
        PatientInfo m_pi = this.MAPP_PHMS.mPatientInfo;
        MakeBaseCase basecase = new MakeBaseCase();
        basecase.personid = m_pi.strPersonId;
        basecase.height = m_pi.nHeight;
        basecase.weight = m_pi.nWeight;
        basecase.datatype = 2;
        this.mBasePath = String.valueOf(this.mCasePath) + ".bas";
        basecase.ToDo(this.mBasePath);
        if (Constants.Detail_Progress && DeviceManager.mDeviceBeanList != null) {
            DeviceManager.m_DeviceBean.mProgress = 60;
            Message msg = new Message();
            msg.what = 502;
            msg.arg2 = 1;
            App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(msg);
        }
        CLog.i("Abpm50w SaveCase", "Make Base file finished");
    }

    public NEW_CASE makeCase() {
        this.mSendDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        this.mName = "Contec";
        this.mPatientID = "1";
        this.mPhotoPath = bs.b;
        CLog.i("Abpm50w SaveCase", "Make NEW_CASE finished");
        if (Constants.Detail_Progress && DeviceManager.mDeviceBeanList != null) {
            DeviceManager.m_DeviceBean.mProgress = 65;
            Message msg = new Message();
            msg.what = 502;
            msg.arg2 = 1;
            App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(msg);
        }
        lzmaFile(String.valueOf(this.mIniName) + ".awp", String.valueOf(this.mIniName) + ".dat");
        NEW_CASE _Case = new NEW_CASE(this.mSendDate, this.mName, this.mPatientID, PageUtil.addUUID(String.valueOf(this.mIniName) + ".dat"), this.mBasePath, this.mPhotoPath, 0);
        _Case.setCaseName(Constants.ABPM50_NAME);
        _Case.setCaseType(2);
        return _Case;
    }

    private boolean SaveUploadData(String date) {
        File file = new File(String.valueOf(this.mCasePath) + ".txt");
        Log.e("Text file path: ", new StringBuilder().append(file).toString());
        if (!file.exists()) {
            try {
                MemoryTimeFile.memoryTime(String.valueOf(this.mCasePath) + ".txt", date, true);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return true;
            }
        } else {
            String string = null;
            try {
                string = MemoryTimeFile.readMemoryTime(String.valueOf(this.mCasePath) + ".txt");
            } catch (Exception e2) {
                e2.printStackTrace();
            }
            if (string.contains(date)) {
                return false;
            }
            try {
                MemoryTimeFile.memoryTime(String.valueOf(this.mCasePath) + ".txt", date, true);
                return true;
            } catch (Exception e3) {
                e3.printStackTrace();
                return true;
            }
        }
    }

    boolean SaveIni(String strFileName, String strBthAddr) {
        FileOperation.makeDirs(strFileName.substring(0, strFileName.lastIndexOf(CookieSpec.PATH_DELIM) + 1));
        IniFile ini = new BasicIniFile();
        File file = new File(strFileName);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }
        LoginUserDao _userinfo = PageUtil.getLoginUserInfo();
        IniFileWriter iniFileWriter = new IniFileWriter(ini, file);
        IniSection dataSection = ini.addSection("PATIENT DATA");
        dataSection.addItem("BTH_ADDR").setValue(strBthAddr);
        dataSection.addItem("Test Count").setValue(1);
        dataSection.addItem("ID").setValue(_userinfo.mID);
        dataSection.addItem("Name").setValue(_userinfo.mUserName);
        dataSection.addItem(UserInfoDao.Language).setValue("Chinese");
        dataSection.addItem("Addr").setValue(_userinfo.mAddress);
        dataSection.addItem("Gender").setValue(_userinfo.mSex);
        dataSection.addItem("BirthDay").setValue(_userinfo.mBirthday);
        dataSection.addItem("Race").setValue(bs.b);
        dataSection.addItem("Height").setValue(_userinfo.mHeight);
        dataSection.addItem("Weight").setValue(_userinfo.mWeight);
        if (_userinfo.mBirthday != null && !_userinfo.mBirthday.equalsIgnoreCase(bs.b)) {
            _userinfo.mAge = new StringBuilder().append(Calendar.getInstance().get(1) - Integer.parseInt(_userinfo.mBirthday.substring(0, 4))).toString();
        }
        dataSection.addItem("Age").setValue(_userinfo.mAge);
        dataSection.addItem("Phone").setValue(_userinfo.mPhone);
        try {
            iniFileWriter.write();
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        return true;
    }

    boolean SaveToFile(String strFileName, String strBthAddr) {
        String strPack;
        String strComment;
        File file = new File(strFileName);
        int nSpanMins = 0;
        int nCount = 0;
        int cTC = this.m_MyInfos.m_ntc;
        int nSys = this.m_MyInfos.m_nSys;
        int nMap = this.m_MyInfos.m_nMap;
        int nDia = this.m_MyInfos.m_nDia;
        int nHR = this.m_MyInfos.m_nHR;
        int nYear = this.m_MyInfos.m_iYear;
        int cMon = this.m_MyInfos.m_iMonth;
        int cDay = this.m_MyInfos.m_iDay;
        int cHour = this.m_MyInfos.m_iHour;
        int cMin = this.m_MyInfos.m_iMinute;
        Log.e("*****************************Abpm50w SaveCase", String.valueOf(cTC) + " " + nSys + " " + nMap + " " + nDia + " " + nHR + " " + nYear + " " + cMon + " " + cDay + " " + cHour + " " + cMin);
        String strDataCount = bs.b;
        boolean bCreate = true;
        boolean bExist = false;
        if (file.exists()) {
            bExist = true;
        }
        if (bExist) {
            Date packTime = new Date(nYear, cMon - 1, cDay, cHour - 1, cMin);
            IniFile ini = new BasicIniFile();
            try {
                new IniFileReader(ini, new File(strFileName)).read();
            } catch (Exception e) {
                e.printStackTrace();
            }
            IniSection testNameSection = ini.getSection("TEST 1");
            nCount = Integer.parseInt(testNameSection.getItem("Count").getValue());
            if (nCount > 0) {
                int nPreSpan = Integer.parseInt(testNameSection.getItem(String.format("%d", new Object[]{Integer.valueOf(nCount)})).getValue().substring(12, 16), 16);
                long beginMill = new Date(Integer.parseInt(testNameSection.getItem("YearBegin").getValue()), Integer.parseInt(testNameSection.getItem("MonthBegin").getValue()) - 1, Integer.parseInt(testNameSection.getItem("DayBegin").getValue()), Integer.parseInt(testNameSection.getItem("HourBegin").getValue()) - 1, Integer.parseInt(testNameSection.getItem("MinBegin").getValue())).getTime();
                long preMill = beginMill + ((long) (nPreSpan * 1000 * 60));
                long nowMill = packTime.getTime();
                nSpanMins = (int) ((nowMill - beginMill) / 60000);
                if (nowMill >= preMill) {
                    bCreate = false;
                    nCount++;
                    strDataCount = String.format("%d", new Object[]{Integer.valueOf(nCount)});
                }
            }
        }
        if (bCreate) {
            FileOperation.makeDirs(strFileName.substring(0, strFileName.lastIndexOf(CookieSpec.PATH_DELIM) + 1));
            IniFile ini2 = new BasicIniFile();
            File file2 = new File(strFileName);
            if (!file2.exists()) {
                try {
                    file2.createNewFile();
                } catch (IOException e2) {
                    e2.printStackTrace();
                    return false;
                }
            }
            IniFileWriter iniFileWriter = new IniFileWriter(ini2, file2);
            String strBeginDate = String.format("%d/%d/%d %d:%d", new Object[]{Integer.valueOf(this.m_MyInfos.m_iYear), Integer.valueOf(this.m_MyInfos.m_iMonth), Integer.valueOf(this.m_MyInfos.m_iDay), Integer.valueOf(this.m_MyInfos.m_iHour), Integer.valueOf(this.m_MyInfos.m_iMinute)});
            IniSection testNameSection2 = ini2.addSection("TEST 1");
            testNameSection2.addItem("Medications").setValue(bs.b);
            testNameSection2.addItem("ReferringPhys").setValue(bs.b);
            testNameSection2.addItem("InterprettingPhys").setValue(bs.b);
            testNameSection2.addItem("Comments").setValue(bs.b);
            testNameSection2.addItem("Clinical Interp").setValue(bs.b);
            testNameSection2.addItem("OutpatientNo").setValue(bs.b);
            testNameSection2.addItem("AdmissionNo").setValue(bs.b);
            testNameSection2.addItem("BedNo").setValue(bs.b);
            testNameSection2.addItem("DepartmentNo").setValue(bs.b);
            testNameSection2.addItem("Email").setValue(bs.b);
            testNameSection2.addItem("TestBeginDate").setValue(strBeginDate);
            testNameSection2.addItem("YearBegin").setValue((long) this.m_MyInfos.m_iYear);
            testNameSection2.addItem("MonthBegin").setValue((long) this.m_MyInfos.m_iMonth);
            testNameSection2.addItem("DayBegin").setValue((long) this.m_MyInfos.m_iDay);
            testNameSection2.addItem("HourBegin").setValue((long) this.m_MyInfos.m_iHour);
            testNameSection2.addItem("MinBegin").setValue((long) this.m_MyInfos.m_iMinute);
            testNameSection2.addItem("StartCount").setValue(0);
            nSpanMins = 0;
            strDataCount = "1";
            nCount = 1;
            try {
                iniFileWriter.write();
            } catch (Exception e3) {
                e3.printStackTrace();
            }
        }
        File file3 = new File(strFileName);
        IniFile ini3 = new BasicIniFile();
        IniFileWriter iniFileWriter2 = new IniFileWriter(ini3, file3);
        try {
            new IniFileReader(ini3, file3).read();
        } catch (IOException e4) {
            e4.printStackTrace();
        }
        if (cTC == 3 || cTC == 0) {
            strPack = String.format("%02X%04X%02X%02X%02X%04X%02X%04X0%02X", new Object[]{0, Integer.valueOf(nSys), Integer.valueOf(nDia), Integer.valueOf(nHR), Integer.valueOf(nMap), Integer.valueOf(nSpanMins), Integer.valueOf(cTC), 0, 0});
        } else {
            strPack = String.format("%02X%04X%02X%02X%02X%04X%02X%04X1%02X", new Object[]{0, 0, 0, 0, 0, Integer.valueOf(nSpanMins), Integer.valueOf(cTC), 0, 0});
        }
        IniSection testNameSection3 = ini3.getSection("TEST 1");
        IniItem countItem = testNameSection3.getItem("Count");
        if (countItem == null) {
            countItem = testNameSection3.addItem("Count");
        }
        countItem.setValue((long) nCount);
        IniItem packItem = testNameSection3.getItem(strDataCount);
        if (packItem == null) {
            packItem = testNameSection3.addItem(strDataCount);
        }
        packItem.setValue(strPack);
        String strComName = String.format("C%d", new Object[]{Integer.valueOf(nCount)});
        int nTemp = Convert(cTC);
        if (nTemp == 0 || 3 == nTemp || -1 == nTemp) {
            strComment = bs.b;
        } else {
            strComment = bs.b;
        }
        IniItem commentItem = testNameSection3.getItem(strComName);
        if (commentItem == null) {
            commentItem = testNameSection3.addItem(strComName);
        }
        commentItem.setValue(strComment);
        try {
            iniFileWriter2.write();
        } catch (Exception e5) {
            e5.printStackTrace();
        }
        Integer.parseInt(testNameSection3.getItem("Count").getValue());
        return true;
    }

    private int Convert(int errorCode) {
        switch (errorCode) {
            case 0:
                return 0;
            case 2:
                return 102;
            case 3:
                return 3;
            case 4:
                return 90;
            case 5:
                return 86;
            case 6:
                return 85;
            case 7:
                return 87;
            case 8:
                return 2;
            case 9:
                return 1;
            case 10:
                return 110;
            case 11:
                return 2;
            case 12:
                return 89;
            case 14:
                return 114;
            case 15:
                return 115;
            case 19:
                return 4;
            default:
                return -1;
        }
    }
}
