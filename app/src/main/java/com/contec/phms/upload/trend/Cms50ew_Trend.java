package com.contec.phms.upload.trend;

import android.util.Base64;
import com.contec.phms.App_phms;
import com.contec.phms.device.template.DeviceData;
import com.contec.phms.db.localdata.Spo2DataDao;
import com.contec.phms.db.localdata.opration.Spo2DataDaoOperation;
import com.contec.phms.util.CLog;
import com.contec.phms.util.PageUtil;
import org.apache.commons.httpclient.HttpMethodBase;

public class Cms50ew_Trend extends Trend {
    private final String TAG = "SpO2";
    public DeviceData mData;

    public Cms50ew_Trend(DeviceData datas) {
        this.mData = datas;
        getContent();
    }

    public HttpMethodBase getMethod() {
        return this.mMethod;
    }

    public String makeContect() {
        byte[] pack;
        byte[] bArr = null;
        if (this.mData != null) {
            byte _size = (byte) this.mData.mDataList.size();
            pack = new byte[((_size * 8) + 3)];
            pack[0] = 1;
            pack[1] = _size;
            pack[2] = 8;
            int i = 3;
            for (int j = 0; j < _size; j++) {
                byte[] _temp = (byte[]) this.mData.mDataList.get(j);
                int i2 = i + 1;
                pack[i] = _temp[0];
                int i3 = i2 + 1;
                pack[i2] = _temp[1];
                int i4 = i3 + 1;
                pack[i3] = _temp[2];
                int i5 = i4 + 1;
                pack[i4] = _temp[3];
                int i6 = i5 + 1;
                pack[i5] = _temp[4];
                int i7 = i6 + 1;
                pack[i6] = _temp[5];
                int i8 = i7 + 1;
                pack[i7] = _temp[6];
                i = i8 + 1;
                pack[i8] = _temp[7];
            }
            if (_size != 0) {
                doPack(pack);
            }
        } else {
            pack = new byte[]{0};
            CLog.e("*****************************", "**pack**pack***pack**pack*********");
        }
        return encodeBASE64(pack);
    }

    public String encodeBASE64(byte[] pack) {
        return Base64.encodeToString(pack, 0);
    }

    public void doPack(byte[] pack) {
        int count = pack.length / 8;
        for (int i = 0; i < count; i++) {
            int index = (i * 8) + 3;
            String dateTime = PageUtil.process_Date(pack[index] + 2000, pack[index + 1], pack[index + 2], pack[index + 3], pack[index + 4], pack[index + 5]);
            byte spo2 = pack[index + 6];
            byte pr = pack[index + 7];
            String unique = this.mData.mFileName;
            Spo2DataDaoOperation operation = Spo2DataDaoOperation.getInstance(App_phms.getInstance().getApplicationContext());
            Boolean isHave = Boolean.valueOf(operation.querySql(dateTime));
            CLog.e("SpO2gotU", unique + "上传的血氧数据为:" + dateTime);
            if (!isHave.booleanValue()) {
                Spo2DataDao data = new Spo2DataDao();
                data.mUnique = unique;
                data.mFlag = "0";
                data.mTime = dateTime;
                data.mSpo2 = new StringBuilder(String.valueOf(spo2)).toString();
                data.mPr = new StringBuilder(String.valueOf(pr)).toString();
                operation.insertSpo2DataDao(data);
            }
            CLog.e("SpO2gotU", this.mData.mFileName + "The uploaded blood oxygen data is: " + spo2 + "_" + pr + " time: " + dateTime);
        }
    }
}
