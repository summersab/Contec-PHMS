package com.contec.phms.login;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import androidx.fragment.app.FragmentActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.contec.phms.App_phms;
import com.contec.phms.R;
import com.contec.phms.SearchDevice.SearchDevice;
import com.contec.phms.Server_Main;
import com.contec.phms.activity.ActivityNewUserLead;
import com.contec.phms.activity.Activitynopsw;
import com.contec.phms.activity.MainActivityNew;
import com.contec.phms.activity.ProductDataActivity;
import com.contec.phms.activity.RegisterMailActivity;
import com.contec.phms.activity.RegisterSelectActivity;
import com.contec.phms.ajaxcallback.AjaxCallBack_login;
import com.contec.phms.ajaxcallback.AjaxCallBack_registNewUser;
import com.contec.phms.device.template.DataFilter;
import com.contec.phms.device.template.DeviceService;
import com.contec.phms.log.SaveLog;
import com.contec.phms.manager.device.DeviceManager;
import com.contec.phms.manager.message.InstantMessageService;
import com.contec.phms.manager.message.MessageManager;
import com.contec.phms.db.DeviceListDaoOperation;
import com.contec.phms.db.LocalLoginInfoDao;
import com.contec.phms.db.LocalLoginInfoManager;
import com.contec.phms.db.LoginUserDao;
import com.contec.phms.db.UserInfoDao;
import com.contec.phms.upload.UploadService;
import com.contec.phms.util.AlertDialogUtil;
import com.contec.phms.util.CLog;
import com.contec.phms.util.CheckUpdateProduct;
import com.contec.phms.util.Constants;
import com.contec.phms.util.FileOperation;
import com.contec.phms.util.NtpMessage;
import com.contec.phms.util.PageUtil;
import com.contec.phms.util.PedometerSharepreferance;
import com.contec.phms.util.PhmsSharedPreferences;
import com.contec.phms.util.SaveHospitalUtils;
import com.contec.phms.util.ScreenAdapter;
import com.contec.phms.util.UpdateManeger;
import com.contec.phms.widget.DialogClass;
import com.contec.phms.widget.ProgressbarCustom;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
//import com.umeng.analytics.MobclickAgent;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.net.ConnectException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.NoRouteToHostException;
import java.net.UnknownHostException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;
import javax.xml.parsers.DocumentBuilderFactory;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import cn.com.contec_net_3_android.Meth_android_registNewUser;
import cn.com.contec_net_3_android.Method_android_login;
//import cn.sharesdk.facebook.Facebook;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.PlatformDb;
//import cn.sharesdk.framework.ShareSDK;
//import cn.sharesdk.google.GooglePlus;
//import cn.sharesdk.sina.weibo.SinaWeibo;
//import cn.sharesdk.tencent.p008qq.QQ;
//import cn.sharesdk.wechat.friends.Wechat;
import de.greenrobot.event.EventBus;
import u.aly.bs;

@SuppressLint({"InlinedApi"})
public class LoginActivity extends FragmentActivity implements Runnable, View.OnClickListener {
    public static LoginActivity mLoginActivity;
    public static String spPassword;
    public static String spUsername;
    private final App_phms MAPP_PHMS_INSTANCE = App_phms.getInstance();
    private int SCREENHEIGH;
    private int SCREENWEIGH;
    String TAG = LoginActivity.class.getSimpleName();
    private boolean _auto_log;
    private boolean _bAuto;
    private String _defaultpassword;
    private HashMap<String, String> _getreturnparams;
    private Intent _intent;
    private String _smsverifystr;
    private Dao<UserInfoDao, String> _userdao;
    private AlertDialog agreementDialog;
    private TextView auto_threelogin;
/*
    private Platform facebook;
    PlatformActionListener facebookPaListener = new PlatformActionListener() {
        public void onCancel(Platform platform, int arg1) {
            platform.removeAccount(true);
            Message msg = new Message();
            msg.what = 106981;
            LoginActivity.this.mHandler.sendMessage(msg);
        }

        public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
            if (i == 8) {
                PlatformDb platDB = platform.getDb();
                LoginActivity.this.mThirdUserName = platDB.getUserName();
                Log.e("=============", "==============");
                Log.e("facebook userid", "====" + platDB.getUserId());
                Log.e("facebook username", "====" + platDB.getUserName());
                Log.e("facebook user platform name", "====" + platDB.getPlatformNname());
                Log.e("===============", "=============");
                LoginActivity.this.mThirdcode = platDB.getUserId();
                Message msg = new Message();
                msg.what = 106980;
                msg.obj = String.valueOf(platDB.getUserId()) + Constants.DOUHAO + platDB.getUserName();
                LoginActivity.this.mHandler.sendMessage(msg);
            }
        }

        public void onError(Platform arg0, int arg1, Throwable arg2) {
            Message msg = new Message();
            msg.what = 106982;
            LoginActivity.this.mHandler.sendMessage(msg);
        }
    };
    private Platform gooleplus;
    PlatformActionListener gooleplusPaListener = new PlatformActionListener() {
        public void onCancel(Platform platform, int arg1) {
            platform.removeAccount(true);
            Message msg = new Message();
            msg.what = 106981;
            LoginActivity.this.mHandler.sendMessage(msg);
        }

        public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
            if (i == 8) {
                PlatformDb platDB = platform.getDb();
                LoginActivity.this.mThirdUserName = platDB.getUserName();
                Log.e("=============", "==============");
                Log.e("googleplus userid", "====" + platDB.getUserId());
                Log.e("googleplus username", "====" + platDB.getUserName());
                Log.e("googleplus user platform name", "====" + platDB.getPlatformNname());
                Log.e("===============", "=============");
                LoginActivity.this.mThirdcode = platDB.getUserId();
                Message msg = new Message();
                msg.what = 106980;
                msg.obj = String.valueOf(platDB.getUserId()) + Constants.DOUHAO + platDB.getUserName();
                LoginActivity.this.mHandler.sendMessage(msg);
            }
        }

        public void onError(Platform arg0, int arg1, Throwable arg2) {
            arg2.printStackTrace();
            String expName = arg2.getClass().getSimpleName();
            Log.e("========", "========" + expName);
            if ("GooglePlusClientNotExistException".equals(expName) || "GooglePlusClientNotExistException".equals(expName) || "GooglePlusClientNotExistException".equals(expName)) {
                Message msg = new Message();
                msg.what = 106984;
                LoginActivity.this.mHandler.sendMessage(msg);
                return;
            }
            Message msg2 = new Message();
            msg2.what = 106982;
            LoginActivity.this.mHandler.sendMessage(msg2);
        }
    };
*/
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    Intent intent = new Intent();
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.setAction("android.intent.action.VIEW");
                    intent.setDataAndType(Uri.fromFile((File) msg.obj), "application/vnd.android.package-archive");
                    LoginActivity.this.mContext.startActivity(intent);
                    return;
                default:
                    return;
            }
        }
    };
    private Boolean hasTask = false;
    private Boolean isExit = false;
    private boolean isLoginButton;
    private AlertDialog mAlertDialogloadapking;
    DialogInterface.OnKeyListener mCancelLogin = new DialogInterface.OnKeyListener() {
        public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
            if (keyCode == 4 && event.getRepeatCount() == 0) {
                LoginActivity.this.m_dialogClass.dismiss();
                LoginActivity.this.miscancellogin = true;
            }
            return false;
        }
    };
    private int mClickTimes;
    private Context mContext;
    private SharedPreferences mCurrentloginUserInfo;
    private String mDefaultName;
    private String mDefaultPassword;
    private DialogClass mDialogClass;
//    private ImageView mFaceBookLogin;
//    private ImageView mGoogleLogin;
    private Handler mHandler;
    private boolean mIfAddUser = false;
    private RelativeLayout mImageView_distances1;
    private String mLanguage;
    private RelativeLayout mLayout_Title;
    private AutoCompleteTextView mLogName;
    private EditText mLogPass;
    private Button mLoginButton;
    private View.OnClickListener mLoginButtonOnClickEvent = new View.OnClickListener() {
        public void onClick(View v) {
            LoginActivity.this.isLoginButton = true;
            if (!PageUtil.checkSDCard(LoginActivity.this)) {
                LoginActivity.this.noSDCardExit();
            } else if (PageUtil.checkNet(LoginActivity.this)) {
                InstantMessageService.stopServer(LoginActivity.this);
                LoginActivity.this.doLogin();
            } else {
                new DialogClass(LoginActivity.this, LoginActivity.this.getResources().getString(R.string.net_disable));
            }
        }
    };
    private TextWatcher mLoginNameTextWatcher = new TextWatcher() {
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void afterTextChanged(Editable s) {
            CLog.e("0000", "00000");
            LoginActivity.this.mLogPass.setText(bs.b);
            if (s.length() > 0) {
                LoginActivity.this.mloginphonenumdelbtn.setVisibility(View.VISIBLE);
            } else {
                LoginActivity.this.mloginphonenumdelbtn.setVisibility(View.INVISIBLE);
            }
        }
    };
    private int mLoginResult;
    private TextView mLoginTitle;
    private String mLoginUserId;
    private MainHandler mMainHandler;
    private final int mNetwoNotgood = Constants.NETWROK_NOT_GOOD;
    private View.OnClickListener mNoSdCardButtonListener = new View.OnClickListener() {
        public void onClick(View v) {
            Intent startMain = new Intent("android.intent.action.MAIN");
            startMain.addCategory("android.intent.category.HOME");
            startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            LoginActivity.this.startActivity(startMain);
            System.exit(0);
        }
    };
    private View.OnFocusChangeListener mOnLogNameFocusChangeListener = new View.OnFocusChangeListener() {
        public void onFocusChange(View v, boolean hasFocus) {
            if (!hasFocus) {
                String _logName = LoginActivity.this.mLogName.getText().toString();
                if (Constants.TestFlag && PageUtil.isPersonalUser(_logName, LoginActivity.this)) {
                    LoginActivity.this.mLogName.setText(LoginActivity.this.mDefaultName);
                }
            }
        }
    };
    private ProgressDialog mProgressDialog;
    private ImageView mQQLogin;
    private Button mRegisterButton;
    private final int mResetPwd = 65;
    private View.OnClickListener mResgisterButtonOnClickEvent = new View.OnClickListener() {
        public void onClick(View v) {
            if (!PageUtil.checkNet(LoginActivity.this)) {
                new DialogClass(LoginActivity.this, LoginActivity.this.getResources().getString(R.string.net_disable));
            } else if (Constants.Language.equals("zh") || Constants.Language.equals("1zh")) {
                LoginActivity.this.agreementDialog = AlertDialogUtil.MyTwoButtonDialog(LoginActivity.this, LoginActivity.this.getResources().getString(R.string.agreement_dialog_title), 0, LoginActivity.this.getResources().getString(R.string.agreement_dialog_content), LoginActivity.this.getResources().getString(R.string.agreement_dialog_agree), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        LoginActivity.this.startActivity(new Intent(LoginActivity.this, RegisterSelectActivity.class));
                    }
                }, LoginActivity.this.getResources().getString(R.string.agreement_dialog_unagree), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }, true);
            } else {
                LoginActivity.this.startActivity(new Intent(LoginActivity.this, RegisterSelectActivity.class));
            }
        }
    };
    private final int mResultCode = 64;
    public SaveLog mSaveLog;
    private Button mSetLanguage;
    private TextView mTextAccount;
    private TextView mTextPwd;
    private Context mThemeContext;
    private String mThirdUserName;
    private String mThirdcode;
    private Thread mThread;
    private UpdateManeger mUpdateManeger;
//    private ImageView mWeChatLogin;
//    private ImageView mWeiboLogin;
    private boolean m_bChangeUser;
    private boolean m_bLoginFailed;
    private CheckBox m_cbxAuto;
    private DialogClass m_dialogClass;
    private ImageView m_distances;
    private RelativeLayout m_layout_login_activity_bg;
    private List<UserInfoDao> m_users;
    private Vector<Boolean> m_vAuto = new Vector<>();
    private Vector<String> m_vPsw = new Vector<>();
    private Vector<Boolean> m_vSavePsw = new Vector<>();
    private Vector<String> m_vUsername = new Vector<>();
    DialogInterface.OnKeyListener mdialogkeylistener = new DialogInterface.OnKeyListener() {
        public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
            if (keyCode == 4 && event.getRepeatCount() == 0) {
                UpdateManeger.cancelUpdate = true;
            }
            return false;
        }
    };
    private ProgressbarCustom mdownlaodprogressbar;
    private TextView mdownloadapk_progresstv;
    private TextView mfindpasswrd;
    private int mheight_scale = 0;
    private boolean miscancellogin = false;
    private RelativeLayout mlogin_bg;
    private final int mloginargserror = Constants.mloginargserror;
    private final int mlogindberror = Constants.mlogindberror;
    private final int mloginduserisnotexit = Constants.mloginduserisnotexit;
    private final int mlogindusernotcorrectpsw = Constants.mlogindusernotcorrectpsw;
    private final int mloginduserstopuser = Constants.mloginduserstopuser;
    private PopupWindow mloginfailPop;
    private Button mloginphonenumdelbtn;
    private final int mqueryuserinfoneterror = Constants.mqueryuserinfoneterror;
    private final int mqueryuserinfotimeout = Constants.mqueryuserinfotimeout;
    private final int mregisteroAuthError = 106981;
    private final int mregisteroAuthFaile = 106982;
    private final int mregisteroAuthGoogleNotExist = 106984;
    private final int mregisteroAuthNotExist = 106983;
    private final int mregisteroAuthsuccess = 106980;
    private final int mregistnewUsersuccess = 106900;
    private String mstrPassword;
    private String mstrUsername;
    private boolean msuccessflag = false;
    private double mwidth_scale = 0.0d;
    private AlertDialog myDialog;
    private UserInfoDao oneuser;
    private Platform qq;
    @SuppressLint({"ShowToast"})
    PlatformActionListener qqPaListener = new PlatformActionListener() {
        public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
            if (i == 8) {
                PlatformDb platDB = platform.getDb();
                LoginActivity.this.mThirdcode = platDB.getUserId();
                LoginActivity.this.mThirdUserName = platDB.getUserName();
                Log.e("=============", "==============");
                Log.e("QQ userid", "====" + platDB.getUserId());
                Log.e("QQ username", "====" + platDB.getUserName());
                Log.e("QQ platform name", "====" + platDB.getPlatformNname());
                Log.e("=============", "==============");
                Message msg = new Message();
                msg.what = 106980;
                msg.obj = String.valueOf(platDB.getUserId()) + Constants.DOUHAO + platDB.getUserName();
                LoginActivity.this.mHandler.sendMessage(msg);
            }
        }

        public void onError(Platform platform, int i, Throwable throwable) {
            Message msg = new Message();
            msg.what = 106982;
            LoginActivity.this.mHandler.sendMessage(msg);
        }

        public void onCancel(Platform platform, int i) {
            platform.removeAccount(true);
            Message msg = new Message();
            msg.what = 106981;
            LoginActivity.this.mHandler.sendMessage(msg);
        }
    };

    private SharedPreferences sp;
    Timer tExit = null;
    private Activity this$1 = this;
    private List<UserInfoDao> users;
/*
    private Platform wechat;
    PlatformActionListener wechatPaListener = new PlatformActionListener() {
        public void onCancel(Platform platform, int arg1) {
            Log.e("=============", "==============");
            Log.e("=======onCancel======", "执行当前取消授权");
            Log.e("=============", "==============");
            platform.removeAccount(true);
            Message msg = new Message();
            msg.what = 106981;
            LoginActivity.this.mHandler.sendMessage(msg);
        }

        public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
            Log.e("=============", "==============onComplete()");
            if (i == 8) {
                PlatformDb platDB = platform.getDb();
                LoginActivity.this.mThirdUserName = platDB.getUserName();
                Log.e("=============", "==============");
                Log.e("userid", "====" + platDB.getUserId());
                Log.e("username", "====" + platDB.getUserName());
                Log.e("platform name", "====" + platDB.getPlatformNname());
                Log.e("=============", "==============");
                LoginActivity.this.mThirdcode = platDB.getUserId();
                Message msg = new Message();
                msg.what = 106980;
                msg.obj = String.valueOf(platDB.getUserId()) + Constants.DOUHAO + platDB.getUserName();
                LoginActivity.this.mHandler.sendMessage(msg);
            }
        }

        public void onError(Platform arg0, int arg1, Throwable arg2) {
            arg2.printStackTrace();
            String expName = arg2.getClass().getSimpleName();
            if ("WechatClientNotExistException".equals(expName) || "WechatTimelineNotSupportedException".equals(expName) || "WechatFavoriteNotSupportedException".equals(expName)) {
                Message msg = new Message();
                msg.what = 106983;
                LoginActivity.this.mHandler.sendMessage(msg);
                return;
            }
            Message msg2 = new Message();
            msg2.what = 106982;
            LoginActivity.this.mHandler.sendMessage(msg2);
        }
    };
    private Platform weibo;
    @SuppressLint({"ShowToast"})
    PlatformActionListener weiboPaListener = new PlatformActionListener() {
        public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
            if (i == 8) {
                PlatformDb platDB = platform.getDb();
                LoginActivity.this.mThirdcode = platDB.getUserId();
                LoginActivity.this.mThirdUserName = platDB.getUserName();
                Log.e("=============", "==============");
                Log.e("userid", "====" + platDB.getUserId());
                Log.e("username", "====" + platDB.getUserName());
                Log.e("platform name", "====" + platDB.getPlatformNname());
                Log.e("=============", "==============");
                Message msg = new Message();
                msg.what = 106980;
                msg.obj = String.valueOf(platDB.getUserId()) + Constants.DOUHAO + platDB.getUserName();
                LoginActivity.this.mHandler.sendMessage(msg);
            }
        }

        public void onError(Platform platform, int i, Throwable throwable) {
            Message msg = new Message();
            msg.what = 106982;
            LoginActivity.this.mHandler.sendMessage(msg);
        }

        public void onCancel(Platform platform, int i) {
            platform.removeAccount(true);
            Message msg = new Message();
            msg.what = 106981;
            LoginActivity.this.mHandler.sendMessage(msg);
        }
    };
*/

    public static LoginActivity getInterface() {
        if (mLoginActivity == null) {
            return new LoginActivity();
        }
        return mLoginActivity;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CLog.i(this.TAG, "call onCreate method");
        EventBus.getDefault().register(this);
        this.mContext = this;
        //ShareSDK.initSDK(this);
        App_phms.getInstance().clearActivity();
        this.mLanguage = PhmsSharedPreferences.getInstance(this.mContext).getString(UserInfoDao.Language, bs.b);
        if (!PhmsSharedPreferences.getInstance(this).getBoolean(Constants.KEY_LOGINED)) {
            PhmsSharedPreferences.getInstance(this).saveBoolean(Constants.KEY_LOGINED, true);
            FileOperation.deleteUploadFiledFile();
        }
        InstantMessageService.stopServer(this);
        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        CLog.i("screendpi", "density = " + metric.density + "   densitydpi = " + metric.densityDpi);
        Constants.SCREEN_WIDTH = metric.widthPixels;
        Constants.SCREEN_HEIGHT = metric.heightPixels;
        inintLoginAll();
        String name = this.mLogName.getText().toString();
        CLog.e("mLogName", name);
        if (LocalLoginInfoManager.getInstance().querySqlThirdCode(name)) {
            LocalLoginInfoDao dao = LocalLoginInfoManager.getInstance().findByCardId(name);
            String thridName = dao.mThirdCode;
            CLog.e("thridName", thridName);
            if (thridName.contains("@")) {
                this.mLogName.setText(thridName);
                this.mLogPass.setText(dao.mPassword);
                CLog.e("thridName1", thridName);
            } else {
                this.mLogName.setText(bs.b);
                this.mLogPass.setText(bs.b);
                CLog.e("thridName2", thridName);
            }
        }
        CLog.e("LoginTest", "haidhfihdfihaishf");
        if (getIntent() != null) {
            String getmail = getIntent().getStringExtra("frommailregist");
            CLog.e("LoginTest", getmail);
            if (getmail != null && !getmail.equals(bs.b)) {
                this.mLogName.setText(getmail);
            }
        }
    }

    private void downloadProductResources() {
        if (!CheckUpdateProduct.isNet(this)) {
            Toast.makeText(this, "Network disconnected", Toast.LENGTH_SHORT).show();
            return;
        }
        Log.i("hxk", "local img version: " + queryImg());
        Log.i("hxk", "local html version: " + queryHtml());
        new AsyncTask<String, Void, String>() {
            protected String doInBackground(String... params) {
                try {
                    JSONObject obj = new JSONObject(EntityUtils.toString(new DefaultHttpClient().execute(new HttpGet(params[0])).getEntity()));
                    int img_verson = obj.getInt("image");
                    int html_verson = obj.getInt("html");
                    Log.i("hxk", "remote img version: " + img_verson);
                    Log.i("hxk", "remote html version: " + html_verson);
                    return String.valueOf(img_verson) + "-" + html_verson;
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }

            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                if (result != null) {
                    Log.i("hxk", result);
                    String[] versons = result.split("-");
                    String[] imgNames = ProductDataActivity.imgs;
                    String[] htmlNames = ProductDataActivity.htmls;
                    File file = new File(String.valueOf(CheckUpdateProduct.LOCAL_HTMLPATH) + "base.css");
                    Log.i("hxk", "base.css file exists: " + file.exists());
                    if (!file.exists()) {
                        CheckUpdateProduct.downloadProductDetail(String.valueOf("http://www.contec365.com/productapp/product/") + "base.css", "base.css", LoginActivity.this);
                    }
                    if (Integer.valueOf(versons[0]).intValue() > LoginActivity.this.queryImg()) {
                        for (int i = 0; i < imgNames.length; i++) {
                            CheckUpdateProduct.downloadProductDetail(String.valueOf("http://www.contec365.com/productapp/product/images/") + imgNames[i], imgNames[i], LoginActivity.this);
                        }
                        LoginActivity.this.saveImgVerson(Integer.valueOf(versons[0]).intValue());
                    }
                    if (Integer.valueOf(versons[1]).intValue() > LoginActivity.this.queryHtml()) {
                        for (int i2 = 0; i2 < htmlNames.length; i2++) {
                            CheckUpdateProduct.downloadProductDetail(String.valueOf("http://www.contec365.com/productapp/product/") + htmlNames[i2], htmlNames[i2], LoginActivity.this);
                        }
                        LoginActivity.this.saveHtmlVerson(Integer.valueOf(versons[1]).intValue());
                    }
                }
            }
        }.execute(new String[]{"http://www.contec365.com/productapp/product/version.json"});
    }

    public void saveImgVerson(int imgVerson) {
        this.sp = getSharedPreferences("img_html", 0);
        SharedPreferences.Editor editor = this.sp.edit();
        editor.putInt("img", imgVerson);
        editor.commit();
    }

    public void saveHtmlVerson(int htmlVerson) {
        this.sp = getSharedPreferences("img_html", 0);
        SharedPreferences.Editor editor = this.sp.edit();
        editor.putInt("html", htmlVerson);
        editor.commit();
    }

    public int queryImg() {
        this.sp = getSharedPreferences("img_html", 0);
        return this.sp.getInt("img", 0);
    }

    public int queryHtml() {
        this.sp = getSharedPreferences("img_html", 0);
        return this.sp.getInt("html", 0);
    }

    public static String getSDPath() {
        if (Environment.getExternalStorageState().equals("mounted")) {
            return Environment.getExternalStorageDirectory().toString();
        }
        return null;
    }

    private boolean isAvilible(Context context, String packageName) {
        List<PackageInfo> pinfo = context.getPackageManager().getInstalledPackages(0);
        List<String> pName = new ArrayList<>();
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                pName.add(pinfo.get(i).packageName);
            }
        }
        return pName.contains(packageName);
    }

    public boolean copyApkFromAssets(Context context) {
        try {
            InputStream is = context.getAssets().open("reader.apk");
            File file = new File(String.valueOf(getSDPath()) + "/copy_enter.apk");
            if (!file.exists()) {
                file.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(file);
            byte[] temp = new byte[1024];
            while (true) {
                int i = is.read(temp);
                if (i == -1) {
                    break;
                }
                fos.write(temp, 0, i);
            }
            fos.flush();
            fos.close();
            this.handler.sendMessage(this.handler.obtainMessage(1, file));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void inintLoginAll() {
        ArrayAdapter<String> _nameAdapter;
        UserInfoDao lastLoginUser;
        Constants.Persent = (float) (Constants.SCREEN_WIDTH / 480);
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter != null) {
            Constants.OPEN_BLUE = !mBluetoothAdapter.isEnabled();
        }
        String[] _url = PageUtil.getServerNameFromSharePre(this);
        Constants.URL = _url[0];
        Constants.URL_REPORT = _url[1];
        Constants.REPORT = false;
        if (getIntent() != null) {
            this.mIfAddUser = getIntent().getBooleanExtra("isAddId", false);
        }
        queryUserInfo();
        if (!(this.users == null || this.users.size() == 0 || (lastLoginUser = this.users.get(0)) == null || lastLoginUser.equals(bs.b))) {
            App_phms.getInstance().mUserInfo.mUserID = lastLoginUser.getmUserId();
            App_phms.getInstance().mUserInfo.mUserName = new PedometerSharepreferance(this).getUserName();
            App_phms.getInstance().mUserInfo.mPassword = lastLoginUser.getPsw();
            App_phms.getInstance().mUserInfo.mSearchInterval = lastLoginUser.getmSearchInterval();
            App_phms.getInstance().mUserInfo.mSex = lastLoginUser.getmSex();
        }
        Constants.USER_EXIT_BEFORE = 0;
        if (this.mLanguage == null || this.mLanguage.equals(bs.b) || this.mLanguage.contains("1")) {
            this.mLanguage = "1" + Locale.getDefault().getLanguage();
        }
        Constants.Language = this.mLanguage;
        if (this.mLanguage.equalsIgnoreCase("zh")) {
            Configuration config = getResources().getConfiguration();
            DisplayMetrics dm = getResources().getDisplayMetrics();
            config.locale = Locale.CHINESE;
            getResources().updateConfiguration(config, dm);
        } else if (this.mLanguage.equalsIgnoreCase("en")) {
            Configuration config2 = getResources().getConfiguration();
            DisplayMetrics dm2 = getResources().getDisplayMetrics();
            config2.locale = Locale.ENGLISH;
            getResources().updateConfiguration(config2, dm2);
        }
        setContentView(R.layout.layout_login);
        App_phms.mCurrentActivity = 0;
        Constants.Electrocardiogram = PhmsSharedPreferences.getInstance(App_phms.getInstance().getApplicationContext()).getString("Electrocardiogram", "auto");
        init_view();
        if (Constants.IS_PAD_NEW) {
            ScreenAdapter.changeLayoutTextSize(this, this.mlogin_bg, 10);
        }
        this._intent = new Intent(this, MainActivityNew.class);
        this.mMainHandler = new MainHandler();
        this.mHandler = new Handler() {
            private DialogClass mupdatadialog;

            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 1:
                        LoginActivity.this.mProgressDialog.setProgress(msg.arg1);
                        LoginActivity.this.mProgressDialog.setMax(msg.arg2);
                        return;
                    case 2:
                        LoginActivity.this.dismissDialog(1);
                        return;
                    case 3:
                        ((InputMethodManager) LoginActivity.this.getSystemService(INPUT_METHOD_SERVICE)).toggleSoftInput(0, 2);
                        if (1 != 0) {
                            this.mupdatadialog = new DialogClass((Context) LoginActivity.this, (String) LoginActivity.this._getreturnparams.get("versioninfo"), (String) LoginActivity.this._getreturnparams.get("content"), (View.OnClickListener) new View.OnClickListener() {
                                public void onClick(View v) {
                                    LoginActivity.this.mAlertDialogloadapking = LoginActivity.this.UpdataDialogClass(LoginActivity.this);
                                    LoginActivity.this.mAlertDialogloadapking.show();
                                    LoginActivity.this.mUpdateManeger.downloadApk();
                                    //C077515.this.mupdatadialog.dismiss();
                                }
                            });
                            return;
                        }
                        LoginActivity.this.UpdataDialogClass(LoginActivity.this).show();
                        LoginActivity.this.mUpdateManeger.downloadApk();
                        return;
                    case 4:
                        LoginActivity.this._intent.putExtra("isupdate", msg.arg1);
                        return;
                    case 222:
                        LoginActivity.this.mdownlaodprogressbar.setProgress(msg.arg1);
                        LoginActivity.this.mdownloadapk_progresstv.setText(String.valueOf(msg.arg1) + "%");
                        return;
                    case 333:
                        try {
                            Thread.currentThread();
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        LoginActivity.this.mAlertDialogloadapking.dismiss();
                        LoginActivity.this.mUpdateManeger.installApk();
                        return;
                    case 1990:
                        if (BluetoothAdapter.getDefaultAdapter().isEnabled()) {
                            BluetoothAdapter.getDefaultAdapter().disable();
                            return;
                        }
                        return;
                    case 65536:
                        LoginActivity.this.loginsuccess();
                        LoginActivity.this.downloadProductResources();
                        return;
                    case Constants.LOGINFAILD /*65537*/:
                        LoginActivity.this.loginfaild(LoginActivity.this.getResources().getString(R.string.str_num_passwd_error));
                        return;
                    case Constants.mloginargserror /*100201*/:
                        LoginActivity.this.loginfaild(LoginActivity.this.getResources().getString(R.string.loginargserror));
                        return;
                    case Constants.mlogindberror /*100202*/:
                        LoginActivity.this.loginfaild(LoginActivity.this.getResources().getString(R.string.logindberror));
                        return;
                    case Constants.mloginduserisnotexit /*100203*/:
                        LoginActivity.this.loginfaild(LoginActivity.this.getResources().getString(R.string.loginuserisnotexit));
                        return;
                    case Constants.mlogindusernotcorrectpsw /*100204*/:
                        LoginActivity.this.loginfaild(LoginActivity.this.getResources().getString(R.string.loginpswnotcorrect));
                        return;
                    case Constants.mloginduserstopuser /*100205*/:
                        LoginActivity.this.loginfaild(LoginActivity.this.getResources().getString(R.string.loginstopuser));
                        return;
                    case Constants.mqueryuserinfotimeout /*104011*/:
                        LoginActivity.this.loginfaild(LoginActivity.this.getResources().getString(R.string.user_networktimeout));
                        return;
                    case Constants.mqueryuserinfoneterror /*104012*/:
                        LoginActivity.this.loginfaild(LoginActivity.this.getResources().getString(R.string.user_networkerror));
                        return;
                    case 106900:
                        String backInforcontext = msg.obj.toString();
                        Log.e("+++++++++++", "++++++++++++++");
                        Log.e("+++++++++++", "++++++++++++++mregistnewUsersuccess" + backInforcontext);
                        Log.e("+++++++++++", "++++++++++++++");
                        if (backInforcontext != null && !backInforcontext.equals(bs.b)) {
                            DocumentBuilderFactory _factory = DocumentBuilderFactory.newInstance();
                            try {
                                String mCardNb = _factory.newDocumentBuilder().parse(new ByteArrayInputStream(backInforcontext.getBytes())).getDocumentElement().getElementsByTagName("uid").item(0).getTextContent();
                                LoginActivity.this.insterregisbackInfoDb(mCardNb);
                                if (LoginActivity.this.isLoginButton) {
                                    LoginActivity.this.mstrPassword = LoginActivity.this.mLogPass.getText().toString();
                                    if (PageUtil.checkNull(LoginActivity.this.mstrUsername, LoginActivity.this.mstrPassword, LoginActivity.this)) {
                                        return;
                                    }
                                } else {
                                    LoginActivity.this.mstrPassword = "123456";
                                }
                                LoginActivity.this.mstrUsername = mCardNb;
                                if (!LoginActivity.this.mIfAddUser || !LoginActivity.this.mstrUsername.equals(App_phms.getInstance().mUserInfo.mUserID)) {
                                    if (LoginActivity.this.m_dialogClass == null || LoginActivity.this.m_dialogClass.equals(bs.b)) {
                                        LoginActivity.this.m_dialogClass = new DialogClass((Context) LoginActivity.this, LoginActivity.this.getString(R.string.login_logining), false, 0, LoginActivity.this.mCancelLogin);
                                    }
                                    AjaxCallBack_login _ajAjaxCallBack_login = new AjaxCallBack_login(LoginActivity.this, LoginActivity.this.mHandler);
                                    _ajAjaxCallBack_login.mUserID = LoginActivity.this.mstrUsername;
                                    _ajAjaxCallBack_login.mPasswd = LoginActivity.this.mstrPassword;
                                    Method_android_login.login(_ajAjaxCallBack_login.mUserID, LoginActivity.this.mstrPassword, 0, _ajAjaxCallBack_login, App_phms.getInstance().mFinalHttp, String.valueOf(Constants.URL) + "/login.php");
                                    App_phms.mCurrentActivity = 1;
                                    return;
                                }
                                Toast.makeText(LoginActivity.this, LoginActivity.this.getResources().getString(R.string.user_has_login_str), Toast.LENGTH_LONG).show();
                                LoginActivity.this.finish();
                                return;
                            } catch (Exception e2) {
                                if (LoginActivity.this.isLoginButton) {
                                    new DialogClass(LoginActivity.this, LoginActivity.this.getResources().getString(R.string.loginuserisnotexit));
                                } else {
                                    Meth_android_registNewUser.registNewUser("00000000000000000000000000000000", LoginActivity.this.mThirdcode, "123456", "1", "6", "ANDROID", bs.b, bs.b, bs.b, bs.b, bs.b, bs.b, bs.b, bs.b, bs.b, bs.b, "contecthirdparty", bs.b, bs.b, new AjaxCallBack_registNewUser(LoginActivity.this, LoginActivity.this.mHandler), App_phms.getInstance().mFinalHttp, String.valueOf(Constants.URL) + "/main.php");
                                }
                                e2.printStackTrace();
                                return;
                            }
                        } else {
                            return;
                        }
                    case 106980:
                        LoginActivity.this.mThirdcode = msg.obj.toString().split(Constants.DOUHAO)[0];
                        LoginActivity.this.mThirdUserName = msg.obj.toString().split(Constants.DOUHAO)[1];
                        Log.e("+++++++++++", "++++++++++++++");
                        Log.e("+++++++++++", "++++++++++++++mregisteroAuthsuccess===>msg.obj.toString():" + msg.obj.toString());
                        Log.e("+++++++++++", "++++++++++++++mregisteroAuthsuccess===>mThirdUserName:" + LoginActivity.this.mThirdUserName);
                        Log.e("+++++++++++", "++++++++++++++");
                        if (LocalLoginInfoManager.getInstance().querySql(LoginActivity.this.mThirdcode)) {
                            LoginActivity.this.mstrUsername = LocalLoginInfoManager.getInstance().findByThirdCode(LoginActivity.this.mThirdcode).mCardNb;
                            LoginActivity.this.mstrPassword = "123456";
                            LoginActivity.this.m_dialogClass = new DialogClass((Context) LoginActivity.this, LoginActivity.this.getString(R.string.login_logining), false, 0, LoginActivity.this.mCancelLogin);
                            AjaxCallBack_login _ajAjaxCallBack_login2 = new AjaxCallBack_login(LoginActivity.this, LoginActivity.this.mHandler);
                            _ajAjaxCallBack_login2.mUserID = LoginActivity.this.mstrUsername;
                            _ajAjaxCallBack_login2.mPasswd = LoginActivity.this.mstrPassword;
                            Method_android_login.login(LoginActivity.this.mstrUsername, LoginActivity.this.mstrPassword, 0, _ajAjaxCallBack_login2, App_phms.getInstance().mFinalHttp, String.valueOf(Constants.URL) + "/login.php");
                            App_phms.mCurrentActivity = 1;
                            return;
                        }
                        AjaxCallBack_registNewUser ajaxCallBack_registNewUser = new AjaxCallBack_registNewUser(LoginActivity.this, LoginActivity.this.mHandler);
                        LoginActivity.this._smsverifystr = "contecthirdparty";
                        LoginActivity.this._defaultpassword = "123456";
                        Meth_android_registNewUser.registNewUser("00000000000000000000000000000000", LoginActivity.this.mThirdcode, LoginActivity.this._defaultpassword, "0", "6", "ANDROID", bs.b, bs.b, bs.b, bs.b, bs.b, bs.b, bs.b, bs.b, bs.b, bs.b, LoginActivity.this._smsverifystr, bs.b, bs.b, ajaxCallBack_registNewUser, App_phms.getInstance().mFinalHttp, String.valueOf(Constants.URL) + "/main.php");
                        return;
                    case 106981:
                        Log.e("=============", "==============");
                        Log.e("=============", "Execute current deauthorization");
                        Log.e("=============", "==============");
                        if (LoginActivity.this.m_dialogClass == null || LoginActivity.this.m_dialogClass.equals(bs.b)) {
                            new DialogClass(LoginActivity.this, LoginActivity.this.getResources().getString(R.string.oauthcancel));
                            return;
                        } else {
                            LoginActivity.this.loginfaild(LoginActivity.this.getResources().getString(R.string.oauthcancel));
                            return;
                        }
                    case 106982:
                        if (LoginActivity.this.m_dialogClass == null || LoginActivity.this.m_dialogClass.equals(bs.b)) {
                            new DialogClass(LoginActivity.this, LoginActivity.this.getResources().getString(R.string.oauthfaile));
                            return;
                        } else {
                            LoginActivity.this.loginfaild(LoginActivity.this.getResources().getString(R.string.oauthfaile));
                            return;
                        }
                    case 106983:
                        if (LoginActivity.this.m_dialogClass == null || LoginActivity.this.m_dialogClass.equals(bs.b)) {
                            new DialogClass(LoginActivity.this, LoginActivity.this.getResources().getString(R.string.oauthWechatnot));
                            return;
                        } else {
                            LoginActivity.this.loginfaild(LoginActivity.this.getResources().getString(R.string.oauthWechatnot));
                            return;
                        }
                    case 106984:
                        if (LoginActivity.this.m_dialogClass == null || LoginActivity.this.m_dialogClass.equals(bs.b)) {
                            new DialogClass(LoginActivity.this, LoginActivity.this.getResources().getString(R.string.oauthGooglenot));
                            return;
                        } else {
                            LoginActivity.this.loginfaild(LoginActivity.this.getResources().getString(R.string.oauthGooglenot));
                            return;
                        }
                    case Constants.NETWROK_NOT_GOOD /*900006*/:
                        LoginActivity.this.loginfaild(LoginActivity.this.getResources().getString(R.string.verifysendsmsserror));
                        return;
                    default:
                        return;
                }
            }
        };
        if (!PageUtil.checkSDCard(this)) {
            noSDCardExit();
        }
        String[] _users = PageUtil.getUserNameFromSharePre(this);
        if (_users != null) {
            if (Constants.IS_PAD_NEW) {
                _nameAdapter = new ArrayAdapter<>(this, R.layout.layout_loginusename_tip_textview, _users);
            } else {
                _nameAdapter = new ArrayAdapter<>(this, R.layout.layout_loginuser_tip_phone, _users);
            }
            this.mLogName.setDropDownBackgroundResource(R.drawable.drawable_history_list);
            this.mLogName.setDropDownVerticalOffset(6);
            this.mLogName.setAdapter(_nameAdapter);
        }
        queryUserInfo();
        queryUserToUi();
        this.mProgressDialog = new ProgressDialog(this);
        if (getIntent() != null && this.mIfAddUser) {
            this.mLogName.setText(bs.b);
            this.mLogPass.setText(bs.b);
        }
        if (getIntent().getExtras() != null) {
            boolean _isfromstarte = getIntent().getExtras().getBoolean("isfromrestart");
            CLog.i("lz", "login re isfromrestart = " + _isfromstarte);
            if (_isfromstarte) {
                SharedPreferences.Editor _editor = getSharedPreferences("CurrentloginUserInfo", 0).edit();
                _editor.putInt("startcount", 0);
                _editor.commit();
                String _username = getIntent().getExtras().getString("username");
                String _password = getIntent().getExtras().getString("password");
                this.mLogName.setText(_username);
                this.mLogPass.setText(_password);
                doLogin();
                return;
            }
        }
        if (PageUtil.checkNet(this)) {
            if (checkAutoLogin() && !PageUtil.isPersonalUser(this.mLogName.getText().toString(), this) && !this.mLogName.getText().toString().equals(bs.b)) {
                autoLogin();
            }
            this.mUpdateManeger = new UpdateManeger(this, this.mHandler);
            this.mThread = new Thread(this);
            this.mThread.start();
        } else if (Constants.TestFlag) {
            new DialogClass(this, getResources().getString(R.string.net_disable));
        } else {
            new DialogClass(this, getResources().getString(R.string.net_disable));
        }
        this.mLogName.addTextChangedListener(this.mLoginNameTextWatcher);
        Bundle mBundle = getIntent().getExtras();
        if (mBundle != null && mBundle.getBoolean("isfromregist")) {
            this.mLogName.setText(mBundle.getString("user"));
            this.mLogPass.setText(bs.b);
        } else if (Constants.TestFlag) {
            this.mDefaultName = "15611733173";
            this.mDefaultPassword = "3333333";
        }
        checkischagneuser();
        if (this.mLogName.getText().length() > 0) {
            this.mloginphonenumdelbtn.setVisibility(View.VISIBLE);
        } else {
            this.mloginphonenumdelbtn.setVisibility(View.GONE);
        }
    }

    private void checkischagneuser() {
        if (getIntent().getExtras() != null) {
            Bundle _bundle = getIntent().getExtras();
            if (_bundle.getBoolean("fromchangeuser")) {
                this.mLogName.setText(_bundle.getString("username"));
                this.mLogPass.setText(_bundle.getString("userpsw"));
                try {
                    List<UserInfoDao> _userDaoList = App_phms.getInstance().mHelper.getUserDao().queryForEq(UserInfoDao.UserId, this.mLogName.getText());
                    if (_userDaoList != null && _userDaoList.size() > 0) {
                        this.m_cbxAuto.setChecked(_userDaoList.get(0).getAutologin());
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                this.mLoginButton.performClick();
            }
        }
    }

    private void checkisaddnewuser(Intent data) {
        if (data.getExtras() != null) {
            Bundle _bundle = data.getExtras();
            if (_bundle.getBoolean("fromaddnewUser")) {
                CLog.i(this.TAG, "Registration successful - username: " + _bundle.getString("username") + "  " + "The password box is about to pop up");
                String phonename = _bundle.getString("username");
                if (phonename == null || phonename.equals(bs.b)) {
                    this.mLogName.setText(_bundle.getString("mailname"));
                } else {
                    this.mLogName.setText(phonename);
                }
                this.mLogPass.setFocusable(true);
                this.mLogPass.setFocusableInTouchMode(true);
                this.mLogPass.requestFocus();
                new Timer().schedule(new TimerTask() {
                    public void run() {
                        ((InputMethodManager) LoginActivity.this.mLogPass.getContext().getSystemService(Context.INPUT_METHOD_SERVICE)).showSoftInput(LoginActivity.this.mLogPass, 0);
                    }
                }, 998);
                this.m_cbxAuto.setChecked(false);
            }
        }
    }

    public Bitmap readBitmap(int id) {
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inPreferredConfig = Bitmap.Config.RGB_565;
        opt.inInputShareable = true;
        opt.inPurgeable = true;
        return BitmapFactory.decodeStream(getResources().openRawResource(id), (Rect) null, opt);
    }

    public AlertDialog UpdataDialogClass(Context pContext) {
        if (this.myDialog != null) {
            this.myDialog.dismiss();
        }
        this.myDialog = new AlertDialog.Builder(pContext).create();
        this.myDialog.getWindow().setBackgroundDrawable(new ColorDrawable());
        this.myDialog.show();
        this.myDialog.setOnKeyListener(this.mdialogkeylistener);
        View layout = ((LayoutInflater) pContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.layout_downloadapk, (ViewGroup) null);
        this.mdownlaodprogressbar = (ProgressbarCustom) layout.findViewById(R.id.downloadapk_progressbar);
        this.mdownloadapk_progresstv = (TextView) layout.findViewById(R.id.downloadapk_progresstv);
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

    protected void onResume() {
        PackageInfo packageInfo;
        boolean isShowLead = false;
        super.onResume();
        CLog.i(this.TAG, "call Onresume method");
        if (this.mLogName != null) {
            this.mLogName.clearFocus();
        }
        if (!PhmsSharedPreferences.getInstance(this.this$1).getBoolean(Constants.KEY_SHOW_LEAD_VIEW)) {
            isShowLead = true;
        }
        if (isShowLead) {
            PhmsSharedPreferences.getInstance(this.this$1).saveBoolean(Constants.KEY_SHOW_LEAD_VIEW, true);
            startActivity(new Intent(this, ActivityNewUserLead.class));
        } else {
            try {
                packageInfo = getPackageManager().getPackageInfo("com.contec", 0);
            } catch (PackageManager.NameNotFoundException e) {
                packageInfo = null;
                e.printStackTrace();
            }
            if (packageInfo != null && !packageInfo.equals(bs.b)) {
                CLog.e(this.TAG, "======代码执行到这里。。。2");
                if (this.mDialogClass == null || this.mDialogClass.equals(bs.b)) {
                    this.mDialogClass = new DialogClass((Context) this, getResources().getString(R.string.uninstall_old_phms), (int) R.drawable.phms_icon, (int) R.drawable.img_phms_icon);
                }
            } else if (this.mDialogClass == null || this.mDialogClass.equals(bs.b)) {
                CLog.e(this.TAG, "======代码执行到这里。。。");
            } else {
                this.mDialogClass.dismiss();
                CLog.e(this.TAG, "代码执行到这里。。。");
            }
        }
        //MobclickAgent.onResume(this);
    }

    protected void onPause() {
        super.onPause();
        //MobclickAgent.onPause(this);
    }

    public void init_view() {
        this.mLayout_Title = (RelativeLayout) findViewById(R.id.linearlayout_title);
        this.mLayout_Title.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                LoginActivity loginActivity = LoginActivity.this;
                loginActivity.mClickTimes = loginActivity.mClickTimes + 1;
                CLog.e("AboutUs", "--mClickTimes--" + LoginActivity.this.mClickTimes);
                if (!LoginActivity.this.isExit.booleanValue()) {
                    LoginActivity.this.isExit = true;
                    if (!LoginActivity.this.hasTask.booleanValue()) {
                        LoginActivity.this.hasTask = true;
                        if (LoginActivity.this.tExit == null) {
                            LoginActivity.this.tExit = new Timer();
                            LoginActivity.this.tExit.schedule(new TimerTask() {
                                public void run() {
                                    LoginActivity.this.tExit.cancel();
                                    LoginActivity.this.tExit.purge();
                                    LoginActivity.this.tExit = null;
                                    LoginActivity.this.mClickTimes = 0;
                                    LoginActivity.this.isExit = false;
                                    LoginActivity.this.hasTask = false;
                                }
                            }, 5000);
                            return;
                        }
                        return;
                    }
                    return;
                }
                CLog.e("AboutUs", "--mUsHttpTextView--");
                if (LoginActivity.this.mClickTimes == 5) {
                    CLog.e("AboutUs", "mUsHttpTextView");
                    AlertDialogUtil.showServerDialog(false, LoginActivity.this.getResources().getString(R.string.net_disable), LoginActivity.this);
                }
            }
        });
        this.mlogin_bg = (RelativeLayout) findViewById(R.id.login_bg);
        getWindowManager().getDefaultDisplay().getMetrics(new DisplayMetrics());
        this.SCREENHEIGH = getWindowManager().getDefaultDisplay().getHeight();
        this.SCREENWEIGH = getWindowManager().getDefaultDisplay().getWidth();
        CLog.e("###############", "SCREENHEIGH:" + this.SCREENHEIGH + "       SCREENWEIGH:" + this.SCREENWEIGH);
        Constants.M_SCREENHEIGH = this.SCREENHEIGH;
        Constants.M_SCREENWEIGH = this.SCREENWEIGH;
        if (Constants.IS_PAD_NEW) {
            this.mwidth_scale = Constants.PAD_WIDTH_SACLE;
            this.mheight_scale = 15;
        } else {
            this.mwidth_scale = Constants.PHONE_WIDTH_SACLE;
            this.mheight_scale = 12;
        }
        this.mLogName = (AutoCompleteTextView) findViewById(R.id.login_edit_account);
        this.mLogName.setOnFocusChangeListener(this.mOnLogNameFocusChangeListener);
        this.mLogName.getLayoutParams().height = this.SCREENHEIGH / this.mheight_scale;
        this.mLogName.getLayoutParams().width = (int) (((double) this.SCREENWEIGH) * this.mwidth_scale);
        this.mLogName.clearFocus();
        this.mLogPass = (EditText) findViewById(R.id.login_edit_pwd);
        this.mLogPass.getLayoutParams().height = this.SCREENHEIGH / this.mheight_scale;
        this.mLogPass.getLayoutParams().width = (int) (((double) this.SCREENWEIGH) * this.mwidth_scale);
        this.m_distances = (ImageView) findViewById(R.id.distances);
        this.m_distances.getLayoutParams().height = (this.SCREENHEIGH * 1) / 30;
        this.mImageView_distances1 = (RelativeLayout) findViewById(R.id.distances2);
        this.mImageView_distances1.getLayoutParams().height = (this.SCREENHEIGH * 1) / 14;
        this.mImageView_distances1.getLayoutParams().width = (this.SCREENWEIGH * 9) / 10;
        this.m_cbxAuto = (CheckBox) findViewById(R.id.login_cb_auto);
        this.m_cbxAuto.getLayoutParams().height = (this.SCREENHEIGH * 1) / 21;
        this.m_layout_login_activity_bg = (RelativeLayout) findViewById(R.id.login_bg);
        this.mLoginTitle = (TextView) findViewById(R.id.login1_text_title);
        this.mTextAccount = (TextView) findViewById(R.id.login1_text_account);
        this.mTextPwd = (TextView) findViewById(R.id.login1_text_psw);
        this.mLoginButton = (Button) findViewById(R.id.login_btn_login);
        this.mRegisterButton = (Button) findViewById(R.id.zhuce_btn);
        this.mLoginButton.setOnClickListener(this.mLoginButtonOnClickEvent);
        this.mRegisterButton.setOnClickListener(this.mResgisterButtonOnClickEvent);
        LinearLayout m_login_user_layout = (LinearLayout) findViewById(R.id.login_user_layout);
        m_login_user_layout.getLayoutParams().height = (this.SCREENHEIGH * 1) / 10;
        m_login_user_layout.getLayoutParams().width = (this.SCREENWEIGH * 9) / 10;
        LinearLayout m_login_psw_layout = (LinearLayout) findViewById(R.id.login_psw_layout);
        m_login_psw_layout.getLayoutParams().height = (this.SCREENHEIGH * 1) / 10;
        m_login_psw_layout.getLayoutParams().width = (this.SCREENWEIGH * 9) / 10;
        LinearLayout m_regis_but_layout = (LinearLayout) findViewById(R.id.regis_but_layout);
        m_regis_but_layout.getLayoutParams().height = (this.SCREENHEIGH * 1) / 14;
        m_regis_but_layout.getLayoutParams().width = (this.SCREENWEIGH * 9) / 10;
        LinearLayout moauthLoginLine = (LinearLayout) findViewById(R.id.oauthline);
        moauthLoginLine.getLayoutParams().height = (this.SCREENHEIGH * 1) / 14;
        moauthLoginLine.getLayoutParams().width = (this.SCREENWEIGH * 9) / 10;
        LinearLayout moauthAllIcon = (LinearLayout) findViewById(R.id.oauth_allicon);
        moauthAllIcon.getLayoutParams().height = (this.SCREENHEIGH * 1) / 14;
        moauthAllIcon.getLayoutParams().width = (this.SCREENWEIGH * 9) / 10;
        this.mloginphonenumdelbtn = (Button) findViewById(R.id.loginphonenumdelbtn);
        this.auto_threelogin = (TextView) findViewById(R.id.auto_threelogin);
        this.mSetLanguage = (Button) findViewById(R.id.login_settings_btn);
        if (Constants.Language.contains("zh")) {
            this.mSetLanguage.setText(getResources().getString(R.string.str_set_en));
        } else {
            this.mSetLanguage.setText(getResources().getString(R.string.str_set_ch));
        }
        this.mSetLanguage.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                LoginActivity.this.changeLanguage();
            }
        });
        this.mloginphonenumdelbtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                LoginActivity.this.mLogName.setText(bs.b);
            }
        });
        this.mfindpasswrd = (TextView) findViewById(R.id.findpasswrd);
        this.mfindpasswrd.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        this.mfindpasswrd.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                Intent _intent = new Intent(LoginActivity.this, Activitynopsw.class);
                _intent.putExtra("toActivitynopsw", true);
                LoginActivity.this.startActivityForResult(_intent, 10001);
            }
        });
        this.mLogName.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId != 5) {
                    return false;
                }
                LoginActivity.this.mLogPass.setFocusable(true);
                return false;
            }
        });
        if (Constants.IS_PAD_NEW) {
            this.m_cbxAuto.getLayoutParams().height = (this.SCREENHEIGH * 1) / 16;
            this.m_cbxAuto.setPadding(ScreenAdapter.dip2px(this, 37.0f), 0, 0, 0);
        }
//        this.mWeChatLogin = (ImageView) findViewById(R.id.wechat_login);
        this.mQQLogin = (ImageView) findViewById(R.id.qq_login);
//        this.mWeiboLogin = (ImageView) findViewById(R.id.weibo_login);
//        this.mGoogleLogin = (ImageView) findViewById(R.id.google_login);
//        this.mFaceBookLogin = (ImageView) findViewById(R.id.facebook_login);
//        this.mWeChatLogin.setOnClickListener(this);
        this.mQQLogin.setOnClickListener(this);
//        this.mWeiboLogin.setOnClickListener(this);
//        this.mGoogleLogin.setOnClickListener(this);
//        this.mFaceBookLogin.setOnClickListener(this);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == 4) {
            Constants.USER_EXIT_BEFORE = 1;
        }
        return super.onKeyDown(keyCode, event);
    }

    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case 0:
                ProgressDialog _Dialog = new ProgressDialog(this);
                _Dialog.setMessage(getString(R.string.waiting));
                _Dialog.setIndeterminate(true);
                _Dialog.setCancelable(true);
                return _Dialog;
            case 1:
                this.mProgressDialog.setProgressStyle(1);
                return this.mProgressDialog;
            default:
                return null;
        }
    }

    private boolean checkAutoLogin() {
        this.m_users = new ArrayList();
        try {
            this._userdao = App_phms.getInstance().mHelper.getUserDao();
        } catch (SQLException e1) {
            e1.printStackTrace();
        }
        try {
            this.m_users = App_phms.getInstance().mHelper.getUserDao().queryForEq(UserInfoDao.UserId, App_phms.getInstance().mUserInfo.mUserID);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (this.m_users.size() <= 0) {
            return false;
        }
        this.oneuser = this.m_users.get(0);
        this._auto_log = this.oneuser.getAutologin();
        if (!this._auto_log) {
            return false;
        }
        this.m_users.removeAll(this.m_users);
        return true;
    }

    private void autoLogin() {
        if (!this.m_bLoginFailed && !this.m_bChangeUser && this.m_cbxAuto.isChecked() && this.m_vUsername.size() > 0) {
            this.mProgressDialog.dismiss();
            this.mLoginButton.performClick();
        }
    }

    private void queryUserInfo() {
        this.users = new ArrayList();
        try {
            QueryBuilder<UserInfoDao, String> querybuilder = App_phms.getInstance().mHelper.getUserDao().queryBuilder();
            querybuilder.orderBy(UserInfoDao.LASTLOGINDATE, false);
            this.users = querybuilder.query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void queryUserToUi() {
        this.m_vUsername.clear();
        this.m_vPsw.clear();
        this.m_vSavePsw.clear();
        this.m_vAuto.clear();
        for (int i = 0; i < this.users.size(); i++) {
            UserInfoDao oneuser2 = this.users.get(i);
            this.m_vUsername.add(oneuser2.getmUserId());
            this.m_vPsw.add(oneuser2.getPsw());
            this.m_vSavePsw.add(Boolean.valueOf(oneuser2.getSavingMode()));
            this.m_vAuto.add(Boolean.valueOf(oneuser2.getAutologin()));
            Log.i("lz", "i=" + i + "username = " + oneuser2.getmUserId() + "psw = " + oneuser2.getPsw());
        }
        if (this.m_vUsername.size() > 0) {
            this.mLogName.setText(this.m_vUsername.get(0));
            if (this.m_vSavePsw.get(0).booleanValue()) {
                this.mLogPass.setText(this.m_vPsw.get(0));
            }
            this.m_cbxAuto.setChecked(this.m_vAuto.get(0).booleanValue());
        }
    }

    private void doLogin() {
        App_phms.mCurrentActivity = 1;
        this.miscancellogin = false;
        this.mstrUsername = this.mLogName.getText().toString().toUpperCase();
        this.mThirdcode = this.mstrUsername;
        AjaxCallBack_registNewUser ajaxCallBack_registNewUser = new AjaxCallBack_registNewUser(this, this.mHandler);
        if (RegisterMailActivity.checkEmail(this.mstrUsername)) {
            if (LocalLoginInfoManager.getInstance().querySql(this.mstrUsername)) {
                this.mstrUsername = LocalLoginInfoManager.getInstance().findByThirdCode(this.mstrUsername).mCardNb;
            } else {
                Meth_android_registNewUser.registNewUser("00000000000000000000000000000000", this.mstrUsername, bs.b, "0", "6", "ANDROID", bs.b, bs.b, bs.b, bs.b, bs.b, bs.b, bs.b, bs.b, bs.b, bs.b, "contecthirdparty", bs.b, bs.b, ajaxCallBack_registNewUser, App_phms.getInstance().mFinalHttp, String.valueOf(Constants.URL) + "/main.php");
                return;
            }
        }
        CLog.e("doLogin", this.mstrUsername);
        this.mstrPassword = this.mLogPass.getText().toString();
        this.mstrPassword = "123456";
        if (!PageUtil.checkNull(this.mstrUsername, this.mstrPassword, this)) {
            if (!this.mIfAddUser || !this.mstrUsername.equals(App_phms.getInstance().mUserInfo.mUserID)) {
                this.m_dialogClass = new DialogClass((Context) this, getString(R.string.login_logining), false, 0, this.mCancelLogin);
                AjaxCallBack_login _ajAjaxCallBack_login = new AjaxCallBack_login(this, this.mHandler);
                _ajAjaxCallBack_login.mUserID = this.mstrUsername;
                _ajAjaxCallBack_login.mPasswd = this.mstrPassword;
                Method_android_login.login(this.mstrUsername, this.mstrPassword, 0, _ajAjaxCallBack_login, App_phms.getInstance().mFinalHttp, String.valueOf(Constants.URL) + "/login.php");
                App_phms.mCurrentActivity = 1;
                return;
            }
            Toast.makeText(this, getResources().getString(R.string.user_has_login_str), Toast.LENGTH_LONG).show();
            finish();
        }
    }

    public void noSDCardExit() {
        new DialogClass(this, getResources().getString(R.string.nosdcard));
    }

    public void run() {
        this._getreturnparams = this.mUpdateManeger.checkUpdate();
        int _isNeedUpdate = Integer.valueOf(this._getreturnparams.get("_isNeedUpdate")).intValue();
        switch (App_phms.mCurrentActivity) {
            case 0:
                switch (_isNeedUpdate) {
                    case 1:
                        this.mHandler.sendEmptyMessage(3);
                        return;
                    default:
                        return;
                }
            case 1:
                this.mHandler.obtainMessage(4, _isNeedUpdate, 0).sendToTarget();
                return;
            case 2:
                switch (_isNeedUpdate) {
                    case 1:
                        Message msg = new Message();
                        msg.what = Constants.V_NEED_UPDATE;
                        msg.arg2 = 1;
                        App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(msg);
                        return;
                    default:
                        return;
                }
            default:
                return;
        }
    }

    private void loginsuccess() {
        spPassword = this.mstrPassword;
        spUsername = this.mstrUsername;
        //new GetTime().execute(new String[0]);
        this.msuccessflag = true;
        this._bAuto = this.m_cbxAuto.isChecked();
        PageUtil.saveUserNameTOSharePre(this, this.mstrUsername);
        LoginUserDao _loginUserInfo = PageUtil.getLoginUserInfo();
        UserInfoDao user = new UserInfoDao();
        user.setmUserId(this.mstrUsername);
        user.setPsw(this.mstrPassword);
        user.setSavingMode(true);
        user.setAutologin(this._bAuto);
        user.setLastLoginData(new Date());
        user.setmSex(_loginUserInfo.mSex);
        user.setmUserName(_loginUserInfo.mUserName);
        try {
            Dao<UserInfoDao, String> userDao = App_phms.getInstance().mHelper.getUserDao();
            UserInfoDao retUser = userDao.queryForId(this.mstrUsername);
            if (retUser == null) {
                App_phms.getInstance().mUserInfo.mSearchInterval = 10;
                App_phms.getInstance().mUserInfo.mBluetoothState = 1;
                App_phms.getInstance().mUserInfo.mLanguage = "1zh";
                user.setmSearchInterval(10);
                user.setmLanguage(Constants.Language);
                user.setmBluetoothState(1);
                userDao.create(user);
            } else {
                App_phms.getInstance().mUserInfo.mSearchInterval = retUser.getmSearchInterval();
                App_phms.getInstance().mUserInfo.mBluetoothState = retUser.getBluetoothstate();
                retUser.setmSex(App_phms.getInstance().mUserInfo.mSex);
                retUser.setmUserName(_loginUserInfo.mUserName);
                retUser.setAutologin(this._bAuto);
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
        _editor.putString("username", this.mstrUsername);
        _editor.putString("password", this.mstrPassword);
        _editor.commit();
        this._intent.putExtra("username", this.mstrUsername);
        this._intent.putExtra("password", this.mstrPassword);
        this._intent.putExtra("savepsw", true);
        this._intent.putExtra("auto", this._bAuto);
        PhmsSharedPreferences.getInstance(this).saveColume("username", this.mstrUsername);
        try {
            Thread.sleep(400);
        } catch (InterruptedException e2) {
            e2.printStackTrace();
        }
        MainActivityNew.currentTab = 0;
        startActivity(this._intent);
        if (this.m_dialogClass != null && !this.m_dialogClass.equals(bs.b)) {
            this.m_dialogClass.dismiss();
        }
        saveHospital(_loginUserInfo);
        finish();
    }

    private void saveHospital(LoginUserDao _loginUserInfo) {
        String mUid = _loginUserInfo.mUID;
        if (TextUtils.isEmpty(PhmsSharedPreferences.getInstance(App_phms.getInstance()).getString(SaveHospitalUtils.spProvince + mUid))) {
            SaveHospitalUtils.saveDeafultHospitalInfo(mUid);
        }
    }

    private void loginfaild(String pstr) {
        if (this.m_dialogClass != null && !this.m_dialogClass.equals(bs.b)) {
            this.m_dialogClass.dismiss();
            new DialogClass(this, pstr).show();
        }
    }

    private void logincancel() {
        this.m_dialogClass.dismiss();
        this.this$1.finish();
    }

    class MainHandler extends Handler {
        public MainHandler() {
        }

        public MainHandler(Looper L) {
            super(L);
        }

        public void handleMessage(Message nMsg) {
        }
    }

    public void onEvent(Message msg) {
        if (msg.arg2 == 1324567) {
            finish();
        }
    }

    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        CLog.i(this.TAG, "loginActivity--out");
        CLog.i("lzerror", "loginActivity--out");
        if (!this.msuccessflag) {
            CLog.i("lzondestroy", "MainActivityNew is ondestroy....");
        }
        SharedPreferences.Editor _editor = getSharedPreferences("CurrentloginUserInfo", 0).edit();
        _editor.putInt("startcount", 0);
        _editor.commit();
        finish();
    }

    private void pedometerDataProcess() {
        String _id = new PedometerSharepreferance(this).getUserID();
        CLog.d("PedometerService", "userid: " + _id);
        if (_id == null || _id.equals(bs.b)) {
            Message msg = new Message();
            if (this.mIfAddUser) {
                msg.what = 512;
                msg.arg2 = 7;
                if (this.mIfAddUser) {
                    msg.arg1 = 8;
                } else {
                    msg.arg1 = 9;
                }
                CLog.d("PedometerService", "添加用戶 通知保存計步器数");
            } else {
                msg.what = Constants.UPDATA_NOTIFITION_USER_NAME;
                msg.arg2 = 7;
                msg.arg1 = 9;
                msg.obj = App_phms.getInstance().mUserInfo;
                CLog.d("PedometerService", "更新notification界面中的 user");
            }
            App_phms.getInstance().mEventBus.post(msg);
        } else if (_id == null || _id.equals(this.mstrUsername)) {
            Message msg2 = new Message();
            msg2.what = Constants.UPDATA_NOTIFITION_USER_NAME;
            msg2.arg2 = 7;
            msg2.arg1 = 9;
            msg2.obj = App_phms.getInstance().mUserInfo;
            App_phms.getInstance().mEventBus.post(msg2);
            CLog.d("PedometerService", "没有切换了用�?更新notification内容");
        } else {
            Message msg3 = new Message();
            msg3.what = 512;
            msg3.arg2 = 7;
            if (this.mIfAddUser) {
                msg3.arg1 = 8;
            } else {
                msg3.arg1 = 9;
            }
            App_phms.getInstance().mEventBus.post(msg3);
            CLog.d("PedometerService", "切换了用�? 关闭后台的service保存数据");
        }
        if (!DeviceService.mReceiveFinished) {
            DeviceService.mReceiveFinished = true;
        }
        SearchDevice.stopServer(this);
        DeviceManager.stopServer(this);
        UploadService.stopServer(this);
        MessageManager.stopServer(this);
        DeviceService.stopServer(this);
        Server_Main.stopServer(this);
        DataFilter.moveDataToFailedDir();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 64) {
            checkisaddnewuser(data);
        } else if (resultCode == 65) {
            checkisaddnewuser(data);
        }
    }

    private class GetTime extends AsyncTask<String, Void, String> {
        String _filetype = bs.b;
        private DialogClass mremovedialog;

        public GetTime() {
        }

        /* JADX WARN: Removed duplicated region for block: B:17:0x0059  */
        /* JADX WARN: Removed duplicated region for block: B:62:? A[RETURN, SYNTHETIC] */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        protected String doInBackground(String... params) {
            Throwable th;
            IOException ex;
            ConnectException e;
            DatagramSocket socket = null;
            InetAddress ipv4Addr = null;
            try {
                ipv4Addr = InetAddress.getByName("203.117.180.36");
            } catch (UnknownHostException e1) {
                e1.printStackTrace();
            }
            int serviceStatus = -1;
            DatagramSocket socket2 = null;
            try {
                socket = new DatagramSocket();
            } catch (Throwable th2) {
                th = th2;
            }
            try {
                socket.setSoTimeout(3000);
                for (int attempts = 0; attempts <= 2 && serviceStatus != 1; attempts++) {
                    try {
                        byte[] data = new NtpMessage().toByteArray();
                        DatagramPacket outgoing = new DatagramPacket(data, data.length, ipv4Addr, 123);
                        long sentTime = System.currentTimeMillis();
                        socket.send(outgoing);
                        DatagramPacket incoming = new DatagramPacket(data, data.length);
                        socket.receive(incoming);
                        long responseTime = System.currentTimeMillis() - sentTime;
                        double destinationTimestamp = (System.currentTimeMillis() / 1000.0d) + 2.2089888E9d;
                        NtpMessage msg = new NtpMessage(incoming.getData());
                        double d = ((msg.receiveTimestamp - msg.originateTimestamp) + (msg.transmitTimestamp - destinationTimestamp)) / 2.0d;
                        byte[] _m = msg.toByteArray();
                        System.out.println(_m);
                        System.out.println("poll: NTP message : " + msg.toString());
                        long _Internet = msg.toLong();
                        PhmsSharedPreferences.getInstance(LoginActivity.this).saveLong(Constants.INTERNET_TIME, _Internet);
                        serviceStatus = 1;
                    } catch (InterruptedIOException e5) {
                        System.out.println("InterruptedIOException: " + ipv4Addr);
                    }
                }
                if (socket != null) {
                    socket.close();
                }
                socket2 = socket;
            } catch (ConnectException e6) {
                e = e6;
                socket2 = socket;
                e.fillInStackTrace();
                System.out.println("Connection exception for address: " + ipv4Addr);
                if (socket2 != null) {
                    socket2.close();
                }
                Calendar.getInstance().getTimeInMillis();
                PhmsSharedPreferences _Preferences = PhmsSharedPreferences.getInstance(LoginActivity.this);
                _Preferences.saveLong(Constants.LOCAL_TIME, Calendar.getInstance().getTimeInMillis());
                if (serviceStatus != 1) {
                }
            } catch (NoRouteToHostException e7) {
                socket2 = socket;
                System.out.println("No route to host exception for address: " + ipv4Addr);
                if (socket2 != null) {
                    socket2.close();
                }
                Calendar.getInstance().getTimeInMillis();
                PhmsSharedPreferences _Preferences2 = PhmsSharedPreferences.getInstance(LoginActivity.this);
                _Preferences2.saveLong(Constants.LOCAL_TIME, Calendar.getInstance().getTimeInMillis());
                if (serviceStatus != 1) {
                }
            } catch (IOException e8) {
                ex = e8;
                socket2 = socket;
                ex.fillInStackTrace();
                System.out.println("IOException while polling address: " + ipv4Addr);
                if (socket2 != null) {
                    socket2.close();
                }
                Calendar.getInstance().getTimeInMillis();
                PhmsSharedPreferences _Preferences22 = PhmsSharedPreferences.getInstance(LoginActivity.this);
                _Preferences22.saveLong(Constants.LOCAL_TIME, Calendar.getInstance().getTimeInMillis());
                if (serviceStatus != 1) {
                }
            } catch (Throwable th3) {
                th = th3;
                socket2 = socket;
                if (socket2 != null) {
                    socket2.close();
                }
                try {
                    throw th;
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
            }
            Calendar.getInstance().getTimeInMillis();
            PhmsSharedPreferences _Preferences222 = PhmsSharedPreferences.getInstance(LoginActivity.this);
            _Preferences222.saveLong(Constants.LOCAL_TIME, Calendar.getInstance().getTimeInMillis());
            if (serviceStatus != 1) {
                return null;
            }
            _Preferences222.saveLong(Constants.INTERNET_TIME, 0L);
            return null;
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

    private void changeLanguage() {
        if (Constants.Language.contains("zh")) {
            this.mSetLanguage.setText(getResources().getString(R.string.str_set_ch));
            Configuration config = getResources().getConfiguration();
            DisplayMetrics dm = getResources().getDisplayMetrics();
            config.locale = Locale.ENGLISH;
            getResources().updateConfiguration(config, dm);
            Constants.Language = "en";
            PhmsSharedPreferences.getInstance(this.mContext).saveColume(UserInfoDao.Language, Constants.Language);
        } else {
            this.auto_threelogin.setText(getResources().getString(R.string.oauthlogin));
            this.mSetLanguage.setText(getResources().getString(R.string.str_set_en));
            Configuration config2 = getResources().getConfiguration();
            DisplayMetrics dm2 = getResources().getDisplayMetrics();
            config2.locale = Locale.CHINESE;
            getResources().updateConfiguration(config2, dm2);
            Constants.Language = "zh";
            PhmsSharedPreferences.getInstance(this.mContext).saveColume(UserInfoDao.Language, Constants.Language);
        }
        this.m_cbxAuto.setText(getResources().getString(R.string.opt_autoLogin));
        this.mLoginTitle.setText(getResources().getString(R.string.login_new));
        this.mTextAccount.setText(getResources().getString(R.string.input_phone));
        this.mTextPwd.setText(getResources().getString(R.string.input_psw));
        this.auto_threelogin.setText(getResources().getString(R.string.oauthlogin));
        this.mLoginButton.setText(getResources().getString(R.string.login_new));
        this.mRegisterButton.setText(getResources().getString(R.string.register_new));
        this.mfindpasswrd.setText(getResources().getString(R.string.retrievepassword));
        this.mLogPass.setHint(getResources().getString(R.string.strInputPswHint));
        this.mLogName.setHint(getResources().getString(R.string.input_phone_hint_login));
    }

    private void updateUI(boolean isSignedIn) {
    }

    public void onClick(View v) {
        switch (v.getId()) {
/*
            case R.id.wechat_login:
                if (PageUtil.checkNet(this)) {
                    this.isLoginButton = false;
                    spPassword = "123456";
                    this.wechat = ShareSDK.getPlatform(Wechat.NAME);
                    if (this.wechat.isValid()) {
                        this.wechat.removeAccount(true);
                        ShareSDK.removeCookieOnAuthorize(true);
                    }
                    this.wechat.setPlatformActionListener(this.wechatPaListener);
                    this.wechat.showUser((String) null);
                    return;
                }
                new DialogClass(this, getResources().getString(R.string.net_disable));
                return;
*/
            case R.id.qq_login:
                if (PageUtil.checkNet(this)) {
                    spPassword = "123456";
                    this.isLoginButton = false;
                    //this.qq = ShareSDK.getPlatform(QQ.NAME);
                    //if (this.qq.isValid()) {
                    //    this.qq.removeAccount(true);
                    //    ShareSDK.removeCookieOnAuthorize(true);
                    //}
                    this.qq.setPlatformActionListener(this.qqPaListener);
                    this.qq.showUser((String) null);
                    return;
                }
                new DialogClass(this, getResources().getString(R.string.net_disable));
                return;
/*
            case R.id.weibo_login:
                if (PageUtil.checkNet(this)) {
                    spPassword = "123456";
                    this.isLoginButton = false;
                    //this.weibo = ShareSDK.getPlatform(SinaWeibo.NAME);
                    //if (this.weibo.isValid()) {
                    //    this.weibo.removeAccount(true);
                    //    ShareSDK.removeCookieOnAuthorize(true);
                    //}
                    this.weibo.setPlatformActionListener(this.weiboPaListener);
                    this.weibo.showUser((String) null);
                    return;
                }
                new DialogClass(this, getResources().getString(R.string.net_disable));
                return;
            case R.id.google_login:
                if (PageUtil.checkNet(this)) {
                    this.isLoginButton = false;
                    spPassword = "123456";
                    this.gooleplus = ShareSDK.getPlatform(GooglePlus.NAME);
                    //if (this.gooleplus.isValid()) {
                    //    this.gooleplus.removeAccount(true);
                    //    ShareSDK.removeCookieOnAuthorize(true);
                    //}
                    this.gooleplus.setPlatformActionListener(this.gooleplusPaListener);
                    this.gooleplus.showUser((String) null);
                    return;
                }
                new DialogClass(this, getResources().getString(R.string.net_disable));
                return;
            case R.id.facebook_login:
                if (PageUtil.checkNet(this)) {
                    spPassword = "123456";
                    this.isLoginButton = false;
                    this.facebook = ShareSDK.getPlatform(Facebook.NAME);
                    //if (this.facebook.isValid()) {
                    //    this.facebook.removeAccount(true);
                    //    ShareSDK.removeCookieOnAuthorize(true);
                    //}
                    this.facebook.setPlatformActionListener(this.facebookPaListener);
                    this.facebook.showUser((String) null);
                    return;
                }
                new DialogClass(this, getResources().getString(R.string.net_disable));
                return;
*/
            default:
                return;
        }
    }

    private void insterregisbackInfoDb(String backInforcontext) {
        LocalLoginInfoDao _localinfor = new LocalLoginInfoDao();
        _localinfor.mCardNb = backInforcontext;
        _localinfor.mPassword = this.mstrPassword;
        _localinfor.mThirdCode = this.mThirdcode;
        LocalLoginInfoManager.getInstance().add(_localinfor);
    }
}
