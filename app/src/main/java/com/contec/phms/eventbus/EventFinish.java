package com.contec.phms.eventbus;

public class EventFinish {
    private int leng;
    private String mMsg;

    public EventFinish(String msg) {
        this.mMsg = msg;
    }

    public EventFinish(String msg, int leng1) {
        this.mMsg = msg;
        this.leng = leng1;
    }

    public String getMsg() {
        return this.mMsg;
    }

    public int getLeng() {
        return this.leng;
    }
}
