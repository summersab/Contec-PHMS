package com.contec.phms.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.DecelerateInterpolator;
import android.widget.AbsListView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Scroller;
import android.widget.TextView;
import com.contec.phms.App_phms;
import com.contec.phms.R;
import com.contec.phms.manager.device.DeviceManager;
import com.contec.phms.util.PageUtil;

public class XListView extends ListView implements AbsListView.OnScrollListener {
    private static final float OFFSET_RADIO = 1.8f;
    private static final int PULL_LOAD_MORE_DELTA = 50;
    private static final int SCROLLBACK_FOOTER = 1;
    private static final int SCROLLBACK_HEADER = 0;
    private static final int SCROLL_DURATION = 400;
    private boolean mEnablePullLoad;
    private boolean mEnablePullRefresh = true;
    private XListViewFooter mFooterView;
    private TextView mHeaderTimeView;
    private XListViewHeader mHeaderView;
    private RelativeLayout mHeaderViewContent;
    private int mHeaderViewHeight;
    private boolean mIsFooterReady = false;
    private float mLastY = -1.0f;
    private IXListViewListener mListViewListener;
    private boolean mPullLoading;
    private boolean mPullRefreshing = false;
    private int mScrollBack;
    private AbsListView.OnScrollListener mScrollListener;
    private Scroller mScroller;
    private int mTotalItemCount;

    public interface IXListViewListener {
        void onLoadMore();

        void onRefresh();
    }

    public interface OnXScrollListener extends AbsListView.OnScrollListener {
        void onXScrolling(View view);
    }

    public XListView(Context context) {
        super(context);
        initWithContext(context);
    }

    public XListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initWithContext(context);
    }

    public XListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initWithContext(context);
    }

    private void initWithContext(Context context) {
        this.mScroller = new Scroller(context, new DecelerateInterpolator());
        super.setOnScrollListener(this);
        this.mHeaderView = new XListViewHeader(context);
        this.mHeaderViewContent = (RelativeLayout) this.mHeaderView.findViewById(R.id.xlistview_header_content);
        this.mHeaderTimeView = (TextView) this.mHeaderView.findViewById(R.id.xlistview_header_time);
        addHeaderView(this.mHeaderView);
        this.mFooterView = new XListViewFooter(context);
        this.mHeaderView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            public void onGlobalLayout() {
                XListView.this.mHeaderViewHeight = XListView.this.mHeaderViewContent.getHeight();
                XListView.this.getViewTreeObserver().removeGlobalOnLayoutListener(this);
            }
        });
    }

    public void setAdapter(ListAdapter adapter) {
        if (!this.mIsFooterReady) {
            this.mIsFooterReady = true;
            addFooterView(this.mFooterView);
        }
        super.setAdapter(adapter);
    }

    public void setPullRefreshEnable(boolean enable) {
        this.mEnablePullRefresh = enable;
        if (!this.mEnablePullRefresh) {
            this.mHeaderViewContent.setVisibility(View.INVISIBLE);
        } else {
            this.mHeaderViewContent.setVisibility(View.VISIBLE);
        }
    }

    public void setPullLoadEnable(boolean enable) {
        this.mEnablePullLoad = enable;
        if (!this.mEnablePullLoad) {
            this.mFooterView.hide();
            this.mFooterView.setOnClickListener((View.OnClickListener) null);
            return;
        }
        this.mPullLoading = false;
        this.mFooterView.show();
        this.mFooterView.setState(0);
        this.mFooterView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                XListView.this.startLoadMore();
            }
        });
    }

    public void stopRefresh() {
        if (this.mPullRefreshing) {
            this.mPullRefreshing = false;
            resetHeaderHeight();
        }
    }

    public void stopLoadMore() {
        if (this.mPullLoading) {
            this.mPullLoading = false;
            this.mFooterView.setState(0);
        }
    }

    public void setRefreshTime(String time) {
        this.mHeaderTimeView.setText(time);
    }

    private void invokeOnScrolling() {
        if (this.mScrollListener instanceof OnXScrollListener) {
            ((OnXScrollListener) this.mScrollListener).onXScrolling(this);
        }
    }

    private void updateHeaderHeight(float delta) {
        this.mHeaderView.setVisiableHeight(((int) delta) + this.mHeaderView.getVisiableHeight());
        if (this.mEnablePullRefresh && !this.mPullRefreshing) {
            if (this.mHeaderView.getVisiableHeight() > this.mHeaderViewHeight) {
                this.mHeaderView.setState(1);
            } else {
                this.mHeaderView.setState(0);
            }
        }
        setSelection(0);
    }

    private void resetHeaderHeight() {
        int height = this.mHeaderView.getVisiableHeight();
        if (height != 0) {
            if (!this.mPullRefreshing || height > this.mHeaderViewHeight) {
                int finalHeight = 0;
                if (this.mPullRefreshing && height > this.mHeaderViewHeight) {
                    finalHeight = this.mHeaderViewHeight;
                }
                this.mScrollBack = 0;
                this.mScroller.startScroll(0, height, 0, finalHeight - height, 400);
                invalidate();
            }
        }
    }

    private void updateFooterHeight(float delta) {
        int height = this.mFooterView.getBottomMargin() + ((int) delta);
        if (this.mEnablePullLoad && !this.mPullLoading) {
            if (height > 50) {
                this.mFooterView.setState(1);
            } else {
                this.mFooterView.setState(0);
            }
        }
        this.mFooterView.setBottomMargin(height);
    }

    private void resetFooterHeight() {
        int bottomMargin = this.mFooterView.getBottomMargin();
        if (bottomMargin > 0) {
            this.mScrollBack = 1;
            this.mScroller.startScroll(0, bottomMargin, 0, -bottomMargin, 400);
            invalidate();
        }
    }

    private void startLoadMore() {
        this.mPullLoading = true;
        this.mFooterView.setState(2);
        if (this.mListViewListener != null) {
            this.mListViewListener.onLoadMore();
        }
    }

    public boolean onTouchEvent(MotionEvent ev) {
        if (this.mLastY == -1.0f) {
            this.mLastY = ev.getRawY();
        }
        switch (ev.getAction()) {
            case 0:
                this.mLastY = ev.getRawY();
                break;
            case 2:
                if (DeviceManager.m_DeviceBean.mState != 1 && DeviceManager.m_DeviceBean.mState != 2 && DeviceManager.m_DeviceBean.mState != 4 && DeviceManager.m_DeviceBean.mState != 6 && DeviceManager.m_DeviceBean.mState != 7) {
                    float deltaY = ev.getRawY() - this.mLastY;
                    this.mLastY = ev.getRawY();
                    if (getFirstVisiblePosition() != 0 || (this.mHeaderView.getVisiableHeight() <= 0 && deltaY <= 0.0f)) {
                        if (getLastVisiblePosition() == this.mTotalItemCount - 1 && (this.mFooterView.getBottomMargin() > 0 || deltaY < 0.0f)) {
                            updateFooterHeight((-deltaY) / OFFSET_RADIO);
                            break;
                        }
                    } else {
                        updateHeaderHeight(deltaY / OFFSET_RADIO);
                        invokeOnScrolling();
                        break;
                    }
                } else {
                    PageUtil.showToast(App_phms.getInstance().getApplicationContext(), App_phms.getInstance().getApplicationContext().getString(R.string.str_collecting_data));
                    break;
                }
                break;
            default:
                this.mLastY = -1.0f;
                if (getFirstVisiblePosition() != 0) {
                    if (getLastVisiblePosition() == this.mTotalItemCount - 1) {
                        if (this.mEnablePullLoad && this.mFooterView.getBottomMargin() > 50) {
                            startLoadMore();
                        }
                        resetFooterHeight();
                        break;
                    }
                } else {
                    if (this.mEnablePullRefresh && this.mHeaderView.getVisiableHeight() > this.mHeaderViewHeight) {
                        this.mPullRefreshing = true;
                        this.mHeaderView.setState(2);
                        if (this.mListViewListener != null) {
                            this.mListViewListener.onRefresh();
                        }
                    }
                    resetHeaderHeight();
                    break;
                }
                break;
        }
        return super.onTouchEvent(ev);
    }

    public void computeScroll() {
        if (this.mScroller.computeScrollOffset()) {
            if (this.mScrollBack == 0) {
                this.mHeaderView.setVisiableHeight(this.mScroller.getCurrY());
            } else {
                this.mFooterView.setBottomMargin(this.mScroller.getCurrY());
            }
            postInvalidate();
            invokeOnScrolling();
        }
        super.computeScroll();
    }

    public void setOnScrollListener(AbsListView.OnScrollListener l) {
        this.mScrollListener = l;
    }

    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (this.mScrollListener != null) {
            this.mScrollListener.onScrollStateChanged(view, scrollState);
        }
    }

    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        this.mTotalItemCount = totalItemCount;
        if (this.mScrollListener != null) {
            this.mScrollListener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
        }
    }

    public void setXListViewListener(IXListViewListener l) {
        this.mListViewListener = l;
    }
}
