package com.contec.phms.upload.trend;

import com.contec.phms.App_phms;
import com.contec.phms.device.template.DeviceData;
import java.util.ArrayList;
import org.apache.commons.httpclient.methods.PostMethod;

public abstract class Trend {
    private final App_phms MAPP_PHMS = App_phms.getInstance();
    public String mContent;
    public ArrayList<DeviceData> mDatas = new ArrayList<>();
    public PostMethod mMethod;
    public String mPHPSession = this.MAPP_PHMS.mUserInfo.mPHPSession;
    public String mPassword = App_phms.getInstance().GetUserInfoPASSWORD();
    public String mUserName = App_phms.getInstance().GetUserInfoNAME();

    public abstract String makeContect();

    public String getContent() {
        String makeContect = makeContect();
        this.mContent = makeContect;
        return makeContect;
    }
}
