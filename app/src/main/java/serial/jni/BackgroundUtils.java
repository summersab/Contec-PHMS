package serial.jni;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

public class BackgroundUtils {
    private int DEFAULT_BACKGROUND_COLOR = -16777216;
    private int DEFAULT_BACKGROUND_GRID_COLOR = Color.rgb(111, 110, 110);
    private Drawable backgroundDrawable;
    private int mViewHeight;
    private int mViewWidth;
    private float perMillmeter = 5.245f;

    public Drawable getBackgroundDrawable(int viewWidth, int viewHeight, int bkColor, int gridColor) {
        if (viewWidth <= 0) {
            viewWidth = BluetoothConnect.MESSAGE_CONNECT_INTERRUPTED;
            viewHeight = 869;
        }
        this.mViewWidth = viewWidth;
        this.mViewHeight = viewHeight;
        this.DEFAULT_BACKGROUND_COLOR = bkColor;
        this.DEFAULT_BACKGROUND_GRID_COLOR = gridColor;
        this.perMillmeter = (1.25f * (2.8000002f * ((float) viewWidth))) / 512.5f;
        changeBackgroundDrawable();
        return this.backgroundDrawable;
    }

    public void changeBackgroundDrawable() {
        int backgroudColor = this.DEFAULT_BACKGROUND_COLOR;
        int gridColor = this.DEFAULT_BACKGROUND_GRID_COLOR;
        Bitmap bitmap = Bitmap.createBitmap(this.mViewWidth, this.mViewHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawColor(backgroudColor);
        Paint mPaint = new Paint();
        mPaint.setStrokeWidth(1.0f);
        mPaint.setColor(gridColor);
        for (int i = 0; ((float) i) < ((float) this.mViewHeight) / this.perMillmeter; i++) {
            float y = ((float) i) * this.perMillmeter;
            if (i % 5 == 0) {
                canvas.drawLine(0.0f, y, (float) this.mViewWidth, y, mPaint);
            }
            for (int j = 0; ((float) j) < ((float) this.mViewWidth) / this.perMillmeter; j++) {
                float x = ((float) j) * this.perMillmeter;
                if (j % 5 == 0 && i == 0) {
                    canvas.drawLine(x, y, x, (float) this.mViewHeight, mPaint);
                } else if (!(j % 5 == 0 || i % 5 == 0)) {
                    canvas.drawPoint(x, y, mPaint);
                }
            }
        }
        canvas.save();
        canvas.restore();
        this.backgroundDrawable = new BitmapDrawable(bitmap);
    }
}
