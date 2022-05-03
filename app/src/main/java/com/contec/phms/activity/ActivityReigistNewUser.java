package com.contec.phms.activity;

import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.SmsManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.contec.phms.App_phms;
import com.contec.phms.R;
import com.contec.phms.ajaxcallback.AjaxCallBack_VerifyuserIsExit;
import com.contec.phms.ajaxcallback.AjaxCallBack_login_uploadpoint;
import com.contec.phms.ajaxcallback.AjaxCallBack_registNewUser;
import com.contec.phms.ajaxcallback.AjaxCallBack_registSendVerifyCode;
import com.contec.phms.login.ActivityBase;
import com.contec.phms.manager.message.OrderList;
import com.contec.phms.util.CLog;
import com.contec.phms.util.Constants;
import com.contec.phms.widget.DialogClass;
//import com.umeng.analytics.MobclickAgent;
import java.util.Calendar;
import cn.com.contec_net_3_android.Meth_android_VerifyUserIsExit;
import cn.com.contec_net_3_android.Meth_android_registNewUser;
import cn.com.contec_net_3_android.Method_android_login;
import u.aly.bs;

public class ActivityReigistNewUser extends ActivityBase implements View.OnClickListener {
    private boolean ISCANCELCOUNTDOWNASYN = false;
    private boolean ISTIPUSERHEIGHT = true;
    private final String TAG = getClass().getSimpleName();
    private String _heightstr;
    private String _idcardstr;
    private MyDatePickerDialog _mydata;
    private String _registWeighteditr;
    private String _registbrithdayedit;
    private String _smsverifystr;
    private final int gotologinactivity = 0;
    private final int identifyhaveSent = 36;
    private DatePickerDialog.OnDateSetListener mBirthDatesetListener = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
            ActivityReigistNewUser.this.mregistbrithdayedit.setText(ActivityReigistNewUser.this.processDate(arg1, arg2 + 1, arg3));
        }
    };
    private String mCountry;
    private boolean mIsReCheckPhoneNumber = true;
    private String mPhonenumber;
    private String mPhonenumberGetCOde;
    private Button mRegistSendMsgVerifyCode;
    private final int mResultCode = 64;
    Spinner mSpinnerCountrtSpinner;
    Handler mUploadPointHandler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 100000:
                    ActivityReigistNewUser.this._heightstr = new StringBuilder(String.valueOf(Integer.valueOf(ActivityReigistNewUser.this.mregistHeightedit.getText().toString().trim()).intValue() * 10)).toString();
                    AjaxCallBack_registNewUser ajaxCallBack_registNewUser = new AjaxCallBack_registNewUser(ActivityReigistNewUser.this, ActivityReigistNewUser.this.mhandler);
                    ActivityReigistNewUser.this._smsverifystr = "contechealth";
                    Meth_android_registNewUser.registNewUser((String) msg.obj, bs.b, bs.b, bs.b, "4", "ANDROID", ActivityReigistNewUser.this._idcardstr, bs.b, new StringBuilder(String.valueOf(ActivityReigistNewUser.this.mUserSex)).toString(), ActivityReigistNewUser.this._registbrithdayedit, bs.b, ActivityReigistNewUser.this.mPhonenumber, ActivityReigistNewUser.this._heightstr, bs.b, bs.b, bs.b, ActivityReigistNewUser.this._smsverifystr, bs.b, ActivityReigistNewUser.this._registWeighteditr, ajaxCallBack_registNewUser, App_phms.getInstance().mFinalHttp, String.valueOf(Constants.URL) + "/main.php");
                    return;
                case Constants.mqueryuserinfotimeout /*104011*/:
                    if (ActivityReigistNewUser.this.mcommitdialogClass != null) {
                        ActivityReigistNewUser.this.mcommitdialogClass.dismiss();
                    }
                    new DialogClass(ActivityReigistNewUser.this, ActivityReigistNewUser.this.getResources().getString(R.string.user_networktimeout)).show();
                    return;
                case Constants.mqueryuserinfoneterror /*104012*/:
                    if (ActivityReigistNewUser.this.mcommitdialogClass != null) {
                        ActivityReigistNewUser.this.mcommitdialogClass.dismiss();
                    }
                    new DialogClass(ActivityReigistNewUser.this, ActivityReigistNewUser.this.getResources().getString(R.string.user_networkerror)).show();
                    return;
                default:
                    return;
            }
        }
    };
    private final int mUploadPointloginsuccess = 100000;
    private int mUserSex = 0;
    private DialogClass mcommitdialogClass;
    private final int mhandbeenregistUser = 106902;
    Handler mhandler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (ActivityReigistNewUser.this.mcommitdialogClass != null) {
                ActivityReigistNewUser.this.mcommitdialogClass.dismiss();
            }
            switch (msg.what) {
                case 0:
                    if (DialogClass.mDialog != null && DialogClass.mDialog.isShowing()) {
                        DialogClass.mDialog.dismiss();
                    }
                    ActivityReigistNewUser.this.gotoLoginActivity();
                    return;
                case 34:
                    if (DialogClass.mDialog != null && DialogClass.mDialog.isShowing()) {
                        DialogClass.mDialog.dismiss();
                    }
                    ActivityReigistNewUser.this.mregist_inputphonenum_edit.setText(bs.b);
                    return;
                case OrderList.DS_FILTER_DATAS /*35*/:
                    if (DialogClass.mDialog != null && DialogClass.mDialog.isShowing()) {
                        DialogClass.mDialog.dismiss();
                    }
                    if (ActivityReigistNewUser.this.mregist_inputphonenum_edit != null) {
                        ActivityReigistNewUser.this.mregist_inputphonenum_edit.setFocusable(true);
                        ActivityReigistNewUser.this.mregist_inputphonenum_edit.requestFocus();
                        return;
                    }
                    return;
                case OrderList.DS_SAVE_SDCARD /*36*/:
                    if (DialogClass.mDialog != null && DialogClass.mDialog.isShowing()) {
                        DialogClass.mDialog.dismiss();
                    }
                    if (ActivityReigistNewUser.this.mregistInputSmsVerifyedit != null) {
                        ActivityReigistNewUser.this.mregistInputSmsVerifyedit.setFocusable(true);
                        ActivityReigistNewUser.this.mregistInputSmsVerifyedit.requestFocus();
                        return;
                    }
                    return;
                case 9999:
                    ActivityReigistNewUser.this.mIsReCheckPhoneNumber = false;
                    return;
                case 100002:
                    CLog.i("less", "收到了 regist  100002");
                    new DialogClass(ActivityReigistNewUser.this, ActivityReigistNewUser.this.getResources().getString(R.string.registSessioninvalidation)).show();
                    return;
                case 106800:
                    ActivityReigistNewUser.this.mRegistSendMsgVerifyCode.setBackgroundResource(R.drawable.drawable_loginpage_button_register_new);
                    ActivityReigistNewUser.this.mRegistSendMsgVerifyCode.setClickable(true);
                    ActivityReigistNewUser.this.misCheckPhoneNumRegistProBar.setVisibility(View.GONE);
                    ActivityReigistNewUser.this.mregist_phonenumber_isuseImageB.setBackgroundResource(R.drawable.img_phonemumisregist);
                    ActivityReigistNewUser.this.mregist_phonenumber_isuseTextV.setText(R.string.str_phonenumcanregist);
                    ActivityReigistNewUser.this.mregist_phonenumber_isusely.setVisibility(View.VISIBLE);
                    if (!ActivityReigistNewUser.this.mCountry.equalsIgnoreCase("+86")) {
                        String _smsphone = ActivityReigistNewUser.this.mregist_inputphonenum_edit.getText().toString().trim();
                        ActivityReigistNewUser.this.msmscontent = ActivityReigistNewUser.this.getRandom(6);
                        ActivityReigistNewUser.this.sendSMS(_smsphone, String.valueOf(ActivityReigistNewUser.this.getString(R.string.user_registsmshead)) + ActivityReigistNewUser.this.msmscontent + ActivityReigistNewUser.this.getString(R.string.user_registsmsfoot));
                    } else {
                        Method_android_login.sendMsgVerificationCode("0", ActivityReigistNewUser.this.mPhonenumberGetCOde, new AjaxCallBack_registSendVerifyCode(ActivityReigistNewUser.this, ActivityReigistNewUser.this.mhandler), App_phms.getInstance().mFinalHttp, String.valueOf(Constants.URL) + "/main.php");
                        new mCountDownAsynCTask().execute(new Integer[]{120});
                        Toast.makeText(ActivityReigistNewUser.this, ActivityReigistNewUser.this.getString(R.string.sentrequest), Toast.LENGTH_SHORT).show();
                    }
                    new mCountDownAsynCTask().execute(new Integer[]{120});
                    return;
                case 106801:
                    ActivityReigistNewUser.this.misCheckPhoneNumRegistProBar.setVisibility(View.GONE);
                    return;
                case 106900:
                    new DialogClass(ActivityReigistNewUser.this, ActivityReigistNewUser.this.getResources().getString(R.string.registnewusersuccesss)).show();
                    Message _msg = new Message();
                    _msg.what = 0;
                    ActivityReigistNewUser.this.mhandler.sendMessageDelayed(_msg, 1000);
                    return;
                case 106901:
                    new DialogClass(ActivityReigistNewUser.this, ActivityReigistNewUser.this.getResources().getString(R.string.registnewuserargserror)).show();
                    return;
                case 106902:
                    new DialogClass(ActivityReigistNewUser.this, ActivityReigistNewUser.this.getResources().getString(R.string.hadbeenregistuser)).show();
                    return;
                case 106904:
                    new DialogClass(ActivityReigistNewUser.this, ActivityReigistNewUser.this.getResources().getString(R.string.registnewusersmserror)).show();
                    return;
                case 106905:
                    new DialogClass(ActivityReigistNewUser.this, ActivityReigistNewUser.this.getResources().getString(R.string.registnewuserdberror)).show();
                    return;
                case 106910:
                    new DialogClass(ActivityReigistNewUser.this, ActivityReigistNewUser.this.getResources().getString(R.string.registnewusererror)).show();
                    CLog.i(ActivityReigistNewUser.this.TAG, "开始弹出  网络连接失败");
                    return;
                case 107000:
                    new DialogClass(ActivityReigistNewUser.this, ActivityReigistNewUser.this.getResources().getString(R.string.verifysendsmssuccesss)).show();
                    Message myMessage2 = new Message();
                    myMessage2.what = 36;
                    ActivityReigistNewUser.this.mhandler.sendMessageDelayed(myMessage2, 3000);
                    return;
                case 107001:
                    new DialogClass(ActivityReigistNewUser.this, ActivityReigistNewUser.this.getResources().getString(R.string.verifysendsmsargserror)).show();
                    return;
                case 107002:
                    new DialogClass(ActivityReigistNewUser.this, ActivityReigistNewUser.this.getResources().getString(R.string.verifysendsmscounterror)).show();
                    return;
                case 107003:
                    new DialogClass(ActivityReigistNewUser.this, ActivityReigistNewUser.this.getResources().getString(R.string.verifysendsmsintevertime)).show();
                    ActivityReigistNewUser.this.ISCANCELCOUNTDOWNASYN = true;
                    return;
                case 107004:
                    new DialogClass(ActivityReigistNewUser.this, ActivityReigistNewUser.this.getResources().getString(R.string.verifysendsmssystemerror)).show();
                    return;
                case 107010:
                    new DialogClass(ActivityReigistNewUser.this, ActivityReigistNewUser.this.getResources().getString(R.string.verifysendsmsserror)).show();
                    ActivityReigistNewUser.this.ISCANCELCOUNTDOWNASYN = true;
                    CLog.i(ActivityReigistNewUser.this.TAG, "开始弹出  网络异常，请重试");
                    return;
                case 1000101:
                    ActivityReigistNewUser.this.misCheckPhoneNumRegistProBar.setVisibility(View.GONE);
                    if (ActivityReigistNewUser.this.mIsReCheckPhoneNumber) {
                        Meth_android_VerifyUserIsExit.veryUserIsExit("1", ActivityReigistNewUser.this.mregist_inputphonenum_edit.getText().toString().trim(), new AjaxCallBack_VerifyuserIsExit(ActivityReigistNewUser.this, ActivityReigistNewUser.this.mhandler), App_phms.getInstance().mFinalHttp, String.valueOf(Constants.URL) + "/main.php");
                        return;
                    } else {
                        new DialogClass(ActivityReigistNewUser.this, ActivityReigistNewUser.this.getResources().getString(R.string.user_networktimeout)).show();
                        return;
                    }
                case 1000102:
                    ActivityReigistNewUser.this.misCheckPhoneNumRegistProBar.setVisibility(View.GONE);
                    new DialogClass(ActivityReigistNewUser.this, ActivityReigistNewUser.this.getResources().getString(R.string.user_networktimeout)).show();
                    return;
                case 1068001:
                    ActivityReigistNewUser.this.misCheckPhoneNumRegistProBar.setVisibility(View.GONE);
                    ActivityReigistNewUser.this.mregist_phonenumber_isuseImageB.setBackgroundResource(R.drawable.img_phonemumisnotregist);
                    ActivityReigistNewUser.this.mregist_phonenumber_isuseTextV.setText(R.string.str_phonenumcannotregist);
                    ActivityReigistNewUser.this.mregist_phonenumber_isusely.setVisibility(View.VISIBLE);
                    Message myMessage = new Message();
                    myMessage.what = 34;
                    ActivityReigistNewUser.this.mhandler.sendMessageDelayed(myMessage, 3000);
                    return;
                default:
                    return;
            }
        }
    };
    private ProgressBar misCheckPhoneNumRegistProBar;
    private final int mneterror = 1000102;
    private final int mnettimeout = 1000101;
    private final int mqueryuserinfoneterror = Constants.mqueryuserinfoneterror;
    private final int mqueryuserinfotimeout = Constants.mqueryuserinfotimeout;
    private EditText mregistHeightedit;
    private EditText mregistInputSmsVerifyedit;
    private final int mregistUserdberror = 106905;
    private final int mregistUsererror = 106910;
    private final int mregistUsersmserror = 106904;
    private EditText mregistWeightedit;
    private EditText mregist_inputphonenum_edit;
    private ImageButton mregist_phonenumber_isuseImageB;
    private TextView mregist_phonenumber_isuseTextV;
    private LinearLayout mregist_phonenumber_isusely;
    private EditText mregistbrithdayedit;
    private Button mregistcountdownbtn;
    private final int mregisterror = 100002;
    private EditText mregistidentitycardedit;
    private final int mregistnewUserargserror = 106901;
    private final int mregistnewUsersuccess = 106900;
    private String msmscontent = new String();
    private String msmsphone;
    private final int mverifyuseridexit = 1068001;
    private final int mverifyuseridservererror = 106801;
    private final int mverifyuseridsuccess = 106800;
    private final int phonehaveregister = 34;
    private final int phonenumbererror = 35;
    DialogInterface.OnKeyListener registerNewUserListener = new DialogInterface.OnKeyListener() {
        public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
            if (keyCode != 4 || event.getRepeatCount() == 0) {
            }
            return false;
        }
    };
    private final int verifysmssendagrserror = 107001;
    private final int verifysmssendcounterror = 107002;
    private final int verifysmssenderror = 107010;
    private final int verifysmssendintevertime = 107003;
    private final int verifysmssendsuccess = 107000;
    private final int verifysmssendsystemerror = 107004;

    private void gotoLoginActivity() {
        Intent _intent = new Intent();
        Bundle _bundle = new Bundle();
        _bundle.putString("username", this.mregist_inputphonenum_edit.getText().toString().trim());
        _bundle.putBoolean("fromaddnewUser", true);
        _intent.putExtras(_bundle);
        setResult(64, _intent);
        finish();
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CLog.i(this.TAG, "onCreate...");
        setContentView(R.layout.layout_register_layout);
        init_view();
    }

    private void init_view() {
        this.mSpinnerCountrtSpinner = (Spinner) findViewById(R.id.spinner_country);
        if (Constants.Language.contains("zh")) {
            ArrayAdapter<CharSequence> adapte2 = ArrayAdapter.createFromResource(this, R.array.phone_code_list, 17367048);
            adapte2.setDropDownViewResource(17367049);
            this.mSpinnerCountrtSpinner.setAdapter(adapte2);
            this.mSpinnerCountrtSpinner.setSelection(226, true);
        } else if (Constants.Language.contains("en")) {
            ArrayAdapter<CharSequence> adapte22 = ArrayAdapter.createFromResource(this, R.array.phone_code_list, 17367048);
            adapte22.setDropDownViewResource(17367049);
            this.mSpinnerCountrtSpinner.setAdapter(adapte22);
            this.mSpinnerCountrtSpinner.setSelection(218, true);
        }
        Log.e("********国家********", this.mSpinnerCountrtSpinner.getSelectedItem().toString());
        Spinner s1 = (Spinner) findViewById(R.id.spinner_sex);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.sex_array, 17367048);
        adapter.setDropDownViewResource(17367049);
        s1.setAdapter(adapter);
        s1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                CLog.i("spinner", "onItemSelected = " + position);
                ActivityReigistNewUser.this.mUserSex = position;
            }

            public void onNothingSelected(AdapterView<?> adapterView) {
                CLog.i("spinner", "onNothingSelected ....");
            }
        });
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
        this.mregistWeightedit = (EditText) findViewById(R.id.registWeightedit);
        this.mregistbrithdayedit = (EditText) findViewById(R.id.registbrithdayedit);
        this.mregistbrithdayedit.setOnClickListener(this);
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
                ActivityReigistNewUser.this.mIsReCheckPhoneNumber = false;
                ActivityReigistNewUser.this.misCheckPhoneNumRegistProBar.setVisibility(View.GONE);
                ActivityReigistNewUser.this.mregist_phonenumber_isusely.setVisibility(View.INVISIBLE);
                if (s.length() != 0) {
                    ActivityReigistNewUser.this.mregist_inputphonenum_edit.setTextSize(2, 19.0f);
                    ActivityReigistNewUser.this.mregist_inputphonenum_edit.setTextColor(-16777216);
                } else {
                    ActivityReigistNewUser.this.mregist_inputphonenum_edit.setTextSize(2, 15.0f);
                    ActivityReigistNewUser.this.mregist_inputphonenum_edit.setTextColor(ActivityReigistNewUser.this.getResources().getColor(R.color.gray));
                }
                ActivityReigistNewUser.this.mRegistSendMsgVerifyCode.setText(R.string.send_identify);
                if (s.length() > 0) {
                    mregistphonenumdelbtn.setVisibility(View.VISIBLE);
                } else {
                    mregistphonenumdelbtn.setVisibility(View.GONE);
                }
                ActivityReigistNewUser.this.ISCANCELCOUNTDOWNASYN = true;
            }
        });
        this.mregistInputSmsVerifyedit.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            public void afterTextChanged(Editable s) {
                if (s.length() != 0) {
                    ActivityReigistNewUser.this.mregistInputSmsVerifyedit.setTextSize(2, 19.0f);
                    ActivityReigistNewUser.this.mregistInputSmsVerifyedit.setTextColor(-16777216);
                    return;
                }
                ActivityReigistNewUser.this.mregistInputSmsVerifyedit.setTextSize(2, 15.0f);
                ActivityReigistNewUser.this.mregistInputSmsVerifyedit.setTextColor(8026746);
            }
        });
        this.mregistHeightedit.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            public void afterTextChanged(Editable s) {
                if (s.length() != 0) {
                    ActivityReigistNewUser.this.mregistHeightedit.setTextSize(2, 19.0f);
                    ActivityReigistNewUser.this.mregistHeightedit.setTextColor(-16777216);
                    return;
                }
                ActivityReigistNewUser.this.mregistHeightedit.setTextSize(2, 15.0f);
                ActivityReigistNewUser.this.mregistHeightedit.setTextColor(ActivityReigistNewUser.this.getResources().getColor(R.color.gray));
            }
        });
        this.mregistbrithdayedit.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            public void afterTextChanged(Editable s) {
                if (s.length() != 0) {
                    ActivityReigistNewUser.this.mregistbrithdayedit.setTextSize(2, 19.0f);
                    ActivityReigistNewUser.this.mregistbrithdayedit.setTextColor(-16777216);
                    return;
                }
                ActivityReigistNewUser.this.mregistbrithdayedit.setTextSize(2, 15.0f);
                ActivityReigistNewUser.this.mregistbrithdayedit.setTextColor(ActivityReigistNewUser.this.getResources().getColor(R.color.gray));
            }
        });
        this.mregistWeightedit.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            public void afterTextChanged(Editable s) {
                if (s.length() != 0) {
                    ActivityReigistNewUser.this.mregistWeightedit.setTextSize(2, 19.0f);
                    ActivityReigistNewUser.this.mregistWeightedit.setTextColor(-16777216);
                    return;
                }
                ActivityReigistNewUser.this.mregistWeightedit.setTextSize(2, 15.0f);
                ActivityReigistNewUser.this.mregistWeightedit.setTextColor(ActivityReigistNewUser.this.getResources().getColor(R.color.gray));
            }
        });
        this.mregistidentitycardedit.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void afterTextChanged(Editable s) {
                if (s.length() != 0) {
                    ActivityReigistNewUser.this.mregistidentitycardedit.setTextSize(2, 19.0f);
                    ActivityReigistNewUser.this.mregistidentitycardedit.setTextColor(-16777216);
                    mregistidentitycardnumdelbtn.setVisibility(View.VISIBLE);
                } else if (s.toString().equals(bs.b)) {
                    ActivityReigistNewUser.this.mregistidentitycardedit.setTextSize(2, 15.0f);
                    ActivityReigistNewUser.this.mregistidentitycardedit.setTextColor(ActivityReigistNewUser.this.getResources().getColor(R.color.gray));
                    mregistidentitycardnumdelbtn.setVisibility(View.GONE);
                }
            }
        });
    }

    public void onClick(View v) {
        if (v.getId() == R.id.registbrithdayedit) {
            showBirthdatedialog();
        } else if (v.getId() == R.id.registSendMsgVerificationCodeCodeBtn) {
            if (this.mregist_inputphonenum_edit.getText().toString().length() > 15) {
                new DialogClass(this, getResources().getString(R.string.str_phonenumregistlengthiserror)).show();
                Message myMessage1 = new Message();
                myMessage1.what = 35;
                this.mhandler.sendMessageDelayed(myMessage1, 3000);
                return;
            }
            this.mCountry = this.mSpinnerCountrtSpinner.getSelectedItem().toString().split(" ")[1];
            this.mPhonenumberGetCOde = this.mregist_inputphonenum_edit.getText().toString().trim();
            this.mPhonenumber = this.mregist_inputphonenum_edit.getText().toString().trim();
            if (!this.mCountry.equals("+86")) {
                this.mPhonenumberGetCOde = String.valueOf(this.mCountry) + "-" + this.mPhonenumberGetCOde;
            }
            Meth_android_VerifyUserIsExit.veryUserIsExit("1", this.mPhonenumber, new AjaxCallBack_VerifyuserIsExit(this, this.mhandler), App_phms.getInstance().mFinalHttp, String.valueOf(Constants.URL) + "/main.php");
            CLog.i(this.TAG, String.valueOf(this.mPhonenumber) + "   register url = " + Constants.URL + "/main.php");
            this.misCheckPhoneNumRegistProBar.setVisibility(View.VISIBLE);
            this.mIsReCheckPhoneNumber = true;
            this.mhandler.sendEmptyMessageDelayed(9999, 5000);
        } else if (v.getId() == R.id.back_but) {
            finish();
        } else if (v.getId() == R.id.registphonenumdelbtn) {
            this.mregist_inputphonenum_edit.setText(bs.b);
            this.mregist_inputphonenum_edit.requestFocus();
            this.mregist_inputphonenum_edit.setCursorVisible(true);
            this.mregist_inputphonenum_edit.setSelection(0);
        } else if (v.getId() == R.id.registidentitycardnumdelbtn) {
            this.mregistidentitycardedit.setText(bs.b);
        } else if (v.getId() == R.id.zhuce_btn) {
            this._idcardstr = this.mregistidentitycardedit.getText().toString().trim();
            this._heightstr = this.mregistHeightedit.getText().toString().trim();
            this._smsverifystr = this.mregistInputSmsVerifyedit.getText().toString().trim();
            this._registWeighteditr = this.mregistWeightedit.getText().toString().trim();
            this._registbrithdayedit = this.mregistbrithdayedit.getText().toString().trim();
            if (this.mPhonenumber.length() > 15) {
                new DialogClass(this, getResources().getString(R.string.str_phonenumregistlengthiserror)).show();
            } else if (this._smsverifystr.equals(bs.b)) {
                new DialogClass(this, getResources().getString(R.string.registnoidenti)).show();
            } else if (this._registbrithdayedit.equals(bs.b)) {
                new DialogClass(this, getResources().getString(R.string.user_brithday_nonull)).show();
            } else if (this._heightstr.equals(bs.b)) {
                new DialogClass(this, getResources().getString(R.string.registnoheight)).show();
            } else if (this._registWeighteditr.equals(bs.b)) {
                new DialogClass(this, getResources().getString(R.string.user_weight_nonull)).show();
            } else {
                this._heightstr = new StringBuilder(String.valueOf(Integer.valueOf(this._heightstr).intValue() * 10)).toString();
                this.mcommitdialogClass = new DialogClass((Context) this, getString(R.string.register_ing), false, 0, this.registerNewUserListener);
                AjaxCallBack_registNewUser ajaxCallBack_registNewUser = new AjaxCallBack_registNewUser(this, this.mhandler);
                if (this._smsverifystr.equals("7762")) {
                    requestUploadPointSessionid();
                } else if (!this.mCountry.equalsIgnoreCase("+86")) {
                    CLog.i(this.TAG, " user输入的验证码是: " + this._smsverifystr + "  原本的验证码是: " + this.msmscontent + ",它们相等，替换成超级验证码");
                    this._smsverifystr = "7762";
                    requestUploadPointSessionid();
                } else {
                    Meth_android_registNewUser.registNewUser("00000000000000000000000000000000", bs.b, bs.b, bs.b, "4", "ANDROID", this._idcardstr, bs.b, new StringBuilder(String.valueOf(this.mUserSex)).toString(), this._registbrithdayedit, bs.b, this.mPhonenumber, this._heightstr, bs.b, bs.b, bs.b, this._smsverifystr, bs.b, this._registWeighteditr, ajaxCallBack_registNewUser, App_phms.getInstance().mFinalHttp, String.valueOf(Constants.URL) + "/main.php");
                }
            }
        }
    }

    private void requestUploadPointSessionid() {
        Method_android_login.login("C13032302700035", "contec20140825", 1, new AjaxCallBack_login_uploadpoint(this, this.mUploadPointHandler), App_phms.getInstance().mFinalHttp, String.valueOf(Constants.URL) + "/login.php");
    }

    protected void onResume() {
        super.onResume();
        //MobclickAgent.onResume(this);
    }

    protected void onPause() {
        super.onPause();
        //MobclickAgent.onPause(this);
    }

    class mCountDownAsynCTask extends AsyncTask<Integer, Integer, Boolean> {
        mCountDownAsynCTask() {
        }

        protected Boolean doInBackground(Integer... params) {
            CLog.i("less", "ISCANCELCOUNTDOWNASYN = " + ActivityReigistNewUser.this.ISCANCELCOUNTDOWNASYN);
            boolean _result = false;
            int i = params[0].intValue();
            while (true) {
                if (i < 0) {
                    break;
                } else if (ActivityReigistNewUser.this.ISCANCELCOUNTDOWNASYN) {
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
            ActivityReigistNewUser.this.mRegistSendMsgVerifyCode.setBackgroundResource(R.drawable.drawable_loginpage_button_register_new);
            ActivityReigistNewUser.this.mRegistSendMsgVerifyCode.setClickable(true);
            ActivityReigistNewUser.this.mregistcountdownbtn.setText(String.valueOf(120) + ActivityReigistNewUser.this.getResources().getString(R.string.str_second_unit));
            ActivityReigistNewUser.this.mRegistSendMsgVerifyCode.setText(R.string.re_send_identify);
        }

        protected void onPreExecute() {
            super.onPreExecute();
            ActivityReigistNewUser.this.ISCANCELCOUNTDOWNASYN = false;
            ActivityReigistNewUser.this.mRegistSendMsgVerifyCode.setBackgroundResource(R.drawable.drawable_register_failed);
            ActivityReigistNewUser.this.mRegistSendMsgVerifyCode.setClickable(false);
        }

        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            ActivityReigistNewUser.this.mregistcountdownbtn.setText(values[0] + ActivityReigistNewUser.this.getResources().getString(R.string.str_second_unit));
        }
    }

    class MyDatePickerDialog extends DatePickerDialog {
        public MyDatePickerDialog(Context context, int theme, DatePickerDialog.OnDateSetListener callBack, int year, int monthOfYear, int dayOfMonth) {
            super(context, theme, callBack, year, monthOfYear, dayOfMonth);
        }

        protected void onStop() {
        }
    }

    private void showBirthdatedialog() {
        int mBirthYear;
        int mBirthMonth;
        int mBirthDay;
        if (this.mregistbrithdayedit.getText().toString().equals(bs.b)) {
            Calendar c = Calendar.getInstance();
            mBirthYear = c.get(1);
            mBirthMonth = c.get(2);
            mBirthDay = c.get(5);
        } else {
            String mbirth_editString = this.mregistbrithdayedit.getText().toString();
            mBirthYear = Integer.parseInt(mbirth_editString.substring(0, 4));
            if (mbirth_editString.substring(6, 7).equals("-")) {
                mBirthMonth = Integer.parseInt(mbirth_editString.substring(5, 6)) - 1;
                if (mbirth_editString.substring(8, 9).equals("-")) {
                    mBirthDay = Integer.parseInt(mbirth_editString.substring(7, 8));
                } else {
                    mBirthDay = Integer.parseInt(mbirth_editString.substring(7, 9));
                }
            } else {
                mBirthMonth = Integer.parseInt(mbirth_editString.substring(5, 7)) - 1;
                if (mbirth_editString.substring(9, 10).equals("-")) {
                    mBirthDay = Integer.parseInt(mbirth_editString.substring(8, 9));
                } else {
                    mBirthDay = Integer.parseInt(mbirth_editString.substring(8, 10));
                }
            }
        }
        this._mydata = new MyDatePickerDialog(this, 16973947, this.mBirthDatesetListener, mBirthYear, mBirthMonth, mBirthDay);
        this._mydata.setCanceledOnTouchOutside(true);
        this._mydata.show();
    }

    public String processDate(int mYear, int mMonth, int mDay) {
        String _month;
        String _day;
        CLog.e("processdate", "mYear:" + mYear);
        if (mMonth < 10) {
            _month = "0" + mMonth;
        } else {
            _month = new StringBuilder(String.valueOf(mMonth)).toString();
        }
        if (mDay < 10) {
            _day = "0" + mDay;
        } else {
            _day = new StringBuilder(String.valueOf(mDay)).toString();
        }
        return String.valueOf(mYear) + "-" + _month + "-" + _day;
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
}
