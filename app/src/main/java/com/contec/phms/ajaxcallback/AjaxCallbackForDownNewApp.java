package com.contec.phms.ajaxcallback;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Message;
import android.view.View;
import com.contec.phms.R;
import com.contec.phms.activity.ActivityManager;
import com.contec.phms.util.AlertDialogUtil;
import com.contec.phms.util.CLog;
import com.contec.phms.util.Constants;
import com.contec.phms.util.PageUtil;
import com.contec.phms.widget.ActivityUpdateProgress;
import java.io.File;
import net.tsz.afinal.http.AjaxCallBack;

public class AjaxCallbackForDownNewApp extends AjaxCallBack<File> {
    private static final ActivityManager ACTIVITY_MANAGER = ActivityManager.getActivityManager();
    private String TAG = AjaxCallbackForDownNewApp.class.getSimpleName();
    ProgressDialog mProgressDialog;

    public boolean isProgress() {
        return super.isProgress();
    }

    public int getRate() {
        return super.getRate();
    }

    public AjaxCallBack<File> progress(boolean progress, int rate) {
        return super.progress(progress, rate);
    }

    public void onStart() {
        super.onStart();
    }

    public void onLoading(long count, long current) {
        super.onLoading(count, current);
        System.out.println("onLoading =====  " + count + "+++" + current);
        if (count > PageUtil.getSDFreeSize_BYTE()) {
            AlertDialogUtil.alertDialog(ACTIVITY_MANAGER.getCurrentActivity(), R.string.sd_card_no_free_size, 17039370, (View.OnClickListener) null);
            return;
        }
        Message _mMessage = new Message();
        _mMessage.what = 1;
        _mMessage.arg1 = (int) ((100 * current) / count);
        ActivityUpdateProgress.mHandler.sendMessage(_mMessage);
    }

    public void onSuccess(File t) {
        super.onSuccess(t);
        CLog.i(this.TAG, "下载完成" + t.getAbsolutePath());
        Constants.DOWNLOADEND = true;
        ACTIVITY_MANAGER.finishCurrentActivity();
        Activity CURRENT_ACTIVITY = getCurerentActivity();
        AlertDialogUtil.MyTwoButtonDialog(CURRENT_ACTIVITY, CURRENT_ACTIVITY.getString(17039380), 17301543, CURRENT_ACTIVITY.getString(R.string.downloadend), CURRENT_ACTIVITY.getString(17039370), (DialogInterface.OnClickListener) null, CURRENT_ACTIVITY.getString(17039360), (DialogInterface.OnClickListener) null, true);
    }

    private Activity getCurerentActivity() {
        return ACTIVITY_MANAGER.getCurrentActivity();
    }

    public void onFailure(Throwable t, String strMsg) {
        super.onFailure(t, 0, strMsg);
    }
}
