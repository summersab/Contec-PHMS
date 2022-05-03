package com.contec.phms.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.contec.phms.R;
import com.contec.phms.util.PageUtil;

public class SwitchView extends LinearLayout {
    private boolean isAninFinish = true;
    private boolean isChangedByTouch = false;
    private int mSwitchBlockWidth;
    private ImageView maskImage;
    private boolean open;
    private OnSwitchChangeListener switchChangeListener;
    private float x;

    public interface OnSwitchChangeListener {
        void onSwitchChanged(boolean z);
    }

    public SwitchView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SwitchView(Context context) {
        super(context);
        init();
    }

    private void init() {
        if (this.open) {
            setBackgroundResource(R.drawable.switch_bg);
        } else {
            setBackgroundResource(R.drawable.switch_bg2);
        }
        this.maskImage = new ImageView(getContext());
        this.maskImage.setLayoutParams(new LinearLayout.LayoutParams(-2, -2));
        this.maskImage.setImageResource(R.drawable.switch_mask);
        PageUtil.measureView(this.maskImage);
        this.mSwitchBlockWidth = this.maskImage.getMeasuredWidth();
        addView(this.maskImage);
    }

    public boolean getSwitchStatus() {
        return this.open;
    }

    public void setSwitchStatus(boolean isOpen) {
        this.open = isOpen;
        if (isOpen) {
            setBackgroundResource(R.drawable.switch_bg);
            setGravity(5);
            return;
        }
        setBackgroundResource(R.drawable.switch_bg2);
        setGravity(3);
    }

    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case 0:
                this.x = event.getX();
                return true;
            case 1:
                if (Math.abs(event.getX() - this.x) <= 5.0f) {
                    changeStatus();
                }
                this.isChangedByTouch = false;
                return true;
            case 2:
                if (event.getX() - this.x > 5.0f && !this.open) {
                    changeStatus();
                    return true;
                } else if (event.getX() - this.x >= -5.0f || !this.open) {
                    return true;
                } else {
                    changeStatus();
                    return true;
                }
            case 3:
                this.isChangedByTouch = false;
                return true;
            default:
                return true;
        }
    }

    private void changeStatus() {
        boolean z = true;
        if (this.isAninFinish && !this.isChangedByTouch) {
            this.isChangedByTouch = true;
            if (this.open) {
                z = false;
            }
            this.open = z;
            this.isAninFinish = false;
            if (this.switchChangeListener != null) {
                this.switchChangeListener.onSwitchChanged(this.open);
            }
            changeOpenStatusWithAnim(this.open);
        }
    }

    private void changeOpenStatusWithAnim(boolean open2) {
        if (open2) {
            setBackgroundResource(R.drawable.switch_bg);
            Animation leftToRight = new TranslateAnimation(1, 0.0f, 0, (float) (getWidth() - this.maskImage.getWidth()), 1, 0.0f, 1, 0.0f);
            leftToRight.setDuration(300);
            leftToRight.setAnimationListener(new Animation.AnimationListener() {
                public void onAnimationStart(Animation animation) {
                }

                public void onAnimationRepeat(Animation animation) {
                }

                public void onAnimationEnd(Animation animation) {
                    SwitchView.this.maskImage.clearAnimation();
                    SwitchView.this.setGravity(5);
                    SwitchView.this.isAninFinish = true;
                }
            });
            this.maskImage.startAnimation(leftToRight);
            return;
        }
        setBackgroundResource(R.drawable.switch_bg2);
        Animation rightToLeft = new TranslateAnimation(1, 0.0f, 0, (float) (this.maskImage.getWidth() - getWidth()), 1, 0.0f, 1, 0.0f);
        rightToLeft.setDuration(300);
        rightToLeft.setAnimationListener(new Animation.AnimationListener() {
            public void onAnimationStart(Animation animation) {
            }

            public void onAnimationRepeat(Animation animation) {
            }

            public void onAnimationEnd(Animation animation) {
                SwitchView.this.maskImage.clearAnimation();
                SwitchView.this.setGravity(3);
                SwitchView.this.isAninFinish = true;
            }
        });
        this.maskImage.startAnimation(rightToLeft);
    }

    public OnSwitchChangeListener getSwitchChangeListener() {
        return this.switchChangeListener;
    }

    public void setOnSwitchChangeListener(OnSwitchChangeListener switchChangeListener2) {
        this.switchChangeListener = switchChangeListener2;
    }

    public int getmSwitchBlockwidth() {
        return this.mSwitchBlockWidth;
    }
}
