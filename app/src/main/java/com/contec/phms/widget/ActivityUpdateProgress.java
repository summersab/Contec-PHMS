package com.contec.phms.widget;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.contec.phms.R;
import com.contec.phms.activity.ActivityManager;
import com.contec.phms.ajaxcallback.AjaxCallbackForDownNewApp;
import com.contec.phms.service.UpdateAppFunction;
import com.contec.phms.util.AlertDialogUtil;
import com.contec.phms.util.CLog;
import com.contec.phms.util.Constants;
import java.io.File;
import net.tsz.afinal.http.HttpHandler;

public class ActivityUpdateProgress extends Activity {
    protected static final String TAG = ActivityUpdateProgress.class.getSimpleName();
    public static Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    CLog.i(ActivityUpdateProgress.TAG, new StringBuilder(String.valueOf(msg.arg1)).toString());
                    ActivityUpdateProgress.mProgressText.setText(String.valueOf(msg.arg1) + "%");
                    ActivityUpdateProgress.mProgressBar.setProgress(msg.arg1);
                    return;
                default:
                    return;
            }
        }
    };
    static ProgressBar mProgressBar;
    static TextView mProgressText;
    HttpHandler<File> mHttpHandler;
    ActivityManager m_ActivityManager = ActivityManager.getActivityManager();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_update_view);
        this.m_ActivityManager.addActivity(this);
        mProgressText = (TextView) findViewById(R.id.content_view_text1);
        mProgressBar = (ProgressBar) findViewById(R.id.content_view_progress);
        this.mHttpHandler = UpdateAppFunction.downNewVersionApp(new AjaxCallbackForDownNewApp(), Constants.NEWAPPNAME);
    }

    protected void onDestroy() {
        super.onDestroy();
        if (!Constants.DOWNLOADEND) {
            AlertDialogUtil.showToast((Context) this, (int) R.string.download_end);
            this.mHttpHandler.stop();
            this.m_ActivityManager.finishCurrentActivity();
        }
    }
}
