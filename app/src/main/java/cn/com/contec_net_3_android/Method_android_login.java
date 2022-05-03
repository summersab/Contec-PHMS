package cn.com.contec_net_3_android;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;
import cn.com.contec.net.login.Method_Login;

public class Method_android_login {
    public static void login(String pstrUsername, String pstrPsw, int pType, AjaxCallBack<String> pAjaxCallBack, FinalHttp pFinalHttp, String pHost) {
        String[] _login_array = Method_Login.login(pstrUsername, pstrPsw, pType);
        AjaxParams _params = new AjaxParams();
        _params.put(_login_array[0], String.valueOf(_login_array[1]) + _login_array[2]);
        pFinalHttp.post(pHost, _params, pAjaxCallBack);
    }

    public static void logOut(String pLogID, String pPass, String pSessionID, AjaxCallBack<String> pAjaxCallBack, FinalHttp pFinalHttp, String pHost) {
        String[] _login_out_array = Method_Login.login_out(pLogID, pPass, pSessionID);
        String MSG_STRING = _login_out_array[0];
        String _message = _login_out_array[1];
        AjaxParams _params = new AjaxParams();
        _params.put(MSG_STRING, _message);
        pFinalHttp.post(pHost, _params, pAjaxCallBack);
    }

    public static void sendMsgVerificationCode(String ptype, String pphonenumber, AjaxCallBack<String> pAjaxCallBack, FinalHttp pFinalHttp, String pHost) {
        String[] _quest_sendmsg_array = Method_Login.sendMsgVerificationCode(ptype, pphonenumber);
        AjaxParams _params = new AjaxParams();
        _params.put(_quest_sendmsg_array[0], String.valueOf(_quest_sendmsg_array[1]) + _quest_sendmsg_array[2]);
        pFinalHttp.post(pHost, _params, pAjaxCallBack);
    }
}
