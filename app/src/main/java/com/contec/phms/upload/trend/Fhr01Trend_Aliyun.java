package com.contec.phms.upload.trend;

import com.alibaba.fastjson.JSON;
////import com.alibaba.sdk.android.top.TopService;
import com.contec.phms.App_phms;
import com.contec.phms.aliyun.AlkTopPostUserDataReqPayload;
import com.contec.phms.aliyun.AlkTopUserData;
import com.contec.phms.manager.device.DeviceManager;
import com.contec.phms.util.GetCurrentTimeMillis;
import java.util.HashMap;

public class Fhr01Trend_Aliyun {
    public byte[] temp;

    public Fhr01Trend_Aliyun(byte[] temp2) {
        this.temp = temp2;
    }

    public HashMap<String, String> getContent() {
        HashMap<String, String> m = new HashMap<>();
        if (this.temp != null) {
            String collectTime = new GetCurrentTimeMillis(this.temp[0], this.temp[1], this.temp[2], this.temp[3], this.temp[4], this.temp[5]).getTimeMillis();
            String FHR = Integer.toString(this.temp[6] & 255);
            String uploadTime = Long.toString(System.currentTimeMillis() / 1000);
            //m.put(TopService.METHOD_KEY, "aliyun.alink.userdata.post");
            m.put("format", "json");
            AlkTopUserData FHRData = new AlkTopUserData();
            FHRData.setUnit("bpm");
            FHRData.setValue(FHR);
            FHRData.setAttribute("FetalHeartRate");
            FHRData.setData_collect_time(collectTime);
            FHRData.setData_upload_time(uploadTime);
            AlkTopPostUserDataReqPayload inputData = new AlkTopPostUserDataReqPayload();
            String Mac = DeviceManager.m_DeviceBean.getMacString();
            String phoneNum = App_phms.getInstance().GetUserInfoNAME();
            inputData.setDevice_id(Mac);
            inputData.setDevice_model("CONTEC_HEALTH_FETALDOPPLER_SONOLINEBT");
            inputData.setData_list(new AlkTopUserData[]{FHRData});
            inputData.setVendor_user_id(phoneNum);
            m.put("input", JSON.toJSONString(inputData));
        }
        return m;
    }
}
