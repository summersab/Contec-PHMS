package com.contec.phms.manager.message;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.Message;
import com.contec.phms.App_phms;
import com.contec.phms.SearchDevice.SearchDevice;
import com.contec.phms.Server_Main;
import com.contec.phms.manager.exception.ExceptionManager;
import com.contec.phms.util.CLog;
import com.contec.phms.util.Constants;

public class MessageManager extends Service {
    public static ExceptionManager mExceptionManager;
    public final String TAG = "MessageManager";

    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onCreate() {
        super.onCreate();
        App_phms.getInstance().mEventBus.register(this);
        mExceptionManager = new ExceptionManager();
        Server_Main.m_Start_First = false;
        startService(new Intent(getApplicationContext(), SearchDevice.class));
    }

    public void onDestroy() {
        super.onDestroy();
        App_phms.getInstance().mEventBus.unregister(this);
    }

    public static void stopServer(Context pContext) {
        pContext.stopService(new Intent(pContext, MessageManager.class));
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    public void onEvent(Message msg) {
        if (msg.arg2 == 4) {
            CLog.i("MessageManager", "Transfer Message:" + msg.what);
            Message msgpost = new Message();
            switch (msg.what / 10) {
                case 1:
                    msgpost.what = msg.what;
                    msgpost.arg2 = 6;
                    App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(msgpost);
                    return;
                case 2:
                    msgpost.what = msg.what;
                    msgpost.arg2 = 3;
                    App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(msgpost);
                    return;
                case 3:
                    msgpost.what = msg.what;
                    msgpost.arg2 = 2;
                    App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(msgpost);
                    return;
                case 5:
                    msgpost.what = msg.what;
                    msgpost.obj = Constants.DEVICE_UPLOAD;
                    msgpost.arg2 = 5;
                    App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(msgpost);
                    return;
                default:
                    return;
            }
        }
    }
}
