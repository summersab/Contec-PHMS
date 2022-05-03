package com.contec.phms.manager.datas;

import com.contec.phms.util.CLog;
import com.contec.phms.util.Constants;
import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;

public class DataObject {
    private static String TAG = "DataObject";

    /* JADX WARNING: Removed duplicated region for block: B:19:0x007c  */
    /* JADX WARNING: Removed duplicated region for block: B:21:0x007f  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.lang.String makeObjectFile_failed(java.lang.Object r14, java.lang.String r15) {
        /*
            r6 = r15
            r1 = r14
            com.contec.phms.device.template.DeviceData r1 = (com.contec.phms.device.template.DeviceData) r1
            java.lang.StringBuilder r10 = new java.lang.StringBuilder
            java.lang.String r11 = java.lang.String.valueOf(r6)
            r10.<init>(r11)
            java.lang.String r11 = "/"
            java.lang.StringBuilder r10 = r10.append(r11)
            java.lang.String r11 = r1.getFileName()
            java.lang.StringBuilder r10 = r10.append(r11)
            java.lang.String r6 = r10.toString()
            java.lang.String r7 = r1.getFileName()
            java.io.File r2 = new java.io.File
            r2.<init>(r6)
            boolean r10 = r2.exists()
            if (r10 != 0) goto L_0x0047
            java.lang.String r10 = "/"
            int r9 = r6.lastIndexOf(r10)
            r10 = 0
            int r11 = r9 + 1
            java.lang.String r0 = r6.substring(r10, r11)
            com.contec.phms.util.FileOperation.makeDirs(r0)
            java.io.File r3 = new java.io.File     // Catch:{ IOException -> 0x005c }
            r3.<init>(r6)     // Catch:{ IOException -> 0x005c }
            r3.createNewFile()     // Catch:{ IOException -> 0x00c9 }
            r2 = r3
        L_0x0047:
            r1 = 0
            r4 = 0
            java.io.ObjectOutputStream r5 = new java.io.ObjectOutputStream     // Catch:{ Exception -> 0x0079 }
            java.io.FileOutputStream r10 = new java.io.FileOutputStream     // Catch:{ Exception -> 0x0079 }
            r10.<init>(r6)     // Catch:{ Exception -> 0x0079 }
            r5.<init>(r10)     // Catch:{ Exception -> 0x0079 }
            r5.writeObject(r14)     // Catch:{ Exception -> 0x00c6 }
            r5.close()     // Catch:{ Exception -> 0x00c6 }
            r4 = 0
            r14 = 0
        L_0x005b:
            return r7
        L_0x005c:
            r8 = move-exception
        L_0x005d:
            r8.printStackTrace()
            java.lang.String r10 = TAG
            java.lang.StringBuilder r11 = new java.lang.StringBuilder
            java.lang.String r12 = "PageUtil.getSDFreeSize_BYTE():"
            r11.<init>(r12)
            long r12 = com.contec.phms.util.PageUtil.getSDFreeSize_BYTE()
            java.lang.StringBuilder r11 = r11.append(r12)
            java.lang.String r11 = r11.toString()
            com.contec.phms.util.CLog.e(r10, r11)
            goto L_0x0047
        L_0x0079:
            r8 = move-exception
        L_0x007a:
            if (r4 == 0) goto L_0x007d
            r4 = 0
        L_0x007d:
            if (r14 == 0) goto L_0x0080
            r14 = 0
        L_0x0080:
            java.lang.String r10 = TAG
            java.lang.StringBuilder r11 = new java.lang.StringBuilder
            java.lang.String r12 = "---PageUtil.getSDFreeSize_BYTE()---:"
            r11.<init>(r12)
            long r12 = com.contec.phms.util.PageUtil.getSDFreeSize_BYTE()
            java.lang.StringBuilder r11 = r11.append(r12)
            java.lang.String r11 = r11.toString()
            com.contec.phms.util.CLog.e(r10, r11)
            r8.printStackTrace()
            com.contec.phms.App_phms r10 = com.contec.phms.App_phms.getInstance()
            android.app.Activity r10 = r10.getlastActivity()
            r11 = 2131099839(0x7f0600bf, float:1.7812043E38)
            r12 = 17039370(0x104000a, float:2.42446E-38)
            r13 = 0
            com.contec.phms.util.AlertDialogUtil.alertDialog(r10, r11, r12, r13)
            java.lang.String r10 = TAG
            java.lang.StringBuilder r11 = new java.lang.StringBuilder
            java.lang.String r12 = "---PageUtil.getSDFreeSize_BYTE():"
            r11.<init>(r12)
            long r12 = com.contec.phms.util.PageUtil.getSDFreeSize_BYTE()
            java.lang.StringBuilder r11 = r11.append(r12)
            java.lang.String r11 = r11.toString()
            com.contec.phms.util.CLog.e(r10, r11)
            goto L_0x005b
        L_0x00c6:
            r8 = move-exception
            r4 = r5
            goto L_0x007a
        L_0x00c9:
            r8 = move-exception
            r2 = r3
            goto L_0x005d
        */
        throw new UnsupportedOperationException("Method not decompiled: com.contec.phms.manager.datas.DataObject.makeObjectFile_failed(java.lang.Object, java.lang.String):java.lang.String");
    }

    /* JADX WARNING: Removed duplicated region for block: B:19:0x007c  */
    /* JADX WARNING: Removed duplicated region for block: B:21:0x007f  */
    /* JADX WARNING: Removed duplicated region for block: B:24:0x0089  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.lang.String makeObjectFile_add(java.lang.Object r14, java.lang.String r15) {
        /*
            r6 = r15
            r1 = r14
            com.contec.phms.device.template.DeviceData r1 = (com.contec.phms.device.template.DeviceData) r1
            java.lang.StringBuilder r10 = new java.lang.StringBuilder
            java.lang.String r11 = java.lang.String.valueOf(r6)
            r10.<init>(r11)
            java.lang.String r11 = "/"
            java.lang.StringBuilder r10 = r10.append(r11)
            java.lang.String r11 = r1.getFileName()
            java.lang.StringBuilder r10 = r10.append(r11)
            java.lang.String r6 = r10.toString()
            java.lang.String r7 = r1.getFileName()
            java.io.File r2 = new java.io.File
            r2.<init>(r6)
            boolean r10 = r2.exists()
            if (r10 != 0) goto L_0x0047
            java.lang.String r10 = "/"
            int r9 = r6.lastIndexOf(r10)
            r10 = 0
            int r11 = r9 + 1
            java.lang.String r0 = r6.substring(r10, r11)
            com.contec.phms.util.FileOperation.makeDirs(r0)
            java.io.File r3 = new java.io.File     // Catch:{ IOException -> 0x005c }
            r3.<init>(r6)     // Catch:{ IOException -> 0x005c }
            r3.createNewFile()     // Catch:{ IOException -> 0x00bd }
            r2 = r3
        L_0x0047:
            r1 = 0
            r4 = 0
            java.io.ObjectOutputStream r5 = new java.io.ObjectOutputStream     // Catch:{ Exception -> 0x0079 }
            java.io.FileOutputStream r10 = new java.io.FileOutputStream     // Catch:{ Exception -> 0x0079 }
            r10.<init>(r6)     // Catch:{ Exception -> 0x0079 }
            r5.<init>(r10)     // Catch:{ Exception -> 0x0079 }
            r5.writeObject(r14)     // Catch:{ Exception -> 0x00ba }
            r5.close()     // Catch:{ Exception -> 0x00ba }
            r4 = 0
            r14 = 0
        L_0x005b:
            return r7
        L_0x005c:
            r8 = move-exception
        L_0x005d:
            r8.printStackTrace()
            java.lang.String r10 = TAG
            java.lang.StringBuilder r11 = new java.lang.StringBuilder
            java.lang.String r12 = "PageUtil.getSDFreeSize_BYTE():"
            r11.<init>(r12)
            long r12 = com.contec.phms.util.PageUtil.getSDFreeSize_BYTE()
            java.lang.StringBuilder r11 = r11.append(r12)
            java.lang.String r11 = r11.toString()
            com.contec.phms.util.CLog.e(r10, r11)
            goto L_0x0047
        L_0x0079:
            r8 = move-exception
        L_0x007a:
            if (r4 == 0) goto L_0x007d
            r4 = 0
        L_0x007d:
            if (r14 == 0) goto L_0x0080
            r14 = 0
        L_0x0080:
            r8.printStackTrace()
            android.os.Looper r10 = android.os.Looper.myLooper()
            if (r10 != 0) goto L_0x008c
            android.os.Looper.prepare()
        L_0x008c:
            com.contec.phms.App_phms r10 = com.contec.phms.App_phms.getInstance()
            android.app.Activity r10 = r10.getlastActivity()
            r11 = 2131099839(0x7f0600bf, float:1.7812043E38)
            r12 = 17039370(0x104000a, float:2.42446E-38)
            r13 = 0
            com.contec.phms.util.AlertDialogUtil.alertDialog(r10, r11, r12, r13)
            android.os.Looper.loop()
            java.lang.String r10 = TAG
            java.lang.StringBuilder r11 = new java.lang.StringBuilder
            java.lang.String r12 = "---PageUtil.getSDFreeSize_BYTE():"
            r11.<init>(r12)
            long r12 = com.contec.phms.util.PageUtil.getSDFreeSize_BYTE()
            java.lang.StringBuilder r11 = r11.append(r12)
            java.lang.String r11 = r11.toString()
            com.contec.phms.util.CLog.e(r10, r11)
            goto L_0x005b
        L_0x00ba:
            r8 = move-exception
            r4 = r5
            goto L_0x007a
        L_0x00bd:
            r8 = move-exception
            r2 = r3
            goto L_0x005d
        */
        throw new UnsupportedOperationException("Method not decompiled: com.contec.phms.manager.datas.DataObject.makeObjectFile_add(java.lang.Object, java.lang.String):java.lang.String");
    }

    public static Object readObject(File file) {
        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
            Object _obj = ois.readObject();
            ois.close();
            return _obj;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Object[] getObjects(DataList list) {
        File[] _files = new File(Constants.DataPath).listFiles();
        for (File name : _files) {
            list.mList.add(name.getName());
        }
        return null;
    }

    public static synchronized boolean remove(String path) {
        boolean _result;
        synchronized (DataObject.class) {
            _result = false;
            File _file = new File(path);
            CLog.i(TAG, "Deleted file path**********: " + path);
            if (_file.isFile() && _file.delete()) {
                CLog.i(TAG, "Deleted file path: " + path);
                _result = true;
            }
        }
        return _result;
    }

    /* JADX WARNING: Removed duplicated region for block: B:15:0x0066 A[Catch:{ Exception -> 0x00d5 }] */
    /* JADX WARNING: Removed duplicated region for block: B:41:0x00d8 A[Catch:{ Exception -> 0x00d5 }] */
    /* JADX WARNING: Removed duplicated region for block: B:43:0x00db A[Catch:{ Exception -> 0x00d5 }] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static synchronized java.lang.String makeObjectFile_addCase(java.lang.Object r18, java.lang.String r19) {
        /*
            java.lang.Class<com.contec.phms.manager.datas.DataObject> r14 = com.contec.phms.manager.datas.DataObject.class
            monitor-enter(r14)
            r0 = r18
            com.contec.phms.device.template.DeviceData r0 = (com.contec.phms.device.template.DeviceData) r0     // Catch:{ all -> 0x00b5 }
            r2 = r0
            java.lang.StringBuilder r13 = new java.lang.StringBuilder     // Catch:{ all -> 0x00b5 }
            java.lang.String r15 = java.lang.String.valueOf(r19)     // Catch:{ all -> 0x00b5 }
            r13.<init>(r15)     // Catch:{ all -> 0x00b5 }
            java.lang.String r15 = "/"
            java.lang.StringBuilder r13 = r13.append(r15)     // Catch:{ all -> 0x00b5 }
            java.lang.String r15 = r2.mDeviceType     // Catch:{ all -> 0x00b5 }
            java.lang.StringBuilder r13 = r13.append(r15)     // Catch:{ all -> 0x00b5 }
            java.lang.String r9 = r13.toString()     // Catch:{ all -> 0x00b5 }
            java.lang.StringBuilder r13 = new java.lang.StringBuilder     // Catch:{ all -> 0x00b5 }
            java.lang.String r15 = java.lang.String.valueOf(r9)     // Catch:{ all -> 0x00b5 }
            r13.<init>(r15)     // Catch:{ all -> 0x00b5 }
            java.lang.String r15 = "/"
            java.lang.StringBuilder r13 = r13.append(r15)     // Catch:{ all -> 0x00b5 }
            java.lang.String r15 = r2.getFileName()     // Catch:{ all -> 0x00b5 }
            java.lang.StringBuilder r13 = r13.append(r15)     // Catch:{ all -> 0x00b5 }
            java.lang.String r9 = r13.toString()     // Catch:{ all -> 0x00b5 }
            java.lang.String r10 = r2.getFileName()     // Catch:{ all -> 0x00b5 }
            java.io.File r5 = new java.io.File     // Catch:{ all -> 0x00b5 }
            r0 = r19
            r5.<init>(r0)     // Catch:{ all -> 0x00b5 }
            boolean r13 = r5.exists()     // Catch:{ all -> 0x00b5 }
            if (r13 != 0) goto L_0x005b
            com.contec.phms.util.FileOperation.caseMakeDirs(r19)     // Catch:{ all -> 0x00b5 }
            java.io.File r6 = new java.io.File     // Catch:{ IOException -> 0x0098 }
            r0 = r19
            r6.<init>(r0)     // Catch:{ IOException -> 0x0098 }
            r6.createNewFile()     // Catch:{ IOException -> 0x00ff }
            r5 = r6
        L_0x005b:
            java.io.File r3 = new java.io.File     // Catch:{ all -> 0x00b5 }
            r3.<init>(r9)     // Catch:{ all -> 0x00b5 }
            boolean r13 = r3.exists()     // Catch:{ all -> 0x00b5 }
            if (r13 != 0) goto L_0x007f
            java.lang.String r13 = "/"
            int r12 = r9.lastIndexOf(r13)     // Catch:{ all -> 0x00b5 }
            r13 = 0
            int r15 = r12 + 1
            java.lang.String r1 = r9.substring(r13, r15)     // Catch:{ all -> 0x00b5 }
            com.contec.phms.util.FileOperation.caseMakeDirs(r1)     // Catch:{ all -> 0x00b5 }
            java.io.File r4 = new java.io.File     // Catch:{ IOException -> 0x00b8 }
            r4.<init>(r9)     // Catch:{ IOException -> 0x00b8 }
            r4.createNewFile()     // Catch:{ IOException -> 0x00fc }
            r3 = r4
        L_0x007f:
            r2 = 0
            r7 = 0
            java.io.ObjectOutputStream r8 = new java.io.ObjectOutputStream     // Catch:{ Exception -> 0x00d5 }
            java.io.FileOutputStream r13 = new java.io.FileOutputStream     // Catch:{ Exception -> 0x00d5 }
            r13.<init>(r9)     // Catch:{ Exception -> 0x00d5 }
            r8.<init>(r13)     // Catch:{ Exception -> 0x00d5 }
            r0 = r18
            r8.writeObject(r0)     // Catch:{ Exception -> 0x00f9 }
            r8.close()     // Catch:{ Exception -> 0x00f9 }
            r7 = 0
            r18 = 0
        L_0x0096:
            monitor-exit(r14)
            return r10
        L_0x0098:
            r11 = move-exception
        L_0x0099:
            r11.printStackTrace()     // Catch:{ all -> 0x00b5 }
            java.lang.String r13 = TAG     // Catch:{ all -> 0x00b5 }
            java.lang.StringBuilder r15 = new java.lang.StringBuilder     // Catch:{ all -> 0x00b5 }
            java.lang.String r16 = "jPageUtil.getSDFreeSize_BYTE():"
            r15.<init>(r16)     // Catch:{ all -> 0x00b5 }
            long r16 = com.contec.phms.util.JPageUtil.getSDFreeSize_BYTE()     // Catch:{ all -> 0x00b5 }
            java.lang.StringBuilder r15 = r15.append(r16)     // Catch:{ all -> 0x00b5 }
            java.lang.String r15 = r15.toString()     // Catch:{ all -> 0x00b5 }
            com.contec.phms.util.CLog.e(r13, r15)     // Catch:{ all -> 0x00b5 }
            goto L_0x005b
        L_0x00b5:
            r13 = move-exception
            monitor-exit(r14)
            throw r13
        L_0x00b8:
            r11 = move-exception
        L_0x00b9:
            r11.printStackTrace()     // Catch:{ all -> 0x00b5 }
            java.lang.String r13 = TAG     // Catch:{ all -> 0x00b5 }
            java.lang.StringBuilder r15 = new java.lang.StringBuilder     // Catch:{ all -> 0x00b5 }
            java.lang.String r16 = "jPageUtil.getSDFreeSize_BYTE():"
            r15.<init>(r16)     // Catch:{ all -> 0x00b5 }
            long r16 = com.contec.phms.util.JPageUtil.getSDFreeSize_BYTE()     // Catch:{ all -> 0x00b5 }
            java.lang.StringBuilder r15 = r15.append(r16)     // Catch:{ all -> 0x00b5 }
            java.lang.String r15 = r15.toString()     // Catch:{ all -> 0x00b5 }
            com.contec.phms.util.CLog.e(r13, r15)     // Catch:{ all -> 0x00b5 }
            goto L_0x007f
        L_0x00d5:
            r11 = move-exception
        L_0x00d6:
            if (r7 == 0) goto L_0x00d9
            r7 = 0
        L_0x00d9:
            if (r18 == 0) goto L_0x00dd
            r18 = 0
        L_0x00dd:
            r11.printStackTrace()     // Catch:{ all -> 0x00b5 }
            java.lang.String r13 = TAG     // Catch:{ all -> 0x00b5 }
            java.lang.StringBuilder r15 = new java.lang.StringBuilder     // Catch:{ all -> 0x00b5 }
            java.lang.String r16 = "---jPageUtil.getSDFreeSize_BYTE():"
            r15.<init>(r16)     // Catch:{ all -> 0x00b5 }
            long r16 = com.contec.phms.util.JPageUtil.getSDFreeSize_BYTE()     // Catch:{ all -> 0x00b5 }
            java.lang.StringBuilder r15 = r15.append(r16)     // Catch:{ all -> 0x00b5 }
            java.lang.String r15 = r15.toString()     // Catch:{ all -> 0x00b5 }
            com.contec.phms.util.CLog.e(r13, r15)     // Catch:{ all -> 0x00b5 }
            goto L_0x0096
        L_0x00f9:
            r11 = move-exception
            r7 = r8
            goto L_0x00d6
        L_0x00fc:
            r11 = move-exception
            r3 = r4
            goto L_0x00b9
        L_0x00ff:
            r11 = move-exception
            r5 = r6
            goto L_0x0099
        */
        throw new UnsupportedOperationException("Method not decompiled: com.contec.phms.manager.datas.DataObject.makeObjectFile_addCase(java.lang.Object, java.lang.String):java.lang.String");
    }
}
