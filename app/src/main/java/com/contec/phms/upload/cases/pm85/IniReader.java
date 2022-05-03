package com.contec.phms.upload.cases.pm85;

import com.alibaba.cchannel.push.receiver.CPushMessageCodec;
import com.j256.ormlite.stmt.query.SimpleComparison;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;
import org.apache.commons.httpclient.cookie.CookieSpec;

public class IniReader {
    public static final String SESSION1 = "GENERAL";
    public static final String SESSION2 = "PATIENT";

    public static boolean saveToIniFile(String iniFilePath, Map<String, String> pIni) {
        try {
            File _fileTemp = new File(iniFilePath.substring(0, iniFilePath.lastIndexOf(CookieSpec.PATH_DELIM)));
            if (!_fileTemp.exists()) {
                _fileTemp.mkdirs();
            }
            FileOutputStream fo = new FileOutputStream(iniFilePath);
            for (Map.Entry<String, String> entry : pIni.entrySet()) {
                fo.write((String.valueOf(entry.getKey()) + SimpleComparison.EQUAL_TO_OPERATION + entry.getValue() + "\n").getBytes());
            }
            fo.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return true;
        }
    }

    public static Properties getIni(String iniFilePath) {
        Properties ini = new Properties();
        try {
            if (!new File(iniFilePath).exists()) {
                return null;
            }
            ini.load(new FileInputStream(iniFilePath));
            return ini;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getValue(String iniFilePath, String key) {
        Properties ini = new Properties();
        try {
            if (!new File(iniFilePath).exists()) {
                return null;
            }
            ini.load(new FileInputStream(iniFilePath));
            if (ini.containsKey(key)) {
                return new String(ini.get(key).toString().getBytes("ISO-8859-1"), CPushMessageCodec.UTF8);
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void upDate(File iniFile, String session, Map<String, String> map) throws IOException {
        IniEditor ini = new IniEditor();
        if (iniFile.exists()) {
            ini.load(iniFile);
            for (Map.Entry<String, String> entry : map.entrySet()) {
                ini.set(session, entry.getKey(), entry.getValue());
            }
            ini.save(iniFile);
        }
    }

    public static void upDate(File iniFile, String session, String key, String value) throws IOException {
        IniEditor ini = new IniEditor();
        if (iniFile.exists()) {
            ini.load(iniFile);
            ini.set(session, key, value);
            ini.save(iniFile);
        }
    }

    public static String select(File iniFile, String session, String key) throws IOException {
        IniEditor ini = new IniEditor();
        if (!iniFile.exists()) {
            return null;
        }
        ini.load(iniFile);
        return ini.get(session, key);
    }
}
