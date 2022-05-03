package com.contec.phms.fragment;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.contec.phms.R;
import com.contec.phms.activity.ProductDataActivity;
import com.contec.phms.activity.ProductdataAdapter;
import com.contec.phms.upload.cases.spir.Product;
import com.contec.phms.util.Constants;
import com.contec.phms.util.ScreenAdapter;
import java.util.ArrayList;

public class FragmentProductData extends FragmentBase {
    private ProductdataAdapter mProductdataAdapter;
    private View mView;
    private TextView mtitle_text;
    private GridView product_data_grid;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.mView = inflater.inflate(R.layout.layout_product_data, container, false);
        init_view(this.mView);
        if (Constants.IS_PAD_NEW) {
            this.product_data_grid.setPadding(30, 30, 30, 0);
            this.product_data_grid.setHorizontalSpacing(35);
            ScreenAdapter.titleLayoutToPad(getActivity(), (RelativeLayout) this.mView.findViewById(R.id.linearlayout_title));
            this.mtitle_text.setTextSize(0, 35.0f);
        }
        return this.mView;
    }

    public void onResume() {
        super.onResume();
        this.mtitle_text.setText(R.string.product_descrip_text);
        this.product_data_grid.setAdapter(this.mProductdataAdapter);
        this.mProductdataAdapter.notifyDataSetChanged();
        init_view(this.mView);
    }

    public void onStop() {
        super.onStop();
    }

    private void init_view(View pView) {
        LinearLayout mlayout_productdata_main = (LinearLayout) pView.findViewById(R.id.layout_productdata_main);
        this.mtitle_text = (TextView) pView.findViewById(R.id.title_text);
        this.product_data_grid = (GridView) pView.findViewById(R.id.product_data_grid);
        this.product_data_grid.setSelector(new ColorDrawable(0));
        final ArrayList<Product> ProductArray = new ArrayList<>();
        ProductArray.add(new Product(R.drawable.drawable_device_cms50d, getString(R.string.device_productname_50EW), getString(R.string.device_type_50EW)));
        ProductArray.add(new Product(R.drawable.drawable_device_cms50iw, getString(R.string.device_productname_cms50k), getString(R.string.device_type_50IW)));
        ProductArray.add(new Product(R.drawable.drawable_device_sp10w, getString(R.string.device_productname_SP10W), getString(R.string.device_type_SP10W)));
        ProductArray.add(new Product(R.drawable.drawable_device_cmxxst, getString(R.string.device_productname_SXT), getString(R.string.device_type_SXT)));
        ProductArray.add(new Product(R.drawable.drawable_device_abpm50, getString(R.string.device_productname_M50W), getString(R.string.device_type_M50W)));
        ProductArray.add(new Product(R.drawable.drawable_device_a08, getString(R.string.device_productname_08AW), getString(R.string.device_type_08AW)));
        ProductArray.add(new Product(R.drawable.drawable_device_wt, getString(R.string.device_productname_WT), getString(R.string.device_type_WT)));
        ProductArray.add(new Product(R.drawable.drawable_device_fhr01, getString(R.string.device_productname_Fhr01), getString(R.string.device_type_Fhr01)));
        ProductArray.add(new Product(R.drawable.drawable_device_bc401, getString(R.string.device_productname_bc401), getString(R.string.device_type_Bc401)));
        ProductArray.add(new Product(R.drawable.drawable_device_eet_1, getString(R.string.device_productname_eet_1), getString(R.string.device_type_Eet_1)));
        ProductArray.add(new Product(R.drawable.drawable_device_pm10, getString(R.string.device_productname_Pm10), getString(R.string.device_type_Pm10)));
        ProductArray.add(new Product(R.drawable.drawable_data_device_pm50, getString(R.string.device_productname_pm50), getString(R.string.device_type_Pm50)));
        ProductArray.add(new Product(R.drawable.drawable_data_device_temp03, getString(R.string.device_productname_temp03), getString(R.string.device_type_Temp03)));
        this.mProductdataAdapter = new ProductdataAdapter(getActivity(), ProductArray);
        this.product_data_grid.setAdapter(this.mProductdataAdapter);
        this.product_data_grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View arg1, int arg2, long arg3) {
                Product product = (Product) ProductArray.get(arg2);
                int device_image = product.getProduct_image();
                String device_name = product.getProduct_name();
                String device_type = product.getProduct_type();
                Bundle _bundle = new Bundle();
                _bundle.putInt("device_image", device_image);
                _bundle.putString("device_name", device_name);
                _bundle.putString("device_type", device_type);
                new FragmentProductActivity();
                Intent _intent = new Intent(FragmentProductData.this.getActivity(), ProductDataActivity.class);
                _intent.putExtras(_bundle);
                FragmentProductData.this.startActivity(_intent);
            }
        });
        this.mProductdataAdapter.notifyDataSetChanged();
        if (Constants.IS_PAD_NEW) {
            ScreenAdapter.changeLayoutTextSize(getActivity(), mlayout_productdata_main, 10);
            this.mtitle_text.setTextSize(0, 35.0f);
        }
    }
}
