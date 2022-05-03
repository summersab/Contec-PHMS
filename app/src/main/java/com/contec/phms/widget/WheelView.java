package com.contec.phms.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Handler;
import android.os.Message;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Interpolator;
import android.widget.Scroller;
import android.widget.TextView;
import com.contec.phms.R;
import com.contec.phms.util.Constants;
import java.util.LinkedList;
import java.util.List;
import u.aly.bs;

public class WheelView extends View {
    private static final int ADDITIONAL_ITEMS_SPACE = 5;
    private static final int ADDITIONAL_ITEM_HEIGHT = ((int) (((double) Constants.SCREEN_HEIGHT) * 0.04d));
    private static final int DEF_VISIBLE_ITEMS = 7;
    private static final int ITEMS_TEXT_COLOR = -2829100;
    private static final int TEXT_SIZE = ((int) (((double) Constants.SCREEN_HEIGHT) * 0.02d));
    private static final int ITEM_OFFSET = (TEXT_SIZE / 5);
    private static final int LABEL_OFFSET = 8;
    private static final int LABEL_TEXT_COLOR = -13421773;
    private static final int MIN_DELTA_FOR_SCROLLING = 1;
    private static final int PADDING = 10;
    private static final int SCROLLING_DURATION = 400;
    private static final int[] SHADOWS_COLORS = {ITEMS_TEXT_COLOR, 16777215, 16777215};
    private static final int VALUE_TEXT_COLOR = -13382560;
    private final int MESSAGE_JUSTIFY = 1;
    private final int MESSAGE_SCROLL = 0;
    private WheelAdapter adapter = null;
    private Handler animationHandler = new Handler() {
        public void handleMessage(Message msg) {
            WheelView.this.scroller.computeScrollOffset();
            int currY = WheelView.this.scroller.getCurrY();
            int delta = WheelView.this.lastScrollY - currY;
            WheelView.this.lastScrollY = currY;
            if (delta != 0) {
                WheelView.this.doScroll(delta);
            }
            if (Math.abs(currY - WheelView.this.scroller.getFinalY()) < 1) {
                int currY2 = WheelView.this.scroller.getFinalY();
                WheelView.this.scroller.forceFinished(true);
            }
            if (!WheelView.this.scroller.isFinished()) {
                WheelView.this.animationHandler.sendEmptyMessage(msg.what);
            } else if (msg.what == 0) {
                WheelView.this.justify();
            } else {
                WheelView.this.finishScrolling();
            }
        }
    };
    private GradientDrawable bottomShadow;
    private Drawable centerDrawable;
    private List<OnWheelChangedListener> changingListeners = new LinkedList();
    private int currentItem = 75;
    private GestureDetector gestureDetector;
    private GestureDetector.SimpleOnGestureListener gestureListener = new GestureDetector.SimpleOnGestureListener() {
        public boolean onDown(MotionEvent e) {
            if (!WheelView.this.isScrollingPerformed) {
                return false;
            }
            WheelView.this.scroller.forceFinished(true);
            WheelView.this.clearMessages();
            return true;
        }

        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            WheelView.this.startScrolling();
            WheelView.this.doScroll((int) (-distanceY));
            return true;
        }

        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            int minY;
            WheelView.this.lastScrollY = (WheelView.this.currentItem * WheelView.this.getItemHeight()) + WheelView.this.scrollingOffset;
            int maxY = WheelView.this.isCyclic ? Integer.MAX_VALUE : WheelView.this.adapter.getItemsCount() * WheelView.this.getItemHeight();
            if (WheelView.this.isCyclic) {
                minY = -maxY;
            } else {
                minY = 0;
            }
            WheelView.this.scroller.fling(0, WheelView.this.lastScrollY, 0, ((int) (-velocityY)) / 2, 0, 0, minY, maxY);
            WheelView.this.setNextMessage(0);
            return true;
        }
    };
    boolean isCyclic = true;
    private boolean isScrollingPerformed;
    private int itemHeight = 0;
    private Layout itemsLayout;
    private TextPaint itemsPaint;
    private int itemsWidth = 0;
    private String label;
    private Layout labelLayout;
    private Layout labelLayouts;
    private TextPaint labelPaint;
    private int labelWidth = 0;
    private String labels;
    private int lastScrollY;
    private Scroller scroller;
    private List<OnWheelScrollListener> scrollingListeners = new LinkedList();
    private int scrollingOffset;
    private GradientDrawable topShadow;
    private TextView tv;
    private float tvHeight;
    private float tvWidth;
    private Layout valueLayout;
    private TextPaint valuePaint;
    private int visibleItems = 7;
    private WheelView wheel;

    public WheelView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initData(context);
    }

    public WheelView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initData(context);
    }

    public WheelView(Context context) {
        super(context);
        initData(context);
    }

    @SuppressLint({"NewApi"})
    private void initData(Context context) {
        this.gestureDetector = new GestureDetector(context, this.gestureListener);
        this.gestureDetector.setIsLongpressEnabled(false);
        this.scroller = new Scroller(context);
    }

    public WheelAdapter getAdapter() {
        return this.adapter;
    }

    public void setAdapter(WheelAdapter adapter2) {
        this.adapter = adapter2;
        invalidateLayouts();
        invalidate();
    }

    public void setInterpolator(Interpolator interpolator) {
        this.scroller.forceFinished(true);
        this.scroller = new Scroller(getContext(), interpolator);
    }

    public int getVisibleItems() {
        return this.visibleItems;
    }

    public void setVisibleItems(int count) {
        this.visibleItems = count;
        invalidate();
    }

    public String getLabel() {
        return this.label;
    }

    public String getLabels() {
        return this.labels;
    }

    public void setLabel(String newLabel) {
        if (this.label == null || !this.label.equals(newLabel)) {
            this.label = newLabel;
            this.labelLayout = null;
            this.labelLayouts = null;
            invalidate();
        }
    }

    public void setLabels(String newLabel) {
        if (this.labels == null || !this.labels.equals(newLabel)) {
            this.labels = newLabel;
            this.labelLayout = null;
            this.labelLayouts = null;
            invalidate();
        }
    }

    public void addChangingListener(OnWheelChangedListener listener) {
        this.changingListeners.add(listener);
    }

    public void removeChangingListener(OnWheelChangedListener listener) {
        this.changingListeners.remove(listener);
    }

    protected void notifyChangingListeners(int oldValue, int newValue) {
        for (OnWheelChangedListener listener : this.changingListeners) {
            listener.onChanged(this, oldValue, newValue);
        }
    }

    public void addScrollingListener(OnWheelScrollListener listener) {
        this.scrollingListeners.add(listener);
    }

    public void removeScrollingListener(OnWheelScrollListener listener) {
        this.scrollingListeners.remove(listener);
    }

    protected void notifyScrollingListenersAboutStart() {
        for (OnWheelScrollListener listener : this.scrollingListeners) {
            listener.onScrollingStarted(this);
        }
    }

    protected void notifyScrollingListenersAboutEnd() {
        for (OnWheelScrollListener listener : this.scrollingListeners) {
            listener.onScrollingFinished(this);
        }
    }

    public int getCurrentItem() {
        return this.currentItem;
    }

    public void setCurrentItem(int index, boolean animated) {
        if (this.adapter != null && this.adapter.getItemsCount() != 0) {
            if (index < 0 || index >= this.adapter.getItemsCount()) {
                if (this.isCyclic) {
                    while (index < 0) {
                        index += this.adapter.getItemsCount();
                    }
                    index %= this.adapter.getItemsCount();
                } else {
                    return;
                }
            }
            if (index == this.currentItem) {
                return;
            }
            if (animated) {
                scroll(index - this.currentItem, 400);
                return;
            }
            invalidateLayouts();
            int old = this.currentItem;
            this.currentItem = index;
            notifyChangingListeners(old, this.currentItem);
            invalidate();
        }
    }

    public void setCurrentItem(int index) {
        setCurrentItem(index, false);
    }

    public boolean isCyclic() {
        return this.isCyclic;
    }

    public void setCyclic(boolean isCyclic2) {
        this.isCyclic = isCyclic2;
        invalidate();
        invalidateLayouts();
    }

    private void invalidateLayouts() {
        this.itemsLayout = null;
        this.valueLayout = null;
        this.scrollingOffset = 0;
    }

    private void initResourcesIfNecessary() {
        if (this.itemsPaint == null) {
            this.itemsPaint = new TextPaint(129);
            this.itemsPaint.setTextSize((float) TEXT_SIZE);
        }
        if (this.valuePaint == null) {
            this.valuePaint = new TextPaint(37);
            this.valuePaint.setTextSize((float) TEXT_SIZE);
        }
        if (this.labelPaint == null) {
            this.labelPaint = new TextPaint(5);
            this.labelPaint.setTextSize((float) TEXT_SIZE);
        }
        if (this.centerDrawable == null) {
            this.centerDrawable = getContext().getResources().getDrawable(R.drawable.wheel_val);
        }
        if (this.topShadow == null) {
            this.topShadow = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, SHADOWS_COLORS);
        }
        if (this.bottomShadow == null) {
            this.bottomShadow = new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP, SHADOWS_COLORS);
        }
        setBackgroundResource(R.drawable.wheel_bg);
    }

    private int getDesiredHeight(Layout layout) {
        if (layout == null) {
            return 0;
        }
        return Math.max(((getItemHeight() * this.visibleItems) - (ITEM_OFFSET * 2)) - ADDITIONAL_ITEM_HEIGHT, getSuggestedMinimumHeight());
    }

    public String getTextItem(int index) {
        if (this.adapter == null || this.adapter.getItemsCount() == 0) {
            return null;
        }
        int count = this.adapter.getItemsCount();
        if ((index < 0 || index >= count) && !this.isCyclic) {
            return null;
        }
        while (index < 0) {
            index += count;
        }
        return this.adapter.getItem(index % count);
    }

    private String buildText(boolean useCurrentValue) {
        String text;
        StringBuilder itemsText = new StringBuilder();
        int addItems = (this.visibleItems / 2) + 1;
        for (int i = this.currentItem - addItems; i <= this.currentItem + addItems; i++) {
            if ((useCurrentValue || i != this.currentItem) && (text = getTextItem(i)) != null) {
                itemsText.append(text);
            }
            if (i < this.currentItem + addItems) {
                itemsText.append("\n");
            }
        }
        return itemsText.toString();
    }

    private int getMaxTextLength() {
        WheelAdapter adapter2 = getAdapter();
        if (adapter2 == null) {
            return 0;
        }
        int adapterLength = adapter2.getMaximumLength();
        if (adapterLength > 0) {
            return adapterLength;
        }
        String maxText = null;
        for (int i = Math.max(this.currentItem - (this.visibleItems / 2), 0); i < Math.min(this.currentItem + this.visibleItems, adapter2.getItemsCount()); i++) {
            String text = adapter2.getItem(i);
            if (text != null && (maxText == null || maxText.length() < text.length())) {
                maxText = text;
            }
        }
        if (maxText != null) {
            return maxText.length();
        }
        return 0;
    }

    private int getItemHeight() {
        if (this.itemHeight != 0) {
            return this.itemHeight;
        }
        if (this.itemsLayout == null || this.itemsLayout.getLineCount() <= 2) {
            return getHeight() / this.visibleItems;
        }
        this.itemHeight = this.itemsLayout.getLineTop(2) - this.itemsLayout.getLineTop(1);
        return this.itemHeight;
    }

    private int calculateLayoutWidth(int widthSize, int mode) {
        int width;
        initResourcesIfNecessary();
        int i = widthSize;
        int maxLength = getMaxTextLength();
        if (maxLength > 0) {
            this.itemsWidth = (int) (((float) maxLength) * Math.ceil(Layout.getDesiredWidth("0", this.itemsPaint)));
        } else {
            this.itemsWidth = 0;
        }
        this.itemsWidth += 5;
        this.labelWidth = 0;
        if (this.label != null && this.label.length() > 0) {
            this.labelWidth = (int) Math.ceil(Layout.getDesiredWidth(this.label, this.valuePaint));
        }
        if (this.labels != null && this.labels.length() > 0) {
            this.labelWidth = (int) Math.ceil(Layout.getDesiredWidth(this.labels, this.labelPaint));
        }
        boolean recalculate = false;
        if (mode == 1073741824) {
            width = widthSize;
            recalculate = true;
        } else {
            int width2 = this.itemsWidth + this.labelWidth + 20;
            if (this.labelWidth > 0) {
                width2 += 8;
            }
            width = Math.max(width2, getSuggestedMinimumWidth());
            if (mode == Integer.MIN_VALUE && widthSize < width) {
                width = widthSize;
                recalculate = true;
            }
        }
        if (recalculate) {
            int pureWidth = (width - 8) - 20;
            if (pureWidth <= 0) {
                this.labelWidth = 0;
                this.itemsWidth = 0;
            }
            if (this.labelWidth > 0) {
                this.itemsWidth = (int) ((((double) this.itemsWidth) * ((double) pureWidth)) / ((double) (this.itemsWidth + this.labelWidth)));
                this.labelWidth = pureWidth - this.itemsWidth;
            } else {
                this.itemsWidth = pureWidth + 8;
            }
        }
        if (this.itemsWidth > 0) {
            createLayouts(this.itemsWidth, this.labelWidth);
        }
        return width;
    }

    private void createLayouts(int widthItems, int widthLabel) {
        String text = null;
        if (this.itemsLayout == null || this.itemsLayout.getWidth() > widthItems) {
            this.itemsLayout = new StaticLayout(buildText(this.isScrollingPerformed), this.itemsPaint, widthItems, widthLabel > 0 ? Layout.Alignment.ALIGN_OPPOSITE : Layout.Alignment.ALIGN_CENTER, 1.0f, (float) ADDITIONAL_ITEM_HEIGHT, false);
        } else {
            this.itemsLayout.increaseWidthTo(widthItems);
        }
        if (!this.isScrollingPerformed && (this.valueLayout == null || this.valueLayout.getWidth() > widthItems)) {
            if (getAdapter() != null) {
                text = getAdapter().getItem(this.currentItem);
            }
            this.valueLayout = new StaticLayout(text != null ? text : bs.b, this.valuePaint, widthItems, widthLabel > 0 ? Layout.Alignment.ALIGN_OPPOSITE : Layout.Alignment.ALIGN_CENTER, 1.0f, (float) ADDITIONAL_ITEM_HEIGHT, false);
            this.labelLayout = new StaticLayout(text != null ? text : bs.b, this.labelPaint, widthItems, widthLabel > 0 ? Layout.Alignment.ALIGN_OPPOSITE : Layout.Alignment.ALIGN_CENTER, 1.0f, (float) ADDITIONAL_ITEM_HEIGHT, false);
        } else if (this.isScrollingPerformed) {
            this.valueLayout = null;
        } else {
            this.valueLayout.increaseWidthTo(widthItems);
        }
        if (widthLabel > 0) {
            if (this.labelLayout == null || this.labelLayout.getWidth() > widthLabel) {
                this.labelLayout = new StaticLayout(this.label, this.labelPaint, widthLabel, Layout.Alignment.ALIGN_NORMAL, 1.0f, (float) ADDITIONAL_ITEM_HEIGHT, false);
            } else {
                this.labelLayout.increaseWidthTo(widthLabel);
            }
            if (this.labelLayouts == null || this.labelLayouts.getWidth() > widthLabel) {
                this.labelLayouts = new StaticLayout(this.labels, this.labelPaint, widthLabel, Layout.Alignment.ALIGN_NORMAL, 1.0f, (float) ADDITIONAL_ITEM_HEIGHT, false);
            } else {
                this.labelLayout.increaseWidthTo(widthLabel);
            }
        }
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int height;
        int widthMode = View.MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = View.MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = View.MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = View.MeasureSpec.getSize(heightMeasureSpec);
        int width = calculateLayoutWidth(widthSize, widthMode);
        if (heightMode == 1073741824) {
            height = heightSize;
        } else {
            height = getDesiredHeight(this.itemsLayout);
            if (heightMode == Integer.MIN_VALUE) {
                height = Math.min(height, heightSize);
            }
        }
        setMeasuredDimension(width, height);
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (this.itemsLayout == null) {
            if (this.itemsWidth == 0) {
                calculateLayoutWidth(getWidth(), 1073741824);
            } else {
                createLayouts(this.itemsWidth, this.labelWidth);
            }
        }
        if (this.itemsWidth > 0) {
            canvas.save();
            canvas.translate(10.0f, (float) (-ITEM_OFFSET));
            drawItems(canvas);
            drawValue(canvas);
            canvas.restore();
        }
        drawCenterRect(canvas);
    }

    private void drawValue(Canvas canvas) {
        this.valuePaint.setColor(VALUE_TEXT_COLOR);
        this.labelPaint.setColor(LABEL_TEXT_COLOR);
        this.valuePaint.drawableState = getDrawableState();
        this.labelPaint.drawableState = getDrawableState();
        Rect bounds = new Rect();
        this.itemsLayout.getLineBounds(this.visibleItems / 2, bounds);
        if (this.labelLayout != null) {
            canvas.save();
            canvas.translate((float) (this.itemsLayout.getWidth() / 8), (float) bounds.top);
            this.labelLayout.draw(canvas);
            canvas.restore();
        }
        if (this.labelLayouts != null) {
            canvas.save();
            canvas.translate((float) this.itemsLayout.getWidth(), (float) bounds.top);
            this.labelLayouts.draw(canvas);
            canvas.restore();
        }
        if (this.valueLayout != null) {
            canvas.save();
            canvas.translate(-50.0f, (float) (bounds.top + this.scrollingOffset));
            this.valueLayout.draw(canvas);
            canvas.restore();
        }
    }

    private void drawItems(Canvas canvas) {
        canvas.save();
        canvas.translate(-50.0f, (float) ((-this.itemsLayout.getLineTop(1)) + this.scrollingOffset));
        this.itemsPaint.setColor(ITEMS_TEXT_COLOR);
        this.itemsPaint.drawableState = getDrawableState();
        this.itemsLayout.draw(canvas);
        canvas.restore();
    }

    private void drawCenterRect(Canvas canvas) {
        int center = getHeight() / 2;
        int offset = (int) (((double) (getItemHeight() / 2)) * 1.2d);
        Paint paint = new Paint();
        paint.setColor(getResources().getColor(R.color.gray));
        paint.setStrokeWidth(3.0f);
        canvas.drawLine(0.0f, (float) (center - offset), (float) getWidth(), (float) (center - offset), paint);
        canvas.drawLine(0.0f, (float) (center + offset), (float) getWidth(), (float) (center + offset), paint);
    }

    public boolean onTouchEvent(MotionEvent event) {
        if (getAdapter() != null && !this.gestureDetector.onTouchEvent(event) && event.getAction() == 1) {
            justify();
        }
        return true;
    }

    private void doScroll(int delta) {
        this.scrollingOffset += delta;
        int count = this.scrollingOffset / getItemHeight();
        int pos = this.currentItem - count;
        if (this.isCyclic && this.adapter.getItemsCount() > 0) {
            while (pos < 0) {
                pos += this.adapter.getItemsCount();
            }
            pos %= this.adapter.getItemsCount();
        } else if (!this.isScrollingPerformed) {
            pos = Math.min(Math.max(pos, 0), this.adapter.getItemsCount() - 1);
        } else if (pos < 0) {
            count = this.currentItem;
            pos = 0;
        } else if (pos >= this.adapter.getItemsCount()) {
            count = (this.currentItem - this.adapter.getItemsCount()) + 1;
            pos = this.adapter.getItemsCount() - 1;
        }
        int offset = this.scrollingOffset;
        if (pos != this.currentItem) {
            setCurrentItem(pos, false);
        } else {
            invalidate();
        }
        this.scrollingOffset = offset - (getItemHeight() * count);
        if (this.scrollingOffset > getHeight()) {
            this.scrollingOffset = (this.scrollingOffset % getHeight()) + getHeight();
        }
    }

    private void setNextMessage(int message) {
        clearMessages();
        this.animationHandler.sendEmptyMessage(message);
    }

    private void clearMessages() {
        this.animationHandler.removeMessages(0);
        this.animationHandler.removeMessages(1);
    }

    private void justify() {
        if (this.adapter != null) {
            this.lastScrollY = 0;
            int offset = this.scrollingOffset;
            int itemHeight2 = getItemHeight();
            boolean needToIncrease = offset > 0 ? this.currentItem < this.adapter.getItemsCount() : this.currentItem > 0;
            if ((this.isCyclic || needToIncrease) && Math.abs((float) offset) > ((float) itemHeight2) / 2.0f) {
                offset = offset < 0 ? offset + itemHeight2 + 1 : offset - (itemHeight2 + 1);
            }
            if (Math.abs(offset) > 1) {
                this.scroller.startScroll(0, 0, 0, offset, 400);
                setNextMessage(1);
                return;
            }
            finishScrolling();
        }
    }

    private void startScrolling() {
        if (!this.isScrollingPerformed) {
            this.isScrollingPerformed = true;
            notifyScrollingListenersAboutStart();
        }
    }

    void finishScrolling() {
        if (this.isScrollingPerformed) {
            notifyScrollingListenersAboutEnd();
            this.isScrollingPerformed = false;
        }
        invalidateLayouts();
        invalidate();
    }

    public void scroll(int itemsToScroll, int time) {
        this.scroller.forceFinished(true);
        this.lastScrollY = this.scrollingOffset;
        this.scroller.startScroll(0, this.lastScrollY, 0, (itemsToScroll * getItemHeight()) - this.lastScrollY, time);
        setNextMessage(0);
        startScrolling();
    }
}
