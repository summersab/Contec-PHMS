package com.zxing.android.decoding;

import android.content.DialogInterface;
import androidx.appcompat.app.AppCompatActivity;

public final class FinishListener implements DialogInterface.OnClickListener, DialogInterface.OnCancelListener, Runnable {
    private final AppCompatActivity activityToFinish;

    public FinishListener(AppCompatActivity activityToFinish2) {
        this.activityToFinish = activityToFinish2;
    }

    public void onCancel(DialogInterface dialogInterface) {
        run();
    }

    public void onClick(DialogInterface dialogInterface, int i) {
        run();
    }

    public void run() {
        this.activityToFinish.finish();
    }
}
