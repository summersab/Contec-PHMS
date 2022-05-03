package com.contec.phms.saxparsing;

import android.util.Log;
import com.conect.json.SqliteConst;
import com.contec.phms.manager.message.MessageFromServer;
import com.contec.phms.db.LoginUserDao;
import com.contec.phms.db.localdata.UrineDataDao;
import java.util.ArrayList;
import java.util.List;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import cn.com.contec.net.util.Constants_jar;
import u.aly.bs;

public class XmlParser extends DefaultHandler {
    private List<MessageFromServer> array = new ArrayList();
    private String mCaseID;
    private String mCaseType;
    private boolean mFound = false;
    private String mID;
    private String mLocalName;
    private String mMsgContent;
    private String mMsgDirection;
    private String mMsgSize;
    private String mMsgType;
    String mName = null;
    private String mSendTime;
    private String mSenderid;
    private String mSendername;

    public List<MessageFromServer> getArray() {
        return this.array;
    }

    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        Log.d("startElement", String.valueOf(localName) + " qName:" + qName);
        if (localName.equals(Constants_jar.MSG_STRING)) {
            this.mFound = true;
            this.mID = bs.b;
            this.mSenderid = bs.b;
            this.mSendername = bs.b;
            this.mMsgContent = bs.b;
            this.mSendTime = bs.b;
            this.mMsgSize = bs.b;
            this.mCaseID = bs.b;
            this.mCaseType = bs.b;
            this.mMsgType = bs.b;
            this.mMsgDirection = bs.b;
        }
        this.mLocalName = localName;
    }

    public void characters(char[] ch, int start, int length) throws SAXException {
        if (this.mFound) {
            String value = new String(ch, start, length);
            if (this.mLocalName != null && !this.mLocalName.equals(bs.b)) {
                if (this.mLocalName.equals(SqliteConst.KEY_ID)) {
                    this.mID = value;
                } else if (this.mLocalName.equals("msgdirection")) {
                    this.mMsgDirection = value;
                } else if (this.mLocalName.equals("sendername")) {
                    this.mSendername = value;
                } else if (this.mLocalName.equals("casetype")) {
                    this.mCaseType = value;
                } else if (this.mLocalName.equals("sendtime")) {
                    this.mSendTime = value;
                } else if (this.mLocalName.equals("msgtype")) {
                    this.mMsgType = value;
                } else if (this.mLocalName.equals("caseid")) {
                    this.mCaseID = value;
                } else if (this.mLocalName.equals(LoginUserDao.SenderId)) {
                    this.mSenderid = value;
                } else if (this.mLocalName.equals("msgcontent")) {
                    this.mMsgContent = String.valueOf(this.mMsgContent) + value;
                }
            }
            Log.d(UrineDataDao.VALUE, "*****************" + this.mLocalName + "  " + value);
        }
    }

    public void endElement(String uri, String localName, String qName) throws SAXException {
        Log.d("endElement", String.valueOf(this.mLocalName) + "  * " + qName);
        this.mLocalName = null;
        if (localName.equals(Constants_jar.MSG_STRING)) {
            if (this.mFound) {
                this.array.add(new MessageFromServer(this.mID, this.mSenderid, this.mSendername, this.mMsgContent, this.mSendTime, this.mMsgSize, this.mCaseID, this.mCaseType, this.mMsgType, this.mMsgDirection));
                this.mFound = false;
            }
        } else if (localName.equals("response")) {
            for (int i = 0; i < this.array.size(); i++) {
                Log.e("over", this.array.get(i).toString());
            }
        }
    }
}
