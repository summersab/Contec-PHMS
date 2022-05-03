package com.contec.phms.device.template;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.Message;
import com.contec.phms.App_phms;
import com.contec.phms.manager.datas.DatasContainer;
import com.contec.phms.manager.device.DeviceManager;
import com.contec.phms.manager.device.ServiceBean;
import com.contec.phms.manager.message.MessageManager;
import com.contec.phms.manager.message.OrderList;
import com.contec.phms.util.CLog;
import com.contec.phms.util.Constants;
import com.contec.phms.util.TimerUtil;
import com.contec.phms.widget.DialogClass;
import java.io.IOException;

public abstract class DeviceService extends Service {
    public static final String TAG = "DeviceService";
    public static boolean mReceiveFinished = true;
    public static boolean synchronous;
    private boolean ifstartnext;
    BluetoothAdapter mAdapter;
    public BluetoothLeDeviceService mBLEService;
    BTConnect mBTConnect;
    DialogClass mClass;
    protected DataFilter mDataFilter;
    protected DatasContainer mDatasContainer;
    BluetoothDevice mDevice;
    private String mDeviceAddress;
    private boolean mOneNum = false;
    public PackManager mPackManager;
    protected ReceiveThread mReceiveThread;
    protected SaveSDCard mSaveSDCard;
    protected SendCommand mSendCommand;
    ServiceBean mServiceBean;
    BluetoothSocket mSocket;
    boolean m_is_register = false;

    public abstract void initObjects();

    public abstract void saveSDCard();

    public abstract void sendCommand();

    public IBinder onBind(Intent intent) {
        return null;
    }

    public static void stopServer(Context pContext) {
        pContext.stopService(new Intent(pContext, MessageManager.class));
    }

    public void onCreate() {
        super.onCreate();
        App_phms.getInstance().mEventBus.register(this);
    }

    public void onDestroy() {
        super.onDestroy();
        try {
            CLog.e(TAG, " onDestroy  deviceservice****");
            clearVariables();
            App_phms.getInstance().mEventBus.unregister(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        if (intent != null) {
            this.ifstartnext = intent.getBooleanExtra("server", false);
            this.mOneNum = true;
            CLog.i(TAG, "onStartCommand  是否是从 回联server 过来的： " + this.ifstartnext);
            if (intent == null || this.ifstartnext) {
                CLog.i(TAG, "startReceive  回联过来的 开始初始化DatasContainer*********");
                this.mDatasContainer = new DatasContainer();
                this.mServiceBean = ServiceBean.getInstance();
                mReceiveFinished = false;
                this.mAdapter = BluetoothAdapter.getDefaultAdapter();
                this.mDevice = this.mAdapter.getRemoteDevice(this.mServiceBean.getmMacAddr());
                initObjects();
                startReceive();
                CLog.i(TAG, "打开接收数据的线程 ");
                ServiceBean.getInstance().setProgress(33);
                nextStep();
                CLog.i(TAG, " 走下一步骤。。。。。。。。。。。 ");
            } else {
                init();
                nextStep();
            }
        }
        return Service.START_STICKY_COMPATIBILITY;
    }

    void init() {
        this.mServiceBean = ServiceBean.getInstance();
        if (this.mServiceBean.getmMacAddr() == null || this.mServiceBean.getmMacAddr().length() <= 2) {
            DeviceManager.mDeviceBeanList.mState = 3;
            DeviceManager.m_DeviceBean.mState = 3;
            App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(DeviceManager.m_DeviceBean);
            return;
        }
        mReceiveFinished = false;
        this.mAdapter = BluetoothAdapter.getDefaultAdapter();
        this.mDevice = this.mAdapter.getRemoteDevice(this.mServiceBean.getmMacAddr());
        CLog.e(TAG, "DeviceService  onStartCommand*****" + this.mServiceBean.getmMacAddr() + " mDevice:" + this.mDevice);
        initObjects();
    }

    public synchronized void makeConnect() {
        CLog.i(TAG, "mDevice:" + this.mDevice);
        if (this.mDevice == null) {
            DeviceManager.mDeviceBeanList.mState = 3;
            DeviceManager.m_DeviceBean.mState = 3;
            App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(DeviceManager.m_DeviceBean);
        } else {
            CLog.d(TAG, "mDevice.getBondState()：" + this.mDevice.getBondState());
            CLog.d(TAG, "设备已经配对  直接连接  ");
            new Thread() {
                public void run() {
                    if (DeviceManager.mDeviceBeanList == null || DeviceManager.m_DeviceBean == null) {
                        DeviceService.mReceiveFinished = true;
                        CLog.e("lzerror", "makecontect 接到设备连接失败的通知饿了");
                        DeviceManager.mDeviceBeanList.mState = 3;
                        App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(DeviceManager.m_DeviceBean);
                        Message msgManager = new Message();
                        msgManager.what = 21;
                        msgManager.arg2 = 4;
                        App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(msgManager);
                        return;
                    }
                    DeviceManager.mDeviceBeanList.mState = 1;
                    DeviceManager.m_DeviceBean.mState = 1;
                    DeviceManager.m_DeviceBean.mProgress = 0;
                    App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(DeviceManager.m_DeviceBean);
                    CLog.i("lzerror", "即将要连接设备 " + DeviceManager.mDeviceBeanList.mDeviceName + "  " + DeviceManager.m_DeviceBean.mCode);
                    BluetoothDevice _device = DeviceService.this.mDevice;
                    if (DeviceManager.m_DeviceBean.getmBluetoothType().equalsIgnoreCase(Constants.DEVICE_BLUEBOOTH_TYPE_CLASSIC)) {
                        DeviceService.this.mBTConnect = new BTConnect(_device, DeviceService.this);
                        DeviceService.this.mSocket = DeviceService.this.mBTConnect.createUUID();
                        if (DeviceService.this.mSocket != null) {
                            try {
                                ServiceBean.getInstance().mSocket = DeviceService.this.mSocket;
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            CLog.i("lzerror", "即将要连接设备 初步成功 ");
                            DeviceService.nextStep();
                            return;
                        }
                        DeviceService.mReceiveFinished = true;
                        DeviceManager.mDeviceBeanList.mState = 3;
                        DeviceManager.m_DeviceBean.mState = 3;
                        DeviceManager.m_DeviceBean.mProgress = 0;
                        Constants.isSuccessOperationDevice = false;
                        App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(DeviceManager.m_DeviceBean);
                        Message msgManager2 = new Message();
                        msgManager2.what = 21;
                        msgManager2.arg2 = 1;
                        App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(msgManager2);
                        return;
                    }
                    CLog.e(DeviceService.TAG, "连接的为BLE设备-----" + DeviceManager.m_DeviceBean.mDeviceName);
                    ServiceBean.getInstance().matchBLEService(DeviceManager.m_DeviceBean.mDeviceName);
                    DeviceService.this.mBLEService = ServiceBean.getInstance().getmBLEService();
                    Intent _intent = new Intent(App_phms.getInstance().getApplicationContext(), DeviceService.this.mBLEService.getClass());
                    Class<?> cls = DeviceService.this.mBLEService.getClass();
                    DeviceService.this.LogI("要开启的服务的名字--" + DeviceService.this.mBLEService.getClass().getName());
                    if (!App_phms.mServiceNameList.contains(cls)) {
                        App_phms.mServiceNameList.add((Class<? extends BluetoothLeDeviceService>) cls);
                        DeviceService.this.LogI("mSerivceNameList大小：" + App_phms.mServiceNameList.size());
                    }
                    DeviceService.this.stopService(_intent);
                    DeviceService.this.startService(_intent);
                    DeviceService.this.stopSelf();
                }
            }.start();
        }
    }

    public void clearVariables() throws IOException {
        mReceiveFinished = true;
        this.mAdapter = null;
        this.mDevice = null;
        this.mServiceBean = null;
        this.mBTConnect = null;
        this.mReceiveThread = null;
        this.mSaveSDCard = null;
        this.mPackManager = null;
        this.mDataFilter = null;
        this.mSendCommand = null;
        if (this.mSocket != null) {
            this.mSocket.close();
            this.mSocket = null;
        }
    }

    public void startReceive() {
        this.mReceiveThread.start();
    }

    public void startUpload() {
    }

    public void processResult() {
        if (DatasContainer.mUploadedDatas.size() != 0) {
            this.mSaveSDCard.saveUploaded();
        }
        if (DatasContainer.mFailedDatas.size() != 0) {
            this.mSaveSDCard.saveFailed();
        }
        nextStep();
    }

    public void filterDatas() {
        this.mDataFilter.filterDatas();
        nextStep();
    }

    public void bluetoothError() {
    }

    public static void nextStep() {
        toStep(ServiceBean.getInstance().getProgress() + 1);
    }

    public static void toStep(int progress) {
        Message msg = new Message();
        msg.what = progress;
        msg.arg2 = 2;
        App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(msg);
    }

    public void finished() {
        try {
            clearVariables();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (!this.ifstartnext) {
            Message msgManager = new Message();
            msgManager.what = 21;
            msgManager.arg2 = 4;
            App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(msgManager);
        }
    }

    public void onEvent(Message msg) {
        if (msg.arg2 == 2) {
            ServiceBean.getInstance().setProgress(msg.what);
            CLog.i(TAG, "Current Process+++++++++++" + msg.what);
            switch (msg.what) {
                case 31:
                    CLog.i(TAG, "Waiting connection");
                    return;
                case 32:
                    CLog.i(TAG, "Connect");
                    TimerUtil.getinstance().clearTimer();
                    makeConnect();
                    return;
                case 33:
                    CLog.i(TAG, "Get datas from device");
                    startReceive();
                    nextStep();
                    return;
                case 34:
                    CLog.i(TAG, "Start send command to device");
                    sendCommand();
                    return;
                case OrderList.DS_FILTER_DATAS /*35*/:
                    toStep(39);
                    return;
                case 37:
                    CLog.i(TAG, "Upload to server");
                    startUpload();
                    return;
                case OrderList.DS_PROCCESS_RESULT /*38*/:
                    CLog.i(TAG, "Process result");
                    processResult();
                    return;
                case OrderList.DS_FINISHED /*39*/:
                    CLog.i(TAG, "Device Process Finished" + BluetoothServerService.isServerSocketValid + " ifstartnext: " + this.ifstartnext);
                    if (this.mOneNum) {
                        Constants.BLUETOOTHSTAT = 4;
                        Message msgManager = new Message();
                        if (this.ifstartnext) {
                            msgManager.obj = Constants.DEVICE_UPLOAD_CALL;
                        } else {
                            msgManager.obj = Constants.DEVICE_UPLOAD;
                        }
                        msgManager.what = 51;
                        msgManager.arg2 = 5;
                        App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(msgManager);
                        CLog.i(TAG, "mEventBusPostOnBackGround");
                        try {
                            clearVariables();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        stopSelf();
                        this.ifstartnext = false;
                        this.mOneNum = false;
                        return;
                    }
                    return;
                default:
                    return;
            }
        }
    }

    public static void stopSelfServer(Context pContex) {
        pContex.stopService(new Intent(pContex, DeviceService.class));
    }

    public void LogI(String msg) {
        if (Constants.mTestFlag) {
            CLog.i(TAG, msg);
        }
    }
}
