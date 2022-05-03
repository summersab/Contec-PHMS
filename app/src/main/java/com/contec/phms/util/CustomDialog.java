package com.contec.phms.util;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.contec.phms.R;

public class CustomDialog extends Dialog {
    public CustomDialog(Context context, int theme) {
        super(context, theme);
    }

    public CustomDialog(Context context) {
        super(context);
    }

    public static class Builder {
        private View contentView;
        private Context context;
        private String message;
        private DialogInterface.OnClickListener negativeButtonClickListener;
        private String negativeButtonText;
        private DialogInterface.OnClickListener positiveButtonClickListener;
        private String positiveButtonText;
        private String title;

        public Builder(Context context2) {
            this.context = context2;
        }

        public Builder setMessage(String message2) {
            this.message = message2;
            return this;
        }

        public Builder setMessage(int message2) {
            this.message = (String) this.context.getText(message2);
            return this;
        }

        public Builder setTitle(int title2) {
            this.title = (String) this.context.getText(title2);
            return this;
        }

        public Builder setTitle(String title2) {
            this.title = title2;
            return this;
        }

        public Builder setContentView(View v) {
            this.contentView = v;
            return this;
        }

        public Builder setPositiveButton(int positiveButtonText2, DialogInterface.OnClickListener listener) {
            this.positiveButtonText = (String) this.context.getText(positiveButtonText2);
            this.positiveButtonClickListener = listener;
            return this;
        }

        public Builder setPositiveButton(String positiveButtonText2, DialogInterface.OnClickListener listener) {
            this.positiveButtonText = positiveButtonText2;
            this.positiveButtonClickListener = listener;
            return this;
        }

        public Builder setNegativeButton(int negativeButtonText2, DialogInterface.OnClickListener listener) {
            this.negativeButtonText = (String) this.context.getText(negativeButtonText2);
            this.negativeButtonClickListener = listener;
            return this;
        }

        public Builder setNegativeButton(String negativeButtonText2, DialogInterface.OnClickListener listener) {
            this.negativeButtonText = negativeButtonText2;
            this.negativeButtonClickListener = listener;
            return this;
        }

        public CustomDialog create() {
            final CustomDialog dialog = new CustomDialog(this.context, R.style.Dialog);
            dialog.setCanceledOnTouchOutside(false);
            View layout = ((LayoutInflater) this.context.getSystemService("layout_inflater")).inflate(R.layout.custom_dialog_layout, (ViewGroup) null);
            dialog.addContentView(layout, new ViewGroup.LayoutParams(-1, -2));
            ((TextView) layout.findViewById(R.id.title)).setText(this.title);
            if (this.positiveButtonText != null) {
                ((Button) layout.findViewById(R.id.positiveButton)).setText(this.positiveButtonText);
                if (this.positiveButtonClickListener != null) {
                    ((Button) layout.findViewById(R.id.positiveButton)).setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            Builder.this.positiveButtonClickListener.onClick(dialog, -1);
                        }
                    });
                }
            } else {
                layout.findViewById(R.id.positiveButton).setVisibility(View.GONE);
            }
            if (this.negativeButtonText != null) {
                ((Button) layout.findViewById(R.id.negativeButton)).setText(this.negativeButtonText);
                if (this.negativeButtonClickListener != null) {
                    ((Button) layout.findViewById(R.id.negativeButton)).setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            Builder.this.positiveButtonClickListener.onClick(dialog, -2);
                        }
                    });
                }
            } else {
                layout.findViewById(R.id.negativeButton).setVisibility(View.GONE);
            }
            if (this.message != null) {
                ((TextView) layout.findViewById(R.id.message)).setText(this.message);
            } else if (this.contentView != null) {
                ((LinearLayout) layout.findViewById(R.id.content)).removeAllViews();
                ((LinearLayout) layout.findViewById(R.id.content)).addView(this.contentView, new ViewGroup.LayoutParams(-2, -2));
            }
            dialog.setContentView(layout);
            return dialog;
        }
    }
}
