package com.contec.phms.device.abpm50w;

import java.io.Serializable;

public class P_Info implements Serializable {
    private static final long serialVersionUID = 1;
    public int m_iDay;
    public int m_iHour;
    public int m_iMinute;
    public int m_iMonth;
    public int m_iYear;
    public int m_nDia;
    public int m_nHR;
    public int m_nMap;
    public int m_nSys;
    public int m_ntc;

    public P_Info(int m_nSys2, int m_nDia2, int m_nHR2, int m_nMap2, int m_iYear2, int m_iMonth2, int m_iDay2, int m_iHour2, int m_iMinute2, int m_ntc2) {
        this.m_nSys = m_nSys2;
        this.m_nDia = m_nDia2;
        this.m_nHR = m_nHR2;
        this.m_nMap = m_nMap2;
        this.m_iYear = m_iYear2;
        this.m_iMonth = m_iMonth2;
        this.m_iDay = m_iDay2;
        this.m_iHour = m_iHour2;
        this.m_iMinute = m_iMinute2;
        this.m_ntc = m_ntc2;
    }

    public P_Info() {
    }
}
