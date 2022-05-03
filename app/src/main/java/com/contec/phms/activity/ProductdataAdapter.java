package com.contec.phms.activity;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.conect.json.CLog;
import com.contec.phms.R;
import com.contec.phms.upload.cases.spir.Product;
import com.contec.phms.util.Constants;
import com.contec.phms.util.DeviceNameUtils;
import com.contec.phms.util.ScreenAdapter;
import java.util.ArrayList;

public class ProductdataAdapter extends BaseAdapter {
    Holder _Holder;
    int layoutHeight = 139;
    private Context mContext;
    private int mLastPosition0Height = 0;
    private ArrayList<Product> mProductList;
    private final int mnoclicktextcolor = Color.rgb(74, 74, 74);
    ViewTreeObserver vto;

    public ProductdataAdapter(Context pContext, ArrayList<Product> ProductList) {
        this.mContext = pContext;
        this.mProductList = ProductList;
        int _occupydistance = Constants.dipTopx(pContext, 74.0f);
        this.layoutHeight = (Constants.M_SCREENWEIGH - _occupydistance) / 3;
        if (Constants.IS_PAD_NEW) {
            _occupydistance = Constants.dipTopx(pContext, 130.0f);
            this.layoutHeight = (Constants.M_SCREENWEIGH - _occupydistance) / 3;
        }
        CLog.i("lz", "ProductdataAdapter  layoutHeight = " + this.layoutHeight + "  " + Constants.M_SCREENWEIGH + "  " + _occupydistance);
    }

    public int getCount() {
        return this.mProductList.size();
    }

    public Object getItem(int arg0) {
        return Integer.valueOf(arg0);
    }

    public long getItemId(int arg0) {
        return (long) arg0;
    }

    private class Holder {
        LinearLayout mlaout_productdataa_adapter_main;
        ImageView mproduct_image;
        TextView mproduct_name;
        TextView mproduct_type;
        LinearLayout mproductimage_layout;

        private Holder() {
        }

        /* synthetic */ Holder(ProductdataAdapter productdataAdapter, Holder holder) {
            this();
        }
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        int _currentPosition0Height;
        if (convertView == null) {
            this._Holder = new Holder(this, (Holder) null);
            convertView = LayoutInflater.from(this.mContext).inflate(R.layout.layout_product_dataitem, (ViewGroup) null);
            this._Holder.mproductimage_layout = (LinearLayout) convertView.findViewById(R.id.productimage_layout);
            this._Holder.mproduct_image = (ImageView) convertView.findViewById(R.id.product_image);
            this._Holder.mproduct_name = (TextView) convertView.findViewById(R.id.product_name);
            this._Holder.mproduct_type = (TextView) convertView.findViewById(R.id.product_type);
            this._Holder.mlaout_productdataa_adapter_main = (LinearLayout) convertView.findViewById(R.id.laout_productdataa_adapter_main);
            if (Constants.IS_PAD_NEW) {
                ScreenAdapter.changeLayoutTextSize(this.mContext, (LinearLayout) convertView.findViewById(R.id.laout_productdataa_adapter_main), 10);
            }
            convertView.setTag(this._Holder);
        } else {
            this._Holder = (Holder) convertView.getTag();
        }
        if (Constants.IS_PAD_NEW) {
            this._Holder.mproductimage_layout.getLayoutParams().width = -1;
            this._Holder.mproduct_image.getLayoutParams().height = -1;
            this._Holder.mproduct_image.getLayoutParams().width = -1;
            int paddingValue = ScreenAdapter.dip2px(this.mContext, 18.0f);
            this._Holder.mproduct_image.setPadding(paddingValue, paddingValue, paddingValue, paddingValue);
        }
        measureView(convertView);
        if (position == 0 && this.mLastPosition0Height < (_currentPosition0Height = convertView.getMeasuredHeight())) {
            this.mLastPosition0Height = _currentPosition0Height;
        }
        if (position != 0) {
            AbsListView.LayoutParams params = new AbsListView.LayoutParams(-2, -2);
            params.height = this.mLastPosition0Height;
            this._Holder.mlaout_productdataa_adapter_main.setLayoutParams(params);
        }
        this._Holder.mproduct_image.setImageResource(this.mProductList.get(position).getProduct_image());
        matchDevice(this.mProductList.get(position).getProduct_type());
        this._Holder.mproductimage_layout.getLayoutParams().height = this.layoutHeight;
        return convertView;
    }

    private void measureView(View child) {
        int childMeasureHeight;
        ViewGroup.LayoutParams lp = child.getLayoutParams();
        if (lp == null) {
            lp = new ViewGroup.LayoutParams(-1, -2);
        }
        int childMeasureWidth = ViewGroup.getChildMeasureSpec(0, 0, lp.width);
        if (lp.height > 0) {
            childMeasureHeight = View.MeasureSpec.makeMeasureSpec(lp.height, 1073741824);
        } else {
            childMeasureHeight = View.MeasureSpec.makeMeasureSpec(0, 0);
        }
        child.measure(childMeasureWidth, childMeasureHeight);
    }

    private void matchDevice(String mproduct_type) {
        if (Constants.CMS50EW.equalsIgnoreCase(mproduct_type)) {
            this._Holder.mproduct_name.setText(this.mContext.getResources().getString(R.string.device_productname_50EW));
        } else if ("CMS50IW".equalsIgnoreCase(mproduct_type)) {
            this._Holder.mproduct_name.setText(this.mContext.getResources().getString(R.string.device_productname_50IW));
        } else if ("SP10W".equalsIgnoreCase(mproduct_type)) {
            this._Holder.mproduct_name.setText(this.mContext.getResources().getString(R.string.device_productname_SP10W));
        } else if ("SXT".equalsIgnoreCase(mproduct_type)) {
            this._Holder.mproduct_name.setText(this.mContext.getResources().getString(R.string.device_productname_SXT));
        } else if ("ABPM50".equalsIgnoreCase(mproduct_type)) {
            this._Holder.mproduct_name.setText(this.mContext.getResources().getString(R.string.device_productname_M50W));
        } else if ("CONTEC08A".equalsIgnoreCase(mproduct_type)) {
            this._Holder.mproduct_name.setText(this.mContext.getResources().getString(R.string.device_productname_08AW));
        } else if ("WT100".equalsIgnoreCase(mproduct_type)) {
            this._Holder.mproduct_name.setText(this.mContext.getResources().getString(R.string.device_productname_WT));
        } else if ("Sonoline-S".equalsIgnoreCase(mproduct_type)) {
            this._Holder.mproduct_name.setText(this.mContext.getResources().getString(R.string.device_productname_Fhr01));
        } else if (Constants.PM85_NAME.equalsIgnoreCase(mproduct_type)) {
            this._Holder.mproduct_name.setText(this.mContext.getResources().getString(R.string.device_productname_Pm85));
        } else if (Constants.PM10_NAME.equalsIgnoreCase(mproduct_type)) {
            this._Holder.mproduct_name.setText(this.mContext.getResources().getString(R.string.device_productname_Pm10));
        } else if ("Bc401".equalsIgnoreCase(mproduct_type)) {
            this._Holder.mproduct_name.setText(this.mContext.getResources().getString(R.string.device_productname_bc401));
        } else if ("Eet_1".equalsIgnoreCase(mproduct_type)) {
            this._Holder.mproduct_name.setText(this.mContext.getResources().getString(R.string.device_productname_eet_1));
        } else if (DeviceNameUtils.PM50.equalsIgnoreCase(mproduct_type)) {
            this._Holder.mproduct_name.setText(this.mContext.getResources().getString(R.string.device_productname_pm50));
        } else if (DeviceNameUtils.TEMP03.equalsIgnoreCase(mproduct_type)) {
            this._Holder.mproduct_name.setText(this.mContext.getResources().getString(R.string.device_productname_temp03));
        }
    }
}
