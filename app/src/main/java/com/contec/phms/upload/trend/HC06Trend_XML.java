package com.contec.phms.upload.trend;

import com.contec.phms.App_phms;
import com.contec.phms.device.template.DeviceData;
import com.contec.phms.db.localdata.TempertureDataDao;
import com.contec.phms.db.localdata.opration.TempertureDataDaoOperation;
import com.contec.phms.util.CLog;
import com.example.temp.bean.EarTempertureDataJar;
import org.apache.commons.httpclient.HttpMethodBase;

public class HC06Trend_XML extends Trend {
    DeviceData mData;

    public HC06Trend_XML(DeviceData data) {
        this.mData = data;
        getContent();
    }

    public HttpMethodBase getMethod() {
        return this.mMethod;
    }

    public String makeContect() {
        StringBuffer _content = new StringBuffer();
        if (this.mData != null) {
            int _size = this.mData.mDataList.size();
            CLog.dT("HC06Trend_XML", "_size:" + _size);
            for (int i = 0; i < _size; i++) {
                EarTempertureDataJar _data = (EarTempertureDataJar) this.mData.mDataList.get(i);
                _content.append("<record><temp>");
                _content.append(new StringBuilder().append(_data.m_data).toString());
                _content.append("</temp>");
                _content.append("<checktime>");
                _content.append(_data.m_saveDate);
                _content.append("</checktime></record>");
                CLog.e("gotu", "temp--" + _data.m_data + "time--" + _data.m_saveDate);
                TempertureDataDaoOperation operation = TempertureDataDaoOperation.getInstance(App_phms.getInstance());
                if (!Boolean.valueOf(operation.querySql(_data.m_saveDate)).booleanValue()) {
                    TempertureDataDao dao = new TempertureDataDao();
                    dao.mTemperture = new StringBuilder(String.valueOf(_data.m_data)).toString();
                    dao.mTime = _data.m_saveDate;
                    dao.mUnique = this.mData.mFileName;
                    dao.mFlag = "0";
                    operation.insertTempertureDataDao(dao);
                }
            }
        }
        return _content.toString();
    }
}
