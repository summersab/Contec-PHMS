package cn.com.contec_net_3_android;

import android.util.Base64;
import android.util.Log;
import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;
import cn.com.contec.net.util.Constants_jar;

public class Meth_android_ReSetPwd {
    public static void reSetPSW(String pPhone, String pSocde, String newPW, AjaxCallBack<String> pAjaxCallBack, FinalHttp pFinalHttp, String pURL) {
        String _messageBody;
        if (newPW != null) {
            _messageBody = "<?xml version=\"1.0\" encoding=\"GBK\" ?><request><phone>" + pPhone + "</phone><scode>" + pSocde + "</scode><newpwd>" + newPW + "</newpwd></request>";
        } else {
            _messageBody = "<?xml version=\"1.0\" encoding=\"GBK\" ?><request><phone>" + pPhone + "</phone><scode>" + pSocde + "</scode></request>";
        }
        Log.i("lz", _messageBody);
        Log.i("phmsnet", "reSetPSW pURL =  " + pURL);
        String[] content = {Constants_jar.MSG_STRING, String.valueOf("000000000000000000000000000000001010890000000000000000000000000000000011") + Base64.encodeToString(_messageBody.getBytes(), 0)};
        AjaxParams _params = new AjaxParams();
        _params.put(content[0], content[1]);
        pFinalHttp.post(pURL, _params, pAjaxCallBack);
    }
}
