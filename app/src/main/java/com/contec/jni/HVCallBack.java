package com.contec.jni;

import com.contec.phms.util.CLog;

public class HVCallBack {
    public int cur;
    private Callback miMsg;
    private boolean misBlocked;
    public int range;

    public HVCallBack(Callback ihandler) {
        this.miMsg = null;
        this.misBlocked = false;
        this.miMsg = ihandler;
        this.range = 0;
        this.cur = 1;
    }

    public HVCallBack() {
        this.miMsg = null;
        this.misBlocked = false;
        this.miMsg = null;
        this.range = 0;
        this.cur = 1;
    }

    public void clear() {
        this.range = 0;
        this.cur = 1;
    }

    public void blockLoadDocument(boolean isblock) {
        this.misBlocked = isblock;
    }

    public void Call(int r, int c, int type) {
        if (this.miMsg != null) {
            this.miMsg.onRecieveMsg(type);
            if (this.misBlocked) {
                try {
                    CLog.i("Call range", "Waitting...");
                    synchronized (this) {
                        wait();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
