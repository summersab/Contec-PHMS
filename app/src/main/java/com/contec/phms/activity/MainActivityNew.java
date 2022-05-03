package com.contec.phms.activity;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
//import com.alibaba.sdk.android.callback.InitResultCallback;
import com.conect.json.CLog;
import com.contec.phms.App_phms;
import com.contec.phms.R;
import com.contec.phms.SearchDevice.SearchDevice;
import com.contec.phms.Server_Main;
import com.contec.phms.device.cms50k.update.DownLoadFile;
import com.contec.phms.device.template.BluetoothServerService;
import com.contec.phms.device.template.DataUploadSucceedNotificationService;
import com.contec.phms.device.template.DeviceService;
import com.contec.phms.device.template.PollingService;
import com.contec.phms.fragment.FragmentDataSerchDevice;
import com.contec.phms.fragment.FragmentHealthReport;
import com.contec.phms.fragment.FragmentManagerDevices;
import com.contec.phms.fragment.FragmentProductData;
import com.contec.phms.fragment.FragmentSerialNumber;
import com.contec.phms.fragment.FragmentSettings;
import com.contec.phms.fragment.Fragmentdevicelist;
import com.contec.phms.log.Service_SaveLog;
import com.contec.phms.manager.device.DeviceManager;
import com.contec.phms.manager.message.InstantMessageService;
import com.contec.phms.manager.message.MessageManager;
import com.contec.phms.upload.UploadService;
import com.contec.phms.util.Constants;
import com.contec.phms.util.PageUtil;
import com.contec.phms.widget.DialogClass;
import com.contec.phms.widget.UpdateMainLanguageInterface;
import com.taobao.tae.sdk.TaeSDK;
//import com.umeng.analytics.MobclickAgent;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivityNew extends FragmentActivity implements View.OnClickListener, UpdateMainLanguageInterface {
    public static int currentTab = 0;
    public static boolean mIsFromNotification = false;
    public static int mfragmentcurrentindex = 0;
    private String TAG = getClass().getSimpleName();
    private int fragmentContentId;
    private Boolean hasTask = false;
    private Boolean isExit = false;
    private LinearLayout mAdvicely;
    private ImageView mBallImageView;
    private LinearLayout mCollectionly;
    private int mCurrentTab = 0;
    private int mFragmentDataSerchDeviceindex = 5;
    public List<Fragment> mFragmentLists = new ArrayList();
    private int mFragmentManagerDevicesindex = 4;
    private LinearLayout mIntroductionly;
    private LinearLayout mMorely;
    private LinearLayout mReportly;
    private int mSettingsindex = 3;
    private DialogClass m_dialogClass;
    private LinearLayout mainbottomitemlayout;
    private int mcheckReportindex = 1;
    private TextView mdata_transfer;
    private int mdevicelistindex = 0;
    private int mfragmentSerialNumberindex = 6;
    private FragmentHealthReport mfragmentcheckreport;
    private int mhealthAdviceindex = 4;
    private TextView mhealthreport;
    private int mproductDataindex = 2;
    private TextView mproductinfo;
    private Bundle mputFragmentSerialNumberdata;
    private TextView msetting_more;
    Timer tExit = new Timer();
    TimerTask task = new TimerTask() {
        public void run() {
            MainActivityNew.this.isExit = false;
            MainActivityNew.this.hasTask = true;
        }
    };

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(128, 128);
        setContentView(R.layout.layout_managerallfragmentnew);
        App_phms.getInstance().addActivity(this);
        App_phms.getInstance().mEventBus.register(this);
        initview();
        initdefaultfragment();
        startService(new Intent(this, InstantMessageService.class));
        if (getIntent().getBooleanExtra(Constants.KEY_CHECK_REPORT, false)) {
            startReport();
        }
        initAliyunSDK();
        startService(new Intent(this, Service_SaveLog.class));
        startService(new Intent(this, PollingService.class));
        App_phms.getInstance().getmRemoveConnectDeviceBeanLists().clear();
        startService(new Intent(this, DataUploadSucceedNotificationService.class));
        new DownLoadFile().startDownFile();
    }

    private void initdefaultfragment() {
        this.mfragmentcheckreport = new FragmentHealthReport();
        Fragmentdevicelist mfragmentdevicelist = new Fragmentdevicelist();
        mfragmentdevicelist.setUpdateTabInterface(this);
        this.mFragmentLists.add(mfragmentdevicelist);
        this.mFragmentLists.add(this.mfragmentcheckreport);
        this.mFragmentLists.add(new FragmentProductData());
        this.mFragmentLists.add(new FragmentSettings());
        this.mFragmentLists.add(new FragmentManagerDevices());
        this.mFragmentLists.add(new FragmentDataSerchDevice());
        this.mFragmentLists.add(new FragmentSerialNumber());
        this.fragmentContentId = R.id.tab_content;
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(this.fragmentContentId, this.mFragmentLists.get(0));
        ft.commit();
    }

    private void initview() {
        this.mainbottomitemlayout = (LinearLayout) findViewById(R.id.mainbottomitemlayout);
        this.mCollectionly = (LinearLayout) findViewById(R.id.main_collection_ly);
        this.mReportly = (LinearLayout) findViewById(R.id.main_report_ly);
        this.mAdvicely = (LinearLayout) findViewById(R.id.main_advice_ly);
        this.mIntroductionly = (LinearLayout) findViewById(R.id.main_introduction_ly);
        this.mMorely = (LinearLayout) findViewById(R.id.main_more_ly);
        this.mBallImageView = (ImageView) findViewById(R.id.show_red_ball_msg);
        this.mBallImageView.setImageResource(R.drawable.img_bottom_menu_report);
        this.mCollectionly.setOnClickListener(this);
        this.mReportly.setOnClickListener(this);
        this.mAdvicely.setOnClickListener(this);
        this.mIntroductionly.setOnClickListener(this);
        this.mMorely.setOnClickListener(this);
        this.mdata_transfer = (TextView) findViewById(R.id.data_transfer);
        this.mhealthreport = (TextView) findViewById(R.id.healthreport);
        this.mproductinfo = (TextView) findViewById(R.id.productinfo);
        this.msetting_more = (TextView) findViewById(R.id.setting_more);
    }

    public void onEvent(Message msg) {
        if (msg.arg2 == 1) {
            switch (msg.what) {
                case Constants.V_START_REPORT /*522*/:
                    if (this.m_dialogClass != null) {
                        this.m_dialogClass.dismiss();
                    }
                    setalllyblack_Gone(this.mReportly);
                    clickbottomitem(this.mcheckReportindex);
                    return;
                case Constants.V_SHOW_MENU_MAIN /*531*/:
                    setalllyblack_Visib(this.mCollectionly);
                    setalllyblack(this.mCollectionly);
                    return;
                case Constants.OPEN_DEVICE_MANAGEDEVICE /*536*/:
                    Constants.REPORT = false;
                    setalllyblack(this.mCollectionly);
                    clickbottomitem(this.mFragmentManagerDevicesindex);
                    return;
                case Constants.CLOSE_DEVICE_MANAGEDEVICE /*537*/:
                    Constants.REPORT = false;
                    setalllyblack(this.mCollectionly);
                    clickbottomitem(this.mdevicelistindex);
                    return;
                case Constants.OPEN_SEARCHDEVICE_FRAGMENT /*538*/:
                    startAddDevice(false);
                    Constants.REPORT = false;
                    setalllyblack(this.mCollectionly);
                    clickbottomitem(this.mFragmentDataSerchDeviceindex);
                    return;
                case Constants.CLOSE_SEARCHDEVICE_FRAGMENT /*539*/:
                    Constants.REPORT = false;
                    setalllyblack(this.mCollectionly);
                    clickbottomitem(this.mdevicelistindex);
                    return;
                case Constants.CLOSE_MANAGERDEVICE_FRAGMENT /*540*/:
                    Constants.REPORT = false;
                    setalllyblack(this.mCollectionly);
                    clickbottomitem(this.mdevicelistindex);
                    return;
                case Constants.OPEN_SERIALNUMBER_FRAGMENT /*541*/:
                    Constants.REPORT = false;
                    setalllyblack(this.mCollectionly);
                    if (msg.getData() != null) {
                        this.mputFragmentSerialNumberdata = msg.getData();
                    }
                    clickbottomitem(this.mfragmentSerialNumberindex);
                    return;
                case Constants.CLOSE_SERIALNUMBER_FRAGMENT /*542*/:
                    startAddDevice(false);
                    Constants.REPORT = false;
                    setalllyblack(this.mCollectionly);
                    clickbottomitem(this.mFragmentDataSerchDeviceindex);
                    return;
                default:
                    return;
            }
        } else if (msg.arg2 == 13) {
            switch (msg.what) {
                case 532:
                    this.mBallImageView.setImageResource(R.drawable.img_bottom_menu_report_msg);
                    return;
                case Constants.Mark_NOVIP /*545*/:
                    new DialogClass((Context) this, getResources().getString(R.string.novip), 16);
                    return;
                case 551:
                    jump_health_report();
                    return;
                default:
                    return;
            }
        }
    }

    private void jump_health_report() {
        if (Constants.REPORT_TEST) {
            Message msgs = new Message();
            msgs.what = Constants.REPORT_PAGE;
            App_phms.getInstance().mEventBus.post(msgs);
            setalllyblack(this.mReportly);
            setalllyblack_Gone(this.mReportly);
            clickbottomitem(this.mcheckReportindex);
        } else {
            deleteDatabase("webview.db");
            deleteDatabase("webviewCache.db");
            deleteDatabase("webviewCookiesChromium.db");
            startReport();
        }
        mIsFromNotification = false;
    }

    protected void onResume() {
        CLog.e("onResume", "onResume");
        com.contec.phms.util.CLog.i("jxx", "call 主界面的onResume method");
        super.onResume();
        Constants.MAIN_STOP = false;
        this.mdata_transfer.setText(R.string.data_transfer_text);
        this.mhealthreport.setText(R.string.healthreport);
        this.mproductinfo.setText(R.string.productinfo);
        this.msetting_more.setText(R.string.setting_more);
        //MobclickAgent.onResume(this);
        if (mIsFromNotification) {
            Message msg = new Message();
            msg.arg2 = 13;
            msg.what = 551;
            App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(msg);
        }
    }

    protected void onPause() {
        super.onPause();
        //MobclickAgent.onPause(this);
    }

    protected void onStop() {
        super.onStop();
        if (this.m_dialogClass != null) {
            this.m_dialogClass.dismiss();
        }
        Constants.MAIN_STOP = true;
        mIsFromNotification = false;
        com.contec.phms.util.CLog.d(this.TAG, "call onStop method");
    }

    public void onClick(View v) {
        Constants.START_USER_INFO_PAGE = false;
        if (v.getId() == R.id.main_collection_ly) {
            if (Constants.REPORT_TEST) {
                setalllyblack_Visib(this.mCollectionly);
                Constants.REPORT = false;
                setalllyblack(this.mCollectionly);
                clickbottomitem(this.mdevicelistindex);
            } else {
                Constants.REPORT = false;
                setalllyblack(this.mCollectionly);
                clickbottomitem(this.mdevicelistindex);
            }
            this.mCurrentTab = 0;
        } else if (v.getId() == R.id.main_report_ly) {
            jump_health_report();
            this.mCurrentTab = 1;
        } else if (v.getId() == R.id.main_advice_ly) {
            Constants.REPORT = false;
            setalllyblack(this.mAdvicely);
            clickbottomitem(this.mhealthAdviceindex);
        } else if (v.getId() == R.id.main_introduction_ly) {
            if (Constants.REPORT_TEST) {
                Message msgs = new Message();
                msgs.what = Constants.ADVICE_HEALTH;
                App_phms.getInstance().mEventBus.post(msgs);
                setalllyblack(this.mIntroductionly);
                this.mCurrentTab = 4;
                return;
            }
            Constants.REPORT = false;
            setalllyblack(this.mIntroductionly);
            clickbottomitem(this.mproductDataindex);
            this.mCurrentTab = 2;
        } else if (v.getId() != R.id.main_more_ly) {
        } else {
            if (Constants.REPORT_TEST) {
                Message msgs2 = new Message();
                msgs2.what = Constants.MY_USERINFO;
                App_phms.getInstance().mEventBus.post(msgs2);
                setalllyblack(this.mMorely);
                this.mCurrentTab = 5;
                return;
            }
            Constants.REPORT = false;
            setalllyblack(this.mMorely);
            clickbottomitem(this.mSettingsindex);
            this.mCurrentTab = 3;
        }
    }

    private void startReport() {
        com.contec.phms.util.CLog.i("lzerror", "startReport Constants.REPORT = " + Constants.REPORT);
        this.mBallImageView.setImageResource(R.drawable.img_bottom_menu_report);
        String netType = PageUtil.getNetType(this);
        Constants.REPORT = false;
        setalllyblack(this.mReportly);
        setalllyblack_Gone(this.mReportly);
        clickbottomitem(this.mcheckReportindex);
    }

    private void startAddDevice(boolean ifCircle) {
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter.isDiscovering()) {
            mBluetoothAdapter.cancelDiscovery();
            Constants.GO_TO_ADD_DEVICE = true;
            SearchDevice.stopServer(this);
            DeviceManager.stopServer(this);
            UploadService.stopServer(this);
            MessageManager.stopServer(this);
            DeviceService.stopServer(this);
            Server_Main.stopServer(this);
            BluetoothServerService.stopServer(this);
        } else if (ifCircle) {
            if (DeviceService.mReceiveFinished || DeviceManager.mDeviceList == null || DeviceManager.mDeviceList.size() <= 0) {
                SearchDevice.stopServer(this);
                DeviceManager.stopServer(this);
                UploadService.stopServer(this);
                MessageManager.stopServer(this);
                DeviceService.stopServer(this);
                Server_Main.stopServer(this);
                Constants.GO_TO_ADD_DEVICE = false;
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
            SearchDevice.stopServer(this);
            DeviceManager.stopServer(this);
            UploadService.stopServer(this);
            MessageManager.stopServer(this);
            DeviceService.stopServer(this);
            Server_Main.stopServer(this);
            Constants.GO_TO_ADD_DEVICE = false;
        }
    }

    private void clickbottomitem(int i) {
        com.contec.phms.util.CLog.i(this.TAG, "fragment " + i);
        if (i != currentTab) {
            if (i == 0) {
                this.mCurrentTab = i;
            }
            com.contec.phms.util.CLog.i(this.TAG, "fragment 即将跳转  " + i);
            mfragmentcurrentindex = i;
            Fragment fragment = this.mFragmentLists.get(i);
            FragmentTransaction ft = obtainFragmentTransaction(i);
            if (i == this.mfragmentSerialNumberindex) {
                try {
                    fragment.setArguments(this.mputFragmentSerialNumberdata);
                } catch (Exception e) {
                    e.printStackTrace();
                    com.contec.phms.util.CLog.i(this.TAG, "fragment.setArguments error");
                }
            }
            if (currentTab == 6) {
                getCurrentFragment().onStop();
            } else {
                getCurrentFragment().onPause();
            }
            if (fragment.isAdded()) {
                fragment.onResume();
            } else {
                ft.add(this.fragmentContentId, fragment);
            }
            showTab(i);
            ft.commit();
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:24:0x0066, code lost:
        if (r0 == false) goto L_0x0068;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean onKeyDown(int r9, android.view.KeyEvent r10) {
        /*
            r8 = this;
            r7 = 4
            r6 = 0
            if (r9 != r7) goto L_0x001d
            int r4 = mfragmentcurrentindex
            int r5 = r8.mcheckReportindex
            if (r4 != r5) goto L_0x001e
            if (r9 != r7) goto L_0x001d
            int r4 = r8.mdevicelistindex
            r8.clickbottomitem(r4)
            android.widget.LinearLayout r4 = r8.mCollectionly
            r8.setalllyblack_Visib(r4)
            android.widget.LinearLayout r4 = r8.mCollectionly
            r8.setalllyblack(r4)
            com.contec.phms.util.Constants.REPORT = r6
        L_0x001d:
            return r6
        L_0x001e:
            int r4 = mfragmentcurrentindex
            int r5 = r8.mfragmentSerialNumberindex
            if (r4 != r5) goto L_0x002a
            int r4 = r8.mFragmentDataSerchDeviceindex
            r8.clickbottomitem(r4)
            goto L_0x001d
        L_0x002a:
            int r4 = mfragmentcurrentindex
            int r5 = r8.mFragmentDataSerchDeviceindex
            if (r4 != r5) goto L_0x0044
            com.contec.phms.manager.device.DeviceListItem r4 = com.contec.phms.manager.device.DeviceManager.mDeviceList
            int r4 = r4.size()
            if (r4 <= 0) goto L_0x003e
            int r4 = r8.mFragmentManagerDevicesindex
            r8.clickbottomitem(r4)
            goto L_0x001d
        L_0x003e:
            int r4 = r8.mdevicelistindex
            r8.clickbottomitem(r4)
            goto L_0x001d
        L_0x0044:
            int r4 = mfragmentcurrentindex
            int r5 = r8.mFragmentManagerDevicesindex
            if (r4 != r5) goto L_0x0050
            int r4 = r8.mdevicelistindex
            r8.clickbottomitem(r4)
            goto L_0x001d
        L_0x0050:
            r0 = 0
            com.contec.phms.manager.device.DeviceListItem r4 = com.contec.phms.manager.device.DeviceManager.mDeviceList
            java.util.List r4 = r4.getListDevice()
            if (r4 == 0) goto L_0x0068
            r1 = 0
        L_0x005a:
            com.contec.phms.manager.device.DeviceListItem r4 = com.contec.phms.manager.device.DeviceManager.mDeviceList
            java.util.List r4 = r4.getListDevice()
            int r4 = r4.size()
            if (r1 < r4) goto L_0x007d
            if (r0 != 0) goto L_0x001d
        L_0x0068:
            android.content.Intent r2 = new android.content.Intent
            java.lang.String r4 = "android.intent.action.MAIN"
            r2.<init>(r4)
            r4 = Intent.FLAG_ACTIVITY_NEW_TASK(0x10000000, float:2.5243549E-29)
            r2.setFlags(r4)
            java.lang.String r4 = "android.intent.category.HOME"
            r2.addCategory(r4)
            r8.startActivity(r2)
            goto L_0x001d
        L_0x007d:
            com.contec.phms.manager.device.DeviceListItem r4 = com.contec.phms.manager.device.DeviceManager.mDeviceList
            com.contec.phms.manager.device.DeviceBeanList r4 = r4.getDevice(r1)
            boolean r4 = r4.misShowDelBtn
            if (r4 == 0) goto L_0x00a5
            r0 = 1
            com.contec.phms.manager.device.DeviceListItem r4 = com.contec.phms.manager.device.DeviceManager.mDeviceList
            com.contec.phms.manager.device.DeviceBeanList r4 = r4.getDevice(r1)
            r4.misShowDelBtn = r6
            android.os.Message r3 = new android.os.Message
            r3.<init>()
            r4 = 520(0x208, float:7.29E-43)
            r3.what = r4
            r4 = 1
            r3.arg2 = r4
            com.contec.phms.App_phms r4 = com.contec.phms.App_phms.getInstance()
            de.greenrobot.event.EventBusPostOnBackGround r4 = r4.mEventBusPostOnBackGround
            r4.postInMainThread(r3)
        L_0x00a5:
            int r1 = r1 + 1
            goto L_0x005a
        */
        throw new UnsupportedOperationException("Method not decompiled: com.contec.phms.activity.MainActivityNew.onKeyDown(int, android.view.KeyEvent):boolean");
    }

    private FragmentTransaction obtainFragmentTransaction(int index) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if (index > currentTab) {
            ft.setCustomAnimations(R.anim.slide_left_in, R.anim.slide_left_out);
        } else {
            ft.setCustomAnimations(R.anim.slide_right_in, R.anim.slide_right_out);
        }
        return ft;
    }

    public Fragment getCurrentFragment() {
        return this.mFragmentLists.get(currentTab);
    }

    private void showTab(int idx) {
        for (int i = 0; i < this.mFragmentLists.size(); i++) {
            Fragment fragment = this.mFragmentLists.get(i);
            FragmentTransaction ft = obtainFragmentTransaction(idx);
            if (idx == i) {
                ft.show(fragment);
            } else if (i == this.mcheckReportindex) {
                try {
                    ft.remove(fragment);
                } catch (Exception e) {
                    e.printStackTrace();
                    com.contec.phms.util.CLog.e(this.TAG, "删除mfragmentcheckreport时出错了");
                }
            } else {
                ft.hide(fragment);
            }
            ft.commit();
        }
        currentTab = idx;
    }

    private void setalllyblack(LinearLayout ply) {
        this.mCollectionly.setBackgroundResource(R.drawable.img_bottom_bg);
        this.mReportly.setBackgroundResource(R.drawable.img_bottom_bg);
        this.mAdvicely.setBackgroundResource(R.drawable.img_bottom_bg);
        this.mIntroductionly.setBackgroundResource(R.drawable.img_bottom_bg);
        this.mMorely.setBackgroundResource(R.drawable.img_bottom_bg);
        ply.setBackgroundResource(R.drawable.img_bottom_bg_s);
        this.mAdvicely.setVisibility(View.GONE);
    }

    private void setalllyblack_Gone(LinearLayout ply) {
        Constants.REPORT_TEST = true;
        ((ImageView) findViewById(R.id.productinfo_imgview)).setImageResource(R.drawable.img_bottom_menu_advice);
        ((ImageView) findViewById(R.id.setting_more_imageview)).setImageResource(R.drawable.img_bottom_menu_userinfo);
        this.msetting_more.setText(getResources().getString(R.string.str_user_info));
        this.mproductinfo.setText(getResources().getString(R.string.str_advice_health));
    }

    private void setalllyblack_Visib(LinearLayout ply) {
        Constants.REPORT_TEST = false;
        ((ImageView) findViewById(R.id.productinfo_imgview)).setImageResource(R.drawable.img_bottom_menu_introductioimg_bottom_n);
        ((ImageView) findViewById(R.id.setting_more_imageview)).setImageResource(R.drawable.img_bottom_menu_more);
        this.msetting_more.setText(getResources().getString(R.string.setting_more));
        this.mproductinfo.setText(getResources().getString(R.string.productinfo));
        this.mainbottomitemlayout.setVisibility(View.VISIBLE);
        this.mCollectionly.setVisibility(View.VISIBLE);
        this.mReportly.setVisibility(View.VISIBLE);
        this.mAdvicely.setVisibility(View.VISIBLE);
        this.mIntroductionly.setVisibility(View.VISIBLE);
        this.mMorely.setVisibility(View.VISIBLE);
        ply.setVisibility(View.VISIBLE);
    }

    protected void onDestroy() {
        super.onDestroy();
        Service_SaveLog.stopServer(this);
        Constants.MAIN_STOP = true;
        App_phms.getInstance().mEventBus.unregister(this);
        App_phms.getInstance().exitall(0);
    }

    public void UpdateTabText() {
        ((ImageView) findViewById(R.id.productinfo_imgview)).setImageResource(R.drawable.img_bottom_menu_introductioimg_bottom_n);
        ((ImageView) findViewById(R.id.setting_more_imageview)).setImageResource(R.drawable.img_bottom_menu_more);
        this.mdata_transfer.setText(R.string.data_transfer_text);
        this.mhealthreport.setText(R.string.healthreport);
        this.mproductinfo.setText(R.string.productinfo);
        this.msetting_more.setText(R.string.setting_more);
        switch (this.mCurrentTab) {
            case 1:
                jump_health_report();
                Constants.REPORT_TEST = true;
                return;
            case 4:
                Constants.REPORT_TEST = true;
                Message msgs = new Message();
                msgs.what = Constants.ADVICE_HEALTH;
                App_phms.getInstance().mEventBus.post(msgs);
                setalllyblack(this.mIntroductionly);
                setalllyblack_Gone(this.mReportly);
                return;
            case 5:
                Constants.REPORT_TEST = true;
                Message msgss = new Message();
                msgss.what = Constants.MY_USERINFO;
                App_phms.getInstance().mEventBus.post(msgss);
                setalllyblack(this.mMorely);
                setalllyblack_Gone(this.mReportly);
                return;
            default:
                return;
        }
    }

    public MainActivityNew getMainObject() {
        return this;
    }

    private void initAliyunSDK() {
        TaeSDK.setEnvIndex(1);
        /*
        TaeSDK.asyncInit(getApplicationContext(), new InitResultCallback() {
            public void onFailure(int arg0, String arg1) {
                Constants.IS_SUCCESS = false;
            }

            public void onSuccess() {
                Constants.IS_SUCCESS = true;
            }
        });
        */
    }
}
