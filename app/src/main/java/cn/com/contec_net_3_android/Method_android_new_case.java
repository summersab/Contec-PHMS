package cn.com.contec_net_3_android;

import android.util.Log;
import java.net.URL;
import java.util.HashMap;
import cn.com.contec.net.login.MethodGetCaseId;
import cn.com.contec_net_3_android_case.UploadUtil;

public class Method_android_new_case {
    public static String getCaseID(String pSessionID, String pHospitalname, String pHospitalID, String pUserName, String pDataTypeName, String pDataType, String pUserID, String pPSW, String pCheckTime, String pSex, String pURL, String pIsautomatic, String pOtherparams, String pLocalCasePath, String pDoctorID, String pDeviceID) {
        String _respones = null;
        try {
            String[] _login_array = MethodGetCaseId.getCaseID(pSessionID, pHospitalname, pHospitalID, pUserName, pDataTypeName, pDataType, pUserID, pPSW, pCheckTime, pSex, pIsautomatic, pOtherparams, pLocalCasePath, pDoctorID, pDeviceID);
            Log.e("getCaseID", "pSessionID:" + pSessionID + "pUserName:" + pUserName + " pDataTypeName: " + pDataTypeName + " pDataType:" + pDataType + "  pUserID:" + pUserID + "  pPSW:" + pPSW + " pCheckTime: " + pCheckTime);
            HashMap hashMap = new HashMap();
            hashMap.put(_login_array[0], _login_array[1]);
            _respones = UploadUtil.submitPostData(hashMap, "UTF-8", new URL(pURL));
            Log.d("UploadCase", "return string��" + _respones);
            return _respones;
        } catch (Exception e) {
            e.printStackTrace();
            return _respones;
        }
    }
}
