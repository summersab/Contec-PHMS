package com.contec.phms.login;

import android.content.Context;
import android.os.Handler;
import com.contec.phms.App_phms;
import com.contec.phms.ajaxcallback.AjaxCallBack_login;
import com.contec.phms.util.Constants;
import cn.com.contec_net_3_android.Method_android_login;

public class LoginMethod {
    Context mContext;
    Handler mHandler;
    String mUserId;
    String mUserPwd;

    public LoginMethod(Context pcontext, String mUserId2, String mUserPwd2, Handler phandler) {
        this.mContext = pcontext;
        this.mHandler = phandler;
        this.mUserId = mUserId2;
        this.mUserPwd = mUserPwd2;
    }

    public void login() {
        AjaxCallBack_login _ajAjaxCallBack_login = new AjaxCallBack_login(this.mContext, this.mHandler);
        _ajAjaxCallBack_login.mUserID = this.mUserId;
        _ajAjaxCallBack_login.mPasswd = this.mUserPwd;
        Method_android_login.login(this.mUserId, this.mUserPwd, 0, _ajAjaxCallBack_login, App_phms.getInstance().mFinalHttp, String.valueOf(Constants.URL) + "/login.php");
    }
}
