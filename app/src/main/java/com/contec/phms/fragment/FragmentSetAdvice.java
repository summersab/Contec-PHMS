package com.contec.phms.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.contec.phms.App_phms;
import com.contec.phms.R;
import com.contec.phms.login.ActivityBase;
import com.contec.phms.db.LoginUserDao;
//import com.contec.phms.sendemail.MailSenderInfo;
//import com.contec.phms.sendemail.SendEmail;
//import com.contec.phms.sendemail.SimpleMailSender;
import com.contec.phms.util.CLog;
import com.contec.phms.util.Constants;
import com.contec.phms.util.PageUtil;
import com.contec.phms.util.ScreenAdapter;
import com.contec.phms.widget.DialogClass;
import u.aly.bs;

public class FragmentSetAdvice extends ActivityBase {
    private int SCREENHEIGH;
    private int SCREENWEIGH;
    private EditText mAdviceEdit;
    private SendemailAsyntask mSendemailAsyntask;
    private DialogClass m_dialogClass;
    //private SendEmail msendEmai;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_set_advice);
        App_phms.getInstance().addActivity(this);
        init_view();
    }

    private void init_view() {
        Button mback_but = (Button) findViewById(R.id.back_but);
        mback_but.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                FragmentSetAdvice.this.finish();
            }
        });
        if (Constants.IS_PAD_NEW) {
            ScreenAdapter.titleLayoutToPad(this, (RelativeLayout) findViewById(R.id.linearlayout_title), mback_but);
            ScreenAdapter.changeLayoutTextSize(this, (RelativeLayout) findViewById(R.id.layout_setadvice_main), 10);
        }
        this.SCREENHEIGH = getWindowManager().getDefaultDisplay().getHeight();
        this.SCREENWEIGH = getWindowManager().getDefaultDisplay().getWidth();
        LinearLayout mAdviceLayout = (LinearLayout) findViewById(R.id.advice_layout);
        mAdviceLayout.getLayoutParams().height = (this.SCREENHEIGH * 1) / 2;
        CLog.i("Text", "heig" + mAdviceLayout.getLayoutParams().height + "sCREENHEIGH" + this.SCREENHEIGH);
        this.mAdviceEdit = (EditText) findViewById(R.id.advice_edit);
        this.mAdviceEdit.setMovementMethod(ScrollingMovementMethod.getInstance());
        Button mCommitBut = (Button) findViewById(R.id.commit_but);
        mCommitBut.getLayoutParams().height = (this.SCREENWEIGH * 1) / 8;
        this.mSendemailAsyntask = new SendemailAsyntask();
        mCommitBut.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                if (!PageUtil.checkNet(FragmentSetAdvice.this)) {
                    new DialogClass(FragmentSetAdvice.this, FragmentSetAdvice.this.getResources().getString(R.string.net_disable));
                    return;
                }
                String _sendcontext = FragmentSetAdvice.this.mAdviceEdit.getText().toString();
                if (_sendcontext == null || _sendcontext.equals(bs.b)) {
                    Toast.makeText(FragmentSetAdvice.this, FragmentSetAdvice.this.getString(R.string.sendemailcontextisnotnull), Toast.LENGTH_SHORT).show();
                    return;
                }
                FragmentSetAdvice.this.m_dialogClass = new DialogClass((Context) FragmentSetAdvice.this, FragmentSetAdvice.this.getString(R.string.issendingemail), false, 0, (DialogInterface.OnKeyListener) null);
                FragmentSetAdvice.this.mSendemailAsyntask.execute(new String[]{_sendcontext});
            }
        });
    }

    class SendemailAsyntask extends AsyncTask<String, String, Boolean> {
        SendemailAsyntask() {
        }

        protected Boolean doInBackground(String... params) {
            return Boolean.valueOf(FragmentSetAdvice.this.sendemailinthread(params[0]));
        }

        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            FragmentSetAdvice.this.m_dialogClass.dismiss();
            if (result.booleanValue()) {
                Toast.makeText(FragmentSetAdvice.this, FragmentSetAdvice.this.getString(R.string.sendemailissuccess), Toast.LENGTH_LONG).show();
                FragmentSetAdvice.this.finish();
                return;
            }
            Toast.makeText(FragmentSetAdvice.this, FragmentSetAdvice.this.getString(R.string.sendemailfailed), Toast.LENGTH_SHORT).show();
        }
    }

    private boolean sendemailinthread(String pemialcontext) {
        try {
        //    MailSenderInfo mailInfo = new MailSenderInfo();
        //    mailInfo.setMailServerHost("smtp.qq.com");
        //    mailInfo.setMailServerPort("25");
        //    mailInfo.setValidate(true);
        //    mailInfo.setUserName("2313103733");
        //    mailInfo.setPassword("yy+qq=1314");
        //    mailInfo.setFromAddress("2313103733@qq.com");
        //    mailInfo.setToAddress("243855542@qq.com");
        //    mailInfo.setSubject("PHMS 改善意见");
        //    LoginUserDao _loginUserInfo = PageUtil.getLoginUserInfo();
        //    String _Phone = _loginUserInfo.mPhone;
        //    mailInfo.setContent(String.valueOf(pemialcontext) + "---反馈人联系电话：" + _Phone + "---姓名：" + _loginUserInfo.mUserName);
        //    new SimpleMailSender().sendTextMail(mailInfo);
            return true;
        } catch (Exception e) {
            Log.e("SendMail", e.getMessage(), e);
            return false;
        }
    }
}
