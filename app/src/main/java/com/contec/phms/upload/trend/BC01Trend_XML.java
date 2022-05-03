package com.contec.phms.upload.trend;

import android.util.Log;
import com.contec.phms.App_phms;
import com.contec.phms.device.bc01.DeviceData;
import com.contec.phms.db.localdata.UrineDataDao;
import com.contec.phms.db.localdata.opration.UrineDataDaoOperation;
import com.contec.phms.util.PageUtil;
import com.example.bm77_bc_code.BC401_Struct;
import org.apache.commons.httpclient.HttpMethodBase;
import org.apache.commons.httpclient.cookie.CookieSpec;
import u.aly.bs;

public class BC01Trend_XML extends Trend {
    private final String TAG = "SpO2";
    public DeviceData mData;

    public BC01Trend_XML(com.contec.phms.device.template.DeviceData data) {
        this.mData = (DeviceData) data;
        getContent();
    }

    public HttpMethodBase getMethod() {
        return this.mMethod;
    }

    public String makeContect() {
        StringBuffer _content = new StringBuffer();
        if (this.mData != null) {
            int _size = this.mData.mDataList.size();
            for (int i = 0; i < _size; i++) {
                BC401_Struct _data = this.mData.mDataList.get(i);
                byte[] provalue = PageUtil.compareDate(PageUtil.process_Date(_data.Year + 2000, _data.Month, _data.Date, _data.Hour, _data.Min, _data.Sec));
                if (provalue != null) {
                    String _dateTime = PageUtil.process_Date((provalue[0] & 255) + 2000, provalue[1] & 255, provalue[2] & 255, provalue[3] & 255, provalue[4] & 255, provalue[5] & 255);
                    String _value = "0" + _data.URO + "0" + _data.BLD + "0" + _data.BIL + "0" + _data.KET + "0" + _data.GLU + "0" + _data.PRO + "0" + _data.PH + "0" + _data.NIT + "0" + _data.LEU + "0" + _data.SG + "0" + _data.VC;
                    String value = String.valueOf(_data.URO) + CookieSpec.PATH_DELIM + _data.BLD + CookieSpec.PATH_DELIM + _data.BIL + CookieSpec.PATH_DELIM + _data.KET + CookieSpec.PATH_DELIM + _data.GLU + CookieSpec.PATH_DELIM + _data.PRO + CookieSpec.PATH_DELIM + _data.PH + CookieSpec.PATH_DELIM + _data.NIT + CookieSpec.PATH_DELIM + _data.LEU + CookieSpec.PATH_DELIM + _data.SG + CookieSpec.PATH_DELIM + _data.VC;
                    String valuedd = null;
                    String _valuenew = null;
                    if (_data.MAL == -8 && _data.CR == -8 && _data.UCA == -8) {
                        Log.e("当前的数据显示", "==============");
                        Log.e("=================", "==============_value" + _value);
                        Log.e("当前的数据显示", "==============");
                        _content.append("<record><urine><value>");
                        _content.append(_value);
                        _content.append("</value></urine>");
                        _content.append("<checktime>");
                        _content.append(_dateTime);
                        _content.append("</checktime></record>");
                    } else if (_data.MAL == 9 && _data.CR == 9 && _data.UCA == 9) {
                        _valuenew = String.valueOf(dataLength(_data.URO1)) + dataLength(_data.BLD1) + dataLength(_data.BIL1) + dataLength(_data.KET1) + dataLength(_data.GLU1) + dataLength(_data.PRO1) + dataLength(_data.PH1) + dataLength(_data.NIT1) + dataLength(_data.LEU1) + dataLength(_data.SG1) + dataLength(_data.VC1);
                        Log.e("当前的数据显示", "==============");
                        Log.e("=================", "==============_value" + _value);
                        Log.e("=================", "==============_valuenew" + _valuenew);
                        Log.e("当前的数据显示", "==============");
                        _content.append("<record><urine><value>");
                        _content.append(_value);
                        _content.append("</value><valuenew>");
                        _content.append(_valuenew);
                        _content.append("</valuenew></urine>");
                        _content.append("<checktime>");
                        _content.append(_dateTime);
                        _content.append("</checktime></record>");
                    } else {
                        String _valueadd = String.valueOf(addDataValue(_data.MAL)) + addDataValue(_data.CR) + addDataValue(_data.UCA);
                        _valuenew = String.valueOf(dataLength(_data.URO1)) + dataLength(_data.BLD1) + dataLength(_data.BIL1) + dataLength(_data.KET1) + dataLength(_data.GLU1) + dataLength(_data.PRO1) + dataLength(_data.PH1) + dataLength(_data.NIT1) + dataLength(_data.LEU1) + dataLength(_data.SG1) + dataLength(_data.VC1) + dataLength(_data.MAL1) + dataLength(_data.CR1) + dataLength(_data.UCA1);
                        valuedd = CookieSpec.PATH_DELIM + _data.MAL + CookieSpec.PATH_DELIM + _data.CR + CookieSpec.PATH_DELIM + _data.UCA;
                        Log.e("当前的数据显示", "==============");
                        Log.e("=================", "==============_value" + _value);
                        Log.e("=================", "==============_valueadd" + _valueadd);
                        Log.e("=================", "==============_valuenew" + _valuenew);
                        Log.e("当前的数据显示", "==============");
                        _content.append("<record><urine><value>");
                        _content.append(_value);
                        _content.append("</value><valueadd>");
                        _content.append(_valueadd);
                        _content.append("</valueadd><valuenew>");
                        _content.append(_valuenew);
                        _content.append("</valuenew></urine>");
                        _content.append("<checktime>");
                        _content.append(_dateTime);
                        _content.append("</checktime></record>");
                    }
                    UrineDataDaoOperation operation = UrineDataDaoOperation.getInstance(App_phms.getInstance().getApplicationContext());
                    if (!Boolean.valueOf(operation.querySql(_dateTime)).booleanValue()) {
                        UrineDataDao data = new UrineDataDao();
                        data.mUnique = this.mData.mFileName;
                        data.mTime = _dateTime;
                        data.mValue = value;
                        data.mValuedd = valuedd;
                        data.mValuenew = _valuenew;
                        data.mFlag = "0";
                        operation.insertUrineDataDao(data);
                    }
                } else {
                    String _dateTime2 = PageUtil.process_Date(_data.Year + 2000, _data.Month, _data.Date, _data.Hour, _data.Min, _data.Sec);
                    String _value2 = "0" + _data.URO + "0" + _data.BLD + "0" + _data.BIL + "0" + _data.KET + "0" + _data.GLU + "0" + _data.PRO + "0" + _data.PH + "0" + _data.NIT + "0" + _data.LEU + "0" + _data.SG + "0" + _data.VC;
                    String value2 = String.valueOf(_data.URO) + CookieSpec.PATH_DELIM + _data.BLD + CookieSpec.PATH_DELIM + _data.BIL + CookieSpec.PATH_DELIM + _data.KET + CookieSpec.PATH_DELIM + _data.GLU + CookieSpec.PATH_DELIM + _data.PRO + CookieSpec.PATH_DELIM + _data.PH + CookieSpec.PATH_DELIM + _data.NIT + CookieSpec.PATH_DELIM + _data.LEU + CookieSpec.PATH_DELIM + _data.SG + CookieSpec.PATH_DELIM + _data.VC;
                    String valueadd = null;
                    String _valuenew2 = null;
                    if (_data.MAL == -8 && _data.CR == -8 && _data.UCA == -8) {
                        Log.e("当前的数据显示", "==============");
                        Log.e("=================", "==============_value" + _value2);
                        Log.e("当前的数据显示", "==============");
                        _content.append("<record><urine><value>");
                        _content.append(_value2);
                        _content.append("</value></urine>");
                        _content.append("<checktime>");
                        _content.append(_dateTime2);
                        _content.append("</checktime></record>");
                    } else if (_data.MAL == 9 && _data.CR == 9 && _data.UCA == 9) {
                        _valuenew2 = String.valueOf(dataLength(_data.URO1)) + dataLength(_data.BLD1) + dataLength(_data.BIL1) + dataLength(_data.KET1) + dataLength(_data.GLU1) + dataLength(_data.PRO1) + dataLength(_data.PH1) + dataLength(_data.NIT1) + dataLength(_data.LEU1) + dataLength(_data.SG1) + dataLength(_data.VC1);
                        Log.e("当前的数据显示", "==============");
                        Log.e("=================", "==============_value" + _value2);
                        Log.e("=================", "==============_valuenew" + _valuenew2);
                        Log.e("当前的数据显示", "==============");
                        _content.append("<record><urine><value>");
                        _content.append(_value2);
                        _content.append("</value><valuenew>");
                        _content.append(_valuenew2);
                        _content.append("</valuenew></urine>");
                        _content.append("<checktime>");
                        _content.append(_dateTime2);
                        _content.append("</checktime></record>");
                    } else {
                        String _valueadd2 = String.valueOf(addDataValue(_data.MAL)) + addDataValue(_data.CR) + addDataValue(_data.UCA);
                        valueadd = CookieSpec.PATH_DELIM + _data.MAL + CookieSpec.PATH_DELIM + _data.CR + CookieSpec.PATH_DELIM + _data.UCA;
                        _valuenew2 = String.valueOf(dataLength(_data.URO1)) + dataLength(_data.BLD1) + dataLength(_data.BIL1) + dataLength(_data.KET1) + dataLength(_data.GLU1) + dataLength(_data.PRO1) + dataLength(_data.PH1) + dataLength(_data.NIT1) + dataLength(_data.LEU1) + dataLength(_data.SG1) + dataLength(_data.VC1) + dataLength(_data.MAL1) + dataLength(_data.CR1) + dataLength(_data.UCA1);
                        Log.e("当前的数据显示", "==============");
                        Log.e("=================", "==============_value" + _value2);
                        Log.e("=================", "==============_valueadd" + _valueadd2);
                        Log.e("=================", "==============_valuenew" + _valuenew2);
                        Log.e("当前的数据显示", "==============");
                        _content.append("<record><urine><value>");
                        _content.append(_value2);
                        _content.append("</value><valueadd>");
                        _content.append(_valueadd2);
                        _content.append("</valueadd><valuenew>");
                        _content.append(_valuenew2);
                        _content.append("</valuenew></urine>");
                        _content.append("<checktime>");
                        _content.append(_dateTime2);
                        _content.append("</checktime></record>");
                    }
                    UrineDataDaoOperation operation2 = UrineDataDaoOperation.getInstance(App_phms.getInstance().getApplicationContext());
                    if (!Boolean.valueOf(operation2.querySql(_dateTime2)).booleanValue()) {
                        UrineDataDao data2 = new UrineDataDao();
                        data2.mTime = _dateTime2;
                        data2.mValue = value2;
                        data2.mValuedd = valueadd;
                        data2.mValuenew = _valuenew2;
                        data2.mFlag = "0";
                        data2.mUnique = this.mData.mFilePath;
                        operation2.insertUrineDataDao(data2);
                    }
                }
            }
        }
        return _content.toString();
    }

    private String addDataValue(byte mAL) {
        if (mAL == 9) {
            return "9" + mAL;
        }
        return "0" + mAL;
    }

    private String dataLength(int uRO1) {
        if (uRO1 < 10) {
            return "00" + uRO1;
        }
        if (uRO1 >= 10 && uRO1 <= 99) {
            return "0" + uRO1;
        }
        if (uRO1 >= 99) {
            return new StringBuilder().append(uRO1).toString();
        }
        return bs.b;
    }
}
