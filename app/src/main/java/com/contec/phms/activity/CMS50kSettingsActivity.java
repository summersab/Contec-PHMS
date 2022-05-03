package com.contec.phms.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import com.contec.phms.App_phms;
import com.contec.phms.R;
import com.contec.phms.db.LoginUserDao;
import com.contec.phms.util.Constants;
import com.contec.phms.util.PageUtil;
import com.contec.phms.util.PhmsSharedPreferences;
import com.contec.phms.util.ScreenAdapter;
//import com.umeng.analytics.MobclickAgent;
import java.text.DecimalFormat;

public class CMS50kSettingsActivity extends Activity implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {
    private static final String TAG = "com.contec.phms.activity.ScreenLightActivity";
    private boolean isOpen = false;
    private Button mBackBtn;
    private ImageView mImView;
    private LoginUserDao mLoginUserDao;
    private float mScreenLight = 0.0f;
    private SeekBar mScreenLightBar;
    private TextView mSetText;
    private PhmsSharedPreferences mSharepreferance;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_screen_pedometer_set);
        App_phms.getInstance().addActivity(this);
        initView();
        loadShare();
        iniSetUp();
        if (Constants.IS_PAD_NEW) {
            ((TextView) findViewById(R.id.title_text)).getLayoutParams().height += 25;
            this.mBackBtn.getLayoutParams().height += 22;
            this.mBackBtn.getLayoutParams().width += 22;
            ScreenAdapter.changeLayoutTextSize(this, (RelativeLayout) findViewById(R.id.layout_pedometerset_main), 10);
        }
    }

    private void initView() {
        this.mBackBtn = (Button) findViewById(R.id.ll_50k_back);
        this.mScreenLightBar = (SeekBar) findViewById(R.id.pedometer_set_sensitivity_progressbar);
        this.mBackBtn.setOnClickListener(this);
        this.mScreenLightBar.setOnSeekBarChangeListener(this);
        this.mSharepreferance = PhmsSharedPreferences.getInstance(this);
        this.mLoginUserDao = PageUtil.getLoginUserInfo();
        this.mSetText = (TextView) findViewById(R.id.screen_text);
        this.mImView = (ImageView) findViewById(R.id.screen_on);
        this.mImView.setOnClickListener(this);
    }

    protected void onResume() {
        super.onResume();
        //MobclickAgent.onResume(this);
    }

    protected void onPause() {
        super.onPause();
        //MobclickAgent.onPause(this);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == 4) {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_50k_back:
                finish();
                return;
            case R.id.screen_on:
                if (this.isOpen) {
                    this.mSetText.setText(R.string.set_50k_auto);
                    this.mImView.setImageResource(R.drawable.drawable_switch_off);
                    this.isOpen = false;
                    return;
                }
                this.mSetText.setText(R.string.set_50k_doc);
                this.mImView.setImageResource(R.drawable.drawable_switch_on);
                this.isOpen = true;
                return;
            default:
                return;
        }
    }

    private void saveSet() {
        int _sensitivity = 2;
        if (this.mScreenLight == 0.0f) {
            _sensitivity = 0;
        } else if (this.mScreenLight == 33.0f) {
            _sensitivity = 1;
        } else if (this.mScreenLight == 66.0f) {
            _sensitivity = 2;
        } else if (this.mScreenLight == 100.0f) {
            _sensitivity = 3;
        }
        SharedPreferences.Editor editor = getSharedPreferences("preference", 0).edit();
        editor.putInt("ScreenLight", _sensitivity);
        editor.commit();
        this.mSharepreferance.saveInt("ScreenLight" + this.mLoginUserDao.mUID, _sensitivity);
        if (this.isOpen) {
            this.mSharepreferance.saveColume("cms50k_card_set", "doctor");
        } else {
            this.mSharepreferance.saveColume("cms50k_card_set", "auto");
        }
    }

    void loadShare() {
        int _screenLight = this.mSharepreferance.getInt("ScreenLight" + this.mLoginUserDao.mUID, 2);
        String cardSet = this.mSharepreferance.getString("cms50k_card_set", "auto");
        if (_screenLight == 0) {
            this.mScreenLight = 0.0f;
        } else if (_screenLight == 1) {
            this.mScreenLight = 33.0f;
        } else if (_screenLight == 2) {
            this.mScreenLight = 66.0f;
        } else if (_screenLight == 3) {
            this.mScreenLight = 100.0f;
        }
        if (cardSet.equalsIgnoreCase("auto")) {
            this.mSetText.setText(R.string.set_50k_auto);
            this.mImView.setImageResource(R.drawable.drawable_switch_off);
            this.isOpen = false;
            return;
        }
        this.mSetText.setText(R.string.set_50k_doc);
        this.mImView.setImageResource(R.drawable.drawable_switch_on);
        this.isOpen = true;
    }

    void iniSetUp() {
        this.mScreenLightBar.setProgress((int) this.mScreenLight);
    }

    String floatFormat(String pattern, float value) {
        return new DecimalFormat(pattern).format((double) value);
    }

    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (!seekBar.equals(this.mScreenLightBar)) {
            return;
        }
        if (progress > 72) {
            seekBar.setProgress(100);
            this.mScreenLight = 100.0f;
        } else if (progress < 72 && progress > 50) {
            seekBar.setProgress(66);
            this.mScreenLight = 66.0f;
        } else if (progress <= 50 && progress > 20) {
            seekBar.setProgress(33);
            this.mScreenLight = 33.0f;
        } else if (progress <= 20) {
            seekBar.setProgress(0);
            this.mScreenLight = 0.0f;
        }
    }

    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    public void onStopTrackingTouch(SeekBar seekBar) {
    }

    protected void onDestroy() {
        super.onDestroy();
        saveSet();
    }
}
