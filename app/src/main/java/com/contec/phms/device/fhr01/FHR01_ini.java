package com.contec.phms.device.fhr01;

import com.alibaba.cchannel.push.receiver.CPushMessageCodec;
import com.contec.phms.db.UserInfoDao;
import com.contec.phms.upload.cases.pm85.IniReader;
import com.contec.phms.util.FileOperation;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.httpclient.cookie.CookieSpec;
import org.dtools.ini.BasicIniFile;
import org.dtools.ini.IniFile;
import org.dtools.ini.IniFileWriter;
import org.dtools.ini.IniSection;
import u.aly.bs;

public class FHR01_ini {
    public String BirthTimes = bs.b;
    public String CaseAddress = bs.b;
    public String CaseAge = bs.b;
    public String CaseHeight = bs.b;
    public String CaseIDCard = bs.b;
    public String CaseName = "samho";
    public String CasePhone = bs.b;
    public String CaseRemark = bs.b;
    public String CaseWeight = bs.b;
    public String CheckTime = bs.b;
    public String ChildMonths = bs.b;
    public String Children = "Single";
    public String HospitalNO = bs.b;
    public String Language = bs.b;
    public String PregnantTimes = bs.b;
    private String Section1 = "System Language";
    private String Section2 = "NewCaseInfo";

    public boolean SaveIni(String strFileName) {
        FileOperation.makeDirs(strFileName.substring(0, strFileName.lastIndexOf(CookieSpec.PATH_DELIM) + 1));
        IniFile ini = new BasicIniFile();
        File file2 = new File(strFileName);
        if (!file2.exists()) {
            try {
                file2.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }
        IniFileWriter iniFileWriter = new IniFileWriter(ini, file2);
        ini.addSection(this.Section1).addItem(UserInfoDao.Language).setValue(this.Language);
        IniSection dataSection2 = ini.addSection(this.Section2);
        dataSection2.addItem("CaseName").setValue(this.CaseName);
        dataSection2.addItem("CaseAge").setValue(this.CaseAge);
        dataSection2.addItem("CaseHeight").setValue(this.CaseHeight);
        dataSection2.addItem("CaseWeight").setValue(this.CaseWeight);
        dataSection2.addItem("Children").setValue(this.Children);
        dataSection2.addItem("PregnantTimes").setValue(this.PregnantTimes);
        dataSection2.addItem("BirthTimes").setValue(this.BirthTimes);
        dataSection2.addItem("ChildMonths").setValue(this.ChildMonths);
        dataSection2.addItem("CheckTime").setValue(this.CheckTime);
        dataSection2.addItem("CaseRemark").setValue(this.CaseRemark);
        dataSection2.addItem("CaseIDCard").setValue(this.CaseIDCard);
        dataSection2.addItem("HospitalNO").setValue(this.HospitalNO);
        dataSection2.addItem("CasePhone").setValue(this.CasePhone);
        dataSection2.addItem("CaseAddress").setValue(this.CaseAddress);
        try {
            iniFileWriter.write();
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        return true;
    }

    private Map<String, String> getMap() {
        Map<String, String> _map = new HashMap<>();
        _map.put(parseUTFtoGBK("CaseName"), parseUTFtoGBK(this.CaseName));
        _map.put(parseUTFtoGBK("CaseAge"), parseUTFtoGBK(this.CaseAge));
        _map.put(parseUTFtoGBK("CaseHeight"), parseUTFtoGBK(this.CaseHeight));
        _map.put(parseUTFtoGBK("CaseWeight"), parseUTFtoGBK(this.CaseWeight));
        _map.put(parseUTFtoGBK("Children"), parseUTFtoGBK(this.Children));
        _map.put(parseUTFtoGBK("PregnantTimes"), parseUTFtoGBK(this.PregnantTimes));
        _map.put(parseUTFtoGBK("BirthTimes"), parseUTFtoGBK(this.BirthTimes));
        _map.put(parseUTFtoGBK("ChildMonths"), parseUTFtoGBK(this.ChildMonths));
        _map.put(parseUTFtoGBK("CheckTime"), parseUTFtoGBK(this.CheckTime));
        _map.put(parseUTFtoGBK("CaseRemark"), parseUTFtoGBK(this.CaseRemark));
        _map.put(parseUTFtoGBK("CaseIDCard"), parseUTFtoGBK(this.CaseIDCard));
        _map.put(parseUTFtoGBK("Hospital NO"), parseUTFtoGBK(this.HospitalNO));
        _map.put(parseUTFtoGBK("CasePhone"), parseUTFtoGBK(this.CasePhone));
        _map.put(parseUTFtoGBK("CaseAddress"), parseUTFtoGBK(this.CaseAddress));
        return _map;
    }

    public void write(String filePath, String fileName) throws IOException {
        IniReader.saveToIniFile(String.valueOf(filePath) + CookieSpec.PATH_DELIM + fileName, getMap());
    }

    private String parseUTFtoGBK(String str) {
        try {
            return new String(str.getBytes(CPushMessageCodec.UTF8), CPushMessageCodec.GBK);
        } catch (Exception e) {
            e.printStackTrace();
            try {
                return new String(str.getBytes(CPushMessageCodec.UTF8), CPushMessageCodec.GBK);
            } catch (Exception e1) {
                e1.printStackTrace();
                return str;
            }
        }
    }
}
