package com.contec.phms.device.template;

import android.annotation.SuppressLint;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import com.contec.phms.App_phms;
import com.contec.phms.SearchDevice.ContecDevices;
import com.contec.phms.domain.WaitConnectDeviceBean;
import com.contec.phms.manager.device.DeviceBean;
import com.contec.phms.manager.device.DeviceBeanList;
import com.contec.phms.manager.device.DeviceManager;
import com.contec.phms.db.DeviceListDaoOperation;
import com.contec.phms.util.CLog;
import com.contec.phms.util.Constants;
import com.contec.phms.util.PageUtil;
import com.contec.phms.util.PhmsSharedPreferences;
import com.contec.phms.widget.ConnectTraditionDialog;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import u.aly.bs;

public class PollingService extends Service {
    private static /* synthetic */ int[] $SWITCH_TABLE$com$contec$phms$util$PageUtil$BroadcastPacket = null;
    private static final int SEARCH_TIME = 3;
    public static final int STOP_SEARCH = 1;
    private static final String TAG = PollingService.class.getSimpleName();
    public static boolean isFromUpoladFaidedMsg = false;
    private final int POLL_INTERVAL = 10;
    private BluetoothLEScan mBleScan;
    private BluetoothAdapter mBluetoothAdapter;
    private Set<String> mDeviceAddressSet;
    private MyHandler mHandler;
    public final BroadcastReceiver mReceiverBluetoothOnOff = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            if ("android.bluetooth.adapter.action.STATE_CHANGED".equals(intent.getAction())) {
                int _state = intent.getIntExtra("android.bluetooth.adapter.extra.STATE", -1);
                if (_state == 12) {
                    CLog.i(PollingService.TAG, "Start search from  enable================");
                    PollingService.this.connectNextDevice_prepare();
                    context.unregisterReceiver(PollingService.this.mReceiverBluetoothOnOff);
                } else if (_state == 10) {
                    CLog.i(PollingService.TAG, "Open Buletooth================");
                }
            }
        }
    };
    private boolean mScanning = false;
    private List<WaitConnectDeviceBean> mWaitConnectDeviceBeanLists;
    private List<WaitConnectDeviceBean> mWaitConnectDeviceBeanLists_tradition;

    static /* synthetic */ int[] $SWITCH_TABLE$com$contec$phms$util$PageUtil$BroadcastPacket() {
        int[] iArr = $SWITCH_TABLE$com$contec$phms$util$PageUtil$BroadcastPacket;
        if (iArr == null) {
            iArr = new int[PageUtil.BroadcastPacket.values().length];
            try {
                iArr[PageUtil.BroadcastPacket.BLUE.ordinal()] = 2;
            } catch (NoSuchFieldError e) {
            }
            try {
                iArr[PageUtil.BroadcastPacket.GREEN.ordinal()] = 3;
            } catch (NoSuchFieldError e2) {
            }
            try {
                iArr[PageUtil.BroadcastPacket.RED.ordinal()] = 1;
            } catch (NoSuchFieldError e3) {
            }
            $SWITCH_TABLE$com$contec$phms$util$PageUtil$BroadcastPacket = iArr;
        }
        return iArr;
    }

    private static class MyHandler extends Handler {
        private WeakReference<PollingService> mWReference;

        public MyHandler(PollingService pollingService) {
            this.mWReference = new WeakReference<>(pollingService);
        }

        @SuppressLint({"NewApi"})
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            PollingService pollingService = (PollingService) this.mWReference.get();
            if (pollingService != null) {
                switch (msg.what) {
                    case 1:
                        pollingService.CLogE("BLe搜索结束");
                        pollingService.mScanning = false;
                        pollingService.mBluetoothAdapter.stopLeScan(pollingService.mBleScan.mLeScanCallback);
                        pollingService.CLogI("传统设备个数：" + pollingService.mWaitConnectDeviceBeanLists_tradition.size());
                        pollingService.CLogI("总设备个数：" + pollingService.mWaitConnectDeviceBeanLists.size());
                        if (pollingService.mWaitConnectDeviceBeanLists_tradition.size() > 0) {
                            pollingService.mWaitConnectDeviceBeanLists.addAll(pollingService.mWaitConnectDeviceBeanLists_tradition);
                        }
                        if (pollingService.mWaitConnectDeviceBeanLists.size() > 0) {
                            CLog.i("jxx", "缓冲集合大小：" + pollingService.mWaitConnectDeviceBeanLists.size());
                            App_phms.getInstance().setmWaitConnectDeviceBeanLists(pollingService.mWaitConnectDeviceBeanLists);
                            CLog.i("jxx", "全局变量集合大小：" + App_phms.getInstance().getmWaitConnectDeviceBeanLists().size());
                            pollingService.startConnectDevice();
                            return;
                        }
                        pollingService.cancleTimer();
                        pollingService.timer();
                        return;
                    default:
                        return;
                }
            }
        }
    }

    public void onCreate() {
        super.onCreate();
        App_phms.getInstance().mEventBus.register(this);
        CLogI("----ble轮询服务类启动");
    }

    public IBinder onBind(Intent arg0) {
        return null;
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            CLogI("call onStartCommand method");
            initData();
            if (!PageUtil.isPhoneSupportBLE(App_phms.getInstance().getApplicationContext())) {
                changeBLEMode_true(false);
                if (App_phms.getInstance().getmWaitConnectDeviceBeanLists().size() > 0) {
                    startConnectDevice();
                } else {
                    CLogI("执行到这里222");
                    startCallBack();
                }
            } else if (JudgeDeviceType()) {
                changeBLEMode_true(true);
                CLog.i("jxx", "xx集合的大小2: " + App_phms.getInstance().getmWaitConnectDeviceBeanLists().size());
                if (App_phms.getInstance().getmWaitConnectDeviceBeanLists().size() > 0) {
                    startConnectDevice();
                } else {
                    startPolling();
                }
            } else {
                changeBLEMode_true(false);
                if (App_phms.getInstance().getmWaitConnectDeviceBeanLists().size() > 0) {
                    startConnectDevice();
                } else {
                    CLogI("执行到这里111");
                    startCallBack();
                }
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }

    private void initData() {
        this.mWaitConnectDeviceBeanLists = new ArrayList();
        this.mWaitConnectDeviceBeanLists_tradition = new ArrayList();
        this.mHandler = new MyHandler(this);
        this.mDeviceAddressSet = new HashSet();
    }

    private synchronized void startPolling() {
        CLogI("开启轮询模式=-------");
        this.mDeviceAddressSet.clear();
        this.mWaitConnectDeviceBeanLists_tradition.clear();
        this.mWaitConnectDeviceBeanLists.clear();
        getBluetoothAdapter();
        scanLeDevice(true);
    }

    @SuppressLint({"NewApi"})
    private void getBluetoothAdapter() {
        if (PageUtil.isPhoneSupportBLE(App_phms.getInstance().getApplicationContext())) {
            BluetoothManager bluetoothManager = (BluetoothManager) getSystemService("bluetooth");
            if (this.mBluetoothAdapter == null) {
                this.mBluetoothAdapter = bluetoothManager.getAdapter();
            }
        } else if (this.mBluetoothAdapter == null) {
            this.mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        }
        if (this.mBluetoothAdapter.getState() == 10) {
            this.mBluetoothAdapter.enable();
        }
        if (this.mBluetoothAdapter == null) {
        }
    }

    @SuppressLint({"NewApi"})
    private void scanLeDevice(boolean enable) {
        if (this.mBleScan == null) {
            this.mBleScan = new BluetoothLEScan(this, (BluetoothLEScan) null);
        }
        if (enable) {
            this.mScanning = true;
            this.mBluetoothAdapter.startLeScan(this.mBleScan.mLeScanCallback);
            this.mHandler.sendEmptyMessageDelayed(1, 3000);
            return;
        }
        this.mScanning = false;
        this.mBluetoothAdapter.stopLeScan(this.mBleScan.mLeScanCallback);
    }

    private class BluetoothLEScan {
        @SuppressLint({"NewApi"})
        public BluetoothAdapter.LeScanCallback mLeScanCallback;

        private BluetoothLEScan() {
            this.mLeScanCallback = new BluetoothAdapter.LeScanCallback() {
                public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
                    String address = device.getAddress();
                    String name = device.getName();
                    if (address != null && name != null && !PollingService.this.mDeviceAddressSet.contains(address)) {
                        PollingService.this.CLogI("找到新设备");
                        PollingService.this.mDeviceAddressSet.add(address);
                        if (scanRecord[5] == 18 && scanRecord[6] == -1) {
                            PollingService.this.checkDevice(device, Constants.DEVICE_BLUEBOOTH_TYPE_BLE, scanRecord);
                        } else {
                            PollingService.this.checkDevice(device, Constants.DEVICE_BLUEBOOTH_TYPE_CLASSIC, scanRecord);
                        }
                    }
                }
            };
        }

        /* synthetic */ BluetoothLEScan(PollingService pollingService, BluetoothLEScan bluetoothLEScan) {
            this();
        }
    }

    /* access modifiers changed from: private */
    @SuppressLint({"NewApi"})
    public void checkDevice(BluetoothDevice device, String mBluetoothType, byte[] scanRecord) {
        CLog.i("jxx", "搜到的设备：" + device.getAddress() + "\n" + "名字：" + device.getName() + "\n类型：" + mBluetoothType);
        if (Build.VERSION.SDK_INT >= 18) {
            CLog.i("jxx", "type:" + device.getType());
        }
        CLog.i("jxx", "-----------------");
        String _name = device.getName();
        if (_name != null) {
            String _deviceName = ContecDevices.checkDevice(_name);
            String _mac = device.getAddress();
            if (_deviceName != null && !_deviceName.equals(bs.b)) {
                boolean ifHave = false;
                ArrayList<DeviceBean> _Beans = DeviceListDaoOperation.getInstance().getDevice();
                DeviceBean currentDeviceBean = new DeviceBean(bs.b, bs.b);
                int i = 0;
                while (true) {
                    if (i < _Beans.size()) {
                        if (_Beans.get(i).mDeviceName.equals(_deviceName) && _mac.equals(_Beans.get(i).mMacAddr)) {
                            ifHave = true;
                            currentDeviceBean = _Beans.get(i);
                            break;
                        }
                        i++;
                    } else {
                        break;
                    }
                }
                if (ifHave) {
                    switch ($SWITCH_TABLE$com$contec$phms$util$PageUtil$BroadcastPacket()[PageUtil.unPackData(scanRecord).ordinal()]) {
                        case 1:
                            String bluetoothType = currentDeviceBean.getmBluetoothType();
                            if (Constants.DEVICE_BLUEBOOTH_TYPE_CLASSIC.equals(bluetoothType)) {
                                List<WaitConnectDeviceBean> getmRemoveConnectDeviceBeanLists = App_phms.getInstance().getmRemoveConnectDeviceBeanLists();
                                CLog.i("jxx", "移除设备集合的大小2:" + App_phms.getInstance().getmRemoveConnectDeviceBeanLists().size());
                                boolean ifFind = false;
                                int i2 = 0;
                                while (true) {
                                    if (i2 < getmRemoveConnectDeviceBeanLists.size()) {
                                        String mMacAddr = getmRemoveConnectDeviceBeanLists.get(i2).getmDevicebean().mMacAddr;
                                        if (mMacAddr != null) {
                                            if (mMacAddr.equals(currentDeviceBean.mMacAddr)) {
                                                ifFind = true;
                                            } else {
                                                ifFind = false;
                                            }
                                        }
                                        i2++;
                                    }
                                }
                                //if (!ifFind) {
                                //    this.mWaitConnectDeviceBeanLists_tradition.add(new WaitConnectDeviceBean(currentDeviceBean, 0, false));
                                //    return;
                                //}
                                //return;
                            } else if (Constants.DEVICE_BLUEBOOTH_TYPE_BLE.equals(bluetoothType)) {
                                this.mWaitConnectDeviceBeanLists.add(new WaitConnectDeviceBean(currentDeviceBean, 0, false));
                                return;
                            } else {
                                return;
                            }
                        case 3:
                            CLogI("添加待连接的设备");
                            this.mWaitConnectDeviceBeanLists.add(new WaitConnectDeviceBean(currentDeviceBean, 0, true));
                            return;
                        default:
                            return;
                    }
                }
            }
        }
    }

    private void startConnectDevice() {
        CLogI("连接下一个设备。。。");
        List<WaitConnectDeviceBean> waitConnectDeviceBeanLists = App_phms.getInstance().getmWaitConnectDeviceBeanLists();
        if (waitConnectDeviceBeanLists.size() > 0) {
            CLogI("进入到if语句");
            sendConnectDeviceMsg_prepare(waitConnectDeviceBeanLists.get(0), false);
            return;
        }
        startPollOrCallBack();
    }

    public void onEvent(Message msg) {
        switch (msg.arg2) {
            case 14:
                CLogI("接收到消息，连接下一个设备 isFromUpoladFaidedMsg:" + isFromUpoladFaidedMsg);
                if (isFromUpoladFaidedMsg) {
                    isFromUpoladFaidedMsg = false;
                    if (!PageUtil.isPhoneSupportBLE(App_phms.getInstance().getApplicationContext())) {
                        changeBLEMode_true(false);
                        startCallBack();
                        return;
                    } else if (JudgeDeviceType()) {
                        changeBLEMode_true(true);
                        connectNextDevice_prepare();
                        return;
                    } else {
                        changeBLEMode_true(false);
                        startCallBack();
                        return;
                    }
                } else {
                    CLogI("接收到消息，连接下一个设备----");
                    if (JudgeDeviceType()) {
                        changeBLEMode_true(true);
                        connectNextDevice_prepare();
                        return;
                    }
                    changeBLEMode_true(false);
                    startCallBack();
                    return;
                }
            default:
                return;
        }
    }

    public void connectNextDevice_prepare() {
        CLogI("call connectNextDevice_prepare method");
        if (DeviceManager.mDeviceList != null && DeviceManager.mDeviceList.size() > 0) {
            getBluetoothAdapter();
            if (!this.mBluetoothAdapter.isEnabled()) {
                registerReceiver(this.mReceiverBluetoothOnOff, new IntentFilter("android.bluetooth.adapter.action.STATE_CHANGED"));
                this.mBluetoothAdapter.enable();
                return;
            }
            connectNextDevice_judgeCurrentDevice();
        }
    }

    private void connectNextDevice_judgeCurrentDevice() {
        CLog.i("jxx", "开始连接下一个设备");
        List<WaitConnectDeviceBean> waitConnectDeviceBeanLists = App_phms.getInstance().getmWaitConnectDeviceBeanLists();
        if (Constants.isSuccessOperationDevice) {
            CLogI("上次操作成功");
            addToRemoveDeviceBeanList(waitConnectDeviceBeanLists);
            tryConnectDevice(waitConnectDeviceBeanLists);
            return;
        }
        CLogI("上次操作失败一次");
        Constants.isSuccessOperationDevice = true;
        if (waitConnectDeviceBeanLists.size() > 0) {
            CLogI("进入到if语句");
            WaitConnectDeviceBean waitConnectDeviceBean = waitConnectDeviceBeanLists.get(0);
            int connectedCount = waitConnectDeviceBean.getmConnectedCount();
            CLogI("count:" + connectedCount);
            if (!PageUtil.isPhoneSupportBLE(App_phms.getInstance().getApplicationContext())) {
                changeBLEMode_true(false);
                JudgeConnectCount(waitConnectDeviceBeanLists, waitConnectDeviceBean, connectedCount);
            } else if (JudgeDeviceType()) {
                changeBLEMode_true(true);
                String bluetoothType = waitConnectDeviceBean.getmDevicebean().getmBluetoothType();
                if (Constants.DEVICE_BLUEBOOTH_TYPE_CLASSIC.equals(bluetoothType)) {
                    if (waitConnectDeviceBean.ismIfFirstAddOrActiveClick()) {
                        JudgeConnectCount(waitConnectDeviceBeanLists, waitConnectDeviceBean, connectedCount);
                    } else {
                        JudgeConnectCount(waitConnectDeviceBeanLists, waitConnectDeviceBean, connectedCount);
                    }
                } else if (Constants.DEVICE_BLUEBOOTH_TYPE_BLE.equals(bluetoothType)) {
                    JudgeConnectCount(waitConnectDeviceBeanLists, waitConnectDeviceBean, connectedCount);
                }
            } else {
                changeBLEMode_true(false);
                JudgeConnectCount(waitConnectDeviceBeanLists, waitConnectDeviceBean, connectedCount);
            }
        }
    }

    private void addToRemoveDeviceBeanList(List<WaitConnectDeviceBean> waitConnectDeviceBeanLists) {
        if (PageUtil.isPhoneSupportBLE(App_phms.getInstance().getApplicationContext()) && JudgeDeviceType()) {
            changeBLEMode_true(true);
            if (waitConnectDeviceBeanLists.size() > 0) {
                WaitConnectDeviceBean waitConnectDeviceBean = waitConnectDeviceBeanLists.get(0);
                String mBluetoothType = waitConnectDeviceBean.getmDevicebean().mBluetoothType;
                boolean ismIfFirstAddOrActiveClick = waitConnectDeviceBean.ismIfFirstAddOrActiveClick();
                if (Constants.DEVICE_BLUEBOOTH_TYPE_CLASSIC.equals(mBluetoothType) && !ismIfFirstAddOrActiveClick) {
                    App_phms.getInstance().getmRemoveConnectDeviceBeanLists().add(waitConnectDeviceBean);
                }
            }
        }
    }

    private void JudgeConnectCount(List<WaitConnectDeviceBean> waitConnectDeviceBeanLists, WaitConnectDeviceBean waitConnectDeviceBean, int connectedCount) {
        if (connectedCount == 1) {
            sendConnectDeviceMsg_prepare(waitConnectDeviceBean, true);
        } else if (connectedCount > 1) {
            addToRemoveDeviceBeanList(waitConnectDeviceBeanLists);
            tryConnectDevice(waitConnectDeviceBeanLists);
        }
    }

    private void tryConnectDevice(List<WaitConnectDeviceBean> waitConnectDeviceBeanLists) {
        if (waitConnectDeviceBeanLists.size() > 0) {
            waitConnectDeviceBeanLists.remove(0);
        } else {
            startPollOrCallBack();
        }
        CLogI("待连接的设备：" + App_phms.getInstance().getmWaitConnectDeviceBeanLists().size());
        if (waitConnectDeviceBeanLists.size() > 0) {
            sendConnectDeviceMsg_prepare(waitConnectDeviceBeanLists.get(0), false);
        } else {
            startPollOrCallBack();
        }
    }

    private void sendConnectDeviceMsg_prepare(final WaitConnectDeviceBean waitConnectDeviceBean, boolean isAgain) {
        if (waitConnectDeviceBean != null && waitConnectDeviceBean.getmDevicebean() != null && waitConnectDeviceBean.getmDevicebean().getmBluetoothType() != null && !waitConnectDeviceBean.getmDevicebean().getmBluetoothType().equalsIgnoreCase(bs.b)) {
            String bluetoothType = waitConnectDeviceBean.getmDevicebean().getmBluetoothType();
            boolean ismIfFirstAddOrActiveClick = waitConnectDeviceBean.ismIfFirstAddOrActiveClick();
            CLogI("代码进入到sendConnectDeviceMsg_prepare----isAgain:" + isAgain + "  bluetoothType:" + bluetoothType);
            if (Constants.DEVICE_BLUEBOOTH_TYPE_CLASSIC.equals(bluetoothType) && !isAgain && !ismIfFirstAddOrActiveClick) {
                CLogI("sendConnectDeviceMsg_prepare--准备弹出框");
                Context topActivity = PageUtil.getTopActivity();
                if (topActivity != null) {
                    final ConnectTraditionDialog connectTraditionDialog = new ConnectTraditionDialog(topActivity, waitConnectDeviceBean.getmDevicebean());
                    connectTraditionDialog.getmDialog().setOnDismissListener(new DialogInterface.OnDismissListener() {
                        public void onDismiss(DialogInterface dialog) {
                            if (connectTraditionDialog.isConnectTraditionDevice()) {
                                PollingService.this.sendMsg(waitConnectDeviceBean);
                                return;
                            }
                            Constants.isSuccessOperationDevice = true;
                            PollingService.this.connectNextDevice_judgeCurrentDevice();
                        }
                    });
                    return;
                }
                Constants.isSuccessOperationDevice = true;
                connectNextDevice_judgeCurrentDevice();
            } else if (Constants.DEVICE_BLUEBOOTH_TYPE_BLE.equals(bluetoothType)) {
                sendMsg(waitConnectDeviceBean);
            } else if (Constants.DEVICE_BLUEBOOTH_TYPE_CLASSIC.equals(bluetoothType)) {
                sendMsg(waitConnectDeviceBean);
            }
        }
    }

    private void sendMsg(WaitConnectDeviceBean waitConnectDeviceBean) {
        DeviceManager.mRefreshBean = new DeviceBean(bs.b, bs.b);
        waitConnectDeviceBean.setmConnectedCount(waitConnectDeviceBean.getmConnectedCount() + 1);
        CLogI("连接次数：" + App_phms.getInstance().getmWaitConnectDeviceBeanLists().get(0).getmConnectedCount() + " " + waitConnectDeviceBean.getmConnectedCount());
        DeviceManager.mRefreshBean = waitConnectDeviceBean.getmDevicebean();
        Message msgs = new Message();
        msgs.what = Constants.CONNECT_DEVICE_HM;
        msgs.arg2 = 1;
        App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(msgs);
    }

    private void startPollOrCallBack() {
        if (!PageUtil.isPhoneSupportBLE(App_phms.getInstance().getApplicationContext())) {
            changeBLEMode_true(false);
            startCallBack();
        } else if (JudgeDeviceType()) {
            changeBLEMode_true(true);
            timer();
        } else {
            changeBLEMode_true(false);
            startCallBack();
        }
    }

    private void startCallBack() {
        CLogI("开启回连模式-------");
        BluetoothServerService.stopServer(App_phms.getInstance().getApplicationContext());
        Intent _intentBServer = new Intent(App_phms.getInstance().getApplicationContext(), BluetoothServerService.class);
        _intentBServer.putExtra("start_thread", true);
        App_phms.getInstance().getApplicationContext().startService(_intentBServer);
    }

    private void timer() {
        CLog.i(TAG, "等待10s,开启下一轮轮询");
        this.mHandler.removeCallbacksAndMessages((Object) null);
        this.mHandler.postDelayed(new Runnable() {
            public void run() {
                PollingService.this.startPolling();
            }
        }, 10000);
    }

    private void cancleTimer() {
        if (this.mHandler != null) {
            this.mHandler.removeCallbacksAndMessages((Object) null);
        }
    }

    public void onDestroy() {
        super.onDestroy();
        exitBle();
        cancleTimer();
        App_phms.getInstance().mEventBus.unregister(this);
    }

    private void exitBle() {
        if (this.mScanning) {
            scanLeDevice(false);
        }
    }

    public static void stopService(Context pContext) {
        CLog.e(TAG, "停止轮询服务类");
        App_phms.getInstance().getmWaitConnectDeviceBeanLists().clear();
        isFromUpoladFaidedMsg = false;
        pContext.stopService(new Intent(pContext, PollingService.class));
    }

    private boolean JudgeDeviceType() {
        boolean ifOpenBLe = PhmsSharedPreferences.getInstance(App_phms.getInstance().getApplicationContext()).getBoolean("ifOpenBLe", true);
        int broadcastPackFiled = 0;
        int traditionCount = 0;
        if (DeviceManager.mDeviceList == null) {
            CLog.i(TAG, "执行到else语句");
        } else if (DeviceManager.mDeviceList.getListDevice() != null) {
            List<DeviceBeanList> deviceBeanLists = DeviceManager.mDeviceList.getListDevice();
            if (deviceBeanLists.size() > 0) {
                for (int i = 0; i < deviceBeanLists.size(); i++) {
                    DeviceBeanList deviceBeanList = deviceBeanLists.get(i);
                    for (int j = 0; j < deviceBeanList.mBeanList.size(); j++) {
                        DeviceBean deviceBean = deviceBeanList.mBeanList.get(j);
                        CLog.i("jxx", "deviceBean.mBroadcastPacketFiled:" + deviceBean.mBroadcastPacketFiled);
                        CLog.i("jxx", "deviceBean.mBluetoothType:" + deviceBean.mBluetoothType);
                        if (Constants.BroadcastPacketHasFiled.equals(deviceBean.mBroadcastPacketFiled) && Constants.DEVICE_BLUEBOOTH_TYPE_BLE.equalsIgnoreCase(deviceBean.mBluetoothType)) {
                            broadcastPackFiled++;
                        } else if (Constants.BroadcastPacketNoFiled.equals(deviceBean.mBroadcastPacketFiled)) {
                            traditionCount++;
                        }
                    }
                }
            } else {
                stopSelf();
            }
        }
        if (ifOpenBLe) {
            if (broadcastPackFiled > 0 && traditionCount == 0) {
                return true;
            }
            if (broadcastPackFiled == 0 && traditionCount > 0) {
                CLog.i(TAG, "执行到else语句111");
                return false;
            } else if (broadcastPackFiled > 0 && traditionCount > 0) {
                return true;
            }
        } else if (broadcastPackFiled > 0 && traditionCount == 0) {
            return true;
        } else {
            if (broadcastPackFiled == 0 && traditionCount > 0) {
                CLog.i(TAG, "执行到else语句222");
                return false;
            } else if (broadcastPackFiled > 0 && traditionCount > 0) {
                CLog.i(TAG, "执行到else语句3");
                return false;
            }
        }
        return false;
    }

    private void changeBLEMode_true(boolean ifOpenBLE) {
    }

    public void CLogI(String msg) {
        if (Constants.mTestFlag) {
            CLog.i(TAG, msg);
        }
    }

    public void CLogE(String msg) {
        if (Constants.mTestFlag) {
            CLog.e(TAG, msg);
        }
    }
}
