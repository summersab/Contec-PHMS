package com.contec.phms.device.pedometer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Message;
import com.conect.json.CLog;
import com.contec.phms.App_phms;

public class PowerOffBrodercastReceiver extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.ACTION_SHUTDOWN") || intent.getAction().equals("android.intent.action.ACTION_DATE_CHANGED")) {
            Message msg = new Message();
            msg.what = 512;
            msg.arg2 = 7;
            msg.arg1 = 8;
            App_phms.getInstance().mEventBus.post(msg);
        }
        CLog.e("lz", "关机**************************************" + intent.getAction());
    }
}
