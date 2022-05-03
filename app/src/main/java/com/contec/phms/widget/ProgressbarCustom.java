package com.contec.phms.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import com.contec.phms.R;

public class ProgressbarCustom extends SurfaceView implements SurfaceHolder.Callback {
    private int h;
    private Bitmap mBgBefor;
    private Bitmap mBgBeforLeft;
    private Bitmap mBgBig;
    private Bitmap mBgSmall;
    private Canvas mCanvas;
    private boolean mFlag = true;
    SurfaceHolder mHolder;
    private Paint mPaint;
    private Paint mPaint2;
    public int mProgress = 0;
    private Thread mThread;
    private int w;

    public ProgressbarCustom(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        this.mHolder = getHolder();
        this.mHolder.addCallback(this);
        this.mHolder.setFormat(-2);
        this.mPaint = new Paint();
        this.mPaint2 = new Paint();
        this.mPaint.setStyle(Paint.Style.FILL);
        this.mPaint.setAntiAlias(true);
        this.mPaint.setFilterBitmap(true);
        this.mPaint2.setStyle(Paint.Style.FILL);
        this.mPaint2.setColor(-7829368);
        this.mPaint2.setAntiAlias(true);
        this.mPaint2.setFilterBitmap(true);
        this.mPaint2.setAlpha(0);
        this.mBgBeforLeft = BitmapFactory.decodeResource(getResources(), R.drawable.img_progress_ho_left);
        this.mBgBig = BitmapFactory.decodeResource(getResources(), R.drawable.img_progress_bg_big);
        this.mBgBefor = BitmapFactory.decodeResource(getResources(), R.drawable.img_progress_ho);
        this.mBgSmall = BitmapFactory.decodeResource(getResources(), R.drawable.img_progress_bg);
        this.w = this.mBgSmall.getWidth();
        this.h = this.mBgSmall.getHeight();
    }

    private void logic() {
    }

    public void setProgress(int press) {
        this.mProgress = (this.w * press) / 100;
    }

    private void drawView() {
        try {
            if (this.mHolder != null) {
                this.mCanvas = this.mHolder.lockCanvas();
                if (this.mCanvas != null) {
                    this.mCanvas.setDrawFilter(new PaintFlagsDrawFilter(0, 3));
                    this.mCanvas.drawColor(0, PorterDuff.Mode.CLEAR);
                    this.mCanvas.save();
                    RectF _rectf = new RectF(0.0f, 4.0f, (float) (this.w + 5), (float) (this.h + 13));
                    this.mCanvas.drawRoundRect(_rectf, 0.0f, 4.0f, this.mPaint2);
                    this.mCanvas.drawBitmap(this.mBgBig, (Rect) null, _rectf, this.mPaint);
                    RectF _rectf2 = new RectF(5.0f, 8.0f, (float) this.w, (float) (this.h + 8));
                    this.mCanvas.drawRoundRect(_rectf2, 5.0f, 8.0f, this.mPaint2);
                    this.mCanvas.drawBitmap(this.mBgSmall, (Rect) null, _rectf2, this.mPaint);
                    if (this.mProgress <= 10) {
                        RectF _rectf3 = new RectF(6.0f, 8.0f, (float) (this.mProgress + 6), (float) (this.h + 8));
                        Rect _rect = new Rect(0, 0, this.mProgress, this.mBgBefor.getHeight());
                        this.mCanvas.drawRoundRect(_rectf3, 6.0f, 8.0f, this.mPaint2);
                        this.mCanvas.drawBitmap(this.mBgBeforLeft, _rect, _rectf3, this.mPaint);
                    } else if (this.mProgress > 10) {
                        RectF _rectf4 = new RectF(12.0f, 8.0f, (float) (this.mProgress + 1), (float) (this.h + 8));
                        Rect _rect2 = new Rect((this.mBgBefor.getWidth() - this.mProgress) + 8, 0, this.mBgBefor.getWidth(), this.mBgBefor.getHeight());
                        this.mCanvas.drawRoundRect(_rectf4, 12.0f, 8.0f, this.mPaint2);
                        this.mCanvas.drawBitmap(this.mBgBefor, _rect2, _rectf4, this.mPaint);
                        RectF _rectf5 = new RectF(6.0f, 8.0f, 16.0f, (float) (this.h + 8));
                        Rect _rect3 = new Rect(0, 0, 10, this.mBgBefor.getHeight());
                        this.mCanvas.drawRoundRect(_rectf5, 6.0f, 8.0f, this.mPaint2);
                        this.mCanvas.drawBitmap(this.mBgBeforLeft, _rect3, _rectf5, this.mPaint);
                    }
                    this.mCanvas.restore();
                }
                this.mHolder.unlockCanvasAndPost(this.mCanvas);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void threadStart() {
        this.mThread = new Thread() {
            public void run() {
                super.run();
                ProgressbarCustom.this.mThread.setName(getClass().getName());
                while (ProgressbarCustom.this.mFlag) {
                    try {
                        ProgressbarCustom.this.drawView();
                        ProgressbarCustom.this.logic();
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        this.mThread.start();
    }

    public void surfaceCreated(SurfaceHolder holder) {
        this.mFlag = true;
        threadStart();
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        this.mFlag = false;
    }
}
