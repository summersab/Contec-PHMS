package com.contec.phms.device.template;

import android.annotation.TargetApi;
import android.app.Service;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.Message;
import com.contec.phms.App_phms;
import com.contec.phms.device.template.BluetoothLeService;
import com.contec.phms.manager.device.DeviceManager;
import com.contec.phms.manager.device.ServiceBean;
import com.contec.phms.manager.message.MessageManagerMain;
import com.contec.phms.util.CLog;
import com.contec.phms.util.Constants;
import java.util.UUID;

@TargetApi(18)
public abstract class BluetoothLeDeviceService extends Service {
    public static final UUID NOTIFY_UUID = UUID.fromString("0000ff02-0000-1000-8000-00805f9b34fb");
    public static final UUID SERVIE_50KUUID = UUID.fromString("0000ff12-0000-1000-8000-00805f9b34fb");
    private static final String TAG = BluetoothLeDeviceService.class.getSimpleName();
    public static final UUID WRITE_UUID = UUID.fromString("0000ff01-0000-1000-8000-00805f9b34fb");
    private ServiceConnection bleServiceConnection = new ServiceConnection() {
        public void onServiceDisconnected(ComponentName name) {
            CLog.e(BluetoothLeDeviceService.TAG, "BluetoothLeService==>onServiceDisconnecteduccess");
            BluetoothLeDeviceService.this.mBindBleService = false;
        }

        public void onServiceConnected(ComponentName name, IBinder service) {
            CLog.e(BluetoothLeDeviceService.TAG, "BluetoothLeService==>onServiceConnected");
            BluetoothLeDeviceService.this.mBindBleService = true;
            BluetoothLeDeviceService.this.mBluetoothLeService = ((BluetoothLeService.LocalBinder) service).getService();
            BluetoothLeDeviceService.this.registerReceiver(BluetoothLeDeviceService.this.mBluetoothLeReceiver, BluetoothLeDeviceService.updateFilter());
            BluetoothLeDeviceService.this.mRegisterReceiver = true;
            if (!BluetoothLeDeviceService.this.mBluetoothLeService.initialize()) {
                CLog.e(BluetoothLeDeviceService.TAG, "Unable to initialize Bluetooth");
            }
            if (!BluetoothLeDeviceService.this.mBleDeviceConnect) {
                BluetoothLeDeviceService.this.mBluetoothLeService.connectBleDevice(ServiceBean.getInstance().getmMacAddr());
            } else if (BluetoothLeDeviceService.this.notification) {
                //BluetoothLeDeviceService.LogI("代码call bleServiceNofityOpen() method");
                BluetoothLeDeviceService.this.bleServiceNofityOpen();
            }
        }
    };
    private BluetoothGattCharacteristic gattCharacteristicNotify;
    private BluetoothGattCharacteristic gattCharacteristicWrite;
    private boolean mBindBleService = false;
    protected boolean mBleDeviceConnect = false;
    private BluetoothGattService mBlueGattService;
    private BroadcastReceiver mBluetoothLeReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            CLog.i(BluetoothLeDeviceService.TAG, "call onReceive method");
            if (BluetoothLeService.ACTION_STATE_CONNECTED.equals(action)) {
                CLog.e("当前手机连接设备成功", "已经把设备连接成功");
                CLog.e(BluetoothLeDeviceService.TAG, "已经和设备连接成功");
                MessageManagerMain.msg_ui_connect_device_success();
                BluetoothLeDeviceService.this.connect();
            } else if (BluetoothLeService.ACTION_STATE_DISCONNECTED.equals(action)) {
                MessageManagerMain.msg_ui_connect_device_fail();
                BluetoothLeDeviceService.this.disConnect();
            } else if (BluetoothLeService.ACTION_SERVICES_DISCOVERED.equals(action)) {
                CLog.e(BluetoothLeDeviceService.TAG, "发现新的服务端");
                BluetoothLeDeviceService.this.serviceDiscovered();
            } else if (BluetoothLeService.ACTION_NOTIFY_OPEN.equals(action)) {
                CLog.e(BluetoothLeDeviceService.TAG, "响应被打开！");
                BluetoothLeDeviceService.this.bleServiceNofityOpen();
            } else if (BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action)) {
                CLog.e(BluetoothLeDeviceService.TAG, "接收到数据！");
                BluetoothLeDeviceService.this.arrangeMessage(intent.getByteArrayExtra(BluetoothLeService.EXTRA_DATA), intent.getByteArrayExtra(BluetoothLeService.EXTRA_DATA).length);
            } else {
                //BluetoothLeDeviceService.LogI("什么也没有。。。");
            }
        }
    };
    private BluetoothLeService mBluetoothLeService;
    private boolean mRegisterReceiver = false;
    private boolean notification;

    public abstract void arrangeMessage(byte[] bArr, int i);

    public abstract void bleServiceNofityOpen();

    public IBinder onBind(Intent intent) {
        return null;
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    public void onCreate() {
        super.onCreate();
        App_phms.getInstance().mEventBus.register(this);
        CLog.i(TAG, "创建服务---【基类】BLE连接服务");
        bindService(new Intent(this, BluetoothLeService.class), this.bleServiceConnection, 1);
    }

    private static IntentFilter updateFilter() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothLeService.ACTION_DATA_AVAILABLE);
        intentFilter.addAction(BluetoothLeService.ACTION_SERVICES_DISCOVERED);
        intentFilter.addAction(BluetoothLeService.ACTION_STATE_CONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_STATE_DISCONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_NOTIFY_OPEN);
        return intentFilter;
    }

    private void serviceDiscovered() {
        this.mBlueGattService = this.mBluetoothLeService.mBluetoothGatt.getService(SERVIE_50KUUID);
        if (this.mBlueGattService == null) {
            DeviceService.mReceiveFinished = true;
            DeviceManager.mDeviceBeanList.mState = 5;
            DeviceManager.m_DeviceBean.mState = 5;
            DeviceManager.m_DeviceBean.mProgress = 0;
            return;
        }
        this.gattCharacteristicWrite = this.mBlueGattService.getCharacteristic(WRITE_UUID);
        this.gattCharacteristicNotify = this.mBlueGattService.getCharacteristic(NOTIFY_UUID);
        if (this.gattCharacteristicNotify != null) {
            LogI("代码执行到serviceDiscovered method内部");
            this.notification = this.mBluetoothLeService.setCharacteristicNotification(this.gattCharacteristicNotify, true);
        }
        if (this.mBlueGattService != null) {
            this.mBlueGattService = null;
        }
        if (this.gattCharacteristicNotify != null) {
            this.gattCharacteristicNotify = null;
        }
    }

    public void disConnectBleDevice() {
        LogI("主动断开设备连接");
        if (this.mBluetoothLeReceiver != null) {
            unregisterReceiver(this.mBluetoothLeReceiver);
            this.mBluetoothLeReceiver = null;
            LogI("注销蓝牙广播接受者.");
        }
        this.mBluetoothLeService.disconnectBleDevice();
    }

    private void connect() {
        CLog.e(TAG, "BluetoothLeService==>ACTION_STATE_CONNECTED");
        this.mBleDeviceConnect = true;
    }

    private void disConnect() {
        CLog.e(TAG, "BluetoothLeService==>ACTION_STATE_DISCONNECTED");
        this.notification = false;
        this.mBleDeviceConnect = false;
        MessageManagerMain.msg_ConnectNextDeviceOrPollingOrCallBack();
        MessageManagerMain.msg_BleDisconnect();
    }

    public void sendData(byte[] data) {
        if (this.gattCharacteristicWrite != null && data != null) {
            this.gattCharacteristicWrite.setValue(data);
            this.gattCharacteristicWrite.setWriteType(1);
            this.mBluetoothLeService.writeDataToBleDevice(this.gattCharacteristicWrite);
        }
    }

    protected void toUpload() {
        disConnectBleDevice();
        MessageManagerMain.msg_ui_receive_data_success();
        MessageManagerMain.msg_ui_upload_dataing();
        DeviceService.mReceiveFinished = true;
        CLog.i(TAG, "给上传服务类发送上传消息");
        Constants.BLUETOOTHSTAT = 4;
        Message msgManager = new Message();
        msgManager.obj = Constants.DEVICE_UPLOAD;
        msgManager.what = 51;
        msgManager.arg2 = 4;
        App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(msgManager);
    }

    protected void noNewData() {
        CLog.i(TAG, "call 。。noNewData");
        disConnectBleDevice();
        MessageManagerMain.msg_ui_no_new_data();
        MessageManagerMain.msg_ConnectNextDeviceOrPollingOrCallBack();
    }

    public void onDestroy() {
        LogI("call " + TAG + "的onDestroy method");
        App_phms.getInstance().mEventBus.unregister(this);
        if (this.mBindBleService) {
            unbindService(this.bleServiceConnection);
            this.mBindBleService = false;
        }
        if (this.mRegisterReceiver) {
            this.mRegisterReceiver = false;
        }
        LogI("代码执行到这里");
        if (this.mBluetoothLeReceiver != null) {
            try {
                unregisterReceiver(this.mBluetoothLeReceiver);
            } catch (Exception e) {
                LogE("注销广播时，发生异常！但是已被我捕捉到");
                e.printStackTrace();
            }
            this.mBluetoothLeReceiver = null;
            LogI("……………………注销掉蓝牙广播接受者");
        }
    }

    public static void stopServer(Context pContext) {
        CLog.e(TAG, "call " + TAG + "的stopServer method");
        int size = App_phms.mServiceNameList.size();
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                pContext.stopService(new Intent(pContext, App_phms.mServiceNameList.get(i)));
                //LogI("关掉服务的名：" + App_phms.mServiceNameList.get(i));
            }
            App_phms.mServiceNameList.clear();
        }
    }

    private void LogI(String msg) {
        if (Constants.mTestFlag) {
            CLog.i(TAG, msg);
        }
    }

    public static void LogE(String msg) {
        if (Constants.mTestFlag) {
            CLog.e(TAG, msg);
        }
    }
}
