package com.contec.phms.util;

import android.app.Activity;
import java.lang.ref.WeakReference;

public class MyActivityManager {
    private static MyActivityManager sInstance = new MyActivityManager();
    private WeakReference<Activity> sCurrentActivityWeakRef;

    private MyActivityManager() {
    }

    public static MyActivityManager getInstance() {
        return sInstance;
    }

    public Activity getCurrentActivity() {
        if (this.sCurrentActivityWeakRef != null) {
            return (Activity) this.sCurrentActivityWeakRef.get();
        }
        return null;
    }

    public void setCurrentActivity(Activity activity) {
        this.sCurrentActivityWeakRef = new WeakReference<>(activity);
    }
}
