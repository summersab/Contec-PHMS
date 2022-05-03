package com.contec.phms.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import com.contec.phms.App_phms;
import com.contec.phms.R;
import com.contec.phms.util.CLog;
import com.contec.phms.util.Constants;
import com.contec.phms.util.PedometerSharepreferance;
import com.contec.phms.util.ScreenAdapter;
//import com.umeng.analytics.MobclickAgent;
import java.text.DecimalFormat;
import java.util.Locale;

public class PedometerSetActivity extends Activity implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {
    private static final float CMTOINCH = 0.4f;
    private static final float INCHTOCM = 2.5f;
    private static final String TAG = "com.contec.phms.activity.PedometerSetActivity";
    private Button mBackBtn;
    private int mFlagLanguage = 1;
    private ImageView mKmMileImg;
    private int mKmMileSystem = 1;
    private float mSensitivity = 81.0f;
    private SeekBar mSensitivityBar;
    private PedometerSharepreferance mSharepreferance;
    private TextView mStepLength;
    private SeekBar mStepLengthBar;
    private TextView mStepLengthUnit;
    private TextView mStepLimit;
    private int mSteps;
    private SeekBar mStepsBar;
    private int mStepsLength = 60;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_pedometer_set);
        App_phms.getInstance().addActivity(this);
        initView();
        judgeLanuage();
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
        this.mSharepreferance = new PedometerSharepreferance(this);
        this.mBackBtn = (Button) findViewById(R.id.set_back_btn);
        this.mStepsBar = (SeekBar) findViewById(R.id.pedometer_set_steps_num_progressbar);
        this.mStepLengthBar = (SeekBar) findViewById(R.id.pedometer_set_steps_length_progressbar);
        this.mSensitivityBar = (SeekBar) findViewById(R.id.pedometer_set_sensitivity_progressbar);
        this.mKmMileImg = (ImageView) findViewById(R.id.pedometer_km_miles);
        this.mStepLimit = (TextView) findViewById(R.id.step_every_day_textview);
        this.mStepLength = (TextView) findViewById(R.id.step_length_textvie);
        this.mStepLengthUnit = (TextView) findViewById(R.id.step_length_unit);
        this.mKmMileImg.setOnClickListener(this);
        this.mBackBtn.setOnClickListener(this);
        this.mStepsBar.setProgress(this.mSharepreferance.getTarget());
        this.mStepsBar.setOnSeekBarChangeListener(this);
        this.mStepLengthBar.setOnSeekBarChangeListener(this);
        this.mSensitivityBar.setOnSeekBarChangeListener(this);
    }

    void judgeLanuage() {
        if (Constants.Language.equalsIgnoreCase("zh")) {
            this.mFlagLanguage = 0;
        } else if (Constants.Language.equalsIgnoreCase("en")) {
            this.mFlagLanguage = 1;
        } else {
            String _language = Locale.getDefault().getLanguage();
            if (_language.equals("en")) {
                this.mFlagLanguage = 1;
            } else if (_language.equals("zh")) {
                this.mFlagLanguage = 0;
            }
        }
        Log.e(TAG, "mFlagLanguage= " + this.mFlagLanguage);
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
        } else if (v.getId() != R.id.pedometer_km_miles) {
        } else {
            if (this.mKmMileSystem == 1) {
                if (this.mFlagLanguage == 0) {
                    this.mKmMileImg.setImageResource(R.drawable.drawable_switch_inch);
                } else {
                    this.mKmMileImg.setImageResource(R.drawable.drawable_switch_mile);
                }
                this.mStepLengthUnit.setText(R.string.str_pedometer_step_length_in);
                this.mStepLength.setText(floatFormat("#0", ((float) this.mStepsLength) * CMTOINCH));
                this.mStepsLength = (int) (((float) this.mStepsLength) * CMTOINCH);
                CLog.e(TAG, "mStepsLength UNIT_METRIC= " + this.mStepsLength);
                this.mKmMileSystem = 2;
            } else if (this.mKmMileSystem == 2) {
                if (this.mFlagLanguage == 0) {
                    this.mKmMileImg.setImageResource(R.drawable.drawable_switch_metric);
                } else {
                    this.mKmMileImg.setImageResource(R.drawable.drawable_switch_km);
                }
                this.mStepLengthUnit.setText(R.string.str_pedometer_step_length_mm);
                this.mStepLength.setText(floatFormat("#0", ((float) this.mStepsLength) * INCHTOCM));
                this.mStepsLength = (int) (((float) this.mStepsLength) * INCHTOCM);
                CLog.e(TAG, "mStepsLength UNIT_ENGLISH= " + this.mStepsLength);
                this.mKmMileSystem = 1;
            }
        }
    }

    private void saveSet() {
        this.mSharepreferance.setWeight(60);
        this.mSharepreferance.setStep_length(this.mStepsLength);
        this.mSharepreferance.setSensitivity(this.mSensitivity);
        this.mSharepreferance.setUnit(this.mKmMileSystem);
        this.mSharepreferance.setTarget(this.mSteps);
        this.mSharepreferance.close();
    }

    void loadShare() {
        this.mSteps = this.mSharepreferance.getTarget();
        this.mStepsLength = this.mSharepreferance.getStep_length();
        this.mKmMileSystem = this.mSharepreferance.getUnit();
        this.mSensitivity = this.mSharepreferance.getSensitivity();
    }

    void iniSetUp() {
        this.mStepLimit.setText(new StringBuilder(String.valueOf(this.mSteps)).toString());
        this.mStepLength.setText(new StringBuilder(String.valueOf(this.mStepsLength)).toString());
        this.mStepsBar.setProgress(this.mSteps);
        this.mSensitivityBar.setProgress((int) this.mSensitivity);
        if (this.mKmMileSystem == 1) {
            if (this.mFlagLanguage == 0) {
                this.mKmMileImg.setImageResource(R.drawable.drawable_switch_metric);
            } else {
                this.mKmMileImg.setImageResource(R.drawable.drawable_switch_km);
            }
            this.mStepLengthUnit.setText(R.string.str_pedometer_step_length_mm);
            this.mStepLengthBar.setProgress(this.mStepsLength);
        } else if (this.mKmMileSystem == 2) {
            if (this.mFlagLanguage == 0) {
                this.mKmMileImg.setImageResource(R.drawable.drawable_switch_inch);
            } else {
                this.mKmMileImg.setImageResource(R.drawable.drawable_switch_mile);
            }
            this.mStepLengthUnit.setText(R.string.str_pedometer_step_length_in);
            this.mStepLengthBar.setProgress((int) (((float) this.mStepsLength) * INCHTOCM));
        }
    }

    String floatFormat(String pattern, float value) {
        return new DecimalFormat(pattern).format((double) value);
    }

    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (seekBar.equals(this.mStepsBar)) {
            int progress2 = (progress / 1000) * 1000;
            this.mStepLimit.setText(new StringBuilder(String.valueOf(progress2)).toString());
            this.mSteps = progress2;
            seekBar.setProgress(progress2);
        } else if (seekBar.equals(this.mStepLengthBar)) {
            if (this.mKmMileSystem == 1) {
                this.mStepLength.setText(new StringBuilder(String.valueOf(progress)).toString());
            } else if (this.mKmMileSystem == 2) {
                TextView textView = this.mStepLength;
                progress = (int) (((float) progress) * CMTOINCH);
                textView.setText(new StringBuilder(String.valueOf(progress)).toString());
            }
            this.mStepsLength = progress;
        } else if (seekBar.equals(this.mSensitivityBar)) {
            seekBar.setProgress(progress);
            this.mSensitivity = (float) progress;
        }
    }

    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    public void onStopTrackingTouch(SeekBar seekBar) {
        if (!seekBar.equals(this.mStepsBar) && !seekBar.equals(this.mStepLengthBar)) {
            seekBar.equals(this.mSensitivityBar);
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
