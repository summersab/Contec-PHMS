package com.contec.phms.ajaxcallback;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import com.alibaba.cchannel.CloudChannelConstants;
import com.contec.phms.util.CLog;
import com.contec.phms.util.Constants;
import java.io.ByteArrayInputStream;
import javax.xml.parsers.DocumentBuilderFactory;
import net.tsz.afinal.http.AjaxCallBack;

public class AjaxCallBack_login_uploadpoint extends AjaxCallBack<String> {
    private String TAG = AjaxCallBack_login_uploadpoint.class.getSimpleName();
    public String mPasswd;
    private final int mUploadPointloginsuccess = 100000;
    public String mUserID;
    private Handler mhandler;
    private final int mqueryuserinfoneterror = Constants.mqueryuserinfoneterror;
    private final int mqueryuserinfotimeout = Constants.mqueryuserinfotimeout;

    public AjaxCallBack_login_uploadpoint(Context context, Handler mhandler2) {
        this.mhandler = mhandler2;
    }

    public boolean isProgress() {
        return super.isProgress();
    }

    public int getRate() {
        return super.getRate();
    }

    public AjaxCallBack<String> progress(boolean progress, int rate) {
        CLog.i(this.TAG, "progress--" + progress + "--rate--" + rate);
        return super.progress(progress, rate);
    }

    public void onStart() {
        super.onStart();
        CLog.i(this.TAG, "start--------");
    }

    public void onLoading(long count, long current) {
        super.onLoading(count, current);
        CLog.i(this.TAG, "onLoading   " + count + "-cout");
    }

    public void onSuccess(String t) {
        super.onSuccess(t);
        dealLoginReturn(t);
    }

    private void dealLoginReturn(String t) {
        CLog.i(this.TAG, "onSuccess  " + t.toString());
        String _result = t.substring(34, 40);
        CLog.i(this.TAG, "onSuccess 4 " + _result);
        if (_result.equals(Constants.SUCCESS)) {
            String _sessionid = insertLoginInfoToDB(t.substring(42, t.length()));
            Message _msg = new Message();
            _msg.what = 100000;
            _msg.obj = _sessionid;
            this.mhandler.sendMessage(_msg);
            return;
        }
        Message _msg2 = new Message();
        _msg2.what = Constants.mqueryuserinfotimeout;
        this.mhandler.sendMessage(_msg2);
    }

    public void onFailure(Throwable t, String strMsg) {
        CLog.i(this.TAG, "Login failed  onFailure " + strMsg);
        super.onFailure(t, 0, strMsg);
        Message _msg = new Message();
        _msg.what = Constants.mqueryuserinfoneterror;
        this.mhandler.sendMessage(_msg);
    }

    private String insertLoginInfoToDB(String pRespones) {
        String mSID = new String();
        DocumentBuilderFactory _factory = DocumentBuilderFactory.newInstance();
        try {
            mSID = _factory.newDocumentBuilder().parse(new ByteArrayInputStream(pRespones.getBytes())).getDocumentElement().getElementsByTagName(CloudChannelConstants.SID).item(0).getTextContent();
            CLog.e("UserInfoFromServer", "mSID: " + mSID);
            return mSID;
        } catch (Exception e) {
            e.printStackTrace();
            return mSID;
        }
    }
}
