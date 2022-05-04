package com.contec.phms.upload;

import android.os.Message;
import android.util.Log;
import com.contec.cms50dj_jar.DeviceData50DJ_Jar;
import com.contec.cms50dj_jar.DeviceDataPedometerJar;
import com.contec.phms.App_phms;
import com.contec.phms.device.template.DeviceData;
import com.contec.phms.manager.device.DeviceManager;
import com.contec.phms.db.LoginUserDao;
import com.contec.phms.db.localdata.Spo2DataDao;
import com.contec.phms.upload.cases.Abpm50Case;
import com.contec.phms.upload.cases.CMS50KCase;
import com.contec.phms.upload.cases.Contec8000GW_Case;
import com.contec.phms.upload.cases.FHR01Case;
import com.contec.phms.upload.cases.PM10Case;
import com.contec.phms.upload.cases.PM85Case;
import com.contec.phms.upload.cases.Sp10wCase;
import com.contec.phms.upload.cases.Spo2Case;
import com.contec.phms.upload.cases.common.NEW_CASE;
import com.contec.phms.upload.trend.BC01Trend_XML;
import com.contec.phms.upload.trend.Cms50DJ_Aliyun_Oxygen;
import com.contec.phms.upload.trend.Cms50DJ_Aliyun_PedometerDay;
import com.contec.phms.upload.trend.Cms50DJ_PedometerDay_Trend;
import com.contec.phms.upload.trend.Cms50DJ_PedometerMin_Trend;
import com.contec.phms.upload.trend.Cms50DJ_Trend;
import com.contec.phms.upload.trend.Cms50K_PedometerDay_Trend;
import com.contec.phms.upload.trend.Cms50K_PedometerMin_Trend;
import com.contec.phms.upload.trend.Cms50ewTrend_Aliyun;
import com.contec.phms.upload.trend.Cms50ew_08A_Trend;
import com.contec.phms.upload.trend.Cms50ew_Trend;
import com.contec.phms.upload.trend.CmssxtTrend_XML;
import com.contec.phms.upload.trend.Contec08aTrend_Aliyun;
import com.contec.phms.upload.trend.Contec08aTrend_XML;
import com.contec.phms.upload.trend.Fhr01Trend;
import com.contec.phms.upload.trend.Fhr01Trend_Aliyun;
import com.contec.phms.upload.trend.HC06Trend_XML;
import com.contec.phms.upload.trend.PM10Trend_XML;
import com.contec.phms.upload.trend.Sp10Trend_XML;
import com.contec.phms.upload.trend.WTTrend_XML;
import com.contec.phms.util.CLog;
import com.contec.phms.util.Constants;
import com.contec.phms.util.DeviceNameUtils;
import com.contec.phms.util.FileOperation;
import com.contec.phms.util.GetCurrentTimeMillis;
import com.contec.phms.util.PageUtil;
import com.contec.phms.util.PhmsSharedPreferences;
import com.taobao.tae.sdk.TopComponent;
import com.taobao.tae.sdk.model.Result;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import org.apache.commons.httpclient.HttpMethodBase;

import cn.com.contec_net_3_android.Method_android_new_case;
import cn.com.contec_net_3_android.Method_android_upload_case;
import cn.com.contec_net_3_android.Method_android_upload_trend;
import u.aly.bs;

public class UploadTask {
    public final String TAG = "UploadTask";
    public String _age = bs.b;
    public String _height = bs.b;
    public LoginUserDao _loginUserInfo;
    public String _otherLanauage;
    public String _phone;
    public String _pid = bs.b;
    public String _sex = bs.b;
    public String _sp10Language;
    public String _weight = bs.b;
    private long difference;
    public HashMap<String, String> hm = null;
    public NEW_CASE mCase;
    public DeviceData mData = null;
    public ArrayList<DeviceData> mList = null;
    public HttpMethodBase mMethod;

    public UploadTask(DeviceData data) {
        this.mData = data;
    }

    public UploadTask(ArrayList<DeviceData> list) {
        this.mList = list;
    }

    public boolean execute() {
        PageUtil.checkUserinfo(App_phms.getInstance().mUserInfo, App_phms.getInstance().getApplicationContext());
        if (this.mList != null) {
            this.mData = this.mList.get(0);
        }
        if (DeviceManager.mDeviceBeanList != null && !this.mData.mDataType.equalsIgnoreCase("sp10w")) {
            DeviceManager.mDeviceBeanList.mState = 7;
            DeviceManager.m_DeviceBean.mState = 7;
            App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(DeviceManager.m_DeviceBean);
        } else if (this.mData != null && this.mData.mDataType.equalsIgnoreCase("sp10w")) {
            DeviceManager.mDeviceBeanList.mState = 7;
            Constants.SP10W_UPLOAD_PROCESS += 50;
            DeviceManager.m_DeviceBean.mProgress = Constants.SP10W_UPLOAD_PROCESS;
            DeviceManager.m_DeviceBean.mState = 7;
            App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(DeviceManager.m_DeviceBean);
        }
        CLog.e("-------------GotYou-------------", this.mData.mUploadType);
        if (this.mData.mUploadType.equals("trend")) {
            String _content = null;
            if (this.mData.mDataType.equals("ECG(PM10)") || this.mData.mDataType.equals("ECG(CMS50K)") || this.mData.mDataType.equals("ECG(CMS50K1)")) {
                _content = new PM10Trend_XML(this.mData).mContent;
                CLog.e("GotYou", "Upload ECG(PM10) Trend");
            } else if (this.mData.mDataType.equals("bc01")) {
                CLog.e("GotYou", "Upload bc01 Trend");
                _content = new BC01Trend_XML(this.mData).mContent;
                CLog.e("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "AAAAAAAAAAAAAAAAAAA");
                CLog.e("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", _content);
                CLog.e("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "AAAAAAAAAAAAAAAAAAA");
            } else if (this.mData.mDataType.equals("wt")) {
                CLog.e("GotYou", "Upload WT Trend");
                _content = new WTTrend_XML(this.mData).mContent;
            } else if (this.mData.mDataType.equals("cmssxt")) {
                CLog.e("GotYou", "Upload CMSSXT Trend");
                _content = new CmssxtTrend_XML(this.mData).mContent;
            } else if (this.mData.mDataType.equals("contec08aw")) {
                CLog.e("GotYou", "Upload contec08aw =====");
                _content = new Contec08aTrend_XML(this.mData).mContent;
            } else if (this.mData.mDataType.equals(DeviceNameUtils.ABPM50W)) {
                CLog.e("GotYou", "Upload abpm50w =====");
                _content = new Contec08aTrend_XML(this.mData).mContent;
            } else if (this.mData.mDataType.equals(DeviceNameUtils.PM50)) {
                CLog.e("GotYou", "Upload pm50 =====");
                _content = new Contec08aTrend_XML(this.mData).mContent;
            } else if (this.mData.mDataType.equals(Spo2DataDao.SPO2)) {
                CLog.e("GotYou", "Upload spo2 =====");
                _content = new Cms50ew_Trend(this.mData).mContent;
            } else if (this.mData.mDataType.equalsIgnoreCase("FHR01")) {
                _content = new Fhr01Trend(this.mData).mContent;
                CLog.e("GotYou", "Upload FHR01 =====");
            } else if (this.mData.mDataType.equals("contec08spo2")) {
                _content = new Cms50ew_08A_Trend(this.mList).mContent;
                CLog.e("GotYou", "Upload contec08spo2 =====");
            } else if (this.mData.mDataType.equalsIgnoreCase("sp10w")) {
                _content = new Sp10Trend_XML(this.mData).mContent;
                CLog.e("GotYou", "Upload sp10w =====");
            } else if (this.mData.mDataType.equalsIgnoreCase("sp0208")) {
                _content = new Cms50DJ_Trend(this.mData).mContent;
                CLog.e("GotYou", "Upload sp0208 =====");
            } else if (this.mData.mDataType.equalsIgnoreCase("pedometerDay")) {
                _content = new Cms50DJ_PedometerDay_Trend(this.mData).mContent;
                CLog.e("GotYou", "Upload pedometerDay =====");
            } else if (this.mData.mDataType.equalsIgnoreCase("pedometerMin")) {
                _content = new Cms50DJ_PedometerMin_Trend(this.mData).mContent;
                CLog.e("GotYou", "Upload pedometerMin =====");
            } else if (this.mData.mDataType.equalsIgnoreCase("temperature")) {
                _content = new HC06Trend_XML(this.mData).mContent;
                CLog.e("GotYou", "Upload temperature =====");
            } else if (this.mData.mDataType.equalsIgnoreCase("pedometerDayK")) {
                _content = new Cms50K_PedometerDay_Trend(this.mData).mContent;
                CLog.e("GotYou", "Upload pedometerDayK =====");
            } else if (this.mData.mDataType.equalsIgnoreCase("pedometerMinK")) {
                _content = new Cms50K_PedometerMin_Trend(this.mData).mContent;
                CLog.e("GotYou", "Upload pedometerMinK =====");
            }
            if (this.mData.mDataType.equals("cmssxt") || this.mData.mDataType.equalsIgnoreCase("sp10w") || this.mData.mDataType.equalsIgnoreCase("wt") || this.mData.mDataType.equalsIgnoreCase("contec08aw") || this.mData.mDataType.equalsIgnoreCase("bc01") || this.mData.mDataType.equalsIgnoreCase("temperature") || this.mData.mDataType.equalsIgnoreCase("ECG(PM10)") || this.mData.mDataType.equalsIgnoreCase("ECG(CMS50K)") || this.mData.mDataType.equalsIgnoreCase("ECG(CMS50K1)") || this.mData.mDataType.equalsIgnoreCase(DeviceNameUtils.ABPM50W) || this.mData.mDataType.equalsIgnoreCase(DeviceNameUtils.PM50)) {
                return uploadTrend(_content, true);
            }
            return uploadTrend(_content, false);
        } else if (!this.mData.mUploadType.equals("case")) {
            return false;
        } else {
            if (DeviceManager.mDeviceBeanList != null && !this.mData.mDataType.equalsIgnoreCase("sp10w")) {
                App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(DeviceManager.m_DeviceBean);
            }
            if (this.mData.mDataType.equals(Spo2DataDao.SPO2)) {
                this.mCase = new Spo2Case(this.mData).process();
            } else if (this.mData.mDataType.equals(DeviceNameUtils.ABPM50W)) {
                this.mCase = new Abpm50Case(this.mData).process();
            } else if (this.mData.mDataType.equals("pm85")) {
                this.mCase = new PM85Case(this.mData).process();
            } else if (this.mData.mDataType.equals("FHR01")) {
                this.mCase = new FHR01Case(this.mData).process();
            } else if (this.mData.mDataType.equals("cms50k") || this.mData.mDataType.equalsIgnoreCase("ECG(CMS50K)") || this.mData.mDataType.equalsIgnoreCase("ECG(CMS50K1)")) {
                if (is50KContinuityOxygenData(this.mData)) {
                    this.mCase = new CMS50KCase(this.mData).process();
                } else {
                    this.mCase = new PM10Case(this.mData).process();
                }
            } else if (this.mData.mDataType.equalsIgnoreCase("sp10w")) {
                Constants.SP10W_UPLOAD_PROCESS += 10;
                DeviceManager.m_DeviceBean.mProgress = Constants.SP10W_UPLOAD_PROCESS;
                App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(DeviceManager.m_DeviceBean);
                this.mCase = new Sp10wCase(this.mData).process();
            } else if (this.mData.mDataType.equalsIgnoreCase("ECG(PM10)")) {
                this.mCase = new PM10Case(this.mData).process();
            } else if (this.mData.mDataType.equalsIgnoreCase("contec8000gw")) {
                this.mCase = new Contec8000GW_Case(this.mData).process();
            }
            boolean _result = uploadCase();
            if (_result && (this.mData.mDataType.equalsIgnoreCase("pm85") || this.mData.mDataType.equalsIgnoreCase(DeviceNameUtils.ABPM50W) || this.mData.mDataType.equalsIgnoreCase(Spo2DataDao.SPO2))) {
                Message msgs = new Message();
                msgs.what = Constants.V_SHOW_NOTIFICATION_INSTANT_MESSAGE_LOCAL_CASE;
                msgs.arg2 = 11;
                msgs.obj = this.mData;
                App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(msgs);
            }
            LoginUserDao _userinfo = PageUtil.getLoginUserInfo();
            if (!_result || _userinfo.mCardType == null || _userinfo.mCardType.equals("2")) {
                return _result;
            }
            if ((!this.mData.mDataType.equalsIgnoreCase("abpm50w") && !this.mData.mDataType.equalsIgnoreCase("pm85") && !this.mData.mDataType.equalsIgnoreCase(Spo2DataDao.SPO2)) || !Constants.NOVIP_NOTICE) {
                return _result;
            }
            Constants.NOVIP_NOTICE = false;
            Message msgs2 = new Message();
            msgs2.what = Constants.Mark_NOVIP;
            msgs2.arg2 = 13;
            App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(msgs2);
            return _result;
        }
    }

    private boolean uploadTrend(String _content, boolean pifxmlupload) {
        String _result_trend = null;
        boolean _result = false;
        String _caseid = bs.b;
        if (this.mData.mDataType.equalsIgnoreCase("ECG(PM10)") || this.mData.mDataType.equalsIgnoreCase("ECG(CMS50K)") || this.mData.mDataType.equalsIgnoreCase("ECG(CMS50K1)")) {
            _caseid = ((com.contec.phms.device.pm10.DeviceData) this.mData).mCaseID;
        }
        LoginUserDao _userinfo = PageUtil.getLoginUserInfo();
        if (pifxmlupload) {
            try {
                _result_trend = Method_android_upload_trend.upLoadThrendOne(_userinfo.mSID, _userinfo.mSenderId, _userinfo.mUID, App_phms.getInstance().mUserInfo.mPassword, String.valueOf(Constants.URL) + "/main.php", _content, _caseid);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            try {
                _result_trend = Method_android_upload_trend.upLoadThrendTwo(_userinfo.mSID, _userinfo.mUID, App_phms.getInstance().mUserInfo.mPassword, _content, String.valueOf(Constants.URL) + "/main.php");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (_result_trend.length() < 40) {
            return false;
        }
        String _code = _result_trend.substring(34, 40);
        if (_code.equals(Constants.SUCCESS)) {
            _result = true;
            if (Constants.IS_SUCCESS) {
                getAliyunUploadContent(this.mData);
            }
        } else {
            if (_result_trend.contains("the sessionid you passed is invalid")) {
                Message msgs = new Message();
                msgs.what = Constants.V_RELOGIN_INBACKGROUND;
                msgs.arg2 = 1;
                App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(msgs);
            } else if (_code.equals(Constants.LOGIN_IN_ANOTHER_PLACE)) {
                Message msgs2 = new Message();
                msgs2.what = Constants.Login_In_Another_Place;
                msgs2.arg2 = 1;
                App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(msgs2);
            } else if (!_code.equals(Constants.CARD_USE_NUM_EXPIRED)) {
                _code.equals(Constants.PASSED_VALIDITY);
            }
            _result = false;
        }
        return _result;
    }

    private boolean uploadCase() {
        boolean _result;
        LoginUserDao _userinfo = PageUtil.getLoginUserInfo();
        int _cardType = 0;
        StringBuffer _content = new StringBuffer();
        com.contec.phms.device.pm10.DeviceData _dataPM10 = null;
        if (this.mData.mDataType.equalsIgnoreCase("ECG(PM10)")) {
            String type = PhmsSharedPreferences.getInstance(App_phms.getInstance().getApplicationContext()).getString("Electrocardiogram", "auto");
            if ("auto".equals(type)) {
                _cardType = 1;
            } else if ("doctor".equals(type)) {
                _cardType = 0;
            }
            CLog.e("UploadTask", "type的值为:" + type + " _cardType的值为：" + _cardType);
            _dataPM10 = (com.contec.phms.device.pm10.DeviceData) this.mData;
            byte[] _temp = _dataPM10.TrendData;
            String _time = (_temp[0] + 2000) + "-" + ((int) _temp[1]) + "-" + ((int) _temp[2]) + " " + ((int) _temp[3]) + ":" + ((int) _temp[4]) + ":" + ((int) _temp[5]);
            Date dDate = null;
            String collectTime = new GetCurrentTimeMillis(_temp[0], _temp[1], _temp[2], _temp[3], _temp[4], _temp[5]).getTime();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
            try {
                this.difference = (System.currentTimeMillis() / 1000) - (dateFormat.parse(collectTime).getTime() / 1000);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if (this.difference < -2592000 || this.difference > 31536000) {
                this.mData.mCheckTimeIllegal = true;
                return false;
            }
            SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
                dDate = format2.parse(_time);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            baseinfo();
            this._sp10Language = "Chinese";
            if (Constants.Language.contains("en")) {
                this._sp10Language = "English";
            } else if (Constants.Language.contains("zh")) {
                this._sp10Language = "Chinese";
            }
            int _pluse = (((_temp[6] & 255) << 7) | (_temp[7] & 255)) & 2047;
            String _pm10result = new StringBuilder().append((int) _temp[8]).append((int) _temp[9]).append((int) _temp[10]).append((int) _temp[11]).append((int) _temp[12]).append((int) _temp[13]).append((int) _temp[14]).append((int) _temp[15]).append((int) _temp[16]).append((int) _temp[17]).append((int) _temp[18]).append((int) _temp[19]).append((int) _temp[20]).append((int) _temp[21]).toString();
            String reTime = format2.format(dDate);
            _content.append("&lt;?xml version=\"1.0\" encoding=\"GBK\"?&gt;&lt;otherparam&gt;");
            _content.append("&lt;heartrate&gt;");
            _content.append(new StringBuilder().append(_pluse).toString());
            _content.append("&lt;/heartrate&gt;&lt;feature&gt;");
            _content.append(_pm10result);
            _content.append("&lt;/feature&gt;&lt;checktime&gt;");
            _content.append(reTime);
            _content.append("&lt;/checktime&gt;");
            _content.append("&lt;caseName&gt;");
            _content.append(this._loginUserInfo.mUserName);
            _content.append("&lt;/caseName&gt;");
            _content.append("&lt;age&gt;");
            _content.append(this._age);
            _content.append("&lt;/age&gt;");
            _content.append("&lt;height&gt;");
            _content.append(this._height);
            _content.append("&lt;/height&gt;");
            _content.append("&lt;weight&gt;");
            _content.append(this._weight);
            _content.append("&lt;/weight&gt;");
            _content.append("&lt;sex&gt;");
            _content.append(this._sex);
            _content.append("&lt;/sex&gt;");
            _content.append("&lt;language&gt;");
            _content.append(this._sp10Language);
            _content.append("&lt;/language&gt;");
            _content.append("&lt;/otherparam&gt;");
        } else if (this.mData.mDataType.equalsIgnoreCase("cms50k") || this.mData.mDataType.equalsIgnoreCase("ECG(CMS50K)") || this.mData.mDataType.equalsIgnoreCase("ECG(CMS50K1)")) {
            String type2 = PhmsSharedPreferences.getInstance(App_phms.getInstance().getApplicationContext()).getString("cms50k_card_set", "auto");
            _dataPM10 = (com.contec.phms.device.pm10.DeviceData) this.mData;
            if ("auto".equals(type2)) {
                _cardType = 1;
            } else if ("doctor".equals(type2)) {
                _cardType = 0;
            }
            baseinfo();
            if (this._loginUserInfo.mPID != null && !this._loginUserInfo.mPID.equalsIgnoreCase(bs.b)) {
                this._pid = new StringBuilder().append(Long.parseLong(this._loginUserInfo.mPID)).toString();
            }
            if (this._loginUserInfo.mPhone != null && !this._loginUserInfo.mPhone.equalsIgnoreCase(bs.b)) {
                this._phone = new StringBuilder().append(Long.parseLong(this._loginUserInfo.mPhone)).toString();
            }
            CLog.e("******************************", "&&&&&&&&&&&&&&&&&&&&&");
            CLog.e("******************************", this._phone);
            CLog.e("******************************", "&&&&&&&&&&&&&&&&&&&&&");
            this._otherLanauage = "chinese";
            if (Constants.Language.contains("en")) {
                this._otherLanauage = "english";
            } else if (Constants.Language.contains("zh")) {
                this._otherLanauage = "chinese";
            }
            byte[] _temp2 = _dataPM10.TrendData;
            int _pluse2 = (((_temp2[6] & 255) << 7) | (_temp2[7] & 255)) & 2047;
            String _pm10result2 = new StringBuilder().append((int) _temp2[8]).append((int) _temp2[9]).append((int) _temp2[10]).append((int) _temp2[11]).append((int) _temp2[12]).append((int) _temp2[13]).append((int) _temp2[14]).append((int) _temp2[15]).append((int) _temp2[16]).append((int) _temp2[17]).append((int) _temp2[18]).append((int) _temp2[19]).append((int) _temp2[20]).append((int) _temp2[21]).toString();
            String _time2 = (_temp2[0] + 2000) + "-" + ((int) _temp2[1]) + "-" + ((int) _temp2[2]) + " " + ((int) _temp2[3]) + ":" + ((int) _temp2[4]) + ":" + ((int) _temp2[5]);
            Date dDate2 = null;
            SimpleDateFormat format22 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
                dDate2 = format22.parse(_time2);
            } catch (ParseException e2) {
                e2.printStackTrace();
            }
            String reTime2 = format22.format(dDate2);
            _content.append("&lt;?xml version=\"1.0\" encoding=\"GBK\"?&gt;&lt;otherparam&gt;");
            _content.append("&lt;heartrate&gt;");
            _content.append(new StringBuilder().append(_pluse2).toString());
            _content.append("&lt;/heartrate&gt;&lt;feature&gt;");
            _content.append(_pm10result2);
            _content.append("&lt;/feature&gt;&lt;checktime&gt;");
            _content.append(reTime2);
            _content.append("&lt;/checktime&gt;");
            _content.append("&lt;pid&gt;");
            _content.append(this._pid);
            _content.append("&lt;/pid&gt;&lt;caseName&gt;");
            _content.append(this._loginUserInfo.mUserName);
            _content.append("&lt;/caseName&gt;");
            _content.append("&lt;age&gt;");
            _content.append(this._age);
            _content.append("&lt;/age&gt;");
            _content.append("&lt;height&gt;");
            _content.append(this._height);
            _content.append("&lt;/height&gt;");
            _content.append("&lt;weight&gt;");
            _content.append(this._weight);
            _content.append("&lt;/weight&gt;");
            _content.append("&lt;sex&gt;");
            _content.append(this._sex);
            _content.append("&lt;/sex&gt;");
            _content.append("&lt;caseAddr&gt;");
            _content.append(this._loginUserInfo.mAddress);
            _content.append("&lt;/caseAddr&gt;");
            _content.append("&lt;phone&gt;");
            _content.append(this._phone);
            _content.append("&lt;/phone&gt;");
            _content.append("&lt;newtype&gt;");
            _content.append("1");
            _content.append("&lt;/newtype&gt;");
            _content.append("&lt;language&gt;");
            _content.append(this._otherLanauage);
            _content.append("&lt;/language&gt;");
            _content.append("&lt;/otherparam&gt;");
        } else {
            baseinfo();
            if (this._loginUserInfo.mPID != null && !this._loginUserInfo.mPID.equalsIgnoreCase(bs.b)) {
                this._pid = new StringBuilder().append(Long.parseLong(this._loginUserInfo.mPID)).toString();
            }
            if (this._loginUserInfo.mPhone != null && !this._loginUserInfo.mPhone.equalsIgnoreCase(bs.b)) {
                this._phone = new StringBuilder().append(Long.parseLong(this._loginUserInfo.mPhone)).toString();
            }
            CLog.e("******************************", "&&&&&&&&&&&&&&&&&&&&&");
            CLog.e("******************************", this._phone);
            CLog.e("******************************", "&&&&&&&&&&&&&&&&&&&&&");
            this._otherLanauage = "chinese";
            if (Constants.Language.contains("en")) {
                this._otherLanauage = "english";
            } else if (Constants.Language.contains("zh")) {
                this._otherLanauage = "chinese";
            }
            _content.append("&lt;?xml version=\"1.0\" encoding=\"GBK\"?&gt;&lt;otherparam&gt;");
            _content.append("&lt;pid&gt;");
            _content.append(this._pid);
            _content.append("&lt;/pid&gt;&lt;caseName&gt;");
            _content.append("username");
            _content.append("&lt;/caseName&gt;");
            _content.append("&lt;age&gt;");
            _content.append(this._age);
            _content.append("&lt;/age&gt;");
            _content.append("&lt;height&gt;");
            _content.append(this._height);
            _content.append("&lt;/height&gt;");
            _content.append("&lt;weight&gt;");
            _content.append(this._weight);
            _content.append("&lt;/weight&gt;");
            _content.append("&lt;sex&gt;");
            _content.append(this._sex);
            _content.append("&lt;/sex&gt;");
            _content.append("&lt;caseAddr&gt;");
            _content.append(this._loginUserInfo.mAddress);
            _content.append("&lt;/caseAddr&gt;");
            _content.append("&lt;phone&gt;");
            _content.append(this._phone);
            _content.append("&lt;/phone&gt;");
            _content.append("&lt;language&gt;");
            _content.append(this._otherLanauage);
            _content.append("&lt;/language&gt;");
            _content.append("&lt;/otherparam&gt;");
        }
        if (this.mData.mDataType.equalsIgnoreCase("contec8000gw")) {
            String reTime3 = this.mCase.getStrPaceTime();
            process8000GW(_content, reTime3);
        }
        String _responedResult = Method_android_new_case.getCaseID(_userinfo.mSID, _userinfo.mHospitalName, _userinfo.mHospitalID, _userinfo.mUserName, this.mCase.getCaseName(), new StringBuilder(String.valueOf(this.mCase.getCaseType())).toString(), _userinfo.mUID, App_phms.getInstance().mUserInfo.mPassword, this.mCase.getStrPaceTime(), App_phms.getInstance().mUserInfo.mSex, String.valueOf(Constants.URL) + "/main.php", new StringBuilder().append(_cardType).toString(), _content.toString(), this.mCase.getStrCasePath(), null, null);
        CLog.i("UploadTask", "_responedResult:" + _responedResult);
        if (_responedResult.length() < 40) {
            return false;
        }
        String _code = _responedResult.substring(34, 40);
        CLog.e("UploadTask", "new  case  return code：" + _code + "   Constants.URL: " + Constants.URL + "/main.php");
        if (_code.equals(Constants.SUCCESS)) {
            String mCaseID = _responedResult.toString().substring(42, 61);
            Method_android_upload_case mUpload = new Method_android_upload_case(null, this.mCase.getStrCasePath(), App_phms.getInstance().mUserInfo.mPHPSession, mCaseID, App_phms.getInstance().mUserInfo.mUserID, App_phms.getInstance().mUserInfo.mPassword, String.valueOf(Constants.URL) + "/main.php");
            Log.e("=============", "==================");
            Log.e("获取当前的sessionID", "SessionID值为：：" + App_phms.getInstance().mUserInfo.mPHPSession);
            Log.e("=============", "==================");
            String _uploadreslut = mUpload.init();
            CLog.e("UploadTask", "++++++++++++++++++++++: " + _uploadreslut);
            if (_uploadreslut == null || _uploadreslut.length() <= 40) {
                _result = false;
            } else {
                String _uploadCode = _uploadreslut.substring(34, 40);
                if (_uploadCode == null || _uploadCode.length() <= 1) {
                    _result = false;
                } else if (!_uploadCode.equals(Constants.SUCCESS)) {
                    _result = false;
                } else if (!this.mData.mDataType.equalsIgnoreCase("ECG(PM10)") && !this.mData.mDataType.equalsIgnoreCase("ECG(CMS50K)") && !this.mData.mDataType.equalsIgnoreCase("ECG(CMS50K1)")) {
                    _result = true;
                } else if (!is50KContinuityOxygenData(this.mData)) {
                    CLog.i("UploadTask", "---心电数据----");
                    _dataPM10.mCaseID = mCaseID;
                    String _p = new PM10Trend_XML(this.mData).mContent;
                    _result = uploadTrend(_p, true);
                } else {
                    CLog.i("UploadTask", "-----连续血氧数据---");
                    _result = true;
                }
            }
            FileOperation.clearTempFiles();
        } else {
            if (_responedResult.contains("the sessionid you passed is invalid")) {
                Message msgs = new Message();
                msgs.what = Constants.V_RELOGIN_INBACKGROUND;
                msgs.arg2 = 1;
                App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(msgs);
            } else if (_code.equals(Constants.LOGIN_IN_ANOTHER_PLACE)) {
                Message msgs2 = new Message();
                msgs2.what = Constants.Login_In_Another_Place;
                msgs2.arg2 = 1;
                App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(msgs2);
            } else if (!_code.equals(Constants.CARD_USE_NUM_EXPIRED)) {
                _code.equals(Constants.PASSED_VALIDITY);
            }
            _result = false;
            FileOperation.clearTempFiles();
        }
        CLog.e("UploadTask", " 得到新病例id  mCase.getCaseType():" + this.mCase.getCaseType() + " mCase.getCaseName():" + this.mCase.getCaseName() + "_userinfo.mUserName:" + _userinfo.mUserName + " filePath" + this.mCase.getStrCasePath());
        return _result;
    }

    private boolean is50KContinuityOxygenData(DeviceData mData2) {
        if ((mData2.mDataType.equalsIgnoreCase("ECG(PM10)") || mData2.mDataType.equalsIgnoreCase("ECG(CMS50K)") || mData2.mDataType.equalsIgnoreCase("ECG(CMS50K1)")) && mData2.is50KContinuityOxygenData) {
            return true;
        }
        return false;
    }

    private void baseinfo() {
        this._loginUserInfo = PageUtil.getLoginUserInfo();
        if (this._loginUserInfo.mBirthday != null && !this._loginUserInfo.mBirthday.equalsIgnoreCase(bs.b)) {
            this._age = new StringBuilder().append(((long) Calendar.getInstance().get(1)) - Long.parseLong(this._loginUserInfo.mBirthday.substring(0, 4))).toString();
        }
        if (this._loginUserInfo.mHeight != null && !this._loginUserInfo.mHeight.equalsIgnoreCase(bs.b)) {
            this._height = new StringBuilder().append((long) Double.parseDouble(this._loginUserInfo.mHeight)).toString();
        }
        if (this._loginUserInfo.mWeight != null && !this._loginUserInfo.mWeight.equalsIgnoreCase(bs.b)) {
            this._weight = new StringBuilder().append((long) Double.parseDouble(this._loginUserInfo.mWeight)).toString();
        }
        if (this._loginUserInfo.mSex != null && !this._loginUserInfo.mSex.equalsIgnoreCase(bs.b)) {
            this._sex = new StringBuilder().append(Long.parseLong(this._loginUserInfo.mSex)).toString();
        }
    }

    private void process8000GW(StringBuffer _content, String reTime) {
        String _age2 = bs.b;
        String _height2 = bs.b;
        String _weight2 = bs.b;
        String _sex2 = bs.b;
        LoginUserDao _loginUserInfo2 = PageUtil.getLoginUserInfo();
        if (_loginUserInfo2.mBirthday != null && !_loginUserInfo2.mBirthday.equalsIgnoreCase(bs.b)) {
            _age2 = new StringBuilder().append(((long) Calendar.getInstance().get(1)) - Long.parseLong(_loginUserInfo2.mBirthday.substring(0, 4))).toString();
        }
        if (_loginUserInfo2.mHeight != null && !_loginUserInfo2.mHeight.equalsIgnoreCase(bs.b)) {
            _height2 = new StringBuilder().append((long) Double.parseDouble(_loginUserInfo2.mHeight)).toString();
        }
        if (_loginUserInfo2.mWeight != null && !_loginUserInfo2.mWeight.equalsIgnoreCase(bs.b)) {
            _weight2 = new StringBuilder().append((long) Double.parseDouble(_loginUserInfo2.mWeight)).toString();
        }
        if (_loginUserInfo2.mSex != null && !_loginUserInfo2.mSex.equalsIgnoreCase(bs.b)) {
            _sex2 = new StringBuilder().append(Long.parseLong(_loginUserInfo2.mSex)).toString();
        }
        _content.append("&lt;?xml version=\"1.0\" encoding=\"GBK\"?&gt;&lt;otherparam&gt;");
        _content.append("&lt;pid&gt;");
        _content.append(bs.b);
        _content.append("&lt;/pid&gt;");
        _content.append("&lt;caseName&gt;");
        _content.append(_loginUserInfo2.mUserName);
        _content.append("&lt;/caseName&gt;");
        _content.append("&lt;age&gt;");
        _content.append(_age2);
        _content.append("&lt;/age&gt;");
        _content.append("&lt;height&gt;");
        _content.append(_height2);
        _content.append("&lt;/height&gt;");
        _content.append("&lt;weight&gt;");
        _content.append(_weight2);
        _content.append("&lt;/weight&gt;");
        _content.append("&lt;sex&gt;");
        _content.append(_sex2);
        _content.append("&lt;/sex&gt;");
        _content.append("&lt;nation&gt;");
        _content.append("汉族");
        _content.append("&lt;/nation&gt;");
        _content.append("&lt;caseAddr&gt;");
        _content.append(_loginUserInfo2.mAre);
        _content.append("&lt;/caseAddr&gt;");
        _content.append("&lt;cop&gt;");
        _content.append(_loginUserInfo2.mUserName);
        _content.append("&lt;/cop&gt;");
        _content.append("&lt;comment&gt;");
        _content.append(bs.b);
        _content.append("&lt;/comment&gt;");
        _content.append("&lt;chief&gt;");
        _content.append("新采集客户端");
        _content.append("&lt;/chief&gt;");
        _content.append("&lt;language&gt;");
        _content.append("chinese");
        _content.append("&lt;/language&gt;");
        _content.append("&lt;yzlb&gt;");
        _content.append(bs.b);
        _content.append("&lt;/yzlb&gt;");
        _content.append("&lt;aid&gt;");
        _content.append(bs.b);
        _content.append("&lt;/aid&gt;");
        _content.append("&lt;appphy&gt;");
        _content.append(bs.b);
        _content.append("&lt;/appphy&gt;");
        _content.append("&lt;asection&gt;");
        _content.append(bs.b);
        _content.append("&lt;/asection&gt;");
        _content.append("&lt;atime&gt;");
        _content.append(bs.b);
        _content.append("&lt;/atime&gt;");
        _content.append("&lt;ha&gt;");
        _content.append(bs.b);
        _content.append("&lt;/ha&gt;");
        _content.append("&lt;hr&gt;");
        _content.append(bs.b);
        _content.append("&lt;/hr&gt;");
        _content.append("&lt;hb&gt;");
        _content.append(bs.b);
        _content.append("&lt;/hb&gt;");
        _content.append("&lt;chan&gt;");
        _content.append("1");
        _content.append("&lt;/chan&gt;");
        _content.append("&lt;pace&gt;");
        _content.append("0");
        _content.append("&lt;/pace&gt;");
        _content.append("&lt;/otherparam&gt;");
    }

    private void getAliyunUploadContent(DeviceData data) {
        if (data == null) {
            return;
        }
        if (this.mData.mDataType.equalsIgnoreCase("sp0208")) {
            DeviceData50DJ_Jar spData = (DeviceData50DJ_Jar) this.mData.mDataList.get(0);
            int length = spData.getmSp02DataList().size();
            for (int i = 0; i < length; i++) {
                this.hm = new Cms50DJ_Aliyun_Oxygen(spData.getmSp02DataList().get(i)).getContent();
                postDataInterface(this.hm);
            }
        } else if (this.mData.mDataType.equalsIgnoreCase("pedometerDay")) {
            DeviceDataPedometerJar pedometerData = (DeviceDataPedometerJar) this.mData.mDataList.get(0);
            int size = pedometerData.getmPedometerDataDayList().size();
            for (int i2 = 0; i2 < size; i2++) {
                this.hm = new Cms50DJ_Aliyun_PedometerDay(pedometerData.getmPedometerDataDayList().get(i2)).getContent();
                postDataInterface(this.hm);
            }
        } else {
            int size2 = data.mDataList.size();
            for (int i3 = 0; i3 < size2; i3++) {
                if (this.mData.mDataType.equals("contec08aw")) {
                    this.hm = new Contec08aTrend_Aliyun((byte[]) data.mDataList.get(i3)).getContent();
                } else if (this.mData.mDataType.equalsIgnoreCase("FHR01")) {
                    this.hm = new Fhr01Trend_Aliyun((byte[]) data.mDataList.get(i3)).getContent();
                } else if (this.mData.mDataType.equalsIgnoreCase(Spo2DataDao.SPO2)) {
                    this.hm = new Cms50ewTrend_Aliyun((byte[]) data.mDataList.get(i3)).getContent();
                }
                postDataInterface(this.hm);
            }
        }
    }

    private void postDataInterface(final HashMap<String, String> hm) {
        new Thread(new Runnable() {
            public void run() {
                Result<String> result = TopComponent.getInstance().invoke(hm);
                if (result.isSuccess()) {
                    Log.e("result--->", (String) result.data);
                }
            }
        }).start();
    }
}
