package com.contec.phms.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import com.contec.phms.App_phms;
//import com.contec.phms.R;
import com.contec.phms.login.LoginActivity;
import com.contec.phms.db.UserInfoDao;
import com.contec.phms.util.CustomDialog;
import com.contec.phms.util.PhmsSharedPreferences;
//import com.umeng.analytics.MobclickAgent;
import de.greenrobot.event.EventBus;
import u.aly.bs;
import com.contec.R;

public class RegisterSexActivity extends Activity {
    private static final String TAG = RegisterSexActivity.class.getSimpleName();
    private CustomDialog dialog;
    private final int mResultCode = 64;
    private Button next_btn;
    private RadioButton r1;
    private RadioButton r2;
    private RadioGroup rg;
    private PhmsSharedPreferences sp;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_register_information_layout);
        Message message = new Message();
        message.arg2 = 1324567;
        EventBus.getDefault().post(message);
        initView();
        this.sp = PhmsSharedPreferences.getInstance(getApplicationContext());
    }

    private void initView() {
        this.next_btn = (Button) findViewById(R.id.register_regist_btn);
        this.rg = (RadioGroup) findViewById(R.id.rg);
        this.r1 = (RadioButton) findViewById(R.id.male);
        this.r2 = (RadioButton) findViewById(R.id.famale);
        this.next_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                if (R.id.famale == RegisterSexActivity.this.rg.getCheckedRadioButtonId()) {
                    RegisterSexActivity.this.sp.saveColume(UserInfoDao.Sex, "1");
                } else {
                    RegisterSexActivity.this.sp.saveColume(UserInfoDao.Sex, "0");
                }
                RegisterSexActivity.this.jumpActivity();
            }
        });
    }

    private void jumpActivity() {
        startActivity(new Intent(this, RegisterHeightActivity.class));
        overridePendingTransition(R.anim.fragment_slide_left_enter, R.anim.fragment_slide_left_out);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode != 4) {
            return false;
        }
        CustomDialog.Builder customBuilder = new CustomDialog.Builder(this);
        customBuilder.setTitle(getString(R.string.str_backup)).setMessage(getString(R.string.str_backup_content)).setNegativeButton(getString(R.string.ok), (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        }).setPositiveButton(getString(R.string.cancel), (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                switch (which) {
                    case -2:
                        Intent _intent = new Intent(RegisterSexActivity.this, LoginActivity.class);
                        Bundle bundle = new Bundle();
                        if (RegisterSexActivity.this.sp.getString("phoneNum") == null || RegisterSexActivity.this.sp.getString("phoneNum").equalsIgnoreCase(bs.b)) {
                            String string = RegisterSexActivity.this.sp.getString("cardUserId");
                            bundle.putString("user", bs.b);
                        } else {
                            bundle.putString("user", RegisterSexActivity.this.sp.getString("phoneNum"));
                        }
                        bundle.putBoolean("isfromregist", true);
                        _intent.putExtras(bundle);
                        RegisterSexActivity.this.startActivity(_intent);
                        RegisterSexActivity.this.finish();
                        return;
                    default:
                        return;
                }
            }
        });
        this.dialog = customBuilder.create();
        this.dialog.show();
        return false;
    }

    protected void onResume() {
        super.onResume();
        App_phms.getInstance().addActivity(this);
        //MobclickAgent.onResume(this);
    }

    protected void onPause() {
        super.onPause();
        //MobclickAgent.onPause(this);
    }
}
