package com.contec.phms.infos;

import android.util.Log;
import com.contec.phms.db.LocalLoginInfoDao;
import com.contec.phms.db.LocalLoginInfoManager;
import com.contec.phms.db.LoginUserDao;
import java.io.ByteArrayInputStream;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import u.aly.bs;

public class oAuthInfor {
    private String mPassword;
    private String mThirdcode;
    private String mUserId;

    public oAuthInfor(String mThirdcode2, String mPassword2, String backInforcontext) {
        this.mThirdcode = mThirdcode2;
        this.mPassword = mPassword2;
        insterregisbackInfoDb(backInforcontext);
    }

    private void insterregisbackInfoDb(String backInforcontext) {
        if (backInforcontext != null && !backInforcontext.equals(bs.b)) {
            new LoginUserDao();
            DocumentBuilderFactory _factory = DocumentBuilderFactory.newInstance();
            try {
                Document _document = _factory.newDocumentBuilder().parse(new ByteArrayInputStream(backInforcontext.getBytes()));
                LocalLoginInfoDao _localinfor = new LocalLoginInfoDao();
                _localinfor.mCardNb = _document.getDocumentElement().getElementsByTagName("uid").item(0).getTextContent();
                _localinfor.mPassword = this.mPassword;
                _localinfor.mThirdCode = this.mThirdcode;
                Log.e("================", "================");
                Log.e("================", "=====userid===========" + _localinfor.mCardNb);
                Log.e("================", "=====password===========" + _localinfor.mPassword);
                Log.e("================", "=====mThirdCode===========" + _localinfor.mThirdCode);
                Log.e("================", "================");
                new LocalLoginInfoManager().add(_localinfor);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public String getmUserId() {
        return this.mUserId;
    }

    public void setmUserId(String mUserId2) {
        this.mUserId = mUserId2;
    }
}
