package com.contec.phms.widget;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import com.contec.phms.R;
import com.contec.phms.util.CLog;

public abstract class IsUpdate50kDialog implements View.OnClickListener {
    private static final String TAG = IsUpdate50kDialog.class.getSimpleName();
    public static AlertDialog mDialog;
    private Button mBtn_cancel_isUpdate50k;
    private Button mBtn_sure_isUpdate50k;
    private Context mContext;
    private ImageView mIv_close_dialog_isUpdate50k;

    public abstract void cancelUpdate50k();

    public abstract void sureUpdate50k();

    public IsUpdate50kDialog(Context context) {
        this.mContext = context;
        View view = LayoutInflater.from(this.mContext).inflate(R.layout.layout_dialog_is_update50k, (ViewGroup) null);
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
        this.mBtn_sure_isUpdate50k = (Button) view.findViewById(R.id.btn_sure_isUpdate50k);
        this.mBtn_cancel_isUpdate50k = (Button) view.findViewById(R.id.btn_cancel_isUpdate50k);
        this.mIv_close_dialog_isUpdate50k = (ImageView) view.findViewById(R.id.iv_close_dialog_isUpdate50k);
        this.mBtn_sure_isUpdate50k.setOnClickListener(this);
        this.mBtn_cancel_isUpdate50k.setOnClickListener(this);
        this.mIv_close_dialog_isUpdate50k.setOnClickListener(this);
    }

    private void initData() {
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_close_dialog_isUpdate50k:
                cancelUpdate50k();
                return;
            case R.id.btn_cancel_isUpdate50k:
                cancelUpdate50k();
                return;
            case R.id.btn_sure_isUpdate50k:
                sureUpdate50k();
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
        CLog.i(TAG, msg);
    }
}
