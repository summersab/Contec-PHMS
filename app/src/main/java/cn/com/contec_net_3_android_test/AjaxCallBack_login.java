package cn.com.contec_net_3_android_test;

import android.util.Log;
import net.tsz.afinal.http.AjaxCallBack;
import cn.com.contec_net_3_android.Constants;

public class AjaxCallBack_login extends AjaxCallBack<String> {
    public boolean isProgress() {
        return super.isProgress();
    }

    public int getRate() {
        return super.getRate();
    }

    public AjaxCallBack<String> progress(boolean progress, int rate) {
        Log.i(Constants.TAG, "progress--" + progress + "--rate--" + rate);
        return super.progress(progress, rate);
    }

    public void onStart() {
        super.onStart();
        Log.i(Constants.TAG, "start--------");
    }

    public void onLoading(long count, long current) {
        super.onLoading(count, current);
        Log.i(Constants.TAG, String.valueOf(count) + "-cout");
    }

    public void onSuccess(String t) {
        super.onSuccess(t);
        Log.i(Constants.TAG, t.toString());
    }

    public void onFailure(Throwable t, String strMsg) {
        super.onFailure(t, 0, strMsg);
        Log.i(Constants.TAG, strMsg.toString());
    }
}
