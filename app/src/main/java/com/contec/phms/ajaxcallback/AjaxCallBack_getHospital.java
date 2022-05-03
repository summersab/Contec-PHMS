package com.contec.phms.ajaxcallback;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import com.contec.phms.util.Constants;
import net.tsz.afinal.http.AjaxCallBack;

public class AjaxCallBack_getHospital extends AjaxCallBack<String> {
    private Context mContext;
    private Handler mHandler;

    public AjaxCallBack_getHospital(Context context, Handler handler) {
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
        if (_result.equals(Constants.SUCCESS)) {
            String _contentString = s.substring(42, s.length());
            Bundle bundle = new Bundle();
            bundle.putString("content", _contentString);
            Log.e("wsd", "hospital list: " + _contentString);
            Message _msg = new Message();
            _msg.what = Constants.REQUEST_HOSPITAL_SUCCESS;
            _msg.setData(bundle);
            this.mHandler.sendMessage(_msg);
        } else if (_result.equals(Integer.valueOf(Constants.REQUEST_HOSPITAL_FAILED))) {
            Message _msg2 = new Message();
            _msg2.what = Constants.REQUEST_HOSPITAL_FAILED;
            this.mHandler.sendMessage(_msg2);
        } else if (_result.equals(Integer.valueOf(Constants.REQUEST_HOSPITAL_DB_FAILED))) {
            Message _msg3 = new Message();
            _msg3.what = Constants.REQUEST_HOSPITAL_DB_FAILED;
            this.mHandler.sendMessage(_msg3);
        }
    }
}
