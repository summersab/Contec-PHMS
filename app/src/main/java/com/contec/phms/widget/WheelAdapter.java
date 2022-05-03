package com.contec.phms.widget;

import android.view.View;
import android.widget.LinearLayout;

public interface WheelAdapter {
    View getItem(int i, View view, LinearLayout linearLayout);

    String getItem(int i);

    int getItemsCount();

    int getMaximumLength();
}
