package com.contec.phms.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import com.contec.phms.R;

public class RegisterSelectActivity extends Activity implements View.OnClickListener {
    private Button back;
    private LinearLayout toMail;
    private LinearLayout toPhone;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_select);
        initView();
    }

    private void initView() {
        this.back = (Button) findViewById(R.id.back_btn);
        this.toMail = (LinearLayout) findViewById(R.id.to_mail_register);
        this.toPhone = (LinearLayout) findViewById(R.id.to_phone_register);
        this.back.setOnClickListener(this);
        this.toMail.setOnClickListener(this);
        this.toPhone.setOnClickListener(this);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_btn:
                finish();
                return;
            case R.id.to_mail_register:
                startActivity(new Intent(this, RegisterMailActivity.class));
                finish();
                return;
            case R.id.to_phone_register:
                startActivity(new Intent(this, RegisterPhoneActivity.class));
                finish();
                return;
            default:
                return;
        }
    }
}
