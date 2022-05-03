package com.contec.phms.ajaxcallback;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import com.contec.phms.infos.UserInfoFromServer;
import com.contec.phms.util.CLog;
import com.contec.phms.util.Constants;
import net.tsz.afinal.http.AjaxCallBack;

public class AjaxCallBack_login extends AjaxCallBack<String> {
    private String TAG = AjaxCallBack_login.class.getSimpleName();
    public String mPasswd;
    public String mUserID;
    private Handler mhandler;
    private final int mloginargserror = Constants.mloginargserror;
    private final int mlogindberror = Constants.mlogindberror;
    private final int mloginduserisnotexit = Constants.mloginduserisnotexit;
    private final int mlogindusernotcorrectpsw = Constants.mlogindusernotcorrectpsw;
    private final int mloginduserstopuser = Constants.mloginduserstopuser;
    private final int mqueryuserinfoneterror = Constants.mqueryuserinfoneterror;
    private final int mqueryuserinfotimeout = Constants.mqueryuserinfotimeout;

    public AjaxCallBack_login(Context context, Handler mhandler2) {
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
            new UserInfoFromServer(t.substring(42, t.length()), this.mUserID, this.mPasswd);
            Message _msg = new Message();
            _msg.what = 65536;
            this.mhandler.sendMessage(_msg);
        } else if (_result.equals("100201")) {
            Message _msg2 = new Message();
            _msg2.what = Constants.mloginargserror;
            this.mhandler.sendMessage(_msg2);
        } else if (_result.equals("100202")) {
            Message _msg3 = new Message();
            _msg3.what = Constants.mlogindberror;
            this.mhandler.sendMessage(_msg3);
        } else if (_result.equals("100203")) {
            Message _msg4 = new Message();
            _msg4.what = Constants.mloginduserisnotexit;
            this.mhandler.sendMessage(_msg4);
        } else if (_result.equals(Constants.LOGIN_FAILURE)) {
            Message _msg5 = new Message();
            _msg5.what = Constants.mlogindusernotcorrectpsw;
            this.mhandler.sendMessage(_msg5);
        } else if (_result.equals("100205")) {
            Message _msg6 = new Message();
            _msg6.what = Constants.mloginduserstopuser;
            this.mhandler.sendMessage(_msg6);
        } else {
            Message _msg7 = new Message();
            _msg7.what = Constants.mqueryuserinfotimeout;
            this.mhandler.sendMessage(_msg7);
        }
    }

    public void onFailure(Throwable t, String strMsg) {
        CLog.i(this.TAG, "Login failed  onFailure " + strMsg);
        super.onFailure(t, 0, strMsg);
        Message _msg = new Message();
        _msg.what = Constants.mqueryuserinfoneterror;
        this.mhandler.sendMessage(_msg);
    }
}
