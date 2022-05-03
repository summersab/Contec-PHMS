package com.contec.phms.ajaxcallback;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import com.contec.phms.util.CLog;
import com.contec.phms.util.Constants;
import net.tsz.afinal.http.AjaxCallBack;

public class AjaxCallBack_registSendVerifyCode extends AjaxCallBack<String> {
    private String TAG = "AjaxCallBack_registSendVerifyCode";
    private Handler mhandler;
    private final int verifysmssendagrserror = 107001;
    private final int verifysmssendcounterror = 107002;
    private final int verifysmssenderror = 107010;
    private final int verifysmssendintevertime = 107003;
    private final int verifysmssendsuccess = 107000;
    private final int verifysmssendsystemerror = 107004;

    public AjaxCallBack_registSendVerifyCode(Context context, Handler mhandler2) {
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
        CLog.i(this.TAG, "onLoading   " + count + "  -cout");
    }

    public void onSuccess(String t) {
        super.onSuccess(t);
        dealLoginReturn(t);
    }

    private void dealLoginReturn(String t) {
        String _result = t.substring(34, 40);
        CLog.i(this.TAG, "success  _result =  " + _result);
        if (_result.equals(Constants.SUCCESS)) {
            CLog.i(this.TAG, "success  _contentString =  " + t.substring(42, t.length()) + "  " + _result);
            Message _msg = new Message();
            _msg.what = 107000;
            this.mhandler.sendMessage(_msg);
        } else if (_result.equals("107001")) {
            Message _msg2 = new Message();
            _msg2.what = 107001;
            this.mhandler.sendMessage(_msg2);
        } else if (_result.equals("107002")) {
            Message _msg3 = new Message();
            _msg3.what = 107002;
            this.mhandler.sendMessage(_msg3);
        } else if (_result.equals("107003")) {
            Message _msg4 = new Message();
            _msg4.what = 107003;
            this.mhandler.sendMessage(_msg4);
        } else if (_result.equals("107004")) {
            Message _msg5 = new Message();
            _msg5.what = 107004;
            this.mhandler.sendMessage(_msg5);
        }
    }

    public void onFailure(Throwable t, String strMsg) {
        super.onFailure(t, 0, strMsg);
    }
}
