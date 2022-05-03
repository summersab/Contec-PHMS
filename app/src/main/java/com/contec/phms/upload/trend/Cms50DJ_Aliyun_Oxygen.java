package com.contec.phms.upload.trend;

import android.util.Log;
import com.alibaba.fastjson.JSON;
////import com.alibaba.sdk.android.top.TopService;
import com.contec.phms.App_phms;
import com.contec.phms.aliyun.AlkTopPostUserDataReqPayload;
import com.contec.phms.aliyun.AlkTopUserData;
import com.contec.phms.manager.device.DeviceManager;
import com.contec.phms.util.GetCurrentTimeMillis;
import java.util.HashMap;

public class Cms50DJ_Aliyun_Oxygen {
    private HashMap<String, String> m;
    private byte[] temp;

    public Cms50DJ_Aliyun_Oxygen(byte[] _temp) {
        this.temp = _temp;
    }

    public HashMap<String, String> getContent() {
        this.m = new HashMap<>();
        if (this.temp != null && this.temp.length > 0) {
            String collectTime = new GetCurrentTimeMillis(this.temp[0], this.temp[1], this.temp[2], this.temp[3], this.temp[4], this.temp[5]).getTimeMillis();
            byte oxygen = this.temp[6];
            byte pulse = this.temp[7];
            String _oxygen = Integer.toString(oxygen);
            String _pulseRate = Integer.toString(pulse);
            String uploadTime = Long.toString(System.currentTimeMillis() / 1000);
            //this.m.put(TopService.METHOD_KEY, "aliyun.alink.userdata.post");
            this.m.put("format", "json");
            AlkTopUserData _oxygenData = new AlkTopUserData();
            _oxygenData.setUnit("%");
            _oxygenData.setValue(_oxygen);
            _oxygenData.setAttribute("oxygen");
            _oxygenData.setData_collect_time(collectTime);
            _oxygenData.setData_upload_time(uploadTime);
            AlkTopUserData _pulseRateData = new AlkTopUserData();
            _pulseRateData.setUnit("bpm");
            _pulseRateData.setValue(_pulseRate);
            _pulseRateData.setAttribute("pulseRate");
            _pulseRateData.setData_collect_time(collectTime);
            _pulseRateData.setData_upload_time(uploadTime);
            AlkTopPostUserDataReqPayload inputData = new AlkTopPostUserDataReqPayload();
            String Mac = DeviceManager.m_DeviceBean.getMacString();
            String phoneNum = App_phms.getInstance().GetUserInfoNAME();
            inputData.setDevice_id(Mac);
            inputData.setDevice_model("CONTEC_HEALTH_OXIMETER_CMS50DBT");
            inputData.setData_list(new AlkTopUserData[]{_oxygenData, _pulseRateData});
            inputData.setVendor_user_id(phoneNum);
            String dataStr = JSON.toJSONString(inputData);
            Log.e("dataStr++++++++>", dataStr);
            this.m.put("input", dataStr);
        }
        return this.m;
    }
}
