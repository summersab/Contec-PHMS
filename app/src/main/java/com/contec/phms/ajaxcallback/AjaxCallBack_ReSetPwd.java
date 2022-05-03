package com.contec.phms.ajaxcallback;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import com.contec.phms.util.CLog;
import com.contec.phms.util.Constants;
import net.tsz.afinal.http.AjaxCallBack;

public class AjaxCallBack_ReSetPwd extends AjaxCallBack<String> {
    private String TAG = "AjaxCallBack_ReSetPwd";
    private Handler mhandler;
    private final int resetpwdagrserror = 108901;
    private final int resetpwdcounterror = 108902;
    private final int resetpwddberror = 108904;
    private final int resetpwdneterror = 108910;
    private final int resetpwdsmsovertime = 108903;
    private final int resetpwdsuccess = 108900;

    public AjaxCallBack_ReSetPwd(Context context, Handler mhandler2) {
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
            _msg.what = 108900;
            this.mhandler.sendMessage(_msg);
        } else if (_result.equals("108901")) {
            Message _msg2 = new Message();
            _msg2.what = 108901;
            this.mhandler.sendMessage(_msg2);
        } else if (_result.equals("108902")) {
            Message _msg3 = new Message();
            _msg3.what = 108902;
            this.mhandler.sendMessage(_msg3);
        } else if (_result.equals("108903")) {
            Message _msg4 = new Message();
            _msg4.what = 108903;
            this.mhandler.sendMessage(_msg4);
        } else if (_result.equals("108904")) {
            Message _msg5 = new Message();
            _msg5.what = 108904;
            this.mhandler.sendMessage(_msg5);
        }
    }

    public void onFailure(Throwable t, String strMsg) {
        super.onFailure(t, 0, strMsg);
        Message _msg = new Message();
        _msg.what = 108910;
        this.mhandler.sendMessage(_msg);
    }
}
