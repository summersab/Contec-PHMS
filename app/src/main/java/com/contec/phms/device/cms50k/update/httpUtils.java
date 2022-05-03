package com.contec.phms.device.cms50k.update;

import android.util.Log;
import com.alibaba.cchannel.push.receiver.CPushMessageCodec;
import com.contec.phms.util.Constants;
import com.lidroid.xutils.http.client.multipart.MIME;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import u.aly.bs;

public class httpUtils {
    public static String wjdhttpPost(String url, Map<String, String> params) throws IOException {
        HttpEntity entity;
        String responseBodyData = null;
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(url);
        httpPost.addHeader(MIME.CONTENT_TYPE, "application/x-www-form-urlencoded");
        httpPost.getParams().setParameter("http.socket.timeout", new Integer(30000));
        httpPost.setEntity(new StringEntity(new StringBuilder().append(getRequestData(params)).toString(), "UTF-8"));
        HttpResponse response = httpClient.execute(httpPost);
        if (response.getStatusLine().getStatusCode() == 200 && (entity = response.getEntity()) != null) {
            responseBodyData = EntityUtils.toString(entity, CPushMessageCodec.UTF8);
            Log.e("httpPost", responseBodyData);
            httpPost.abort();
        }
        return responseBodyData;
    }

    private static StringBuffer getRequestData(Map<String, String> params) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("{");
        try {
            for (String key : params.keySet()) {
                stringBuffer.append("\"").append(key).append("\":\"").append(params.get(key)).append("\"").append(Constants.DOUHAO);
            }
            stringBuffer.deleteCharAt(stringBuffer.length() - 1);
            stringBuffer.append("}");
            String sb = new StringBuilder().append(stringBuffer).toString();
            Log.i("info", "�ҵ�hashmap���JSON ��ݷ���" + stringBuffer);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stringBuffer;
    }

    public static String httpGet(String url, String queryString) throws Exception {
        String responseData = null;
        if (queryString != null && !queryString.equals(bs.b)) {
            url = String.valueOf(url) + "?" + queryString;
        }
        HttpClient httpClient = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(url);
        httpGet.addHeader(MIME.CONTENT_TYPE, "application/x-www-form-urlencoded");
        httpGet.getParams().setParameter("http.socket.timeout", 30000);
        try {
            HttpResponse response = httpClient.execute(httpGet);
            if (response.getStatusLine().getStatusCode() == 200) {
                Log.i("info", "xxx");
                responseData = EntityUtils.toString(response.getEntity(), CPushMessageCodec.UTF8);
            }
            httpGet.abort();
            return responseData;
        } catch (Exception e) {
            Log.i("jx", e.toString());
            throw new Exception(e);
        } catch (Throwable th) {
            httpGet.abort();
            throw th;
        }
    }

    public static String httpPost(String url, Map<String, String> params) throws IOException {
        String responseBodyData = null;
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(url);
        httpPost.addHeader(MIME.CONTENT_TYPE, "application/x-www-form-urlencoded");
        httpPost.getParams().setParameter("http.socket.timeout", new Integer(30000));
        List<NameValuePair> parameters = new ArrayList<>();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            parameters.add(new BasicNameValuePair(entry.getKey().toString(), entry.getValue().toString()));
        }
        httpPost.setEntity(new UrlEncodedFormEntity(parameters, "UTF-8"));
        HttpResponse response = httpClient.execute(httpPost);
        response.getStatusLine().getStatusCode();
        HttpEntity entity = response.getEntity();
        if (entity != null) {
            responseBodyData = EntityUtils.toString(entity, CPushMessageCodec.UTF8);
        }
        httpPost.abort();
        return responseBodyData;
    }
}
