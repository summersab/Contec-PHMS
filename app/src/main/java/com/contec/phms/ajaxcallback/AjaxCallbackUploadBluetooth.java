package com.contec.phms.ajaxcallback;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import com.contec.phms.util.Constants;
import net.tsz.afinal.http.AjaxCallBack;

public class AjaxCallbackUploadBluetooth extends AjaxCallBack<String> {
    private Context mContext;
    private Handler mHandler;

    public AjaxCallbackUploadBluetooth(Context context, Handler handler) {
        this.mContext = context;
        this.mHandler = handler;
    }

    public void onStart() {
        super.onStart();
    }

    public void onSuccess(String t) {
        super.onSuccess(t);
        doResponse(t);
    }

    public void onFailure(Throwable t, String strMsg) {
        super.onFailure(t, 0, strMsg);
        Log.e("wsd", "strMsg" + strMsg);
        Message _msg = new Message();
        _msg.what = Constants.HOSPITAL_FAILED;
        this.mHandler.sendMessage(_msg);
    }

    public void onLoading(long count, long current) {
        super.onLoading(count, current);
    }

    public boolean isProgress() {
        return super.isProgress();
    }

    public AjaxCallBack<String> progress(boolean progress, int rate) {
        return super.progress(progress, rate);
    }

    public int getRate() {
        return super.getRate();
    }

    private void doResponse(String s) {
        String _result = s.substring(34, 40);
        Log.e("===>", _result);
        _result.equals(Constants.SUCCESS);
    }
}
