package com.contec.phms.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Vibrator;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.alibaba.fastjson.JSON;
import com.contec.circleimage.widget.CircularImage;
import com.contec.phms.App_phms;
import com.contec.phms.R;
import com.contec.phms.SearchDevice.SearchDevice;
import com.contec.phms.SearchDevice.ContecDevices;
import com.contec.phms.Server_Main;
import com.contec.phms.activity.ActivityChooseHead;
import com.contec.phms.activity.ActivityLoginAnotherPlace;
import com.contec.phms.activity.CustomNotification;
import com.contec.phms.activity.MultiDirectionSlidingDrawer;
import com.contec.phms.activity.PedometerSetActivity;
import com.contec.phms.device.pedometer.PedometerService;
import com.contec.phms.device.template.BluetoothServerService;
import com.contec.phms.device.template.DataFilter;
import com.contec.phms.device.template.DeviceService;
import com.contec.phms.device.template.PollingService;
import com.contec.phms.domain.BluetoothPhoneNotes;
import com.contec.phms.domain.WaitConnectDeviceBean;
import com.contec.phms.eventbus.EventFragment;
import com.contec.phms.login.LoginActivity;
import com.contec.phms.login.LoginMethod;
import com.contec.phms.manager.device.DeviceBean;
import com.contec.phms.manager.device.DeviceBeanList;
import com.contec.phms.manager.device.DeviceManager;
import com.contec.phms.manager.message.InstantMessageService;
import com.contec.phms.manager.message.MessageManager;
import com.contec.phms.manager.message.MessageManagerMain;
import com.contec.phms.db.DatabaseHelper;
import com.contec.phms.db.DeviceListDaoOperation;
import com.contec.phms.db.DeviceListItemBeanDao;
import com.contec.phms.db.LocalLoginInfoManager;
import com.contec.phms.db.LoginUserDao;
import com.contec.phms.db.PedometerHistoryDao;
import com.contec.phms.db.PedometerSumStepKm;
import com.contec.phms.db.UserInfoDao;
import com.contec.phms.upload.UploadService;
import com.contec.phms.upload.trend.Pedometer_Trend;
import com.contec.phms.util.AlertDialogUtil;
import com.contec.phms.util.CLog;
import com.contec.phms.util.Constants;
import com.contec.phms.util.DeviceNameUtils;
import com.contec.phms.util.FileOperation;
import com.contec.phms.util.PageUtil;
import com.contec.phms.util.PedometerSharepreferance;
import com.contec.phms.util.PhmsSharedPreferences;
import com.contec.phms.util.ScreenAdapter;
import com.contec.phms.util.TimerUtil;
import com.contec.phms.util.UpdateManeger;
import com.contec.phms.widget.BallProgressView;
import com.contec.phms.widget.DialogClass;
import com.contec.phms.widget.UpdateMainLanguageInterface;
import com.contec.phms.widget.XListView;
import com.j256.ormlite.dao.Dao;
//import com.sun.mail.iap.Response;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import org.apache.commons.httpclient.cookie.CookieSpec;
import u.aly.bs;

@SuppressLint({"NewApi", "HandlerLeak"})
public class Fragmentdevicelist extends Fragment implements Runnable, View.OnClickListener, XListView.IXListViewListener {
    private static /* synthetic */ int[] $SWITCH_TABLE$com$contec$phms$util$PageUtil$BroadcastPacket = null;
    private static final int BLE_SEARCH_END = 50002;
    private static final int HANDLER_UPDATE_SPEED = 1;
    private static final int HANDLER_UPDATE_TIME = 0;
    public static boolean ISDRAWERISCLOSE = false;
    private static final int PHONE_NO_OPEN_BLE = 50005;
    private static final int PHONE_NO_SUPPORT_BLE = 50004;
    public static final int REQUEST_THEME = 3;
    private static final int SEARCH_DEVICE = 50001;
    private static final int SEARCH_END_NO_DEVICE = 50003;
    private static final String TAG = Fragmentdevicelist.class.getSimpleName();
    public static boolean is_destory = false;
    public static boolean mAdd = true;
    public static Fragmentdevicelist mFragmentdevicelist;
    public static SharedPreferences mSharedPreferences;
    public static boolean m_open_blue = false;
    public static boolean m_open_service = false;
    public static int m_time = 10;
    public static final int pop_user_men = 0;
    final int CALLBACK_OPEN = Constants.CHECK_TIME_ILLEGAL;
    private boolean ISREGISTBLUETOOTHREVIER = false;
    boolean _askUpgrade;
    private float _calSum;
    private CircularImage _devicelist_change_user;
    private int _distance;
    private HashMap<String, String> _getreturnparams;
    Dao<UserInfoDao, String> _userdao = null;
    private ProgressBar bar;
    private String bluetoothAddress;
    private View.OnClickListener cancleListener = new View.OnClickListener() {
        public void onClick(View v) {
            Intent i = new Intent(Fragmentdevicelist.this.getActivity(), LoginActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            Fragmentdevicelist.this.getActivity().startActivity(i);
        }
    };
    private long connectTime = 0;
    private long connectTimeClassic = 0;
    private TextView devicelist_data_collection;
    private boolean ifFindNewDevice = false;
    private boolean isRegisterReceiver = false;
    private boolean isVisibleHistoryBtn = false;
    private float mAverageSpeed;
    private boolean mBackground = true;
    private BallProgressView mBallProgressView;
    private BluetoothLEScan mBleScan;
    BluetoothAdapter mBluetoothAdapter;
    private BroadcastReceiver mBluetoothReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action != null && action.equalsIgnoreCase("android.bluetooth.adapter.action.STATE_CHANGED")) {
                if (BluetoothAdapter.getDefaultAdapter().getState() == BluetoothAdapter.STATE_TURNING_ON) {
                    Fragmentdevicelist.this.turningonBluetooth = System.currentTimeMillis();
                } else if (BluetoothAdapter.getDefaultAdapter().getState() == BluetoothAdapter.STATE_ON && Fragmentdevicelist.this.turningonBluetooth != 0) {
                    Fragmentdevicelist.this.openBluetooth = System.currentTimeMillis() - Fragmentdevicelist.this.turningonBluetooth;
                    Fragmentdevicelist.this.uploadBluetooth("APP_BT_STATE_BD_START", Fragmentdevicelist.this.getNotesString("BLUETOOTH", bs.b, "APP_BT_STATE_BD_START", new StringBuilder(String.valueOf(Fragmentdevicelist.this.openBluetooth)).toString()));
                }
            }
        }
    };
    private boolean mBluetoothRegister = false;
    private float mCalories = 0.0f;
    DialogInterface.OnKeyListener mCancelLogin = new DialogInterface.OnKeyListener() {
        public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
            if (keyCode != 4 || event.getRepeatCount() == 0) {
            }
            return false;
        }
    };
    private boolean mCheckDevice = false;
    private boolean mClassicSacn = false;
    private ServiceConnection mConnection = new ServiceConnection() {
        public void onServiceDisconnected(ComponentName name) {
            if (!Fragmentdevicelist.this.mBackground) {
                Fragmentdevicelist.this.mService = null;
            }
        }

        public void onServiceConnected(ComponentName name, IBinder service) {
            Fragmentdevicelist.this.mService = ((PedometerService.StepBinder) service).getService();
            Fragmentdevicelist.this.mService.addCallBack(Fragmentdevicelist.this.mPedomCallBack);
            Fragmentdevicelist.this.mHandler.sendEmptyMessage(102);
            if (Fragmentdevicelist.this.mPauseFlag) {
                Fragmentdevicelist.this.mService.setData(Fragmentdevicelist.this.mStepsInt, Fragmentdevicelist.this.mTimeInt, Fragmentdevicelist.this.mDistance, Fragmentdevicelist.this.mCalories, Fragmentdevicelist.this.mAverageSpeed);
                Fragmentdevicelist.this.mPauseFlag = false;
            } else if (!Fragmentdevicelist.this.mBackground) {
                Fragmentdevicelist.this.mService.reSetData();
            } else {
                Fragmentdevicelist.this.mService.refresh();
            }
            Fragmentdevicelist.this.mService.reSetShare();
            Fragmentdevicelist.this.mUnit = Fragmentdevicelist.this.mService.getUnit();
            if (Fragmentdevicelist.this.mUnit == 1) {
                float _temp = ((float) Fragmentdevicelist.this.mDistanceSumTodayInt) + Fragmentdevicelist.this.mDistance;
                if (_temp >= 1000.0f) {
                    Fragmentdevicelist.this.mpebometer_handler_Distance_Text.setText(String.valueOf(Fragmentdevicelist.this.floatFormat("#0.00", _temp * 0.001f)) + Fragmentdevicelist.this.getString(R.string.pebometer_kilometer));
                } else {
                    Fragmentdevicelist.this.mpebometer_handler_Distance_Text.setText(String.valueOf(Fragmentdevicelist.this.floatFormat("#0", _temp)) + Fragmentdevicelist.this.getString(R.string.pebometer_meter));
                }
            } else {
                float _temp2 = (((float) Fragmentdevicelist.this.mDistanceSumTodayInt) * 3.2808f) + Fragmentdevicelist.this.mDistance;
                if (_temp2 >= 1000.0f) {
                    Fragmentdevicelist.this.mpebometer_handler_Distance_Text.setText(String.valueOf(Fragmentdevicelist.this.floatFormat("#0.00", _temp2 * 1.8939393E-4f)) + Fragmentdevicelist.this.getString(R.string.pedometer_distance_kin));
                } else {
                    Fragmentdevicelist.this.mpebometer_handler_Distance_Text.setText(String.valueOf(Fragmentdevicelist.this.floatFormat("#0", _temp2)) + Fragmentdevicelist.this.getString(R.string.pedometer_speed_in));
                }
            }
            CLog.e(Fragmentdevicelist.TAG, "mService.getUnit()= " + Fragmentdevicelist.this.mUnit + "  mService.mDistanceSumTodayInt= " + Fragmentdevicelist.this.mDistanceSumTodayInt);
        }
    };
    private Dao<PedometerHistoryDao, String> mDao;
    private DatabaseHelper mDatabaseHelper;
    private String mDateStr;
    private String mDateStrSum;
    private float mDistance = 0.0f;
    private int mDistanceSumTodayInt = 0;
    private Handler mHandler;
    private List<PedometerHistoryDao> mList;
    private boolean mNormal = true;
    private boolean mPauseFlag = false;
    private PedometerService.CallBack mPedomCallBack;
    private List<PedometerHistoryDao> mPedometerHistoryDaoList;
    private PedometerSharepreferance mPedometerSharepreferance;
    private PhmsSharedPreferences mPhmsSharedPreferences;
    private PopupWindow mPop;
    private ProgressDialog mProgressDialog;
    public BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            if ("android.bluetooth.adapter.action.STATE_CHANGED".equals(intent.getAction()) && Fragmentdevicelist.this.mBluetoothAdapter.getState() == BluetoothAdapter.STATE_ON) {
                Fragmentdevicelist.m_open_blue = true;
                if (DeviceManager.mDeviceList != null && DeviceManager.mDeviceList.size() > 0) {
                    Constants.BLUETOOTHSTAT = 1;
                    Fragmentdevicelist.this.mcontext.startService(new Intent(Fragmentdevicelist.this.mcontext, Server_Main.class));
                    Fragmentdevicelist.m_open_service = true;
                }
                Fragmentdevicelist.this.mcontext.unregisterReceiver(this);
            }
        }
    };
    public MultiDirectionSlidingDrawer mRunDrawer;
    public final BroadcastReceiver mScanReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if ("android.bluetooth.device.action.FOUND".equals(action)) {
                BluetoothDevice device = (BluetoothDevice) intent.getParcelableExtra("android.bluetooth.device.extra.DEVICE");
                CLog.i(Fragmentdevicelist.TAG, "BroadcastReceiver: " + action + "  device name:" + device.getName() + "  mac:" + device.getAddress());
                if (!Fragmentdevicelist.this.mCheckDevice) {
                    Fragmentdevicelist.this.mCheckDevice = Fragmentdevicelist.this.checkDevice(device, Constants.DEVICE_BLUEBOOTH_TYPE_CLASSIC, false, new byte[0]);
                }
            } else if ("android.bluetooth.adapter.action.DISCOVERY_FINISHED".equals(action)) {
                Fragmentdevicelist.this.mClassicSacn = false;
                Fragmentdevicelist.this.ui_DeviceManage(Fragmentdevicelist.this.mdevicelist_add_devices, true);
            } else if ("android.bluetooth.adapter.action.STATE_CHANGED".equals(action) && Fragmentdevicelist.this.mBluetoothAdapter.getState() == BluetoothAdapter.STATE_ON) {
                Fragmentdevicelist.this.mBluetoothAdapter.startDiscovery();
                Fragmentdevicelist.this.mClassicSacn = true;
            }
        }
    };
    private boolean mScanning = false;
    private boolean mScreenOff = false;
    private BroadcastReceiver mScreenReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("android.intent.action.SCREEN_OFF")) {
                CLog.e(Fragmentdevicelist.TAG, "the activity is off!");
                Fragmentdevicelist.this.mScreenOff = true;
            }
        }
    };
    private PedometerService mService;
    private boolean mServiceConnect = false;
    private boolean mServiceIsBind = false;
    private TextView mSpeedsText;
    private int mStepTarget = 0;
    private int mSteps = 0;
    private int mStepsInt = 0;
    private int mStepsSumInt = 0;
    private int mSumSteps = 0;
    private TextView mTextPull;
    private int mTimeInt;
    private int mTotalSeconds = 0;
    private int mUnit = 1;
    private int mUnitLast = 1;
    private UpdateManeger mUpdateManeger;
    private UpdateMainLanguageInterface mUpdateTabTextInterface;
    private boolean mUsedBLEScan = false;
    private UserAdapter mUserAdapter;
    private String mUserID;
    private String mUserName;
    private TextView mUserNameText;
    private FrameLayout mView;
    public BluetoothListAdapter m_adapter;
    private XListView m_newDevicesListView_phone;
    DialogInterface.OnKeyListener mcommitpedemeterdatalistener = new DialogInterface.OnKeyListener() {
        public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
            if (keyCode != 4 || event.getRepeatCount() == 0) {
            }
            return false;
        }
    };
    private DialogClass mcommitpedometerdatadialog;
    private Context mcontext;
    private Button mdevicelist_add_devices;
    private TextView mexit_to_BT_status_te;
    private RelativeLayout mfirst_search_new_devices_layout;
    private boolean misreLogin = false;
    private boolean misregistscreenreceiver = false;
    private boolean misunbindservice = false;
    private final int mloginargserror = Constants.mloginargserror;
    private final int mlogindberror = Constants.mlogindberror;
    private DialogClass mlogindialogClass;
    private final int mloginduserisnotexit = Constants.mloginduserisnotexit;
    private final int mlogindusernotcorrectpsw = Constants.mlogindusernotcorrectpsw;
    private final int mloginduserstopuser = Constants.mloginduserstopuser;
    private String mloginuserid;
    private String mloginuserpwd;
    private TextView mpebometer_Distance_Text;
    private TextView mpebometer_Speed_Text;
    private TextView mpebometer_TimeHaveGo;
    private TextView mpebometer_handler_Distance_Text;
    private TextView mpebometer_handler_StepsTextSmall;
    private TextView mpebometer_handler_calorie_Text;
    private TextView mpebometer_handler_steptarget_aim;
    private Button mpebometer_puse_btn;
    private Button mpebometer_stop_btn;
    private ProgressBar mpedometer_handler_stepaverage_progesss;
    private TextView mpop_bot_add;
    private ListView mpop_center_list;
    private final int mqueryuserinfoneterror = Constants.mqueryuserinfoneterror;
    private final int mqueryuserinfotimeout = Constants.mqueryuserinfotimeout;
    private ImageButton msearch_new_devices_bt;
    private RelativeLayout muser_layout;
    private boolean onPause = false;
    private long openBluetooth = 0;
    private long openTime = 0;
    private Button pedo_set;
    private boolean phoneHardWareIsSupportBLE = false;
    private boolean phoneSystemVersionIsSupport = false;
    private List<UserInfoDao> popUserArray;
    private SharedPreferences preferences;
    DialogClass searchDialog;
    private TextView size;
    private long startTime = 0;
    private long startTimeClassic = 0;
    private View.OnClickListener sureListener = new View.OnClickListener() {
        public void onClick(View v) {
            Fragmentdevicelist.this.reloginAnother();
        }
    };
    private long turningonBluetooth = 0;
    private Vibrator vibrator;

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

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        App_phms.getInstance().mEventBus.register(this);
        this.mPedometerSharepreferance = new PedometerSharepreferance(getActivity());
        this.mcontext = getActivity();
        initHandler();
        ifRelogin();
        init();
        initList();
        init_runlayout_view();
        mFragmentdevicelist = this;
        this.m_newDevicesListView_phone.requestFocus();
        this.m_newDevicesListView_phone.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                CLog.e(Fragmentdevicelist.TAG, "listview 是否获取焦点：" + hasFocus);
                if (hasFocus) {
                    Message msg = new Message();
                    msg.what = 502;
                    msg.arg2 = 1;
                    App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(msg);
                }
            }
        });
        initrundate();
        TimerUtil.getinstance().startTimer();
        return this.mView;
    }

    private void ifRelogin() {
        Intent _intent = getActivity().getIntent();
        if (_intent != null && this.mPedometerSharepreferance.getUserID() != null && !this.mPedometerSharepreferance.getUserID().equals(bs.b) && this.mPedometerSharepreferance.getExit()) {
            this.misreLogin = _intent.getBooleanExtra(Constants.KEY_PEDOMETOR, false);
        }
        if (this.misreLogin) {
            relogin();
        }
    }

    void initList() {
        if (DeviceManager.mDeviceBeanList != null) {
            if (DeviceManager.mDeviceBeanList.mBeanList != null) {
                DeviceManager.mDeviceBeanList.mBeanList.clear();
            }
            DeviceManager.mWhichDevice = 0;
            DeviceManager.mWhichItem = 0;
            DeviceManager.mWhichSortIndex = 0;
        }
        if (DeviceManager.mDeviceList != null) {
            if (DeviceManager.mDeviceList.getListDevice() != null) {
                for (int i = 0; i < DeviceManager.mDeviceList.getListDevice().size(); i++) {
                    DeviceManager.mDeviceList.getListDevice().get(i).mBeanList.clear();
                }
                DeviceManager.mDeviceList.getListDevice().clear();
            }
            DeviceManager.mDeviceList.removeAll();
            DeviceManager.mDeviceList = null;
        }
        DeviceManager.mDeviceList = DeviceListDaoOperation.getInstance().getDeviceListItem();
        mAdd = true;
        this.m_adapter.setmNewlistBean(DeviceManager.mDeviceList.getListDevice());
        this.m_adapter.notifyDataSetChanged();
        if (DeviceManager.mDeviceList == null || DeviceManager.mDeviceList.size() <= 0) {
            this.msearch_new_devices_bt.setVisibility(View.VISIBLE);
            this.mfirst_search_new_devices_layout.setVisibility(View.VISIBLE);
            this.m_newDevicesListView_phone.setVisibility(View.GONE);
            this.mTextPull.setVisibility(View.GONE);
            return;
        }
        this.msearch_new_devices_bt.setVisibility(View.GONE);
        this.mfirst_search_new_devices_layout.setVisibility(View.GONE);
        this.m_newDevicesListView_phone.setVisibility(View.VISIBLE);
        this.mTextPull.setVisibility(View.VISIBLE);
    }

    public void init() {
        this.mDatabaseHelper = App_phms.getInstance().mHelper;
        this.mPhmsSharedPreferences = PhmsSharedPreferences.getInstance(getActivity());
        if (Constants.Language.equalsIgnoreCase("zh")) {
            Configuration config = getResources().getConfiguration();
            DisplayMetrics dm = getResources().getDisplayMetrics();
            config.locale = Locale.CHINESE;
            getResources().updateConfiguration(config, dm);
        } else if (Constants.Language.equalsIgnoreCase("en")) {
            Configuration config2 = getResources().getConfiguration();
            DisplayMetrics dm2 = getResources().getDisplayMetrics();
            config2.locale = Locale.ENGLISH;
            getResources().updateConfiguration(config2, dm2);
        }
        if (Constants.IS_PAD_NEW) {
            this.mView = (FrameLayout) LayoutInflater.from(getActivity()).inflate(R.layout.layout_main_phone_new, (ViewGroup) null);
        } else {
            this.mView = (FrameLayout) LayoutInflater.from(getActivity()).inflate(R.layout.layout_main_phone_new, (ViewGroup) null);
        }
        this.mcontext = getActivity();
        App_phms.mCurrentActivity = 2;
        mFragmentdevicelist = this;
        is_destory = false;
        this.mProgressDialog = new ProgressDialog(this.mcontext);
        this.mProgressDialog.setCancelable(true);
        mSharedPreferences = this.mcontext.getSharedPreferences("BluetoothList", 0);
        this.devicelist_data_collection = (TextView) this.mView.findViewById(R.id.devicelist_data_collection);
        this.mTextPull = (TextView) this.mView.findViewById(R.id.tv_pull);
        this.mTextPull.setVisibility(View.GONE);
        Button _pebometer_puse_btn = (Button) this.mView.findViewById(R.id.pebometer_puse_btn);
        if (_pebometer_puse_btn != null) {
            CLog.i("lz", "_pebometer_puse_btn is null.");
            _pebometer_puse_btn.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                }
            });
        } else {
            CLog.i("lz", "_pebometer_puse_btn is not null.");
        }
        FrameLayout mlayout_Fragmentdevicelist_main = (FrameLayout) this.mView.findViewById(R.id.layout_Fragmentdevicelist_main);
        this.mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        this.mRunDrawer = (MultiDirectionSlidingDrawer) this.mView.findViewById(R.id.rundrawer);
        this.mRunDrawer.setOnDrawerOpenListener(new MultiDirectionSlidingDrawer.OnDrawerOpenListener() {
            public void onDrawerOpened() {
                Constants.REPORT = false;
                Constants.START_USER_INFO_PAGE = false;
                Fragmentdevicelist.this.drawhandlerlayoutchangewhenopendraw();
            }
        });
        this.mRunDrawer.setOnDrawerCloseListener(new MultiDirectionSlidingDrawer.OnDrawerCloseListener() {
            public void onDrawerClosed() {
                Constants.REPORT = false;
                Constants.START_USER_INFO_PAGE = false;
                Fragmentdevicelist.this.OnCloseDraw();
            }
        });
        this.msearch_new_devices_bt = (ImageButton) this.mView.findViewById(R.id.search_new_devices_bt);
        this.msearch_new_devices_bt.getLayoutParams().height = Constants.M_SCREENWEIGH / 2;
        this.msearch_new_devices_bt.getLayoutParams().width = Constants.M_SCREENWEIGH / 2;
        this.msearch_new_devices_bt.setOnClickListener(this);
        if (Constants.IS_PAD_NEW) {
            this.m_newDevicesListView_phone = (XListView) this.mView.findViewById(R.id.list_view_device);
            if (this.m_adapter == null) {
                this.m_adapter = new BluetoothListAdapter(this.mcontext);
            }
            this.m_newDevicesListView_phone.setAdapter(this.m_adapter);
        } else {
            this.m_newDevicesListView_phone = (XListView) this.mView.findViewById(R.id.list_view_device);
            if (this.m_adapter == null) {
                this.m_adapter = new BluetoothListAdapter(this.mcontext);
            }
            this.m_newDevicesListView_phone.setAdapter(this.m_adapter);
        }
        PageUtil.checkUserinfo(App_phms.getInstance().mUserInfo, getActivity());
        m_time = App_phms.getInstance().mUserInfo.mSearchInterval;
        this.mfirst_search_new_devices_layout = (RelativeLayout) this.mView.findViewById(R.id.first_search_new_devices_layout);
        this.muser_layout = (RelativeLayout) this.mView.findViewById(R.id.userlayout);
        this.muser_layout.setOnClickListener(this);
        this.mdevicelist_add_devices = (Button) this.mView.findViewById(R.id.devicelist_add_devices);
        this.mdevicelist_add_devices.setOnClickListener(this);
        if (Constants.IS_PAD_NEW) {
            ScreenAdapter.changeLayoutTextSize(getActivity(), mlayout_Fragmentdevicelist_main, 10);
        }
        this.pedo_set = (Button) this.mView.findViewById(R.id.pedo_set);
        this.pedo_set.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Fragmentdevicelist.this.startActivity(new Intent(Fragmentdevicelist.this.getActivity(), PedometerSetActivity.class));
            }
        });
        this.mUserNameText = (TextView) this.mView.findViewById(R.id.user_name_text);
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
        this.mUserNameText.setText(_userName);
        this._devicelist_change_user = (CircularImage) this.mView.findViewById(R.id.devicelist_change_user);
        getSdcardPhoto(App_phms.getInstance().GetUserInfoNAME(), "imagehead.png");
        this.mexit_to_BT_status_te = (TextView) this.mView.findViewById(R.id.exit_to_BT_status_te);
        this.m_newDevicesListView_phone.setXListViewListener(this);
        this.m_newDevicesListView_phone.setPullLoadEnable(false);
    }

    private void initrundate() {
        initCallBack();
        registerScreenReceiver();
    }

    private void drawhandlerlayoutchangewhenclosedraw() {
        ((LinearLayout) getActivity().findViewById(R.id.mainbottomitemlayout)).setVisibility(View.VISIBLE);
        ((LinearLayout) this.mView.findViewById(R.id.step_progressbar_layout)).setVisibility(View.VISIBLE);
        LinearLayout mImageHandleLayout = (LinearLayout) this.mView.findViewById(R.id.image_handle_layout);
        mImageHandleLayout.getLayoutParams().height = ScreenAdapter.dip2px(this.mcontext, 35.0f);
        mImageHandleLayout.setBackgroundResource(R.drawable.drawable_handle_bg);
        ((ImageView) this.mView.findViewById(R.id.img_handler_drop_btn)).setImageResource(R.drawable.drawable_drop_down_btn);
        ((RelativeLayout) this.mView.findViewById(R.id.linearlayout_title)).setVisibility(View.VISIBLE);
        ((RelativeLayout) this.mView.findViewById(R.id.drawer_layout)).setVisibility(View.GONE);
        ((LinearLayout) this.mView.findViewById(R.id.handle)).getLayoutParams().height = ScreenAdapter.dip2px(this.mcontext, 110.0f);
        LinearLayout mHandleTopLayout = (LinearLayout) this.mView.findViewById(R.id.handle_top_layout);
        mHandleTopLayout.getLayoutParams().height = ScreenAdapter.dip2px(this.mcontext, 75.0f);
        mHandleTopLayout.setVisibility(View.VISIBLE);
        ISDRAWERISCLOSE = false;
        this.mdevicelist_add_devices.setEnabled(true);
        this.msearch_new_devices_bt.setEnabled(true);
    }

    private void drawhandlerlayoutchangewhenopendraw() {
        CustomNotification.getInstance(this.mcontext).clearNotification();
        onResumeinit();
        ((LinearLayout) getActivity().findViewById(R.id.mainbottomitemlayout)).setVisibility(View.GONE);
        ((LinearLayout) this.mView.findViewById(R.id.step_progressbar_layout)).setVisibility(View.GONE);
        LinearLayout mImageHandleLayout = (LinearLayout) this.mView.findViewById(R.id.image_handle_layout);
        mImageHandleLayout.setBackgroundResource(R.drawable.drawable_handle_bg);
        mImageHandleLayout.getLayoutParams().height = ScreenAdapter.dip2px(this.mcontext, 25.0f);
        mImageHandleLayout.setBackgroundResource(R.drawable.img_handle_half_bg);
        ((ImageView) this.mView.findViewById(R.id.img_handler_drop_btn)).setImageResource(R.drawable.img_drop_up_btn);
        ((RelativeLayout) this.mView.findViewById(R.id.linearlayout_title)).setVisibility(View.GONE);
        ((RelativeLayout) this.mView.findViewById(R.id.drawer_layout)).setVisibility(View.VISIBLE);
        ((LinearLayout) this.mView.findViewById(R.id.handle)).getLayoutParams().height = ScreenAdapter.dip2px(this.mcontext, 25.0f);
        LinearLayout mHandleTopLayout = (LinearLayout) this.mView.findViewById(R.id.handle_top_layout);
        mHandleTopLayout.getLayoutParams().height = 0;
        mHandleTopLayout.setVisibility(View.INVISIBLE);
        ISDRAWERISCLOSE = true;
    }

    private void OnCloseDraw() {
        drawhandlerlayoutchangewhenclosedraw();
        new Thread() {
            public void run() {
                try {
                    sleep(800);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (Fragmentdevicelist.this.mService != null) {
                    Fragmentdevicelist.this.whencolsedrawrunlayoutchange();
                }
            }
        }.start();
    }

    private void whencolsedrawrunlayoutchange() {
        Message msg = new Message();
        msg.what = Constants.UPDATA_NOTIFITION_SUM_STEPS;
        msg.arg2 = 10;
        msg.arg1 = this.mStepsInt;
        if (!this.isVisibleHistoryBtn) {
            msg.obj = App_phms.getInstance().mUserInfo;
        } else {
            msg.obj = null;
        }
        App_phms.getInstance().mEventBus.post(msg);
        if (!this.isVisibleHistoryBtn) {
            EventFragment _fragment = new EventFragment();
            _fragment.setmWhichCommand(6);
            App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(_fragment);
            CLog.e("PedometerService", " 通知修改主界面的总部数");
        }
        this.mPedometerSharepreferance.setBackPedometer(true);
    }

    private void checkBlueTooth() {
        CLog.e(TAG, "+++++++++++++++再次搜索设备+++++++++++");
        if (!this.mBluetoothAdapter.isEnabled()) {
            this.mcontext.registerReceiver(this.mReceiver, new IntentFilter("android.bluetooth.adapter.action.STATE_CHANGED"));
            this.mBluetoothAdapter.enable();
        } else if (DeviceManager.mDeviceList != null && DeviceManager.mDeviceList.size() > 0) {
            Constants.BLUETOOTHSTAT = 1;
            m_open_service = true;
        }
    }

    private void setPedometerDataInfo() {
        PageUtil.checkUserinfo(App_phms.getInstance().mUserInfo, getActivity());
        String mUserName2 = App_phms.getInstance().mUserInfo.mUserID;
        try {
            List<PedometerHistoryDao> _list = App_phms.getInstance().mHelper.getPedometerhistoryDao().queryBuilder().where().eq("UserID", bs.b).query();
            List<PedometerHistoryDao> mList2 = App_phms.getInstance().mHelper.getPedometerhistoryDao().queryBuilder().where().eq("UserID", mUserName2).and().eq(PedometerHistoryDao.IsUploaded, "2").query();
            if (_list != null && _list.size() > 0) {
                mList2.addAll(_list);
            }
            if (mList2 != null && mList2.size() > 0) {
                new StringBuilder(String.valueOf(mList2.size())).toString();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void init_runlayout_view() {
        this.mPedometerSharepreferance = new PedometerSharepreferance(getActivity());
        this.mBallProgressView = (BallProgressView) this.mView.findViewById(R.id.ballprogressView);
        this.mSpeedsText = (TextView) this.mView.findViewById(R.id.text_pedometer_speed);
        this.mpebometer_TimeHaveGo = (TextView) this.mView.findViewById(R.id.pedometer_show_time_text);
        this.mpebometer_Distance_Text = (TextView) this.mView.findViewById(R.id.pedometer_distance_text);
        this.mpebometer_Speed_Text = (TextView) this.mView.findViewById(R.id.pedometer_speed);
        this.mpebometer_puse_btn = (Button) this.mView.findViewById(R.id.pebometer_puse_btn);
        this.mpebometer_stop_btn = (Button) this.mView.findViewById(R.id.pebometer_over_btn);
        this.mpebometer_puse_btn.setOnClickListener(this);
        this.mpebometer_stop_btn.setOnClickListener(this);
        this.mpebometer_handler_calorie_Text = (TextView) this.mView.findViewById(R.id.pedometer_handler_show_cal_text);
        this.mpebometer_handler_Distance_Text = (TextView) this.mView.findViewById(R.id.pedometer_handler_distance_text);
        this.mpebometer_handler_StepsTextSmall = (TextView) this.mView.findViewById(R.id.pedometer_handler_steps_big_text);
        this.mpebometer_handler_steptarget_aim = (TextView) this.mView.findViewById(R.id.pedometer_handler_steptarget_aim);
        this.mpedometer_handler_stepaverage_progesss = (ProgressBar) this.mView.findViewById(R.id.pedometer_handler_progressBar_setpaverage);
    }

    private void initrunvalue() {
        try {
            List<PedometerSumStepKm> _dao = this.mDatabaseHelper.getPedometerSumStepKmDao().queryBuilder().where().eq("Date", new SimpleDateFormat("yyyy-MM-dd").format(new Date())).and().eq("UserID", App_phms.getInstance().mUserInfo.mUserID).query();
            this._distance = 0;
            this._calSum = 0.0f;
            if (_dao == null || _dao.size() <= 0) {
                this.mpebometer_handler_StepsTextSmall.setText(new StringBuilder(String.valueOf(this.mSteps)).toString());
                sethandlerDistancetextviewvalue(this._distance);
                setHandlerlayoutCaloriesValuse(this._calSum + this.mCalories);
                Message msg = new Message();
                msg.what = 502;
                msg.arg2 = 1;
                App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(msg);
            }
            PedometerSumStepKm _sumstep = _dao.get(0);
            this.mSumSteps = _sumstep.getmSumStep();
            this.mpebometer_handler_StepsTextSmall.setText(new StringBuilder(String.valueOf(this.mSumSteps + this.mSteps)).toString());
            this._distance = _sumstep.getmSumDistance();
            this.mDistanceSumTodayInt = this._distance;
            this._calSum = _sumstep.getmCal();
            sethandlerDistancetextviewvalue(this._distance);
            setHandlerlayoutCaloriesValuse(this._calSum + this.mCalories);
            Message msg2 = new Message();
            msg2.what = 502;
            msg2.arg2 = 1;
            App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(msg2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void changeUIState() {
    }

    public void setUpdateTabInterface(UpdateMainLanguageInterface mUpdateInterface) {
        this.mUpdateTabTextInterface = mUpdateInterface;
    }

    public void onResume() {
        super.onResume();
        CLog.i("jxx", "call Fragmentdevicelist的onResume method");
        this.onPause = false;
        if (Constants.Language.contains("zh")) {
            Configuration config = getResources().getConfiguration();
            DisplayMetrics dm = getResources().getDisplayMetrics();
            config.locale = Locale.CHINESE;
            getResources().updateConfiguration(config, dm);
        } else if (Constants.Language.equalsIgnoreCase("en")) {
            Configuration config2 = getResources().getConfiguration();
            DisplayMetrics dm2 = getResources().getDisplayMetrics();
            config2.locale = Locale.ENGLISH;
            getResources().updateConfiguration(config2, dm2);
        }
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
        this.mUserNameText.setText(_userName);
        FragmentActivity activity = getActivity();
        getActivity();
        ((InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(this.mView.getWindowToken(), 0);
        this.m_adapter.notifyDataSetChanged();
        PageUtil.checkUserinfo(App_phms.getInstance().mUserInfo, getActivity());
        new PedometerSharepreferance(getActivity()).setExit(false);
        Intent _intent = getActivity().getIntent();
        if (_intent != null && _intent.getBooleanExtra(Constants.KEY_PEDOMETOR, false)) {
            _intent.getIntExtra("steps", 0);
        }
        if (Constants.ISFROMSERACHDEVICE) {
            CLog.i(TAG, "FragmentdeviceNew onResume 开始主动连接模式");
            Constants.ISFROMSERACHDEVICE = false;
            checkBlueTooth();
        } else if (Constants.START_8000GW) {
            CLog.eT(TAG, "开启上传8000G文件********");
            App_phms.getInstance().getApplicationContext().startService(new Intent(App_phms.getInstance().getApplicationContext(), UploadService.class));
            Message msgManager = new Message();
            msgManager.obj = Constants.DEVICE_UPLOAD;
            msgManager.what = 51;
            msgManager.arg2 = 5;
            App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(msgManager);
            Constants.START_8000GW = false;
        } else if (DeviceManager.mRefreshBean != null) {
            SearchDevice.stopServer(getActivity());
            DeviceManager.stopServer(getActivity());
            UploadService.stopServer(getActivity());
            MessageManager.stopServer(getActivity());
            DeviceService.stopServer(getActivity());
            Server_Main.stopServer(getActivity());
            CLog.i(TAG, "FragmentdeviceNew onResume  刷新刷新刷新");
            init_value(DeviceManager.mRefreshBean);
            BluetoothServerService.stopServer(getActivity());
            Constants.BLUETOOTHSTAT = 1;
            getActivity().startService(new Intent(getActivity(), Server_Main.class));
        } else {
            CLog.i(TAG, "FragmentdeviceNew onResume  打开回连模式");
            if (Constants.REPORT_TEST) {
                Constants.REPORT_TEST = false;
            }
        }
        if (this.mUpdateTabTextInterface != null) {
            this.mUpdateTabTextInterface.getMainObject().UpdateTabText();
        }
        initrunvalue();
        setLayoutVlaueWhenLanguageChanges();
        this.m_newDevicesListView_phone.setAdapter(this.m_adapter);
        this.m_adapter.notifyDataSetChanged();
        initData();
        if (this.mService != null) {
            Log.i("lz", "onresume is not null resumeinit");
            onResumeinit();
        }
        if (this.misreLogin) {
            onResumeinit();
        }
        judgeShowSearch();
        this.mexit_to_BT_status_te.setText(R.string.exit_to_BT_status);
        String _language = Locale.getDefault().getLanguage();
        int _up_iv_bg = 0;
        if (Constants.Language.equals("zh")) {
            _up_iv_bg = R.drawable.drawable_search_new_devices;
        } else if (Constants.Language.equals("en")) {
            _up_iv_bg = R.drawable.drawable_search_new_devices_en;
        } else if (_language.equals("zh")) {
            _up_iv_bg = R.drawable.drawable_search_new_devices;
        } else if (_language.equals("en")) {
            _up_iv_bg = R.drawable.drawable_search_new_devices_en;
        }
        this.mdevicelist_add_devices.setText(getResources().getString(R.string.theme_text));
        this.msearch_new_devices_bt.setBackgroundResource(_up_iv_bg);
        if (!this.mBluetoothAdapter.isEnabled()) {
            IntentFilter filter = new IntentFilter();
            filter.addAction("android.bluetooth.adapter.action.STATE_CHANGED");
            filter.addAction("android.bluetooth.adapter.action.REQUEST_ENABLE");
            this.mcontext.registerReceiver(this.mBluetoothReceiver, filter);
            this.mBluetoothRegister = true;
            this.mBluetoothAdapter.enable();
        }
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
    }

    private void judgeShowSearch() {
        if (DeviceListDaoOperation.getInstance().getDeviceListItem() == null || DeviceListDaoOperation.getInstance().getDeviceListItem().size() <= 0) {
            this.msearch_new_devices_bt.setVisibility(View.VISIBLE);
            this.mfirst_search_new_devices_layout.setVisibility(View.VISIBLE);
            this.m_newDevicesListView_phone.setVisibility(View.GONE);
            this.mTextPull.setVisibility(View.GONE);
            return;
        }
        this.msearch_new_devices_bt.setVisibility(View.GONE);
        this.mfirst_search_new_devices_layout.setVisibility(View.GONE);
        this.m_newDevicesListView_phone.setVisibility(View.VISIBLE);
        this.mTextPull.setVisibility(View.VISIBLE);
    }

    private void init_value(DeviceBean pBean) {
        CLog.e("yyj", "init_value 更新进度");
        if (this.m_adapter != null) {
            this.m_adapter.notifyDataSetChanged();
        }
    }

    private void setLayoutVlaueWhenLanguageChanges() {
        this.devicelist_data_collection.setText(R.string.data_transfer_text);
        if (this.mpop_bot_add != null) {
            this.mpop_bot_add.setText(R.string.pop_add_count);
        }
        String language = Locale.getDefault().getLanguage();
        this.mTextPull.setText(R.string.str_pull_to_search);
    }

    public static Fragmentdevicelist getInterface() {
        if (mFragmentdevicelist == null) {
            return new Fragmentdevicelist();
        }
        return mFragmentdevicelist;
    }

    public boolean isConnected() {
        NetworkInfo info = ((ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        if (info != null) {
            return info.isAvailable();
        }
        return false;
    }

    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    public void run() {
        this._getreturnparams = this.mUpdateManeger.checkUpdate();
        int _isNeedUpdate = Integer.valueOf(this._getreturnparams.get("_isNeedUpdate")).intValue();
        Message msg = new Message();
        msg.arg2 = 1;
        switch (_isNeedUpdate) {
            case 1:
                msg.what = Constants.V_NEED_UPDATE;
                break;
            case 2:
                msg.what = 507;
                break;
            case 3:
                msg.what = Constants.V_NETWORK_ERROR;
                break;
            case 4:
                msg.what = Constants.V_CONNECTION_TIMEOUT;
                break;
        }
        App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(msg);
    }

    private void initPopMenu() {
        int SCREENWEIGH = getActivity().getWindowManager().getDefaultDisplay().getWidth();
        View view = getActivity().getLayoutInflater().inflate(R.layout.layout_data_userpop, (ViewGroup) null);
        ImageView mpop_top_image = (ImageView) view.findViewById(R.id.pop_top_image);
        this.mpop_center_list = (ListView) view.findViewById(R.id.pop_center_list);
        LinearLayout mpop_bot_add_layout = (LinearLayout) view.findViewById(R.id.pop_bot_add_layout);
        if (this.mPop == null) {
            if (Constants.IS_PAD_NEW) {
                this.mPop = new PopupWindow(view, (SCREENWEIGH * 7) / 12, -2);
                mpop_top_image.getLayoutParams().width = -1;
                mpop_top_image.getLayoutParams().height = ScreenAdapter.dip2px(this.mcontext, 55.0f);
                this.mpop_center_list.getLayoutParams().width = -1;
                this.mpop_center_list.setPadding(35, 0, 35, 0);
                mpop_bot_add_layout.getLayoutParams().width = -1;
                mpop_bot_add_layout.getLayoutParams().height = mpop_top_image.getHeight() + 75;
            } else {
                this.mPop = new PopupWindow(view, (SCREENWEIGH * 9) / 10, -2);
            }
            this.mPop.setBackgroundDrawable(new ColorDrawable(0));
            this.mPop.setOutsideTouchable(true);
            this.mPop.setFocusable(true);
            this.mpop_bot_add = (TextView) view.findViewById(R.id.pop_bot_add);
            this.popUserArray = new ArrayList();
            try {
                this.popUserArray = App_phms.getInstance().mHelper.getUserDao().queryForAll();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            this.mUserAdapter = new UserAdapter(getActivity(), this.popUserArray, this.mPop);
            this.mpop_center_list.setAdapter(this.mUserAdapter);
        }
        if (this.mPop.isShowing()) {
            this.mPop.dismiss();
        }
        this.mpop_center_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View arg1, final int arg2, long arg3) {
                Fragmentdevicelist.this.mUserAdapter.mIfItemClicked = true;
                Fragmentdevicelist.this.mUserAdapter.mCheckedItem = arg2;
                Fragmentdevicelist.this.mpop_center_list.setSelection(arg2);
                Fragmentdevicelist.this.mUserAdapter.notifyDataSetChanged();
                new Thread() {
                    public void run() {
                        try {
                            Thread.sleep(500);
                            Message msg = new Message();
                            msg.what = 1;
                            msg.arg1 = arg2;
                            Fragmentdevicelist.this.mHandler.sendMessage(msg);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }.start();
            }
        });
        this.mHandler = new Handler() {
            public void dispatchMessage(Message msg) {
                if (msg.what == 1) {
                    Fragmentdevicelist.this.changeUser(msg.arg1);
                }
            }
        };
        mpop_bot_add_layout.setOnClickListener(this);
    }

    private void changeUser(int pPosition) {
        UserInfoDao _userDao = this.popUserArray.get(pPosition);
        if (!_userDao.getmUserId().equals(App_phms.getInstance().mUserInfo.mUserID)) {
            if (this.mPop.isShowing()) {
                this.mPop.dismiss();
            }
            String _nozerowillphonenumber = _userDao.getmUserId().substring(0);
            Intent _intent = new Intent(getActivity(), LoginActivity.class);
            Bundle _bundle = new Bundle();
            _bundle.putBoolean("fromchangeuser", true);
            _bundle.putString("username", _nozerowillphonenumber);
            _bundle.putString("userpsw", _userDao.getPsw());
            _intent.putExtras(_bundle);
            startActivity(_intent);
        } else if (this.mPop.isShowing()) {
            this.mPop.dismiss();
        }
        this.mUserAdapter.mIfItemClicked = false;
        this.mUserAdapter.mCheckedItem = -1;
    }

    private void StartIntentLogin() {
        if (this.mPop.isShowing()) {
            this.mPop.dismiss();
        }
        Intent _intent = new Intent(getActivity(), LoginActivity.class);
        Bundle _bundle = new Bundle();
        _bundle.putBoolean("fromaddnewUser", true);
        _bundle.putString("username", bs.b);
        _bundle.putString("userpsw", bs.b);
        _intent.putExtras(_bundle);
        startActivity(_intent);
    }

    private void showAllUser() {
        initPopMenu();
        this.mPop.showAsDropDown(this.muser_layout, 0, -30);
    }

    class UserAdapter extends BaseAdapter {
        public int mCheckedItem;
        public Context mContext;
        public boolean mIfItemClicked = false;
        private PopupWindow mPop;
        public List<UserInfoDao> popUserArray;

        public UserAdapter(Context pContext, List<UserInfoDao> popUserArray2, PopupWindow pPop) {
            this.mContext = pContext;
            this.popUserArray = popUserArray2;
            this.mPop = pPop;
        }

        public int getCount() {
            if (this.popUserArray != null) {
                return this.popUserArray.size();
            }
            return 0;
        }

        public Object getItem(int position) {
            return this.popUserArray.get(position);
        }

        public long getItemId(int position) {
            return (long) position;
        }

        public View getView(final int position, View convertView, ViewGroup parent) {
            View view = LayoutInflater.from(this.mContext).inflate(R.layout.layout_data_poplist_item, (ViewGroup) null);
            ImageView muser_head = (ImageView) view.findViewById(R.id.user_head);
            TextView muser_name = (TextView) view.findViewById(R.id.user_name);
            String _userName = this.popUserArray.get(position).getmUserName();
            String _userId = this.popUserArray.get(position).getmUserId().substring(0);
            if (_userName == null || _userName.equals(bs.b)) {
                muser_name.setText("请设置姓名");
            } else {
                muser_name.setText(_userName);
            }
            TextView muser_id = (TextView) view.findViewById(R.id.user_id);
            muser_id.setText(_userId);
            ImageView mUserDel = (ImageView) view.findViewById(R.id.user_radioImg);
            mUserDel.setImageResource(R.drawable.drawable_set_devicemanager_del);
            mUserDel.setOnClickListener(new View.OnClickListener() {
                public void onClick(View arg0) {
                    try {
                        UserInfoDao _userInfoDao = UserAdapter.this.popUserArray.get(position);
                        App_phms.getInstance().mHelper.getUserDao().delete(_userInfoDao);
                        UserAdapter.this.popUserArray.remove(position);
                        UserAdapter.this.notifyDataSetChanged();
                        String _userIdDel = _userInfoDao.getmUserId();
                        if (_userIdDel != null && _userIdDel.equals(App_phms.getInstance().mUserInfo.mUserID)) {
                            App_phms.getInstance().exitall(0);
                            if (!DeviceService.mReceiveFinished) {
                                DeviceService.mReceiveFinished = true;
                            }
                            UserAdapter.this.mContext.startActivity(new Intent(UserAdapter.this.mContext, LoginActivity.class));
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            });
            muser_head.setImageResource(R.drawable.drawable_pop_user_men_noselect);
            if (this.mIfItemClicked) {
                if (position == this.mCheckedItem) {
                    CLog.e("UserAdapter  mCheckedItem: ", new StringBuilder(String.valueOf(this.mCheckedItem)).toString());
                    muser_head.setImageResource(R.drawable.drawable_pop_user_men_green);
                    muser_name.getPaint().setFakeBoldText(true);
                    muser_id.getPaint().setFakeBoldText(true);
                }
            } else if (_userId == null || !_userId.equals(App_phms.getInstance().mUserInfo.mUserID.substring(0))) {
                muser_head.setImageResource(R.drawable.drawable_pop_user_men_noselect);
            } else {
                muser_head.setImageResource(R.drawable.drawable_pop_user_men_green);
                muser_name.getPaint().setFakeBoldText(true);
                muser_id.getPaint().setFakeBoldText(true);
            }
            return view;
        }
    }

    public void onClick(View v) {
        Constants.REPORT = false;
        Constants.START_USER_INFO_PAGE = false;
        if (PageUtil.isFastDoubleClick() || v.getId() == R.id.listdevice_starrun_layout) {
            return;
        }
        if (v.getId() == R.id.devicelist_add_devices) {
            enterManagerDeviceFragment();
        } else if (v.getId() == R.id.userlayout) {
            startActivityForResult(new Intent(getActivity(), ActivityChooseHead.class), 1);
        } else if (v.getId() == R.id.pop_bot_add_layout) {
            StartIntentLogin();
        } else if (v.getId() == R.id.search_new_devices_bt) {
            enterSearchDeviceFragment();
        } else if (v.getId() == R.id.pebometer_puse_btn) {
            Object _mpebometer_stop_btntag = this.mpebometer_stop_btn.getTag();
            if (_mpebometer_stop_btntag != null) {
                String trim = this.mpebometer_stop_btn.getTag().toString().trim();
                if (_mpebometer_stop_btntag != null && _mpebometer_stop_btntag.equals("stoped")) {
                    bindrunService();
                    startrunService();
                    CLog.e(TAG, "启动service******************");
                    new Thread() {
                        public void run() {
                            try {
                                Thread.sleep(300);
                                Fragmentdevicelist.this.mHandler.sendEmptyMessage(5);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }.start();
                    this.mpebometer_puse_btn.setText(this.mcontext.getString(R.string.str_pedometer_puse));
                    this.mpebometer_stop_btn.setTag(bs.b);
                    return;
                }
            }
            String _tempbtnname = this.mpebometer_puse_btn.getText().toString().trim();
            if (_tempbtnname.equals(this.mcontext.getString(R.string.str_pedometer_start))) {
                this.mpebometer_puse_btn.setText(this.mcontext.getString(R.string.str_pedometer_puse));
                if (this.mService != null) {
                    this.mService.setPause(false);
                }
            } else if (_tempbtnname.equals(this.mcontext.getString(R.string.str_pedometer_puse))) {
                if (this.mService != null) {
                    this.mService.setPause(true);
                }
                this.mPauseFlag = true;
                this.mpebometer_puse_btn.setText(this.mcontext.getString(R.string.str_pedometer_start));
            }
        } else if (v.getId() == R.id.pebometer_over_btn) {
            if (this.mService == null) {
                CLog.i("lz", "stop mservice is  null");
                return;
            }
            CLog.i("lz", "stop mservice is not null");
            this.mpebometer_puse_btn.setText(this.mcontext.getString(R.string.str_pedometer_start));
            this.mpebometer_stop_btn.setTag("stoped");
            unbindrunService();
            if (this.mStepsInt != 0) {
                Message msg = new Message();
                msg.what = 512;
                if (!this.isVisibleHistoryBtn) {
                    msg.arg1 = 9;
                } else {
                    msg.arg1 = 8;
                }
                msg.arg2 = 7;
                App_phms.getInstance().mEventBus.post(msg);
            } else {
                if (!this.isVisibleHistoryBtn) {
                    EventFragment _fragment = new EventFragment();
                    _fragment.setmWhichCommand(6);
                    App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(_fragment);
                    CLog.e(TAG, "结束程序  没有数据可以上传");
                }
                if (this.mBackground) {
                    stoprunService();
                }
            }
            this.mService = null;
            upLoadPedemeterData();
        } else if (v.getId() == R.id.devicelist_change_user) {
            startActivityForResult(new Intent(getActivity(), ActivityChooseHead.class), 1);
        }
    }

    private void startAddDevice(boolean ifCircle) {
        BluetoothAdapter mBluetoothAdapter2 = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter2.isDiscovering()) {
            mBluetoothAdapter2.cancelDiscovery();
            Constants.GO_TO_ADD_DEVICE = true;
            SearchDevice.stopServer(getActivity());
            DeviceManager.stopServer(getActivity());
            UploadService.stopServer(getActivity());
            MessageManager.stopServer(getActivity());
            DeviceService.stopServer(getActivity());
            Server_Main.stopServer(getActivity());
            startActivity(new Intent(this.mcontext, FragmentDataSerchDevice.class));
        } else if (ifCircle) {
            if (DeviceService.mReceiveFinished || DeviceManager.mDeviceList == null || DeviceManager.mDeviceList.size() <= 0) {
                SearchDevice.stopServer(getActivity());
                DeviceManager.stopServer(getActivity());
                UploadService.stopServer(getActivity());
                MessageManager.stopServer(getActivity());
                DeviceService.stopServer(getActivity());
                Server_Main.stopServer(getActivity());
                Constants.GO_TO_ADD_DEVICE = false;
                startActivity(new Intent(this.mcontext, FragmentDataSerchDevice.class));
                return;
            }
            Constants.GO_TO_ADD_DEVICE = true;
            Message msgs = new Message();
            msgs.what = Constants.V_SHOW_ADD_DEVICE_DIALOG;
            msgs.arg2 = 1;
            App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(msgs);
        } else if (!DeviceService.mReceiveFinished) {
            Constants.GO_TO_ADD_DEVICE = true;
            Message msgs2 = new Message();
            msgs2.what = Constants.V_SHOW_ADD_DEVICE_DIALOG;
            msgs2.arg2 = 1;
            App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(msgs2);
        } else {
            SearchDevice.stopServer(getActivity());
            DeviceManager.stopServer(getActivity());
            UploadService.stopServer(getActivity());
            MessageManager.stopServer(getActivity());
            DeviceService.stopServer(getActivity());
            Server_Main.stopServer(getActivity());
            Constants.GO_TO_ADD_DEVICE = false;
            startActivity(new Intent(this.mcontext, FragmentDataSerchDevice.class));
        }
    }

    private void enterManagerDeviceFragment() {
        BluetoothServerService.stopServer(getActivity());
        Message msg = new Message();
        msg.what = Constants.OPEN_DEVICE_MANAGEDEVICE;
        msg.arg2 = 1;
        App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(msg);
    }

    private void enterSearchDeviceFragment() {
        Message msg = new Message();
        msg.what = Constants.OPEN_SEARCHDEVICE_FRAGMENT;
        msg.arg2 = 1;
        App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(msg);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode) {
            case 1:
                if (!(data == null || data.getExtras().getParcelable("data") == null)) {
                    Bitmap tempdata = (Bitmap) data.getExtras().getParcelable("data");
                    Bitmap photo = Bitmap.createScaledBitmap(tempdata, 200, 200, true);
                    SaveHeadiconToSDCard(tempdata);
                    this._devicelist_change_user.setBackgroundResource(0);
                    this._devicelist_change_user.setImageBitmap(photo);
                    break;
                }
            case 2:
                if (data != null) {
                    int extras = data.getIntExtra("grid_item", 1);
                    SaveHeadiconToSDCard(extras);
                    this._devicelist_change_user.setBackgroundResource(0);
                    this._devicelist_change_user.setImageResource(extras);
                    break;
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void getSdcardPhoto(String puserflag, String picturename) {
        String pSdCardPath = Environment.getExternalStorageDirectory() + CookieSpec.PATH_DELIM;
        String _append = "contec/userinfo/" + puserflag + CookieSpec.PATH_DELIM;
        FileOperation.makeDirs(String.valueOf(pSdCardPath) + _append);
        try {
            if (new File(String.valueOf(pSdCardPath) + _append + picturename).exists()) {
                this._devicelist_change_user.setImageBitmap(BitmapFactory.decodeFile(String.valueOf(pSdCardPath) + _append + picturename));
                return;
            }
            this._devicelist_change_user.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.img_140915_twelve));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void SaveHeadiconToSDCard(int pictureSource) {
        Bitmap photo = BitmapFactory.decodeResource(getActivity().getResources(), pictureSource);
        String _path = FileOperation.getSdcardUserHeadPath(App_phms.getInstance().GetUserInfoNAME());
        File file = new File(_path);
        if (!file.exists()) {
            file.mkdir();
        }
        try {
            photo.compress(Bitmap.CompressFormat.PNG, 60, new FileOutputStream(String.valueOf(_path) + "imagehead.png", false));
            photo.compress(Bitmap.CompressFormat.PNG, 60, new ByteArrayOutputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void SaveHeadiconToSDCard(Bitmap _bitmap) {
        Bitmap photo = _bitmap;
        String _path = FileOperation.getSdcardUserHeadPath(App_phms.getInstance().GetUserInfoNAME());
        File file = new File(_path);
        if (!file.exists()) {
            file.mkdir();
        }
        try {
            photo.compress(Bitmap.CompressFormat.PNG, 60, new FileOutputStream(String.valueOf(_path) + "imagehead.png", false));
            photo.compress(Bitmap.CompressFormat.PNG, 60, new ByteArrayOutputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onEvent(DeviceBean mBean) {
        CLog.e("yyj", "更新进度");
        if (mBean != null) {
            if (this.mdevicelist_add_devices != null) {
                setEnableEnterDeviceManager(mBean.mState, this.mdevicelist_add_devices);
            }
            if (this.m_adapter != null) {
                this.m_adapter.notifyDataSetChanged();
            }
        }
    }

    public void onEvent(Message msg) {
        if (msg.arg2 == 1) {
            CLog.d(TAG, "更新list列表内容命令 " + msg.what);
            switch (msg.what) {
                case 500:
                    CLog.i(TAG, "清空list列表");
                    this.m_adapter.clear();
                    this.m_adapter.notifyDataSetChanged();
                    return;
                case 501:
                    CLog.i(TAG, "V_ADD_SEARCH_DEVICE*******************************");
                    this.m_adapter.getDeviceList();
                    this.m_adapter.notifyDataSetChanged();
                    this.m_newDevicesListView_phone.requestFocus();
                    if (DeviceManager.mDeviceList == null || DeviceManager.mDeviceList.size() <= 0) {
                        this.msearch_new_devices_bt.setVisibility(View.VISIBLE);
                        this.mfirst_search_new_devices_layout.setVisibility(View.VISIBLE);
                        this.m_newDevicesListView_phone.setVisibility(View.GONE);
                        this.msearch_new_devices_bt.setEnabled(true);
                        this.mfirst_search_new_devices_layout.setEnabled(true);
                        this.mTextPull.setVisibility(View.GONE);
                        return;
                    }
                    this.msearch_new_devices_bt.setVisibility(View.GONE);
                    this.mfirst_search_new_devices_layout.setVisibility(View.GONE);
                    this.m_newDevicesListView_phone.setVisibility(View.VISIBLE);
                    this.mTextPull.setVisibility(View.VISIBLE);
                    return;
                case 502:
                    this.m_adapter.notifyDataSetChanged();
                    return;
                case Constants.V_PEDOMETER_NO_UPLOAD_DATA /*510*/:
                    setPedometerDataInfo();
                    return;
                case Constants.V_UPDATE_PEDOMETER_SUM_STEPS /*513*/:
                    CLog.d("PedometerService", "V_UPDATE_PEDOMETER_SUM_STEPS mSteps:" + msg.arg1);
                    return;
                case Constants.V_NOTIFY_SEARCH_TIME_LONG_CANCLE /*518*/:
                    AlertDialogUtil.cancleDialog();
                    return;
                case Constants.Login_In_Another_Place /*519*/:
                    TimerUtil.getinstance().clearTimer();
                    Activity last = App_phms.getInstance().getlastActivity();
                    if (last == null || last.getClass().getName().contains("ActivityLoginAnotherPlace")) {
                        last.finish();
                        Intent inte = new Intent(App_phms.getInstance().getBaseContext(), ActivityLoginAnotherPlace.class);
                        inte.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        inte.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(inte);
                        CLog.dT(TAG, "已经启动了ActivityLoginAnotherPlace activity");
                        return;
                    }
                    Intent inte2 = new Intent(App_phms.getInstance().getBaseContext(), ActivityLoginAnotherPlace.class);
                    inte2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(inte2);
                    return;
                case Constants.Del_Device /*520*/:
                    this.m_adapter.notifyDataSetChanged();
                    if (DeviceManager.mDeviceList == null || DeviceManager.mDeviceList.size() <= 0) {
                        CLog.e("del", "删除设备回来****************");
                        this.m_newDevicesListView_phone.setVisibility(View.GONE);
                        this.msearch_new_devices_bt.setVisibility(View.VISIBLE);
                        this.mfirst_search_new_devices_layout.setVisibility(View.VISIBLE);
                        this.mTextPull.setVisibility(View.GONE);
                        this.msearch_new_devices_bt.setEnabled(true);
                        SearchDevice.stopServer(getActivity());
                        DeviceManager.stopServer(getActivity());
                        UploadService.stopServer(getActivity());
                        MessageManager.stopServer(getActivity());
                        DeviceService.stopServer(getActivity());
                        Server_Main.stopServer(getActivity());
                        DataFilter.moveDataToFailedDir();
                        return;
                    }
                    this.msearch_new_devices_bt.setVisibility(View.GONE);
                    this.mfirst_search_new_devices_layout.setVisibility(View.GONE);
                    this.m_newDevicesListView_phone.setVisibility(View.VISIBLE);
                    this.mTextPull.setVisibility(View.VISIBLE);
                    return;
                case Constants.V_NOTIFY_REC_DATA_NET /*521*/:
                    if (DeviceManager.m_DeviceBean != null && DeviceManager.m_DeviceBean.mState == 4) {
                        AlertDialogUtil.showBlutLongDialog(false, getResources().getString(R.string.rec_notic_report), getActivity());
                    } else if (DeviceManager.m_DeviceBean != null && DeviceManager.m_DeviceBean.mState == 1) {
                        AlertDialogUtil.showBlutLongDialog(false, getResources().getString(R.string.rec_notic_report_connecting), getActivity());
                    }
                    CLog.e(TAG, "--------report-----");
                    return;
                case Constants.V_NOTIFY_PERSON_INFO /*525*/:
                    AlertDialogUtil.showBlutLongDialog(false, getResources().getString(R.string.rec_notic_user_info), getActivity());
                    CLog.e(TAG, "--------report-----");
                    return;
                case Constants.V_START_USER_INFO /*526*/:
                    Constants.START_USER_INFO_PAGE = false;
                    startActivity(new Intent(getActivity(), ActivityUserData.class));
                    return;
                case Constants.V_RELOGIN_INBACKGROUND /*528*/:
                    reloginBackGround();
                    return;
                case Constants.V_SHOW_ADD_DEVICE_DIALOG /*533*/:
                    if (DeviceManager.m_DeviceBean != null && DeviceManager.m_DeviceBean.mState == 4) {
                        AlertDialogUtil.showBlutLongDialog(false, getResources().getString(R.string.rec_notic_add_device), getActivity());
                        return;
                    } else if (DeviceManager.m_DeviceBean != null && DeviceManager.m_DeviceBean.mState == 1) {
                        AlertDialogUtil.showBlutLongDialog(false, getResources().getString(R.string.rec_notic_add_device_connecting), getActivity());
                        return;
                    } else {
                        return;
                    }
                case Constants.START_ADD_DEVICE /*534*/:
                    startActivity(new Intent(this.mcontext, FragmentDataSerchDevice.class));
                    return;
                case Constants.CONNECT_DEVICE_HM /*543*/:
                    if (DeviceManager.mRefreshBean != null) {
                        SearchDevice.stopServer(getActivity());
                        DeviceManager.stopServer(getActivity());
                        UploadService.stopServer(getActivity());
                        MessageManager.stopServer(getActivity());
                        DeviceService.stopServer(getActivity());
                        Server_Main.stopServer(getActivity());
                        CLog.i(TAG, "手动链接某个设备***********");
                        init_value(DeviceManager.mRefreshBean);
                        Constants.BLUETOOTHSTAT = 1;
                        getActivity().startService(new Intent(getActivity(), Server_Main.class));
                        return;
                    }
                    return;
                case Constants.Login_In_Another_Place_Sure /*550*/:
                    reloginAnother();
                    return;
                case 551:
                    Intent i = new Intent(getActivity(), LoginActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    getActivity().startActivity(i);
                    return;
                case Constants.EVENT_DEVICEMANAGERSTATE /*554*/:
                    ui_DeviceManage(this.mdevicelist_add_devices, true);
                    return;
                case Constants.CHECK_TIME_ILLEGAL /*1234*/:
                    new DialogClass(this.mcontext, getResources().getString(R.string.str_collect_data_illegal));
                    return;
                default:
                    return;
            }
        }
    }

    void initCallBack() {
        this.mPedomCallBack = new PedometerService.CallBack() {
            public void setUserName(String pUserID) {
                Fragmentdevicelist.this.mUserID = pUserID;
            }

            public void startTime(String startTime) {
            }

            public void stepsChanged(float value) {
                Fragmentdevicelist.this.mStepsInt = (int) value;
                Fragmentdevicelist.this.mBallProgressView.setmTargetSteps(Fragmentdevicelist.this.mStepTarget);
                Fragmentdevicelist.this.mBallProgressView.setmSteps(Fragmentdevicelist.this.mStepsSumInt + Fragmentdevicelist.this.mStepsInt);
                Fragmentdevicelist.this.mBallProgressView.invalidate();
                Fragmentdevicelist.this.mpedometer_handler_stepaverage_progesss.setProgress(((Fragmentdevicelist.this.mStepsSumInt + Fragmentdevicelist.this.mStepsInt) * 100) / Fragmentdevicelist.this.mStepTarget);
                Fragmentdevicelist.this.mpebometer_handler_StepsTextSmall.setText(new StringBuilder(String.valueOf(Fragmentdevicelist.this.mStepsSumInt + Fragmentdevicelist.this.mStepsInt)).toString());
                if (Fragmentdevicelist.this.mStepsInt == Fragmentdevicelist.this.mStepTarget) {
                    new Thread() {
                        public void run() {
                            Fragmentdevicelist.this.vibrator = (Vibrator) Fragmentdevicelist.this.mcontext.getSystemService(Context.VIBRATOR_SERVICE);
                            Fragmentdevicelist.this.vibrator.vibrate(new long[]{100, 400, 100, 400}, 2);
                            try {
                                Thread.sleep(2000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            Fragmentdevicelist.this.vibrator.cancel();
                        }
                    }.start();
                    Toast.makeText(Fragmentdevicelist.this.mcontext, Fragmentdevicelist.this.mcontext.getResources().getString(R.string.str_pedometer_step_over), Toast.LENGTH_LONG).show();
                }
            }

            public void speedChanged(float value) {
                Fragmentdevicelist.this.mAverageSpeed = value;
                Fragmentdevicelist.this.mHandler.obtainMessage(1, Float.valueOf(value)).sendToTarget();
            }

            public void timeChanged(int value) {
                Fragmentdevicelist.this.mTimeInt = value;
                Fragmentdevicelist.this.mHandler.obtainMessage(0, Integer.valueOf(Fragmentdevicelist.this.mTimeInt)).sendToTarget();
            }

            public void distanceChanged(float value) {
                Fragmentdevicelist.this.mDistance = value;
                CLog.e(Fragmentdevicelist.TAG, "mDistance= " + Fragmentdevicelist.this.mDistance);
                Fragmentdevicelist.this.setpebometer_Distancetextviewvalue(Float.valueOf(value));
            }

            public void caloriesChanged(float value) {
                Fragmentdevicelist.this.mCalories = value;
                CLog.i("lz", "calories value = " + value);
                Fragmentdevicelist.this.setlayoutCaloriesValuse(value);
                Fragmentdevicelist.this.setHandlerlayoutCaloriesValuse(Fragmentdevicelist.this._calSum + Fragmentdevicelist.this.mCalories);
            }
        };
    }

    /* access modifiers changed from: private */
    /* JADX WARNING: Removed duplicated region for block: B:20:0x0233  */
    @android.annotation.SuppressLint({"SimpleDateFormat"})
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void initData() {
        /*
            r20 = this;
            com.contec.phms.App_phms r17 = com.contec.phms.App_phms.getInstance()
            r0 = r17
            com.contec.phms.infos.UserInfo r0 = r0.mUserInfo
            r17 = r0
            r0 = r17
            java.lang.String r0 = r0.mUserID
            r17 = r0
            r0 = r17
            r1 = r20
            r1.mUserID = r0
            com.contec.phms.App_phms r17 = com.contec.phms.App_phms.getInstance()
            r0 = r17
            com.contec.phms.infos.UserInfo r0 = r0.mUserInfo
            r17 = r0
            r0 = r17
            java.lang.String r0 = r0.mUserName
            r17 = r0
            r0 = r17
            r1 = r20
            r1.mUserName = r0
            r0 = r20
            com.contec.phms.util.PedometerSharepreferance r0 = r0.mPedometerSharepreferance
            r17 = r0
            int r17 = r17.getTarget()
            r0 = r17
            r1 = r20
            r1.mStepTarget = r0
            r0 = r20
            com.contec.phms.widget.BallProgressView r0 = r0.mBallProgressView
            r17 = r0
            r0 = r20
            int r0 = r0.mStepTarget
            r18 = r0
            r17.setmTargetSteps(r18)
            r0 = r20
            android.widget.TextView r0 = r0.mpebometer_handler_steptarget_aim
            r17 = r0
            java.lang.StringBuilder r18 = new java.lang.StringBuilder
            r0 = r20
            int r0 = r0.mStepTarget
            r19 = r0
            java.lang.String r19 = java.lang.String.valueOf(r19)
            r18.<init>(r19)
            java.lang.String r18 = r18.toString()
            r17.setText(r18)
            java.text.SimpleDateFormat r5 = new java.text.SimpleDateFormat
            java.lang.String r17 = "yyyy-MM-dd"
            r0 = r17
            r5.<init>(r0)
            long r17 = java.lang.System.currentTimeMillis()
            java.lang.Long r17 = java.lang.Long.valueOf(r17)
            r0 = r17
            java.lang.String r17 = r5.format(r0)
            r0 = r17
            r1 = r20
            r1.mDateStrSum = r0
            com.contec.phms.App_phms r17 = com.contec.phms.App_phms.getInstance()
            r0 = r17
            com.contec.phms.db.DatabaseHelper r0 = r0.mHelper
            r17 = r0
            com.j256.ormlite.dao.Dao r17 = r17.getPedometerhistoryDao()
            r0 = r17
            r1 = r20
            r1.mDao = r0
            r4 = 0
            r10 = 0
            r11 = 0
            r0 = r20
            com.j256.ormlite.dao.Dao<com.contec.phms.db.PedometerHistoryDao, java.lang.String> r0 = r0.mDao     // Catch:{ SQLException -> 0x0276 }
            r17 = r0
            com.j256.ormlite.stmt.QueryBuilder r17 = r17.queryBuilder()     // Catch:{ SQLException -> 0x0276 }
            com.j256.ormlite.stmt.Where r17 = r17.where()     // Catch:{ SQLException -> 0x0276 }
            java.lang.String r18 = "Date"
            r0 = r20
            java.lang.String r0 = r0.mDateStrSum     // Catch:{ SQLException -> 0x0276 }
            r19 = r0
            com.j256.ormlite.stmt.Where r17 = r17.eq(r18, r19)     // Catch:{ SQLException -> 0x0276 }
            com.j256.ormlite.stmt.Where r17 = r17.and()     // Catch:{ SQLException -> 0x0276 }
            java.lang.String r18 = "UserID"
            r0 = r20
            java.lang.String r0 = r0.mUserID     // Catch:{ SQLException -> 0x0276 }
            r19 = r0
            com.j256.ormlite.stmt.Where r17 = r17.eq(r18, r19)     // Catch:{ SQLException -> 0x0276 }
            java.util.List r17 = r17.query()     // Catch:{ SQLException -> 0x0276 }
            r0 = r17
            r1 = r20
            r1.mPedometerHistoryDaoList = r0     // Catch:{ SQLException -> 0x0276 }
            r0 = r20
            java.util.List<com.contec.phms.db.PedometerHistoryDao> r0 = r0.mPedometerHistoryDaoList     // Catch:{ SQLException -> 0x0276 }
            r17 = r0
            int r17 = r17.size()     // Catch:{ SQLException -> 0x0276 }
            if (r17 <= 0) goto L_0x00ed
            r16 = 0
        L_0x00dd:
            r0 = r20
            java.util.List<com.contec.phms.db.PedometerHistoryDao> r0 = r0.mPedometerHistoryDaoList     // Catch:{ SQLException -> 0x0276 }
            r17 = r0
            int r17 = r17.size()     // Catch:{ SQLException -> 0x0276 }
            r0 = r16
            r1 = r17
            if (r0 < r1) goto L_0x0250
        L_0x00ed:
            java.text.SimpleDateFormat r5 = new java.text.SimpleDateFormat
            java.lang.String r17 = "yyyy年MM月dd日 E"
            r0 = r17
            r5.<init>(r0)
            long r17 = java.lang.System.currentTimeMillis()
            java.lang.Long r17 = java.lang.Long.valueOf(r17)
            r0 = r17
            java.lang.String r17 = r5.format(r0)
            r0 = r17
            r1 = r20
            r1.mDateStr = r0
            java.lang.String r17 = com.contec.phms.util.Constants.Language
            java.lang.String r18 = "en"
            boolean r17 = r17.equals(r18)
            if (r17 == 0) goto L_0x027c
            java.util.Calendar r13 = java.util.Calendar.getInstance()
            java.util.Date r17 = r13.getTime()
            java.lang.String r3 = r17.toString()
            java.lang.StringBuilder r17 = new java.lang.StringBuilder
            r18 = 0
            r19 = 10
            r0 = r18
            r1 = r19
            java.lang.String r18 = r3.substring(r0, r1)
            java.lang.String r18 = java.lang.String.valueOf(r18)
            r17.<init>(r18)
            java.lang.String r18 = " "
            java.lang.StringBuilder r17 = r17.append(r18)
            int r18 = r3.length()
            int r18 = r18 + -4
            int r19 = r3.length()
            r0 = r18
            r1 = r19
            java.lang.String r18 = r3.substring(r0, r1)
            java.lang.StringBuilder r17 = r17.append(r18)
            java.lang.String r17 = r17.toString()
            r0 = r17
            r1 = r20
            r1.mDateStr = r0
            r13 = 0
        L_0x015c:
            java.lang.String r17 = "PedometerService"
            java.lang.StringBuilder r18 = new java.lang.StringBuilder
            java.lang.String r19 = "**********mUserName:"
            r18.<init>(r19)
            r0 = r20
            java.lang.String r0 = r0.mUserName
            r19 = r0
            java.lang.StringBuilder r18 = r18.append(r19)
            java.lang.String r19 = "     "
            java.lang.StringBuilder r18 = r18.append(r19)
            r0 = r20
            java.lang.String r0 = r0.mUserID
            r19 = r0
            java.lang.StringBuilder r18 = r18.append(r19)
            java.lang.String r18 = r18.toString()
            com.contec.phms.util.CLog.e(r17, r18)
            java.text.SimpleDateFormat r5 = new java.text.SimpleDateFormat
            java.lang.String r17 = "HH:mm:ss"
            r0 = r17
            r5.<init>(r0)
            r0 = r20
            r0.mDistanceSumTodayInt = r4
            r0 = r20
            r0.mStepsSumInt = r10
            r0 = r20
            r0.mTotalSeconds = r11
            com.contec.phms.App_phms r17 = com.contec.phms.App_phms.getInstance()     // Catch:{ SQLException -> 0x03be }
            r0 = r17
            com.contec.phms.db.DatabaseHelper r0 = r0.mHelper     // Catch:{ SQLException -> 0x03be }
            r17 = r0
            com.j256.ormlite.dao.Dao r17 = r17.getPedometerSumStepKmDao()     // Catch:{ SQLException -> 0x03be }
            com.j256.ormlite.stmt.QueryBuilder r17 = r17.queryBuilder()     // Catch:{ SQLException -> 0x03be }
            com.j256.ormlite.stmt.Where r17 = r17.where()     // Catch:{ SQLException -> 0x03be }
            java.lang.String r18 = "Date"
            r0 = r20
            java.lang.String r0 = r0.mDateStrSum     // Catch:{ SQLException -> 0x03be }
            r19 = r0
            com.j256.ormlite.stmt.Where r17 = r17.eq(r18, r19)     // Catch:{ SQLException -> 0x03be }
            com.j256.ormlite.stmt.Where r17 = r17.and()     // Catch:{ SQLException -> 0x03be }
            java.lang.String r18 = "UserID"
            r0 = r20
            java.lang.String r0 = r0.mUserID     // Catch:{ SQLException -> 0x03be }
            r19 = r0
            com.j256.ormlite.stmt.Where r17 = r17.eq(r18, r19)     // Catch:{ SQLException -> 0x03be }
            java.util.List r7 = r17.query()     // Catch:{ SQLException -> 0x03be }
            java.lang.String r17 = TAG     // Catch:{ SQLException -> 0x03be }
            java.lang.StringBuilder r18 = new java.lang.StringBuilder     // Catch:{ SQLException -> 0x03be }
            java.lang.String r19 = "**********_listPedometerSumStepKm:"
            r18.<init>(r19)     // Catch:{ SQLException -> 0x03be }
            r0 = r18
            java.lang.StringBuilder r18 = r0.append(r7)     // Catch:{ SQLException -> 0x03be }
            java.lang.String r18 = r18.toString()     // Catch:{ SQLException -> 0x03be }
            com.contec.phms.util.CLog.e(r17, r18)     // Catch:{ SQLException -> 0x03be }
            if (r7 == 0) goto L_0x03ac
            int r17 = r7.size()     // Catch:{ SQLException -> 0x03be }
            if (r17 <= 0) goto L_0x03ac
            r17 = 0
            r0 = r17
            java.lang.Object r9 = r7.get(r0)     // Catch:{ SQLException -> 0x03be }
            com.contec.phms.db.PedometerSumStepKm r9 = (com.contec.phms.db.PedometerSumStepKm) r9     // Catch:{ SQLException -> 0x03be }
            int r17 = r9.getmSumStep()     // Catch:{ SQLException -> 0x03be }
            r0 = r17
            r1 = r20
            r1.mStepsSumInt = r0     // Catch:{ SQLException -> 0x03be }
            int r17 = r9.getmSumDistance()     // Catch:{ SQLException -> 0x03be }
            r0 = r17
            r1 = r20
            r1.mDistanceSumTodayInt = r0     // Catch:{ SQLException -> 0x03be }
            java.lang.String r17 = TAG     // Catch:{ SQLException -> 0x03be }
            java.lang.StringBuilder r18 = new java.lang.StringBuilder     // Catch:{ SQLException -> 0x03be }
            java.lang.String r19 = "**********_listPedometerSumStepKm:"
            r18.<init>(r19)     // Catch:{ SQLException -> 0x03be }
            float r19 = r9.getmCal()     // Catch:{ SQLException -> 0x03be }
            java.lang.StringBuilder r18 = r18.append(r19)     // Catch:{ SQLException -> 0x03be }
            java.lang.String r18 = r18.toString()     // Catch:{ SQLException -> 0x03be }
            com.contec.phms.util.CLog.e(r17, r18)     // Catch:{ SQLException -> 0x03be }
        L_0x0225:
            r0 = r20
            int r0 = r0.mUnit
            r17 = r0
            r18 = 1
            r0 = r17
            r1 = r18
            if (r0 == r1) goto L_0x0246
            float r0 = (float) r4
            r17 = r0
            r18 = 1079113889(0x4051f8a1, float:3.2808)
            float r17 = r17 * r18
            r0 = r17
            int r0 = (int) r0
            r17 = r0
            r0 = r17
            r1 = r20
            r1.mDistanceSumTodayInt = r0
        L_0x0246:
            r0 = r20
            com.contec.phms.widget.BallProgressView r0 = r0.mBallProgressView
            r17 = r0
            r17.invalidate()
            return
        L_0x0250:
            r0 = r20
            java.util.List<com.contec.phms.db.PedometerHistoryDao> r0 = r0.mPedometerHistoryDaoList     // Catch:{ SQLException -> 0x0276 }
            r17 = r0
            r0 = r17
            r1 = r16
            java.lang.Object r2 = r0.get(r1)     // Catch:{ SQLException -> 0x0276 }
            com.contec.phms.db.PedometerHistoryDao r2 = (com.contec.phms.db.PedometerHistoryDao) r2     // Catch:{ SQLException -> 0x0276 }
            int r17 = r2.getmDistance()     // Catch:{ SQLException -> 0x0276 }
            int r4 = r4 + r17
            int r17 = r2.getmStep()     // Catch:{ SQLException -> 0x0276 }
            int r10 = r10 + r17
            int r17 = r2.getmSecond()     // Catch:{ SQLException -> 0x0276 }
            int r11 = r11 + r17
            int r16 = r16 + 1
            goto L_0x00dd
        L_0x0276:
            r15 = move-exception
            r15.printStackTrace()
            goto L_0x00ed
        L_0x027c:
            java.lang.String r17 = com.contec.phms.util.Constants.Language
            java.lang.String r18 = "1"
            boolean r17 = r17.contains(r18)
            if (r17 == 0) goto L_0x02e1
            java.util.Locale r8 = java.util.Locale.getDefault()
            java.lang.String r6 = r8.getLanguage()
            java.lang.String r17 = "en"
            r0 = r17
            boolean r17 = r6.equals(r0)
            if (r17 == 0) goto L_0x015c
            java.util.Calendar r13 = java.util.Calendar.getInstance()
            java.util.Date r17 = r13.getTime()
            java.lang.String r3 = r17.toString()
            java.lang.StringBuilder r17 = new java.lang.StringBuilder
            r18 = 0
            r19 = 10
            r0 = r18
            r1 = r19
            java.lang.String r18 = r3.substring(r0, r1)
            java.lang.String r18 = java.lang.String.valueOf(r18)
            r17.<init>(r18)
            java.lang.String r18 = " "
            java.lang.StringBuilder r17 = r17.append(r18)
            int r18 = r3.length()
            int r18 = r18 + -4
            int r19 = r3.length()
            r0 = r18
            r1 = r19
            java.lang.String r18 = r3.substring(r0, r1)
            java.lang.StringBuilder r17 = r17.append(r18)
            java.lang.String r17 = r17.toString()
            r0 = r17
            r1 = r20
            r1.mDateStr = r0
            goto L_0x015c
        L_0x02e1:
            java.lang.String r17 = com.contec.phms.util.Constants.Language
            if (r17 == 0) goto L_0x02ef
            java.lang.String r17 = com.contec.phms.util.Constants.Language
            java.lang.String r18 = ""
            boolean r17 = r17.equals(r18)
            if (r17 == 0) goto L_0x034a
        L_0x02ef:
            java.util.Locale r8 = java.util.Locale.getDefault()
            java.lang.String r6 = r8.getLanguage()
            java.lang.String r17 = "en"
            r0 = r17
            boolean r17 = r6.equals(r0)
            if (r17 == 0) goto L_0x015c
            java.util.Calendar r13 = java.util.Calendar.getInstance()
            java.util.Date r17 = r13.getTime()
            java.lang.String r3 = r17.toString()
            java.lang.StringBuilder r17 = new java.lang.StringBuilder
            r18 = 0
            r19 = 10
            r0 = r18
            r1 = r19
            java.lang.String r18 = r3.substring(r0, r1)
            java.lang.String r18 = java.lang.String.valueOf(r18)
            r17.<init>(r18)
            java.lang.String r18 = " "
            java.lang.StringBuilder r17 = r17.append(r18)
            int r18 = r3.length()
            int r18 = r18 + -4
            int r19 = r3.length()
            r0 = r18
            r1 = r19
            java.lang.String r18 = r3.substring(r0, r1)
            java.lang.StringBuilder r17 = r17.append(r18)
            java.lang.String r17 = r17.toString()
            r0 = r17
            r1 = r20
            r1.mDateStr = r0
            goto L_0x015c
        L_0x034a:
            java.lang.String r17 = com.contec.phms.util.Constants.Language
            java.lang.String r18 = "zh"
            boolean r17 = r17.equals(r18)
            if (r17 == 0) goto L_0x015c
            java.util.Locale r8 = java.util.Locale.getDefault()
            java.lang.String r6 = r8.getLanguage()
            java.lang.String r17 = "en"
            r0 = r17
            boolean r17 = r6.equals(r0)
            if (r17 == 0) goto L_0x015c
            java.text.SimpleDateFormat r5 = new java.text.SimpleDateFormat
            java.lang.String r17 = "yyyy年MM月dd日"
            r0 = r17
            r5.<init>(r0)
            long r17 = java.lang.System.currentTimeMillis()
            java.lang.Long r17 = java.lang.Long.valueOf(r17)
            r0 = r17
            java.lang.String r17 = r5.format(r0)
            r0 = r17
            r1 = r20
            r1.mDateStr = r0
            java.lang.String r12 = r20.getweek()
            r0 = r20
            java.lang.String r0 = r0.mDateStr
            r17 = r0
            java.lang.StringBuilder r18 = new java.lang.StringBuilder
            java.lang.String r17 = java.lang.String.valueOf(r17)
            r0 = r18
            r1 = r17
            r0.<init>(r1)
            r0 = r18
            java.lang.StringBuilder r17 = r0.append(r12)
            java.lang.String r17 = r17.toString()
            r0 = r17
            r1 = r20
            r1.mDateStr = r0
            goto L_0x015c
        L_0x03ac:
            r17 = 0
            r0 = r17
            r1 = r20
            r1.mStepsSumInt = r0     // Catch:{ SQLException -> 0x03be }
            r17 = 0
            r0 = r17
            r1 = r20
            r1.mDistanceSumTodayInt = r0     // Catch:{ SQLException -> 0x03be }
            goto L_0x0225
        L_0x03be:
            r14 = move-exception
            r14.printStackTrace()
            goto L_0x0225
        */
        throw new UnsupportedOperationException("Method not decompiled: com.contec.phms.fragment.Fragmentdevicelist.initData():void");
    }

    private String getweek() {
        String _week = new SimpleDateFormat("E").format(Long.valueOf(System.currentTimeMillis()));
        if (_week.equals("Fri")) {
            return " 周五";
        }
        if (_week.equals("Thu")) {
            return " 周四";
        }
        if (_week.equals("Wed")) {
            return " 周三";
        }
        if (_week.equals("Tue")) {
            return " 周二";
        }
        if (_week.equals("Mon")) {
            return " 周一";
        }
        if (_week.equals("Sat")) {
            return " 周六";
        }
        if (_week.equals("Sun")) {
            return " 周日";
        }
        return _week;
    }

    String floatFormat(String pattern, float value) {
        return new DecimalFormat(pattern).format((double) value);
    }

    void initHandler() {
        this.mHandler = new Handler(new Handler.Callback() {
            public boolean handleMessage(Message msg) {
                switch (msg.what) {
                    case 0:
                        Fragmentdevicelist.this.mpebometer_TimeHaveGo.setText(Fragmentdevicelist.this.dateFormat(((Integer) msg.obj).intValue()));
                        break;
                    case 1:
                        Fragmentdevicelist.this.setpedometerspeedvalue(((Float) msg.obj).floatValue());
                        break;
                    case 5:
                        Fragmentdevicelist.this.initCallBack();
                        Fragmentdevicelist.this.initData();
                        Fragmentdevicelist.this.updateView();
                        Fragmentdevicelist.this.mpebometer_handler_StepsTextSmall.setText(new StringBuilder(String.valueOf(Fragmentdevicelist.this.mStepsSumInt + Fragmentdevicelist.this.mStepsInt)).toString());
                        Fragmentdevicelist.this.mpedometer_handler_stepaverage_progesss.setProgress(((Fragmentdevicelist.this.mStepsSumInt + Fragmentdevicelist.this.mStepsInt) * 100) / Fragmentdevicelist.this.mStepTarget);
                        Fragmentdevicelist.this.mBallProgressView.setmSteps(Fragmentdevicelist.this.mStepsSumInt + Fragmentdevicelist.this.mStepsInt);
                        Fragmentdevicelist.this.mBallProgressView.invalidate();
                        break;
                    case 102:
                        if (Fragmentdevicelist.this.mService != null) {
                            Fragmentdevicelist.this.mStepsInt = (int) Fragmentdevicelist.this.mService.mSteps;
                            CLog.e(Fragmentdevicelist.TAG, "===================mStepsInt:" + Fragmentdevicelist.this.mStepsInt);
                        }
                        Fragmentdevicelist.this.initCallBack();
                        Fragmentdevicelist.this.initData();
                        Fragmentdevicelist.this.updateView();
                        Fragmentdevicelist.this.initrunvalue();
                        break;
                    case Fragmentdevicelist.SEARCH_DEVICE /*50001*/:
                        CLog.i("jxx", "代码执行到这里摧毁轮询服务1");
                        Fragmentdevicelist.this.stopServer();
                        Fragmentdevicelist.this.mHandler.sendEmptyMessage(Fragmentdevicelist.BLE_SEARCH_END);
                        final SerchedDevice device = (SerchedDevice) msg.obj;
                        if (Fragmentdevicelist.this.searchDialog == null) {
                            Fragmentdevicelist.this.searchDialog = new DialogClass(Fragmentdevicelist.this.mcontext, device.getDevice_image(), device.getDevice_name(), (View.OnClickListener) new View.OnClickListener() {
                                public void onClick(View v) {
                                    Fragmentdevicelist.this.searchDialog.dismiss();
                                    Fragmentdevicelist.this.saveDevice(Fragmentdevicelist.this.mcontext, device.getmDeviceName(), device.getmMac(), device.getmCode(), device.getmBluetoothType(), device.getScanRecord());
                                    Constants.ISFROMSERACHDEVICE = false;
                                    Log.i("jxx", "f集合的大小：" + App_phms.getInstance().getmWaitConnectDeviceBeanLists().size());
                                    Fragmentdevicelist.this.ifFindNewDevice = false;
                                    Fragmentdevicelist.this.mHandler.removeMessages(Fragmentdevicelist.SEARCH_END_NO_DEVICE);
                                    Fragmentdevicelist.this.startPollServer();
                                }
                            }, (View.OnClickListener) new View.OnClickListener() {
                                public void onClick(View v) {
                                    Fragmentdevicelist.this.searchDialog.dismiss();
                                    Fragmentdevicelist.this.ifFindNewDevice = false;
                                    Fragmentdevicelist.this.mHandler.removeMessages(Fragmentdevicelist.SEARCH_END_NO_DEVICE);
                                    Fragmentdevicelist.this.startPollServer();
                                    Fragmentdevicelist.this.ui_DeviceManage(Fragmentdevicelist.this.mdevicelist_add_devices, true);
                                }
                            });
                            break;
                        }
                        break;
                    case Fragmentdevicelist.BLE_SEARCH_END /*50002*/:
                        Fragmentdevicelist.this.loadScuess();
                        if (!Fragmentdevicelist.this.onPause) {
                            if (!Fragmentdevicelist.this.mScanning) {
                                if (Fragmentdevicelist.this.mClassicSacn) {
                                    Fragmentdevicelist.this.mClassicSacn = false;
                                    Fragmentdevicelist.this.mBluetoothAdapter.cancelDiscovery();
                                    break;
                                }
                            } else {
                                Fragmentdevicelist.this.mScanning = false;
                                Fragmentdevicelist.this.mBluetoothAdapter.stopLeScan(Fragmentdevicelist.this.mBleScan.mLeScanCallback);
                                break;
                            }
                        }
                        break;
                    case Fragmentdevicelist.SEARCH_END_NO_DEVICE /*50003*/:
                        Fragmentdevicelist.this.ui_DeviceManage(Fragmentdevicelist.this.mdevicelist_add_devices, true);
                        Fragmentdevicelist.this.loadScuess();
                        if (Fragmentdevicelist.this.mScanning) {
                            Fragmentdevicelist.this.mScanning = false;
                            Fragmentdevicelist.this.mBluetoothAdapter.stopLeScan(Fragmentdevicelist.this.mBleScan.mLeScanCallback);
                            Toast.makeText(Fragmentdevicelist.this.getActivity().getApplicationContext(), Fragmentdevicelist.this.getActivity().getString(R.string.str_search_no_device), Toast.LENGTH_LONG).show();
                        } else if (Fragmentdevicelist.this.mClassicSacn) {
                            Fragmentdevicelist.this.mClassicSacn = false;
                            Fragmentdevicelist.this.mBluetoothAdapter.cancelDiscovery();
                            Toast.makeText(Fragmentdevicelist.this.getActivity().getApplicationContext(), Fragmentdevicelist.this.getActivity().getString(R.string.str_search_no_device), Toast.LENGTH_LONG).show();
                        }
                        if (!Fragmentdevicelist.this.ifFindNewDevice) {
                            Fragmentdevicelist.this.startPollServer();
                            break;
                        }
                        break;
                    case Fragmentdevicelist.PHONE_NO_SUPPORT_BLE /*50004*/:
                        Fragmentdevicelist.this.startPollServer();
                        Fragmentdevicelist.this.loadScuess();
                        Toast.makeText(Fragmentdevicelist.this.getActivity().getApplicationContext(), Fragmentdevicelist.this.getActivity().getString(R.string.str_serchdevice_dialog), Toast.LENGTH_LONG).show();
                        break;
                    case Fragmentdevicelist.PHONE_NO_OPEN_BLE /*50005*/:
                        Fragmentdevicelist.this.mHandler.removeMessages(Fragmentdevicelist.SEARCH_END_NO_DEVICE);
                        Fragmentdevicelist.this.loadScuess();
                        Toast.makeText(Fragmentdevicelist.this.getActivity().getApplicationContext(), Fragmentdevicelist.this.getActivity().getString(R.string.str_search_open_ble), Toast.LENGTH_LONG).show();
                        Fragmentdevicelist.this.startPollServer();
                        break;
                    case 65536:
                        Fragmentdevicelist.this.loginsuccess();
                        TimerUtil.getinstance().startTimer();
                        CLog.d(Fragmentdevicelist.TAG, "登陆成功  重启 消息 service");
                        InstantMessageService.stopServer(Fragmentdevicelist.this.getActivity());
                        Fragmentdevicelist.this.getActivity().startService(new Intent(Fragmentdevicelist.this.getActivity(), InstantMessageService.class));
                        break;
                    case Constants.LOGINFAILD /*65537*/:
                        Fragmentdevicelist.this.loginfaild(Fragmentdevicelist.this.getActivity().getResources().getString(R.string.str_num_passwd_error));
                        break;
                    case Constants.mloginargserror /*100201*/:
                        Fragmentdevicelist.this.loginfaild(Fragmentdevicelist.this.getActivity().getResources().getString(R.string.loginargserror));
                        break;
                    case Constants.mlogindberror /*100202*/:
                        Fragmentdevicelist.this.loginfaild(Fragmentdevicelist.this.getActivity().getResources().getString(R.string.logindberror));
                        break;
                    case Constants.mloginduserisnotexit /*100203*/:
                        Fragmentdevicelist.this.loginfaild(Fragmentdevicelist.this.getActivity().getResources().getString(R.string.loginuserisnotexit));
                        break;
                    case Constants.mlogindusernotcorrectpsw /*100204*/:
                        Fragmentdevicelist.this.loginfaild(Fragmentdevicelist.this.getActivity().getResources().getString(R.string.loginpswnotcorrect));
                        break;
                    case Constants.mloginduserstopuser /*100205*/:
                        Fragmentdevicelist.this.loginfaild(Fragmentdevicelist.this.getActivity().getResources().getString(R.string.loginstopuser));
                        break;
                    case Constants.mqueryuserinfotimeout /*104011*/:
                        Fragmentdevicelist.this.loginfaild(Fragmentdevicelist.this.getActivity().getResources().getString(R.string.user_networktimeout));
                        break;
                    case Constants.mqueryuserinfoneterror /*104012*/:
                        Fragmentdevicelist.this.loginfaild(Fragmentdevicelist.this.getActivity().getResources().getString(R.string.user_networkerror));
                        break;
                }
                return false;
            }
        });
    }

    private void relogin() {
        try {
            List<UserInfoDao> _user = App_phms.getInstance().mHelper.getUserDao().queryForEq(UserInfoDao.UserId, this.mPedometerSharepreferance.getUserID());
            if (_user == null || _user.size() <= 0) {
                getActivity().finish();
                return;
            }
            this.mloginuserid = _user.get(0).getmUserId();
            this.mloginuserpwd = _user.get(0).getPsw();
            this.mlogindialogClass = new DialogClass(this.mcontext, this.mcontext.getResources().getString(R.string.login_logining), false, 0, this.mCancelLogin);
            new LoginMethod(getActivity(), this.mloginuserid, this.mloginuserpwd, this.mHandler).login();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void reloginBackGround() {
        LoginUserDao _user = PageUtil.getLoginUserInfo();
        if (_user != null) {
            try {
                this.mloginuserid = _user.mUID;
                this.mloginuserpwd = App_phms.getInstance().mHelper.getUserDao().queryForEq(UserInfoDao.UserId, this.mloginuserid).get(0).getPsw();
                new LoginMethod(getActivity(), this.mloginuserid, this.mloginuserpwd, this.mHandler).login();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            App_phms.getInstance().exitall(0);
            new Thread() {
                public void run() {
                    try {
                        Thread.sleep(1000);
                        Intent i = Fragmentdevicelist.this.getActivity().getPackageManager().getLaunchIntentForPackage(Fragmentdevicelist.this.getActivity().getPackageName());
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        Fragmentdevicelist.this.getActivity().startActivity(i);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }.start();
        }
    }

    private void reloginAnother() {
        LoginUserDao _user = PageUtil.getLoginUserInfo();
        if (_user != null) {
            try {
                this.mloginuserid = _user.mUID;
                this.mloginuserpwd = App_phms.getInstance().mHelper.getUserDao().queryForEq(UserInfoDao.UserId, this.mloginuserid).get(0).getPsw();
                this.mlogindialogClass = new DialogClass(this.mcontext, this.mcontext.getResources().getString(R.string.login_logining), false, 0, this.mCancelLogin);
                new LoginMethod(getActivity(), this.mloginuserid, this.mloginuserpwd, this.mHandler).login();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            App_phms.getInstance().exitall(0);
            new Thread() {
                public void run() {
                    try {
                        Thread.sleep(1000);
                        Intent i = Fragmentdevicelist.this.getActivity().getPackageManager().getLaunchIntentForPackage(Fragmentdevicelist.this.getActivity().getPackageName());
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        Fragmentdevicelist.this.getActivity().startActivity(i);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }.start();
        }
    }

    private void loginfaild(String pstr) {
        if (this.mlogindialogClass != null) {
            this.mlogindialogClass.dismiss();
        }
        new DialogClass(getActivity(), pstr);
        new Thread() {
            public void run() {
                try {
                    Thread.sleep(1000);
                    App_phms.getInstance().exitall(0);
                    Thread.sleep(1000);
                    Intent i = App_phms.getInstance().getApplicationContext().getPackageManager().getLaunchIntentForPackage(App_phms.getInstance().getApplicationContext().getPackageName());
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    App_phms.getInstance().getApplicationContext().startActivity(i);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    private void loginsuccess() {
        PageUtil.saveUserNameTOSharePre(getActivity(), this.mloginuserid);
        LoginUserDao _loginUserInfo = PageUtil.getLoginUserInfo();
        UserInfoDao user = new UserInfoDao();
        user.setmUserId(this.mloginuserid);
        user.setPsw(this.mloginuserpwd);
        user.setSavingMode(true);
        user.setLastLoginData(new Date());
        user.setmSex(_loginUserInfo.mSex);
        user.setmUserName(_loginUserInfo.mUserName);
        try {
            Dao<UserInfoDao, String> userDao = App_phms.getInstance().mHelper.getUserDao();
            UserInfoDao retUser = userDao.queryForId(this.mloginuserid);
            if (retUser == null) {
                App_phms.getInstance().mUserInfo.mSearchInterval = 10;
                App_phms.getInstance().mUserInfo.mBluetoothState = 1;
                App_phms.getInstance().mUserInfo.mLanguage = "1zh";
                Constants.Language = App_phms.getInstance().mUserInfo.mLanguage;
                user.setmSearchInterval(10);
                user.setmLanguage("1zh");
                user.setmBluetoothState(1);
                userDao.create(user);
            } else {
                App_phms.getInstance().mUserInfo.mSearchInterval = retUser.getmSearchInterval();
                App_phms.getInstance().mUserInfo.mBluetoothState = retUser.getBluetoothstate();
                App_phms.getInstance().mUserInfo.mLanguage = retUser.getmLanguage();
                Constants.Language = App_phms.getInstance().mUserInfo.mLanguage;
                retUser.setmSex(App_phms.getInstance().mUserInfo.mSex);
                retUser.setmUserName(App_phms.getInstance().mUserInfo.mUserName);
                Constants.Language = App_phms.getInstance().mUserInfo.mLanguage;
                retUser.setLastLoginData(new Date());
                userDao.update(retUser);
            }
            PageUtil.getUserInfo();
            if (Constants.Language.contains("1")) {
                String _language = Locale.getDefault().getLanguage();
                Configuration config2 = this.mcontext.getResources().getConfiguration();
                DisplayMetrics dm2 = this.mcontext.getResources().getDisplayMetrics();
                if (_language.contains("zh")) {
                    config2.locale = Locale.CHINESE;
                } else if (_language.contains("en")) {
                    config2.locale = Locale.ENGLISH;
                }
                this.mcontext.getResources().updateConfiguration(config2, dm2);
            } else if (Constants.Language.equals("en")) {
                Configuration config1 = this.mcontext.getResources().getConfiguration();
                DisplayMetrics dm1 = this.mcontext.getResources().getDisplayMetrics();
                config1.locale = Locale.ENGLISH;
                this.mcontext.getResources().updateConfiguration(config1, dm1);
            } else if (Constants.Language.equals("zh")) {
                Configuration config = this.mcontext.getResources().getConfiguration();
                DisplayMetrics dm = this.mcontext.getResources().getDisplayMetrics();
                config.locale = Locale.CHINESE;
                this.mcontext.getResources().updateConfiguration(config, dm);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        SharedPreferences.Editor _editor = App_phms.getInstance().mCurrentloginUserInfo.edit();
        _editor.putString("username", this.mloginuserid);
        _editor.putString("password", this.mloginuserpwd);
        _editor.commit();
        PhmsSharedPreferences.getInstance(this.mcontext).saveColume("username", this.mloginuserid);
        try {
            Thread.sleep(400);
        } catch (InterruptedException e2) {
            e2.printStackTrace();
        }
        if (this.mlogindialogClass != null) {
            this.mlogindialogClass.dismiss();
        }
    }

    String dateFormat(int seconds) {
        Date _date = new Date(0, 0, 0, 0, 0, seconds);
        int _day = _date.getDay();
        int _hours = _date.getHours();
        if (_day != 0) {
            _hours += _day * 24;
        }
        return String.format("%02d:%02d:%02d", new Object[]{Integer.valueOf(_hours), Integer.valueOf(_date.getMinutes()), Integer.valueOf(_date.getSeconds())});
    }

    private void updateView() {
        setlayoutCaloriesValuse(this.mCalories);
        setpebometer_Distancetextviewvalue(Float.valueOf(this.mDistance));
        this.mpebometer_handler_StepsTextSmall.setText(new StringBuilder(String.valueOf(this.mStepsSumInt)).toString());
        this.mpedometer_handler_stepaverage_progesss.setProgress(((this.mStepsSumInt + this.mStepsInt) * 100) / this.mStepTarget);
        this.mpebometer_TimeHaveGo.setText(dateFormat(this.mTimeInt));
        setpedometerspeedvalue(this.mAverageSpeed);
        if (this.mService == null || !this.mService.getPause()) {
            this.mpebometer_puse_btn.setText(R.string.str_pedometer_puse);
            this.mPedometerSharepreferance.getBackPedometer();
        } else {
            this.mpebometer_puse_btn.setText(R.string.str_pedometer_start);
        }
        this.mBallProgressView.setmSteps(this.mStepsSumInt + this.mStepsInt);
        this.mBallProgressView.invalidate();
    }

    private void setHandlerlayoutCaloriesValuse(float pvalue) {
        if (Float.valueOf(floatFormat("#0.00", pvalue)).floatValue() < 1000.0f) {
            this.mpebometer_handler_calorie_Text.setText(String.valueOf(floatFormat("#0.00", pvalue)) + this.mcontext.getString(R.string.pebometer_calorie));
        } else {
            this.mpebometer_handler_calorie_Text.setText(String.valueOf(floatFormat("#0.00", pvalue / 1000.0f)) + this.mcontext.getString(R.string.pebometer_kilocalorie));
        }
    }

    private void setlayoutCaloriesValuse(float pvalue) {
        String _cal;
        float kilomCalorie = pvalue / 1000.0f;
        if (Float.valueOf(floatFormat("#0.00", pvalue)).floatValue() < 1000.0f) {
            _cal = floatFormat("#0.000", kilomCalorie);
        } else {
            _cal = floatFormat("#0.0", kilomCalorie);
        }
        this.mBallProgressView.setmCal(Float.parseFloat(_cal));
    }

    void registerScreenReceiver() {
        this.misregistscreenreceiver = true;
        this.mcontext.registerReceiver(this.mScreenReceiver, new IntentFilter("android.intent.action.SCREEN_OFF"));
    }

    void unRegisterScreenReceiver() {
        if (this.mReceiver != null && this.misregistscreenreceiver) {
            this.misregistscreenreceiver = false;
            this.mcontext.unregisterReceiver(this.mScreenReceiver);
        }
    }

    void bindrunService() {
        this.misunbindservice = true;
        CLog.e(TAG, "SERVICE bind");
        this.mServiceIsBind = true;
        this.mcontext.bindService(new Intent(this.mcontext, PedometerService.class), this.mConnection, 3);
    }

    void unbindrunService() {
        CLog.e(TAG, "SERVICE Unbind");
        if (this.mService != null && this.mServiceIsBind && this.misunbindservice) {
            this.misunbindservice = false;
            this.mcontext.unbindService(this.mConnection);
            this.mServiceIsBind = false;
        }
    }

    void startrunService() {
        CLog.e(TAG, "SERVICE startService");
        Intent _intent = new Intent(this.mcontext, PedometerService.class);
        _intent.putExtra(Constants.KEY_IS_VIS_HISTORY, true);
        this.mcontext.startService(_intent);
    }

    void stoprunService() {
        if (this.mService != null) {
            CLog.e(TAG, "SERVICE stopService");
            this.mcontext.stopService(new Intent(this.mcontext, PedometerService.class));
        }
    }

    private void setpedometerspeedvalue(float value) {
        if (this.mUnit == 1) {
            this.mpebometer_Speed_Text.setText(floatFormat("#0.00", value));
            return;
        }
        float f = value * 1.8939393E-4f;
        this.mpebometer_Speed_Text.setText(floatFormat("#0.00", value));
    }

    private void sethandlerDistancetextviewvalue(int value) {
        if (this.mUnit == 1) {
            float _temp = (float) value;
            if (_temp >= 1000.0f) {
                this.mpebometer_handler_Distance_Text.setText(String.valueOf(floatFormat("#0.00", _temp * 0.001f)) + this.mcontext.getResources().getString(R.string.pebometer_kilometer));
            } else {
                this.mpebometer_handler_Distance_Text.setText(String.valueOf(floatFormat("#0", _temp)) + this.mcontext.getResources().getString(R.string.pebometer_meter));
            }
        } else {
            float _temp2 = (((float) this.mDistanceSumTodayInt) * 3.2808f) + ((float) value);
            if (_temp2 >= 1000.0f) {
                this.mpebometer_handler_Distance_Text.setText(String.valueOf(floatFormat("#0.00", _temp2 * 1.8939393E-4f)) + this.mcontext.getResources().getString(R.string.pedometer_distance_kin));
            } else {
                this.mpebometer_handler_Distance_Text.setText(String.valueOf(floatFormat("#0", _temp2)) + this.mcontext.getResources().getString(R.string.pedometer_speed_in));
            }
        }
    }

    private void setpebometer_Distancetextviewvalue(Float value) {
        if (this.mUnit == 1) {
            if (value.floatValue() >= 1000.0f) {
                this.mpebometer_Distance_Text.setText(floatFormat("#0", value.floatValue() * 0.001f));
            } else {
                this.mpebometer_Distance_Text.setText(floatFormat("#0", value.floatValue()));
            }
            float _temp = ((float) this.mDistanceSumTodayInt) + value.floatValue();
            if (_temp >= 1000.0f) {
                this.mpebometer_handler_Distance_Text.setText(String.valueOf(floatFormat("#0.00", (((float) this.mDistanceSumTodayInt) + _temp) * 0.001f)) + this.mcontext.getResources().getString(R.string.pebometer_kilometer));
                return;
            }
            this.mpebometer_handler_Distance_Text.setText(String.valueOf(floatFormat("#0", _temp)) + this.mcontext.getResources().getString(R.string.pebometer_meter));
            return;
        }
        if (value.floatValue() >= 1000.0f) {
            this.mpebometer_Distance_Text.setText(String.valueOf(floatFormat("#0", value.floatValue() * 1.8939393E-4f)) + this.mcontext.getResources().getString(R.string.pedometer_distance_kin));
        } else {
            this.mpebometer_Distance_Text.setText(String.valueOf(floatFormat("#0", value.floatValue())) + this.mcontext.getResources().getString(R.string.pedometer_speed_in));
        }
        float _temp2 = (((float) this.mDistanceSumTodayInt) * 3.2808f) + value.floatValue();
        if (_temp2 >= 1000.0f) {
            this.mpebometer_handler_Distance_Text.setText(String.valueOf(floatFormat("#0.00", _temp2 * 1.8939393E-4f)) + this.mcontext.getResources().getString(R.string.pedometer_distance_kin));
        } else {
            this.mpebometer_handler_Distance_Text.setText(String.valueOf(floatFormat("#0", _temp2)) + this.mcontext.getResources().getString(R.string.pedometer_speed_in));
        }
    }

    protected void onResumeinit() {
        float _mDistance;
        this.mPedometerSharepreferance = new PedometerSharepreferance(this.mcontext);
        this.mStepTarget = this.mPedometerSharepreferance.getTarget();
        CLog.e(TAG, "onResume()");
        if (this.mService == null) {
            bindrunService();
            startrunService();
            CLog.e(TAG, "启动service******************");
        } else if (this.mScreenOff) {
            this.mScreenOff = false;
        } else {
            this.mService.reSetShare();
            this.mUnit = this.mService.getUnit();
            CLog.e(TAG, "mUnit= " + this.mUnit + " mUnitLast= " + this.mUnitLast);
            if (this.mUnit != this.mUnitLast) {
                CLog.e(TAG, "mDistance= " + this.mDistance);
                if (this.mUnit == 1) {
                    this.mDistance *= 0.3048f;
                    if (this.mDistance >= 1000.0f) {
                        _mDistance = this.mDistance * 0.001f;
                    } else {
                        _mDistance = this.mDistance;
                    }
                } else {
                    this.mDistance *= 3.2808f;
                    if (this.mDistance >= 1000.0f) {
                        _mDistance = this.mDistance * 1.8939393E-4f;
                    } else {
                        _mDistance = this.mDistance;
                    }
                }
                CLog.e(TAG, "mDistance= " + this.mDistance + " mDistance/mTimeInt=" + (this.mDistance / ((float) this.mTimeInt)) + " mDistanceSumTodayInt=" + this.mDistanceSumTodayInt);
                PedometerService pedometerService = this.mService;
                float f = this.mDistance;
                float f2 = this.mDistance / ((float) this.mTimeInt);
                this.mAverageSpeed = f2;
                pedometerService.setDistance(f, f2);
                setpebometer_Distancetextviewvalue(Float.valueOf(_mDistance));
                setpedometerspeedvalue(this.mAverageSpeed);
            }
            if (this.mUnit == 1) {
                this.mSpeedsText.setText(R.string.str_pedometer_sport_speed);
                if (this.mDistanceSumTodayInt > 1000) {
                    this.mpebometer_handler_Distance_Text.setText(String.valueOf(floatFormat("#0.00", ((float) this.mDistanceSumTodayInt) * 0.001f)) + getString(R.string.pebometer_kilometer));
                } else {
                    this.mpebometer_handler_Distance_Text.setText(String.valueOf(floatFormat("#0", ((float) this.mDistanceSumTodayInt) * 3.2808f)) + getString(R.string.pebometer_meter));
                }
            } else {
                this.mSpeedsText.setText(R.string.str_pedometer_sport_speed);
                if (((float) this.mDistanceSumTodayInt) * 3.2808f > 1000.0f) {
                    this.mpebometer_handler_Distance_Text.setText(String.valueOf(floatFormat("#0.00", ((float) this.mDistanceSumTodayInt) * 3.2808f * 1.8939393E-4f)) + getString(R.string.pedometer_distance_kin));
                } else {
                    this.mpebometer_handler_Distance_Text.setText(String.valueOf(floatFormat("#0", ((float) this.mDistanceSumTodayInt) * 3.2808f)) + getString(R.string.pedometer_speed_in));
                }
            }
        }
        new Thread() {
            public void run() {
                try {
                    Thread.sleep(300);
                    Fragmentdevicelist.this.mHandler.sendEmptyMessage(5);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    private void upLoadPedemeterData() {
        this.mList = new ArrayList();
        this.mUserID = App_phms.getInstance().mUserInfo.mUserID;
        try {
            List<PedometerHistoryDao> _list = App_phms.getInstance().mHelper.getPedometerhistoryDao().queryBuilder().where().eq("UserID", this.mUserID).query();
            this.mList = App_phms.getInstance().mHelper.getPedometerhistoryDao().queryBuilder().where().eq("UserID", bs.b).and().eq(PedometerHistoryDao.IsUploaded, "2").query();
            if (_list != null && _list.size() > 0) {
                this.mList.addAll(_list);
            }
            if (this.mList != null && this.mList.size() > 0) {
                this.mcommitpedometerdatadialog = new DialogClass((Context) getActivity(), getString(R.string.str_upload_case), false, 0, this.mcommitpedemeterdatalistener);
                new Thread() {
                    public void run() {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        new Pedometer_Trend(Fragmentdevicelist.this.getActivity(), Fragmentdevicelist.this.mList);
                    }
                }.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onEvent(EventFragment event) {
        if (event.getmWhichCommand() == 2) {
            int mProgress = event.getmProgress();
            if (mProgress == 100) {
                cancleDialog(mProgress);
            } else if (mProgress == 110) {
                cancleDialog(mProgress);
            }
        }
    }

    private void cancleDialog(int sufa) {
        this.mcommitpedometerdatadialog.dismiss();
        if (sufa == 100) {
            new DialogClass(getActivity(), getResources().getString(R.string.str_upload_success));
        } else if (sufa == 110 || sufa == 0) {
            new DialogClass(getActivity(), getResources().getString(R.string.str_upload_failed));
        }
        Message msg = new Message();
        msg.what = Constants.V_PEDOMETER_NO_UPLOAD_DATA;
        msg.arg2 = 1;
        App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(msg);
    }

    public void onDestroy() {
        super.onDestroy();
        unbindrunService();
        unRegisterScreenReceiver();
        App_phms.getInstance().mEventBus.unregister(this);
    }

    private void setEnableEnterDeviceManager(int p_state, Button _imagebtn_startmanagerdevice) {
        CLog.i("btntext", "现在设备的状态是    p_state  =  " + p_state);
        switch (p_state) {
            case 0:
                if (DeviceManager.m_DeviceBean == null || DeviceManager.m_DeviceBean.mDeviceName == null || !DeviceManager.m_DeviceBean.mDeviceName.contains(Constants.DEVICE_8000GW_NAME)) {
                    _imagebtn_startmanagerdevice.setClickable(false);
                    _imagebtn_startmanagerdevice.setTextColor(getResources().getColor(R.color.gray1));
                    return;
                }
                _imagebtn_startmanagerdevice.setClickable(true);
                _imagebtn_startmanagerdevice.setTextColor(getResources().getColorStateList(R.drawable.drawable_blackwhite_bg));
                return;
            case 1:
                _imagebtn_startmanagerdevice.setClickable(false);
                _imagebtn_startmanagerdevice.setTextColor(getResources().getColor(R.color.gray1));
                return;
            case 2:
                _imagebtn_startmanagerdevice.setClickable(false);
                _imagebtn_startmanagerdevice.setTextColor(getResources().getColor(R.color.gray1));
                return;
            case 3:
                _imagebtn_startmanagerdevice.setClickable(true);
                _imagebtn_startmanagerdevice.setTextColor(getResources().getColorStateList(R.drawable.drawable_blackwhite_bg));
                return;
            case 4:
                _imagebtn_startmanagerdevice.setClickable(false);
                _imagebtn_startmanagerdevice.setTextColor(getResources().getColor(R.color.gray1));
                return;
            case 5:
                _imagebtn_startmanagerdevice.setClickable(true);
                _imagebtn_startmanagerdevice.setTextColor(getResources().getColorStateList(R.drawable.drawable_blackwhite_bg));
                return;
            case 6:
                _imagebtn_startmanagerdevice.setClickable(false);
                _imagebtn_startmanagerdevice.setTextColor(getResources().getColor(R.color.gray1));
                return;
            case 7:
                _imagebtn_startmanagerdevice.setClickable(false);
                _imagebtn_startmanagerdevice.setTextColor(getResources().getColor(R.color.gray1));
                return;
            case 8:
                _imagebtn_startmanagerdevice.setClickable(true);
                _imagebtn_startmanagerdevice.setTextColor(getResources().getColorStateList(R.drawable.drawable_blackwhite_bg));
                if (DeviceManager.m_DeviceBean != null) {
                    DeviceManager.m_DeviceBean.mState = 0;
                    return;
                }
                return;
            case 9:
                _imagebtn_startmanagerdevice.setClickable(true);
                _imagebtn_startmanagerdevice.setTextColor(getResources().getColorStateList(R.drawable.drawable_blackwhite_bg));
                return;
            case 10:
                _imagebtn_startmanagerdevice.setClickable(true);
                _imagebtn_startmanagerdevice.setTextColor(getResources().getColorStateList(R.drawable.drawable_blackwhite_bg));
                return;
            case 11:
                _imagebtn_startmanagerdevice.setClickable(true);
                _imagebtn_startmanagerdevice.setTextColor(getResources().getColorStateList(R.drawable.drawable_blackwhite_bg));
                return;
            case 12:
                _imagebtn_startmanagerdevice.setClickable(true);
                _imagebtn_startmanagerdevice.setTextColor(getResources().getColorStateList(R.drawable.drawable_blackwhite_bg));
                return;
            case 13:
                _imagebtn_startmanagerdevice.setClickable(true);
                _imagebtn_startmanagerdevice.setTextColor(getResources().getColorStateList(R.drawable.drawable_blackwhite_bg));
                return;
            case 14:
                _imagebtn_startmanagerdevice.setClickable(true);
                _imagebtn_startmanagerdevice.setTextColor(getResources().getColorStateList(R.drawable.drawable_blackwhite_bg));
                return;
            case 15:
                _imagebtn_startmanagerdevice.setClickable(true);
                _imagebtn_startmanagerdevice.setTextColor(getResources().getColorStateList(R.drawable.drawable_blackwhite_bg));
                return;
            case 16:
                _imagebtn_startmanagerdevice.setClickable(true);
                _imagebtn_startmanagerdevice.setTextColor(getResources().getColorStateList(R.drawable.drawable_blackwhite_bg));
                return;
            case 17:
                _imagebtn_startmanagerdevice.setClickable(true);
                _imagebtn_startmanagerdevice.setTextColor(getResources().getColorStateList(R.drawable.drawable_blackwhite_bg));
                return;
            case 18:
                _imagebtn_startmanagerdevice.setClickable(false);
                _imagebtn_startmanagerdevice.setTextColor(getResources().getColor(R.color.gray1));
                return;
            case 19:
                _imagebtn_startmanagerdevice.setClickable(false);
                _imagebtn_startmanagerdevice.setTextColor(getResources().getColor(R.color.gray1));
                return;
            case 20:
                _imagebtn_startmanagerdevice.setClickable(true);
                _imagebtn_startmanagerdevice.setTextColor(getResources().getColorStateList(R.drawable.drawable_blackwhite_bg));
                return;
            case 21:
                _imagebtn_startmanagerdevice.setClickable(true);
                _imagebtn_startmanagerdevice.setTextColor(getResources().getColorStateList(R.drawable.drawable_blackwhite_bg));
                return;
            case 24:
                _imagebtn_startmanagerdevice.setClickable(true);
                _imagebtn_startmanagerdevice.setTextColor(getResources().getColorStateList(R.drawable.drawable_blackwhite_bg));
                return;
            case 27:
                _imagebtn_startmanagerdevice.setClickable(true);
                _imagebtn_startmanagerdevice.setTextColor(getResources().getColorStateList(R.drawable.drawable_blackwhite_bg));
                return;
//            case Response.TYPE_MASK /*28*/:
//                _imagebtn_startmanagerdevice.setClickable(true);
//                _imagebtn_startmanagerdevice.setTextColor(getResources().getColorStateList(R.drawable.drawable_blackwhite_bg));
//                return;
            case MessageManagerMain.BluetoothLeDeviceService_cms50k:
                _imagebtn_startmanagerdevice.setClickable(false);
                _imagebtn_startmanagerdevice.setTextColor(getResources().getColor(R.color.gray1));
                return;
            case 31:
                _imagebtn_startmanagerdevice.setClickable(true);
                _imagebtn_startmanagerdevice.setTextColor(getResources().getColorStateList(R.drawable.drawable_blackwhite_bg));
                return;
            case 32:
                _imagebtn_startmanagerdevice.setClickable(true);
                _imagebtn_startmanagerdevice.setTextColor(getResources().getColorStateList(R.drawable.drawable_blackwhite_bg));
                return;
            case 33:
                _imagebtn_startmanagerdevice.setClickable(true);
                _imagebtn_startmanagerdevice.setTextColor(getResources().getColorStateList(R.drawable.drawable_blackwhite_bg));
                return;
            default:
                return;
        }
    }

    private void sendMessageToConnectNextDevice() {
        Message msgManager = new Message();
        msgManager.what = 21;
        msgManager.arg2 = 4;
        App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(msgManager);
    }

    public void onStop() {
        super.onStop();
    }

    private void uploadBluetooth(String action, String notes) {
        LoginUserDao loginUserInfo = PageUtil.getLoginUserInfo();
    }

    private String getNotesString(String type, String btdevice, String action, String duration) {
        BluetoothPhoneNotes notes = new BluetoothPhoneNotes();
        notes.setBrand(Build.MANUFACTURER);
        notes.setModel(Build.MODEL);
        notes.setBttype(type);
        notes.setBtdevice(btdevice);
        notes.setAction(action);
        notes.setDuration(duration);
        return JSON.toJSONString(notes);
    }

    public void onRefresh() {
        if (this.searchDialog != null) {
            this.searchDialog = null;
        }
        this.mTextPull.setVisibility(View.INVISIBLE);
        stopServer();
        Log.i("jxx", "代码执行到这里摧毁轮询服务1");
        ui_DeviceManage(this.mdevicelist_add_devices, false);
        registerScanBluetooth(this.mUsedBLEScan);
    }

    public void onLoadMore() {
    }

    /* access modifiers changed from: private */
    @SuppressLint({"SimpleDateFormat"})
    public void loadScuess() {
        this.mTextPull.setVisibility(View.VISIBLE);
        this.m_newDevicesListView_phone.stopRefresh();
        this.m_newDevicesListView_phone.setRefreshTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Long.valueOf(System.currentTimeMillis())));
    }

    public void registerScanBluetooth(boolean isBle) {
        if (!isBle) {
            LogI("使用传统蓝牙搜索设备");
            this.ISREGISTBLUETOOTHREVIER = true;
            this.mCheckDevice = false;
            CLog.i(TAG, "register ActivityDataSerchDevice receiver");
            IntentFilter filter = new IntentFilter("android.bluetooth.device.action.FOUND");
            filter.addAction("android.bluetooth.adapter.action.DISCOVERY_FINISHED");
            filter.addAction("android.bluetooth.adapter.action.STATE_CHANGED");
            getActivity().registerReceiver(this.mScanReceiver, filter);
            this.mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            if (this.mBluetoothAdapter.getState() == BluetoothAdapter.STATE_ON) {
                CLog.i(TAG, "判断是否是搜索状态：" + this.mBluetoothAdapter.isDiscovering());
                if (this.mBluetoothAdapter.isDiscovering()) {
                    this.mBluetoothAdapter.cancelDiscovery();
                }
                if (!this.mBluetoothAdapter.startDiscovery()) {
                    CLog.i(TAG, "startDiscovery failed, close bluetooth!");
                    this.mBluetoothAdapter.startDiscovery();
                }
                this.mClassicSacn = true;
                this.mHandler.sendEmptyMessageDelayed(SEARCH_END_NO_DEVICE, 10000);
            } else if (this.mBluetoothAdapter.getState() == BluetoothAdapter.STATE_OFF) {
                this.mBluetoothAdapter.enable();
            }
        } else if (!this.phoneHardWareIsSupportBLE || !this.phoneSystemVersionIsSupport) {
            registerScanBluetooth(false);
        } else if (PhmsSharedPreferences.getInstance(App_phms.getInstance().getApplicationContext()).getBoolean("ifOpenBLe", true)) {
            this.mBluetoothAdapter = ((BluetoothManager) getActivity().getSystemService(Context.BLUETOOTH_SERVICE)).getAdapter();
            if (this.mBluetoothAdapter.getState() == BluetoothAdapter.STATE_OFF) {
                this.mBluetoothAdapter.enable();
            }
            if (this.mBluetoothAdapter == null) {
                registerScanBluetooth(false);
                return;
            }
            LogI("使用BLE搜索设备");
            scanLeDevice(true);
        } else {
            registerScanBluetooth(false);
        }
    }

    private synchronized boolean checkDevice(BluetoothDevice device, String mBluetoothType, boolean isBle, byte[] scanRecord) {
        boolean z;
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
                ArrayList<DeviceBean> _Beans = DeviceListDaoOperation.getInstance().getDevice();
                int i = 0;
                while (true) {
                    if (i < _Beans.size()) {
                        if (_Beans.get(i).mDeviceName.equals(_deviceName) && _mac.equals(_Beans.get(i).mMacAddr)) {
                            ifadd = false;
                            break;
                        }
                        i++;
                    } else {
                        break;
                    }
                }
                if (ifadd) {
                    z = matchDeviceName(_deviceName, _code, _mac, device, mBluetoothType, isBle, scanRecord);
                } else {
                    z = false;
                }
            }
        }
        z = false;
        return z;
    }

    private synchronized boolean matchDeviceName(String pDeviceName, String pCode, String pMac, BluetoothDevice pDevice, String mBluetoothType, boolean isBle, byte[] scanRecord) {
        boolean f;
        Message msg = new Message();
        f = false;
        if (Constants.DEVICE_8000GW_NAME.equalsIgnoreCase(pDeviceName)) {
            f = true;
            msg.obj = new SerchedDevice(R.drawable.drawable_data_device_ecg, String.valueOf(getString(R.string.str_8000G)) + pCode, pCode, pMac, pDeviceName, pDevice, mBluetoothType, scanRecord);
            msg.what = SEARCH_DEVICE;
        } else if (Constants.PM10_NAME.equalsIgnoreCase(pDeviceName)) {
            f = true;
            msg.obj = new SerchedDevice(R.drawable.drawable_device_pm10, String.valueOf(getString(R.string.device_productname_pm10)) + pCode, pCode, pMac, pDeviceName, pDevice, mBluetoothType, scanRecord);
            msg.what = SEARCH_DEVICE;
        } else if ("TEMP01".equalsIgnoreCase(pDeviceName)) {
            f = true;
            msg.obj = new SerchedDevice(R.drawable.drawable_device_ear_temperature, String.valueOf(getString(R.string.device_productname_hc06)) + pCode, pCode, pMac, pDeviceName, pDevice, mBluetoothType, scanRecord);
            msg.what = SEARCH_DEVICE;
        } else if ("BC01".equalsIgnoreCase(pDeviceName)) {
            f = true;
            msg.obj = new SerchedDevice(R.drawable.drawable_device_bc01, String.valueOf(getString(R.string.device_productname_bc01)) + pCode, pCode, pMac, pDeviceName, pDevice, mBluetoothType, scanRecord);
            msg.what = SEARCH_DEVICE;
        } else if ("CMS50D".equalsIgnoreCase(pDeviceName)) {
            f = true;
            msg.obj = new SerchedDevice(R.drawable.drawable_device_cms50d, "CMS50D-BT" + pCode, pCode, pMac, pDeviceName, pDevice, mBluetoothType, scanRecord);
            msg.what = SEARCH_DEVICE;
        } else if (Constants.CMS50EW.equalsIgnoreCase(pDeviceName)) {
            f = true;
            msg.obj = new SerchedDevice(R.drawable.drawable_data_device_cms50ew, String.valueOf(getString(R.string.device_productname_50EW)) + pCode, pCode, pMac, pDeviceName, pDevice, mBluetoothType, scanRecord);
            msg.what = SEARCH_DEVICE;
        } else if ("CMS50IW".equalsIgnoreCase(pDeviceName)) {
            f = true;
            msg.obj = new SerchedDevice(R.drawable.cms50iw, String.valueOf(getString(R.string.device_productname_50IW)) + pCode, pCode, pMac, pDeviceName, pDevice, mBluetoothType, scanRecord);
            msg.what = SEARCH_DEVICE;
        } else if ("SP10W".equalsIgnoreCase(pDeviceName)) {
            f = true;
            msg.obj = new SerchedDevice(R.drawable.drawable_data_device_sp10w, String.valueOf(getString(R.string.device_productname_SP10W)) + pCode, pCode, pMac, pDeviceName, pDevice, mBluetoothType, scanRecord);
            msg.what = SEARCH_DEVICE;
        } else if ("CMSSXT".equalsIgnoreCase(pDeviceName)) {
            f = true;
            msg.obj = new SerchedDevice(R.drawable.drawable_data_device_cmxxst, String.valueOf(getString(R.string.device_productname_SXT)) + pCode, pCode, pMac, pDeviceName, pDevice, mBluetoothType, scanRecord);
            msg.what = SEARCH_DEVICE;
        } else if ("ABPM50W".equalsIgnoreCase(pDeviceName)) {
            f = true;
            msg.obj = new SerchedDevice(R.drawable.drawable_data_device_abpm50, String.valueOf(getString(R.string.device_productname_M50W)) + pCode, pCode, pMac, pDeviceName, pDevice, mBluetoothType, scanRecord);
            msg.what = SEARCH_DEVICE;
        } else if (DeviceNameUtils.PM50.equalsIgnoreCase(pDeviceName)) {
            f = true;
            msg.obj = new SerchedDevice(R.drawable.drawable_data_device_pm50, String.valueOf(getString(R.string.device_productname_pm50)) + pCode, pCode, pMac, pDeviceName, pDevice, mBluetoothType, scanRecord);
            msg.what = SEARCH_DEVICE;
        } else if (DeviceNameUtils.TEMP03.equalsIgnoreCase(pDeviceName)) {
            f = true;
            msg.obj = new SerchedDevice(R.drawable.drawable_data_device_temp03, String.valueOf(getString(R.string.device_productname_temp03)) + pCode, pCode, pMac, pDeviceName, pDevice, mBluetoothType, scanRecord);
            msg.what = SEARCH_DEVICE;
        } else if ("CONTEC08AW".equalsIgnoreCase(pDeviceName)) {
            f = true;
            msg.obj = new SerchedDevice(R.drawable.drawable_data_device_a08, String.valueOf(getString(R.string.device_productname_08AW)) + pCode, pCode, pMac, pDeviceName, pDevice, mBluetoothType, scanRecord);
            msg.what = SEARCH_DEVICE;
        } else if ("CONTEC08C".equalsIgnoreCase(pDeviceName)) {
            f = true;
            msg.obj = new SerchedDevice(R.drawable.drawable_device_c08, String.valueOf(getString(R.string.device_productname_08AW)) + pCode, pCode, pMac, pDeviceName, pDevice, mBluetoothType, scanRecord);
            msg.what = SEARCH_DEVICE;
        } else if ("CMS50F".equalsIgnoreCase(pDeviceName)) {
            f = true;
            msg.obj = new SerchedDevice(R.drawable.drawable_device_cms50f, String.valueOf(getString(R.string.device_name_50IW)) + pCode, pCode, pMac, pDeviceName, pDevice, mBluetoothType, scanRecord);
            msg.what = SEARCH_DEVICE;
        } else if ("WT".equalsIgnoreCase(pDeviceName)) {
            f = true;
            msg.obj = new SerchedDevice(R.drawable.drawable_data_device_wt, String.valueOf(getString(R.string.device_productname_WT)) + pCode, pCode, pMac, pDeviceName, pDevice, mBluetoothType, scanRecord);
            msg.what = SEARCH_DEVICE;
        } else if ("FHR01".equalsIgnoreCase(pDeviceName)) {
            f = true;
            msg.obj = new SerchedDevice(R.drawable.drawable_data_device_fhr01, String.valueOf(getString(R.string.device_productname_Fhr01)) + pCode, pCode, pMac, pDeviceName, pDevice, mBluetoothType, scanRecord);
            msg.what = SEARCH_DEVICE;
        } else if (Constants.PM85_NAME.equalsIgnoreCase(pDeviceName)) {
            f = true;
            msg.obj = new SerchedDevice(R.drawable.drawable_data_device_pm85, String.valueOf(getString(R.string.device_productname_Pm85)) + pCode, pCode, pMac, pDeviceName, pDevice, mBluetoothType, scanRecord);
            msg.what = SEARCH_DEVICE;
        } else if (Constants.CMS50K_NAME.equalsIgnoreCase(pDeviceName)) {
            if (!isBle) {
                this.mHandler.sendEmptyMessage(PHONE_NO_OPEN_BLE);
            } else if (!this.phoneHardWareIsSupportBLE || !this.phoneSystemVersionIsSupport) {
                this.mHandler.sendEmptyMessage(PHONE_NO_SUPPORT_BLE);
            } else {
                f = true;
                msg.obj = new SerchedDevice(R.drawable.cms50k, String.valueOf(getString(R.string.device_productname_cms50k)) + pCode, pCode, pMac, pDeviceName, pDevice, mBluetoothType, scanRecord);
                msg.what = SEARCH_DEVICE;
            }
        } else if (Constants.CMS50K1_NAME.equalsIgnoreCase(pDeviceName)) {
            if (!isBle) {
                this.mHandler.sendEmptyMessage(PHONE_NO_OPEN_BLE);
            } else if (!this.phoneHardWareIsSupportBLE || !this.phoneSystemVersionIsSupport) {
                this.mHandler.sendEmptyMessage(PHONE_NO_SUPPORT_BLE);
            } else {
                f = true;
                msg.obj = new SerchedDevice(R.drawable.cms50k1, String.valueOf(getString(R.string.device_productname_cms50k)) + pCode, pCode, pMac, pDeviceName, pDevice, mBluetoothType, scanRecord);
                msg.what = SEARCH_DEVICE;
            }
        }
        if (f) {
            this.ifFindNewDevice = true;
            this.mHandler.sendMessageDelayed(msg, 1500);
        }
        return f;
    }

    private void saveDevice(Context pcontext, String pName, String pMac, String pCode, String pBluetoothType, byte[] scanRecord) {
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
                if (Constants.DEVICE_BLUEBOOTH_TYPE_BLE.equals(pBluetoothType)) {
                    CLog.i("jxx", "pBluetoothType类型--：" + pBluetoothType);
                    _beanDao.mBroadcastPacketFiled = Constants.BroadcastPacketHasFiled;
                } else {
                    _beanDao.mBroadcastPacketFiled = broadcastPacketFiled;
                }
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
                    initList();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void scanLeDevice(boolean enable) {
        if (this.mBleScan != null) {
            this.mBleScan = null;
        }
        this.mBleScan = new BluetoothLEScan();
        if (enable) {
            this.mScanning = true;
            this.mBluetoothAdapter.startLeScan(this.mBleScan.mLeScanCallback);
            this.mHandler.sendEmptyMessageDelayed(SEARCH_END_NO_DEVICE, 8000);
            return;
        }
        this.mScanning = false;
        this.mBluetoothAdapter.stopLeScan(this.mBleScan.mLeScanCallback);
    }

    class BluetoothLEScan {
        boolean checkDevice = false;
        public BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {
            public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
                if (!PhmsSharedPreferences.getInstance(Fragmentdevicelist.this.getActivity()).getBoolean("ifOpenBLe", true)) {
                    return;
                }
                if (scanRecord[5] == 18 && scanRecord[6] == -1) {
                    boolean unused = Fragmentdevicelist.this.checkDevice(device, Constants.DEVICE_BLUEBOOTH_TYPE_BLE, true, scanRecord);
                } else {
                    boolean unused2 = Fragmentdevicelist.this.checkDevice(device, Constants.DEVICE_BLUEBOOTH_TYPE_CLASSIC, false, scanRecord);
                }
            }
        };

        BluetoothLEScan() {
        }
    }

    private void ui_DeviceManage(Button btn, boolean ifClick) {
        if (ifClick) {
            btn.setClickable(true);
            btn.setTextColor(getResources().getColorStateList(R.drawable.drawable_blackwhite_bg));
            return;
        }
        btn.setClickable(false);
        btn.setTextColor(getResources().getColor(R.color.gray1));
    }

    public void LogI(String msg) {
        if (Constants.mTestFlag) {
            CLog.i("wsd", msg);
        }
    }

    public void onPause() {
        super.onPause();
        this.onPause = true;
    }

    private void stopServer() {
        CLog.i("jxx", "call 停止轮询服务类 method4");
        PollingService.stopService(getActivity());
    }

    private void startPollServer() {
        getActivity().startService(new Intent(getActivity(), PollingService.class));
    }
}
