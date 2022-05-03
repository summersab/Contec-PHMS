package com.contec.phms.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import com.contec.phms.R;
import com.contec.phms.util.CLog;
import com.contec.phms.util.Constants;
import com.contec.phms.util.PaintHelper;

public class BallProgressView extends View {
    private String TAG = "BallProgressView";
    private float mCal;
    private Canvas mCanvas;
    private float mCenX = 0.0f;
    private float mCenXCal = 0.0f;
    private float mCenY = 0.0f;
    private float mCenYCal = 0.0f;
    private Context mContext;
    private float mProgress;
    private float mRadius = 0.0f;
    private float mRadiusCal = 0.0f;
    public float mScreenHeight = 0.0f;
    public float mScreenWidth = 0.0f;
    private int mSteps = 0;
    private int mTargetSteps = 1000;

    public BallProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        PaintHelper.getInstance(this.mContext);
    }

    public void draw(Canvas canvas) {
        super.draw(canvas);
        this.mCanvas = canvas;
        this.mProgress = ((float) this.mSteps) / ((float) this.mTargetSteps);
        this.mScreenWidth = (float) Constants.SCREEN_WIDTH;
        this.mScreenHeight = (float) Constants.SCREEN_HEIGHT;
        CLog.e(this.TAG, "mScreenWidth:" + this.mScreenWidth + " mScreenHeight:" + this.mScreenHeight);
        drawView();
    }

    public void setmSteps(int mSteps2) {
        this.mSteps = mSteps2;
    }

    public void setmTargetSteps(int mTargetSteps2) {
        this.mTargetSteps = mTargetSteps2;
    }

    public void setProgress(float press) {
        this.mProgress = press;
    }

    public void setmCal(float mCal2) {
        this.mCal = mCal2;
    }

    private void drawView() {
        try {
            this.mCenX = ((this.mScreenWidth / 4.0f) * 3.0f) - (20.0f * Constants.Persent);
            this.mRadius = this.mScreenWidth / 4.0f;
            this.mCenY = this.mScreenHeight / 4.0f;
            this.mCenXCal = this.mScreenWidth / 4.0f;
            this.mRadiusCal = (this.mScreenWidth / 4.0f) - (this.mScreenWidth / 14.0f);
            this.mCenYCal = this.mCenY + (this.mScreenWidth / 4.0f);
            if (this.mCanvas != null) {
                drawProgress(this.mCanvas, this.mCenX, this.mCenY, this.mRadius, this.mProgress);
                drawCal(this.mCanvas, this.mCenXCal, this.mCenYCal, this.mRadiusCal, this.mCal);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void drawProgress(Canvas pCanvas, float px, float py, float radius, float pPro) {
        float _startXline = ((float) (((double) px) - (((double) radius) * Math.sin(45.0d)))) - (5.0f * Constants.Persent);
        float _startYline = ((float) (((double) py) - (((double) radius) * Math.sin(45.0d)))) + (35.0f * Constants.Persent);
        this.mCanvas.drawLine(px, py, _startXline - (30.0f * Constants.Persent), _startYline - (20.0f * Constants.Persent), PaintHelper.getmDarkGreen());
        String _strDaySteps = this.mContext.getString(R.string.str_steps_day);
        String _strTargetSteps = this.mContext.getString(R.string.str_target_steps);
        this.mCanvas.drawText(_strDaySteps, (_startXline - (40.0f * Constants.Persent)) - PaintHelper.getmDarkGreenText().measureText(_strDaySteps), _startYline - (35.0f * Constants.Persent), PaintHelper.getmDarkGreenText());
        String str = _strTargetSteps;
        this.mCanvas.drawText(str, ((_startXline - (40.0f * Constants.Persent)) - ((PaintHelper.getmDarkGreenText().measureText(_strDaySteps) - PaintHelper.getmDarkGreenTextSmall().measureText(_strTargetSteps)) / 2.0f)) - PaintHelper.getmDarkGreenTextSmall().measureText(_strTargetSteps), (10.0f * Constants.Persent) + _startYline, PaintHelper.getmDarkGreenTextSmall());
        this.mCanvas.drawLine(_startXline - (30.0f * Constants.Persent), _startYline - (20.0f * Constants.Persent), (_startXline - (50.0f * Constants.Persent)) - PaintHelper.getmDarkGreenText().measureText(_strDaySteps), _startYline - (20.0f * Constants.Persent), PaintHelper.getmDarkGreen());
        pCanvas.drawCircle(px, py, radius, PaintHelper.getmWhite());
        pCanvas.drawCircle(px, py, radius, PaintHelper.getmLightGreen());
        Canvas canvas = pCanvas;
        canvas.drawArc(new RectF(px - radius, py - radius, px + radius, py + radius), 90.0f - ((360.0f * pPro) / 2.0f), pPro * 360.0f, false, PaintHelper.getmDarkGreen());
        String _steps = new StringBuilder(String.valueOf(this.mSteps)).toString();
        String _target = new StringBuilder(String.valueOf(this.mTargetSteps)).toString();
        this.mCanvas.drawText(_steps, px - (PaintHelper.getmWhite().measureText(_steps) / 2.0f), (5.0f * Constants.Persent) + py, PaintHelper.getmWhite());
        this.mCanvas.drawLine((20.0f * Constants.Persent) + (px - radius), py + (20.0f * Constants.Persent), (px + radius) - (20.0f * Constants.Persent), py + (20.0f * Constants.Persent), PaintHelper.getmWhite());
        this.mCanvas.drawText(_target, px - (PaintHelper.getmWhiteTarget().measureText(_target) / 2.0f), (70.0f * Constants.Persent) + py, PaintHelper.getmWhiteTarget());
    }

    private void drawCal(Canvas pCanvas, float px, float py, float radius, float pCal) {
        float _startXline = ((float) (((double) px) + (((double) radius) * Math.sin(45.0d)))) + (5.0f * Constants.Persent);
        float _startYline = ((float) (((double) py) + (((double) radius) * Math.sin(45.0d)))) - (35.0f * Constants.Persent);
        this.mCanvas.drawLine(px, py, _startXline + (20.0f * Constants.Persent), _startYline + (20.0f * Constants.Persent), PaintHelper.getmOrange());
        this.mCanvas.drawLine((20.0f * Constants.Persent) + _startXline, (20.0f * Constants.Persent) + _startYline, (50.0f * Constants.Persent) + _startXline, (20.0f * Constants.Persent) + _startYline, PaintHelper.getmOrange());
        String _cal = new StringBuilder(String.valueOf(pCal)).toString();
        pCanvas.drawCircle(px, py, radius, PaintHelper.getmWhite());
        pCanvas.drawCircle(px, py, radius, PaintHelper.getmOrange());
        this.mCanvas.drawText(_cal, px - (PaintHelper.getmWhiteCal().measureText(_cal) / 2.0f), (15.0f * Constants.Persent) + py, PaintHelper.getmWhiteCal());
        this.mCanvas.drawText(this.mContext.getString(R.string.str_cal_str_per), (50.0f * Constants.Persent) + _startXline, (30.0f * Constants.Persent) + _startYline, PaintHelper.getmOrangeText());
    }
}
