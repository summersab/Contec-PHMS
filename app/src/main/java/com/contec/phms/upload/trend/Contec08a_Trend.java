package com.contec.phms.upload.trend;

import android.util.Base64;
import com.contec.phms.App_phms;
import com.contec.phms.device.template.DeviceData;
import com.contec.phms.manager.device.DeviceManager;
import com.contec.phms.util.CLog;
import org.apache.commons.httpclient.HttpMethodBase;

public class Contec08a_Trend extends Trend {
    private final String TAG = "Contec08a";
    public DeviceData mData;

    public Contec08a_Trend(DeviceData datas) {
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
            pack = new byte[((_size * 9) + 3)];
            pack[0] = 2;
            pack[1] = _size;
            pack[2] = 9;
            int j = 0;
            int i = 3;
            while (j < _size) {
                if (DeviceManager.mDeviceBeanList != null) {
                    DeviceManager.mDeviceBeanList.mProgress = (j / _size) * 100;
                    App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(DeviceManager.m_DeviceBean);
                    CLog.i("Contec08a", "Contec08a上传进度百分比：" + ((j / _size) * 100));
                }
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
                pack[i6] = (byte) (_temp[5] & Byte.MAX_VALUE);
                int i8 = i7 + 1;
                pack[i7] = _temp[6];
                int i9 = i8 + 1;
                pack[i8] = _temp[7];
                pack[i9] = _temp[8];
                j++;
                i = i9 + 1;
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
