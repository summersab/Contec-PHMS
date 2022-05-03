package com.contec.phms.saxparsing;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import com.contec.phms.App_phms;
import com.contec.phms.R;
import com.contec.phms.activity.MainActivityNew;
import com.contec.phms.util.Constants;
import java.util.Random;

public class MessageNotification {
    private final String TAG = "MessageNotification";
    private Context mContext = App_phms.getInstance().getApplicationContext();
    public Notification mNotification;
    private NotificationManager mNotificationManager;

    public MessageNotification(Context pContext, String pMsg, boolean pIfSkip) {
        long when = System.currentTimeMillis();
        this.mNotification = new Notification();
        this.mNotification.icon = R.drawable.img_phms_icon_small;
        this.mNotification.tickerText = pMsg;
        this.mNotification.when = when;
        this.mNotification.flags = 16;
        this.mNotification.defaults |= 1;
        this.mNotification.vibrate = new long[]{1000, 1000, 1000, 1000, 1000};
        this.mNotificationManager = (NotificationManager) this.mContext.getSystemService("notification");
        showNotification(pMsg, pIfSkip);
    }

    private void showNotification(String pMsg, boolean pIfSkip) {
        Intent intent;
        RemoteViews rv = new RemoteViews(this.mContext.getPackageName(), R.layout.layout_message_notifaction);
        rv.setTextViewText(R.id.msg_content, pMsg);
        Random random = new Random();
        if (pIfSkip) {
            intent = new Intent(this.mContext, MainActivityNew.class);
            intent.setAction("android.intent.action.MAIN");
            intent.addCategory("android.intent.category.LAUNCHER");
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.setFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
            intent.putExtra(Constants.KEY_CHECK_REPORT, true);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        } else {
            intent = new Intent();
        }
        this.mNotification.contentIntent = PendingIntent.getActivity(this.mContext, random.nextInt(), intent, 134217728);
        this.mNotification.contentView = rv;
        this.mNotificationManager.notify(random.nextInt(), this.mNotification);
    }
}
