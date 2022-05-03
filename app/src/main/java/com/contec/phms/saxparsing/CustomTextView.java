package com.contec.phms.saxparsing;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import androidx.core.internal.view.SupportMenu;
import android.view.View;
import com.contec.phms.R;
import u.aly.bs;

public class CustomTextView extends View implements Runnable {
    private Bitmap bitmap = new BitmapDrawable(getResources().openRawResource(R.drawable.account_add)).getBitmap();
    private Paint m_paint = new Paint();
    private int strLeft = 40;

    public CustomTextView(Context context) {
        super(context);
        new Thread(this).start();
    }

    Rect GetStringRect(String strText) {
        Rect rect = new Rect();
        this.m_paint.getTextBounds(strText, 0, strText.length(), rect);
        return rect;
    }

    void marquee(Canvas canvas, String txtStr) {
        String substring = txtStr.substring(0, txtStr.length());
        Rect rect = GetStringRect(txtStr);
        int len = txtStr.length();
        while (this.strLeft + rect.width() > 320 && len > 0) {
            rect = GetStringRect(txtStr.substring(0, len));
            len--;
        }
        this.m_paint.setColor(SupportMenu.CATEGORY_MASK);
        String txtLeft = txtStr.substring(0, len);
        if (!txtLeft.equals(bs.b)) {
            canvas.drawText(txtLeft, (float) this.strLeft, 40.0f, this.m_paint);
            this.strLeft += 20;
        } else {
            this.strLeft = 40;
        }
        String txtRight = txtStr.substring(len, txtStr.length());
        if (!txtRight.equals(bs.b)) {
            canvas.drawText(txtRight, 20.0f, 40.0f, this.m_paint);
        }
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        marquee(canvas, "这是一个测试的例子");
    }

    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                Thread.sleep(100);
            } catch (Exception e) {
                Thread.currentThread().interrupt();
            }
            postInvalidate();
        }
    }
}
