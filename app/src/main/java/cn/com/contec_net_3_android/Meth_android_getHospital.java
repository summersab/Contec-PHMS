package cn.com.contec_net_3_android;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;
import cn.com.contec.net.util.Base64_encoding;
import cn.com.contec.net.util.Constants_jar;

public class Meth_android_getHospital {
    public static void getHospital(String areaId, String pSessionId, AjaxCallBack<String> pAjaxCallBack, FinalHttp pFinalHttp, String pHost) {
        String[] _hospital_array = {Constants_jar.MSG_STRING, String.valueOf("00000000000000000000000000000000101104" + pSessionId + "11") + Base64_encoding.encoding("<?xml version=\"1.0\" encoding=\"UTF-8\" ?><request><areaid>" + areaId + "</areaid><rank></rank><beta>" + "2" + "</beta></request>")};
        AjaxParams _params = new AjaxParams();
        _params.put(_hospital_array[0], _hospital_array[1]);
        pFinalHttp.post(pHost, _params, pAjaxCallBack);
    }
}
