package cn.com.contec_net_3_android;

import android.util.Base64;
import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;
import cn.com.contec.net.util.Constants_jar;
import cn.com.contec.net.util.MD5_encoding;

public class UploadBluetoothState {
    public static void uploadBtState(String userID, String psw, String pSessionID, String title, String notes, String phone, AjaxCallBack<String> ajaxCallBack, FinalHttp pFinalHttp, String pURL) {
        String _MessageHeader = "101051" + pSessionID + "11";
        String _MD5psw_user = MD5_encoding.MD5(String.valueOf(userID) + psw);
        String _base64Message = Base64.encodeToString(("<?xml version=\"1.0\" encoding=\"UTF-8\" ?><request><title>" + title + "</title><notes>" + notes + "</notes>" + "<phone>" + phone + "</phone></request>").getBytes(), 0);
        String[] content = {Constants_jar.MSG_STRING, String.valueOf(String.valueOf(MD5_encoding.MD5(String.valueOf(_MD5psw_user) + _MessageHeader + _base64Message)) + _MessageHeader) + _base64Message};
        AjaxParams _params = new AjaxParams();
        _params.put(content[0], content[1]);
        pFinalHttp.post(pURL, _params, ajaxCallBack);
    }
}
