package com.contec.phms.util;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.contec.phms.App_phms;
import com.contec.phms.R;
import com.contec.phms.device.cms50k.update.Update50KUtils;
import com.contec.phms.infos.UserInfo;
import com.contec.phms.db.LoginUserDao;
import com.contec.phms.db.UserInfoDao;
import com.contec.phms.upload.UploadService;
import com.contec.phms.widget.DialogClass;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import org.apache.commons.httpclient.cookie.CookieSpec;
import u.aly.bs;

public class PageUtil {
    private static String TAG = "PageUtil";
    private static final String Tag = PageUtil.class.getSimpleName();
    private static long m_lastClickTime;
    public static Toast toast;

    public enum BroadcastPacket {
        RED,
        BLUE,
        GREEN
    }

    public static void delTitle(Activity pActivity) {
        pActivity.requestWindowFeature(1);
        pActivity.getWindow().setFlags(1024, 1024);
    }

    public static boolean isNumeric(String str) {
        int chr;
        int i = str.length();
        do {
            i--;
            if (i >= 0) {
                chr = str.charAt(i);
                if (chr < 48) {
                    break;
                }
            } else {
                return true;
            }
        } while (chr <= 57);
        return false;
    }

    public static String addUUID(String pPath) {
        File _datFile = new File(pPath);
        byte[] _b = new byte[2014];
        try {
            OutputStream _out = new FileOutputStream(new File(String.valueOf(pPath) + ".UUID"));
            _out.write("C8802A95-FAEB-5664-722F-DD85B551C4C6".getBytes());
            InputStream _in = new FileInputStream(_datFile);
            while (true) {
                int _n = _in.read(_b);
                if (_n == -1) {
                    break;
                }
                _out.write(_b, 0, _n);
            }
            _in.close();
            _out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return String.valueOf(pPath) + ".UUID";
    }

    public static String getNetType(Context pContext) {
        NetworkInfo netWrokInfo = ((ConnectivityManager) pContext.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        if (netWrokInfo == null || !netWrokInfo.isAvailable()) {
            return "null";
        }
        return netWrokInfo.getTypeName();
    }

    public static boolean checkSDCard(Activity pActivity) {
        if (!Environment.getExternalStorageState().equals("mounted")) {
            return false;
        }
        AssetManager _AssetManager = pActivity.getAssets();
        File _file = new File(Environment.getExternalStorageDirectory(), "/config.ini");
        try {
            InputStream _InputStream = _AssetManager.open("config.ini", 0);
            FileOutputStream _FileOutputStream = new FileOutputStream(_file);
            byte[] _buffer = new byte[1024];
            while (true) {
                int _byteread = _InputStream.read(_buffer);
                if (_byteread == -1) {
                    break;
                }
                _FileOutputStream.write(_buffer, 0, _byteread);
            }
            _InputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    public static int getFailedTimes(String pPath, String pFileMAC) {
        String[] _FailedFile = UploadService.getFileList(Constants.UPLOAD_FIAIL_DADT);
        int N = 0;
        if (_FailedFile == null) {
            return 0;
        }
        for (String contains : _FailedFile) {
            if (contains.contains(pFileMAC)) {
                N++;
            }
        }
        return N;
    }

    public static String[] getUserNameFromSharePre(Context pContext) {
        String[] _users = null;
        String _shareUsers = pContext.getSharedPreferences(Constants.BLUE_LIST_SHARE, 0).getString(Constants.USERS, bs.b);
        if (_shareUsers == null || bs.b.equals(_shareUsers)) {
            return _users;
        }
        return _shareUsers.split(Constants.DOUHAO);
    }

    public static boolean isPersonalUser(String plogName, Context pContext) {
        if (plogName == null || !plogName.matches("(?i)[^a-z]*[a-z]+[^a-z]*")) {
            return false;
        }
        new DialogClass(pContext, pContext.getResources().getString(R.string.net_disable));
        return true;
    }

    public static boolean isPersonalUser(String plogName) {
        if (plogName == null || !plogName.matches("(?i)[^a-z]*[a-z]+[^a-z]*")) {
            return false;
        }
        return true;
    }

    public static boolean checkNull(String strUsername, String strPassword, Context pContext) {
        boolean _null = false;
        if (strUsername == null || bs.b.equals(strUsername) || strPassword == null || bs.b.equals(strPassword)) {
            _null = true;
            if (strUsername == null || bs.b.equals(strUsername)) {
                new DialogClass(pContext, pContext.getResources().getString(R.string.str_please_putin_num));
            } else if (strPassword == null || bs.b.equals(strPassword)) {
                new DialogClass(pContext, pContext.getResources().getString(R.string.str_please_putin_passwd));
            }
        }
        return _null;
    }

    public static boolean checkNet(Context pContext) {
        NetworkInfo netWrokInfo = ((ConnectivityManager) pContext.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        if (netWrokInfo != null && netWrokInfo.isAvailable()) {
            return true;
        }
        Constants.NET_ERROR = 1;
        return false;
    }

    public static void saveServerNameTOSharePre(Context pContext, String pServerName) {
        SharedPreferences _Preferences = pContext.getSharedPreferences(Constants.BLUE_LIST_SHARE, 0);
        String _shareUsers = _Preferences.getString(Constants.SERVER_NAME, bs.b);
        if (_shareUsers == null || bs.b.equals(_shareUsers)) {
            _Preferences.edit().putString(Constants.SERVER_NAME, pServerName).commit();
        } else if (!pServerName.equals(_shareUsers)) {
            _Preferences.edit().putString(Constants.SERVER_NAME, pServerName).commit();
        }
    }

    public static String[] getServerNameFromSharePre(Context pContext) {
        String[] _shareUsers = pContext.getSharedPreferences(Constants.BLUE_LIST_SHARE, 0).getString(Constants.SERVER_NAME, bs.b).split(";");
        if (_shareUsers.length != 1) {
            return _shareUsers;
        }
        return new String[]{Update50KUtils.MainURl, "http://newm.contec365.com"};
    }

    public static void saveUserNameTOSharePre(Context pContext, String pUsername) {
        int _key = 0;
        SharedPreferences _Preferences = pContext.getSharedPreferences(Constants.BLUE_LIST_SHARE, 0);
        String _shareUsers = _Preferences.getString(Constants.USERS, bs.b);
        if (_shareUsers == null || bs.b.equals(_shareUsers)) {
            _Preferences.edit().putString(Constants.USERS, pUsername).commit();
            return;
        }
        String[] _users = _shareUsers.split(Constants.DOUHAO);
        int i = 0;
        while (true) {
            if (i >= _users.length) {
                break;
            } else if (_users[i].equals(pUsername)) {
                _key = 1;
                break;
            } else {
                i++;
            }
        }
        if (_key == 0) {
            _Preferences.edit().putString(Constants.USERS, String.valueOf(_shareUsers) + Constants.DOUHAO + pUsername).commit();
        }
    }

    public static void installPdfReader(Context pContext) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.setDataAndType(Uri.parse("file:///android_asset/pdf.apk"), "application/vnd.android.package-archive");
        pContext.startActivity(intent);
    }

    public static String getNowALLTime() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date());
    }

    public static String getNowDate() {
        return new SimpleDateFormat("yyyy-MM-dd").format(new Date());
    }

    public static void kill_background(Context p_Context) {
        ActivityManager activityManger = (ActivityManager) p_Context.getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo mInfo = new ActivityManager.MemoryInfo();
        Log.i("before kill ", new StringBuilder(String.valueOf(mInfo.availMem)).toString());
        activityManger.getMemoryInfo(mInfo);
        List<ActivityManager.RunningAppProcessInfo> list = activityManger.getRunningAppProcesses();
        if (list != null) {
            for (int i = 0; i < list.size(); i++) {
                ActivityManager.RunningAppProcessInfo apinfo = list.get(i);
                String[] pkgList = apinfo.pkgList;
                if (apinfo.importance > 300) {
                    for (String killBackgroundProcesses : pkgList) {
                        activityManger.killBackgroundProcesses(killBackgroundProcesses);
                    }
                }
            }
        }
        Log.i("after kill ", new StringBuilder(String.valueOf(mInfo.availMem)).toString());
    }

    public static boolean isFastDoubleClick() {
        long _time = System.currentTimeMillis();
        long _dis_time = _time - m_lastClickTime;
        if (0 >= _dis_time || _dis_time >= 800) {
            m_lastClickTime = _time;
            return false;
        }
        CLog.i(Tag, "is fast click");
        return true;
    }

    public static long getSDFreeSize_BYTE() {
        StatFs sf = new StatFs(Environment.getExternalStorageDirectory().getPath());
        return ((long) sf.getAvailableBlocks()) * ((long) sf.getBlockSize());
    }

    public static void isHavePath(String path) {
        File _file = new File(path);
        if (!_file.exists()) {
            _file.mkdirs();
        }
    }

    public static String getCurrentTime(String format) {
        return new SimpleDateFormat(format).format(new Date());
    }

    public static void makedir(String path) {
        File file = new File(path);
        if (!file.exists()) {
            file.mkdir();
        }
    }

    public static LoginUserDao getUserInfo() {
        UserInfoDao _userDao;
        LoginUserDao _loginUserDao = null;
        try {
            List<LoginUserDao> _listLoginUserDao = App_phms.getInstance().mHelper.getLoginUserDao().queryForAll();
            if (!(_listLoginUserDao == null || _listLoginUserDao.size() <= 0 || (_loginUserDao = _listLoginUserDao.get(0)) == null || (_userDao = App_phms.getInstance().mHelper.getUserDao().queryForId(_loginUserDao.mUID)) == null || _userDao.equals(bs.b))) {
                App_phms.getInstance().mUserInfo.mUserName = _userDao.getmUserName();
                App_phms.getInstance().mUserInfo.mPassword = _userDao.getPsw();
                App_phms.getInstance().mUserInfo.mSex = _loginUserDao.mSex;
                App_phms.getInstance().mUserInfo.mPHPSession = _loginUserDao.mSID;
                App_phms.getInstance().mUserInfo.mUserID = _loginUserDao.mUID;
                App_phms.getInstance().mUserInfo.mSearchInterval = _userDao.getmSearchInterval();
                App_phms.getInstance().mUserInfo.mBluetoothState = _userDao.getBluetoothstate();
                App_phms.getInstance().mUserInfo.mLanguage = _userDao.getmLanguage();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return _loginUserDao;
    }

    public static LoginUserDao getLoginUserInfo() {
        try {
            List<LoginUserDao> _userlist = App_phms.getInstance().mHelper.getLoginUserDao().queryForAll();
            if (_userlist == null || _userlist.size() <= 0) {
                return null;
            }
            return _userlist.get(0);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void checkUserinfo(UserInfo pUserInfo, final Context pcontext) {
        if (pUserInfo == null || pUserInfo.mUserName == null || pUserInfo.mUserID == null || pUserInfo.mSex == null || pUserInfo.mLanguage == null || pUserInfo.mUserID.equals(bs.b)) {
            App_phms.getInstance().mUserInfo = new UserInfo();
            App_phms.getInstance().exitall(0);
            new Thread() {
                public void run() {
                    try {
                        Thread.sleep(1000);
                        Intent i = pcontext.getPackageManager().getLaunchIntentForPackage(pcontext.getPackageName());
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        pcontext.startActivity(i);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }.start();
        }
    }

    public static String processDate(int mYear, int mMonth, int mDay, int mHour, int mMinute, int mSs) {
        String _month;
        String _day;
        String _hour;
        String _minute;
        String _ss;
        CLog.e("processdate", "mYear:" + mYear);
        if (mMonth < 10) {
            _month = "0" + mMonth;
        } else {
            _month = new StringBuilder(String.valueOf(mMonth)).toString();
        }
        if (mDay < 10) {
            _day = "0" + mDay;
        } else {
            _day = new StringBuilder(String.valueOf(mDay)).toString();
        }
        if (mHour < 10) {
            _hour = "0" + mHour;
        } else {
            _hour = new StringBuilder(String.valueOf(mHour)).toString();
        }
        if (mMinute < 10) {
            _minute = "0" + mMinute;
        } else {
            _minute = new StringBuilder(String.valueOf(mMinute)).toString();
        }
        if (mSs < 10) {
            _ss = "0" + mSs;
        } else {
            _ss = new StringBuilder(String.valueOf(mSs)).toString();
        }
        return String.valueOf(mYear) + CookieSpec.PATH_DELIM + _month + CookieSpec.PATH_DELIM + _day + " " + _hour + ":" + _minute + ":" + _ss;
    }

    public static String process_Date(int mYear, int mMonth, int mDay, int mHour, int mMinute, int mSs) {
        String _month;
        String _day;
        String _hour;
        String _minute;
        String _ss;
        CLog.e("processdate", "mYear:" + mYear);
        if (mMonth < 10) {
            _month = "0" + mMonth;
        } else {
            _month = new StringBuilder(String.valueOf(mMonth)).toString();
        }
        if (mDay < 10) {
            _day = "0" + mDay;
        } else {
            _day = new StringBuilder(String.valueOf(mDay)).toString();
        }
        if (mHour < 10) {
            _hour = "0" + mHour;
        } else {
            _hour = new StringBuilder(String.valueOf(mHour)).toString();
        }
        if (mMinute < 10) {
            _minute = "0" + mMinute;
        } else {
            _minute = new StringBuilder(String.valueOf(mMinute)).toString();
        }
        if (mSs < 10) {
            _ss = "0" + mSs;
        } else {
            _ss = new StringBuilder(String.valueOf(mSs)).toString();
        }
        return String.valueOf(mYear) + "-" + _month + "-" + _day + " " + _hour + ":" + _minute + ":" + _ss;
    }

    public static String getStringTime(long pdate) {
        SimpleDateFormat _for = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date date = new Date();
            date.setTime(pdate);
            return _for.format(date);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static byte[] getStringTimeByteNow() {
        byte[] _timeByte = new byte[6];
        try {
            Calendar c = Calendar.getInstance();
            int _min = c.get(12);
            if (_min < 10) {
                c.add(11, -1);
                _min = 59;
            }
            _timeByte[0] = (byte) (c.get(1) - 2000);
            _timeByte[1] = (byte) (c.get(2) + 1);
            _timeByte[2] = (byte) c.get(5);
            _timeByte[3] = (byte) c.get(11);
            Random r = new Random();
            int _min2 = r.nextInt(_min);
            int _ss = r.nextInt(59);
            _timeByte[4] = (byte) _min2;
            _timeByte[5] = (byte) _ss;
            return _timeByte;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getDateFormByte(byte pyear, byte pmonth, byte pday, byte phour, byte pmin, byte pss) {
        StringBuffer _date = new StringBuffer();
        if (pyear < 10) {
            _date.append("200" + pyear);
        } else {
            _date.append("20" + pyear);
        }
        _date.append("-");
        if (pmonth < 10) {
            _date.append("0" + pmonth);
        } else {
            _date.append(pmonth);
        }
        _date.append("-");
        if (pday < 10) {
            _date.append("0" + pday);
        } else {
            _date.append(pday);
        }
        _date.append(" ");
        if (phour < 10) {
            _date.append("0" + phour);
        } else {
            _date.append(phour);
        }
        _date.append(":");
        if (pmin < 10) {
            _date.append("0" + pmin);
        } else {
            _date.append(pmin);
        }
        _date.append(":");
        if (pss < 10) {
            _date.append("0" + pss);
        } else {
            _date.append(pss);
        }
        return _date.toString();
    }

    public static byte[] compareDate(Calendar pDate) {
        Calendar _calendar = Calendar.getInstance();
        _calendar.add(1, -1);
        String _startTime = getStringTime(_calendar.getTimeInMillis());
        _calendar.add(1, 1);
        _calendar.add(2, 1);
        String _endTime = getStringTime(_calendar.getTimeInMillis());
        boolean _start = compareDate(getStringTime(pDate.getTimeInMillis()), _startTime);
        boolean _end = compareDate(getStringTime(pDate.getTimeInMillis()), _endTime);
        if (!_start && _end) {
            return null;
        }
        byte[] _nowDateByte = getStringTimeByteNow();
        CLog.d("ReceiveThread", " Invalid date pDate: " + pDate + "  startime:" + _startTime + "  " + _endTime + "  \nyear:" + _nowDateByte[0] + " month:" + _nowDateByte[1] + " day:" + _nowDateByte[2] + "  hour:" + _nowDateByte[3] + "  min:" + _nowDateByte[4] + " ss:" + _nowDateByte[5] + " \n" + "start:" + _start + " end:" + _end);
        return _nowDateByte;
    }

    public static byte[] compareDate(String pDate) {
        long netTime = PhmsSharedPreferences.getInstance(App_phms.getInstance().getBaseContext()).getLong(Constants.INTERNET_TIME);
        long loginTime = PhmsSharedPreferences.getInstance(App_phms.getInstance().getBaseContext()).getLong(Constants.LOCAL_TIME);
        long nowTime = System.currentTimeMillis();
        if (netTime != 0) {
            nowTime = (nowTime - loginTime) + netTime;
        }
        Calendar _calendar = Calendar.getInstance();
        _calendar.setTimeInMillis(nowTime);
        String nowdateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(_calendar.getTime());
        FileOperation.writeToSDCard("对比时间中*********：存储的网络时间：" + getDateTime(netTime) + "   本地登录的时间：" + getDateTime(loginTime) + "  加减之后的时间：" + nowdateTime, "Cms50D_BT");
        FileOperation.writeToSDCard("对比时间中*********：存储的网络时间：" + getDateTime(netTime) + "   本地登录的时间：" + getDateTime(loginTime) + "  加减之后的时间：" + nowdateTime, "Cms50E_BT");
        CLog.dT(TAG, "存储的网络时间：" + netTime + "   本地登录的时间：" + loginTime + "  加减之后的时间：" + nowdateTime);
        _calendar.add(1, -1);
        String _startTime = getStringTime(_calendar.getTimeInMillis());
        _calendar.add(1, 1);
        _calendar.add(2, 1);
        String _endTime = getStringTime(_calendar.getTimeInMillis());
        boolean _start = compareDate(pDate, _startTime);
        boolean _end = compareDate(pDate, _endTime);
        if (!_start && _end) {
            return null;
        }
        byte[] _nowDateByte = getStringTimeByteNow();
        CLog.d("ReceiveThread", " 不合法日期pDate:" + pDate + "  startime:" + _startTime + "  " + _endTime + "  \nyear:" + _nowDateByte[0] + " month:" + _nowDateByte[1] + " day:" + _nowDateByte[2] + "  hour:" + _nowDateByte[3] + "  min:" + _nowDateByte[4] + " ss:" + _nowDateByte[5] + " \n" + "start:" + _start + " end:" + _end);
        return _nowDateByte;
    }

    private static String getDateTime(long timemill) {
        Calendar _calendar = Calendar.getInstance();
        _calendar.setTimeInMillis(timemill);
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(_calendar.getTime());
    }

    private static boolean compareDate(String dataofdate, String endtime) {
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (dataofdate == null || endtime == null) {
            return false;
        }
        try {
            if (sf.parse(dataofdate).getTime() <= sf.parse(endtime).getTime()) {
                return true;
            }
            return false;
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static String toASCLL(String s) {
        byte[] baKeyword = new byte[(s.length() / 2)];
        for (int i = 0; i < baKeyword.length; i++) {
            try {
                baKeyword[i] = (byte) (Integer.parseInt(s.substring(i * 2, (i * 2) + 2), 16) & 255);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            //return new String(baKeyword, NTLM.DEFAULT_CHARSET);
            return new String(baKeyword, "ASCII");
        } catch (Exception e1) {
            e1.printStackTrace();
            return s;
        }
    }

    public static boolean isPhoneSupportBLE(Context context) {
        if (!context.getPackageManager().hasSystemFeature("android.hardware.bluetooth_le") || Build.VERSION.SDK_INT < 18) {
            return false;
        }
        return true;
    }

    public static void measureView(View child) {
        int childMeasureHeight;
        ViewGroup.LayoutParams lp = child.getLayoutParams();
        if (lp == null) {
            lp = new ViewGroup.LayoutParams(-1, -2);
        }
        int childMeasureWidth = ViewGroup.getChildMeasureSpec(0, 0, lp.width);
        if (lp.height > 0) {
            childMeasureHeight = View.MeasureSpec.makeMeasureSpec(lp.height, View.MeasureSpec.AT_MOST);
        } else {
            childMeasureHeight = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        }
        child.measure(childMeasureWidth, childMeasureHeight);
    }

    public static void showToast(Context mContext, String msg) {
        if (toast == null) {
            toast = Toast.makeText(mContext, bs.b, Toast.LENGTH_SHORT);
        }
        toast.setText(msg);
        toast.show();
    }

    public static Context getTopActivity() {
        Activity currentActivity = MyActivityManager.getInstance().getCurrentActivity();
        CLog.i("jxx", "代码执行到currentActivity: " + currentActivity);
        return currentActivity;
    }

    public static String getTopActivityClassName() {
        String ActivityClassName = bs.b;
        List<ActivityManager.RunningTaskInfo> runningTaskInfos = ((ActivityManager) App_phms.getInstance().getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE)).getRunningTasks(1);
        if (runningTaskInfos != null) {
            ActivityClassName = runningTaskInfos.get(0).topActivity.getClassName();
        }
        CLog.i("jxx", "栈顶的Activity的名字为：" + ActivityClassName);
        return ActivityClassName;
    }

    public static BroadcastPacket unPackData(byte[] scanRecord) {
        if (scanRecord == null || scanRecord.length == 0) {
            return BroadcastPacket.RED;
        }
        StringBuilder builder2 = new StringBuilder();
        StringBuilder builder3 = new StringBuilder();
        for (int i = 0; i < scanRecord.length; i++) {
            builder2.append(toASCLL(Integer.toHexString(scanRecord[i])));
            builder3.append(String.valueOf(Integer.toHexString(scanRecord[i])) + " ");
        }
        CLog.i(TAG, "原数据:" + builder3.toString());
        CLog.i(TAG, "处理后的数据:" + builder2.toString() + "\n-----");
        String str = builder2.toString();
        if (str == null) {
            return BroadcastPacket.RED;
        }
        if (!str.contains("DT")) {
            CLog.e("没有数据的时候", "============时间不正确时候进行发送命令");
            return BroadcastPacket.RED;
        } else if (str.contains("DATA")) {
            return BroadcastPacket.GREEN;
        } else {
            if (str.contains(getCurrTime())) {
                CLog.e("没有数据的时候", "============时间正确===不发送命令" + getCurrTime());
                return BroadcastPacket.BLUE;
            }
            CLog.e("没有数据的时候", "============时间[不正确]===发送命令" + getCurrTime());
            return BroadcastPacket.GREEN;
        }
    }

    private static String getCurrTime() {
        String mCurrmonth;
        String mCurrdate;
        Calendar c = Calendar.getInstance();
        int year = c.get(1);
        int month = c.get(2) + 1;
        int date = c.get(5);
        String mCurryear = new StringBuilder().append(year).toString().substring(2, 4);
        if (month < 10) {
            mCurrmonth = "0" + month;
        } else {
            mCurrmonth = new StringBuilder().append(month).toString();
        }
        if (date < 10) {
            mCurrdate = "0" + date;
        } else {
            mCurrdate = new StringBuilder().append(date).toString();
        }
        return String.valueOf(mCurryear) + mCurrmonth + mCurrdate;
    }
}
