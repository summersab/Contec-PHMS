package com.contec.phms.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import com.contec.phms.App_phms;
import com.contec.phms.R;
import com.contec.phms.eventbus.EventReLoadReport;
import com.contec.phms.login.LoginActivity;
import com.contec.phms.util.CLog;
import com.contec.phms.util.Constants;
import com.contec.phms.util.HttpPostDataUtil;
import com.contec.phms.util.PageUtil;
import com.contec.phms.util.PhmsSharedPreferences;
import com.contec.phms.widget.DialogClass;
import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import org.apache.http.util.EncodingUtils;
import u.aly.bs;

@SuppressLint({"NewApi"})
public class FragmentHealthReport extends FragmentWebViewBase {
    public static int mHealthReportIndex = 0;
    private DialogClass m_dialogClass;

    private PhmsSharedPreferences sp;

    public /* bridge */ /* synthetic */ Intent getFileIntent(File file) {
        return super.getFileIntent(file);
    }

    public /* bridge */ /* synthetic */ void installApk(Context context, String str) {
        super.installApk(context, str);
    }

    public /* bridge */ /* synthetic */ boolean isNetworkConnected(Context context) {
        return super.isNetworkConnected(context);
    }

    public /* bridge */ /* synthetic */ void onClick(View view) {
        super.onClick(view);
    }

    public /* bridge */ /* synthetic */ View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        return super.onCreateView(layoutInflater, viewGroup, bundle);
    }

    public /* bridge */ /* synthetic */ void onEvent(EventReLoadReport eventReLoadReport) {
        super.onEvent(eventReLoadReport);
    }

    public /* bridge */ /* synthetic */ boolean onKeyDown(int i, KeyEvent keyEvent) {
        return super.onKeyDown(i, keyEvent);
    }

    public /* bridge */ /* synthetic */ void onPause() {
        super.onPause();
    }

    public /* bridge */ /* synthetic */ void onResume() {
        super.onResume();
    }

    public /* bridge */ /* synthetic */ void onStop() {
        super.onStop();
    }

    public /* bridge */ /* synthetic */ void writeToSDCard(String str, InputStream inputStream, String str2) {
        super.writeToSDCard(str, inputStream, str2);
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void onDestroy() {
        super.onDestroy();
        CLog.dT("FragmentWebViewBase", "FragmentHealthReport onDestroy  清空webview的缓存");
        this.mWebView.stopLoading();
        clearCacheFolder(getActivity().getCacheDir(), System.currentTimeMillis());
        this.mWebView.destroyDrawingCache();
        this.mWebView.clearHistory();
        this.mWebView.clearFormData();
        this.mWebView.clearCache(true);
        this.mWebView.clearSslPreferences();
        this.mWebView.clearMatches();
        this.mWebView.removeAllViews();
    }

    private int clearCacheFolder(File dir, long numDays) {
        CookieSyncManager.createInstance(getActivity());
        CookieSyncManager.getInstance().startSync();
        CookieManager.getInstance().removeSessionCookie();
        int deletedFiles = 0;
        if (dir != null && dir.isDirectory()) {
            try {
                for (File child : dir.listFiles()) {
                    if (child.isDirectory()) {
                        deletedFiles += clearCacheFolder(child, numDays);
                    }
                    if (child.lastModified() < numDays && child.delete()) {
                        deletedFiles++;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return deletedFiles;
    }

    protected void setUrl() {
        String url;
        clearCacheFolder(getActivity().getCacheDir(), System.currentTimeMillis());
        this.mWebView.clearHistory();
        this.mWebView.clearFormData();
        this.mWebView.clearCache(true);
        this.mWebView.clearSslPreferences();
        this.mWebView.clearMatches();
        this.mWebView.removeAllViews();
        this.sp = PhmsSharedPreferences.getInstance(getActivity());
        String _username = LoginActivity.spUsername;
        String _pwd = LoginActivity.spPassword;
        CLog.eT("FragmentWebViewBase", String.valueOf(_username) + "  " + _pwd);
        String mPostDate = "&data[Card][uid]=" + _username + "&data[Card][pwd]=" + _pwd;
        CLog.dT("FragmentWebViewBase", "健康报告*********************+mPostDate" + mPostDate);
        if (Constants.Language.contains("1zh") || Constants.Language.contains("1en")) {
            if (!Locale.getDefault().getLanguage().contains("zh")) {
                url = String.valueOf(Constants.URL_REPORT) + "/user/mlogin/en?";
            } else {
                url = String.valueOf(Constants.URL_REPORT) + "/user/mlogin/zh?";
            }
        } else if (!Constants.Language.contains("zh")) {
            url = String.valueOf(Constants.URL_REPORT) + "/user/mlogin/en?";
        } else {
            url = String.valueOf(Constants.URL_REPORT) + "/user/mlogin/zh?";
        }
        this.mWebView.postUrl(url, EncodingUtils.getBytes(mPostDate, "GBK"));
        CLog.eT("FragmentWebViewBase", "健康报告*********************+url" + url);
        CLog.eT("FragmentWebViewBase", "posturl************** :" + Constants.URL_REPORT + "/user/mlogin");
    }

    public void onEvent(Message msg) {
        String url;
        String url2;
        if (msg.what == 523) {
            if (this.m_dialogClass != null) {
                this.m_dialogClass.dismiss();
            }
            new DialogClass(getActivity(), getResources().getString(R.string.user_networkerror));
        } else if (msg.what == 524) {
            this.mWebView.postUrl(this.mUrl, (byte[]) null);
            if (this.m_dialogClass != null) {
                this.m_dialogClass.dismiss();
            }
        } else if (msg.what == 546) {
            String _username = PageUtil.getLoginUserInfo().mUID;
            String _pwd = PageUtil.getLoginUserInfo().mPsw;
            CLog.eT("FragmentWebViewBase", String.valueOf(_username) + "  " + _pwd);
            String mPostDate = "&data[Card][uid]=" + _username + "&data[Card][pwd]=" + _pwd;
            CLog.dT("FragmentWebViewBase", "健康建议*********************+mPostDate" + mPostDate);
            if (Constants.Language.contains("1zh") || Constants.Language.contains("1en")) {
                if (!Locale.getDefault().getLanguage().contains("zh")) {
                    url2 = String.valueOf(Constants.URL_REPORT) + "/user/mlogin1/en?";
                } else {
                    url2 = String.valueOf(Constants.URL_REPORT) + "/user/mlogin1/zh?";
                }
            } else if (!Constants.Language.contains("zh")) {
                url2 = String.valueOf(Constants.URL_REPORT) + "/user/mlogin1/en?";
            } else {
                url2 = String.valueOf(Constants.URL_REPORT) + "/user/mlogin1/zh?";
            }
            this.mWebView.postUrl(url2, EncodingUtils.getBytes(mPostDate, "GBK"));
            CLog.dT("FragmentWebViewBase", "健康建议*********************+url" + url2);
            mHealthReportIndex = 1;
        } else if (msg.what == 547) {
            String _username2 = PageUtil.getLoginUserInfo().mUID;
            String _pwd2 = PageUtil.getLoginUserInfo().mPsw;
            CLog.eT("FragmentWebViewBase", String.valueOf(_username2) + "  " + _pwd2);
            String mPostDate2 = "&data[Card][uid]=" + _username2 + "&data[Card][pwd]=" + _pwd2;
            CLog.dT("FragmentWebViewBase", " user信息*********************+mPostDate" + mPostDate2);
            if (Constants.Language.contains("1zh") || Constants.Language.contains("1en")) {
                if (!Locale.getDefault().getLanguage().contains("zh")) {
                    url = String.valueOf(Constants.URL_REPORT) + "/user/mlogin2/en?";
                } else {
                    url = String.valueOf(Constants.URL_REPORT) + "/user/mlogin2/zh?";
                }
            } else if (!Constants.Language.contains("zh")) {
                url = String.valueOf(Constants.URL_REPORT) + "/user/mlogin2/en?";
            } else {
                url = String.valueOf(Constants.URL_REPORT) + "/user/mlogin2/zh?";
            }
            this.mWebView.postUrl(url, EncodingUtils.getBytes(mPostDate2, "GBK"));
            CLog.dT("FragmentWebViewBase", " user信息*********************url: " + url);
            mHealthReportIndex = 2;
        } else if (msg.what == 548) {
            mHealthReportIndex = 0;
            CLog.dT("FragmentWebViewBase", "健康报告**********************");
            Map<String, String> additionalHttpHeaders = new HashMap<>();
            if (Constants.Language.contains("1zh") || Constants.Language.contains("1en")) {
                if (!Locale.getDefault().getLanguage().contains("zh")) {
                    additionalHttpHeaders.put("Accept-Language", "en-us,en");
                } else {
                    additionalHttpHeaders.put("Accept-Language", "zh-cn,zh");
                }
            } else if (!Constants.Language.contains("zh")) {
                additionalHttpHeaders.put("Accept-Language", "en-us,en");
            } else {
                additionalHttpHeaders.put("Accept-Language", "zh-cn,zh");
            }
            additionalHttpHeaders.put("Charset", "GBK");
            this.mWebView.loadUrl(String.valueOf(Constants.URL_REPORT) + "/mobile");
        }
    }

    private void startReport() {
        new Thread() {
            public void run() {
                String _md5String = HttpPostDataUtil.getInstance().postRequestData(Constants.REPORT_URL_CHECK);
                if (_md5String == null || _md5String.equals(bs.b)) {
                    Message msgs = new Message();
                    msgs.what = Constants.V_DISMISS_DIALOGE;
                    App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(msgs);
                    return;
                }
                FragmentHealthReport.this.mUrl = String.valueOf(Constants.REPORT_URL_GET) + _md5String;
                Message msgs2 = new Message();
                msgs2.what = Constants.V_GET_REPORT_MD5;
                App_phms.getInstance().mEventBus.post(msgs2);
            }
        }.start();
    }

    protected void reloadWebView() {
        CLog.dT("FragmentWebViewBase", "FragmentCheckReport----reloadWebView");
        this.mShowFlag = true;
        mHandler.postDelayed(this.mSecondDelay, 20000);
        this.mWebView.clearHistory();
        clearCacheFolder(getActivity().getCacheDir(), System.currentTimeMillis());
        this.mWebView.clearHistory();
        this.mWebView.clearFormData();
        this.mWebView.clearCache(true);
        this.mWebView.clearSslPreferences();
        this.mWebView.clearMatches();
        this.mWebView.removeAllViews();
        String _username = PageUtil.getLoginUserInfo().mUID;
        String _pwd = PageUtil.getLoginUserInfo().mPsw;
        CLog.dT("FragmentWebViewBase", String.valueOf(_username) + "  " + _pwd);
        this.mWebView.postUrl(String.valueOf(Constants.URL_REPORT) + "/user/mlogin?", EncodingUtils.getBytes("&data[Card][uid]=" + _username + "&data[Card][pwd]=" + _pwd, "GBK"));
        CLog.dT("FragmentWebViewBase", "posturl************** :" + this.mWebView.getUrl());
    }
}
