package cn.com.contec_net_3_android;

import android.util.Base64;
import android.util.Log;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import cn.com.contec.net.util.Constants_jar;
import cn.com.contec.net.util.MD5_encoding;
import cn.com.contec_net_3_android_case.UploadUtil;

public class Method_android_login_machine {
    public static String loginMachine(String pSessionID, String pUserID, String pPSW, String pSenderid, String pUrl) throws Exception {
        String _MessageHeader = "101095" + pSessionID + "11";
        String _MD5psw_user = MD5_encoding.MD5(String.valueOf(pUserID) + pPSW);
        String _base64Message = Base64.encodeToString(("<?xml version=\"1.0\" encoding=\"UTF-8\" ?><request><senderid>" + pSenderid + "</senderid>" + "</request>").getBytes(), 0);
        String _MessageHeaderSend = String.valueOf(MD5_encoding.MD5(String.valueOf(_MD5psw_user) + _MessageHeader + _base64Message)) + _MessageHeader;
        Map<String, String> params = new HashMap<>();
        params.put(Constants_jar.MSG_STRING, String.valueOf(_MessageHeaderSend) + _base64Message);
        String _respones = UploadUtil.submitPostData(params, "UTF-8", new URL(pUrl));
        Log.d("loginMachine", "return string��" + _respones);
        return _respones;
    }
}
