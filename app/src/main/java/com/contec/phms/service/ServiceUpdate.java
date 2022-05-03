package com.contec.phms.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import com.contec.phms.util.CLog;
import com.contec.phms.util.Constants;
import com.contec.phms.util.PageUtil;

public class ServiceUpdate extends Service implements Runnable {
    private static final String TAG = ServiceUpdate.class.getSimpleName();

    public void onCreate() {
        Thread _Thread = new Thread(this);
        _Thread.setName(TAG);
        _Thread.start();
        super.onCreate();
    }

    public void run() {
        boolean _have;
        if (PageUtil.checkNet(this) && (_have = UpdateAppFunction.checkNewVersion(this, Constants.SERVICEADDRESS, getPackageName()))) {
            CLog.i(TAG, String.valueOf(_have) + TAG);
            Constants.NEEDUPDATE = true;
        }
    }

    public IBinder onBind(Intent arg0) {
        return null;
    }
}
