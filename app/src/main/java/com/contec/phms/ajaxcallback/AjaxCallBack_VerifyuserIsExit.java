package com.contec.phms.ajaxcallback;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import com.contec.phms.util.CLog;
import com.contec.phms.util.Constants;
import net.tsz.afinal.http.AjaxCallBack;

public class AjaxCallBack_VerifyuserIsExit extends AjaxCallBack<String> {
    private String TAG = "AjaxCallBack_registSendVerifyCode";
    private Handler mhandler;
    private final int mneterror = 1000102;
    private final int mnettimeout = 1000101;
    private final int mverifyuseridexit = 1068001;
    private final int mverifyuseridservererror = 106801;
    private final int mverifyuseridsuccess = 106800;

    public AjaxCallBack_VerifyuserIsExit(Context context, Handler mhandler2) {
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
        String _result = t.substring(34, 40);
        CLog.i("less", "onSuccess 4 " + _result);
        CLog.i("less", "t " + t);
        String _messagebodytype = t.substring(40, 42);
        CLog.i("less", "_messagebodytype " + _messagebodytype);
        if (_result.equals(Constants.SUCCESS) && _messagebodytype.equals("10")) {
            Message _msg = new Message();
            _msg.what = 106800;
            this.mhandler.sendMessage(_msg);
        } else if (_result.equals(Constants.SUCCESS) && _messagebodytype.equals("12")) {
            Message _msg2 = new Message();
            _msg2.what = 1068001;
            this.mhandler.sendMessage(_msg2);
        } else if (_result.equals(106801)) {
            Message _msg3 = new Message();
            _msg3.what = 106801;
            this.mhandler.sendMessage(_msg3);
        } else {
            Message _msg4 = new Message();
            _msg4.what = 1000101;
            this.mhandler.sendMessage(_msg4);
        }
    }

    public void onFailure(Throwable t, String strMsg) {
        super.onFailure(t, 0, strMsg);
        Message _msg = new Message();
        _msg.what = 1000102;
        this.mhandler.sendMessage(_msg);
    }
}
