package cn.com.contec_net_3_android_case;

import android.util.Base64;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import cn.com.contec.net.util.Constants_jar;
import cn.com.contec.net.util.MD5_encoding;

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

    /* JADX WARNING: type inference failed for: r23v0, types: [java.net.URLConnection] */
    /* JADX WARNING: Multi-variable type inference failed */
    /* JADX WARNING: Removed duplicated region for block: B:19:0x00e6 A[SYNTHETIC, Splitter:B:19:0x00e6] */
    /* JADX WARNING: Removed duplicated region for block: B:22:0x00ed A[Catch:{ IOException -> 0x0260 }] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.lang.String post(java.lang.String r28, java.util.Map<java.lang.String, java.lang.String> r29, cn.com.contec_net_3_android_case.FormFile[] r30) {
        /*
            r8 = 0
            r16 = 0
            java.lang.String r4 = "---------7d4a6d158c9"
            java.lang.String r5 = "multipart/form-data"
            java.net.URL r22 = new java.net.URL     // Catch:{ Exception -> 0x0266 }
            r0 = r22
            r1 = r28
            r0.<init>(r1)     // Catch:{ Exception -> 0x0266 }
            java.net.URLConnection r23 = r22.openConnection()     // Catch:{ Exception -> 0x0266 }
            r0 = r23
            java.net.HttpURLConnection r0 = (java.net.HttpURLConnection) r0     // Catch:{ Exception -> 0x0266 }
            r8 = r0
            r23 = 1
            r0 = r23
            r8.setDoInput(r0)     // Catch:{ Exception -> 0x0266 }
            r23 = 1
            r0 = r23
            r8.setDoOutput(r0)     // Catch:{ Exception -> 0x0266 }
            r23 = 0
            r0 = r23
            r8.setUseCaches(r0)     // Catch:{ Exception -> 0x0266 }
            r23 = 10000(0x2710, float:1.4013E-41)
            r0 = r23
            r8.setConnectTimeout(r0)     // Catch:{ Exception -> 0x0266 }
            r23 = 10000(0x2710, float:1.4013E-41)
            r0 = r23
            r8.setReadTimeout(r0)     // Catch:{ Exception -> 0x0266 }
            java.lang.String r23 = "POST"
            r0 = r23
            r8.setRequestMethod(r0)     // Catch:{ Exception -> 0x0266 }
            java.lang.String r23 = "Connection"
            java.lang.String r24 = "Keep-Alive"
            r0 = r23
            r1 = r24
            r8.setRequestProperty(r0, r1)     // Catch:{ Exception -> 0x0266 }
            java.lang.String r23 = "Charset"
            java.lang.String r24 = "GBK"
            r0 = r23
            r1 = r24
            r8.setRequestProperty(r0, r1)     // Catch:{ Exception -> 0x0266 }
            java.lang.String r23 = "Content-Type"
            java.lang.StringBuilder r24 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x0266 }
            java.lang.String r25 = java.lang.String.valueOf(r5)     // Catch:{ Exception -> 0x0266 }
            r24.<init>(r25)     // Catch:{ Exception -> 0x0266 }
            java.lang.String r25 = "; boundary="
            java.lang.StringBuilder r24 = r24.append(r25)     // Catch:{ Exception -> 0x0266 }
            r0 = r24
            java.lang.StringBuilder r24 = r0.append(r4)     // Catch:{ Exception -> 0x0266 }
            java.lang.String r24 = r24.toString()     // Catch:{ Exception -> 0x0266 }
            r0 = r23
            r1 = r24
            r8.setRequestProperty(r0, r1)     // Catch:{ Exception -> 0x0266 }
            java.lang.StringBuilder r20 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x0266 }
            r20.<init>()     // Catch:{ Exception -> 0x0266 }
            java.io.DataOutputStream r17 = new java.io.DataOutputStream     // Catch:{ Exception -> 0x0266 }
            java.io.OutputStream r23 = r8.getOutputStream()     // Catch:{ Exception -> 0x0266 }
            r0 = r17
            r1 = r23
            r0.<init>(r1)     // Catch:{ Exception -> 0x0266 }
            if (r29 == 0) goto L_0x009d
            java.util.Set r23 = r29.entrySet()     // Catch:{ Exception -> 0x00e1 }
            java.util.Iterator r24 = r23.iterator()     // Catch:{ Exception -> 0x00e1 }
        L_0x0097:
            boolean r23 = r24.hasNext()     // Catch:{ Exception -> 0x00e1 }
            if (r23 != 0) goto L_0x00f4
        L_0x009d:
            if (r30 == 0) goto L_0x00ac
            r0 = r30
            int r0 = r0.length     // Catch:{ Exception -> 0x00e1 }
            r24 = r0
            r23 = 0
        L_0x00a6:
            r0 = r23
            r1 = r24
            if (r0 < r1) goto L_0x0166
        L_0x00ac:
            java.lang.StringBuilder r23 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x00e1 }
            java.lang.String r24 = "--"
            r23.<init>(r24)     // Catch:{ Exception -> 0x00e1 }
            r0 = r23
            java.lang.StringBuilder r23 = r0.append(r4)     // Catch:{ Exception -> 0x00e1 }
            java.lang.String r24 = "--\r\n"
            java.lang.StringBuilder r23 = r23.append(r24)     // Catch:{ Exception -> 0x00e1 }
            java.lang.String r23 = r23.toString()     // Catch:{ Exception -> 0x00e1 }
            byte[] r11 = r23.getBytes()     // Catch:{ Exception -> 0x00e1 }
            r0 = r17
            r0.write(r11)     // Catch:{ Exception -> 0x00e1 }
            r17.flush()     // Catch:{ Exception -> 0x00e1 }
            int r7 = r8.getResponseCode()     // Catch:{ Exception -> 0x00e1 }
            r23 = 200(0xc8, float:2.8E-43)
            r0 = r23
            if (r7 == r0) goto L_0x020c
            java.lang.RuntimeException r23 = new java.lang.RuntimeException     // Catch:{ Exception -> 0x00e1 }
            java.lang.String r24 = "The request of url failure"
            r23.<init>(r24)     // Catch:{ Exception -> 0x00e1 }
            throw r23     // Catch:{ Exception -> 0x00e1 }
        L_0x00e1:
            r9 = move-exception
            r16 = r17
        L_0x00e4:
            if (r16 == 0) goto L_0x00eb
            r16.close()     // Catch:{ IOException -> 0x0260 }
            r16 = 0
        L_0x00eb:
            if (r8 == 0) goto L_0x00f1
            r8.disconnect()     // Catch:{ IOException -> 0x0260 }
            r8 = 0
        L_0x00f1:
            java.lang.String r15 = ""
        L_0x00f3:
            return r15
        L_0x00f4:
            java.lang.Object r12 = r24.next()     // Catch:{ Exception -> 0x00e1 }
            java.util.Map$Entry r12 = (java.util.Map.Entry) r12     // Catch:{ Exception -> 0x00e1 }
            java.lang.String r23 = "--"
            r0 = r20
            r1 = r23
            r0.append(r1)     // Catch:{ Exception -> 0x00e1 }
            r0 = r20
            r0.append(r4)     // Catch:{ Exception -> 0x00e1 }
            java.lang.String r23 = "\r\n"
            r0 = r20
            r1 = r23
            r0.append(r1)     // Catch:{ Exception -> 0x00e1 }
            java.lang.StringBuilder r25 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x00e1 }
            java.lang.String r23 = "Content-Disposition: form-data; name=\""
            r0 = r25
            r1 = r23
            r0.<init>(r1)     // Catch:{ Exception -> 0x00e1 }
            java.lang.Object r23 = r12.getKey()     // Catch:{ Exception -> 0x00e1 }
            java.lang.String r23 = (java.lang.String) r23     // Catch:{ Exception -> 0x00e1 }
            r0 = r25
            r1 = r23
            java.lang.StringBuilder r23 = r0.append(r1)     // Catch:{ Exception -> 0x00e1 }
            java.lang.String r25 = "\"\r\n\r\n"
            r0 = r23
            r1 = r25
            java.lang.StringBuilder r23 = r0.append(r1)     // Catch:{ Exception -> 0x00e1 }
            java.lang.String r23 = r23.toString()     // Catch:{ Exception -> 0x00e1 }
            r0 = r20
            r1 = r23
            r0.append(r1)     // Catch:{ Exception -> 0x00e1 }
            java.lang.Object r23 = r12.getValue()     // Catch:{ Exception -> 0x00e1 }
            java.lang.String r23 = (java.lang.String) r23     // Catch:{ Exception -> 0x00e1 }
            r0 = r20
            r1 = r23
            r0.append(r1)     // Catch:{ Exception -> 0x00e1 }
            java.lang.String r23 = "\r\n"
            r0 = r20
            r1 = r23
            r0.append(r1)     // Catch:{ Exception -> 0x00e1 }
            java.lang.String r23 = r20.toString()     // Catch:{ Exception -> 0x00e1 }
            byte[] r23 = r23.getBytes()     // Catch:{ Exception -> 0x00e1 }
            r0 = r17
            r1 = r23
            r0.write(r1)     // Catch:{ Exception -> 0x00e1 }
            goto L_0x0097
        L_0x0166:
            r13 = r30[r23]     // Catch:{ Exception -> 0x00e1 }
            java.lang.StringBuilder r21 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x00e1 }
            r21.<init>()     // Catch:{ Exception -> 0x00e1 }
            java.lang.String r25 = "--"
            r0 = r21
            r1 = r25
            r0.append(r1)     // Catch:{ Exception -> 0x00e1 }
            r0 = r21
            r0.append(r4)     // Catch:{ Exception -> 0x00e1 }
            java.lang.String r25 = "\r\n"
            r0 = r21
            r1 = r25
            r0.append(r1)     // Catch:{ Exception -> 0x00e1 }
            java.lang.StringBuilder r25 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x00e1 }
            java.lang.String r26 = "Content-Disposition: form-data;name=\""
            r25.<init>(r26)     // Catch:{ Exception -> 0x00e1 }
            java.lang.String r26 = r13.getFormname()     // Catch:{ Exception -> 0x00e1 }
            java.lang.StringBuilder r25 = r25.append(r26)     // Catch:{ Exception -> 0x00e1 }
            java.lang.String r26 = "\";filename=\""
            java.lang.StringBuilder r25 = r25.append(r26)     // Catch:{ Exception -> 0x00e1 }
            java.lang.String r26 = r13.getFilname()     // Catch:{ Exception -> 0x00e1 }
            java.lang.StringBuilder r25 = r25.append(r26)     // Catch:{ Exception -> 0x00e1 }
            java.lang.String r26 = "\"\r\n"
            java.lang.StringBuilder r25 = r25.append(r26)     // Catch:{ Exception -> 0x00e1 }
            java.lang.String r25 = r25.toString()     // Catch:{ Exception -> 0x00e1 }
            r0 = r21
            r1 = r25
            r0.append(r1)     // Catch:{ Exception -> 0x00e1 }
            java.lang.StringBuilder r25 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x00e1 }
            java.lang.String r26 = "Content-Type: "
            r25.<init>(r26)     // Catch:{ Exception -> 0x00e1 }
            java.lang.String r26 = r13.getContentType()     // Catch:{ Exception -> 0x00e1 }
            java.lang.StringBuilder r25 = r25.append(r26)     // Catch:{ Exception -> 0x00e1 }
            java.lang.String r26 = "\r\n\r\n"
            java.lang.StringBuilder r25 = r25.append(r26)     // Catch:{ Exception -> 0x00e1 }
            java.lang.String r25 = r25.toString()     // Catch:{ Exception -> 0x00e1 }
            r0 = r21
            r1 = r25
            r0.append(r1)     // Catch:{ Exception -> 0x00e1 }
            java.lang.String r25 = r21.toString()     // Catch:{ Exception -> 0x00e1 }
            byte[] r25 = r25.getBytes()     // Catch:{ Exception -> 0x00e1 }
            r0 = r17
            r1 = r25
            r0.write(r1)     // Catch:{ Exception -> 0x00e1 }
            byte[] r25 = r13.getData()     // Catch:{ Exception -> 0x00e1 }
            r26 = 0
            byte[] r27 = r13.getData()     // Catch:{ Exception -> 0x00e1 }
            r0 = r27
            int r0 = r0.length     // Catch:{ Exception -> 0x00e1 }
            r27 = r0
            r0 = r17
            r1 = r25
            r2 = r26
            r3 = r27
            r0.write(r1, r2, r3)     // Catch:{ Exception -> 0x00e1 }
            java.lang.String r25 = "\r\n"
            byte[] r25 = r25.getBytes()     // Catch:{ Exception -> 0x00e1 }
            r0 = r17
            r1 = r25
            r0.write(r1)     // Catch:{ Exception -> 0x00e1 }
            int r23 = r23 + 1
            goto L_0x00a6
        L_0x020c:
            java.io.InputStream r14 = r8.getInputStream()     // Catch:{ Exception -> 0x00e1 }
            java.io.BufferedReader r6 = new java.io.BufferedReader     // Catch:{ Exception -> 0x00e1 }
            java.io.InputStreamReader r23 = new java.io.InputStreamReader     // Catch:{ Exception -> 0x00e1 }
            java.lang.String r24 = "gbk"
            r0 = r23
            r1 = r24
            r0.<init>(r14, r1)     // Catch:{ Exception -> 0x00e1 }
            r0 = r23
            r6.<init>(r0)     // Catch:{ Exception -> 0x00e1 }
            java.lang.StringBuffer r18 = new java.lang.StringBuffer     // Catch:{ Exception -> 0x00e1 }
            r18.<init>()     // Catch:{ Exception -> 0x00e1 }
            java.lang.String r19 = ""
        L_0x0229:
            java.lang.String r19 = r6.readLine()     // Catch:{ Exception -> 0x00e1 }
            if (r19 != 0) goto L_0x0253
            java.lang.String r15 = new java.lang.String     // Catch:{ Exception -> 0x00e1 }
            java.lang.String r23 = r18.toString()     // Catch:{ Exception -> 0x00e1 }
            r0 = r23
            r15.<init>(r0)     // Catch:{ Exception -> 0x00e1 }
            r14.close()     // Catch:{ Exception -> 0x00e1 }
            r6.close()     // Catch:{ Exception -> 0x00e1 }
            r17.close()     // Catch:{ Exception -> 0x00e1 }
            r8.disconnect()     // Catch:{ Exception -> 0x00e1 }
            r22 = 0
            r20 = 0
            r14 = 0
            r6 = 0
            r18 = 0
            r16 = 0
            r8 = 0
            goto L_0x00f3
        L_0x0253:
            r18.append(r19)     // Catch:{ Exception -> 0x00e1 }
            java.lang.String r23 = "\r\n"
            r0 = r18
            r1 = r23
            r0.append(r1)     // Catch:{ Exception -> 0x00e1 }
            goto L_0x0229
        L_0x0260:
            r10 = move-exception
            r10.printStackTrace()
            goto L_0x00f1
        L_0x0266:
            r9 = move-exception
            goto L_0x00e4
        */
        throw new UnsupportedOperationException("Method not decompiled: cn.com.contec_net_3_android_case.MethodUploadFile.post(java.lang.String, java.util.Map, cn.com.contec_net_3_android_case.FormFile[]):java.lang.String");
    }
}
