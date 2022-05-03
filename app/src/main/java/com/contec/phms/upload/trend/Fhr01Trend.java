package com.contec.phms.upload.trend;

import android.util.Base64;
import com.contec.phms.App_phms;
import com.contec.phms.device.template.DeviceData;
import com.contec.phms.manager.device.DeviceManager;
import com.contec.phms.db.localdata.FetalHeartDataDao;
import com.contec.phms.db.localdata.opration.FeltalHeartDataDaoOperation;
import com.contec.phms.util.CLog;
import com.contec.phms.util.PageUtil;
import org.apache.commons.httpclient.HttpMethodBase;
import u.aly.dp;

public class Fhr01Trend extends Trend {
    private final String TAG = "FHR01";
    public DeviceData mData;

    public Fhr01Trend(DeviceData datas) {
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
            pack = new byte[((_size * 11) + 3)];
            pack[0] = dp.k;
            pack[1] = _size;
            pack[2] = 11;
            int j = 0;
            int i = 3;
            while (j < _size) {
                if (DeviceManager.mDeviceBeanList != null) {
                    DeviceManager.m_DeviceBean.mProgress = (j / _size) * 100;
                    App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(DeviceManager.m_DeviceBean);
                    CLog.i("FHR01", "FHR01 Upload Progress Percentage: " + ((j / _size) * 100));
                }
                byte[] _temp = (byte[]) this.mData.mDataList.get(j);
                int i2 = i + 1;
                pack[i] = _temp[0];
                int i3 = i2 + 1;
                pack[i2] = (byte) (_temp[1] + 1);
                int i4 = i3 + 1;
                pack[i3] = _temp[2];
                int i5 = i4 + 1;
                pack[i4] = _temp[3];
                int i6 = i5 + 1;
                pack[i5] = _temp[4];
                int i7 = i6 + 1;
                pack[i6] = 0;
                int i8 = i7 + 1;
                pack[i7] = _temp[6];
                int i9 = i8 + 1;
                pack[i8] = 0;
                int i10 = i9 + 1;
                pack[i9] = 0;
                int i11 = i10 + 1;
                pack[i10] = 0;
                pack[i11] = 0;
                j++;
                i = i11 + 1;
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

    private void doPack(byte[] pack) {
        int count = pack.length / 8;
        for (int i = 0; i < count; i++) {
            int index = (i * 8) + 3;
            String dateTime = PageUtil.process_Date(pack[index] + 2000, pack[index + 1], pack[index + 2], pack[index + 3], pack[index + 4], pack[index + 5]);
            byte fetalHeartRate = pack[index + 7];
            FeltalHeartDataDaoOperation operation = FeltalHeartDataDaoOperation.getInstance(App_phms.getInstance().getApplicationContext());
            if (!Boolean.valueOf(operation.querySql(dateTime)).booleanValue()) {
                FetalHeartDataDao data = new FetalHeartDataDao();
                data.mTime = dateTime;
                data.mFetalHeartRate = new StringBuilder(String.valueOf(fetalHeartRate)).toString();
                data.mUnique = this.mData.mFileName;
                data.mFlag = "0";
                operation.insertFetalHeartDataDao(data);
                CLog.e("FHR01gotU", "上传的胎心数据为:" + fetalHeartRate + "time: " + dateTime);
            }
        }
    }

    public String encodeBASE64(byte[] pack) {
        return Base64.encodeToString(pack, 0);
    }
}
