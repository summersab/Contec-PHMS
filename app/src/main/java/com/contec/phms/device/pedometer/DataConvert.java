package com.contec.phms.device.pedometer;

import com.contec.phms.util.CLog;
import com.contec.phms.util.PedometerSharepreferance;

public class DataConvert {
    private static double IMPERIAL_WALKING_FACTOR = 0.517d;
    private static double METRIC_WALKING_FACTOR = 0.708d;
    private int mBodyWeight;
    private CallBack mCallBack;
    private float mCalories;
    private float mDistance;
    private boolean mIsMetric = false;
    private boolean mPause = false;
    private PedometerSharepreferance mSharepreferance;
    private int mStepCount;
    private float mStepLength;

    public interface CallBack {
        void passValue();

        void valueChanged(float f, float f2, float f3);
    }

    public DataConvert(CallBack pCallBack, PedometerSharepreferance pSharepreferance) {
        this.mCallBack = pCallBack;
        this.mSharepreferance = pSharepreferance;
    }

    public void reSetCalries() {
        this.mCalories = 0.0f;
    }

    public void setPause(boolean flag) {
        this.mPause = flag;
    }

    public void reSetStepCount() {
        this.mStepCount = 0;
    }

    public void reSetDistance() {
        this.mDistance = 0.0f;
    }

    public void reSetCalries(float value) {
        this.mCalories = value;
    }

    public void reSetSteps(int value) {
        this.mStepCount = value;
    }

    public void reSetDistance(float value) {
        this.mDistance = value;
    }

    public void loadShare() {
        if (this.mSharepreferance.getUnit() == 1) {
            this.mIsMetric = true;
        } else {
            this.mIsMetric = false;
        }
        this.mStepLength = (float) this.mSharepreferance.getStep_length();
        this.mBodyWeight = this.mSharepreferance.getWeight();
    }

    public void onStart() {
        stepsStart();
        distanceStart();
        caloriesStart();
        notifyDataChange();
    }

    void stepsStart() {
        if (!this.mPause) {
            this.mStepCount++;
        }
    }

    void distanceStart() {
        CLog.e("com.contec.phms.device.pedometer.DataConvert", "mStepLength= " + this.mStepLength);
        if (this.mIsMetric) {
            if (!this.mPause) {
                this.mDistance += (float) (((double) this.mStepLength) / 100.0d);
            }
        } else if (!this.mPause) {
            this.mDistance += (float) (((double) this.mStepLength) / 12.0d);
        }
    }

    void caloriesStart() {
        if (this.mIsMetric) {
            if (!this.mPause) {
                this.mCalories = (float) (((double) this.mCalories) + (((((double) this.mBodyWeight) * METRIC_WALKING_FACTOR) * ((double) this.mStepLength)) / 100000.0d));
            }
        } else if (!this.mPause) {
            this.mCalories = (float) (((double) this.mCalories) + (((((double) this.mBodyWeight) * IMPERIAL_WALKING_FACTOR) * ((double) this.mStepLength)) / 63360.0d));
        }
    }

    void notifyDataChange() {
        this.mCallBack.valueChanged((float) this.mStepCount, this.mDistance, this.mCalories);
    }
}
