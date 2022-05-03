package com.contec.phms.upload.cases.pm85;

import com.contec.phms.db.LoginUserDao;
import com.contec.phms.util.Constants;
import com.contec.phms.util.FileOperation;
import com.zxing.android.decoding.Intents;
import java.io.File;
import java.io.IOException;
import org.apache.commons.httpclient.cookie.CookieSpec;
import org.dtools.ini.BasicIniFile;
import org.dtools.ini.IniFile;
import org.dtools.ini.IniFileWriter;
import org.dtools.ini.IniSection;
import u.aly.bs;

public class PM85_ini {
    public String ACG = bs.b;
    public String Address = bs.b;
    public String BloodType = bs.b;
    public String CASEID = bs.b;
    public String CHAN = bs.b;
    public String CHIFE = bs.b;
    public String DATETIME = bs.b;
    public String EvtType = bs.b;
    public String HEIGHT = bs.b;
    public String HOSPITAL = bs.b;
    public String ID = bs.b;
    public String IDCard = bs.b;
    public String LANGUAGE = bs.b;
    public String NAME = bs.b;
    public byte PACE = 0;
    public String PART = bs.b;
    public String PAST = bs.b;
    public String PHONE = bs.b;
    public String PRESENT = bs.b;
    public String SECOND = bs.b;
    public int SEX = 0;
    public String SID = bs.b;
    public String Section1 = IniReader.SESSION1;
    public String Section2 = IniReader.SESSION2;
    public String SmpTime = bs.b;
    public String TYPE = Constants.PM85_NAME;
    public String UPLOADER = bs.b;
    public String WEIGHT = bs.b;

    public boolean SaveIni(String strFileName) {
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
        IniFileWriter iniFileWriter = new IniFileWriter(ini, file);
        IniSection dataSection = ini.addSection(IniReader.SESSION1);
        dataSection.addItem("HOSPITAL").setValue(this.HOSPITAL);
        dataSection.addItem("ID").setValue(this.ID);
        dataSection.addItem("UPLOADER").setValue(this.UPLOADER);
        IniSection dataSection2 = ini.addSection(IniReader.SESSION2);
        dataSection2.addItem(LoginUserDao.SID).setValue(this.SID);
        dataSection2.addItem("NAME").setValue(this.NAME);
        dataSection2.addItem("CASEID").setValue(this.CASEID);
        dataSection2.addItem("PART").setValue(this.PART);
        dataSection2.addItem("DATETIME").setValue(this.DATETIME);
        dataSection2.addItem(Intents.WifiConnect.TYPE).setValue(this.TYPE);
        dataSection2.addItem("CHAN").setValue(this.CHAN);
        dataSection2.addItem("SEX").setValue((long) this.SEX);
        dataSection2.addItem("ACG").setValue(this.ACG);
        dataSection2.addItem("PACE").setValue((long) this.PACE);
        dataSection2.addItem("HEIGHT").setValue(this.HEIGHT);
        dataSection2.addItem("WEIGHT").setValue(this.WEIGHT);
        dataSection2.addItem("SECOND").setValue(this.SECOND);
        dataSection2.addItem("LANGUAGE").setValue(this.LANGUAGE);
        dataSection2.addItem("CHIFE").setValue(this.CHIFE);
        dataSection2.addItem("Address").setValue(this.Address);
        dataSection2.addItem("PHONE").setValue(this.PHONE);
        dataSection2.addItem("EvtType").setValue(this.EvtType);
        dataSection2.addItem("IDCard").setValue(this.IDCard);
        dataSection2.addItem("PAST").setValue(this.PAST);
        dataSection2.addItem("PRESENT").setValue(this.PRESENT);
        dataSection2.addItem("BloodType").setValue(this.BloodType);
        dataSection2.addItem("SmpTime").setValue(this.SmpTime);
        try {
            iniFileWriter.write();
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        return true;
    }
}
