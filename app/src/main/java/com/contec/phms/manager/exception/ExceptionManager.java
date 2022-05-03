package com.contec.phms.manager.exception;

import android.os.Message;
import com.contec.phms.App_phms;
import com.contec.phms.manager.message.OrderList;
import com.contec.phms.util.CLog;

public class ExceptionManager {
    public static final String TAG = "ExceptionManager";

    public void excute(int type) {
        printExcetion(type);
        switch (type) {
            case OrderList.EM_BLUETOOTH_ERROR /*41*/:
                bluetoothError();
                return;
            default:
                return;
        }
    }

    public void bluetoothError() {
        Message msgManager = new Message();
        msgManager.what = 21;
        msgManager.arg2 = 4;
        App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(msgManager);
    }

    public static void printExcetion(int type) {
        CLog.e(TAG, "Exception: " + type);
    }
}
