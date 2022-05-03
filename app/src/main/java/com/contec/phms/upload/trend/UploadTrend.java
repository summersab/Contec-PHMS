package com.contec.phms.upload.trend;

import com.contec.phms.App_phms;
import com.contec.phms.manager.device.DeviceManager;
import com.contec.phms.upload.UploadService;
import com.contec.phms.util.CLog;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethodBase;
import cn.com.contec.net.util.Constants_jar;

public class UploadTrend {
    public final String TAG = "UploadTrend";
    public HttpClient mHttpClient = App_phms.getInstance().mUserInfo.mHttpClient;
    public HttpMethodBase mHttpMethod;

    public UploadTrend(HttpMethodBase httpMethod) {
        this.mHttpClient.getHttpConnectionManager().getParams().setConnectionTimeout(Constants_jar.PROCESS_NEED_DOWN_CASE_XML_END);
        this.mHttpMethod = httpMethod;
    }

    public boolean execute() {
        return upload();
    }

    public boolean upload() {
        boolean _result = false;
        try {
            this.mHttpClient.executeMethod(this.mHttpMethod);
            if (this.mHttpMethod.getStatusCode() == 200) {
                String _resultStr = this.mHttpMethod.getResponseBodyAsString();
                if (_resultStr.contains("error")) {
                    _result = false;
                    UploadService.mFaildRe = 0;
                    if (_resultStr.equals("<div style='display:none'>aaaloginokdegree error") || _resultStr.equals("ERR_DEGREE_EXCEED_LIMIT")) {
                        UploadService.mFaildRe = 1;
                    }
                } else {
                    _result = _resultStr.contains("HTTP_SUCCESS");
                }
                CLog.i("UploadTrend", _resultStr);
            } else if (DeviceManager.mDeviceBeanList != null) {
                DeviceManager.mDeviceBeanList.mState = 9;
                DeviceManager.m_DeviceBean.mState = 9;
                DeviceManager.m_DeviceBean.mProgress = 0;
                DeviceManager.m_DeviceBean.mFailedReasons = 0;
                App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(DeviceManager.m_DeviceBean);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return _result;
    }
}
