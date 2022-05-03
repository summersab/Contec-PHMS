package com.contec.phms.infos;

import com.contec.phms.db.UserInfoDao;
import com.contec.phms.util.Constants;
import java.io.File;
import java.io.IOException;
import org.dtools.ini.BasicIniFile;
import org.dtools.ini.IniFile;
import org.dtools.ini.IniFileWriter;
import org.dtools.ini.IniSection;
import u.aly.bs;

public class ConfigInfo {
    static final String mConfigPath = (String.valueOf(Constants.ContecPath) + "/Config.ini");
    public String mAddr;
    public String mAuto;
    public String mCaseNum;
    public String mDay;
    public String mFlag;
    public String mHeight;
    public String mID;
    public String mMonth;
    public String mName;
    public String mOrganizationM;
    public String mPeople;
    public String mPhone;
    public String mSex;
    public String mUploader;
    public String mWaste;
    public String mWeight;
    public String mYear;

    public ConfigInfo() {
        initInfo();
    }

    public void initInfo() {
        this.mWaste = "3";
        this.mFlag = "0";
        this.mAuto = "0";
        this.mDay = "22";
        this.mMonth = "9";
        this.mYear = "1985";
        this.mSex = "女";
        this.mWeight = "70";
        this.mHeight = "171";
        this.mPeople = "汉";
        this.mPhone = "110";
        this.mOrganizationM = bs.b;
        this.mAddr = bs.b;
        this.mID = bs.b;
        this.mName = "Contec";
        this.mCaseNum = "casenum";
    }

    public void makeConfig() {
        File file = new File(String.valueOf(Constants.ContecPath) + "/Config.ini");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            IniFile contecIniFile = new BasicIniFile();
            IniFileWriter iniFileWriter = new IniFileWriter(contecIniFile, file);
            IniSection MailSection = contecIniFile.addSection("MailSetting");
            IniSection DownLoadReportSection = contecIniFile.addSection("DownLoadReport");
            IniSection PatientSection = contecIniFile.addSection("PATIENT DATA");
            PatientSection.addItem("Height").setValue(171);
            PatientSection.addItem("Weight").setValue("70");
            PatientSection.addItem("Day").setValue(22);
            PatientSection.addItem("Uploader").setValue("uploader");
            PatientSection.addItem("Month").setValue(9);
            PatientSection.addItem("Year").setValue(1985);
            PatientSection.addItem(UserInfoDao.Sex).setValue("女");
            PatientSection.addItem("People").setValue("汉");
            PatientSection.addItem("Phone").setValue("110");
            PatientSection.addItem("OrganizationM").setValue(bs.b);
            PatientSection.addItem("Addr").setValue(bs.b);
            PatientSection.addItem("ID").setValue(bs.b);
            PatientSection.addItem("Name").setValue("Contec");
            PatientSection.addItem("CaseNum").setValue("casenum");
            MailSection.addItem("waste").setValue(3);
            DownLoadReportSection.addItem("Flag").setValue(0);
            DownLoadReportSection.addItem("Auto").setValue(0);
            try {
                iniFileWriter.write();
            } catch (IOException e2) {
                e2.printStackTrace();
            }
        }
    }
}
