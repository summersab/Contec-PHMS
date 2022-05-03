package com.contec.phms.widget;

public interface OnWheelScrollListener {
    void onScrollingFinished(BirthdayWheelView birthdayWheelView);

    void onScrollingFinished(WheelView wheelView);

    void onScrollingStarted(BirthdayWheelView birthdayWheelView);

    void onScrollingStarted(WheelView wheelView);
}
