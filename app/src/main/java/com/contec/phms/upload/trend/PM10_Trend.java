package com.contec.phms.upload.trend;

import android.util.Base64;
import com.contec.phms.device.template.DeviceData;
import com.contec.phms.util.CLog;
import org.apache.commons.httpclient.HttpMethodBase;

public class PM10_Trend extends Trend {
    private final String TAG = "SpO2";
    public DeviceData mData;

    public PM10_Trend(DeviceData datas) {
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
            pack = new byte[((_size * 22) + 3)];
            pack[0] = 18;
            pack[1] = _size;
            pack[2] = 22;
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
                int i9 = i8 + 1;
                pack[i8] = _temp[7];
                int i10 = i9 + 1;
                pack[i9] = _temp[8];
                int i11 = i10 + 1;
                pack[i10] = _temp[9];
                int i12 = i11 + 1;
                pack[i11] = _temp[10];
                int i13 = i12 + 1;
                pack[i12] = _temp[11];
                int i14 = i13 + 1;
                pack[i13] = _temp[12];
                int i15 = i14 + 1;
                pack[i14] = _temp[13];
                int i16 = i15 + 1;
                pack[i15] = _temp[14];
                int i17 = i16 + 1;
                pack[i16] = _temp[15];
                int i18 = i17 + 1;
                pack[i17] = _temp[16];
                int i19 = i18 + 1;
                pack[i18] = _temp[17];
                int i20 = i19 + 1;
                pack[i19] = _temp[18];
                int i21 = i20 + 1;
                pack[i20] = _temp[19];
                int i22 = i21 + 1;
                pack[i21] = _temp[20];
                i = i22 + 1;
                pack[i22] = _temp[21];
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
