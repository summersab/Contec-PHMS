package com.contec.phms.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.SmsManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.alibaba.cchannel.push.receiver.CPushMessageCodec;
import com.contec.phms.App_phms;
import com.contec.phms.R;
import com.contec.phms.ajaxcallback.AjaxCallBack_VerifyuserIsExit;
import com.contec.phms.ajaxcallback.AjaxCallBack_getHospital;
import com.contec.phms.ajaxcallback.AjaxCallBack_login_uploadpoint;
import com.contec.phms.ajaxcallback.AjaxCallBack_registNewUser;
import com.contec.phms.ajaxcallback.AjaxCallBack_registSendVerifyCode;
import com.contec.phms.domain.CityListItem;
import com.contec.phms.domain.HospitalBean;
import com.contec.phms.login.ActivityBase;
import com.contec.phms.login.LoginActivity;
import com.contec.phms.manager.message.OrderList;
import com.contec.phms.db.LocalLoginInfoDao;
import com.contec.phms.db.LocalLoginInfoManager;
import com.contec.phms.db.LoginUserDao;
import com.contec.phms.db.RegisterPhoneCityDBManager;
import com.contec.phms.db.localdata.PluseDataDao;
import com.contec.phms.util.CLog;
import com.contec.phms.util.Constants;
import com.contec.phms.util.CustomDialog;
import com.contec.phms.util.ParseXmlService;
import com.contec.phms.util.PhmsSharedPreferences;
import com.contec.phms.util.SelectHospitalDialog;
import com.contec.phms.widget.DialogClass;
//import com.umeng.analytics.MobclickAgent;
import com.zxing.android.CaptureActivity;
import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import cn.com.contec_net_3_android.Meth_android_VerifyUserIsExit;
import cn.com.contec_net_3_android.Meth_android_getHospital;
import cn.com.contec_net_3_android.Meth_android_registNewUser;
import cn.com.contec_net_3_android.Method_android_login;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.PlatformDb;
//import cn.sharesdk.sina.weibo.SinaWeibo;
//import cn.sharesdk.tencent.p008qq.QQ;
import u.aly.bs;

@SuppressLint({"HandlerLeak"})
public class RegisterPhoneActivity extends ActivityBase implements View.OnClickListener {
    public static String Language = "zh";
    private static final String TAG = RegisterPhoneActivity.class.getSimpleName();
    private static final String[] countrys_en = {"+93", "+355", "+213", "+682", "+376", "+244", "+1264", "+672", "+1268", "+54", "+374", "+297", "+43", "+61", "+994", "+1242", "+973", "+880", "+1246", "+375", "+32", "+501", "+229", "+1441", "+975", "+591", "+387", "+267", "+55", "+1284", "+673", "+359", "+226", "+257", "+855", "+237", "+1", "+238", "+1345", "+236", "+235", "+56", "+86", "+61", "+61", "+57", "+269", "+243", "+242", "+682", "+506", "+225", "+385", "+53", "+357", "+420", "+45", "+253", "+1767", "+1809", "+593", "+20", "+503", "+240", "+291", "+372", "+251", "+500", "+298", "+679", "+358", "+33", "+689", "+241", "+220", "+995", "+49", "+233", "+350", "+30", "+299", "+1473", "+1671", "+502", "+224", "+245", "+592", "+509", "+504", "+852", "+36", "+354", "+91", "+62", "+98", "+964", "+353", "+44", "+972", "+39", "+1876", "+81", "+962", "+73", "+254", "+686", "+965", "+996", "+856", "+371", "+961", "+266", "+231", "+218", "+423", "+370", "+352", "+853", "+389", "+261", "+265", "+60", "+960", "+223", "+356", "+692", "+222", "+230", "+269", "+52", "+691", "+373", "+377", "+976", bs.b, "+1664", "+212", "+258", "+95", "+264", "+674", "+977", "+599", "+31", "+687", "+64", "+505", "+227", "+234", "+683", "+6723", "+1", "+47", "+968", "+92", "+680", "+507", "+675", "+595", "+51", "+63", "+48", "+351", "+1809", "+974", "+262", "+40", "+7", "+250", "+33", "+290", "+1869", "+1758", "+00", "+508", "+1784", "+685", "+378", "+239", "+966", "+221", "+248", "+232", "+65", "+421", "+386", "+677", "+252", "+27", "+82", "+34", "+94", "+784", "+249", "+597", "+268", "+46", "+41", "+963", "+886", "+992", "+255", "+66", "+670", "+228", "+690", "+676", "+1868", "+216", "+90", "+993", "+1649", "+688", "+1340", "+256", "+380", "+971", "+44", "+1", "+598", "+998", "+678", "+379", "+58", "+84", "+681", "+967", "+260", "+263"};
    private static final String[] countrys_zh = {"+355", "+213", "+93", "+54", "+353", "+20", "+251", "+372", "+971", "+297", "+968", "+376", "+244", "+1264", "+1268", "+61", "+43", "+994", "+1246", "+675", "+1242", "+375", "+1441", "+92", "+595", "+973", "+507", "+359", "+55", "+1670", "+229", "+32", "+354", "+267", "+1", "+48", "+591", "+501", "+387", "+975", "+226", "+257", "+850", "+240", "+45", "+49", "+670", "+228", "+1809", "+1767", "+593", "+291", "+7", "+33", "+298", "+379", "+689", "+1599", "+679", "+63", "+358", "+238", "+500", "+220", "+242", "+243", "+299", "+1473", "+995", "+57", "+506", "+1671", "+53", "+592", "+509", "+82", "+7", "+382", "+31", "+599", "+504", "+233", "+1", "+855", "+241", "+253", "+420", "+996", "+686", "+263", "+224", "+245", "+1345", "+237", "+974", "+61", "+385", "+269", "+254", "+225", "+965", "+682", "+266", "+856", "+371", "+961", "+231", "+218", "+423", "+370", "+40", "+352", "+250", "+261", "+960", "+356", "+60", "+265", "+223", "+44", "+230", "+222", "+389", "+692", "+262", "+1", "+1684", "+1340", "+976", "+880", "+1664", "+95", "+691", "+51", "+373", "+212", "+377", "+258", "+52", "+264", "+27", "+672", "+674", "+977", "+505", "+227", "+234", "+683", "+47", "+680", "+870", "+351", "+81", "+46", "+41", "+503", "+381", "+232", "+221", "+357", "+248", "+685", "+966", "+590", "+61", "+239", "+290", "+1869", "+1758", "+378", "+508", "+1784", "+94", "+421", "+386", "+268", "+249", "+597", "+677", "+252", "+66", "+886", "+992", "+676", "+255", "+1649", "+1868", "+90", "+993", "+216", "+690", "+688", "+681", "+678", "+502", "+58", "+673", "+256", "+380", "+598", "+998", "+34", "+30", "+65", "+687", "+64", "+36", "+963", "+1876", "+374", "+967", "+39", "+964", "+98", "+91", "+62", "+44", "+1284", "+972", "+962", "+84", "+260", "+235", "+350", "+56", "+236", "+86", "+853", "+852"};
    public static final String hospitalId = "H1303230270";
    private boolean ISCANCELCOUNTDOWNASYN = false;
    private String _defaultpassword;
    private String _smsverifystr;
    private Button back_btn;
    private Button btn_register_del;
    private String city = null;
    private String[] countrys;
    private SelectHospitalDialog dialog;
    private String district = null;
    private String districtId = null;
/*
    private Platform facebook;
    PlatformActionListener facebookPaListener = new PlatformActionListener() {
        public void onCancel(Platform arg0, int arg1) {
        }

        public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
            if (i == 8) {
                PlatformDb platDB = platform.getDb();
                Log.e("=============", "==============");
                Log.e("facebook userid", "====" + platDB.getUserId());
                Log.e("===============", "=============");
                RegisterPhoneActivity.this.mThirdcode = platDB.getUserId();
                Message msg = new Message();
                msg.what = 106980;
                msg.obj = platDB.getUserId();
                RegisterPhoneActivity.this.mhandler.sendMessage(msg);
            }
        }

        public void onError(Platform arg0, int arg1, Throwable arg2) {
        }
    };

*/
    private Button getCode;
/*
    private Platform gooleplus;
    PlatformActionListener gooleplusPaListener = new PlatformActionListener() {
        public void onCancel(Platform arg0, int arg1) {
        }

        public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
            if (i == 8) {
                PlatformDb platDB = platform.getDb();
                Log.e("=============", "==============");
                Log.e("googleplus userid", "====" + platDB.getUserId());
                Log.e("===============", "=============");
                RegisterPhoneActivity.this.mThirdcode = platDB.getUserId();
                Message msg = new Message();
                msg.what = 106980;
                msg.obj = platDB.getUserId();
                RegisterPhoneActivity.this.mhandler.sendMessage(msg);
            }
        }

        public void onError(Platform arg0, int arg1, Throwable arg2) {
        }
    };

*/
    private final int gotoSexActivity = 37;
    private final int hospital_noresponse = 110410;
    private final int identifyhaveSent = 36;
    private String inputPhoneId;
    List<String> list = new ArrayList();
    private List<HospitalBean> listHospital;
    private LinearLayout ll_linkage;
//    private Button mBtnFaceBookReg;
//    private Button mBtnGoogleReg;
    private Button mBtnQqRegister;
//    private Button mBtnWeChatReg;
//    private Button mBtnWeiboReg;
    private String mCountry = null;
    private RegisterPhoneCityDBManager mDbManager;
    private Dialog mDialog;
    private boolean mFlage = false;
    Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Constants.REQUEST_HOSPITAL_SUCCESS /*110400*/:
                    RegisterPhoneActivity.this.listHospital = ParseXmlService.readXML(msg.getData().getString("content"));
                    Log.e("listHospitalSize>>>>", new StringBuilder().append(RegisterPhoneActivity.this.listHospital.size()).toString());
                    if (RegisterPhoneActivity.this.listHospital == null || RegisterPhoneActivity.this.listHospital.size() <= 0) {
                        Toast toast = Toast.makeText(RegisterPhoneActivity.this, R.string.str_no_response, Toast.LENGTH_LONG);
                        toast.setGravity(17, 0, 0);
                        toast.show();
                        HospitalBean _Bean = new HospitalBean();
                        _Bean.setHospitalId(bs.b);
                        _Bean.setHospitalName("无可选医院");
                        RegisterPhoneActivity.this.listHospital.add(_Bean);
                        new HospitalAdapter(RegisterPhoneActivity.this.getApplicationContext(), RegisterPhoneActivity.this.listHospital, false);
                        return;
                    }
                    new HospitalAdapter(RegisterPhoneActivity.this.getApplicationContext(), RegisterPhoneActivity.this.listHospital, true);
                    return;
                case Constants.REQUEST_HOSPITAL_FAILED /*110401*/:
                    new DialogClass(RegisterPhoneActivity.this, RegisterPhoneActivity.this.getResources().getString(R.string.user_parameter_error));
                    return;
                case Constants.REQUEST_HOSPITAL_DB_FAILED /*110402*/:
                    new DialogClass(RegisterPhoneActivity.this, RegisterPhoneActivity.this.getResources().getString(R.string.user_DB_error));
                    return;
                case Constants.HOSPITAL_FAILED /*110403*/:
                    new DialogClass(RegisterPhoneActivity.this, RegisterPhoneActivity.this.getResources().getString(R.string.user_networkerror));
                    return;
                default:
                    return;
            }
        }
    };
    private TextView mHopitalName;
    private boolean mIsReCheckPhoneNumber = true;
    private String mLoginUserId;
    private LinearLayout mNoReceCode;
    private String mPhonenumber;
    private String mPhonenumberGetCode;
    private TextView mRegistHostipalName;
    private EditText mRegist_inputphoneid_edit;
    private SQLiteDatabase mSdb;
    private Spinner mSpinnerCountrtSpinner;
    TextWatcher mTextWatcher = new TextWatcher() {
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void afterTextChanged(Editable s) {
            if (RegisterPhoneActivity.this.mregist_inputphonenum_edit.getText().toString().trim().length() != 0) {
                RegisterPhoneActivity.this.btn_register_del.setVisibility(View.VISIBLE);
                RegisterPhoneActivity.this.mFlage = true;
            }
            RegisterPhoneActivity.this.mFlage = false;
            if (s.length() != 0) {
                RegisterPhoneActivity.this.mregist_inputphonenum_edit.setTextSize(2, 19.0f);
                RegisterPhoneActivity.this.mregist_inputphonenum_edit.setTextColor(-16777216);
            } else {
                RegisterPhoneActivity.this.mregist_inputphonenum_edit.setTextSize(2, 15.0f);
                RegisterPhoneActivity.this.mregist_inputphonenum_edit.setTextColor(RegisterPhoneActivity.this.getResources().getColor(R.color.gray));
            }
            RegisterPhoneActivity.this.getCode.setText(R.string.send_identify);
            RegisterPhoneActivity.this.ISCANCELCOUNTDOWNASYN = true;
        }
    };
    private String mThirdcode;
    private TextView mTvCode;
    Handler mUploadPointHandler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 100000:
                    String _sessionid = (String) msg.obj;
                    AjaxCallBack_registNewUser ajaxCallBack_registNewUser = new AjaxCallBack_registNewUser(RegisterPhoneActivity.this, RegisterPhoneActivity.this.mhandler);
                    if (!"+86".equalsIgnoreCase(RegisterPhoneActivity.this.mCountry)) {
                        RegisterPhoneActivity.this._smsverifystr = "contechealth";
                        Meth_android_registNewUser.registNewUser(_sessionid, bs.b, bs.b, bs.b, "4", "ANDROID", bs.b, bs.b, bs.b, bs.b, bs.b, RegisterPhoneActivity.this.mPhonenumber, bs.b, bs.b, "H61100435", bs.b, RegisterPhoneActivity.this._smsverifystr, bs.b, bs.b, ajaxCallBack_registNewUser, App_phms.getInstance().mFinalHttp, String.valueOf(Constants.URL) + "/main.php");
                        return;
                    }
                    RegisterPhoneActivity.this._smsverifystr = "contechealth";
                    Meth_android_registNewUser.registNewUser(_sessionid, bs.b, bs.b, bs.b, "4", "ANDROID", bs.b, bs.b, bs.b, bs.b, bs.b, RegisterPhoneActivity.this.mPhonenumber, bs.b, bs.b, RegisterPhoneActivity.hospitalId, bs.b, RegisterPhoneActivity.this._smsverifystr, bs.b, bs.b, ajaxCallBack_registNewUser, App_phms.getInstance().mFinalHttp, String.valueOf(Constants.URL) + "/main.php");
                    return;
                case Constants.mqueryuserinfotimeout /*104011*/:
                    if (RegisterPhoneActivity.this.mcommitdialogClass != null) {
                        RegisterPhoneActivity.this.mcommitdialogClass.dismiss();
                    }
                    new DialogClass(RegisterPhoneActivity.this, RegisterPhoneActivity.this.getResources().getString(R.string.user_networktimeout));
                    return;
                case Constants.mqueryuserinfoneterror /*104012*/:
                    if (RegisterPhoneActivity.this.mcommitdialogClass != null) {
                        RegisterPhoneActivity.this.mcommitdialogClass.dismiss();
                    }
                    new DialogClass(RegisterPhoneActivity.this, RegisterPhoneActivity.this.getResources().getString(R.string.user_networkerror));
                    return;
                default:
                    return;
            }
        }
    };
    private final int mUploadPointloginsuccess = 100000;
    private DialogClass mcommitdialogClass;
    private final int mhandbeenregistUser = 106902;
    Handler mhandler = new Handler() {
        public void handleMessage(Message msg) {
            if (RegisterPhoneActivity.this.mcommitdialogClass != null) {
                RegisterPhoneActivity.this.mcommitdialogClass.dismiss();
            }
            Message myMessage3 = new Message();
            myMessage3.what = 36;
            switch (msg.what) {
                case 34:
                    new DialogClass(RegisterPhoneActivity.this, RegisterPhoneActivity.this.getResources().getString(R.string.hadbeenregistuser));
                    RegisterPhoneActivity.this.mhandler.sendMessageDelayed(myMessage3, 3000);
                    return;
                case OrderList.DS_FILTER_DATAS /*35*/:
                    if (DialogClass.mDialog != null && DialogClass.mDialog.isShowing()) {
                        DialogClass.mDialog.dismiss();
                    }
                    if (RegisterPhoneActivity.this.mregist_inputphonenum_edit != null) {
                        RegisterPhoneActivity.this.mregist_inputphonenum_edit.setFocusable(true);
                        RegisterPhoneActivity.this.mregist_inputphonenum_edit.requestFocus();
                        RegisterPhoneActivity.this.mNoReceCode.setVisibility(View.GONE);
                        return;
                    }
                    return;
                case OrderList.DS_SAVE_SDCARD /*36*/:
                    if (DialogClass.mDialog != null && DialogClass.mDialog.isShowing()) {
                        DialogClass.mDialog.dismiss();
                        return;
                    }
                    return;
                case 37:
                    RegisterPhoneActivity.this.GotoSexActivity();
                    return;
                case 9999:
                    RegisterPhoneActivity.this.mIsReCheckPhoneNumber = false;
                    return;
                case 100002:
                    new DialogClass(RegisterPhoneActivity.this, RegisterPhoneActivity.this.getResources().getString(R.string.registSessioninvalidation));
                    RegisterPhoneActivity.this.mhandler.sendMessageDelayed(myMessage3, 3000);
                    return;
                case 106800:
                    RegisterPhoneActivity.this.getCode.setClickable(false);
                    if (RegisterPhoneActivity.this.registSmsEdit != null) {
                        RegisterPhoneActivity.this.registSmsEdit.setFocusable(true);
                        RegisterPhoneActivity.this.registSmsEdit.requestFocus();
                    }
                    Method_android_login.sendMsgVerificationCode("0", RegisterPhoneActivity.this.mPhonenumberGetCode, new AjaxCallBack_registSendVerifyCode(RegisterPhoneActivity.this, RegisterPhoneActivity.this.mhandler), App_phms.getInstance().mFinalHttp, String.valueOf(Constants.URL) + "/main.php");
                    new mCountDownAsynCTask().execute(new Integer[]{60});
                    return;
                case 106900:
                    new DialogClass(RegisterPhoneActivity.this, RegisterPhoneActivity.this.getResources().getString(R.string.registnewusersuccesss));
                    RegisterPhoneActivity.this.insterregisbackInfoDb(msg.obj.toString());
                    Message _msg = new Message();
                    _msg.what = 37;
                    RegisterPhoneActivity.this.mhandler.sendMessageDelayed(_msg, 1000);
                    return;
                case 106901:
                    new DialogClass(RegisterPhoneActivity.this, RegisterPhoneActivity.this.getResources().getString(R.string.registnewuserargserror));
                    RegisterPhoneActivity.this.mhandler.sendMessageDelayed(myMessage3, 3000);
                    return;
                case 106902:
                    new DialogClass(RegisterPhoneActivity.this, RegisterPhoneActivity.this.getResources().getString(R.string.hadbeenregistuser));
                    RegisterPhoneActivity.this.mhandler.sendMessageDelayed(myMessage3, 3000);
                    return;
                case 106904:
                    new DialogClass(RegisterPhoneActivity.this, RegisterPhoneActivity.this.getResources().getString(R.string.registnewusersmserror));
                    RegisterPhoneActivity.this.mhandler.sendMessageDelayed(myMessage3, 3000);
                    return;
                case 106905:
                    new DialogClass(RegisterPhoneActivity.this, RegisterPhoneActivity.this.getResources().getString(R.string.registnewuserdberror));
                    RegisterPhoneActivity.this.mhandler.sendMessageDelayed(myMessage3, 3000);
                    return;
                case 106910:
                    new DialogClass((Context) RegisterPhoneActivity.this, 1000, RegisterPhoneActivity.this.getResources().getString(R.string.registnewusererror));
                    RegisterPhoneActivity.this.mhandler.sendMessageDelayed(myMessage3, 3000);
                    return;
                case 106980:
                    AjaxCallBack_registNewUser ajaxCallBack_registNewUser = new AjaxCallBack_registNewUser(RegisterPhoneActivity.this, RegisterPhoneActivity.this.mhandler);
                    RegisterPhoneActivity.this._smsverifystr = "contecthirdparty";
                    RegisterPhoneActivity.this._defaultpassword = "123456";
                    Meth_android_registNewUser.registNewUser("00000000000000000000000000000000", msg.obj.toString(), RegisterPhoneActivity.this._defaultpassword, "1", "6", "ANDROID", bs.b, bs.b, bs.b, bs.b, bs.b, bs.b, bs.b, bs.b, RegisterPhoneActivity.hospitalId, bs.b, RegisterPhoneActivity.this._smsverifystr, bs.b, bs.b, ajaxCallBack_registNewUser, App_phms.getInstance().mFinalHttp, String.valueOf(Constants.URL) + "/main.php");
                    return;
                case 107000:
                    new DialogClass(RegisterPhoneActivity.this, RegisterPhoneActivity.this.getResources().getString(R.string.verifysendsmssuccesss));
                    Message myMessage2 = new Message();
                    myMessage2.what = 36;
                    RegisterPhoneActivity.this.mhandler.sendMessageDelayed(myMessage2, 3000);
                    return;
                case 107001:
                    new DialogClass(RegisterPhoneActivity.this, RegisterPhoneActivity.this.getResources().getString(R.string.verifysendsmsargserror));
                    RegisterPhoneActivity.this.mhandler.sendMessageDelayed(myMessage3, 3000);
                    RegisterPhoneActivity.this.mFlage = true;
                    RegisterPhoneActivity.this.ISCANCELCOUNTDOWNASYN = true;
                    new mCountDownAsynCTask().cancel(true);
                    return;
                case 107002:
                    new DialogClass(RegisterPhoneActivity.this, RegisterPhoneActivity.this.getResources().getString(R.string.verifysendsmscounterror));
                    return;
                case 107003:
                    new DialogClass(RegisterPhoneActivity.this, RegisterPhoneActivity.this.getResources().getString(R.string.verifysendsmsintevertime));
                    RegisterPhoneActivity.this.mhandler.sendMessageDelayed(myMessage3, 3000);
                    RegisterPhoneActivity.this.mFlage = true;
                    RegisterPhoneActivity.this.ISCANCELCOUNTDOWNASYN = true;
                    return;
                case 107004:
                    new DialogClass(RegisterPhoneActivity.this, RegisterPhoneActivity.this.getResources().getString(R.string.verifysendsmssystemerror));
                    RegisterPhoneActivity.this.mhandler.sendMessageDelayed(myMessage3, 3000);
                    return;
                case 107010:
                    new DialogClass(RegisterPhoneActivity.this, RegisterPhoneActivity.this.getResources().getString(R.string.verifysendsmsserror));
                    RegisterPhoneActivity.this.mhandler.sendMessageDelayed(myMessage3, 3000);
                    RegisterPhoneActivity.this.ISCANCELCOUNTDOWNASYN = true;
                    return;
                case 1000101:
                    if (RegisterPhoneActivity.this.mIsReCheckPhoneNumber) {
                        String _phonenumber = RegisterPhoneActivity.this.mregist_inputphonenum_edit.getText().toString().trim();
                        Meth_android_VerifyUserIsExit.veryUserIsExit("1", _phonenumber, new AjaxCallBack_VerifyuserIsExit(RegisterPhoneActivity.this, RegisterPhoneActivity.this.mhandler), App_phms.getInstance().mFinalHttp, String.valueOf(Constants.URL) + "/main.php");
                        return;
                    }
                    new DialogClass(RegisterPhoneActivity.this, RegisterPhoneActivity.this.getResources().getString(R.string.user_networktimeout));
                    RegisterPhoneActivity.this.mhandler.sendMessageDelayed(myMessage3, 2000);
                    return;
                case 1000102:
                    new DialogClass(RegisterPhoneActivity.this, RegisterPhoneActivity.this.getResources().getString(R.string.user_networktimeout));
                    RegisterPhoneActivity.this.mhandler.sendMessageDelayed(myMessage3, 2000);
                    return;
                case 1068001:
                    Message myMessage = new Message();
                    myMessage.what = 34;
                    RegisterPhoneActivity.this.mhandler.sendMessageDelayed(myMessage, 3000);
                    return;
                default:
                    return;
            }
        }
    };
    private final int mneterror = 1000102;
    private final int mnettimeout = 1000101;
    private final int mqueryuserinfoneterror = Constants.mqueryuserinfoneterror;
    private final int mqueryuserinfotimeout = Constants.mqueryuserinfotimeout;
    private final int mregistUserdberror = 106905;
    private final int mregistUsererror = 106910;
    private final int mregistUsersmserror = 106904;
    private EditText mregist_inputphonenum_edit;
    private final int mregisteroAuthsuccess = 106980;
    private final int mregisterror = 100002;
    private final int mregistnewUserargserror = 106901;
    private final int mregistnewUsersuccess = 106900;
    private String msmscontent = new String();
    private String msmsphone;
    private final int mverifyuseridexit = 1068001;
    private final int mverifyuseridservererror = 106801;
    private final int mverifyuseridsuccess = 106800;
    private final int phonehaveregister = 34;
    private final int phonenumbererror = 35;
    private String province = null;
    private Platform qq;
    @SuppressLint({"ShowToast"})
    PlatformActionListener qqPaListener = new PlatformActionListener() {
        public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
            if (i == 8) {
                PlatformDb platDB = platform.getDb();
                RegisterPhoneActivity.this.mThirdcode = platDB.getUserId();
                Message msg = new Message();
                msg.what = 106980;
                msg.obj = platDB.getUserId();
                RegisterPhoneActivity.this.mhandler.sendMessage(msg);
            }
        }

        public void onError(Platform platform, int i, Throwable throwable) {
            Toast.makeText(RegisterPhoneActivity.this, "Login failed", Toast.LENGTH_LONG).show();
            RegisterPhoneActivity.this.qq.removeAccount(false);
        }

        public void onCancel(Platform platform, int i) {
            platform.removeAccount(true);
            Toast.makeText(RegisterPhoneActivity.this, "Cancel login", Toast.LENGTH_LONG).show();
        }
    };
    private EditText registSmsEdit;
    private Button regist_btn;
    DialogInterface.OnKeyListener registerNewUserListener = new DialogInterface.OnKeyListener() {
        public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
            if (keyCode != 4 || event.getRepeatCount() == 0) {
            }
            return false;
        }
    };

    private PhmsSharedPreferences sp;
    private final int verifysmssendagrserror = 107001;
    private final int verifysmssendcounterror = 107002;
    private final int verifysmssenderror = 107010;
    private final int verifysmssendintevertime = 107003;
    private final int verifysmssendsuccess = 107000;
    private final int verifysmssendsystemerror = 107004;
/*
    private Platform wechat;
    PlatformActionListener wechatPaListener = new PlatformActionListener() {
        public void onCancel(Platform arg0, int arg1) {
            Log.e("=============", "==============onCancel()");
        }

        public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
            Log.e("=============", "==============onComplete()");
            if (i == 8) {
                PlatformDb platDB = platform.getDb();
                Log.e("=============", "==============");
                Log.e("userid", "====" + platDB.getUserId());
                Log.e("===============", "=============");
                RegisterPhoneActivity.this.mThirdcode = platDB.getUserId();
                Message msg = new Message();
                msg.what = 106980;
                msg.obj = platDB.getUserId();
                RegisterPhoneActivity.this.mhandler.sendMessage(msg);
            }
        }

        public void onError(Platform arg0, int arg1, Throwable arg2) {
            Log.e("=============", "==============onError()");
        }
    };
    private Platform weibo;
    @SuppressLint({"ShowToast"})
    PlatformActionListener weiboPaListener = new PlatformActionListener() {
        public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
            if (i == 8) {
                PlatformDb platDB = platform.getDb();
                RegisterPhoneActivity.this.mThirdcode = platDB.getUserId();
                Message msg = new Message();
                msg.what = 106980;
                msg.obj = platDB.getUserId();
                RegisterPhoneActivity.this.mhandler.sendMessage(msg);
            }
        }

        public void onError(Platform platform, int i, Throwable throwable) {
            Toast.makeText(RegisterPhoneActivity.this, "Login failed", Toast.LENGTH_LONG).show();
            RegisterPhoneActivity.this.weibo.removeAccount(false);
        }

        public void onCancel(Platform platform, int i) {
            Toast.makeText(RegisterPhoneActivity.this, "取消授权", Toast.LENGTH_LONG).show();
        }
    };
*/

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_register);
        if (Constants.Language.contains("zh")) {
            this.countrys = countrys_zh;
        } else if (Constants.Language.contains("en")) {
            this.countrys = countrys_en;
        }
        initView();
        //ShareSDK.initSDK(this);
    }

    @SuppressLint({"CutPasteId"})
    private void initView() {
        this.getCode = (Button) findViewById(R.id.get_code);
        this.regist_btn = (Button) findViewById(R.id.register_btn);
        this.back_btn = (Button) findViewById(R.id.back_btn);
        this.mRegist_inputphoneid_edit = (EditText) findViewById(R.id.regist_inputphoneid_edit);
        this.mregist_inputphonenum_edit = (EditText) findViewById(R.id.regist_inputphone_edit);
        this.registSmsEdit = (EditText) findViewById(R.id.registSmsEdit);
        this.mTvCode = (TextView) findViewById(R.id.tv_nocode);
        this.btn_register_del = (Button) findViewById(R.id.btn_register_del);
        this.mRegistHostipalName = (TextView) findViewById(R.id.registHostipalName);
        this.mHopitalName = (TextView) findViewById(R.id.registHostipalName);
        this.ll_linkage = (LinearLayout) findViewById(R.id.ll_linkage);
        this.mNoReceCode = (LinearLayout) findViewById(R.id.no_receive_code);
        this.mBtnQqRegister = (Button) findViewById(R.id.btn_qqlogin);
//        this.mBtnFaceBookReg = (Button) findViewById(R.id.btn_facebooklogin);
//        this.mBtnWeiboReg = (Button) findViewById(R.id.btn_weibologin);
//        this.mBtnWeChatReg = (Button) findViewById(R.id.btn_wechatlogin);
//        this.mBtnGoogleReg = (Button) findViewById(R.id.btn_goolepluslogin);
        this.sp = PhmsSharedPreferences.getInstance(this);
        this.back_btn.setOnClickListener(this);
        this.getCode.setOnClickListener(this);
        this.regist_btn.setOnClickListener(this);
        this.mTvCode.setOnClickListener(this);
        this.mHopitalName.setOnClickListener(this);
        this.btn_register_del.setOnClickListener(this);
        this.mBtnQqRegister.setOnClickListener(this);
//        this.mBtnWeiboReg.setOnClickListener(this);
//        this.mBtnWeChatReg.setOnClickListener(this);
//        this.mBtnFaceBookReg.setOnClickListener(this);
//        this.mBtnGoogleReg.setOnClickListener(this);
        this.mregist_inputphonenum_edit.setFocusable(true);
        this.mregist_inputphonenum_edit.setFocusableInTouchMode(true);
        this.mregist_inputphonenum_edit.requestFocus();
        this.mNoReceCode.setVisibility(View.GONE);
        this.mFlage = true;
        this.mregist_inputphonenum_edit.addTextChangedListener(this.mTextWatcher);
        this.mSpinnerCountrtSpinner = (Spinner) findViewById(R.id.spinner_country_zc);
        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        Constants.SCREEN_WIDTH = metric.widthPixels;
        Constants.SCREEN_HEIGHT = metric.heightPixels;
        if (Constants.Language.contains("zh")) {
            setAdapter();
            this.mSpinnerCountrtSpinner.setSelection(226, true);
        } else if (Constants.Language.contains("en")) {
            setAdapter();
            this.mSpinnerCountrtSpinner.setSelection(218, true);
            this.mRegist_inputphoneid_edit.setText("+1");
        }
        changeView();
        this.mSpinnerCountrtSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                RegisterPhoneActivity.this.mRegist_inputphoneid_edit.setText(RegisterPhoneActivity.this.countrys[RegisterPhoneActivity.this.mSpinnerCountrtSpinner.getSelectedItemPosition()]);
                RegisterPhoneActivity.this.changeView();
            }

            public void onNothingSelected(AdapterView<?> adapterView) {
                int countryId = RegisterPhoneActivity.this.mSpinnerCountrtSpinner.getSelectedItemPosition();
                Log.e("jiabianhaohaohaoaho", "ssssssss" + countryId);
                RegisterPhoneActivity.this.mRegist_inputphoneid_edit.setText(RegisterPhoneActivity.this.countrys[countryId]);
            }
        });
        this.mRegist_inputphoneid_edit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    RegisterPhoneActivity.this.mRegist_inputphoneid_edit.setSelection(RegisterPhoneActivity.this.mRegist_inputphoneid_edit.getText().length());
                    RegisterPhoneActivity.this.mRegist_inputphoneid_edit.setText(bs.b);
                    return;
                }
                String editString = RegisterPhoneActivity.this.mRegist_inputphoneid_edit.getText().toString();
                if (!editString.contains("+")) {
                    RegisterPhoneActivity.this.inputPhoneId = "+" + editString;
                    RegisterPhoneActivity.this.mRegist_inputphoneid_edit.setText("+" + editString);
                } else {
                    RegisterPhoneActivity.this.inputPhoneId = editString;
                }
                if (RegisterPhoneActivity.this.inputPhoneId != null) {
                    int i = 0;
                    boolean flag = false;
                    while (true) {
                        if (i >= RegisterPhoneActivity.this.countrys.length) {
                            break;
                        } else if (RegisterPhoneActivity.this.countrys[i].equals(RegisterPhoneActivity.this.inputPhoneId)) {
                            flag = true;
                            break;
                        } else {
                            flag = false;
                            i++;
                        }
                    }
                    if (flag) {
                        RegisterPhoneActivity.this.mSpinnerCountrtSpinner.setSelection(i, true);
                        if (RegisterPhoneActivity.this.inputPhoneId.equals("+1")) {
                            RegisterPhoneActivity.this.mSpinnerCountrtSpinner.performClick();
                            Toast toast = Toast.makeText(RegisterPhoneActivity.this, RegisterPhoneActivity.this.getString(R.string.str_select_country), Toast.LENGTH_LONG);
                            toast.setGravity(17, 0, 0);
                            toast.show();
                        } else if (RegisterPhoneActivity.this.inputPhoneId.equals("+7")) {
                            Toast toast2 = Toast.makeText(RegisterPhoneActivity.this, RegisterPhoneActivity.this.getString(R.string.str_select_country), Toast.LENGTH_LONG);
                            toast2.setGravity(17, 0, 0);
                            toast2.show();
                            RegisterPhoneActivity.this.mSpinnerCountrtSpinner.performClick();
                        } else if (RegisterPhoneActivity.this.inputPhoneId.equals("+44")) {
                            Toast toast3 = Toast.makeText(RegisterPhoneActivity.this, RegisterPhoneActivity.this.getString(R.string.str_select_country), Toast.LENGTH_LONG);
                            toast3.setGravity(17, 0, 0);
                            toast3.show();
                            RegisterPhoneActivity.this.mSpinnerCountrtSpinner.performClick();
                        } else if (RegisterPhoneActivity.this.inputPhoneId.equals("+61")) {
                            Toast toast4 = Toast.makeText(RegisterPhoneActivity.this, RegisterPhoneActivity.this.getString(R.string.str_select_country), Toast.LENGTH_LONG);
                            toast4.setGravity(17, 0, 0);
                            toast4.show();
                            RegisterPhoneActivity.this.mSpinnerCountrtSpinner.performClick();
                        }
                    } else {
                        new DialogClass(RegisterPhoneActivity.this, RegisterPhoneActivity.this.getResources().getString(R.string.str_cont_find));
                        RegisterPhoneActivity.this.mRegist_inputphoneid_edit.setText(bs.b);
                    }
                }
            }
        });
        spinnerProvince();
    }

    @SuppressLint("ResourceType")
    private void setAdapter() {
//        ArrayAdapter<String> adapte2 = new ArrayAdapter<>(this, 17367048, getResources().getStringArray(R.array.phone_code_list2));
//        adapte2.setDropDownViewResource(17367049);
        @SuppressLint("ResourceType") ArrayAdapter<String> adapte2 = new ArrayAdapter<>(this, 17367048, getResources().getStringArray(R.array.phone_code_list2));
        adapte2.setDropDownViewResource(17367049);
        this.mSpinnerCountrtSpinner.setAdapter(adapte2);
    }

    private void insterregisbackInfoDb(String backInforcontext) {
        if (backInforcontext != null && !backInforcontext.equals(bs.b)) {
            new LoginUserDao();
            DocumentBuilderFactory _factory = DocumentBuilderFactory.newInstance();
            try {
                Document _document = _factory.newDocumentBuilder().parse(new ByteArrayInputStream(backInforcontext.getBytes()));
                LocalLoginInfoDao _localinfor = new LocalLoginInfoDao();
                _localinfor.mCardNb = _document.getDocumentElement().getElementsByTagName("uid").item(0).getTextContent();
                this.mLoginUserId = _localinfor.mCardNb;
                _localinfor.mPassword = this._defaultpassword;
                _localinfor.mThirdCode = this.mThirdcode;
                LocalLoginInfoManager.getInstance().add(_localinfor);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void GotoSexActivity() {
        this.sp.saveColume("phoneNum", this.mPhonenumber);
        startActivity(new Intent(this, RegisterSexActivity.class));
        finish();
    }

    @SuppressLint({"ShowToast"})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_btn:
                startActivity(new Intent(this, LoginActivity.class));
                finish();
                return;
            case R.id.btn_register_del:
                this.mregist_inputphonenum_edit.setText(bs.b);
                this.mregist_inputphonenum_edit.setFocusable(true);
                this.mregist_inputphonenum_edit.setFocusableInTouchMode(true);
                this.mregist_inputphonenum_edit.requestFocus();
                this.mNoReceCode.setVisibility(View.GONE);
                this.mFlage = true;
                this.btn_register_del.setVisibility(View.INVISIBLE);
                return;
            case R.id.get_code:
                this.mCountry = this.mRegist_inputphoneid_edit.getText().toString().trim();
                if (this.mCountry.equals("+86")) {
                    if (this.mregist_inputphonenum_edit.getText().toString().length() != 11) {
                        new DialogClass(this, getResources().getString(R.string.str_phonenumlengthiserror));
                        Message myMessage1 = new Message();
                        myMessage1.what = 35;
                        this.mhandler.sendMessageDelayed(myMessage1, 1000);
                        return;
                    }
                    Toast.makeText(getApplicationContext(), getString(R.string.sentrequest), Toast.LENGTH_SHORT).show();
                    this.mPhonenumberGetCode = this.mregist_inputphonenum_edit.getText().toString().trim();
                    this.mPhonenumber = this.mregist_inputphonenum_edit.getText().toString().trim();
                    if (!this.mCountry.equals("+86")) {
                        this.mPhonenumberGetCode = String.valueOf(this.mCountry) + "-" + this.mPhonenumberGetCode;
                    }
                    Meth_android_VerifyUserIsExit.veryUserIsExit("1", this.mPhonenumber, new AjaxCallBack_VerifyuserIsExit(this, this.mhandler), App_phms.getInstance().mFinalHttp, String.valueOf(Constants.URL) + "/main.php");
                    this.mIsReCheckPhoneNumber = true;
                    this.mhandler.sendEmptyMessageDelayed(9999, 5000);
                    return;
                } else if (this.mregist_inputphonenum_edit.length() <= 5) {
                    Toast.makeText(this, "输入正确手机号", Toast.LENGTH_LONG).show();
                    return;
                } else if (this.getCode.getText().toString() == "重发验证码" || this.getCode.getText().toString().equals("重发验证码")) {
                    toShowDialog();
                    return;
                } else {
                    Toast.makeText(getApplicationContext(), getString(R.string.sentrequest), Toast.LENGTH_SHORT).show();
                    this.mPhonenumberGetCode = this.mregist_inputphonenum_edit.getText().toString().trim();
                    this.mPhonenumber = this.mregist_inputphonenum_edit.getText().toString().trim();
                    if (!this.mCountry.equals("+86")) {
                        this.mPhonenumberGetCode = String.valueOf(this.mCountry) + "-" + this.mPhonenumberGetCode;
                    }
                    Meth_android_VerifyUserIsExit.veryUserIsExit("1", this.mPhonenumber, new AjaxCallBack_VerifyuserIsExit(this, this.mhandler), App_phms.getInstance().mFinalHttp, String.valueOf(Constants.URL) + "/main.php");
                    this.mIsReCheckPhoneNumber = true;
                    this.mhandler.sendEmptyMessageDelayed(9999, 5000);
                    return;
                }
            case R.id.registHostipalName:
                selectHospitalDialog();
                return;
            case R.id.register_btn:
                this._smsverifystr = this.registSmsEdit.getText().toString().trim();
                this.mNoReceCode.setVisibility(View.GONE);
                String phoneNum = this.mregist_inputphonenum_edit.getText().toString().trim();
                this.mPhonenumber = this.mregist_inputphonenum_edit.getText().toString().trim();
                this.mCountry = this.mRegist_inputphoneid_edit.getText().toString().trim();
                if (!this.mCountry.equalsIgnoreCase("+86") || !this.mRegistHostipalName.getText().toString().trim().equalsIgnoreCase(bs.b)) {
                    if (this.dialog != null) {
                        this.dialog.equals(bs.b);
                    }
                    if (phoneNum.length() > 15 || phoneNum.equals(bs.b)) {
                        new DialogClass(this, getResources().getString(R.string.str_phonenumregistlengthiserror));
                        return;
                    } else if (this._smsverifystr.equals(bs.b)) {
                        new DialogClass(this, getResources().getString(R.string.registnoidenti));
                        return;
                    } else {
                        this.mcommitdialogClass = new DialogClass((Context) this, getString(R.string.register_ing), false, 0, this.registerNewUserListener);
                        AjaxCallBack_registNewUser ajaxCallBack_registNewUser = new AjaxCallBack_registNewUser(this, this.mhandler);
                        if (this._smsverifystr.equals("7762")) {
                            requestUploadPointSessionid();
                            return;
                        } else if (!"+86".equalsIgnoreCase(this.mCountry)) {
                            CLog.i(TAG, " user输入的验证码是: " + this._smsverifystr + "  原本的验证码是: " + this.msmscontent + ",它们相等，替换成超级验证码");
                            this._smsverifystr = "7762";
                            requestUploadPointSessionid();
                            return;
                        } else {
                            Meth_android_registNewUser.registNewUser("00000000000000000000000000000000", bs.b, bs.b, bs.b, "4", "ANDROID", bs.b, bs.b, bs.b, bs.b, bs.b, this.mPhonenumber, bs.b, bs.b, hospitalId, bs.b, this._smsverifystr, bs.b, bs.b, ajaxCallBack_registNewUser, App_phms.getInstance().mFinalHttp, String.valueOf(Constants.URL) + "/main.php");
                            return;
                        }
                    }
                } else {
                    new DialogClass(this, getResources().getString(R.string.str_check_hospital));
                    return;
                }
            /*
            case R.id.btn_qqlogin:
                this.qq = ShareSDK.getPlatform(QQ.NAME);
                if (this.qq.isValid()) {
                    this.qq.removeAccount(true);
                    ShareSDK.removeCookieOnAuthorize(true);
                }
                this.qq.setPlatformActionListener(this.qqPaListener);
                this.qq.showUser((String) null);
                this.qq.authorize();
                return;
            case R.id.btn_facebooklogin:
                this.facebook = ShareSDK.getPlatform(Facebook.NAME);
                if (this.facebook.isValid()) {
                    this.facebook.removeAccount(true);
                    ShareSDK.removeCookieOnAuthorize(true);
                }
                this.facebook.setPlatformActionListener(this.facebookPaListener);
                this.facebook.showUser((String) null);
                this.facebook.authorize();
                return;
            case R.id.btn_weibologin:
                this.weibo = ShareSDK.getPlatform(SinaWeibo.NAME);
                if (this.weibo.isValid()) {
                    this.weibo.removeAccount(true);
                    ShareSDK.removeCookieOnAuthorize(true);
                }
                this.weibo.setPlatformActionListener(this.weiboPaListener);
                this.weibo.showUser((String) null);
                this.weibo.authorize();
                return;
            case R.id.btn_wechatlogin:
                this.wechat = ShareSDK.getPlatform(Wechat.NAME);
                if (this.wechat.isValid()) {
                    this.wechat.removeAccount(true);
                    ShareSDK.removeCookieOnAuthorize(true);
                }
                this.wechat.setPlatformActionListener(this.wechatPaListener);
                this.wechat.showUser((String) null);
                return;
            case R.id.btn_goolepluslogin:
                this.gooleplus = ShareSDK.getPlatform(GooglePlus.NAME);
                if (this.gooleplus.isValid()) {
                    this.gooleplus.removeAccount(true);
                    ShareSDK.removeCookieOnAuthorize(true);
                }
                this.gooleplus.setPlatformActionListener(this.gooleplusPaListener);
                this.gooleplus.showUser((String) null);
                this.gooleplus.authorize();
                return;

             */
            case R.id.tv_nocode:
                startActivityForResult(new Intent(this, CaptureActivity.class), 0);
                return;
            default:
                return;
        }
    }

    private void toShowDialog() {
        CustomDialog.Builder customBuilder = new CustomDialog.Builder(this);
        customBuilder.setTitle(getString(R.string.str_backup)).setMessage(getString(R.string.str_change_phone)).setNegativeButton(getString(R.string.str_yes), (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        }).setPositiveButton(getString(R.string.str_no), (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                switch (which) {
                    case -2:
                        RegisterPhoneActivity.this.ISCANCELCOUNTDOWNASYN = true;
                        RegisterPhoneActivity.this.mFlage = true;
                        new mCountDownAsynCTask().cancel(true);
                        RegisterPhoneActivity.this.getCode.setText(R.string.send_identify);
                        RegisterPhoneActivity.this.mNoReceCode.setVisibility(View.VISIBLE);
                        return;
                    case -1:
                        RegisterPhoneActivity.this.mregist_inputphonenum_edit.setText(bs.b);
                        RegisterPhoneActivity.this.mregist_inputphonenum_edit.requestFocus();
                        RegisterPhoneActivity.this.mNoReceCode.setVisibility(View.GONE);
                        RegisterPhoneActivity.this.mFlage = true;
                        RegisterPhoneActivity.this.mDialog.dismiss();
                        return;
                    default:
                        return;
                }
            }
        });
        this.mDialog = customBuilder.create();
        this.mDialog.show();
    }

    private void selectHospitalDialog() {
        this.dialog = new SelectHospitalDialog(this, R.style.Dialog, this.mRegistHostipalName);
        this.dialog.show();
        Display d = getWindowManager().getDefaultDisplay();
        Window dialogWindow = this.dialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        dialogWindow.setGravity(17);
        lp.height = (int) (((double) d.getHeight()) * 0.38d);
        lp.width = (int) (((double) d.getWidth()) * 0.9d);
        dialogWindow.setAttributes(lp);
    }

    /* access modifiers changed from: protected */
    @SuppressLint({"ShowToast"})
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0 && resultCode == -1) {
            String[] strs = data.getStringExtra(PluseDataDao.RESULT).split("_");
            if (strs.length == 3) {
                this.mRegistHostipalName.setText(strs[2]);
                return;
            }
            Toast.makeText(this, "不是正确的医院名称", Toast.LENGTH_LONG).show();
        }
    }

    private void requestUploadPointSessionid() {
        Method_android_login.login("C13032302700035", "contec20140825", 1, new AjaxCallBack_login_uploadpoint(this, this.mUploadPointHandler), App_phms.getInstance().mFinalHttp, String.valueOf(Constants.URL) + "/login.php");
    }

    private void sendSMS(String phoneNum, String smsContent) {
        if (phoneNum.equals(bs.b)) {
            Toast.makeText(this, getString(R.string.str_phonenumregistlengthiserror), Toast.LENGTH_SHORT).show();
            return;
        }
        SmsManager smsManager = SmsManager.getDefault();
        if (smsContent.length() > 70) {
            for (String sms : smsManager.divideMessage(smsContent)) {
                smsManager.sendTextMessage(phoneNum, (String) null, sms, (PendingIntent) null, (PendingIntent) null);
            }
            return;
        }
        smsManager.sendTextMessage(phoneNum, (String) null, smsContent, (PendingIntent) null, (PendingIntent) null);
    }

    private String getRandom(int n) {
        StringBuilder _strBuilder = new StringBuilder();
        for (int i = 0; i < n; i++) {
            _strBuilder.append((int) (Math.random() * 10.0d));
        }
        System.out.println(_strBuilder.toString());
        return _strBuilder.toString();
    }

    class mCountDownAsynCTask extends AsyncTask<Integer, Integer, Boolean> {
        mCountDownAsynCTask() {
        }

        protected Boolean doInBackground(Integer... params) {
            CLog.i("less", "ISCANCELCOUNTDOWNASYN = " + RegisterPhoneActivity.this.ISCANCELCOUNTDOWNASYN);
            boolean _result = false;
            int i = params[0].intValue();
            while (true) {
                if (i < 0) {
                    break;
                } else if (RegisterPhoneActivity.this.ISCANCELCOUNTDOWNASYN) {
                    _result = true;
                    break;
                } else {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    publishProgress(new Integer[]{Integer.valueOf(i)});
                    i--;
                }
            }
            return Boolean.valueOf(_result);
        }

        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            RegisterPhoneActivity.this.getCode.setClickable(true);
            if (RegisterPhoneActivity.this.mFlage) {
                RegisterPhoneActivity.this.getCode.setText(R.string.send_identify);
                RegisterPhoneActivity.this.mFlage = false;
                return;
            }
            RegisterPhoneActivity.this.getCode.setText(R.string.re_send_identify);
        }

        protected void onPreExecute() {
            super.onPreExecute();
            RegisterPhoneActivity.this.ISCANCELCOUNTDOWNASYN = false;
            RegisterPhoneActivity.this.getCode.setClickable(false);
        }

        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            RegisterPhoneActivity.this.getCode.setText(values[0] + RegisterPhoneActivity.this.getResources().getString(R.string.str_register_getcode));
        }
    }

    private void spinnerProvince() {
        this.mDbManager = new RegisterPhoneCityDBManager(this);
        this.mDbManager.openDatabase();
        this.mSdb = this.mDbManager.getmDatabase();
        List<CityListItem> list2 = new ArrayList<>();
        try {
            if (this.mSdb != null && !this.mSdb.equals(bs.b)) {
                Cursor cursor = this.mSdb.rawQuery("select * from province", (String[]) null);
                cursor.moveToFirst();
                while (!cursor.isLast()) {
                    @SuppressLint("Range") String code = cursor.getString(cursor.getColumnIndex("code"));
                    String name = new String(cursor.getBlob(2), CPushMessageCodec.GBK);
                    CityListItem cityListItem = new CityListItem();
                    cityListItem.setName(name);
                    cityListItem.setPcode(code);
                    list2.add(cityListItem);
                    cursor.moveToNext();
                }
                @SuppressLint("Range") String code2 = cursor.getString(cursor.getColumnIndex("code"));
                String name2 = new String(cursor.getBlob(2), CPushMessageCodec.GBK);
                CityListItem cityListItem2 = new CityListItem();
                cityListItem2.setName(name2);
                cityListItem2.setPcode(code2);
                list2.add(cityListItem2);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        this.mDbManager.closeDatabase();
        if (this.mSdb != null && !this.mSdb.equals(bs.b)) {
            this.mSdb.close();
        }
        new RegisterPhoneCityAdapter(this, list2);
    }

    private void spinnerCity(String pcode) {
        this.mDbManager = new RegisterPhoneCityDBManager(this);
        this.mDbManager.openDatabase();
        this.mSdb = this.mDbManager.getmDatabase();
        List<CityListItem> list2 = new ArrayList<>();
        try {
            String sql = "select * from city where pcode='" + pcode + "'";
            if (this.mSdb != null && !this.mSdb.equals(bs.b)) {
                Cursor cursor = this.mSdb.rawQuery(sql, (String[]) null);
                cursor.moveToFirst();
                while (!cursor.isLast()) {
                    @SuppressLint("Range") String code = cursor.getString(cursor.getColumnIndex("code"));
                    String name = new String(cursor.getBlob(2), CPushMessageCodec.GBK);
                    CityListItem cityListItem = new CityListItem();
                    cityListItem.setName(name);
                    cityListItem.setPcode(code);
                    list2.add(cityListItem);
                    cursor.moveToNext();
                }
                @SuppressLint("Range") String code2 = cursor.getString(cursor.getColumnIndex("code"));
                String name2 = new String(cursor.getBlob(2), CPushMessageCodec.GBK);
                CityListItem cityListItem2 = new CityListItem();
                cityListItem2.setName(name2);
                cityListItem2.setPcode(code2);
                list2.add(cityListItem2);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        this.mDbManager.closeDatabase();
        if (this.mSdb != null && !this.mSdb.equals(bs.b)) {
            this.mSdb.close();
        }
        new RegisterPhoneCityAdapter(this, list2);
    }

    private void spinnerDistrict(String pcode) {
        this.mDbManager = new RegisterPhoneCityDBManager(this);
        this.mDbManager.openDatabase();
        this.mSdb = this.mDbManager.getmDatabase();
        List<CityListItem> list2 = new ArrayList<>();
        try {
            String sql = "select * from district where pcode='" + pcode + "'";
            if (this.mSdb != null && !this.mSdb.equals(bs.b)) {
                Cursor cursor = this.mSdb.rawQuery(sql, (String[]) null);
                cursor.moveToFirst();
                while (!cursor.isLast()) {
                    @SuppressLint("Range") String code = cursor.getString(cursor.getColumnIndex("code"));
                    String name = new String(cursor.getBlob(2), CPushMessageCodec.GBK);
                    CityListItem cityListItem = new CityListItem();
                    cityListItem.setName(name);
                    cityListItem.setPcode(code);
                    cityListItem.setId(new String(cursor.getBlob(1), CPushMessageCodec.UTF8));
                    list2.add(cityListItem);
                    cursor.moveToNext();
                }
                @SuppressLint("Range") String code2 = cursor.getString(cursor.getColumnIndex("code"));
                String name2 = new String(cursor.getBlob(2), CPushMessageCodec.GBK);
                CityListItem cityListItem2 = new CityListItem();
                cityListItem2.setName(name2);
                cityListItem2.setPcode(code2);
                cityListItem2.setId(new String(cursor.getBlob(1), CPushMessageCodec.UTF8));
                list2.add(cityListItem2);
            }
        } catch (Exception e) {
        }
        this.mDbManager.closeDatabase();
        if (this.mSdb != null && !this.mSdb.equals(bs.b)) {
            this.mSdb.close();
        }
        new RegisterPhoneCityAdapter(this, list2);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode != 4) {
            return false;
        }
        startActivity(new Intent(this, LoginActivity.class));
        finish();
        return false;
    }

    class SpinnerOnSelectedListener1 implements AdapterView.OnItemSelectedListener {
        SpinnerOnSelectedListener1() {
        }

        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
            RegisterPhoneActivity.this.province = ((CityListItem) adapterView.getItemAtPosition(position)).getName();
            String pcode = ((CityListItem) adapterView.getItemAtPosition(position)).getPcode();
            RegisterPhoneActivity.this.spinnerCity(pcode);
            RegisterPhoneActivity.this.spinnerDistrict(pcode);
        }

        public void onNothingSelected(AdapterView<?> adapterView) {
        }
    }

    class SpinnerOnSelectedListener2 implements AdapterView.OnItemSelectedListener {
        SpinnerOnSelectedListener2() {
        }

        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
            RegisterPhoneActivity.this.city = ((CityListItem) adapterView.getItemAtPosition(position)).getName();
            RegisterPhoneActivity.this.spinnerDistrict(((CityListItem) adapterView.getItemAtPosition(position)).getPcode());
        }

        public void onNothingSelected(AdapterView<?> adapterView) {
        }
    }

    class SpinnerOnSelectedListener3 implements AdapterView.OnItemSelectedListener {
        SpinnerOnSelectedListener3() {
        }

        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
            RegisterPhoneActivity.this.district = ((CityListItem) adapterView.getItemAtPosition(position)).getName();
            RegisterPhoneActivity.this.districtId = ((CityListItem) adapterView.getItemAtPosition(position)).getId();
            RegisterPhoneActivity.this.doPostHospital(RegisterPhoneActivity.this.districtId.substring(0, 6));
        }

        public void onNothingSelected(AdapterView<?> adapterView) {
        }
    }

    private void doPostHospital(String districtId2) {
        Meth_android_getHospital.getHospital(districtId2, "00000000000000000000000000000000", new AjaxCallBack_getHospital(getApplicationContext(), this.mHandler), App_phms.getInstance().mFinalHttp, String.valueOf(Constants.URL) + "/main.php");
    }

    private void changeView() {
        if (this.mRegist_inputphoneid_edit.getText().toString().equals("+86")) {
            this.ll_linkage.setVisibility(View.GONE);
            this.mTvCode.setVisibility(View.GONE);
            return;
        }
        this.ll_linkage.setVisibility(View.GONE);
        this.mTvCode.setVisibility(View.GONE);
    }

    protected void onResume() {
        super.onResume();
        //MobclickAgent.onResume(this);
    }

    protected void onPause() {
        super.onPause();
        //MobclickAgent.onPause(this);
    }

    protected void onDestroy() {
        super.onDestroy();
        new mCountDownAsynCTask().cancel(true);
        this.ISCANCELCOUNTDOWNASYN = true;
    }
}
