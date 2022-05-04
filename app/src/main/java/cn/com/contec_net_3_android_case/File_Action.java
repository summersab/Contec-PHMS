package cn.com.contec_net_3_android_case;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.httpclient.cookie.CookieSpec;
import u.aly.bs;

public class File_Action {
    public static boolean MakeDirectory(String path) {
        File file = new File(path);
        if (file.exists() && file.isDirectory()) {
            return true;
        }
        if (file.exists() && file.isFile()) {
            return false;
        }
        boolean ret = true;
        String strDir = new String(path);
        int nPos = strDir.lastIndexOf(CookieSpec.PATH_DELIM);
        String strDir2 = strDir.substring(0, nPos);
        String strParent = new String(bs.b);
        if (nPos != -1) {
            strParent = strDir2.substring(0, nPos);
        }
        if (strParent.length() > "/sdcard".length()) {
            ret = new File(strParent).exists();
        }
        if (!ret) {
            ret = MakeDirectory(strParent);
        }
        if (ret) {
            ret = file.mkdir();
        }
        if (!file.exists()) {
            return false;
        }
        return ret;
    }

    public static void memoryData(String path, byte[] _pData, boolean isContinue) throws IOException {
        File file = new File(path);
        if (!file.exists()) {
            file.createNewFile();
        }
        FileOutputStream out = new FileOutputStream(file, isContinue);
        out.write(_pData);
        out.close();
    }

    public static byte[] readFromSD(String path) {
        Throwable th;
        Exception e;
        InputStream inputStream = null;
        File file = new File(path);
        if (!file.exists()) {
            return null;
        }
        InputStream inputStream2 = null;
        try {
            try {
                inputStream = new BufferedInputStream(new FileInputStream(file));
            } catch (Exception e2) {
                e = e2;
            }
        } catch (Throwable th2) {
            th = th2;
        }
        try {
            byte[] data = new byte[inputStream.available()];
            inputStream.read(data);
            if (inputStream == null) {
                return data;
            }
            try {
                inputStream.close();
                return data;
            } catch (Exception e3) {
                e3.printStackTrace();
                return data;
            }
        } catch (Exception e4) {
            e = e4;
            inputStream2 = inputStream;
            e.printStackTrace();
            if (inputStream2 != null) {
                try {
                    inputStream2.close();
                } catch (Exception e5) {
                    e5.printStackTrace();
                }
            }
            return null;
        } catch (Throwable th3) {
            th = th3;
            inputStream2 = inputStream;
            if (inputStream2 != null) {
                try {
                    inputStream2.close();
                } catch (Exception e6) {
                    e6.printStackTrace();
                }
            }
            try {
                throw th;
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }
        return new byte[0];
    }
}
