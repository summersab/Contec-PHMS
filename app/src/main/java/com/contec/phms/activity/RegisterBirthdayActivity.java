package com.contec.phms.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import com.contec.phms.App_phms;
//import com.contec.phms.R;
import com.contec.phms.login.LoginActivity;
import com.contec.phms.util.CLog;
import com.contec.phms.util.CustomDialog;
import com.contec.phms.util.PhmsSharedPreferences;
import com.contec.phms.widget.BirthdayWheelView;
import com.contec.phms.widget.DialogClass;
import com.contec.phms.widget.NumericWheelAdapter;
import com.contec.phms.widget.OnWheelChangedListener;
import com.contec.phms.widget.WheelView;
//import com.umeng.analytics.MobclickAgent;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import u.aly.bs;
import com.contec.R;

public class RegisterBirthdayActivity extends Activity {
    private static int END_YEAR = 2015;
    private static int START_YEAR = 1900;
    private Button back_but;
    private String birthday;
    private int day;
    private int dayItem;
    private CustomDialog dialog;
    private DialogClass keyBackDialog;
    private int mDay;
    private int mMonth;
    private int mYear;
    private int month;
    private int monthItem;
    private Button regist_btn;
    private PhmsSharedPreferences sp;
    private BirthdayWheelView wv_day;
    private BirthdayWheelView wv_month;
    private BirthdayWheelView wv_year;
    private int year;
    private int yearItem;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_register_birthday_layout);
        showDateTimePicker();
        this.sp = PhmsSharedPreferences.getInstance(getApplicationContext());
        this.regist_btn = (Button) findViewById(R.id.brithday_next_btn);
        this.regist_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                RegisterBirthdayActivity.this.yearItem = RegisterBirthdayActivity.this.wv_year.getCurrentItem();
                RegisterBirthdayActivity.this.monthItem = RegisterBirthdayActivity.this.wv_month.getCurrentItem();
                RegisterBirthdayActivity.this.dayItem = RegisterBirthdayActivity.this.wv_day.getCurrentItem();
                RegisterBirthdayActivity.this.sp.saveInt("yearItem", RegisterBirthdayActivity.this.yearItem);
                RegisterBirthdayActivity.this.sp.saveInt("monthItem", RegisterBirthdayActivity.this.monthItem);
                RegisterBirthdayActivity.this.sp.saveInt("dayItem", RegisterBirthdayActivity.this.dayItem);
                RegisterBirthdayActivity.this.mYear = RegisterBirthdayActivity.this.wv_year.getCurrentItem() + 1900;
                RegisterBirthdayActivity.this.mMonth = RegisterBirthdayActivity.this.wv_month.getCurrentItem() + 1;
                RegisterBirthdayActivity.this.mDay = RegisterBirthdayActivity.this.wv_day.getCurrentItem() + 1;
                RegisterBirthdayActivity.this.birthday = RegisterBirthdayActivity.this.processDate(RegisterBirthdayActivity.this.mYear, RegisterBirthdayActivity.this.mMonth, RegisterBirthdayActivity.this.mDay);
                RegisterBirthdayActivity.this.sp.saveColume("Birthday", RegisterBirthdayActivity.this.birthday);
                RegisterBirthdayActivity.this.startActivity(new Intent(RegisterBirthdayActivity.this, RegisterCardIdActivity.class));
                RegisterBirthdayActivity.this.overridePendingTransition(R.anim.fragment_slide_left_enter, R.anim.fragment_slide_left_out);
                RegisterBirthdayActivity.this.finish();
            }
        });
        this.back_but = (Button) findViewById(R.id.brithday_back_btn);
        this.back_but.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (R.id.brithday_back_btn == v.getId()) {
                    RegisterBirthdayActivity.this.startActivity(new Intent(RegisterBirthdayActivity.this, RegisterWeightActivity.class));
                    RegisterBirthdayActivity.this.overridePendingTransition(R.anim.fragment_slide_right_enter, R.anim.fragment_slide_right_out);
                    RegisterBirthdayActivity.this.finish();
                }
            }
        });
    }

    private void showDateTimePicker() {
        Calendar calendar = Calendar.getInstance();
        this.year = calendar.get(1);
        this.month = calendar.get(2);
        this.day = calendar.get(5);
        final List<String> list_big = Arrays.asList(new String[]{"1", "3", "5", "7", "8", "10", "12"});
        final List<String> list_little = Arrays.asList(new String[]{"4", "6", "9", "11"});
        this.wv_year = (BirthdayWheelView) findViewById(R.id.year);
        this.wv_year.setAdapter(new NumericWheelAdapter(START_YEAR, END_YEAR));
        this.wv_year.setCyclic(true);
        this.wv_year.setLabel(getString(R.string.str_year));
        this.wv_year.setCurrentItem(this.year);
        this.wv_month = (BirthdayWheelView) findViewById(R.id.month);
        this.wv_month.setAdapter(new NumericWheelAdapter(1, 12));
        this.wv_month.setCyclic(true);
        this.wv_month.setLabel(getString(R.string.str_month));
        this.wv_month.setCurrentItem(this.month);
        this.wv_day = (BirthdayWheelView) findViewById(R.id.day);
        this.wv_day.setCyclic(true);
        if (list_big.contains(String.valueOf(this.month + 1))) {
            this.wv_day.setAdapter(new NumericWheelAdapter(1, 31));
        } else if (list_little.contains(String.valueOf(this.month + 1))) {
            this.wv_day.setAdapter(new NumericWheelAdapter(1, 30));
        } else if ((this.year % 4 != 0 || this.year % 100 == 0) && this.year % 400 != 0) {
            this.wv_day.setAdapter(new NumericWheelAdapter(1, 28));
        } else {
            this.wv_day.setAdapter(new NumericWheelAdapter(1, 29));
        }
        this.wv_day.setLabel(getString(R.string.str_day));
        this.wv_day.setCurrentItem(this.day);
        OnWheelChangedListener wheelListener_year = new OnWheelChangedListener() {
            public void onChanged(BirthdayWheelView wheel, int oldValue, int newValue) {
                int year_num = newValue + RegisterBirthdayActivity.START_YEAR;
                if (list_big.contains(String.valueOf(RegisterBirthdayActivity.this.wv_month.getCurrentItem() + 1))) {
                    RegisterBirthdayActivity.this.wv_day.setAdapter(new NumericWheelAdapter(1, 31));
                } else if (list_little.contains(String.valueOf(RegisterBirthdayActivity.this.wv_month.getCurrentItem() + 1))) {
                    RegisterBirthdayActivity.this.wv_day.setAdapter(new NumericWheelAdapter(1, 30));
                } else if ((year_num % 4 != 0 || year_num % 100 == 0) && year_num % 400 != 0) {
                    RegisterBirthdayActivity.this.wv_day.setAdapter(new NumericWheelAdapter(1, 28));
                } else {
                    RegisterBirthdayActivity.this.wv_day.setAdapter(new NumericWheelAdapter(1, 29));
                }
            }

            public void onChanged(WheelView WheelView, int oldValue, int newValue) {
            }
        };
        OnWheelChangedListener wheelListener_month = new OnWheelChangedListener() {
            public void onChanged(BirthdayWheelView wheel, int oldValue, int newValue) {
                int month_num = newValue + 1;
                if (list_big.contains(String.valueOf(month_num))) {
                    RegisterBirthdayActivity.this.wv_day.setAdapter(new NumericWheelAdapter(1, 31));
                } else if (list_little.contains(String.valueOf(month_num))) {
                    RegisterBirthdayActivity.this.wv_day.setAdapter(new NumericWheelAdapter(1, 30));
                } else if (((RegisterBirthdayActivity.this.wv_year.getCurrentItem() + RegisterBirthdayActivity.START_YEAR) % 4 != 0 || (RegisterBirthdayActivity.this.wv_year.getCurrentItem() + RegisterBirthdayActivity.START_YEAR) % 100 == 0) && (RegisterBirthdayActivity.this.wv_year.getCurrentItem() + RegisterBirthdayActivity.START_YEAR) % 400 != 0) {
                    RegisterBirthdayActivity.this.wv_day.setAdapter(new NumericWheelAdapter(1, 28));
                } else {
                    RegisterBirthdayActivity.this.wv_day.setAdapter(new NumericWheelAdapter(1, 29));
                }
            }

            public void onChanged(WheelView WheelView, int oldValue, int newValue) {
            }
        };
        this.wv_year.addChangingListener(wheelListener_year);
        this.wv_month.addChangingListener(wheelListener_month);
    }

    public String processDate(int mYear2, int mMonth2, int mDay2) {
        String _month;
        String _day;
        CLog.e("processdate", "mYear:" + mYear2);
        if (mMonth2 < 10) {
            _month = "0" + mMonth2;
        } else {
            _month = new StringBuilder(String.valueOf(mMonth2)).toString();
        }
        if (mDay2 < 10) {
            _day = "0" + mDay2;
        } else {
            _day = new StringBuilder(String.valueOf(mDay2)).toString();
        }
        return String.valueOf(mYear2) + "-" + _month + "-" + _day;
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
                        Intent _intent = new Intent(RegisterBirthdayActivity.this, LoginActivity.class);
                        Bundle bundle = new Bundle();
                        if (RegisterBirthdayActivity.this.sp.getString("phoneNum") == null || RegisterBirthdayActivity.this.sp.getString("phoneNum").equalsIgnoreCase(bs.b)) {
                            String string = RegisterBirthdayActivity.this.sp.getString("cardUserId");
                            bundle.putString("user", bs.b);
                        } else {
                            bundle.putString("user", RegisterBirthdayActivity.this.sp.getString("phoneNum"));
                        }
                        bundle.putBoolean("isfromregist", true);
                        _intent.putExtras(bundle);
                        RegisterBirthdayActivity.this.startActivity(_intent);
                        RegisterBirthdayActivity.this.finish();
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
        this.yearItem = this.sp.getInt("yearItem", this.year);
        this.monthItem = this.sp.getInt("monthItem", this.month);
        this.dayItem = this.sp.getInt("dayItem", this.day);
        this.wv_year.setCurrentItem(this.yearItem);
        this.wv_month.setCurrentItem(this.monthItem);
        this.wv_day.setCurrentItem(this.dayItem);
        //MobclickAgent.onResume(this);
    }

    protected void onPause() {
        super.onPause();
        //MobclickAgent.onPause(this);
    }
}
