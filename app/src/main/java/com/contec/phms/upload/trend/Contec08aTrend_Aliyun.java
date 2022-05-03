package com.contec.phms.upload.trend;

import android.util.Log;
import com.alibaba.fastjson.JSON;
//import com.alibaba.sdk.android.top.TopService;
import com.contec.phms.App_phms;
import com.contec.phms.aliyun.AlkTopPostUserDataReqPayload;
import com.contec.phms.aliyun.AlkTopUserData;
import com.contec.phms.manager.device.DeviceManager;
import com.contec.phms.util.GetCurrentTimeMillis;
import java.util.HashMap;

public class Contec08aTrend_Aliyun {
    private HashMap<String, String> m;
    private byte[] temp;

    public Contec08aTrend_Aliyun(byte[] temp2) {
        this.temp = temp2;
    }

    public HashMap<String, String> getContent() {
        this.m = new HashMap<>();
        if (this.temp != null && this.temp.length > 0) {
            String collectTime = new GetCurrentTimeMillis(this.temp[0], this.temp[1], this.temp[2], this.temp[3], this.temp[4], this.temp[5]).getTimeMillis();
            String DBP = Integer.toString(this.temp[7] & 255);
            String SBP = Integer.toString((((this.temp[5] & 255) << 8) | (this.temp[6] & 255)) & 2047);
            String uploadTime = Long.toString(System.currentTimeMillis() / 1000);
            //this.m.put(TopService.METHOD_KEY, "aliyun.alink.userdata.post");
            this.m.put("format", "json");
            AlkTopUserData _DBPData = new AlkTopUserData();
            _DBPData.setUnit("mmHg");
            _DBPData.setValue(DBP);
            _DBPData.setAttribute("DBP");
            _DBPData.setData_collect_time(collectTime);
            _DBPData.setData_upload_time(uploadTime);
            AlkTopUserData _SBPData = new AlkTopUserData();
            _SBPData.setUnit("mmHg");
            _SBPData.setValue(SBP);
            _SBPData.setAttribute("SBP");
            _SBPData.setData_collect_time(collectTime);
            _SBPData.setData_upload_time(uploadTime);
            AlkTopPostUserDataReqPayload inputData = new AlkTopPostUserDataReqPayload();
            String Mac = DeviceManager.m_DeviceBean.getMacString();
            String phoneNum = App_phms.getInstance().GetUserInfoNAME();
            inputData.setDevice_id(Mac);
            inputData.setDevice_model("CONTEC_HEALTH_BLOODPRESSUREMONITOR_CONTEC08ABT");
            inputData.setData_list(new AlkTopUserData[]{_DBPData, _SBPData});
            inputData.setVendor_user_id(phoneNum);
            String dataStr = JSON.toJSONString(inputData);
            Log.e("--->", dataStr);
            this.m.put("input", dataStr);
        }
        return this.m;
    }
}
