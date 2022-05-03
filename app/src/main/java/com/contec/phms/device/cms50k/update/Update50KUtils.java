package com.contec.phms.device.cms50k.update;

import android.os.Environment;
import android.util.Log;
import com.contec.phms.App_phms;
import com.contec.phms.util.Constants;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.List;
import org.apache.commons.httpclient.cookie.CookieSpec;
import u.aly.bs;
import u.aly.dp;

public class Update50KUtils {
    public static final String MainURl = "http://data2.contec365.com";
    public static final String TAG = Update50KUtils.class.getSimpleName();
    public static final String XmlURL = "http://data2.contec365.com/updatesoftware/androidsoftware/4020/current.xml";
    public static final String fileUrl = (Environment.getExternalStorageDirectory() + CookieSpec.PATH_DELIM + "CYw/");
    public static boolean flagClose = false;
    private static FileInputStream instream = null;
    public static long islong = 0;
    public static long mUpdateFileSize = 0;
    public static RandomAccessFile randomFile = null;
    public static final String updateFilename = "update.bin";
    public static final String updateXMlFilename = "updateFile.xml";

    /* JADX WARNING: Code restructure failed: missing block: B:19:0x0050, code lost:
        r2 = r3;
        r0 = r1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:21:?, code lost:
        r5 = r7.next();
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.util.List<com.contec.phms.device.cms50k.update.Xml50KUpdateBean> XMLParser(java.lang.String r13) {
        /*
            r8 = 0
            r0 = 0
            r2 = 0
            if (r13 == 0) goto L_0x001b
            java.lang.String r10 = r13.trim()     // Catch:{ Exception -> 0x0034 }
            java.lang.String r11 = ""
            boolean r10 = r10.equals(r11)     // Catch:{ Exception -> 0x0034 }
            if (r10 != 0) goto L_0x001b
            java.io.ByteArrayInputStream r9 = new java.io.ByteArrayInputStream     // Catch:{ Exception -> 0x0034 }
            byte[] r10 = r13.getBytes()     // Catch:{ Exception -> 0x0034 }
            r9.<init>(r10)     // Catch:{ Exception -> 0x0034 }
            r8 = r9
        L_0x001b:
            org.xmlpull.v1.XmlPullParser r7 = android.util.Xml.newPullParser()
            java.lang.String r10 = "UTF-8"
            r7.setInput(r8, r10)     // Catch:{ XmlPullParserException -> 0x0117, IOException -> 0x011d }
            int r5 = r7.getEventType()     // Catch:{ XmlPullParserException -> 0x0117, IOException -> 0x011d }
            r3 = r2
            r1 = r0
        L_0x002a:
            r10 = 1
            if (r5 != r10) goto L_0x004d
            r8.close()     // Catch:{ XmlPullParserException -> 0x012a, IOException -> 0x0123 }
            r2 = r3
            r0 = r1
        L_0x0032:
            r3 = r2
        L_0x0033:
            return r3
        L_0x0034:
            r4 = move-exception
            java.lang.String r10 = "info"
            java.lang.StringBuilder r11 = new java.lang.StringBuilder
            java.lang.String r12 = r4.getMessage()
            java.lang.String r12 = java.lang.String.valueOf(r12)
            r11.<init>(r12)
            java.lang.String r11 = r11.toString()
            android.util.Log.i(r10, r11)
            r3 = r2
            goto L_0x0033
        L_0x004d:
            switch(r5) {
                case 0: goto L_0x0059;
                case 1: goto L_0x0050;
                case 2: goto L_0x0060;
                case 3: goto L_0x0104;
                default: goto L_0x0050;
            }
        L_0x0050:
            r2 = r3
            r0 = r1
        L_0x0052:
            int r5 = r7.next()     // Catch:{ XmlPullParserException -> 0x0117, IOException -> 0x011d }
            r3 = r2
            r1 = r0
            goto L_0x002a
        L_0x0059:
            java.util.ArrayList r2 = new java.util.ArrayList     // Catch:{ XmlPullParserException -> 0x012a, IOException -> 0x0123 }
            r2.<init>()     // Catch:{ XmlPullParserException -> 0x012a, IOException -> 0x0123 }
            r0 = r1
            goto L_0x0052
        L_0x0060:
            java.lang.String r6 = r7.getName()     // Catch:{ XmlPullParserException -> 0x012a, IOException -> 0x0123 }
            java.lang.String r10 = "file"
            boolean r10 = r6.equalsIgnoreCase(r10)     // Catch:{ XmlPullParserException -> 0x012a, IOException -> 0x0123 }
            if (r10 == 0) goto L_0x0134
            com.contec.phms.device.cms50k.update.Xml50KUpdateBean r0 = new com.contec.phms.device.cms50k.update.Xml50KUpdateBean     // Catch:{ XmlPullParserException -> 0x012a, IOException -> 0x0123 }
            r0.<init>()     // Catch:{ XmlPullParserException -> 0x012a, IOException -> 0x0123 }
        L_0x0071:
            java.lang.String r10 = "type"
            boolean r10 = r6.equalsIgnoreCase(r10)     // Catch:{ XmlPullParserException -> 0x012e, IOException -> 0x0127 }
            if (r10 == 0) goto L_0x0081
            java.lang.String r10 = r7.nextText()     // Catch:{ XmlPullParserException -> 0x012e, IOException -> 0x0127 }
            r0.type = r10     // Catch:{ XmlPullParserException -> 0x012e, IOException -> 0x0127 }
            r2 = r3
            goto L_0x0052
        L_0x0081:
            java.lang.String r10 = "fname"
            boolean r10 = r6.equalsIgnoreCase(r10)     // Catch:{ XmlPullParserException -> 0x012e, IOException -> 0x0127 }
            if (r10 == 0) goto L_0x0091
            java.lang.String r10 = r7.nextText()     // Catch:{ XmlPullParserException -> 0x012e, IOException -> 0x0127 }
            r0.fname = r10     // Catch:{ XmlPullParserException -> 0x012e, IOException -> 0x0127 }
            r2 = r3
            goto L_0x0052
        L_0x0091:
            java.lang.String r10 = "md5"
            boolean r10 = r6.equalsIgnoreCase(r10)     // Catch:{ XmlPullParserException -> 0x012e, IOException -> 0x0127 }
            if (r10 == 0) goto L_0x00a1
            java.lang.String r10 = r7.nextText()     // Catch:{ XmlPullParserException -> 0x012e, IOException -> 0x0127 }
            r0.md5 = r10     // Catch:{ XmlPullParserException -> 0x012e, IOException -> 0x0127 }
            r2 = r3
            goto L_0x0052
        L_0x00a1:
            java.lang.String r10 = "path"
            boolean r10 = r6.equalsIgnoreCase(r10)     // Catch:{ XmlPullParserException -> 0x012e, IOException -> 0x0127 }
            if (r10 == 0) goto L_0x00b1
            java.lang.String r10 = r7.nextText()     // Catch:{ XmlPullParserException -> 0x012e, IOException -> 0x0127 }
            r0.path = r10     // Catch:{ XmlPullParserException -> 0x012e, IOException -> 0x0127 }
            r2 = r3
            goto L_0x0052
        L_0x00b1:
            java.lang.String r10 = "size"
            boolean r10 = r6.equalsIgnoreCase(r10)     // Catch:{ XmlPullParserException -> 0x012e, IOException -> 0x0127 }
            if (r10 == 0) goto L_0x00c1
            java.lang.String r10 = r7.nextText()     // Catch:{ XmlPullParserException -> 0x012e, IOException -> 0x0127 }
            r0.size = r10     // Catch:{ XmlPullParserException -> 0x012e, IOException -> 0x0127 }
            r2 = r3
            goto L_0x0052
        L_0x00c1:
            java.lang.String r10 = "typecode"
            boolean r10 = r6.equalsIgnoreCase(r10)     // Catch:{ XmlPullParserException -> 0x012e, IOException -> 0x0127 }
            if (r10 == 0) goto L_0x00d1
            java.lang.String r10 = r7.nextText()     // Catch:{ XmlPullParserException -> 0x012e, IOException -> 0x0127 }
            r0.typecode = r10     // Catch:{ XmlPullParserException -> 0x012e, IOException -> 0x0127 }
            r2 = r3
            goto L_0x0052
        L_0x00d1:
            java.lang.String r10 = "version"
            boolean r10 = r6.equalsIgnoreCase(r10)     // Catch:{ XmlPullParserException -> 0x012e, IOException -> 0x0127 }
            if (r10 == 0) goto L_0x00e2
            java.lang.String r10 = r7.nextText()     // Catch:{ XmlPullParserException -> 0x012e, IOException -> 0x0127 }
            r0.version = r10     // Catch:{ XmlPullParserException -> 0x012e, IOException -> 0x0127 }
            r2 = r3
            goto L_0x0052
        L_0x00e2:
            java.lang.String r10 = "uploaddate"
            boolean r10 = r6.equalsIgnoreCase(r10)     // Catch:{ XmlPullParserException -> 0x012e, IOException -> 0x0127 }
            if (r10 == 0) goto L_0x00f3
            java.lang.String r10 = r7.nextText()     // Catch:{ XmlPullParserException -> 0x012e, IOException -> 0x0127 }
            r0.uploaddate = r10     // Catch:{ XmlPullParserException -> 0x012e, IOException -> 0x0127 }
            r2 = r3
            goto L_0x0052
        L_0x00f3:
            java.lang.String r10 = "description"
            boolean r10 = r6.equalsIgnoreCase(r10)     // Catch:{ XmlPullParserException -> 0x012e, IOException -> 0x0127 }
            if (r10 == 0) goto L_0x0131
            java.lang.String r10 = r7.nextText()     // Catch:{ XmlPullParserException -> 0x012e, IOException -> 0x0127 }
            r0.description = r10     // Catch:{ XmlPullParserException -> 0x012e, IOException -> 0x0127 }
            r2 = r3
            goto L_0x0052
        L_0x0104:
            java.lang.String r10 = r7.getName()     // Catch:{ XmlPullParserException -> 0x012a, IOException -> 0x0123 }
            java.lang.String r11 = "file"
            boolean r10 = r10.equalsIgnoreCase(r11)     // Catch:{ XmlPullParserException -> 0x012a, IOException -> 0x0123 }
            if (r10 == 0) goto L_0x0050
            r3.add(r1)     // Catch:{ XmlPullParserException -> 0x012a, IOException -> 0x0123 }
            r0 = 0
            r2 = r3
            goto L_0x0052
        L_0x0117:
            r4 = move-exception
        L_0x0118:
            r4.printStackTrace()
            goto L_0x0032
        L_0x011d:
            r4 = move-exception
        L_0x011e:
            r4.printStackTrace()
            goto L_0x0032
        L_0x0123:
            r4 = move-exception
            r2 = r3
            r0 = r1
            goto L_0x011e
        L_0x0127:
            r4 = move-exception
            r2 = r3
            goto L_0x011e
        L_0x012a:
            r4 = move-exception
            r2 = r3
            r0 = r1
            goto L_0x0118
        L_0x012e:
            r4 = move-exception
            r2 = r3
            goto L_0x0118
        L_0x0131:
            r2 = r3
            goto L_0x0052
        L_0x0134:
            r0 = r1
            goto L_0x0071
        */
        throw new UnsupportedOperationException("Method not decompiled: com.contec.phms.device.cms50k.update.Update50KUtils.XMLParser(java.lang.String):java.util.List");
    }

    public static byte[] doPackIsUpdateCommand(byte[] pack) {
        int hiPre = ((pack[7] << 8) | (pack[6] & 255)) & 65535;
        String valueOf = String.valueOf(hiPre);
        int hiPre1 = (((pack[11] & 255) << 24) | ((pack[10] & 255) << dp.n) | ((pack[9] & 255) << 8) | (pack[8] & 255)) & -1;
        mUpdateFileSize = (long) hiPre1;
        Log.e("wsd1", "String  long >>" + Integer.toHexString(hiPre1));
        return new byte[]{-76, 0, (byte) (hiPre & 127), (byte) ((hiPre >> 7) & 127), (byte) ((hiPre >> 14) & 127), (byte) (hiPre1 & 127), (byte) ((hiPre1 >> 7) & 127), (byte) ((hiPre1 >> 14) & 127), 0};
    }

    public static byte[] doPackUpdateData(byte[] pack) {
        byte mm;
        byte mm2;
        byte mm3;
        byte mm4;
        byte mm5;
        byte mm6;
        byte[] pack1 = new byte[50];
        pack1[0] = -75;
        for (int i = 0; i < 7; i++) {
            byte mm7 = pack[i];
            byte mm1 = mm7;
            if (mm7 < 0) {
                mm6 = (byte) ((((byte) (((byte) ((pack[i] << 0) & 128)) >> (7 - i))) ^ -1) + 1);
                int i2 = i + 7;
                pack1[i2] = (byte) (pack1[i2] | (mm1 & Byte.MAX_VALUE));
            } else {
                mm6 = (byte) (((byte) ((mm7 << 0) & 128)) >> (7 - i));
                int i3 = i + 7;
                pack1[i3] = (byte) (pack1[i3] | (mm1 >> 0));
            }
            pack1[1] = (byte) (pack1[1] | mm6);
        }
        for (int i4 = 0; i4 < 7; i4++) {
            byte mm8 = pack[i4 + 7];
            byte mm12 = mm8;
            if (mm8 < 0) {
                mm5 = (byte) ((((byte) (((byte) ((mm8 << 0) & 128)) >> (7 - i4))) ^ -1) + 1);
                int i5 = i4 + 14;
                pack1[i5] = (byte) (pack1[i5] | (mm12 & Byte.MAX_VALUE));
            } else {
                mm5 = (byte) (((byte) ((mm8 << 0) & 128)) >> (7 - i4));
                int i6 = i4 + 14;
                pack1[i6] = (byte) (pack1[i6] | (mm12 >> 0));
            }
            pack1[2] = (byte) (pack1[2] | mm5);
        }
        for (int i7 = 0; i7 < 7; i7++) {
            byte mm9 = pack[i7 + 14];
            byte mm13 = mm9;
            if (mm9 < 0) {
                mm4 = (byte) ((((byte) (((byte) ((mm9 << 0) & 128)) >> (7 - i7))) ^ -1) + 1);
                int i8 = i7 + 21;
                pack1[i8] = (byte) (pack1[i8] | (mm13 & Byte.MAX_VALUE));
            } else {
                mm4 = (byte) (((byte) ((mm9 << 0) & 128)) >> (7 - i7));
                int i9 = i7 + 21;
                pack1[i9] = (byte) (pack1[i9] | (mm13 >> 0));
            }
            pack1[3] = (byte) (pack1[3] | mm4);
        }
        for (int i10 = 0; i10 < 7; i10++) {
            byte mm10 = pack[i10 + 21];
            byte mm14 = mm10;
            if (mm10 < 0) {
                mm3 = (byte) ((((byte) (((byte) ((mm10 << 0) & 128)) >> (7 - i10))) ^ -1) + 1);
                int i11 = i10 + 28;
                pack1[i11] = (byte) (pack1[i11] | (mm14 & Byte.MAX_VALUE));
            } else {
                mm3 = (byte) (((byte) ((mm10 << 0) & 128)) >> (7 - i10));
                int i12 = i10 + 28;
                pack1[i12] = (byte) (pack1[i12] | (mm14 >> 0));
            }
            pack1[4] = (byte) (pack1[4] | mm3);
        }
        for (int i13 = 0; i13 < 7; i13++) {
            byte mm11 = pack[i13 + 28];
            byte mm15 = mm11;
            if (mm11 < 0) {
                mm2 = (byte) ((((byte) (((byte) ((mm11 << 0) & 128)) >> (7 - i13))) ^ -1) + 1);
                int i14 = i13 + 35;
                pack1[i14] = (byte) (pack1[i14] | (mm15 & Byte.MAX_VALUE));
            } else {
                mm2 = (byte) (((byte) ((mm11 << 0) & 128)) >> (7 - i13));
                int i15 = i13 + 35;
                pack1[i15] = (byte) (pack1[i15] | (mm15 >> 0));
            }
            pack1[5] = (byte) (pack1[5] | mm2);
        }
        for (int i16 = 0; i16 < 7; i16++) {
            byte mm16 = pack[i16 + 35];
            byte mm17 = mm16;
            if (mm16 < 0) {
                mm = (byte) ((((byte) (((byte) ((mm16 << 0) & 128)) >> (7 - i16))) ^ -1) + 1);
                int i17 = i16 + 42;
                pack1[i17] = (byte) (pack1[i17] | (mm17 & Byte.MAX_VALUE));
            } else {
                mm = (byte) (((byte) ((mm16 << 0) & 128)) >> (7 - i16));
                int i18 = i16 + 42;
                pack1[i18] = (byte) (pack1[i18] | (mm17 >> 0));
            }
            pack1[6] = (byte) (pack1[6] | mm);
        }
        return pack1;
    }

    /* JADX WARNING: Removed duplicated region for block: B:21:0x003c A[SYNTHETIC, Splitter:B:21:0x003c] */
    /* JADX WARNING: Removed duplicated region for block: B:29:0x0050 A[SYNTHETIC, Splitter:B:29:0x0050] */
    /* JADX WARNING: Removed duplicated region for block: B:35:0x005c A[SYNTHETIC, Splitter:B:35:0x005c] */
    /* JADX WARNING: Unknown top exception splitter block from list: {B:18:0x0032=Splitter:B:18:0x0032, B:26:0x0046=Splitter:B:26:0x0046} */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static byte[] readSDFile(java.lang.String r8) {
        /*
            r1 = 0
            byte[] r1 = (byte[]) r1
            java.io.File r3 = new java.io.File
            r3.<init>(r8)
            boolean r7 = r3.exists()
            if (r7 != 0) goto L_0x0017
            r3.createNewFile()     // Catch:{ Exception -> 0x0012 }
        L_0x0011:
            return r1
        L_0x0012:
            r2 = move-exception
            r2.printStackTrace()
            goto L_0x0011
        L_0x0017:
            r4 = 0
            java.io.FileInputStream r5 = new java.io.FileInputStream     // Catch:{ FileNotFoundException -> 0x0031, IOException -> 0x0045 }
            r5.<init>(r3)     // Catch:{ FileNotFoundException -> 0x0031, IOException -> 0x0045 }
            int r6 = r5.available()     // Catch:{ FileNotFoundException -> 0x006b, IOException -> 0x0068, all -> 0x0065 }
            byte[] r1 = new byte[r6]     // Catch:{ FileNotFoundException -> 0x006b, IOException -> 0x0068, all -> 0x0065 }
            r5.read(r1)     // Catch:{ FileNotFoundException -> 0x006b, IOException -> 0x0068, all -> 0x0065 }
            if (r5 == 0) goto L_0x0011
            r5.close()     // Catch:{ IOException -> 0x002c }
            goto L_0x0011
        L_0x002c:
            r2 = move-exception
            r2.printStackTrace()
            goto L_0x0011
        L_0x0031:
            r2 = move-exception
        L_0x0032:
            r2.printStackTrace()     // Catch:{ all -> 0x0059 }
            r7 = 0
            r0 = r7
            byte[] r0 = (byte[]) r0     // Catch:{ all -> 0x0059 }
            r1 = r0
            if (r4 == 0) goto L_0x0011
            r4.close()     // Catch:{ IOException -> 0x0040 }
            goto L_0x0011
        L_0x0040:
            r2 = move-exception
            r2.printStackTrace()
            goto L_0x0011
        L_0x0045:
            r2 = move-exception
        L_0x0046:
            r2.printStackTrace()     // Catch:{ all -> 0x0059 }
            r7 = 0
            r0 = r7
            byte[] r0 = (byte[]) r0     // Catch:{ all -> 0x0059 }
            r1 = r0
            if (r4 == 0) goto L_0x0011
            r4.close()     // Catch:{ IOException -> 0x0054 }
            goto L_0x0011
        L_0x0054:
            r2 = move-exception
            r2.printStackTrace()
            goto L_0x0011
        L_0x0059:
            r7 = move-exception
        L_0x005a:
            if (r4 == 0) goto L_0x005f
            r4.close()     // Catch:{ IOException -> 0x0060 }
        L_0x005f:
            throw r7
        L_0x0060:
            r2 = move-exception
            r2.printStackTrace()
            goto L_0x005f
        L_0x0065:
            r7 = move-exception
            r4 = r5
            goto L_0x005a
        L_0x0068:
            r2 = move-exception
            r4 = r5
            goto L_0x0046
        L_0x006b:
            r2 = move-exception
            r4 = r5
            goto L_0x0032
        */
        throw new UnsupportedOperationException("Method not decompiled: com.contec.phms.device.cms50k.update.Update50KUtils.readSDFile(java.lang.String):byte[]");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:11:?, code lost:
        r3 = r7.next();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:9:0x0030, code lost:
        r0 = r1;
        r8 = r9;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.util.List<com.contec.phms.device.cms50k.update.Xml50KUpdateBean> pullParseXmlFile() {
        /*
            r8 = 0
            r0 = 0
            org.xmlpull.v1.XmlPullParserFactory r4 = org.xmlpull.v1.XmlPullParserFactory.newInstance()     // Catch:{ XmlPullParserException -> 0x00db, IOException -> 0x00e1 }
            org.xmlpull.v1.XmlPullParser r7 = r4.newPullParser()     // Catch:{ XmlPullParserException -> 0x00db, IOException -> 0x00e1 }
            java.io.File r10 = new java.io.File     // Catch:{ XmlPullParserException -> 0x00db, IOException -> 0x00e1 }
            java.lang.String r11 = fileUrl     // Catch:{ XmlPullParserException -> 0x00db, IOException -> 0x00e1 }
            java.lang.String r12 = "updateFile.xml"
            r10.<init>(r11, r12)     // Catch:{ XmlPullParserException -> 0x00db, IOException -> 0x00e1 }
            java.io.FileInputStream r5 = new java.io.FileInputStream     // Catch:{ XmlPullParserException -> 0x00db, IOException -> 0x00e1 }
            r5.<init>(r10)     // Catch:{ XmlPullParserException -> 0x00db, IOException -> 0x00e1 }
            java.lang.String r11 = "utf-8"
            r7.setInput(r5, r11)     // Catch:{ XmlPullParserException -> 0x00db, IOException -> 0x00e1 }
            int r3 = r7.getEventType()     // Catch:{ XmlPullParserException -> 0x00db, IOException -> 0x00e1 }
            r1 = r0
            r9 = r8
        L_0x0023:
            r11 = 1
            if (r11 != r3) goto L_0x0029
            r0 = r1
            r8 = r9
        L_0x0028:
            return r8
        L_0x0029:
            java.lang.String r6 = r7.getName()     // Catch:{ XmlPullParserException -> 0x00ee, IOException -> 0x00e7 }
            switch(r3) {
                case 0: goto L_0x0039;
                case 1: goto L_0x00d7;
                case 2: goto L_0x0040;
                case 3: goto L_0x00c8;
                default: goto L_0x0030;
            }
        L_0x0030:
            r0 = r1
            r8 = r9
        L_0x0032:
            int r3 = r7.next()     // Catch:{ XmlPullParserException -> 0x00db, IOException -> 0x00e1 }
            r1 = r0
            r9 = r8
            goto L_0x0023
        L_0x0039:
            java.util.ArrayList r8 = new java.util.ArrayList     // Catch:{ XmlPullParserException -> 0x00ee, IOException -> 0x00e7 }
            r8.<init>()     // Catch:{ XmlPullParserException -> 0x00ee, IOException -> 0x00e7 }
            r0 = r1
            goto L_0x0032
        L_0x0040:
            java.lang.String r11 = "file"
            boolean r11 = r11.equals(r6)     // Catch:{ XmlPullParserException -> 0x00ee, IOException -> 0x00e7 }
            if (r11 == 0) goto L_0x00f8
            com.contec.phms.device.cms50k.update.Xml50KUpdateBean r0 = new com.contec.phms.device.cms50k.update.Xml50KUpdateBean     // Catch:{ XmlPullParserException -> 0x00ee, IOException -> 0x00e7 }
            r0.<init>()     // Catch:{ XmlPullParserException -> 0x00ee, IOException -> 0x00e7 }
        L_0x004d:
            java.lang.String r11 = "fname"
            boolean r11 = r11.equals(r6)     // Catch:{ XmlPullParserException -> 0x00f2, IOException -> 0x00eb }
            if (r11 == 0) goto L_0x005c
            java.lang.String r11 = r7.nextText()     // Catch:{ XmlPullParserException -> 0x00f2, IOException -> 0x00eb }
            r0.setFname(r11)     // Catch:{ XmlPullParserException -> 0x00f2, IOException -> 0x00eb }
        L_0x005c:
            java.lang.String r11 = "md5"
            boolean r11 = r11.equals(r6)     // Catch:{ XmlPullParserException -> 0x00f2, IOException -> 0x00eb }
            if (r11 == 0) goto L_0x006b
            java.lang.String r11 = r7.nextText()     // Catch:{ XmlPullParserException -> 0x00f2, IOException -> 0x00eb }
            r0.setMd5(r11)     // Catch:{ XmlPullParserException -> 0x00f2, IOException -> 0x00eb }
        L_0x006b:
            java.lang.String r11 = "path"
            boolean r11 = r11.equals(r6)     // Catch:{ XmlPullParserException -> 0x00f2, IOException -> 0x00eb }
            if (r11 == 0) goto L_0x007a
            java.lang.String r11 = r7.nextText()     // Catch:{ XmlPullParserException -> 0x00f2, IOException -> 0x00eb }
            r0.setPath(r11)     // Catch:{ XmlPullParserException -> 0x00f2, IOException -> 0x00eb }
        L_0x007a:
            java.lang.String r11 = "size"
            boolean r11 = r11.equals(r6)     // Catch:{ XmlPullParserException -> 0x00f2, IOException -> 0x00eb }
            if (r11 == 0) goto L_0x0089
            java.lang.String r11 = r7.nextText()     // Catch:{ XmlPullParserException -> 0x00f2, IOException -> 0x00eb }
            r0.setSize(r11)     // Catch:{ XmlPullParserException -> 0x00f2, IOException -> 0x00eb }
        L_0x0089:
            java.lang.String r11 = "version"
            boolean r11 = r11.equals(r6)     // Catch:{ XmlPullParserException -> 0x00f2, IOException -> 0x00eb }
            if (r11 == 0) goto L_0x0098
            java.lang.String r11 = r7.nextText()     // Catch:{ XmlPullParserException -> 0x00f2, IOException -> 0x00eb }
            r0.setVersion(r11)     // Catch:{ XmlPullParserException -> 0x00f2, IOException -> 0x00eb }
        L_0x0098:
            java.lang.String r11 = "typecode"
            boolean r11 = r11.equals(r6)     // Catch:{ XmlPullParserException -> 0x00f2, IOException -> 0x00eb }
            if (r11 == 0) goto L_0x00a7
            java.lang.String r11 = r7.nextText()     // Catch:{ XmlPullParserException -> 0x00f2, IOException -> 0x00eb }
            r0.setType(r11)     // Catch:{ XmlPullParserException -> 0x00f2, IOException -> 0x00eb }
        L_0x00a7:
            java.lang.String r11 = "description"
            boolean r11 = r11.equals(r6)     // Catch:{ XmlPullParserException -> 0x00f2, IOException -> 0x00eb }
            if (r11 == 0) goto L_0x00b6
            java.lang.String r11 = r7.nextText()     // Catch:{ XmlPullParserException -> 0x00f2, IOException -> 0x00eb }
            r0.setDescription(r11)     // Catch:{ XmlPullParserException -> 0x00f2, IOException -> 0x00eb }
        L_0x00b6:
            java.lang.String r11 = "uploaddate"
            boolean r11 = r11.equals(r6)     // Catch:{ XmlPullParserException -> 0x00f2, IOException -> 0x00eb }
            if (r11 == 0) goto L_0x00f5
            java.lang.String r11 = r7.nextText()     // Catch:{ XmlPullParserException -> 0x00f2, IOException -> 0x00eb }
            r0.setUploaddate(r11)     // Catch:{ XmlPullParserException -> 0x00f2, IOException -> 0x00eb }
            r8 = r9
            goto L_0x0032
        L_0x00c8:
            java.lang.String r11 = "file"
            boolean r11 = r11.equals(r6)     // Catch:{ XmlPullParserException -> 0x00ee, IOException -> 0x00e7 }
            if (r11 == 0) goto L_0x0030
            r9.add(r1)     // Catch:{ XmlPullParserException -> 0x00ee, IOException -> 0x00e7 }
            r0 = 0
            r8 = r9
            goto L_0x0032
        L_0x00d7:
            r0 = r1
            r8 = r9
            goto L_0x0032
        L_0x00db:
            r2 = move-exception
        L_0x00dc:
            r2.printStackTrace()
            goto L_0x0028
        L_0x00e1:
            r2 = move-exception
        L_0x00e2:
            r2.printStackTrace()
            goto L_0x0028
        L_0x00e7:
            r2 = move-exception
            r0 = r1
            r8 = r9
            goto L_0x00e2
        L_0x00eb:
            r2 = move-exception
            r8 = r9
            goto L_0x00e2
        L_0x00ee:
            r2 = move-exception
            r0 = r1
            r8 = r9
            goto L_0x00dc
        L_0x00f2:
            r2 = move-exception
            r8 = r9
            goto L_0x00dc
        L_0x00f5:
            r8 = r9
            goto L_0x0032
        L_0x00f8:
            r0 = r1
            goto L_0x004d
        */
        throw new UnsupportedOperationException("Method not decompiled: com.contec.phms.device.cms50k.update.Update50KUtils.pullParseXmlFile():java.util.List");
    }

    /* JADX WARNING: Removed duplicated region for block: B:32:0x017a A[SYNTHETIC, Splitter:B:32:0x017a] */
    /* JADX WARNING: Removed duplicated region for block: B:40:0x018b A[SYNTHETIC, Splitter:B:40:0x018b] */
    /* JADX WARNING: Removed duplicated region for block: B:48:0x019c A[SYNTHETIC, Splitter:B:48:0x019c] */
    /* JADX WARNING: Removed duplicated region for block: B:54:0x01aa A[SYNTHETIC, Splitter:B:54:0x01aa] */
    /* JADX WARNING: Removed duplicated region for block: B:76:? A[RETURN, SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:78:? A[RETURN, SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:80:? A[RETURN, SYNTHETIC] */
    /* JADX WARNING: Unknown top exception splitter block from list: {B:21:0x0164=Splitter:B:21:0x0164, B:29:0x0175=Splitter:B:29:0x0175, B:37:0x0186=Splitter:B:37:0x0186, B:45:0x0197=Splitter:B:45:0x0197} */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static void createUpdateXmlFile(java.util.List<com.contec.phms.device.cms50k.update.Xml50KUpdateBean> r11) {
        /*
            java.lang.String r8 = android.os.Environment.getExternalStorageState()
            java.lang.String r9 = "mounted"
            boolean r8 = r8.equals(r9)
            if (r8 == 0) goto L_0x005a
            org.xmlpull.v1.XmlSerializer r6 = android.util.Xml.newSerializer()
            java.lang.String r2 = fileUrl
            java.io.File r1 = new java.io.File
            r1.<init>(r2)
            boolean r8 = r1.exists()
            if (r8 != 0) goto L_0x0020
            r1.mkdir()
        L_0x0020:
            java.io.File r7 = new java.io.File
            java.lang.String r8 = "updateFile.xml"
            r7.<init>(r2, r8)
            r3 = 0
            java.io.FileOutputStream r4 = new java.io.FileOutputStream     // Catch:{ FileNotFoundException -> 0x01c5, IllegalArgumentException -> 0x0174, IllegalStateException -> 0x0185, IOException -> 0x0196 }
            r4.<init>(r7)     // Catch:{ FileNotFoundException -> 0x01c5, IllegalArgumentException -> 0x0174, IllegalStateException -> 0x0185, IOException -> 0x0196 }
            java.lang.String r8 = "utf-8"
            r6.setOutput(r4, r8)     // Catch:{ FileNotFoundException -> 0x0162, IllegalArgumentException -> 0x01c2, IllegalStateException -> 0x01bf, IOException -> 0x01bc, all -> 0x01b9 }
            java.lang.String r8 = "utf-8"
            r9 = 1
            java.lang.Boolean r9 = java.lang.Boolean.valueOf(r9)     // Catch:{ FileNotFoundException -> 0x0162, IllegalArgumentException -> 0x01c2, IllegalStateException -> 0x01bf, IOException -> 0x01bc, all -> 0x01b9 }
            r6.startDocument(r8, r9)     // Catch:{ FileNotFoundException -> 0x0162, IllegalArgumentException -> 0x01c2, IllegalStateException -> 0x01bf, IOException -> 0x01bc, all -> 0x01b9 }
            r8 = 0
            java.lang.String r9 = "response"
            r6.startTag(r8, r9)     // Catch:{ FileNotFoundException -> 0x0162, IllegalArgumentException -> 0x01c2, IllegalStateException -> 0x01bf, IOException -> 0x01bc, all -> 0x01b9 }
            java.util.Iterator r8 = r11.iterator()     // Catch:{ FileNotFoundException -> 0x0162, IllegalArgumentException -> 0x01c2, IllegalStateException -> 0x01bf, IOException -> 0x01bc, all -> 0x01b9 }
        L_0x0046:
            boolean r9 = r8.hasNext()     // Catch:{ FileNotFoundException -> 0x0162, IllegalArgumentException -> 0x01c2, IllegalStateException -> 0x01bf, IOException -> 0x01bc, all -> 0x01b9 }
            if (r9 != 0) goto L_0x005b
            r8 = 0
            java.lang.String r9 = "response"
            r6.endTag(r8, r9)     // Catch:{ FileNotFoundException -> 0x0162, IllegalArgumentException -> 0x01c2, IllegalStateException -> 0x01bf, IOException -> 0x01bc, all -> 0x01b9 }
            r6.endDocument()     // Catch:{ FileNotFoundException -> 0x0162, IllegalArgumentException -> 0x01c2, IllegalStateException -> 0x01bf, IOException -> 0x01bc, all -> 0x01b9 }
            if (r4 == 0) goto L_0x005a
            r4.close()     // Catch:{ IOException -> 0x01b3 }
        L_0x005a:
            return
        L_0x005b:
            java.lang.Object r5 = r8.next()     // Catch:{ FileNotFoundException -> 0x0162, IllegalArgumentException -> 0x01c2, IllegalStateException -> 0x01bf, IOException -> 0x01bc, all -> 0x01b9 }
            com.contec.phms.device.cms50k.update.Xml50KUpdateBean r5 = (com.contec.phms.device.cms50k.update.Xml50KUpdateBean) r5     // Catch:{ FileNotFoundException -> 0x0162, IllegalArgumentException -> 0x01c2, IllegalStateException -> 0x01bf, IOException -> 0x01bc, all -> 0x01b9 }
            r9 = 0
            java.lang.String r10 = "file"
            r6.startTag(r9, r10)     // Catch:{ FileNotFoundException -> 0x0162, IllegalArgumentException -> 0x01c2, IllegalStateException -> 0x01bf, IOException -> 0x01bc, all -> 0x01b9 }
            r9 = 0
            java.lang.String r10 = "fname"
            r6.startTag(r9, r10)     // Catch:{ FileNotFoundException -> 0x0162, IllegalArgumentException -> 0x01c2, IllegalStateException -> 0x01bf, IOException -> 0x01bc, all -> 0x01b9 }
            java.lang.StringBuilder r9 = new java.lang.StringBuilder     // Catch:{ FileNotFoundException -> 0x0162, IllegalArgumentException -> 0x01c2, IllegalStateException -> 0x01bf, IOException -> 0x01bc, all -> 0x01b9 }
            java.lang.String r10 = r5.getFname()     // Catch:{ FileNotFoundException -> 0x0162, IllegalArgumentException -> 0x01c2, IllegalStateException -> 0x01bf, IOException -> 0x01bc, all -> 0x01b9 }
            java.lang.String r10 = java.lang.String.valueOf(r10)     // Catch:{ FileNotFoundException -> 0x0162, IllegalArgumentException -> 0x01c2, IllegalStateException -> 0x01bf, IOException -> 0x01bc, all -> 0x01b9 }
            r9.<init>(r10)     // Catch:{ FileNotFoundException -> 0x0162, IllegalArgumentException -> 0x01c2, IllegalStateException -> 0x01bf, IOException -> 0x01bc, all -> 0x01b9 }
            java.lang.String r9 = r9.toString()     // Catch:{ FileNotFoundException -> 0x0162, IllegalArgumentException -> 0x01c2, IllegalStateException -> 0x01bf, IOException -> 0x01bc, all -> 0x01b9 }
            r6.text(r9)     // Catch:{ FileNotFoundException -> 0x0162, IllegalArgumentException -> 0x01c2, IllegalStateException -> 0x01bf, IOException -> 0x01bc, all -> 0x01b9 }
            r9 = 0
            java.lang.String r10 = "fname"
            r6.endTag(r9, r10)     // Catch:{ FileNotFoundException -> 0x0162, IllegalArgumentException -> 0x01c2, IllegalStateException -> 0x01bf, IOException -> 0x01bc, all -> 0x01b9 }
            r9 = 0
            java.lang.String r10 = "md5"
            r6.startTag(r9, r10)     // Catch:{ FileNotFoundException -> 0x0162, IllegalArgumentException -> 0x01c2, IllegalStateException -> 0x01bf, IOException -> 0x01bc, all -> 0x01b9 }
            java.lang.StringBuilder r9 = new java.lang.StringBuilder     // Catch:{ FileNotFoundException -> 0x0162, IllegalArgumentException -> 0x01c2, IllegalStateException -> 0x01bf, IOException -> 0x01bc, all -> 0x01b9 }
            java.lang.String r10 = r5.getMd5()     // Catch:{ FileNotFoundException -> 0x0162, IllegalArgumentException -> 0x01c2, IllegalStateException -> 0x01bf, IOException -> 0x01bc, all -> 0x01b9 }
            java.lang.String r10 = java.lang.String.valueOf(r10)     // Catch:{ FileNotFoundException -> 0x0162, IllegalArgumentException -> 0x01c2, IllegalStateException -> 0x01bf, IOException -> 0x01bc, all -> 0x01b9 }
            r9.<init>(r10)     // Catch:{ FileNotFoundException -> 0x0162, IllegalArgumentException -> 0x01c2, IllegalStateException -> 0x01bf, IOException -> 0x01bc, all -> 0x01b9 }
            java.lang.String r9 = r9.toString()     // Catch:{ FileNotFoundException -> 0x0162, IllegalArgumentException -> 0x01c2, IllegalStateException -> 0x01bf, IOException -> 0x01bc, all -> 0x01b9 }
            r6.text(r9)     // Catch:{ FileNotFoundException -> 0x0162, IllegalArgumentException -> 0x01c2, IllegalStateException -> 0x01bf, IOException -> 0x01bc, all -> 0x01b9 }
            r9 = 0
            java.lang.String r10 = "md5"
            r6.endTag(r9, r10)     // Catch:{ FileNotFoundException -> 0x0162, IllegalArgumentException -> 0x01c2, IllegalStateException -> 0x01bf, IOException -> 0x01bc, all -> 0x01b9 }
            r9 = 0
            java.lang.String r10 = "path"
            r6.startTag(r9, r10)     // Catch:{ FileNotFoundException -> 0x0162, IllegalArgumentException -> 0x01c2, IllegalStateException -> 0x01bf, IOException -> 0x01bc, all -> 0x01b9 }
            java.lang.String r9 = r5.getPath()     // Catch:{ FileNotFoundException -> 0x0162, IllegalArgumentException -> 0x01c2, IllegalStateException -> 0x01bf, IOException -> 0x01bc, all -> 0x01b9 }
            r6.text(r9)     // Catch:{ FileNotFoundException -> 0x0162, IllegalArgumentException -> 0x01c2, IllegalStateException -> 0x01bf, IOException -> 0x01bc, all -> 0x01b9 }
            r9 = 0
            java.lang.String r10 = "path"
            r6.endTag(r9, r10)     // Catch:{ FileNotFoundException -> 0x0162, IllegalArgumentException -> 0x01c2, IllegalStateException -> 0x01bf, IOException -> 0x01bc, all -> 0x01b9 }
            r9 = 0
            java.lang.String r10 = "size"
            r6.startTag(r9, r10)     // Catch:{ FileNotFoundException -> 0x0162, IllegalArgumentException -> 0x01c2, IllegalStateException -> 0x01bf, IOException -> 0x01bc, all -> 0x01b9 }
            java.lang.StringBuilder r9 = new java.lang.StringBuilder     // Catch:{ FileNotFoundException -> 0x0162, IllegalArgumentException -> 0x01c2, IllegalStateException -> 0x01bf, IOException -> 0x01bc, all -> 0x01b9 }
            java.lang.String r10 = r5.getSize()     // Catch:{ FileNotFoundException -> 0x0162, IllegalArgumentException -> 0x01c2, IllegalStateException -> 0x01bf, IOException -> 0x01bc, all -> 0x01b9 }
            java.lang.String r10 = java.lang.String.valueOf(r10)     // Catch:{ FileNotFoundException -> 0x0162, IllegalArgumentException -> 0x01c2, IllegalStateException -> 0x01bf, IOException -> 0x01bc, all -> 0x01b9 }
            r9.<init>(r10)     // Catch:{ FileNotFoundException -> 0x0162, IllegalArgumentException -> 0x01c2, IllegalStateException -> 0x01bf, IOException -> 0x01bc, all -> 0x01b9 }
            java.lang.String r9 = r9.toString()     // Catch:{ FileNotFoundException -> 0x0162, IllegalArgumentException -> 0x01c2, IllegalStateException -> 0x01bf, IOException -> 0x01bc, all -> 0x01b9 }
            r6.text(r9)     // Catch:{ FileNotFoundException -> 0x0162, IllegalArgumentException -> 0x01c2, IllegalStateException -> 0x01bf, IOException -> 0x01bc, all -> 0x01b9 }
            r9 = 0
            java.lang.String r10 = "size"
            r6.endTag(r9, r10)     // Catch:{ FileNotFoundException -> 0x0162, IllegalArgumentException -> 0x01c2, IllegalStateException -> 0x01bf, IOException -> 0x01bc, all -> 0x01b9 }
            r9 = 0
            java.lang.String r10 = "version"
            r6.startTag(r9, r10)     // Catch:{ FileNotFoundException -> 0x0162, IllegalArgumentException -> 0x01c2, IllegalStateException -> 0x01bf, IOException -> 0x01bc, all -> 0x01b9 }
            java.lang.StringBuilder r9 = new java.lang.StringBuilder     // Catch:{ FileNotFoundException -> 0x0162, IllegalArgumentException -> 0x01c2, IllegalStateException -> 0x01bf, IOException -> 0x01bc, all -> 0x01b9 }
            java.lang.String r10 = r5.getVersion()     // Catch:{ FileNotFoundException -> 0x0162, IllegalArgumentException -> 0x01c2, IllegalStateException -> 0x01bf, IOException -> 0x01bc, all -> 0x01b9 }
            java.lang.String r10 = java.lang.String.valueOf(r10)     // Catch:{ FileNotFoundException -> 0x0162, IllegalArgumentException -> 0x01c2, IllegalStateException -> 0x01bf, IOException -> 0x01bc, all -> 0x01b9 }
            r9.<init>(r10)     // Catch:{ FileNotFoundException -> 0x0162, IllegalArgumentException -> 0x01c2, IllegalStateException -> 0x01bf, IOException -> 0x01bc, all -> 0x01b9 }
            java.lang.String r9 = r9.toString()     // Catch:{ FileNotFoundException -> 0x0162, IllegalArgumentException -> 0x01c2, IllegalStateException -> 0x01bf, IOException -> 0x01bc, all -> 0x01b9 }
            r6.text(r9)     // Catch:{ FileNotFoundException -> 0x0162, IllegalArgumentException -> 0x01c2, IllegalStateException -> 0x01bf, IOException -> 0x01bc, all -> 0x01b9 }
            r9 = 0
            java.lang.String r10 = "version"
            r6.endTag(r9, r10)     // Catch:{ FileNotFoundException -> 0x0162, IllegalArgumentException -> 0x01c2, IllegalStateException -> 0x01bf, IOException -> 0x01bc, all -> 0x01b9 }
            r9 = 0
            java.lang.String r10 = "typecode"
            r6.startTag(r9, r10)     // Catch:{ FileNotFoundException -> 0x0162, IllegalArgumentException -> 0x01c2, IllegalStateException -> 0x01bf, IOException -> 0x01bc, all -> 0x01b9 }
            java.lang.StringBuilder r9 = new java.lang.StringBuilder     // Catch:{ FileNotFoundException -> 0x0162, IllegalArgumentException -> 0x01c2, IllegalStateException -> 0x01bf, IOException -> 0x01bc, all -> 0x01b9 }
            java.lang.String r10 = r5.getTypecode()     // Catch:{ FileNotFoundException -> 0x0162, IllegalArgumentException -> 0x01c2, IllegalStateException -> 0x01bf, IOException -> 0x01bc, all -> 0x01b9 }
            java.lang.String r10 = java.lang.String.valueOf(r10)     // Catch:{ FileNotFoundException -> 0x0162, IllegalArgumentException -> 0x01c2, IllegalStateException -> 0x01bf, IOException -> 0x01bc, all -> 0x01b9 }
            r9.<init>(r10)     // Catch:{ FileNotFoundException -> 0x0162, IllegalArgumentException -> 0x01c2, IllegalStateException -> 0x01bf, IOException -> 0x01bc, all -> 0x01b9 }
            java.lang.String r9 = r9.toString()     // Catch:{ FileNotFoundException -> 0x0162, IllegalArgumentException -> 0x01c2, IllegalStateException -> 0x01bf, IOException -> 0x01bc, all -> 0x01b9 }
            r6.text(r9)     // Catch:{ FileNotFoundException -> 0x0162, IllegalArgumentException -> 0x01c2, IllegalStateException -> 0x01bf, IOException -> 0x01bc, all -> 0x01b9 }
            r9 = 0
            java.lang.String r10 = "typecode"
            r6.endTag(r9, r10)     // Catch:{ FileNotFoundException -> 0x0162, IllegalArgumentException -> 0x01c2, IllegalStateException -> 0x01bf, IOException -> 0x01bc, all -> 0x01b9 }
            r9 = 0
            java.lang.String r10 = "description"
            r6.startTag(r9, r10)     // Catch:{ FileNotFoundException -> 0x0162, IllegalArgumentException -> 0x01c2, IllegalStateException -> 0x01bf, IOException -> 0x01bc, all -> 0x01b9 }
            java.lang.StringBuilder r9 = new java.lang.StringBuilder     // Catch:{ FileNotFoundException -> 0x0162, IllegalArgumentException -> 0x01c2, IllegalStateException -> 0x01bf, IOException -> 0x01bc, all -> 0x01b9 }
            java.lang.String r10 = r5.getDescription()     // Catch:{ FileNotFoundException -> 0x0162, IllegalArgumentException -> 0x01c2, IllegalStateException -> 0x01bf, IOException -> 0x01bc, all -> 0x01b9 }
            java.lang.String r10 = java.lang.String.valueOf(r10)     // Catch:{ FileNotFoundException -> 0x0162, IllegalArgumentException -> 0x01c2, IllegalStateException -> 0x01bf, IOException -> 0x01bc, all -> 0x01b9 }
            r9.<init>(r10)     // Catch:{ FileNotFoundException -> 0x0162, IllegalArgumentException -> 0x01c2, IllegalStateException -> 0x01bf, IOException -> 0x01bc, all -> 0x01b9 }
            java.lang.String r9 = r9.toString()     // Catch:{ FileNotFoundException -> 0x0162, IllegalArgumentException -> 0x01c2, IllegalStateException -> 0x01bf, IOException -> 0x01bc, all -> 0x01b9 }
            r6.text(r9)     // Catch:{ FileNotFoundException -> 0x0162, IllegalArgumentException -> 0x01c2, IllegalStateException -> 0x01bf, IOException -> 0x01bc, all -> 0x01b9 }
            r9 = 0
            java.lang.String r10 = "description"
            r6.endTag(r9, r10)     // Catch:{ FileNotFoundException -> 0x0162, IllegalArgumentException -> 0x01c2, IllegalStateException -> 0x01bf, IOException -> 0x01bc, all -> 0x01b9 }
            r9 = 0
            java.lang.String r10 = "uploaddate"
            r6.startTag(r9, r10)     // Catch:{ FileNotFoundException -> 0x0162, IllegalArgumentException -> 0x01c2, IllegalStateException -> 0x01bf, IOException -> 0x01bc, all -> 0x01b9 }
            java.lang.StringBuilder r9 = new java.lang.StringBuilder     // Catch:{ FileNotFoundException -> 0x0162, IllegalArgumentException -> 0x01c2, IllegalStateException -> 0x01bf, IOException -> 0x01bc, all -> 0x01b9 }
            java.lang.String r10 = r5.getUploaddate()     // Catch:{ FileNotFoundException -> 0x0162, IllegalArgumentException -> 0x01c2, IllegalStateException -> 0x01bf, IOException -> 0x01bc, all -> 0x01b9 }
            java.lang.String r10 = java.lang.String.valueOf(r10)     // Catch:{ FileNotFoundException -> 0x0162, IllegalArgumentException -> 0x01c2, IllegalStateException -> 0x01bf, IOException -> 0x01bc, all -> 0x01b9 }
            r9.<init>(r10)     // Catch:{ FileNotFoundException -> 0x0162, IllegalArgumentException -> 0x01c2, IllegalStateException -> 0x01bf, IOException -> 0x01bc, all -> 0x01b9 }
            java.lang.String r9 = r9.toString()     // Catch:{ FileNotFoundException -> 0x0162, IllegalArgumentException -> 0x01c2, IllegalStateException -> 0x01bf, IOException -> 0x01bc, all -> 0x01b9 }
            r6.text(r9)     // Catch:{ FileNotFoundException -> 0x0162, IllegalArgumentException -> 0x01c2, IllegalStateException -> 0x01bf, IOException -> 0x01bc, all -> 0x01b9 }
            r9 = 0
            java.lang.String r10 = "uploaddate"
            r6.endTag(r9, r10)     // Catch:{ FileNotFoundException -> 0x0162, IllegalArgumentException -> 0x01c2, IllegalStateException -> 0x01bf, IOException -> 0x01bc, all -> 0x01b9 }
            r9 = 0
            java.lang.String r10 = "file"
            r6.endTag(r9, r10)     // Catch:{ FileNotFoundException -> 0x0162, IllegalArgumentException -> 0x01c2, IllegalStateException -> 0x01bf, IOException -> 0x01bc, all -> 0x01b9 }
            goto L_0x0046
        L_0x0162:
            r0 = move-exception
            r3 = r4
        L_0x0164:
            r0.printStackTrace()     // Catch:{ all -> 0x01a7 }
            if (r3 == 0) goto L_0x005a
            r3.close()     // Catch:{ IOException -> 0x016e }
            goto L_0x005a
        L_0x016e:
            r0 = move-exception
            r0.printStackTrace()
            goto L_0x005a
        L_0x0174:
            r0 = move-exception
        L_0x0175:
            r0.printStackTrace()     // Catch:{ all -> 0x01a7 }
            if (r3 == 0) goto L_0x005a
            r3.close()     // Catch:{ IOException -> 0x017f }
            goto L_0x005a
        L_0x017f:
            r0 = move-exception
            r0.printStackTrace()
            goto L_0x005a
        L_0x0185:
            r0 = move-exception
        L_0x0186:
            r0.printStackTrace()     // Catch:{ all -> 0x01a7 }
            if (r3 == 0) goto L_0x005a
            r3.close()     // Catch:{ IOException -> 0x0190 }
            goto L_0x005a
        L_0x0190:
            r0 = move-exception
            r0.printStackTrace()
            goto L_0x005a
        L_0x0196:
            r0 = move-exception
        L_0x0197:
            r0.printStackTrace()     // Catch:{ all -> 0x01a7 }
            if (r3 == 0) goto L_0x005a
            r3.close()     // Catch:{ IOException -> 0x01a1 }
            goto L_0x005a
        L_0x01a1:
            r0 = move-exception
            r0.printStackTrace()
            goto L_0x005a
        L_0x01a7:
            r8 = move-exception
        L_0x01a8:
            if (r3 == 0) goto L_0x01ad
            r3.close()     // Catch:{ IOException -> 0x01ae }
        L_0x01ad:
            throw r8
        L_0x01ae:
            r0 = move-exception
            r0.printStackTrace()
            goto L_0x01ad
        L_0x01b3:
            r0 = move-exception
            r0.printStackTrace()
            goto L_0x005a
        L_0x01b9:
            r8 = move-exception
            r3 = r4
            goto L_0x01a8
        L_0x01bc:
            r0 = move-exception
            r3 = r4
            goto L_0x0197
        L_0x01bf:
            r0 = move-exception
            r3 = r4
            goto L_0x0186
        L_0x01c2:
            r0 = move-exception
            r3 = r4
            goto L_0x0175
        L_0x01c5:
            r0 = move-exception
            goto L_0x0164
        */
        throw new UnsupportedOperationException("Method not decompiled: com.contec.phms.device.cms50k.update.Update50KUtils.createUpdateXmlFile(java.util.List):void");
    }

    public static String getFileMD5(File file) {
        if (!file.exists() || !file.isFile()) {
            return null;
        }
        byte[] buffer = new byte[1024];
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            FileInputStream in = new FileInputStream(file);
            while (true) {
                try {
                    int len = in.read(buffer, 0, 1024);
                    if (len == -1) {
                        in.close();
                        return new BigInteger(1, digest.digest()).toString(16);
                    }
                    digest.update(buffer, 0, len);
                } catch (Exception e) {
                    e = e;
                    FileInputStream fileInputStream = in;
                }
            }
        } catch (Exception e2) {
            Exception e = e2;
            e.printStackTrace();
            return null;
        }
    }

    public static byte[] readFileByRandomAccess(String fileName) {
        return null;
    }

    public static byte[] sum_Check(byte[] pack) {
        int CHECK_SUM = 0;
        int _size = pack.length - 1;
        for (int i = 0; i < _size; i++) {
            CHECK_SUM += pack[i] & 255;
        }
        pack[pack.length - 1] = (byte) (CHECK_SUM & 127);
        return pack;
    }

    public static List<Xml50KUpdateBean> getXmlBean(List<Xml50KUpdateBean> list) {
        return null;
    }

    public static byte[] readFile() {
        byte[] data = new byte[42];
        byte[] cmd = new byte[50];
        if (instream != null) {
            cmd[0] = -75;
            try {
                instream.read(data, 0, 42);
            } catch (IOException e) {
                e.printStackTrace();
            }
            for (int i = 1; i < 7; i++) {
                for (int j = 0; j < 7; j++) {
                    cmd[i] = (byte) (cmd[i] | ((data[((i - 1) * 7) + j] & 128) >> (7 - j)));
                }
                cmd[i] = (byte) (cmd[i] & Byte.MAX_VALUE);
            }
            for (int i2 = 0; i2 < 42; i2++) {
                cmd[i2 + 7] = (byte) (data[i2] & Byte.MAX_VALUE);
            }
            for (int i3 = 0; i3 < 49; i3++) {
                cmd[49] = (byte) (cmd[49] + cmd[i3]);
            }
            cmd[49] = (byte) (cmd[49] & Byte.MAX_VALUE);
        }
        return cmd;
    }

    public static byte[] ReadTxtFile(String strFilePath, boolean blag) {
        byte[] back = null;
        File file = new File(strFilePath);
        if (!file.exists()) {
            try {
                file.createNewFile();
                Log.i("info", "我的测试  ===========文件不存在");
                App_phms.preferences.edit().putString("isfinish", "1").commit();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (file.isDirectory()) {
            Log.d("TestFile", "The File doesn't not exist.");
        } else {
            back = TestRead3(file, blag);
        }
        Log.i("info", bs.b);
        return back;
    }

    public static byte[] TestRead3(File file, boolean flag) {
        byte[] string3 = null;
        try {
            instream = new FileInputStream(file);
            ByteArrayOutputStream bos = null;
            byte[] buffer = new byte[32];
            byte[] buffer2 = new byte[42];
            if (!flag) {
                for (int len = -1; len == -1; len = 1) {
                    int len2 = instream.read(buffer);
                    bos = new ByteArrayOutputStream();
                    bos.write(buffer, 0, len2);
                    string3 = doPackIsUpdateCommand(bos.toByteArray());
                }
            } else if (flag) {
                while (flag) {
                    int len22 = instream.read(buffer2);
                    bos = new ByteArrayOutputStream();
                    bos.write(buffer2, 0, len22);
                    byte[] string2 = bos.toByteArray();
                    Log.i("info", "=============" + string2);
                    String bytes2HexString = bytes2HexString(string2);
                    string3 = doPackUpdateData(string2);
                    flag = false;
                }
            }
            instream.close();
            bos.close();
        } catch (Exception e) {
        }
        return string3;
    }

    public static String bytes2HexString(byte[] b) {
        String ret = bs.b;
        for (byte b2 : b) {
            String hex = Integer.toHexString(b2 & 255);
            if (hex.length() == 1) {
                hex = String.valueOf('0') + hex;
            }
            ret = String.valueOf(ret) + hex.toUpperCase() + Constants.DOUHAO;
        }
        int i = 0 + 1;
        Log.i("info", String.valueOf(ret) + "=======" + 0);
        return ret;
    }
}
