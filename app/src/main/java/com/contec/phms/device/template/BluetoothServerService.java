package com.contec.phms.device.template;

import android.annotation.SuppressLint;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.IBinder;
import com.contec.phms.App_phms;
import com.contec.phms.manager.device.DeviceBean;
import com.contec.phms.manager.device.DeviceManager;
import com.contec.phms.manager.device.ServiceBean;
import com.contec.phms.upload.UploadService;
import com.contec.phms.util.CLog;
import com.contec.phms.util.TimerUtil;
import java.io.IOException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

@SuppressLint({"NewApi"})
public class BluetoothServerService extends Service {
    private static final String NAME = "BluetoothConn";
    private static String TAG = "BluetoothServerService";
    private static BroadcastReceiver connectedDevices = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Bundle b = intent.getExtras();
            Object[] lstName = b.keySet().toArray();
            for (Object obj : lstName) {
                String keyName = obj.toString();
                CLog.e(keyName, String.valueOf(b.get(keyName)));
            }
            BluetoothDevice device = (BluetoothDevice) intent.getParcelableExtra("android.bluetooth.device.extra.DEVICE");
            if ("android.bluetooth.device.action.ACL_CONNECTED".equals(action)) {
                if (!BluetoothServerService.mStartAccept) {
                    BluetoothServerService.mStartAccept = BluetoothServerService.checkDevice(device);
                    CLog.e(BluetoothServerService.TAG, String.valueOf(device.getName()) + "**************连接成功*************" + BluetoothServerService.mStartAccept + "  isServerSocketValid:" + BluetoothServerService.isServerSocketValid);
                    TimerUtil.getinstance().clearTimer();
                } else {
                    CLog.d(BluetoothServerService.TAG, "已经有个设备在回联了 应该接受这个socket 并关闭");
                }
                if (!BluetoothServerService.mStartAccept) {
                    BluetoothServerService.mOtherDeviceCallback = true;
                    return;
                }
                return;
            }
            CLog.e(BluetoothServerService.TAG, "*********************  action:" + action);
        }
    };
    public static boolean isServerSocketValid = false;
    static boolean mOtherDeviceCallback = false;
    private static boolean mStartAccept = false;
    private boolean mRegisterReceiver = false;
    private Thread mThread;
    private Timer mTimer;
    private BluetoothServerSocket mmServerSocket = null;

    public void onCreate() {
        super.onCreate();
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null && intent.getBooleanExtra("start_thread", false)) {
            CLog.e(TAG, "开启回联模式*******************");
            startThread();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    private static boolean checkDevice(BluetoothDevice device) {
        String _mac = device.getAddress();
        if (_mac == null || _mac.length() <= 4) {
            return false;
        }
        CLog.i(TAG, "devicelist .size()=++++=" + DeviceManager.mDeviceList.size());
        for (int i = 0; i < DeviceManager.mDeviceList.size(); i++) {
            List<DeviceBean> _BeanList = DeviceManager.mDeviceList.getDevice(i).mBeanList;
            int n = 0;
            while (n < _BeanList.size()) {
                DeviceBean mBean = _BeanList.get(n);
                if (mBean.mMacAddr.length() <= 0 || !_mac.equalsIgnoreCase(mBean.mMacAddr)) {
                    n++;
                } else {
                    DeviceManager.m_DeviceBean = mBean;
                    DeviceManager.m_DeviceBean.mCode = mBean.mCode;
                    DeviceManager.m_DeviceBean.mDeviceName = mBean.mDeviceName;
                    DeviceManager.m_DeviceBean.mMacAddr = _mac;
                    DeviceManager.mDeviceBeanList = DeviceManager.mDeviceList.getDevice(i);
                    DeviceManager.mDeviceBeanList.mState = mBean.mState;
                    return true;
                }
            }
        }
        return false;
    }

    private void timer() {
        this.mTimer = new Timer();
        this.mTimer.schedule(new TimerTask() {
            public void run() {
                try {
                    if (BluetoothServerService.this.mmServerSocket != null) {
                        CLog.e(BluetoothServerService.TAG, "回联超时****************关闭mmServerSocket");
                        BluetoothServerService.this.mmServerSocket.close();
                        BluetoothServerService.this.mmServerSocket = null;
                    }
                    BluetoothServerService.this.cancleTimer();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }, 8000);
    }

    private void cancleTimer() {
        if (this.mTimer != null) {
            this.mTimer.cancel();
        }
        if (this.mTimer != null) {
            this.mTimer.purge();
        }
        this.mTimer = null;
    }

    public void exit() {
        mStartAccept = false;
        CLog.i(TAG, "disconnect " + isServerSocketValid);
        try {
            if (this.mmServerSocket != null) {
                CLog.i(TAG, "closing: serverSocket name = " + this.mmServerSocket.toString());
                this.mmServerSocket.close();
                this.mmServerSocket = null;
                CLog.i(TAG, "Closed successfully*****");
            }
            if (this.mRegisterReceiver) {
                CLog.e(TAG, "Log out? mRegisterReceiver callback:" + this.mRegisterReceiver);
                App_phms.getInstance().getApplicationContext().unregisterReceiver(connectedDevices);
                this.mRegisterReceiver = false;
            }
            isServerSocketValid = false;
            this.mThread = null;
        } catch (IOException e) {
            CLog.e(TAG, "close() of server failed");
            e.printStackTrace();
        }
    }

    public void onDestroy() {
        exit();
        super.onDestroy();
        CLog.e(TAG, "ondestory BluetoothServerService");
    }

    public static void stopServer(Context pContext) {
        pContext.stopService(new Intent(pContext, BluetoothServerService.class));
    }

    public void startThread() {
        if (!this.mRegisterReceiver) {
            IntentFilter intent = new IntentFilter();
            intent.addAction("android.bluetooth.device.action.FOUND");
            intent.addAction("android.bluetooth.device.action.BOND_STATE_CHANGED");
            intent.addAction("android.bluetooth.adapter.action.SCAN_MODE_CHANGED");
            intent.addAction("android.bluetooth.adapter.action.STATE_CHANGED");
            intent.addAction("android.bluetooth.device.action.ACL_CONNECTED");
            App_phms.getInstance().getApplicationContext().registerReceiver(connectedDevices, intent);
            this.mRegisterReceiver = true;
        }
        this.mThread = new Thread() {
            public void run() {
                super.run();
                BluetoothServerSocket serverSocket = null;
                try {
                    CLog.i(BluetoothServerService.TAG, "开始获得serverSocket Enter the listen server socket");
                    serverSocket = BluetoothAdapter.getDefaultAdapter().listenUsingInsecureRfcommWithServiceRecord(BluetoothServerService.NAME, BTConnect.MY_UUID);
                    CLog.i(BluetoothServerService.TAG, "[ServerSocketThread] serverSocket hash code = " + serverSocket.hashCode());
                    BluetoothServerService.isServerSocketValid = true;
                } catch (IOException e) {
                    e.printStackTrace();
                    BluetoothServerService.isServerSocketValid = false;
                }
                if (serverSocket != null) {
                    BluetoothServerService.this.mmServerSocket = serverSocket;
                    CLog.i(BluetoothServerService.TAG, "[ServerSocketThread] serverSocket name = " + BluetoothServerService.this.mmServerSocket.toString());
                    BluetoothSocket socket = null;
                    while (true) {
                        if (!BluetoothServerService.isServerSocketValid) {
                            break;
                        }
                        try {
                            CLog.i(BluetoothServerService.TAG, "开始等待的回联**********" + BluetoothServerService.this.mmServerSocket.hashCode() + BluetoothServerService.isServerSocketValid);
                            if (BluetoothServerService.this.mmServerSocket != null) {
                                socket = BluetoothServerService.this.mmServerSocket.accept();
                                CLog.i(BluetoothServerService.TAG, "回联成功*******");
                            }
                            if (BluetoothServerService.mStartAccept) {
                                BluetoothServerService.mStartAccept = false;
                                if (socket != null) {
                                    synchronized (this) {
                                        CLog.i(BluetoothServerService.TAG, "得到回联的socket: " + socket.getRemoteDevice() + " is connected." + BluetoothServerService.mStartAccept);
                                        BluetoothServerService.this.timer();
                                        DeviceManager.m_DeviceBean.mState = 2;
                                        DeviceManager.m_DeviceBean.mProgress = 0;
                                        DeviceManager.m_DeviceBean.mProgress = 0;
                                        DeviceManager.mDeviceBeanList.mState = 2;
                                        App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(DeviceManager.m_DeviceBean);
                                        ServiceBean.getInstance().mSocket = socket;
                                        ServiceBean.getInstance().init(DeviceManager.m_DeviceBean);
                                        Intent _start = new Intent(App_phms.getInstance().getApplicationContext(), ServiceBean.getInstance().getmService().getClass());
                                        _start.putExtra("server", true);
                                        App_phms.getInstance().getApplicationContext().startService(_start);
                                        App_phms.getInstance().getApplicationContext().startService(new Intent(App_phms.getInstance().getApplicationContext(), UploadService.class));
                                        BluetoothServerService.this.cancleTimer();
                                        BluetoothServerService.this.exit();
                                        CLog.i(BluetoothServerService.TAG, "得到回联的socket 结束：");
                                    }
                                    break;
                                }
                                continue;
                            } else if (BluetoothServerService.mOtherDeviceCallback) {
                                CLog.d(BluetoothServerService.TAG, "其他设备回联过来的、、、、、");
                                try {
                                    if (BluetoothServerService.this.mmServerSocket != null) {
                                        socket.close();
                                        CLog.d(BluetoothServerService.TAG, "Get the other device's socket and close it**************");
                                    }
                                    BluetoothServerService.mOtherDeviceCallback = false;
                                } catch (IOException e2) {
                                    e2.printStackTrace();
                                }
                            } else {
                                try {
                                    if (!(BluetoothServerService.this.mmServerSocket == null || socket == null)) {
                                        socket.close();
                                        CLog.d(BluetoothServerService.TAG, "不是其他设备 来的      其他设备回联过来的、、、、、11111");
                                    }
                                    BluetoothServerService.mOtherDeviceCallback = false;
                                } catch (IOException e3) {
                                    e3.printStackTrace();
                                }
                                CLog.d(BluetoothServerService.TAG, "不是其他设备 来的      其他设备回联过来的、、、、、");
                            }
                        } catch (IOException e4) {
                            CLog.e(BluetoothServerService.TAG, "Callback failed*******" + e4);
                            BluetoothServerService.isServerSocketValid = false;
                        }
                    }
                    CLog.i(BluetoothServerService.TAG, "回联结束一次********");
                    return;
                }
                CLog.i(BluetoothServerService.TAG, "serverScoket ===null********");
                BluetoothServerService.this.exit();
            }
        };
        this.mThread.start();
    }

    public IBinder onBind(Intent arg0) {
        return null;
    }
}
