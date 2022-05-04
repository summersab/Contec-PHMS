package com.contec.phms.upload.cases.uploadfile;

import com.alibaba.cchannel.push.receiver.CPushMessageCodec;
import com.contec.phms.util.CLog;
import com.lidroid.xutils.http.client.multipart.MIME;

import org.apache.commons.httpclient.methods.MultipartPostMethod;
import org.dtools.ini.IniUtilities;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

import cn.com.contec.net.util.Constants_jar;
import u.aly.bs;

public class PostFile {
    public static final String TAG = "PostFile";

    public static String post(String actionUrl, Map<String, String> params, FormFile[] files) {
        HttpURLConnection conn = null;
        DataOutputStream outStream = null;
        try {
            URL url = new URL(actionUrl);
            conn = (HttpURLConnection) url.openConnection();
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.setConnectTimeout(Constants_jar.PROCESS_NEED_DOWN_CASE_XML_END);
            conn.setReadTimeout(Constants_jar.PROCESS_NEED_DOWN_CASE_XML_END);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("Charset", "GBK");
            conn.setRequestProperty(MIME.CONTENT_TYPE, String.valueOf(MultipartPostMethod.MULTIPART_FORM_CONTENT_TYPE) + "; boundary=---------7d4a6d158c9");
            StringBuilder sb = new StringBuilder();
            DataOutputStream outStream2 = new DataOutputStream(conn.getOutputStream());
            if (params != null) {
                try {
                    for (Map.Entry<String, String> entry : params.entrySet()) {
                        sb.append("--");
                        sb.append("---------7d4a6d158c9");
                        sb.append(IniUtilities.NEW_LINE);
                        sb.append("Content-Disposition: form-data; name=\"" + entry.getKey() + "\"\r\n\r\n");
                        sb.append(entry.getValue());
                        sb.append(IniUtilities.NEW_LINE);
                        outStream2.write(sb.toString().getBytes());
                    }
                } catch (Exception e) {
                    outStream = outStream2;
                    CLog.e(TAG, "Unknown Host");
                    if (outStream != null) {
                        try {
                            outStream.close();
                        } catch (IOException e1) {
                            e1.printStackTrace();
                            return bs.b;
                        }
                    }
                    if (conn != null) {
                        conn.disconnect();
                    }
                    return bs.b;
                }
            }
            if (files != null) {
                for (FormFile file : files) {
                    StringBuilder split = new StringBuilder();
                    split.append("--");
                    split.append("---------7d4a6d158c9");
                    split.append(IniUtilities.NEW_LINE);
                    split.append("Content-Disposition: form-data;name=\"" + file.getFormname() + "\";filename=\"" + file.getFilname() + "\"\r\n");
                    split.append("Content-Type: " + file.getContentType() + "\r\n\r\n");
                    outStream2.write(split.toString().getBytes());
                    outStream2.write(file.getData(), 0, file.getData().length);
                    outStream2.write(IniUtilities.NEW_LINE.getBytes());
                }
            }
            byte[] end_data = ("-----------7d4a6d158c9--\r\n").getBytes();
            outStream2.write(end_data);
            outStream2.flush();
            int cah = conn.getResponseCode();
            if (cah != 200) {
                throw new RuntimeException("The request of url failure");
            }
            InputStream is = conn.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is, CPushMessageCodec.GBK));
            StringBuffer resBuffer = new StringBuffer();
            while (true) {
                String resTemp = br.readLine();
                if (resTemp == null) {
                    String str = new String(resBuffer.toString());
                    is.close();
                    br.close();
                    outStream2.close();
                    conn.disconnect();
                    return str;
                }
                resBuffer.append(resTemp);
                resBuffer.append(IniUtilities.NEW_LINE);
            }
        } catch (Exception e2) {
        }
        return actionUrl;
    }
}
