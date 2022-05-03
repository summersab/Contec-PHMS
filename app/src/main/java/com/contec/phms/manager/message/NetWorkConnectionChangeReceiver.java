package com.contec.phms.manager.message;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.conect.json.CLog;
import com.contec.phms.db.LoginUserDao;
import com.contec.phms.util.PageUtil;

public class NetWorkConnectionChangeReceiver extends BroadcastReceiver {
    private String TAG = getClass().getSimpleName();
    private Context mContext;

    public void onReceive(Context context, Intent intent) {
        this.mContext = context;
        boolean _ifCloseNet = intent.getBooleanExtra("noConnectivity", false);
        CLog.e(this.TAG, "_ifCloseNet:" + _ifCloseNet);
        if (_ifCloseNet) {
            InstantMessageService.stopServer(this.mContext);
            return;
        }
        LoginUserDao _user = PageUtil.getLoginUserInfo();
        if (_user != null && _user.mSID != null && _user.mUID != null && _user.mPsw != null) {
            this.mContext.startService(new Intent(this.mContext, InstantMessageService.class));
        }
    }
}
