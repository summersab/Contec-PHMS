package com.contec.phms.ajaxcallback;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import com.contec.phms.util.CLog;
import com.contec.phms.util.Constants;
import net.tsz.afinal.http.AjaxCallBack;

public class AjaxCallBack_submitmodfiyUserinfo extends AjaxCallBack<String> {
    private String TAG = "lz";
    private Handler mhandler;

    public AjaxCallBack_submitmodfiyUserinfo(Context context, Handler mhandler2) {
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
            Message _msg = new Message();
            _msg.what = Constants.MODIFY_USERINFO_SUCCESS;
            this.mhandler.sendMessage(_msg);
        } else if (_result.equals(Integer.valueOf(Constants.MODIFY_USERINFO_DBEROR))) {
            Message _msg2 = new Message();
            _msg2.what = Constants.MODIFY_USERINFO_DBEROR;
            this.mhandler.sendMessage(_msg2);
        } else if (_result.equals(Integer.valueOf(Constants.MODIFY_USERINFO_ERROR))) {
            Message _msg3 = new Message();
            _msg3.what = Constants.MODIFY_USERINFO_ERROR;
            this.mhandler.sendMessage(_msg3);
        }
    }

    public void onFailure(Throwable t, String strMsg) {
        super.onFailure(t, 0, strMsg);
        if (strMsg != null) {
            CLog.i(this.TAG, "onFailure   " + strMsg.toString());
        }
    }
}
