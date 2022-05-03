package com.contec.phms.device.abpm50w;

import com.contec.phms.util.DeviceNameUtils;
import java.util.ArrayList;
import java.util.Date;

public class DeviceData extends com.contec.phms.device.template.DeviceData {
    private static final long serialVersionUID = 1;
    public P_Info mInfo;
    public ArrayList<P_Info> mInfos = new ArrayList<>();
    private int mType;
    public int m_nDia;
    public int m_nHR;
    public int m_nMap;
    public int m_nSys;
    public int m_nTC;
    public int m_state;

    public DeviceData(byte[] pack) {
        super(pack);
    }

    public ArrayList<P_Info> getM_savedata() {
        return this.mInfos;
    }

    public void setM_savedata() {
        this.mInfo = new P_Info(this.m_nSys, this.m_nDia, this.m_nHR, this.m_nMap, this.mDate[0], this.mDate[1], this.mDate[2], this.mDate[3], this.mDate[4], this.m_nTC);
        this.mInfos.add(this.mInfo);
    }

    public int getmType() {
        return this.mType;
    }

    public void setmType(int mDataType) {
        this.mType = mDataType;
    }

    public void initInfo() {
        this.mUploadType = "case";
        this.mDataType = DeviceNameUtils.ABPM50W;
    }

    public void setSaveDate() {
        this.mSaveDate = new Date(this.mDate[0], this.mDate[1], this.mDate[2], this.mDate[3], this.mDate[4]);
    }
}
