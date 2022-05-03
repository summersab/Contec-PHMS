package com.contec.phms.service;

import android.content.Context;
import android.content.pm.PackageManager;
import com.contec.phms.App_phms;
import com.contec.phms.util.CLog;
import com.contec.phms.util.Constants;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.HttpHandler;
import org.apache.http.HttpEntity;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;

public class UpdateAppFunction {
    private static final String TAG = UpdateAppFunction.class.getSimpleName();
    private static int m_newVerCode;
    private static String m_newVerName;

    public static boolean checkNewVersion(Context pContext, String pServerAddress, String pAppPackageName) {
        if (!getServerVerCode(pServerAddress) || m_newVerCode <= getVerCode(pContext, pAppPackageName)) {
            return false;
        }
        return true;
    }

    public static int getVerCode(Context pContext, String pAppPackageName) {
        try {
            return pContext.getPackageManager().getPackageInfo(pAppPackageName, 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            return -1;
        }
    }

    public static boolean getServerVerCode(String pServiceAddress) {
        try {
            JSONArray array = new JSONArray(getVerJsonContent(String.valueOf(pServiceAddress) + "ver.json"));
            if (array.length() > 0) {
                try {
                    m_newVerCode = Integer.parseInt(array.getJSONObject(0).getString("verCode"));
                } catch (Exception e) {
                    m_newVerCode = -1;
                    return false;
                }
            }
            return true;
        } catch (Exception e2) {
            CLog.e(TAG, e2.getMessage());
            return false;
        }
    }

    private static String getVerJsonContent(String pUrl) throws Exception {
        StringBuilder sb = new StringBuilder();
        HttpClient client = new DefaultHttpClient();
        HttpParams httpParams = client.getParams();
        HttpConnectionParams.setConnectionTimeout(httpParams, 3000);
        HttpConnectionParams.setSoTimeout(httpParams, BaseImageDownloader.DEFAULT_HTTP_CONNECT_TIMEOUT);
        HttpEntity entity = client.execute(new HttpGet(pUrl)).getEntity();
        if (entity != null) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(entity.getContent()), 8192);
            while (true) {
                String line = reader.readLine();
                if (line == null) {
                    break;
                }
                sb.append(String.valueOf(line) + "\n");
            }
            reader.close();
        }
        return sb.toString();
    }

    public static HttpHandler<File> downNewVersionApp(AjaxCallBack<File> pAjaxCallBack, String pApkName) {
        return App_phms.getInstance().mFinalHttp.download(Constants.NEWAPPADDRESS, String.valueOf(Constants.NEWAPPLOCALADDRESS) + pApkName, pAjaxCallBack);
    }

    public static HttpHandler<File> downNewVersionAppAppend(AjaxCallBack<File> pAjaxCallBack, String pApkName) {
        return App_phms.getInstance().mFinalHttp.download(Constants.NEWAPPADDRESS, String.valueOf(Constants.NEWAPPLOCALADDRESS) + pApkName, true, pAjaxCallBack);
    }
}
