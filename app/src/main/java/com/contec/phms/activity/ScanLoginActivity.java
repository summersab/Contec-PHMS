package com.contec.phms.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.contec.phms.App_phms;
import com.contec.phms.R;
import com.contec.phms.db.LoginUserDao;
import com.contec.phms.util.Constants;
import com.contec.phms.util.PageUtil;
import cn.com.contec_net_3_android.Method_android_login_confirm_code;
import cn.com.contec_net_3_android.Method_android_login_machine;
import u.aly.bs;

public class ScanLoginActivity extends Activity implements View.OnClickListener {
    private final String loginconfirmberror = "109602";
    private final String loginconfirmcodeb = "109604";
    private final String loginconfirmcodeberror = "109603";
    private final String loginconfirmpameserror = "109601";
    private final String loginmachinedberror = "109502";
    private final String loginmachinedcodeberror = "109503";
    private final String loginmachinepameserror = "109501";
    private TextView mScanCancel;
    private Button mScanLogin;
    private TextView mTvlose;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scan_login_app);
        initview();
    }

    private void initview() {
        this.mScanLogin = (Button) findViewById(R.id.scan_login);
        this.mScanCancel = (TextView) findViewById(R.id.scan_cancel);
        this.mTvlose = (TextView) findViewById(R.id.tv_close);
        this.mScanCancel.setOnClickListener(this);
        this.mScanLogin.setOnClickListener(this);
        this.mTvlose.setOnClickListener(this);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_close:
                finish();
                overridePendingTransition(R.anim.activity_scan_login_close, R.anim.activity_stay);
                return;
            case R.id.scan_login:
                login();
                return;
            case R.id.scan_cancel:
                finish();
                overridePendingTransition(R.anim.activity_scan_login_close, R.anim.activity_stay);
                return;
            default:
                return;
        }
    }

    private void login() {
        LoginUserDao _userinfo = PageUtil.getLoginUserInfo();
        String resultStr = null;
        String scanResult = getIntent().getStringExtra("scanResult");
        try {
            resultStr = Method_android_login_machine.loginMachine(_userinfo.mSID, _userinfo.mUID, App_phms.getInstance().mUserInfo.mPassword, scanResult, String.valueOf(Constants.URL) + "/main.php");
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (resultStr == null || resultStr.equalsIgnoreCase(bs.b)) {
            Toast.makeText(this, "获取二维码返回结果失败,检查网络重新扫一扫", Toast.LENGTH_LONG).show();
            return;
        }
        String _result = resultStr.substring(34, 40);
        if (_result.equals(Constants.SUCCESS) || _result == Constants.SUCCESS) {
            try {
                resultStr = Method_android_login_confirm_code.loginMachine(_userinfo.mSID, _userinfo.mUID, App_phms.getInstance().mUserInfo.mPassword, scanResult, String.valueOf(Constants.URL) + "/main.php");
            } catch (Exception e2) {
                e2.printStackTrace();
            }
            String _confrimresult = resultStr.substring(34, 40);
            if (_confrimresult.equals(Constants.SUCCESS) || _confrimresult == Constants.SUCCESS) {
                Toast.makeText(this, "登录成功", Toast.LENGTH_LONG).show();
                finish();
            } else if (_confrimresult.equals("109601") || _confrimresult == "109601") {
                Toast.makeText(this, "参数校验错误", Toast.LENGTH_LONG).show();
            } else if (_confrimresult.equals("109602") || _confrimresult == "109602") {
                Toast.makeText(this, "二维码已失效", Toast.LENGTH_LONG).show();
            } else if (_confrimresult.equals("109603") || _confrimresult == "109603") {
                Toast.makeText(this, "无法识别的二维码", Toast.LENGTH_LONG).show();
            } else if (_confrimresult.equals("109604") || _confrimresult == "109604") {
                Toast.makeText(this, "二维码状态冲突", Toast.LENGTH_LONG).show();
            }
        } else if (_result.equals("109501") || _result == "109501") {
            Toast.makeText(this, "参数校验错误", Toast.LENGTH_LONG).show();
        } else if (_result.equals("109502") || _result == "109502") {
            Toast.makeText(this, "二维码已失效", Toast.LENGTH_LONG).show();
        } else if (_result.equals("109503") || _result == "109503") {
            Toast.makeText(this, "无法识别的二维码", Toast.LENGTH_LONG).show();
        }
    }
}
