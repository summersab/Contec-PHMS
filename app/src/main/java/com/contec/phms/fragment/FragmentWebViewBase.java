package com.contec.phms.fragment;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.CookieManager;
import android.webkit.DownloadListener;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.contec.phms.App_phms;
import com.contec.phms.R;
import com.contec.phms.ajaxcallback.AjaxCallBack_login;
import com.contec.phms.eventbus.EventReLoadReport;
import com.contec.phms.manager.message.InstantMessageService;
import com.contec.phms.db.DeviceListDaoOperation;
import com.contec.phms.db.LoginUserDao;
import com.contec.phms.db.UserInfoDao;
import com.contec.phms.db.localdata.BloodDDataDao;
import com.contec.phms.db.localdata.CmssxtDataDao;
import com.contec.phms.db.localdata.FetalHeartDataDao;
import com.contec.phms.db.localdata.FvcDataDao;
import com.contec.phms.db.localdata.PedometerDayDataDao;
import com.contec.phms.db.localdata.PluseDataDao;
import com.contec.phms.db.localdata.Spo2DataDao;
import com.contec.phms.db.localdata.TempertureDataDao;
import com.contec.phms.db.localdata.UrineDataDao;
import com.contec.phms.db.localdata.WeightDataDao;
import com.contec.phms.db.localdata.opration.BloodDDataDaoOperation;
import com.contec.phms.db.localdata.opration.CmssxtDataDaoOperation;
import com.contec.phms.db.localdata.opration.FeltalHeartDataDaoOperation;
import com.contec.phms.db.localdata.opration.FvcDataDaoOperation;
import com.contec.phms.db.localdata.opration.PedometerDayDataDaoOperation;
import com.contec.phms.db.localdata.opration.PluseDataDaoOperation;
import com.contec.phms.db.localdata.opration.Spo2DataDaoOperation;
import com.contec.phms.db.localdata.opration.TempertureDataDaoOperation;
import com.contec.phms.db.localdata.opration.UrineDataDaoOperation;
import com.contec.phms.db.localdata.opration.WeightDataDaoOperation;
import com.contec.phms.util.CLog;
import com.contec.phms.util.Constants;
import com.contec.phms.util.DownloadApkThread;
import com.contec.phms.util.PageUtil;
import com.contec.phms.util.PhmsSharedPreferences;
import com.contec.phms.util.ScreenAdapter;
import com.contec.phms.widget.DialogClass;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.query.SimpleComparison;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.Timer;
import java.util.TimerTask;
import org.apache.commons.httpclient.cookie.CookieSpec;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import cn.com.contec_net_3_android.Method_android_login;
import de.greenrobot.event.EventBus;
import u.aly.bs;

@TargetApi(11)
abstract class FragmentWebViewBase extends FragmentBase implements View.OnClickListener {
    protected static final String TAG = "FragmentWebViewBase";
    protected static Handler mHandler = new Handler();
    public static File savefile;
    private final int DOWNLOAD = 222;
    private final int DOWNLOAD_FINISH = 333;
    LocalDataAdapter adapter;
    private DownloadApkThread downloadApkThread;
    protected LinearLayout errorLayoutShow;
    private List<LocalDataAdapterData> listData;
    protected boolean mChangePage = false;
    protected TextView mCheckReportNetInfoTV;
    protected int mCurrentProgress = 0;
    private ProgressDialog mDialog;
    protected DialogClass mDialogClass = null;
    private Handler mDownloadhandler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 222) {
                FragmentWebViewBase.this.setProgress(msg.arg1);
            } else if (msg.what == 333) {
                FragmentWebViewBase.this.setProgress(100);
                if (FragmentWebViewBase.this.progressdialog.isShowing()) {
                    FragmentWebViewBase.this.progressdialog.dismiss();
                }
                FragmentWebViewBase.this.installApk(FragmentWebViewBase.this.getActivity(), Environment.getExternalStorageDirectory() + CookieSpec.PATH_DELIM + "phms_download");
            }
        }
    };
    protected EventBus mEventBus = App_phms.getInstance().mEventBus;
    private Handler mHandlerLogin;
    protected LinearLayout mLoadErrorLayout;
    protected String mPostUserInfo = bs.b;
    protected ProgressBar mProBar;
    protected SecondDelay mSecondDelay;
    String mSession = bs.b;
    protected boolean mShowFlag = false;
    protected final long mTimeout = 20000;
    private Timer mTimer;
    protected String mUrl;
    protected View mView;
    protected WebSettings mWebSettings;
    public WebView mWebView;
    private ProgressBar mgrogress;
    private FrameLayout mlayout_checkreport_main;
    protected ListView no_hava_data;
    private Dialog progressdialog;
    private TextView progresstext;

    protected abstract void reloadWebView();

    protected abstract void setUrl();

    FragmentWebViewBase() {
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void timer() {
        this.mTimer = new Timer();
        this.mTimer.schedule(new TimerTask() {
            public void run() {
                CLog.eT(FragmentWebViewBase.TAG, "webview 开始计时 **********************");
                FragmentWebViewBase.this.mWebView.reload();
                if (FragmentWebViewBase.this.mTimer != null) {
                    if (FragmentWebViewBase.this.mTimer != null) {
                        FragmentWebViewBase.this.mTimer.cancel();
                    }
                    if (FragmentWebViewBase.this.mTimer != null) {
                        FragmentWebViewBase.this.mTimer.purge();
                    }
                    FragmentWebViewBase.this.timer();
                }
            }
        }, 900000);
    }

    public class SecondDelay implements Runnable {
        public SecondDelay() {
        }

        public void run() {
            CLog.dT(FragmentWebViewBase.TAG, "20秒时间到了*****************************");
            FragmentWebViewBase.this.mWebView.reload();
        }
    }

    public void onEvent(EventReLoadReport pEventReLoadReport) {
        CLog.eT("reload", ">>>>>>>>>>>>>>>>>>>>reload");
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CLog.eT(TAG, " FragmentWebViewBase  onCreateView********************************");
        this.mSecondDelay = new SecondDelay();
        this.mView = LayoutInflater.from(getActivity()).inflate(R.layout.layout_activity_checkreport, container, false);
        this.listData = new ArrayList();
        initView(this.mView);
        this.mHandlerLogin = new Handler() {
            public void dispatchMessage(Message msg) {
                switch (msg.what) {
                    case 65536:
                        FragmentWebViewBase.this.getActivity().startService(new Intent(FragmentWebViewBase.this.getActivity(), InstantMessageService.class));
                        FragmentWebViewBase.this.loginsuccess();
                        return;
                    case Constants.LOGINFAILD /*65537*/:
                        FragmentWebViewBase.this.getActivity().startService(new Intent(FragmentWebViewBase.this.getActivity(), InstantMessageService.class));
                        CLog.dT(FragmentWebViewBase.TAG, "登陆失败***********");
                        return;
                    default:
                        return;
                }
            }
        };
        App_phms.getInstance().mEventBus.register(this);
        initWebSettings();
        return this.mView;
    }

    private void initView(View pView) {
        this.mlayout_checkreport_main = (FrameLayout) pView.findViewById(R.id.layout_checkreport_main);
        if (Constants.IS_PAD_NEW) {
            ScreenAdapter.changeLayoutTextSize(getActivity(), this.mlayout_checkreport_main, 10);
        }
        this.mProBar = (ProgressBar) pView.findViewById(R.id.layout_activity_check_report_progressbar);
        this.mWebView = null;
        this.mWebView = (WebView) pView.findViewById(R.id.layout_activity_check_report_webview);
        this.mCheckReportNetInfoTV = (TextView) pView.findViewById(R.id.checkreport_netinfo_tv);
        this.mLoadErrorLayout = (LinearLayout) pView.findViewById(R.id.loaderror_layout);
        this.errorLayoutShow = (LinearLayout) pView.findViewById(R.id.no_local_data);
        this.no_hava_data = (ListView) pView.findViewById(R.id.no_net_hava_data);
        this.adapter = new LocalDataAdapter(this.listData, getActivity());
        this.no_hava_data.addHeaderView((LinearLayout) LayoutInflater.from(getActivity()).inflate(R.layout.localdatahead, (ViewGroup) null));
        this.no_hava_data.setAdapter(this.adapter);
    }

    private class MyWebViewDownLoadListener implements DownloadListener {
        private MyWebViewDownLoadListener() {
        }

        /* synthetic */ MyWebViewDownLoadListener(FragmentWebViewBase fragmentWebViewBase, MyWebViewDownLoadListener myWebViewDownLoadListener) {
            this();
        }

        public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
            System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>.");
            if (!Environment.getExternalStorageState().equals("mounted")) {
                Toast t = Toast.makeText(FragmentWebViewBase.this.getActivity(), "SD card required。", Toast.LENGTH_LONG);
                t.setGravity(17, 0, 0);
                t.show();
                return;
            }
            new DownloaderTask().execute(new String[]{url, mimetype.substring(mimetype.lastIndexOf(CookieSpec.PATH_DELIM) + 1)});
        }
    }

    private class DownloaderTask extends AsyncTask<String, Void, String> {
        String _filetype = bs.b;
        private DialogClass mremovedialog;

        public DownloaderTask() {
        }

        protected String doInBackground(String... params) {
            String fileName;
            String url = params[0];
            this._filetype = params[1];
            SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
            if (url.contains("apk")) {
                fileName = url.substring(url.lastIndexOf(CookieSpec.PATH_DELIM) + 1);
            } else {
                fileName = String.valueOf(url.substring(url.lastIndexOf(CookieSpec.PATH_DELIM) + 1)) + df.format(new Date());
            }
            String fileName2 = URLDecoder.decode(fileName);
            Log.i("tag", "fileName=" + fileName2);
            String _path = String.valueOf(Constants.PATH_BASE) + "/phms/reprot/";
            PageUtil.isHavePath(_path);
            if (new File(_path, fileName2).exists()) {
                Log.i("tag", "The file has already exists.");
                return fileName2;
            }
            try {
                HttpClient client = new DefaultHttpClient();
                HttpGet get = new HttpGet(url);
                get.addHeader("Cookie", FragmentWebViewBase.this.mSession);
                HttpResponse response = client.execute(get);
                if (200 != response.getStatusLine().getStatusCode()) {
                    return null;
                }
                HttpEntity entity = response.getEntity();
                InputStream input = entity.getContent();
                FragmentWebViewBase.this.writeToSDCard(fileName2, input, this._filetype);
                input.close();
                entity.consumeContent();
                return fileName2;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        protected void onCancelled() {
            super.onCancelled();
        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            FragmentWebViewBase.this.closeProgressDialog();
            if (result == null) {
                Toast t = Toast.makeText(FragmentWebViewBase.this.getActivity(), "Connection error. Please try again later.", Toast.LENGTH_LONG);
                t.setGravity(17, 0, 0);
                t.show();
                return;
            }
            if (!result.contains("apk")) {
                result = String.valueOf(result) + ".pdf";
            }
            String _path = String.valueOf(Constants.PATH_BASE) + "/phms/reprot/";
            PageUtil.isHavePath(_path);
            File file = new File(_path, result);
            FragmentWebViewBase.savefile = file;
            if (result.contains("apk")) {
                FragmentWebViewBase.this.installApk(FragmentWebViewBase.this.getActivity(), _path);
                return;
            }
            Log.i("tag", "Path=" + file.getAbsolutePath());
            try {
                FragmentWebViewBase.this.startActivity(FragmentWebViewBase.this.getFileIntent(file));
            } catch (ActivityNotFoundException e) {
                System.out.println(e);
                this.mremovedialog = new DialogClass((Context) FragmentWebViewBase.this.getActivity(), FragmentWebViewBase.this.getActivity().getString(R.string.flow_record_download), FragmentWebViewBase.this.getActivity().getString(R.string.str_askdownload), (DialogInterface.OnKeyListener) null, (View.OnClickListener) new View.OnClickListener() {
                    public void onClick(View arg0) {
                        DownloaderTask.this.mremovedialog.dismiss();
                        FragmentWebViewBase.this.openDownloadDialog();
                    }
                }, (View.OnClickListener) new View.OnClickListener() {
                    public void onClick(View arg0) {
                        DownloaderTask.this.mremovedialog.dismiss();
                    }
                });
            }
        }

        protected void onPreExecute() {
            super.onPreExecute();
            FragmentWebViewBase.this.showProgressDialog();
        }

        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
    }

    private void setProgress(int progress) {
        if (this.mgrogress != null && this.progresstext != null) {
            this.mgrogress.setProgress(progress);
            this.progresstext.setText(String.valueOf(progress) + "%");
        }
    }

    private void openDownloadDialog() {
        this.progressdialog = new Dialog(getActivity(), R.style.dialog_pedometer);
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.layout_download, (ViewGroup) null);
        this.progressdialog.setContentView(view);
        this.progressdialog.setCanceledOnTouchOutside(true);
        this.mgrogress = (ProgressBar) view.findViewById(R.id.download_progress);
        this.progresstext = (TextView) view.findViewById(R.id.progresstext);
        ((Button) view.findViewById(R.id.cancle)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                if (FragmentWebViewBase.this.progressdialog.isShowing()) {
                    FragmentWebViewBase.this.progressdialog.dismiss();
                    FragmentWebViewBase.this.downloadApkThread.cancelDownloadApk();
                }
            }
        });
        Window dialogWindow = this.progressdialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = (Constants.M_SCREENWEIGH * 3) / 4;
        lp.height = (Constants.M_SCREENWEIGH * 1) / 2;
        this.progressdialog.setCanceledOnTouchOutside(false);
        dialogWindow.setAttributes(lp);
        this.progressdialog.show();
        this.downloadApkThread = new DownloadApkThread(this.mDownloadhandler);
        this.downloadApkThread.downloadApk();
    }

    public void installApk(Context contex, String mApkpath) {
        File apkfile = new File(mApkpath, "reader.apk");
        if (apkfile.exists()) {
            Intent _intent = new Intent("android.intent.action.VIEW");
            _intent.setDataAndType(Uri.parse("file://" + apkfile.toString()), "application/vnd.android.package-archive");
            contex.startActivity(_intent);
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return false;
    }

    private void showProgressDialog() {
        if (this.mDialog == null) {
            this.mDialog = new ProgressDialog(getActivity());
            this.mDialog.setProgressStyle(0);
            this.mDialog.setMessage("正在加载 ，请等待...");
            this.mDialog.setIndeterminate(false);
            this.mDialog.setCancelable(true);
            this.mDialog.setCanceledOnTouchOutside(false);
            this.mDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                public void onDismiss(DialogInterface dialog) {
                    FragmentWebViewBase.this.mDialog = null;
                }
            });
            this.mDialog.show();
        }
    }

    private void closeProgressDialog() {
        if (this.mDialog != null) {
            this.mDialog.dismiss();
            this.mDialog = null;
        }
    }

    public Intent getFileIntent(File file) {
        Uri fromFile = Uri.fromFile(file);
        String type = getMIMEType(file);
        Log.i("tag", "type=" + type);
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.setDataAndType(Uri.parse("file://" + file.toString()), type);
        return intent;
    }

    public void writeToSDCard(String fileName, InputStream input, String pFileType) {
        String file_path;
        if (Environment.getExternalStorageState().equals("mounted")) {
            String _path = String.valueOf(Constants.PATH_BASE) + "/phms/reprot/";
            PageUtil.isHavePath(_path);
            if (fileName.contains("apk")) {
                file_path = fileName;
            } else {
                file_path = String.valueOf(fileName) + ".pdf";
            }
            try {
                FileOutputStream fos = new FileOutputStream(new File(_path, file_path));
                byte[] b = new byte[2048];
                while (true) {
                    int j = input.read(b);
                    if (j == -1) {
                        fos.flush();
                        fos.close();
                        return;
                    }
                    fos.write(b, 0, j);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e2) {
                e2.printStackTrace();
            }
        } else {
            Log.i("tag", "NO SDCard.");
        }
    }

    private String getMIMEType(File f) {
        String fName = f.getName();
        String end = fName.substring(fName.lastIndexOf(".") + 1, fName.length()).toLowerCase();
        if (end.equals("pdf")) {
            return "application/pdf";
        }
        if (end.equals("m4a") || end.equals("mp3") || end.equals("mid") || end.equals("xmf") || end.equals("ogg") || end.equals("wav")) {
            return "audio/*";
        }
        if (end.equals("3gp") || end.equals("mp4")) {
            return "video/*";
        }
        if (end.equals("jpg") || end.equals("gif") || end.equals("png") || end.equals("jpeg") || end.equals("bmp")) {
            return "image/*";
        }
        if (end.equalsIgnoreCase("apk")) {
            return "application/vnd.android.package-archive";
        }
        return "*/*";
    }

    @SuppressLint({"SetJavaScriptEnabled"})
    private void initWebSettings() {
        this.mShowFlag = true;
        mHandler.postDelayed(this.mSecondDelay, 20000);
        initValueBeforeReloadWebView();
        this.mWebView.clearView();
        this.mWebSettings = this.mWebView.getSettings();
        this.mWebSettings.setJavaScriptEnabled(true);
        this.mWebSettings.setSavePassword(true);
        this.mWebSettings.setSaveFormData(false);
        this.mWebSettings.setBuiltInZoomControls(true);
        this.mWebSettings.setSupportZoom(true);
        this.mProBar.setVisibility(View.VISIBLE);
        this.mWebSettings.setBlockNetworkImage(true);
        this.mWebSettings.setPluginState(WebSettings.PluginState.ON);
        this.mWebView.requestFocus();
        this.mWebSettings.setAllowFileAccess(true);
        this.mWebView.setScrollBarStyle(33554432);
        this.mWebView.setInitialScale(99);
        if (Build.VERSION.SDK_INT >= 11) {
            this.mWebView.setLayerType(1, (Paint) null);
        }
        this.mWebSettings.setLoadWithOverviewMode(true);
        this.mWebView.setScrollBarStyle(33554432);
        this.mWebView.setScrollBarStyle(0);
        this.mWebView.setVerticalScrollBarEnabled(false);
        this.mWebView.setDownloadListener(new MyWebViewDownLoadListener(this, (MyWebViewDownLoadListener) null));
        timer();
        setUrl();
        this.mWebView.addJavascriptInterface(new Object() {
            public void clickOnAndroid(final String message) {
                FragmentWebViewBase.mHandler.post(new Runnable() {
                    public void run() {
                        InstantMessageService.stopServer(FragmentWebViewBase.this.getActivity());
                        CLog.eT(FragmentWebViewBase.TAG, "clickOnAndroid **************");
                        FragmentWebViewBase.this.dealUserinfo(message);
                    }
                });
            }

            public void myDevice() {
                CLog.eT(FragmentWebViewBase.TAG, "我的设备 **************");
                Message msg = new Message();
                msg.what = Constants.GO_BACK_DEVICE;
                msg.arg2 = 13;
                App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(msg);
            }

            public String isApp() {
                CLog.eT(FragmentWebViewBase.TAG, "isApp  **************");
                return "26";
            }
        }, "contec");
        this.mWebView.setWebChromeClient(new WebChromeClient() {
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                CLog.dT(FragmentWebViewBase.TAG, "message:" + message + "   result:" + result + "  url:" + url);
                return super.onJsAlert(view, url, message, result);
            }

            public void onProgressChanged(WebView view, int newProgress) {
                if (FragmentWebViewBase.this.mChangePage) {
                    FragmentWebViewBase.mHandler.removeCallbacks(FragmentWebViewBase.this.mSecondDelay);
                } else if (FragmentWebViewBase.this.getActivity() == null || PageUtil.checkNet(FragmentWebViewBase.this.getActivity())) {
                    FragmentWebViewBase.this.mCurrentProgress = newProgress;
                    if (FragmentWebViewBase.this.mShowFlag || FragmentWebViewBase.this.mCurrentProgress >= 25) {
                        FragmentWebViewBase.this.initviewafterloadsuccess();
                    } else {
                        FragmentWebViewBase.this.initViewAfterLoadError();
                    }
                } else {
                    FragmentWebViewBase.mHandler.removeCallbacks(FragmentWebViewBase.this.mSecondDelay);
                    FragmentWebViewBase.this.initViewAfterLoadError();
                }
            }
        });
        this.mWebView.setWebViewClient(new LangageWebChromeClient());
    }

    public void onClick(View v) {
    }

    protected void initValueBeforeReloadWebView() {
        if (getActivity() != null) {
            this.mLoadErrorLayout.setVisibility(View.VISIBLE);
            this.mCheckReportNetInfoTV.setText(getActivity().getString(R.string.str_loadingnetview));
        }
    }

    private void initviewafterloadsuccess() {
        this.mLoadErrorLayout.setVisibility(View.GONE);
    }

    private void initViewAfterLoadError() {
        CLog.e("initViewAfterLoadError", "initViewAfterLoadError");
        this.mCheckReportNetInfoTV.setText(getActivity().getString(R.string.str_net_error));
        this.mLoadErrorLayout.setVisibility(View.VISIBLE);
        Spo2DataDao Spo2dao = Spo2DataDaoOperation.getInstance(getActivity()).queryLast();
        BloodDDataDao Blooddao = BloodDDataDaoOperation.getInstance(getActivity()).queryLast();
        CmssxtDataDao Cmssxtdao = CmssxtDataDaoOperation.getInstance(getActivity()).queryLast();
        FetalHeartDataDao FetalHeartdao = FeltalHeartDataDaoOperation.getInstance(getActivity()).queryLast();
        FvcDataDao Fvcdao = FvcDataDaoOperation.getInstance(getActivity()).queryLast();
        PedometerDayDataDao Daydao = PedometerDayDataDaoOperation.getInstance(getActivity()).queryLast();
        PluseDataDao Plusedao = PluseDataDaoOperation.getInstance(getActivity()).queryLast();
        TempertureDataDao Temperturedao = TempertureDataDaoOperation.getInstance(getActivity()).queryLast();
        UrineDataDao Urinedao = UrineDataDaoOperation.getInstance(getActivity()).queryLast();
        WeightDataDao Weightdao = WeightDataDaoOperation.getInstance(getActivity()).queryLast();
        this.listData.clear();
        if (Spo2dao != null) {
            LocalDataAdapterData data = new LocalDataAdapterData();
            data.type = getResources().getString(R.string.xueyang);
            data.data = String.valueOf(Spo2dao.mSpo2) + CookieSpec.PATH_DELIM + Spo2dao.mPr;
            data.date = Spo2dao.mTime;
            data.ivId = R.drawable.dia_ox;
            this.listData.add(data);
        }
        if (Blooddao != null) {
            LocalDataAdapterData data1 = new LocalDataAdapterData();
            data1.type = getResources().getString(R.string.xueya);
            data1.data = String.valueOf(Blooddao.mHigh) + CookieSpec.PATH_DELIM + Blooddao.mLow;
            data1.date = Blooddao.mTime;
            data1.ivId = R.drawable.dia_press;
            this.listData.add(data1);
        }
        if (Cmssxtdao != null) {
            LocalDataAdapterData data2 = new LocalDataAdapterData();
            data2.type = getResources().getString(R.string.xuetang);
            data2.data = Cmssxtdao.mData;
            data2.date = Cmssxtdao.mTime;
            data2.ivId = R.drawable.dia_blood;
            this.listData.add(data2);
        }
        if (FetalHeartdao != null) {
            LocalDataAdapterData data3 = new LocalDataAdapterData();
            data3.type = getResources().getString(R.string.fetal_rate);
            data3.data = FetalHeartdao.mFetalHeartRate;
            data3.date = FetalHeartdao.mTime;
            data3.ivId = R.drawable.dia_xinlv;
            this.listData.add(data3);
        }
        if (Fvcdao != null) {
            LocalDataAdapterData data4 = new LocalDataAdapterData();
            data4.data = Fvcdao.mFvc;
            data4.type = getResources().getString(R.string.str_fgn);
            data4.date = Fvcdao.mTime;
            data4.ivId = R.drawable.dia_lung;
            this.listData.add(data4);
        }
        if (Daydao != null) {
            LocalDataAdapterData data5 = new LocalDataAdapterData();
            data5.type = getResources().getString(R.string.today_steps);
            data5.data = Daydao.mSteps;
            data5.date = Daydao.mTime;
            data5.ivId = R.drawable.dia_run;
            this.listData.add(data5);
        }
        if (Plusedao != null) {
            LocalDataAdapterData data6 = new LocalDataAdapterData();
            data6.type = getResources().getString(R.string.heart_rate);
            data6.data = Plusedao.mPluse;
            data6.date = Plusedao.mTime;
            data6.ivId = R.drawable.dia_xindian;
            this.listData.add(data6);
        }
        if (Temperturedao != null) {
            LocalDataAdapterData data7 = new LocalDataAdapterData();
            data7.type = getResources().getString(R.string.heat);
            data7.data = Temperturedao.mTemperture;
            data7.date = Temperturedao.mTime;
            CLog.e("initViewAfterLoadError", String.valueOf(data7.data) + "===" + data7.date);
            data7.ivId = R.drawable.dia_ter;
            this.listData.add(data7);
        }
        if (Urinedao != null) {
            LocalDataAdapterData data8 = new LocalDataAdapterData();
            data8.type = getResources().getString(R.string.pee_nomal);
            data8.data = Urinedao.mValue;
            data8.date = Urinedao.mTime;
            data8.ivId = R.drawable.dia_pee;
            this.listData.add(data8);
        }
        if (Weightdao != null) {
            LocalDataAdapterData data9 = new LocalDataAdapterData();
            data9.type = getResources().getString(R.string.str_user_weight);
            data9.data = Weightdao.mWeight;
            data9.date = Weightdao.mTime;
            data9.ivId = R.drawable.dia_wt;
            this.listData.add(data9);
        }
        if (this.listData.size() != 0) {
            this.no_hava_data.setVisibility(View.VISIBLE);
            this.adapter.updataData(this.listData);
            this.errorLayoutShow.setVisibility(View.GONE);
            return;
        }
        this.no_hava_data.setVisibility(View.GONE);
        this.errorLayoutShow.setVisibility(View.VISIBLE);
    }

    public void onResume() {
        super.onResume();
        Log.i(TAG, "report onresume 9");
        if (!isNetworkConnected(getActivity())) {
            Toast.makeText(getActivity(), getResources().getString(R.string.offline_advice), Toast.LENGTH_SHORT).show();
        }
    }

    public void onStop() {
        Log.i(TAG, "onStop onStop 9********************");
        CLog.eT(TAG, " FragmentWebViewBase  onDestroy********************************");
        super.onStop();
    }

    public void onDestroy() {
        super.onDestroy();
        if (this.mTimer != null) {
            if (this.mTimer != null) {
                this.mTimer.cancel();
            }
            if (this.mTimer != null) {
                this.mTimer.purge();
            }
        }
        App_phms.getInstance().mEventBus.unregister(this);
        CLog.eT(TAG, " FragmentWebViewBase  onDestroy********************************");
    }

    public void onPause() {
        super.onPause();
    }

    private void dealUserinfo(String message) {
        String[] resultArray;
        CLog.dT(TAG, "***************message:" + message);
        StringTokenizer _strToken = new StringTokenizer(message, "&", true);
        String password1 = bs.b;
        String password2 = bs.b;
        String password3 = bs.b;
        while (_strToken.hasMoreElements()) {
            String result = _strToken.nextToken();
            if (!(result == null || (resultArray = result.split(SimpleComparison.EQUAL_TO_OPERATION)) == null)) {
                String result1 = bs.b;
                if (resultArray.length > 1) {
                    result1 = resultArray[1];
                }
                if (resultArray[0].equals("data[Card][birthday]")) {
                    String brithday = result1;
                } else if (resultArray[0].equals("data[Card][name]")) {
                    String name = result1;
                } else if (resultArray[0].equals("data[cardinfo][email]")) {
                    String email = result1;
                } else if (resultArray[0].equals("data[Card][pid]")) {
                    String pid = result1;
                } else if (resultArray[0].equals("data[cardinfo][weight]")) {
                    String weight = result1;
                } else if (resultArray[0].equals("data[cardinfo][sporttarget]")) {
                    String sporttarget = result1;
                } else if (resultArray[0].equals("data[cardinfo][weighttarget]")) {
                    String weighttarget = result1;
                } else if (resultArray[0].equals("data[Card][sex]")) {
                    String sex = result1;
                } else if (resultArray[0].equals("password2")) {
                    password2 = result1;
                } else if (resultArray[0].equals("password3")) {
                    password3 = result1;
                } else if (resultArray[0].equals("password1")) {
                    password1 = result1;
                }
            }
        }
        try {
            LoginUserDao _Loginuserdao = App_phms.getInstance().mHelper.getLoginUserDao().queryBuilder().queryForFirst();
            if (password1 != null && !password1.equals("undefined") && password2 != null && !password2.equals(bs.b) && password3 != null && !password3.equals(bs.b) && password2.equals(password3)) {
                CLog.dT(TAG, "更改了密码");
                _Loginuserdao.mPsw = password2;
            }
            AjaxCallBack_login _ajAjaxCallBack_login = new AjaxCallBack_login(getActivity(), this.mHandlerLogin);
            _ajAjaxCallBack_login.mUserID = _Loginuserdao.mUID;
            _ajAjaxCallBack_login.mPasswd = _Loginuserdao.mPsw;
            Method_android_login.login(_Loginuserdao.mUID, _Loginuserdao.mPsw, 0, _ajAjaxCallBack_login, App_phms.getInstance().mFinalHttp, String.valueOf(Constants.URL) + "/login.php");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loginsuccess() {
        LoginUserDao _loginUserInfo = PageUtil.getLoginUserInfo();
        try {
            Dao<UserInfoDao, String> userDao = App_phms.getInstance().mHelper.getUserDao();
            UserInfoDao retUser = userDao.queryForId(_loginUserInfo.mUID);
            if (retUser != null) {
                App_phms.getInstance().mUserInfo.mSearchInterval = retUser.getmSearchInterval();
                App_phms.getInstance().mUserInfo.mBluetoothState = retUser.getBluetoothstate();
                App_phms.getInstance().mUserInfo.mLanguage = retUser.getmLanguage();
                Constants.Language = App_phms.getInstance().mUserInfo.mLanguage;
                retUser.setmSex(App_phms.getInstance().mUserInfo.mSex);
                retUser.setmUserName(_loginUserInfo.mUserName);
                Constants.Language = App_phms.getInstance().mUserInfo.mLanguage;
                retUser.setLastLoginData(new Date());
                retUser.setPsw(_loginUserInfo.mPsw);
                userDao.update(retUser);
            }
            PageUtil.getUserInfo();
            if (Constants.Language.contains("1")) {
                String _language = Locale.getDefault().getLanguage();
                Configuration config2 = getActivity().getResources().getConfiguration();
                DisplayMetrics dm2 = getActivity().getResources().getDisplayMetrics();
                if (_language.contains("zh")) {
                    config2.locale = Locale.CHINESE;
                } else if (_language.contains("en")) {
                    config2.locale = Locale.ENGLISH;
                }
                getActivity().getResources().updateConfiguration(config2, dm2);
                App_phms.getInstance().showBeans = DeviceListDaoOperation.getInstance().getDevice();
                SharedPreferences.Editor _editor = App_phms.getInstance().mCurrentloginUserInfo.edit();
                _editor.putString("username", _loginUserInfo.mUID);
                _editor.putString("password", _loginUserInfo.mPsw);
                _editor.commit();
                PhmsSharedPreferences.getInstance(getActivity()).saveColume("username", _loginUserInfo.mUID);
            }
            if (Constants.Language.equals("en")) {
                Configuration config1 = getActivity().getResources().getConfiguration();
                DisplayMetrics dm1 = getActivity().getResources().getDisplayMetrics();
                config1.locale = Locale.ENGLISH;
                getActivity().getResources().updateConfiguration(config1, dm1);
            } else if (Constants.Language.equals("zh")) {
                Configuration config = getActivity().getResources().getConfiguration();
                DisplayMetrics dm = getActivity().getResources().getDisplayMetrics();
                config.locale = Locale.CHINESE;
                getActivity().getResources().updateConfiguration(config, dm);
            }
            App_phms.getInstance().showBeans = DeviceListDaoOperation.getInstance().getDevice();
            SharedPreferences.Editor _editor2 = App_phms.getInstance().mCurrentloginUserInfo.edit();
            _editor2.putString("username", _loginUserInfo.mUID);
            _editor2.putString("password", _loginUserInfo.mPsw);
            _editor2.commit();
            PhmsSharedPreferences.getInstance(getActivity()).saveColume("username", _loginUserInfo.mUID);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    class LangageWebChromeClient extends WebViewClient {
        LangageWebChromeClient() {
        }

        public void onLoadResource(WebView view, String url) {
            super.onLoadResource(view, url);
        }

        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Map<String, String> additionalHttpHeaders = new HashMap<>();
            CLog.eT(FragmentWebViewBase.TAG, "shouldOverrideUrlLoading()" + url + "  Constants.Language:" + Constants.Language + "  CookieStr:" + CookieManager.getInstance().getCookie(url));
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
            FragmentWebViewBase.this.mWebView.loadUrl(url);
            return super.shouldOverrideUrlLoading(view, url);
        }

        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            FragmentWebViewBase.this.mProBar.setVisibility(View.VISIBLE);
            CLog.eT(FragmentWebViewBase.TAG, "WebViewClient-->onPageStarted()  mShowFlag:" + FragmentWebViewBase.this.mShowFlag);
            System.out.println(">>>>>>>>>>>>>>>>>>>>>:" + url);
            if (url != null) {
                if ("http://newm.contec365.com/mobile/MobileWelcome/index/1".equals(url) || "http://newm.contec365.com/mobile".equals(url) || "http://newm.contec365.com/user/mlogin/en?".equals(url)) {
                    FragmentHealthReport.mHealthReportIndex = 0;
                } else {
                    FragmentHealthReport.mHealthReportIndex = 3;
                }
            }
            if (FragmentWebViewBase.this.mChangePage) {
                FragmentWebViewBase.mHandler.removeCallbacks(FragmentWebViewBase.this.mSecondDelay);
            } else if (FragmentWebViewBase.this.getActivity() != null && !PageUtil.checkNet(FragmentWebViewBase.this.getActivity())) {
                FragmentWebViewBase.mHandler.removeCallbacks(FragmentWebViewBase.this.mSecondDelay);
                FragmentWebViewBase.this.initViewAfterLoadError();
            } else if (!FragmentWebViewBase.this.mShowFlag) {
                view.stopLoading();
                FragmentWebViewBase.this.initViewAfterLoadError();
            }
        }

        public void onPageFinished(WebView view, String url) {
            CLog.eT(FragmentWebViewBase.TAG, "WebViewClient-->onPageFinished()----》" + FragmentWebViewBase.this.mCurrentProgress + "***************");
            String CookieStr = CookieManager.getInstance().getCookie(url);
            FragmentWebViewBase.this.mSession = CookieStr;
            Log.e("sunzn", "Cookies = " + CookieStr);
            Message msgs = new Message();
            msgs.what = Constants.V_GET_REPORT_MD5;
            App_phms.getInstance().mEventBus.post(msgs);
            if (FragmentWebViewBase.this.mChangePage) {
                FragmentWebViewBase.mHandler.removeCallbacks(FragmentWebViewBase.this.mSecondDelay);
                return;
            }
            if (FragmentWebViewBase.this.mCurrentProgress <= 30) {
                view.clearCache(false);
            }
            if (FragmentWebViewBase.this.getActivity() == null || PageUtil.checkNet(FragmentWebViewBase.this.getActivity())) {
                CLog.eT(FragmentWebViewBase.TAG, "mShowFlag= " + FragmentWebViewBase.this.mShowFlag);
                FragmentWebViewBase.mHandler.removeCallbacks(FragmentWebViewBase.this.mSecondDelay);
                FragmentWebViewBase.this.mWebSettings.setBlockNetworkImage(false);
                FragmentWebViewBase.this.mProBar.setVisibility(View.GONE);
                return;
            }
            FragmentWebViewBase.mHandler.removeCallbacks(FragmentWebViewBase.this.mSecondDelay);
            FragmentWebViewBase.this.initViewAfterLoadError();
        }
    }

    public boolean isNetworkConnected(Context context) {
        NetworkInfo mNetworkInfo;
        if (context == null || (mNetworkInfo = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo()) == null) {
            return false;
        }
        return mNetworkInfo.isAvailable();
    }
}
