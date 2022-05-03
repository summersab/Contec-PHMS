package com.contec.phms.device.pedometer;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import com.contec.phms.util.CLog;
import java.util.ArrayList;

public class StepSensorEventListener implements SensorEventListener {
    private static final String TAG = "com.contec.phms.device.pedometer.StepSensorEventListener";
    private long mCurrentTimeConstruct;
    private DataConvert mDataConver;
    private StepFilter mFilter;
    private float mLimit;
    private ArrayList<Point> mPointList;

    public interface CallBack {
        void valueChanged(float f);
    }

    public StepSensorEventListener() {
        this.mLimit = 9.0f;
        this.mCurrentTimeConstruct = 0;
        this.mPointList = new ArrayList<>();
        this.mFilter = new StepFilter();
        this.mCurrentTimeConstruct = System.currentTimeMillis();
    }

    public void setSensitivity(float sensitivity) {
        this.mLimit = 12.0f - sensitivity;
    }

    public void setDataConvert(DataConvert pDataConver) {
        this.mDataConver = pDataConver;
    }

    public void onSensorChanged(SensorEvent event) {
        Sensor sensor = event.sensor;
        synchronized (this) {
            if (sensor.getType() != 1) {
                CLog.e(TAG, "Type= " + sensor.getType());
            }
            if (sensor.getType() == 3) {
                CLog.e(TAG, "changed direction");
            } else if (System.currentTimeMillis() - this.mCurrentTimeConstruct <= 1000) {
                this.mPointList.add(new Point(event.values, event.timestamp));
            } else {
                if (this.mFilter == null) {
                    this.mFilter = new StepFilter();
                }
                int _tempStep = this.mFilter.initData(this.mPointList, this.mLimit);
                for (int i = 0; i < _tempStep; i++) {
                    this.mDataConver.onStart();
                }
                while (this.mPointList.size() != 0) {
                    this.mPointList.remove(0);
                }
                this.mCurrentTimeConstruct = System.currentTimeMillis();
            }
        }
    }

    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }
}
