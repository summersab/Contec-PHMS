package com.contec.phms.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class UIUtils {
    public static int dp2px(Context context, float dpVal) {
        return (int) TypedValue.applyDimension(1, dpVal, context.getResources().getDisplayMetrics());
    }

    public static int sp2px(Context context, float spVal) {
        return (int) TypedValue.applyDimension(2, spVal, context.getResources().getDisplayMetrics());
    }

    public static float px2dp(Context context, float pxVal) {
        return pxVal / context.getResources().getDisplayMetrics().density;
    }

    public static float px2sp(Context context, float pxVal) {
        return pxVal / context.getResources().getDisplayMetrics().scaledDensity;
    }

    public static void setViewSize(View view, int width, int height) {
        ViewGroup.LayoutParams params;
        if (view != null && (params = view.getLayoutParams()) != null) {
            params.width = width;
            params.height = height;
        }
    }

    public static void removeParent(View view) {
        if (view != null && view.getParent() != null) {
            ((ViewGroup) view.getParent()).removeView(view);
        }
    }

    public static int getScreenWidth(Context context) {
        DisplayMetrics outMetrics = new DisplayMetrics();
        ((WindowManager) context.getSystemService("window")).getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.widthPixels;
    }

    public static int getScreenHeight(Context context) {
        DisplayMetrics outMetrics = new DisplayMetrics();
        ((WindowManager) context.getSystemService("window")).getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.heightPixels;
    }

    public static int getStatusHeight(Context context) {
        try {
            Class<?> clazz = Class.forName("com.android.internal.R$dimen");
            return context.getResources().getDimensionPixelSize(Integer.parseInt(clazz.getField("status_bar_height").get(clazz.newInstance()).toString()));
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public static Bitmap snapShotWithStatusBar(Activity activity) {
        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap bp = Bitmap.createBitmap(view.getDrawingCache(), 0, 0, getScreenWidth(activity), getScreenHeight(activity));
        view.destroyDrawingCache();
        return bp;
    }

    public static Bitmap snapShotWithoutStatusBar(Activity activity) {
        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap bmp = view.getDrawingCache();
        Rect frame = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int statusBarHeight = frame.top;
        Bitmap bp = Bitmap.createBitmap(bmp, 0, statusBarHeight, getScreenWidth(activity), getScreenHeight(activity) - statusBarHeight);
        view.destroyDrawingCache();
        return bp;
    }

    public static void titleLayoutToPad(Context context, RelativeLayout title_layout, Button title_back, Button title_next) {
        title_layout.getLayoutParams().width = -1;
        title_layout.getLayoutParams().height += 30;
        title_back.getLayoutParams().width += 25;
        title_back.getLayoutParams().height += 25;
        title_next.getLayoutParams().width += 25;
        title_next.getLayoutParams().height += 25;
    }

    public static void titleLayoutToPad(Context context, RelativeLayout title_layout) {
        title_layout.getLayoutParams().width = -1;
        title_layout.getLayoutParams().height += 30;
    }

    public static void titleLayoutToPad(Context context, RelativeLayout title_layout, Button title_back) {
        title_layout.getLayoutParams().width = -1;
        title_layout.getLayoutParams().height += 30;
        title_back.getLayoutParams().width += 25;
        title_back.getLayoutParams().height += 25;
    }

    public static boolean isTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout & 15) >= 3;
    }

    public static void changeLayoutTextSize(Context mcontext, ViewGroup mviewgroup, int maddsize) {
        for (int i = 0; i < mviewgroup.getChildCount(); i++) {
            View v = mviewgroup.getChildAt(i);
            if (v instanceof ViewGroup) {
                changeLayoutTextSize(mcontext, (ViewGroup) v, maddsize);
            } else if (v instanceof ImageView) {
                ImageView imageView = (ImageView) mviewgroup.getChildAt(i);
            } else if (v instanceof TextView) {
                TextView myTextView = (TextView) mviewgroup.getChildAt(i);
                myTextView.setTextSize(0, ((float) maddsize) + px2sp(mcontext, Float.valueOf(myTextView.getTextSize()).floatValue()));
            } else if (v instanceof Button) {
                Button myButtonView = (Button) mviewgroup.getChildAt(i);
                myButtonView.setTextSize(0, ((float) maddsize) + px2sp(mcontext, Float.valueOf(myButtonView.getTextSize()).floatValue()));
            }
        }
    }

    public static void changeLayoutSize(Context mcontext, ViewGroup mviewgroup, int maddsize) {
        for (int i = 0; i < mviewgroup.getChildCount(); i++) {
            View v = mviewgroup.getChildAt(i);
            if (v instanceof ViewGroup) {
                changeLayoutTextSize(mcontext, (ViewGroup) v, maddsize);
            } else if (v instanceof ImageView) {
                ImageView imageView = (ImageView) mviewgroup.getChildAt(i);
            } else if (v instanceof TextView) {
                ((TextView) mviewgroup.getChildAt(i)).setTextSize(0, (float) maddsize);
            } else if (v instanceof Button) {
                ((Button) mviewgroup.getChildAt(i)).setTextSize(0, (float) maddsize);
            }
        }
    }

    public static void changeLayoutSize(Context mcontext, ViewGroup mviewgroup, int paddwidth, int paddheight) {
        for (int i = 0; i < mviewgroup.getChildCount(); i++) {
            View v = mviewgroup.getChildAt(i);
            if (v instanceof ViewGroup) {
                changeLayoutSize(mcontext, (ViewGroup) v, paddwidth, paddheight);
            } else if (v instanceof View) {
                v.getLayoutParams().width += dp2px(mcontext, (float) paddwidth);
                v.getLayoutParams().height += dp2px(mcontext, (float) paddheight);
            }
        }
    }
}
