package com.contec.phms.SearchDevice;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.os.Message;
import com.contec.phms.App_phms;
import com.contec.phms.manager.device.DeviceBean;
import com.contec.phms.manager.device.DeviceBeanList;
import com.contec.phms.manager.device.DeviceManager;
import com.contec.phms.db.DeviceListDaoOperation;
import com.contec.phms.util.CLog;
import com.contec.phms.util.Constants;
import java.util.List;
import u.aly.bs;

public class SearchDevice extends Service {
    private static BluetoothAdapter mBluetoothAdapter;
    public static int mListPosition = 0;
    private static boolean mStart = false;
    String TAG = "SearchDevice";
    boolean _register = false;
    public final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if ("android.bluetooth.device.action.FOUND".equals(action)) {
                BluetoothDevice device = (BluetoothDevice) intent.getParcelableExtra("android.bluetooth.device.extra.DEVICE");
                CLog.i(SearchDevice.this.TAG, "Get Device: " + device.getName() + ":    " + device.getAddress());
                SearchDevice.this.checkDevice(device);
            } else if ("android.bluetooth.adapter.action.DISCOVERY_FINISHED".equals(action)) {
                SearchDevice.this.changeUIState();
                CLog.i(SearchDevice.this.TAG, "Search Finished");
                SearchDevice.mListPosition = 0;
                SearchDevice.this.stopSelf();
            } else if ("android.bluetooth.adapter.action.STATE_CHANGED".equals(action)) {
                int _state = intent.getIntExtra("android.bluetooth.adapter.extra.STATE", -1);
                CLog.i(SearchDevice.this.TAG, "Bluetooth state changed: " + action + "   _State:" + _state);
                if (_state == 12) {
                    CLog.i(SearchDevice.this.TAG, "Start search from  enable================");
                    SearchDevice.this.startSearch();
                } else if (_state == 10) {
                    CLog.i(SearchDevice.this.TAG, "Open Buletooth================");
                }
            }
        }
    };

    public static void stopServer(Context pContext) {
        if (mStart) {
            if (mBluetoothAdapter != null && mBluetoothAdapter.isDiscovering()) {
                mBluetoothAdapter.cancelDiscovery();
            }
            pContext.stopService(new Intent(pContext, SearchDevice.class));
        }
    }

    public void startSearch() {
        boolean _toConntect = DeviceListDaoOperation.getInstance().isHasMac();
        CLog.d(this.TAG, "_toConntect************:" + _toConntect);
        if (!_toConntect) {
            Message msg = new Message();
            int _size = DeviceManager.mDeviceList.size();
            CLog.i(this.TAG, "mdevicelist.size(**********************)" + _size);
            if (_size > 0) {
                msg.what = 501;
                msg.arg2 = 1;
                App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(msg);
            }
            for (int i = 0; i < _size; i++) {
                DeviceBeanList mBean = DeviceManager.mDeviceList.getDevice(i);
                mBean.mState = 14;
                for (int n = 0; n < mBean.mBeanList.size(); n++) {
                    mBean.mBeanList.get(n).mState = 14;
                }
                mBean.clearPath();
            }
            App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(DeviceManager.m_DeviceBean);
            register();
            Constants.BLUETOOTHSTAT = 2;
            CLog.i(this.TAG, "StartSearch()");
            mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            if (mBluetoothAdapter.getState() == 12) {
                if (mBluetoothAdapter.isDiscovering()) {
                    mBluetoothAdapter.cancelDiscovery();
                }
                if (!mBluetoothAdapter.startDiscovery()) {
                    CLog.i(this.TAG, "startDiscovery failed, close bluetooth!");
                    mBluetoothAdapter.disable();
                }
            } else if (mBluetoothAdapter.getState() == 10) {
                mBluetoothAdapter.enable();
                CLog.i(this.TAG, "Bluetooth State: " + mBluetoothAdapter.isEnabled());
            }
        } else {
            App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(DeviceManager.m_DeviceBean);
            mListPosition = 0;
            Message msgManager = new Message();
            msgManager.what = 12;
            msgManager.arg2 = 4;
            App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(msgManager);
            stopSelf();
        }
    }

    boolean checkDevice(BluetoothDevice device) {
        String _name = device.getName();
        if (_name != null) {
            if (_name != null && _name.length() > 4) {
                String _code = _name.substring(_name.length() - 4, _name.length());
            }
            String _deviceName = ContecDevices.checkDevice(_name);
            if (_deviceName != null && !_deviceName.equals(bs.b)) {
                String _deviceMac = device.getAddress();
                CLog.i(this.TAG, "devicelist .size()=++++=" + DeviceManager.mDeviceList.size());
                for (int i = 0; i < DeviceManager.mDeviceList.size(); i++) {
                    List<DeviceBean> _BeanList = DeviceManager.mDeviceList.getDevice(i).mBeanList;
                    int n = 0;
                    while (n < _BeanList.size()) {
                        DeviceBean mBean = _BeanList.get(n);
                        if (mBean.mCode.length() <= 0 || !_deviceMac.equalsIgnoreCase(mBean.mMacAddr) || !_deviceName.equalsIgnoreCase(mBean.mDeviceName)) {
                            n++;
                        } else {
                            if (mBean.mMacAddr == null || mBean.mMacAddr.equals(bs.b)) {
                                DeviceListDaoOperation.getInstance().updateDeviceCode(new StringBuilder(String.valueOf(mBean.mId)).toString(), _deviceMac);
                            }
                            mBean.mMacAddr = _deviceMac;
                            mBean.mState = 0;
                            boolean _isfound = false;
                            for (int m = 0; m < n; m++) {
                                if (_BeanList.get(m).mState == 14 && !_isfound) {
                                    _BeanList.set(m, mBean);
                                    _BeanList.set(n, _BeanList.get(m));
                                    App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(DeviceManager.m_DeviceBean);
                                    _isfound = true;
                                }
                            }
                            for (int j = 0; j < i; j++) {
                                if (DeviceManager.mDeviceList.getDevice(j).mBeanList.get(0).mState == 14) {
                                    DeviceBeanList _DeviceBeanList = DeviceManager.mDeviceList.getDevice(j);
                                    DeviceManager.mDeviceList.setObject(j, DeviceManager.mDeviceList.getDevice(i));
                                    DeviceManager.mDeviceList.setObject(i, _DeviceBeanList);
                                    App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(DeviceManager.m_DeviceBean);
                                    return false;
                                }
                            }
                            App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(DeviceManager.m_DeviceBean);
                            return false;
                        }
                    }
                }
                return true;
            }
        }
        return false;
    }

    public void register() {
        CLog.i(this.TAG, "register()");
        if (!this._register) {
            this._register = true;
            registerReceiver(this.mReceiver, new IntentFilter("android.bluetooth.device.action.FOUND"));
            registerReceiver(this.mReceiver, new IntentFilter("android.bluetooth.adapter.action.DISCOVERY_FINISHED"));
            registerReceiver(this.mReceiver, new IntentFilter("android.bluetooth.adapter.action.STATE_CHANGED"));
        }
    }

    void changeUIState() {
        int _size = DeviceManager.mDeviceList.size();
        if (_size == 0) {
            Message msg = new Message();
            msg.what = 504;
            msg.arg2 = 1;
            App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(msg);
            return;
        }
        for (int i = 0; i < _size; i++) {
            DeviceBeanList _DeviceBean = DeviceManager.mDeviceList.getDevice(i);
            if (DeviceManager.mDeviceList.getDevice(i).mBeanList.get(0).mState == 14) {
                DeviceManager.mDeviceList.getDevice(i).mState = 13;
            } else {
                DeviceManager.mDeviceList.getDevice(i).mState = 0;
            }
            for (int j = 0; j < _DeviceBean.mBeanList.size(); j++) {
                if (_DeviceBean.mBeanList.get(j).mState == 14) {
                    _DeviceBean.mBeanList.get(j).mState = 13;
                }
            }
            App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(DeviceManager.m_DeviceBean);
        }
    }

    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onDestroy() {
        CLog.i(this.TAG, "Stop SearchDevice");
        if (this._register) {
            unregisterReceiver(this.mReceiver);
            Message msgManager = new Message();
            msgManager.what = 12;
            msgManager.arg2 = 4;
            App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(msgManager);
        }
    }

    public void onCreate() {
        super.onCreate();
        if (Constants.BLUETOOTHSTAT == 1) {
            CLog.i(this.TAG, "要call startSearch method");
            startSearch();
        }
        mStart = true;
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        CLog.i(this.TAG, "Start Search Device");
        App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(DeviceManager.m_DeviceBean);
        return super.onStartCommand(intent, flags, startId);
    }
}
