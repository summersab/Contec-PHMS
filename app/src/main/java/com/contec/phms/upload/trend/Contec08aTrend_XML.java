package com.contec.phms.upload.trend;

import com.contec.phms.App_phms;
import com.contec.phms.device.template.DeviceData;
import com.contec.phms.db.localdata.BloodDDataDao;
import com.contec.phms.db.localdata.opration.BloodDDataDaoOperation;
import com.contec.phms.util.CLog;
import org.apache.commons.httpclient.HttpMethodBase;

public class Contec08aTrend_XML extends Trend {
    private final String TAG = "SpO2";
    public DeviceData mData;

    public Contec08aTrend_XML(DeviceData datas) {
        this.mData = datas;
        getContent();
    }

    public HttpMethodBase getMethod() {
        return this.mMethod;
    }

    public String makeContect() {
        StringBuffer _content = new StringBuffer();
        if (this.mData != null) {
            int _size = this.mData.mDataList.size();
            for (int i = 0; i < _size; i++) {
                byte[] _temp = (byte[]) this.mData.mDataList.get(i);
                String reTime = (_temp[0] + 2000) + "-" + _temp[1] + "-" + _temp[2] + " " + _temp[3] + ":" + _temp[4] + ":" + _temp[9];
                int _di = _temp[7] & 255;
                int _gao = (((_temp[5] & 255) << 8) | (_temp[6] & 255)) & 2047;
                int _ping = _temp[8] & 255;
                CLog.e("GotYou", "Upload contec08aw =====" + reTime + "低" + _di + "高" + _gao + "平" + _ping);
                _content.append("<record><bp><sys>");
                _content.append(new StringBuilder().append(_gao).toString());
                _content.append("</sys><mean>");
                _content.append(new StringBuilder().append(_ping).toString());
                _content.append("</mean><dia>");
                _content.append(new StringBuilder().append(_di).toString());
                _content.append("</dia></bp><checktime>");
                _content.append(reTime);
                _content.append("</checktime></record>");
                BloodDDataDaoOperation operation = BloodDDataDaoOperation.getInstance(App_phms.getInstance().getApplicationContext());
                if (!Boolean.valueOf(operation.querySql(reTime)).booleanValue()) {
                    BloodDDataDao data = new BloodDDataDao();
                    data.mUnique = this.mData.mFileName;
                    data.mTime = reTime;
                    data.mAverage = new StringBuilder(String.valueOf(_ping)).toString();
                    data.mHigh = new StringBuilder(String.valueOf(_gao)).toString();
                    data.mLow = new StringBuilder(String.valueOf(_di)).toString();
                    data.mFlag = "0";
                    operation.insertBloodDDataDao(data);
                }
            }
        }
        return _content.toString();
    }
}
