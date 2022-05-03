package com.contec.phms.upload.trend;

import android.util.Base64;
import com.contec.phms.App_phms;
import com.contec.phms.device.pm10.DeviceData;
import com.contec.phms.db.localdata.PluseDataDao;
import com.contec.phms.db.localdata.opration.PluseDataDaoOperation;
import com.contec.phms.util.CLog;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.commons.httpclient.HttpMethodBase;

public class PM10Trend_XML extends Trend {
    private final String TAG = "PM10Trend_XML";
    public DeviceData mData;

    public PM10Trend_XML(com.contec.phms.device.template.DeviceData datas) {
        this.mData = (DeviceData) datas;
        getContent();
    }

    public HttpMethodBase getMethod() {
        return this.mMethod;
    }

    public String makeContect() {
        StringBuffer _content = new StringBuffer();
        if (!(this.mData == null || this.mData.TrendData == null)) {
            byte[] _temp = this.mData.TrendData;
            String _time = (_temp[0] + 2000) + "-" + _temp[1] + "-" + _temp[2] + " " + _temp[3] + ":" + _temp[4] + ":" + _temp[5];
            Date dDate = null;
            SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
                dDate = format2.parse(_time);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            int _pluse = (((_temp[6] & 255) << 7) | (_temp[7] & 255)) & 2047;
            String _result = new StringBuilder().append(_temp[8]).append(_temp[9]).append(_temp[10]).append(_temp[11]).append(_temp[12]).append(_temp[13]).append(_temp[14]).append(_temp[15]).append(_temp[16]).append(_temp[17]).append(_temp[18]).append(_temp[19]).append(_temp[20]).append(_temp[21]).toString();
            String reTime = format2.format(dDate);
            _content.append("<record><hrconclusion><value>");
            _content.append(new StringBuilder().append(_pluse).toString());
            _content.append("</value><conclusion>");
            _content.append(_result);
            _content.append("</conclusion></hrconclusion><checktime>");
            _content.append(reTime);
            _content.append("</checktime></record>");
            PluseDataDaoOperation operation = PluseDataDaoOperation.getInstance(App_phms.getInstance());
            if (!Boolean.valueOf(operation.querySql(reTime)).booleanValue()) {
                PluseDataDao dao = new PluseDataDao();
                dao.mUnique = this.mData.mFileName;
                dao.mPluse = new StringBuilder(String.valueOf(_pluse)).toString();
                dao.mResult = _result;
                dao.mTime = reTime;
                dao.mFlag = "0";
                operation.insertPluseDao(dao);
            }
            CLog.e("PM10Trend_XMLgotU", "_pluse" + _pluse + "_result" + _result + "reTime" + reTime);
        }
        System.out.println(_content.toString());
        return _content.toString();
    }

    public String encodeBASE64(byte[] pack) {
        return Base64.encodeToString(pack, 0);
    }
}
