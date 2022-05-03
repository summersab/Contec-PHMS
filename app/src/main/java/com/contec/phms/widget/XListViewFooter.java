package com.contec.phms.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.contec.phms.R;

public class XListViewFooter extends LinearLayout {
    public static final int STATE_LOADING = 2;
    public static final int STATE_NORMAL = 0;
    public static final int STATE_READY = 1;
    private View mContentView;
    private Context mContext;
    private TextView mHintView;
    private View mProgressBar;

    public XListViewFooter(Context context) {
        super(context);
        initView(context);
    }

    public XListViewFooter(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public void setState(int state) {
        this.mHintView.setVisibility(View.INVISIBLE);
        this.mProgressBar.setVisibility(View.INVISIBLE);
        this.mHintView.setVisibility(View.INVISIBLE);
        if (state == 1) {
            this.mHintView.setVisibility(View.VISIBLE);
            this.mHintView.setText(R.string.xlistview_footer_hint_ready);
        } else if (state == 2) {
            this.mProgressBar.setVisibility(View.VISIBLE);
        } else {
            this.mHintView.setVisibility(View.VISIBLE);
            this.mHintView.setText(R.string.xlistview_footer_hint_normal);
        }
    }

    public void setBottomMargin(int height) {
        if (height >= 0) {
            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) this.mContentView.getLayoutParams();
            lp.bottomMargin = height;
            this.mContentView.setLayoutParams(lp);
        }
    }

    public int getBottomMargin() {
        return ((LinearLayout.LayoutParams) this.mContentView.getLayoutParams()).bottomMargin;
    }

    public void normal() {
        this.mHintView.setVisibility(View.VISIBLE);
        this.mProgressBar.setVisibility(View.GONE);
    }

    public void loading() {
        this.mHintView.setVisibility(View.GONE);
        this.mProgressBar.setVisibility(View.VISIBLE);
    }

    public void hide() {
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) this.mContentView.getLayoutParams();
        lp.height = 0;
        this.mContentView.setLayoutParams(lp);
    }

    public void show() {
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) this.mContentView.getLayoutParams();
        lp.height = -2;
        this.mContentView.setLayoutParams(lp);
    }

    private void initView(Context context) {
        this.mContext = context;
        LinearLayout moreView = (LinearLayout) LayoutInflater.from(this.mContext).inflate(R.layout.xlistview_footer, (ViewGroup) null);
        addView(moreView);
        moreView.setLayoutParams(new LinearLayout.LayoutParams(-1, -2));
        this.mContentView = moreView.findViewById(R.id.xlistview_footer_content);
        this.mProgressBar = moreView.findViewById(R.id.xlistview_footer_progressbar);
        this.mHintView = (TextView) moreView.findViewById(R.id.xlistview_footer_hint_textview);
    }
}
