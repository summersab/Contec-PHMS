package com.contec.phms.upload.trend;

import android.util.Base64;
import com.contec.cms50dj_jar.DeviceDataPedometerJar;
import com.contec.cms50dj_jar.MinData;
import com.contec.phms.App_phms;
import com.contec.phms.device.template.DeviceData;
import com.contec.phms.manager.device.DeviceManager;
import com.contec.phms.db.localdata.PedometerMinDataDao;
import com.contec.phms.db.localdata.opration.PedometerMinDataDaoOperation;
import com.contec.phms.util.CLog;
import com.contec.phms.util.Constants;
import org.apache.commons.httpclient.HttpMethodBase;
import u.aly.bs;

public class Cms50DJ_PedometerMin_Trend extends Trend {
    private final String TAG = "Cms50DJ_PedometerMin_Trend";
    public DeviceData mData;

    public Cms50DJ_PedometerMin_Trend(DeviceData datas) {
        this.mData = datas;
        getContent();
    }

    public HttpMethodBase getMethod() {
        return this.mMethod;
    }

    public String makeContect() {
        byte[] pack;
        int _cal;
        int _steps;
        byte[] bArr = null;
        if (this.mData == null || this.mData.mDataList.get(0) == null) {
            pack = new byte[]{0};
        } else {
            DeviceDataPedometerJar _djData = (DeviceDataPedometerJar) this.mData.mDataList.get(0);
            byte _size = (byte) _djData.getmPedometerDataMinList().size();
            int _mindataLength = 0;
            for (int i = 0; i < _djData.getmPedometerDataMinList().size(); i++) {
                _mindataLength += _djData.getmPedometerDataMinList().get(i).mMinDataList.size() * 4;
            }
            pack = new byte[(_mindataLength + 12)];
            for (int i2 = 0; i2 < _djData.getmPedometerDataMinList().size(); i2++) {
                MinData _mindata = _djData.getmPedometerDataMinList().get(i2);
                pack[0] = 8;
                pack[1] = _size;
                pack[2] = 0;
                pack[3] = _mindata.mStartDate[0];
                pack[4] = _mindata.mStartDate[1];
                pack[5] = _mindata.mStartDate[2];
                pack[6] = _mindata.mStartDate[3];
                pack[7] = 0;
                int n = 8;
                int _sumSteps = 0;
                int _sumCal = 0;
                for (int j = 0; j < _mindata.mMinDataList.size(); j++) {
                    byte[] _temp = _mindata.mMinDataList.get(j);
                    byte[] bArr2 = new byte[_mindata.mMinDataList.get(j).length];
                    byte[] _temp2 = _mindata.mMinDataList.get(j);
                    if (DeviceManager.m_DeviceBean.getmBluetoothType().equalsIgnoreCase(Constants.DEVICE_BLUEBOOTH_TYPE_CLASSIC)) {
                        _cal = (_temp2[1] & 255) | ((_temp2[0] & 255) << 8);
                        _steps = (_temp2[3] & 255) | ((_temp2[2] & 255) << 8);
                    } else {
                        _cal = ((_temp2[1] & 255) << 7) | (_temp2[0] & 255);
                        _steps = ((_temp2[3] & 255) << 7) | (_temp2[2] & 255);
                    }
                    CLog.d("Cms50DJ_PedometerMin_Trend", "cal_" + _cal + "  step:" + _steps);
                    if (_cal == 65535 || _cal == 32767) {
                        int n2 = n + 1;
                        pack[n] = _temp[0];
                        int n3 = n2 + 1;
                        pack[n2] = _temp[1];
                        int n4 = n3 + 1;
                        pack[n3] = _temp[2];
                        n = n4 + 1;
                        pack[n4] = _temp[3];
                    } else {
                        int n5 = n + 1;
                        pack[n] = _temp[0];
                        int n6 = n5 + 1;
                        pack[n5] = _temp[1];
                        int n7 = n6 + 1;
                        pack[n6] = _temp[2];
                        n = n7 + 1;
                        pack[n7] = _temp[3];
                        _sumCal += _cal;
                        _sumSteps += _steps;
                    }
                }
                int n8 = n + 1;
                pack[n] = (byte) ((_sumCal >> 8) & 255);
                int n9 = n8 + 1;
                pack[n8] = (byte) (_sumCal & 255);
                int n10 = n9 + 1;
                pack[n9] = (byte) ((_sumSteps >> 8) & 255);
                int i3 = n10 + 1;
                pack[n10] = (byte) (_sumSteps & 255);
                byte year = _mindata.mStartDate[0];
                byte month = _mindata.mStartDate[1];
                byte day = _mindata.mStartDate[2];
                byte hour = _mindata.mStartDate[3];
                StringBuilder builder = new StringBuilder();
                builder.append("20" + year + "-");
                if (month < 10) {
                    builder.append("0" + month + "-");
                } else {
                    builder.append(String.valueOf(month) + "-");
                }
                if (day < 10) {
                    builder.append("0" + day);
                } else {
                    builder.append(day);
                }
                builder.append(" ");
                if (hour < 10) {
                    builder.append("0" + hour);
                } else {
                    builder.append(hour);
                }
                String recordeDate = builder.toString();
                PedometerMinDataDaoOperation operation = PedometerMinDataDaoOperation.getInstance(App_phms.getInstance().getApplicationContext());
                if (!Boolean.valueOf(operation.querySql(recordeDate)).booleanValue()) {
                    PedometerMinDataDao data = new PedometerMinDataDao();
                    data.mTime = recordeDate;
                    data.mCal = new StringBuilder(String.valueOf(_sumCal)).toString();
                    data.mCaltarget = bs.b;
                    data.mSteps = new StringBuilder(String.valueOf(_sumSteps)).toString();
                    data.mUnique = this.mData.mFileName;
                    data.mFlag = "0";
                    operation.insertPedometerMinDao(data);
                }
            }
        }
        return encodeBASE64(pack);
    }

    public String encodeBASE64(byte[] pack) {
        return Base64.encodeToString(pack, 0);
    }
}
