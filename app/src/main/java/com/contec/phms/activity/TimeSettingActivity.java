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
//import com.contec.phms.R;
import com.contec.phms.login.ActivityBase;
import com.contec.phms.db.UserInfoDao;
import com.contec.phms.util.Constants;
import com.contec.phms.util.ScreenAdapter;
import com.contec.R;

public class TimeSettingActivity extends ActivityBase {
    private BaseAdapter mBaseAdapter;
    private ListView mListView;
    int[] name = {5, 10, 20};

    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(1);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_time_interval_setting);
        App_phms.getInstance().addActivity(this);
        Button mback_but = (Button) findViewById(R.id.back_but);
        mback_but.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                TimeSettingActivity.this.finish();
            }
        });
        if (Constants.IS_PAD_NEW) {
            ScreenAdapter.changeLayoutTextSize(this, (RelativeLayout) findViewById(R.id.layout_timesetting_main), 10);
        }
        int height = getWindowManager().getDefaultDisplay().getHeight();
        int width = getWindowManager().getDefaultDisplay().getWidth();
        ((TextView) findViewById(R.id.title_text)).setText(getString(R.string.intervaltime_text));
        this.mListView = (ListView) findViewById(R.id.list_theme);
        this.mBaseAdapter = new mBaseAdapter(this);
        this.mListView.setAdapter(this.mBaseAdapter);
        this.mListView.setItemChecked(1, true);
        this.mListView.setSelection(1);
        this.mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View arg1, int position, long id) {
                TimeSettingActivity.this.finish();
                switch (position) {
                    case 0:
                        App_phms.getInstance().mUserInfo.mSearchInterval = 5;
                        TimeSettingActivity.this.mListView.setSelection(0);
                        break;
                    case 1:
                        App_phms.getInstance().mUserInfo.mSearchInterval = 10;
                        TimeSettingActivity.this.mListView.setSelection(1);
                        break;
                    case 2:
                        App_phms.getInstance().mUserInfo.mSearchInterval = 20;
                        TimeSettingActivity.this.mListView.setSelection(2);
                        break;
                }
                App_phms.time_interval_state = true;
            }
        });
        if (Constants.IS_PAD_NEW) {
            ScreenAdapter.titleLayoutToPad(this, (RelativeLayout) findViewById(R.id.linearlayout_title), mback_but);
        }
    }

    protected void onDestroy() {
        super.onDestroy();
        exitSaveInfo();
    }

    private void exitSaveInfo() {
        try {
            UserInfoDao _user = App_phms.getInstance().mHelper.getUserDao().queryForId(App_phms.getInstance().mUserInfo.mUserID);
            _user.setmSearchInterval(App_phms.getInstance().mUserInfo.mSearchInterval);
            App_phms.getInstance().mHelper.getUserDao().update(_user);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode != 4) {
            return false;
        }
        finish();
        return false;
    }

    class mBaseAdapter extends BaseAdapter {
        public Context mContext;

        public mBaseAdapter(Context pContext) {
            this.mContext = pContext;
        }

        public int getCount() {
            return TimeSettingActivity.this.name.length;
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
            if (TimeSettingActivity.this.name[position] == App_phms.getInstance().mUserInfo.mSearchInterval) {
                redio_imag.setImageResource(R.drawable.drawable_radio_btn_s);
            }
            menu_text.setText(String.valueOf(TimeSettingActivity.this.name[position % TimeSettingActivity.this.name.length]) + TimeSettingActivity.this.getString(R.string.s));
            return view;
        }
    }
}
