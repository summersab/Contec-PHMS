package com.contec.phms.util;

import android.os.Environment;
import android.os.Handler;
//import com.alibaba.cchannel.CloudChannelConstants;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import org.apache.commons.httpclient.cookie.CookieSpec;

public class DownloadApkThread {
    private final int DOWNLOAD = 222;
    private final int DOWNLOAD_FINISH = 333;
    public boolean cancelUpdate = false;
    private String mSavePath;
    private Handler mhandler;
    private int progress;

    public DownloadApkThread(Handler handler) {
        this.mhandler = handler;
    }

    public void downloadApk() {
        DownloadThread downloadthread = new DownloadThread(this.mhandler);
        downloadthread.setName("Download apk thread");
        downloadthread.start();
    }

    public void cancelDownloadApk() {
        this.cancelUpdate = true;
    }

    private class DownloadThread extends Thread {
        private InputStream is;
        private int length;
        private Handler mhandler;

        public DownloadThread(Handler mhandler2) {
            this.mhandler = mhandler2;
        }

        public void run() {
            try {
                if (Environment.getExternalStorageState().equals("mounted")) {
                    DownloadApkThread.this.mSavePath = String.valueOf(Environment.getExternalStorageDirectory() + CookieSpec.PATH_DELIM) + "phms_download";
                    HttpURLConnection conn = (HttpURLConnection) new URL("http://admin.contec365.com/file/reader.apk").openConnection();
                    //conn.setConnectTimeout(CloudChannelConstants.WEBVIEW_PROXY_HTTP_TIMEOUT);
                    conn.setRequestMethod("GET");
                    conn.connect();
                    if (!DownloadApkThread.this.cancelUpdate) {
                        this.length = conn.getContentLength();
                        this.is = conn.getInputStream();
                    }
                    File file = new File(DownloadApkThread.this.mSavePath);
                    if (!file.exists()) {
                        file.mkdir();
                    }
                    FileOutputStream fos = new FileOutputStream(new File(DownloadApkThread.this.mSavePath, "reader.apk"));
                    int count = 0;
                    byte[] buf = new byte[1024];
                    while (true) {
                        int numread = this.is.read(buf);
                        count += numread;
                        DownloadApkThread.this.progress = (int) ((((float) count) / ((float) this.length)) * 100.0f);
                        this.mhandler.obtainMessage(222, DownloadApkThread.this.progress, 0).sendToTarget();
                        if (numread > 0) {
                            fos.write(buf, 0, numread);
                            if (DownloadApkThread.this.cancelUpdate) {
                                break;
                            }
                        } else {
                            this.mhandler.sendEmptyMessage(333);
                            System.out.println("下载完毕");
                            break;
                        }
                    }
                    fos.close();
                    this.is.close();
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e2) {
                e2.printStackTrace();
            }
        }
    }
}
