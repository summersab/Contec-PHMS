package com.contec.phms.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.ProgressBar;

public class TextProgressBar extends ProgressBar {
    private Paint mPaint;
    private String str;

    public TextProgressBar(Context context) {
        super(context);
        initText();
    }

    public TextProgressBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initText();
    }

    public TextProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        initText();
    }

    public void setProgress(int progress) {
        setText(progress);
        super.setProgress(progress);
    }

    protected synchronized void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Rect rect = new Rect();
        this.mPaint.getTextBounds(this.str, 0, this.str.length(), rect);
        canvas.drawText(this.str, (float) ((getWidth() / 2) - rect.centerX()), (float) ((getHeight() / 2) - rect.centerY()), this.mPaint);
    }

    private void initText() {
        this.mPaint = new Paint();
        this.mPaint.setAntiAlias(true);
        this.mPaint.setColor(0);
    }

    private void setText(int progress) {
        this.str = String.valueOf(String.valueOf((int) (((((float) progress) * 1.0f) / ((float) getMax())) * 100.0f))) + "%";
    }
}
