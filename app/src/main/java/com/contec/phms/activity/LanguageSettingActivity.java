package com.contec.phms.activity;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.contec.phms.App_phms;
import com.contec.phms.R;
import com.contec.phms.login.ActivityBase;
import com.contec.phms.db.UserInfoDao;
import com.contec.phms.util.Constants;
import com.contec.phms.util.PhmsSharedPreferences;
import com.contec.phms.util.ScreenAdapter;
//import com.umeng.analytics.MobclickAgent;
import java.util.Locale;
import u.aly.bs;

public class LanguageSettingActivity extends ActivityBase {
    private BaseAdapter mBaseAdapter;
    private ListView mListView;
    String[] name = {"auto", "zh", "en"};

    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(1);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_time_interval_setting);
        App_phms.getInstance().addActivity(this);
        Button mback_but = (Button) findViewById(R.id.back_but);
        mback_but.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                LanguageSettingActivity.this.finish();
            }
        });
        int SCREENHEIGH = getWindowManager().getDefaultDisplay().getHeight();
        RelativeLayout.LayoutParams reLayoutParams = new RelativeLayout.LayoutParams((getWindowManager().getDefaultDisplay().getWidth() * 4) / 12, (SCREENHEIGH * 6) / 20);
        reLayoutParams.addRule(14, -1);
        reLayoutParams.topMargin = (SCREENHEIGH / 2) + 10;
        ((TextView) findViewById(R.id.title_text)).setText(getString(R.string.language_text));
        this.mListView = (ListView) findViewById(R.id.list_theme);
        this.mBaseAdapter = new mBaseAdapter(this);
        this.mListView.setAdapter(this.mBaseAdapter);
        this.mListView.setItemChecked(1, true);
        this.mListView.setSelection(1);
        this.mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View arg1, int position, long id) {
                LanguageSettingActivity.this.finish();
                switch (position) {
                    case 0:
                        String _language = Locale.getDefault().getLanguage();
                        Configuration config2 = LanguageSettingActivity.this.getResources().getConfiguration();
                        DisplayMetrics dm2 = LanguageSettingActivity.this.getResources().getDisplayMetrics();
                        if (_language.equalsIgnoreCase("zh")) {
                            config2.locale = Locale.CHINESE;
                        } else {
                            config2.locale = Locale.ENGLISH;
                        }
                        LanguageSettingActivity.this.getResources().updateConfiguration(config2, dm2);
                        LanguageSettingActivity.this.mListView.setSelection(0);
                        Constants.Language = "1" + _language;
                        App_phms.getInstance().mUserInfo.mLanguage = Constants.Language;
                        break;
                    case 1:
                        Configuration config = LanguageSettingActivity.this.getResources().getConfiguration();
                        DisplayMetrics dm = LanguageSettingActivity.this.getResources().getDisplayMetrics();
                        config.locale = Locale.CHINESE;
                        LanguageSettingActivity.this.getResources().updateConfiguration(config, dm);
                        LanguageSettingActivity.this.mListView.setSelection(1);
                        Constants.Language = "zh";
                        App_phms.getInstance().mUserInfo.mLanguage = Constants.Language;
                        break;
                    case 2:
                        Configuration config1 = LanguageSettingActivity.this.getResources().getConfiguration();
                        DisplayMetrics dm1 = LanguageSettingActivity.this.getResources().getDisplayMetrics();
                        config1.locale = Locale.ENGLISH;
                        LanguageSettingActivity.this.getResources().updateConfiguration(config1, dm1);
                        LanguageSettingActivity.this.mListView.setSelection(2);
                        Constants.Language = "en";
                        App_phms.getInstance().mUserInfo.mLanguage = Constants.Language;
                        break;
                }
                App_phms.language_set_state = true;
            }
        });
        if (Constants.IS_PAD_NEW) {
            ScreenAdapter.titleLayoutToPad(this, (RelativeLayout) findViewById(R.id.linearlayout_title), mback_but);
            ScreenAdapter.changeLayoutTextSize(this, (RelativeLayout) findViewById(R.id.layout_timesetting_main), 10);
        }
    }

    protected void onDestroy() {
        super.onDestroy();
        exitSaveInfo();
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode != 4) {
            return false;
        }
        finish();
        return false;
    }

    private void exitSaveInfo() {
        PhmsSharedPreferences.getInstance(getApplicationContext()).saveColume(UserInfoDao.Language, Constants.Language);
    }

    class mBaseAdapter extends BaseAdapter {
        public Context mContext;

        public mBaseAdapter(Context pContext) {
            this.mContext = pContext;
        }

        public int getCount() {
            return LanguageSettingActivity.this.name.length;
        }

        public Object getItem(int position) {
            return Integer.valueOf(position);
        }

        public long getItemId(int position) {
            return (long) position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            View view = LayoutInflater.from(this.mContext).inflate(R.layout.layout_theme_item, (ViewGroup) null);
            LinearLayout mlayout = (LinearLayout) view.findViewById(R.id.layout);
            if (position == 0) {
                mlayout.setBackgroundResource(R.drawable.drawable_inputbox_top);
            } else if (position == 1) {
                mlayout.setBackgroundResource(R.drawable.drawable_inputbox_mid);
            } else if (position == 2) {
                mlayout.setBackgroundResource(R.drawable.drawable_inputbox_bottom);
            }
            if (Constants.IS_PAD_NEW) {
                ScreenAdapter.changeLayoutTextSize(this.mContext, mlayout, 10);
            }
            TextView menu_text = (TextView) view.findViewById(R.id.list_text);
            ImageView redio_imag = (ImageView) view.findViewById(R.id.list_radioImg);
            redio_imag.setImageResource(R.drawable.drawable_radio_btn);
            if (LanguageSettingActivity.this.name[position].equalsIgnoreCase(Constants.Language)) {
                redio_imag.setImageResource(R.drawable.drawable_radio_btn_s);
            } else if ((Constants.Language.length() == 3 || Constants.Language.equalsIgnoreCase(bs.b)) && LanguageSettingActivity.this.name[position].equalsIgnoreCase("auto")) {
                redio_imag.setImageResource(R.drawable.drawable_radio_btn_s);
            }
            if (LanguageSettingActivity.this.name[position].equalsIgnoreCase("zh")) {
                menu_text.setText(R.string.language_zh);
            } else if (LanguageSettingActivity.this.name[position].equalsIgnoreCase("en")) {
                menu_text.setText(R.string.language_en);
            } else {
                menu_text.setText(R.string.language_aut);
            }
            return view;
        }
    }

    protected void onResume() {
        super.onResume();
        //MobclickAgent.onResume(this);
    }

    protected void onPause() {
        super.onPause();
        //MobclickAgent.onPause(this);
    }
}
