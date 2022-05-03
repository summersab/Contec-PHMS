package com.contec.phms.upload.trend;

import android.util.Base64;
import com.alibaba.cchannel.CloudChannel;
import com.contec.phms.device.template.DeviceData;
import com.contec.phms.util.CLog;
import com.contec.phms.util.PageUtil;
import org.apache.commons.httpclient.HttpMethodBase;
import cn.com.contec.jar.sp10w.DeviceDataJar;

public class Sp10Trend extends Trend {
    public DeviceData mDatas;

    public Sp10Trend(DeviceData datas) {
        this.mDatas = datas;
        getContent();
    }

    public HttpMethodBase getMethod() {
        return this.mMethod;
    }

    public String makeContect() {
        byte[] pack;
        byte[] bArr = null;
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (this.mDatas != null) {
            byte _size = (byte) this.mDatas.mDataList.size();
            pack = new byte[((_size * CloudChannel.SDK_VERSION) + 3)];
            pack[0] = 6;
            pack[1] = _size;
            pack[2] = CloudChannel.SDK_VERSION;
            int j = 0;
            int i = 3;
            while (j < _size) {
                DeviceDataJar mUserDatas = (DeviceDataJar) this.mDatas.mDataList.get(j);
                String _time = mUserDatas.mPatientInfo.mTime;
                CLog.e("Sp10Trend", "time:" + _time);
                if (_time == null) {
                    _time = PageUtil.getCurrentTime("yyyy_MM_dd HH:mm:ss");
                }
                byte _year = (byte) Integer.parseInt(_time.substring(2, 4).toString());
                byte _month = (byte) Integer.parseInt(_time.substring(5, 7).toString());
                byte _day = (byte) Integer.parseInt(_time.substring(8, 10).toString());
                byte _hour = (byte) Integer.parseInt(_time.substring(11, 13).toString());
                byte _mm = (byte) Integer.parseInt(_time.substring(14, 16).toString());
                byte[] pack1 = mUserDatas.mParamInfo.info1;
                byte[] pack2 = mUserDatas.mParamInfo.info2;
                int i2 = i + 1;
                pack[i] = _year;
                int i3 = i2 + 1;
                pack[i2] = _month;
                int i4 = i3 + 1;
                pack[i3] = _day;
                int i5 = i4 + 1;
                pack[i4] = _hour;
                int i6 = i5 + 1;
                pack[i5] = _mm;
                int test = (((pack1[2] & 255) | ((pack1[3] & 255) << 8)) & 65535) * 10;
                int i7 = i6 + 1;
                pack[i6] = (byte) ((test >> 8) & 255);
                int i8 = i7 + 1;
                pack[i7] = (byte) (test & 255);
                int test2 = (((pack1[4] & 255) | ((pack1[5] & 255) << 8)) & 65535) * 10;
                int i9 = i8 + 1;
                pack[i8] = (byte) ((test2 >> 8) & 255);
                int i10 = i9 + 1;
                pack[i9] = (byte) (test2 & 255);
                int test3 = (((pack1[6] & 255) | ((pack1[7] & 255) << 8)) & 65535) * 10;
                int i11 = i10 + 1;
                pack[i10] = (byte) ((test3 >> 8) & 255);
                int i12 = i11 + 1;
                pack[i11] = (byte) (test3 & 255);
                int test4 = (((pack2[2] & 255) | ((pack2[3] & 255) << 8)) & 65535) * 10;
                int i13 = i12 + 1;
                pack[i12] = (byte) ((test4 >> 8) & 255);
                int i14 = i13 + 1;
                pack[i13] = (byte) (test4 & 255);
                int test5 = (((pack2[6] & 255) | ((pack2[7] & 255) << 8)) & 65535) * 10;
                int i15 = i14 + 1;
                pack[i14] = (byte) ((test5 >> 8) & 255);
                int i16 = i15 + 1;
                pack[i15] = (byte) (test5 & 255);
                int test6 = (((pack2[4] & 255) | ((pack2[5] & 255) << 8)) & 65535) * 10;
                int i17 = i16 + 1;
                pack[i16] = (byte) ((test6 >> 8) & 255);
                pack[i17] = (byte) (test6 & 255);
                j++;
                i = i17 + 1;
            }
        } else {
            pack = new byte[]{0};
        }
        return encodeBASE64(pack);
    }

    public String encodeBASE64(byte[] pack) {
        return Base64.encodeToString(pack, 0);
    }
}
