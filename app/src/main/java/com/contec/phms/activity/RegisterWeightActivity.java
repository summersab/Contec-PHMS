package com.contec.phms.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import com.contec.phms.App_phms;
//import com.contec.phms.R;
import com.contec.phms.login.LoginActivity;
import com.contec.phms.db.UserInfoDao;
import com.contec.phms.util.Constants;
import com.contec.phms.util.CustomDialog;
import com.contec.phms.util.PhmsSharedPreferences;
import com.contec.phms.widget.ArrayWheelAdapter;
import com.contec.phms.widget.DialogClass;
import com.contec.phms.widget.WheelView;
//import com.umeng.analytics.MobclickAgent;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Locale;
import u.aly.bs;
import com.contec.R;

public class RegisterWeightActivity extends Activity implements View.OnClickListener {
    private String[] arrays;
    private Button back_btn;
    private CustomDialog dialog;
    private boolean isOpen = false;
    private int item;
    private DialogClass keyBackDialog;
    private ImageView mImView;
    private String mLanguage;
    private ArrayList<String> namepounds = new ArrayList<>();
    private ArrayList<String> names = new ArrayList<>();
    private Button regist_btn;
    private int size = this.names.size();
    private int sizepounnd = this.namepounds.size();
    private PhmsSharedPreferences sp;
    private String textItem;
    private WheelView weightWheel;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_register_weight_layout);
        initView();
    }

    private void initView() {
        this.weightWheel = (WheelView) findViewById(R.id.weight_wheel);
        this.regist_btn = (Button) findViewById(R.id.weight_next_btn);
        this.back_btn = (Button) findViewById(R.id.weight_back_btn);
        this.regist_btn.setOnClickListener(this);
        this.back_btn.setOnClickListener(this);
        this.sp = PhmsSharedPreferences.getInstance(this);
        arrToStr();
        this.arrays = (String[]) this.names.toArray(new String[this.size]);
        this.weightWheel.setAdapter(new ArrayWheelAdapter(this.arrays));
        this.weightWheel.setLabel(getString(R.string.str_user_weight));
        this.mLanguage = PhmsSharedPreferences.getInstance(this).getString(UserInfoDao.Language, bs.b);
        if (this.mLanguage == null || this.mLanguage.equals(bs.b) || this.mLanguage.contains("1")) {
            this.mLanguage = "1" + Locale.getDefault().getLanguage();
        }
        Constants.Language = this.mLanguage;
        if (Constants.Language != null && !Constants.Language.equalsIgnoreCase(bs.b)) {
            if (Constants.Language.equalsIgnoreCase("zh") || Constants.Language.equalsIgnoreCase("1zh")) {
                this.weightWheel.setLabels("kg");
            } else if (Constants.Language.equalsIgnoreCase("en") || Constants.Language.equalsIgnoreCase("1eh")) {
                this.weightWheel.setLabels("lb");
                arrToStrpound();
                this.arrays = (String[]) this.namepounds.toArray(new String[this.sizepounnd]);
                this.weightWheel.setAdapter(new ArrayWheelAdapter(this.arrays));
            }
        }
    }

    public void arrToStr() {
        for (double x = 300.0d; x < 1510.0d; x += 1.0d) {
            this.names.add(new StringBuilder(String.valueOf(x / 10.0d)).toString());
        }
    }

    public void arrToStrpound() {
        for (float x = 300.0f; x < 1510.0f; x += 2.0f) {
            this.namepounds.add(new StringBuilder(String.valueOf((float) (((double) x) / 10.0d))).toString());
        }
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.weight_back_btn:
                startActivity(new Intent(this, RegisterHeightActivity.class));
                overridePendingTransition(R.anim.fragment_slide_right_enter, R.anim.fragment_slide_right_out);
                finish();
                return;
            case R.id.weight_next_btn:
                this.item = this.weightWheel.getCurrentItem();
                this.textItem = this.arrays[this.item];
                Log.e("当前体重的值：：", "kg之前item::" + this.item);
                Log.e("当前体重的值：：", "kg之前textItem::" + this.textItem);
                if (Constants.Language != null && !Constants.Language.equalsIgnoreCase(bs.b) && (Constants.Language.equalsIgnoreCase("en") || Constants.Language.equalsIgnoreCase("1en"))) {
                    this.textItem = new StringBuilder().append(new BigDecimal((double) (Float.valueOf(this.textItem).floatValue() / 2.2f)).setScale(1, 1)).toString();
                    Log.e("============", "===========");
                    Log.e("当前体重的值：：", "磅item::" + this.item);
                    Log.e("当前体重的值：：", "磅textItem::" + this.textItem);
                    Log.e("============", "===========");
                }
                this.sp.saveColume("Weight", this.textItem);
                this.sp.saveInt("WeightItem", this.item);
                startActivity(new Intent(this, RegisterBirthdayActivity.class));
                overridePendingTransition(R.anim.fragment_slide_left_enter, R.anim.fragment_slide_left_out);
                finish();
                return;
            default:
                return;
        }
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
                        Intent _intent = new Intent(RegisterWeightActivity.this, LoginActivity.class);
                        Bundle bundle = new Bundle();
                        if (RegisterWeightActivity.this.sp.getString("phoneNum") == null || RegisterWeightActivity.this.sp.getString("phoneNum").equalsIgnoreCase(bs.b)) {
                            String string = RegisterWeightActivity.this.sp.getString("cardUserId");
                            bundle.putString("user", bs.b);
                        } else {
                            bundle.putString("user", RegisterWeightActivity.this.sp.getString("phoneNum"));
                        }
                        bundle.putBoolean("isfromregist", true);
                        _intent.putExtras(bundle);
                        RegisterWeightActivity.this.startActivity(_intent);
                        RegisterWeightActivity.this.finish();
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
        this.mLanguage = PhmsSharedPreferences.getInstance(this).getString(UserInfoDao.Language, bs.b);
        if (this.mLanguage == null || this.mLanguage.equals(bs.b) || this.mLanguage.contains("1")) {
            this.mLanguage = "1" + Locale.getDefault().getLanguage();
        }
        if (Constants.Language != null && !Constants.Language.equalsIgnoreCase(bs.b)) {
            if (Constants.Language.equalsIgnoreCase("zh") || Constants.Language.equalsIgnoreCase("1zh")) {
                this.item = 200;
                this.weightWheel.setCurrentItem(this.item);
            } else if (Constants.Language.equalsIgnoreCase("en") || Constants.Language.equalsIgnoreCase("1en")) {
                this.item = 400;
                this.weightWheel.setCurrentItem(this.item);
            }
        }
        //MobclickAgent.onResume(this);
    }

    protected void onPause() {
        super.onPause();
        //MobclickAgent.onPause(this);
    }
}
