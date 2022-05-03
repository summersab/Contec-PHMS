package com.contec.phms.util;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.contec.phms.R;
import com.contec.phms.device.cms50k.update.Update50KUtils;

public class AlertDialogUtil {
    private static Dialog mDialog;
    public static Dialog mDialogCircle;
    public static ProgressBar mProgressbar;

    public static void showServerDialog(boolean pHaveProgressbar, String pmsg, final Context pContext) {
        View _customView = LayoutInflater.from(pContext).inflate(R.layout.layout_serverdialog, (ViewGroup) null);
        final AutoCompleteTextView mMsgText = (AutoCompleteTextView) _customView.findViewById(R.id.editText1);
        final AutoCompleteTextView mMsgReport = (AutoCompleteTextView) _customView.findViewById(R.id.editText2);
        if (Constants.URL.contains("us")) {
            Constants.URL = "http://us.contec365.com";
        } else {
            Constants.URL = Update50KUtils.MainURl;
        }
        if (Constants.URL_REPORT.contains("usm")) {
            Constants.URL_REPORT = "http://mobile.contec365.com";
        } else {
            Constants.URL_REPORT = "http://usm.contec365.com";
        }
        mMsgText.setText(Constants.URL);
        mMsgReport.setText(Constants.URL_REPORT);
        mMsgText.setAdapter(new ArrayAdapter<>(pContext, 17367048, new String[]{"http://us.contec365.com", Update50KUtils.MainURl}));
        mMsgText.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                mMsgText.showDropDown();
            }
        });
        mMsgText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View arg0, boolean arg1) {
                if (arg1) {
                    mMsgText.showDropDown();
                }
            }
        });
        mMsgReport.setAdapter(new ArrayAdapter<>(pContext, 17367048, new String[]{"http://usm.contec365.com", "http://mobile.contec365.com"}));
        mMsgReport.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mMsgReport.showDropDown();
            }
        });
        mMsgReport.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View arg0, boolean arg1) {
                if (arg1) {
                    mMsgReport.showDropDown();
                }
            }
        });
        Button mSureButton = (Button) _customView.findViewById(R.id.button1);
        Button mCancelButton = (Button) _customView.findViewById(R.id.button2);
        mDialogCircle = new Dialog(pContext, R.style.dialog_pedometer);
        mDialogCircle.setContentView(_customView);
        mDialogCircle.setCanceledOnTouchOutside(true);
        mDialogCircle.show();
        if (Constants.IS_PAD_NEW) {
            LinearLayout dialogLayout = (LinearLayout) _customView.findViewById(R.id.bg_dialog);
            mMsgText.setTextSize(2, (float) (ScreenAdapter.px2sp(pContext, mMsgText.getTextSize()) + 8));
            dialogLayout.getLayoutParams().width = (Constants.M_SCREENWEIGH * 1) / 3;
            dialogLayout.getLayoutParams().height = (Constants.M_SCREENWEIGH * 1) / 3;
        }
        mCancelButton.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                AlertDialogUtil.mDialogCircle.cancel();
                return true;
            }
        });
        mSureButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                String _URL_Data = mMsgText.getText().toString();
                String _URL_Report = mMsgReport.getText().toString();
                PageUtil.saveServerNameTOSharePre(pContext, String.valueOf(_URL_Data) + ";" + _URL_Report);
                Constants.URL = _URL_Data;
                Constants.URL_REPORT = _URL_Report;
                AlertDialogUtil.mDialogCircle.cancel();
            }
        });
    }

    public static void dimissDialog() {
        if (mDialog != null) {
            mDialog.dismiss();
        }
    }

    public static void showBlutLongDialog(boolean pHaveProgressbar, String pmsg, Context pContext) {
        View _customView = LayoutInflater.from(pContext).inflate(R.layout.layout_dialog_reprot_progressbar, (ViewGroup) null);
        TextView mMsgText = (TextView) _customView.findViewById(R.id.show_msg);
        mMsgText.setText(pmsg);
        mMsgText.setTextSize(20.0f);
        ((TextView) _customView.findViewById(R.id.show_msg_pedometer)).setVisibility(View.GONE);
        mDialogCircle = new Dialog(pContext, R.style.dialog_pedometer);
        mDialogCircle.setContentView(_customView);
        mDialogCircle.setCanceledOnTouchOutside(true);
        if (!mDialogCircle.isShowing()) {
            mDialogCircle.show();
        }
        if (Constants.IS_PAD_NEW) {
            LinearLayout dialogLayout = (LinearLayout) _customView.findViewById(R.id.bg_dialog);
            mMsgText.setTextSize(2, (float) (ScreenAdapter.px2sp(pContext, mMsgText.getTextSize()) + 8));
            dialogLayout.getLayoutParams().width = (Constants.M_SCREENWEIGH * 1) / 3;
            dialogLayout.getLayoutParams().height = (Constants.M_SCREENWEIGH * 1) / 3;
        } else {
            LinearLayout dialogLayout2 = (LinearLayout) _customView.findViewById(R.id.bg_dialog);
            dialogLayout2.getLayoutParams().width = (Constants.M_SCREENWEIGH * 4) / 5;
            dialogLayout2.getLayoutParams().height = (Constants.M_SCREENWEIGH * 2) / 5;
        }
        _customView.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                AlertDialogUtil.mDialogCircle.cancel();
                return true;
            }
        });
    }

    public static void cancleDialog() {
        if (mDialogCircle != null) {
            mDialogCircle.dismiss();
        }
    }

    public static void showPromotDialogparing(boolean pHaveProgressbar, String pmsg, Context pContext) {
        View _customView = LayoutInflater.from(pContext).inflate(R.layout.layout_dialog_paring, (ViewGroup) null);
        TextView mMsgText = (TextView) _customView.findViewById(R.id.textView1);
        mMsgText.setTextColor(pContext.getResources().getColor(R.color.black));
        mMsgText.setTextSize(26.0f);
        mMsgText.setText(pmsg);
        mDialogCircle = new Dialog(pContext, R.style.dialog_pedometer);
        mDialogCircle.setContentView(_customView);
        mDialogCircle.getWindow().getAttributes().gravity = 48;
        mDialogCircle.setCanceledOnTouchOutside(true);
        mDialogCircle.getWindow().setType(2003);
        mDialogCircle.show();
        if (Constants.IS_PAD_NEW) {
            LinearLayout dialogLayout = (LinearLayout) _customView.findViewById(R.id.bg_dialog);
            mMsgText.setTextSize(2, (float) (ScreenAdapter.px2sp(pContext, mMsgText.getTextSize()) + 8));
            dialogLayout.getLayoutParams().width = (Constants.M_SCREENWEIGH * 1) / 3;
            dialogLayout.getLayoutParams().height = (Constants.M_SCREENWEIGH * 1) / 3;
        }
        LinearLayout dialogLayout2 = (LinearLayout) _customView.findViewById(R.id.bg_dialog);
        dialogLayout2.getLayoutParams().width = (Constants.M_SCREENWEIGH * 2) / 3;
        dialogLayout2.getLayoutParams().height = (Constants.M_SCREENWEIGH * 3) / 12;
        _customView.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                AlertDialogUtil.mDialogCircle.cancel();
                return true;
            }
        });
    }

    public static void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static void showToast(Context context, int resourceId) {
        Toast.makeText(context, context.getResources().getString(resourceId), Toast.LENGTH_SHORT).show();
    }

    public static AlertDialog alertDialog(Context pContext, int pMessageID, int pButtonTextID, View.OnClickListener pOnClickListener) {
        return new AlertDialog.Builder(pContext).setMessage(pContext.getString(pMessageID)).setPositiveButton(pButtonTextID, (DialogInterface.OnClickListener) pOnClickListener).show();
    }

    public static AlertDialog MyTwoButtonDialog(Context pContext, String pDialogTitle, int pIcon, String pDialogContent, String pOkButtonText, DialogInterface.OnClickListener m_UpdateListener, String pNegativeButtonText, DialogInterface.OnClickListener m_CancleUpdateListener, boolean pSetCancelable) {
        AlertDialog.Builder _Builder = new AlertDialog.Builder(pContext);
        _Builder.setCancelable(pSetCancelable);
        _Builder.setTitle(pDialogTitle);
        _Builder.setIcon(pIcon);
        _Builder.setMessage(pDialogContent);
        _Builder.setPositiveButton(pOkButtonText, m_UpdateListener);
        _Builder.setNegativeButton(pNegativeButtonText, m_CancleUpdateListener);
        return _Builder.show();
    }
}
