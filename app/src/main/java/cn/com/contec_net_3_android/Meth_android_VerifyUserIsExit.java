package cn.com.contec_net_3_android;

import android.util.Base64;
import android.util.Log;
import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;
import cn.com.contec.net.util.Constants_jar;

public class Meth_android_VerifyUserIsExit {
    public static void veryUserIsExit(String ptype, String puid, AjaxCallBack<String> pAjaxCallBack, FinalHttp pFinalHttp, String pURL) {
        String _messageBody = "<?xml version=\"1.0\" encoding=\"GBK\" ?><request><uid>" + puid + "</uid><type>" + ptype + "</type></request>";
        Log.i("lz", _messageBody);
        String[] content = {Constants_jar.MSG_STRING, String.valueOf("000000000000000000000000000000001010680000000000000000000000000000000011") + Base64.encodeToString(_messageBody.getBytes(), 0)};
        AjaxParams _params = new AjaxParams();
        _params.put(content[0], content[1]);
        pFinalHttp.post(pURL, _params, pAjaxCallBack);
    }
}
