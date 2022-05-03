package com.contec.phms.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
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

public class Pedometer50DSetActivity extends Activity implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {
    private static final String TAG = "com.contec.phms.activity.PedometerSetActivity";
    private Button mBackBtn;
    private LoginUserDao mLoginUserDao;
    private float mSensitivity = 50.0f;
    private SeekBar mSensitivityBar;
    private PhmsSharedPreferences mSharepreferance;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_50d_pedometer_set);
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
        this.mBackBtn = (Button) findViewById(R.id.set_back_btn);
        this.mSensitivityBar = (SeekBar) findViewById(R.id.pedometer_set_sensitivity_progressbar);
        this.mBackBtn.setOnClickListener(this);
        this.mSensitivityBar.setOnSeekBarChangeListener(this);
        this.mSharepreferance = PhmsSharedPreferences.getInstance(this);
        this.mLoginUserDao = PageUtil.getLoginUserInfo();
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == 4) {
            saveSet();
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    public void onClick(View v) {
        if (v.getId() == R.id.set_back_btn) {
            saveSet();
            finish();
        }
    }

    private void saveSet() {
        int _sensitivity = 2;
        if (this.mSensitivity == 0.0f) {
            _sensitivity = 1;
        } else if (this.mSensitivity == 50.0f) {
            _sensitivity = 2;
        } else if (this.mSensitivity == 100.0f) {
            _sensitivity = 3;
        }
        this.mSharepreferance.saveInt("Sensitivity" + this.mLoginUserDao.mUID, _sensitivity);
    }

    void loadShare() {
        int _sensitivity = this.mSharepreferance.getInt("Sensitivity" + this.mLoginUserDao.mUID, 2);
        if (_sensitivity == 1) {
            this.mSensitivity = 0.0f;
        } else if (_sensitivity == 2) {
            this.mSensitivity = 50.0f;
        } else if (_sensitivity == 3) {
            this.mSensitivity = 100.0f;
        }
    }

    void iniSetUp() {
        this.mSensitivityBar.setProgress((int) this.mSensitivity);
    }

    String floatFormat(String pattern, float value) {
        return new DecimalFormat(pattern).format((double) value);
    }

    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (!seekBar.equals(this.mSensitivityBar)) {
            return;
        }
        if (progress >= 75) {
            seekBar.setProgress(100);
            this.mSensitivity = 100.0f;
        } else if (progress < 75 && progress > 25) {
            seekBar.setProgress(50);
            this.mSensitivity = 50.0f;
        } else if (progress <= 25) {
            seekBar.setProgress(0);
            this.mSensitivity = 0.0f;
        }
    }

    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    public void onStopTrackingTouch(SeekBar seekBar) {
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
