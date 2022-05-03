package com.contec.phms.ajaxcallback;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import com.contec.phms.util.CLog;
import com.contec.phms.util.Constants;
import net.tsz.afinal.http.AjaxCallBack;

public class AjaxCallBack_registNewUser extends AjaxCallBack<String> {
    private String TAG = AjaxCallBack_registNewUser.class.getSimpleName();
    private String mPassword;
    private String mThirdcode;
    private final int mhandbeenregistUser = 106902;
    private Handler mhandler;
    private final int mnettimeout = 1000101;
    private final int mregistUserdberror = 106905;
    private final int mregistUsererror = 106910;
    private final int mregistUsersmserror = 106904;
    private final int mregisterror = 100002;
    private final int mregistnewUserargserror = 106901;
    private final int mregistnewUsersuccess = 106900;

    public AjaxCallBack_registNewUser(Context context, Handler mhandler2) {
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
        CLog.i(this.TAG, "注册....  msg " + t);
        String _result = t.substring(34, 40);
        CLog.i(this.TAG, "注册....  onSuccess 4 " + _result);
        if (_result.equals(Constants.SUCCESS)) {
            Message _msg = new Message();
            _msg.obj = t.substring(42, t.length());
            _msg.what = 106900;
            this.mhandler.sendMessage(_msg);
        } else if (_result.equals("106901")) {
            Message _msg2 = new Message();
            _msg2.what = 106901;
            this.mhandler.sendMessage(_msg2);
        } else if (_result.equals("106902")) {
            Message _msg3 = new Message();
            _msg3.what = 106902;
            this.mhandler.sendMessage(_msg3);
        } else if (_result.equals("106904")) {
            Message _msg4 = new Message();
            _msg4.what = 106904;
            this.mhandler.sendMessage(_msg4);
        } else if (_result.equals(Constants.INVALID_SID)) {
            CLog.i("less", "regist  100002");
            Message _msg5 = new Message();
            _msg5.what = 100002;
            this.mhandler.sendMessage(_msg5);
        } else if (_result.equals("106905")) {
            Message _msg6 = new Message();
            _msg6.what = 106905;
            this.mhandler.sendMessage(_msg6);
        } else {
            Message _msg7 = new Message();
            _msg7.what = 1000101;
            this.mhandler.sendMessage(_msg7);
        }
    }

    public void onFailure(Throwable t, String strMsg) {
        super.onFailure(t, 0, strMsg);
        if (strMsg != null) {
            CLog.i(this.TAG, "onFailure   " + strMsg.toString());
        } else {
            CLog.i(this.TAG, "onFailure  出差返回的消息都是null ");
        }
        Message _msg = new Message();
        _msg.what = 106910;
        this.mhandler.sendMessage(_msg);
    }
}
