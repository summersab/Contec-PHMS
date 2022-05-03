package com.contec.phms.upload.cases.pm85;

import com.alibaba.cchannel.push.receiver.CPushMessageCodec;
import com.contec.phms.db.LoginUserDao;
import com.contec.phms.util.Constants;
import com.zxing.android.decoding.Intents;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.httpclient.cookie.CookieSpec;
import u.aly.bs;

public class IniInfoPm85 {
    public int ACG = 0;
    public String Address = bs.b;
    public String CASEID = bs.b;
    public String CHAN = bs.b;
    public String CHIFE = bs.b;
    public String DATETIME = bs.b;
    public String EvtType = bs.b;
    public int HEIGHT = 0;
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
    public int SECOND = 0;
    public int SEX = 0;
    public String SID = bs.b;
    public String Section1 = IniReader.SESSION1;
    public String Section2 = IniReader.SESSION2;
    public String TYPE = Constants.PM85_NAME;
    public String UPLOADER = bs.b;
    public int WEIGHT = 0;

    public Map<String, String> getMap() throws IOException {
        Map<String, String> _map = new HashMap<>();
        _map.put(parseUTFtoGBK("[GENERAL]"), parseUTFtoGBK(this.Section1));
        _map.put(parseUTFtoGBK("HOSPITAL"), parseUTFtoGBK(this.HOSPITAL));
        _map.put(parseUTFtoGBK("ID"), parseUTFtoGBK(this.ID));
        _map.put(parseUTFtoGBK("UPLOADER"), parseUTFtoGBK(this.UPLOADER));
        _map.put(parseUTFtoGBK("[PATIENT]"), parseUTFtoGBK(this.Section1));
        _map.put(parseUTFtoGBK(LoginUserDao.SID), parseUTFtoGBK(this.SID));
        _map.put(parseUTFtoGBK("NAME"), parseUTFtoGBK(this.NAME));
        _map.put(parseUTFtoGBK("CASEID"), parseUTFtoGBK(this.CASEID));
        _map.put(parseUTFtoGBK("PART"), parseUTFtoGBK(this.PART));
        _map.put(parseUTFtoGBK("DATETIME"), parseUTFtoGBK(this.DATETIME));
        _map.put(parseUTFtoGBK(Intents.WifiConnect.TYPE), parseUTFtoGBK(this.TYPE));
        _map.put(parseUTFtoGBK("CHAN"), parseUTFtoGBK(this.CHAN));
        _map.put("SEX", new StringBuilder().append(this.SEX).toString());
        _map.put("ACG", new StringBuilder().append(this.ACG).toString());
        _map.put("PACE", new StringBuilder().append(this.PACE & 255).toString());
        _map.put("HEIGHT", new StringBuilder().append(this.HEIGHT).toString());
        _map.put("WEIGHT", new StringBuilder().append(this.WEIGHT).toString());
        _map.put("SECOND", new StringBuilder().append(this.SECOND).toString());
        _map.put(parseUTFtoGBK("LANGUAGE"), parseUTFtoGBK(this.LANGUAGE));
        _map.put(parseUTFtoGBK("CHIFE"), parseUTFtoGBK(this.CHIFE));
        _map.put(parseUTFtoGBK("Address"), parseUTFtoGBK(this.Address));
        _map.put(parseUTFtoGBK("PHONE"), parseUTFtoGBK(this.PHONE));
        _map.put(parseUTFtoGBK("EvtType"), parseUTFtoGBK(this.EvtType));
        _map.put(parseUTFtoGBK("IDCard"), parseUTFtoGBK(this.IDCard));
        _map.put(parseUTFtoGBK("PAST"), parseUTFtoGBK(this.PAST));
        _map.put(parseUTFtoGBK("PRESENT"), parseUTFtoGBK(this.PRESENT));
        return _map;
    }

    public void write(String filePath, String fileName) throws IOException {
        IniEditor ini = new IniEditor();
        ini.addSection(this.Section1);
        ini.set(this.Section1, parseUTFtoGBK("HOSPITAL"), parseUTFtoGBK(this.HOSPITAL));
        ini.set(this.Section1, parseUTFtoGBK("ID"), parseUTFtoGBK(this.ID));
        ini.set(this.Section1, parseUTFtoGBK("UPLOADER"), parseUTFtoGBK(this.UPLOADER));
        ini.addSection(this.Section2);
        ini.set(this.Section2, parseUTFtoGBK(LoginUserDao.SID), parseUTFtoGBK(this.SID));
        ini.set(this.Section2, parseUTFtoGBK("NAME"), parseUTFtoGBK(this.NAME));
        ini.set(this.Section2, parseUTFtoGBK("CASEID"), parseUTFtoGBK(this.CASEID));
        ini.set(this.Section2, parseUTFtoGBK("PART"), parseUTFtoGBK(this.PART));
        ini.set(this.Section2, parseUTFtoGBK("DATETIME"), parseUTFtoGBK(this.DATETIME));
        ini.set(this.Section2, parseUTFtoGBK(Intents.WifiConnect.TYPE), parseUTFtoGBK(this.TYPE));
        ini.set(this.Section2, parseUTFtoGBK("CHAN"), parseUTFtoGBK(this.CHAN));
        ini.set(this.Section2, "SEX", new StringBuilder().append(this.SEX).toString());
        ini.set(this.Section2, "ACG", new StringBuilder().append(this.ACG).toString());
        ini.set(this.Section2, "PACE", new StringBuilder().append(this.PACE & 255).toString());
        ini.set(this.Section2, "HEIGHT", new StringBuilder().append(this.HEIGHT).toString());
        ini.set(this.Section2, "WEIGHT", new StringBuilder().append(this.WEIGHT).toString());
        ini.set(this.Section2, "SECOND", new StringBuilder().append(this.SECOND).toString());
        ini.set(this.Section2, parseUTFtoGBK("LANGUAGE"), parseUTFtoGBK(this.LANGUAGE));
        ini.set(this.Section2, parseUTFtoGBK("CHIFE"), parseUTFtoGBK(this.CHIFE));
        ini.set(this.Section2, parseUTFtoGBK("Address"), parseUTFtoGBK(this.Address));
        ini.set(this.Section2, parseUTFtoGBK("PHONE"), parseUTFtoGBK(this.PHONE));
        ini.set(this.Section2, parseUTFtoGBK("EvtType"), parseUTFtoGBK(this.EvtType));
        ini.set(this.Section2, parseUTFtoGBK("IDCard"), parseUTFtoGBK(this.IDCard));
        ini.set(this.Section2, parseUTFtoGBK("PAST"), parseUTFtoGBK(this.PAST));
        ini.set(this.Section2, parseUTFtoGBK("PRESENT"), parseUTFtoGBK(this.PRESENT));
        File _file = new File(filePath);
        if (!_file.exists()) {
            _file.mkdirs();
        }
        ini.save(_file + CookieSpec.PATH_DELIM + fileName);
    }

    private String parseUTFtoGBK(String str) {
        try {
            return new String(str.getBytes(CPushMessageCodec.UTF8), CPushMessageCodec.GBK);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            try {
                return new String(str.getBytes(CPushMessageCodec.UTF8), CPushMessageCodec.GBK);
            } catch (UnsupportedEncodingException e1) {
                e1.printStackTrace();
                return str;
            }
        }
    }
}
