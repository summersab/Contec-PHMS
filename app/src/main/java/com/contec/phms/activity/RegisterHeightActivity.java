package com.contec.phms.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import com.contec.phms.App_phms;
//import com.contec.phms.R;
import com.contec.phms.login.LoginActivity;
import com.contec.phms.db.UserInfoDao;
import com.contec.phms.util.Constants;
import com.contec.phms.util.CustomDialog;
import com.contec.phms.util.PhmsSharedPreferences;
import com.contec.phms.widget.ArrayWheelAdapter;
import com.contec.phms.widget.BirthdayWheelView;
import com.contec.phms.widget.DialogClass;
import com.contec.phms.widget.NumericWheelAdapter;
import com.contec.phms.widget.WheelView;
//import com.umeng.analytics.MobclickAgent;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Locale;
import u.aly.bs;
import com.contec.R;

public class RegisterHeightActivity extends Activity implements View.OnClickListener {
    private String[] arrays;
    private String[] arraysft;
    private String[] arraysin;
    private Button back_btn;
    private CustomDialog dialog;
    private WheelView heightWheel;
    private BirthdayWheelView inheightwheel;
    private int item;
    private int itemin;
    private DialogClass keyBackDialog;
    private String mLanguage;
    private ArrayList<String> names = new ArrayList<>();
    private ArrayList<String> namesft = new ArrayList<>();
    private ArrayList<String> namesin = new ArrayList<>();
    private Button regist_btn;
    private PhmsSharedPreferences sp;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_register_height_layout);
        initView();
    }

    private void initView() {
        this.heightWheel = (WheelView) findViewById(R.id.heightWheel);
        this.inheightwheel = (BirthdayWheelView) findViewById(R.id.heightinWheel);
        this.regist_btn = (Button) findViewById(R.id.height_next_btn);
        this.back_btn = (Button) findViewById(R.id.height_back);
        this.regist_btn.setOnClickListener(this);
        this.back_btn.setOnClickListener(this);
        this.mLanguage = PhmsSharedPreferences.getInstance(this).getString(UserInfoDao.Language, bs.b);
        if (this.mLanguage == null || this.mLanguage.equals(bs.b) || this.mLanguage.contains("1")) {
            this.mLanguage = "1" + Locale.getDefault().getLanguage();
        }
        if (Constants.Language != null && !Constants.Language.equalsIgnoreCase(bs.b)) {
            if (Constants.Language.equalsIgnoreCase("zh") || Constants.Language.equalsIgnoreCase("1zh")) {
                arrToStr();
                this.heightWheel.setAdapter(new ArrayWheelAdapter(this.arrays));
                this.heightWheel.setLabel(getString(R.string.desc));
                this.heightWheel.setLabels("cm");
                this.inheightwheel.setVisibility(View.GONE);
            } else if (Constants.Language.equalsIgnoreCase("en") || Constants.Language.equalsIgnoreCase("1en")) {
                this.inheightwheel.setVisibility(View.VISIBLE);
                arrToStrFt();
                this.heightWheel.setAdapter(new ArrayWheelAdapter(this.arraysft));
                this.heightWheel.setLabel(getString(R.string.desc));
                this.heightWheel.setLabels("ft");
                this.inheightwheel.setAdapter(new NumericWheelAdapter(0, 11));
                this.inheightwheel.setCyclic(true);
                this.inheightwheel.setLabel(getString(R.string.height_in));
            }
        }
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.height_back:
                startActivity(new Intent(this, RegisterSexActivity.class));
                overridePendingTransition(R.anim.fragment_slide_right_enter, R.anim.fragment_slide_right_out);
                finish();
                return;
            case R.id.height_next_btn:
                String textItem = null;
                if (Constants.Language == null || Constants.Language.equalsIgnoreCase(bs.b)) {
                    this.item = this.heightWheel.getCurrentItem();
                    textItem = this.arrays[this.item];
                } else if (Constants.Language.equalsIgnoreCase("en") || Constants.Language.equalsIgnoreCase("1en")) {
                    this.item = this.heightWheel.getCurrentItem();
                    String textItem2 = this.arraysft[this.item];
                    this.itemin = this.inheightwheel.getCurrentItem();
                    Log.e("Current value", "Feet: " + textItem2);
                    textItem = new StringBuilder().append(new BigDecimal((double) (((float) ((Integer.valueOf(textItem2).intValue() * 12) + this.itemin)) / 0.39f)).setScale(1, 1)).toString();
                    Log.e("Current value", "Inches: " + this.itemin);
                    Log.e("Current value", "Centimeters: " + textItem);
                }
                this.sp.saveInt("HeightItem", this.item);
                this.sp.saveColume("Height", textItem);
                Log.e("============", "===========");
                Log.e("当前身高的值：：", "厘米item::" + this.item);
                Log.e("当前身高的值：：", "厘米textItem::" + textItem);
                Log.e("============", "===========");
                startActivity(new Intent(this, RegisterWeightActivity.class));
                overridePendingTransition(R.anim.fragment_slide_left_enter, R.anim.fragment_slide_left_out);
                finish();
                return;
            default:
                return;
        }
    }

    public void arrToStr() {
        for (int x = 100; x < 2200; x += 10) {
            this.names.add(new StringBuilder(String.valueOf(x / 10)).toString());
        }
        this.arrays = (String[]) this.names.toArray(new String[this.names.size()]);
    }

    private void arrToStrFt() {
        for (int x = 2; x < 9; x++) {
            this.namesft.add(new StringBuilder(String.valueOf(x)).toString());
        }
        this.arraysft = (String[]) this.namesft.toArray(new String[this.namesft.size()]);
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
                        Intent _intent = new Intent(RegisterHeightActivity.this, LoginActivity.class);
                        Bundle bundle = new Bundle();
                        if (RegisterHeightActivity.this.sp.getString("phoneNum") == null || RegisterHeightActivity.this.sp.getString("phoneNum").equalsIgnoreCase(bs.b)) {
                            String string = RegisterHeightActivity.this.sp.getString("cardUserId");
                            bundle.putString("user", bs.b);
                        } else {
                            bundle.putString("user", RegisterHeightActivity.this.sp.getString("phoneNum"));
                        }
                        _intent.putExtras(bundle);
                        RegisterHeightActivity.this.startActivity(_intent);
                        RegisterHeightActivity.this.finish();
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
        this.sp = PhmsSharedPreferences.getInstance(this);
        this.mLanguage = PhmsSharedPreferences.getInstance(this).getString(UserInfoDao.Language, bs.b);
        if (this.mLanguage == null || this.mLanguage.equals(bs.b) || this.mLanguage.contains("1")) {
            this.mLanguage = "1" + Locale.getDefault().getLanguage();
        }
        if (Constants.Language != null && !Constants.Language.equalsIgnoreCase(bs.b)) {
            if (Constants.Language.equalsIgnoreCase("zh") || Constants.Language.equalsIgnoreCase("1zh")) {
                this.item = 570;
                this.heightWheel.setCurrentItem(this.item);
            } else if (Constants.Language.equalsIgnoreCase("en") || Constants.Language.equalsIgnoreCase("1en")) {
                this.item = 500;
                this.heightWheel.setCurrentItem(this.item);
            }
        }
        //MobclickAgent.onResume(this);
    }

    protected void onPause() {
        super.onPause();
        //MobclickAgent.onPause(this);
    }
}
