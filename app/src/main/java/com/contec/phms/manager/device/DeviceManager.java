package com.contec.phms.manager.device;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import com.contec.phms.App_phms;
import com.contec.phms.SearchDevice.SearchDevice;
import com.contec.phms.SearchDevice.SortDeviceContainer;
import com.contec.phms.SearchDevice.SortDeviceContainerList;
import com.contec.phms.SearchDevice.SortDeviceContainerMap;
import com.contec.phms.Server_Main;
import com.contec.phms.device.abpm50w.DeviceService;
import com.contec.phms.manager.datas.DatasContainer;
import com.contec.phms.manager.message.MessageManager;
import com.contec.phms.upload.UploadService;
import com.contec.phms.util.CLog;
import com.contec.phms.util.Constants;
import java.util.ArrayList;
import java.util.HashMap;
import u.aly.bs;
import serial.jni.GLActivity;

public class DeviceManager extends Service {
    public static DeviceBeanList mDeviceBeanList = new DeviceBeanList();
    public static DeviceListItem mDeviceList = new DeviceListItem();
    public static DeviceBean mRefreshBean = null;
    public static int mWhichDevice = 0;
    public static int mWhichItem = 0;
    public static int mWhichSortIndex = 0;
    public static DeviceBean m_DeviceBean = new DeviceBean(bs.b, bs.b);
    public static boolean mis_prioritysearch = true;
    String TAG = "DeviceManager";
    private ArrayList<DeviceBean> mCanSortDeviceBeanList;
    DatasContainer mDatasContainer;
    ServiceBean mServiceBean;
    private HashMap<String, SortDeviceContainer> mWillSortDeviceContainer;
    private BroadcastReceiver searchDevices = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Bundle b = intent.getExtras();
            Object[] lstName = b.keySet().toArray();
            for (Object obj : lstName) {
                String keyName = obj.toString();
                CLog.e(keyName, String.valueOf(b.get(keyName)));
            }
            if ("android.bluetooth.device.action.BOND_STATE_CHANGED".equals(action)) {
                switch (((BluetoothDevice) intent.getParcelableExtra("android.bluetooth.device.extra.DEVICE")).getBondState()) {
                    case 10:
                        CLog.d(DeviceManager.this.TAG, "unpair");
                        DeviceManager.this.unregisterReceiver(DeviceManager.this.searchDevices);
                        CLog.dT(DeviceManager.this.TAG, "connection failed***********");
                        DeviceManager.mDeviceBeanList.mState = 3;
                        DeviceManager.m_DeviceBean.mState = 3;
                        DeviceManager.m_DeviceBean.mProgress = 0;
                        App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(DeviceManager.m_DeviceBean);
                        Constants.START_8000GW = false;
                        return;
                    case 11:
                        CLog.d(DeviceManager.this.TAG, "pairing......");
                        return;
                    case 12:
                        CLog.d(DeviceManager.this.TAG, "Pairing complete - connecting devices ***********************");
                        DeviceManager.this.start8000GW();
                        DeviceManager.this.unregisterReceiver(DeviceManager.this.searchDevices);
                        return;
                    default:
                        return;
                }
            }
        }
    };

    public IBinder onBind(Intent arg0) {
        return null;
    }

    public void onCreate() {
        super.onCreate();
        App_phms.getInstance().mEventBus.register(this);
        mWhichItem = 0;
        mWhichDevice = 0;
        mWhichSortIndex = 0;
    }

    public static void stopServer(Context pContext) {
        pContext.stopService(new Intent(pContext, DeviceManager.class));
    }

    public void onDestroy() {
        super.onDestroy();
        if (this.mWillSortDeviceContainer != null) {
            this.mWillSortDeviceContainer.clear();
        }
        if (this.mCanSortDeviceBeanList != null) {
            this.mCanSortDeviceBeanList.clear();
        }
        App_phms.getInstance().mEventBus.unregister(this);
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        CLog.i(this.TAG, "onstartCommand*********************");
        Constants.BLUETOOTHSTAT = 3;
        init();
        initCanSortArraylist();
        if (mDeviceList.size() > 0) {
            mDeviceBeanList = mDeviceList.popDevice(0);
        }
        startNext();
        return 0;
    }

    private void initCanSortArraylist() {
        this.mWillSortDeviceContainer = null;
        this.mWillSortDeviceContainer = new HashMap<>();
        this.mCanSortDeviceBeanList = null;
        mWhichSortIndex = 0;
        this.mCanSortDeviceBeanList = new ArrayList<>();
        if (mRefreshBean != null) {
            mRefreshBean.ifAddNew = true;
            this.mCanSortDeviceBeanList.add(mRefreshBean);
            mRefreshBean = null;
            return;
        }
        Constants.ISFIRSTPRIORITYSORT = false;
        SortDeviceContainerList.getInstance().clearSortlist();
        for (int i = 0; i < mDeviceList.getListDevice().size(); i++) {
            for (int j = 0; j < mDeviceList.getDevice(i).mBeanList.size(); j++) {
                DeviceBean _devicebean = mDeviceList.getDevice(i).mBeanList.get(j);
                CLog.e(this.TAG, "添加所有的设备list到排序中：" + _devicebean.mDeviceName + "  code: " + _devicebean.mCode + "  ifaddNew:" + _devicebean.ifAddNew);
                this.mCanSortDeviceBeanList.add(_devicebean);
            }
        }
    }

    void init() {
        this.mDatasContainer = new DatasContainer();
    }

    boolean checkList() {
        if (mWhichSortIndex >= this.mCanSortDeviceBeanList.size()) {
            return true;
        }
        return false;
    }

    public void startDevice() {
        DeviceBean _getsortdeviceBean = this.mCanSortDeviceBeanList.get(mWhichSortIndex);
        m_DeviceBean = _getsortdeviceBean;
        int i = 0;
        loop0:
        while (true) {
            if (i >= mDeviceList.getListDevice().size()) {
                break;
            }
            CLog.d(this.TAG, "遍历设备   = " + mDeviceList.getDevice(i).mDeviceName + "   " + _getsortdeviceBean.mDeviceName + "  sort_code:" + _getsortdeviceBean.mCode + " ifaddnew:" + _getsortdeviceBean.ifAddNew);
            if (mDeviceList.getDevice(i).mDeviceName.equals(_getsortdeviceBean.mDeviceName)) {
                int j = 0;
                while (j < mDeviceList.getDevice(i).mBeanList.size()) {
                    DeviceBean _devicebean = mDeviceList.getDevice(i).mBeanList.get(j);
                    if (!_devicebean.mCode.equals(_getsortdeviceBean.mCode) || !_devicebean.ifAddNew) {
                        if (_devicebean.mCode.equals(_getsortdeviceBean.mCode) && _getsortdeviceBean.ifAddNew) {
                            mDeviceBeanList = mDeviceList.popDevice(i);
                            m_DeviceBean = _devicebean;
                            m_DeviceBean.ifAddNew = true;
                            CLog.i(this.TAG, "刷新链接的  是   = " + m_DeviceBean.mDeviceName + "   " + m_DeviceBean.mCode + "  mMacAddr = " + m_DeviceBean.mMacAddr);
                            break loop0;
                        }
                        CLog.i(this.TAG, "Skip previously added devices = " + _devicebean.mDeviceName + "   " + _devicebean.mCode + "  mMacAddr = " + _devicebean.mMacAddr);
                        j++;
                    } else {
                        mDeviceBeanList = mDeviceList.popDevice(i);
                        m_DeviceBean = _devicebean;
                        CLog.i(this.TAG, "自动 正在连接的是   = " + m_DeviceBean.mDeviceName + "   " + m_DeviceBean.mCode + "  mMacAddr = " + m_DeviceBean.mMacAddr);
                        break loop0;
                    }
                }
                continue;
            }
            i++;
        }
        CLog.e(this.TAG, "开始链接遍历出来的设备   = " + m_DeviceBean.mDeviceName + "  m_DeviceBean_code:" + m_DeviceBean.mCode + " ifaddnew:" + m_DeviceBean.ifAddNew + "  m_DeviceBean.mMacAddr :" + m_DeviceBean.mMacAddr);
        if (m_DeviceBean.mDeviceName != null && m_DeviceBean.mDeviceName.equalsIgnoreCase(Constants.DEVICE_8000GW_NAME) && m_DeviceBean.ifAddNew) {
            BluetoothDevice _device = BluetoothAdapter.getDefaultAdapter().getRemoteDevice(m_DeviceBean.mMacAddr);
            if (_device != null && _device.getBondState() == 12) {
                start8000GW();
            } else if (_device != null) {
                CLog.e(this.TAG, "_device!=null");
                try {
                    IntentFilter intent = new IntentFilter();
                    intent.addAction("android.bluetooth.device.action.BOND_STATE_CHANGED");
                    registerReceiver(this.searchDevices, intent);
                    CLog.d(this.TAG, "开始call 配对：" + ((Boolean) BluetoothDevice.class.getMethod("createBond", new Class[0]).invoke(_device, new Object[0])).booleanValue());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else if (m_DeviceBean.mMacAddr == null || m_DeviceBean.mMacAddr.equals(bs.b) || m_DeviceBean.mMacAddr.length() <= 2 || !m_DeviceBean.ifAddNew) {
            mDeviceBeanList.mState = 13;
            m_DeviceBean.mState = 13;
            App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(m_DeviceBean);
            Message msg = new Message();
            msg.what = 21;
            msg.arg2 = 3;
            App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(msg);
        } else {
            m_DeviceBean.mState = 1;
            m_DeviceBean.ifAddNew = false;
            mDeviceBeanList.mState = 1;
            App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(m_DeviceBean);
            ServiceBean.getInstance().init(m_DeviceBean);
            this.mServiceBean = ServiceBean.getInstance();
            startService(new Intent(this, this.mServiceBean.getmService().getClass()));
            CLog.i(this.TAG, "启动" + this.mServiceBean.getmService().getClass().getPackage().getName());
        }
    }

    private void start8000GW() {
        Intent inten = new Intent(this, GLActivity.class);
        inten.putExtra("device_address", m_DeviceBean.mMacAddr);
        inten.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(inten);
        Constants.START_8000GW = true;
        m_DeviceBean.ifAddNew = false;
        mDeviceBeanList.mState = 1;
        m_DeviceBean.mState = 1;
        m_DeviceBean.mProgress = 0;
        App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(m_DeviceBean);
    }

    public boolean stopDevice() {
        CLog.i(this.TAG, "Stop Device:" + this.mServiceBean.getmDeviceName());
        return stopService(new Intent(this, this.mServiceBean.getmService().getClass()));
    }

    public void startNext() {
        CLog.i(this.TAG, "Start Next Device");
        Constants.GO_TO_ADD_DEVICE = false;
        if (Constants.GO_TO_ADD_DEVICE) {
            Constants.GO_TO_ADD_DEVICE = false;
            Message msgss = new Message();
            msgss.what = Constants.V_NOTIFY_SEARCH_TIME_LONG_CANCLE;
            msgss.arg2 = 1;
            App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(msgss);
            CLog.e(this.TAG, "----------Start Next Device GO TO  ADD DEVICE-------------");
            SearchDevice.stopServer(this);
            stopServer(this);
            UploadService.stopServer(this);
            MessageManager.stopServer(this);
            DeviceService.stopServer(this);
            Server_Main.stopServer(this);
            Message msgs = new Message();
            msgs.what = Constants.START_ADD_DEVICE;
            msgs.arg2 = 1;
            App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(msgs);
        } else if (Constants.REPORT) {
            Message _msg = new Message();
            _msg.what = 532;
            _msg.arg2 = 6;
            App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(_msg);
            Constants.REPORT = false;
            Message msgss2 = new Message();
            msgss2.what = Constants.V_NOTIFY_SEARCH_TIME_LONG_CANCLE;
            msgss2.arg2 = 1;
            App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(msgss2);
            SearchDevice.stopServer(this);
            stopServer(this);
            UploadService.stopServer(this);
            MessageManager.stopServer(this);
            DeviceService.stopServer(this);
            Server_Main.stopServer(this);
            Message msgs2 = new Message();
            msgs2.what = Constants.V_START_REPORT;
            msgs2.arg2 = 1;
            App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(msgs2);
        } else {
            if (this.mServiceBean != null) {
                CLog.d(this.TAG, "关闭之前的service ********************");
                stopDevice();
                clearVariables();
            }
            if (checkList()) {
                CLog.e("lzerror", "设备全部搜索完成*********");
                Constants.BLUETOOTHSTAT = 4;
                CLog.e(this.TAG, "设备依次执行完成，开启回联*****或者是回联成功 开启下一次回联");
                CLog.i("jxx", "代码只想到这里2");
                Message msgManager = new Message();
                msgManager.what = 66;
                msgManager.arg2 = 14;
                msgManager.obj = false;
                App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(msgManager);
                stopSelf();
                return;
            }
            startDevice();
        }
    }

    void clearVariables() {
        this.mServiceBean = null;
    }

    public void onEvent(Message msg) {
        if (msg.arg2 == 3) {
            switch (msg.what) {
                case 21:
                    CLog.i(this.TAG, "OrderList.DM_NEXT_DEVICE  handler limian  +  mWhichDevice++");
                    mWhichDevice++;
                    mWhichSortIndex++;
                    startNext();
                    return;
                case 22:
                    CLog.i(this.TAG, "e OrderList.DM_PROCESS_DEVICE  handler limian");
                    startDevice();
                    return;
                case 54:
                    CLog.i(this.TAG, "SercheDevice 过来的");
                    mis_prioritysearch = true;
                    startNext();
                    return;
                case 55:
                    String _devicename = Constants.PRIORITY_DEVICENAME;
                    String _devicecode = Constants.PRIORITY_DEVICECODE;
                    String _sortdevicenamecode = String.valueOf(_devicename) + _devicecode;
                    SortDeviceContainer _SortDeviceContainer = new SortDeviceContainer(_sortdevicenamecode);
                    HashMap<String, SortDeviceContainer> mSortContainer = SortDeviceContainerMap.getInstance().getmSortDeviceMap();
                    DeviceBean deviceBeanfromnamecode = getDeviceBeanfromnamecode(_devicename, _devicecode);
                    int _inserposition = mWhichSortIndex + 1 + mSortContainer.size();
                    this.mCanSortDeviceBeanList.add(_inserposition, Constants.PRIORITY_DEVICE);
                    mSortContainer.put(_sortdevicenamecode, _SortDeviceContainer);
                    for (int i = mWhichSortIndex + 1 + 1; i < this.mCanSortDeviceBeanList.size(); i++) {
                        DeviceBean _willdelDevicebean = this.mCanSortDeviceBeanList.get(i);
                        if (_sortdevicenamecode.equals(String.valueOf(_willdelDevicebean.mDeviceName) + _willdelDevicebean.mCode)) {
                            this.mCanSortDeviceBeanList.remove(i);
                        }
                    }
                    CLog.i("lzerror", "Connection inserted; total number of connections: " + this.mCanSortDeviceBeanList.size() + "   Insert position: " + _inserposition + "  Currently connected: " + mWhichSortIndex + "  device = " + _SortDeviceContainer.getmDeviceNameCode());
                    return;
                case 58:
                    String _delname = Constants.PRIORITYDEL_DEVICENAME;
                    String _delcode = Constants.PRIORITYDEL_DEVICECODE;
                    int i2 = 0;
                    while (i2 < this.mCanSortDeviceBeanList.size()) {
                        DeviceBean _devicebean = this.mCanSortDeviceBeanList.get(i2);
                        if ((String.valueOf(_devicebean.mDeviceName) + _devicebean.mCode).equals(String.valueOf(_delname) + _delcode)) {
                            this.mCanSortDeviceBeanList.remove(i2);
                            i2--;
                            mWhichSortIndex--;
                        }
                        i2++;
                    }
                    return;
                default:
                    return;
            }
        }
    }

    private DeviceBean getDeviceBeanfromnamecode(String pname, String pcode) {
        for (int i = 0; i < mDeviceList.getListDevice().size(); i++) {
            if (mDeviceList.getDevice(i).mDeviceName.equals(pname)) {
                for (int j = 0; j < mDeviceList.getDevice(i).mBeanList.size(); j++) {
                    DeviceBean _devicebean = mDeviceList.getDevice(i).mBeanList.get(j);
                    if (pcode.equals(_devicebean.mCode)) {
                        return _devicebean;
                    }
                }
                continue;
            }
        }
        return null;
    }
}
