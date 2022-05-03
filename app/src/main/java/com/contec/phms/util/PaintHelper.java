package com.contec.phms.util;

import android.content.Context;
import android.graphics.Paint;

public class PaintHelper {
    private static Context mContext;
    private static Paint mDarkGreen = null;
    private static Paint mDarkGreenText = null;
    private static Paint mDarkGreenTextSmall = null;
    private static Paint mLightGreen = null;
    private static Paint mOrange = null;
    private static Paint mOrangeText = null;
    private static PaintHelper mPaintHelper;
    private static Paint mWhite = null;
    private static Paint mWhiteCal = null;
    private static Paint mWhiteSmall = null;

    public static PaintHelper getInstance(Context pContext) {
        if (mPaintHelper == null) {
            mPaintHelper = new PaintHelper(pContext);
        }
        return mPaintHelper;
    }

    private PaintHelper(Context pContext) {
        mContext = pContext;
    }

    public static Paint getmWhite() {
        if (mWhite == null) {
            mWhite = new Paint();
            mWhite.setAntiAlias(true);
            mWhite.setFilterBitmap(true);
            mWhite.setARGB(250, 255, 255, 255);
            mWhite.setTextSize(70.0f * Constants.Persent);
            mWhite.setStrokeWidth(2.0f * Constants.Persent);
        }
        return mWhite;
    }

    public static Paint getmWhiteCal() {
        if (mWhiteCal == null) {
            mWhiteCal = new Paint();
            mWhiteCal.setAntiAlias(true);
            mWhiteCal.setFilterBitmap(true);
            mWhiteCal.setARGB(250, 255, 255, 255);
            mWhiteCal.setTextSize(50.0f * Constants.Persent);
        }
        return mWhiteCal;
    }

    public static Paint getmWhiteTarget() {
        if (mWhiteSmall == null) {
            mWhiteSmall = new Paint();
            mWhiteSmall.setAntiAlias(true);
            mWhiteSmall.setFilterBitmap(true);
            mWhiteSmall.setARGB(250, 255, 255, 255);
            mWhiteSmall.setTextSize(52.0f * Constants.Persent);
        }
        return mWhiteSmall;
    }

    public static Paint getmLightGreen() {
        if (mLightGreen == null) {
            mLightGreen = new Paint();
            mLightGreen.setAntiAlias(true);
            mLightGreen.setFilterBitmap(true);
            mLightGreen.setARGB(250, 197, 237, 200);
        }
        return mLightGreen;
    }

    public static Paint getmDarkGreen() {
        if (mDarkGreen == null) {
            mDarkGreen = new Paint();
            mDarkGreen.setAntiAlias(true);
            mDarkGreen.setFilterBitmap(true);
            mDarkGreen.setARGB(250, 61, 195, 72);
            mDarkGreen.setStrokeWidth(2.0f * Constants.Persent);
        }
        return mDarkGreen;
    }

    public static Paint getmDarkGreenText() {
        if (mDarkGreenText == null) {
            mDarkGreenText = new Paint();
            mDarkGreenText.setAntiAlias(true);
            mDarkGreenText.setFilterBitmap(true);
            mDarkGreenText.setARGB(250, 61, 195, 72);
            mDarkGreenText.setTextSize(40.0f * Constants.Persent);
        }
        return mDarkGreenText;
    }

    public static Paint getmDarkGreenTextSmall() {
        if (mDarkGreenTextSmall == null) {
            mDarkGreenTextSmall = new Paint();
            mDarkGreenTextSmall.setAntiAlias(true);
            mDarkGreenTextSmall.setFilterBitmap(true);
            mDarkGreenTextSmall.setARGB(250, 61, 195, 72);
            mDarkGreenTextSmall.setTextSize(30.0f * Constants.Persent);
        }
        return mDarkGreenTextSmall;
    }

    public static Paint getmOrange() {
        if (mOrange == null) {
            mOrange = new Paint();
            mOrange.setAntiAlias(true);
            mOrange.setFilterBitmap(true);
            mOrange.setARGB(250, 239, 124, 31);
            mOrange.setStrokeWidth(2.0f * Constants.Persent);
        }
        return mOrange;
    }

    public static Paint getmOrangeText() {
        if (mOrangeText == null) {
            mOrangeText = new Paint();
            mOrangeText.setAntiAlias(true);
            mOrangeText.setFilterBitmap(true);
            mOrangeText.setARGB(250, 239, 124, 31);
            mOrangeText.setTextSize(32.0f * Constants.Persent);
        }
        return mOrangeText;
    }
}
