package com.contec.phms.device.template;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import com.contec.phms.App_phms;
import com.contec.phms.manager.device.DeviceManager;
import com.contec.phms.util.CLog;
import com.contec.phms.util.Constants;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;
import java.util.concurrent.Executors;

@SuppressLint({"NewApi"})
public class BTConnect {
    public static String CLASSIC_CONNECTED = "com.contec.phms.classic.connect";
    public static String CLASSIC_DISCONNECTED = "com.contec.phms.classic.disconnect";
    public static String CLASSIC_START = "com.contec.phms.classic.start";
    public static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private static final String TAG = "BTConnect";
    private Context mContext;
    private final BluetoothDevice mDevice;
    private boolean mIfConnecTimeOut = false;
    private BluetoothSocket mSocket = null;
    private Timer mTimer;

    public BTConnect(BluetoothDevice pBluetoothDevice, Context mContext2) {
        this.mDevice = pBluetoothDevice;
        this.mContext = mContext2;
    }

    public synchronized BluetoothSocket createUUID() {
        BluetoothSocket _tmp;
        timer(20);
        int i = 0;
        while (true) {
            if (i >= 5) {
                break;
            }
            CLog.i(TAG, "开始连接设备************" + DeviceManager.mDeviceBeanList.mDeviceName + "  " + DeviceManager.m_DeviceBean.mCode);
            broadcast(CLASSIC_START);
            try {
                if (!this.mIfConnecTimeOut) {
                    if (this.mDevice != null) {
                        _tmp = this.mDevice.createInsecureRfcommSocketToServiceRecord(MY_UUID);
                    } else {
                        _tmp = null;
                    }
                    this.mSocket = _tmp;
                    if (this.mSocket == null) {
                        CLog.i(TAG, "mSocket 连接失败 " + i);
                    } else if (Constants.REPORT) {
                        DeviceManager.mDeviceBeanList.mState = 3;
                        DeviceManager.m_DeviceBean.mState = 3;
                        DeviceManager.m_DeviceBean.mProgress = 0;
                        CLog.i(TAG, "即将要连接设备 5次 i == 0 失败 " + DeviceManager.mDeviceBeanList.mDeviceName + "  " + DeviceManager.m_DeviceBean.mCode);
                        broadcast(CLASSIC_DISCONNECTED);
                        App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(DeviceManager.m_DeviceBean);
                        this.mSocket.close();
                        this.mSocket = null;
                        break;
                    } else {
                        CLog.i(TAG, "开始获取mSocket值");
                        this.mSocket = run();
                        CLog.i(TAG, "获取mSocket值结束");
                        broadcast(CLASSIC_CONNECTED);
                        if (this.mSocket != null) {
                            CLog.i(TAG, " 连接成功**** 清除计时 " + i);
                            cancleTimer();
                            DeviceManager.mDeviceBeanList.mState = 2;
                            DeviceManager.m_DeviceBean.mState = 2;
                            CLog.i(TAG, "第" + i + "次成功了 " + DeviceManager.mDeviceBeanList.mDeviceName + " " + DeviceManager.m_DeviceBean.mCode);
                            App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(DeviceManager.m_DeviceBean);
                            break;
                        } else if (i == 4) {
                            DeviceManager.mDeviceBeanList.mState = 3;
                            DeviceManager.m_DeviceBean.mState = 3;
                            DeviceManager.m_DeviceBean.mProgress = 0;
                            broadcast(CLASSIC_DISCONNECTED);
                            CLog.i(TAG, "连接5次都失败 " + DeviceManager.mDeviceBeanList.mDeviceName + "  " + DeviceManager.m_DeviceBean.mCode);
                            App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(DeviceManager.m_DeviceBean);
                            this.mSocket = null;
                        }
                    }
                    i++;
                } else {
                    CLog.i(TAG, " 连接时间超时**** " + i);
                    broadcast(CLASSIC_DISCONNECTED);
                    Constants.isSuccessOperationDevice = false;
                    if (this.mSocket != null) {
                        this.mSocket.close();
                        this.mSocket = null;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return this.mSocket;
    }

    private void timer(int second) {
        this.mIfConnecTimeOut = false;
        if (this.mTimer != null) {
            this.mTimer.cancel();
            this.mTimer.purge();
            this.mTimer = null;
        }
        this.mTimer = new Timer();
        this.mTimer.schedule(new TimerTask() {
            public void run() {
                BTConnect.this.mIfConnecTimeOut = true;
                if (BTConnect.this.mTimer != null) {
                    BTConnect.this.mTimer.cancel();
                    BTConnect.this.mTimer.purge();
                    BTConnect.this.mTimer = null;
                }
            }
        }, (long) (second * 1000));
    }

    private void cancleTimer() {
        if (this.mTimer != null) {
            this.mIfConnecTimeOut = false;
            this.mTimer.cancel();
            this.mTimer.purge();
            this.mTimer = null;
        }
    }

    private BluetoothSocket run() {
        CLog.i(TAG, "---call run method，mSocket: " + this.mSocket);
        try {
            return Executors.newFixedThreadPool(1).submit(new MyCallable(this.mSocket)).get();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void broadcast(String action) {
        this.mContext.sendBroadcast(new Intent(action));
    }
}
