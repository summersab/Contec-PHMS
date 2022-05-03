package cn.com.contec_net_3_android_case;

import com.j256.ormlite.stmt.query.SimpleComparison;
import com.lidroid.xutils.http.client.multipart.MIME;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;
import cn.com.contec.net.util.Constants_jar;
import u.aly.bs;

public class UploadUtil {
    public static String submitPostData(Map<String, String> params, String encode, URL url) {
        byte[] data = getRequestData(params, encode).toString().getBytes();
        try {
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setConnectTimeout(Constants_jar.PROCESS_NEED_DOWN_CASE_XML_END);
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setUseCaches(false);
            httpURLConnection.setRequestProperty(MIME.CONTENT_TYPE, "application/x-www-form-urlencoded");
            httpURLConnection.setRequestProperty("Content-Length", String.valueOf(data.length));
            httpURLConnection.getOutputStream().write(data);
            if (httpURLConnection.getResponseCode() == 200) {
                return dealResponseResult(httpURLConnection.getInputStream());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bs.b;
    }

    private static StringBuffer getRequestData(Map<String, String> params, String encode) {
        StringBuffer stringBuffer = new StringBuffer();
        try {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                stringBuffer.append(entry.getKey()).append(SimpleComparison.EQUAL_TO_OPERATION).append(URLEncoder.encode(entry.getValue(), encode)).append("&");
            }
            stringBuffer.deleteCharAt(stringBuffer.length() - 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stringBuffer;
    }

    private static String dealResponseResult(InputStream inputStream) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] data = new byte[1024];
        while (true) {
            try {
                int len = inputStream.read(data);
                if (len == -1) {
                    break;
                }
                byteArrayOutputStream.write(data, 0, len);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return new String(byteArrayOutputStream.toByteArray());
    }
}
