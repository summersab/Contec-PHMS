package com.contec.phms.activity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.contec.phms.App_phms;
import com.contec.phms.R;
import com.contec.phms.ajaxcallback.AjaxCallBack_registNewUser;
import com.contec.phms.login.LoginActivity;
import com.contec.phms.db.LocalLoginInfoDao;
import com.contec.phms.db.LocalLoginInfoManager;
import com.contec.phms.util.CLog;
import com.contec.phms.util.Constants;
import com.contec.phms.util.PhmsSharedPreferences;
import com.contec.phms.widget.DialogClass;
import com.contec.phms.widget.MailBeRegisterDialog;
import com.contec.phms.widget.MailRegisterSucsess;
import java.io.ByteArrayInputStream;
import java.util.regex.Pattern;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Element;
import cn.com.contec_net_3_android.Meth_android_registNewUser;
import u.aly.bs;

public class RegisterMailActivity extends Activity implements View.OnClickListener {
    private String _defaultpassword;
    private Button back;
    private String mCardNb = bs.b;
    Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 106900:
                    RegisterMailActivity.this.registerDialog.dismiss();
                    CLog.e("RegisterMailActivity", msg.obj.toString());
                    String backInforcontext = msg.obj.toString();
                    if (backInforcontext != null && !backInforcontext.equals(bs.b)) {
                        DocumentBuilderFactory _factory = DocumentBuilderFactory.newInstance();
                        try {
                            Element _infoElement = _factory.newDocumentBuilder().parse(new ByteArrayInputStream(backInforcontext.getBytes())).getDocumentElement();
                            String backType = _infoElement.getElementsByTagName("type").item(0).getTextContent();
                            RegisterMailActivity.this.mCardNb = _infoElement.getElementsByTagName("uid").item(0).getTextContent();
                            RegisterMailActivity.this.insterregisbackInfoDb(RegisterMailActivity.this.mCardNb);
                            if (!backType.equals(bs.b)) {
                                if (backType.equals("0")) {
                                    final MailRegisterSucsess sucDialog = new MailRegisterSucsess(RegisterMailActivity.this);
                                    sucDialog.setMailAddress(RegisterMailActivity.this.thirdCode);
                                    sucDialog.getLoginBt().setOnClickListener(new View.OnClickListener() {
                                        public void onClick(View v) {
                                            RegisterMailActivity.this.GotoSexActivity();
                                        }
                                    });
                                    sucDialog.getModifyBt().setOnClickListener(new View.OnClickListener() {
                                        public void onClick(View v) {
                                            sucDialog.dismiss();
                                            Intent intent = new Intent(RegisterMailActivity.this, Activitynopsw.class);
                                            intent.putExtra("ismail", true);
                                            intent.putExtra("mail", RegisterMailActivity.this.thirdCode);
                                            RegisterMailActivity.this.startActivity(intent);
                                            RegisterMailActivity.this.finish();
                                        }
                                    });
                                } else if (backType.equals("1")) {
                                    final MailBeRegisterDialog dialog = new MailBeRegisterDialog(RegisterMailActivity.this);
                                    dialog.setMailAddress(RegisterMailActivity.this.thirdCode);
                                    dialog.getmLogin().setOnClickListener(new View.OnClickListener() {
                                        public void onClick(View v) {
                                            dialog.dismiss();
                                            Intent toLoginIntent = new Intent(RegisterMailActivity.this, LoginActivity.class);
                                            toLoginIntent.putExtra("frommailregist", RegisterMailActivity.this.thirdCode);
                                            RegisterMailActivity.this.startActivity(toLoginIntent);
                                            RegisterMailActivity.this.finish();
                                        }
                                    });
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    RegisterMailActivity.this.insterregisbackInfoDb(msg.obj.toString());
                    return;
                default:
                    return;
            }
        }
    };
    private EditText mail;
    private boolean miscancellogin = false;
    private final int mregistnewUsersuccess = 106900;
    DialogClass registerDialog;
    private PhmsSharedPreferences sp;
    private String thirdCode;
    private Button to_register;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_mail);
        this.sp = PhmsSharedPreferences.getInstance(this);
        initView();
    }

    private void initView() {
        this.mail = (EditText) findViewById(R.id.input_mail);
        this.back = (Button) findViewById(R.id.back_btn);
        this.to_register = (Button) findViewById(R.id.to_register);
        this.back.setOnClickListener(this);
        this.to_register.setOnClickListener(this);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_btn:
                finish();
                return;
            case R.id.to_register:
                longinWithMail();
                return;
            default:
                return;
        }
    }

    private void insterregisbackInfoDb(String thirdNum) {
        if (thirdNum != null && !thirdNum.equals(bs.b)) {
            LocalLoginInfoDao _localinfor = new LocalLoginInfoDao();
            _localinfor.mCardNb = thirdNum;
            _localinfor.mPassword = this._defaultpassword;
            _localinfor.mThirdCode = this.thirdCode;
            LocalLoginInfoManager.getInstance().add(_localinfor);
        }
    }

    private void longinWithMail() {
        AjaxCallBack_registNewUser ajaxCallBack_registNewUser = new AjaxCallBack_registNewUser(this, this.mHandler);
        this._defaultpassword = "123456";
        this.thirdCode = this.mail.getText().toString();
        if (this.thirdCode.length() > 30) {
            Toast.makeText(this, getResources().getString(R.string.please_input_right_fomat_mail_length), Toast.LENGTH_SHORT).show();
        } else if (checkEmail(this.thirdCode)) {
            this.miscancellogin = false;
            this.registerDialog = new DialogClass((Context) this, getString(R.string.registing), false, 0, new DialogInterface.OnKeyListener() {
                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                    if (keyCode == 4 && event.getRepeatCount() == 0) {
                        RegisterMailActivity.this.registerDialog.dismiss();
                        RegisterMailActivity.this.miscancellogin = true;
                    }
                    return false;
                }
            });
            if (LocalLoginInfoManager.getInstance().querySql(this.thirdCode)) {
                this.registerDialog.dismiss();
                MailBeRegisterDialog mailBeRegisterDialog = new MailBeRegisterDialog(this);
                mailBeRegisterDialog.setMailAddress(this.thirdCode);
                final MailBeRegisterDialog mailBeRegisterDialog2 = mailBeRegisterDialog;
                mailBeRegisterDialog.getmLogin().setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        mailBeRegisterDialog2.dismiss();
                        Intent toLoginIntent = new Intent(RegisterMailActivity.this, LoginActivity.class);
                        toLoginIntent.putExtra("frommailregist", RegisterMailActivity.this.thirdCode);
                        RegisterMailActivity.this.startActivity(toLoginIntent);
                        RegisterMailActivity.this.finish();
                    }
                });
                return;
            }
            Meth_android_registNewUser.registNewUser("00000000000000000000000000000000", this.thirdCode, this._defaultpassword, "1", "6", "ANDROID", bs.b, bs.b, bs.b, bs.b, bs.b, bs.b, bs.b, bs.b, RegisterPhoneActivity.hospitalId, bs.b, "contecthirdparty", bs.b, bs.b, ajaxCallBack_registNewUser, App_phms.getInstance().mFinalHttp, String.valueOf(Constants.URL) + "/main.php");
        } else {
            Toast.makeText(this, getResources().getString(R.string.please_input_right_fomat_mail), Toast.LENGTH_SHORT).show();
        }
    }

    private void GotoSexActivity() {
        this.sp.saveColume("cardUserId", this.mCardNb);
        this.sp.saveColume("phoneNum", bs.b);
        startActivity(new Intent(this, RegisterSexActivity.class));
        finish();
    }

    public static boolean checkEmail(String email) {
        try {
            return Pattern.compile("^([a-z0-9A-Z]+[-|_|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$").matcher(email).matches();
        } catch (Exception e) {
            return false;
        }
    }
}
