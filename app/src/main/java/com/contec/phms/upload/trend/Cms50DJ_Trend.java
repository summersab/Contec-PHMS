package com.contec.phms.upload.trend;

import android.util.Base64;
import com.contec.phms.App_phms;
import com.contec.phms.device.template.DeviceData;
import com.contec.phms.db.localdata.Spo2DataDao;
import com.contec.phms.db.localdata.opration.Spo2DataDaoOperation;
import com.contec.phms.util.CLog;
import com.contec.phms.util.PageUtil;
import org.apache.commons.httpclient.HttpMethodBase;

public class Cms50DJ_Trend extends Trend {
    private final String TAG = "SpO208Trend";
    public DeviceData mData;

    public Cms50DJ_Trend(DeviceData datas) {
        this.mData = datas;
        getContent();
    }

    public HttpMethodBase getMethod() {
        return this.mMethod;
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r18v0, resolved type: byte} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r18v1, resolved type: byte} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r18v3, resolved type: byte} */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.lang.String makeContect() {
        /*
            r22 = this;
            r20 = 0
            byte[] r20 = (byte[]) r20
            r14 = 1
            r0 = r22
            com.contec.phms.device.template.DeviceData r2 = r0.mData
            if (r2 == 0) goto L_0x01fa
            r0 = r22
            com.contec.phms.device.template.DeviceData r2 = r0.mData
            java.util.ArrayList<java.lang.Object> r2 = r2.mDataList
            r3 = 0
            java.lang.Object r2 = r2.get(r3)
            if (r2 == 0) goto L_0x01fa
            r0 = r22
            com.contec.phms.device.template.DeviceData r2 = r0.mData
            java.util.ArrayList<java.lang.Object> r2 = r2.mDataList
            r3 = 0
            java.lang.Object r9 = r2.get(r3)
            com.contec.cms50dj_jar.DeviceData50DJ_Jar r9 = (com.contec.cms50dj_jar.DeviceData50DJ_Jar) r9
            java.util.List r2 = r9.getmSp02DataList()
            int r2 = r2.size()
            byte r11 = (byte) r2
            r8 = 8
            int r2 = r11 * r8
            int r10 = r2 + 3
            byte[] r0 = new byte[r10]
            r20 = r0
            r2 = 0
            r20[r2] = r14
            r2 = 1
            r20[r2] = r11
            r2 = 2
            r20[r2] = r8
            r16 = 3
            r18 = 0
            r17 = r16
        L_0x0047:
            r0 = r18
            if (r0 < r11) goto L_0x0064
            if (r11 == 0) goto L_0x0054
            r0 = r22
            r1 = r20
            r0.doPack(r1)
        L_0x0054:
            java.lang.String r2 = "************************>>>>>>>>>>>>>>>>>>>>>***********************************"
            java.lang.String r3 = "Cms50D_BT"
            com.contec.phms.util.FileOperation.writeToSDCard(r2, r3)
            r0 = r22
            r1 = r20
            java.lang.String r2 = r0.encodeBASE64(r1)
            return r2
        L_0x0064:
            com.contec.phms.manager.device.DeviceBeanList r2 = com.contec.phms.manager.device.DeviceManager.mDeviceBeanList
            if (r2 == 0) goto L_0x0093
            com.contec.phms.manager.device.DeviceBean r2 = com.contec.phms.manager.device.DeviceManager.m_DeviceBean
            int r3 = r18 / r11
            int r3 = r3 * 100
            r2.mProgress = r3
            com.contec.phms.App_phms r2 = com.contec.phms.App_phms.getInstance()
            de.greenrobot.event.EventBusPostOnBackGround r2 = r2.mEventBusPostOnBackGround
            com.contec.phms.manager.device.DeviceBean r3 = com.contec.phms.manager.device.DeviceManager.m_DeviceBean
            r2.postInMainThread(r3)
            java.lang.String r2 = "SpO208Trend"
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            java.lang.String r4 = "SpO2上传进度百分比："
            r3.<init>(r4)
            int r4 = r18 / r11
            int r4 = r4 * 100
            java.lang.StringBuilder r3 = r3.append(r4)
            java.lang.String r3 = r3.toString()
            com.contec.phms.util.CLog.i(r2, r3)
        L_0x0093:
            java.util.List r2 = r9.getmSp02DataList()
            r0 = r18
            java.lang.Object r19 = r2.get(r0)
            byte[] r19 = (byte[]) r19
            r2 = 0
            byte r2 = r19[r2]
            r3 = 1
            byte r3 = r19[r3]
            r4 = 2
            byte r4 = r19[r4]
            r5 = 3
            byte r5 = r19[r5]
            r6 = 4
            byte r6 = r19[r6]
            r7 = 5
            byte r7 = r19[r7]
            java.lang.String r12 = com.contec.phms.util.PageUtil.getDateFormByte(r2, r3, r4, r5, r6, r7)
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            java.lang.String r3 = "上传*******数据时间："
            r2.<init>(r3)
            java.lang.StringBuilder r2 = r2.append(r12)
            java.lang.String r2 = r2.toString()
            java.lang.String r3 = "Cms50D_BT"
            com.contec.phms.util.FileOperation.writeToSDCard(r2, r3)
            java.lang.String r2 = "SpO208Trend"
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            java.lang.String r4 = "判断时间是否合法："
            r3.<init>(r4)
            java.lang.StringBuilder r3 = r3.append(r12)
            java.lang.String r3 = r3.toString()
            com.contec.phms.util.CLog.dT(r2, r3)
            byte[] r21 = com.contec.phms.util.PageUtil.compareDate((java.lang.String) r12)
            if (r21 == 0) goto L_0x0131
            r2 = 0
            r3 = 0
            byte r3 = r21[r3]
            r19[r2] = r3
            r2 = 1
            r3 = 1
            byte r3 = r21[r3]
            r19[r2] = r3
            r2 = 2
            r3 = 2
            byte r3 = r21[r3]
            r19[r2] = r3
            r2 = 3
            r3 = 3
            byte r3 = r21[r3]
            r19[r2] = r3
            r2 = 4
            r3 = 4
            byte r3 = r21[r3]
            r19[r2] = r3
            r2 = 5
            r3 = 5
            byte r3 = r21[r3]
            r19[r2] = r3
            r2 = 0
            byte r2 = r19[r2]
            r3 = 1
            byte r3 = r19[r3]
            r4 = 2
            byte r4 = r19[r4]
            r5 = 3
            byte r5 = r19[r5]
            r6 = 4
            byte r6 = r19[r6]
            r7 = 5
            byte r7 = r19[r7]
            java.lang.String r12 = com.contec.phms.util.PageUtil.getDateFormByte(r2, r3, r4, r5, r6, r7)
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            java.lang.String r3 = "上传*****时间不合法骄正之后的数据时间："
            r2.<init>(r3)
            java.lang.StringBuilder r2 = r2.append(r12)
            java.lang.String r2 = r2.toString()
            java.lang.String r3 = "Cms50D_BT"
            com.contec.phms.util.FileOperation.writeToSDCard(r2, r3)
        L_0x0131:
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            java.lang.String r3 = "year:"
            r2.<init>(r3)
            r3 = 0
            byte r3 = r19[r3]
            java.lang.StringBuilder r2 = r2.append(r3)
            java.lang.String r3 = " month:"
            java.lang.StringBuilder r2 = r2.append(r3)
            r3 = 1
            byte r3 = r19[r3]
            java.lang.StringBuilder r2 = r2.append(r3)
            java.lang.String r3 = "  day:"
            java.lang.StringBuilder r2 = r2.append(r3)
            r3 = 2
            byte r3 = r19[r3]
            java.lang.StringBuilder r2 = r2.append(r3)
            java.lang.String r3 = " hour:"
            java.lang.StringBuilder r2 = r2.append(r3)
            r3 = 3
            byte r3 = r19[r3]
            java.lang.StringBuilder r2 = r2.append(r3)
            java.lang.String r3 = "  min:"
            java.lang.StringBuilder r2 = r2.append(r3)
            r3 = 4
            byte r3 = r19[r3]
            java.lang.StringBuilder r2 = r2.append(r3)
            java.lang.String r3 = "  second:"
            java.lang.StringBuilder r2 = r2.append(r3)
            r3 = 5
            byte r3 = r19[r3]
            java.lang.StringBuilder r2 = r2.append(r3)
            java.lang.String r3 = "  spo:"
            java.lang.StringBuilder r2 = r2.append(r3)
            r3 = 6
            byte r3 = r19[r3]
            java.lang.StringBuilder r2 = r2.append(r3)
            java.lang.String r3 = "  pluse:"
            java.lang.StringBuilder r2 = r2.append(r3)
            r3 = 7
            byte r3 = r19[r3]
            java.lang.StringBuilder r2 = r2.append(r3)
            java.lang.String r15 = r2.toString()
            java.lang.String r2 = "SpO208Trend"
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            java.lang.String r4 = "血氧数据："
            r3.<init>(r4)
            java.lang.StringBuilder r3 = r3.append(r15)
            java.lang.String r3 = r3.toString()
            com.contec.phms.util.CLog.e(r2, r3)
            java.util.List r2 = r9.getmSp02DataList()
            r0 = r18
            java.lang.Object r13 = r2.get(r0)
            byte[] r13 = (byte[]) r13
            int r16 = r17 + 1
            r2 = 0
            byte r2 = r13[r2]
            r20[r17] = r2
            int r17 = r16 + 1
            r2 = 1
            byte r2 = r13[r2]
            r20[r16] = r2
            int r16 = r17 + 1
            r2 = 2
            byte r2 = r13[r2]
            r20[r17] = r2
            int r17 = r16 + 1
            r2 = 3
            byte r2 = r13[r2]
            r20[r16] = r2
            int r16 = r17 + 1
            r2 = 4
            byte r2 = r13[r2]
            r20[r17] = r2
            int r17 = r16 + 1
            r2 = 5
            byte r2 = r13[r2]
            r20[r16] = r2
            int r16 = r17 + 1
            r2 = 6
            byte r2 = r13[r2]
            r20[r17] = r2
            int r17 = r16 + 1
            r2 = 7
            byte r2 = r13[r2]
            r20[r16] = r2
            int r18 = r18 + 1
            goto L_0x0047
        L_0x01fa:
            r2 = 1
            byte[] r0 = new byte[r2]
            r20 = r0
            r2 = 0
            r3 = 0
            r20[r2] = r3
            java.lang.String r2 = "*****************************"
            java.lang.String r3 = "**pack**pack***pack**pack*********"
            com.contec.phms.util.CLog.e(r2, r3)
            goto L_0x0054
        */
        throw new UnsupportedOperationException("Method not decompiled: com.contec.phms.upload.trend.Cms50DJ_Trend.makeContect():java.lang.String");
    }

    public String encodeBASE64(byte[] pack) {
        return Base64.encodeToString(pack, 0);
    }

    public void doPack(byte[] pack) {
        int count = pack.length / 8;
        for (int i = 0; i < count; i++) {
            int index = (i * 8) + 3;
            String dateTime = PageUtil.process_Date(pack[index] + 2000, pack[index + 1], pack[index + 2], pack[index + 3], pack[index + 4], pack[index + 5]);
            byte spo2 = pack[index + 6];
            byte pr = pack[index + 7];
            Spo2DataDaoOperation operation = Spo2DataDaoOperation.getInstance(App_phms.getInstance().getApplicationContext());
            if (!Boolean.valueOf(operation.querySql(dateTime)).booleanValue()) {
                Spo2DataDao data = new Spo2DataDao();
                data.mTime = dateTime;
                data.mSpo2 = new StringBuilder(String.valueOf(spo2)).toString();
                data.mPr = new StringBuilder(String.valueOf(pr)).toString();
                data.mUnique = this.mData.mFileName;
                data.mFlag = "0";
                operation.insertSpo2DataDao(data);
            }
            CLog.e("SpO208TrendgotU", "The uploaded blood oxygen data is: " + spo2 + "_" + pr + " time: " + dateTime);
        }
    }
}
