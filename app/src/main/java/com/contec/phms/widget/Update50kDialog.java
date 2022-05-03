package com.contec.phms.widget;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.contec.phms.R;

public abstract class Update50kDialog implements View.OnClickListener {
    private static final String TAG = Update50kDialog.class.getSimpleName();
    public static AlertDialog mDialog;
    private Button mBtn_cancel_update_50k;
    private Context mContext;
    private ImageView mIv_close_dialog_update50k;
    private TextProgressBar mPb_50k_update;
    private TextView mTv_update_percent;

    public abstract void cancelUpdate50k();

    public Update50kDialog(Context context) {
        this.mContext = context;
        View view = LayoutInflater.from(this.mContext).inflate(R.layout.layout_dialog_sp_progressbar, (ViewGroup) null);
        initView(view);
        initData();
        mDialog = new AlertDialog.Builder(this.mContext).create();
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }
        mDialog.show();
        mDialog.setContentView(view);
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.setCancelable(true);
        mDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            public void onDismiss(DialogInterface dialog) {
            }
        });
    }

    private void initView(View view) {
        this.mPb_50k_update = (TextProgressBar) view.findViewById(R.id.pb_50k_update);
        this.mTv_update_percent = (TextView) view.findViewById(R.id.tv_update_percent);
        this.mBtn_cancel_update_50k = (Button) view.findViewById(R.id.btn_cancel_update_50k);
        this.mIv_close_dialog_update50k = (ImageView) view.findViewById(R.id.iv_close_dialog_update50k);
        this.mIv_close_dialog_update50k.setOnClickListener(this);
        this.mBtn_cancel_update_50k.setOnClickListener(this);
    }

    private void initData() {
        this.mPb_50k_update.setMax(100);
        this.mTv_update_percent.setText("0%");
        this.mPb_50k_update.setProgress(0);
    }

    public void setProgress(int progress) {
        this.mTv_update_percent.setText(String.valueOf(progress) + "%");
        this.mPb_50k_update.setProgress(progress);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_close_dialog_update50k:
                cancelUpdate50k();
                return;
            case R.id.btn_cancel_update_50k:
                cancelUpdate50k();
                return;
            default:
                return;
        }
    }

    public Dialog getmDialog() {
        return mDialog;
    }

    public void show() {
        if (mDialog != null && !mDialog.isShowing()) {
            mDialog.show();
        }
    }

    public void dismiss() {
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }
    }

    public void LogI(String msg) {
        Log.i(TAG, msg);
    }
}
