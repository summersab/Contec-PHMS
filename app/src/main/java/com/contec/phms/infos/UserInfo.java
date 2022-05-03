package com.contec.phms.infos;

import org.apache.commons.httpclient.HttpClient;
import u.aly.bs;

public class UserInfo {
    public int mBluetoothState = 1;
    public HttpClient mHttpClient = new HttpClient();
    public String mLanguage = bs.b;
    public String mPHPSession = bs.b;
    public String mPassword = bs.b;
    public int mSearchInterval = 10;
    public String mSex = bs.b;
    public String mUserID = bs.b;
    public String mUserName = bs.b;

    public String toString() {
        return "mUserName: " + this.mUserID + " mPassword:" + this.mPassword + " mPHPSession: " + this.mPHPSession + "  mSex:" + this.mSex + " mDoctorName: " + this.mUserName + "  mBluetoothState:" + this.mBluetoothState + "  mLanguage:" + this.mLanguage + "  mSearchInterval:" + this.mSearchInterval;
    }
}
