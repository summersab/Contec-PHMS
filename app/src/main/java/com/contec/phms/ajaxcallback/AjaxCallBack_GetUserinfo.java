package com.contec.phms.ajaxcallback;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import com.contec.phms.db.LoginUserDao;
import com.contec.phms.util.CLog;
import com.contec.phms.util.Constants;
import com.contec.phms.util.SaveHospitalUtils;
import com.contec.phms.widget.TransUserinfo;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import net.tsz.afinal.http.AjaxCallBack;
import cn.com.contec.net.util.XMLparser;

public class AjaxCallBack_GetUserinfo extends AjaxCallBack<String> {
    private String TAG = "lz";
    private Context mContext;
    private String mTempSaveLoginInfo = "currentlogininfo.xml";
    private String[] mgetmapvalusename = {"username", LoginUserDao.UserSex, "useridcard", "userphone", "userbirthday", "useraddress", "userare", "usercardtype", "userdiskspace", "userhospitalname", "userUsed", "usertotal", "usercreatedate", "userstartdate", "userenddate"};
    private String[] mgetxmlvalusename = {"name", "sex", "pid", "tel", LoginUserDao.Birthday, LoginUserDao.Address, "area", LoginUserDao.CardType, "diskspace", SaveHospitalUtils.spHospitalname, LoginUserDao.Used, LoginUserDao.Total, LoginUserDao.CreateDate, LoginUserDao.StartDate, LoginUserDao.EndDate};
    private Handler mhandler;
    private final int mloginInAnotherPlace = 100007;
    private final int mqueryuserinfoargserror = 104001;
    private final int mqueryuserinfodberror = 104002;
    private final int mqueryuserinfoneterror = Constants.mqueryuserinfoneterror;
    private int mqueryuserinfosuccess = 104000;
    private final int mqueryuserinfotimeout = Constants.mqueryuserinfotimeout;

    public AjaxCallBack_GetUserinfo(Context context, Handler mhandler2) {
        this.mContext = context;
        this.mhandler = mhandler2;
    }

    public boolean isProgress() {
        return super.isProgress();
    }

    public int getRate() {
        return super.getRate();
    }

    public AjaxCallBack<String> progress(boolean progress, int rate) {
        CLog.i(this.TAG, "progress--" + progress + "--rate--" + rate);
        return super.progress(progress, rate);
    }

    public void onStart() {
        super.onStart();
        CLog.i(this.TAG, "AjaxCallBack_GetUserinfo start--------");
    }

    public void onLoading(long count, long current) {
        super.onLoading(count, current);
        CLog.i(this.TAG, "AjaxCallBack_GetUserinfo onLoading   " + count + "-cout");
    }

    public void onSuccess(String t) {
        super.onSuccess(t);
        dealLoginReturn(t);
    }

    private void dealLoginReturn(String t) {
        CLog.i(this.TAG, " onSuccess  " + t.toString());
        String _result = t.substring(34, 40);
        CLog.i(this.TAG, "AjaxCallBack_GetUserinfoon Success 4 " + _result);
        if (_result.equals(Constants.SUCCESS)) {
            String _contentString = t.substring(42, t.length());
            try {
                XMLparser.saveXMLtoFile(this.mContext, _contentString, this.mTempSaveLoginInfo, 0);
            } catch (Exception e) {
                e.printStackTrace();
            }
            TransUserinfo _translz = new TransUserinfo(createnewarraylist(new ByteArrayInputStream(_contentString.getBytes())));
            Bundle _bundle = new Bundle();
            _bundle.putSerializable("newuserinfo", _translz);
            Message _msg = new Message();
            _msg.what = this.mqueryuserinfosuccess;
            _msg.setData(_bundle);
            this.mhandler.sendMessage(_msg);
        } else if (_result.equals("104001")) {
            Message _msg2 = new Message();
            _msg2.what = 104001;
            this.mhandler.sendMessage(_msg2);
        } else if (_result.equals("104002")) {
            Message _msg3 = new Message();
            _msg3.what = 104002;
            this.mhandler.sendMessage(_msg3);
        } else if (_result.equals(Constants.LOGIN_IN_ANOTHER_PLACE)) {
            Message _msg4 = new Message();
            _msg4.what = 100007;
            this.mhandler.sendMessage(_msg4);
        }
    }

    public void onFailure(Throwable t, String strMsg) {
        super.onFailure(t, 0, strMsg);
        if (strMsg != null) {
            CLog.i(this.TAG, "AjaxCallBack_GetUserinfo onFailure   " + strMsg.toString());
        }
        Message _msg = new Message();
        _msg.what = Constants.mqueryuserinfoneterror;
        this.mhandler.sendMessage(_msg);
    }

    private HashMap<String, String> createnewarraylist(InputStream _inputstream) {
        HashMap mapUserinfo = new HashMap();
        for (int i = 0; i < this.mgetxmlvalusename.length; i++) {
            mapUserinfo.put(this.mgetmapvalusename[i], XMLparser.getVaueByOneTag(this.mContext, this.mTempSaveLoginInfo, this.mgetxmlvalusename[i]));
        }
        return mapUserinfo;
    }
}
