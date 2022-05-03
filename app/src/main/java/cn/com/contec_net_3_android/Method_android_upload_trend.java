package cn.com.contec_net_3_android;

import android.util.Base64;
import android.util.Log;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import cn.com.contec.net.util.Constants_jar;
import cn.com.contec.net.util.MD5_encoding;
import cn.com.contec_net_3_android_case.UploadUtil;

public class Method_android_upload_trend {
    public static String upLoadThrend(String pSessionID, String pHr, String pbloodsugar, String pweight, String pheight, String ptemp, String presp, String pspo2, String ppr, String psys, String pmean, String pdia, String pfetalheartrate, String ppalacepressure, String pquickening, String pfvc, String pfev1, String ppef, String pfev1rate, String pfef25, String pfef2575, String pfef75, String psteps, String pdistance, String pcalories, String pUserID, String pPSW, String pCheckTime, String pUrl) throws Exception {
        String _MessageHeader = "101034" + pSessionID + "11";
        String _MD5psw_user = MD5_encoding.MD5(String.valueOf(pUserID) + pPSW);
        String _base64Message = Base64.encodeToString(("<?xml version=\"1.0\" encoding=\"UTF-8\" ?><request><cardid>" + pUserID + "</cardid>" + "<deviceid></deviceid><devicename></devicename>" + "<record><hr>" + pHr + "</hr>" + "<checktime>" + pCheckTime + "</checktime>" + "</record></request>").getBytes(), 0);
        String _MessageHeaderSend = String.valueOf(MD5_encoding.MD5(String.valueOf(_MD5psw_user) + _MessageHeader + _base64Message)) + _MessageHeader;
        Map<String, String> params = new HashMap<>();
        params.put(Constants_jar.MSG_STRING, String.valueOf(_MessageHeaderSend) + _base64Message);
        String _respones = UploadUtil.submitPostData(params, "UTF-8", new URL(pUrl));
        Log.d("UploadTrend", "return string��" + _respones);
        return _respones;
    }

    public static String upLoadThrendOne(String pSessionID, String pSenderID, String pUserID, String pPSW, String pUrl, String pContent, String pCaseID) throws Exception {
        String _MessageHeader = "101034" + pSessionID + "11";
        String _MessageForm = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?><request><cardid>" + pSenderID + "</cardid>" + "<deviceid></deviceid><devicename></devicename>" + "<caseid>" + pCaseID + "</caseid>" + pContent + "</request>";
        String _MD5psw_user = MD5_encoding.MD5(String.valueOf(pUserID) + pPSW);
        String _base64Message = Base64.encodeToString(_MessageForm.getBytes(), 0);
        String _MessageHeaderSend = String.valueOf(MD5_encoding.MD5(String.valueOf(_MD5psw_user) + _MessageHeader + _base64Message)) + _MessageHeader;
        Map<String, String> params = new HashMap<>();
        params.put(Constants_jar.MSG_STRING, String.valueOf(_MessageHeaderSend) + _base64Message);
        String _respones = UploadUtil.submitPostData(params, "UTF-8", new URL(pUrl));
        Log.d("UploadTrend", "return string��" + _respones + "  messageForm:" + _MessageForm);
        return _respones;
    }

    public static String upLoadThrendTwo(String pSessionID, String pUserID, String pPSW, String pcontent, String pUrl) throws Exception {
        String _MessageHeader = "101052" + pSessionID + "10";
        String _base64Message = pcontent;
        String _MessageHeaderSend = String.valueOf(MD5_encoding.MD5(String.valueOf(MD5_encoding.MD5(String.valueOf(pUserID) + pPSW)) + _MessageHeader + _base64Message)) + _MessageHeader;
        Map<String, String> params = new HashMap<>();
        params.put(Constants_jar.MSG_STRING, String.valueOf(_MessageHeaderSend) + _base64Message);
        String _respones = UploadUtil.submitPostData(params, "UTF-8", new URL(pUrl));
        Log.d("UploadTrend", "return string��" + _respones);
        return _respones;
    }
}
