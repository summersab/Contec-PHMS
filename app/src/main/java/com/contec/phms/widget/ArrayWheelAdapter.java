package com.contec.phms.widget;

import android.view.View;
import android.widget.LinearLayout;

public class ArrayWheelAdapter<T> implements WheelAdapter {
    public static final int DEFAULT_LENGTH = -1;
    private T[] items;
    private int length;

    public ArrayWheelAdapter(T[] items2, int length2) {
        this.items = items2;
        this.length = length2;
    }

    public ArrayWheelAdapter(T[] items2) {
        this(items2, -1);
    }

    public String getItem(int index) {
        if (index < 0 || index >= this.items.length) {
            return null;
        }
        return this.items[index].toString();
    }

    public int getItemsCount() {
        return this.items.length;
    }

    public int getMaximumLength() {
        return this.length;
    }

    public View getItem(int index, View view, LinearLayout itemsLayout) {
        return null;
    }
}
