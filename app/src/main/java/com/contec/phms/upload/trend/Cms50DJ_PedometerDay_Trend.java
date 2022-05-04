package com.contec.phms.upload.trend;

import android.util.Base64;

import com.contec.cms50dj_jar.DeviceDataPedometerJar;
import com.contec.phms.App_phms;
import com.contec.phms.db.localdata.PedometerDayDataDao;
import com.contec.phms.db.localdata.opration.PedometerDayDataDaoOperation;
import com.contec.phms.device.template.DeviceData;
import com.contec.phms.manager.device.DeviceManager;
import com.contec.phms.util.CLog;
import com.contec.phms.util.Constants;

import org.apache.commons.httpclient.HttpMethodBase;

import u.aly.dp;

public class Cms50DJ_PedometerDay_Trend extends Trend {
    private final String TAG = "Cms50DJ_PedometerDay_Trend";
    public DeviceData mData;

    public Cms50DJ_PedometerDay_Trend(DeviceData datas) {
        this.mData = datas;
        getContent();
    }

    public HttpMethodBase getMethod() {
        return this.mMethod;
    }

    @Override // com.contec.phms.upload.trend.Trend
    public String makeContect() {
        byte[] pack;
        int _steps;
        int _caltarget;
        int _cal;
        if (this.mData == null || this.mData.mDataList.get(0) == null) {
            pack = new byte[]{0};
            CLog.e("*****************************", "**pack**pack***pack**pack*********");
        } else {
            DeviceDataPedometerJar _djData = (DeviceDataPedometerJar) this.mData.mDataList.get(0);
            byte _size = (byte) _djData.getmPedometerDataDayList().size();
            int _length = (_size * dp.l) + 3;
            pack = new byte[_length];
            pack[0] = 9;
            pack[1] = _size;
            pack[2] = dp.l;
            int i = 3;
            int j = 0;
            while (true) {
                int i2 = i;
                if (j >= _size) {
                    break;
                }
                byte[] _temp = _djData.getmPedometerDataDayList().get(j);
                if (DeviceManager.m_DeviceBean.getmBluetoothType().equalsIgnoreCase(Constants.DEVICE_BLUEBOOTH_TYPE_CLASSIC)) {
                    _cal = ((_temp[7] & 255) << 8) | (_temp[8] & 255);
                    _caltarget = ((_temp[5] & 255) << 8) | (_temp[6] & 255);
                    _steps = ((_temp[3] & 255) << 8) | (_temp[4] & 255);
                } else {
                    _steps = ((_temp[6] & 255) << 8) | (_temp[5] & 255);
                    _caltarget = ((_temp[8] & 255) << 8) | (_temp[7] & 255);
                    _cal = ((_temp[4] & 255) << 8) | (_temp[3] & 255);
                }
                if (_cal != 65535) {
                    int i3 = i2 + 1;
                    pack[i2] = _temp[0];
                    int i4 = i3 + 1;
                    pack[i3] = _temp[1];
                    int i5 = i4 + 1;
                    pack[i4] = _temp[2];
                    int i6 = i5 + 1;
                    pack[i5] = 0;
                    int i7 = i6 + 1;
                    pack[i6] = 0;
                    int i8 = i7 + 1;
                    pack[i7] = (byte) ((_steps >> 16) & 255);
                    int i9 = i8 + 1;
                    pack[i8] = (byte) ((_steps >> 8) & 255);
                    int i10 = i9 + 1;
                    pack[i9] = (byte) (_steps & 255);
                    int i11 = i10 + 1;
                    pack[i10] = (byte) ((_caltarget >> 16) & 255);
                    int i12 = i11 + 1;
                    pack[i11] = (byte) ((_caltarget >> 8) & 255);
                    int i13 = i12 + 1;
                    pack[i12] = (byte) (_caltarget & 255);
                    int i14 = i13 + 1;
                    pack[i13] = (byte) ((_cal >> 16) & 255);
                    int i15 = i14 + 1;
                    pack[i14] = (byte) ((_cal >> 8) & 255);
                    i2 = i15 + 1;
                    pack[i15] = (byte) (_cal & 255);
                    StringBuilder builder = new StringBuilder();
                    builder.append("20" + ((int) _temp[0]) + "-");
                    if (_temp[1] < 10) {
                        builder.append("0" + ((int) _temp[1]) + "-");
                    } else {
                        builder.append(String.valueOf((int) _temp[1]) + "-");
                    }
                    if (_temp[2] < 10) {
                        builder.append("0" + ((int) _temp[2]));
                    } else {
                        builder.append((int) _temp[2]);
                    }
                    String recordeDate = builder.toString();
                    PedometerDayDataDaoOperation operation = PedometerDayDataDaoOperation.getInstance(App_phms.getInstance().getApplicationContext());
                    Boolean isHave = Boolean.valueOf(operation.querySql(recordeDate));
                    if (!isHave.booleanValue()) {
                        PedometerDayDataDao data = new PedometerDayDataDao();
                        data.mTime = recordeDate;
                        data.mCal = new StringBuilder(String.valueOf(_cal)).toString();
                        data.mCaltarget = new StringBuilder(String.valueOf(_caltarget)).toString();
                        data.mSteps = new StringBuilder(String.valueOf(_steps)).toString();
                        data.mUnique = this.mData.mFileName;
                        data.mFlag = "0";
                        operation.insertPedometerDayDataDao(data);
                    }
                }
                i = i2;
                j++;
            }
        }
        return encodeBASE64(pack);
    }

    public String encodeBASE64(byte[] pack) {
        return Base64.encodeToString(pack, 0);
    }
}
