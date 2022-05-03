package com.contec.phms.activity;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Message;
import android.widget.RemoteViews;
import com.contec.phms.App_phms;
import com.contec.phms.R;
import com.contec.phms.infos.UserInfo;
import com.contec.phms.db.PedometerSumStepKm;
import com.contec.phms.util.CLog;
import com.contec.phms.util.Constants;
import com.contec.phms.util.PedometerSharepreferance;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import u.aly.bs;

public class CustomNotification {
    private static CustomNotification mCustomNotification;
    private final String TAG;
    private Context mContext;
    public Notification mNotification;
    private NotificationManager mNotificationManager;
    private PedometerSharepreferance mPedometerSharepreferance;
    boolean showNotification;

    public static CustomNotification getInstance(Context pContext) {
        if (mCustomNotification == null) {
            mCustomNotification = new CustomNotification(pContext);
        }
        return mCustomNotification;
    }

    private CustomNotification(Context pContext) {
        this.TAG = "CustomNotification";
        this.showNotification = false;
        this.showNotification = false;
        this.mContext = pContext;
        this.mPedometerSharepreferance = new PedometerSharepreferance(this.mContext);
        App_phms.getInstance().mEventBus.register(this);
        String tickerText = pContext.getString(R.string.str_upload_pedometer_content_ch);
        long when = System.currentTimeMillis();
        this.mNotification = new Notification();
        this.mNotification.icon = R.drawable.img_phms_icon_small;
        this.mNotification.tickerText = tickerText;
        this.mNotification.when = when;
        this.mNotification.flags = 32;
        this.mNotificationManager = (NotificationManager) this.mContext.getSystemService("notification");
    }

    public void showNotification(int pSteps, UserInfo pUserinfo) {
        String _name = bs.b;
        String _userId = bs.b;
        if (pUserinfo != null) {
            if (pUserinfo.mUserName != null && !pUserinfo.mUserName.equals(bs.b)) {
                _name = String.valueOf(pUserinfo.mUserName) + this.mContext.getString(R.string.pedometer_notifi_str);
            } else if (pUserinfo.mUserID != null && !pUserinfo.mUserID.equals(bs.b)) {
                _name = String.valueOf(pUserinfo.mUserID.substring(pUserinfo.mUserID.length() - 4, pUserinfo.mUserID.length())) + this.mContext.getString(R.string.pedometer_notifi_str);
            }
            _userId = pUserinfo.mUserID;
        }
        int mStepTarget = this.mPedometerSharepreferance.getTarget();
        int mSumSteps = 0;
        try {
            List<PedometerSumStepKm> _dao = App_phms.getInstance().mHelper.getPedometerSumStepKmDao().queryBuilder().where().eq("Date", new SimpleDateFormat("yyyy-MM-dd").format(new Date())).and().eq("UserID", _userId).query();
            if (_dao != null && _dao.size() > 0) {
                mSumSteps = _dao.get(0).getmSumStep();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        RemoteViews rv = new RemoteViews(this.mContext.getPackageName(), R.layout.layout_custom_notifaction);
        int _sumSteps = mSumSteps + pSteps;
        int _per = (_sumSteps * 100) / mStepTarget;
        if (_name.equals(bs.b)) {
            _name = this.mContext.getString(R.string.pedometer_notifi_step_str);
        }
        rv.setTextViewText(R.id.tv_rv, _name);
        rv.setTextViewText(R.id.runed, new StringBuilder().append(_sumSteps).toString());
        rv.setTextViewText(R.id.aim, new StringBuilder(String.valueOf(mStepTarget)).toString());
        rv.setTextViewText(R.id.runed, new StringBuilder().append(_sumSteps).toString());
        rv.setProgressBar(R.id.pb_rv, 100, _per, false);
        rv.setTextViewText(R.id.progresstx, String.valueOf(_per) + "%");
        Intent intent = new Intent(this.mContext, MainActivityNew.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra(Constants.KEY_PEDOMETOR, true);
        this.mNotification.contentIntent = PendingIntent.getActivity(this.mContext, 0, intent, 0);
        this.mNotification.contentView = rv;
        this.mNotificationManager.notify(0, this.mNotification);
    }

    public void clearNotification() {
        this.showNotification = false;
        if (this.mNotificationManager != null) {
            this.mNotificationManager.cancelAll();
        }
    }

    public void onEvent(Message msg) {
        if (msg.arg2 == 1) {
            if (msg.what == 513) {
                CLog.d("PedometerService", "更新notification的步数：" + this.showNotification + "   msg.obj:" + msg.obj);
                if (!this.showNotification) {
                    return;
                }
                if (msg.obj == null) {
                    showNotification(msg.arg1, (UserInfo) null);
                } else {
                    showNotification(msg.arg1, (UserInfo) msg.obj);
                }
            }
        } else if (msg.arg2 == 10 && msg.what == 515) {
            if (msg.obj == null) {
                showNotification(msg.arg1, (UserInfo) null);
            } else {
                showNotification(msg.arg1, (UserInfo) msg.obj);
            }
            this.showNotification = true;
            CLog.e("PedometerService", "启动notification  msg.obj:" + msg.obj);
        }
    }
}
