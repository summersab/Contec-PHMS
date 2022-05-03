package com.contec.phms.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.alibaba.fastjson.asm.Opcodes;
import com.contec.phms.R;

public class XListViewHeader extends LinearLayout {
    public static final int STATE_NORMAL = 0;
    public static final int STATE_READY = 1;
    public static final int STATE_REFRESHING = 2;
    private final int ROTATE_ANIM_DURATION = Opcodes.GETFIELD;
    private ImageView mArrowImageView;
    private LinearLayout mContainer;
    private TextView mHintTextView;
    private ProgressBar mProgressBar;
    private Animation mRotateDownAnim;
    private Animation mRotateUpAnim;
    private int mState = 0;
    private TextView mTv_lastupdated;

    public XListViewHeader(Context context) {
        super(context);
        initView(context);
    }

    public XListViewHeader(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    private void initView(Context context) {
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(-1, 0);
        this.mContainer = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.xlistview_header, (ViewGroup) null);
        addView(this.mContainer, lp);
        setGravity(80);
        this.mArrowImageView = (ImageView) findViewById(R.id.xlistview_header_arrow);
        this.mHintTextView = (TextView) findViewById(R.id.xlistview_header_hint_textview);
        this.mProgressBar = (ProgressBar) findViewById(R.id.xlistview_header_progressbar);
        this.mTv_lastupdated = (TextView) findViewById(R.id.tv_lastupdated);
        this.mHintTextView.setText(R.string.xlistview_header_hint_normal);
        this.mTv_lastupdated.setText(R.string.xlistview_header_last_time);
        this.mRotateUpAnim = new RotateAnimation(0.0f, -180.0f, 1, 0.5f, 1, 0.5f);
        this.mRotateUpAnim.setDuration(180);
        this.mRotateUpAnim.setFillAfter(true);
        this.mRotateDownAnim = new RotateAnimation(-180.0f, 0.0f, 1, 0.5f, 1, 0.5f);
        this.mRotateDownAnim.setDuration(180);
        this.mRotateDownAnim.setFillAfter(true);
    }

    public void setState(int state) {
        if (state == 2) {
            this.mArrowImageView.clearAnimation();
            this.mArrowImageView.setVisibility(View.INVISIBLE);
            this.mProgressBar.setVisibility(View.VISIBLE);
        } else {
            this.mArrowImageView.setVisibility(View.VISIBLE);
            this.mProgressBar.setVisibility(View.INVISIBLE);
        }
        switch (state) {
            case 0:
                if (this.mState == 1) {
                    this.mArrowImageView.startAnimation(this.mRotateDownAnim);
                }
                if (this.mState == 2) {
                    this.mArrowImageView.clearAnimation();
                }
                this.mHintTextView.setText(R.string.xlistview_header_hint_normal);
                this.mTv_lastupdated.setText(R.string.xlistview_header_last_time);
                break;
            case 1:
                if (this.mState != 1) {
                    this.mArrowImageView.clearAnimation();
                    this.mArrowImageView.startAnimation(this.mRotateUpAnim);
                }
                this.mHintTextView.setText(R.string.xlistview_header_hint_ready);
                this.mTv_lastupdated.setText(R.string.xlistview_header_last_time);
                break;
            case 2:
                this.mHintTextView.setText(R.string.xlistview_header_hint_loading);
                this.mTv_lastupdated.setText(R.string.xlistview_header_last_time);
                break;
        }
        this.mState = state;
    }

    public void setVisiableHeight(int height) {
        if (height < 0) {
            height = 0;
        }
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) this.mContainer.getLayoutParams();
        lp.height = height;
        this.mContainer.setLayoutParams(lp);
    }

    public int getVisiableHeight() {
        return this.mContainer.getHeight();
    }
}
