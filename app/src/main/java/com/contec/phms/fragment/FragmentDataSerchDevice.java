package com.contec.phms.fragment;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import androidx.fragment.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.contec.circleimage.widget.CircularImage;
import com.contec.phms.App_phms;
import com.contec.phms.R;
import com.contec.phms.SearchDevice.ContecDevices;
import com.contec.phms.activity.MainActivityNew;
import com.contec.phms.device.template.PollingService;
import com.contec.phms.domain.WaitConnectDeviceBean;
import com.contec.phms.manager.device.DeviceBean;
import com.contec.phms.manager.device.DeviceBeanList;
import com.contec.phms.manager.device.DeviceManager;
import com.contec.phms.db.DeviceListDaoOperation;
import com.contec.phms.db.DeviceListItemBeanDao;
import com.contec.phms.db.LocalLoginInfoManager;
import com.contec.phms.util.CLog;
import com.contec.phms.util.Constants;
import com.contec.phms.util.CustomDialog;
import com.contec.phms.util.DeviceNameUtils;
import com.contec.phms.util.FileOperation;
import com.contec.phms.util.PageUtil;
import com.contec.phms.widget.DialogClass;
import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.httpclient.cookie.CookieSpec;
import u.aly.bs;

@SuppressLint({"NewApi"})
public class FragmentDataSerchDevice extends FragmentBase {
    private static String TAG = FragmentDataSerchDevice.class.getSimpleName();
    public static String mDeviceCode;
    public static String mDeviceName;
    public static Handler mHandler;
    public static String mMac;
    public static int mPosition;
    private boolean ISREGISTBLUETOOTHREVIER = false;
    private String SDCARD_PATH = "/mnt/sdcard/phms/";
    private final int SEARCH_TIME = 8;
    CustomDialog dialog;
    private BluetoothLEScan mBleScan;
    private BluetoothAdapter mBluetoothAdapter;
    private DialogClass mDaClass;
    private ArrayList<SerchedDevice> mDeviceArray;
    private CircularImage mDevicelist_change_user;
    private TextView mDevices_add_count;
    private String mHeadName = "imagehead.png";
    public final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if ("android.bluetooth.device.action.FOUND".equals(action)) {
                BluetoothDevice device = (BluetoothDevice) intent.getParcelableExtra("android.bluetooth.device.extra.DEVICE");
                CLog.i(FragmentDataSerchDevice.TAG, "BroadcastReceiver: " + action + "  device name:" + device.getName() + "  mac:" + device.getAddress());
                if (device.getName() == null) {
                    return;
                }
                if (!device.getName().contains("SpO209") || !device.getName().contains("SpO210")) {
                    FragmentDataSerchDevice.this.checkDevice(device, Constants.DEVICE_BLUEBOOTH_TYPE_CLASSIC, new byte[0]);
                }
            } else if ("android.bluetooth.adapter.action.DISCOVERY_FINISHED".equals(action)) {
                FragmentDataSerchDevice.this.mautoSearchDevicesProgressBar.setVisibility(View.INVISIBLE);
            } else if ("android.bluetooth.adapter.action.STATE_CHANGED".equals(action) && FragmentDataSerchDevice.this.mBluetoothAdapter.getState() == 12) {
                FragmentDataSerchDevice.this.mBluetoothAdapter.startDiscovery();
            }
        }
    };
    private Button mRefreshdevices;
    private Button mReturnbtn;
    private boolean mScanning = false;
    private DataSerchDeviceAdapter mSerchedAdapter;
    private boolean mUsedBLEScan = false;
    private TextView mUserNameTextView;
    private View mView;
    private ProgressBar mautoSearchDevicesProgressBar;
    private TextView mdevicelist_data_collection;
    private int mfragmentSerialNumberindex = 7;
    private boolean onPause = false;
    private boolean phoneHardWareIsSupportBLE = false;
    private boolean phoneSystemVersionIsSupport = false;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.mView = inflater.inflate(R.layout.layout_data_serchdevice, container, false);
        initView(this.mView);
        initData(this.mView);
        return this.mView;
    }

    private void initData(View pview) {
        this.mDeviceArray = new ArrayList<>();
        this.mDevices_add_count.setText(new StringBuilder(String.valueOf(String.valueOf(getActivity().getString(R.string.serch_device_promt)) + this.mDeviceArray.size() + getString(R.string.serch_device_promt_howmuch))).toString());
        String _userName = PageUtil.getLoginUserInfo().mUserName;
        if (_userName == null || _userName.equals(bs.b)) {
            String name = PageUtil.getLoginUserInfo().mUID;
            if (LocalLoginInfoManager.getInstance().querySqlThirdCode(name)) {
                String _userName2 = LocalLoginInfoManager.getInstance().findByCardId(name).mThirdCode;
                if (_userName2.contains("@")) {
                    String[] targetQ = _userName2.split("@");
                    _userName = targetQ[0].substring(targetQ[0].length() - 4);
                } else {
                    _userName = name.substring(name.length() - 4);
                }
            } else {
                _userName = name.substring(name.length() - 4);
            }
        }
        this.mUserNameTextView.setText(_userName);
        getSdcardPhoto(App_phms.getInstance().GetUserInfoNAME(), "imagehead.jpg");
        this.mSerchedAdapter = new DataSerchDeviceAdapter(getActivity(), this.mDeviceArray);
        ((ListView) pview.findViewById(R.id.serchedlist)).setAdapter(this.mSerchedAdapter);
        mHandler = new Handler() {
            public void dispatchMessage(Message msg) {
                switch (msg.what) {
                    case 1:
                        CLog.i(FragmentDataSerchDevice.TAG, "确认添加设了，发送了handlerle ");
                        FragmentDataSerchDevice.this.mDaClass = new DialogClass(FragmentDataSerchDevice.this.getActivity(), FragmentDataSerchDevice.this.getActivity().getResources().getString(R.string.add_device_success_str));
                        Constants.ADD_DEVICE = true;
                        FragmentDataSerchDevice.this.cancleDialog();
                        FragmentDataSerchDevice.this.mDeviceArray.remove(msg.arg1);
                        FragmentDataSerchDevice.this.mSerchedAdapter.notifyDataSetChanged();
                        FragmentDataSerchDevice.this.mDevices_add_count.setText(new StringBuilder(String.valueOf(String.valueOf(FragmentDataSerchDevice.this.getString(R.string.serch_device_promt)) + FragmentDataSerchDevice.this.mDeviceArray.size() + FragmentDataSerchDevice.this.getString(R.string.serch_device_promt_howmuch))).toString());
                        FragmentDataSerchDevice.this.onStop();
                        if (FragmentDataSerchDevice.this.mDeviceArray.size() == 0) {
                            FragmentDataSerchDevice.this.sendCloseSerachFragmentMsg();
                            return;
                        }
                        return;
                    case 2:
                        if (FragmentDataSerchDevice.this.mDaClass != null) {
                            FragmentDataSerchDevice.this.mDaClass.dismiss();
                            return;
                        }
                        return;
                    case 3:
                        FragmentDataSerchDevice.this.mSerchedAdapter.notifyDataSetChanged();
                        FragmentDataSerchDevice.this.mDevices_add_count.setText(new StringBuilder(String.valueOf(String.valueOf(FragmentDataSerchDevice.this.getActivity().getString(R.string.serch_device_promt)) + FragmentDataSerchDevice.this.mDeviceArray.size() + FragmentDataSerchDevice.this.getActivity().getString(R.string.serch_device_promt_howmuch))).toString());
                        return;
                    case 4:
                        if (!FragmentDataSerchDevice.this.onPause) {
                            FragmentDataSerchDevice.this.mScanning = false;
                            FragmentDataSerchDevice.this.mBluetoothAdapter.stopLeScan(FragmentDataSerchDevice.this.mBleScan.mLeScanCallback);
                            FragmentDataSerchDevice.this.mautoSearchDevicesProgressBar.setVisibility(View.INVISIBLE);
                            return;
                        }
                        return;
                    case 5:
                        Toast.makeText(FragmentDataSerchDevice.this.getActivity().getApplicationContext(), FragmentDataSerchDevice.this.getActivity().getString(R.string.str_serchdevice_dialog), Toast.LENGTH_LONG).show();
                        return;
                    default:
                        return;
                }
            }
        };
        this.mReturnbtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                FragmentDataSerchDevice.this.sendCloseSerachFragmentMsg();
            }
        });
        this.mRefreshdevices.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                if (FragmentDataSerchDevice.this.mBluetoothAdapter.isDiscovering()) {
                    FragmentDataSerchDevice.this.mBluetoothAdapter.cancelDiscovery();
                    FragmentDataSerchDevice.this.register(FragmentDataSerchDevice.this.mUsedBLEScan);
                    return;
                }
                Constants.GO_TO_ADD_DEVICE = false;
                FragmentDataSerchDevice.this.mautoSearchDevicesProgressBar.setVisibility(View.VISIBLE);
                FragmentDataSerchDevice.this.mDeviceArray.clear();
                FragmentDataSerchDevice.this.mSerchedAdapter.notifyDataSetChanged();
                CLog.e(FragmentDataSerchDevice.TAG, "   添加设备的onresume 状态******  要清空列表了");
                FragmentDataSerchDevice.this.mDevices_add_count.setText(new StringBuilder(String.valueOf(String.valueOf(FragmentDataSerchDevice.this.getActivity().getString(R.string.serch_device_promt)) + FragmentDataSerchDevice.this.mDeviceArray.size() + FragmentDataSerchDevice.this.getActivity().getString(R.string.serch_device_promt_howmuch))).toString());
                FragmentDataSerchDevice.this.register(FragmentDataSerchDevice.this.mUsedBLEScan);
            }
        });
    }

    private void sendCloseSerachFragmentMsg() {
        getActivity().startService(new Intent(getActivity(), PollingService.class));
        Message msg = new Message();
        msg.what = Constants.CLOSE_SEARCHDEVICE_FRAGMENT;
        msg.arg2 = 1;
        App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(msg);
    }

    public void onStart() {
        super.onStart();
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CLog.e(TAG, "添加设备的地方回来了onCreate  ******************");
    }

    private void initView(View pview) {
        this.mdevicelist_data_collection = (TextView) pview.findViewById(R.id.devicelist_data_collection);
        this.mautoSearchDevicesProgressBar = (ProgressBar) pview.findViewById(R.id.autoSearchDevicesProgressBar);
        this.mDevices_add_count = (TextView) pview.findViewById(R.id.devices_add_count);
        this.mDevices_add_count = (TextView) pview.findViewById(R.id.devices_add_count);
        this.mDevices_add_count = (TextView) pview.findViewById(R.id.devices_add_count);
        this.mDevices_add_count = (TextView) pview.findViewById(R.id.devices_add_count);
        this.mDevices_add_count = (TextView) pview.findViewById(R.id.devices_add_count);
        this.mDevices_add_count = (TextView) pview.findViewById(R.id.devices_add_count);
        this.mDevices_add_count = (TextView) pview.findViewById(R.id.devices_add_count);
        this.mDevicelist_change_user = (CircularImage) pview.findViewById(R.id.devicelist_change_user);
        this.mReturnbtn = (Button) pview.findViewById(R.id.returnbtn);
        this.mRefreshdevices = (Button) pview.findViewById(R.id.adddevices);
        this.mUserNameTextView = (TextView) this.mView.findViewById(R.id.user_name_text);
    }

    public void register(boolean isBle) {
        if (!isBle) {
            LogI("使用传统蓝牙搜索设备");
            this.ISREGISTBLUETOOTHREVIER = true;
            CLog.i(TAG, "register ActivityDataSerchDevice receiver");
            IntentFilter filter = new IntentFilter("android.bluetooth.device.action.FOUND");
            filter.addAction("android.bluetooth.adapter.action.DISCOVERY_FINISHED");
            filter.addAction("android.bluetooth.adapter.action.STATE_CHANGED");
            getActivity().registerReceiver(this.mReceiver, filter);
            this.mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            if (this.mBluetoothAdapter.getState() == 12) {
                CLog.i(TAG, "判断是否是搜索状态：" + this.mBluetoothAdapter.isDiscovering());
                if (this.mBluetoothAdapter.isDiscovering()) {
                    this.mBluetoothAdapter.cancelDiscovery();
                }
                if (!this.mBluetoothAdapter.startDiscovery()) {
                    CLog.i(TAG, "startDiscovery failed, close bluetooth!");
                    this.mBluetoothAdapter.startDiscovery();
                }
            } else if (this.mBluetoothAdapter.getState() == 10) {
                this.mBluetoothAdapter.enable();
                CLog.i(TAG, "Bluetooth State: " + this.mBluetoothAdapter.isEnabled());
            }
        } else if (!this.phoneHardWareIsSupportBLE || !this.phoneSystemVersionIsSupport) {
            register(false);
        } else {
            this.mBluetoothAdapter = ((BluetoothManager) getActivity().getSystemService("bluetooth")).getAdapter();
            if (this.mBluetoothAdapter.getState() == 10) {
                this.mBluetoothAdapter.enable();
            }
            if (this.mBluetoothAdapter == null) {
                register(false);
                return;
            }
            CLog.i(TAG, "使用BLE搜索设备");
            scanLeDevice(true);
        }
    }

    public void onResume() {
        super.onResume();
        this.onPause = false;
        if (MainActivityNew.mfragmentcurrentindex != 5) {
            CLog.e(TAG, "我要退出了   添加设备的onresume 状态******  MainActivityNew.currentTab != 5");
            return;
        }
        CLog.i("jxx", "call 停止轮询服务类 method5");
        PollingService.stopService(getActivity());
        String _userName = PageUtil.getLoginUserInfo().mUserName;
        if (_userName == null || _userName.equals(bs.b)) {
            String name = PageUtil.getLoginUserInfo().mUID;
            if (LocalLoginInfoManager.getInstance().querySqlThirdCode(name)) {
                String _userName2 = LocalLoginInfoManager.getInstance().findByCardId(name).mThirdCode;
                if (_userName2.contains("@")) {
                    String[] targetQ = _userName2.split("@");
                    _userName = targetQ[0].substring(targetQ[0].length() - 4);
                } else {
                    _userName = name.substring(name.length() - 4);
                }
            } else {
                _userName = name.substring(name.length() - 4);
            }
        }
        this.mUserNameTextView.setText(_userName);
        if (!getActivity().getPackageManager().hasSystemFeature("android.hardware.bluetooth_le")) {
            this.phoneHardWareIsSupportBLE = false;
            this.mUsedBLEScan = false;
        } else {
            this.phoneHardWareIsSupportBLE = true;
            this.mUsedBLEScan = true;
        }
        if (Build.VERSION.SDK_INT >= 18) {
            this.phoneSystemVersionIsSupport = true;
        }
        if (MainActivityNew.currentTab == 0 || MainActivityNew.currentTab == 4) {
            this.mDeviceArray.clear();
            this.mSerchedAdapter.notifyDataSetChanged();
            CLog.e(TAG, "   添加设备的onresume 状态******  要清空列表了");
            this.mDevices_add_count.setText(new StringBuilder(String.valueOf(String.valueOf(getActivity().getString(R.string.serch_device_promt)) + this.mDeviceArray.size() + getString(R.string.serch_device_promt_howmuch))).toString());
        }
        FragmentActivity activity = getActivity();
        getActivity();
        ((InputMethodManager) activity.getSystemService("input_method")).hideSoftInputFromWindow(this.mView.getWindowToken(), 0);
        getSdcardPhoto(App_phms.getInstance().GetUserInfoNAME(), this.mHeadName);
        CLog.e(TAG, "添加设备的onresume 状态***********************");
        Constants.GO_TO_ADD_DEVICE = false;
        this.mautoSearchDevicesProgressBar.setVisibility(View.VISIBLE);
        register(this.mUsedBLEScan);
        languageAdapter();
    }

    private void languageAdapter() {
        this.mdevicelist_data_collection.setText(getActivity().getString(R.string.serch_device_title));
        this.mDevices_add_count.setText(new StringBuilder(String.valueOf(String.valueOf(getActivity().getString(R.string.serch_device_promt)) + this.mDeviceArray.size() + getString(R.string.serch_device_promt_howmuch))).toString());
        this.mReturnbtn.setText(getActivity().getString(R.string.back_btn_text));
        this.mRefreshdevices.setText(getActivity().getString(R.string.refresh_btn_text));
    }

    public void onStop() {
        super.onStop();
        if (this.ISREGISTBLUETOOTHREVIER) {
            CLog.e(TAG, "添加设备的onStop 状态***********************");
            Message msgs = new Message();
            msgs.what = 501;
            msgs.arg2 = 1;
            App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(msgs);
            if (this.mBluetoothAdapter.isDiscovering()) {
                this.mBluetoothAdapter.cancelDiscovery();
            }
            getActivity().unregisterReceiver(this.mReceiver);
            this.ISREGISTBLUETOOTHREVIER = false;
        }
        exitBle();
    }

    public void onPause() {
        super.onPause();
        if (this.ISREGISTBLUETOOTHREVIER) {
            CLog.e(TAG, "添加设备的onPause 状态***********************");
            Message msgs = new Message();
            msgs.what = 501;
            msgs.arg2 = 1;
            App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(msgs);
            if (this.mBluetoothAdapter.isDiscovering()) {
                this.mBluetoothAdapter.cancelDiscovery();
            }
            getActivity().unregisterReceiver(this.mReceiver);
            this.ISREGISTBLUETOOTHREVIER = false;
        }
        exitBle();
        this.onPause = true;
    }

    public void onDestroy() {
        super.onDestroy();
    }

    private void checkDevice(BluetoothDevice device, String mBluetoothType, byte[] scanRecord) {
        CLog.i("jxx", "搜到的设备：" + device.getAddress() + "\n" + "名字：" + device.getName() + "\n类型：" + mBluetoothType);
        if (Build.VERSION.SDK_INT >= 18) {
            CLog.i("jxx", "type:" + device.getType());
        }
        CLog.i("jxx", "-----------------");
        String _name = device.getName();
        if (_name != null) {
            String _code = bs.b;
            if (_name != null && _name.length() > 4) {
                _code = _name.substring(_name.length() - 4, _name.length());
            }
            String _deviceName = ContecDevices.checkDevice(_name);
            String _mac = device.getAddress();
            if (_deviceName != null && !_deviceName.equals(bs.b)) {
                boolean ifadd = true;
                int i = 0;
                while (true) {
                    if (i < this.mDeviceArray.size()) {
                        if (this.mDeviceArray.get(i).getmDeviceName().equals(_deviceName) && _mac.equals(this.mDeviceArray.get(i).getmMac())) {
                            ifadd = false;
                            break;
                        }
                        i++;
                    } else {
                        break;
                    }
                }
                ArrayList<DeviceBean> _Beans = DeviceListDaoOperation.getInstance().getDevice();
                int i2 = 0;
                while (true) {
                    if (i2 < _Beans.size()) {
                        if (_Beans.get(i2).mDeviceName.equals(_deviceName) && _mac.equals(_Beans.get(i2).mMacAddr)) {
                            ifadd = false;
                            break;
                        }
                        i2++;
                    } else {
                        break;
                    }
                }
                if (ifadd) {
                    matchDeviceName(_deviceName, _code, _mac, device, mBluetoothType, scanRecord);
                }
            }
        }
    }

    private void getSdcardPhoto(String puserflag, String picturename) {
        String pSdCardPath = Environment.getExternalStorageDirectory() + CookieSpec.PATH_DELIM;
        String _append = "contec/userinfo/" + puserflag + CookieSpec.PATH_DELIM;
        FileOperation.makeDirs(String.valueOf(pSdCardPath) + _append);
        try {
            if (new File(String.valueOf(pSdCardPath) + _append + picturename).exists()) {
                this.mDevicelist_change_user.setImageBitmap(BitmapFactory.decodeFile(String.valueOf(pSdCardPath) + _append + picturename));
                return;
            }
            this.mDevicelist_change_user.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.img_140915_twelve));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void cancleDialog() {
        new Thread() {
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                FragmentDataSerchDevice.mHandler.sendEmptyMessage(2);
            }
        }.start();
    }

    private void matchDeviceName(String pDeviceName, String pCode, String pMac, BluetoothDevice pDevice, String mBluetoothType, byte[] scanRecord) {
        CLog.d(TAG, "******************" + pDeviceName);
        if (Constants.DEVICE_8000GW_NAME.equalsIgnoreCase(pDeviceName)) {
            this.mDeviceArray.add(new SerchedDevice(R.drawable.drawable_data_device_ecg, String.valueOf(getString(R.string.str_8000G)) + "\n" + pCode, pCode, pMac, pDeviceName, pDevice, mBluetoothType, scanRecord));
        } else if (Constants.PM10_NAME.equalsIgnoreCase(pDeviceName)) {
            this.mDeviceArray.add(new SerchedDevice(R.drawable.drawable_device_pm10, String.valueOf(getString(R.string.device_productname_pm10)) + "\n" + pCode, pCode, pMac, pDeviceName, pDevice, mBluetoothType, scanRecord));
        } else if ("TEMP01".equalsIgnoreCase(pDeviceName)) {
            this.mDeviceArray.add(new SerchedDevice(R.drawable.drawable_device_ear_temperature, String.valueOf(getString(R.string.device_productname_hc06)) + "\n" + pCode, pCode, pMac, pDeviceName, pDevice, mBluetoothType, scanRecord));
        } else if ("BC01".equalsIgnoreCase(pDeviceName)) {
            this.mDeviceArray.add(new SerchedDevice(R.drawable.drawable_device_bc01, String.valueOf(getString(R.string.device_productname_bc01)) + "\n" + pCode, pCode, pMac, pDeviceName, pDevice, mBluetoothType, scanRecord));
        } else if ("CMS50D".equalsIgnoreCase(pDeviceName)) {
            this.mDeviceArray.add(new SerchedDevice(R.drawable.drawable_device_cms50d, "CMS50D-BT\n" + pCode, pCode, pMac, pDeviceName, pDevice, mBluetoothType, scanRecord));
        } else if (Constants.CMS50EW.equalsIgnoreCase(pDeviceName)) {
            this.mDeviceArray.add(new SerchedDevice(R.drawable.drawable_data_device_cms50ew, String.valueOf(getString(R.string.device_productname_50EW)) + "\n" + pCode, pCode, pMac, pDeviceName, pDevice, mBluetoothType, scanRecord));
        } else if ("CMS50IW".equalsIgnoreCase(pDeviceName)) {
            this.mDeviceArray.add(new SerchedDevice(R.drawable.cms50iw, String.valueOf(getString(R.string.device_productname_50IW)) + "\n" + pCode, pCode, pMac, pDeviceName, pDevice, mBluetoothType, scanRecord));
        } else if ("SP10W".equalsIgnoreCase(pDeviceName)) {
            this.mDeviceArray.add(new SerchedDevice(R.drawable.drawable_data_device_sp10w, String.valueOf(getString(R.string.device_productname_SP10W)) + "\n" + pCode, pCode, pMac, pDeviceName, pDevice, mBluetoothType, scanRecord));
        } else if ("CMSSXT".equalsIgnoreCase(pDeviceName)) {
            this.mDeviceArray.add(new SerchedDevice(R.drawable.drawable_data_device_cmxxst, String.valueOf(getString(R.string.device_productname_SXT)) + "\n" + pCode, pCode, pMac, pDeviceName, pDevice, mBluetoothType, scanRecord));
        } else if ("ABPM50W".equalsIgnoreCase(pDeviceName)) {
            this.mDeviceArray.add(new SerchedDevice(R.drawable.drawable_data_device_abpm50, String.valueOf(getString(R.string.device_productname_M50W)) + "\n" + pCode, pCode, pMac, pDeviceName, pDevice, mBluetoothType, scanRecord));
        } else if (DeviceNameUtils.PM50.equalsIgnoreCase(pDeviceName)) {
            this.mDeviceArray.add(new SerchedDevice(R.drawable.drawable_data_device_pm50, String.valueOf(getString(R.string.device_productname_pm50)) + "\n" + pCode, pCode, pMac, pDeviceName, pDevice, mBluetoothType, scanRecord));
        } else if (DeviceNameUtils.TEMP03.equalsIgnoreCase(pDeviceName)) {
            this.mDeviceArray.add(new SerchedDevice(R.drawable.drawable_data_device_temp03, String.valueOf(getString(R.string.device_productname_temp03)) + "\n" + pCode, pCode, pMac, pDeviceName, pDevice, mBluetoothType, scanRecord));
        } else if ("CONTEC08AW".equalsIgnoreCase(pDeviceName)) {
            this.mDeviceArray.add(new SerchedDevice(R.drawable.drawable_data_device_a08, String.valueOf(getString(R.string.device_productname_08AW)) + "\n" + pCode, pCode, pMac, pDeviceName, pDevice, mBluetoothType, scanRecord));
        } else if ("CONTEC08C".equalsIgnoreCase(pDeviceName)) {
            this.mDeviceArray.add(new SerchedDevice(R.drawable.drawable_device_c08, String.valueOf(getString(R.string.device_productname_08AW)) + "\n" + pCode, pCode, pMac, pDeviceName, pDevice, mBluetoothType, scanRecord));
        } else if ("CMS50F".equalsIgnoreCase(pDeviceName)) {
            this.mDeviceArray.add(new SerchedDevice(R.drawable.drawable_device_cms50f, String.valueOf(getString(R.string.device_name_50IW)) + "\n" + pCode, pCode, pMac, pDeviceName, pDevice, mBluetoothType, scanRecord));
        } else if ("WT".equalsIgnoreCase(pDeviceName)) {
            this.mDeviceArray.add(new SerchedDevice(R.drawable.drawable_data_device_wt, String.valueOf(getString(R.string.device_productname_WT)) + "\n" + pCode, pCode, pMac, pDeviceName, pDevice, mBluetoothType, scanRecord));
        } else if ("FHR01".equalsIgnoreCase(pDeviceName)) {
            this.mDeviceArray.add(new SerchedDevice(R.drawable.drawable_data_device_fhr01, String.valueOf(getString(R.string.device_productname_Fhr01)) + "\n" + pCode, pCode, pMac, pDeviceName, pDevice, mBluetoothType, scanRecord));
        } else if (Constants.PM85_NAME.equalsIgnoreCase(pDeviceName)) {
            this.mDeviceArray.add(new SerchedDevice(R.drawable.drawable_data_device_pm85, String.valueOf(getString(R.string.device_productname_Pm85)) + "\n" + pCode, pCode, pMac, pDeviceName, pDevice, mBluetoothType, scanRecord));
        } else if (Constants.CMS50K_NAME.equalsIgnoreCase(pDeviceName)) {
            if (!this.phoneHardWareIsSupportBLE || !this.phoneSystemVersionIsSupport) {
                mHandler.sendEmptyMessage(5);
            } else {
                this.mDeviceArray.add(new SerchedDevice(R.drawable.cms50k, String.valueOf(getString(R.string.device_productname_cms50k)) + "\n" + pCode, pCode, pMac, pDeviceName, pDevice, mBluetoothType, scanRecord));
            }
        } else if (Constants.CMS50K1_NAME.equalsIgnoreCase(pDeviceName)) {
            if (!this.phoneHardWareIsSupportBLE || !this.phoneSystemVersionIsSupport) {
                mHandler.sendEmptyMessage(5);
            } else {
                this.mDeviceArray.add(new SerchedDevice(R.drawable.cms50k1, String.valueOf(getString(R.string.device_productname_cms50k)) + "\n" + pCode, pCode, pMac, pDeviceName, pDevice, mBluetoothType, scanRecord));
            }
        }
        mHandler.sendEmptyMessage(3);
    }

    class DataSerchDeviceAdapter extends BaseAdapter {
        private /* synthetic */ int[] $SWITCH_TABLE$com$contec$phms$util$PageUtil$BroadcastPacket;
        Holder _Holder;
        private Context mContext;
        private ArrayList<SerchedDevice> mSerchedList;

        /* synthetic */ int[] $SWITCH_TABLE$com$contec$phms$util$PageUtil$BroadcastPacket() {
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

        public DataSerchDeviceAdapter(Context pContext, ArrayList<SerchedDevice> pSerchedList) {
            this.mContext = pContext;
            this.mSerchedList = pSerchedList;
        }

        public int getCount() {
            return this.mSerchedList.size();
        }

        public Object getItem(int arg0) {
            return Integer.valueOf(arg0);
        }

        public long getItemId(int arg0) {
            return (long) arg0;
        }

        private class Holder {
            Button mDeviceAdd;
            ImageView mdevice_image;
            TextView mdevice_name;

            private Holder() {
            }

            /* synthetic */ Holder(DataSerchDeviceAdapter dataSerchDeviceAdapter, Holder holder) {
                this();
            }
        }

        public View getView(final int position, View convertView, ViewGroup parent) {
            this._Holder = new Holder(this, (Holder) null);
            if (convertView == null) {
                convertView = LayoutInflater.from(this.mContext).inflate(R.layout.layout_data_serchdeviceitem, (ViewGroup) null);
                this._Holder.mdevice_image = (ImageView) convertView.findViewById(R.id.serch_device_image);
                this._Holder.mdevice_name = (TextView) convertView.findViewById(R.id.serch_device_name);
                this._Holder.mDeviceAdd = (Button) convertView.findViewById(R.id.device_sel_btn);
                convertView.setTag(this._Holder);
            } else {
                this._Holder = (Holder) convertView.getTag();
            }
            this._Holder.mdevice_image.setImageResource(this.mSerchedList.get(position).getDevice_image());
            this._Holder.mdevice_name.setText(this.mSerchedList.get(position).getDevice_name());
            this._Holder.mDeviceAdd.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    SerchedDevice _device = (SerchedDevice) DataSerchDeviceAdapter.this.mSerchedList.get(position);
                    DataSerchDeviceAdapter.this.saveDevice(FragmentDataSerchDevice.this.getActivity(), _device.getmDeviceName(), _device.getmMac(), _device.getmCode(), position, _device.getmBluetoothType(), _device.getScanRecord());
                }
            });
            return convertView;
        }

        private void saveDevice(Context pcontext, String pName, String pMac, String pCode, int pPosition, String pBluetoothType, byte[] scanRecord) {
            try {
                List<DeviceListItemBeanDao> _beanList = App_phms.getInstance().mHelper.getDeviceListItemDao().queryBuilder().where().eq(DeviceListItemBeanDao.DeviceName, pName).and().eq("UserName", App_phms.getInstance().mUserInfo.mUserID).and().eq(DeviceListItemBeanDao.DeviceCode, pCode).query();
                if (_beanList == null || _beanList.size() <= 0) {
                    DeviceListItemBeanDao _beanDao = new DeviceListItemBeanDao();
                    _beanDao.mDeviceName = pName;
                    _beanDao.mDeviceCode = pCode;
                    _beanDao.mUserName = App_phms.getInstance().mUserInfo.mUserID;
                    _beanDao.mDeviceMac = pMac;
                    _beanDao.mUseNum = 0;
                    _beanDao.isNew = true;
                    _beanDao.mBluetoothType = pBluetoothType;
                    String broadcastPacketFiled = bs.b;
                    switch ($SWITCH_TABLE$com$contec$phms$util$PageUtil$BroadcastPacket()[PageUtil.unPackData(scanRecord).ordinal()]) {
                        case 1:
                            broadcastPacketFiled = Constants.BroadcastPacketNoFiled;
                            break;
                        case 2:
                            broadcastPacketFiled = Constants.BroadcastPacketHasFiled;
                            break;
                        case 3:
                            broadcastPacketFiled = Constants.BroadcastPacketHasFiled;
                            break;
                    }
                    CLog.i("jxx", "pBluetoothType类型：" + pBluetoothType);
                    if (Constants.DEVICE_BLUEBOOTH_TYPE_BLE.equals(pBluetoothType)) {
                        CLog.i("jxx", "pBluetoothType类型--：" + pBluetoothType);
                        _beanDao.mBroadcastPacketFiled = Constants.BroadcastPacketHasFiled;
                    } else {
                        _beanDao.mBroadcastPacketFiled = broadcastPacketFiled;
                    }
                    CLog.i("jxx", "_beanDao.mBroadcastPacketFiled:" + _beanDao.mBroadcastPacketFiled);
                    if (_beanDao.isNew) {
                        _beanDao.isNew = false;
                        DeviceListDaoOperation.getInstance().insertDevice(_beanDao);
                        boolean _ifadd = true;
                        for (int i = 0; i < DeviceManager.mDeviceList.getListDevice().size(); i++) {
                            if (DeviceManager.mDeviceList.getDevice(i).mDeviceName.equals(_beanDao.mDeviceName)) {
                                DeviceBean _devicebean = new DeviceBean(_beanDao.mDeviceName, _beanDao.mDeviceMac, _beanDao.mDeviceCode, _beanDao.mId, bs.b);
                                _devicebean.ifAddNew = true;
                                _devicebean.mBluetoothType = pBluetoothType;
                                if (Constants.DEVICE_BLUEBOOTH_TYPE_BLE.equals(pBluetoothType)) {
                                    _devicebean.mBroadcastPacketFiled = Constants.BroadcastPacketHasFiled;
                                } else {
                                    _devicebean.mBroadcastPacketFiled = broadcastPacketFiled;
                                }
                                if (Constants.DEVICE_BLUEBOOTH_TYPE_BLE.equals(pBluetoothType)) {
                                    DeviceManager.mDeviceList.getDevice(i).mBeanList.add(0, _devicebean);
                                } else {
                                    DeviceManager.mDeviceList.getDevice(i).mBeanList.add(_devicebean);
                                }
                                _ifadd = false;
                            }
                        }
                        if (_ifadd) {
                            DeviceBeanList _DeviceBeanList = new DeviceBeanList();
                            _DeviceBeanList.mDeviceName = _beanDao.mDeviceName;
                            _DeviceBeanList.mState = 27;
                            DeviceBean _devicebean2 = new DeviceBean(_beanDao.mDeviceName, _beanDao.mDeviceMac, _beanDao.mDeviceCode, _beanDao.mId, bs.b);
                            _devicebean2.ifAddNew = true;
                            _devicebean2.mBluetoothType = pBluetoothType;
                            if (Constants.DEVICE_BLUEBOOTH_TYPE_BLE.equals(pBluetoothType)) {
                                _devicebean2.mBroadcastPacketFiled = Constants.BroadcastPacketHasFiled;
                            } else {
                                _devicebean2.mBroadcastPacketFiled = broadcastPacketFiled;
                            }
                            _DeviceBeanList.mBeanList.add(_devicebean2);
                            if (Constants.DEVICE_BLUEBOOTH_TYPE_BLE.equals(pBluetoothType)) {
                                DeviceManager.mDeviceList.getListDevice().add(0, _DeviceBeanList);
                            } else {
                                DeviceManager.mDeviceList.getListDevice().add(_DeviceBeanList);
                            }
                            App_phms.getInstance().showBeans.add(_devicebean2);
                        }
                        DeviceBean devicebean = new DeviceBean(_beanDao.mDeviceName, _beanDao.mDeviceMac, _beanDao.mDeviceCode, _beanDao.mId, bs.b);
                        devicebean.setmBluetoothType(pBluetoothType);
                        devicebean.setmBroadcastPacketFiled(broadcastPacketFiled);
                        App_phms.getInstance().getmWaitConnectDeviceBeanLists().add(new WaitConnectDeviceBean(devicebean, 0, true));
                        Constants.ISFROMSERACHDEVICE = true;
                        Message msg = new Message();
                        msg.what = 1;
                        msg.arg1 = pPosition;
                        FragmentDataSerchDevice.mHandler.sendMessage(msg);
                        return;
                    }
                    return;
                }
                FragmentDataSerchDevice.mHandler.sendEmptyMessage(2);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void scanLeDevice(boolean enable) {
        if (this.mBleScan == null) {
            this.mBleScan = new BluetoothLEScan();
        }
        if (enable) {
            this.mScanning = true;
            this.mBluetoothAdapter.startLeScan(this.mBleScan.mLeScanCallback);
            mHandler.sendEmptyMessageDelayed(4, 8000);
            return;
        }
        this.mScanning = false;
        this.mBluetoothAdapter.stopLeScan(this.mBleScan.mLeScanCallback);
    }

    class BluetoothLEScan {
        public BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {
            public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
                String mBluetoothType;
                if (scanRecord[5] == 18 && scanRecord[6] == -1) {
                    mBluetoothType = Constants.DEVICE_BLUEBOOTH_TYPE_BLE;
                    FragmentDataSerchDevice.this.checkDevice(device, mBluetoothType, scanRecord);
                } else {
                    mBluetoothType = Constants.DEVICE_BLUEBOOTH_TYPE_CLASSIC;
                    FragmentDataSerchDevice.this.checkDevice(device, mBluetoothType, scanRecord);
                }
                CLog.i(FragmentDataSerchDevice.TAG, "蓝牙类型：" + mBluetoothType);
                String unused = FragmentDataSerchDevice.this.unPackData(device, scanRecord);
            }
        };

        BluetoothLEScan() {
        }
    }

    private String unPackData(BluetoothDevice device, byte[] scanRecord) {
        StringBuilder builder2 = new StringBuilder();
        StringBuilder builder3 = new StringBuilder();
        for (int i = 0; i < scanRecord.length; i++) {
            builder2.append(PageUtil.toASCLL(Integer.toHexString(scanRecord[i])));
            builder3.append(String.valueOf(Integer.toHexString(scanRecord[i])) + " ");
        }
        CLog.i(TAG, "设备名字：" + device.getName());
        CLog.i(TAG, "原数据:" + builder3.toString());
        CLog.i(TAG, "处理后的数据:" + builder2.toString() + "\n-----");
        if (scanRecord.length <= 13) {
            return bs.b;
        }
        StringBuilder builder = new StringBuilder();
        String temp1 = PageUtil.toASCLL(Integer.toHexString(scanRecord[9]));
        String temp2 = PageUtil.toASCLL(Integer.toHexString(scanRecord[10]));
        String temp3 = PageUtil.toASCLL(Integer.toHexString(scanRecord[11]));
        return builder.append(String.valueOf(temp1) + temp2 + temp3 + PageUtil.toASCLL(Integer.toHexString(scanRecord[12]))).toString();
    }

    private void exitBle() {
        if (this.mScanning) {
            scanLeDevice(false);
        }
    }

    public void LogI(String msg) {
        if (Constants.mTestFlag) {
            CLog.i("jxx", msg);
        }
    }
}
