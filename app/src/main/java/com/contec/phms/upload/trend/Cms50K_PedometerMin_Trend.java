package com.contec.phms.upload.trend;

import android.util.Base64;
import android.util.Log;

import com.contec.jar.cms50k.MinData;
import com.contec.phms.App_phms;
import com.contec.phms.db.localdata.PedometerMinDataDao;
import com.contec.phms.db.localdata.opration.PedometerMinDataDaoOperation;
import com.contec.phms.device.template.DeviceData;
import org.apache.commons.httpclient.HttpMethodBase;

import u.aly.bs;

public class Cms50K_PedometerMin_Trend extends Trend {
    private final String TAG = "Cms50K_PedometerMin_Trend";
    public DeviceData mData;

    public Cms50K_PedometerMin_Trend(DeviceData datas) {
        this.mData = datas;
        getContent();
    }

    public HttpMethodBase getMethod() {
        return this.mMethod;
    }

    @Override // com.contec.phms.upload.trend.Trend
    public String makeContect() {
        byte[] pack;
        if (this.mData == null || this.mData.mDataList.get(0) == null) {
            pack = new byte[]{0};
        } else {
            byte _size = (byte) this.mData.mDataList.size();
            int _mindataLength = 0;
            for (int i = 0; i < _size; i++) {
                MinData _xData = (MinData) this.mData.mDataList.get(i);
                _mindataLength += _xData.mMinDataList.size() * 4;
            }
            int _length = _mindataLength + 12;
            pack = new byte[_length];
            for (int i2 = 0; i2 < _size; i2++) {
                MinData _mindata = (MinData) this.mData.mDataList.get(i2);
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
                    byte[] bArr = new byte[_mindata.mMinDataList.get(j).length];
                    byte[] _temp2 = _mindata.mMinDataList.get(j);
                    int _cal = ((_temp2[1] & 255) << 8) | (_temp2[0] & 255);
                    int _steps = ((_temp2[3] & 255) << 8) | (_temp2[2] & 255);
                    if (_cal == 65535 || _steps == 65535) {
                        _cal = 0;
                        _steps = 0;
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
                        pack[n] = _temp[1];
                        int n6 = n5 + 1;
                        pack[n5] = _temp[0];
                        int n7 = n6 + 1;
                        pack[n6] = _temp[3];
                        n = n7 + 1;
                        pack[n7] = _temp[2];
                        _sumCal += _cal;
                        _sumSteps += _steps;
                    }
                    Log.e("wsd1", "cal_" + _cal + "  step:" + _steps);
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
                builder.append("20" + ((int) year) + "-");
                if (month < 10) {
                    builder.append("0" + ((int) month) + "-");
                } else {
                    builder.append(String.valueOf((int) month) + "-");
                }
                if (day < 10) {
                    builder.append("0" + ((int) day));
                } else {
                    builder.append((int) day);
                }
                builder.append(" ");
                if (hour < 10) {
                    builder.append("0" + ((int) hour));
                } else {
                    builder.append((int) hour);
                }
                String recordeDate = builder.toString();
                PedometerMinDataDaoOperation operation = PedometerMinDataDaoOperation.getInstance(App_phms.getInstance().getApplicationContext());
                Boolean isHave = Boolean.valueOf(operation.querySql(recordeDate));
                if (!isHave.booleanValue()) {
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
