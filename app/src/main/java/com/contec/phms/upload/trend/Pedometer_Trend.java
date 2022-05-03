package com.contec.phms.upload.trend;

import android.content.Context;
import android.os.Message;
import android.util.Base64;
import com.contec.phms.App_phms;
import com.contec.phms.R;
import com.contec.phms.eventbus.EventFragment;
import com.contec.phms.db.HistoryDao;
import com.contec.phms.db.LoginUserDao;
import com.contec.phms.db.PedometerHistoryDao;
import com.contec.phms.util.CLog;
import com.contec.phms.util.Constants;
import com.contec.phms.util.PageUtil;
import java.util.List;
import cn.com.contec_net_3_android.Method_android_upload_trend;

public class Pedometer_Trend extends Trend {
    private final String TAG = getClass().getSimpleName();
    private Context mContext;
    private List<PedometerHistoryDao> mList;

    public Pedometer_Trend(Context pContext, List<PedometerHistoryDao> plist) {
        this.mContext = pContext;
        this.mList = plist;
        getContent();
        upload();
    }

    private boolean uploadTrend(String _content) {
        LoginUserDao _userinfo = PageUtil.getLoginUserInfo();
        try {
            String _code = Method_android_upload_trend.upLoadThrendTwo(_userinfo.mSID, _userinfo.mUID, App_phms.getInstance().mUserInfo.mPassword, _content, String.valueOf(Constants.URL) + "/main.php").substring(34, 40);
            if (_code.equals(Constants.SUCCESS)) {
                return true;
            }
            if (_code.equals(Constants.LOGIN_IN_ANOTHER_PLACE)) {
                Message msgs = new Message();
                msgs.what = Constants.Login_In_Another_Place;
                msgs.arg2 = 1;
                App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(msgs);
            } else if (!_code.equals(Constants.CARD_USE_NUM_EXPIRED)) {
                _code.equals(Constants.PASSED_VALIDITY);
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean upload() {
        boolean _request = false;
        if (this.mList.size() > 0) {
            _request = uploadTrend(this.mContent);
            if (_request) {
                int i = 0;
                while (i < this.mList.size()) {
                    try {
                        PedometerHistoryDao _Dao = this.mList.get(i);
                        HistoryDao mHistoryDao = new HistoryDao();
                        mHistoryDao.setContent(this.mContext.getResources().getString(R.string.str_upload_pedometer_content));
                        mHistoryDao.setDate(_Dao.getmUploadDate());
                        mHistoryDao.setUser(App_phms.getInstance().mUserInfo.mUserID);
                        App_phms.getInstance().mHelper.getHistoryDao().create(mHistoryDao);
                        App_phms.getInstance().mHelper.getPedometerhistoryDao().delete(_Dao);
                        i++;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                CLog.d(this.TAG, "计步器数据上传成功.........");
                EventFragment _fragment = new EventFragment();
                _fragment.setmWhichCommand(2);
                _fragment.setmProgress(100);
                App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(_fragment);
            } else {
                int i2 = 0;
                while (i2 < this.mList.size()) {
                    try {
                        PedometerHistoryDao _Dao2 = this.mList.get(i2);
                        _Dao2.setmUserID(App_phms.getInstance().mUserInfo.mUserID);
                        App_phms.getInstance().mHelper.getPedometerhistoryDao().update(_Dao2);
                        i2++;
                    } catch (Exception e2) {
                        e2.printStackTrace();
                    }
                }
                EventFragment _fragment2 = new EventFragment();
                _fragment2.setmWhichCommand(2);
                _fragment2.setmProgress(110);
                App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(_fragment2);
            }
        }
        return _request;
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r21v0, resolved type: byte} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r21v1, resolved type: byte} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v9, resolved type: byte} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v1, resolved type: byte} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v63, resolved type: byte} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v8, resolved type: byte} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r21v3, resolved type: byte} */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.lang.String makeContect() {
        /*
            r27 = this;
            r22 = 0
            byte[] r22 = (byte[]) r22
            r15 = 8
            r0 = r27
            java.util.List<com.contec.phms.db.PedometerHistoryDao> r0 = r0.mList
            r24 = r0
            if (r24 == 0) goto L_0x0221
            r0 = r27
            java.util.List<com.contec.phms.db.PedometerHistoryDao> r0 = r0.mList
            r24 = r0
            int r24 = r24.size()
            r0 = r24
            byte r12 = (byte) r0
            r3 = 0
            int r24 = r12 * r3
            int r24 = r24 + 3
            int r7 = r24 + 4
            byte[] r0 = new byte[r7]
            r22 = r0
            r24 = 0
            r22[r24] = r15
            r24 = 1
            r22[r24] = r12
            r24 = 2
            r25 = 0
            r22[r24] = r25
            r19 = 3
            r24 = 80
            int r11 = r24 / r12
            r23 = 0
            r17 = 0
            r21 = 0
            r20 = r19
        L_0x0042:
            r0 = r21
            if (r0 < r12) goto L_0x008f
            int r19 = r20 + 1
            int r24 = r17 >> 8
            r0 = r24
            r0 = r0 & 255(0xff, float:3.57E-43)
            r24 = r0
            r0 = r24
            byte r0 = (byte) r0
            r24 = r0
            r22[r20] = r24
            int r20 = r19 + 1
            r0 = r17
            r0 = r0 & 255(0xff, float:3.57E-43)
            r24 = r0
            r0 = r24
            byte r0 = (byte) r0
            r24 = r0
            r22[r19] = r24
            int r19 = r20 + 1
            int r24 = r23 >> 8
            r0 = r24
            r0 = r0 & 255(0xff, float:3.57E-43)
            r24 = r0
            r0 = r24
            byte r0 = (byte) r0
            r24 = r0
            r22[r20] = r24
            int r20 = r19 + 1
            r0 = r23
            r0 = r0 & 255(0xff, float:3.57E-43)
            r24 = r0
            r0 = r24
            byte r0 = (byte) r0
            r24 = r0
            r22[r19] = r24
        L_0x0086:
            r0 = r27
            r1 = r22
            java.lang.String r24 = r0.encodeBASE64(r1)
            return r24
        L_0x008f:
            r0 = r27
            java.util.List<com.contec.phms.db.PedometerHistoryDao> r0 = r0.mList
            r24 = r0
            r0 = r24
            r1 = r21
            java.lang.Object r10 = r0.get(r1)
            com.contec.phms.db.PedometerHistoryDao r10 = (com.contec.phms.db.PedometerHistoryDao) r10
            java.lang.String r24 = "more"
            java.lang.StringBuilder r25 = new java.lang.StringBuilder
            java.lang.String r26 = "makecontent  _pedomterData list = "
            r25.<init>(r26)
            java.lang.String r26 = r10.toString()
            java.lang.StringBuilder r25 = r25.append(r26)
            java.lang.String r25 = r25.toString()
            com.contec.phms.util.CLog.i(r24, r25)
            java.lang.String r14 = r10.getmStartTime()
            r24 = 2
            r25 = 4
            r0 = r24
            r1 = r25
            java.lang.String r24 = r14.substring(r0, r1)
            java.lang.String r24 = r24.toString()
            int r24 = java.lang.Integer.parseInt(r24)
            r0 = r24
            byte r0 = (byte) r0
            r16 = r0
            r24 = 5
            r25 = 7
            r0 = r24
            r1 = r25
            java.lang.String r24 = r14.substring(r0, r1)
            java.lang.String r24 = r24.toString()
            int r24 = java.lang.Integer.parseInt(r24)
            r0 = r24
            byte r9 = (byte) r0
            r24 = 8
            r25 = 10
            r0 = r24
            r1 = r25
            java.lang.String r24 = r14.substring(r0, r1)
            java.lang.String r24 = r24.toString()
            int r24 = java.lang.Integer.parseInt(r24)
            r0 = r24
            byte r4 = (byte) r0
            r24 = 11
            r25 = 13
            r0 = r24
            r1 = r25
            java.lang.String r24 = r14.substring(r0, r1)
            java.lang.String r24 = r24.toString()
            int r24 = java.lang.Integer.parseInt(r24)
            r0 = r24
            byte r6 = (byte) r0
            r24 = 14
            r25 = 16
            r0 = r24
            r1 = r25
            java.lang.String r24 = r14.substring(r0, r1)
            java.lang.String r24 = r24.toString()
            int r24 = java.lang.Integer.parseInt(r24)
            r0 = r24
            byte r8 = (byte) r0
            java.lang.String r24 = "more"
            java.lang.StringBuilder r25 = new java.lang.StringBuilder
            java.lang.String r26 = "makecontent  _pedomterData time = "
            r25.<init>(r26)
            r0 = r25
            r1 = r16
            java.lang.StringBuilder r25 = r0.append(r1)
            java.lang.String r26 = "  "
            java.lang.StringBuilder r25 = r25.append(r26)
            r0 = r25
            java.lang.StringBuilder r25 = r0.append(r9)
            java.lang.String r26 = "  "
            java.lang.StringBuilder r25 = r25.append(r26)
            r0 = r25
            java.lang.StringBuilder r25 = r0.append(r4)
            java.lang.String r26 = "  "
            java.lang.StringBuilder r25 = r25.append(r26)
            r0 = r25
            java.lang.StringBuilder r25 = r0.append(r6)
            java.lang.String r26 = "  "
            java.lang.StringBuilder r25 = r25.append(r26)
            r0 = r25
            java.lang.StringBuilder r25 = r0.append(r8)
            java.lang.String r25 = r25.toString()
            com.contec.phms.util.CLog.i(r24, r25)
            int r19 = r20 + 1
            r22[r20] = r16
            int r20 = r19 + 1
            r22[r19] = r9
            int r19 = r20 + 1
            r22[r20] = r4
            int r20 = r19 + 1
            r22[r19] = r6
            int r19 = r20 + 1
            r22[r20] = r8
            int r13 = r10.getmStep()
            int r2 = r10.getmCalories()
            int r20 = r19 + 1
            int r24 = r2 >> 8
            r0 = r24
            r0 = r0 & 255(0xff, float:3.57E-43)
            r24 = r0
            r0 = r24
            byte r0 = (byte) r0
            r24 = r0
            r22[r19] = r24
            int r19 = r20 + 1
            r0 = r2 & 255(0xff, float:3.57E-43)
            r24 = r0
            r0 = r24
            byte r0 = (byte) r0
            r24 = r0
            r22[r20] = r24
            int r20 = r19 + 1
            int r24 = r13 >> 8
            r0 = r24
            r0 = r0 & 255(0xff, float:3.57E-43)
            r24 = r0
            r0 = r24
            byte r0 = (byte) r0
            r24 = r0
            r22[r19] = r24
            int r19 = r20 + 1
            r0 = r13 & 255(0xff, float:3.57E-43)
            r24 = r0
            r0 = r24
            byte r0 = (byte) r0
            r24 = r0
            r22[r20] = r24
            com.contec.phms.eventbus.EventFragment r5 = new com.contec.phms.eventbus.EventFragment
            r5.<init>()
            r24 = 2
            r0 = r24
            r5.setmWhichCommand(r0)
            int r24 = r21 + 1
            int r24 = r24 * r11
            int r24 = r24 + 10
            r0 = r24
            r5.setmProgress(r0)
            com.contec.phms.App_phms r24 = com.contec.phms.App_phms.getInstance()     // Catch:{ Exception -> 0x021c }
            r0 = r24
            de.greenrobot.event.EventBus r0 = r0.mEventBus     // Catch:{ Exception -> 0x021c }
            r24 = r0
            r0 = r24
            r0.post(r5)     // Catch:{ Exception -> 0x021c }
        L_0x01f6:
            r5 = 0
            r0 = r27
            java.util.List<com.contec.phms.db.PedometerHistoryDao> r0 = r0.mList
            r24 = r0
            int r24 = r24.size()
            int r24 = r24 + -1
            r0 = r21
            r1 = r24
            if (r0 != r1) goto L_0x0216
            int r23 = r10.getmSumStep()
            float r24 = r10.getmSumCalories()
            r0 = r24
            int r0 = (int) r0
            r17 = r0
        L_0x0216:
            int r21 = r21 + 1
            r20 = r19
            goto L_0x0042
        L_0x021c:
            r18 = move-exception
            r18.printStackTrace()
            goto L_0x01f6
        L_0x0221:
            r24 = 1
            r0 = r24
            byte[] r0 = new byte[r0]
            r22 = r0
            r24 = 0
            r25 = 0
            r22[r24] = r25
            goto L_0x0086
        */
        throw new UnsupportedOperationException("Method not decompiled: com.contec.phms.upload.trend.Pedometer_Trend.makeContect():java.lang.String");
    }

    public String encodeBASE64(byte[] pack) {
        return Base64.encodeToString(pack, 0);
    }
}
