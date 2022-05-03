package com.contec.phms.manager.message;

public class MessageFromServer {
    private String mCaseID;
    private String mCaseType;
    private String mID;
    private String mMsgContent;
    private String mMsgDirection;
    private String mMsgSize;
    private String mMsgType;
    private String mSendTime;
    private String mSenderid;
    private String mSendername;

    public MessageFromServer() {
    }

    public MessageFromServer(String mID2, String mSenderid2, String mSendername2, String mMsgContent2, String mSendTime2, String mMsgSize2, String mCaseID2, String mCaseType2, String mMsgType2, String mMsgDirection2) {
        this.mID = mID2;
        this.mSenderid = mSenderid2;
        this.mSendername = mSendername2;
        this.mMsgContent = mMsgContent2;
        this.mSendTime = mSendTime2;
        this.mMsgSize = mMsgSize2;
        this.mCaseID = mCaseID2;
        this.mCaseType = mCaseType2;
        this.mMsgType = mMsgType2;
        this.mMsgDirection = mMsgDirection2;
    }

    public String getmID() {
        return this.mID;
    }

    public void setmID(String mID2) {
        this.mID = mID2;
    }

    public String getmSenderid() {
        return this.mSenderid;
    }

    public void setmSenderid(String mSenderid2) {
        this.mSenderid = mSenderid2;
    }

    public String getmSendername() {
        return this.mSendername;
    }

    public void setmSendername(String mSendername2) {
        this.mSendername = mSendername2;
    }

    public String getmMsgContent() {
        return this.mMsgContent;
    }

    public void setmMsgContent(String mMsgContent2) {
        this.mMsgContent = mMsgContent2;
    }

    public String getmSendTime() {
        return this.mSendTime;
    }

    public void setmSendTime(String mSendTime2) {
        this.mSendTime = mSendTime2;
    }

    public String getmMsgSize() {
        return this.mMsgSize;
    }

    public void setmMsgSize(String mMsgSize2) {
        this.mMsgSize = mMsgSize2;
    }

    public String getmCaseID() {
        return this.mCaseID;
    }

    public void setmCaseID(String mCaseID2) {
        this.mCaseID = mCaseID2;
    }

    public String getmCaseType() {
        return this.mCaseType;
    }

    public void setmCaseType(String mCaseType2) {
        this.mCaseType = mCaseType2;
    }

    public String getmMsgType() {
        return this.mMsgType;
    }

    public void setmMsgType(String mMsgType2) {
        this.mMsgType = mMsgType2;
    }

    public String getmMsgDirection() {
        return this.mMsgDirection;
    }

    public void setmMsgDirection(String mMsgDirection2) {
        this.mMsgDirection = mMsgDirection2;
    }

    public String toString() {
        return "mID:" + this.mID + " mSenderid:" + this.mSenderid + " mSendername:" + this.mSendername + " mMsgContent:" + this.mMsgContent + " mSendTime:" + this.mSendTime + " mMsgSize:" + this.mMsgSize + " mCaseID:" + this.mCaseID + " mCaseType:" + this.mCaseType + " mMsgType:" + this.mMsgType + " mMsgDirection:" + this.mMsgDirection;
    }
}
