package com.contec.phms.util;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ScreenAdapter {
    public static int dip2px(Context context, float dpValue) {
        return (int) ((dpValue * context.getResources().getDisplayMetrics().density) + 0.5f);
    }

    public static int px2dip(Context context, float pxValue) {
        return (int) ((pxValue / context.getResources().getDisplayMetrics().density) + 0.5f);
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
                myTextView.setTextSize(0, (float) (px2sp(mcontext, Float.valueOf(myTextView.getTextSize()).floatValue()) + maddsize));
            } else if (v instanceof Button) {
                Button myButtonView = (Button) mviewgroup.getChildAt(i);
                myButtonView.setTextSize(0, (float) (px2sp(mcontext, Float.valueOf(myButtonView.getTextSize()).floatValue()) + maddsize));
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
                v.getLayoutParams().width += dip2px(mcontext, (float) paddwidth);
                v.getLayoutParams().height += dip2px(mcontext, (float) paddheight);
            }
        }
    }

    public static int px2sp(Context context, float pxValue) {
        return (int) ((pxValue / context.getResources().getDisplayMetrics().scaledDensity) + 0.5f);
    }
}
