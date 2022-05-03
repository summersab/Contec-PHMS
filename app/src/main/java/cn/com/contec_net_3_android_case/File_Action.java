package cn.com.contec_net_3_android_case;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
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

    /* JADX WARNING: Removed duplicated region for block: B:17:0x0033 A[SYNTHETIC, Splitter:B:17:0x0033] */
    /* JADX WARNING: Removed duplicated region for block: B:24:0x0040 A[SYNTHETIC, Splitter:B:24:0x0040] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static byte[] readFromSD(java.lang.String r7) {
        /*
            r5 = 0
            java.io.File r2 = new java.io.File
            r2.<init>(r7)
            boolean r6 = r2.exists()
            if (r6 != 0) goto L_0x000e
            r0 = r5
        L_0x000d:
            return r0
        L_0x000e:
            r3 = 0
            java.io.BufferedInputStream r4 = new java.io.BufferedInputStream     // Catch:{ Exception -> 0x002d }
            java.io.FileInputStream r6 = new java.io.FileInputStream     // Catch:{ Exception -> 0x002d }
            r6.<init>(r2)     // Catch:{ Exception -> 0x002d }
            r4.<init>(r6)     // Catch:{ Exception -> 0x002d }
            int r6 = r4.available()     // Catch:{ Exception -> 0x004c, all -> 0x0049 }
            byte[] r0 = new byte[r6]     // Catch:{ Exception -> 0x004c, all -> 0x0049 }
            r4.read(r0)     // Catch:{ Exception -> 0x004c, all -> 0x0049 }
            if (r4 == 0) goto L_0x000d
            r4.close()     // Catch:{ Exception -> 0x0028 }
            goto L_0x000d
        L_0x0028:
            r1 = move-exception
            r1.printStackTrace()
            goto L_0x000d
        L_0x002d:
            r1 = move-exception
        L_0x002e:
            r1.printStackTrace()     // Catch:{ all -> 0x003d }
            if (r3 == 0) goto L_0x0036
            r3.close()     // Catch:{ Exception -> 0x0038 }
        L_0x0036:
            r0 = r5
            goto L_0x000d
        L_0x0038:
            r1 = move-exception
            r1.printStackTrace()
            goto L_0x0036
        L_0x003d:
            r5 = move-exception
        L_0x003e:
            if (r3 == 0) goto L_0x0043
            r3.close()     // Catch:{ Exception -> 0x0044 }
        L_0x0043:
            throw r5
        L_0x0044:
            r1 = move-exception
            r1.printStackTrace()
            goto L_0x0043
        L_0x0049:
            r5 = move-exception
            r3 = r4
            goto L_0x003e
        L_0x004c:
            r1 = move-exception
            r3 = r4
            goto L_0x002e
        */
        throw new UnsupportedOperationException("Method not decompiled: cn.com.contec_net_3_android_case.File_Action.readFromSD(java.lang.String):byte[]");
    }
}
