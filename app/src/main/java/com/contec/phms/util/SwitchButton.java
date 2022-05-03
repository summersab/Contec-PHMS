package com.contec.phms.util;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageButton;

public class SwitchButton extends androidx.appcompat.widget.AppCompatImageButton {
    private boolean isTurnOn;
    private OnSwitchListener onSwitchListener;
    private int turnOffResource;
    private int turnOnResource;

    public interface OnSwitchListener {
        void onSwitch(View view, boolean z);
    }

    public SwitchButton(Context context) {
        this(context, (AttributeSet) null);
    }

    public SwitchButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SwitchButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.isTurnOn = false;
        this.turnOnResource = 0;
        this.turnOffResource = 0;
        init();
    }

    private void init() {
        changeRes(this.isTurnOn);
        setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                SwitchButton.this.isTurnOn = !SwitchButton.this.isTurnOn;
                SwitchButton.this.changeRes(SwitchButton.this.isTurnOn);
                if (SwitchButton.this.onSwitchListener != null) {
                    SwitchButton.this.onSwitchListener.onSwitch(v, SwitchButton.this.isTurnOn);
                }
            }
        });
    }

    private void changeRes(boolean isTurnOn2) {
        if (isTurnOn2) {
            setImageResource(this.turnOnResource);
        } else {
            setImageResource(this.turnOffResource);
        }
    }

    public void setTurnOn(boolean isTurnOn2) {
        this.isTurnOn = isTurnOn2;
        changeRes(isTurnOn2);
    }

    public void setTurnOnResource(int turnOnResource2) {
        this.turnOnResource = turnOnResource2;
        changeRes(this.isTurnOn);
    }

    public void setTurnOffResource(int turnOffResource2) {
        this.turnOffResource = turnOffResource2;
        changeRes(this.isTurnOn);
    }

    public void setOnSwitchListener(OnSwitchListener onSwitchListener2) {
        this.onSwitchListener = onSwitchListener2;
    }
}
