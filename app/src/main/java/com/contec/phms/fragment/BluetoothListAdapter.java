package com.contec.phms.fragment;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.contec.phms.R;
import com.contec.phms.manager.device.DeviceBean;
import com.contec.phms.manager.device.DeviceBeanList;
import com.contec.phms.manager.device.DeviceManager;
import com.contec.phms.util.CLog;
import com.contec.phms.util.Constants;
import com.contec.phms.util.DeviceNameUtils;
import com.contec.phms.util.PageUtil;
import com.contec.phms.util.ScreenAdapter;
import com.contec.phms.widget.CustomDevicescellview;
import android.widget.GridLayout;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BluetoothListAdapter extends BaseAdapter {
    float DownX;
    private final String TAG = "BluetoothListAdapter";
    float UpX;
    DeviceBeanList _deviceBeanList;
    private ProgressBar contectProgress;
    private Context context;
    private ImageView deviceimags;
    private Map<Integer, Integer> isVisibility;
    boolean mConnect;
    private View mDeleteView;
    private LayoutInflater mInflater;
    private List<DeviceBeanList> mNewlistBean = new ArrayList();
    boolean mUse;
    private TextView name;
    private TextView progressText;

    public BluetoothListAdapter(Context context2) {
        this.mInflater = LayoutInflater.from(context2);
        this.context = context2;
    }

    public int getCount() {
        return this.mNewlistBean.size();
    }

    public void setmNewlistBean(List<DeviceBeanList> mNewlistBean2) {
        this.mNewlistBean = mNewlistBean2;
    }

    public DeviceBeanList getItem(int position) {
        return this.mNewlistBean.get(position);
    }

    public long getItemId(int position) {
        return (long) position;
    }

    public void addBluetoothListBean(DeviceBeanList p_deviceBean) {
        this.mNewlistBean.add(p_deviceBean);
    }

    public void getDeviceList() {
        this.mNewlistBean = DeviceManager.mDeviceList.getListDevice();
    }

    public void remove(int i) {
        this.mNewlistBean.remove(i);
    }

    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View mconvertView = LayoutInflater.from(this.context).inflate(R.layout.layout_device_item_copy, (ViewGroup) null);
        this.name = (TextView) mconvertView.findViewById(R.id.device_info);
        this.deviceimags = (ImageView) mconvertView.findViewById(R.id.deviceimags);
        this.progressText = (TextView) mconvertView.findViewById(R.id.progresstext);
        this.contectProgress = (ProgressBar) mconvertView.findViewById(R.id.waiting_contect);
        GridLayout mGridLayout = (GridLayout) mconvertView.findViewById(R.id.grid_layout);
        this._deviceBeanList = this.mNewlistBean.get(position);
        String _device = this._deviceBeanList.mDeviceName;
        int i = this._deviceBeanList.mState;
        matchDevice(_device);
        this.name.setTextColor(this.context.getResources().getColor(R.color.blue_1));
        this.progressText.setVisibility(View.VISIBLE);
        this.progressText.setTextColor(this.context.getResources().getColor(R.color.default_pedometer_textcolor));
        List<DeviceBean> _Beans = this.mNewlistBean.get(position).mBeanList;
        if (_Beans != null) {
            for (int i2 = 0; i2 < _Beans.size(); i2++) {
                DeviceBean _devicebean = _Beans.get(i2);
                String _beanscode = _devicebean.mCode;
                String _beanname = _devicebean.mDeviceName;
                int _beanId = _devicebean.mId;
                int _beanstate = _devicebean.mState;
                GridLayout.LayoutParams mNodealaddbuttonparmas = new GridLayout.LayoutParams();
                mNodealaddbuttonparmas.rightMargin = ScreenAdapter.dip2px(this.context, 3.0f);
                mNodealaddbuttonparmas.setGravity(17);
                CustomDevicescellview customDevicescellview = new CustomDevicescellview(this.context);
                customDevicescellview.setmFailedReasons(_devicebean.mFailedReasons);
                if (PageUtil.getFailedTimes(Constants.DataPath, _Beans.get(i2).getDeivceUniqueness()) > 0) {
                    customDevicescellview.setmHasFailedFile(true);
                } else {
                    customDevicescellview.setmHasFailedFile(false);
                }
                customDevicescellview.setItemText(_beanscode);
                customDevicescellview.setmDeviceName(_beanname);
                customDevicescellview.setmDeviceID(_beanId);
                customDevicescellview.setdelbtnvisiable(this.mNewlistBean.get(position).misShowDelBtn);
                customDevicescellview.setMdevicebean(_devicebean);
                if (_devicebean.mProgress != 0) {
                    customDevicescellview.setprogressbarprogress(_devicebean.mProgress, _beanstate);
                }
                customDevicescellview.setmState(_beanstate);
                CLog.i("lw", String.valueOf(position) + " name = " + _devicebean.mDeviceName + " _beanscode =" + _beanscode);
                mGridLayout.addView(customDevicescellview, mGridLayout.getChildCount(), mNodealaddbuttonparmas);
            }
        }
        if (Constants.IS_PAD_NEW) {
            ((LinearLayout) mconvertView.findViewById(R.id.layout_progress)).getLayoutParams().width = ScreenAdapter.dip2px(this.context, 90.0f);
            ScreenAdapter.changeLayoutTextSize(this.context, (RelativeLayout) mconvertView.findViewById(R.id.layout_bluetoothadapter_main), 3);
            RelativeLayout.LayoutParams _deviceimagslpparams = (RelativeLayout.LayoutParams) this.deviceimags.getLayoutParams();
            _deviceimagslpparams.leftMargin = ScreenAdapter.dip2px(this.context, 25.0f);
            _deviceimagslpparams.rightMargin = ScreenAdapter.dip2px(this.context, 20.0f);
            this.progressText.setTextSize(2, 2.13116518E9f);
        }
        return mconvertView;
    }

    protected void removeListItem(View rowView) {
        rowView.setAnimation(AnimationUtils.loadAnimation(this.context, R.anim.push_left_in));
    }

    void diappear(View rowView) {
        if (rowView.getVisibility() == View.VISIBLE) {
            rowView.setAnimation(AnimationUtils.loadAnimation(this.context, R.anim.push_right_out));
        }
    }

    private void switchTextClor(int p_state, TextView p_TV) {
        Log.e("BluetoothListAdapter", "-------------------------->" + p_state);
        switch (p_state) {
            case 2:
                if (DeviceManager.m_DeviceBean != null && DeviceManager.m_DeviceBean.mDeviceName.equals("HC")) {
                    this.contectProgress.setVisibility(View.VISIBLE);
                    p_TV.setText(this.context.getResources().getString(R.string.str_linked));
                    return;
                }
                return;
            case 4:
                this.contectProgress.setVisibility(View.VISIBLE);
                p_TV.setText(this.context.getResources().getString(R.string.str_upload_case));
                return;
            case 5:
                this.contectProgress.setVisibility(View.INVISIBLE);
                p_TV.setText(this.context.getResources().getString(R.string.str_upload_failed));
                return;
            case 6:
                this.contectProgress.setVisibility(View.VISIBLE);
                p_TV.setText(this.context.getResources().getString(R.string.str_upload_case));
                return;
            case 7:
                this.contectProgress.setVisibility(View.VISIBLE);
                p_TV.setText(this.context.getResources().getString(R.string.str_upload_case));
                return;
            case 8:
                this.contectProgress.setVisibility(View.INVISIBLE);
                p_TV.setText(this.context.getResources().getString(R.string.str_upload_success));
                return;
            case 9:
                this.contectProgress.setVisibility(View.INVISIBLE);
                p_TV.setText(this.context.getResources().getString(R.string.str_upload_failed));
                return;
            case 31:
                this.contectProgress.setVisibility(View.INVISIBLE);
                p_TV.setText(this.context.getResources().getString(R.string.update_filed));
                return;
            case 32:
                this.contectProgress.setVisibility(View.INVISIBLE);
                p_TV.setText(this.context.getResources().getString(R.string.update_cancle));
                return;
            case 33:
                this.contectProgress.setVisibility(View.INVISIBLE);
                p_TV.setText(this.context.getResources().getString(R.string.update_successful));
                return;
            case 34:
                this.contectProgress.setVisibility(View.INVISIBLE);
                p_TV.setText("Upgrade failed");
                return;
            default:
                return;
        }
    }

    private void matchDevice(String _device) {
        if (Constants.DEVICE_8000GW_NAME.equalsIgnoreCase(_device)) {
            this.name.setText(this.context.getResources().getString(R.string.str_8000G));
            this.deviceimags.setImageResource(R.drawable.drawable_data_device_ecg);
        } else if (Constants.PM10_NAME.equalsIgnoreCase(_device)) {
            this.name.setText(this.context.getResources().getString(R.string.device_productname_pm10));
            this.deviceimags.setImageResource(R.drawable.drawable_device_pm10);
        } else if ("TEMP01".equalsIgnoreCase(_device)) {
            this.name.setText(this.context.getResources().getString(R.string.device_productname_hc06));
            this.deviceimags.setImageResource(R.drawable.drawable_device_ear_temperature);
        } else if ("BC01".equalsIgnoreCase(_device)) {
            this.name.setText(this.context.getResources().getString(R.string.device_productname_bc01));
            this.deviceimags.setImageResource(R.drawable.drawable_device_bc01);
        } else if ("CMS50D".equalsIgnoreCase(_device)) {
            this.name.setText(this.context.getResources().getString(R.string.device_productname_cmd50d));
            this.deviceimags.setImageResource(R.drawable.drawable_device_cms50d);
        } else if ("CMS50IW".equals(_device)) {
            this.name.setText(this.context.getResources().getString(R.string.device_productname_50IW));
            this.deviceimags.setImageResource(R.drawable.cms50iw);
        } else if ("cmssxt".equalsIgnoreCase(_device)) {
            this.name.setText(this.context.getResources().getString(R.string.device_productname_SXT));
            this.deviceimags.setImageResource(R.drawable.drawable_data_device_cmxxst);
        } else if ("ABPM50W".equalsIgnoreCase(_device)) {
            this.name.setText(this.context.getResources().getString(R.string.device_productname_M50W));
            this.deviceimags.setImageResource(R.drawable.drawable_data_device_abpm50);
        } else if (DeviceNameUtils.PM50.equalsIgnoreCase(_device)) {
            this.name.setText(this.context.getResources().getString(R.string.device_productname_pm50));
            this.deviceimags.setImageResource(R.drawable.drawable_data_device_pm50);
        } else if (DeviceNameUtils.TEMP03.equalsIgnoreCase(_device)) {
            this.name.setText(this.context.getResources().getString(R.string.device_productname_temp03));
            this.deviceimags.setImageResource(R.drawable.drawable_data_device_temp03);
        } else if ("CONTEC08AW".equalsIgnoreCase(_device)) {
            this.name.setText(this.context.getResources().getString(R.string.device_productname_08AW));
            this.deviceimags.setImageResource(R.drawable.drawable_data_device_a08);
        } else if ("CONTEC08C".equalsIgnoreCase(_device)) {
            this.name.setText(this.context.getResources().getString(R.string.device_productname_08AW));
            this.deviceimags.setImageResource(R.drawable.drawable_device_c08);
        } else if ("CMS50F".equalsIgnoreCase(_device)) {
            this.name.setText(this.context.getResources().getString(R.string.device_name_50IW));
            this.deviceimags.setImageResource(R.drawable.drawable_device_cms50f);
        } else if (Constants.CMS50EW.equalsIgnoreCase(_device) || "CMS50DW".equalsIgnoreCase(_device)) {
            this.name.setText(this.context.getResources().getString(R.string.device_productname_50EW));
            this.deviceimags.setImageResource(R.drawable.drawable_data_device_cms50ew);
        } else if ("sp10w".equalsIgnoreCase(_device)) {
            this.name.setText(this.context.getResources().getString(R.string.device_productname_SP10W));
            this.deviceimags.setImageResource(R.drawable.drawable_data_device_sp10w);
        } else if ("cmsvesd".equalsIgnoreCase(_device)) {
            this.name.setText(this.context.getResources().getString(R.string.device_productname_VESD));
            this.deviceimags.setImageResource(R.drawable.drawable_device_cmsvesd);
        } else if ("wt".equalsIgnoreCase(_device)) {
            this.name.setText(this.context.getResources().getString(R.string.device_productname_WT));
            this.deviceimags.setImageResource(R.drawable.drawable_data_device_wt);
        } else if ("FHR01".equalsIgnoreCase(_device)) {
            this.name.setText(this.context.getResources().getString(R.string.device_productname_Fhr01));
            this.deviceimags.setImageResource(R.drawable.drawable_data_device_fhr01);
        } else if (Constants.PM85_NAME.equalsIgnoreCase(_device)) {
            this.name.setText(this.context.getResources().getString(R.string.device_productname_Pm85));
            this.deviceimags.setImageResource(R.drawable.drawable_data_device_pm85);
        } else if (Constants.CMS50K_NAME.equalsIgnoreCase(_device)) {
            this.name.setText(this.context.getResources().getString(R.string.device_productname_cms50k));
            this.deviceimags.setImageResource(R.drawable.cms50k);
        } else if (Constants.CMS50K1_NAME.equalsIgnoreCase(_device)) {
            this.name.setText(this.context.getResources().getString(R.string.device_productname_cms50k));
            this.deviceimags.setImageResource(R.drawable.cms50k1);
        }
    }

    public Map<Integer, Integer> getIsVisibility() {
        return this.isVisibility;
    }

    public void clear() {
        if (this.mNewlistBean != null) {
            DeviceManager.mDeviceList.removeAll();
            this.mNewlistBean.clear();
        }
    }
}
