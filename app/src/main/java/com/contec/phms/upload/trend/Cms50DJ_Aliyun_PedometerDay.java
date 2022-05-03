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

public class Cms50DJ_Aliyun_PedometerDay {
    private HashMap<String, String> m;
    private byte[] temp;

    public Cms50DJ_Aliyun_PedometerDay(byte[] _temp) {
        this.temp = _temp;
    }

    public HashMap<String, String> getContent() {
        this.m = new HashMap<>();
        if (this.temp != null && this.temp.length > 0) {
            String collectTime = new GetCurrentTimeMillis(this.temp[0], this.temp[1], this.temp[2], 0, 0, 0).getTimeMillis();
            int cal = ((this.temp[7] & 255) << 8) | (this.temp[8] & 255);
            int calTarget = ((this.temp[5] & 255) << 8) | (this.temp[6] & 255);
            String _cal = Integer.toString(cal);
            String _cal_target = Integer.toString(calTarget);
            String _steps = Integer.toString(((this.temp[3] & 255) << 8) | (this.temp[4] & 255));
            String uploadTime = Long.toString(System.currentTimeMillis() / 1000);
            //this.m.put(TopService.METHOD_KEY, "aliyun.alink.userdata.post");
            this.m.put("format", "json");
            AlkTopUserData _calData = new AlkTopUserData();
            _calData.setUnit("kcal");
            _calData.setValue(_cal);
            _calData.setAttribute("calorie");
            _calData.setData_collect_time(collectTime);
            _calData.setData_upload_time(uploadTime);
            AlkTopUserData _calTargetData = new AlkTopUserData();
            _calTargetData.setUnit("kcal");
            _calTargetData.setValue(_cal_target);
            _calTargetData.setAttribute("targetCalorie");
            _calTargetData.setData_collect_time(collectTime);
            _calTargetData.setData_upload_time(uploadTime);
            AlkTopUserData _stepsData = new AlkTopUserData();
            _stepsData.setUnit("steps");
            _stepsData.setValue(_steps);
            _stepsData.setAttribute("steps");
            _stepsData.setData_collect_time(collectTime);
            _stepsData.setData_upload_time(uploadTime);
            AlkTopPostUserDataReqPayload inputData = new AlkTopPostUserDataReqPayload();
            String Mac = DeviceManager.m_DeviceBean.getMacString();
            String phoneNum = App_phms.getInstance().GetUserInfoNAME();
            inputData.setDevice_id(Mac);
            inputData.setDevice_model("CONTEC_HEALTH_OXIMETER_CMS50DBT");
            inputData.setData_list(new AlkTopUserData[]{_stepsData, _calData, _calTargetData});
            inputData.setVendor_user_id(phoneNum);
            String dataStr = JSON.toJSONString(inputData);
            //Log.e("PedometerDataStr++++++++>", dataStr);
            this.m.put("input", dataStr);
        }
        return this.m;
    }
}
