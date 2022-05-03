package com.contec.phms.device.template;

import android.annotation.SuppressLint;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import com.contec.phms.App_phms;
import com.contec.phms.manager.message.MessageManagerMain;
import com.contec.phms.util.CLog;
import com.contec.phms.util.Constants;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

@SuppressLint({"NewApi"})
public class BluetoothLeService extends Service {
    public static final String ACTION_DATA_AVAILABLE = "com.contec.phms.bluetooth.le.ACTION_DATA_AVAILABLE";
    public static final String ACTION_NOTIFY_OPEN = "com.contec.phms.bluetooth.le.ACTION_NOTIFY_OPEN";
    public static final String ACTION_SERVICES_DISCOVERED = "com.contec.phms.bluetooth.le.SERVICES_DISCOVERED";
    public static final String ACTION_STATE_CONNECTED = "com.contec.phms.bluetooth.le.STATE_CONNECTED";
    public static final String ACTION_STATE_DISCONNECTED = "com.contec.phms.bluetooth.le.STATE_DISCONNECTED";
    public static String CLIENT_CHARACTERISTIC_CONFIG = "00002902-0000-1000-8000-00805f9b34fb";
    public static final String EXTRA_DATA = "com.contec.phms.bluetooth.le.EXTRA_DATA";
    private static final String TAG = BluetoothLeService.class.getSimpleName();
    private final IBinder mBinder = new LocalBinder();
    private BluetoothAdapter mBluetoothAdapter;
    private String mBluetoothDeviceAddress;
    public BluetoothGatt mBluetoothGatt;
    private BluetoothManager mBluetoothManager;
    private final BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            BluetoothLeService.this.LogI("代码执行到onConnectionStateChange method中");
            if (newState == 2) {
                BluetoothLeService.this.LogI("连接上设备");
                CLog.i(BluetoothLeService.TAG, "Attempting to start service discovery:" + BluetoothLeService.this.mBluetoothGatt.discoverServices());
                BluetoothLeService.this.broadcastState(BluetoothLeService.ACTION_STATE_CONNECTED);
            } else if (newState == 0) {
                BluetoothLeService.this.broadcastState(BluetoothLeService.ACTION_STATE_DISCONNECTED);
            }
        }

        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            if (status == 0) {
                BluetoothLeService.this.LogI("发现新的服务端");
                BluetoothLeService.this.cancleTimer();
                BluetoothLeService.this.mflag = true;
                BluetoothLeService.this.timer(15000);
                BluetoothLeService.this.broadcastState(BluetoothLeService.ACTION_SERVICES_DISCOVERED);
            }
        }

        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
        }

        public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
        }

        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            BluetoothLeService.this.LogI("ACTION_DATA_AVAILABLE");
            BluetoothLeService.this.cancleTimer();
            BluetoothLeService.this.mflag = true;
            BluetoothLeService.this.timer(20000);
            BluetoothLeService.this.broadcastUpdate(BluetoothLeService.ACTION_DATA_AVAILABLE, characteristic);
        }

        public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
            if (gatt.equals(BluetoothLeService.this.mBluetoothGatt) && status == 0) {
                BluetoothLeService.this.LogI("服务端的大门打开了，欢迎光临");
                BluetoothLeService.this.cancleTimer();
                BluetoothLeService.this.broadcastState(BluetoothLeService.ACTION_NOTIFY_OPEN);
            }
        }
    };
    private Timer mTimer;
    private boolean mflag = false;

    public void onCreate() {
        super.onCreate();
        CLog.i(TAG, "创建BLE服务类。。。");
    }

    public class LocalBinder extends Binder {
        public LocalBinder() {
        }

        public BluetoothLeService getService() {
            return BluetoothLeService.this;
        }
    }

    public IBinder onBind(Intent intent) {
        return this.mBinder;
    }

    public boolean onUnbind(Intent intent) {
        closeBleGatt();
        return super.onUnbind(intent);
    }

    public boolean initialize() {
        if (this.mBluetoothManager == null) {
            this.mBluetoothManager = (BluetoothManager) getSystemService("bluetooth");
            if (this.mBluetoothManager == null) {
                return false;
            }
        }
        this.mBluetoothAdapter = this.mBluetoothManager.getAdapter();
        if (this.mBluetoothAdapter == null) {
            return false;
        }
        return true;
    }

    public boolean connectBleDevice(String address) {
        CLog.i(TAG, "开始连接指定设备");
        timer(15000);
        if (this.mBluetoothAdapter == null || address == null) {
            CLog.dT(TAG, "BluetoothAdapter not initialized or unspecified address.");
            return false;
        } else if (this.mBluetoothDeviceAddress == null || !address.equals(this.mBluetoothDeviceAddress) || this.mBluetoothGatt == null) {
            BluetoothDevice device = this.mBluetoothAdapter.getRemoteDevice(address);
            LogI("device:" + device);
            if (device == null) {
                CLog.d(TAG, "Device not found.  Unable to connect.");
                return false;
            }
            if (this.mBluetoothGatt != null) {
                this.mBluetoothGatt.close();
                this.mBluetoothGatt.disconnect();
                this.mBluetoothGatt = null;
            }
            this.mBluetoothGatt = device.connectGatt(this, true, this.mGattCallback);
            LogI("mBluetoothGatt----:" + this.mBluetoothGatt);
            this.mBluetoothDeviceAddress = address;
            return true;
        } else {
            CLog.d(TAG, "Trying to use an existing mBluetoothGatt for connection.");
            if (!this.mBluetoothGatt.connect()) {
                return false;
            }
            return true;
        }
    }

    public boolean setCharacteristicNotification(BluetoothGattCharacteristic characteristic, boolean enabled) {
        if (this.mBluetoothAdapter == null || this.mBluetoothGatt == null) {
            CLog.e(TAG, "BluetoothAdapter not initialized");
            return false;
        }
        this.mBluetoothGatt.setCharacteristicNotification(characteristic, enabled);
        BluetoothGattDescriptor descriptor = characteristic.getDescriptor(UUID.fromString(CLIENT_CHARACTERISTIC_CONFIG));
        descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
        return this.mBluetoothGatt.writeDescriptor(descriptor);
    }

    public void writeDataToBleDevice(BluetoothGattCharacteristic characteristic) {
        if (this.mBluetoothGatt != null && characteristic != null) {
            this.mBluetoothGatt.writeCharacteristic(characteristic);
        }
    }

    private void broadcastUpdate(String action, BluetoothGattCharacteristic characteristic) {
        LogI("call broadcastUpdate method");
        Intent intent = new Intent(action);
        intent.putExtra(EXTRA_DATA, characteristic.getValue());
        sendBroadcast(intent);
    }

    private void broadcastState(String action) {
        sendBroadcast(new Intent(action));
    }

    private void timer(final long delay) {
        this.mTimer = new Timer();
        this.mTimer.schedule(new TimerTask() {
            public void run() {
                BluetoothLeService.this.LogI("设备连接超时，超时" + (delay / 1000) + "秒");
                if (BluetoothLeService.this.mTimer != null) {
                    BluetoothLeService.this.mTimer.cancel();
                    BluetoothLeService.this.mTimer.purge();
                    BluetoothLeService.this.mTimer = null;
                    if (!BluetoothLeService.this.mflag) {
                        MessageManagerMain.msg_ui_connect_device_fail();
                    } else {
                        MessageManagerMain.msg_ui_receive_data_fail();
                        BluetoothLeService.this.mflag = false;
                    }
                    MessageManagerMain.msg_ConnectNextDeviceOrPollingOrCallBack();
                }
            }
        }, delay);
    }

    private void cancleTimer() {
        if (this.mTimer != null) {
            this.mTimer.cancel();
            this.mTimer.purge();
            this.mTimer = null;
        }
    }

    public void disconnectBleDevice() {
        cancleTimer();
        if (this.mBluetoothAdapter == null || this.mBluetoothGatt == null) {
            Log.w(TAG, "BluetoothAdapter not initialized");
            return;
        }
        this.mBluetoothGatt.disconnect();
        closeBleGatt();
    }

    public void closeBleGatt() {
        if (this.mBluetoothGatt != null) {
            this.mBluetoothGatt.close();
            this.mBluetoothGatt = null;
        }
    }

    public void LogI(String msg) {
        if (Constants.mTestFlag) {
            CLog.i(TAG, msg);
        }
    }

    public void readCharacteristic(BluetoothGattCharacteristic characteristic) {
        if (this.mBluetoothAdapter == null || this.mBluetoothGatt == null) {
            Log.e(TAG, "BluetoothAdapter not initialized");
        } else {
            this.mBluetoothGatt.readCharacteristic(characteristic);
        }
    }

    public List<BluetoothGattService> getSupportedGattServices() {
        if (this.mBluetoothGatt == null) {
            return null;
        }
        return this.mBluetoothGatt.getServices();
    }

    public void onDestroy() {
        LogI("call " + TAG + "的onDestroy method");
        super.onDestroy();
    }

    public static void stopServer() {
        App_phms.getInstance().getApplicationContext().stopService(new Intent(App_phms.getInstance().getApplicationContext(), BluetoothLeService.class));
    }
}
