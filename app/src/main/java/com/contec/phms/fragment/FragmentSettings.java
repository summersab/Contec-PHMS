package com.contec.phms.fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.contec.phms.App_phms;
//import com.contec.phms.R;
import com.contec.phms.activity.ActivityHistoryRecord;
import com.contec.phms.activity.ActivityNewUserLead;
import com.contec.phms.activity.CMS50kSettingsActivity;
import com.contec.phms.activity.ChangeHospitalActivity;
import com.contec.phms.activity.ElectrocardiogramSetting;
import com.contec.phms.activity.LanguageSettingActivity;
import com.contec.phms.activity.Pedometer50DSetActivity;
import com.contec.phms.activity.ScanLoginActivity;
import com.contec.phms.activity.TimeSettingActivity;
import com.contec.phms.device.template.PollingService;
import com.contec.phms.infos.UserInfo;
import com.contec.phms.login.LoginActivity;
import com.contec.phms.db.UserInfoDao;
import com.contec.phms.db.localdata.PluseDataDao;
import com.contec.phms.util.CLog;
import com.contec.phms.util.Constants;
import com.contec.phms.util.PageUtil;
import com.contec.phms.util.PhmsSharedPreferences;
import com.contec.phms.util.ScreenAdapter;
import com.contec.phms.util.TimerUtil;
import com.contec.phms.util.UpdateManeger;
import com.contec.phms.widget.DialogClass;
import com.contec.phms.widget.ProgressbarCustom;
import com.contec.phms.widget.SwitchView;
import com.contec.phms.widget.UIUtils;
import com.j256.ormlite.dao.Dao;
import com.zxing.android.CaptureActivity;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import u.aly.bs;
import com.contec.R;

public class FragmentSettings extends FragmentBase implements View.OnTouchListener, View.OnClickListener, Runnable {
    public DialogClass _DownloadProgress;
    private HashMap<String, String> _getreturnparams;
    private TextView checkupdate_current_text;
    int id = 0;
    private boolean isAsk = true;
    private TextView language_on_text;
    private final String loginconfirmberror = "109602";
    private final String loginconfirmcodeb = "109604";
    private final String loginconfirmcodeberror = "109603";
    private final String loginconfirmpameserror = "109601";
    private final String loginmachinedberror = "109502";
    private final String loginmachinedcodeberror = "109503";
    private final String loginmachinepameserror = "109501";
    private AlertDialog mAlertDialogloadapking;
    BluetoothAdapter mBluetoothAdapter;
    private Handlerdownloadd mHandler;
    private TextView mLogOutText;
    private RelativeLayout mModifyHosipital;
    private TextView mNewLeadUserText;
    public final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            if (!"android.bluetooth.adapter.action.STATE_CHANGED".equals(intent.getAction())) {
                return;
            }
            if (FragmentSettings.this.mBluetoothAdapter.getState() == 12) {
                FragmentSettings.this.m_bluetooth_on_image.setImageResource(R.drawable.drawable_switch_on);
                FragmentSettings.this.m_bluetooth_on_image.setClickable(true);
                FragmentSettings.this.m_bluetooth_promt.setText(bs.b);
            } else if (FragmentSettings.this.mBluetoothAdapter.getState() == 10) {
                FragmentSettings.this.m_bluetooth_on_image.setImageResource(R.drawable.drawable_switch_off);
                FragmentSettings.this.m_bluetooth_on_image.setClickable(true);
                FragmentSettings.this.m_bluetooth_promt.setText(bs.b);
            }
        }
    };
    private boolean mRegisterReceiver = false;
    private TextView mScreenLight;
    private TextView mSelectHospital;
    private TextView mTv_bluetooth_search_mode;
    private TextView mTv_set_new_BLE;
    private TextView mTv_set_old_BLE;
    private TextView mTv_set_old_BLE_en_temp;
    private TextView mTv_set_old_BLE_temp;
    private UpdateManeger mUpdateManeger;
    private UserInfo mUserinfo;
    private SwitchView mWidgetSwitchview;
    private RelativeLayout m_aboutus_layout;
    private TextView m_aboutus_text;
    private RelativeLayout m_advice_layout;
    private TextView m_advice_text;
    private TextView m_app_set_text;
    private RelativeLayout m_autologin_layout;
    private TextView m_autologin_textview;
    private ImageView m_bluetooth_on_image;
    private TextView m_bluetooth_promt;
    private TextView m_bluetooth_text;
    private RelativeLayout m_checkupdate_layout;
    private TextView m_checkupdate_text;
    private RelativeLayout m_datarecord_layout;
    private TextView m_datarecord_text;
    private RelativeLayout m_intervaltime_layout;
    private TextView m_intervaltime_text;
    private RelativeLayout m_language_layout;
    private TextView m_language_text;
    private RelativeLayout m_lead_user_layout;
    private RelativeLayout m_logout_layout;
    private RelativeLayout m_scan_layout;
    private RelativeLayout m_set_50d_layout;
    private RelativeLayout m_set_screen_layout;
    private TextView m_set_title_text;
    private TextView m_system_set_text;
    private boolean mcancelcheckupdata = false;
    private DialogClass mcheckupdatadialogClass;
    DialogInterface.OnKeyListener mcheckupdatalinstner = new DialogInterface.OnKeyListener() {
        public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
            if (keyCode == 4 && event.getRepeatCount() == 0) {
                FragmentSettings.this.mcheckupdatadialogClass.dismiss();
                FragmentSettings.this.mcancelcheckupdata = true;
            }
            return false;
        }
    };
    DialogInterface.OnKeyListener mdialogkeylistener = new DialogInterface.OnKeyListener() {
        public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
            if (keyCode == 4 && event.getRepeatCount() == 0) {
                UpdateManeger.cancelUpdate = true;
            }
            return false;
        }
    };
    public ProgressbarCustom mdownlaodprogressbar;
    public TextView mdownloadapk_progresstv;
    private AlertDialog myDialog;
    private RelativeLayout rl_electrocardiogram;
    private TextView scanText;
    private TextView set_50d_text;
    private TextView time_interval_on_text;
    private TextView tv_electrocardiogram;
    private String version;

    class Handlerdownloadd extends Handler {
        private DialogClass mupdatadialog;

        Handlerdownloadd() {
        }

        @SuppressLint({"ShowToast"})
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 222:
                    FragmentSettings.this.mdownlaodprogressbar.setProgress(msg.arg1);
                    FragmentSettings.this.mdownloadapk_progresstv.setText(String.valueOf(msg.arg1) + "%");
                    return;
                case 333:
                    try {
                        Thread.currentThread();
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    FragmentSettings.this.mAlertDialogloadapking.dismiss();
                    FragmentSettings.this.mUpdateManeger.installApk();
                    return;
                case Constants.V_NEED_UPDATE /*506*/:
                    if (!FragmentSettings.this.isAsk) {
                        FragmentSettings.this.MDialogClass(FragmentSettings.this.getActivity()).show();
                        FragmentSettings.this.mUpdateManeger.downloadApk();
                        return;
                    }
                    this.mupdatadialog = new DialogClass((Context) FragmentSettings.this.getActivity(), (String) FragmentSettings.this._getreturnparams.get("versioninfo"), (String) FragmentSettings.this._getreturnparams.get("content"), (View.OnClickListener) new View.OnClickListener() {
                        public void onClick(View v) {
                            FragmentSettings.this.mAlertDialogloadapking = FragmentSettings.this.MDialogClass(FragmentSettings.this.getActivity());
                            FragmentSettings.this.mAlertDialogloadapking.show();
                            FragmentSettings.this.mUpdateManeger.downloadApk();
                            Handlerdownloadd.this.mupdatadialog.dismiss();
                        }
                    });
                    return;
                case 507:
                    new DialogClass(FragmentSettings.this.getActivity(), FragmentSettings.this.getActivity().getResources().getString(R.string.no_ver_for_update));
                    return;
                case Constants.V_NETWORK_ERROR /*508*/:
                    new DialogClass(FragmentSettings.this.getActivity(), FragmentSettings.this.getActivity().getResources().getString(R.string.network_anomalies));
                    return;
                case Constants.V_CONNECTION_TIMEOUT /*509*/:
                    new DialogClass(FragmentSettings.this.getActivity(), FragmentSettings.this.getActivity().getResources().getString(R.string.connection_timeout));
                    return;
                default:
                    return;
            }
        }
    }

    public AlertDialog MDialogClass(Context pContext) {
        if (this.myDialog != null) {
            this.myDialog.dismiss();
        }
        this.myDialog = new AlertDialog.Builder(pContext).create();
        this.myDialog.show();
        this.myDialog.setOnKeyListener(this.mdialogkeylistener);
        View layout = ((LayoutInflater) pContext.getSystemService("layout_inflater")).inflate(R.layout.layout_downloadapk, (ViewGroup) null);
        this.mdownlaodprogressbar = (ProgressbarCustom) layout.findViewById(R.id.downloadapk_progressbar);
        this.mdownloadapk_progresstv = (TextView) layout.findViewById(R.id.downloadapk_progresstv);
        if (Constants.IS_PAD_NEW) {
            this.mdownloadapk_progresstv.setTextSize(0, 25.0f);
        }
        this.mdownloadapk_progresstv.setText("0%");
        this.myDialog.getWindow().setContentView(layout);
        Window dialogWindow = this.myDialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = Constants.M_SCREENWEIGH;
        lp.height = Constants.M_SCREENHEIGH;
        this.myDialog.setCanceledOnTouchOutside(false);
        dialogWindow.setAttributes(lp);
        return this.myDialog;
    }

    private boolean checkAutoLogin() {
        List<UserInfoDao> m_users = new ArrayList<>();
        try {
            m_users = App_phms.getInstance().mHelper.getUserDao().queryForEq(UserInfoDao.UserId, this.mUserinfo.mUserID);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (m_users.size() <= 0 || !m_users.get(0).getAutologin()) {
            return false;
        }
        m_users.removeAll(m_users);
        return true;
    }

    public void onResume() {
        super.onResume();
        this.mRegisterReceiver = false;
        ChangeLanuage();
        initData();
    }

    private void initview(View pview) {
        this.mUserinfo = App_phms.getInstance().mUserInfo;
        this.mHandler = new Handlerdownloadd();
        this.mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        this.time_interval_on_text = (TextView) pview.findViewById(R.id.time_interval_on);
        this.language_on_text = (TextView) pview.findViewById(R.id.language_on);
        this.scanText = (TextView) pview.findViewById(R.id.scan_text);
        PackageInfo packInfo = null;
        try {
            packInfo = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        LinearLayout mlayout_menuactivity_main = (LinearLayout) pview.findViewById(R.id.layout_menuactivity_main);
        if (Constants.IS_PAD_NEW) {
            ScreenAdapter.changeLayoutTextSize(getActivity(), mlayout_menuactivity_main, 10);
        }
        this.version = packInfo.versionName;
        this.checkupdate_current_text = (TextView) pview.findViewById(R.id.checkupdate_current);
        this.mSelectHospital = (TextView) pview.findViewById(R.id.set_select_hospital);
        this.checkupdate_current_text.setText(String.valueOf(getString(R.string.checkupdate_cureent)) + this.version);
        this.m_set_title_text = (TextView) pview.findViewById(R.id.set_title_text);
        this.set_50d_text = (TextView) pview.findViewById(R.id.set_50d_text);
        this.m_app_set_text = (TextView) pview.findViewById(R.id.app_set_text);
        this.m_system_set_text = (TextView) pview.findViewById(R.id.system_set_text);
        this.m_intervaltime_text = (TextView) pview.findViewById(R.id.intervaltime_text);
        this.m_datarecord_text = (TextView) pview.findViewById(R.id.datarecord_text);
        this.m_bluetooth_text = (TextView) pview.findViewById(R.id.bluetooth_text);
        this.m_bluetooth_promt = (TextView) pview.findViewById(R.id.bluetooth_promt);
        this.m_language_text = (TextView) pview.findViewById(R.id.language_text);
        this.mScreenLight = (TextView) pview.findViewById(R.id.set_screen_text);
        this.m_aboutus_text = (TextView) pview.findViewById(R.id.aboutus_text);
        this.m_advice_text = (TextView) pview.findViewById(R.id.advice_text);
        this.m_checkupdate_text = (TextView) pview.findViewById(R.id.checkupdate_text);
        this.mLogOutText = (TextView) pview.findViewById(R.id.logout_text);
        this.mNewLeadUserText = (TextView) pview.findViewById(R.id.new_user_lead_text);
        this.m_intervaltime_layout = (RelativeLayout) pview.findViewById(R.id.intervaltime_layout);
        this.m_datarecord_layout = (RelativeLayout) pview.findViewById(R.id.datarecord_layout);
        this.m_aboutus_layout = (RelativeLayout) pview.findViewById(R.id.aboutus_layout);
        this.m_scan_layout = (RelativeLayout) pview.findViewById(R.id.scan_layout);
        this.m_advice_layout = (RelativeLayout) pview.findViewById(R.id.advice_layout);
        this.m_checkupdate_layout = (RelativeLayout) pview.findViewById(R.id.checkupdate_layout);
        this.m_language_layout = (RelativeLayout) pview.findViewById(R.id.language_layout);
        this.m_autologin_layout = (RelativeLayout) pview.findViewById(R.id.autologin_layout);
        this.m_logout_layout = (RelativeLayout) pview.findViewById(R.id.log_out_layout);
        this.m_lead_user_layout = (RelativeLayout) pview.findViewById(R.id.new_user_lead_layout);
        this.m_set_50d_layout = (RelativeLayout) pview.findViewById(R.id.set_50d_layout);
        this.m_autologin_textview = (TextView) pview.findViewById(R.id.autologin_text);
        this.m_set_screen_layout = (RelativeLayout) pview.findViewById(R.id.set_screen_layout);
        this.rl_electrocardiogram = (RelativeLayout) pview.findViewById(R.id.rl_electrocardiogram);
        this.tv_electrocardiogram = (TextView) pview.findViewById(R.id.tv_electrocardiogram);
        this.mWidgetSwitchview = (SwitchView) pview.findViewById(R.id.custom_switchview);
        this.mTv_set_new_BLE = (TextView) pview.findViewById(R.id.tv_set_new_BLE);
        this.mTv_set_old_BLE = (TextView) pview.findViewById(R.id.tv_set_old_BLE);
        this.mTv_bluetooth_search_mode = (TextView) pview.findViewById(R.id.tv_bluetooth_search_mode);
        this.mTv_set_old_BLE_temp = (TextView) pview.findViewById(R.id.tv_set_old_BLE_temp);
        this.mTv_set_old_BLE_en_temp = (TextView) pview.findViewById(R.id.tv_set_old_BLE_en_temp);
        this.mModifyHosipital = (RelativeLayout) pview.findViewById(R.id.modify_hosipital);
        PageUtil.measureView(this.mWidgetSwitchview);
        PageUtil.measureView(this.mTv_set_old_BLE);
        this.m_autologin_layout.setOnTouchListener(this);
        this.m_bluetooth_on_image = (ImageView) pview.findViewById(R.id.bluetooth_on);
        this.m_bluetooth_on_image.setOnClickListener(this);
        this.m_intervaltime_layout.setOnTouchListener(this);
        this.m_datarecord_layout.setOnTouchListener(this);
        this.m_aboutus_layout.setOnTouchListener(this);
        this.m_scan_layout.setOnTouchListener(this);
        this.m_advice_layout.setOnTouchListener(this);
        this.m_checkupdate_layout.setOnTouchListener(this);
        this.m_language_layout.setOnTouchListener(this);
        this.m_logout_layout.setOnTouchListener(this);
        this.m_lead_user_layout.setOnTouchListener(this);
        this.m_set_50d_layout.setOnTouchListener(this);
        this.m_set_screen_layout.setOnTouchListener(this);
        this.rl_electrocardiogram.setOnTouchListener(this);
        this.mModifyHosipital.setOnTouchListener(this);
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View mview = inflater.inflate(R.layout.layout_set_setlist, (ViewGroup) null);
        initview(mview);
        initData();
        return mview;
    }

    private void initData() {
        initBLESearchMode();
    }

    private void initBLESearchMode() {
        int measuredWidth = this.mTv_set_old_BLE.getMeasuredWidth();
        int switchBlockwidth = this.mWidgetSwitchview.getmSwitchBlockwidth();
        ViewGroup.LayoutParams layoutParams = this.mWidgetSwitchview.getLayoutParams();
        layoutParams.width = measuredWidth + switchBlockwidth + UIUtils.dp2px(getActivity(), 10.0f);
        this.mWidgetSwitchview.setLayoutParams(layoutParams);
        if (PhmsSharedPreferences.getInstance(App_phms.getInstance().getApplicationContext()).getBoolean("ifOpenBLe", true)) {
            this.mTv_set_new_BLE.setVisibility(View.VISIBLE);
            this.mTv_set_old_BLE.setVisibility(View.INVISIBLE);
            this.mWidgetSwitchview.setSwitchStatus(true);
        } else {
            this.mWidgetSwitchview.setSwitchStatus(false);
            this.mTv_set_old_BLE.setVisibility(View.VISIBLE);
            this.mTv_set_new_BLE.setVisibility(View.INVISIBLE);
        }
        this.mWidgetSwitchview.setOnSwitchChangeListener(new SwitchView.OnSwitchChangeListener() {
            public void onSwitchChanged(boolean open) {
                if (open) {
                    FragmentSettings.this.mTv_set_new_BLE.setVisibility(View.VISIBLE);
                    FragmentSettings.this.mTv_set_old_BLE.setVisibility(View.INVISIBLE);
                    PhmsSharedPreferences.getInstance(FragmentSettings.this.getActivity()).saveBoolean("ifOpenBLe", true);
                } else {
                    FragmentSettings.this.mTv_set_old_BLE.setVisibility(View.VISIBLE);
                    FragmentSettings.this.mTv_set_new_BLE.setVisibility(View.INVISIBLE);
                    PhmsSharedPreferences.getInstance(FragmentSettings.this.getActivity()).saveBoolean("ifOpenBLe", false);
                }
                Log.i("jxx", "call `PollingService.stopService()` method2");
                PollingService.stopService(FragmentSettings.this.getActivity());
                FragmentSettings.this.getActivity().startService(new Intent(FragmentSettings.this.getActivity(), PollingService.class));
            }
        });
    }

    protected void ChangeLanuage() {
        this.set_50d_text.setText(R.string.set_50d_text);
        this.mSelectHospital.setText(R.string.change_hopital_title);
        this.m_set_title_text.setText(R.string.setting_text);
        this.m_app_set_text.setText(R.string.app_set_text);
        this.m_system_set_text.setText(R.string.system_set_text);
        this.m_intervaltime_text.setText(R.string.intervaltime_text);
        this.m_datarecord_text.setText(R.string.datarecord_text);
        this.m_bluetooth_text.setText(R.string.bluetooth_text);
        this.m_language_text.setText(R.string.language_text);
        this.mScreenLight.setText(R.string.set_screen_text);
        this.m_aboutus_text.setText(R.string.aboutus_text);
        this.m_advice_text.setText(R.string.advice_text);
        this.m_checkupdate_text.setText(R.string.checkupdate_text);
        this.mLogOutText.setText(R.string.str_logout);
        this.mNewLeadUserText.setText(R.string.new_user_lead_text);
        this.scanText.setText(R.string.register_saoyisao);
        this.tv_electrocardiogram.setText(R.string.electrocardiogram_analyse);
        this.mTv_bluetooth_search_mode.setText(R.string.bluetooth_search_mode);
        this.mTv_set_new_BLE.setText(R.string.new_BLE);
        this.mTv_set_old_BLE.setText(R.string.old_BLE);
        if (this.version.equals(bs.b)) {
            this.version = "4.5";
        }
        this.checkupdate_current_text.setText(String.valueOf(getString(R.string.checkupdate_cureent)) + this.version);
        this.m_autologin_textview.setText(R.string.opt_cancle_autologin);
        if (!App_phms.time_interval_state) {
            this.time_interval_on_text.setText(String.valueOf(this.mUserinfo.mSearchInterval) + getString(R.string.s));
        } else if (App_phms.time_interval_state) {
            this.time_interval_on_text.setText(String.valueOf(this.mUserinfo.mSearchInterval) + getString(R.string.s));
        }
        if (!App_phms.language_set_state) {
            String languageShare = Constants.Language;
            if (languageShare.equalsIgnoreCase("zh")) {
                this.language_on_text.setText(getString(R.string.language_zh));
            } else if (languageShare.equalsIgnoreCase("en")) {
                this.language_on_text.setText(getString(R.string.language_en));
            } else if (languageShare.equalsIgnoreCase("1en") || languageShare.equalsIgnoreCase("1zh") || languageShare.equals(bs.b)) {
                this.language_on_text.setText(getString(R.string.language_aut));
            }
        } else if (App_phms.language_set_state) {
            if (Constants.Language.equalsIgnoreCase("zh")) {
                this.language_on_text.setText(getString(R.string.language_zh));
            } else if (Constants.Language.equalsIgnoreCase("en")) {
                this.language_on_text.setText(getString(R.string.language_en));
            } else if (Constants.Language.equalsIgnoreCase("1en") || Constants.Language.equalsIgnoreCase("1zh") || Constants.Language.equals(bs.b)) {
                this.language_on_text.setText(getString(R.string.language_aut));
            }
        }
        if (this.mBluetoothAdapter.isEnabled()) {
            this.m_bluetooth_on_image.setImageResource(R.drawable.drawable_switch_on);
        } else {
            this.m_bluetooth_on_image.setImageResource(R.drawable.drawable_switch_off);
        }
        IntentFilter filter = new IntentFilter("android.bluetooth.adapter.action.STATE_CHANGED");
        this.mRegisterReceiver = true;
        getActivity().registerReceiver(this.mReceiver, filter);
    }

    public void onClick(View arg0) {
        switch (arg0.getId()) {
            case R.id.bluetooth_on:
                if (this.mBluetoothAdapter.isEnabled()) {
                    this.m_bluetooth_on_image.setImageResource(R.drawable.drawable_switch_off);
                    this.m_bluetooth_on_image.setClickable(false);
                    this.m_bluetooth_promt.setText(R.string.bluetooth_promt_off);
                    this.mBluetoothAdapter.disable();
                    return;
                }
                this.m_bluetooth_on_image.setImageResource(R.drawable.drawable_switch_on);
                this.m_bluetooth_on_image.setClickable(false);
                this.m_bluetooth_promt.setText(R.string.bluetooth_promt_on);
                this.mBluetoothAdapter.enable();
                return;
            default:
                return;
        }
    }

    public void checkver() {
        if (!PageUtil.checkNet(getActivity())) {
            new DialogClass(getActivity(), getActivity().getResources().getString(R.string.net_disable));
            return;
        }
        this.isAsk = true;
        this.mUpdateManeger = new UpdateManeger(getActivity(), this.mHandler);
        new Thread(this).start();
        this.mcheckupdatadialogClass = new DialogClass((Context) getActivity(), getString(R.string.more_checkupdata_ischecking), false, 0, this.mcheckupdatalinstner);
    }

    public void onStop() {
        super.onStop();
        if (this.mReceiver != null && this.mRegisterReceiver) {
            getActivity().unregisterReceiver(this.mReceiver);
            this.mRegisterReceiver = true;
        }
    }

    public void run() {
        this._getreturnparams = this.mUpdateManeger.checkUpdate();
        int _isNeedUpdate = Integer.valueOf(this._getreturnparams.get("_isNeedUpdate")).intValue();
        CLog.i("lz", "_isNeedUpdate = " + _isNeedUpdate);
        this.mcheckupdatadialogClass.dismiss();
        if (this.mcancelcheckupdata) {
            this.mcancelcheckupdata = false;
            return;
        }
        switch (_isNeedUpdate) {
            case 1:
                this.mHandler.sendEmptyMessage(Constants.V_NEED_UPDATE);
                return;
            case 2:
                this.mHandler.sendEmptyMessage(507);
                return;
            case 3:
                this.mHandler.sendEmptyMessage(Constants.V_NETWORK_ERROR);
                return;
            case 4:
                this.mHandler.sendEmptyMessage(Constants.V_CONNECTION_TIMEOUT);
                return;
            default:
                return;
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0 && resultCode == -1) {
            String scanResult = data.getExtras().getString(PluseDataDao.RESULT);
            Intent intent = new Intent(getActivity(), ScanLoginActivity.class);
            intent.putExtra("scanResult", scanResult);
            startActivity(intent);
            getActivity().overridePendingTransition(R.anim.activity_scan_login_open, 0);
        }
    }

    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == 0) {
            if (v.getId() == R.id.intervaltime_layout) {
                this.id = R.id.intervaltime_layout;
                return true;
            } else if (v.getId() == R.id.datarecord_layout) {
                this.id = R.id.datarecord_layout;
                return true;
            } else if (v.getId() == R.id.language_layout) {
                this.id = R.id.language_layout;
                return true;
            } else if (v.getId() == R.id.aboutus_layout) {
                this.id = R.id.aboutus_layout;
                return true;
            } else if (v.getId() == R.id.scan_layout) {
                this.id = R.id.scan_layout;
                return true;
            } else if (v.getId() == R.id.advice_layout) {
                this.id = R.id.advice_layout;
                return true;
            } else if (v.getId() == R.id.modify_hosipital) {
                this.id = R.id.modify_hosipital;
                return true;
            } else if (v.getId() == R.id.checkupdate_layout) {
                this.id = R.id.checkupdate_layout;
                return true;
            } else if (v.getId() == R.id.autologin_layout) {
                this.id = R.id.autologin_layout;
                return true;
            } else if (v.getId() == R.id.log_out_layout) {
                this.id = R.id.log_out_layout;
                return true;
            } else if (v.getId() == R.id.new_user_lead_layout) {
                this.id = R.id.new_user_lead_layout;
                return true;
            } else if (v.getId() == R.id.set_50d_layout) {
                this.id = R.id.set_50d_layout;
                return true;
            } else if (v.getId() == R.id.set_screen_layout) {
                this.id = R.id.set_screen_layout;
                return true;
            } else if (v.getId() != R.id.rl_electrocardiogram) {
                return true;
            } else {
                this.id = R.id.rl_electrocardiogram;
                return true;
            }
        } else if (event.getAction() != 1) {
            return true;
        } else {
            if (this.id == R.id.intervaltime_layout) {
                startActivity(new Intent(getActivity(), TimeSettingActivity.class));
                return true;
            } else if (this.id == R.id.datarecord_layout) {
                startActivity(new Intent(getActivity(), ActivityHistoryRecord.class));
                return true;
            } else if (this.id == R.id.language_layout) {
                startActivity(new Intent(getActivity(), LanguageSettingActivity.class));
                return true;
            } else if (this.id == R.id.aboutus_layout) {
                startActivity(new Intent(getActivity(), FragmentSetAboutUs.class));
                return true;
            } else if (this.id == R.id.scan_layout) {
                startActivityForResult(new Intent(getActivity(), CaptureActivity.class), 0);
                return true;
            } else if (this.id == R.id.advice_layout) {
                startActivity(new Intent(getActivity(), FragmentSetAdvice.class));
                return true;
            } else if (this.id == R.id.modify_hosipital) {
                startActivity(new Intent(getActivity(), ChangeHospitalActivity.class));
                return true;
            } else if (this.id == R.id.checkupdate_layout) {
                checkver();
                return true;
            } else if (this.id == R.id.autologin_layout) {
                App_phms.getInstance().exitall(0);
                TimerUtil.getinstance().clearTimer();
                return true;
            } else if (this.id == R.id.rl_electrocardiogram) {
                startActivity(new Intent(getActivity(), ElectrocardiogramSetting.class));
                return true;
            } else if (v.getId() == R.id.log_out_layout) {
                if (checkAutoLogin()) {
                    try {
                        Dao<UserInfoDao, String> userDao = App_phms.getInstance().mHelper.getUserDao();
                        UserInfoDao retUser = userDao.queryForId(App_phms.getInstance().mUserInfo.mUserID);
                        retUser.setAutologin(false);
                        userDao.update(retUser);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
                App_phms.getInstance().exitall(0);
                TimerUtil.getinstance().clearTimer();
                if (this != null) {
                    startActivity(new Intent(App_phms.getInstance().getApplicationContext(), LoginActivity.class));
                    return true;
                }
                CLog.i("lz", "Program exited - don't start activity.");
                return true;
            } else if (v.getId() == R.id.new_user_lead_layout) {
                startActivity(new Intent(getActivity(), ActivityNewUserLead.class));
                return true;
            } else if (v.getId() == R.id.set_50d_layout) {
                startActivity(new Intent(getActivity(), Pedometer50DSetActivity.class));
                return true;
            } else if (this.id != R.id.set_screen_layout) {
                return true;
            } else {
                startActivity(new Intent(getActivity(), CMS50kSettingsActivity.class));
                return true;
            }
        }
    }
}
