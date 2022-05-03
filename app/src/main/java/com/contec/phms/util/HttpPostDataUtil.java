package com.contec.phms.util;

import com.contec.phms.App_phms;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.httpclient.params.HttpConnectionParams;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import cn.com.contec.net.util.Constants_jar;

public class HttpPostDataUtil {
    private static final String TAG = HttpPostDataUtil.class.getSimpleName();
    private static HttpPostDataUtil mHttpPostData;

    public static HttpPostDataUtil getInstance() {
        if (mHttpPostData == null) {
            mHttpPostData = new HttpPostDataUtil();
        }
        return mHttpPostData;
    }

    public String postRequestData(String pUrl) {
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("data[Card][uid]", App_phms.getInstance().mUserInfo.mUserID));
        params.add(new BasicNameValuePair("data[Card][pwd]", App_phms.getInstance().mUserInfo.mPassword));
        return doPostData(pUrl, params);
    }

    private String doPostData(String url, List<NameValuePair> params) {
        HttpPost _post = new HttpPost(url);
        CLog.e(TAG, "请求数据：" + url + "   " + params.toString());
        DefaultHttpClient _client = new DefaultHttpClient();
        _client.getParams().setParameter(HttpConnectionParams.CONNECTION_TIMEOUT, Integer.valueOf(Constants_jar.PROCESS_NEED_DOWN_CASE_XML_END));
        _client.getParams().setParameter("http.socket.timeout", Integer.valueOf(Constants_jar.PROCESS_NEED_DOWN_CASE_XML_END));
        StringBuilder result = new StringBuilder();
        try {
            _post.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
            HttpResponse response = _client.execute(_post);
            if (response.getStatusLine().getStatusCode() == 200) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
                for (String s = reader.readLine(); s != null; s = reader.readLine()) {
                    result.append(s);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        CLog.e(TAG, "请求数据服务返回信息：" + result.toString());
        return result.toString();
    }
}
