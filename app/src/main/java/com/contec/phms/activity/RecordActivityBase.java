package com.contec.phms.activity;

import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import com.contec.phms.login.ActivityBase;
//import com.umeng.analytics.MobclickAgent;

public abstract class RecordActivityBase extends ActivityBase implements GestureDetector.OnGestureListener, View.OnTouchListener {
    private GestureDetector mGesture;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mGesture = new GestureDetector(this);
    }

    public boolean onTouch(View arg0, MotionEvent arg1) {
        return this.mGesture.onTouchEvent(arg1);
    }

    public boolean onDown(MotionEvent arg0) {
        return false;
    }

    public void onLongPress(MotionEvent arg0) {
    }

    public boolean onScroll(MotionEvent arg0, MotionEvent arg1, float arg2, float arg3) {
        return false;
    }

    public void onShowPress(MotionEvent arg0) {
    }

    public boolean onSingleTapUp(MotionEvent arg0) {
        return false;
    }

    protected void onResume() {
        super.onResume();
        //MobclickAgent.onResume(this);
    }

    protected void onPause() {
        super.onPause();
        //MobclickAgent.onPause(this);
    }
}
