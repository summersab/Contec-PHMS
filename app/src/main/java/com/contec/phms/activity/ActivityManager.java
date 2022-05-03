package com.contec.phms.activity;

import android.app.Activity;
import android.content.Context;
import java.util.Iterator;
import java.util.Stack;

public class ActivityManager {
    private static final String TAG = ActivityManager.class.getSimpleName();
    private static Stack<Activity> activityStack;
    private static ActivityManager instance;

    private ActivityManager() {
    }

    public static ActivityManager getActivityManager() {
        if (instance == null) {
            instance = new ActivityManager();
        }
        return instance;
    }

    public void addActivity(Activity activity) {
        if (activityStack == null) {
            activityStack = new Stack<>();
        }
        activityStack.add(activity);
    }

    public Activity getCurrentActivity() {
        return (Activity) activityStack.lastElement();
    }

    public void finishCurrentActivity() {
        finishActivity((Activity) activityStack.lastElement());
    }

    public void finishActivity(Activity activity) {
        if (activity != null) {
            activityStack.remove(activity);
            activity.finish();
        }
    }

    public void finishActivity(Class<?> cls) {
        Iterator it = activityStack.iterator();
        while (it.hasNext()) {
            Activity activity = (Activity) it.next();
            if (activity.getClass().equals(cls)) {
                finishActivity(activity);
            }
        }
    }

    public void finishAllActivity() {
        int size = activityStack.size();
        for (int i = 0; i < size; i++) {
            if (activityStack.get(i) != null) {
                ((Activity) activityStack.get(i)).finish();
            }
        }
        activityStack.clear();
    }

    public void exitApplication(Context context) {
        try {
            finishAllActivity();
            System.exit(0);
        } catch (Exception e) {
        }
    }

    public int getActivityCount() {
        if (activityStack != null) {
            return activityStack.size();
        }
        return 0;
    }
}
