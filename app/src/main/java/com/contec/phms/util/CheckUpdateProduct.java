package com.contec.phms.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import com.contec.phms.login.LoginActivity;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import java.io.File;

public class CheckUpdateProduct {
    public static final String LOCAL_HTMLPATH = (String.valueOf(LoginActivity.getSDPath()) + "/contec/html/");

    public static void downloadProductDetail(String url, final String resourceName, Context context) {
        String path = null;
        if (resourceName.endsWith(".html") || resourceName.endsWith(".css")) {
            path = "sdcard/contec/html/";
        } else if (resourceName.endsWith(".png")) {
            path = "sdcard/contec/html/images/";
        }
        new HttpUtils().download(url, String.valueOf(path) + resourceName, true, true, (RequestCallBack<File>) new RequestCallBack<File>() {
            public void onSuccess(ResponseInfo<File> responseInfo) {
                Log.i("hxk", String.valueOf(resourceName) + "下载成功！");
            }

            public void onFailure(HttpException arg0, String arg1) {
                Log.i("hxk", String.valueOf(resourceName) + "下载失败，原因--->" + arg1);
            }
        });
    }

    public static boolean isNet(Context context) {
        NetworkInfo info = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
        if (info == null) {
            return false;
        }
        return info.isConnected();
    }
}
