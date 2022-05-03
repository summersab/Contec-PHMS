package com.contec.phms.upload.cases.spir;

import com.contec.phms.db.LoginUserDao;
import com.contec.phms.upload.cases.pm85.IniReader;
import com.contec.phms.util.PageUtil;
import com.zxing.android.decoding.Intents;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import org.dtools.ini.BasicIniFile;
import org.dtools.ini.IniFile;
import org.dtools.ini.IniFileWriter;
import org.dtools.ini.IniSection;
import u.aly.bs;

public class PatientINI implements Serializable {
    private static final long serialVersionUID = 1;

    public void createINI(String path) {
        LoginUserDao _loginInfor = PageUtil.getLoginUserInfo();
        File file = new File(path);
        IniFile _iFile = new BasicIniFile();
        IniFileWriter iniFileWriter = new IniFileWriter(_iFile, file);
        IniSection _section = _iFile.addSection(IniReader.SESSION1);
        _section.addItem("HOSPITAL").setValue("康泰医学系统有限公司");
        _section.addItem("ID").setValue(79);
        _section.addItem("UPLOADER").setValue("NULL");
        _section.addItem("REPORTTO").setValue("C13030200060002@contechealth.com");
        IniSection _section2 = _iFile.addSection(IniReader.SESSION2);
        _section2.addItem("NAME").setValue(_loginInfor.mUserName);
        _section2.addItem("CASEID").setValue(bs.b);
        _section2.addItem("PART").setValue(bs.b);
        _section2.addItem("BEDID").setValue(bs.b);
        _section2.addItem("CARDID").setValue(bs.b);
        _section2.addItem("HCARDID").setValue(bs.b);
        _section2.addItem("DATETIME").setValue("2012-06-13 14:58:47");
        _section2.addItem(LoginUserDao.SID).setValue("120613145847_22_rr_Chinese");
        _section2.addItem(Intents.WifiConnect.TYPE).setValue("Portable");
        _section2.addItem("SEX").setValue(_loginInfor.mSex);
        _section2.addItem("AGE").setValue(26);
        _section2.addItem("PACE").setValue(0);
        _section2.addItem("HEIGHT").setValue(_loginInfor.mHeight);
        _section2.addItem("WEIGHT").setValue(_loginInfor.mWeight);
        _section2.addItem("SECOND").setValue(" ");
        _section.addItem("CHAN").setValue(0);
        _section2.addItem("TEXT").setValue(" ");
        _section2.addItem("CHIEF").setValue(" ");
        _section2.addItem("PRESENT").setValue(" ");
        _section2.addItem("PAST").setValue(" ");
        try {
            iniFileWriter.write();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
