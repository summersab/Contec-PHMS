package com.contec.phms.upload.trend;

import com.contec.phms.App_phms;
import com.contec.phms.device.template.DeviceData;
import com.contec.phms.db.localdata.CmssxtDataDao;
import com.contec.phms.db.localdata.opration.CmssxtDataDaoOperation;
import org.apache.commons.httpclient.HttpMethodBase;
import cn.com.contec.jar.cmssxt.CmssxtDataJar;

public class CmssxtTrend_XML extends Trend {
    private final String TAG = "SpO2";
    public DeviceData mData;

    public CmssxtTrend_XML(DeviceData datas) {
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
                CmssxtDataJar _data = (CmssxtDataJar) this.mData.mDataList.get(i);
                _content.append("<record><bloodsugar><bloodsugar>");
                _content.append(new StringBuilder(String.valueOf(_data.m_data)).toString());
                _content.append("</bloodsugar><flag>0</flag></bloodsugar><checktime>");
                _content.append(_data.m_saveDate);
                _content.append("</checktime></record>");
                CmssxtDataDaoOperation operation = CmssxtDataDaoOperation.getInstance(App_phms.getInstance().getApplicationContext());
                if (!Boolean.valueOf(operation.querySql(_data.m_saveDate)).booleanValue()) {
                    CmssxtDataDao data = new CmssxtDataDao();
                    data.mTime = _data.m_saveDate;
                    data.mData = new StringBuilder(String.valueOf(_data.m_data)).toString();
                    data.mUnique = this.mData.mFilePath;
                    data.mFlag = "0";
                    operation.insertCmssxtDataDao(data);
                }
            }
        }
        return _content.toString();
    }
}
