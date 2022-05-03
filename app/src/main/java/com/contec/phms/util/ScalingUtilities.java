package com.contec.phms.util;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

public class ScalingUtilities {

    public enum ScalingLogic {
        FIT
    }

    public static Bitmap decodeResource(Resources res, int resId, int dstWidth, int dstHeight, ScalingLogic scalingLogic) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);
        options.inJustDecodeBounds = false;
        options.inSampleSize = calculateSampleSize(options.outWidth, options.outHeight, dstWidth, dstHeight, scalingLogic);
        return BitmapFactory.decodeResource(res, resId, options);
    }

    public static Bitmap decodeResourceByPaht(String picpath, int dstWidth, int dstHeight, ScalingLogic scalingLogic) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(picpath, options);
        options.inJustDecodeBounds = false;
        options.inPreferredConfig = Bitmap.Config.ARGB_4444;
        options.inSampleSize = calculateSampleSize(options.outWidth, options.outHeight, dstWidth, dstHeight, scalingLogic);
        return BitmapFactory.decodeFile(picpath, options);
    }

    public static Bitmap createScaledBitmap(Bitmap unscaledBitmap, int dstWidth, int dstHeight, ScalingLogic scalingLogic) {
        Rect srcRect = calculateSrcRect(unscaledBitmap.getWidth(), unscaledBitmap.getHeight(), dstWidth, dstHeight, scalingLogic);
        Rect dstRect = calculateDstRect(unscaledBitmap.getWidth(), unscaledBitmap.getHeight(), dstWidth, dstHeight, scalingLogic);
        Bitmap scaledBitmap = Bitmap.createBitmap(dstRect.width(), dstRect.height(), Bitmap.Config.ARGB_4444);
        new Canvas(scaledBitmap).drawBitmap(unscaledBitmap, srcRect, dstRect, new Paint(2));
        return scaledBitmap;
    }

    public static int calculateSampleSize(int srcWidth, int srcHeight, int dstWidth, int dstHeight, ScalingLogic scalingLogic) {
        if (((float) srcWidth) / ((float) srcHeight) > ((float) dstWidth) / ((float) dstHeight)) {
            return srcHeight / dstHeight;
        }
        return srcWidth / dstWidth;
    }

    public static Rect calculateSrcRect(int srcWidth, int srcHeight, int dstWidth, int dstHeight, ScalingLogic scalingLogic) {
        return new Rect(0, 0, srcWidth, srcHeight);
    }

    public static Rect calculateDstRect(int srcWidth, int srcHeight, int dstWidth, int dstHeight, ScalingLogic scalingLogic) {
        return new Rect(0, 0, dstWidth, dstHeight);
    }
}
