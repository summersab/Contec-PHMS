package com.contec.phms.upload.trend;

import android.util.Base64;
import com.contec.phms.App_phms;
import com.contec.phms.device.template.DeviceData;
import com.contec.phms.manager.device.DeviceManager;
import com.contec.phms.util.CLog;
import java.util.ArrayList;
import org.apache.commons.httpclient.HttpMethodBase;

public class Cms50ew_08A_Trend extends Trend {
    private final String TAG = "SpO2";

    public Cms50ew_08A_Trend(ArrayList<DeviceData> datas) {
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
            pack = new byte[((_size * 8) + 3)];
            pack[0] = 1;
            pack[1] = _size;
            pack[2] = 8;
            int i = 3;
            for (int j = 0; j < _size; j++) {
                if (DeviceManager.mDeviceBeanList != null) {
                    DeviceManager.m_DeviceBean.mProgress = (j / _size) * 100;
                    App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(DeviceManager.m_DeviceBean);
                    CLog.i("SpO2", "SpO2 upload progress percentage: " + ((j / _size) * 100));
                }
                byte[] _temp = ((DeviceData) this.mDatas.get(j)).mPack;
                CLog.d("******************", "type:" + ((DeviceData) this.mDatas.get(j)).mFileName);
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
