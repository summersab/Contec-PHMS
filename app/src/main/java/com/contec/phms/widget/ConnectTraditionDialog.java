package com.contec.phms.widget;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.contec.phms.R;
import com.contec.phms.manager.device.DeviceBean;
import com.contec.phms.util.CLog;
import com.contec.phms.util.Constants;
import com.contec.phms.util.DeviceNameUtils;

public class ConnectTraditionDialog implements View.OnClickListener {
    private String TAG = ConnectTraditionDialog.class.getSimpleName();
    private boolean isConnectTraditionDevice = false;
    private Button mBtn_cancel_connect;
    private Button mBtn_sure_connect;
    private Context mContext;
    private DeviceBean mDeviceBean;
    public Dialog mDialog;
    private ImageView mIv_device_icon;
    private TextView mTv_device_info;

    public ConnectTraditionDialog(Context context, DeviceBean deviceBean) {
        this.mContext = context;
        this.mDeviceBean = deviceBean;
        View view = LayoutInflater.from(this.mContext).inflate(R.layout.dialog_connect_tradition_device, (ViewGroup) null);
        initView(view);
        initData();
        this.mDialog = new AlertDialog.Builder(this.mContext).create();
        if (this.mDialog != null && this.mDialog.isShowing()) {
            this.mDialog.dismiss();
        }
        this.mDialog.show();
        this.mDialog.setContentView(view);
        this.mDialog.setCanceledOnTouchOutside(true);
        this.mDialog.setCancelable(true);
    }

    private void initView(View view) {
        int screenWidth = (UIUtils.getScreenWidth(this.mContext) * 3) / 4;
        this.mIv_device_icon = (ImageView) view.findViewById(R.id.iv_device_icon);
        this.mTv_device_info = (TextView) view.findViewById(R.id.tv_device_info);
        this.mBtn_sure_connect = (Button) view.findViewById(R.id.btn_sure_connect);
        this.mBtn_cancel_connect = (Button) view.findViewById(R.id.btn_cancel_connect);
        this.mBtn_sure_connect.setOnClickListener(this);
        this.mBtn_cancel_connect.setOnClickListener(this);
    }

    private void initData() {
        matchDeviceIconName(this.mDeviceBean.mDeviceName);
    }

    private void matchDeviceIconName(String pDeviceName) {
        if (Constants.DEVICE_8000GW_NAME.equalsIgnoreCase(pDeviceName)) {
            this.mIv_device_icon.setBackgroundResource(R.drawable.drawable_data_device_ecg);
            this.mTv_device_info.setText(String.valueOf(this.mContext.getString(R.string.str_8000G)) + this.mDeviceBean.mCode);
        } else if (Constants.PM10_NAME.equalsIgnoreCase(pDeviceName)) {
            this.mIv_device_icon.setBackgroundResource(R.drawable.drawable_device_pm10);
            this.mTv_device_info.setText(String.valueOf(this.mContext.getString(R.string.device_productname_pm10)) + this.mDeviceBean.mCode);
        } else if ("TEMP01".equalsIgnoreCase(pDeviceName)) {
            this.mIv_device_icon.setBackgroundResource(R.drawable.drawable_device_ear_temperature);
            this.mTv_device_info.setText(String.valueOf(this.mContext.getString(R.string.device_productname_hc06)) + this.mDeviceBean.mCode);
        } else if ("BC01".equalsIgnoreCase(pDeviceName)) {
            this.mIv_device_icon.setBackgroundResource(R.drawable.drawable_device_bc01);
            this.mTv_device_info.setText(String.valueOf(this.mContext.getString(R.string.device_productname_bc01)) + this.mDeviceBean.mCode);
        } else if ("CMS50D".equalsIgnoreCase(pDeviceName)) {
            this.mIv_device_icon.setBackgroundResource(R.drawable.drawable_device_cms50d);
            this.mTv_device_info.setText(String.valueOf(this.mContext.getString(R.string.device_productname_50EW)) + this.mDeviceBean.mCode);
        } else if (Constants.CMS50EW.equalsIgnoreCase(pDeviceName)) {
            this.mTv_device_info.setText(String.valueOf(this.mContext.getString(R.string.device_productname_50EW)) + this.mDeviceBean.mCode);
            this.mIv_device_icon.setBackgroundResource(R.drawable.drawable_data_device_cms50ew);
        } else if ("CMS50IW".equalsIgnoreCase(pDeviceName)) {
            this.mTv_device_info.setText(String.valueOf(this.mContext.getString(R.string.device_productname_50IW)) + this.mDeviceBean.mCode);
            this.mIv_device_icon.setBackgroundResource(R.drawable.cms50iw);
        } else if ("SP10W".equalsIgnoreCase(pDeviceName)) {
            this.mTv_device_info.setText(String.valueOf(this.mContext.getString(R.string.device_productname_SP10W)) + this.mDeviceBean.mCode);
            this.mIv_device_icon.setBackgroundResource(R.drawable.drawable_data_device_sp10w);
        } else if ("CMSSXT".equalsIgnoreCase(pDeviceName)) {
            this.mTv_device_info.setText(String.valueOf(this.mContext.getString(R.string.device_productname_SXT)) + this.mDeviceBean.mCode);
            this.mIv_device_icon.setBackgroundResource(R.drawable.drawable_data_device_cmxxst);
        } else if ("ABPM50W".equalsIgnoreCase(pDeviceName)) {
            this.mTv_device_info.setText(String.valueOf(this.mContext.getString(R.string.device_productname_M50W)) + this.mDeviceBean.mCode);
            this.mIv_device_icon.setBackgroundResource(R.drawable.drawable_data_device_abpm50);
        } else if (DeviceNameUtils.PM50.equalsIgnoreCase(pDeviceName)) {
            this.mTv_device_info.setText(String.valueOf(this.mContext.getString(R.string.device_productname_pm50)) + this.mDeviceBean.mCode);
            this.mIv_device_icon.setBackgroundResource(R.drawable.drawable_data_device_pm50);
        } else if (DeviceNameUtils.TEMP03.equalsIgnoreCase(pDeviceName)) {
            this.mTv_device_info.setText(String.valueOf(this.mContext.getString(R.string.device_productname_temp03)) + this.mDeviceBean.mCode);
            this.mIv_device_icon.setBackgroundResource(R.drawable.drawable_data_device_temp03);
        } else if ("CONTEC08AW".equalsIgnoreCase(pDeviceName)) {
            this.mTv_device_info.setText(String.valueOf(this.mContext.getString(R.string.device_productname_08AW)) + this.mDeviceBean.mCode);
            this.mIv_device_icon.setBackgroundResource(R.drawable.drawable_data_device_a08);
        } else if ("CONTEC08C".equalsIgnoreCase(pDeviceName)) {
            this.mTv_device_info.setText(String.valueOf(this.mContext.getString(R.string.device_productname_08AW)) + this.mDeviceBean.mCode);
            this.mIv_device_icon.setBackgroundResource(R.drawable.drawable_device_c08);
        } else if ("CMS50F".equalsIgnoreCase(pDeviceName)) {
            this.mTv_device_info.setText(String.valueOf(this.mContext.getString(R.string.device_name_50IW)) + this.mDeviceBean.mCode);
            this.mIv_device_icon.setBackgroundResource(R.drawable.drawable_device_cms50f);
        } else if ("WT".equalsIgnoreCase(pDeviceName)) {
            this.mTv_device_info.setText(String.valueOf(this.mContext.getString(R.string.device_productname_WT)) + this.mDeviceBean.mCode);
            this.mIv_device_icon.setBackgroundResource(R.drawable.drawable_data_device_wt);
        } else if ("FHR01".equalsIgnoreCase(pDeviceName)) {
            this.mTv_device_info.setText(String.valueOf(this.mContext.getString(R.string.device_productname_Fhr01)) + this.mDeviceBean.mCode);
            this.mIv_device_icon.setBackgroundResource(R.drawable.drawable_data_device_fhr01);
        } else if (Constants.PM85_NAME.equalsIgnoreCase(pDeviceName)) {
            this.mTv_device_info.setText(String.valueOf(this.mContext.getString(R.string.device_productname_Pm85)) + this.mDeviceBean.mCode);
            this.mIv_device_icon.setBackgroundResource(R.drawable.drawable_data_device_pm85);
        } else if (Constants.CMS50K_NAME.equalsIgnoreCase(pDeviceName)) {
            this.mTv_device_info.setText(String.valueOf(this.mContext.getString(R.string.device_productname_cms50k)) + this.mDeviceBean.mCode);
        } else if (Constants.CMS50K1_NAME.equalsIgnoreCase(pDeviceName)) {
            this.mTv_device_info.setText(String.valueOf(this.mContext.getString(R.string.device_productname_cms50k)) + this.mDeviceBean.mCode);
        }
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_sure_connect:
                this.isConnectTraditionDevice = true;
                dismiss();
                return;
            case R.id.btn_cancel_connect:
                this.isConnectTraditionDevice = false;
                dismiss();
                return;
            default:
                return;
        }
    }

    public Dialog getmDialog() {
        return this.mDialog;
    }

    public void dismiss() {
        if (this.mDialog != null && this.mDialog.isShowing()) {
            this.mDialog.dismiss();
        }
    }

    public boolean isConnectTraditionDevice() {
        return this.isConnectTraditionDevice;
    }

    public void LogI(String msg) {
        if (Constants.mTestFlag) {
            CLog.i(this.TAG, msg);
        }
    }

    public void LogE(String msg) {
        if (Constants.mTestFlag) {
            CLog.e(this.TAG, msg);
        }
    }
}
