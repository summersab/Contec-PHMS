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

public class MailRegisterSucsess implements View.OnClickListener {
    public static boolean isClickClose = false;
    private String TAG = MailRegisterSucsess.class.getSimpleName();
    private ImageView mClose;
    private Context mContext;
    public Dialog mDialog;
    private TextView mKey;
    private Button mLogin;
    private TextView mMail;
    private ImageView mModifyKey;

    public MailRegisterSucsess(Context context) {
        this.mContext = context;
        View view = LayoutInflater.from(this.mContext).inflate(R.layout.layout_mail_register_sucsess, (ViewGroup) null);
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
        this.mLogin = (Button) view.findViewById(R.id.btn_login);
        this.mMail = (TextView) view.findViewById(R.id.mail);
        this.mKey = (TextView) view.findViewById(R.id.key);
        this.mModifyKey = (ImageView) view.findViewById(R.id.modify_key);
        this.mClose.setOnClickListener(this);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_close_dialog:
                isClickClose = true;
                dismiss();
                return;
            default:
                return;
        }
    }

    public ImageView getModifyBt() {
        return this.mModifyKey;
    }

    public Button getLoginBt() {
        return this.mLogin;
    }

    public void setMailAddress(String mail) {
        this.mMail.setText(mail);
    }

    public Dialog getmDialog() {
        return this.mDialog;
    }

    public void dismiss() {
        if (this.mDialog != null && this.mDialog.isShowing()) {
            this.mDialog.dismiss();
        }
    }
}
