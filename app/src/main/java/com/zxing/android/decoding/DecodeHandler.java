package com.zxing.android.decoding;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.ReaderException;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import com.zxing.android.CaptureActivity;
import com.zxing.android.camera.PlanarYUVLuminanceSource;
import java.util.Hashtable;

final class DecodeHandler extends Handler {
    private static final String TAG = DecodeHandler.class.getSimpleName();
    private final CaptureActivity activity;
    private final MultiFormatReader multiFormatReader = new MultiFormatReader();

    DecodeHandler(CaptureActivity activity2, Hashtable<DecodeHintType, Object> hints) {
        this.multiFormatReader.setHints(hints);
        this.activity = activity2;
    }

    public void handleMessage(Message message) {
        switch (message.what) {
            case 2:
                decode((byte[]) message.obj, message.arg1, message.arg2);
                return;
            case 8:
                Looper.myLooper().quit();
                return;
            default:
                return;
        }
    }

    private void decode(byte[] data, int width, int height) {
        long start = System.currentTimeMillis();
        Result rawResult = null;
        byte[] rotatedData = new byte[data.length];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                rotatedData[(((x * height) + height) - y) - 1] = data[(y * width) + x];
            }
        }
        int tmp = width;
        PlanarYUVLuminanceSource source = this.activity.getCameraManager().buildLuminanceSource(rotatedData, height, tmp);
        try {
            rawResult = this.multiFormatReader.decodeWithState(new BinaryBitmap(new HybridBinarizer(source)));
        } catch (ReaderException e) {
        } finally {
            this.multiFormatReader.reset();
        }
        if (rawResult != null) {
            Log.d(TAG, "Found barcode (" + (System.currentTimeMillis() - start) + " ms):\n" + rawResult.toString());
            Message message = Message.obtain(this.activity.getHandler(), 4, rawResult);
            Bundle bundle = new Bundle();
            bundle.putParcelable(DecodeThread.BARCODE_BITMAP, source.renderCroppedGreyscaleBitmap());
            message.setData(bundle);
            message.sendToTarget();
            return;
        }
        Message.obtain(this.activity.getHandler(), 3).sendToTarget();
    }
}
