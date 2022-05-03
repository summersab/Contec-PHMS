package com.contec.phms.widget;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.contec.phms.R;

public class MailBeRegisterDialog implements View.OnClickListener {
    private String TAG = MailBeRegisterDialog.class.getSimpleName();
    private ImageView mClose;
    private Context mContext;
    public Dialog mDialog;
    private Button mLogin;
    private TextView mMailAddress;
    private Button mModify;

    public MailBeRegisterDialog(Context context) {
        this.mContext = context;
        View view = LayoutInflater.from(this.mContext).inflate(R.layout.layout_mailberegister, (ViewGroup) null);
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
        this.mClose = (ImageView) view.findViewById(R.id.iv_close_dialog);
        this.mMailAddress = (TextView) view.findViewById(R.id.mail_address);
        this.mModify = (Button) view.findViewById(R.id.modify);
        this.mLogin = (Button) view.findViewById(R.id.login);
        this.mClose.setOnClickListener(this);
        this.mModify.setOnClickListener(this);
        this.mLogin.setOnClickListener(this);
    }

    public void setMailAddress(String content) {
        this.mMailAddress.setText(content);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_close_dialog:
                dismiss();
                return;
            case R.id.modify:
                dismiss();
                return;
            default:
                return;
        }
    }

    public Dialog getmDialog() {
        return this.mDialog;
    }

    public Button getmLogin() {
        return this.mLogin;
    }

    public void dismiss() {
        if (this.mDialog != null && this.mDialog.isShowing()) {
            this.mDialog.dismiss();
        }
    }
}
