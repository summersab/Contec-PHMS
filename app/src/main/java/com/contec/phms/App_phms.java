package com.contec.phms;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import com.contec.phms.SearchDevice.SearchDevice;
import com.contec.phms.device.template.BluetoothLeDeviceService;
import com.contec.phms.device.template.BluetoothServerService;
import com.contec.phms.device.template.DataFilter;
import com.contec.phms.device.template.DataUploadSucceedNotificationService;
import com.contec.phms.device.template.DeviceService;
import com.contec.phms.device.template.PollingService;
import com.contec.phms.domain.WaitConnectDeviceBean;
import com.contec.phms.infos.ConfigInfo;
import com.contec.phms.infos.PatientInfo;
import com.contec.phms.infos.UserInfo;
import com.contec.phms.manager.device.DeviceBean;
import com.contec.phms.manager.device.DeviceManager;
import com.contec.phms.manager.message.InstantMessageService;
import com.contec.phms.manager.message.MessageManager;
import com.contec.phms.db.DatabaseHelper;
import com.contec.phms.upload.UploadService;
import com.contec.phms.util.CLog;
import com.contec.phms.util.Constants;
import com.contec.phms.util.MyActivityManager;
import com.contec.phms.util.PageUtil;
import com.contec.phms.util.PedometerSharepreferance;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import net.tsz.afinal.FinalHttp;
import de.greenrobot.event.EventBus;
import de.greenrobot.event.EventBusPostOnAys;
import de.greenrobot.event.EventBusPostOnBackGround;
import u.aly.bs;

public class App_phms extends Application {
    public static boolean ISPRESSHOME = false;
    public static boolean PHMSISSKILLED = false;
    public static String TAG = "App_phms";
    private static App_phms instance;
    public static boolean language_set_state = false;
    public static int mCurrentActivity = 0;
    public static List<Class<? extends BluetoothLeDeviceService>> mServiceNameList;
    public static SharedPreferences preferences;
    public static boolean time_interval_state = false;
    private List<Activity> activityList = new LinkedList();
    public ConfigInfo mConfigiInfo;
    public List<String> mContecDevice;
    public SharedPreferences mCurrentloginUserInfo;
    public EventBus mEventBus;
    public EventBusPostOnAys mEventBusPostOnAys;
    public EventBusPostOnBackGround mEventBusPostOnBackGround;
    public FinalHttp mFinalHttp;
    public DatabaseHelper mHelper;
    public PatientInfo mPatientInfo;
    private List<WaitConnectDeviceBean> mRemoveConnectDeviceBeanLists;
    private SharedPreferences mSharedPreferences;
    public UserInfo mUserInfo;
    private List<WaitConnectDeviceBean> mWaitConnectDeviceBeanLists;
    public int mstartcount = 0;
    public ArrayList<DeviceBean> showBeans = new ArrayList<>();

    public static App_phms getInstance() {
        return instance;
    }

    public String GetUserInfoNAME() {
        return this.mCurrentloginUserInfo.getString("username", bs.b);
    }

    public String GetUserInfoPASSWORD() {
        return this.mCurrentloginUserInfo.getString("password", bs.b);
    }

    public DatabaseHelper getDatabaseHelper(Context pContext) {
        if (this.mHelper == null) {
            this.mHelper = (DatabaseHelper) OpenHelperManager.getHelper(pContext, DatabaseHelper.class);
        }
        return this.mHelper;
    }

    public void destoryDataHelp() {
        if (this.mHelper != null) {
            OpenHelperManager.releaseHelper();
            this.mHelper = null;
        }
    }

    @SuppressLint({"NewApi"})
    public void onCreate() {
        super.onCreate();
        instance = this;
        CLog.e("lzerror", "程序第一次执行....." + PageUtil.getNowALLTime());
        preferences = getSharedPreferences("update", 0);
        this.mEventBus = new EventBus();
        this.mEventBusPostOnAys = new EventBusPostOnAys(this.mEventBus);
        this.mEventBusPostOnBackGround = new EventBusPostOnBackGround(this.mEventBus);
        this.mHelper = getDatabaseHelper(getApplicationContext());
        this.mFinalHttp = new FinalHttp();
        this.mFinalHttp.configTimeout(8000);
        this.mFinalHttp.configRequestExecutionRetryCount(1);
        this.mUserInfo = new UserInfo();
        this.mPatientInfo = new PatientInfo();
        this.mConfigiInfo = new ConfigInfo();
        this.mSharedPreferences = getSharedPreferences("BluetoothList", 0);
        this.mCurrentloginUserInfo = getSharedPreferences("CurrentloginUserInfo", 0);
        this.mWaitConnectDeviceBeanLists = new ArrayList();
        this.mRemoveConnectDeviceBeanLists = new ArrayList();
        mServiceNameList = new ArrayList();
        if (Build.VERSION.SDK_INT >= 14) {
            registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {
                public void onActivityStopped(Activity activity) {
                }

                public void onActivityStarted(Activity activity) {
                }

                public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
                }

                public void onActivityResumed(Activity activity) {
                    MyActivityManager.getInstance().setCurrentActivity(activity);
                }

                public void onActivityPaused(Activity activity) {
                }

                public void onActivityDestroyed(Activity activity) {
                }

                public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                }
            });
        }
    }

    public void addActivity(Activity activity) {
        this.activityList.add(activity);
    }

    public Activity getlastActivity() {
        return this.activityList.get(this.activityList.size() - 1);
    }

    public void exit(int pexitflag) {
        for (Activity activity : this.activityList) {
            if (activity != null) {
                activity.finish();
            }
        }
        clearDeviceList(pexitflag);
    }

    private void clearDeviceList(int pexitflag) {
        if (pexitflag == 0) {
            SharedPreferences.Editor _editor = this.mCurrentloginUserInfo.edit();
            _editor.putString("username", bs.b);
            _editor.putString("password", bs.b);
            _editor.commit();
        }
        if (DeviceManager.mDeviceList != null) {
            DeviceManager.mDeviceList.removeAll();
        }
        if (DeviceManager.mDeviceBeanList != null) {
            if (DeviceManager.mDeviceBeanList.mBeanList != null) {
                DeviceManager.mDeviceBeanList.mBeanList.clear();
            }
            DeviceManager.mWhichDevice = 0;
            DeviceManager.mWhichItem = 0;
            DeviceManager.mWhichSortIndex = 0;
        }
        if (DeviceManager.mDeviceList != null) {
            if (DeviceManager.mDeviceList.getListDevice() != null) {
                for (int i = 0; i < DeviceManager.mDeviceList.getListDevice().size(); i++) {
                    DeviceManager.mDeviceList.getListDevice().get(i).mBeanList.clear();
                }
                DeviceManager.mDeviceList.getListDevice().clear();
            }
            DeviceManager.mDeviceList.removeAll();
            DeviceManager.mDeviceList = null;
        }
        DeviceManager.m_DeviceBean = new DeviceBean(bs.b, bs.b);
    }

    public void clearActivity() {
        InstantMessageService.stopServer(this);
        new PedometerSharepreferance(this).setExit(true);
        BluetoothServerService.stopServer(this);
        finish_a();
        clearDeviceList(0);
        if (this.activityList != null && this.activityList.size() > 0) {
            for (Activity activity : this.activityList) {
                if (activity != null) {
                    activity.finish();
                }
            }
        }
    }

    public void onLowMemory() {
        super.onLowMemory();
        CLog.i("lz", "memory is low ");
    }

    public void exitall(int pexitflag) {
        InstantMessageService.stopServer(this);
        new PedometerSharepreferance(this).setExit(true);
        BluetoothServerService.stopServer(this);
        finish_a();
        exit(pexitflag);
    }

    public void finish_Activity() {
        SearchDevice.stopServer(this);
        DeviceManager.stopServer(this);
        UploadService.stopServer(this);
        MessageManager.stopServer(this);
        DeviceService.stopServer(this);
        Server_Main.stopServer(this);
        DataFilter.moveDataToFailedDir();
        if (Constants.OPEN_BLUE) {
            BluetoothAdapter.getDefaultAdapter().disable();
        }
    }

    public void finish_a() {
        SearchDevice.stopServer(this);
        DeviceManager.stopServer(this);
        UploadService.stopServer(this);
        MessageManager.stopServer(this);
        DeviceService.stopServer(this);
        Server_Main.stopServer(this);
        PollingService.stopService(this);
        DataUploadSucceedNotificationService.stopService(this);
        DataFilter.moveDataToFailedDir();
        if (Constants.OPEN_BLUE) {
            BluetoothAdapter.getDefaultAdapter().disable();
        }
        getInstance().getmRemoveConnectDeviceBeanLists().clear();
    }

    public void restartphms(Context pfromcontext, Class<?> ptocontext) {
        PHMSISSKILLED = false;
        Intent _intent = new Intent(pfromcontext, ptocontext);
        Bundle _bundle = new Bundle();
        String _username = this.mCurrentloginUserInfo.getString("username", bs.b);
        String _password = this.mCurrentloginUserInfo.getString("password", bs.b);
        if (_username.equals(bs.b) || _password.equals(bs.b)) {
            Toast.makeText(pfromcontext, getString(R.string.phmsrestartfailed), Toast.LENGTH_SHORT).show();
            exitall(1);
        }
        _bundle.putString("username", _username);
        _bundle.putString("password", _password);
        _bundle.putBoolean("isfromrestart", true);
        _intent.putExtras(_bundle);
        pfromcontext.startActivity(_intent);
    }

    public List<WaitConnectDeviceBean> getmWaitConnectDeviceBeanLists() {
        return this.mWaitConnectDeviceBeanLists;
    }

    public void setmWaitConnectDeviceBeanLists(List<WaitConnectDeviceBean> mWaitConnectDeviceBeanLists2) {
        this.mWaitConnectDeviceBeanLists.clear();
        this.mWaitConnectDeviceBeanLists.addAll(mWaitConnectDeviceBeanLists2);
    }

    public List<WaitConnectDeviceBean> getmRemoveConnectDeviceBeanLists() {
        return this.mRemoveConnectDeviceBeanLists;
    }

    public boolean isBackground(Context context) {
        for (ActivityManager.RunningAppProcessInfo appProcess : ((ActivityManager) context.getSystemService("activity")).getRunningAppProcesses()) {
            if (appProcess.processName.equals(context.getPackageName())) {
                Log.i(context.getPackageName(), "此appimportace =" + appProcess.importance + ",context.getClass().getName()=" + context.getClass().getName());
                if (appProcess.importance != 100) {
                    Log.i(context.getPackageName(), "处于后台" + appProcess.processName);
                    return true;
                }
                Log.i(context.getPackageName(), "处于前台" + appProcess.processName);
                return false;
            }
        }
        return false;
    }
}
