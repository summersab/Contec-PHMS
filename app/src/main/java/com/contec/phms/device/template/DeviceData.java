package com.contec.phms.device.template;

import com.contec.phms.manager.device.ServiceBean;
import com.contec.phms.upload.cases.spo2.DeviceInfo;
import com.contec.phms.upload.cases.spo2.SaveTime_T;
import com.contec.phms.upload.cases.spo2.UserInfo;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import u.aly.bs;

public class DeviceData implements Serializable {
    private static final long serialVersionUID = 1;
    public String Uniquenes;
    public boolean is50KContinuityOxygenData;
    public boolean mCheckTimeIllegal;
    public double mData;
    public ArrayList<Object> mDataList;
    public String mDataType;
    public int[] mDate;
    public DeviceInfo mDeviceInfo;
    public String mDeviceType;
    public byte[] mFileInfo;
    public String mFileName;
    public String mFilePath;
    public byte[] mPack;
    public Date mSaveDate;
    public byte[] mSaveInfo;
    public SaveTime_T mSaveTime;
    public String mUploadType;
    public UserInfo mUserInfo;
    public int upLoadTimes;
    public byte[] TrendData;
    public byte[] CaseData;
    public String mUserName;
    public long mAge;
    public long mHeight;
    public long mWeight;
    public long mSex;
    public String mAddress;
    public String mPhone;
    public int mPI;

    //    public abstract void initInfo();
    public void initInfo() {}

//    public abstract void setSaveDate();
    public void setSaveDate() {}

    public int getUpLoadTimes() {
        return this.upLoadTimes;
    }

    public void setUpLoadTimes(int upLoadTimes2) {
        this.upLoadTimes = upLoadTimes2;
    }

    public String getUniquenes() {
        return this.Uniquenes;
    }

    public void setUniquenes(String pUniquenes) {
        this.Uniquenes = pUniquenes;
    }

    public String getmUploadType() {
        return this.mUploadType;
    }

    public void setmUploadType(String mUploadType2) {
        this.mUploadType = mUploadType2;
    }

    public String getmDataType() {
        return this.mDataType;
    }

    public void setmDataType(String mDataType2) {
        this.mDataType = mDataType2;
    }

    public DeviceData(byte[] pack) {
        this.upLoadTimes = 0;
        this.mCheckTimeIllegal = false;
        this.is50KContinuityOxygenData = false;
        this.mSaveDate = new Date();
        this.mDeviceType = ServiceBean.getInstance().getmDeviceName();
        this.mPack = pack;
        this.mDataList = new ArrayList<>();
        initInfo();
    }

    public DeviceData() {
        this.upLoadTimes = 0;
        this.mCheckTimeIllegal = false;
        this.is50KContinuityOxygenData = false;
        this.mSaveDate = new Date();
        this.mDeviceType = ServiceBean.getInstance().getmDeviceName();
        this.mDataList = new ArrayList<>();
        initInfo();
    }

    public void addData(byte[] pack) {
        this.mDataList.add(pack);
    }

    public String dateToString() {
        String _temp = bs.b;
        if (this.mDate == null) {
            return new SimpleDateFormat("yyyyMMddHHmmss").format(this.mSaveDate);
        }
        for (int i = 0; i < this.mDate.length; i++) {
            _temp = String.valueOf(_temp) + byte2String((byte) this.mDate[i]);
        }
        return _temp;
    }

    public String byte2String(byte pack) {
        if (pack < 10) {
            return "0" + pack;
        }
        return new StringBuilder().append(pack).toString();
    }

    public String getFileName() {
        this.mFileName = String.valueOf(this.mDataType) + "." + this.mUploadType + "." + dateToString() + "." + this.Uniquenes;
        return this.mFileName;
    }

    public boolean isIs50KContinuityOxygenData() {
        return this.is50KContinuityOxygenData;
    }

    public void setIs50KContinuityOxygenData(boolean is50kContinuityOxygenData) {
        this.is50KContinuityOxygenData = is50kContinuityOxygenData;
    }

    public String toSaveString() {
        StringBuffer _temp = new StringBuffer();
        _temp.append(this.mDeviceType);
        _temp.append(":");
        _temp.append(dateToString());
        return new String(_temp);
    }

    public void makeInfos() {
    }

    public void makeSaveTime() {
    }
}
