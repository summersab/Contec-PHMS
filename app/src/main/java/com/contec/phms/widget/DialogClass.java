package com.contec.phms.widget;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.contec.phms.R;
import com.contec.phms.util.Constants;
import com.contec.phms.util.ScreenAdapter;
import com.contec.phms.util.UpdateManeger;

public class DialogClass {
    public static Dialog mDialog;
    public static CheckBox m_not_ask_CheckBox;
    public static AlertDialog myDialog;

    public DialogClass(Context pContext, String str, View.OnClickListener p_listener, boolean m) {
    }

    public DialogClass(Context pContext, String str, View.OnClickListener p_listener) {
    }

    public DialogClass(Context pContext, String str, boolean isSetSize, int pTextsize, DialogInterface.OnKeyListener ponKeyListener) {
        View _customView = LayoutInflater.from(pContext).inflate(R.layout.layout_dialog_circle_progressbar, (ViewGroup) null);
        TextView mMsgText = (TextView) _customView.findViewById(R.id.show_msg);
        mMsgText.setText(str);
        ((TextView) _customView.findViewById(R.id.show_msg_pedometer)).setVisibility(View.GONE);
        mDialog = new Dialog(pContext, R.style.dialog_pedometer);
        mDialog.show();
        mDialog.setContentView(_customView);
        mDialog.setCanceledOnTouchOutside(true);
        mDialog.setCancelable(false);
        if (ponKeyListener != null) {
            mDialog.setOnKeyListener(ponKeyListener);
        }
        mDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            public void onShow(DialogInterface dialog) {
            }
        });
        LinearLayout dialogLayout = (LinearLayout) _customView.findViewById(R.id.bg_dialog);
        dialogLayout.getLayoutParams().width = (Constants.M_SCREENWEIGH * 5) / 7;
        dialogLayout.getLayoutParams().height = (Constants.M_SCREENWEIGH * 10) / 21;
        if (Constants.IS_PAD_NEW) {
            mMsgText.setTextSize(2, (float) (ScreenAdapter.px2sp(pContext, mMsgText.getTextSize()) + 8));
            dialogLayout.getLayoutParams().width = (Constants.M_SCREENWEIGH * 5) / 7;
            dialogLayout.getLayoutParams().height = (Constants.M_SCREENWEIGH * 10) / 21;
        }
    }

    public DialogClass(Context pContext, String str, DialogInterface.OnKeyListener ponKeyListener, View.OnClickListener psuerListener, View.OnClickListener pCanleListener) {
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }
        View _customView = LayoutInflater.from(pContext).inflate(R.layout.layout_dialog_login_another_place, (ViewGroup) null);
        ((Button) _customView.findViewById(R.id.sure)).setOnClickListener(psuerListener);
        ((Button) _customView.findViewById(R.id.cancle)).setOnClickListener(pCanleListener);
        ((TextView) _customView.findViewById(R.id.show_msg)).setText(str);
        mDialog = new Dialog(pContext, R.style.dialog_pedometer);
        mDialog.setContentView(_customView);
        mDialog.setCanceledOnTouchOutside(true);
        mDialog.setCancelable(false);
        if (ponKeyListener != null) {
            mDialog.setOnKeyListener(ponKeyListener);
        }
        mDialog.getWindow().setType(2003);
        mDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            public void onShow(DialogInterface dialog) {
            }
        });
        Window dialogWindow = mDialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = (Constants.M_SCREENWEIGH * 3) / 4;
        lp.height = (Constants.M_SCREENWEIGH * 1) / 2;
        mDialog.setCanceledOnTouchOutside(false);
        dialogWindow.setAttributes(lp);
        mDialog.show();
    }

    public DialogClass(Context pContext, String strtitle, String strcontent, DialogInterface.OnKeyListener ponKeyListener, View.OnClickListener psuerListener, View.OnClickListener pCanleListener) {
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }
        View _customView = LayoutInflater.from(pContext).inflate(R.layout.layout_dialog_remove_devices, (ViewGroup) null);
        ((Button) _customView.findViewById(R.id.sure)).setOnClickListener(psuerListener);
        ((Button) _customView.findViewById(R.id.cancle)).setOnClickListener(pCanleListener);
        ((TextView) _customView.findViewById(R.id.titletext)).setText(new StringBuilder(String.valueOf(strtitle)).toString());
        ((TextView) _customView.findViewById(R.id.show_msg)).setText(new StringBuilder(String.valueOf(strcontent)).toString());
        mDialog = new Dialog(pContext, R.style.dialog_pedometer);
        mDialog.show();
        mDialog.setContentView(_customView);
        mDialog.setCanceledOnTouchOutside(true);
        mDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            public void onShow(DialogInterface dialog) {
            }
        });
        Window dialogWindow = mDialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = (Constants.M_SCREENWEIGH * 3) / 4;
        lp.height = Constants.M_SCREENWEIGH / 2;
        dialogWindow.setAttributes(lp);
    }

    public DialogClass(Context pContext, String str, DialogInterface.OnKeyListener ponKeyListener, View.OnClickListener psuerListener, View.OnClickListener pCanleListener, String test) {
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }
        View _customView = LayoutInflater.from(pContext).inflate(R.layout.layout_dialog_deletedevice, (ViewGroup) null);
        TextView mMsgText = (TextView) _customView.findViewById(R.id.show_msg);
        ((Button) _customView.findViewById(R.id.sure)).setOnClickListener(psuerListener);
        ((Button) _customView.findViewById(R.id.cancle)).setOnClickListener(pCanleListener);
        mMsgText.setText(str);
        mDialog = new Dialog(pContext, R.style.dialog_pedometer);
        mDialog.setContentView(_customView);
        mDialog.setCanceledOnTouchOutside(true);
        mDialog.setCancelable(false);
        if (ponKeyListener != null) {
            mDialog.setOnKeyListener(ponKeyListener);
        }
        mDialog.getWindow().setType(2003);
        mDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            public void onShow(DialogInterface dialog) {
            }
        });
        LinearLayout dialogLayout = (LinearLayout) _customView.findViewById(R.id.bg_dialog);
        dialogLayout.getLayoutParams().width = (Constants.M_SCREENWEIGH * 5) / 7;
        dialogLayout.getLayoutParams().height = (Constants.M_SCREENWEIGH * 10) / 21;
        if (Constants.IS_PAD_NEW) {
            mMsgText.setTextSize(2, (float) (ScreenAdapter.px2sp(pContext, mMsgText.getTextSize()) + 8));
            dialogLayout.getLayoutParams().width = (Constants.M_SCREENWEIGH * 5) / 7;
            dialogLayout.getLayoutParams().height = (Constants.M_SCREENWEIGH * 10) / 21;
        }
        mDialog.show();
    }

    public int dip2px(Context context, float dpValue) {
        return (int) ((dpValue * context.getResources().getDisplayMetrics().density) + 0.5f);
    }

    public DialogClass(Context pContext, String str, View.OnClickListener pokonClick, View.OnClickListener pcancelonClic) {
        double phone_or_pad_dialog_scale;
        if (myDialog != null) {
            myDialog.dismiss();
        }
        myDialog = new AlertDialog.Builder(pContext).create();
        myDialog.show();
        View layout = ((LayoutInflater) pContext.getSystemService("layout_inflater")).inflate(R.layout.layout_errordialog, (ViewGroup) null);
        TextView _text_message = (TextView) layout.findViewById(R.id.dialogonlyhave_text);
        _text_message.setText(str);
        ((TextView) layout.findViewById(R.id.errordialogensure)).setOnClickListener(pokonClick);
        ((TextView) layout.findViewById(R.id.errordialogecancel)).setOnClickListener(pcancelonClic);
        myDialog.getWindow().setContentView(layout);
        Window dialogWindow = myDialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        if (Constants.IS_PAD_NEW) {
            phone_or_pad_dialog_scale = 0.75d;
            _text_message.setTextSize(2, (float) (ScreenAdapter.px2sp(pContext, _text_message.getTextSize()) + 8));
        } else {
            phone_or_pad_dialog_scale = 1.0d;
        }
        lp.width = (int) (((double) (Constants.M_SCREENWEIGH / 2)) * phone_or_pad_dialog_scale);
        lp.height = (int) (((double) (Constants.M_SCREENWEIGH / 2)) * phone_or_pad_dialog_scale);
        myDialog.setCanceledOnTouchOutside(false);
        dialogWindow.setAttributes(lp);
    }

    public DialogClass(Context pContext, String strcode, String strcontent, View.OnClickListener p_listener) {
        if (myDialog != null) {
            myDialog.dismiss();
        }
        myDialog = new AlertDialog.Builder(pContext).create();
        myDialog.show();
        View layout = ((LayoutInflater) pContext.getSystemService("layout_inflater")).inflate(R.layout.layout_versionupdata, (ViewGroup) null);
        TextView mversionupdatatiptv = (TextView) layout.findViewById(R.id.versionupdatatiptv);
        ((TextView) layout.findViewById(R.id.updata_versioncode)).setText(strcode);
        ((TextView) layout.findViewById(R.id.updata_versioncontent)).setText(strcontent);
        LinearLayout mupdate_ensurebtn = (LinearLayout) layout.findViewById(R.id.update_ensurebtn);
        LinearLayout mupdata_cancelbtn = (LinearLayout) layout.findViewById(R.id.updata_cancelbtn);
        if (Constants.IS_PAD_NEW) {
            ScreenAdapter.changeLayoutTextSize(pContext, (LinearLayout) layout.findViewById(R.id.layout_updatecontent_main), 10);
            mversionupdatatiptv.getLayoutParams().height = ScreenAdapter.dip2px(pContext, 55.0f);
        }
        mupdate_ensurebtn.setOnClickListener(p_listener);
        mupdata_cancelbtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                DialogClass.myDialog.dismiss();
                UpdateManeger.cancelUpdate = true;
            }
        });
        myDialog.getWindow().setContentView(layout);
        Window dialogWindow = myDialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = (Constants.M_SCREENWEIGH * 3) / 4;
        lp.height = (Constants.M_SCREENHEIGH * 1) / 2;
        myDialog.setCanceledOnTouchOutside(false);
        dialogWindow.setAttributes(lp);
    }

    public DialogClass(Context pContext, String textview_tip) {
        View _customView = LayoutInflater.from(pContext).inflate(R.layout.layout_devicelist_delitem_tip, (ViewGroup) null);
        TextView mMsgText = (TextView) _customView.findViewById(R.id.textview_tip);
        mMsgText.setText(textview_tip);
        mDialog = new AlertDialog.Builder(pContext).create();
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }
        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable());
        mDialog.show();
        mDialog.setContentView(_customView);
        mDialog.setCanceledOnTouchOutside(true);
        mDialog.setCancelable(true);
        LinearLayout dialogLayout = (LinearLayout) _customView.findViewById(R.id.bg_dialog);
        dialogLayout.getLayoutParams().width = (Constants.M_SCREENWEIGH * 5) / 7;
        dialogLayout.getLayoutParams().height = (Constants.M_SCREENWEIGH * 7) / 21;
        if (Constants.IS_PAD_NEW) {
            mMsgText.setTextSize(2, (float) (ScreenAdapter.px2sp(pContext, mMsgText.getTextSize()) + 8));
            dialogLayout.getLayoutParams().width = (Constants.M_SCREENWEIGH * 5) / 7;
            dialogLayout.getLayoutParams().height = (Constants.M_SCREENWEIGH * 10) / 21;
        }
    }

    public DialogClass(final Context pContext, String textview_tip, int newImage, int oldImage) {
        View _customView = LayoutInflater.from(pContext).inflate(R.layout.uninstalloldphms, (ViewGroup) null);
        Button mbtnText = (Button) _customView.findViewById(R.id.btn_uninstall);
        mbtnText.setText(textview_tip);
        mbtnText.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                pContext.startActivity(new Intent("android.intent.action.DELETE", Uri.parse("package:com.contec")));
            }
        });
        mDialog = new AlertDialog.Builder(pContext).create();
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }
        mDialog.show();
        mDialog.setContentView(_customView);
        mDialog.setCanceledOnTouchOutside(true);
        mDialog.setCancelable(false);
        LinearLayout dialogLayout = (LinearLayout) _customView.findViewById(R.id.bg_dialog);
        dialogLayout.getLayoutParams().width = (int) (((float) (Constants.M_SCREENWEIGH * 4)) / 4.2f);
        dialogLayout.getLayoutParams().height = (Constants.M_SCREENHEIGH * 4) / 5;
        if (Constants.IS_PAD_NEW) {
            mbtnText.setTextSize(2, (float) (ScreenAdapter.px2sp(pContext, mbtnText.getTextSize()) + 8));
            dialogLayout.getLayoutParams().width = (Constants.M_SCREENWEIGH * 5) / 7;
            dialogLayout.getLayoutParams().height = (Constants.M_SCREENWEIGH * 10) / 21;
        }
    }

    public DialogClass(Context pContext, int time, String textview_tip) {
        View _customView = LayoutInflater.from(pContext).inflate(R.layout.layout_devicelist_delitem_tip, (ViewGroup) null);
        TextView mMsgText = (TextView) _customView.findViewById(R.id.textview_tip);
        mMsgText.setText(textview_tip);
        mDialog = new AlertDialog.Builder(pContext).create();
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }
        mDialog.show();
        mDialog.setContentView(_customView);
        mDialog.setCanceledOnTouchOutside(true);
        mDialog.setCancelable(true);
        LinearLayout dialogLayout = (LinearLayout) _customView.findViewById(R.id.bg_dialog);
        dialogLayout.getLayoutParams().width = (Constants.M_SCREENWEIGH * 5) / 7;
        dialogLayout.getLayoutParams().height = (Constants.M_SCREENWEIGH * 7) / 21;
        if (Constants.IS_PAD_NEW) {
            mMsgText.setTextSize(2, (float) (ScreenAdapter.px2sp(pContext, mMsgText.getTextSize()) + 8));
            dialogLayout.getLayoutParams().width = (Constants.M_SCREENWEIGH * 5) / 7;
            dialogLayout.getLayoutParams().height = (Constants.M_SCREENWEIGH * 10) / 21;
        }
        try {
            Thread.sleep((long) time);
            mDialog.dismiss();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public DialogClass(Context pContext, String textview_tip, int pTextSize) {
        View _customView = LayoutInflater.from(pContext).inflate(R.layout.layout_devicelist_delitem_tip, (ViewGroup) null);
        TextView mMsgText = (TextView) _customView.findViewById(R.id.textview_tip);
        mMsgText.setTextSize((float) pTextSize);
        mMsgText.setText(textview_tip);
        mDialog = new AlertDialog.Builder(pContext).create();
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }
        mDialog.show();
        mDialog.setContentView(_customView);
        mDialog.setCanceledOnTouchOutside(true);
        mDialog.setCancelable(true);
        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable());
        LinearLayout dialogLayout = (LinearLayout) _customView.findViewById(R.id.bg_dialog);
        dialogLayout.getLayoutParams().width = (Constants.M_SCREENWEIGH * 5) / 7;
        dialogLayout.getLayoutParams().height = (Constants.M_SCREENWEIGH * 10) / 21;
        if (Constants.IS_PAD_NEW) {
            mMsgText.setTextSize(2, (float) (ScreenAdapter.px2sp(pContext, mMsgText.getTextSize()) + 8));
            dialogLayout.getLayoutParams().width = (Constants.M_SCREENWEIGH * 5) / 7;
            dialogLayout.getLayoutParams().height = (Constants.M_SCREENWEIGH * 10) / 21;
        }
    }

    public DialogClass(Context pContext, String pDeviceTyep, String pDeviceCode) {
        View _customView = LayoutInflater.from(pContext).inflate(R.layout.layout_handadddevicesuccessdialog, (ViewGroup) null);
        TextView mMsgText = (TextView) _customView.findViewById(R.id.handadddevicesuccessinfo);
        mMsgText.setText(String.valueOf(pDeviceTyep) + "-" + pDeviceCode);
        mDialog = new AlertDialog.Builder(pContext).create();
        mDialog.show();
        mDialog.setContentView(_customView);
        mDialog.setCanceledOnTouchOutside(true);
        mDialog.setCancelable(true);
        mDialog.getWindow().setType(2003);
        LinearLayout dialogLayout = (LinearLayout) _customView.findViewById(R.id.bg_dialog);
        dialogLayout.getLayoutParams().width = (Constants.M_SCREENWEIGH * 5) / 6;
        dialogLayout.getLayoutParams().height = (Constants.M_SCREENWEIGH * 7) / 18;
        if (Constants.IS_PAD_NEW) {
            mMsgText.setTextSize(2, (float) (ScreenAdapter.px2sp(pContext, mMsgText.getTextSize()) + 8));
            dialogLayout.getLayoutParams().width = (Constants.M_SCREENWEIGH * 5) / 7;
            dialogLayout.getLayoutParams().height = (Constants.M_SCREENWEIGH * 10) / 21;
        }
    }

    public void show() {
        if (myDialog != null && !myDialog.isShowing()) {
            myDialog.show();
        }
    }

    public void dismiss() {
        if (myDialog != null && myDialog.isShowing()) {
            myDialog.dismiss();
        }
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }
    }

    public DialogClass(Context pContext, int resouce, String strcontent, View.OnClickListener psuerListener, View.OnClickListener pCanleListener) {
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }
        View _customView = LayoutInflater.from(pContext).inflate(R.layout.layout_dialog_sacn_new_device, (ViewGroup) null);
        ((Button) _customView.findViewById(R.id.bt_sure)).setOnClickListener(psuerListener);
        ((Button) _customView.findViewById(R.id.bt_cancle)).setOnClickListener(pCanleListener);
        ((TextView) _customView.findViewById(R.id.tv_type)).setText(new StringBuilder(String.valueOf(strcontent)).toString());
        ((ImageView) _customView.findViewById(R.id.iv_icon)).setImageResource(resouce);
        mDialog = new Dialog(pContext, R.style.dialog_pedometer);
        mDialog.show();
        mDialog.setContentView(_customView);
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            public void onShow(DialogInterface dialog) {
            }
        });
        Window dialogWindow = mDialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = (Constants.M_SCREENWEIGH * 3) / 4;
        lp.height = Constants.M_SCREENWEIGH / 2;
        dialogWindow.setAttributes(lp);
    }

    public DialogClass(Context pContext, View.OnClickListener psuerListener, View.OnClickListener pCanleListener) {
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }
        View _customView = LayoutInflater.from(pContext).inflate(R.layout.layout_dialog_jump_health_report, (ViewGroup) null);
        ((Button) _customView.findViewById(R.id.sure)).setOnClickListener(psuerListener);
        ((Button) _customView.findViewById(R.id.cancle)).setOnClickListener(pCanleListener);
        mDialog = new Dialog(pContext, R.style.dialog_pedometer);
        mDialog.setContentView(_customView);
        mDialog.setCanceledOnTouchOutside(true);
        mDialog.setCancelable(false);
        mDialog.getWindow().setType(2003);
        mDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            public void onShow(DialogInterface dialog) {
            }
        });
        Window dialogWindow = mDialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = (Constants.M_SCREENWEIGH * 3) / 4;
        lp.height = (Constants.M_SCREENWEIGH * 1) / 2;
        mDialog.setCanceledOnTouchOutside(false);
        dialogWindow.setAttributes(lp);
        mDialog.show();
    }

    private int getResource(int type) {
        return R.drawable.drawable_data_device_a08;
    }
}
