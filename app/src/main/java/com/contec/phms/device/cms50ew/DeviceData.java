package com.contec.phms.device.cms50ew;

import com.contec.phms.db.localdata.Spo2DataDao;
import com.contec.phms.upload.cases.spo2.DeviceInfo;
import com.contec.phms.upload.cases.spo2.SaveTime_T;
import com.contec.phms.upload.cases.spo2.UserInfo;
import java.io.Serializable;
import java.util.Date;
import u.aly.bs;

public class DeviceData extends com.contec.phms.device.template.DeviceData implements Serializable {
    private static final long serialVersionUID = 1;
    public String mAddress = bs.b;
    public long mAge = 0;
    public int mDataLen = 0;
    public long mHeight = 0;
    public int mPI = 1;
    public String mPhone = bs.b;
    public long mSex;
    public String mUserName;
    public long mWeight = 0;

    public DeviceData(byte[] pack) {
        super(pack);
    }

    public void clear() {
        this.mUserName = bs.b;
        this.mAge = 0;
        this.mHeight = 0;
        this.mWeight = 0;
        this.mSex = 0;
        this.mPI = 1;
        this.mDataLen = 0;
        this.mPhone = bs.b;
        this.mAddress = bs.b;
    }

    public void makeInfos() {
        makeUserInfo();
        makeDeviceInfo();
        makeSaveTime();
    }

    public void makeUserInfo() {
        this.mUserInfo = new UserInfo();
        this.mUserInfo.Name(this.mUserName);
        this.mUserInfo.Age(this.mAge);
        this.mUserInfo.Height(this.mHeight);
        this.mUserInfo.Weight(this.mWeight);
        this.mUserInfo.Sex(this.mSex);
        this.mUserInfo.Comment(this.mAddress);
        this.mUserInfo.Nationa(this.mPhone);
    }

    public void makeDeviceInfo() {
        this.mDeviceInfo = new DeviceInfo();
        this.mDeviceInfo.PIType((long) this.mPI);
    }

    public void makeSaveTime() {
        this.mSaveTime = new SaveTime_T();
        this.mSaveTime.SetTime((long) this.mDate[0], (long) this.mDate[1], (long) this.mDate[2], (long) this.mDate[3], (long) this.mDate[4], (long) this.mDate[5]);
        setSaveDate();
    }

    public void initInfo() {
        this.mUploadType = "trend";
        this.mDataType = Spo2DataDao.SPO2;
    }

    public void setSaveDate() {
        this.mSaveDate = new Date(this.mDate[0], this.mDate[1], this.mDate[2], this.mDate[3], this.mDate[4], this.mDate[5]);
    }
}
