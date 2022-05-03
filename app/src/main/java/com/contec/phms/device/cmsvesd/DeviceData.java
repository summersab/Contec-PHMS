package com.contec.phms.device.cmsvesd;

import com.contec.phms.db.localdata.Spo2DataDao;
import com.contec.phms.upload.cases.spo2.DeviceInfo;
import com.contec.phms.upload.cases.spo2.SaveTime_T;
import com.contec.phms.upload.cases.spo2.SpO2PulsePack;
import com.contec.phms.upload.cases.spo2.UserInfo;
import java.util.Date;

public class DeviceData extends com.contec.phms.device.template.DeviceData {
    private static final long serialVersionUID = 1;
    public int mFileLen;
    public int mInfoLen;
    public int mType;

    public DeviceData(byte[] pack) {
        super(pack);
        this.mDate = new int[6];
    }

    public void makeInfos() {
        makeUserInfo();
        makeDeviceInfo();
        makeSaveTime();
    }

    public void makeUserInfo() {
        this.mUserInfo = new UserInfo();
        this.mUserInfo.Name("leequer");
        this.mUserInfo.Age(25);
        this.mUserInfo.Height(1700);
        this.mUserInfo.Weight(70000);
    }

    public void makeDeviceInfo() {
        this.mDeviceInfo = new DeviceInfo();
        this.mDeviceInfo.PIType(serialVersionUID);
    }

    public void makeSaveTime() {
        this.mSaveTime = new SaveTime_T();
        this.mSaveTime.SetTime((long) this.mDate[0], (long) this.mDate[1], (long) this.mDate[2], (long) this.mDate[3], (long) this.mDate[4], (long) this.mDate[5]);
        setSaveDate();
    }

    public void addData(byte[] pack) {
        if (pack[0] == -1 && pack[1] == -1) {
            this.mDataList.add(new SpO2PulsePack(new byte[]{pack[5], pack[7]}));
            return;
        }
        setFileInfo(pack);
    }

    public void setFileInfo(byte[] pack) {
        this.mFileInfo = pack;
        for (int i = 0; i < this.mDate.length; i++) {
            this.mDate[i] = this.mFileInfo[i + 3];
        }
        if (this.mDate[0] < 1900) {
            int[] iArr = this.mDate;
            iArr[0] = iArr[0] + 2000;
        }
        this.mSaveInfo = pack;
    }

    public void initInfo() {
        this.mUploadType = "case";
        this.mDataType = Spo2DataDao.SPO2;
    }

    public void setSaveDate() {
        this.mSaveDate = new Date(this.mDate[0], this.mDate[1], this.mDate[2], this.mDate[3], this.mDate[4], this.mDate[5]);
    }
}
