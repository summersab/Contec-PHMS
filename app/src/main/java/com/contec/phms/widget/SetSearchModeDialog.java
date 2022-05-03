package com.contec.phms.widget;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import com.contec.phms.R;
import com.contec.phms.util.Constants;
import com.contec.phms.util.PhmsSharedPreferences;

public class SetSearchModeDialog implements View.OnClickListener {
    public static boolean isClickClose = false;
    private String TAG = SetSearchModeDialog.class.getSimpleName();
    private Button mBtn_sure_search_mode;
    private Context mContext;
    public Dialog mDialog;
    private ImageView mIv_close_dialog;
    private ImageView mIv_new_right;
    private ImageView mIv_old_right;
    private RelativeLayout mRl_new_ble;
    private RelativeLayout mRl_old_ble;

    public SetSearchModeDialog(Context context) {
        this.mContext = context;
        View view = LayoutInflater.from(this.mContext).inflate(R.layout.layout_set_search_mode_dialog, (ViewGroup) null);
        initView(view);
        initData();
        this.mDialog = new AlertDialog.Builder(this.mContext).create();
        if (this.mDialog != null && this.mDialog.isShowing()) {
            this.mDialog.dismiss();
        }
        this.mDialog.show();
        this.mDialog.setContentView(view);
        this.mDialog.setCanceledOnTouchOutside(true);
        this.mDialog.setCancelable(false);
    }

    private void initData() {
    }

    private void initView(View view) {
        this.mIv_close_dialog = (ImageView) view.findViewById(R.id.iv_close_dialog);
        this.mBtn_sure_search_mode = (Button) view.findViewById(R.id.btn_sure_search_mode);
        this.mIv_new_right = (ImageView) view.findViewById(R.id.iv_new_right);
        this.mIv_old_right = (ImageView) view.findViewById(R.id.iv_old_right);
        this.mRl_new_ble = (RelativeLayout) view.findViewById(R.id.rl_new_ble);
        this.mRl_old_ble = (RelativeLayout) view.findViewById(R.id.rl_old_ble);
        this.mIv_close_dialog.setOnClickListener(this);
        this.mRl_new_ble.setOnClickListener(this);
        this.mRl_old_ble.setOnClickListener(this);
        this.mBtn_sure_search_mode.setOnClickListener(this);
        this.mIv_old_right.setVisibility(View.INVISIBLE);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_close_dialog:
                isClickClose = true;
                dismiss();
                return;
            case R.id.rl_new_ble:
                isClickClose = false;
                LogI("点击了新模式");
                if (this.mIv_old_right.getVisibility() != 4 || this.mIv_new_right.getVisibility() != 0) {
                    this.mIv_new_right.setVisibility(View.VISIBLE);
                    this.mIv_old_right.setVisibility(View.INVISIBLE);
                    return;
                }
                return;
            case R.id.rl_old_ble:
                isClickClose = false;
                LogI("点击了旧模式");
                if (this.mIv_new_right.getVisibility() != 4 || this.mIv_old_right.getVisibility() != 0) {
                    this.mIv_old_right.setVisibility(View.VISIBLE);
                    this.mIv_new_right.setVisibility(View.INVISIBLE);
                    return;
                }
                return;
            case R.id.btn_sure_search_mode:
                isClickClose = false;
                dismiss();
                return;
            default:
                return;
        }
    }

    public Dialog getmDialog() {
        return this.mDialog;
    }

    public void dismiss() {
        saveSearchMode();
        if (this.mDialog != null && this.mDialog.isShowing()) {
            this.mDialog.dismiss();
        }
    }

    private void saveSearchMode() {
        if (this.mIv_new_right.getVisibility() == 0) {
            PhmsSharedPreferences.getInstance(this.mContext).saveBoolean("ifOpenBLe", true);
        } else if (this.mIv_old_right.getVisibility() == 0) {
            PhmsSharedPreferences.getInstance(this.mContext).saveBoolean("ifOpenBLe", false);
        }
        LogI("ble mode: " + PhmsSharedPreferences.getInstance(this.mContext).getBoolean("ifOpenBLe", true));
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
