package com.contec.phms.upload.trend;

import android.util.Base64;
import com.contec.phms.App_phms;
import com.contec.phms.device.template.DeviceData;
import com.contec.phms.manager.device.DeviceManager;
import com.contec.phms.util.CLog;
import com.contec.phms.util.PrintBytes;
import java.util.ArrayList;
import org.apache.commons.httpclient.HttpMethodBase;

public class WTTrend extends Trend {
    private final String TAG = "WTTrend";

    public WTTrend(ArrayList<DeviceData> datas) {
        this.mDatas = datas;
        getContent();
    }

    public HttpMethodBase getMethod() {
        return this.mMethod;
    }

    public String makeContect() {
        byte[] pack;
        byte[] bArr = null;
        if (this.mDatas != null) {
            byte _size = (byte) this.mDatas.size();
            pack = new byte[((_size * 7) + 3)];
            pack[0] = 4;
            pack[1] = _size;
            pack[2] = 7;
            int j = 0;
            int i = 3;
            while (j < _size) {
                if (DeviceManager.mDeviceBeanList != null) {
                    DeviceManager.m_DeviceBean.mProgress = (j / _size) * 100;
                    App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(DeviceManager.m_DeviceBean);
                    CLog.i("WTTrend", "WT上传进度百分比：" + ((j / _size) * 100));
                }
                byte[] _temp = ((DeviceData) this.mDatas.get(j)).mPack;
                CLog.d("******************", "type:" + ((DeviceData) this.mDatas.get(j)).mFileName);
                PrintBytes.printData(_temp);
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
                pack[i6] = _temp[6];
                pack[i7] = _temp[7];
                j++;
                i = i7 + 1;
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
}
