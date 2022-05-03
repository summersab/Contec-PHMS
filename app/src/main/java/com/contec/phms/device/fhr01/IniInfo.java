package com.contec.phms.device.fhr01;

import com.alibaba.cchannel.push.receiver.CPushMessageCodec;
import com.contec.phms.upload.cases.pm85.IniReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.httpclient.cookie.CookieSpec;
import u.aly.bs;

public class IniInfo {
    public String BirthTimes = bs.b;
    public String CaseAddress = bs.b;
    public String CaseAge = bs.b;
    public String CaseHeight = bs.b;
    public String CaseIDCard = bs.b;
    public String CaseKey = bs.b;
    public String CaseName = "samho";
    public String CasePhone = bs.b;
    public String CaseRemark = bs.b;
    public String CaseSex = bs.b;
    public String CaseWeight = bs.b;
    public String CheckTime = bs.b;
    public String ChildMonths = bs.b;
    public String Children = bs.b;
    public String Custom4 = bs.b;
    public String Custom5 = bs.b;
    public String Custom6 = bs.b;
    public String PregnantTimes = bs.b;
    public String SmpTime = bs.b;

    private Map<String, String> getMap() {
        Map<String, String> _map = new HashMap<>();
        _map.put(parseUTFtoGBK("CaseName"), parseUTFtoGBK(this.CaseName));
        _map.put(parseUTFtoGBK("CaseSex"), parseUTFtoGBK(this.CaseSex));
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
        _map.put(parseUTFtoGBK("SmpTime"), parseUTFtoGBK(this.SmpTime));
        _map.put(parseUTFtoGBK("CasePhone"), parseUTFtoGBK(this.CasePhone));
        _map.put(parseUTFtoGBK("CaseAddress"), parseUTFtoGBK(this.CaseAddress));
        _map.put(parseUTFtoGBK("CaseKey"), parseUTFtoGBK(this.CaseKey));
        _map.put(parseUTFtoGBK("Custom4"), parseUTFtoGBK(this.Custom4));
        _map.put(parseUTFtoGBK("Custom5"), parseUTFtoGBK(this.Custom5));
        _map.put(parseUTFtoGBK("Custom6"), parseUTFtoGBK(this.Custom6));
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
