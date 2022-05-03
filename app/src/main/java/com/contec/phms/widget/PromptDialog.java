package com.contec.phms.widget;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.contec.phms.R;

@SuppressLint({"InflateParams"})
public class PromptDialog extends Dialog {
    public static final int BUTTON_1 = 1;
    public static final int BUTTON_2 = 2;
    public static final int BUTTON_3 = 3;
    public static final int BUTTON_COUNT_ONE = 1;
    public static final int BUTTON_COUNT_THREE = 3;
    public static final int BUTTON_COUNT_TWO = 2;
    public static final int BUTTON_COUNT_ZERO = 0;
    public static final int VIEW_STYLE_NORMAL = 1;
    public static final int VIEW_STYLE_TITLEBAR = 2;
    public static final int VIEW_STYLE_TITLEBAR_SKYBLUE = 3;
    private Context context;

    public interface OnClickListener {
        void onClick(Dialog dialog, int i);
    }

    public interface OnLinearClickListener {
        void onClick(int i);
    }

    protected PromptDialog(Context context2, int theme) {
        super(context2, theme);
        this.context = context2;
    }

    protected PromptDialog(Context context2) {
        this(context2, (int) R.style.PromptDialogStyle);
    }

    protected PromptDialog(Context context2, boolean cancelableOnTouchOutside) {
        this(context2);
        setCanceledOnTouchOutside(cancelableOnTouchOutside);
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = UIUtils.getScreenWidth(this.context) - (UIUtils.dp2px(this.context, 30.0f) * 2);
        window.setAttributes(params);
    }

    @SuppressLint({"NewApi", "InflateParams"})
    public static class Builder {
        private ColorStateList button1ColorStateList;
        private int button1Flag;
        private OnClickListener button1Listener;
        private float button1Size;
        private CharSequence button1Text;
        private int button1TextColor;
        private ColorStateList button2ColorStateList;
        private int button2Flag;
        private OnClickListener button2Listener;
        private float button2Size;
        private CharSequence button2Text;
        private int button2TextColor;
        private ColorStateList button3ColorStateList;
        private int button3Flag;
        private OnClickListener button3Listener;
        private float button3Size;
        private CharSequence button3Text;
        private int button3TextColor;
        private boolean cancelable = true;
        private boolean canceledOnTouchOutside;
        private Context context;
        private PromptDialog dialog;
        private Drawable icon;
        private OnLinearClickListener linearListener;
        private CharSequence message;
        private CharSequence message2;
        private int messageColor;
        private ColorStateList messageColorStateList;
        private float messageSize;
        private CharSequence title;
        private int titleColor;
        private ColorStateList titleColorStateList;
        private float titleSize;
        private int titlebarGravity;
        private View view;

        public Builder(Context context2, int theme) {
            this.dialog = new PromptDialog(context2, theme);
            this.context = context2;
            initData();
        }

        public Builder(Context context2) {
            this.dialog = new PromptDialog(context2);
            this.context = context2;
            initData();
        }

        private void initData() {
            this.button1TextColor = Color.parseColor("#808080");
            this.button2TextColor = Color.parseColor("#808080");
            this.button3TextColor = Color.parseColor("#808080");
            this.messageColor = Color.parseColor("#696969");
            this.titleColor = -16777216;
            this.button1Size = 16.0f;
            this.button2Size = 16.0f;
            this.button3Size = 16.0f;
            this.messageSize = 15.0f;
            this.titleSize = 18.0f;
            this.titlebarGravity = 17;
        }

        public Context getContext() {
            return this.context;
        }

        public Builder setTitleBarGravity(int titlebarGravity2) {
            this.titlebarGravity = titlebarGravity2;
            return this;
        }

        public Builder setTitle(CharSequence title2) {
            this.title = title2;
            return this;
        }

        public Builder setTitle(int titleResId) {
            this.title = this.context.getResources().getString(titleResId);
            return this;
        }

        public Builder setTitleColor(int titleColor2) {
            this.titleColor = titleColor2;
            return this;
        }

        public Builder setTitleColor(ColorStateList titleColor2) {
            this.titleColorStateList = titleColor2;
            return this;
        }

        public Builder setTitleSize(float titleSize2) {
            this.titleSize = titleSize2;
            return this;
        }

        public Builder setIcon(Drawable icon2) {
            this.icon = icon2;
            return this;
        }

        public Builder setIcon(int iconResId) {
            this.icon = this.context.getResources().getDrawable(iconResId);
            return this;
        }

        public Builder setMessage(CharSequence message3, OnLinearClickListener listener) {
            this.message = message3;
            this.linearListener = listener;
            return this;
        }

        public Builder setMessage(int messageResId, OnLinearClickListener listener) {
            this.message = this.context.getResources().getString(messageResId);
            this.linearListener = listener;
            return this;
        }

        public Builder setMessage2(CharSequence message3, OnLinearClickListener listener) {
            this.message2 = message3;
            this.linearListener = listener;
            return this;
        }

        public Builder setMessage2(int messageResId, OnLinearClickListener listener) {
            this.message2 = this.context.getResources().getString(messageResId);
            this.linearListener = listener;
            return this;
        }

        public Builder setMessageColor(int color) {
            this.messageColor = color;
            return this;
        }

        public Builder setMessageColor(ColorStateList color) {
            this.messageColorStateList = color;
            return this;
        }

        public Builder setMessageSize(float size) {
            this.messageSize = size;
            return this;
        }

        public Builder setButton1(CharSequence text, OnClickListener listener) {
            this.button1Text = text;
            this.button1Listener = listener;
            this.button1Flag = 1;
            return this;
        }

        public Builder setButton1(int textId, OnClickListener listener) {
            this.button1Text = this.context.getResources().getString(textId);
            this.button1Listener = listener;
            this.button1Flag = 1;
            return this;
        }

        public Builder setButton1TextColor(int color) {
            this.button1TextColor = color;
            return this;
        }

        public Builder setButton1TextColor(ColorStateList color) {
            this.button1ColorStateList = color;
            return this;
        }

        public Builder setButton1Size(float button1Size2) {
            this.button1Size = button1Size2;
            return this;
        }

        public Builder setButton2(CharSequence text, OnClickListener listener) {
            this.button2Text = text;
            this.button2Listener = listener;
            this.button2Flag = 2;
            return this;
        }

        public Builder setButton2(int textId, OnClickListener listener) {
            this.button2Text = this.context.getResources().getString(textId);
            this.button2Listener = listener;
            this.button2Flag = 2;
            return this;
        }

        public Builder setButton2TextColor(int color) {
            this.button2TextColor = color;
            return this;
        }

        public Builder setButton2TextColor(ColorStateList color) {
            this.button2ColorStateList = color;
            return this;
        }

        public Builder setButton2Size(float button2Size2) {
            this.button2Size = button2Size2;
            return this;
        }

        public Builder setButton3(CharSequence text, OnClickListener listener) {
            this.button3Text = text;
            this.button3Listener = listener;
            this.button3Flag = 4;
            return this;
        }

        public Builder setButton3(int textId, OnClickListener listener) {
            this.button3Text = this.context.getResources().getString(textId);
            this.button3Listener = listener;
            this.button3Flag = 4;
            return this;
        }

        public Builder setButton3TextColor(int color) {
            this.button3TextColor = color;
            return this;
        }

        public Builder setButton3TextColor(ColorStateList color) {
            this.button3ColorStateList = color;
            return this;
        }

        public Builder setButton3Size(float button3Size2) {
            this.button3Size = button3Size2;
            return this;
        }

        public Builder setCancelable(boolean cancelable2) {
            this.cancelable = cancelable2;
            return this;
        }

        public Builder setCanceledOnTouchOutside(boolean canceled) {
            this.canceledOnTouchOutside = canceled;
            return this;
        }

        public Builder setView(View view2) {
            this.view = view2;
            return this;
        }

        @SuppressLint({"InflateParams"})
        public PromptDialog create() {
            if (this.dialog == null) {
                return null;
            }
            this.titleColor = -1;
            this.titlebarGravity = 3;
            View mView = LayoutInflater.from(this.context).inflate(R.layout.prompt_dialog_titlebar_skyblue, (ViewGroup) null);
            LinearLayout mTitleBar = (LinearLayout) mView.findViewById(R.id.titlebar);
            TextView mTitle = (TextView) mView.findViewById(R.id.title);
            TextView mMessage = (TextView) mView.findViewById(R.id.message);
            TextView mMessage2 = (TextView) mView.findViewById(R.id.message2);
            LinearLayout addView = (LinearLayout) mView.findViewById(R.id.layout_addview);
            LinearLayout addView2 = (LinearLayout) mView.findViewById(R.id.layout_addview2);
            addView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View arg0) {
                    if (Builder.this.linearListener != null) {
                        Builder.this.linearListener.onClick(0);
                    }
                }
            });
            addView2.setOnClickListener(new View.OnClickListener() {
                public void onClick(View arg0) {
                    if (Builder.this.linearListener != null) {
                        Builder.this.linearListener.onClick(1);
                    }
                }
            });
            TextView btnLeft = (TextView) mView.findViewById(R.id.button_left);
            TextView btnCenter = (TextView) mView.findViewById(R.id.button_center);
            TextView btnRight = (TextView) mView.findViewById(R.id.button_right);
            View btnDivider1 = mView.findViewById(R.id.btn_divider1);
            View btnDivider2 = mView.findViewById(R.id.btn_divider2);
            View msgBtnDivider = mView.findViewById(R.id.msg_btn_divider);
            LinearLayout btnView = (LinearLayout) mView.findViewById(R.id.btn_view);
            View title_msg_divider2 = mView.findViewById(R.id.title_msg_divider2);
            if (this.title == null && this.icon == null) {
                mTitle.setVisibility(View.GONE);
            } else {
                mTitle.setVisibility(View.VISIBLE);
                mTitle.setText(this.title);
                mTitle.setTextSize(this.titleSize);
                mTitle.setTextColor(this.titleColor);
                if (this.titleColorStateList != null) {
                    mTitle.setTextColor(this.titleColorStateList);
                }
                mTitle.setCompoundDrawables(this.icon, (Drawable) null, (Drawable) null, (Drawable) null);
                mTitleBar.setGravity(this.titlebarGravity);
            }
            if (this.message != null) {
                mMessage.setVisibility(View.VISIBLE);
                mMessage.setText(this.message);
                mMessage.setTextSize(this.messageSize);
                mMessage.setTextColor(this.messageColor);
                if (this.messageColorStateList != null) {
                    mMessage.setTextColor(this.messageColorStateList);
                }
            } else {
                mMessage.setVisibility(View.GONE);
            }
            if (this.message2 != null) {
                title_msg_divider2.setVisibility(View.VISIBLE);
                addView2.setVisibility(View.VISIBLE);
                mMessage2.setVisibility(View.VISIBLE);
                mMessage2.setText(this.message2);
                mMessage2.setTextSize(this.messageSize);
                mMessage2.setTextColor(this.messageColor);
                if (this.messageColorStateList != null) {
                    mMessage2.setTextColor(this.messageColorStateList);
                }
            } else {
                title_msg_divider2.setVisibility(View.GONE);
                addView2.setVisibility(View.GONE);
                mMessage2.setVisibility(View.GONE);
            }
            if (this.view != null) {
                addView.removeAllViews();
                addView.addView(this.view);
                addView.setGravity(17);
            }
            if (this.view != null) {
                addView2.removeAllViews();
                addView2.addView(this.view);
                addView2.setGravity(17);
            }
            switch (this.button1Flag + this.button2Flag + this.button3Flag) {
                case 1:
                case 5:
                    btnCenter.setVisibility(View.VISIBLE);
                    btnLeft.setVisibility(View.GONE);
                    btnRight.setVisibility(View.GONE);
                    btnCenter.setBackgroundResource(R.drawable.fynn_prompt_dialog_btn_single_selector);
                    if (this.button1Text != null) {
                        btnCenter.setText(this.button1Text);
                        btnCenter.setTextSize(this.button1Size);
                        btnCenter.setTextColor(this.button1TextColor);
                        if (this.button1ColorStateList != null) {
                            btnCenter.setTextColor(this.button1ColorStateList);
                        }
                        if (this.button1Listener != null) {
                            btnCenter.setOnClickListener(new View.OnClickListener() {
                                public void onClick(View arg0) {
                                    Builder.this.button1Listener.onClick(Builder.this.dialog, 1);
                                }
                            });
                            break;
                        }
                    }
                    break;
                case 3:
                    btnLeft.setVisibility(View.VISIBLE);
                    btnRight.setVisibility(View.VISIBLE);
                    btnCenter.setVisibility(View.GONE);
                    btnDivider1.setVisibility(View.VISIBLE);
                    btnDivider2.setVisibility(View.GONE);
                    if (this.button1Text != null) {
                        btnLeft.setText(this.button1Text);
                        btnLeft.setTextSize(this.button1Size);
                        btnLeft.setTextColor(this.button1TextColor);
                        if (this.button1ColorStateList != null) {
                            btnLeft.setTextColor(this.button1ColorStateList);
                        }
                        if (this.button1Listener != null) {
                            btnLeft.setOnClickListener(new View.OnClickListener() {
                                public void onClick(View arg0) {
                                    Builder.this.button1Listener.onClick(Builder.this.dialog, 1);
                                }
                            });
                        }
                    }
                    if (this.button2Text != null) {
                        btnRight.setText(this.button2Text);
                        btnRight.setTextSize(this.button2Size);
                        btnRight.setTextColor(this.button2TextColor);
                        if (this.button2ColorStateList != null) {
                            btnRight.setTextColor(this.button2ColorStateList);
                        }
                        if (this.button2Listener != null) {
                            btnRight.setOnClickListener(new View.OnClickListener() {
                                public void onClick(View arg0) {
                                    Builder.this.button2Listener.onClick(Builder.this.dialog, 2);
                                }
                            });
                            break;
                        }
                    }
                    break;
                case 7:
                    btnLeft.setVisibility(View.VISIBLE);
                    btnCenter.setVisibility(View.VISIBLE);
                    btnRight.setVisibility(View.VISIBLE);
                    btnDivider1.setVisibility(View.VISIBLE);
                    btnDivider2.setVisibility(View.VISIBLE);
                    if (this.button1Text != null) {
                        btnLeft.setText(this.button1Text);
                        btnLeft.setTextSize(this.button1Size);
                        btnLeft.setTextColor(this.button1TextColor);
                        if (this.button1ColorStateList != null) {
                            btnLeft.setTextColor(this.button1ColorStateList);
                        }
                        if (this.button1Listener != null) {
                            btnLeft.setOnClickListener(new View.OnClickListener() {
                                public void onClick(View arg0) {
                                    Builder.this.button1Listener.onClick(Builder.this.dialog, 1);
                                }
                            });
                        }
                    }
                    if (this.button2Text != null) {
                        btnCenter.setText(this.button2Text);
                        btnCenter.setText(this.button2Text);
                        btnCenter.setTextSize(this.button2Size);
                        btnCenter.setTextColor(this.button2TextColor);
                        if (this.button2ColorStateList != null) {
                            btnCenter.setTextColor(this.button2ColorStateList);
                        }
                        if (this.button2Listener != null) {
                            btnCenter.setOnClickListener(new View.OnClickListener() {
                                public void onClick(View arg0) {
                                    Builder.this.button2Listener.onClick(Builder.this.dialog, 2);
                                }
                            });
                        }
                    }
                    if (this.button3Text != null) {
                        btnRight.setText(this.button3Text);
                        btnRight.setTextSize(this.button3Size);
                        btnRight.setTextColor(this.button3TextColor);
                        if (this.button3ColorStateList != null) {
                            btnRight.setTextColor(this.button3ColorStateList);
                        }
                        if (this.button3Listener != null) {
                            btnRight.setOnClickListener(new View.OnClickListener() {
                                public void onClick(View arg0) {
                                    Builder.this.button3Listener.onClick(Builder.this.dialog, 3);
                                }
                            });
                            break;
                        }
                    }
                    break;
                default:
                    btnView.setVisibility(View.GONE);
                    msgBtnDivider.setVisibility(View.GONE);
                    break;
            }
            this.dialog.setCancelable(this.cancelable);
            this.dialog.setCanceledOnTouchOutside(this.canceledOnTouchOutside);
            this.dialog.setContentView(mView);
            return this.dialog;
        }

        public PromptDialog show() {
            create().show();
            return this.dialog;
        }
    }
}
