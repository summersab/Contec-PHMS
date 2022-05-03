package com.contec.phms.upload.trend;

import android.util.Base64;
import com.contec.phms.device.template.DeviceData;
import java.util.ArrayList;
import org.apache.commons.httpclient.HttpMethodBase;

public class CmssxtTrend extends Trend {
    public CmssxtTrend(ArrayList<DeviceData> datas) {
        this.mDatas = datas;
        getContent();
    }

    public HttpMethodBase getMethod() {
        return this.mMethod;
    }

    public String makeContect() {
        byte[] pack;
        int i;
        byte[] bArr = null;
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (this.mDatas != null) {
            byte _size = (byte) this.mDatas.size();
            pack = new byte[((_size * 7) + 3)];
            pack[0] = 3;
            pack[1] = _size;
            pack[2] = 7;
            int j = 0;
            int i2 = 3;
            while (j < _size) {
                DeviceData _Data = (DeviceData) this.mDatas.get(j);
                String year = new StringBuilder(String.valueOf(_Data.mDate[0])).toString();
                if (year.length() > 2) {
                    i = Integer.parseInt(year.substring(2, 4).toString());
                } else {
                    i = _Data.mDate[0];
                }
                int i3 = i2 + 1;
                pack[i2] = (byte) i;
                int i4 = i3 + 1;
                pack[i3] = (byte) _Data.mDate[1];
                int i5 = i4 + 1;
                pack[i4] = (byte) _Data.mDate[2];
                int i6 = i5 + 1;
                pack[i5] = (byte) _Data.mDate[3];
                int i7 = i6 + 1;
                pack[i6] = (byte) _Data.mDate[4];
                int data = (int) (_Data.mData * 10.0d);
                int i8 = i7 + 1;
                pack[i7] = (byte) ((data >> 8) & 255);
                pack[i8] = (byte) (data & 255);
                j++;
                i2 = i8 + 1;
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
