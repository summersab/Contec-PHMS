package com.contec.phms.activity;

import android.content.Context;
import android.os.Bundle;
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
import com.contec.phms.util.Constants;
import com.contec.phms.util.PhmsSharedPreferences;
import com.contec.phms.util.ScreenAdapter;
//import com.umeng.analytics.MobclickAgent;

public class ElectrocardiogramSetting extends ActivityBase {
    private ListView mListView;
    String[] name = {"auto", "zh", "en"};

    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(1);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_electrocardiogram_setting);
        App_phms.getInstance().addActivity(this);
        initView();
        initData();
    }

    private void initView() {
        int SCREENHEIGH = getWindowManager().getDefaultDisplay().getHeight();
        RelativeLayout.LayoutParams reLayoutParams = new RelativeLayout.LayoutParams((getWindowManager().getDefaultDisplay().getWidth() * 4) / 12, (SCREENHEIGH * 6) / 20);
        reLayoutParams.addRule(14, -1);
        reLayoutParams.topMargin = (SCREENHEIGH / 2) + 10;
        Button mback_but = (Button) findViewById(R.id.back_but);
        mback_but.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                ElectrocardiogramSetting.this.finish();
            }
        });
        if (Constants.IS_PAD_NEW) {
            ScreenAdapter.titleLayoutToPad(this, (RelativeLayout) findViewById(R.id.linearlayout_title), mback_but);
            ScreenAdapter.changeLayoutTextSize(this, (RelativeLayout) findViewById(R.id.rl_electrocardiogram_main), 10);
        }
        ((TextView) findViewById(R.id.title_text)).setText(getString(R.string.electrocardiogram_analyse));
        this.mListView = (ListView) findViewById(R.id.lv_electrocardiogram);
    }

    private void initData() {
        this.mListView.setAdapter(new mBaseAdapter(this));
        this.mListView.setItemChecked(1, true);
        this.mListView.setSelection(1);
        this.mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View arg1, int position, long id) {
                ElectrocardiogramSetting.this.finish();
                switch (position) {
                    case 0:
                        Constants.Electrocardiogram = "auto";
                        ElectrocardiogramSetting.this.mListView.setSelection(0);
                        break;
                    case 1:
                        Constants.Electrocardiogram = "doctor";
                        ElectrocardiogramSetting.this.mListView.setSelection(1);
                        break;
                }
                App_phms.language_set_state = true;
            }
        });
    }

    protected void onResume() {
        super.onResume();
        //MobclickAgent.onResume(this);
    }

    protected void onPause() {
        super.onPause();
        //MobclickAgent.onPause(this);
    }

    protected void onDestroy() {
        super.onDestroy();
        exitSaveInfo();
    }

    private void exitSaveInfo() {
        PhmsSharedPreferences.getInstance(getApplicationContext()).saveColume("Electrocardiogram", Constants.Electrocardiogram);
    }

    class mBaseAdapter extends BaseAdapter {
        public Context mContext;

        public mBaseAdapter(Context pContext) {
            this.mContext = pContext;
        }

        public int getCount() {
            return 2;
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
                mlayout.setBackgroundResource(R.drawable.drawable_inputbox_bottom);
            }
            if (Constants.IS_PAD_NEW) {
                ScreenAdapter.changeLayoutTextSize(this.mContext, mlayout, 10);
            }
            TextView menu_text = (TextView) view.findViewById(R.id.list_text);
            ImageView redio_imag = (ImageView) view.findViewById(R.id.list_radioImg);
            redio_imag.setImageResource(R.drawable.drawable_radio_btn);
            if (Constants.Electrocardiogram.trim().equals("auto")) {
                if (position == 0) {
                    redio_imag.setImageResource(R.drawable.drawable_radio_btn_s);
                }
            } else if (Constants.Electrocardiogram.trim().equals("doctor") && position == 1) {
                redio_imag.setImageResource(R.drawable.drawable_radio_btn_s);
            }
            if (position == 0) {
                menu_text.setText(R.string.theme_auto);
            } else if (position == 1) {
                menu_text.setText(R.string.doctor);
            }
            return view;
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode != 4) {
            return false;
        }
        finish();
        return false;
    }
}
