package com.contec.phms.device.cms50k.update;

import android.os.AsyncTask;
import android.util.Log;
import com.contec.phms.App_phms;
import com.contec.phms.util.CLog;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.List;
import org.apache.commons.httpclient.cookie.CookieSpec;
import u.aly.bs;

public class DownLoadFile {
    public static final String TAG = DownLoadFile.class.getSimpleName();
    int mCurrentVersion = 0;
    int mMaxVersionValue = 0;

    public void startDownFile() {
        new MyTask().execute(new Void[0]);
    }

    class MyTask extends AsyncTask<Void, Void, List<Xml50KUpdateBean>> {
        MyTask() {
        }

        protected List<Xml50KUpdateBean> doInBackground(Void... arg0) {
            try {
                String respones = httpUtils.httpGet(Update50KUtils.XmlURL, bs.b);
                Log.i("info", respones);
                return Update50KUtils.XMLParser(respones);
            } catch (Exception e) {
                Log.i("info", "抛出异常了");
                e.printStackTrace();
                return null;
            }
        }

        protected void onPostExecute(List<Xml50KUpdateBean> xmlBeanLists) {
            super.onPostExecute(xmlBeanLists);
            if (xmlBeanLists != null && xmlBeanLists.size() > 0) {
                Update50KUtils.createUpdateXmlFile(xmlBeanLists);
                int maxVersionIndex = 0;
                for (int i = 0; i < xmlBeanLists.size(); i++) {
                    String version = xmlBeanLists.get(i).version;
                    if (Integer.valueOf(version).intValue() > DownLoadFile.this.mMaxVersionValue) {
                        maxVersionIndex = i;
                        DownLoadFile.this.mMaxVersionValue = Integer.valueOf(version).intValue();
                    }
                }
                CLog.i("jx", "highest version: " + DownLoadFile.this.mMaxVersionValue + "--index: " + maxVersionIndex);
                Xml50KUpdateBean xmlBean = xmlBeanLists.get(maxVersionIndex);
                DownLoadFile.this.mCurrentVersion = App_phms.preferences.getInt("currentVersion", 999);
                boolean ifFileExit = true;
                if (!new File(Update50KUtils.fileUrl).exists()) {
                    ifFileExit = false;
                }
                if (!new File(String.valueOf(Update50KUtils.fileUrl) + Update50KUtils.updateFilename).exists()) {
                    ifFileExit = false;
                }
                if (DownLoadFile.this.mCurrentVersion == 999 || DownLoadFile.this.mMaxVersionValue > DownLoadFile.this.mCurrentVersion || !ifFileExit) {
                    new downloadFileThread(Update50KUtils.MainURl + xmlBean.path + CookieSpec.PATH_DELIM + xmlBean.fname).start();
                }
            }
        }
    }

    private class downloadFileThread extends Thread {
        private FileOutputStream fos;
        private InputStream is;
        private int length;
        private String urlx = bs.b;

        public downloadFileThread(String url) {
            this.urlx = url;
        }

        /*  JADX ERROR: StackOverflow in pass: MarkFinallyVisitor
            jadx.core.utils.exceptions.JadxOverflowException: 
            	at jadx.core.utils.ErrorsCounter.addError(ErrorsCounter.java:47)
            	at jadx.core.utils.ErrorsCounter.methodError(ErrorsCounter.java:81)
            */
        public void run() {
            /*
                r29 = this;
                java.lang.String r25 = android.os.Environment.getExternalStorageState()     // Catch:{ MalformedURLException -> 0x013f, IOException -> 0x02ea }
                java.lang.String r26 = "mounted"
                boolean r25 = r25.equals(r26)     // Catch:{ MalformedURLException -> 0x013f, IOException -> 0x02ea }
                if (r25 == 0) goto L_0x00ba
                java.lang.String r20 = com.contec.phms.device.cms50k.update.Update50KUtils.fileUrl     // Catch:{ MalformedURLException -> 0x013f, IOException -> 0x02ea }
                java.net.URL r23 = new java.net.URL     // Catch:{ MalformedURLException -> 0x013f, IOException -> 0x02ea }
                r0 = r29
                java.lang.String r0 = r0.urlx     // Catch:{ MalformedURLException -> 0x013f, IOException -> 0x02ea }
                r25 = r0
                r0 = r23
                r1 = r25
                r0.<init>(r1)     // Catch:{ MalformedURLException -> 0x013f, IOException -> 0x02ea }
                java.net.URLConnection r9 = r23.openConnection()     // Catch:{ MalformedURLException -> 0x013f, IOException -> 0x02ea }
                java.net.HttpURLConnection r9 = (java.net.HttpURLConnection) r9     // Catch:{ MalformedURLException -> 0x013f, IOException -> 0x02ea }
                r25 = 6000(0x1770, float:8.408E-42)
                r0 = r25
                r9.setConnectTimeout(r0)     // Catch:{ MalformedURLException -> 0x013f, IOException -> 0x02ea }
                java.lang.String r25 = "GET"
                r0 = r25
                r9.setRequestMethod(r0)     // Catch:{ MalformedURLException -> 0x013f, IOException -> 0x02ea }
                r9.connect()     // Catch:{ MalformedURLException -> 0x013f, IOException -> 0x02ea }
                int r25 = r9.getContentLength()     // Catch:{ MalformedURLException -> 0x013f, IOException -> 0x02ea }
                r0 = r25
                r1 = r29
                r1.length = r0     // Catch:{ MalformedURLException -> 0x013f, IOException -> 0x02ea }
                android.content.SharedPreferences r25 = com.contec.phms.App_phms.preferences     // Catch:{ MalformedURLException -> 0x013f, IOException -> 0x02ea }
                android.content.SharedPreferences$Editor r11 = r25.edit()     // Catch:{ MalformedURLException -> 0x013f, IOException -> 0x02ea }
                java.lang.String r25 = "long"
                r0 = r29
                int r0 = r0.length     // Catch:{ MalformedURLException -> 0x013f, IOException -> 0x02ea }
                r26 = r0
                r0 = r26
                long r0 = (long) r0     // Catch:{ MalformedURLException -> 0x013f, IOException -> 0x02ea }
                r26 = r0
                r0 = r25
                r1 = r26
                r11.putLong(r0, r1)     // Catch:{ MalformedURLException -> 0x013f, IOException -> 0x02ea }
                r11.commit()     // Catch:{ MalformedURLException -> 0x013f, IOException -> 0x02ea }
                java.io.InputStream r25 = r9.getInputStream()     // Catch:{ MalformedURLException -> 0x013f, IOException -> 0x02ea }
                r0 = r25
                r1 = r29
                r1.is = r0     // Catch:{ MalformedURLException -> 0x013f, IOException -> 0x02ea }
                java.io.File r15 = new java.io.File     // Catch:{ MalformedURLException -> 0x013f, IOException -> 0x02ea }
                r0 = r20
                r15.<init>(r0)     // Catch:{ MalformedURLException -> 0x013f, IOException -> 0x02ea }
                boolean r25 = r15.exists()     // Catch:{ MalformedURLException -> 0x013f, IOException -> 0x02ea }
                if (r25 != 0) goto L_0x0075
                r15.mkdir()     // Catch:{ MalformedURLException -> 0x013f, IOException -> 0x02ea }
            L_0x0075:
                java.io.File r6 = new java.io.File     // Catch:{ MalformedURLException -> 0x013f, IOException -> 0x02ea }
                java.lang.String r25 = "update.bin"
                r0 = r20
                r1 = r25
                r6.<init>(r0, r1)     // Catch:{ MalformedURLException -> 0x013f, IOException -> 0x02ea }
                boolean r25 = r6.exists()     // Catch:{ MalformedURLException -> 0x013f, IOException -> 0x02ea }
                if (r25 == 0) goto L_0x008c
                r6.delete()     // Catch:{ MalformedURLException -> 0x013f, IOException -> 0x02ea }
                r6.createNewFile()     // Catch:{ MalformedURLException -> 0x013f, IOException -> 0x02ea }
            L_0x008c:
                java.io.FileOutputStream r25 = new java.io.FileOutputStream     // Catch:{ MalformedURLException -> 0x013f, IOException -> 0x02ea }
                r0 = r25
                r0.<init>(r6)     // Catch:{ MalformedURLException -> 0x013f, IOException -> 0x02ea }
                r0 = r25
                r1 = r29
                r1.fos = r0     // Catch:{ MalformedURLException -> 0x013f, IOException -> 0x02ea }
                r25 = 1024(0x400, float:1.435E-42)
                r0 = r25
                byte[] r8 = new byte[r0]     // Catch:{ MalformedURLException -> 0x013f, IOException -> 0x02ea }
            L_0x009f:
                r0 = r29
                java.io.InputStream r0 = r0.is     // Catch:{ MalformedURLException -> 0x013f, IOException -> 0x02ea }
                r25 = r0
                r0 = r25
                int r18 = r0.read(r8)     // Catch:{ MalformedURLException -> 0x013f, IOException -> 0x02ea }
                r25 = -1
                r0 = r18
                r1 = r25
                if (r0 != r1) goto L_0x012c
                java.lang.String r25 = "jx"
                java.lang.String r26 = "写入成功"
                android.util.Log.i(r25, r26)     // Catch:{ MalformedURLException -> 0x013f, IOException -> 0x02ea }
            L_0x00ba:
                r0 = r29
                java.io.FileOutputStream r0 = r0.fos     // Catch:{ IOException -> 0x063c }
                r25 = r0
                if (r25 == 0) goto L_0x00cb
                r0 = r29
                java.io.FileOutputStream r0 = r0.fos     // Catch:{ IOException -> 0x063c }
                r25 = r0
                r25.close()     // Catch:{ IOException -> 0x063c }
            L_0x00cb:
                r0 = r29
                java.io.InputStream r0 = r0.is     // Catch:{ IOException -> 0x0642 }
                r25 = r0
                if (r25 == 0) goto L_0x00dc
                r0 = r29
                java.io.InputStream r0 = r0.is     // Catch:{ IOException -> 0x0642 }
                r25 = r0
                r25.close()     // Catch:{ IOException -> 0x0642 }
            L_0x00dc:
                java.io.File r25 = new java.io.File
                java.lang.String r26 = com.contec.phms.device.cms50k.update.Update50KUtils.fileUrl
                java.lang.String r27 = "update.bin"
                r25.<init>(r26, r27)
                java.lang.String r16 = com.contec.phms.device.cms50k.update.Update50KUtils.getFileMD5(r25)
                java.util.List r7 = com.contec.phms.device.cms50k.update.Update50KUtils.pullParseXmlFile()
                r0 = r29
                com.contec.phms.device.cms50k.update.DownLoadFile r0 = com.contec.phms.device.cms50k.update.DownLoadFile.this
                r25 = r0
                r0 = r25
                java.lang.String r24 = r0.getXmlMd5(r7)
                r0 = r29
                com.contec.phms.device.cms50k.update.DownLoadFile r0 = com.contec.phms.device.cms50k.update.DownLoadFile.this
                r25 = r0
                r0 = r25
                r1 = r16
                r2 = r24
                boolean r17 = r0.compareMd5(r1, r2)
                if (r17 != 0) goto L_0x0691
                java.io.File r15 = new java.io.File
                java.lang.String r25 = com.contec.phms.device.cms50k.update.Update50KUtils.fileUrl
                r0 = r25
                r15.<init>(r0)
                boolean r25 = r15.exists()
                if (r25 == 0) goto L_0x0655
                boolean r25 = r15.isDirectory()
                if (r25 == 0) goto L_0x0655
                java.io.File[] r19 = r15.listFiles()
                r0 = r19
                int r0 = r0.length
                r25 = r0
                if (r25 != 0) goto L_0x0648
            L_0x012b:
                return
            L_0x012c:
                r0 = r29
                java.io.FileOutputStream r0 = r0.fos     // Catch:{ MalformedURLException -> 0x013f, IOException -> 0x02ea }
                r25 = r0
                r26 = 0
                r0 = r25
                r1 = r26
                r2 = r18
                r0.write(r8, r1, r2)     // Catch:{ MalformedURLException -> 0x013f, IOException -> 0x02ea }
                goto L_0x009f
            L_0x013f:
                r10 = move-exception
                r10.printStackTrace()     // Catch:{ all -> 0x0495 }
                r0 = r29
                java.io.FileOutputStream r0 = r0.fos     // Catch:{ IOException -> 0x01ef }
                r25 = r0
                if (r25 == 0) goto L_0x0154
                r0 = r29
                java.io.FileOutputStream r0 = r0.fos     // Catch:{ IOException -> 0x01ef }
                r25 = r0
                r25.close()     // Catch:{ IOException -> 0x01ef }
            L_0x0154:
                r0 = r29
                java.io.InputStream r0 = r0.is     // Catch:{ IOException -> 0x01f5 }
                r25 = r0
                if (r25 == 0) goto L_0x0165
                r0 = r29
                java.io.InputStream r0 = r0.is     // Catch:{ IOException -> 0x01f5 }
                r25 = r0
                r25.close()     // Catch:{ IOException -> 0x01f5 }
            L_0x0165:
                java.io.File r25 = new java.io.File
                java.lang.String r26 = com.contec.phms.device.cms50k.update.Update50KUtils.fileUrl
                java.lang.String r27 = "update.bin"
                r25.<init>(r26, r27)
                java.lang.String r16 = com.contec.phms.device.cms50k.update.Update50KUtils.getFileMD5(r25)
                java.util.List r7 = com.contec.phms.device.cms50k.update.Update50KUtils.pullParseXmlFile()
                r0 = r29
                com.contec.phms.device.cms50k.update.DownLoadFile r0 = com.contec.phms.device.cms50k.update.DownLoadFile.this
                r25 = r0
                r0 = r25
                java.lang.String r24 = r0.getXmlMd5(r7)
                r0 = r29
                com.contec.phms.device.cms50k.update.DownLoadFile r0 = com.contec.phms.device.cms50k.update.DownLoadFile.this
                r25 = r0
                r0 = r25
                r1 = r16
                r2 = r24
                boolean r17 = r0.compareMd5(r1, r2)
                if (r17 != 0) goto L_0x0209
                java.io.File r15 = new java.io.File
                java.lang.String r25 = com.contec.phms.device.cms50k.update.Update50KUtils.fileUrl
                r0 = r25
                r15.<init>(r0)
                boolean r25 = r15.exists()
                if (r25 == 0) goto L_0x01c1
                boolean r25 = r15.isDirectory()
                if (r25 == 0) goto L_0x01c1
                java.io.File[] r19 = r15.listFiles()
                r0 = r19
                int r0 = r0.length
                r25 = r0
                if (r25 == 0) goto L_0x012b
                r0 = r19
                int r0 = r0.length
                r26 = r0
                r25 = 0
            L_0x01bb:
                r0 = r25
                r1 = r26
                if (r0 < r1) goto L_0x01fb
            L_0x01c1:
                android.content.SharedPreferences r25 = com.contec.phms.App_phms.preferences
                android.content.SharedPreferences$Editor r12 = r25.edit()
                java.lang.String r25 = "currentVersion"
                r26 = 999(0x3e7, float:1.4E-42)
                r0 = r25
                r1 = r26
                r12.putInt(r0, r1)
                r12.commit()
            L_0x01d5:
                java.lang.String r25 = "jxx"
                java.lang.StringBuilder r26 = new java.lang.StringBuilder
                java.lang.String r27 = "md5值是否一样："
                r26.<init>(r27)
                r0 = r26
                r1 = r17
                java.lang.StringBuilder r26 = r0.append(r1)
                java.lang.String r26 = r26.toString()
                android.util.Log.i(r25, r26)
                goto L_0x012b
            L_0x01ef:
                r10 = move-exception
                r10.printStackTrace()
                goto L_0x0154
            L_0x01f5:
                r10 = move-exception
                r10.printStackTrace()
                goto L_0x0165
            L_0x01fb:
                r14 = r19[r25]
                boolean r27 = r14.isFile()
                if (r27 == 0) goto L_0x0206
                r14.delete()
            L_0x0206:
                int r25 = r25 + 1
                goto L_0x01bb
            L_0x0209:
                java.lang.StringBuilder r25 = new java.lang.StringBuilder
                java.lang.String r26 = com.contec.phms.device.cms50k.update.Update50KUtils.fileUrl
                java.lang.String r26 = java.lang.String.valueOf(r26)
                r25.<init>(r26)
                java.lang.String r26 = "update.bin"
                java.lang.StringBuilder r25 = r25.append(r26)
                java.lang.String r25 = r25.toString()
                byte[] r21 = com.contec.phms.device.cms50k.update.Update50KUtils.readSDFile(r25)
                if (r21 == 0) goto L_0x02c0
                r25 = 16
                r0 = r25
                byte[] r0 = new byte[r0]
                r22 = r0
                r25 = 0
                r26 = 0
                r27 = 16
                r0 = r21
                r1 = r25
                r2 = r22
                r3 = r26
                r4 = r27
                java.lang.System.arraycopy(r0, r1, r2, r3, r4)
                r25 = 7
                byte r25 = r22[r25]
                int r25 = r25 << 8
                r26 = 6
                byte r26 = r22[r26]
                r0 = r26
                r0 = r0 & 255(0xff, float:3.57E-43)
                r26 = r0
                r25 = r25 | r26
                r26 = 65535(0xffff, float:9.1834E-41)
                r5 = r25 & r26
                java.lang.String r25 = "jxx"
                java.lang.StringBuilder r26 = new java.lang.StringBuilder
                java.lang.String r27 = "版本号："
                r26.<init>(r27)
                r0 = r26
                java.lang.StringBuilder r26 = r0.append(r5)
                java.lang.String r27 = "--"
                java.lang.StringBuilder r26 = r26.append(r27)
                r27 = 6
                byte r27 = r22[r27]
                java.lang.String r27 = java.lang.Integer.toHexString(r27)
                java.lang.StringBuilder r26 = r26.append(r27)
                java.lang.String r27 = " "
                java.lang.StringBuilder r26 = r26.append(r27)
                r27 = 7
                byte r27 = r22[r27]
                java.lang.String r27 = java.lang.Integer.toHexString(r27)
                java.lang.StringBuilder r26 = r26.append(r27)
                java.lang.String r26 = r26.toString()
                android.util.Log.i(r25, r26)
                android.content.SharedPreferences r25 = com.contec.phms.App_phms.preferences
                android.content.SharedPreferences$Editor r13 = r25.edit()
                java.lang.String r25 = "CMS50K_Server_Version"
                r0 = r25
                r13.putInt(r0, r5)
                r13.commit()
                android.content.SharedPreferences r25 = com.contec.phms.App_phms.preferences
                android.content.SharedPreferences$Editor r12 = r25.edit()
                java.lang.String r25 = "currentVersion"
                r0 = r29
                com.contec.phms.device.cms50k.update.DownLoadFile r0 = com.contec.phms.device.cms50k.update.DownLoadFile.this
                r26 = r0
                r0 = r26
                int r0 = r0.mMaxVersionValue
                r26 = r0
                r0 = r25
                r1 = r26
                r12.putInt(r0, r1)
                r12.commit()
                goto L_0x01d5
            L_0x02c0:
                android.content.SharedPreferences r25 = com.contec.phms.App_phms.preferences
                android.content.SharedPreferences$Editor r12 = r25.edit()
                java.lang.String r25 = "CMS50K_Server_Version"
                r26 = 999(0x3e7, float:1.4E-42)
                r0 = r25
                r1 = r26
                r12.putInt(r0, r1)
                r12.commit()
                android.content.SharedPreferences r25 = com.contec.phms.App_phms.preferences
                android.content.SharedPreferences$Editor r13 = r25.edit()
                java.lang.String r25 = "currentVersion"
                r26 = 999(0x3e7, float:1.4E-42)
                r0 = r25
                r1 = r26
                r13.putInt(r0, r1)
                r13.commit()
                goto L_0x01d5
            L_0x02ea:
                r10 = move-exception
                r10.printStackTrace()     // Catch:{ all -> 0x0495 }
                r0 = r29
                java.io.FileOutputStream r0 = r0.fos     // Catch:{ IOException -> 0x039a }
                r25 = r0
                if (r25 == 0) goto L_0x02ff
                r0 = r29
                java.io.FileOutputStream r0 = r0.fos     // Catch:{ IOException -> 0x039a }
                r25 = r0
                r25.close()     // Catch:{ IOException -> 0x039a }
            L_0x02ff:
                r0 = r29
                java.io.InputStream r0 = r0.is     // Catch:{ IOException -> 0x03a0 }
                r25 = r0
                if (r25 == 0) goto L_0x0310
                r0 = r29
                java.io.InputStream r0 = r0.is     // Catch:{ IOException -> 0x03a0 }
                r25 = r0
                r25.close()     // Catch:{ IOException -> 0x03a0 }
            L_0x0310:
                java.io.File r25 = new java.io.File
                java.lang.String r26 = com.contec.phms.device.cms50k.update.Update50KUtils.fileUrl
                java.lang.String r27 = "update.bin"
                r25.<init>(r26, r27)
                java.lang.String r16 = com.contec.phms.device.cms50k.update.Update50KUtils.getFileMD5(r25)
                java.util.List r7 = com.contec.phms.device.cms50k.update.Update50KUtils.pullParseXmlFile()
                r0 = r29
                com.contec.phms.device.cms50k.update.DownLoadFile r0 = com.contec.phms.device.cms50k.update.DownLoadFile.this
                r25 = r0
                r0 = r25
                java.lang.String r24 = r0.getXmlMd5(r7)
                r0 = r29
                com.contec.phms.device.cms50k.update.DownLoadFile r0 = com.contec.phms.device.cms50k.update.DownLoadFile.this
                r25 = r0
                r0 = r25
                r1 = r16
                r2 = r24
                boolean r17 = r0.compareMd5(r1, r2)
                if (r17 != 0) goto L_0x03b4
                java.io.File r15 = new java.io.File
                java.lang.String r25 = com.contec.phms.device.cms50k.update.Update50KUtils.fileUrl
                r0 = r25
                r15.<init>(r0)
                boolean r25 = r15.exists()
                if (r25 == 0) goto L_0x036c
                boolean r25 = r15.isDirectory()
                if (r25 == 0) goto L_0x036c
                java.io.File[] r19 = r15.listFiles()
                r0 = r19
                int r0 = r0.length
                r25 = r0
                if (r25 == 0) goto L_0x012b
                r0 = r19
                int r0 = r0.length
                r26 = r0
                r25 = 0
            L_0x0366:
                r0 = r25
                r1 = r26
                if (r0 < r1) goto L_0x03a6
            L_0x036c:
                android.content.SharedPreferences r25 = com.contec.phms.App_phms.preferences
                android.content.SharedPreferences$Editor r12 = r25.edit()
                java.lang.String r25 = "currentVersion"
                r26 = 999(0x3e7, float:1.4E-42)
                r0 = r25
                r1 = r26
                r12.putInt(r0, r1)
                r12.commit()
            L_0x0380:
                java.lang.String r25 = "jxx"
                java.lang.StringBuilder r26 = new java.lang.StringBuilder
                java.lang.String r27 = "md5值是否一样："
                r26.<init>(r27)
                r0 = r26
                r1 = r17
                java.lang.StringBuilder r26 = r0.append(r1)
                java.lang.String r26 = r26.toString()
                android.util.Log.i(r25, r26)
                goto L_0x012b
            L_0x039a:
                r10 = move-exception
                r10.printStackTrace()
                goto L_0x02ff
            L_0x03a0:
                r10 = move-exception
                r10.printStackTrace()
                goto L_0x0310
            L_0x03a6:
                r14 = r19[r25]
                boolean r27 = r14.isFile()
                if (r27 == 0) goto L_0x03b1
                r14.delete()
            L_0x03b1:
                int r25 = r25 + 1
                goto L_0x0366
            L_0x03b4:
                java.lang.StringBuilder r25 = new java.lang.StringBuilder
                java.lang.String r26 = com.contec.phms.device.cms50k.update.Update50KUtils.fileUrl
                java.lang.String r26 = java.lang.String.valueOf(r26)
                r25.<init>(r26)
                java.lang.String r26 = "update.bin"
                java.lang.StringBuilder r25 = r25.append(r26)
                java.lang.String r25 = r25.toString()
                byte[] r21 = com.contec.phms.device.cms50k.update.Update50KUtils.readSDFile(r25)
                if (r21 == 0) goto L_0x046b
                r25 = 16
                r0 = r25
                byte[] r0 = new byte[r0]
                r22 = r0
                r25 = 0
                r26 = 0
                r27 = 16
                r0 = r21
                r1 = r25
                r2 = r22
                r3 = r26
                r4 = r27
                java.lang.System.arraycopy(r0, r1, r2, r3, r4)
                r25 = 7
                byte r25 = r22[r25]
                int r25 = r25 << 8
                r26 = 6
                byte r26 = r22[r26]
                r0 = r26
                r0 = r0 & 255(0xff, float:3.57E-43)
                r26 = r0
                r25 = r25 | r26
                r26 = 65535(0xffff, float:9.1834E-41)
                r5 = r25 & r26
                java.lang.String r25 = "jxx"
                java.lang.StringBuilder r26 = new java.lang.StringBuilder
                java.lang.String r27 = "版本号："
                r26.<init>(r27)
                r0 = r26
                java.lang.StringBuilder r26 = r0.append(r5)
                java.lang.String r27 = "--"
                java.lang.StringBuilder r26 = r26.append(r27)
                r27 = 6
                byte r27 = r22[r27]
                java.lang.String r27 = java.lang.Integer.toHexString(r27)
                java.lang.StringBuilder r26 = r26.append(r27)
                java.lang.String r27 = " "
                java.lang.StringBuilder r26 = r26.append(r27)
                r27 = 7
                byte r27 = r22[r27]
                java.lang.String r27 = java.lang.Integer.toHexString(r27)
                java.lang.StringBuilder r26 = r26.append(r27)
                java.lang.String r26 = r26.toString()
                android.util.Log.i(r25, r26)
                android.content.SharedPreferences r25 = com.contec.phms.App_phms.preferences
                android.content.SharedPreferences$Editor r13 = r25.edit()
                java.lang.String r25 = "CMS50K_Server_Version"
                r0 = r25
                r13.putInt(r0, r5)
                r13.commit()
                android.content.SharedPreferences r25 = com.contec.phms.App_phms.preferences
                android.content.SharedPreferences$Editor r12 = r25.edit()
                java.lang.String r25 = "currentVersion"
                r0 = r29
                com.contec.phms.device.cms50k.update.DownLoadFile r0 = com.contec.phms.device.cms50k.update.DownLoadFile.this
                r26 = r0
                r0 = r26
                int r0 = r0.mMaxVersionValue
                r26 = r0
                r0 = r25
                r1 = r26
                r12.putInt(r0, r1)
                r12.commit()
                goto L_0x0380
            L_0x046b:
                android.content.SharedPreferences r25 = com.contec.phms.App_phms.preferences
                android.content.SharedPreferences$Editor r12 = r25.edit()
                java.lang.String r25 = "CMS50K_Server_Version"
                r26 = 999(0x3e7, float:1.4E-42)
                r0 = r25
                r1 = r26
                r12.putInt(r0, r1)
                r12.commit()
                android.content.SharedPreferences r25 = com.contec.phms.App_phms.preferences
                android.content.SharedPreferences$Editor r13 = r25.edit()
                java.lang.String r25 = "currentVersion"
                r26 = 999(0x3e7, float:1.4E-42)
                r0 = r25
                r1 = r26
                r13.putInt(r0, r1)
                r13.commit()
                goto L_0x0380
            L_0x0495:
                r25 = move-exception
                r0 = r29
                java.io.FileOutputStream r0 = r0.fos     // Catch:{ IOException -> 0x0541 }
                r26 = r0
                if (r26 == 0) goto L_0x04a7
                r0 = r29
                java.io.FileOutputStream r0 = r0.fos     // Catch:{ IOException -> 0x0541 }
                r26 = r0
                r26.close()     // Catch:{ IOException -> 0x0541 }
            L_0x04a7:
                r0 = r29
                java.io.InputStream r0 = r0.is     // Catch:{ IOException -> 0x0547 }
                r26 = r0
                if (r26 == 0) goto L_0x04b8
                r0 = r29
                java.io.InputStream r0 = r0.is     // Catch:{ IOException -> 0x0547 }
                r26 = r0
                r26.close()     // Catch:{ IOException -> 0x0547 }
            L_0x04b8:
                java.io.File r26 = new java.io.File
                java.lang.String r27 = com.contec.phms.device.cms50k.update.Update50KUtils.fileUrl
                java.lang.String r28 = "update.bin"
                r26.<init>(r27, r28)
                java.lang.String r16 = com.contec.phms.device.cms50k.update.Update50KUtils.getFileMD5(r26)
                java.util.List r7 = com.contec.phms.device.cms50k.update.Update50KUtils.pullParseXmlFile()
                r0 = r29
                com.contec.phms.device.cms50k.update.DownLoadFile r0 = com.contec.phms.device.cms50k.update.DownLoadFile.this
                r26 = r0
                r0 = r26
                java.lang.String r24 = r0.getXmlMd5(r7)
                r0 = r29
                com.contec.phms.device.cms50k.update.DownLoadFile r0 = com.contec.phms.device.cms50k.update.DownLoadFile.this
                r26 = r0
                r0 = r26
                r1 = r16
                r2 = r24
                boolean r17 = r0.compareMd5(r1, r2)
                if (r17 != 0) goto L_0x055b
                java.io.File r15 = new java.io.File
                java.lang.String r26 = com.contec.phms.device.cms50k.update.Update50KUtils.fileUrl
                r0 = r26
                r15.<init>(r0)
                boolean r26 = r15.exists()
                if (r26 == 0) goto L_0x0514
                boolean r26 = r15.isDirectory()
                if (r26 == 0) goto L_0x0514
                java.io.File[] r19 = r15.listFiles()
                r0 = r19
                int r0 = r0.length
                r26 = r0
                if (r26 == 0) goto L_0x012b
                r0 = r19
                int r0 = r0.length
                r27 = r0
                r26 = 0
            L_0x050e:
                r0 = r26
                r1 = r27
                if (r0 < r1) goto L_0x054d
            L_0x0514:
                android.content.SharedPreferences r26 = com.contec.phms.App_phms.preferences
                android.content.SharedPreferences$Editor r12 = r26.edit()
                java.lang.String r26 = "currentVersion"
                r27 = 999(0x3e7, float:1.4E-42)
                r0 = r26
                r1 = r27
                r12.putInt(r0, r1)
                r12.commit()
            L_0x0528:
                java.lang.String r26 = "jxx"
                java.lang.StringBuilder r27 = new java.lang.StringBuilder
                java.lang.String r28 = "md5值是否一样："
                r27.<init>(r28)
                r0 = r27
                r1 = r17
                java.lang.StringBuilder r27 = r0.append(r1)
                java.lang.String r27 = r27.toString()
                android.util.Log.i(r26, r27)
                throw r25
            L_0x0541:
                r10 = move-exception
                r10.printStackTrace()
                goto L_0x04a7
            L_0x0547:
                r10 = move-exception
                r10.printStackTrace()
                goto L_0x04b8
            L_0x054d:
                r14 = r19[r26]
                boolean r28 = r14.isFile()
                if (r28 == 0) goto L_0x0558
                r14.delete()
            L_0x0558:
                int r26 = r26 + 1
                goto L_0x050e
            L_0x055b:
                java.lang.StringBuilder r26 = new java.lang.StringBuilder
                java.lang.String r27 = com.contec.phms.device.cms50k.update.Update50KUtils.fileUrl
                java.lang.String r27 = java.lang.String.valueOf(r27)
                r26.<init>(r27)
                java.lang.String r27 = "update.bin"
                java.lang.StringBuilder r26 = r26.append(r27)
                java.lang.String r26 = r26.toString()
                byte[] r21 = com.contec.phms.device.cms50k.update.Update50KUtils.readSDFile(r26)
                if (r21 == 0) goto L_0x0612
                r26 = 16
                r0 = r26
                byte[] r0 = new byte[r0]
                r22 = r0
                r26 = 0
                r27 = 0
                r28 = 16
                r0 = r21
                r1 = r26
                r2 = r22
                r3 = r27
                r4 = r28
                java.lang.System.arraycopy(r0, r1, r2, r3, r4)
                r26 = 7
                byte r26 = r22[r26]
                int r26 = r26 << 8
                r27 = 6
                byte r27 = r22[r27]
                r0 = r27
                r0 = r0 & 255(0xff, float:3.57E-43)
                r27 = r0
                r26 = r26 | r27
                r27 = 65535(0xffff, float:9.1834E-41)
                r5 = r26 & r27
                java.lang.String r26 = "jxx"
                java.lang.StringBuilder r27 = new java.lang.StringBuilder
                java.lang.String r28 = "版本号："
                r27.<init>(r28)
                r0 = r27
                java.lang.StringBuilder r27 = r0.append(r5)
                java.lang.String r28 = "--"
                java.lang.StringBuilder r27 = r27.append(r28)
                r28 = 6
                byte r28 = r22[r28]
                java.lang.String r28 = java.lang.Integer.toHexString(r28)
                java.lang.StringBuilder r27 = r27.append(r28)
                java.lang.String r28 = " "
                java.lang.StringBuilder r27 = r27.append(r28)
                r28 = 7
                byte r28 = r22[r28]
                java.lang.String r28 = java.lang.Integer.toHexString(r28)
                java.lang.StringBuilder r27 = r27.append(r28)
                java.lang.String r27 = r27.toString()
                android.util.Log.i(r26, r27)
                android.content.SharedPreferences r26 = com.contec.phms.App_phms.preferences
                android.content.SharedPreferences$Editor r13 = r26.edit()
                java.lang.String r26 = "CMS50K_Server_Version"
                r0 = r26
                r13.putInt(r0, r5)
                r13.commit()
                android.content.SharedPreferences r26 = com.contec.phms.App_phms.preferences
                android.content.SharedPreferences$Editor r12 = r26.edit()
                java.lang.String r26 = "currentVersion"
                r0 = r29
                com.contec.phms.device.cms50k.update.DownLoadFile r0 = com.contec.phms.device.cms50k.update.DownLoadFile.this
                r27 = r0
                r0 = r27
                int r0 = r0.mMaxVersionValue
                r27 = r0
                r0 = r26
                r1 = r27
                r12.putInt(r0, r1)
                r12.commit()
                goto L_0x0528
            L_0x0612:
                android.content.SharedPreferences r26 = com.contec.phms.App_phms.preferences
                android.content.SharedPreferences$Editor r12 = r26.edit()
                java.lang.String r26 = "CMS50K_Server_Version"
                r27 = 999(0x3e7, float:1.4E-42)
                r0 = r26
                r1 = r27
                r12.putInt(r0, r1)
                r12.commit()
                android.content.SharedPreferences r26 = com.contec.phms.App_phms.preferences
                android.content.SharedPreferences$Editor r13 = r26.edit()
                java.lang.String r26 = "currentVersion"
                r27 = 999(0x3e7, float:1.4E-42)
                r0 = r26
                r1 = r27
                r13.putInt(r0, r1)
                r13.commit()
                goto L_0x0528
            L_0x063c:
                r10 = move-exception
                r10.printStackTrace()
                goto L_0x00cb
            L_0x0642:
                r10 = move-exception
                r10.printStackTrace()
                goto L_0x00dc
            L_0x0648:
                r0 = r19
                int r0 = r0.length
                r26 = r0
                r25 = 0
            L_0x064f:
                r0 = r25
                r1 = r26
                if (r0 < r1) goto L_0x0683
            L_0x0655:
                android.content.SharedPreferences r25 = com.contec.phms.App_phms.preferences
                android.content.SharedPreferences$Editor r12 = r25.edit()
                java.lang.String r25 = "currentVersion"
                r26 = 999(0x3e7, float:1.4E-42)
                r0 = r25
                r1 = r26
                r12.putInt(r0, r1)
                r12.commit()
            L_0x0669:
                java.lang.String r25 = "jxx"
                java.lang.StringBuilder r26 = new java.lang.StringBuilder
                java.lang.String r27 = "md5值是否一样："
                r26.<init>(r27)
                r0 = r26
                r1 = r17
                java.lang.StringBuilder r26 = r0.append(r1)
                java.lang.String r26 = r26.toString()
                android.util.Log.i(r25, r26)
                goto L_0x012b
            L_0x0683:
                r14 = r19[r25]
                boolean r27 = r14.isFile()
                if (r27 == 0) goto L_0x068e
                r14.delete()
            L_0x068e:
                int r25 = r25 + 1
                goto L_0x064f
            L_0x0691:
                java.lang.StringBuilder r25 = new java.lang.StringBuilder
                java.lang.String r26 = com.contec.phms.device.cms50k.update.Update50KUtils.fileUrl
                java.lang.String r26 = java.lang.String.valueOf(r26)
                r25.<init>(r26)
                java.lang.String r26 = "update.bin"
                java.lang.StringBuilder r25 = r25.append(r26)
                java.lang.String r25 = r25.toString()
                byte[] r21 = com.contec.phms.device.cms50k.update.Update50KUtils.readSDFile(r25)
                if (r21 == 0) goto L_0x0748
                r25 = 16
                r0 = r25
                byte[] r0 = new byte[r0]
                r22 = r0
                r25 = 0
                r26 = 0
                r27 = 16
                r0 = r21
                r1 = r25
                r2 = r22
                r3 = r26
                r4 = r27
                java.lang.System.arraycopy(r0, r1, r2, r3, r4)
                r25 = 7
                byte r25 = r22[r25]
                int r25 = r25 << 8
                r26 = 6
                byte r26 = r22[r26]
                r0 = r26
                r0 = r0 & 255(0xff, float:3.57E-43)
                r26 = r0
                r25 = r25 | r26
                r26 = 65535(0xffff, float:9.1834E-41)
                r5 = r25 & r26
                java.lang.String r25 = "jxx"
                java.lang.StringBuilder r26 = new java.lang.StringBuilder
                java.lang.String r27 = "版本号："
                r26.<init>(r27)
                r0 = r26
                java.lang.StringBuilder r26 = r0.append(r5)
                java.lang.String r27 = "--"
                java.lang.StringBuilder r26 = r26.append(r27)
                r27 = 6
                byte r27 = r22[r27]
                java.lang.String r27 = java.lang.Integer.toHexString(r27)
                java.lang.StringBuilder r26 = r26.append(r27)
                java.lang.String r27 = " "
                java.lang.StringBuilder r26 = r26.append(r27)
                r27 = 7
                byte r27 = r22[r27]
                java.lang.String r27 = java.lang.Integer.toHexString(r27)
                java.lang.StringBuilder r26 = r26.append(r27)
                java.lang.String r26 = r26.toString()
                android.util.Log.i(r25, r26)
                android.content.SharedPreferences r25 = com.contec.phms.App_phms.preferences
                android.content.SharedPreferences$Editor r13 = r25.edit()
                java.lang.String r25 = "CMS50K_Server_Version"
                r0 = r25
                r13.putInt(r0, r5)
                r13.commit()
                android.content.SharedPreferences r25 = com.contec.phms.App_phms.preferences
                android.content.SharedPreferences$Editor r12 = r25.edit()
                java.lang.String r25 = "currentVersion"
                r0 = r29
                com.contec.phms.device.cms50k.update.DownLoadFile r0 = com.contec.phms.device.cms50k.update.DownLoadFile.this
                r26 = r0
                r0 = r26
                int r0 = r0.mMaxVersionValue
                r26 = r0
                r0 = r25
                r1 = r26
                r12.putInt(r0, r1)
                r12.commit()
                goto L_0x0669
            L_0x0748:
                android.content.SharedPreferences r25 = com.contec.phms.App_phms.preferences
                android.content.SharedPreferences$Editor r12 = r25.edit()
                java.lang.String r25 = "CMS50K_Server_Version"
                r26 = 999(0x3e7, float:1.4E-42)
                r0 = r25
                r1 = r26
                r12.putInt(r0, r1)
                r12.commit()
                android.content.SharedPreferences r25 = com.contec.phms.App_phms.preferences
                android.content.SharedPreferences$Editor r13 = r25.edit()
                java.lang.String r25 = "currentVersion"
                r26 = 999(0x3e7, float:1.4E-42)
                r0 = r25
                r1 = r26
                r13.putInt(r0, r1)
                r13.commit()
                goto L_0x0669
            */
            throw new UnsupportedOperationException("Method not decompiled: com.contec.phms.device.cms50k.update.DownLoadFile.downloadFileThread.run():void");
        }
    }

    private String getXmlMd5(List<Xml50KUpdateBean> xmlBeanLists) {
        for (int i = 0; i < xmlBeanLists.size(); i++) {
            Xml50KUpdateBean xmlBean = xmlBeanLists.get(i);
            if (Integer.valueOf(xmlBean.version).intValue() == this.mMaxVersionValue) {
                return xmlBean.md5;
            }
        }
        return bs.b;
    }

    private boolean compareMd5(String fileMD5, String xmlMD5) {
        if (fileMD5 == null || xmlMD5 == null || !xmlMD5.equals(fileMD5)) {
            return false;
        }
        return true;
    }
}
