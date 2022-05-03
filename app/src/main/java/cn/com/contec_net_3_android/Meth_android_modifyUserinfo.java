package cn.com.contec_net_3_android;

import android.util.Base64;
import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;
import cn.com.contec.net.util.Constants_jar;
import cn.com.contec.net.util.MD5_encoding;

public class Meth_android_modifyUserinfo {
    public static void modifyuserinfo(String pLogID, String pPass, String pSessionID, String pIDCard, String pName, String pBirthday, String pSex, String pHeight, String pWeight, String pLmp, AjaxCallBack<String> pAjaxCallBack, FinalHttp pFinalHttp, String pURL, String pHospital) {
        String _MessageHeader = "101044" + pSessionID + "11";
        String _MD5psw_user = MD5_encoding.MD5(String.valueOf(pLogID) + pPass);
        String _base64Message = Base64.encodeToString(("<?xml version=\"1.0\" encoding=\"UTF-8\" ?><request><pid>" + pIDCard + "</pid><name>" + pName + "</name><sex>" + pSex + "</sex><birthday>" + pBirthday + "</birthday><address>" + "</address><lmp>" + pLmp + "</lmp><token></token><height>" + pHeight + "</height><weight>" + pWeight + "</weight><hospitalid>" + pHospital + "</hospitalid></request>").getBytes(), 0);
        String[] content = {Constants_jar.MSG_STRING, String.valueOf(String.valueOf(MD5_encoding.MD5(String.valueOf(_MD5psw_user) + _MessageHeader + _base64Message)) + _MessageHeader) + _base64Message};
        AjaxParams _params = new AjaxParams();
        _params.put(content[0], content[1]);
        pFinalHttp.post(pURL, _params, pAjaxCallBack);
    }
}
