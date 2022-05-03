package cn.com.contec_net_3_android;

import android.util.Base64;
import android.util.Log;
import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;
import cn.com.contec.net.util.Constants_jar;

public class Meth_android_registNewUser {
    public static void registNewUser(String psessionid, String poAuth, String pPassword, String pIsregister, String ptype, String pregfrom, String pPersionID, String pName, String pSex, String pBirthday, String pAddress, String pphone, String pheight, String pupload_unitid, String phospitalid, String phgroupid, String pscode, String pwxuid, String pweight, AjaxCallBack<String> pAjaxCallBack, FinalHttp pFinalHttp, String pURL) {
        String _messageBody;
        String _MessageHeader = "00000000000000000000000000000000101069" + psessionid + "11";
        if (ptype == "6" || ptype.equalsIgnoreCase("6")) {
            _messageBody = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?><request><type>" + ptype + "</type><regfrom>" + pregfrom + "</regfrom><personid>" + pPersionID + "</personid><name>" + pName + "</name><sex>" + pSex + "</sex><birthday>" + pBirthday + "</birthday><address>" + pAddress + "</address><phone>" + pphone + "</phone><height>" + pheight + "</height><upload_unitid>" + pupload_unitid + "</upload_unitid><hospitalid>" + phospitalid + "</hospitalid><hgroupid>" + phgroupid + "</hgroupid><scode>" + pscode + "</scode><wxuid>" + pwxuid + "</wxuid><weight>" + pweight + "</weight><thirdcode>" + poAuth + "</thirdcode><password>" + pPassword + "</password><isregister>" + pIsregister + "</isregister></request>";
        } else {
            _messageBody = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?><request><type>" + ptype + "</type><regfrom>" + pregfrom + "</regfrom><personid>" + pPersionID + "</personid><name>" + pName + "</name><sex>" + pSex + "</sex><birthday>" + pBirthday + "</birthday><address>" + pAddress + "</address><phone>" + pphone + "</phone><height>" + pheight + "</height><upload_unitid>" + pupload_unitid + "</upload_unitid><hospitalid>" + phospitalid + "</hospitalid><hgroupid>" + phgroupid + "</hgroupid><scode>" + pscode + "</scode><wxuid>" + pwxuid + "</wxuid><weight>" + pweight + "</weight></request>";
        }
        Log.i("lz", " _messageBody =" + _messageBody);
        String[] content = {Constants_jar.MSG_STRING, String.valueOf(_MessageHeader) + Base64.encodeToString(_messageBody.getBytes(), 0)};
        AjaxParams _params = new AjaxParams();
        _params.put(content[0], content[1]);
        pFinalHttp.post(pURL, _params, pAjaxCallBack);
    }
}
