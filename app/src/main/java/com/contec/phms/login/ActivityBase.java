package com.contec.phms.login;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import com.contec.phms.App_phms;
import com.contec.phms.activity.ActivityManager;
import com.contec.phms.util.AlertDialogUtil;
import com.contec.phms.util.CLog;
import com.contec.phms.util.Constants;
import com.contec.phms.widget.ActivityUpdateProgress;

public class ActivityBase extends Activity {
    private static final String TAG = ActivityBase.class.getSimpleName();
    public AlertDialog mUpdateAlertDialog;
    private DialogInterface.OnClickListener m_CancleUpdateListener = new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int which) {
            Constants.NEEDUPDATE = false;
        }
    };
    private DialogInterface.OnClickListener m_UpdateListener = new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int which) {
            Constants.NEEDUPDATE = false;
            ActivityBase _currentActivity = (ActivityBase) ActivityManager.getActivityManager().getCurrentActivity();
            _currentActivity.startActivity(new Intent(_currentActivity, ActivityUpdateProgress.class));
        }
    };
    public ActivityManager myActivityManager = ActivityManager.getActivityManager();
    public DialogInterface.OnClickListener p_onClickEvent;

    protected void onStart() {
        super.onStart();
        CLog.i(TAG, "onStart");
    }

    public void AlertForUpdate() {
        if (Constants.NEEDUPDATE) {
            this.mUpdateAlertDialog = AlertDialogUtil.MyTwoButtonDialog(this, getString(17039380), 0, "是否更新", getString(17039370), this.m_UpdateListener, getString(17039360), this.m_CancleUpdateListener, true);
        }
    }

    protected void onRestart() {
        super.onRestart();
        AlertForUpdate();
    }

    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App_phms.getInstance().addActivity(this);
        int SCREENHEIGH = getWindowManager().getDefaultDisplay().getHeight();
        int SCREENWEIGH = getWindowManager().getDefaultDisplay().getWidth();
        CLog.e("###############", "SCREENHEIGH:" + SCREENHEIGH + "       SCREENWEIGH:" + SCREENWEIGH);
        Constants.M_SCREENHEIGH = SCREENHEIGH;
        Constants.M_SCREENWEIGH = SCREENWEIGH;
    }
}
