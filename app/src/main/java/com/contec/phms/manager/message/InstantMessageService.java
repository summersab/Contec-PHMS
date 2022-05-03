package com.contec.phms.manager.message;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import com.conect.json.SqliteConst;
import com.contec.phms.App_phms;
import com.contec.phms.R;
import com.contec.phms.device.template.DeviceData;
import com.contec.phms.login.LoginMethod;
import com.contec.phms.db.LoginUserDao;
import com.contec.phms.db.UserInfoDao;
import com.contec.phms.db.localdata.Spo2DataDao;
import com.contec.phms.saxparsing.CallParsing;
import com.contec.phms.saxparsing.MessageNotification;
import com.contec.phms.util.CLog;
import com.contec.phms.util.Constants;
import com.contec.phms.util.PageUtil;
import com.contec.phms.util.PhmsSharedPreferences;
import com.j256.ormlite.dao.Dao;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import cn.com.contec.jar.sp10w.DeviceDataJar;
import cn.com.contec.net.util.Constants_jar;
import cn.com.contec.net.util.MD5_encoding;
import u.aly.bs;

public class InstantMessageService extends Service {
    private static String TAG = "InstantMessageService";
    private static boolean ifWhile = false;
    private static DefaultHttpClient mClient;
    private Handler mHandler = new Handler() {
        public void dispatchMessage(Message msg) {
            if (msg.what == 65536) {
                InstantMessageService.this.loginsuccess();
                new Thread() {
                    public void run() {
                        LoginUserDao _user = PageUtil.getLoginUserInfo();
                        InstantMessageService.this.getMsg(_user.mSID, _user.mUID, _user.mPsw);
                    }
                }.start();
                return;
            }
            InstantMessageService.ifWhile = false;
        }
    };
    private List<MessageFromServer> mListMsg = new ArrayList();
    private String mloginuserid;
    private String mloginuserpwd;

    public void onCreate() {
        super.onCreate();
        App_phms.getInstance().mEventBus.register(this);
        new Thread() {
            public void run() {
                LoginUserDao _user = PageUtil.getLoginUserInfo();
                InstantMessageService.this.getMsg(_user.mSID, _user.mUID, _user.mPsw);
            }
        }.start();
    }

    public void onEvent(Message msg) {
        if (msg.arg2 == 11) {
            String _contentMsg = bs.b;
            SimpleDateFormat _format = new SimpleDateFormat("HH:mm:ss");
            LoginUserDao _userLoginUserDao = PageUtil.getLoginUserInfo();
            switch (msg.what) {
                case Constants.V_SHOW_NOTIFICATION_INSTANT_MESSAGE /*527*/:
                    for (int i = 0; i < this.mListMsg.size(); i++) {
                        if (_userLoginUserDao != null) {
                            if (_userLoginUserDao.mUserName != null && !_userLoginUserDao.mUserName.equals(bs.b)) {
                                _contentMsg = String.valueOf(getString(R.string.str_notify_message_ch)) + _userLoginUserDao.mUserName;
                            } else if (_userLoginUserDao.mUID != null && !_userLoginUserDao.mUID.equals(bs.b)) {
                                _contentMsg = String.valueOf(getString(R.string.str_notify_message_ch)) + _userLoginUserDao.mUID.substring(_userLoginUserDao.mUID.length() - 4, _userLoginUserDao.mUID.length());
                            }
                        }
                        MessageFromServer _msg = this.mListMsg.get(i);
                        String castype = getCaseType(_msg.getmCaseType());
                        String[] strings = _msg.getmMsgContent().split(";");
                        boolean _ifOrder = false;
                        if (strings[0].equals("type=2")) {
                            _contentMsg = String.valueOf(_contentMsg) + getString(R.string.str_notify_reprod_case_message) + castype + getString(R.string.str_notify_reprod_case_message_str);
                            _ifOrder = true;
                        } else if (strings[0].equals("type=6")) {
                            _contentMsg = String.valueOf(_contentMsg) + getString(R.string.str_notify_advice_message);
                            _ifOrder = true;
                        } else if (strings[0].contains("type=3")) {
                            _contentMsg = String.valueOf(_contentMsg) + getString(R.string.str_notify_reprod_case_message) + castype + getString(R.string.str_notify_reprod_case_message_str);
                            _ifOrder = true;
                        } else if (strings[0].contains("type=4")) {
                            _contentMsg = String.valueOf(_contentMsg) + getString(R.string.str_notify_reprod_case_message) + castype + getString(R.string.str_notify_reprod_case_invalid_message_str);
                            _ifOrder = true;
                        } else if (!strings[0].contains("type=")) {
                            strings[0].contains("type=5");
                        }
                        CLog.d(TAG, "contentmsg:" + _contentMsg);
                        if (_ifOrder) {
                            new MessageNotification(this, _contentMsg, true);
                            Message _msggo = new Message();
                            _msggo.what = 532;
                            _msggo.arg2 = 13;
                            App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(_msggo);
                            _contentMsg = bs.b;
                        }
                    }
                    return;
                case Constants.V_SHOW_NOTIFICATION_INSTANT_MESSAGE_LOCAL_TREND /*529*/:
                    DeviceData _data = (DeviceData) msg.obj;
                    String _date = ".[" + _format.format(new Date()) + "]";
                    if (_userLoginUserDao != null) {
                        if (_userLoginUserDao.mUserName != null && !_userLoginUserDao.mUserName.equals(bs.b)) {
                            _contentMsg = String.valueOf(getString(R.string.str_notify_message_ch)) + _userLoginUserDao.mUserName;
                        } else if (_userLoginUserDao.mUID != null && !_userLoginUserDao.mUID.equals(bs.b)) {
                            _contentMsg = String.valueOf(getString(R.string.str_notify_message_ch)) + App_phms.getInstance().mUserInfo.mUserID.substring(_userLoginUserDao.mUID.length() - 4, _userLoginUserDao.mUID.length());
                        }
                    }
                    if (_data.mDataType.equals("wt")) {
                        String _contentMsg2 = String.valueOf(_contentMsg) + getString(R.string.str_upload_wt_data) + (((double) (((_data.mPack[6] & 255) << 8) | (_data.mPack[7] & 255))) / 100.0d) + "kg" + _date;
                        return;
                    } else if (_data.mDataType.equals("cmssxt")) {
                        String _contentMsg3 = String.valueOf(_contentMsg) + getString(R.string.str_upload_cmxxt_data) + _data.mData + "mmol/L" + _date;
                        return;
                    } else if (_data.mDataType.equals("contec08aw")) {
                        String _contentMsg4 = String.valueOf(_contentMsg) + getString(R.string.str_upload_xueya_data) + (((_data.mPack[5] << 8) | _data.mPack[6]) & 255) + getString(R.string.str_low_press) + (_data.mPack[7] & 255) + _date;
                        return;
                    } else if (_data.mDataType.equals(Spo2DataDao.SPO2)) {
                        if (_data.mDeviceType.equalsIgnoreCase(Constants.CMS50EW)) {
                            byte[] x = (byte[]) _data.mDataList.get(_data.mDataList.size() - 1);
                            String _contentMsg5 = String.valueOf(_contentMsg) + getString(R.string.str_xueyang_data) + (x[6] & 255) + getString(R.string.str_xueyang_mai_lv) + (x[7] & 255) + _date;
                            return;
                        }
                        return;
                    } else if (_data.mDataType.equalsIgnoreCase("FHR01")) {
                        String _contentMsg6 = String.valueOf(_contentMsg) + getString(R.string.str_fhr_case_date) + (((byte[]) _data.mDataList.get(_data.mDataList.size() - 1))[6] & 255) + "BPM" + _date;
                        return;
                    } else if (_data.mDataType.equals("contec08spo2")) {
                        return;
                    } else {
                        if (_data.mDataType.equalsIgnoreCase("sp10w")) {
                            byte[] pack1 = ((DeviceDataJar) _data.mDataList.get(_data.mDataList.size() - 1)).mParamInfo.info1;
                            String _contentMsg7 = String.valueOf(_contentMsg) + getString(R.string.str_feihuoliang) + ((((float) (((pack1[2] & 255) | ((pack1[3] & 255) << 8)) & 65535)) * 10.0f) / 1000.0f) + ", FEV1-" + ((((float) (((pack1[4] & 255) | ((pack1[5] & 255) << 8)) & 65535)) * 10.0f) / 1000.0f) + _date;
                            return;
                        }
                        _data.mDataType.equalsIgnoreCase("abpm50wtrend");
                        return;
                    }
                case Constants.V_SHOW_NOTIFICATION_INSTANT_MESSAGE_LOCAL_CASE /*530*/:
                    String _dateCase = "[" + _format.format(new Date()) + "]";
                    DeviceData _dataCase = (DeviceData) msg.obj;
                    CLog.d(TAG, "本地上传病例  提示消息:" + _dataCase.mDataType);
                    if (_userLoginUserDao != null) {
                        if (_userLoginUserDao.mUserName != null && !_userLoginUserDao.mUserName.equals(bs.b)) {
                            _contentMsg = String.valueOf(getString(R.string.str_notify_message_ch)) + _userLoginUserDao.mUserName;
                        } else if (_userLoginUserDao.mUID != null && !_userLoginUserDao.mUID.equals(bs.b)) {
                            _contentMsg = String.valueOf(getString(R.string.str_notify_message_ch)) + _userLoginUserDao.mUID.substring(_userLoginUserDao.mUID.length() - 4, _userLoginUserDao.mUID.length());
                        }
                    }
                    if (_dataCase.mDataType.equals(Spo2DataDao.SPO2)) {
                        String _contentMsg8 = String.valueOf(_contentMsg) + getString(R.string.str_xueyang_case_date) + _dateCase;
                        return;
                    } else if (_dataCase.mDataType.equals("abpm50w")) {
                        String _contentMsg9 = String.valueOf(_contentMsg) + getString(R.string.str_abpm50w) + _dateCase;
                        return;
                    } else if (_dataCase.mDataType.equals("pm85")) {
                        String _contentMsg10 = String.valueOf(_contentMsg) + getString(R.string.str_pm85_date) + _dateCase;
                        return;
                    } else if (_dataCase.mDataType.equals("FHR01")) {
                        String _contentMsg11 = String.valueOf(_contentMsg) + getString(R.string.str_fhr_case_date) + _dateCase;
                        return;
                    } else if (_dataCase.mDataType.equalsIgnoreCase("sp10w")) {
                        String _contentMsg12 = String.valueOf(_contentMsg) + getString(R.string.str_sp10w_case_check_date) + _dateCase;
                        return;
                    } else {
                        return;
                    }
                case Constants.NOTIFI_INVALID_SID /*535*/:
                    reloginBackGround();
                    return;
                default:
                    return;
            }
        }
    }

    private String getCaseType(String pCaseType) {
        if (pCaseType.equals("1")) {
            return getString(R.string.str_zhinengtijian);
        }
        if (pCaseType.equals("2")) {
            return getString(R.string.str_dtxy);
        }
        if (pCaseType.equals("3")) {
            return getString(R.string.str_dtxyang);
        }
        if (pCaseType.equals("4")) {
            return getString(R.string.str_xdgzz);
        }
        if (pCaseType.equals("5")) {
            return getString(R.string.str_fgn);
        }
        if (pCaseType.equals("6")) {
            return getString(R.string.str_smcs);
        }
        if (pCaseType.equals("7")) {
            return getString(R.string.str_dtxd);
        }
        if (pCaseType.equals("8")) {
            return getString(R.string.str_nddxt);
        }
        if (pCaseType.equals("9")) {
            return getString(R.string.str_xdtj);
        }
        if (pCaseType.equals("10")) {
            return getString(R.string.str_pdxdt);
        }
        if (pCaseType.equals("11")) {
            return Constants.PM85_NAME;
        }
        if (pCaseType.equals("12")) {
            return "PM80";
        }
        if (pCaseType.equals("13")) {
            return getString(R.string.str_cxxx);
        }
        if (pCaseType.equals("14")) {
            return getString(R.string.str_yctj);
        }
        if (pCaseType.equals("15")) {
            return getString(R.string.str_ycyx);
        }
        return bs.b;
    }

    public void onDestroy() {
        super.onDestroy();
        ifWhile = false;
        if (mClient != null) {
            mClient.clearRequestInterceptors();
            mClient.getConnectionManager().closeExpiredConnections();
            mClient.getConnectionManager().shutdown();
            mClient = null;
            CLog.e(TAG, "关闭请求链接：" + ifWhile);
        }
        App_phms.getInstance().mEventBus.unregister(this);
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    public void getMsg(String pSessionID, String pUserID, String pPsw) {
        CLog.d(TAG, "getMsg:开始请求消息" + pUserID + " " + pPsw);
        try {
            String _MessageHeader = "101031" + pSessionID + "11";
            System.out.println("<?xml version=\"1.0\" encoding=\"gb2312\"?><request><senderid></senderid><caseid></caseid><casetype></casetype><msgtype></msgtype><msgdirection>0</msgdirection></request>");
            String _MD5psw_user = MD5_encoding.MD5(String.valueOf(pUserID) + pPsw);
            String _base64Message = Base64.encodeToString("<?xml version=\"1.0\" encoding=\"gb2312\"?><request><senderid></senderid><caseid></caseid><casetype></casetype><msgtype></msgtype><msgdirection>0</msgdirection></request>".getBytes(), 0);
            postRequestData(String.valueOf(Constants.URL) + "/submsg", String.valueOf(String.valueOf(MD5_encoding.MD5(String.valueOf(_MD5psw_user) + _MessageHeader + _base64Message)) + _MessageHeader) + _base64Message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String postRequestData(String pUrl, String msg) {
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair(Constants_jar.MSG_STRING, msg));
        return doPostData(pUrl, params);
    }

    private String doPostData(String url, List<NameValuePair> params) {
        String _result;
        HttpPost _post = new HttpPost(url);
        _post.addHeader("User-Agent", "contec-ctype7/1.0");
        _post.addHeader("Connection", "Keep-Alive");
        Log.e(SqliteConst.KEY_UPLOAD, "request data: " + url);
        mClient = new DefaultHttpClient();
        StringBuilder result = new StringBuilder();
        try {
            _post.setEntity(new UrlEncodedFormEntity(params, "GBK"));
            ifWhile = true;
            while (ifWhile) {
                CLog.e(TAG, "WHILE LOOP********************");
                Thread.sleep(1000);
                HttpResponse response = mClient.execute(_post);
                if (response.getStatusLine().getStatusCode() == 200 && (_result = dealResponseResult(response.getEntity().getContent())) != null && !_result.equals(bs.b) && _result.length() > 42) {
                    String _code = _result.substring(34, 40);
                    Log.e(TAG, "Response: " + _result.toString());
                    if (_code.equals(Constants.SUCCESS)) {
                        this.mListMsg = CallParsing.getInstance().getMessageFromServer(_result.substring(42));
                        Message msgs = new Message();
                        msgs.what = Constants.V_SHOW_NOTIFICATION_INSTANT_MESSAGE;
                        msgs.arg2 = 11;
                        App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(msgs);
                    } else {
                        ifWhile = false;
                        if (_code.equals(Constants.LOGIN_IN_ANOTHER_PLACE)) {
                            Message msgs2 = new Message();
                            msgs2.what = Constants.Login_In_Another_Place;
                            msgs2.arg2 = 1;
                            App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(msgs2);
                        }
                        if (_code.equals(Constants.INVALID_SID)) {
                            Message msgs3 = new Message();
                            msgs3.what = Constants.NOTIFI_INVALID_SID;
                            msgs3.arg2 = 11;
                            App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(msgs3);
                        }
                    }
                }
            }
        } catch (Exception e) {
            if (mClient != null) {
                mClient.clearRequestInterceptors();
                mClient.getConnectionManager().closeExpiredConnections();
                mClient.getConnectionManager().shutdown();
                mClient = null;
                CLog.e(TAG, "Connection closed with an error: " + ifWhile);
            }
            ifWhile = false;
            e.printStackTrace();
        }
        return result.toString();
    }

    private static String dealResponseResult(InputStream inputStream) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] data = new byte[1024];
        boolean _isOver = false;
        while (!_isOver) {
            try {
                int len = inputStream.read(data);
                if (len == -1) {
                    break;
                }
                byteArrayOutputStream.write(data, 0, len);
                if (byteArrayOutputStream.toString().contains("</response>")) {
                    _isOver = true;
                }
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
        try {
            return new String(byteArrayOutputStream.toByteArray(), "GBK");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void stopServer(Context pContext) {
        ifWhile = false;
        if (mClient != null) {
            mClient.clearRequestInterceptors();
            mClient.getConnectionManager().closeExpiredConnections();
            mClient.getConnectionManager().shutdown();
            mClient = null;
            CLog.e(TAG, "关闭请求链接：" + ifWhile);
        }
        pContext.stopService(new Intent(pContext, InstantMessageService.class));
    }

    private void reloginBackGround() {
        LoginUserDao _user = PageUtil.getLoginUserInfo();
        CLog.e(TAG, "Session Failed! Log in again*****************uid:" + _user.mUID + " psw:" + _user.mPsw);
        if (_user != null) {
            try {
                this.mloginuserid = _user.mUID;
                this.mloginuserpwd = _user.mPsw;
                new LoginMethod(this, this.mloginuserid, this.mloginuserpwd, this.mHandler).login();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void loginsuccess() {
        PageUtil.saveUserNameTOSharePre(this, this.mloginuserid);
        LoginUserDao _loginUserInfo = PageUtil.getLoginUserInfo();
        UserInfoDao user = new UserInfoDao();
        user.setmUserId(this.mloginuserid);
        user.setPsw(this.mloginuserpwd);
        user.setSavingMode(true);
        user.setLastLoginData(new Date());
        user.setmSex(_loginUserInfo.mSex);
        user.setmUserName(_loginUserInfo.mUserName);
        try {
            Dao<UserInfoDao, String> userDao = App_phms.getInstance().mHelper.getUserDao();
            UserInfoDao retUser = userDao.queryForId(this.mloginuserid);
            if (retUser == null) {
                App_phms.getInstance().mUserInfo.mSearchInterval = 10;
                App_phms.getInstance().mUserInfo.mBluetoothState = 1;
                App_phms.getInstance().mUserInfo.mLanguage = "1zh";
                user.setmSearchInterval(10);
                user.setmLanguage(Constants.Language);
                user.setmBluetoothState(1);
                userDao.create(user);
            } else {
                App_phms.getInstance().mUserInfo.mSearchInterval = retUser.getmSearchInterval();
                App_phms.getInstance().mUserInfo.mBluetoothState = retUser.getBluetoothstate();
                retUser.setmSex(App_phms.getInstance().mUserInfo.mSex);
                retUser.setmUserName(App_phms.getInstance().mUserInfo.mUserName);
                retUser.setmLanguage(Constants.Language);
                retUser.setLastLoginData(new Date());
                userDao.update(retUser);
            }
            PageUtil.getUserInfo();
            if (Constants.Language.contains("1")) {
                String _language = Locale.getDefault().getLanguage();
                Configuration config2 = getResources().getConfiguration();
                DisplayMetrics dm2 = getResources().getDisplayMetrics();
                if (_language.contains("zh")) {
                    config2.locale = Locale.CHINESE;
                } else if (_language.contains("en")) {
                    config2.locale = Locale.ENGLISH;
                }
                getResources().updateConfiguration(config2, dm2);
            } else if (Constants.Language.equals("en")) {
                Configuration config1 = getResources().getConfiguration();
                DisplayMetrics dm1 = getResources().getDisplayMetrics();
                config1.locale = Locale.ENGLISH;
                getResources().updateConfiguration(config1, dm1);
            } else if (Constants.Language.equals("zh")) {
                Configuration config = getResources().getConfiguration();
                DisplayMetrics dm = getResources().getDisplayMetrics();
                config.locale = Locale.CHINESE;
                getResources().updateConfiguration(config, dm);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        SharedPreferences.Editor _editor = App_phms.getInstance().mCurrentloginUserInfo.edit();
        _editor.putString("username", this.mloginuserid);
        _editor.putString("password", this.mloginuserpwd);
        _editor.commit();
        PhmsSharedPreferences.getInstance(this).saveColume("username", this.mloginuserid);
    }

    public IBinder onBind(Intent arg0) {
        return null;
    }
}
