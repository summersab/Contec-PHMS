package com.contec.phms;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.Message;
import com.contec.phms.SearchDevice.SearchDevice;
import com.contec.phms.SearchDevice.SortDeviceContainer;
import com.contec.phms.SearchDevice.SortDeviceContainerList;
import com.contec.phms.device.template.BluetoothServerService;
import com.contec.phms.device.template.DataFilter;
import com.contec.phms.eventbus.EventFragment;
import com.contec.phms.manager.device.DeviceManager;
import com.contec.phms.manager.message.MessageManager;
import com.contec.phms.upload.UploadService;
import com.contec.phms.util.CLog;
import com.contec.phms.util.Constants;
import com.contec.phms.util.FileOperation;
import com.contec.phms.util.PageUtil;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import java.util.Timer;
import java.util.TimerTask;
import org.apache.commons.httpclient.cookie.CookieSpec;

public class Server_Main extends Service {
    public static Context mContext;
    public static boolean m_Start_First;
    final int SEARCH_INTERVAL = BaseImageDownloader.DEFAULT_HTTP_READ_TIMEOUT;
    public final String TAG = Server_Main.class.getSimpleName();
    Intent _deviceManager;
    Intent _messageManager;
    Intent _networkManager;
    Intent _notifications;
    Intent _searchDevice;
    Intent _uploadService;
    TimerTask task;
    Timer timer;

    void sercher() {
        Constants.CLEAR_LIST = false;
    }

    void task_init() {
        this.task = new TimerTask() {
            public void run() {
                Server_Main.this.timer = null;
                Server_Main.this.sercher();
            }
        };
    }

    public Server_Main() {
        Constants.CLEAR_LIST = false;
        initDirs();
    }

    public void onCreate() {
        super.onCreate();
        CLog.e(this.TAG, "开始主要的serivce");
        Constants.BLUETOOTHSTAT = 1;
        App_phms.getInstance().mEventBus.register(this);
        EventFragment _fragment = new EventFragment();
        _fragment.setmWhichCommand(3);
        App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(_fragment);
    }

    public void onDestroy() {
        super.onDestroy();
        App_phms.getInstance().mEventBus.unregister(this);
    }

    public static void stopServer(Context pContext) {
        pContext.stopService(new Intent(pContext, Server_Main.class));
    }

    public IBinder onBind(Intent intent) {
        return null;
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        mContext = getApplicationContext();
        Constants.BLUETOOTHSTAT = 1;
        startMessageManager();
        startUploadService();
        m_Start_First = true;
        return super.onStartCommand(intent, flags, startId);
    }

    public void startSearch() {
        this._searchDevice = new Intent(getApplicationContext(), SearchDevice.class);
        startService(this._searchDevice);
    }

    public void startDeviceManager() {
        this._deviceManager = new Intent(getApplicationContext(), DeviceManager.class);
        startService(this._deviceManager);
    }

    public void startMessageManager() {
        this._messageManager = new Intent(getApplicationContext(), MessageManager.class);
        startService(this._messageManager);
    }

    public void startUploadService() {
        this._uploadService = new Intent(getApplicationContext(), UploadService.class);
        startService(this._uploadService);
    }

    public void initDirs() {
        FileOperation.makeDirs(Constants.ContecPath);
        FileOperation.makeDirs(String.valueOf(Constants.ContecPath) + "/temp/");
        FileOperation.makeDirs(String.valueOf(Constants.DataPath) + CookieSpec.PATH_DELIM);
        FileOperation.makeDirs(String.valueOf(Constants.UPLOAD_FIAIL_DADT) + CookieSpec.PATH_DELIM);
    }

    public void startSearchTimer() {
        CLog.i("lzerror", "10s 开始搜索" + BluetoothServerService.isServerSocketValid);
        new Thread() {
            public void run() {
                if (!BluetoothServerService.isServerSocketValid) {
                    setName("server_main_wait_time");
                    try {
                        PageUtil.checkUserinfo(App_phms.getInstance().mUserInfo, Server_Main.this);
                        if (Server_Main.this.timer != null) {
                            Server_Main.this.timer.cancel();
                            Server_Main.this.timer.purge();
                            Server_Main.this.timer = null;
                            Server_Main.this.task = null;
                        }
                        Server_Main.this.timer = null;
                        Server_Main.this.task = null;
                        Server_Main.this.task_init();
                        Server_Main.this.timer = new Timer();
                        Server_Main.this.timer.schedule(Server_Main.this.task, (long) (App_phms.getInstance().mUserInfo.mSearchInterval * 1000));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }

    public void onEvent(Message msg) {
        if (msg.arg2 == 6) {
            switch (msg.what) {
                case 11:
                    Constants.CLEAR_LIST = true;
                    initDirs();
                    DataFilter.moveDataToFailedDir();
                    Constants.BLUETOOTHSTAT = 1;
                    startSearchTimer();
                    return;
                case 12:
                    startDeviceManager();
                    return;
                case 56:
                    prioritysearchdevice();
                    return;
                case 57:
                    updataprioritylist();
                    return;
                case 532:
                    if (this.timer != null) {
                        this.timer.cancel();
                        this.timer.purge();
                        this.timer = null;
                        new Thread() {
                            public void run() {
                                try {
                                    Thread.sleep(1500);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                Server_Main.this.sercher();
                            }
                        }.start();
                        return;
                    }
                    return;
                default:
                    return;
            }
        }
    }

    private void updataprioritylist() {
        CLog.i("lzdeldevi", "有设备被删除了，蓝牙此时状态是 ：" + Constants.BLUETOOTHSTAT);
        if (Constants.BLUETOOTHSTAT == 3) {
            CLog.i("zkq", "蓝牙正在连接  更新 插入排序了 发送广播了  3");
            Message msgs = new Message();
            msgs.what = 58;
            msgs.arg2 = 3;
            App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(msgs);
        }
    }

    private void prioritysearchdevice() {
        if (Constants.BLUETOOTHSTAT == 3) {
            CLog.i("zkq", "蓝牙正在连接   插入排序了 发送广播了  ");
            Message msgs = new Message();
            msgs.what = 55;
            msgs.arg2 = 3;
            App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(msgs);
            return;
        }
        CLog.i("zkq", "蓝牙不在连接，现在要判断蓝牙状态 124");
        Constants.ISFIRSTPRIORITYSORT = true;
        String _devicename = Constants.PRIORITY_DEVICENAME;
        SortDeviceContainerList.getInstance().addelement(new SortDeviceContainer(String.valueOf(_devicename) + Constants.PRIORITY_DEVICECODE));
        if (Constants.BLUETOOTHSTAT != 2 && Constants.BLUETOOTHSTAT != 4 && Constants.BLUETOOTHSTAT == 1) {
            startSearch();
        }
    }
}
