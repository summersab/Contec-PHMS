package com.zxing.android.decoding;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.zxing.android.CaptureActivity;
import com.zxing.android.view.ViewfinderResultPointCallback;
import java.util.Vector;

public final class CaptureActivityHandler extends Handler {
    private static final String TAG = CaptureActivityHandler.class.getSimpleName();
    private final CaptureActivity activity;
    private final DecodeThread decodeThread;
    private State state = State.SUCCESS;

    private enum State {
        PREVIEW,
        SUCCESS,
        DONE
    }

    public CaptureActivityHandler(CaptureActivity activity2, Vector<BarcodeFormat> decodeFormats, String characterSet) {
        this.activity = activity2;
        this.decodeThread = new DecodeThread(activity2, decodeFormats, characterSet, new ViewfinderResultPointCallback(activity2.getViewfinderView()));
        this.decodeThread.start();
        activity2.getCameraManager().startPreview();
        restartPreviewAndDecode();
    }

    public void handleMessage(Message message) {
        Bitmap barcode;
        switch (message.what) {
            case 1:
                if (this.state == State.PREVIEW) {
                    this.activity.getCameraManager().requestAutoFocus(this, 1);
                    return;
                }
                return;
            case 3:
                this.state = State.PREVIEW;
                this.activity.getCameraManager().requestPreviewFrame(this.decodeThread.getHandler(), 2);
                return;
            case 4:
                Log.d(TAG, "Got decode succeeded message");
                this.state = State.SUCCESS;
                Bundle bundle = message.getData();
                if (bundle == null) {
                    barcode = null;
                } else {
                    barcode = (Bitmap) bundle.getParcelable(DecodeThread.BARCODE_BITMAP);
                }
                this.activity.handleDecode((Result) message.obj, barcode);
                return;
            case 7:
                Log.d(TAG, "Got product query message");
                Intent intent = new Intent("android.intent.action.VIEW", Uri.parse((String) message.obj));
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
                this.activity.startActivity(intent);
                return;
            case 8:
                Log.d(TAG, "Got restart preview message");
                restartPreviewAndDecode();
                return;
            case 9:
                Log.d(TAG, "Got return scan result message");
                this.activity.setResult(-1, (Intent) message.obj);
                this.activity.finish();
                return;
            default:
                return;
        }
    }

    public void quitSynchronously() {
        this.state = State.DONE;
        this.activity.getCameraManager().stopPreview();
        Message.obtain(this.decodeThread.getHandler(), 8).sendToTarget();
        try {
            this.decodeThread.join();
        } catch (InterruptedException e) {
        }
        removeMessages(4);
        removeMessages(3);
    }

    private void restartPreviewAndDecode() {
        if (this.state == State.SUCCESS) {
            this.state = State.PREVIEW;
            this.activity.getCameraManager().requestPreviewFrame(this.decodeThread.getHandler(), 2);
            this.activity.getCameraManager().requestAutoFocus(this, 1);
            this.activity.drawViewfinder();
        }
    }
}
