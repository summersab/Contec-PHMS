package com.contec.phms.util;

import android.content.Intent;
import android.os.Message;
import com.contec.phms.App_phms;
import com.contec.phms.device.template.BluetoothServerService;
import com.contec.phms.device.template.PollingService;
import com.contec.phms.upload.UploadService;
import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

public class TimerUtil {
    public static String TAG = "TimerUtil";
    public static TimerUtil mTimerUtil;
    public Timer mTimer;

    public static TimerUtil getinstance() {
        if (mTimerUtil == null) {
            mTimerUtil = new TimerUtil();
        }
        return mTimerUtil;
    }

    public void startTimer() {
        if (this.mTimer == null) {
            this.mTimer = new Timer();
            this.mTimer.schedule(new TimerTask() {
                public void run() {
                    CLog.dT(TimerUtil.TAG, "时间到了 **********");
                    File _file = new File(Constants.UPLOAD_FIAIL_DADT);
                    if (_file == null || _file.list() == null || _file.list().length <= 0) {
                        PollingService.isFromUpoladFaidedMsg = false;
                    } else {
                        BluetoothServerService.stopServer(App_phms.getInstance().getBaseContext());
                        CLog.dT(TimerUtil.TAG, "有失败的文件，开始上传失败的文件 ");
                        App_phms.getInstance().getApplicationContext().startService(new Intent(App_phms.getInstance().getApplicationContext(), UploadService.class));
                        PollingService.isFromUpoladFaidedMsg = true;
                        Message msgManager = new Message();
                        msgManager.obj = Constants.DEVICE_UPLOAD_TIMER;
                        msgManager.what = 51;
                        msgManager.arg2 = 5;
                        App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(msgManager);
                    }
                    TimerUtil.this.clearTimer();
                }
            }, 10000);
        }
    }

    public void clearTimer() {
        if (this.mTimer != null) {
            if (this.mTimer != null) {
                this.mTimer.cancel();
            }
            if (this.mTimer != null) {
                this.mTimer.purge();
            }
            this.mTimer = null;
        }
    }
}
