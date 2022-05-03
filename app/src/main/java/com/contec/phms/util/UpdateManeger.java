package com.contec.phms.util;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import com.alibaba.cchannel.CloudChannelConstants;
//import com.alibaba.sdk.android.Constants;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import org.apache.commons.httpclient.cookie.CookieSpec;
import org.json.JSONObject;

public class UpdateManeger {
    private static final int DOWNLOAD = 222;
    private static final int DOWNLOAD_FINISH = 333;
    public static boolean cancelUpdate = false;
    private Context mContext;
    private SharedPreferences mCurrentloginUserInfo;
    private Handler mHandler;
    HashMap<String, String> mHashMap;
    private String mSavePath;
    private int progress;

    public UpdateManeger(Context pContext, Handler pHandler) {
        this.mContext = pContext;
        this.mHandler = pHandler;
        cancelUpdate = false;
    }

    public HashMap<String, String> checkUpdate() {
        String path;
        int _isNeedUpdate = 2;
        new ParseXmlService();
        this.mHashMap = new HashMap<>();
        //if (Constants.Language.equals("en")) {
            path = "http://data1.contec365.com/update/android/xmlphms_version_code_en.json";
        //} else {
            path = "http://data1.contec365.com/update/android/xmlphms_version_code.json";
        //}
        try {
            HttpURLConnection conn = (HttpURLConnection) new URL(path).openConnection();
            conn.setReadTimeout(BaseImageDownloader.DEFAULT_HTTP_CONNECT_TIMEOUT);
            conn.setConnectTimeout(BaseImageDownloader.DEFAULT_HTTP_CONNECT_TIMEOUT);
            conn.setRequestMethod("GET");
            if (conn.getResponseCode() == 200) {
                InputStream inStream = conn.getInputStream();
                byte[] buf = new byte[8192];
                StringBuilder sb = new StringBuilder();
                while (true) {
                    int len = inStream.read(buf);
                    if (len == -1) {
                        break;
                    }
                    sb.append(new String(buf, 0, len));
                }
                CLog.e("updateStr", "sb.toString()");
                String jsonStr = sb.toString();
                if (jsonStr != null && jsonStr.startsWith("﻿")) {
                    jsonStr = jsonStr.substring(1);
                }
                JSONObject dataJson = new JSONObject(jsonStr);
                this.mHashMap.put("version", dataJson.getString("verCode"));
                this.mHashMap.put("versioninfo", dataJson.getString("verCodeint"));
                this.mHashMap.put("name", dataJson.getString("verName"));
                //this.mHashMap.put(Constants.URL, dataJson.getString(Constants.URL));
                this.mHashMap.put("content", dataJson.getString("content"));
                this.mCurrentloginUserInfo = this.mContext.getSharedPreferences("CurrentloginUserInfo", 0);
                SharedPreferences.Editor _Editor = this.mCurrentloginUserInfo.edit();
                //_Editor.putInt("CMS50EW_net", Integer.parseInt(dataJson.getString(Constants.CMS50EW)));
                _Editor.putInt("CMS50IW_net", Integer.parseInt(dataJson.getString("CMS50IW")));
                _Editor.putInt("SP10W_net", Integer.parseInt(dataJson.getString("SP10W")));
                _Editor.putInt("SXT_net", Integer.parseInt(dataJson.getString("SXT")));
                _Editor.putInt("ABPM50_net", Integer.parseInt(dataJson.getString("ABPM50")));
                _Editor.putInt("CONTEC08A_net", Integer.parseInt(dataJson.getString("CONTEC08A")));
                _Editor.putInt("WT100_net", Integer.parseInt(dataJson.getString("WT100")));
                _Editor.putInt("Sonoline-S_net", Integer.parseInt(dataJson.getString("Sonoline-S")));
                //_Editor.putInt("PM85_net", Integer.parseInt(dataJson.getString(Constants.PM85_NAME)));
                _Editor.commit();
                conn.disconnect();
                if (this.mHashMap != null && Integer.valueOf(this.mHashMap.get("version")).intValue() > getVersionCode(this.mContext)) {
                    _isNeedUpdate = 1;
                }
                this.mHashMap.put("_isNeedUpdate", new StringBuilder(String.valueOf(_isNeedUpdate)).toString());
                return this.mHashMap;
            }
            HashMap<String, String> _returnparas = new HashMap<>();
            _returnparas.put("_isNeedUpdate", new StringBuilder(String.valueOf(3)).toString());
            return _returnparas;
        } catch (Exception e) {
            e.printStackTrace();
            HashMap<String, String> _returnparas2 = new HashMap<>();
            _returnparas2.put("_isNeedUpdate", new StringBuilder(String.valueOf(4)).toString());
            return _returnparas2;
        }
    }

    private int getVersionCode(Context context) {
        try {
            return this.mContext.getPackageManager().getPackageInfo("com.contec.phms", 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public void downloadApk() {
        new downloadApkThread(this, (downloadApkThread) null).start();
    }

    private class downloadApkThread extends Thread {
        private InputStream is;
        private int length;

        private downloadApkThread() {
        }

        /* synthetic */ downloadApkThread(UpdateManeger updateManeger, downloadApkThread downloadapkthread) {
            this();
        }

        public void run() {
            try {
                if (Environment.getExternalStorageState().equals("mounted")) {
                    UpdateManeger.this.mSavePath = String.valueOf(Environment.getExternalStorageDirectory() + CookieSpec.PATH_DELIM) + "phms_download";
                    //HttpURLConnection conn = (HttpURLConnection) new URL(UpdateManeger.this.mHashMap.get(Constants.URL)).openConnection();
                    //conn.setConnectTimeout(CloudChannelConstants.WEBVIEW_PROXY_HTTP_TIMEOUT);
                    //conn.setRequestMethod("GET");
                    //conn.connect();
                    if (!UpdateManeger.cancelUpdate) {
                    //    this.length = conn.getContentLength();
                    //    this.is = conn.getInputStream();
                    }
                    File file = new File(UpdateManeger.this.mSavePath);
                    if (!file.exists()) {
                        file.mkdir();
                    }
                    FileOutputStream fos = new FileOutputStream(new File(UpdateManeger.this.mSavePath, "PHMS"));
                    int count = 0;
                    byte[] buf = new byte[1024];
                    while (true) {
                        int numread = this.is.read(buf);
                        count += numread;
                        UpdateManeger.this.progress = (int) ((((float) count) / ((float) this.length)) * 100.0f);
                        UpdateManeger.this.mHandler.obtainMessage(UpdateManeger.DOWNLOAD, UpdateManeger.this.progress, 0).sendToTarget();
                        if (numread > 0) {
                            fos.write(buf, 0, numread);
                            if (UpdateManeger.cancelUpdate) {
                                break;
                            }
                        } else {
                            UpdateManeger.this.mHandler.sendEmptyMessage(UpdateManeger.DOWNLOAD_FINISH);
                            break;
                        }
                    }
                    fos.close();
                    this.is.close();
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e2) {
                e2.printStackTrace();
            }
        }
    }

    public void installApk() {
        File apkfile = new File(this.mSavePath, "PHMS");
        if (apkfile.exists()) {
            Intent _intent = new Intent("android.intent.action.VIEW");
            _intent.setDataAndType(Uri.parse("file://" + apkfile.toString()), "application/vnd.android.package-archive");
            this.mContext.startActivity(_intent);
        }
    }
}
