package com.zxing.android.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.View;
import com.contec.phms.R;
import com.google.zxing.ResultPoint;
import com.zxing.android.camera.CameraManager;
import java.util.ArrayList;
import java.util.Collection;

@SuppressLint({"DrawAllocation"})
public final class ViewfinderView extends View {
    private static final long ANIMATION_DELAY = 30;
    private static final int CORNER_WIDTH = 10;
    private static final int MAX_RESULT_POINTS = 5;
    private static final int MIDDLE_LINE_PADDING = 5;
    private static final int MIDDLE_LINE_WIDTH = 6;
    private static final int OPAQUE = 255;
    private static final int SPEEN_DISTANCE = 5;
    private static final int TEXT_PADDING_TOP = 30;
    private static final int TEXT_SIZE = 16;
    private static float density;
    private int ScreenRate;
    private CameraManager cameraManager;
    boolean isFirst;
    private Collection<ResultPoint> lastPossibleResultPoints;
    private final int maskColor;
    private Paint paint;
    private Collection<ResultPoint> possibleResultPoints;
    private Bitmap resultBitmap;
    private final int resultColor;
    private final int resultPointColor;
    private int slideBottom;
    private int slideTop;

    public ViewfinderView(Context context) {
        this(context, (AttributeSet) null);
    }

    public ViewfinderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.paint = new Paint(1);
        this.maskColor = 1610612736;
        this.resultColor = -1342177280;
        this.resultPointColor = -1056964864;
        this.possibleResultPoints = new ArrayList(5);
        this.lastPossibleResultPoints = null;
        density = context.getResources().getDisplayMetrics().density;
        this.ScreenRate = (int) (20.0f * density);
    }

    public void setCameraManager(CameraManager cameraManager2) {
        this.cameraManager = cameraManager2;
    }

    public void onDraw(Canvas canvas) {
        Rect frame = this.cameraManager.getFramingRect();
        if (frame != null) {
            if (!this.isFirst) {
                this.isFirst = true;
                this.slideTop = frame.top;
                this.slideBottom = frame.bottom;
            }
            int width = canvas.getWidth();
            int height = canvas.getHeight();
            this.paint.setColor(this.resultBitmap != null ? this.resultColor : this.maskColor);
            canvas.drawRect(0.0f, 0.0f, (float) width, (float) frame.top, this.paint);
            canvas.drawRect(0.0f, (float) frame.top, (float) frame.left, (float) (frame.bottom + 1), this.paint);
            canvas.drawRect((float) (frame.right + 1), (float) frame.top, (float) width, (float) (frame.bottom + 1), this.paint);
            canvas.drawRect(0.0f, (float) (frame.bottom + 1), (float) width, (float) height, this.paint);
            if (this.resultBitmap != null) {
                this.paint.setAlpha(255);
                canvas.drawBitmap(this.resultBitmap, (float) frame.left, (float) frame.top, this.paint);
                return;
            }
            this.paint.setColor(-1);
            canvas.drawRect((float) frame.left, (float) frame.top, (float) (frame.right + 1), (float) (frame.top + 2), this.paint);
            canvas.drawRect((float) frame.left, (float) (frame.top + 2), (float) (frame.left + 2), (float) (frame.bottom - 1), this.paint);
            canvas.drawRect((float) (frame.right - 1), (float) frame.top, (float) (frame.right + 1), (float) (frame.bottom - 1), this.paint);
            canvas.drawRect((float) frame.left, (float) (frame.bottom - 1), (float) (frame.right + 1), (float) (frame.bottom + 1), this.paint);
            this.paint.setColor(-13382560);
            this.paint.setAntiAlias(true);
            canvas.drawRect((float) ((frame.left - 10) + 2), (float) ((frame.top - 10) + 2), (float) (((frame.left + this.ScreenRate) - 10) + 2), (float) (frame.top + 2), this.paint);
            canvas.drawRect((float) ((frame.left - 10) + 2), (float) ((frame.top - 10) + 2), (float) (frame.left + 2), (float) (((frame.top + this.ScreenRate) - 10) + 2), this.paint);
            canvas.drawRect((float) (((frame.right - this.ScreenRate) + 10) - 2), (float) ((frame.top - 10) + 2), (float) ((frame.right + 10) - 2), (float) (frame.top + 2), this.paint);
            canvas.drawRect((float) (frame.right - 2), (float) ((frame.top - 10) + 2), (float) ((frame.right + 10) - 2), (float) (((frame.top + this.ScreenRate) - 10) + 2), this.paint);
            canvas.drawRect((float) ((frame.left - 10) + 2), (float) (frame.bottom - 2), (float) (((frame.left + this.ScreenRate) - 10) + 2), (float) ((frame.bottom + 10) - 2), this.paint);
            canvas.drawRect((float) ((frame.left - 10) + 2), (float) (((frame.bottom - this.ScreenRate) + 10) - 2), (float) (frame.left + 2), (float) ((frame.bottom + 10) - 2), this.paint);
            canvas.drawRect((float) (((frame.right - this.ScreenRate) + 10) - 2), (float) (frame.bottom - 2), (float) ((frame.right + 10) - 2), (float) ((frame.bottom + 10) - 2), this.paint);
            canvas.drawRect((float) (frame.right - 2), (float) (((frame.bottom - this.ScreenRate) + 10) - 2), (float) ((frame.right + 10) - 2), (float) ((frame.bottom + 10) - 2), this.paint);
            this.slideTop += 5;
            if (this.slideTop >= frame.bottom) {
                this.slideTop = frame.top;
            }
            Rect lineRect = new Rect();
            lineRect.left = frame.left;
            lineRect.right = frame.right;
            lineRect.top = this.slideTop;
            lineRect.bottom = this.slideTop + 18;
            canvas.drawBitmap(((BitmapDrawable) getResources().getDrawable(R.drawable.qr_scan_line)).getBitmap(), (Rect) null, lineRect, this.paint);
            postInvalidateDelayed(ANIMATION_DELAY, frame.left, frame.top, frame.right, frame.bottom);
        }
    }

    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }

    public void drawViewfinder() {
        Bitmap resultBitmap2 = this.resultBitmap;
        this.resultBitmap = null;
        if (resultBitmap2 != null) {
            resultBitmap2.recycle();
        }
        invalidate();
    }

    public void drawResultBitmap(Bitmap barcode) {
        this.resultBitmap = barcode;
        invalidate();
    }

    public void addPossibleResultPoint(ResultPoint point) {
        this.possibleResultPoints.add(point);
    }
}
