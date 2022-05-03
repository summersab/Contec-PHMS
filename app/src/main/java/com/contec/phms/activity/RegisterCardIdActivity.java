package com.contec.phms.activity;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import com.contec.phms.App_phms;
//import com.contec.phms.R;
import com.contec.phms.ajaxcallback.AjaxCallBack_login;
import com.contec.phms.ajaxcallback.AjaxCallBack_submitmodfiyUserinfo;
import com.contec.phms.login.LoginActivity;
import com.contec.phms.db.DeviceListDaoOperation;
import com.contec.phms.db.LoginUserDao;
import com.contec.phms.db.UserInfoDao;
import com.contec.phms.util.Constants;
import com.contec.phms.util.CustomDialog;
import com.contec.phms.util.PageUtil;
import com.contec.phms.util.PhmsSharedPreferences;
import com.contec.phms.util.SaveHospitalUtils;
import com.contec.phms.util.ToastUtils;
import com.contec.phms.widget.DialogClass;
import com.j256.ormlite.dao.Dao;
//import com.umeng.analytics.MobclickAgent;
import java.sql.SQLException;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Pattern;
import cn.com.contec_net_3_android.Meth_android_modifyUserinfo;
import cn.com.contec_net_3_android.Method_android_login;
import u.aly.bs;
import com.contec.R;

public class RegisterCardIdActivity extends Activity implements View.OnClickListener {
    private Intent _intent;
    private String cardId;
    private String carduserId;
    private CustomDialog dialog;
    private DialogClass keyBackDialog;
    private String mBirthday;
    private Button mBtn_back;
    private Button mBtn_register;
    DialogInterface.OnKeyListener mCancelLogin = new DialogInterface.OnKeyListener() {
        public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
            if (keyCode == 4 && event.getRepeatCount() == 0) {
                RegisterCardIdActivity.this.m_dialogClass.dismiss();
                RegisterCardIdActivity.this.miscancellogin = true;
            }
            return false;
        }
    };
    private EditText mEt_card;
    Handler mHandler = new Handler() {
        private DialogClass mupdatadialog;

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 2:
                    RegisterCardIdActivity.this.dismissDialog(1);
                    return;
                case 1990:
                    if (BluetoothAdapter.getDefaultAdapter().isEnabled()) {
                        BluetoothAdapter.getDefaultAdapter().disable();
                        return;
                    }
                    return;
                case 65536:
                    RegisterCardIdActivity.this.loginsuccess();
                    return;
                case Constants.LOGINFAILD /*65537*/:
                    RegisterCardIdActivity.this.loginfaild(RegisterCardIdActivity.this.getResources().getString(R.string.str_num_passwd_error));
                    return;
                case Constants.mloginargserror /*100201*/:
                    RegisterCardIdActivity.this.loginfaild(RegisterCardIdActivity.this.getResources().getString(R.string.loginargserror));
                    return;
                case Constants.mlogindberror /*100202*/:
                    RegisterCardIdActivity.this.loginfaild(RegisterCardIdActivity.this.getResources().getString(R.string.logindberror));
                    return;
                case Constants.mloginduserisnotexit /*100203*/:
                    RegisterCardIdActivity.this.loginfaild(RegisterCardIdActivity.this.getResources().getString(R.string.loginuserisnotexit));
                    return;
                case Constants.mlogindusernotcorrectpsw /*100204*/:
                    RegisterCardIdActivity.this.loginfaild(RegisterCardIdActivity.this.getResources().getString(R.string.loginpswnotcorrect));
                    return;
                case Constants.mloginduserstopuser /*100205*/:
                    RegisterCardIdActivity.this.loginfaild(RegisterCardIdActivity.this.getResources().getString(R.string.loginstopuser));
                    return;
                case Constants.mqueryuserinfotimeout /*104011*/:
                    RegisterCardIdActivity.this.loginfaild(RegisterCardIdActivity.this.getResources().getString(R.string.user_networktimeout));
                    return;
                case Constants.mqueryuserinfoneterror /*104012*/:
                    RegisterCardIdActivity.this.loginfaild(RegisterCardIdActivity.this.getResources().getString(R.string.user_networkerror));
                    return;
                default:
                    return;
            }
        }
    };
    private String mHeight;
    private final int mResultCode = 64;
    private String mSex;
    private String mWeight;
    private DialogClass m_dialogClass;
    private boolean miscancellogin = false;
    private LinearLayout mll_jump;
    private final int mloginargserror = Constants.mloginargserror;
    private final int mlogindberror = Constants.mlogindberror;
    private final int mloginduserisnotexit = Constants.mloginduserisnotexit;
    private final int mlogindusernotcorrectpsw = Constants.mlogindusernotcorrectpsw;
    private final int mloginduserstopuser = Constants.mloginduserstopuser;
    private Handler modifyHandler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int _flag = msg.what;
            if (_flag == 104400) {
                RegisterCardIdActivity.this.m_dialogClass.dismiss();
                RegisterCardIdActivity.this.startActivity(RegisterCardIdActivity.this._intent);
                RegisterCardIdActivity.this.finish();
            } else if (_flag == 104403) {
                RegisterCardIdActivity.this.m_dialogClass.dismiss();
                new DialogClass(RegisterCardIdActivity.this, RegisterCardIdActivity.this.getResources().getString(R.string.modofyinfroparaerror));
            } else if (_flag == 104402) {
                RegisterCardIdActivity.this.m_dialogClass.dismiss();
                new DialogClass(RegisterCardIdActivity.this, RegisterCardIdActivity.this.getResources().getString(R.string.modofyinfrodberror));
            }
        }
    };
    private final int mqueryuserinfoneterror = Constants.mqueryuserinfoneterror;
    private final int mqueryuserinfotimeout = Constants.mqueryuserinfotimeout;
    private String mstrPassword;
    private String mstrUsername;
    private boolean msuccessflag = false;
    private PhmsSharedPreferences sp;
    private Activity this$1 = this;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_register_card_layout);
        this.sp = PhmsSharedPreferences.getInstance(this);
        this.mBtn_back = (Button) findViewById(R.id.id_back_btn);
        this.mll_jump = (LinearLayout) findViewById(R.id.jump_but);
        this.mBtn_register = (Button) findViewById(R.id.register_success_btn);
        this.mEt_card = (EditText) findViewById(R.id.regist_cardid_edit);
        this.mll_jump.setOnClickListener(this);
        this.mBtn_back.setOnClickListener(this);
        this.mBtn_register.setOnClickListener(this);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.id_back_btn:
                startActivity(new Intent(this, RegisterBirthdayActivity.class));
                overridePendingTransition(R.anim.fragment_slide_right_enter, R.anim.fragment_slide_right_out);
                finish();
                return;
            case R.id.jump_but:
                this.cardId = bs.b;
                this.sp.saveColume("cardId", this.cardId);
                judgeCardID(this.mEt_card.getText().toString().trim());
                return;
            case R.id.register_success_btn:
                this.cardId = this.mEt_card.getText().toString().trim();
                this.sp.saveColume("cardId", this.cardId);
                judgeCardID(this.cardId);
                return;
            default:
                return;
        }
    }

    private void judgeCardID(String _cardId) {
        if (TextUtils.isEmpty(_cardId)) {
            doLogin();
        } else if (judgeUserIdCard(_cardId)) {
            doLogin();
        } else {
            ToastUtils.showToast(this, getResources().getString(R.string.please_input_right_idcard_mail));
        }
    }

    private boolean judgeUserIdCard(String _cardId) {
        return Pattern.compile("(^\\d{18}$)|(^\\d{15}$)").matcher(_cardId).matches();
    }

    private void doLogin() {
        App_phms.mCurrentActivity = 1;
        if (this.sp.getString("phoneNum") == null || this.sp.getString("phoneNum").equalsIgnoreCase(bs.b)) {
            this.carduserId = this.sp.getString("cardUserId");
        } else {
            this.mstrUsername = this.sp.getString("phoneNum");
            this.mstrPassword = this.mstrUsername.substring(this.mstrUsername.length() - 6, this.mstrUsername.length());
        }
        this.m_dialogClass = new DialogClass((Context) this, getString(R.string.lording), false, 0, this.mCancelLogin);
        AjaxCallBack_login _ajAjaxCallBack_login = new AjaxCallBack_login(this, this.mHandler);
        if (this.mstrUsername == null || this.mstrUsername.equalsIgnoreCase(bs.b)) {
            _ajAjaxCallBack_login.mUserID = this.carduserId;
            _ajAjaxCallBack_login.mPasswd = "123456";
        } else {
            _ajAjaxCallBack_login.mUserID = this.mstrUsername;
            _ajAjaxCallBack_login.mPasswd = this.mstrPassword;
        }
        LoginActivity.spUsername = _ajAjaxCallBack_login.mUserID;
        LoginActivity.spPassword = _ajAjaxCallBack_login.mPasswd;
        Method_android_login.login(_ajAjaxCallBack_login.mUserID, _ajAjaxCallBack_login.mPasswd, 0, _ajAjaxCallBack_login, App_phms.getInstance().mFinalHttp, String.valueOf(Constants.URL) + "/login.php");
        App_phms.mCurrentActivity = 1;
    }

    private void loginsuccess() {
        UserInfoDao retUser;
        new GetTime().execute(new String[0]);
        this.msuccessflag = true;
        if (this.mstrUsername == null || this.mstrUsername.equalsIgnoreCase(bs.b)) {
            PageUtil.saveUserNameTOSharePre(this, this.carduserId);
        } else {
            PageUtil.saveUserNameTOSharePre(this, this.mstrUsername);
        }
        LoginUserDao _loginUserInfo = PageUtil.getLoginUserInfo();
        saveHospital(_loginUserInfo);
        UserInfoDao user = new UserInfoDao();
        if (this.mstrUsername == null || this.mstrUsername.equalsIgnoreCase(bs.b)) {
            user.setmUserId(this.carduserId);
            this.mstrPassword = "123456";
            user.setPsw(this.mstrPassword);
        } else {
            user.setmUserId(this.mstrUsername);
            user.setPsw(this.mstrPassword);
        }
        user.setSavingMode(true);
        user.setLastLoginData(new Date());
        user.setmSex(_loginUserInfo.mSex);
        user.setmUserName(_loginUserInfo.mUserName);
        try {
            Dao<UserInfoDao, String> userDao = App_phms.getInstance().mHelper.getUserDao();
            if (this.mstrUsername == null || this.mstrUsername.equalsIgnoreCase(bs.b)) {
                retUser = userDao.queryForId(this.carduserId);
            } else {
                retUser = userDao.queryForId(this.mstrUsername);
            }
            if (retUser == null) {
                App_phms.getInstance().mUserInfo.mSearchInterval = 10;
                App_phms.getInstance().mUserInfo.mBluetoothState = 1;
                if (Constants.Language != null) {
                    App_phms.getInstance().mUserInfo.mLanguage = Constants.Language;
                    user.setmLanguage(Constants.Language);
                }
                user.setmSearchInterval(10);
                user.setmBluetoothState(1);
                userDao.create(user);
            } else {
                App_phms.getInstance().mUserInfo.mSearchInterval = retUser.getmSearchInterval();
                App_phms.getInstance().mUserInfo.mBluetoothState = retUser.getBluetoothstate();
                App_phms.getInstance().mUserInfo.mLanguage = retUser.getmLanguage();
                Constants.Language = App_phms.getInstance().mUserInfo.mLanguage;
                retUser.setmSex(App_phms.getInstance().mUserInfo.mSex);
                retUser.setmUserName(_loginUserInfo.mUserName);
                Constants.Language = App_phms.getInstance().mUserInfo.mLanguage;
                retUser.setLastLoginData(new Date());
                retUser.setPsw(this.mstrPassword);
                userDao.update(retUser);
            }
            PageUtil.getUserInfo();
            if (Constants.Language.contains("1")) {
                String _language = Locale.getDefault().getLanguage();
                Configuration config2 = this.this$1.getResources().getConfiguration();
                DisplayMetrics dm2 = this.this$1.getResources().getDisplayMetrics();
                if (_language.contains("zh")) {
                    config2.locale = Locale.CHINESE;
                } else if (_language.contains("en")) {
                    config2.locale = Locale.ENGLISH;
                }
                this.this$1.getResources().updateConfiguration(config2, dm2);
            } else if (Constants.Language.equals("en")) {
                Configuration config1 = this.this$1.getResources().getConfiguration();
                DisplayMetrics dm1 = this.this$1.getResources().getDisplayMetrics();
                config1.locale = Locale.ENGLISH;
                this.this$1.getResources().updateConfiguration(config1, dm1);
            } else if (Constants.Language.equals("zh")) {
                Configuration config = this.this$1.getResources().getConfiguration();
                DisplayMetrics dm = this.this$1.getResources().getDisplayMetrics();
                config.locale = Locale.CHINESE;
                this.this$1.getResources().updateConfiguration(config, dm);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        App_phms.getInstance().showBeans = DeviceListDaoOperation.getInstance().getDevice();
        SharedPreferences.Editor _editor = App_phms.getInstance().mCurrentloginUserInfo.edit();
        if (this.mstrUsername != null && !this.mstrUsername.equalsIgnoreCase(bs.b)) {
            _editor.putString("username", this.mstrUsername);
            _editor.putString("password", this.mstrPassword);
            _editor.commit();
        }
        this._intent = new Intent(this, MainActivityNew.class);
        if (this.mstrUsername != null && !this.mstrUsername.equalsIgnoreCase(bs.b)) {
            this._intent.putExtra("username", this.mstrUsername);
            this._intent.putExtra("password", this.mstrPassword);
            this._intent.putExtra("savepsw", true);
        }
        PhmsSharedPreferences.getInstance(this).saveColume("username", this.mstrUsername);
        try {
            Thread.sleep(400);
        } catch (InterruptedException e2) {
            e2.printStackTrace();
        }
        MainActivityNew.currentTab = 0;
        modifyUserinfo();
    }

    private void saveHospital(LoginUserDao _loginUserInfo) {
        String mUid = _loginUserInfo.mUID;
        if (TextUtils.isEmpty(PhmsSharedPreferences.getInstance(App_phms.getInstance()).getString(SaveHospitalUtils.spProvince + mUid))) {
            SaveHospitalUtils.saveDeafultHospitalInfo(mUid);
        }
    }

    private void modifyUserinfo() {
        this.mSex = this.sp.getString(UserInfoDao.Sex);
        this.mWeight = this.sp.getString("Weight");
        this.mHeight = this.sp.getString("Height");
        this.mBirthday = this.sp.getString("Birthday");
        if (this.cardId == null) {
            this.cardId = bs.b;
        }
        AjaxCallBack_submitmodfiyUserinfo mcallbacksubmitmofiyuserinfo = new AjaxCallBack_submitmodfiyUserinfo(this, this.modifyHandler);
        if (this.mstrUsername == null || this.mstrUsername.equalsIgnoreCase(bs.b)) {
            Meth_android_modifyUserinfo.modifyuserinfo(this.carduserId, "123456", App_phms.getInstance().mUserInfo.mPHPSession, this.cardId, bs.b, this.mBirthday, this.mSex, this.mHeight, this.mWeight, bs.b, mcallbacksubmitmofiyuserinfo, App_phms.getInstance().mFinalHttp, String.valueOf(Constants.URL) + "/main.php", bs.b);
        } else {
            Meth_android_modifyUserinfo.modifyuserinfo(this.mstrUsername, this.mstrPassword, App_phms.getInstance().mUserInfo.mPHPSession, this.cardId, bs.b, this.mBirthday, this.mSex, this.mHeight, this.mWeight, bs.b, mcallbacksubmitmofiyuserinfo, App_phms.getInstance().mFinalHttp, String.valueOf(Constants.URL) + "/main.php", bs.b);
        }
    }

    private void loginfaild(String pstr) {
        this.m_dialogClass.dismiss();
        new DialogClass(this, pstr);
    }

    private void logincancel() {
        this.m_dialogClass.dismiss();
    }

    private class GetTime extends AsyncTask<String, Void, String> {
        String _filetype = bs.b;
        private DialogClass mremovedialog;

        public GetTime() {
        }

        /* access modifiers changed from: protected */
        /* JADX WARNING: Removed duplicated region for block: B:18:0x0059  */
        /* JADX WARNING: Removed duplicated region for block: B:38:0x0175  */
        /* JADX WARNING: Removed duplicated region for block: B:43:0x0196  */
        /* JADX WARNING: Removed duplicated region for block: B:46:0x019e  */
        /* JADX WARNING: Removed duplicated region for block: B:60:? A[RETURN, SYNTHETIC] */
        /* JADX WARNING: Unknown top exception splitter block from list: {B:35:0x015a=Splitter:B:35:0x015a, B:40:0x017b=Splitter:B:40:0x017b, B:30:0x013c=Splitter:B:30:0x013c} */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public java.lang.String doInBackground(java.lang.String... r35) {
            /*
                r34 = this;
                r23 = 2
                r20 = 123(0x7b, float:1.72E-43)
                r29 = 3000(0xbb8, float:4.204E-42)
                r15 = 0
                java.lang.String r30 = "203.117.180.36"
                java.net.InetAddress r15 = java.net.InetAddress.getByName(r30)     // Catch:{ UnknownHostException -> 0x0067 }
            L_0x000d:
                r26 = -1
                r27 = 0
                r21 = -1
                java.net.DatagramSocket r28 = new java.net.DatagramSocket     // Catch:{ NoRouteToHostException -> 0x01ae, ConnectException -> 0x0159, IOException -> 0x017a }
                r28.<init>()     // Catch:{ NoRouteToHostException -> 0x01ae, ConnectException -> 0x0159, IOException -> 0x017a }
                r28.setSoTimeout(r29)     // Catch:{ NoRouteToHostException -> 0x0139, ConnectException -> 0x01aa, IOException -> 0x01a6, all -> 0x01a2 }
                r7 = 0
            L_0x001c:
                r0 = r23
                if (r7 > r0) goto L_0x0028
                r30 = 1
                r0 = r26
                r1 = r30
                if (r0 != r1) goto L_0x006c
            L_0x0028:
                if (r28 == 0) goto L_0x002d
                r28.close()
            L_0x002d:
                r27 = r28
            L_0x002f:
                java.util.Calendar r30 = java.util.Calendar.getInstance()
                r30.getTimeInMillis()
                r0 = r34
                com.contec.phms.activity.RegisterCardIdActivity r0 = com.contec.phms.activity.RegisterCardIdActivity.this
                r30 = r0
                com.contec.phms.util.PhmsSharedPreferences r5 = com.contec.phms.util.PhmsSharedPreferences.getInstance(r30)
                java.lang.String r30 = "LOCAL_TIME"
                java.util.Calendar r31 = java.util.Calendar.getInstance()
                long r31 = r31.getTimeInMillis()
                r0 = r30
                r1 = r31
                r5.saveLong(r0, r1)
                r30 = 1
                r0 = r26
                r1 = r30
                if (r0 == r1) goto L_0x0064
                java.lang.String r30 = "INTERNET_TIME"
                r31 = 0
                r0 = r30
                r1 = r31
                r5.saveLong(r0, r1)
            L_0x0064:
                r30 = 0
                return r30
            L_0x0067:
                r12 = move-exception
                r12.printStackTrace()
                goto L_0x000d
            L_0x006c:
                com.contec.phms.util.NtpMessage r30 = new com.contec.phms.util.NtpMessage     // Catch:{ InterruptedIOException -> 0x0121 }
                r30.<init>()     // Catch:{ InterruptedIOException -> 0x0121 }
                byte[] r8 = r30.toByteArray()     // Catch:{ InterruptedIOException -> 0x0121 }
                java.net.DatagramPacket r19 = new java.net.DatagramPacket     // Catch:{ InterruptedIOException -> 0x0121 }
                int r0 = r8.length     // Catch:{ InterruptedIOException -> 0x0121 }
                r30 = r0
                r0 = r19
                r1 = r30
                r2 = r20
                r0.<init>(r8, r1, r15, r2)     // Catch:{ InterruptedIOException -> 0x0121 }
                long r24 = java.lang.System.currentTimeMillis()     // Catch:{ InterruptedIOException -> 0x0121 }
                r0 = r28
                r1 = r19
                r0.send(r1)     // Catch:{ InterruptedIOException -> 0x0121 }
                java.net.DatagramPacket r14 = new java.net.DatagramPacket     // Catch:{ InterruptedIOException -> 0x0121 }
                int r0 = r8.length     // Catch:{ InterruptedIOException -> 0x0121 }
                r30 = r0
                r0 = r30
                r14.<init>(r8, r0)     // Catch:{ InterruptedIOException -> 0x0121 }
                r0 = r28
                r0.receive(r14)     // Catch:{ InterruptedIOException -> 0x0121 }
                long r30 = java.lang.System.currentTimeMillis()     // Catch:{ InterruptedIOException -> 0x0121 }
                long r21 = r30 - r24
                long r30 = java.lang.System.currentTimeMillis()     // Catch:{ InterruptedIOException -> 0x0121 }
                r0 = r30
                double r0 = (double) r0     // Catch:{ InterruptedIOException -> 0x0121 }
                r30 = r0
                r32 = 4652007308841189376(0x408f400000000000, double:1000.0)
                double r30 = r30 / r32
                r32 = 4746922992901029888(0x41e0754fd0000000, double:2.2089888E9)
                double r9 = r30 + r32
                com.contec.phms.util.NtpMessage r18 = new com.contec.phms.util.NtpMessage     // Catch:{ InterruptedIOException -> 0x0121 }
                byte[] r30 = r14.getData()     // Catch:{ InterruptedIOException -> 0x0121 }
                r0 = r18
                r1 = r30
                r0.<init>(r1)     // Catch:{ InterruptedIOException -> 0x0121 }
                r0 = r18
                double r0 = r0.receiveTimestamp     // Catch:{ InterruptedIOException -> 0x0121 }
                r30 = r0
                r0 = r18
                double r0 = r0.originateTimestamp     // Catch:{ InterruptedIOException -> 0x0121 }
                r32 = r0
                double r30 = r30 - r32
                r0 = r18
                double r0 = r0.transmitTimestamp     // Catch:{ InterruptedIOException -> 0x0121 }
                r32 = r0
                double r32 = r32 - r9
                double r30 = r30 + r32
                r32 = 4611686018427387904(0x4000000000000000, double:2.0)
                double r16 = r30 / r32
                byte[] r6 = r18.toByteArray()     // Catch:{ InterruptedIOException -> 0x0121 }
                java.io.PrintStream r30 = java.lang.System.out     // Catch:{ InterruptedIOException -> 0x0121 }
                r0 = r30
                r0.println(r6)     // Catch:{ InterruptedIOException -> 0x0121 }
                java.io.PrintStream r30 = java.lang.System.out     // Catch:{ InterruptedIOException -> 0x0121 }
                java.lang.StringBuilder r31 = new java.lang.StringBuilder     // Catch:{ InterruptedIOException -> 0x0121 }
                java.lang.String r32 = "poll: NTP message : "
                r31.<init>(r32)     // Catch:{ InterruptedIOException -> 0x0121 }
                java.lang.String r32 = r18.toString()     // Catch:{ InterruptedIOException -> 0x0121 }
                java.lang.StringBuilder r31 = r31.append(r32)     // Catch:{ InterruptedIOException -> 0x0121 }
                java.lang.String r31 = r31.toString()     // Catch:{ InterruptedIOException -> 0x0121 }
                r30.println(r31)     // Catch:{ InterruptedIOException -> 0x0121 }
                long r3 = r18.toLong()     // Catch:{ InterruptedIOException -> 0x0121 }
                r0 = r34
                com.contec.phms.activity.RegisterCardIdActivity r0 = com.contec.phms.activity.RegisterCardIdActivity.this     // Catch:{ InterruptedIOException -> 0x0121 }
                r30 = r0
                com.contec.phms.util.PhmsSharedPreferences r5 = com.contec.phms.util.PhmsSharedPreferences.getInstance(r30)     // Catch:{ InterruptedIOException -> 0x0121 }
                java.lang.String r30 = "INTERNET_TIME"
                r0 = r30
                r5.saveLong(r0, r3)     // Catch:{ InterruptedIOException -> 0x0121 }
                r26 = 1
            L_0x011d:
                int r7 = r7 + 1
                goto L_0x001c
            L_0x0121:
                r13 = move-exception
                java.io.PrintStream r30 = java.lang.System.out     // Catch:{ NoRouteToHostException -> 0x0139, ConnectException -> 0x01aa, IOException -> 0x01a6, all -> 0x01a2 }
                java.lang.StringBuilder r31 = new java.lang.StringBuilder     // Catch:{ NoRouteToHostException -> 0x0139, ConnectException -> 0x01aa, IOException -> 0x01a6, all -> 0x01a2 }
                java.lang.String r32 = "InterruptedIOException: "
                r31.<init>(r32)     // Catch:{ NoRouteToHostException -> 0x0139, ConnectException -> 0x01aa, IOException -> 0x01a6, all -> 0x01a2 }
                r0 = r31
                java.lang.StringBuilder r31 = r0.append(r15)     // Catch:{ NoRouteToHostException -> 0x0139, ConnectException -> 0x01aa, IOException -> 0x01a6, all -> 0x01a2 }
                java.lang.String r31 = r31.toString()     // Catch:{ NoRouteToHostException -> 0x0139, ConnectException -> 0x01aa, IOException -> 0x01a6, all -> 0x01a2 }
                r30.println(r31)     // Catch:{ NoRouteToHostException -> 0x0139, ConnectException -> 0x01aa, IOException -> 0x01a6, all -> 0x01a2 }
                goto L_0x011d
            L_0x0139:
                r11 = move-exception
                r27 = r28
            L_0x013c:
                java.io.PrintStream r30 = java.lang.System.out     // Catch:{ all -> 0x019b }
                java.lang.StringBuilder r31 = new java.lang.StringBuilder     // Catch:{ all -> 0x019b }
                java.lang.String r32 = "No route to host exception for address: "
                r31.<init>(r32)     // Catch:{ all -> 0x019b }
                r0 = r31
                java.lang.StringBuilder r31 = r0.append(r15)     // Catch:{ all -> 0x019b }
                java.lang.String r31 = r31.toString()     // Catch:{ all -> 0x019b }
                r30.println(r31)     // Catch:{ all -> 0x019b }
                if (r27 == 0) goto L_0x002f
                r27.close()
                goto L_0x002f
            L_0x0159:
                r11 = move-exception
            L_0x015a:
                r11.fillInStackTrace()     // Catch:{ all -> 0x019b }
                java.io.PrintStream r30 = java.lang.System.out     // Catch:{ all -> 0x019b }
                java.lang.StringBuilder r31 = new java.lang.StringBuilder     // Catch:{ all -> 0x019b }
                java.lang.String r32 = "Connection exception for address: "
                r31.<init>(r32)     // Catch:{ all -> 0x019b }
                r0 = r31
                java.lang.StringBuilder r31 = r0.append(r15)     // Catch:{ all -> 0x019b }
                java.lang.String r31 = r31.toString()     // Catch:{ all -> 0x019b }
                r30.println(r31)     // Catch:{ all -> 0x019b }
                if (r27 == 0) goto L_0x002f
                r27.close()
                goto L_0x002f
            L_0x017a:
                r13 = move-exception
            L_0x017b:
                r13.fillInStackTrace()     // Catch:{ all -> 0x019b }
                java.io.PrintStream r30 = java.lang.System.out     // Catch:{ all -> 0x019b }
                java.lang.StringBuilder r31 = new java.lang.StringBuilder     // Catch:{ all -> 0x019b }
                java.lang.String r32 = "IOException while polling address: "
                r31.<init>(r32)     // Catch:{ all -> 0x019b }
                r0 = r31
                java.lang.StringBuilder r31 = r0.append(r15)     // Catch:{ all -> 0x019b }
                java.lang.String r31 = r31.toString()     // Catch:{ all -> 0x019b }
                r30.println(r31)     // Catch:{ all -> 0x019b }
                if (r27 == 0) goto L_0x002f
                r27.close()
                goto L_0x002f
            L_0x019b:
                r30 = move-exception
            L_0x019c:
                if (r27 == 0) goto L_0x01a1
                r27.close()
            L_0x01a1:
                throw r30
            L_0x01a2:
                r30 = move-exception
                r27 = r28
                goto L_0x019c
            L_0x01a6:
                r13 = move-exception
                r27 = r28
                goto L_0x017b
            L_0x01aa:
                r11 = move-exception
                r27 = r28
                goto L_0x015a
            L_0x01ae:
                r11 = move-exception
                goto L_0x013c
            */
            throw new UnsupportedOperationException("Method not decompiled: com.contec.phms.activity.RegisterCardIdActivity.GetTime.doInBackground(java.lang.String[]):java.lang.String");
        }

        protected void onCancelled() {
            super.onCancelled();
        }

        protected void onPostExecute(String result) {
        }

        protected void onPreExecute() {
        }

        protected void onProgressUpdate(Void... values) {
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode != 4) {
            return false;
        }
        CustomDialog.Builder customBuilder = new CustomDialog.Builder(this);
        customBuilder.setTitle(getString(R.string.str_backup)).setMessage(getString(R.string.str_backup_content)).setNegativeButton(getString(R.string.ok), (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        }).setPositiveButton(getString(R.string.cancel), (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                switch (which) {
                    case -2:
                        Intent _intent = new Intent(RegisterCardIdActivity.this, LoginActivity.class);
                        Bundle bundle = new Bundle();
                        if (RegisterCardIdActivity.this.sp.getString("phoneNum") == null || RegisterCardIdActivity.this.sp.getString("phoneNum").equalsIgnoreCase(bs.b)) {
                            String string = RegisterCardIdActivity.this.sp.getString("cardUserId");
                            bundle.putString("user", bs.b);
                        } else {
                            bundle.putString("user", RegisterCardIdActivity.this.sp.getString("phoneNum"));
                        }
                        bundle.putBoolean("isfromregist", true);
                        _intent.putExtras(bundle);
                        RegisterCardIdActivity.this.startActivity(_intent);
                        RegisterCardIdActivity.this.finish();
                        return;
                    default:
                        return;
                }
            }
        });
        this.dialog = customBuilder.create();
        this.dialog.show();
        return false;
    }

    protected void onResume() {
        super.onResume();
        //MobclickAgent.onResume(this);
    }

    protected void onPause() {
        super.onPause();
        //MobclickAgent.onPause(this);
    }
}
