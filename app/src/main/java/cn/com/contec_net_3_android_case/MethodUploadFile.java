package cn.com.contec_net_3_android_case;

import android.util.Base64;

import com.alibaba.cchannel.push.receiver.CPushMessageCodec;
import com.lidroid.xutils.http.client.multipart.MIME;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.httpclient.methods.MultipartPostMethod;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.dtools.ini.IniUtilities;

import cn.com.contec.net.util.Constants_jar;
import cn.com.contec.net.util.MD5_encoding;
import u.aly.bs;

public class MethodUploadFile {
    public static String uploadCase(String pSessionID, String pCaseId, int pTotalSize, ArrayList<Case> pList, String pCasePath, String pUserID, String pPsw, String pOrder, String pFilePath, String pUrl) {
        String _MessageHeader = "101007" + pSessionID + "11";
        String _MessageForm = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?><request><caseid>" + pCaseId + "</caseid>" + "<order>" + pOrder + "</order>" + "<totalsize>" + pTotalSize + "</totalsize>" + "<serveruri>" + pCasePath + "</serveruri>" + "<md51>" + "<start>" + pList.get(0).start + "</start>" + "<end>" + pList.get(0).end + "</end>" + "<md5>" + pList.get(0).MD5 + "</md5>" + "</md51>" + "<md52>" + "<start>" + pList.get(1).start + "</start>" + "<end>" + pList.get(1).end + "</end>" + "<md5>" + pList.get(1).MD5 + "</md5>" + "</md52>" + "<md53>" + "<start>" + pList.get(2).start + "</start>" + "<end>" + pList.get(2).end + "</end>" + "<md5>" + pList.get(2).MD5 + "</md5>" + "</md53>" + "</request>";
        System.out.println(_MessageForm);
        String _psw_user_md5 = MD5_encoding.MD5(String.valueOf(pUserID) + pPsw);
        String _base64Message = Base64.encodeToString(_MessageForm.getBytes(), 0);
        return uploadFile(pUrl, String.valueOf(String.valueOf(MD5_encoding.MD5(String.valueOf(_psw_user_md5) + _MessageHeader + _base64Message)) + _MessageHeader) + _base64Message, pFilePath);
    }

    private static String uploadFile(String url, String msg, String filePath) {
        try {
            FileInputStream fis = new FileInputStream(new File(filePath));
            byte[] data = new byte[fis.available()];
            fis.read(data);
            fis.close();
            Map<String, String> map = new HashMap<>();
            map.put(Constants_jar.MSG_STRING, msg);
            return post(url, map, new FormFile[]{new FormFile(filePath, data, "filename", FilePart.DEFAULT_CONTENT_TYPE)});
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

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
