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
import com.contec.phms.manager.device.DeviceManager;
import com.contec.phms.db.DeviceListDaoOperation;
import com.contec.phms.util.CLog;
import com.contec.phms.util.Constants;
import java.util.List;
import u.aly.bs;

public class SearchSingleDevice extends Service {
    private static BluetoothAdapter mBluetoothAdapter;
    private boolean FINDDEVICEMAC = false;
    String TAG = "SearchSingleDevice";
    boolean _register = false;
    public final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if ("android.bluetooth.device.action.FOUND".equals(action)) {
                BluetoothDevice device = (BluetoothDevice) intent.getParcelableExtra("android.bluetooth.device.extra.DEVICE");
                CLog.i(SearchSingleDevice.this.TAG, "Get Device: " + device.getName() + ":    " + device.getAddress());
                SearchSingleDevice.this.checkDevice(device);
            } else if ("android.bluetooth.adapter.action.DISCOVERY_FINISHED".equals(action)) {
                CLog.i(SearchSingleDevice.this.TAG, "Search Finished");
                SearchSingleDevice.this.stopSelf();
            } else if ("android.bluetooth.adapter.action.STATE_CHANGED".equals(action)) {
                int _state = intent.getIntExtra("android.bluetooth.adapter.extra.STATE", -1);
                CLog.i(SearchSingleDevice.this.TAG, "Bluetooth state changed: " + action + "   _State:" + _state);
                if (_state == 12) {
                    CLog.i(SearchSingleDevice.this.TAG, "Start search from  enable================");
                    SearchSingleDevice.this.startSearch();
                } else if (_state == 10) {
                    CLog.i(SearchSingleDevice.this.TAG, "Open Buletooth================");
                }
            }
        }
    };

    public static void stopServer(Context pContext) {
        if (mBluetoothAdapter != null && mBluetoothAdapter.isDiscovering()) {
            mBluetoothAdapter.cancelDiscovery();
        }
        pContext.stopService(new Intent(pContext, SearchSingleDevice.class));
    }

    public void startSearch() {
        Message msg = new Message();
        if (DeviceManager.mDeviceList.size() > 0) {
            msg.what = 501;
            msg.arg2 = 1;
            App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(msg);
        }
        for (int i = 0; i < DeviceManager.mDeviceList.getListDevice().size(); i++) {
            if (DeviceManager.mDeviceList.getDevice(i).mDeviceName.equals(Constants.PRIORITY_DEVICENAME)) {
                for (int j = 0; j < DeviceManager.mDeviceList.getDevice(i).mBeanList.size(); j++) {
                    if (Constants.PRIORITY_DEVICECODE.equals(DeviceManager.mDeviceList.getDevice(i).mBeanList.get(j).mCode)) {
                        DeviceBean _priority_devicebean = DeviceManager.mDeviceList.getDevice(i).mBeanList.get(j);
                        _priority_devicebean.mState = 14;
                        _priority_devicebean.clearDataPath();
                    }
                }
            }
        }
        App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(DeviceManager.m_DeviceBean);
        register();
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
    }

    boolean checkDevice(BluetoothDevice device) {
        String _name = device.getName();
        if (_name == null) {
            return false;
        }
        String _code = bs.b;
        if (_name != null && _name.length() > 4) {
            _code = _name.substring(_name.length() - 4, _name.length());
        }
        String _deviceName = ContecDevices.checkDevice(_name);
        if (_deviceName == null || _deviceName.equals(bs.b)) {
            return false;
        }
        String _deviceMac = device.getAddress();
        CLog.i(this.TAG, "devicelist .size()=++++=" + DeviceManager.mDeviceList.size());
        int i = 0;
        while (true) {
            if (i >= DeviceManager.mDeviceList.size()) {
                break;
            }
            List<DeviceBean> _BeanList = DeviceManager.mDeviceList.getDevice(i).mBeanList;
            int n = 0;
            while (n < _BeanList.size()) {
                DeviceBean mBean = _BeanList.get(n);
                if (mBean.mCode.length() <= 0 || !_code.equalsIgnoreCase(mBean.mCode) || !_deviceName.equalsIgnoreCase(mBean.mDeviceName)) {
                    n++;
                } else if (mBean.mMacAddr != null && !mBean.mMacAddr.equals(bs.b)) {
                    return false;
                } else {
                    DeviceListDaoOperation.getInstance().updateDeviceCode(new StringBuilder(String.valueOf(mBean.mId)).toString(), _deviceMac);
                    if (!_deviceName.equals(Constants.PRIORITY_DEVICENAME) || !_code.equals(Constants.PRIORITY_DEVICECODE)) {
                        return false;
                    }
                    this.FINDDEVICEMAC = true;
                    stopSelf();
                }
            }
            i++;
        }
        return true;
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

    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onDestroy() {
        CLog.i(this.TAG, "Stop SearchDevice");
        if (mBluetoothAdapter.getState() == 12 && mBluetoothAdapter.isDiscovering()) {
            mBluetoothAdapter.cancelDiscovery();
        }
        if (this._register) {
            unregisterReceiver(this.mReceiver);
        }
        Message msgManager = new Message();
        msgManager.what = 54;
        msgManager.arg2 = 4;
        App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(msgManager);
    }

    public void onCreate() {
        super.onCreate();
        startSearch();
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        CLog.i(this.TAG, "Start Search Device");
        App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(DeviceManager.m_DeviceBean);
        return super.onStartCommand(intent, flags, startId);
    }
}
