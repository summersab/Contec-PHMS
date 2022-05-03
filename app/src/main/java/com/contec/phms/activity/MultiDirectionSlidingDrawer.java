package com.contec.phms.activity;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SoundEffectConstants;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;
import com.contec.phms.R;

public class MultiDirectionSlidingDrawer extends ViewGroup {
    private static final int ANIMATION_FRAME_DURATION = 16;
    private static final int COLLAPSED_FULL_CLOSED = -10002;
    private static final int EXPANDED_FULL_OPEN = -10001;
    public static final String LOG_TAG = "Sliding";
    private static final float MAXIMUM_ACCELERATION = 2000.0f;
    private static final float MAXIMUM_MAJOR_VELOCITY = 200.0f;
    private static final float MAXIMUM_MINOR_VELOCITY = 150.0f;
    private static final float MAXIMUM_TAP_VELOCITY = 100.0f;
    private static final int MSG_ANIMATE = 1000;
    public static final int ORIENTATION_BTT = 1;
    public static final int ORIENTATION_LTR = 2;
    public static final int ORIENTATION_RTL = 0;
    public static final int ORIENTATION_TTB = 3;
    private static final int TAP_THRESHOLD = 6;
    private static final int VELOCITY_UNITS = 1000;
    private boolean mAllowSingleTap;
    private boolean mAnimateOnClick;
    private float mAnimatedAcceleration;
    private float mAnimatedVelocity;
    private boolean mAnimating;
    private long mAnimationLastTime;
    private float mAnimationPosition;
    private int mBottomOffset;
    private View mContent;
    private final int mContentId;
    private long mCurrentAnimationTime;
    private boolean mExpanded;
    private final Rect mFrame;
    private View mHandle;
    private int mHandleHeight;
    private final int mHandleId;
    private int mHandleWidth;
    private final Handler mHandler;
    private final Rect mInvalidate;
    private boolean mInvert;
    private boolean mLocked;
    private int mMaximumAcceleration;
    private int mMaximumMajorVelocity;
    private int mMaximumMinorVelocity;
    private final int mMaximumTapVelocity;
    private OnDrawerCloseListener mOnDrawerCloseListener;
    private OnDrawerOpenListener mOnDrawerOpenListener;
    private OnDrawerScrollListener mOnDrawerScrollListener;
    private final int mTapThreshold;
    private int mTopOffset;
    private int mTouchDelta;
    private boolean mTracking;
    private VelocityTracker mVelocityTracker;
    private final int mVelocityUnits;
    private boolean mVertical;

    public interface OnDrawerCloseListener {
        void onDrawerClosed();
    }

    public interface OnDrawerOpenListener {
        void onDrawerOpened();
    }

    public interface OnDrawerScrollListener {
        void onScrollEnded();

        void onScrollStarted();
    }

    public MultiDirectionSlidingDrawer(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MultiDirectionSlidingDrawer(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        boolean z;
        boolean z2;
        this.mFrame = new Rect();
        this.mInvalidate = new Rect();
        this.mHandler = new SlidingHandler(this, (SlidingHandler) null);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.MultiDirectionSlidingDrawer, defStyle, 0);
        int orientation = a.getInt(0, 1);
        if (orientation == 1 || orientation == 3) {
            z = true;
        } else {
            z = false;
        }
        this.mVertical = z;
        this.mBottomOffset = (int) a.getDimension(3, 0.0f);
        this.mTopOffset = (int) a.getDimension(4, 0.0f);
        this.mAnimateOnClick = a.getBoolean(6, true);
        if (orientation == 3 || orientation == 2) {
            z2 = true;
        } else {
            z2 = false;
        }
        this.mInvert = z2;
        int handleId = a.getResourceId(1, 0);
        if (handleId == 0) {
            throw new IllegalArgumentException("The handle attribute is required and must refer to a valid child.");
        }
        int contentId = a.getResourceId(2, 0);
        if (contentId == 0) {
            throw new IllegalArgumentException("The content attribute is required and must refer to a valid child.");
        } else if (handleId == contentId) {
            throw new IllegalArgumentException("The content and handle attributes must refer to different children.");
        } else {
            this.mHandleId = handleId;
            this.mContentId = contentId;
            float density = getResources().getDisplayMetrics().density;
            this.mTapThreshold = (int) ((6.0f * density) + 0.5f);
            this.mMaximumTapVelocity = (int) ((MAXIMUM_TAP_VELOCITY * density) + 0.5f);
            this.mMaximumMinorVelocity = (int) ((MAXIMUM_MINOR_VELOCITY * density) + 0.5f);
            this.mMaximumMajorVelocity = (int) ((MAXIMUM_MAJOR_VELOCITY * density) + 0.5f);
            this.mMaximumAcceleration = (int) ((MAXIMUM_ACCELERATION * density) + 0.5f);
            this.mVelocityUnits = (int) ((1000.0f * density) + 0.5f);
            if (this.mInvert) {
                this.mMaximumAcceleration = -this.mMaximumAcceleration;
                this.mMaximumMajorVelocity = -this.mMaximumMajorVelocity;
                this.mMaximumMinorVelocity = -this.mMaximumMinorVelocity;
            }
            a.recycle();
            setAlwaysDrawnWithCacheEnabled(false);
        }
    }

    protected void onFinishInflate() {
        super.onFinishInflate();
        this.mHandle = findViewById(this.mHandleId);
        if (this.mHandle == null) {
            throw new IllegalArgumentException("The handle attribute is must refer to an existing child.");
        }
        this.mHandle.setOnClickListener(new DrawerToggler(this, (DrawerToggler) null));
        this.mContent = findViewById(this.mContentId);
        if (this.mContent == null) {
            throw new IllegalArgumentException("The content attribute is must refer to an existing child.");
        }
        this.mContent.setVisibility(View.GONE);
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSpecMode = View.MeasureSpec.getMode(widthMeasureSpec);
        int widthSpecSize = View.MeasureSpec.getSize(widthMeasureSpec);
        int heightSpecMode = View.MeasureSpec.getMode(heightMeasureSpec);
        int heightSpecSize = View.MeasureSpec.getSize(heightMeasureSpec);
        if (widthSpecMode == 0 || heightSpecMode == 0) {
            throw new RuntimeException("SlidingDrawer cannot have UNSPECIFIED dimensions");
        }
        View handle = this.mHandle;
        measureChild(handle, widthMeasureSpec, heightMeasureSpec);
        if (this.mVertical) {
            this.mContent.measure(View.MeasureSpec.makeMeasureSpec(widthSpecSize, MeasureSpec.getMode(1073741824)), View.MeasureSpec.makeMeasureSpec((heightSpecSize - handle.getMeasuredHeight()) - this.mTopOffset, MeasureSpec.getMode(1073741824)));
        } else {
            this.mContent.measure(View.MeasureSpec.makeMeasureSpec((widthSpecSize - handle.getMeasuredWidth()) - this.mTopOffset, MeasureSpec.getMode(1073741824)), View.MeasureSpec.makeMeasureSpec(heightSpecSize, MeasureSpec.getMode(1073741824)));
        }
        setMeasuredDimension(widthSpecSize, heightSpecSize);
    }

    protected void dispatchDraw(Canvas canvas) {
        int left;
        int left2;
        int right;
        int i = 0;
        long drawingTime = getDrawingTime();
        View handle = this.mHandle;
        boolean isVertical = this.mVertical;
        drawChild(canvas, handle, drawingTime);
        if (this.mTracking || this.mAnimating) {
            Bitmap cache = this.mContent.getDrawingCache();
            if (cache == null) {
                canvas.save();
                if (this.mInvert) {
                    if (isVertical) {
                        left2 = 0;
                    } else {
                        left2 = (handle.getLeft() - this.mTopOffset) - this.mContent.getMeasuredWidth();
                    }
                    float f = (float) left2;
                    if (isVertical) {
                        i = (handle.getTop() - this.mTopOffset) - this.mContent.getMeasuredHeight();
                    }
                    canvas.translate(f, (float) i);
                } else {
                    if (isVertical) {
                        left = 0;
                    } else {
                        left = handle.getLeft() - this.mTopOffset;
                    }
                    float f2 = (float) left;
                    if (isVertical) {
                        i = handle.getTop() - this.mTopOffset;
                    }
                    canvas.translate(f2, (float) i);
                }
                drawChild(canvas, this.mContent, drawingTime);
                canvas.restore();
            } else if (!isVertical) {
                if (this.mInvert) {
                    right = handle.getLeft() - cache.getWidth();
                } else {
                    right = handle.getRight();
                }
                canvas.drawBitmap(cache, (float) right, 0.0f, (Paint) null);
            } else if (this.mInvert) {
                canvas.drawBitmap(cache, 0.0f, (float) ((handle.getTop() - (getBottom() - getTop())) + this.mHandleHeight), (Paint) null);
            } else {
                canvas.drawBitmap(cache, 0.0f, (float) handle.getBottom(), (Paint) null);
            }
            invalidate();
        } else if (this.mExpanded) {
            drawChild(canvas, this.mContent, drawingTime);
        }
    }

    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int handleTop = 0;
        int handleLeft;
        int handleLeft2 = 0;
        int handleTop2;
        if (!this.mTracking) {
            int width = r - l;
            int height = b - t;
            View handle = this.mHandle;
            int handleWidth = handle.getMeasuredWidth();
            int handleHeight = handle.getMeasuredHeight();
            Log.d(LOG_TAG, "handleHeight: " + handleHeight);
            View content = this.mContent;
            if (this.mVertical) {
                handleLeft2 = (width - handleWidth) / 2;
                if (this.mInvert) {
                    Log.d(LOG_TAG, "content.layout(1)");
                    if (this.mExpanded) {
                        handleTop = (height - this.mBottomOffset) - handleHeight;
                    } else {
                        handleTop = this.mTopOffset;
                    }
                    content.layout(0, this.mTopOffset, content.getMeasuredWidth(), this.mTopOffset + content.getMeasuredHeight());
                } else {
                    if (this.mExpanded) {
                        handleTop2 = this.mTopOffset;
                    } else {
                        handleTop2 = (height - handleHeight) + this.mBottomOffset;
                    }
                    content.layout(0, this.mTopOffset + handleHeight, content.getMeasuredWidth(), this.mTopOffset + handleHeight + content.getMeasuredHeight());
                }
            } else {
                handleTop = (height - handleHeight) / 2;
                if (this.mInvert) {
                    if (this.mExpanded) {
                        handleLeft2 = (width - this.mBottomOffset) - handleWidth;
                    } else {
                        handleLeft2 = this.mTopOffset;
                    }
                    content.layout(this.mTopOffset, 0, this.mTopOffset + content.getMeasuredWidth(), content.getMeasuredHeight());
                } else {
                    if (this.mExpanded) {
                        handleLeft = this.mTopOffset;
                    } else {
                        handleLeft = (width - handleWidth) + this.mBottomOffset;
                    }
                    content.layout(this.mTopOffset + handleWidth, 0, this.mTopOffset + handleWidth + content.getMeasuredWidth(), content.getMeasuredHeight());
                }
            }
            handle.layout(handleLeft2, handleTop, handleLeft2 + handleWidth, handleTop + handleHeight);
            this.mHandleHeight = handle.getHeight();
            this.mHandleWidth = handle.getWidth();
        }
    }

    public boolean onInterceptTouchEvent(MotionEvent event) {
        if (this.mLocked) {
            return false;
        }
        int action = event.getAction();
        float x = event.getX();
        float y = event.getY();
        Rect frame = this.mFrame;
        View handle = this.mHandle;
        handle.getHitRect(frame);
        if (!this.mTracking && !frame.contains((int) x, (int) y)) {
            return false;
        }
        if (action == 0) {
            this.mTracking = true;
            handle.setPressed(true);
            prepareContent();
            if (this.mOnDrawerScrollListener != null) {
                this.mOnDrawerScrollListener.onScrollStarted();
            }
            if (this.mVertical) {
                int top = this.mHandle.getTop();
                this.mTouchDelta = ((int) y) - top;
                prepareTracking(top);
            } else {
                int left = this.mHandle.getLeft();
                this.mTouchDelta = ((int) x) - left;
                prepareTracking(left);
            }
            this.mVelocityTracker.addMovement(event);
        }
        return true;
    }

    public boolean onTouchEvent(MotionEvent event) {
        boolean negative;
        boolean c1;
        boolean c2;
        boolean c3;
        boolean c4;
        if (this.mLocked) {
            return true;
        }
        if (this.mTracking) {
            this.mVelocityTracker.addMovement(event);
            switch (event.getAction()) {
                case 1:
                case 3:
                    VelocityTracker velocityTracker = this.mVelocityTracker;
                    velocityTracker.computeCurrentVelocity(this.mVelocityUnits);
                    float yVelocity = velocityTracker.getYVelocity();
                    float xVelocity = velocityTracker.getXVelocity();
                    boolean vertical = this.mVertical;
                    if (vertical) {
                        negative = yVelocity < 0.0f;
                        if (xVelocity < 0.0f) {
                            xVelocity = -xVelocity;
                        }
                        if ((!this.mInvert && xVelocity > ((float) this.mMaximumMinorVelocity)) || (this.mInvert && xVelocity < ((float) this.mMaximumMinorVelocity))) {
                            xVelocity = (float) this.mMaximumMinorVelocity;
                        }
                    } else {
                        negative = xVelocity < 0.0f;
                        if (yVelocity < 0.0f) {
                            yVelocity = -yVelocity;
                        }
                        if ((!this.mInvert && yVelocity > ((float) this.mMaximumMinorVelocity)) || (this.mInvert && yVelocity < ((float) this.mMaximumMinorVelocity))) {
                            yVelocity = (float) this.mMaximumMinorVelocity;
                        }
                    }
                    float velocity = (float) Math.hypot((double) xVelocity, (double) yVelocity);
                    if (negative) {
                        velocity = -velocity;
                    }
                    int handleTop = this.mHandle.getTop();
                    int handleLeft = this.mHandle.getLeft();
                    int handleBottom = this.mHandle.getBottom();
                    int handleRight = this.mHandle.getRight();
                    if (Math.abs(velocity) >= ((float) this.mMaximumTapVelocity)) {
                        if (!vertical) {
                            handleTop = handleLeft;
                        }
                        performFling(handleTop, velocity, false);
                        break;
                    } else {
                        if (this.mInvert) {
                            c1 = this.mExpanded && getBottom() - handleBottom < this.mTapThreshold + this.mBottomOffset;
                            c2 = !this.mExpanded && handleTop < (this.mTopOffset + this.mHandleHeight) - this.mTapThreshold;
                            c3 = this.mExpanded && getRight() - handleRight < this.mTapThreshold + this.mBottomOffset;
                            c4 = !this.mExpanded && handleLeft > (this.mTopOffset + this.mHandleWidth) + this.mTapThreshold;
                        } else {
                            c1 = this.mExpanded && handleTop < this.mTapThreshold + this.mTopOffset;
                            c2 = !this.mExpanded && handleTop > (((this.mBottomOffset + getBottom()) - getTop()) - this.mHandleHeight) - this.mTapThreshold;
                            c3 = this.mExpanded && handleLeft < this.mTapThreshold + this.mTopOffset;
                            c4 = !this.mExpanded && handleLeft > (((this.mBottomOffset + getRight()) - getLeft()) - this.mHandleWidth) - this.mTapThreshold;
                        }
                        Log.d(LOG_TAG, "ACTION_UP: c1: " + c1 + ", c2: " + c2 + ", c3: " + c3 + ", c4: " + c4);
                        if (!vertical ? c3 || c4 : c1 || c2) {
                            if (!this.mAllowSingleTap) {
                                if (!vertical) {
                                    handleTop = handleLeft;
                                }
                                performFling(handleTop, velocity, false);
                                break;
                            } else {
                                playSoundEffect(SoundEffectConstants.CLICK);
                                if (!this.mExpanded) {
                                    if (!vertical) {
                                        handleTop = handleLeft;
                                    }
                                    animateOpen(handleTop);
                                    break;
                                } else {
                                    if (!vertical) {
                                        handleTop = handleLeft;
                                    }
                                    animateClose(handleTop);
                                    break;
                                }
                            }
                        } else {
                            if (!vertical) {
                                handleTop = handleLeft;
                            }
                            performFling(handleTop, velocity, false);
                            break;
                        }
                    }
                    //break;
                case 2:
                    moveHandle(((int) (this.mVertical ? event.getY() : event.getX())) - this.mTouchDelta);
                    break;
            }
        }
        if (this.mTracking || this.mAnimating || super.onTouchEvent(event)) {
            return true;
        }
        return false;
    }

    private void animateClose(int position) {
        prepareTracking(position);
        performFling(position, (float) this.mMaximumAcceleration, true);
    }

    private void animateOpen(int position) {
        prepareTracking(position);
        performFling(position, (float) (-this.mMaximumAcceleration), true);
    }

    private void performFling(int position, float velocity, boolean always) {
        boolean c1;
        boolean c2;
        boolean c3;
        boolean c12;
        boolean c22;
        boolean c32;
        this.mAnimationPosition = (float) position;
        this.mAnimatedVelocity = velocity;
        if (this.mExpanded) {
            int bottom = this.mVertical ? getBottom() : getRight();
            int handleHeight = this.mVertical ? this.mHandleHeight : this.mHandleWidth;
            Log.d(LOG_TAG, "position: " + position + ", velocity: " + velocity + ", mMaximumMajorVelocity: " + this.mMaximumMajorVelocity);
            if (this.mInvert) {
                c12 = velocity < ((float) this.mMaximumMajorVelocity);
            } else {
                c12 = velocity > ((float) this.mMaximumMajorVelocity);
            }
            if (this.mInvert) {
                c22 = (bottom - (position + handleHeight)) + this.mBottomOffset > handleHeight;
            } else {
                c22 = position > (this.mVertical ? this.mHandleHeight : this.mHandleWidth) + this.mTopOffset;
            }
            if (this.mInvert) {
                c32 = velocity < ((float) (-this.mMaximumMajorVelocity));
            } else {
                c32 = velocity > ((float) (-this.mMaximumMajorVelocity));
            }
            Log.d(LOG_TAG, "EXPANDED. c1: " + c12 + ", c2: " + c22 + ", c3: " + c32);
            if (always || c12 || (c22 && c32)) {
                this.mAnimatedAcceleration = (float) this.mMaximumAcceleration;
                if (this.mInvert) {
                    if (velocity > 0.0f) {
                        this.mAnimatedVelocity = 0.0f;
                    }
                } else if (velocity < 0.0f) {
                    this.mAnimatedVelocity = 0.0f;
                }
            } else {
                this.mAnimatedAcceleration = (float) (-this.mMaximumAcceleration);
                if (this.mInvert) {
                    if (velocity < 0.0f) {
                        this.mAnimatedVelocity = 0.0f;
                    }
                } else if (velocity > 0.0f) {
                    this.mAnimatedVelocity = 0.0f;
                }
            }
        } else {
            if (this.mInvert) {
                c1 = velocity < ((float) this.mMaximumMajorVelocity);
            } else {
                c1 = velocity > ((float) this.mMaximumMajorVelocity);
            }
            if (this.mInvert) {
                c2 = position < (this.mVertical ? getHeight() : getWidth()) / 2;
            } else {
                c2 = position > (this.mVertical ? getHeight() : getWidth()) / 2;
            }
            if (this.mInvert) {
                c3 = velocity < ((float) (-this.mMaximumMajorVelocity));
            } else {
                c3 = velocity > ((float) (-this.mMaximumMajorVelocity));
            }
            Log.d(LOG_TAG, "COLLAPSED. position: " + position + ", velocity: " + velocity + ", mMaximumMajorVelocity: " + this.mMaximumMajorVelocity);
            Log.d(LOG_TAG, "COLLAPSED. always: " + always + ", c1: " + c1 + ", c2: " + c2 + ", c3: " + c3);
            if (always || (!c1 && (!c2 || !c3))) {
                this.mAnimatedAcceleration = (float) (-this.mMaximumAcceleration);
                if (this.mInvert) {
                    if (velocity < 0.0f) {
                        this.mAnimatedVelocity = 0.0f;
                    }
                } else if (velocity > 0.0f) {
                    this.mAnimatedVelocity = 0.0f;
                }
            } else {
                this.mAnimatedAcceleration = (float) this.mMaximumAcceleration;
                if (this.mInvert) {
                    if (velocity > 0.0f) {
                        this.mAnimatedVelocity = 0.0f;
                    }
                } else if (velocity < 0.0f) {
                    this.mAnimatedVelocity = 0.0f;
                }
            }
        }
        long now = SystemClock.uptimeMillis();
        this.mAnimationLastTime = now;
        this.mCurrentAnimationTime = 16 + now;
        this.mAnimating = true;
        this.mHandler.removeMessages(1000);
        this.mHandler.sendMessageAtTime(this.mHandler.obtainMessage(1000), this.mCurrentAnimationTime);
        stopTracking();
    }

    private void prepareTracking(int position) {
        boolean opening;
        int width;
        this.mTracking = true;
        this.mVelocityTracker = VelocityTracker.obtain();
        if (this.mExpanded) {
            opening = false;
        } else {
            opening = true;
        }
        if (opening) {
            this.mAnimatedAcceleration = (float) this.mMaximumAcceleration;
            this.mAnimatedVelocity = (float) this.mMaximumMajorVelocity;
            if (this.mInvert) {
                this.mAnimationPosition = (float) this.mTopOffset;
            } else {
                int i = this.mBottomOffset;
                if (this.mVertical) {
                    width = getHeight() - this.mHandleHeight;
                } else {
                    width = getWidth() - this.mHandleWidth;
                }
                this.mAnimationPosition = (float) (width + i);
            }
            moveHandle((int) this.mAnimationPosition);
            this.mAnimating = true;
            this.mHandler.removeMessages(1000);
            long now = SystemClock.uptimeMillis();
            this.mAnimationLastTime = now;
            this.mCurrentAnimationTime = 16 + now;
            this.mAnimating = true;
            return;
        }
        if (this.mAnimating) {
            this.mAnimating = false;
            this.mHandler.removeMessages(1000);
        }
        moveHandle(position);
    }

    private void moveHandle(int position) {
        View handle = this.mHandle;
        if (this.mVertical) {
            if (position == EXPANDED_FULL_OPEN) {
                if (this.mInvert) {
                    handle.offsetTopAndBottom(((this.mBottomOffset + getBottom()) - getTop()) - this.mHandleHeight);
                } else {
                    handle.offsetTopAndBottom(this.mTopOffset - handle.getTop());
                }
                invalidate();
            } else if (position == COLLAPSED_FULL_CLOSED) {
                if (this.mInvert) {
                    handle.offsetTopAndBottom(this.mTopOffset - handle.getTop());
                } else {
                    handle.offsetTopAndBottom((((this.mBottomOffset + getBottom()) - getTop()) - this.mHandleHeight) - handle.getTop());
                }
                invalidate();
            } else {
                int top = handle.getTop();
                int deltaY = position - top;
                if (position < this.mTopOffset) {
                    deltaY = this.mTopOffset - top;
                } else if (deltaY > (((this.mBottomOffset + getBottom()) - getTop()) - this.mHandleHeight) - top) {
                    deltaY = (((this.mBottomOffset + getBottom()) - getTop()) - this.mHandleHeight) - top;
                }
                handle.offsetTopAndBottom(deltaY);
                Rect frame = this.mFrame;
                Rect region = this.mInvalidate;
                handle.getHitRect(frame);
                region.set(frame);
                region.union(frame.left, frame.top - deltaY, frame.right, frame.bottom - deltaY);
                region.union(0, frame.bottom - deltaY, getWidth(), (frame.bottom - deltaY) + this.mContent.getHeight());
                invalidate(region);
            }
        } else if (position == EXPANDED_FULL_OPEN) {
            if (this.mInvert) {
                handle.offsetLeftAndRight(((this.mBottomOffset + getRight()) - getLeft()) - this.mHandleWidth);
            } else {
                handle.offsetLeftAndRight(this.mTopOffset - handle.getLeft());
            }
            invalidate();
        } else if (position == COLLAPSED_FULL_CLOSED) {
            if (this.mInvert) {
                handle.offsetLeftAndRight(this.mTopOffset - handle.getLeft());
            } else {
                handle.offsetLeftAndRight((((this.mBottomOffset + getRight()) - getLeft()) - this.mHandleWidth) - handle.getLeft());
            }
            invalidate();
        } else {
            int left = handle.getLeft();
            int deltaX = position - left;
            if (position < this.mTopOffset) {
                deltaX = this.mTopOffset - left;
            } else if (deltaX > (((this.mBottomOffset + getRight()) - getLeft()) - this.mHandleWidth) - left) {
                deltaX = (((this.mBottomOffset + getRight()) - getLeft()) - this.mHandleWidth) - left;
            }
            handle.offsetLeftAndRight(deltaX);
            Rect frame2 = this.mFrame;
            Rect region2 = this.mInvalidate;
            handle.getHitRect(frame2);
            region2.set(frame2);
            region2.union(frame2.left - deltaX, frame2.top, frame2.right - deltaX, frame2.bottom);
            region2.union(frame2.right - deltaX, 0, (frame2.right - deltaX) + this.mContent.getWidth(), getHeight());
            invalidate(region2);
        }
    }

    private void prepareContent() {
        if (!this.mAnimating) {
            View content = this.mContent;
            if (content.isLayoutRequested()) {
                if (this.mVertical) {
                    int handleHeight = this.mHandleHeight;
                    content.measure(View.MeasureSpec.makeMeasureSpec(getRight() - getLeft(), MeasureSpec.getMode(1073741824)), View.MeasureSpec.makeMeasureSpec(((getBottom() - getTop()) - handleHeight) - this.mTopOffset, MeasureSpec.getMode(1073741824)));
                    Log.d(LOG_TAG, "content.layout(2)");
                    if (this.mInvert) {
                        content.layout(0, this.mTopOffset, content.getMeasuredWidth(), this.mTopOffset + content.getMeasuredHeight());
                    } else {
                        content.layout(0, this.mTopOffset + handleHeight, content.getMeasuredWidth(), this.mTopOffset + handleHeight + content.getMeasuredHeight());
                    }
                } else {
                    int handleWidth = this.mHandle.getWidth();
                    content.measure(View.MeasureSpec.makeMeasureSpec(((getRight() - getLeft()) - handleWidth) - this.mTopOffset, MeasureSpec.getMode(1073741824)), View.MeasureSpec.makeMeasureSpec(getBottom() - getTop(), MeasureSpec.getMode(1073741824)));
                    if (this.mInvert) {
                        content.layout(this.mTopOffset, 0, this.mTopOffset + content.getMeasuredWidth(), content.getMeasuredHeight());
                    } else {
                        content.layout(this.mTopOffset + handleWidth, 0, this.mTopOffset + handleWidth + content.getMeasuredWidth(), content.getMeasuredHeight());
                    }
                }
            }
            content.getViewTreeObserver().dispatchOnPreDraw();
            content.buildDrawingCache();
            content.setVisibility(View.GONE);
        }
    }

    private void stopTracking() {
        this.mHandle.setPressed(false);
        this.mTracking = false;
        if (this.mOnDrawerScrollListener != null) {
            this.mOnDrawerScrollListener.onScrollEnded();
        }
        if (this.mVelocityTracker != null) {
            this.mVelocityTracker.recycle();
            this.mVelocityTracker = null;
        }
    }

    private void doAnimation() {
        if (this.mAnimating) {
            incrementAnimation();
            if (!this.mInvert) {
                if (this.mAnimationPosition >= ((float) (((this.mVertical ? getHeight() : getWidth()) + this.mBottomOffset) - 1))) {
                    this.mAnimating = false;
                    closeDrawer();
                } else if (this.mAnimationPosition < ((float) this.mTopOffset)) {
                    this.mAnimating = false;
                    openDrawer();
                } else {
                    moveHandle((int) this.mAnimationPosition);
                    this.mCurrentAnimationTime += 16;
                    this.mHandler.sendMessageAtTime(this.mHandler.obtainMessage(1000), this.mCurrentAnimationTime);
                }
            } else if (this.mAnimationPosition < ((float) this.mTopOffset)) {
                this.mAnimating = false;
                closeDrawer();
            } else {
                if (this.mAnimationPosition >= ((float) (((this.mVertical ? getHeight() : getWidth()) + this.mTopOffset) - 1))) {
                    this.mAnimating = false;
                    openDrawer();
                    return;
                }
                moveHandle((int) this.mAnimationPosition);
                this.mCurrentAnimationTime += 16;
                this.mHandler.sendMessageAtTime(this.mHandler.obtainMessage(1000), this.mCurrentAnimationTime);
            }
        }
    }

    private void incrementAnimation() {
        long now = SystemClock.uptimeMillis();
        float t = ((float) (now - this.mAnimationLastTime)) / 1000.0f;
        float position = this.mAnimationPosition;
        float v = this.mAnimatedVelocity;
        float a = this.mInvert ? this.mAnimatedAcceleration : this.mAnimatedAcceleration;
        this.mAnimationPosition = (v * t) + position + (0.5f * a * t * t);
        this.mAnimatedVelocity = (a * t) + v;
        this.mAnimationLastTime = now;
    }

    public void toggle() {
        if (!this.mExpanded) {
            openDrawer();
        } else {
            closeDrawer();
        }
        invalidate();
        requestLayout();
    }

    public void animateToggle() {
        if (!this.mExpanded) {
            animateOpen();
        } else {
            animateClose();
        }
    }

    public void open() {
        openDrawer();
        invalidate();
        requestLayout();
        sendAccessibilityEvent(32);
    }

    public void close() {
        closeDrawer();
        invalidate();
        requestLayout();
    }

    public void animateClose() {
        prepareContent();
        OnDrawerScrollListener scrollListener = this.mOnDrawerScrollListener;
        if (scrollListener != null) {
            scrollListener.onScrollStarted();
        }
        animateClose(this.mVertical ? this.mHandle.getTop() : this.mHandle.getLeft());
        if (scrollListener != null) {
            scrollListener.onScrollEnded();
        }
    }

    public void animateOpen() {
        prepareContent();
        OnDrawerScrollListener scrollListener = this.mOnDrawerScrollListener;
        if (scrollListener != null) {
            scrollListener.onScrollStarted();
        }
        animateOpen(this.mVertical ? this.mHandle.getTop() : this.mHandle.getLeft());
        sendAccessibilityEvent(32);
        if (scrollListener != null) {
            scrollListener.onScrollEnded();
        }
    }

    private void closeDrawer() {
        moveHandle(COLLAPSED_FULL_CLOSED);
        this.mContent.setVisibility(View.GONE);
        this.mContent.destroyDrawingCache();
        if (this.mExpanded) {
            this.mExpanded = false;
            if (this.mOnDrawerCloseListener != null) {
                this.mOnDrawerCloseListener.onDrawerClosed();
            }
        }
    }

    private void openDrawer() {
        moveHandle(EXPANDED_FULL_OPEN);
        this.mContent.setVisibility(View.VISIBLE);
        if (!this.mExpanded) {
            this.mExpanded = true;
            if (this.mOnDrawerOpenListener != null) {
                this.mOnDrawerOpenListener.onDrawerOpened();
            }
        }
    }

    public void setOnDrawerOpenListener(OnDrawerOpenListener onDrawerOpenListener) {
        this.mOnDrawerOpenListener = onDrawerOpenListener;
    }

    public void setOnDrawerCloseListener(OnDrawerCloseListener onDrawerCloseListener) {
        this.mOnDrawerCloseListener = onDrawerCloseListener;
    }

    public void setOnDrawerScrollListener(OnDrawerScrollListener onDrawerScrollListener) {
        this.mOnDrawerScrollListener = onDrawerScrollListener;
    }

    public View getHandle() {
        return this.mHandle;
    }

    public View getContent() {
        return this.mContent;
    }

    public void unlock() {
        this.mLocked = false;
    }

    public void lock() {
        this.mLocked = true;
    }

    public boolean isOpened() {
        return this.mExpanded;
    }

    public boolean isMoving() {
        return this.mTracking || this.mAnimating;
    }

    private class DrawerToggler implements View.OnClickListener {
        private DrawerToggler() {
        }

        /* synthetic */ DrawerToggler(MultiDirectionSlidingDrawer multiDirectionSlidingDrawer, DrawerToggler drawerToggler) {
            this();
        }

        public void onClick(View v) {
            if (!MultiDirectionSlidingDrawer.this.mLocked) {
                if (MultiDirectionSlidingDrawer.this.mAnimateOnClick) {
                    MultiDirectionSlidingDrawer.this.animateToggle();
                } else {
                    MultiDirectionSlidingDrawer.this.toggle();
                }
            }
        }
    }

    private class SlidingHandler extends Handler {
        private SlidingHandler() {
        }

        /* synthetic */ SlidingHandler(MultiDirectionSlidingDrawer multiDirectionSlidingDrawer, SlidingHandler slidingHandler) {
            this();
        }

        public void handleMessage(Message m) {
            switch (m.what) {
                case 1000:
                    MultiDirectionSlidingDrawer.this.doAnimation();
                    return;
                default:
                    return;
            }
        }
    }
}
