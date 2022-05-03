package com.contec.phms.upload.trend;

import android.util.Base64;
import com.contec.phms.App_phms;
import com.contec.phms.device.template.DeviceData;
import com.contec.phms.db.localdata.WeightDataDao;
import com.contec.phms.db.localdata.opration.WeightDataDaoOperation;
import com.contec.phms.util.CLog;
import org.apache.commons.httpclient.HttpMethodBase;
import cn.com.contec.jar.wt100.WTDeviceDataJar;

public class WTTrend_XML extends Trend {
    private final String TAG = "WTTrend";
    public DeviceData mData;

    public WTTrend_XML(DeviceData datas) {
        this.mData = datas;
        getContent();
    }

    public HttpMethodBase getMethod() {
        return this.mMethod;
    }

    public String makeContect() {
        StringBuffer _content = new StringBuffer();
        if (this.mData != null) {
            for (int i = 0; i < this.mData.mDataList.size(); i++) {
                WTDeviceDataJar _data = (WTDeviceDataJar) this.mData.mDataList.get(i);
                _content.append("<record><weight>");
                _content.append(new StringBuilder(String.valueOf(_data.m_data)).toString());
                _content.append("</weight><checktime>");
                _content.append(_data.m_saveDate);
                _content.append("</checktime></record>");
                WeightDataDaoOperation operation = WeightDataDaoOperation.getInstance(App_phms.getInstance().getApplicationContext());
                if (!Boolean.valueOf(operation.querySql(_data.m_saveDate)).booleanValue()) {
                    WeightDataDao data = new WeightDataDao();
                    data.mTime = _data.m_saveDate;
                    data.mWeight = new StringBuilder(String.valueOf(_data.m_data)).toString();
                    data.mUnique = this.mData.mFileName;
                    data.mFlag = "0";
                    operation.insertWeightDataDao(data);
                }
            }
        }
        CLog.i("WTTrend", "要上传的体重数据是: " + _content.toString());
        return _content.toString();
    }

    public String encodeBASE64(byte[] pack) {
        return Base64.encodeToString(pack, 0);
    }
}
