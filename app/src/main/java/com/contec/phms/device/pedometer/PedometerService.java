package com.contec.phms.device.pedometer;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Binder;
import android.os.IBinder;
import android.os.Message;
import android.os.PowerManager;
import com.contec.phms.App_phms;
import com.contec.phms.activity.CustomNotification;
import com.contec.phms.util.CLog;
import com.contec.phms.util.Constants;
import com.contec.phms.util.PageUtil;
import com.contec.phms.util.PedometerSharepreferance;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import u.aly.bs;

public class PedometerService extends Service {
    private static final String TAG = "PedometerService";
    private boolean ISSTOPCOUTTIME = false;
    private final IBinder mBinder = new StepBinder();
    private CallBack mCallBack;
    private DataConvert.CallBack mCallBackDC;
    private float mCalories;
    private DataConvert mDataConvert;
    private float mDistance;
    private StepSensorEventListener mEventListener;
    private float mHistoryDaoCalories = 0.0f;
    private float mHistoryDaoSteps = 0.0f;
    private boolean mPause = false;
    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("android.intent.action.SCREEN_OFF")) {
                PedometerService.this.unregisterDetector();
                PedometerService.this.registerDetector();
                PedometerService.this.wakeLock.release();
                PedometerService.this.acquireWakeLock();
            }
        }
    };
    private int mSecond = 0;
    private Sensor mSensor;
    private SensorManager mSensorManager;
    private PedometerSharepreferance mSharepreferance;
    private float mSpeed;
    private String mStartTime;
    public float mSteps;
    private TimerTask mTask;
    private Timer mTimer = new Timer();
    private String mUserID;
    private String mUserName;
    private float mpreDistance = 0.0f;
    private PowerManager.WakeLock wakeLock;

    public interface CallBack {
        void caloriesChanged(float f);

        void distanceChanged(float f);

        void setUserName(String str);

        void speedChanged(float f);

        void startTime(String str);

        void stepsChanged(float f);

        void timeChanged(int i);
    }

    public void addCallBack(CallBack pCallBack) {
        this.mCallBack = pCallBack;
    }

    public void reSetData() {
        if (this.mDataConvert != null) {
            this.mDataConvert.reSetCalries();
            this.mDataConvert.reSetDistance();
            this.mDataConvert.reSetStepCount();
        }
        this.mSecond = 0;
        this.mSpeed = 0.0f;
    }

    public void setData(int steps, int time, float distance, float calries, float speed) {
        synchronized (this) {
            if (this.mDataConvert != null) {
                CLog.e(TAG, "setData() mDataConvert!=null");
                this.mDataConvert.reSetCalries(calries);
                this.mDataConvert.reSetDistance(distance);
                this.mDataConvert.reSetSteps(steps);
            }
            this.mSecond = time;
            this.mDistance = distance;
            this.mSteps = (float) steps;
            this.mCalories = calries;
            this.mSpeed = speed;
        }
    }

    public void setDistance(float dis, float speed) {
        synchronized (this) {
            if (this.mDataConvert != null) {
                CLog.e(TAG, "setData() mDataConvert!=null");
                this.mDataConvert.reSetDistance(dis);
                this.mDistance = dis;
            }
            this.mSpeed = speed;
        }
    }

    public void reSetShare() {
        if (this.mDataConvert != null) {
            this.mDataConvert.loadShare();
        }
        if (this.mEventListener != null && this.mSharepreferance != null) {
            this.mEventListener.setSensitivity(this.mSharepreferance.getSensitivity());
        }
    }

    public int getUnit() {
        return this.mSharepreferance.getUnit();
    }

    public void onCreate() {
        super.onCreate();
        acquireWakeLock();
        App_phms.getInstance().mEventBus.register(this);
        this.mStartTime = new SimpleDateFormat("HH:mm:ss").format(new Date());
        initConvert();
        this.mSharepreferance = new PedometerSharepreferance(this);
        this.mEventListener = new StepSensorEventListener();
        this.mDataConvert = new DataConvert(this.mCallBackDC, this.mSharepreferance);
        this.mEventListener.setSensitivity(this.mSharepreferance.getSensitivity());
        this.mEventListener.setDataConvert(this.mDataConvert);
        this.mSensorManager = (SensorManager) getSystemService("sensor");
        registerDetector();
        registerReceiver(this.mReceiver, new IntentFilter("android.intent.action.SCREEN_OFF"));
        startTimer();
        this.ISSTOPCOUTTIME = true;
        new CountTimeThread().start();
        App_phms.getInstance().mCurrentloginUserInfo.edit().putString("LastSavePedometerDateTime", new SimpleDateFormat("yyyy-MM-dd HH:mm").format(Long.valueOf(System.currentTimeMillis()))).commit();
    }

    class CountTimeThread extends Thread {
        CountTimeThread() {
        }

        public void run() {
            super.run();
            while (PedometerService.this.ISSTOPCOUTTIME) {
                try {
                    Thread.sleep(300000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (PedometerService.this.ISSTOPCOUTTIME) {
                    PedometerService.this.mHistoryDaoSteps = PedometerService.this.mSteps - PedometerService.this.mHistoryDaoSteps;
                    PedometerService.this.mHistoryDaoCalories = PedometerService.this.mCalories - PedometerService.this.mHistoryDaoCalories;
                    PedometerService.this.saveStepData(0);
                }
            }
        }
    }

    private void initConvert() {
        this.mCallBackDC = new DataConvert.CallBack() {
            public void valueChanged(float steps, float distance, float calu) {
                PedometerService.this.mSteps = steps;
                PedometerService.this.mCalories = calu;
                PedometerService.this.mDistance = distance;
                Message msg = new Message();
                msg.what = Constants.V_UPDATE_PEDOMETER_SUM_STEPS;
                msg.arg2 = 1;
                msg.arg1 = (int) PedometerService.this.mSteps;
                if (PedometerService.this.mUserID == null) {
                    msg.obj = null;
                } else if (PedometerService.this.mUserID.equals(bs.b)) {
                    msg.obj = null;
                } else {
                    msg.obj = App_phms.getInstance().mUserInfo;
                }
                App_phms.getInstance().mEventBus.post(msg);
                if (PedometerService.this.mCallBack != null) {
                    PedometerService.this.mCallBack.setUserName(PedometerService.this.mUserID);
                    PedometerService.this.mCallBack.startTime(PedometerService.this.mStartTime);
                    PedometerService.this.mCallBack.stepsChanged(PedometerService.this.mSteps);
                    PedometerService.this.mCallBack.caloriesChanged(PedometerService.this.mCalories);
                    PedometerService.this.mCallBack.distanceChanged(PedometerService.this.mDistance);
                }
            }

            public void passValue() {
            }
        };
    }

    public IBinder onBind(Intent intent) {
        return this.mBinder;
    }

    private void registerDetector() {
        this.mSensor = this.mSensorManager.getDefaultSensor(1);
        this.mSensorManager.registerListener(this.mEventListener, this.mSensor, 0);
    }

    private void unregisterDetector() {
        this.mSensorManager.unregisterListener(this.mEventListener);
    }

    public class StepBinder extends Binder {
        public StepBinder() {
        }

        public PedometerService getService() {
            return PedometerService.this;
        }
    }

    void startTimer() {
        this.mTask = new TimerTask() {
            public void run() {
                if (!PedometerService.this.mPause) {
                    PedometerService pedometerService = PedometerService.this;
                    pedometerService.mSecond = pedometerService.mSecond + 1;
                    if (PedometerService.this.mSecond != 0 && PedometerService.this.mSecond % 2 == 0) {
                        PedometerService.this.mSpeed = (PedometerService.this.mDistance - PedometerService.this.mpreDistance) / 2.0f;
                        CLog.i("lzc", "mpreDistance = " + PedometerService.this.mpreDistance + "   mDistance = " + PedometerService.this.mDistance);
                        PedometerService.this.mpreDistance = PedometerService.this.mDistance;
                    }
                    if (PedometerService.this.mCallBack != null) {
                        PedometerService.this.mCallBack.timeChanged(PedometerService.this.mSecond);
                        if (PedometerService.this.mSecond % 2 == 0) {
                            PedometerService.this.mCallBack.speedChanged(PedometerService.this.mSpeed);
                        }
                    }
                }
            }
        };
        this.mTimer.schedule(this.mTask, 1000, 1000);
    }

    public void timePause() {
        if (this.mTimer != null) {
            this.mTimer.cancel();
        }
    }

    public void timeResume() {
        if (this.mTimer != null && this.mTask != null) {
            this.mTimer.schedule(this.mTask, 1000, 1000);
        }
    }

    public void onDestroy() {
        CLog.e(TAG, "onDestroy()");
        this.ISSTOPCOUTTIME = false;
        CLog.i("more", "onDestroy save .");
        this.mSharepreferance.setBackPedometer(false);
        this.mSharepreferance.setUserID(bs.b);
        this.mSharepreferance.setUserName(bs.b);
        timePause();
        unregisterReceiver(this.mReceiver);
        unregisterDetector();
        App_phms.getInstance().mEventBus.unregister(this);
        this.wakeLock.release();
        super.onDestroy();
    }

    public void refresh() {
        if (this.mDataConvert != null) {
            this.mDataConvert.notifyDataChange();
        }
        if (this.mCallBack != null) {
            this.mCallBack.timeChanged(this.mSecond);
            this.mCallBack.speedChanged(this.mSpeed);
        }
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        boolean z = false;
        boolean isLogined = false;
        if (intent != null) {
            isLogined = intent.getBooleanExtra(Constants.KEY_IS_VIS_HISTORY, false);
        }
        if (isLogined) {
            String _userid = PageUtil.getLoginUserInfo().mUID;
            String _username = PageUtil.getLoginUserInfo().mUserName;
            this.mUserID = _userid;
            this.mUserName = _username;
        } else if (App_phms.getInstance().mUserInfo == null || App_phms.getInstance().mUserInfo.mUserID == null) {
            this.mUserID = bs.b;
            this.mUserName = bs.b;
        } else {
            this.mUserID = App_phms.getInstance().mUserInfo.mUserID;
            this.mUserName = App_phms.getInstance().mUserInfo.mUserName;
        }
        this.mSharepreferance.setUserID(this.mUserID);
        this.mSharepreferance.setUserName(this.mUserName);
        StringBuilder sb = new StringBuilder("onstartcommand: 是否登录：");
        if (!isLogined) {
            z = true;
        }
        CLog.d(TAG, sb.append(z).append("  mUserID:").append(this.mUserID).toString());
        initConvert();
        return super.onStartCommand(intent, flags, startId);
    }

    private void saveStepData(int pChangUserLogin) {
        ConstantSaveStepData _step = new ConstantSaveStepData();
        _step.mUserID = this.mUserID;
        _step.mStartTimeStr = this.mStartTime;
        _step.mStepsInt = (int) this.mHistoryDaoSteps;
        _step.mTimeInt = this.mSecond;
        _step.mDistance = (int) this.mDistance;
        _step.mCalories = this.mHistoryDaoCalories;
        _step.mUnit = getUnit();
        _step.mHistoryDaoStep = (int) this.mHistoryDaoSteps;
        _step.mHistoryCalories = this.mHistoryDaoCalories;
        _step.mChangUserLogin = pChangUserLogin;
        if (this.mUserID.length() > 0) {
            _step.isVisibleHistoryBtn = false;
        } else {
            _step.isVisibleHistoryBtn = true;
        }
        CLog.d(TAG, "保存前的数据状态:" + _step.toString());
        _step.saveToDB();
        this.mHistoryDaoSteps = this.mSteps;
        this.mHistoryDaoCalories = this.mCalories;
    }

    public void setPause(boolean flag) {
        this.mPause = flag;
        if (this.mDataConvert != null) {
            this.mDataConvert.setPause(this.mPause);
        }
        if (this.mCallBack != null) {
            this.mCallBack.speedChanged(0.0f);
            this.mpreDistance = this.mDistance;
        }
    }

    public boolean getPause() {
        return this.mPause;
    }

    public void onEvent(Message msg) {
        if (msg.arg2 != 7) {
            return;
        }
        if (msg.what == 512) {
            this.mHistoryDaoSteps = this.mSteps - this.mHistoryDaoSteps;
            this.mHistoryDaoCalories = this.mCalories - this.mHistoryDaoCalories;
            saveStepData(msg.arg1);
            CLog.i("more", "event save .");
            CustomNotification.getInstance(this).clearNotification();
            this.mSharepreferance.setUserID(bs.b);
            this.mSharepreferance.setUserName(bs.b);
            stopSelf();
            CLog.d(TAG, "停止计步器的service");
        } else if (msg.what == 514) {
            Message msga = new Message();
            msga.what = Constants.V_UPDATE_PEDOMETER_SUM_STEPS;
            msga.arg2 = 1;
            msga.arg1 = (int) this.mSteps;
            this.mUserID = App_phms.getInstance().mUserInfo.mUserID;
            this.mUserName = App_phms.getInstance().mUserInfo.mUserName;
            msga.obj = App_phms.getInstance().mUserInfo;
            App_phms.getInstance().mEventBus.post(msga);
            CLog.d(TAG, "更新主界面总步数" + msga.obj.toString());
        } else if (msg.what == 516) {
            Message msga2 = new Message();
            msga2.what = Constants.V_UPDATE_PEDOMETER_SUM_STEPS;
            msga2.arg2 = 1;
            msga2.arg1 = (int) this.mSteps;
            this.mUserID = App_phms.getInstance().mUserInfo.mUserID;
            this.mUserName = App_phms.getInstance().mUserInfo.mUserName;
            this.mSharepreferance.setUserID(this.mUserID);
            this.mSharepreferance.setUserName(this.mUserName);
            msga2.obj = App_phms.getInstance().mUserInfo;
            App_phms.getInstance().mEventBus.post(msga2);
            CLog.d(TAG, "更新计步器notification中的 user名称");
        }
    }

    private void acquireWakeLock() {
        this.wakeLock = ((PowerManager) getSystemService("power")).newWakeLock(1, TAG);
        this.wakeLock.acquire();
    }
}
