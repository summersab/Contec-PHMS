package com.contec.phms.device.cms50k.update;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import com.alibaba.cchannel.CloudChannelConstants;
import com.contec.phms.App_phms;
import com.contec.phms.util.CLog;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
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

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            try {
                try {
                    if (Environment.getExternalStorageState().equals("mounted")) {
                        String mSavePath = Update50KUtils.fileUrl;
                        URL url = new URL(this.urlx);
                        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                        //conn.setConnectTimeout(CloudChannelConstants.WEBVIEW_PROXY_HTTP_TIMEOUT);
                        conn.setRequestMethod("GET");
                        conn.connect();
                        this.length = conn.getContentLength();
                        SharedPreferences.Editor editor = App_phms.preferences.edit();
                        editor.putLong("long", this.length);
                        editor.commit();
                        this.is = conn.getInputStream();
                        File file = new File(mSavePath);
                        if (!file.exists()) {
                            file.mkdir();
                        }
                        File apkFile = new File(mSavePath, Update50KUtils.updateFilename);
                        if (apkFile.exists()) {
                            apkFile.delete();
                            apkFile.createNewFile();
                        }
                        this.fos = new FileOutputStream(apkFile);
                        byte[] buf = new byte[1024];
                        while (true) {
                            int len = this.is.read(buf);
                            if (len == -1) {
                                break;
                            }
                            this.fos.write(buf, 0, len);
                        }
                        Log.i("jx", "写入成功");
                    }
                    try {
                        if (this.fos != null) {
                            this.fos.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        if (this.is != null) {
                            this.is.close();
                        }
                    } catch (IOException e2) {
                        e2.printStackTrace();
                    }
                    String fileMD5 = Update50KUtils.getFileMD5(new File(Update50KUtils.fileUrl, Update50KUtils.updateFilename));
                    List<Xml50KUpdateBean> beanLists = Update50KUtils.pullParseXmlFile();
                    String xmlMD5 = DownLoadFile.this.getXmlMd5(beanLists);
                    boolean ifSame = DownLoadFile.this.compareMd5(fileMD5, xmlMD5);
                    if (!ifSame) {
                        File file2 = new File(Update50KUtils.fileUrl);
                        if (file2.exists() && file2.isDirectory()) {
                            File[] list = file2.listFiles();
                            if (list.length != 0) {
                                for (File f : list) {
                                    if (f.isFile()) {
                                        f.delete();
                                    }
                                }
                            } else {
                                return;
                            }
                        }
                        SharedPreferences.Editor editor1 = App_phms.preferences.edit();
                        editor1.putInt("currentVersion", 999);
                        editor1.commit();
                    } else {
                        byte[] mUpdateDatas = Update50KUtils.readSDFile(String.valueOf(Update50KUtils.fileUrl) + Update50KUtils.updateFilename);
                        if (mUpdateDatas != null) {
                            byte[] pack1 = new byte[16];
                            System.arraycopy(mUpdateDatas, 0, pack1, 0, 16);
                            int NewVersion = ((pack1[7] << 8) | (pack1[6] & 255)) & 65535;
                            Log.i("jxx", "版本号：" + NewVersion + "--" + Integer.toHexString(pack1[6]) + " " + Integer.toHexString(pack1[7]));
                            SharedPreferences.Editor editor2 = App_phms.preferences.edit();
                            editor2.putInt("CMS50K_Server_Version", NewVersion);
                            editor2.commit();
                            SharedPreferences.Editor editor12 = App_phms.preferences.edit();
                            editor12.putInt("currentVersion", DownLoadFile.this.mMaxVersionValue);
                            editor12.commit();
                        } else {
                            SharedPreferences.Editor editor13 = App_phms.preferences.edit();
                            editor13.putInt("CMS50K_Server_Version", 999);
                            editor13.commit();
                            SharedPreferences.Editor editor22 = App_phms.preferences.edit();
                            editor22.putInt("currentVersion", 999);
                            editor22.commit();
                        }
                    }
                    Log.i("jxx", "md5值是否一样：" + ifSame);
                } catch (Throwable th) {
                    try {
                        if (this.fos != null) {
                            this.fos.close();
                        }
                    } catch (IOException e3) {
                        e3.printStackTrace();
                    }
                    try {
                        if (this.is != null) {
                            this.is.close();
                        }
                    } catch (IOException e4) {
                        e4.printStackTrace();
                    }
                    String fileMD52 = Update50KUtils.getFileMD5(new File(Update50KUtils.fileUrl, Update50KUtils.updateFilename));
                    List<Xml50KUpdateBean> beanLists2 = Update50KUtils.pullParseXmlFile();
                    String xmlMD52 = DownLoadFile.this.getXmlMd5(beanLists2);
                    boolean ifSame2 = DownLoadFile.this.compareMd5(fileMD52, xmlMD52);
                    if (!ifSame2) {
                        File file3 = new File(Update50KUtils.fileUrl);
                        if (file3.exists() && file3.isDirectory()) {
                            File[] list2 = file3.listFiles();
                            if (list2.length != 0) {
                                for (File f2 : list2) {
                                    if (f2.isFile()) {
                                        f2.delete();
                                    }
                                }
                            } else {
                                return;
                            }
                        }
                        SharedPreferences.Editor editor14 = App_phms.preferences.edit();
                        editor14.putInt("currentVersion", 999);
                        editor14.commit();
                    } else {
                        byte[] mUpdateDatas2 = Update50KUtils.readSDFile(String.valueOf(Update50KUtils.fileUrl) + Update50KUtils.updateFilename);
                        if (mUpdateDatas2 != null) {
                            byte[] pack12 = new byte[16];
                            System.arraycopy(mUpdateDatas2, 0, pack12, 0, 16);
                            int NewVersion2 = ((pack12[7] << 8) | (pack12[6] & 255)) & 65535;
                            Log.i("jxx", "版本号：" + NewVersion2 + "--" + Integer.toHexString(pack12[6]) + " " + Integer.toHexString(pack12[7]));
                            SharedPreferences.Editor editor23 = App_phms.preferences.edit();
                            editor23.putInt("CMS50K_Server_Version", NewVersion2);
                            editor23.commit();
                            SharedPreferences.Editor editor15 = App_phms.preferences.edit();
                            editor15.putInt("currentVersion", DownLoadFile.this.mMaxVersionValue);
                            editor15.commit();
                        } else {
                            SharedPreferences.Editor editor16 = App_phms.preferences.edit();
                            editor16.putInt("CMS50K_Server_Version", 999);
                            editor16.commit();
                            SharedPreferences.Editor editor24 = App_phms.preferences.edit();
                            editor24.putInt("currentVersion", 999);
                            editor24.commit();
                        }
                    }
                    Log.i("jxx", "md5值是否一样：" + ifSame2);
                    throw th;
                }
            } catch (MalformedURLException e5) {
                e5.printStackTrace();
                try {
                    if (this.fos != null) {
                        this.fos.close();
                    }
                } catch (IOException e6) {
                    e6.printStackTrace();
                }
                try {
                    if (this.is != null) {
                        this.is.close();
                    }
                } catch (IOException e7) {
                    e7.printStackTrace();
                }
                String fileMD53 = Update50KUtils.getFileMD5(new File(Update50KUtils.fileUrl, Update50KUtils.updateFilename));
                List<Xml50KUpdateBean> beanLists3 = Update50KUtils.pullParseXmlFile();
                String xmlMD53 = DownLoadFile.this.getXmlMd5(beanLists3);
                boolean ifSame3 = DownLoadFile.this.compareMd5(fileMD53, xmlMD53);
                if (!ifSame3) {
                    File file4 = new File(Update50KUtils.fileUrl);
                    if (file4.exists() && file4.isDirectory()) {
                        File[] list3 = file4.listFiles();
                        if (list3.length != 0) {
                            for (File f3 : list3) {
                                if (f3.isFile()) {
                                    f3.delete();
                                }
                            }
                        } else {
                            return;
                        }
                    }
                    SharedPreferences.Editor editor17 = App_phms.preferences.edit();
                    editor17.putInt("currentVersion", 999);
                    editor17.commit();
                } else {
                    byte[] mUpdateDatas3 = Update50KUtils.readSDFile(String.valueOf(Update50KUtils.fileUrl) + Update50KUtils.updateFilename);
                    if (mUpdateDatas3 != null) {
                        byte[] pack13 = new byte[16];
                        System.arraycopy(mUpdateDatas3, 0, pack13, 0, 16);
                        int NewVersion3 = ((pack13[7] << 8) | (pack13[6] & 255)) & 65535;
                        Log.i("jxx", "版本号：" + NewVersion3 + "--" + Integer.toHexString(pack13[6]) + " " + Integer.toHexString(pack13[7]));
                        SharedPreferences.Editor editor25 = App_phms.preferences.edit();
                        editor25.putInt("CMS50K_Server_Version", NewVersion3);
                        editor25.commit();
                        SharedPreferences.Editor editor18 = App_phms.preferences.edit();
                        editor18.putInt("currentVersion", DownLoadFile.this.mMaxVersionValue);
                        editor18.commit();
                    } else {
                        SharedPreferences.Editor editor19 = App_phms.preferences.edit();
                        editor19.putInt("CMS50K_Server_Version", 999);
                        editor19.commit();
                        SharedPreferences.Editor editor26 = App_phms.preferences.edit();
                        editor26.putInt("currentVersion", 999);
                        editor26.commit();
                    }
                }
                Log.i("jxx", "md5值是否一样：" + ifSame3);
            } catch (IOException e8) {
                e8.printStackTrace();
                try {
                    if (this.fos != null) {
                        this.fos.close();
                    }
                } catch (IOException e9) {
                    e9.printStackTrace();
                }
                try {
                    if (this.is != null) {
                        this.is.close();
                    }
                } catch (IOException e10) {
                    e10.printStackTrace();
                }
                String fileMD54 = Update50KUtils.getFileMD5(new File(Update50KUtils.fileUrl, Update50KUtils.updateFilename));
                List<Xml50KUpdateBean> beanLists4 = Update50KUtils.pullParseXmlFile();
                String xmlMD54 = DownLoadFile.this.getXmlMd5(beanLists4);
                boolean ifSame4 = DownLoadFile.this.compareMd5(fileMD54, xmlMD54);
                if (!ifSame4) {
                    File file5 = new File(Update50KUtils.fileUrl);
                    if (file5.exists() && file5.isDirectory()) {
                        File[] list4 = file5.listFiles();
                        if (list4.length != 0) {
                            for (File f4 : list4) {
                                if (f4.isFile()) {
                                    f4.delete();
                                }
                            }
                        } else {
                            return;
                        }
                    }
                    SharedPreferences.Editor editor110 = App_phms.preferences.edit();
                    editor110.putInt("currentVersion", 999);
                    editor110.commit();
                } else {
                    byte[] mUpdateDatas4 = Update50KUtils.readSDFile(String.valueOf(Update50KUtils.fileUrl) + Update50KUtils.updateFilename);
                    if (mUpdateDatas4 != null) {
                        byte[] pack14 = new byte[16];
                        System.arraycopy(mUpdateDatas4, 0, pack14, 0, 16);
                        int NewVersion4 = ((pack14[7] << 8) | (pack14[6] & 255)) & 65535;
                        Log.i("jxx", "版本号：" + NewVersion4 + "--" + Integer.toHexString(pack14[6]) + " " + Integer.toHexString(pack14[7]));
                        SharedPreferences.Editor editor27 = App_phms.preferences.edit();
                        editor27.putInt("CMS50K_Server_Version", NewVersion4);
                        editor27.commit();
                        SharedPreferences.Editor editor111 = App_phms.preferences.edit();
                        editor111.putInt("currentVersion", DownLoadFile.this.mMaxVersionValue);
                        editor111.commit();
                    } else {
                        SharedPreferences.Editor editor112 = App_phms.preferences.edit();
                        editor112.putInt("CMS50K_Server_Version", 999);
                        editor112.commit();
                        SharedPreferences.Editor editor28 = App_phms.preferences.edit();
                        editor28.putInt("currentVersion", 999);
                        editor28.commit();
                    }
                }
                Log.i("jxx", "md5值是否一样：" + ifSame4);
            }
        }
    }

    private String getXmlMd5(List<Xml50KUpdateBean> xmlBeanLists) {
        return "md5";
/*
        for (int i = 0; i < xmlBeanLists.size(); i++) {
            Xml50KUpdateBean xmlBean = xmlBeanLists.get(i);
            if (Integer.valueOf(xmlBean.version).intValue() == this.mMaxVersionValue) {
                return xmlBean.md5;
            }
        }
        return bs.b;
 */
    }

    private boolean compareMd5(String fileMD5, String xmlMD5) {
        if (fileMD5 == null || xmlMD5 == null || !xmlMD5.equals(fileMD5)) {
            return false;
        }
        return true;
    }
}
