package com.contec.phms.widget;

import android.view.View;
import android.widget.LinearLayout;

public class NumericWheelAdapter implements WheelAdapter {
    public static final int DEFAULT_MAX_VALUE = 9;
    private static final int DEFAULT_MIN_VALUE = 0;
    private String format;
    private int maxValue;
    private int minValue;

    public NumericWheelAdapter() {
        this(0, 9);
    }

    public NumericWheelAdapter(int minValue2, int maxValue2) {
        this(minValue2, maxValue2, (String) null);
    }

    public NumericWheelAdapter(int minValue2, int maxValue2, String format2) {
        this.minValue = minValue2;
        this.maxValue = maxValue2;
        this.format = format2;
    }

    public String getItem(int index) {
        if (index < 0 || index >= getItemsCount()) {
            return null;
        }
        int value = this.minValue + index;
        if (this.format == null) {
            return Integer.toString(value);
        }
        return String.format(this.format, new Object[]{Integer.valueOf(value)});
    }

    public int getItemsCount() {
        return (this.maxValue - this.minValue) + 1;
    }

    public int getMaximumLength() {
        int maxLen = Integer.toString(Math.max(Math.abs(this.maxValue), Math.abs(this.minValue))).length();
        if (this.minValue < 0) {
            return maxLen + 1;
        }
        return maxLen;
    }

    public View getItem(int index, View view, LinearLayout itemsLayout) {
        return null;
    }
}
