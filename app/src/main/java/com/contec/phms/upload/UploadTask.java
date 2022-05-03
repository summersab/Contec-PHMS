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
import com.contec.phms.util.PageUtil;
import com.taobao.tae.sdk.TopComponent;
import com.taobao.tae.sdk.model.Result;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import org.apache.commons.httpclient.HttpMethodBase;
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

    /* JADX WARNING: type inference failed for: r0v3, types: [com.contec.phms.device.template.DeviceData] */
    /* JADX WARNING: type inference failed for: r0v173, types: [com.contec.phms.device.template.DeviceData] */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private boolean uploadCase() {
        /*
            r42 = this;
            com.contec.phms.db.LoginUserDao r32 = com.contec.phms.util.PageUtil.getLoginUserInfo()
            r19 = 0
            java.lang.StringBuffer r21 = new java.lang.StringBuffer
            r21.<init>()
            r22 = 0
            r0 = r42
            com.contec.phms.device.template.DeviceData r4 = r0.mData
            java.lang.String r4 = r4.mDataType
            java.lang.String r5 = "ECG(PM10)"
            boolean r4 = r4.equalsIgnoreCase(r5)
            if (r4 == 0) goto L_0x03af
            com.contec.phms.App_phms r4 = com.contec.phms.App_phms.getInstance()
            android.content.Context r4 = r4.getApplicationContext()
            com.contec.phms.util.PhmsSharedPreferences r4 = com.contec.phms.util.PhmsSharedPreferences.getInstance(r4)
            java.lang.String r5 = "Electrocardiogram"
            java.lang.String r6 = "auto"
            java.lang.String r41 = r4.getString(r5, r6)
            java.lang.String r4 = "auto"
            r0 = r41
            boolean r4 = r4.equals(r0)
            if (r4 == 0) goto L_0x011f
            r19 = 1
        L_0x003b:
            java.lang.String r4 = "UploadTask"
            java.lang.StringBuilder r5 = new java.lang.StringBuilder
            java.lang.String r6 = "type的值为:"
            r5.<init>(r6)
            r0 = r41
            java.lang.StringBuilder r5 = r5.append(r0)
            java.lang.String r6 = " _cardType的值为："
            java.lang.StringBuilder r5 = r5.append(r6)
            r0 = r19
            java.lang.StringBuilder r5 = r5.append(r0)
            java.lang.String r5 = r5.toString()
            com.contec.phms.util.CLog.e(r4, r5)
            r0 = r42
            com.contec.phms.device.template.DeviceData r0 = r0.mData
            r22 = r0
            com.contec.phms.device.pm10.DeviceData r22 = (com.contec.phms.device.pm10.DeviceData) r22
            r0 = r22
            byte[] r0 = r0.TrendData
            r28 = r0
            java.lang.StringBuilder r4 = new java.lang.StringBuilder
            r4.<init>()
            r5 = 0
            byte r5 = r28[r5]
            int r5 = r5 + 2000
            java.lang.StringBuilder r4 = r4.append(r5)
            java.lang.String r5 = "-"
            java.lang.StringBuilder r4 = r4.append(r5)
            r5 = 1
            byte r5 = r28[r5]
            java.lang.StringBuilder r4 = r4.append(r5)
            java.lang.String r5 = "-"
            java.lang.StringBuilder r4 = r4.append(r5)
            r5 = 2
            byte r5 = r28[r5]
            java.lang.StringBuilder r4 = r4.append(r5)
            java.lang.String r5 = " "
            java.lang.StringBuilder r4 = r4.append(r5)
            r5 = 3
            byte r5 = r28[r5]
            java.lang.StringBuilder r4 = r4.append(r5)
            java.lang.String r5 = ":"
            java.lang.StringBuilder r4 = r4.append(r5)
            r5 = 4
            byte r5 = r28[r5]
            java.lang.StringBuilder r4 = r4.append(r5)
            java.lang.String r5 = ":"
            java.lang.StringBuilder r4 = r4.append(r5)
            r5 = 5
            byte r5 = r28[r5]
            java.lang.StringBuilder r4 = r4.append(r5)
            java.lang.String r29 = r4.toString()
            r34 = 0
            com.contec.phms.util.GetCurrentTimeMillis r3 = new com.contec.phms.util.GetCurrentTimeMillis
            r4 = 0
            byte r4 = r28[r4]
            r5 = 1
            byte r5 = r28[r5]
            r6 = 2
            byte r6 = r28[r6]
            r8 = 3
            byte r7 = r28[r8]
            r8 = 4
            byte r8 = r28[r8]
            r9 = 5
            byte r9 = r28[r9]
            r3.<init>(r4, r5, r6, r7, r8, r9)
            java.lang.String r33 = r3.getTime()
            java.text.SimpleDateFormat r35 = new java.text.SimpleDateFormat
            java.lang.String r4 = "yyyyMMddHHmmss"
            r0 = r35
            r0.<init>(r4)
            long r4 = java.lang.System.currentTimeMillis()     // Catch:{ ParseException -> 0x012d }
            r8 = 1000(0x3e8, double:4.94E-321)
            long r4 = r4 / r8
            r0 = r35
            r1 = r33
            java.util.Date r6 = r0.parse(r1)     // Catch:{ ParseException -> 0x012d }
            long r8 = r6.getTime()     // Catch:{ ParseException -> 0x012d }
            r10 = 1000(0x3e8, double:4.94E-321)
            long r8 = r8 / r10
            long r4 = r4 - r8
            r0 = r42
            r0.difference = r4     // Catch:{ ParseException -> 0x012d }
        L_0x00ff:
            r0 = r42
            long r4 = r0.difference
            r8 = -2592000(0xffffffffffd87300, double:NaN)
            int r4 = (r4 > r8 ? 1 : (r4 == r8 ? 0 : -1))
            if (r4 < 0) goto L_0x0115
            r0 = r42
            long r4 = r0.difference
            r8 = 31536000(0x1e13380, double:1.5580854E-316)
            int r4 = (r4 > r8 ? 1 : (r4 == r8 ? 0 : -1))
            if (r4 <= 0) goto L_0x0132
        L_0x0115:
            r0 = r42
            com.contec.phms.device.template.DeviceData r4 = r0.mData
            r5 = 1
            r4.mCheckTimeIllegal = r5
            r27 = 0
        L_0x011e:
            return r27
        L_0x011f:
            java.lang.String r4 = "doctor"
            r0 = r41
            boolean r4 = r4.equals(r0)
            if (r4 == 0) goto L_0x003b
            r19 = 0
            goto L_0x003b
        L_0x012d:
            r37 = move-exception
            r37.printStackTrace()
            goto L_0x00ff
        L_0x0132:
            java.text.SimpleDateFormat r38 = new java.text.SimpleDateFormat
            java.lang.String r4 = "yyyy-MM-dd HH:mm:ss"
            r0 = r38
            r0.<init>(r4)
            r0 = r38
            r1 = r29
            java.util.Date r34 = r0.parse(r1)     // Catch:{ ParseException -> 0x0397 }
        L_0x0143:
            r42.baseinfo()
            java.lang.String r4 = "Chinese"
            r0 = r42
            r0._sp10Language = r4
            java.lang.String r4 = com.contec.phms.util.Constants.Language
            java.lang.String r5 = "en"
            boolean r4 = r4.contains(r5)
            if (r4 == 0) goto L_0x039d
            java.lang.String r4 = "English"
            r0 = r42
            r0._sp10Language = r4
        L_0x015c:
            r4 = 6
            byte r4 = r28[r4]
            r4 = r4 & 255(0xff, float:3.57E-43)
            int r4 = r4 << 7
            r5 = 7
            byte r5 = r28[r5]
            r5 = r5 & 255(0xff, float:3.57E-43)
            r4 = r4 | r5
            r0 = r4 & 2047(0x7ff, float:2.868E-42)
            r24 = r0
            java.lang.StringBuilder r4 = new java.lang.StringBuilder
            r4.<init>()
            r5 = 8
            byte r5 = r28[r5]
            java.lang.StringBuilder r4 = r4.append(r5)
            r5 = 9
            byte r5 = r28[r5]
            java.lang.StringBuilder r4 = r4.append(r5)
            r5 = 10
            byte r5 = r28[r5]
            java.lang.StringBuilder r4 = r4.append(r5)
            r5 = 11
            byte r5 = r28[r5]
            java.lang.StringBuilder r4 = r4.append(r5)
            r5 = 12
            byte r5 = r28[r5]
            java.lang.StringBuilder r4 = r4.append(r5)
            r5 = 13
            byte r5 = r28[r5]
            java.lang.StringBuilder r4 = r4.append(r5)
            r5 = 14
            byte r5 = r28[r5]
            java.lang.StringBuilder r4 = r4.append(r5)
            r5 = 15
            byte r5 = r28[r5]
            java.lang.StringBuilder r4 = r4.append(r5)
            r5 = 16
            byte r5 = r28[r5]
            java.lang.StringBuilder r4 = r4.append(r5)
            r5 = 17
            byte r5 = r28[r5]
            java.lang.StringBuilder r4 = r4.append(r5)
            r5 = 18
            byte r5 = r28[r5]
            java.lang.StringBuilder r4 = r4.append(r5)
            r5 = 19
            byte r5 = r28[r5]
            java.lang.StringBuilder r4 = r4.append(r5)
            r5 = 20
            byte r5 = r28[r5]
            java.lang.StringBuilder r4 = r4.append(r5)
            r5 = 21
            byte r5 = r28[r5]
            java.lang.StringBuilder r4 = r4.append(r5)
            java.lang.String r25 = r4.toString()
            r0 = r38
            r1 = r34
            java.lang.String r40 = r0.format(r1)
            java.lang.String r4 = "&lt;?xml version=\"1.0\" encoding=\"GBK\"?&gt;&lt;otherparam&gt;"
            r0 = r21
            r0.append(r4)
            java.lang.String r4 = "&lt;heartrate&gt;"
            r0 = r21
            r0.append(r4)
            java.lang.StringBuilder r4 = new java.lang.StringBuilder
            r4.<init>()
            r0 = r24
            java.lang.StringBuilder r4 = r4.append(r0)
            java.lang.String r4 = r4.toString()
            r0 = r21
            r0.append(r4)
            java.lang.String r4 = "&lt;/heartrate&gt;&lt;feature&gt;"
            r0 = r21
            r0.append(r4)
            java.lang.StringBuilder r4 = new java.lang.StringBuilder
            r4.<init>()
            r0 = r25
            java.lang.StringBuilder r4 = r4.append(r0)
            java.lang.String r4 = r4.toString()
            r0 = r21
            r0.append(r4)
            java.lang.String r4 = "&lt;/feature&gt;&lt;checktime&gt;"
            r0 = r21
            r0.append(r4)
            r0 = r21
            r1 = r40
            r0.append(r1)
            java.lang.String r4 = "&lt;/checktime&gt;"
            r0 = r21
            r0.append(r4)
            java.lang.String r4 = "&lt;caseName&gt;"
            r0 = r21
            r0.append(r4)
            r0 = r42
            com.contec.phms.db.LoginUserDao r4 = r0._loginUserInfo
            java.lang.String r4 = r4.mUserName
            r0 = r21
            r0.append(r4)
            java.lang.String r4 = "&lt;/caseName&gt;"
            r0 = r21
            r0.append(r4)
            java.lang.String r4 = "&lt;age&gt;"
            r0 = r21
            r0.append(r4)
            r0 = r42
            java.lang.String r4 = r0._age
            r0 = r21
            r0.append(r4)
            java.lang.String r4 = "&lt;/age&gt;"
            r0 = r21
            r0.append(r4)
            java.lang.String r4 = "&lt;height&gt;"
            r0 = r21
            r0.append(r4)
            r0 = r42
            java.lang.String r4 = r0._height
            r0 = r21
            r0.append(r4)
            java.lang.String r4 = "&lt;/height&gt;"
            r0 = r21
            r0.append(r4)
            java.lang.String r4 = "&lt;weight&gt;"
            r0 = r21
            r0.append(r4)
            r0 = r42
            java.lang.String r4 = r0._weight
            r0 = r21
            r0.append(r4)
            java.lang.String r4 = "&lt;/weight&gt;"
            r0 = r21
            r0.append(r4)
            java.lang.String r4 = "&lt;sex&gt;"
            r0 = r21
            r0.append(r4)
            r0 = r42
            java.lang.String r4 = r0._sex
            r0 = r21
            r0.append(r4)
            java.lang.String r4 = "&lt;/sex&gt;"
            r0 = r21
            r0.append(r4)
            java.lang.String r4 = "&lt;language&gt;"
            r0 = r21
            r0.append(r4)
            r0 = r42
            java.lang.String r4 = r0._sp10Language
            r0 = r21
            r0.append(r4)
            java.lang.String r4 = "&lt;/language&gt;"
            r0 = r21
            r0.append(r4)
            java.lang.String r4 = "&lt;/otherparam&gt;"
            r0 = r21
            r0.append(r4)
        L_0x02d3:
            r0 = r42
            com.contec.phms.device.template.DeviceData r4 = r0.mData
            java.lang.String r4 = r4.mDataType
            java.lang.String r5 = "contec8000gw"
            boolean r4 = r4.equalsIgnoreCase(r5)
            if (r4 == 0) goto L_0x02f2
            r0 = r42
            com.contec.phms.upload.cases.common.NEW_CASE r4 = r0.mCase
            java.lang.String r40 = r4.getStrPaceTime()
            r0 = r42
            r1 = r21
            r2 = r40
            r0.process8000GW(r1, r2)
        L_0x02f2:
            r0 = r32
            java.lang.String r3 = r0.mSID
            r0 = r32
            java.lang.String r4 = r0.mHospitalName
            r0 = r32
            java.lang.String r5 = r0.mHospitalID
            r0 = r32
            java.lang.String r6 = r0.mUserName
            r0 = r42
            com.contec.phms.upload.cases.common.NEW_CASE r8 = r0.mCase
            java.lang.String r7 = r8.getCaseName()
            java.lang.StringBuilder r8 = new java.lang.StringBuilder
            r0 = r42
            com.contec.phms.upload.cases.common.NEW_CASE r9 = r0.mCase
            int r9 = r9.getCaseType()
            java.lang.String r9 = java.lang.String.valueOf(r9)
            r8.<init>(r9)
            java.lang.String r8 = r8.toString()
            r0 = r32
            java.lang.String r9 = r0.mUID
            com.contec.phms.App_phms r10 = com.contec.phms.App_phms.getInstance()
            com.contec.phms.infos.UserInfo r10 = r10.mUserInfo
            java.lang.String r10 = r10.mPassword
            r0 = r42
            com.contec.phms.upload.cases.common.NEW_CASE r11 = r0.mCase
            java.lang.String r11 = r11.getStrPaceTime()
            com.contec.phms.App_phms r12 = com.contec.phms.App_phms.getInstance()
            com.contec.phms.infos.UserInfo r12 = r12.mUserInfo
            java.lang.String r12 = r12.mSex
            java.lang.StringBuilder r13 = new java.lang.StringBuilder
            java.lang.String r14 = com.contec.phms.util.Constants.URL
            java.lang.String r14 = java.lang.String.valueOf(r14)
            r13.<init>(r14)
            java.lang.String r14 = "/main.php"
            java.lang.StringBuilder r13 = r13.append(r14)
            java.lang.String r13 = r13.toString()
            java.lang.StringBuilder r14 = new java.lang.StringBuilder
            r14.<init>()
            r0 = r19
            java.lang.StringBuilder r14 = r14.append(r0)
            java.lang.String r14 = r14.toString()
            java.lang.String r15 = r21.toString()
            r0 = r42
            com.contec.phms.upload.cases.common.NEW_CASE r0 = r0.mCase
            r16 = r0
            java.lang.String r16 = r16.getStrCasePath()
            r17 = 0
            r18 = 0
            java.lang.String r26 = cn.com.contec_net_3_android.Method_android_new_case.getCaseID(r3, r4, r5, r6, r7, r8, r9, r10, r11, r12, r13, r14, r15, r16, r17, r18)
            java.lang.String r4 = "UploadTask"
            java.lang.StringBuilder r5 = new java.lang.StringBuilder
            java.lang.String r6 = "_responedResult:"
            r5.<init>(r6)
            r0 = r26
            java.lang.StringBuilder r5 = r5.append(r0)
            java.lang.String r5 = r5.toString()
            com.contec.phms.util.CLog.i(r4, r5)
            int r4 = r26.length()
            r5 = 40
            if (r4 >= r5) goto L_0x08bd
            r27 = 0
            goto L_0x011e
        L_0x0397:
            r36 = move-exception
            r36.printStackTrace()
            goto L_0x0143
        L_0x039d:
            java.lang.String r4 = com.contec.phms.util.Constants.Language
            java.lang.String r5 = "zh"
            boolean r4 = r4.contains(r5)
            if (r4 == 0) goto L_0x015c
            java.lang.String r4 = "Chinese"
            r0 = r42
            r0._sp10Language = r4
            goto L_0x015c
        L_0x03af:
            r0 = r42
            com.contec.phms.device.template.DeviceData r4 = r0.mData
            java.lang.String r4 = r4.mDataType
            java.lang.String r5 = "cms50k"
            boolean r4 = r4.equalsIgnoreCase(r5)
            if (r4 != 0) goto L_0x03d9
            r0 = r42
            com.contec.phms.device.template.DeviceData r4 = r0.mData
            java.lang.String r4 = r4.mDataType
            java.lang.String r5 = "ECG(CMS50K)"
            boolean r4 = r4.equalsIgnoreCase(r5)
            if (r4 != 0) goto L_0x03d9
            r0 = r42
            com.contec.phms.device.template.DeviceData r4 = r0.mData
            java.lang.String r4 = r4.mDataType
            java.lang.String r5 = "ECG(CMS50K1)"
            boolean r4 = r4.equalsIgnoreCase(r5)
            if (r4 == 0) goto L_0x071a
        L_0x03d9:
            com.contec.phms.App_phms r4 = com.contec.phms.App_phms.getInstance()
            android.content.Context r4 = r4.getApplicationContext()
            com.contec.phms.util.PhmsSharedPreferences r4 = com.contec.phms.util.PhmsSharedPreferences.getInstance(r4)
            java.lang.String r5 = "cms50k_card_set"
            java.lang.String r6 = "auto"
            java.lang.String r41 = r4.getString(r5, r6)
            r0 = r42
            com.contec.phms.device.template.DeviceData r0 = r0.mData
            r22 = r0
            com.contec.phms.device.pm10.DeviceData r22 = (com.contec.phms.device.pm10.DeviceData) r22
            java.lang.String r4 = "auto"
            r0 = r41
            boolean r4 = r4.equals(r0)
            if (r4 == 0) goto L_0x06f4
            r19 = 1
        L_0x0401:
            r42.baseinfo()
            r0 = r42
            com.contec.phms.db.LoginUserDao r4 = r0._loginUserInfo
            java.lang.String r4 = r4.mPID
            if (r4 == 0) goto L_0x0435
            r0 = r42
            com.contec.phms.db.LoginUserDao r4 = r0._loginUserInfo
            java.lang.String r4 = r4.mPID
            java.lang.String r5 = ""
            boolean r4 = r4.equalsIgnoreCase(r5)
            if (r4 != 0) goto L_0x0435
            java.lang.StringBuilder r4 = new java.lang.StringBuilder
            r4.<init>()
            r0 = r42
            com.contec.phms.db.LoginUserDao r5 = r0._loginUserInfo
            java.lang.String r5 = r5.mPID
            long r5 = java.lang.Long.parseLong(r5)
            java.lang.StringBuilder r4 = r4.append(r5)
            java.lang.String r4 = r4.toString()
            r0 = r42
            r0._pid = r4
        L_0x0435:
            r0 = r42
            com.contec.phms.db.LoginUserDao r4 = r0._loginUserInfo
            java.lang.String r4 = r4.mPhone
            if (r4 == 0) goto L_0x0466
            r0 = r42
            com.contec.phms.db.LoginUserDao r4 = r0._loginUserInfo
            java.lang.String r4 = r4.mPhone
            java.lang.String r5 = ""
            boolean r4 = r4.equalsIgnoreCase(r5)
            if (r4 != 0) goto L_0x0466
            java.lang.StringBuilder r4 = new java.lang.StringBuilder
            r4.<init>()
            r0 = r42
            com.contec.phms.db.LoginUserDao r5 = r0._loginUserInfo
            java.lang.String r5 = r5.mPhone
            long r5 = java.lang.Long.parseLong(r5)
            java.lang.StringBuilder r4 = r4.append(r5)
            java.lang.String r4 = r4.toString()
            r0 = r42
            r0._phone = r4
        L_0x0466:
            java.lang.String r4 = "******************************"
            java.lang.String r5 = "&&&&&&&&&&&&&&&&&&&&&"
            com.contec.phms.util.CLog.e(r4, r5)
            java.lang.String r4 = "******************************"
            java.lang.StringBuilder r5 = new java.lang.StringBuilder
            r5.<init>()
            r0 = r42
            java.lang.String r6 = r0._phone
            java.lang.StringBuilder r5 = r5.append(r6)
            java.lang.String r5 = r5.toString()
            com.contec.phms.util.CLog.e(r4, r5)
            java.lang.String r4 = "******************************"
            java.lang.String r5 = "&&&&&&&&&&&&&&&&&&&&&"
            com.contec.phms.util.CLog.e(r4, r5)
            java.lang.String r4 = "chinese"
            r0 = r42
            r0._otherLanauage = r4
            java.lang.String r4 = com.contec.phms.util.Constants.Language
            java.lang.String r5 = "en"
            boolean r4 = r4.contains(r5)
            if (r4 == 0) goto L_0x0702
            java.lang.String r4 = "english"
            r0 = r42
            r0._otherLanauage = r4
        L_0x04a0:
            r0 = r22
            byte[] r0 = r0.TrendData
            r28 = r0
            r4 = 6
            byte r4 = r28[r4]
            r4 = r4 & 255(0xff, float:3.57E-43)
            int r4 = r4 << 7
            r5 = 7
            byte r5 = r28[r5]
            r5 = r5 & 255(0xff, float:3.57E-43)
            r4 = r4 | r5
            r0 = r4 & 2047(0x7ff, float:2.868E-42)
            r24 = r0
            java.lang.StringBuilder r4 = new java.lang.StringBuilder
            r4.<init>()
            r5 = 8
            byte r5 = r28[r5]
            java.lang.StringBuilder r4 = r4.append(r5)
            r5 = 9
            byte r5 = r28[r5]
            java.lang.StringBuilder r4 = r4.append(r5)
            r5 = 10
            byte r5 = r28[r5]
            java.lang.StringBuilder r4 = r4.append(r5)
            r5 = 11
            byte r5 = r28[r5]
            java.lang.StringBuilder r4 = r4.append(r5)
            r5 = 12
            byte r5 = r28[r5]
            java.lang.StringBuilder r4 = r4.append(r5)
            r5 = 13
            byte r5 = r28[r5]
            java.lang.StringBuilder r4 = r4.append(r5)
            r5 = 14
            byte r5 = r28[r5]
            java.lang.StringBuilder r4 = r4.append(r5)
            r5 = 15
            byte r5 = r28[r5]
            java.lang.StringBuilder r4 = r4.append(r5)
            r5 = 16
            byte r5 = r28[r5]
            java.lang.StringBuilder r4 = r4.append(r5)
            r5 = 17
            byte r5 = r28[r5]
            java.lang.StringBuilder r4 = r4.append(r5)
            r5 = 18
            byte r5 = r28[r5]
            java.lang.StringBuilder r4 = r4.append(r5)
            r5 = 19
            byte r5 = r28[r5]
            java.lang.StringBuilder r4 = r4.append(r5)
            r5 = 20
            byte r5 = r28[r5]
            java.lang.StringBuilder r4 = r4.append(r5)
            r5 = 21
            byte r5 = r28[r5]
            java.lang.StringBuilder r4 = r4.append(r5)
            java.lang.String r25 = r4.toString()
            java.lang.StringBuilder r4 = new java.lang.StringBuilder
            r4.<init>()
            r5 = 0
            byte r5 = r28[r5]
            int r5 = r5 + 2000
            java.lang.StringBuilder r4 = r4.append(r5)
            java.lang.String r5 = "-"
            java.lang.StringBuilder r4 = r4.append(r5)
            r5 = 1
            byte r5 = r28[r5]
            java.lang.StringBuilder r4 = r4.append(r5)
            java.lang.String r5 = "-"
            java.lang.StringBuilder r4 = r4.append(r5)
            r5 = 2
            byte r5 = r28[r5]
            java.lang.StringBuilder r4 = r4.append(r5)
            java.lang.String r5 = " "
            java.lang.StringBuilder r4 = r4.append(r5)
            r5 = 3
            byte r5 = r28[r5]
            java.lang.StringBuilder r4 = r4.append(r5)
            java.lang.String r5 = ":"
            java.lang.StringBuilder r4 = r4.append(r5)
            r5 = 4
            byte r5 = r28[r5]
            java.lang.StringBuilder r4 = r4.append(r5)
            java.lang.String r5 = ":"
            java.lang.StringBuilder r4 = r4.append(r5)
            r5 = 5
            byte r5 = r28[r5]
            java.lang.StringBuilder r4 = r4.append(r5)
            java.lang.String r29 = r4.toString()
            r34 = 0
            java.text.SimpleDateFormat r38 = new java.text.SimpleDateFormat
            java.lang.String r4 = "yyyy-MM-dd HH:mm:ss"
            r0 = r38
            r0.<init>(r4)
            r0 = r38
            r1 = r29
            java.util.Date r34 = r0.parse(r1)     // Catch:{ ParseException -> 0x0714 }
        L_0x0596:
            r0 = r38
            r1 = r34
            java.lang.String r40 = r0.format(r1)
            java.lang.String r4 = "&lt;?xml version=\"1.0\" encoding=\"GBK\"?&gt;&lt;otherparam&gt;"
            r0 = r21
            r0.append(r4)
            java.lang.String r4 = "&lt;heartrate&gt;"
            r0 = r21
            r0.append(r4)
            java.lang.StringBuilder r4 = new java.lang.StringBuilder
            r4.<init>()
            r0 = r24
            java.lang.StringBuilder r4 = r4.append(r0)
            java.lang.String r4 = r4.toString()
            r0 = r21
            r0.append(r4)
            java.lang.String r4 = "&lt;/heartrate&gt;&lt;feature&gt;"
            r0 = r21
            r0.append(r4)
            java.lang.StringBuilder r4 = new java.lang.StringBuilder
            r4.<init>()
            r0 = r25
            java.lang.StringBuilder r4 = r4.append(r0)
            java.lang.String r4 = r4.toString()
            r0 = r21
            r0.append(r4)
            java.lang.String r4 = "&lt;/feature&gt;&lt;checktime&gt;"
            r0 = r21
            r0.append(r4)
            r0 = r21
            r1 = r40
            r0.append(r1)
            java.lang.String r4 = "&lt;/checktime&gt;"
            r0 = r21
            r0.append(r4)
            java.lang.String r4 = "&lt;pid&gt;"
            r0 = r21
            r0.append(r4)
            java.lang.StringBuilder r4 = new java.lang.StringBuilder
            r4.<init>()
            r0 = r42
            java.lang.String r5 = r0._pid
            java.lang.StringBuilder r4 = r4.append(r5)
            java.lang.String r4 = r4.toString()
            r0 = r21
            r0.append(r4)
            java.lang.String r4 = "&lt;/pid&gt;&lt;caseName&gt;"
            r0 = r21
            r0.append(r4)
            java.lang.StringBuilder r4 = new java.lang.StringBuilder
            r4.<init>()
            r0 = r42
            com.contec.phms.db.LoginUserDao r5 = r0._loginUserInfo
            java.lang.String r5 = r5.mUserName
            java.lang.StringBuilder r4 = r4.append(r5)
            java.lang.String r4 = r4.toString()
            r0 = r21
            r0.append(r4)
            java.lang.String r4 = "&lt;/caseName&gt;"
            r0 = r21
            r0.append(r4)
            java.lang.String r4 = "&lt;age&gt;"
            r0 = r21
            r0.append(r4)
            r0 = r42
            java.lang.String r4 = r0._age
            r0 = r21
            r0.append(r4)
            java.lang.String r4 = "&lt;/age&gt;"
            r0 = r21
            r0.append(r4)
            java.lang.String r4 = "&lt;height&gt;"
            r0 = r21
            r0.append(r4)
            r0 = r42
            java.lang.String r4 = r0._height
            r0 = r21
            r0.append(r4)
            java.lang.String r4 = "&lt;/height&gt;"
            r0 = r21
            r0.append(r4)
            java.lang.String r4 = "&lt;weight&gt;"
            r0 = r21
            r0.append(r4)
            r0 = r42
            java.lang.String r4 = r0._weight
            r0 = r21
            r0.append(r4)
            java.lang.String r4 = "&lt;/weight&gt;"
            r0 = r21
            r0.append(r4)
            java.lang.String r4 = "&lt;sex&gt;"
            r0 = r21
            r0.append(r4)
            r0 = r42
            java.lang.String r4 = r0._sex
            r0 = r21
            r0.append(r4)
            java.lang.String r4 = "&lt;/sex&gt;"
            r0 = r21
            r0.append(r4)
            java.lang.String r4 = "&lt;caseAddr&gt;"
            r0 = r21
            r0.append(r4)
            r0 = r42
            com.contec.phms.db.LoginUserDao r4 = r0._loginUserInfo
            java.lang.String r4 = r4.mAddress
            r0 = r21
            r0.append(r4)
            java.lang.String r4 = "&lt;/caseAddr&gt;"
            r0 = r21
            r0.append(r4)
            java.lang.String r4 = "&lt;phone&gt;"
            r0 = r21
            r0.append(r4)
            r0 = r42
            java.lang.String r4 = r0._phone
            r0 = r21
            r0.append(r4)
            java.lang.String r4 = "&lt;/phone&gt;"
            r0 = r21
            r0.append(r4)
            java.lang.String r4 = "&lt;newtype&gt;"
            r0 = r21
            r0.append(r4)
            java.lang.String r4 = "1"
            r0 = r21
            r0.append(r4)
            java.lang.String r4 = "&lt;/newtype&gt;"
            r0 = r21
            r0.append(r4)
            java.lang.String r4 = "&lt;language&gt;"
            r0 = r21
            r0.append(r4)
            r0 = r42
            java.lang.String r4 = r0._otherLanauage
            r0 = r21
            r0.append(r4)
            java.lang.String r4 = "&lt;/language&gt;"
            r0 = r21
            r0.append(r4)
            java.lang.String r4 = "&lt;/otherparam&gt;"
            r0 = r21
            r0.append(r4)
            goto L_0x02d3
        L_0x06f4:
            java.lang.String r4 = "doctor"
            r0 = r41
            boolean r4 = r4.equals(r0)
            if (r4 == 0) goto L_0x0401
            r19 = 0
            goto L_0x0401
        L_0x0702:
            java.lang.String r4 = com.contec.phms.util.Constants.Language
            java.lang.String r5 = "zh"
            boolean r4 = r4.contains(r5)
            if (r4 == 0) goto L_0x04a0
            java.lang.String r4 = "chinese"
            r0 = r42
            r0._otherLanauage = r4
            goto L_0x04a0
        L_0x0714:
            r36 = move-exception
            r36.printStackTrace()
            goto L_0x0596
        L_0x071a:
            r42.baseinfo()
            r0 = r42
            com.contec.phms.db.LoginUserDao r4 = r0._loginUserInfo
            java.lang.String r4 = r4.mPID
            if (r4 == 0) goto L_0x074e
            r0 = r42
            com.contec.phms.db.LoginUserDao r4 = r0._loginUserInfo
            java.lang.String r4 = r4.mPID
            java.lang.String r5 = ""
            boolean r4 = r4.equalsIgnoreCase(r5)
            if (r4 != 0) goto L_0x074e
            java.lang.StringBuilder r4 = new java.lang.StringBuilder
            r4.<init>()
            r0 = r42
            com.contec.phms.db.LoginUserDao r5 = r0._loginUserInfo
            java.lang.String r5 = r5.mPID
            long r5 = java.lang.Long.parseLong(r5)
            java.lang.StringBuilder r4 = r4.append(r5)
            java.lang.String r4 = r4.toString()
            r0 = r42
            r0._pid = r4
        L_0x074e:
            r0 = r42
            com.contec.phms.db.LoginUserDao r4 = r0._loginUserInfo
            java.lang.String r4 = r4.mPhone
            if (r4 == 0) goto L_0x077f
            r0 = r42
            com.contec.phms.db.LoginUserDao r4 = r0._loginUserInfo
            java.lang.String r4 = r4.mPhone
            java.lang.String r5 = ""
            boolean r4 = r4.equalsIgnoreCase(r5)
            if (r4 != 0) goto L_0x077f
            java.lang.StringBuilder r4 = new java.lang.StringBuilder
            r4.<init>()
            r0 = r42
            com.contec.phms.db.LoginUserDao r5 = r0._loginUserInfo
            java.lang.String r5 = r5.mPhone
            long r5 = java.lang.Long.parseLong(r5)
            java.lang.StringBuilder r4 = r4.append(r5)
            java.lang.String r4 = r4.toString()
            r0 = r42
            r0._phone = r4
        L_0x077f:
            java.lang.String r4 = "******************************"
            java.lang.String r5 = "&&&&&&&&&&&&&&&&&&&&&"
            com.contec.phms.util.CLog.e(r4, r5)
            java.lang.String r4 = "******************************"
            java.lang.StringBuilder r5 = new java.lang.StringBuilder
            r5.<init>()
            r0 = r42
            java.lang.String r6 = r0._phone
            java.lang.StringBuilder r5 = r5.append(r6)
            java.lang.String r5 = r5.toString()
            com.contec.phms.util.CLog.e(r4, r5)
            java.lang.String r4 = "******************************"
            java.lang.String r5 = "&&&&&&&&&&&&&&&&&&&&&"
            com.contec.phms.util.CLog.e(r4, r5)
            java.lang.String r4 = "chinese"
            r0 = r42
            r0._otherLanauage = r4
            java.lang.String r4 = com.contec.phms.util.Constants.Language
            java.lang.String r5 = "en"
            boolean r4 = r4.contains(r5)
            if (r4 == 0) goto L_0x08ab
            java.lang.String r4 = "english"
            r0 = r42
            r0._otherLanauage = r4
        L_0x07b9:
            java.lang.String r4 = "&lt;?xml version=\"1.0\" encoding=\"GBK\"?&gt;&lt;otherparam&gt;"
            r0 = r21
            r0.append(r4)
            java.lang.String r4 = "&lt;pid&gt;"
            r0 = r21
            r0.append(r4)
            java.lang.StringBuilder r4 = new java.lang.StringBuilder
            r4.<init>()
            r0 = r42
            java.lang.String r5 = r0._pid
            java.lang.StringBuilder r4 = r4.append(r5)
            java.lang.String r4 = r4.toString()
            r0 = r21
            r0.append(r4)
            java.lang.String r4 = "&lt;/pid&gt;&lt;caseName&gt;"
            r0 = r21
            r0.append(r4)
            java.lang.StringBuilder r4 = new java.lang.StringBuilder
            r4.<init>()
            java.lang.String r5 = "username"
            java.lang.StringBuilder r4 = r4.append(r5)
            java.lang.String r4 = r4.toString()
            r0 = r21
            r0.append(r4)
            java.lang.String r4 = "&lt;/caseName&gt;"
            r0 = r21
            r0.append(r4)
            java.lang.String r4 = "&lt;age&gt;"
            r0 = r21
            r0.append(r4)
            r0 = r42
            java.lang.String r4 = r0._age
            r0 = r21
            r0.append(r4)
            java.lang.String r4 = "&lt;/age&gt;"
            r0 = r21
            r0.append(r4)
            java.lang.String r4 = "&lt;height&gt;"
            r0 = r21
            r0.append(r4)
            r0 = r42
            java.lang.String r4 = r0._height
            r0 = r21
            r0.append(r4)
            java.lang.String r4 = "&lt;/height&gt;"
            r0 = r21
            r0.append(r4)
            java.lang.String r4 = "&lt;weight&gt;"
            r0 = r21
            r0.append(r4)
            r0 = r42
            java.lang.String r4 = r0._weight
            r0 = r21
            r0.append(r4)
            java.lang.String r4 = "&lt;/weight&gt;"
            r0 = r21
            r0.append(r4)
            java.lang.String r4 = "&lt;sex&gt;"
            r0 = r21
            r0.append(r4)
            r0 = r42
            java.lang.String r4 = r0._sex
            r0 = r21
            r0.append(r4)
            java.lang.String r4 = "&lt;/sex&gt;"
            r0 = r21
            r0.append(r4)
            java.lang.String r4 = "&lt;caseAddr&gt;"
            r0 = r21
            r0.append(r4)
            r0 = r42
            com.contec.phms.db.LoginUserDao r4 = r0._loginUserInfo
            java.lang.String r4 = r4.mAddress
            r0 = r21
            r0.append(r4)
            java.lang.String r4 = "&lt;/caseAddr&gt;"
            r0 = r21
            r0.append(r4)
            java.lang.String r4 = "&lt;phone&gt;"
            r0 = r21
            r0.append(r4)
            r0 = r42
            java.lang.String r4 = r0._phone
            r0 = r21
            r0.append(r4)
            java.lang.String r4 = "&lt;/phone&gt;"
            r0 = r21
            r0.append(r4)
            java.lang.String r4 = "&lt;language&gt;"
            r0 = r21
            r0.append(r4)
            r0 = r42
            java.lang.String r4 = r0._otherLanauage
            r0 = r21
            r0.append(r4)
            java.lang.String r4 = "&lt;/language&gt;"
            r0 = r21
            r0.append(r4)
            java.lang.String r4 = "&lt;/otherparam&gt;"
            r0 = r21
            r0.append(r4)
            goto L_0x02d3
        L_0x08ab:
            java.lang.String r4 = com.contec.phms.util.Constants.Language
            java.lang.String r5 = "zh"
            boolean r4 = r4.contains(r5)
            if (r4 == 0) goto L_0x07b9
            java.lang.String r4 = "chinese"
            r0 = r42
            r0._otherLanauage = r4
            goto L_0x07b9
        L_0x08bd:
            r4 = 34
            r5 = 40
            r0 = r26
            java.lang.String r20 = r0.substring(r4, r5)
            java.lang.String r4 = "UploadTask"
            java.lang.StringBuilder r5 = new java.lang.StringBuilder
            java.lang.String r6 = "new  case  return code: "
            r5.<init>(r6)
            r0 = r20
            java.lang.StringBuilder r5 = r5.append(r0)
            java.lang.String r6 = "   Constants.URL: "
            java.lang.StringBuilder r5 = r5.append(r6)
            java.lang.String r6 = com.contec.phms.util.Constants.URL
            java.lang.StringBuilder r5 = r5.append(r6)
            java.lang.String r6 = "/main.php"
            java.lang.StringBuilder r5 = r5.append(r6)
            java.lang.String r5 = r5.toString()
            com.contec.phms.util.CLog.e(r4, r5)
            java.lang.String r4 = "100000"
            r0 = r20
            boolean r4 = r0.equals(r4)
            if (r4 == 0) goto L_0x0a6b
            java.lang.String r4 = r26.toString()
            r5 = 42
            r6 = 61
            java.lang.String r7 = r4.substring(r5, r6)
            cn.com.contec_net_3_android.Method_android_upload_case r3 = new cn.com.contec_net_3_android.Method_android_upload_case
            r4 = 0
            r0 = r42
            com.contec.phms.upload.cases.common.NEW_CASE r5 = r0.mCase
            java.lang.String r5 = r5.getStrCasePath()
            com.contec.phms.App_phms r6 = com.contec.phms.App_phms.getInstance()
            com.contec.phms.infos.UserInfo r6 = r6.mUserInfo
            java.lang.String r6 = r6.mPHPSession
            com.contec.phms.App_phms r8 = com.contec.phms.App_phms.getInstance()
            com.contec.phms.infos.UserInfo r8 = r8.mUserInfo
            java.lang.String r8 = r8.mUserID
            com.contec.phms.App_phms r9 = com.contec.phms.App_phms.getInstance()
            com.contec.phms.infos.UserInfo r9 = r9.mUserInfo
            java.lang.String r9 = r9.mPassword
            java.lang.StringBuilder r10 = new java.lang.StringBuilder
            java.lang.String r11 = com.contec.phms.util.Constants.URL
            java.lang.String r11 = java.lang.String.valueOf(r11)
            r10.<init>(r11)
            java.lang.String r11 = "/main.php"
            java.lang.StringBuilder r10 = r10.append(r11)
            java.lang.String r10 = r10.toString()
            r3.<init>(r4, r5, r6, r7, r8, r9, r10)
            java.lang.String r4 = "============="
            java.lang.String r5 = "=================="
            android.util.Log.e(r4, r5)
            java.lang.String r4 = "获取当前的sessionID"
            java.lang.StringBuilder r5 = new java.lang.StringBuilder
            java.lang.String r6 = "SessionID值为：："
            r5.<init>(r6)
            com.contec.phms.App_phms r6 = com.contec.phms.App_phms.getInstance()
            com.contec.phms.infos.UserInfo r6 = r6.mUserInfo
            java.lang.String r6 = r6.mPHPSession
            java.lang.StringBuilder r5 = r5.append(r6)
            java.lang.String r5 = r5.toString()
            android.util.Log.e(r4, r5)
            java.lang.String r4 = "============="
            java.lang.String r5 = "=================="
            android.util.Log.e(r4, r5)
            java.lang.String r31 = r3.init()
            java.lang.String r4 = "UploadTask"
            java.lang.StringBuilder r5 = new java.lang.StringBuilder
            java.lang.String r6 = "++++++++++++++++++++++: "
            r5.<init>(r6)
            r0 = r31
            java.lang.StringBuilder r5 = r5.append(r0)
            java.lang.String r5 = r5.toString()
            com.contec.phms.util.CLog.e(r4, r5)
            if (r31 == 0) goto L_0x0a68
            int r4 = r31.length()
            r5 = 40
            if (r4 <= r5) goto L_0x0a68
            r4 = 34
            r5 = 40
            r0 = r31
            java.lang.String r30 = r0.substring(r4, r5)
            if (r30 == 0) goto L_0x0a65
            int r4 = r30.length()
            r5 = 1
            if (r4 <= r5) goto L_0x0a65
            java.lang.String r4 = "100000"
            r0 = r30
            boolean r4 = r0.equals(r4)
            if (r4 == 0) goto L_0x0a62
            r0 = r42
            com.contec.phms.device.template.DeviceData r4 = r0.mData
            java.lang.String r4 = r4.mDataType
            java.lang.String r5 = "ECG(PM10)"
            boolean r4 = r4.equalsIgnoreCase(r5)
            if (r4 != 0) goto L_0x09d5
            r0 = r42
            com.contec.phms.device.template.DeviceData r4 = r0.mData
            java.lang.String r4 = r4.mDataType
            java.lang.String r5 = "ECG(CMS50K)"
            boolean r4 = r4.equalsIgnoreCase(r5)
            if (r4 != 0) goto L_0x09d5
            r0 = r42
            com.contec.phms.device.template.DeviceData r4 = r0.mData
            java.lang.String r4 = r4.mDataType
            java.lang.String r5 = "ECG(CMS50K1)"
            boolean r4 = r4.equalsIgnoreCase(r5)
            if (r4 == 0) goto L_0x0a5f
        L_0x09d5:
            r0 = r42
            com.contec.phms.device.template.DeviceData r4 = r0.mData
            r0 = r42
            boolean r4 = r0.is50KContinuityOxygenData(r4)
            if (r4 != 0) goto L_0x0a55
            java.lang.String r4 = "UploadTask"
            java.lang.String r5 = "---心电数据----"
            com.contec.phms.util.CLog.i(r4, r5)
            r0 = r22
            r0.mCaseID = r7
            com.contec.phms.upload.trend.PM10Trend_XML r4 = new com.contec.phms.upload.trend.PM10Trend_XML
            r0 = r42
            com.contec.phms.device.template.DeviceData r5 = r0.mData
            r4.<init>(r5)
            java.lang.String r0 = r4.mContent
            r23 = r0
            r4 = 1
            r0 = r42
            r1 = r23
            boolean r27 = r0.uploadTrend(r1, r4)
        L_0x0a02:
            com.contec.phms.util.FileOperation.clearTempFiles()
        L_0x0a05:
            java.lang.String r4 = "UploadTask"
            java.lang.StringBuilder r5 = new java.lang.StringBuilder
            java.lang.String r6 = " 得到新病例id  mCase.getCaseType():"
            r5.<init>(r6)
            r0 = r42
            com.contec.phms.upload.cases.common.NEW_CASE r6 = r0.mCase
            int r6 = r6.getCaseType()
            java.lang.StringBuilder r5 = r5.append(r6)
            java.lang.String r6 = " mCase.getCaseName():"
            java.lang.StringBuilder r5 = r5.append(r6)
            r0 = r42
            com.contec.phms.upload.cases.common.NEW_CASE r6 = r0.mCase
            java.lang.String r6 = r6.getCaseName()
            java.lang.StringBuilder r5 = r5.append(r6)
            java.lang.String r6 = "_userinfo.mUserName:"
            java.lang.StringBuilder r5 = r5.append(r6)
            r0 = r32
            java.lang.String r6 = r0.mUserName
            java.lang.StringBuilder r5 = r5.append(r6)
            java.lang.String r6 = " filePath"
            java.lang.StringBuilder r5 = r5.append(r6)
            r0 = r42
            com.contec.phms.upload.cases.common.NEW_CASE r6 = r0.mCase
            java.lang.String r6 = r6.getStrCasePath()
            java.lang.StringBuilder r5 = r5.append(r6)
            java.lang.String r5 = r5.toString()
            com.contec.phms.util.CLog.e(r4, r5)
            goto L_0x011e
        L_0x0a55:
            java.lang.String r4 = "UploadTask"
            java.lang.String r5 = "-----连续血氧数据---"
            com.contec.phms.util.CLog.i(r4, r5)
            r27 = 1
            goto L_0x0a02
        L_0x0a5f:
            r27 = 1
            goto L_0x0a02
        L_0x0a62:
            r27 = 0
            goto L_0x0a02
        L_0x0a65:
            r27 = 0
            goto L_0x0a02
        L_0x0a68:
            r27 = 0
            goto L_0x0a02
        L_0x0a6b:
            java.lang.String r4 = "the sessionid you passed is invalid"
            r0 = r26
            boolean r4 = r0.contains(r4)
            if (r4 == 0) goto L_0x0a97
            android.os.Message r39 = new android.os.Message
            r39.<init>()
            r4 = 528(0x210, float:7.4E-43)
            r0 = r39
            r0.what = r4
            r4 = 1
            r0 = r39
            r0.arg2 = r4
            com.contec.phms.App_phms r4 = com.contec.phms.App_phms.getInstance()
            de.greenrobot.event.EventBusPostOnBackGround r4 = r4.mEventBusPostOnBackGround
            r0 = r39
            r4.postInMainThread(r0)
        L_0x0a90:
            r27 = 0
            com.contec.phms.util.FileOperation.clearTempFiles()
            goto L_0x0a05
        L_0x0a97:
            java.lang.String r4 = "100007"
            r0 = r20
            boolean r4 = r0.equals(r4)
            if (r4 == 0) goto L_0x0abd
            android.os.Message r39 = new android.os.Message
            r39.<init>()
            r4 = 519(0x207, float:7.27E-43)
            r0 = r39
            r0.what = r4
            r4 = 1
            r0 = r39
            r0.arg2 = r4
            com.contec.phms.App_phms r4 = com.contec.phms.App_phms.getInstance()
            de.greenrobot.event.EventBusPostOnBackGround r4 = r4.mEventBusPostOnBackGround
            r0 = r39
            r4.postInMainThread(r0)
            goto L_0x0a90
        L_0x0abd:
            java.lang.String r4 = "100004"
            r0 = r20
            boolean r4 = r0.equals(r4)
            if (r4 != 0) goto L_0x0a90
            java.lang.String r4 = "100003"
            r0 = r20
            r0.equals(r4)
            goto L_0x0a90
        */
        throw new UnsupportedOperationException("Method not decompiled: com.contec.phms.upload.UploadTask.uploadCase():boolean");
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
