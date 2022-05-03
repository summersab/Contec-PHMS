package com.conect.json;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

public abstract class JarTestActivity extends AppCompatActivity implements GestureDetector.OnGestureListener, View.OnTouchListener {
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
}
