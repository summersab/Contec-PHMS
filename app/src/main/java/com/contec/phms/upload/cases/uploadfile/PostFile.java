package com.contec.phms.upload.cases.uploadfile;

public class PostFile {
    public static final String TAG = "PostFile";

    /* JADX WARNING: type inference failed for: r23v1, types: [java.net.URLConnection] */
    /* JADX WARNING: Multi-variable type inference failed */
    /* JADX WARNING: Removed duplicated region for block: B:20:0x00ed A[SYNTHETIC, Splitter:B:20:0x00ed] */
    /* JADX WARNING: Removed duplicated region for block: B:23:0x00f4 A[Catch:{ IOException -> 0x0267 }] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.lang.String post(java.lang.String r28, java.util.Map<java.lang.String, java.lang.String> r29, com.contec.phms.upload.cases.uploadfile.FormFile[] r30) {
        /*
            r8 = 0
            r16 = 0
            java.lang.String r4 = "---------7d4a6d158c9"
            java.lang.String r5 = "multipart/form-data"
            java.net.URL r22 = new java.net.URL     // Catch:{ Exception -> 0x026d }
            r0 = r22
            r1 = r28
            r0.<init>(r1)     // Catch:{ Exception -> 0x026d }
            java.net.URLConnection r23 = r22.openConnection()     // Catch:{ Exception -> 0x026d }
            r0 = r23
            java.net.HttpURLConnection r0 = (java.net.HttpURLConnection) r0     // Catch:{ Exception -> 0x026d }
            r8 = r0
            r23 = 1
            r0 = r23
            r8.setDoInput(r0)     // Catch:{ Exception -> 0x026d }
            r23 = 1
            r0 = r23
            r8.setDoOutput(r0)     // Catch:{ Exception -> 0x026d }
            r23 = 0
            r0 = r23
            r8.setUseCaches(r0)     // Catch:{ Exception -> 0x026d }
            r23 = 10000(0x2710, float:1.4013E-41)
            r0 = r23
            r8.setConnectTimeout(r0)     // Catch:{ Exception -> 0x026d }
            r23 = 10000(0x2710, float:1.4013E-41)
            r0 = r23
            r8.setReadTimeout(r0)     // Catch:{ Exception -> 0x026d }
            java.lang.String r23 = "POST"
            r0 = r23
            r8.setRequestMethod(r0)     // Catch:{ Exception -> 0x026d }
            java.lang.String r23 = "Connection"
            java.lang.String r24 = "Keep-Alive"
            r0 = r23
            r1 = r24
            r8.setRequestProperty(r0, r1)     // Catch:{ Exception -> 0x026d }
            java.lang.String r23 = "Charset"
            java.lang.String r24 = "GBK"
            r0 = r23
            r1 = r24
            r8.setRequestProperty(r0, r1)     // Catch:{ Exception -> 0x026d }
            java.lang.String r23 = "Content-Type"
            java.lang.StringBuilder r24 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x026d }
            java.lang.String r25 = java.lang.String.valueOf(r5)     // Catch:{ Exception -> 0x026d }
            r24.<init>(r25)     // Catch:{ Exception -> 0x026d }
            java.lang.String r25 = "; boundary="
            java.lang.StringBuilder r24 = r24.append(r25)     // Catch:{ Exception -> 0x026d }
            r0 = r24
            java.lang.StringBuilder r24 = r0.append(r4)     // Catch:{ Exception -> 0x026d }
            java.lang.String r24 = r24.toString()     // Catch:{ Exception -> 0x026d }
            r0 = r23
            r1 = r24
            r8.setRequestProperty(r0, r1)     // Catch:{ Exception -> 0x026d }
            java.lang.StringBuilder r20 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x026d }
            r20.<init>()     // Catch:{ Exception -> 0x026d }
            java.io.DataOutputStream r17 = new java.io.DataOutputStream     // Catch:{ Exception -> 0x026d }
            java.io.OutputStream r23 = r8.getOutputStream()     // Catch:{ Exception -> 0x026d }
            r0 = r17
            r1 = r23
            r0.<init>(r1)     // Catch:{ Exception -> 0x026d }
            if (r29 == 0) goto L_0x009d
            java.util.Set r23 = r29.entrySet()     // Catch:{ Exception -> 0x00e1 }
            java.util.Iterator r24 = r23.iterator()     // Catch:{ Exception -> 0x00e1 }
        L_0x0097:
            boolean r23 = r24.hasNext()     // Catch:{ Exception -> 0x00e1 }
            if (r23 != 0) goto L_0x00fb
        L_0x009d:
            if (r30 == 0) goto L_0x00ac
            r0 = r30
            int r0 = r0.length     // Catch:{ Exception -> 0x00e1 }
            r24 = r0
            r23 = 0
        L_0x00a6:
            r0 = r23
            r1 = r24
            if (r0 < r1) goto L_0x016d
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
            if (r7 == r0) goto L_0x0213
            java.lang.RuntimeException r23 = new java.lang.RuntimeException     // Catch:{ Exception -> 0x00e1 }
            java.lang.String r24 = "The request of url failure"
            r23.<init>(r24)     // Catch:{ Exception -> 0x00e1 }
            throw r23     // Catch:{ Exception -> 0x00e1 }
        L_0x00e1:
            r9 = move-exception
            r16 = r17
        L_0x00e4:
            java.lang.String r23 = "PostFile"
            java.lang.String r24 = "Unknown Host"
            com.contec.phms.util.CLog.e(r23, r24)
            if (r16 == 0) goto L_0x00f2
            r16.close()     // Catch:{ IOException -> 0x0267 }
            r16 = 0
        L_0x00f2:
            if (r8 == 0) goto L_0x00f8
            r8.disconnect()     // Catch:{ IOException -> 0x0267 }
            r8 = 0
        L_0x00f8:
            java.lang.String r15 = ""
        L_0x00fa:
            return r15
        L_0x00fb:
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
        L_0x016d:
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
        L_0x0213:
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
        L_0x0230:
            java.lang.String r19 = r6.readLine()     // Catch:{ Exception -> 0x00e1 }
            if (r19 != 0) goto L_0x025a
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
            goto L_0x00fa
        L_0x025a:
            r18.append(r19)     // Catch:{ Exception -> 0x00e1 }
            java.lang.String r23 = "\r\n"
            r0 = r18
            r1 = r23
            r0.append(r1)     // Catch:{ Exception -> 0x00e1 }
            goto L_0x0230
        L_0x0267:
            r10 = move-exception
            r10.printStackTrace()
            goto L_0x00f8
        L_0x026d:
            r9 = move-exception
            goto L_0x00e4
        */
        throw new UnsupportedOperationException("Method not decompiled: com.contec.phms.upload.cases.uploadfile.PostFile.post(java.lang.String, java.util.Map, com.contec.phms.upload.cases.uploadfile.FormFile[]):java.lang.String");
    }
}
