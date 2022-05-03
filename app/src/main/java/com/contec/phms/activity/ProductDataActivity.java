package com.contec.phms.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import com.contec.phms.R;
import com.contec.phms.login.ActivityBase;
import com.contec.phms.util.CheckUpdateProduct;
import com.contec.phms.util.Constants;
//import com.umeng.analytics.MobclickAgent;
import java.io.File;

public class ProductDataActivity extends ActivityBase implements View.OnClickListener {
    public static String[] htmls = {"CMS50D-BT_EN.html", "CMS50D-BT_CH.html", "CMS50K_EN.html", "CMS50K_CH.html", "SP10BT_EN.html", "SP10BT_CH.html", "Bloodglucosemeter_EN.html", "Bloodglucosemeter_CH.html", "ABPM50BT_EN.html", "ABPM50BT_CH.html", "CONTEC08C-BT_EN.html", "CONTEC08C-BT_CH.html", "WT100BT_EN.html", "WT100BT_CH.html", "Fhr_EN.html", "Fhr_CH.html", "BC401BT_EN.html", "BC401BT_CH.html", "EET-1(BT)_EN.html", "EET-1(BT)_CH.html", "PM10_EN.html", "PM10_CH.html", "PM50BT_CH.html", "PM50BT_EN.html", "TEMP03BT_CH.html", "TEMP03BT_EN.html"};
    public static String[] imgs = {"ABPM50BT.png", "BC401BT.png", "Bloodglucosemeter.png", "CMS50D-BT.png", "CMS50K.png", "CONTEC08C-BT.png", "EET-1(BT).png", "Fhr.png", "PM10.png", "SP10BT.png", "WT100BT.png", "PM50BT.png", "TEMP03BT.png"};
    private Button btn_back;
    private Bundle bundle;
    private String deviceType;
    private int imageId = -1;
    private ImageView img_noData;
    private String languageType;
    private WebView pWebView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_activity_productdata);
        initView();
        initViewsOper();
        initClic();
    }

    private void initView() {
        this.pWebView = (WebView) findViewById(R.id.wb_product_details);
        this.btn_back = (Button) findViewById(R.id.back_but);
        this.img_noData = (ImageView) findViewById(R.id.img_product_nodata);
    }

    @SuppressLint({"SdCardPath"})
    private void initViewsOper() {
        String deviceType2;
        String url;
        String deviceType3 = getResult();
        if (!new File(String.valueOf(CheckUpdateProduct.LOCAL_HTMLPATH) + "base.css").exists()) {
            CheckUpdateProduct.downloadProductDetail("http://www.contec365.com/productapp/product/base.css", "base.css", this);
        }
        if (!new File(String.valueOf(CheckUpdateProduct.LOCAL_HTMLPATH) + "images/" + deviceType3 + ".png").exists()) {
            CheckUpdateProduct.downloadProductDetail("http://www.contec365.com/productapp/product/images/" + deviceType3 + ".png", String.valueOf(deviceType3) + ".png", this);
        }
        this.languageType = Constants.Language;
        if (this.languageType.equals("en")) {
            deviceType2 = String.valueOf(deviceType3) + "_EN.html";
            this.imageId = R.drawable.no_data_en;
        } else {
            deviceType2 = String.valueOf(deviceType3) + "_CH.html";
            this.imageId = R.drawable.no_data_ch;
        }
        this.pWebView.setHorizontalScrollBarEnabled(false);
        this.pWebView.setVerticalScrollBarEnabled(false);
        this.pWebView.setOnLongClickListener(new View.OnLongClickListener() {
            public boolean onLongClick(View v) {
                return true;
            }
        });
        if (!new File(String.valueOf(CheckUpdateProduct.LOCAL_HTMLPATH) + deviceType2).exists()) {
            url = "http://www.contec365.com/productapp/product/" + deviceType2;
            CheckUpdateProduct.downloadProductDetail("http://www.contec365.com/productapp/product/" + deviceType2, deviceType2, this);
            if (!CheckUpdateProduct.isNet(this)) {
                this.img_noData.setImageResource(this.imageId);
                this.img_noData.setVisibility(View.VISIBLE);
                this.pWebView.setVisibility(View.GONE);
                return;
            }
        } else {
            url = "file:///sdcard/contec/html/" + deviceType2;
        }
        this.pWebView.loadUrl(url);
    }

    private void initClic() {
        this.btn_back.setOnClickListener(this);
    }

    private String getResult() {
        this.bundle = getIntent().getExtras();
        this.deviceType = this.bundle.getString("device_type");
        if (this.deviceType.equals(Constants.CMS50EW)) {
            return "CMS50D-BT";
        }
        if (this.deviceType.equals("CMS50IW")) {
            return Constants.CMS50K_NAME;
        }
        if (this.deviceType.equals("SP10W")) {
            return "SP10BT";
        }
        if (this.deviceType.equals("SXT")) {
            return "Bloodglucosemeter";
        }
        if (this.deviceType.equals("ABPM50")) {
            return "ABPM50BT";
        }
        if (this.deviceType.equals("CONTEC08A")) {
            return "CONTEC08C-BT";
        }
        if (this.deviceType.equals("WT100")) {
            return "WT100BT";
        }
        if (this.deviceType.equals("Sonoline-S")) {
            return "Fhr";
        }
        if (this.deviceType.equals("Bc401")) {
            return "BC401BT";
        }
        if (this.deviceType.equals("eet_1")) {
            return "EET-1(BT)";
        }
        if (this.deviceType.equals("Pm10")) {
            return Constants.PM10_NAME;
        }
        if (this.deviceType.equals("PM50")) {
            return "PM50BT";
        }
        if (this.deviceType.equals("TEMP03")) {
            return "TEMP03BT";
        }
        return null;
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_but:
                finish();
                return;
            default:
                return;
        }
    }

    protected void onResume() {
        super.onResume();
        //MobclickAgent.onResume(this);
    }

    protected void onPause() {
        super.onPause();
        //MobclickAgent.onPause(this);
    }
}
