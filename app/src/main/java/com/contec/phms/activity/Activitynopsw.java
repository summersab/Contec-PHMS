package com.contec.phms.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.conect.json.CLog;
import com.contec.phms.App_phms;
import com.contec.phms.R;
import com.contec.phms.ajaxcallback.AjaxCallBack_ReSetPwd;
import com.contec.phms.ajaxcallback.AjaxCallBack_VerifyuserIsExit;
import com.contec.phms.ajaxcallback.AjaxCallBack_registNewUser;
import com.contec.phms.ajaxcallback.AjaxCallBack_registSendVerifyCode;
import com.contec.phms.login.ActivityBase;
import com.contec.phms.login.LoginActivity;
import com.contec.phms.manager.message.OrderList;
import com.contec.phms.db.LocalLoginInfoDao;
import com.contec.phms.db.LocalLoginInfoManager;
import com.contec.phms.util.Constants;
import com.contec.phms.widget.DialogClass;
//import com.umeng.analytics.MobclickAgent;
import java.io.ByteArrayInputStream;
import java.util.regex.Pattern;
import javax.xml.parsers.DocumentBuilderFactory;
import cn.com.contec_net_3_android.Meth_android_ReSetPwd;
import cn.com.contec_net_3_android.Meth_android_VerifyUserIsExit;
import cn.com.contec_net_3_android.Meth_android_registNewUser;
import cn.com.contec_net_3_android.Method_android_login;
import u.aly.bs;

public class Activitynopsw extends ActivityBase implements View.OnClickListener {
    private boolean ISCANCELCOUNTDOWNASYN = false;
    private boolean ISTIPUSERHEIGHT = true;
    private final String TAG = getClass().getSimpleName();
    private final int gotologinactivity = 0;
    private final int identifyhaveSent = 36;
    private boolean mIsReCheckPhoneNumber = true;
    private Button mRegistSendMsgVerifyCode;
    private final int mResetPwd = 65;
    private String mail;
    private EditText mailId;
    private Button mailIdReset;
    private LinearLayout mailSecret;
    private Button mailSort;
    private DialogClass mcommitdialogClass;
    private final int mhandbeenregistUser = 106902;
    Handler mhandler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (Activitynopsw.this.mcommitdialogClass != null) {
                Activitynopsw.this.mcommitdialogClass.dismiss();
            }
            switch (msg.what) {
                case 0:
                    if (DialogClass.mDialog != null && DialogClass.mDialog.isShowing()) {
                        DialogClass.mDialog.dismiss();
                    }
                    Activitynopsw.this.gotoLoginActivity();
                    return;
                case 34:
                    if (DialogClass.mDialog != null && DialogClass.mDialog.isShowing()) {
                        DialogClass.mDialog.dismiss();
                    }
                    Activitynopsw.this.mregist_inputphonenum_edit.setText(bs.b);
                    return;
                case OrderList.DS_FILTER_DATAS /*35*/:
                    if (DialogClass.mDialog != null && DialogClass.mDialog.isShowing()) {
                        DialogClass.mDialog.dismiss();
                    }
                    if (Activitynopsw.this.mregist_inputphonenum_edit != null) {
                        Activitynopsw.this.mregist_inputphonenum_edit.setFocusable(true);
                        Activitynopsw.this.mregist_inputphonenum_edit.requestFocus();
                        return;
                    }
                    return;
                case OrderList.DS_SAVE_SDCARD /*36*/:
                    if (DialogClass.mDialog != null && DialogClass.mDialog.isShowing()) {
                        DialogClass.mDialog.dismiss();
                    }
                    if (Activitynopsw.this.mregistInputSmsVerifyedit != null) {
                        Activitynopsw.this.mregistInputSmsVerifyedit.setFocusable(true);
                        Activitynopsw.this.mregistInputSmsVerifyedit.requestFocus();
                        return;
                    }
                    return;
                case 9999:
                    Activitynopsw.this.mIsReCheckPhoneNumber = false;
                    return;
                case 100002:
                    CLog.i("less", "收到了 regist  100002");
                    new DialogClass(Activitynopsw.this, Activitynopsw.this.getResources().getString(R.string.registSessioninvalidation)).show();
                    return;
                case 106800:
                    Activitynopsw.this.misCheckPhoneNumRegistProBar.setVisibility(View.GONE);
                    Activitynopsw.this.mregist_phonenumber_isuseImageB.setBackgroundResource(R.drawable.img_phonemumisnotregist);
                    Activitynopsw.this.mregist_phonenumber_isuseTextV.setText(Activitynopsw.this.getString(R.string.resetpwdphoneisnotexit));
                    Activitynopsw.this.mregist_phonenumber_isusely.setVisibility(View.VISIBLE);
                    return;
                case 106801:
                    Activitynopsw.this.misCheckPhoneNumRegistProBar.setVisibility(View.GONE);
                    Toast.makeText(Activitynopsw.this, "Server error while verifying phone number！", Toast.LENGTH_SHORT).show();
                    return;
                case 106900:
                    String backInforcontext = msg.obj.toString();
                    if (backInforcontext != null && !backInforcontext.equals(bs.b)) {
                        DocumentBuilderFactory _factory = DocumentBuilderFactory.newInstance();
                        try {
                            String mCardNb = _factory.newDocumentBuilder().parse(new ByteArrayInputStream(backInforcontext.getBytes())).getDocumentElement().getElementsByTagName("uid").item(0).getTextContent();
                            Activitynopsw.this.insterregisbackInfoDb(mCardNb);
                            Activitynopsw.this.doResetSecret(mCardNb);
                            return;
                        } catch (Exception e) {
                            new DialogClass(Activitynopsw.this, Activitynopsw.this.getResources().getString(R.string.loginuserisnotexit));
                            e.printStackTrace();
                            return;
                        }
                    } else {
                        return;
                    }
                case 106901:
                    new DialogClass(Activitynopsw.this, Activitynopsw.this.getResources().getString(R.string.registnewuserargserror)).show();
                    return;
                case 106902:
                    new DialogClass(Activitynopsw.this, Activitynopsw.this.getResources().getString(R.string.hadbeenregistuser)).show();
                    return;
                case 106904:
                    new DialogClass(Activitynopsw.this, Activitynopsw.this.getResources().getString(R.string.registnewusersmserror)).show();
                    return;
                case 106905:
                    new DialogClass(Activitynopsw.this, Activitynopsw.this.getResources().getString(R.string.registnewuserdberror)).show();
                    return;
                case 106910:
                    new DialogClass(Activitynopsw.this, Activitynopsw.this.getResources().getString(R.string.registnewusererror)).show();
                    com.contec.phms.util.CLog.i(Activitynopsw.this.TAG, "开始弹出  网络连接失败");
                    return;
                case 107000:
                    new DialogClass(Activitynopsw.this, Activitynopsw.this.getResources().getString(R.string.verifysendsmssuccesss)).show();
                    Message myMessage2 = new Message();
                    myMessage2.what = 36;
                    Activitynopsw.this.mhandler.sendMessageDelayed(myMessage2, 3000);
                    return;
                case 107001:
                    new DialogClass(Activitynopsw.this, Activitynopsw.this.getResources().getString(R.string.verifysendsmsargserror)).show();
                    return;
                case 107002:
                    new DialogClass(Activitynopsw.this, Activitynopsw.this.getResources().getString(R.string.verifysendsmscounterror)).show();
                    return;
                case 107003:
                    new DialogClass(Activitynopsw.this, Activitynopsw.this.getResources().getString(R.string.verifysendsmsintevertime)).show();
                    Activitynopsw.this.ISCANCELCOUNTDOWNASYN = true;
                    return;
                case 107004:
                    new DialogClass(Activitynopsw.this, Activitynopsw.this.getResources().getString(R.string.verifysendsmssystemerror)).show();
                    return;
                case 107010:
                    new DialogClass(Activitynopsw.this, Activitynopsw.this.getResources().getString(R.string.verifysendsmsserror)).show();
                    Activitynopsw.this.ISCANCELCOUNTDOWNASYN = true;
                    com.contec.phms.util.CLog.i(Activitynopsw.this.TAG, "开始弹出  网络异常，请重试");
                    return;
                case 108900:
                    Activitynopsw.this.updateSecretInfoDb(Activitynopsw.this.mail);
                    new DialogClass(Activitynopsw.this, Activitynopsw.this.getResources().getString(R.string.resetpwdsuccess)).show();
                    Message _msg1 = new Message();
                    _msg1.what = 0;
                    Activitynopsw.this.mhandler.sendMessageDelayed(_msg1, 1000);
                    return;
                case 108901:
                    new DialogClass(Activitynopsw.this, Activitynopsw.this.getResources().getString(R.string.resetpwdagrserror)).show();
                    return;
                case 108902:
                    new DialogClass(Activitynopsw.this, Activitynopsw.this.getResources().getString(R.string.resetpwdcounterror)).show();
                    return;
                case 108903:
                    new DialogClass(Activitynopsw.this, Activitynopsw.this.getResources().getString(R.string.resetpwdsmsovertime)).show();
                    return;
                case 108904:
                    new DialogClass(Activitynopsw.this, Activitynopsw.this.getResources().getString(R.string.resetpwddberror)).show();
                    return;
                case 108910:
                    new DialogClass(Activitynopsw.this, Activitynopsw.this.getResources().getString(R.string.network_anomalies)).show();
                    return;
                case 1000101:
                    Activitynopsw.this.misCheckPhoneNumRegistProBar.setVisibility(View.GONE);
                    if (Activitynopsw.this.mIsReCheckPhoneNumber) {
                        Meth_android_VerifyUserIsExit.veryUserIsExit("1", Activitynopsw.this.mregist_inputphonenum_edit.getText().toString().trim(), new AjaxCallBack_VerifyuserIsExit(Activitynopsw.this, Activitynopsw.this.mhandler), App_phms.getInstance().mFinalHttp, String.valueOf(Constants.URL) + "/main.php");
                        return;
                    } else {
                        new DialogClass(Activitynopsw.this, Activitynopsw.this.getResources().getString(R.string.user_networktimeout)).show();
                        return;
                    }
                case 1000102:
                    Activitynopsw.this.misCheckPhoneNumRegistProBar.setVisibility(View.GONE);
                    new DialogClass(Activitynopsw.this, Activitynopsw.this.getResources().getString(R.string.user_networktimeout)).show();
                    return;
                case 1068001:
                    Activitynopsw.this.mRegistSendMsgVerifyCode.setBackgroundResource(R.drawable.drawable_loginpage_button_register_new);
                    Activitynopsw.this.mRegistSendMsgVerifyCode.setClickable(true);
                    Activitynopsw.this.misCheckPhoneNumRegistProBar.setVisibility(View.GONE);
                    Activitynopsw.this.mregist_phonenumber_isuseImageB.setBackgroundResource(R.drawable.img_phonemumisregist);
                    Activitynopsw.this.mregist_phonenumber_isuseTextV.setText(Activitynopsw.this.getString(R.string.resetpwdphoneexittip));
                    Activitynopsw.this.mregist_phonenumber_isusely.setVisibility(View.VISIBLE);
                    return;
                default:
                    return;
            }
        }
    };
    private ProgressBar misCheckPhoneNumRegistProBar;
    private final int mneterror = 1000102;
    private final int mnettimeout = 1000101;
    private EditText mregistHeightedit;
    private EditText mregistInputSmsVerifyedit;
    private final int mregistUserdberror = 106905;
    private final int mregistUsererror = 106910;
    private final int mregistUsersmserror = 106904;
    private EditText mregist_inputphonenum_edit;
    private ImageButton mregist_phonenumber_isuseImageB;
    private TextView mregist_phonenumber_isuseTextV;
    private LinearLayout mregist_phonenumber_isusely;
    private Button mregistcountdownbtn;
    private final int mregisterror = 100002;
    private EditText mregistidentitycardedit;
    private final int mregistnewUserargserror = 106901;
    private final int mregistnewUsersuccess = 106900;
    private final int mverifyuseridexit = 1068001;
    private final int mverifyuseridservererror = 106801;
    private final int mverifyuseridsuccess = 106800;
    private EditText newSecret;
    private LinearLayout phoneSecret;
    private Button phoneSort;
    private final int phonehaveregister = 34;
    private final int phonenumbererror = 35;
    DialogInterface.OnKeyListener registerNewUserListener = new DialogInterface.OnKeyListener() {
        public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
            if (keyCode != 4 || event.getRepeatCount() == 0) {
            }
            return false;
        }
    };
    private EditText repitNewSecret;
    private String resetNewSecret = "123456";
    private final int resetpwdagrserror = 108901;
    private final int resetpwdcounterror = 108902;
    private final int resetpwddberror = 108904;
    private final int resetpwdneterror = 108910;
    private final int resetpwdsmsovertime = 108903;
    private final int resetpwdsuccess = 108900;
    private LinearLayout sortSelect;
    private final int verifysmssendagrserror = 107001;
    private final int verifysmssendcounterror = 107002;
    private final int verifysmssenderror = 107010;
    private final int verifysmssendintevertime = 107003;
    private final int verifysmssendsuccess = 107000;
    private final int verifysmssendsystemerror = 107004;

    protected void onResume() {
        super.onResume();
        //MobclickAgent.onResume(this);
    }

    protected void onPause() {
        super.onPause();
        //MobclickAgent.onPause(this);
    }

    private void gotoLoginActivity() {
        if (getIntent().getBooleanExtra("toActivitynopsw", false)) {
            Intent _intent = new Intent();
            Bundle _bundle = new Bundle();
            _bundle.putString("username", this.mregist_inputphonenum_edit.getText().toString().trim());
            _bundle.putString("mailname", this.mail);
            _bundle.putBoolean("fromaddnewUser", true);
            _intent.putExtras(_bundle);
            setResult(65, _intent);
        } else {
            Intent toLogin = new Intent(this, LoginActivity.class);
            toLogin.putExtra("frommailregist", this.mail);
            startActivity(toLogin);
        }
        finish();
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_resetpwd_layout);
        init_view();
    }

    private void init_view() {
        this.sortSelect = (LinearLayout) findViewById(R.id.sort_secret);
        this.phoneSecret = (LinearLayout) findViewById(R.id.phone_secret_layout);
        this.mailSecret = (LinearLayout) findViewById(R.id.mail_secret_layout);
        this.phoneSort = (Button) findViewById(R.id.phone_is);
        this.mailSort = (Button) findViewById(R.id.mail_is);
        this.phoneSort.setOnClickListener(this);
        this.mailSort.setOnClickListener(this);
        this.mailIdReset = (Button) findViewById(R.id.mail_reset_btn);
        this.newSecret = (EditText) findViewById(R.id.mail_new_secret_et);
        this.repitNewSecret = (EditText) findViewById(R.id.mail_new_secret_repit_et);
        this.mailId = (EditText) findViewById(R.id.mail_id);
        this.mailIdReset.setOnClickListener(this);
        Intent getIntent = getIntent();
        if (getIntent.getBooleanExtra("ismail", false)) {
            this.sortSelect.setVisibility(View.GONE);
            this.mailSecret.setVisibility(View.VISIBLE);
        }
        this.mailId.setText(getIntent.getStringExtra("mail"));
        this.mRegistSendMsgVerifyCode = (Button) findViewById(R.id.registSendMsgVerificationCodeCodeBtn);
        this.mRegistSendMsgVerifyCode.setOnClickListener(this);
        this.mregist_phonenumber_isusely = (LinearLayout) findViewById(R.id.regist_phonenumber_isusely);
        this.mregist_phonenumber_isuseImageB = (ImageButton) findViewById(R.id.regist_phonenumber_isuseImageB);
        this.mregist_phonenumber_isuseTextV = (TextView) findViewById(R.id.regist_phonenumber_isuseTextV);
        this.misCheckPhoneNumRegistProBar = (ProgressBar) findViewById(R.id.isCheckPhoneNumRegistProBar);
        this.mregistcountdownbtn = (Button) findViewById(R.id.registcountdownbtn);
        this.mregistcountdownbtn.setText(String.valueOf(120) + getResources().getString(R.string.str_second_unit));
        ((Button) findViewById(R.id.zhuce_btn)).setOnClickListener(this);
        ((Button) findViewById(R.id.back_but)).setOnClickListener(this);
        this.mregist_inputphonenum_edit = (EditText) findViewById(R.id.regist_inputphonenum_edit);
        this.mregistidentitycardedit = (EditText) findViewById(R.id.registidentitycardedit);
        this.mregistHeightedit = (EditText) findViewById(R.id.registHeightedit);
        this.mregistInputSmsVerifyedit = (EditText) findViewById(R.id.registInputSmsVerifyedit);
        final ImageButton mregistphonenumdelbtn = (ImageButton) findViewById(R.id.registphonenumdelbtn);
        mregistphonenumdelbtn.setOnClickListener(this);
        final ImageButton mregistidentitycardnumdelbtn = (ImageButton) findViewById(R.id.registidentitycardnumdelbtn);
        mregistidentitycardnumdelbtn.setOnClickListener(this);
        this.mregist_inputphonenum_edit.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void afterTextChanged(Editable s) {
                Activitynopsw.this.mIsReCheckPhoneNumber = false;
                Activitynopsw.this.misCheckPhoneNumRegistProBar.setVisibility(View.GONE);
                Activitynopsw.this.mregist_phonenumber_isusely.setVisibility(View.INVISIBLE);
                if (s.length() != 0) {
                    Activitynopsw.this.mregist_inputphonenum_edit.setTextSize(2, 20.0f);
                    Activitynopsw.this.mregist_inputphonenum_edit.setTextColor(-16777216);
                } else {
                    Activitynopsw.this.mregist_inputphonenum_edit.setTextSize(2, 15.0f);
                    Activitynopsw.this.mregist_inputphonenum_edit.setTextColor(Activitynopsw.this.getResources().getColor(R.color.gray));
                }
                Activitynopsw.this.mRegistSendMsgVerifyCode.setText(R.string.send_identify);
                if (s.length() == 11) {
                    String _phonenumber = s.toString().trim();
                    Meth_android_VerifyUserIsExit.veryUserIsExit("1", _phonenumber, new AjaxCallBack_VerifyuserIsExit(Activitynopsw.this, Activitynopsw.this.mhandler), App_phms.getInstance().mFinalHttp, String.valueOf(Constants.URL) + "/main.php");
                    CLog.i(Activitynopsw.this.TAG, String.valueOf(_phonenumber) + "   register url = " + Constants.URL + "/main.php");
                    Activitynopsw.this.misCheckPhoneNumRegistProBar.setVisibility(View.VISIBLE);
                    Activitynopsw.this.mIsReCheckPhoneNumber = true;
                    Activitynopsw.this.mhandler.sendEmptyMessageDelayed(9999, 5000);
                }
                if (s.length() > 0) {
                    mregistphonenumdelbtn.setVisibility(View.VISIBLE);
                } else {
                    mregistphonenumdelbtn.setVisibility(View.GONE);
                }
                Activitynopsw.this.ISCANCELCOUNTDOWNASYN = true;
            }
        });
        this.mregistInputSmsVerifyedit.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            public void afterTextChanged(Editable s) {
                if (s.length() != 0) {
                    Activitynopsw.this.mregistInputSmsVerifyedit.setTextSize(2, 20.0f);
                    Activitynopsw.this.mregistInputSmsVerifyedit.setTextColor(-16777216);
                    return;
                }
                Activitynopsw.this.mregistInputSmsVerifyedit.setTextSize(2, 15.0f);
                Activitynopsw.this.mregistInputSmsVerifyedit.setTextColor(8026746);
            }
        });
        this.mregistHeightedit.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            public void afterTextChanged(Editable s) {
                if (s.length() != 0) {
                    Activitynopsw.this.mregistHeightedit.setTextSize(2, 20.0f);
                    Activitynopsw.this.mregistHeightedit.setTextColor(-16777216);
                    return;
                }
                Activitynopsw.this.mregistHeightedit.setTextSize(2, 15.0f);
                Activitynopsw.this.mregistHeightedit.setTextColor(Activitynopsw.this.getResources().getColor(R.color.gray));
            }
        });
        this.mregistidentitycardedit.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void afterTextChanged(Editable s) {
                if (s.length() != 0) {
                    Activitynopsw.this.mregistidentitycardedit.setTextSize(2, 20.0f);
                    Activitynopsw.this.mregistidentitycardedit.setTextColor(-16777216);
                    mregistidentitycardnumdelbtn.setVisibility(View.VISIBLE);
                } else if (s.toString().equals(bs.b)) {
                    Activitynopsw.this.mregistidentitycardedit.setTextSize(2, 15.0f);
                    Activitynopsw.this.mregistidentitycardedit.setTextColor(Activitynopsw.this.getResources().getColor(R.color.gray));
                    mregistidentitycardnumdelbtn.setVisibility(View.GONE);
                }
            }
        });
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_but:
                finish();
                return;
            case R.id.zhuce_btn:
                String _phonenumber = this.mregist_inputphonenum_edit.getText().toString().trim();
                String trim = this.mregistidentitycardedit.getText().toString().trim();
                String trim2 = this.mregist_inputphonenum_edit.getText().toString().trim();
                String trim3 = this.mregistHeightedit.getText().toString().trim();
                String _smsverifystr = this.mregistInputSmsVerifyedit.getText().toString().trim();
                if (_phonenumber.length() != 11) {
                    new DialogClass(this, getResources().getString(R.string.str_phonenumregistlengthiserror)).show();
                    return;
                } else if (_smsverifystr.equals(bs.b)) {
                    new DialogClass(this, getResources().getString(R.string.registnoidenti)).show();
                    return;
                } else {
                    com.contec.phms.util.CLog.i(this.TAG, "开始重置密码了..");
                    this.mcommitdialogClass = new DialogClass((Context) this, getResources().getString(R.string.resetpwding), false, 0, this.registerNewUserListener);
                    Meth_android_ReSetPwd.reSetPSW(_phonenumber, _smsverifystr, (String) null, new AjaxCallBack_ReSetPwd(this, this.mhandler), App_phms.getInstance().mFinalHttp, String.valueOf(Constants.URL) + "/main.php");
                    return;
                }
            case R.id.registphonenumdelbtn:
                this.mregist_inputphonenum_edit.setText(bs.b);
                this.mregist_inputphonenum_edit.requestFocus();
                this.mregist_inputphonenum_edit.setCursorVisible(true);
                this.mregist_inputphonenum_edit.setSelection(0);
                return;
            case R.id.registSendMsgVerificationCodeCodeBtn:
                if (this.mregist_inputphonenum_edit.getText().toString().length() != 11) {
                    new DialogClass(this, getResources().getString(R.string.str_phonenumregistlengthiserror)).show();
                    Message myMessage1 = new Message();
                    myMessage1.what = 35;
                    this.mhandler.sendMessageDelayed(myMessage1, 3000);
                    return;
                }
                Method_android_login.sendMsgVerificationCode("1", this.mregist_inputphonenum_edit.getText().toString().trim(), new AjaxCallBack_registSendVerifyCode(this, this.mhandler), App_phms.getInstance().mFinalHttp, String.valueOf(Constants.URL) + "/main.php");
                new mCountDownAsynCTask().execute(new Integer[]{120});
                Toast.makeText(this, getString(R.string.sentrequest), Toast.LENGTH_SHORT).show();
                return;
            case R.id.registidentitycardnumdelbtn:
                this.mregistidentitycardedit.setText(bs.b);
                return;
            case R.id.phone_is:
                this.sortSelect.setVisibility(View.GONE);
                this.phoneSecret.setVisibility(View.VISIBLE);
                return;
            case R.id.mail_is:
                this.sortSelect.setVisibility(View.GONE);
                this.mailSecret.setVisibility(View.VISIBLE);
                return;
            case R.id.mail_reset_btn:
                this.mail = this.mailId.getText().toString();
                if (!RegisterMailActivity.checkEmail(this.mail)) {
                    Toast.makeText(this, getResources().getString(R.string.please_input_right_fomat_mail), Toast.LENGTH_SHORT).show();
                    return;
                } else if (LocalLoginInfoManager.getInstance().querySql(this.mail)) {
                    doResetSecret(LocalLoginInfoManager.getInstance().findByThirdCode(this.mail).mCardNb);
                    return;
                } else {
                    Meth_android_registNewUser.registNewUser("00000000000000000000000000000000", this.mail, bs.b, "0", "6", "ANDROID", bs.b, bs.b, bs.b, bs.b, bs.b, bs.b, bs.b, bs.b, bs.b, bs.b, "contecthirdparty", bs.b, bs.b, new AjaxCallBack_registNewUser(this, this.mhandler), App_phms.getInstance().mFinalHttp, String.valueOf(Constants.URL) + "/main.php");
                    return;
                }
            default:
                return;
        }
    }

    private void doResetSecret(String cardId) {
        String new_recret = this.newSecret.getText().toString();
        this.resetNewSecret = this.repitNewSecret.getText().toString();
        if (new_recret.equals(bs.b) || this.resetNewSecret.equals(bs.b)) {
            Toast.makeText(this, getResources().getString(R.string.mail_not_null), Toast.LENGTH_SHORT).show();
        } else if (!checkSecret(new_recret) || !checkSecret(this.resetNewSecret) || new_recret.length() < 6 || new_recret.length() > 16) {
            Toast.makeText(this, getResources().getString(R.string.secret_only_num_letter), Toast.LENGTH_SHORT).show();
        } else if (new_recret.equals(this.resetNewSecret)) {
            this.mcommitdialogClass = new DialogClass((Context) this, getResources().getString(R.string.resetpwding), false, 0, this.registerNewUserListener);
            AjaxCallBack_ReSetPwd _ajaxcallback = new AjaxCallBack_ReSetPwd(this, this.mhandler);
            Meth_android_ReSetPwd.reSetPSW(cardId, "contecthirdparty", this.resetNewSecret, _ajaxcallback, App_phms.getInstance().mFinalHttp, String.valueOf(Constants.URL) + "/main.php");
            new DialogClass(this, getResources().getString(R.string.user_modifyUserinfosuccess)).show();
        } else {
            Toast.makeText(this, getResources().getString(R.string.not_same), Toast.LENGTH_SHORT).show();
        }
    }

    class mCountDownAsynCTask extends AsyncTask<Integer, Integer, Boolean> {
        mCountDownAsynCTask() {
        }

        protected Boolean doInBackground(Integer... params) {
            com.contec.phms.util.CLog.i("less", "ISCANCELCOUNTDOWNASYN = " + Activitynopsw.this.ISCANCELCOUNTDOWNASYN);
            boolean _result = false;
            int i = params[0].intValue();
            while (true) {
                if (i < 0) {
                    break;
                } else if (Activitynopsw.this.ISCANCELCOUNTDOWNASYN) {
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
            Activitynopsw.this.mRegistSendMsgVerifyCode.setBackgroundResource(R.drawable.drawable_loginpage_button_register_new);
            Activitynopsw.this.mRegistSendMsgVerifyCode.setClickable(true);
            Activitynopsw.this.mregistcountdownbtn.setText(String.valueOf(120) + Activitynopsw.this.getResources().getString(R.string.str_second_unit));
            Activitynopsw.this.mRegistSendMsgVerifyCode.setText(R.string.re_send_identify);
        }

        protected void onPreExecute() {
            super.onPreExecute();
            Activitynopsw.this.ISCANCELCOUNTDOWNASYN = false;
            Activitynopsw.this.mRegistSendMsgVerifyCode.setBackgroundResource(R.drawable.drawable_register_failed);
            Activitynopsw.this.mRegistSendMsgVerifyCode.setClickable(false);
        }

        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            Activitynopsw.this.mregistcountdownbtn.setText(values[0] + Activitynopsw.this.getResources().getString(R.string.str_second_unit));
        }
    }

    public static boolean checkSecret(String secret) {
        try {
            return Pattern.compile("^[a-zA-Z0-9]+$").matcher(secret).matches();
        } catch (Exception e) {
            return false;
        }
    }

    private void insterregisbackInfoDb(String backInforcontext) {
        LocalLoginInfoDao _localinfor = new LocalLoginInfoDao();
        _localinfor.mCardNb = backInforcontext;
        _localinfor.mPassword = this.resetNewSecret;
        _localinfor.mThirdCode = this.mail;
        LocalLoginInfoManager.getInstance().add(_localinfor);
    }

    private void updateSecretInfoDb(String thirdCode) {
        LocalLoginInfoDao _localinfor = LocalLoginInfoManager.getInstance().findByThirdCode(thirdCode);
        if (_localinfor != null) {
            _localinfor.mPassword = this.resetNewSecret;
            LocalLoginInfoManager.getInstance().add(_localinfor);
        }
    }
}
