package com.contec.phms.upload.trend;

import android.util.Base64;
import android.util.Log;
import com.contec.phms.App_phms;
import com.contec.phms.device.template.DeviceData;
import com.contec.phms.manager.device.DeviceManager;
import com.contec.phms.db.localdata.FvcDataDao;
import com.contec.phms.db.localdata.opration.FvcDataDaoOperation;
import com.contec.phms.util.CLog;
import com.contec.phms.util.Constants;
import com.contec.phms.util.JPageUtil;
import com.contec.phms.util.PageUtil;
import com.contec.sp10.code.DeviceDataJar;
import org.apache.commons.httpclient.HttpMethodBase;

public class Sp10Trend_XML extends Trend {
    public DeviceData mDatas;

    public Sp10Trend_XML(DeviceData datas) {
        this.mDatas = datas;
        getContent();
    }

    public HttpMethodBase getMethod() {
        return this.mMethod;
    }

    public String makeContect() {
        StringBuffer _content;
        new StringBuffer();
        if (DeviceManager.m_DeviceBean.getmBluetoothType().equalsIgnoreCase(Constants.DEVICE_BLUEBOOTH_TYPE_CLASSIC)) {
            _content = getBM77Data();
        } else {
            _content = getBleData();
        }
        return _content.toString();
    }

    private StringBuffer getBleData() {
        StringBuffer _content = new StringBuffer();
        if (this.mDatas != null) {
            for (int i = 0; i < this.mDatas.mDataList.size(); i++) {
                DeviceDataJar _data = (DeviceDataJar) this.mDatas.mDataList.get(i);
                if (_data.mPatientInfo.mTime == null) {
                    _data.mPatientInfo.mTime = JPageUtil.getStringTime(this.mDatas.mSaveDate);
                }
                byte[] provalue = PageUtil.compareDate(_data.mPatientInfo.mTime);
                if (provalue != null) {
                    String _dateCompare = PageUtil.process_Date((provalue[0] & 255) + 2000, provalue[1] & 255, provalue[2] & 255, provalue[3] & 255, provalue[4] & 255, provalue[5] & 255);
                    Log.e("测试，校正后的时间:", _dateCompare);
                    _content.append("<record><fvc><fvc>");
                    _content.append(_data.mParamInfo.mFVC * 1000.0d);
                    _content.append("</fvc><fev1>");
                    _content.append(_data.mParamInfo.mFEV1 * 1000.0d);
                    _content.append("</fev1><pef>");
                    _content.append(_data.mParamInfo.mPEF * 1000.0d);
                    _content.append("</pef><fev1rate>");
                    _content.append(_data.mParamInfo.mFEV1Per);
                    _content.append("</fev1rate><fef25>");
                    _content.append(_data.mParamInfo.mFEF25 * 1000.0d);
                    _content.append("</fef25><fef2575> ");
                    _content.append(_data.mParamInfo.mFEF2575 * 1000.0d);
                    _content.append(" </fef2575><fef75>");
                    _content.append(_data.mParamInfo.mFEF75 * 1000.0d);
                    _content.append("</fef75> </fvc><checktime>");
                    _content.append(_dateCompare);
                    _content.append("</checktime></record>");
                    CLog.e("FvcDataDao", "_value" + _data.mParamInfo.mFVC + "_valueadd" + _data.mParamInfo.mPEF + "_valuenew" + _data.mParamInfo.mFEV1);
                    FvcDataDaoOperation operation = FvcDataDaoOperation.getInstance(App_phms.getInstance().getApplicationContext());
                    if (!Boolean.valueOf(operation.querySql(_dateCompare)).booleanValue()) {
                        FvcDataDao data = new FvcDataDao();
                        data.mTime = _dateCompare;
                        data.mFef25 = new StringBuilder(String.valueOf(_data.mParamInfo.mFEF25 * 1000.0d)).toString();
                        data.mFef2575 = new StringBuilder(String.valueOf(_data.mParamInfo.mFEF2575 * 1000.0d)).toString();
                        data.mFef75 = new StringBuilder(String.valueOf(_data.mParamInfo.mFEF75 * 1000.0d)).toString();
                        data.mFev1 = new StringBuilder(String.valueOf(_data.mParamInfo.mFEV1 * 1000.0d)).toString();
                        data.mFev1Rate = new StringBuilder(String.valueOf(_data.mParamInfo.mFEV1Per)).toString();
                        data.mFvc = new StringBuilder(String.valueOf(_data.mParamInfo.mFVC * 1000.0d)).toString();
                        data.mPef = new StringBuilder(String.valueOf(_data.mParamInfo.mPEF * 1000.0d)).toString();
                        data.mUnique = this.mDatas.mFileName;
                        data.mFlag = "0";
                        operation.insertFvcDao(data);
                    }
                } else {
                    Log.e("测试，时间正确:", _data.mPatientInfo.mTime);
                    _content.append("<record><fvc><fvc>");
                    _content.append(_data.mParamInfo.mFVC * 1000.0d);
                    _content.append("</fvc><fev1>");
                    _content.append(_data.mParamInfo.mFEV1 * 1000.0d);
                    _content.append("</fev1><pef>");
                    _content.append(_data.mParamInfo.mPEF * 1000.0d);
                    _content.append("</pef><fev1rate>");
                    _content.append(_data.mParamInfo.mFEV1Per);
                    _content.append("</fev1rate><fef25>");
                    _content.append(_data.mParamInfo.mFEF25 * 1000.0d);
                    _content.append("</fef25><fef2575> ");
                    _content.append(_data.mParamInfo.mFEF2575 * 1000.0d);
                    _content.append(" </fef2575><fef75>");
                    _content.append(_data.mParamInfo.mFEF75 * 1000.0d);
                    _content.append("</fef75> </fvc><checktime>");
                    _content.append(_data.mPatientInfo.mTime);
                    _content.append("</checktime></record>");
                    CLog.e("FvcDataDao", "_value" + _data.mParamInfo.mFVC + "_valueadd" + _data.mParamInfo.mPEF + "_valuenew" + _data.mParamInfo.mFEV1);
                    FvcDataDaoOperation operation2 = FvcDataDaoOperation.getInstance(App_phms.getInstance().getApplicationContext());
                    if (!Boolean.valueOf(operation2.querySql(_data.mPatientInfo.mTime)).booleanValue()) {
                        FvcDataDao data2 = new FvcDataDao();
                        data2.mTime = _data.mPatientInfo.mTime;
                        data2.mFef25 = new StringBuilder(String.valueOf(_data.mParamInfo.mFEF25 * 1000.0d)).toString();
                        data2.mFef2575 = new StringBuilder(String.valueOf(_data.mParamInfo.mFEF2575 * 1000.0d)).toString();
                        data2.mFef75 = new StringBuilder(String.valueOf(_data.mParamInfo.mFEF75 * 1000.0d)).toString();
                        data2.mFev1 = new StringBuilder(String.valueOf(_data.mParamInfo.mFEV1 * 1000.0d)).toString();
                        data2.mFev1Rate = new StringBuilder(String.valueOf(_data.mParamInfo.mFEV1Per)).toString();
                        data2.mFvc = new StringBuilder(String.valueOf(_data.mParamInfo.mFVC)).toString();
                        data2.mPef = new StringBuilder(String.valueOf(_data.mParamInfo.mPEF * 1000.0d)).toString();
                        data2.mUnique = this.mDatas.mFileName;
                        data2.mFlag = "0";
                        operation2.insertFvcDao(data2);
                    }
                }
            }
        }
        return _content;
    }

    private StringBuffer getBM77Data() {
        StringBuffer _content = new StringBuffer();
        if (this.mDatas != null) {
            for (int i = 0; i < this.mDatas.mDataList.size(); i++) {
                cn.com.contec.jar.sp10w.DeviceDataJar _data = (cn.com.contec.jar.sp10w.DeviceDataJar) this.mDatas.mDataList.get(i);
                if (_data.mPatientInfo.mTime == null) {
                    _data.mPatientInfo.mTime = JPageUtil.getStringTime(this.mDatas.mSaveDate);
                }
                byte[] provalue = PageUtil.compareDate(_data.mPatientInfo.mTime);
                if (provalue != null) {
                    String _dateCompare = PageUtil.process_Date((provalue[0] & 255) + 2000, provalue[1] & 255, provalue[2] & 255, provalue[3] & 255, provalue[4] & 255, provalue[5] & 255);
                    Log.e("测试，校正后的时间:", _dateCompare);
                    _content.append("<record><fvc><fvc>");
                    _content.append(_data.mParamInfo.mFVC * 1000.0d);
                    _content.append("</fvc><fev1>");
                    _content.append(_data.mParamInfo.mFEV1 * 1000.0d);
                    _content.append("</fev1><pef>");
                    _content.append(_data.mParamInfo.mPEF * 1000.0d);
                    _content.append("</pef><fev1rate>");
                    _content.append(_data.mParamInfo.mFEV1Per);
                    _content.append("</fev1rate><fef25>");
                    _content.append(_data.mParamInfo.mFEF25 * 1000.0d);
                    _content.append("</fef25><fef2575> ");
                    _content.append(_data.mParamInfo.mFEF2575 * 1000.0d);
                    _content.append(" </fef2575><fef75>");
                    _content.append(_data.mParamInfo.mFEF75 * 1000.0d);
                    _content.append("</fef75> </fvc><checktime>");
                    _content.append(_dateCompare);
                    _content.append("</checktime></record>");
                    CLog.e("FvcDataDao", "_value" + _data.mParamInfo.mFVC + "_valueadd" + _data.mParamInfo.mPEF + "_valuenew" + _data.mParamInfo.mFEV1);
                    FvcDataDaoOperation operation = FvcDataDaoOperation.getInstance(App_phms.getInstance().getApplicationContext());
                    if (!Boolean.valueOf(operation.querySql(_dateCompare)).booleanValue()) {
                        FvcDataDao data = new FvcDataDao();
                        data.mTime = _dateCompare;
                        data.mFef25 = new StringBuilder(String.valueOf(_data.mParamInfo.mFEF25 * 1000.0d)).toString();
                        data.mFef2575 = new StringBuilder(String.valueOf(_data.mParamInfo.mFEF2575 * 1000.0d)).toString();
                        data.mFef75 = new StringBuilder(String.valueOf(_data.mParamInfo.mFEF75 * 1000.0d)).toString();
                        data.mFev1 = new StringBuilder(String.valueOf(_data.mParamInfo.mFEV1 * 1000.0d)).toString();
                        data.mFev1Rate = new StringBuilder(String.valueOf(_data.mParamInfo.mFEV1Per)).toString();
                        data.mFvc = new StringBuilder(String.valueOf(_data.mParamInfo.mFVC * 1000.0d)).toString();
                        data.mPef = new StringBuilder(String.valueOf(_data.mParamInfo.mPEF * 1000.0d)).toString();
                        data.mUnique = this.mDatas.mFileName;
                        data.mFlag = "0";
                        operation.insertFvcDao(data);
                    }
                } else {
                    Log.e("测试，时间正确:", _data.mPatientInfo.mTime);
                    _content.append("<record><fvc><fvc>");
                    _content.append(_data.mParamInfo.mFVC * 1000.0d);
                    _content.append("</fvc><fev1>");
                    _content.append(_data.mParamInfo.mFEV1 * 1000.0d);
                    _content.append("</fev1><pef>");
                    _content.append(_data.mParamInfo.mPEF * 1000.0d);
                    _content.append("</pef><fev1rate>");
                    _content.append(_data.mParamInfo.mFEV1Per);
                    _content.append("</fev1rate><fef25>");
                    _content.append(_data.mParamInfo.mFEF25 * 1000.0d);
                    _content.append("</fef25><fef2575> ");
                    _content.append(_data.mParamInfo.mFEF2575 * 1000.0d);
                    _content.append(" </fef2575><fef75>");
                    _content.append(_data.mParamInfo.mFEF75 * 1000.0d);
                    _content.append("</fef75> </fvc><checktime>");
                    _content.append(_data.mPatientInfo.mTime);
                    _content.append("</checktime></record>");
                    CLog.e("FvcDataDao", "_value" + _data.mParamInfo.mFVC + "_valueadd" + _data.mParamInfo.mPEF + "_valuenew" + _data.mParamInfo.mFEV1);
                    FvcDataDaoOperation operation2 = FvcDataDaoOperation.getInstance(App_phms.getInstance().getApplicationContext());
                    if (!Boolean.valueOf(operation2.querySql(_data.mPatientInfo.mTime)).booleanValue()) {
                        FvcDataDao data2 = new FvcDataDao();
                        data2.mTime = _data.mPatientInfo.mTime;
                        data2.mFef25 = new StringBuilder(String.valueOf(_data.mParamInfo.mFEF25 * 1000.0d)).toString();
                        data2.mFef2575 = new StringBuilder(String.valueOf(_data.mParamInfo.mFEF2575 * 1000.0d)).toString();
                        data2.mFef75 = new StringBuilder(String.valueOf(_data.mParamInfo.mFEF75 * 1000.0d)).toString();
                        data2.mFev1 = new StringBuilder(String.valueOf(_data.mParamInfo.mFEV1 * 1000.0d)).toString();
                        data2.mFev1Rate = new StringBuilder(String.valueOf(_data.mParamInfo.mFEV1Per)).toString();
                        data2.mFvc = new StringBuilder(String.valueOf(_data.mParamInfo.mFVC)).toString();
                        data2.mPef = new StringBuilder(String.valueOf(_data.mParamInfo.mPEF * 1000.0d)).toString();
                        data2.mUnique = this.mDatas.mFileName;
                        data2.mFlag = "0";
                        operation2.insertFvcDao(data2);
                    }
                }
            }
        }
        return _content;
    }

    public String encodeBASE64(byte[] pack) {
        return Base64.encodeToString(pack, 0);
    }
}
