package com.contec.phms.upload.trend;

import android.util.Base64;
import com.contec.phms.device.bc01.DeviceData;
import com.contec.phms.util.CLog;
import com.example.bm77_bc_code.BC401_Struct;
import org.apache.commons.httpclient.HttpMethodBase;
import u.aly.dp;

public class BC01_Trend extends Trend {
    private final String TAG = "BC01";
    public DeviceData mData;

    public BC01_Trend(com.contec.phms.device.template.DeviceData datas) {
        this.mData = (DeviceData) datas;
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
            pack = new byte[((_size * 27) + 3)];
            pack[0] = dp.l;
            pack[1] = _size;
            pack[2] = 27;
            int j = 0;
            int i = 3;
            while (j < _size) {
                BC401_Struct _data = this.mData.mDataList.get(j);
                int i2 = i + 1;
                pack[i] = (byte) _data.Year;
                int i3 = i2 + 1;
                pack[i2] = (byte) _data.Month;
                int i4 = i3 + 1;
                pack[i3] = (byte) _data.Date;
                int i5 = i4 + 1;
                pack[i4] = (byte) _data.Hour;
                int i6 = i5 + 1;
                pack[i5] = (byte) _data.Min;
                int i7 = i6 + 1;
                pack[i6] = (byte) (_data.URO_Real + 48);
                int i8 = i7 + 1;
                pack[i7] = (byte) (_data.URO + 48);
                int i9 = i8 + 1;
                pack[i8] = (byte) (_data.BLD_Real + 48);
                int i10 = i9 + 1;
                pack[i9] = (byte) (_data.BLD + 48);
                int i11 = i10 + 1;
                pack[i10] = (byte) (_data.BIL_Real + 48);
                int i12 = i11 + 1;
                pack[i11] = (byte) (_data.BIL + 48);
                int i13 = i12 + 1;
                pack[i12] = (byte) (_data.KET_Real + 48);
                int i14 = i13 + 1;
                pack[i13] = (byte) (_data.KET + 48);
                int i15 = i14 + 1;
                pack[i14] = (byte) (_data.GLU_Real + 48);
                int i16 = i15 + 1;
                pack[i15] = (byte) (_data.GLU + 48);
                int i17 = i16 + 1;
                pack[i16] = (byte) (_data.PRO_Real + 48);
                int i18 = i17 + 1;
                pack[i17] = (byte) (_data.PRO + 48);
                int i19 = i18 + 1;
                pack[i18] = (byte) (_data.PH_Real + 48);
                int i20 = i19 + 1;
                pack[i19] = (byte) (_data.PH + 48);
                int i21 = i20 + 1;
                pack[i20] = (byte) (_data.NIT_Real + 48);
                int i22 = i21 + 1;
                pack[i21] = (byte) (_data.NIT + 48);
                int i23 = i22 + 1;
                pack[i22] = (byte) (_data.LEU_Real + 48);
                int i24 = i23 + 1;
                pack[i23] = (byte) (_data.LEU + 48);
                int i25 = i24 + 1;
                pack[i24] = (byte) (_data.SG_Real + 48);
                int i26 = i25 + 1;
                pack[i25] = (byte) (_data.SG + 48);
                int i27 = i26 + 1;
                pack[i26] = (byte) (_data.VC_Real + 48);
                pack[i27] = (byte) (_data.VC + 48);
                j++;
                i = i27 + 1;
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
