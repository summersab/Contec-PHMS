package com.contec.phms.upload.trend;

import android.util.Base64;
import com.contec.phms.device.template.DeviceData;
import org.apache.commons.httpclient.HttpMethodBase;

public class Cms50K_PedometerMin_Trend extends Trend {
    private final String TAG = "Cms50K_PedometerMin_Trend";
    public DeviceData mData;

    public Cms50K_PedometerMin_Trend(DeviceData datas) {
        this.mData = datas;
        getContent();
    }

    public HttpMethodBase getMethod() {
        return this.mMethod;
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r19v0, resolved type: byte} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r19v1, resolved type: byte} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r19v2, resolved type: byte} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r19v3, resolved type: byte} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r19v6, resolved type: byte} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r19v7, resolved type: byte} */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.lang.String makeContect() {
        /*
            r32 = this;
            r26 = 0
            byte[] r26 = (byte[]) r26
            r13 = 8
            r0 = r32
            com.contec.phms.device.template.DeviceData r0 = r0.mData
            r29 = r0
            if (r29 == 0) goto L_0x0353
            r0 = r32
            com.contec.phms.device.template.DeviceData r0 = r0.mData
            r29 = r0
            r0 = r29
            java.util.ArrayList<java.lang.Object> r0 = r0.mDataList
            r29 = r0
            r30 = 0
            java.lang.Object r29 = r29.get(r30)
            if (r29 == 0) goto L_0x0353
            r0 = r32
            com.contec.phms.device.template.DeviceData r0 = r0.mData
            r29 = r0
            r0 = r29
            java.util.ArrayList<java.lang.Object> r0 = r0.mDataList
            r29 = r0
            int r29 = r29.size()
            r0 = r29
            byte r7 = (byte) r0
            r6 = 0
            r19 = 0
        L_0x0038:
            r0 = r19
            if (r0 < r7) goto L_0x0052
            r3 = 0
            int r4 = r6 + 12
            byte[] r0 = new byte[r4]
            r26 = r0
            r19 = 0
        L_0x0045:
            r0 = r19
            if (r0 < r7) goto L_0x0077
        L_0x0049:
            r0 = r32
            r1 = r26
            java.lang.String r29 = r0.encodeBASE64(r1)
            return r29
        L_0x0052:
            r0 = r32
            com.contec.phms.device.template.DeviceData r0 = r0.mData
            r29 = r0
            r0 = r29
            java.util.ArrayList<java.lang.Object> r0 = r0.mDataList
            r29 = r0
            r0 = r29
            r1 = r19
            java.lang.Object r14 = r0.get(r1)
            com.contec.jar.cms50k.MinData r14 = (com.contec.jar.cms50k.MinData) r14
            java.util.List<byte[]> r0 = r14.mMinDataList
            r29 = r0
            int r29 = r29.size()
            int r29 = r29 * 4
            int r6 = r6 + r29
            int r19 = r19 + 1
            goto L_0x0038
        L_0x0077:
            r0 = r32
            com.contec.phms.device.template.DeviceData r0 = r0.mData
            r29 = r0
            r0 = r29
            java.util.ArrayList<java.lang.Object> r0 = r0.mDataList
            r29 = r0
            r0 = r29
            r1 = r19
            java.lang.Object r5 = r0.get(r1)
            com.contec.jar.cms50k.MinData r5 = (com.contec.jar.cms50k.MinData) r5
            r29 = 0
            r26[r29] = r13
            r29 = 1
            r26[r29] = r7
            r29 = 2
            r26[r29] = r3
            r29 = 3
            byte[] r0 = r5.mStartDate
            r30 = r0
            r31 = 0
            byte r30 = r30[r31]
            r26[r29] = r30
            r29 = 4
            byte[] r0 = r5.mStartDate
            r30 = r0
            r31 = 1
            byte r30 = r30[r31]
            r26[r29] = r30
            r29 = 5
            byte[] r0 = r5.mStartDate
            r30 = r0
            r31 = 2
            byte r30 = r30[r31]
            r26[r29] = r30
            r29 = 6
            byte[] r0 = r5.mStartDate
            r30 = r0
            r31 = 3
            byte r30 = r30[r31]
            r26[r29] = r30
            r29 = 7
            r30 = 0
            r26[r29] = r30
            r23 = 8
            r10 = 0
            r9 = 0
            r21 = 0
        L_0x00d5:
            java.util.List<byte[]> r0 = r5.mMinDataList
            r29 = r0
            int r29 = r29.size()
            r0 = r21
            r1 = r29
            if (r0 < r1) goto L_0x024f
            int r24 = r23 + 1
            int r29 = r9 >> 8
            r0 = r29
            r0 = r0 & 255(0xff, float:3.57E-43)
            r29 = r0
            r0 = r29
            byte r0 = (byte) r0
            r29 = r0
            r26[r23] = r29
            int r23 = r24 + 1
            r0 = r9 & 255(0xff, float:3.57E-43)
            r29 = r0
            r0 = r29
            byte r0 = (byte) r0
            r29 = r0
            r26[r24] = r29
            int r24 = r23 + 1
            int r29 = r10 >> 8
            r0 = r29
            r0 = r0 & 255(0xff, float:3.57E-43)
            r29 = r0
            r0 = r29
            byte r0 = (byte) r0
            r29 = r0
            r26[r23] = r29
            int r23 = r24 + 1
            r0 = r10 & 255(0xff, float:3.57E-43)
            r29 = r0
            r0 = r29
            byte r0 = (byte) r0
            r29 = r0
            r26[r24] = r29
            byte[] r0 = r5.mStartDate
            r29 = r0
            r30 = 0
            byte r28 = r29[r30]
            byte[] r0 = r5.mStartDate
            r29 = r0
            r30 = 1
            byte r22 = r29[r30]
            byte[] r0 = r5.mStartDate
            r29 = r0
            r30 = 2
            byte r17 = r29[r30]
            byte[] r0 = r5.mStartDate
            r29 = r0
            r30 = 3
            byte r18 = r29[r30]
            java.lang.StringBuilder r15 = new java.lang.StringBuilder
            r15.<init>()
            java.lang.StringBuilder r29 = new java.lang.StringBuilder
            java.lang.String r30 = "20"
            r29.<init>(r30)
            r0 = r29
            r1 = r28
            java.lang.StringBuilder r29 = r0.append(r1)
            java.lang.String r30 = "-"
            java.lang.StringBuilder r29 = r29.append(r30)
            java.lang.String r29 = r29.toString()
            r0 = r29
            r15.append(r0)
            r29 = 10
            r0 = r22
            r1 = r29
            if (r0 >= r1) goto L_0x032b
            java.lang.StringBuilder r29 = new java.lang.StringBuilder
            java.lang.String r30 = "0"
            r29.<init>(r30)
            r0 = r29
            r1 = r22
            java.lang.StringBuilder r29 = r0.append(r1)
            java.lang.String r30 = "-"
            java.lang.StringBuilder r29 = r29.append(r30)
            java.lang.String r29 = r29.toString()
            r0 = r29
            r15.append(r0)
        L_0x0188:
            r29 = 10
            r0 = r17
            r1 = r29
            if (r0 >= r1) goto L_0x0345
            java.lang.StringBuilder r29 = new java.lang.StringBuilder
            java.lang.String r30 = "0"
            r29.<init>(r30)
            r0 = r29
            r1 = r17
            java.lang.StringBuilder r29 = r0.append(r1)
            java.lang.String r29 = r29.toString()
            r0 = r29
            r15.append(r0)
        L_0x01a8:
            java.lang.String r29 = " "
            r0 = r29
            r15.append(r0)
            r29 = 10
            r0 = r18
            r1 = r29
            if (r0 >= r1) goto L_0x034c
            java.lang.StringBuilder r29 = new java.lang.StringBuilder
            java.lang.String r30 = "0"
            r29.<init>(r30)
            r0 = r29
            r1 = r18
            java.lang.StringBuilder r29 = r0.append(r1)
            java.lang.String r29 = r29.toString()
            r0 = r29
            r15.append(r0)
        L_0x01cf:
            java.lang.String r27 = r15.toString()
            com.contec.phms.App_phms r29 = com.contec.phms.App_phms.getInstance()
            android.content.Context r29 = r29.getApplicationContext()
            com.contec.phms.db.localdata.opration.PedometerMinDataDaoOperation r25 = com.contec.phms.db.localdata.opration.PedometerMinDataDaoOperation.getInstance(r29)
            r0 = r25
            r1 = r27
            boolean r29 = r0.querySql(r1)
            java.lang.Boolean r20 = java.lang.Boolean.valueOf(r29)
            boolean r29 = r20.booleanValue()
            if (r29 != 0) goto L_0x024b
            com.contec.phms.db.localdata.PedometerMinDataDao r16 = new com.contec.phms.db.localdata.PedometerMinDataDao
            r16.<init>()
            r0 = r27
            r1 = r16
            r1.mTime = r0
            java.lang.StringBuilder r29 = new java.lang.StringBuilder
            java.lang.String r30 = java.lang.String.valueOf(r9)
            r29.<init>(r30)
            java.lang.String r29 = r29.toString()
            r0 = r29
            r1 = r16
            r1.mCal = r0
            java.lang.String r29 = ""
            r0 = r29
            r1 = r16
            r1.mCaltarget = r0
            java.lang.StringBuilder r29 = new java.lang.StringBuilder
            java.lang.String r30 = java.lang.String.valueOf(r10)
            r29.<init>(r30)
            java.lang.String r29 = r29.toString()
            r0 = r29
            r1 = r16
            r1.mSteps = r0
            r0 = r32
            com.contec.phms.device.template.DeviceData r0 = r0.mData
            r29 = r0
            r0 = r29
            java.lang.String r0 = r0.mFileName
            r29 = r0
            r0 = r29
            r1 = r16
            r1.mUnique = r0
            java.lang.String r29 = "0"
            r0 = r29
            r1 = r16
            r1.mFlag = r0
            r0 = r25
            r1 = r16
            r0.insertPedometerMinDao(r1)
        L_0x024b:
            int r19 = r19 + 1
            goto L_0x0045
        L_0x024f:
            java.util.List<byte[]> r0 = r5.mMinDataList
            r29 = r0
            r0 = r29
            r1 = r21
            java.lang.Object r11 = r0.get(r1)
            byte[] r11 = (byte[]) r11
            java.util.List<byte[]> r0 = r5.mMinDataList
            r29 = r0
            r0 = r29
            r1 = r21
            java.lang.Object r29 = r0.get(r1)
            byte[] r29 = (byte[]) r29
            r0 = r29
            int r0 = r0.length
            r29 = r0
            r0 = r29
            byte[] r12 = new byte[r0]
            java.util.List<byte[]> r0 = r5.mMinDataList
            r29 = r0
            r0 = r29
            r1 = r21
            java.lang.Object r12 = r0.get(r1)
            byte[] r12 = (byte[]) r12
            r29 = 1
            byte r29 = r12[r29]
            r0 = r29
            r0 = r0 & 255(0xff, float:3.57E-43)
            r29 = r0
            int r29 = r29 << 8
            r30 = 0
            byte r30 = r12[r30]
            r0 = r30
            r0 = r0 & 255(0xff, float:3.57E-43)
            r30 = r0
            r2 = r29 | r30
            r29 = 3
            byte r29 = r12[r29]
            r0 = r29
            r0 = r0 & 255(0xff, float:3.57E-43)
            r29 = r0
            int r29 = r29 << 8
            r30 = 2
            byte r30 = r12[r30]
            r0 = r30
            r0 = r0 & 255(0xff, float:3.57E-43)
            r30 = r0
            r8 = r29 | r30
            r29 = 65535(0xffff, float:9.1834E-41)
            r0 = r29
            if (r2 == r0) goto L_0x02c0
            r29 = 65535(0xffff, float:9.1834E-41)
            r0 = r29
            if (r8 != r0) goto L_0x0308
        L_0x02c0:
            r2 = 0
            r8 = 0
            int r24 = r23 + 1
            r29 = 0
            byte r29 = r11[r29]
            r26[r23] = r29
            int r23 = r24 + 1
            r29 = 1
            byte r29 = r11[r29]
            r26[r24] = r29
            int r24 = r23 + 1
            r29 = 2
            byte r29 = r11[r29]
            r26[r23] = r29
            int r23 = r24 + 1
            r29 = 3
            byte r29 = r11[r29]
            r26[r24] = r29
        L_0x02e2:
            java.lang.String r29 = "wsd1"
            java.lang.StringBuilder r30 = new java.lang.StringBuilder
            java.lang.String r31 = "cal_"
            r30.<init>(r31)
            r0 = r30
            java.lang.StringBuilder r30 = r0.append(r2)
            java.lang.String r31 = "  step:"
            java.lang.StringBuilder r30 = r30.append(r31)
            r0 = r30
            java.lang.StringBuilder r30 = r0.append(r8)
            java.lang.String r30 = r30.toString()
            android.util.Log.e(r29, r30)
            int r21 = r21 + 1
            goto L_0x00d5
        L_0x0308:
            int r24 = r23 + 1
            r29 = 1
            byte r29 = r11[r29]
            r26[r23] = r29
            int r23 = r24 + 1
            r29 = 0
            byte r29 = r11[r29]
            r26[r24] = r29
            int r24 = r23 + 1
            r29 = 3
            byte r29 = r11[r29]
            r26[r23] = r29
            int r23 = r24 + 1
            r29 = 2
            byte r29 = r11[r29]
            r26[r24] = r29
            int r9 = r9 + r2
            int r10 = r10 + r8
            goto L_0x02e2
        L_0x032b:
            java.lang.StringBuilder r29 = new java.lang.StringBuilder
            java.lang.String r30 = java.lang.String.valueOf(r22)
            r29.<init>(r30)
            java.lang.String r30 = "-"
            java.lang.StringBuilder r29 = r29.append(r30)
            java.lang.String r29 = r29.toString()
            r0 = r29
            r15.append(r0)
            goto L_0x0188
        L_0x0345:
            r0 = r17
            r15.append(r0)
            goto L_0x01a8
        L_0x034c:
            r0 = r18
            r15.append(r0)
            goto L_0x01cf
        L_0x0353:
            r29 = 1
            r0 = r29
            byte[] r0 = new byte[r0]
            r26 = r0
            r29 = 0
            r30 = 0
            r26[r29] = r30
            goto L_0x0049
        */
        throw new UnsupportedOperationException("Method not decompiled: com.contec.phms.upload.trend.Cms50K_PedometerMin_Trend.makeContect():java.lang.String");
    }

    public String encodeBASE64(byte[] pack) {
        return Base64.encodeToString(pack, 0);
    }
}
