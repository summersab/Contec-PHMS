package com.contec.phms.device.pedometer;

import android.os.Environment;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

public class StepFilter {
    public static String TAG = "com.contec.pedometer.util.StepFilter";
    private ArrayList<Integer> mArrayInt = new ArrayList<>();
    private ArrayList<Point> mLastArrayPoint = new ArrayList<>();
    public int mLimit = 10;
    private float mThreshold = 0.0f;
    private float mThresholdJump = 1.0f;
    private int mTimeSpan = 200000;
    private int mTrend = -1;

    int intData(int[] data, DataConvert pDataConver, float limit) {
        int _tempTrrnd;
        this.mLimit = (int) limit;
        int _count = 0;
        int _max = data[0];
        int _min = data[0];
        int _lastInt = 0;
        int _sum = 0;
        for (int _point = 0; _point < data.length; _point++) {
            _sum += data[_point];
            if (data[_point] < _min) {
                _min = data[_point];
            }
            if (data[_point] > _max) {
                _max = data[_point];
            }
        }
        this.mThreshold = (float) (_sum / data.length);
        for (int i = 0; i < data.length; i++) {
            if (i == 0) {
                _lastInt = data[i];
                this.mArrayInt.add(Integer.valueOf(data[i]));
            } else {
                if (data[i] >= _lastInt) {
                    _tempTrrnd = 1;
                } else {
                    _tempTrrnd = 0;
                }
                _lastInt = data[i];
                if (_tempTrrnd == this.mTrend) {
                    this.mArrayInt.add(Integer.valueOf(data[i]));
                } else {
                    if (this.mTrend == 0) {
                        if (((float) this.mArrayInt.get(0).intValue()) >= this.mThreshold && ((float) this.mArrayInt.get(this.mArrayInt.size() - 1).intValue()) < this.mThreshold && Math.abs(this.mArrayInt.get(0).intValue() - this.mArrayInt.get(this.mArrayInt.size() - 1).intValue()) >= this.mLimit) {
                            _count++;
                            pDataConver.onStart();
                        }
                    } else if (((float) this.mArrayInt.get(0).intValue()) < this.mThreshold && ((float) this.mArrayInt.get(this.mArrayInt.size() - 1).intValue()) >= this.mThreshold && Math.abs(this.mArrayInt.get(0).intValue() - this.mArrayInt.get(this.mArrayInt.size() - 1).intValue()) >= this.mLimit) {
                        _count++;
                        pDataConver.onStart();
                    }
                    while (this.mArrayInt.size() != 0 && this.mArrayInt.size() != 1) {
                        this.mArrayInt.remove(0);
                    }
                    this.mArrayInt.add(Integer.valueOf(_lastInt));
                    this.mTrend = _tempTrrnd;
                }
            }
            if (i == data.length - 1) {
                if (this.mTrend == 0) {
                    if (((float) this.mArrayInt.get(0).intValue()) >= this.mThreshold && ((float) this.mArrayInt.get(this.mArrayInt.size() - 1).intValue()) < this.mThreshold && Math.abs(this.mArrayInt.get(0).intValue() - this.mArrayInt.get(this.mArrayInt.size() - 1).intValue()) >= this.mLimit) {
                        _count++;
                        pDataConver.onStart();
                    }
                } else if (((float) this.mArrayInt.get(0).intValue()) < this.mThreshold && ((float) this.mArrayInt.get(this.mArrayInt.size() - 1).intValue()) >= this.mThreshold && Math.abs(this.mArrayInt.get(0).intValue() - this.mArrayInt.get(this.mArrayInt.size() - 1).intValue()) >= this.mLimit) {
                    _count++;
                    pDataConver.onStart();
                }
                while (this.mArrayInt.size() != 0 && this.mArrayInt.size() != 1) {
                    this.mArrayInt.remove(0);
                }
            }
        }
        return _count;
    }

    public int initData(ArrayList<Point> pointer, float limit) {
        this.mLimit = (int) limit;
        ArrayList<Point> mArrayPoint = new ArrayList<>();
        ArrayList<Point> pointerNew = new ArrayList<>();
        for (int i = 0; i < this.mLastArrayPoint.size() + pointer.size(); i++) {
            if (i < this.mLastArrayPoint.size()) {
                pointerNew.add(this.mLastArrayPoint.get(i));
            } else {
                pointerNew.add(pointer.get(i - this.mLastArrayPoint.size()));
            }
        }
        if (pointerNew.size() == 0) {
            return 0;
        }
        int _count = 0;
        int _sum = 0;
        int _tempTrend = 0;
        float _min = (float) pointerNew.get(0).sqrt;
        float _max = (float) pointerNew.get(0).sqrt;
        Point _lastPointer = null;
        Point _lastDownPointer = null;
        for (int i2 = 0; i2 < pointerNew.size(); i2++) {
            _sum += pointerNew.get(i2).sqrt;
            if (((float) pointerNew.get(i2).sqrt) < _min) {
                _min = (float) pointerNew.get(i2).sqrt;
            }
            if (((float) pointerNew.get(i2).sqrt) > _max) {
                _max = (float) pointerNew.get(i2).sqrt;
            }
        }
        this.mThreshold = (float) (_sum / pointerNew.size());
        for (int i3 = 0; i3 < pointerNew.size(); i3++) {
            Point _tempPoint = pointerNew.get(i3);
            if (_lastPointer == null) {
                _lastPointer = _tempPoint;
                mArrayPoint.add(_tempPoint);
            } else {
                if (_tempPoint.sqrt > _lastPointer.sqrt) {
                    _tempTrend = 1;
                } else {
                    if (_tempPoint.sqrt < _lastPointer.sqrt) {
                        _tempTrend = 0;
                    }
                }
                _lastPointer = _tempPoint;
                if (this.mTrend == -1) {
                    this.mTrend = _tempTrend;
                }
                if (_tempTrend == this.mTrend) {
                    mArrayPoint.add(_tempPoint);
                    if (i3 == pointerNew.size() - 1) {
                        while (this.mLastArrayPoint.size() != 0) {
                            this.mLastArrayPoint.remove(0);
                        }
                        for (int k = 0; k < mArrayPoint.size(); k++) {
                            this.mLastArrayPoint.add(mArrayPoint.get(k));
                        }
                        this.mTrend = -1;
                    }
                } else {
                    if (this.mTrend == 0 && ((float) mArrayPoint.get(0).sqrt) >= this.mThreshold && ((float) mArrayPoint.get(mArrayPoint.size() - 1).sqrt) < this.mThreshold + 1.0f && 1.0f + this.mThreshold < ((float) mArrayPoint.get(0).sqrt)) {
                        long _tempTime = 0;
                        if (_lastDownPointer != null) {
                            _tempTime = _lastDownPointer.time;
                        }
                        if (mArrayPoint.get(mArrayPoint.size() - 1).time - _tempTime > ((long) this.mTimeSpan) && ((((float) mArrayPoint.get(mArrayPoint.size() - 1).sqrt) <= this.mThreshold - this.mThresholdJump || ((float) mArrayPoint.get(mArrayPoint.size() - 1).sqrt) >= this.mThreshold + this.mThresholdJump || ((float) mArrayPoint.get(mArrayPoint.size() - 1).sqrt) == this.mThreshold) && Math.abs(mArrayPoint.get(0).sqrt - mArrayPoint.get(mArrayPoint.size() - 1).sqrt) >= this.mLimit)) {
                            _lastDownPointer = mArrayPoint.get(mArrayPoint.size() - 1);
                            _count++;
                        }
                    }
                    while (mArrayPoint.size() != 0 && mArrayPoint.size() != 1) {
                        mArrayPoint.remove(0);
                    }
                    this.mTrend = _tempTrend;
                }
            }
        }
        return _count;
    }

    void write(ArrayList<Point> data) {
        try {
            OutputStream os = new FileOutputStream(new File(Environment.getExternalStorageDirectory().getPath()) + "/samho/" + System.currentTimeMillis() + ".dte");
            OutputStreamWriter osw = new OutputStreamWriter(os);
            for (int i = 0; i < data.size(); i++) {
                osw.write(String.valueOf(data.get(i).sqrt) + " ");
            }
            osw.close();
            os.flush();
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
