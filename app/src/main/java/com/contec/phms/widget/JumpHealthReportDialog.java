package com.contec.phms.widget;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.contec.phms.App_phms;
import com.contec.phms.R;
import com.contec.phms.activity.MainActivityNew;
import com.contec.phms.util.Constants;
import com.contec.phms.util.MyActivityManager;
import com.contec.phms.util.PageUtil;
import java.util.Timer;
import java.util.TimerTask;

public class JumpHealthReportDialog implements View.OnClickListener {
    private String TAG = JumpHealthReportDialog.class.getSimpleName();
    private Button mBtn_cancel;
    private Button mBtn_sure;
    private Context mContext;
    public Dialog mDialog;
    private TextView mJumpOrRefresh;

    public JumpHealthReportDialog(Context context, String info) {
        this.mContext = context;
        View view = LayoutInflater.from(this.mContext).inflate(R.layout.layout_dialog_jump_health_report, (ViewGroup) null);
        initView(view);
        initData(info);
        this.mDialog = new AlertDialog.Builder(this.mContext).create();
        if (this.mDialog != null && this.mDialog.isShowing()) {
            this.mDialog.dismiss();
        }
        this.mDialog.show();
        this.mDialog.setContentView(view);
        this.mDialog.setCanceledOnTouchOutside(true);
        this.mDialog.setCancelable(true);
    }

    private void initView(View view) {
        int screenWidth = (UIUtils.getScreenWidth(this.mContext) * 3) / 4;
        this.mBtn_sure = (Button) view.findViewById(R.id.btn_cancle);
        this.mBtn_cancel = (Button) view.findViewById(R.id.btn_sure);
        this.mJumpOrRefresh = (TextView) view.findViewById(R.id.tv_jumpOrRefresh);
        this.mBtn_sure.setOnClickListener(this);
        this.mBtn_cancel.setOnClickListener(this);
    }

    private void initData(String info) {
        this.mJumpOrRefresh.setText(info);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_sure:
                Activity currentActivity = MyActivityManager.getInstance().getCurrentActivity();
                if (currentActivity == null) {
                    return;
                }
                if (PageUtil.getTopActivityClassName().contains(MainActivityNew.class.getName())) {
                    dismiss();
                    sendMsg();
                    return;
                }
                currentActivity.finish();
                dismiss();
                new Timer().schedule(new TimerTask() {
                    public void run() {
                        JumpHealthReportDialog.this.sendMsg();
                    }
                }, 30);
                return;
            case R.id.btn_cancle:
                dismiss();
                return;
            default:
                return;
        }
    }

    private void sendMsg() {
        Message msg = new Message();
        msg.arg2 = 13;
        msg.what = 551;
        App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(msg);
    }

    public Dialog getmDialog() {
        return this.mDialog;
    }

    public void dismiss() {
        if (this.mDialog != null && this.mDialog.isShowing()) {
            this.mDialog.dismiss();
        }
    }

    public void LogI(String msg) {
        if (Constants.mTestFlag) {
            Log.i(this.TAG, msg);
        }
    }

    public void LogE(String msg) {
        if (Constants.mTestFlag) {
            Log.e(this.TAG, msg);
        }
    }
}
