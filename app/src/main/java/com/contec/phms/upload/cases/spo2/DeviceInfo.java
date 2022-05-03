package com.contec.phms.upload.cases.spo2;

import java.io.OutputStream;
import java.io.Serializable;
import u.aly.bs;

public class DeviceInfo implements Serializable {
    public static final int e_strlen = 32;
    private static final long serialVersionUID = 1;
    public long m_PIType;
    public String m_deviceCompany = bs.b;
    public String m_deviceID = bs.b;
    public String m_deviceType = bs.b;
    public long m_saveDataType;

    public long SaveDataType() {
        return this.m_saveDataType;
    }

    public void SaveDataType(long N) {
        this.m_saveDataType = N;
    }

    public long PIType() {
        return this.m_PIType;
    }

    public void PIType(long N) {
        this.m_PIType = N;
    }

    public String DeviceType() {
        return this.m_deviceType;
    }

    public void DeviceType(String p) {
        this.m_deviceType = p;
    }

    public String DeviceID() {
        return this.m_deviceID;
    }

    public void DeviceID(String p) {
        this.m_deviceID = p;
    }

    public String DeviceCompany() {
        return this.m_deviceCompany;
    }

    public void DeviceCompany(String p) {
        this.m_deviceCompany = p;
    }

    public boolean writeToFile(OutputStream out) {
        Util.writeLong(out, this.m_PIType);
        Util.writeLong(out, this.m_saveDataType);
        Util.writeString(out, this.m_deviceType, 32);
        Util.writeString(out, this.m_deviceID, 32);
        Util.writeString(out, this.m_deviceCompany, 32);
        return true;
    }
}
