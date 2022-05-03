package com.contec.phms.fragment;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import com.alibaba.wireless.security.SecExceptionCode;
import com.contec.phms.App_phms;
import com.contec.phms.R;
import com.contec.phms.login.ActivityBase;
import com.contec.phms.util.BitMapUtil;
import com.contec.phms.util.Constants;
import com.contec.phms.util.ScreenAdapter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Locale;
import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.HttpHandler;
import de.greenrobot.event.EventBus;

public class FragmentProductActivity extends ActivityBase {
    private int _Bc401_en_native;
    private int _Bc401_native;
    private int _Bc401_net;
    private SharedPreferences.Editor _Editor;
    private int _Eet_1_en_native;
    private int _Eet_1_native;
    private int _Eet_1_net;
    private int _Pm10_en_native;
    private int _Pm10_native;
    private int _Pm10_net;
    private int _abpm50_en_native;
    private int _abpm50_native;
    private int _abpm50_net;
    private int _cms50ew_en_native;
    private int _cms50ew_native;
    private int _cms50ew_net;
    private int _cms50iw_en_native;
    private int _cms50iw_native;
    private int _cms50iw_net;
    private int _contec08a_en_native;
    private int _contec08a_native;
    private int _contec08a_net;
    private int _pm85_en_native;
    private int _pm85_native;
    private int _pm85_net;
    private int _sonoline_en_native;
    private int _sonoline_native;
    private int _sonoline_net;
    private int _sp10w_en_native;
    private int _sp10w_native;
    private int _sp10w_net;
    private int _sxt_en_native;
    private int _sxt_native;
    private int _sxt_net;
    private int _wt100_en_native;
    private int _wt100_native;
    private int _wt100_net;
    private String device_type;
    private HttpHandler handler;
    private Bitmap mBitmap;
    private Bundle mBundle;
    private SharedPreferences mCurrentloginUserInfo;
    private ImageView mDeviceDetailsImage;
    private ImageView mDeviceImage;
    private final String mEnBitmapPath = "http://data1.contec365.com/update/android/en/";
    private EventBus mEventBus = App_phms.getInstance().mEventBus;
    private final String mZhBitmapPath = "http://data1.contec365.com/update/android/zh/";
    private Button mback_but;
    private View mview;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mview = getLayoutInflater().inflate(R.layout.layout_product_details, (ViewGroup) null, false);
        setContentView(this.mview);
        this.mBundle = getIntent().getExtras();
        init_view(this.mview);
        LinearLayout mlayout_productdetails_main = (LinearLayout) this.mview.findViewById(R.id.layout_productdetails_main);
        if (Constants.IS_PAD_NEW) {
            ((ScrollView) this.mview.findViewById(R.id.scrollView)).setPadding(0, ScreenAdapter.dip2px(this, 55.0f), 0, ScreenAdapter.dip2px(this, 12.0f));
            ((LinearLayout.LayoutParams) ((LinearLayout) this.mview.findViewById(R.id.detail_top_layout)).getLayoutParams()).height = ScreenAdapter.dip2px(this, 220.0f);
            LinearLayout.LayoutParams lptopleft = (LinearLayout.LayoutParams) ((LinearLayout) this.mview.findViewById(R.id.detail_top_left_layout)).getLayoutParams();
            lptopleft.width = ScreenAdapter.dip2px(this, 220.0f);
            lptopleft.height = ScreenAdapter.dip2px(this, 220.0f);
            ScreenAdapter.titleLayoutToPad(this, (RelativeLayout) this.mview.findViewById(R.id.linearlayout_title), this.mback_but);
            this.mDeviceImage.getLayoutParams().height = -1;
            this.mDeviceImage.getLayoutParams().width = -1;
            int paddingValue = ScreenAdapter.dip2px(this, 18.0f);
            this.mDeviceImage.setPadding(paddingValue, paddingValue, paddingValue, paddingValue);
            ScreenAdapter.changeLayoutTextSize(this, mlayout_productdetails_main, 10);
        }
    }

    private void init_view(View pview) {
        this.mCurrentloginUserInfo = getSharedPreferences("CurrentloginUserInfo", 0);
        this._Editor = this.mCurrentloginUserInfo.edit();
        this._cms50ew_native = this.mCurrentloginUserInfo.getInt(Constants.CMS50EW, 1);
        this._cms50ew_en_native = this.mCurrentloginUserInfo.getInt("CMS50EW_EN", 1);
        this._cms50iw_native = this.mCurrentloginUserInfo.getInt("CMS50IW", 1);
        this._cms50iw_en_native = this.mCurrentloginUserInfo.getInt("CMS50IW_EN", 1);
        this._sp10w_native = this.mCurrentloginUserInfo.getInt("SP10W", 1);
        this._sp10w_en_native = this.mCurrentloginUserInfo.getInt("SP10W_EN", 1);
        this._sxt_native = this.mCurrentloginUserInfo.getInt("SXT", 1);
        this._sxt_en_native = this.mCurrentloginUserInfo.getInt("SXT_EN", 1);
        this._abpm50_native = this.mCurrentloginUserInfo.getInt("ABPM50", 1);
        this._abpm50_en_native = this.mCurrentloginUserInfo.getInt("ABPM50_EN", 1);
        this._contec08a_native = this.mCurrentloginUserInfo.getInt("CONTEC08A", 1);
        this._contec08a_en_native = this.mCurrentloginUserInfo.getInt("CONTEC08A_EN", 1);
        this._wt100_native = this.mCurrentloginUserInfo.getInt("WT100", 1);
        this._wt100_en_native = this.mCurrentloginUserInfo.getInt("WT100_EN", 1);
        this._sonoline_native = this.mCurrentloginUserInfo.getInt("Sonoline-S", 1);
        this._sonoline_en_native = this.mCurrentloginUserInfo.getInt("Sonoline-S_EN", 1);
        this._pm85_native = this.mCurrentloginUserInfo.getInt(Constants.PM85_NAME, 1);
        this._pm85_en_native = this.mCurrentloginUserInfo.getInt("PM85_EN", 1);
        this._cms50ew_net = this.mCurrentloginUserInfo.getInt("CMS50EW_net", 1);
        this._cms50iw_net = this.mCurrentloginUserInfo.getInt("CMS50IW_net", 1);
        this._sp10w_net = this.mCurrentloginUserInfo.getInt("SP10W_net", 1);
        this._sxt_net = this.mCurrentloginUserInfo.getInt("SXT_net", 1);
        this._abpm50_net = this.mCurrentloginUserInfo.getInt("ABPM50_net", 1);
        this._contec08a_net = this.mCurrentloginUserInfo.getInt("CONTEC08A_net", 1);
        this._wt100_net = this.mCurrentloginUserInfo.getInt("WT100_net", 1);
        this._sonoline_net = this.mCurrentloginUserInfo.getInt("Sonoline-S_net", 1);
        this._pm85_net = this.mCurrentloginUserInfo.getInt("PM85_net", 1);
        int device_image = this.mBundle.getInt("device_image");
        String device_name = this.mBundle.getString("device_name");
        this.device_type = this.mBundle.getString("device_type");
        this.mDeviceImage = (ImageView) pview.findViewById(R.id.device_image);
        TextView mDeviceNameText = (TextView) pview.findViewById(R.id.device_name);
        TextView mDeviceTypeText = (TextView) pview.findViewById(R.id.device_type);
        this.mDeviceDetailsImage = (ImageView) pview.findViewById(R.id.device_details);
        String _language = Locale.getDefault().getLanguage();
        if (Constants.Language.equalsIgnoreCase("zh")) {
            product_details_zh();
        } else if (Constants.Language.equalsIgnoreCase("en")) {
            product_details_en();
        } else if (_language.equals("zh")) {
            product_details_zh();
        } else if (_language.equals("en")) {
            product_details_en();
        }
        this.mDeviceImage.setImageResource(device_image);
        mDeviceNameText.setText(device_name);
        mDeviceTypeText.setText(this.device_type);
        this.mback_but = (Button) pview.findViewById(R.id.back_but);
        this.mback_but.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                FragmentProductActivity.this.finish();
            }
        });
    }

    private void product_details_en() {
        if (this.device_type.equalsIgnoreCase(Constants.CMS50EW)) {
            if (this._cms50ew_en_native < this._cms50ew_net) {
                ProductDetailsDownload("http://data1.contec365.com/update/android/en/drawable_product_details_cms50ew_en.png", "drawable_product_details_cms50ew_en.png");
            } else {
                _load_native_Bitmap(String.valueOf(Constants.ALBUM_PATH) + "native/drawable_product_details_cms50ew_en.png");
            }
        } else if (this.device_type.equalsIgnoreCase("CMS50IW")) {
            if (this._cms50iw_en_native < this._cms50iw_net) {
                ProductDetailsDownload("http://data1.contec365.com/update/android/en/drawable_product_details_cms50iw_en.png", "drawable_product_details_cms50iw_en.png");
            } else {
                _load_native_Bitmap(String.valueOf(Constants.ALBUM_PATH) + "native/drawable_product_details_cms50iw_en.png");
            }
        } else if (this.device_type.equalsIgnoreCase("SP10W")) {
            if (this._sp10w_en_native < this._sp10w_net) {
                ProductDetailsDownload("http://data1.contec365.com/update/android/en/drawable_product_details_sp10w_en.png", "drawable_product_details_sp10w_en.png");
            } else {
                _load_native_Bitmap(String.valueOf(Constants.ALBUM_PATH) + "native/drawable_product_details_sp10w_en.png");
            }
        } else if (this.device_type.equalsIgnoreCase("SXT")) {
            if (this._sxt_en_native < this._sxt_net) {
                ProductDetailsDownload("http://data1.contec365.com/update/android/en/drawable_product_details_sxt_en.png", "drawable_product_details_sxt_en.png");
            } else {
                _load_native_Bitmap(String.valueOf(Constants.ALBUM_PATH) + "native/drawable_product_details_sxt_en.png");
            }
        } else if (this.device_type.equalsIgnoreCase("ABPM50")) {
            if (this._abpm50_en_native < this._abpm50_net) {
                ProductDetailsDownload("http://data1.contec365.com/update/android/en/drawable_product_details_abpm50_en.png", "drawable_product_details_abpm50_en.png");
            } else {
                _load_native_Bitmap(String.valueOf(Constants.ALBUM_PATH) + "native/drawable_product_details_abpm50_en.png");
            }
        } else if (this.device_type.equalsIgnoreCase("CONTEC08A")) {
            if (this._contec08a_en_native < this._contec08a_net) {
                ProductDetailsDownload("http://data1.contec365.com/update/android/en/drawable_product_details_contec08a_en.png", "drawable_product_details_contec08a_en.png");
            } else {
                _load_native_Bitmap(String.valueOf(Constants.ALBUM_PATH) + "native/drawable_product_details_contec08a_en.png");
            }
        } else if (this.device_type.equalsIgnoreCase("WT100")) {
            if (this._wt100_en_native < this._wt100_net) {
                ProductDetailsDownload("http://data1.contec365.com/update/android/en/drawable_product_details_wt100_en.png", "drawable_product_details_wt100_en.png");
            } else {
                _load_native_Bitmap(String.valueOf(Constants.ALBUM_PATH) + "native/drawable_product_details_wt100_en.png");
            }
        } else if (this.device_type.equalsIgnoreCase("Sonoline-S")) {
            if (this._sonoline_en_native < this._sonoline_net) {
                ProductDetailsDownload("http://data1.contec365.com/update/android/en/drawable_product_details_fhr01_en.png", "drawable_product_details_fhr01_en.png");
            } else {
                _load_native_Bitmap(String.valueOf(Constants.ALBUM_PATH) + "native/drawable_product_details_fhr01_en.png");
            }
        } else if (this.device_type.equalsIgnoreCase(Constants.PM85_NAME)) {
            if (this._pm85_en_native < this._pm85_net) {
                ProductDetailsDownload("http://data1.contec365.com/update/android/en/drawable_product_details_pm85_en.png", "drawable_product_details_pm85_en.png");
            } else {
                _load_native_Bitmap(String.valueOf(Constants.ALBUM_PATH) + "native/drawable_product_details_pm85_en.png");
            }
        } else if (this.device_type.equalsIgnoreCase("Bc401")) {
            if (this._Bc401_en_native < this._Bc401_net) {
                ProductDetailsDownload("http://data1.contec365.com/update/android/zh/drawable_product_details_bc401_en.png", "drawable_product_details_bc401_en.png");
            } else {
                _load_native_Bitmap(String.valueOf(Constants.ALBUM_PATH) + "native/drawable_product_details_bc401_en.png", R.drawable.drawable_product_details_bc401_en);
            }
        } else if (this.device_type.equalsIgnoreCase("Eet_1")) {
            if (this._Eet_1_en_native < this._Eet_1_net) {
                ProductDetailsDownload("http://data1.contec365.com/update/android/zh/drawable_product_details_eet_1_en.png", "drawable_product_details_eet_1_en.png");
            } else {
                _load_native_Bitmap(String.valueOf(Constants.ALBUM_PATH) + "native/drawable_product_details_eet_1_en.png", R.drawable.drawable_product_details_eet_1_en);
            }
        } else if (!this.device_type.equalsIgnoreCase("Pm10")) {
        } else {
            if (this._Pm10_en_native < this._Pm10_net) {
                ProductDetailsDownload("http://data1.contec365.com/update/android/zh/drawable_product_details_pm10_en.png", "drawable_product_details_pm10_en.png");
            } else {
                _load_native_Bitmap(String.valueOf(Constants.ALBUM_PATH) + "native/drawable_product_details_pm10_en.png", R.drawable.drawable_product_details_pm10_en);
            }
        }
    }

    private void product_details_zh() {
        if (this.device_type.equalsIgnoreCase(Constants.CMS50EW)) {
            if (this._cms50ew_native < this._cms50ew_net) {
                ProductDetailsDownload("http://data1.contec365.com/update/android/zh/drawable_product_details_cms50ew.png", "drawable_product_details_cms50ew.png");
            } else {
                _load_native_Bitmap(String.valueOf(Constants.ALBUM_PATH) + "native/drawable_product_details_cms50ew.png");
            }
        } else if (this.device_type.equalsIgnoreCase("CMS50IW")) {
            if (this._cms50iw_native < this._cms50iw_net) {
                ProductDetailsDownload("http://data1.contec365.com/update/android/zh/drawable_product_details_cms50iw.png", "drawable_product_details_cms50iw.png");
            } else {
                _load_native_Bitmap(String.valueOf(Constants.ALBUM_PATH) + "native/drawable_product_details_cms50iw.png");
            }
        } else if (this.device_type.equalsIgnoreCase("SP10W")) {
            if (this._sp10w_native < this._sp10w_net) {
                ProductDetailsDownload("http://data1.contec365.com/update/android/zh/drawable_product_details_sp10w.png", "drawable_product_details_sp10w.png");
            } else {
                _load_native_Bitmap(String.valueOf(Constants.ALBUM_PATH) + "native/drawable_product_details_sp10w.png");
            }
        } else if (this.device_type.equalsIgnoreCase("SXT")) {
            if (this._sxt_native < this._sxt_net) {
                ProductDetailsDownload("http://data1.contec365.com/update/android/zh/drawable_product_details_sxt.png", "drawable_product_details_sxt.png");
            } else {
                _load_native_Bitmap(String.valueOf(Constants.ALBUM_PATH) + "native/drawable_product_details_sxt.png");
            }
        } else if (this.device_type.equalsIgnoreCase("ABPM50")) {
            if (this._abpm50_native < this._abpm50_net) {
                ProductDetailsDownload("http://data1.contec365.com/update/android/zh/drawable_product_details_abpm50.png", "drawable_product_details_abpm50.png");
            } else {
                _load_native_Bitmap(String.valueOf(Constants.ALBUM_PATH) + "native/drawable_product_details_abpm50.png");
            }
        } else if (this.device_type.equalsIgnoreCase("CONTEC08A")) {
            if (this._contec08a_native < this._contec08a_net) {
                ProductDetailsDownload("http://data1.contec365.com/update/android/zh/drawable_product_details_contec08a.png", "drawable_product_details_contec08a.png");
            } else {
                _load_native_Bitmap(String.valueOf(Constants.ALBUM_PATH) + "native/drawable_product_details_contec08a.png");
            }
        } else if (this.device_type.equalsIgnoreCase("WT100")) {
            if (this._wt100_native < this._wt100_net) {
                ProductDetailsDownload("http://data1.contec365.com/update/android/zh/drawable_product_details_wt100.png", "drawable_product_details_wt100.png");
            } else {
                _load_native_Bitmap(String.valueOf(Constants.ALBUM_PATH) + "native/drawable_product_details_wt100.png");
            }
        } else if (this.device_type.equalsIgnoreCase("Sonoline-S")) {
            if (this._sonoline_native < this._sonoline_net) {
                ProductDetailsDownload("http://data1.contec365.com/update/android/zh/drawable_product_details_fhr01.png", "drawable_product_details_fhr01.png");
            } else {
                _load_native_Bitmap(String.valueOf(Constants.ALBUM_PATH) + "native/drawable_product_details_fhr01.png");
            }
        } else if (this.device_type.equalsIgnoreCase(Constants.PM85_NAME)) {
            if (this._pm85_native < this._pm85_net) {
                ProductDetailsDownload("http://data1.contec365.com/update/android/zh/drawable_product_details_pm85.png", "drawable_product_details_pm85.png");
            } else {
                _load_native_Bitmap(String.valueOf(Constants.ALBUM_PATH) + "native/drawable_product_details_pm85.png");
            }
        } else if (this.device_type.equalsIgnoreCase("Bc401")) {
            if (this._Bc401_native < this._Bc401_net) {
                ProductDetailsDownload("http://data1.contec365.com/update/android/zh/drawable_product_details_bc401.png", "drawable_product_details_bc401.png");
            } else {
                _load_native_Bitmap(String.valueOf(Constants.ALBUM_PATH) + "native/drawable_product_details_bc401.png", R.drawable.drawable_product_details_bc401);
            }
        } else if (this.device_type.equalsIgnoreCase("Eet_1")) {
            if (this._Eet_1_native < this._Eet_1_net) {
                ProductDetailsDownload("http://data1.contec365.com/update/android/zh/drawable_product_details_eet_1.png", "drawable_product_details_eet_1.png");
            } else {
                _load_native_Bitmap(String.valueOf(Constants.ALBUM_PATH) + "native/drawable_product_details_eet_1.png", R.drawable.drawable_product_details_eet_1);
            }
        } else if (!this.device_type.equalsIgnoreCase("Pm10")) {
        } else {
            if (this._Pm10_native < this._Pm10_net) {
                ProductDetailsDownload("http://data1.contec365.com/update/android/zh/drawable_product_details_pm10.png", "drawable_product_details_pm10.png");
            } else {
                _load_native_Bitmap(String.valueOf(Constants.ALBUM_PATH) + "native/drawable_product_details_pm10.png", R.drawable.drawable_product_details_pm10);
            }
        }
    }

    private void _load_native_Bitmap(String pBitmapPath) {
        this.mBitmap = BitMapUtil.getBitmap(pBitmapPath, 400, SecExceptionCode.SEC_ERROR_PKG_VALID);
        this.mDeviceDetailsImage.setImageBitmap(this.mBitmap);
    }

    private void _load_native_Bitmap(String pBitmapPath, int drawableId) {
        this.mBitmap = BitMapUtil.getBitmap(pBitmapPath, 400, SecExceptionCode.SEC_ERROR_PKG_VALID);
        if (this.mBitmap != null) {
            this.mDeviceDetailsImage.setImageBitmap(this.mBitmap);
            return;
        }
        this.mBitmap = getDefaultProductDetails(drawableId);
        this.mDeviceDetailsImage.setImageBitmap(this.mBitmap);
    }

    private Bitmap getDefaultProductDetails(int drawableId) {
        Bitmap decodeResource = BitmapFactory.decodeResource(getResources(), drawableId);
        this.mBitmap = decodeResource;
        return decodeResource;
    }

    private void _put_update_product(String pUpdateProduct, int pUpdateNetVersion) {
        this._Editor.putInt(pUpdateProduct, pUpdateNetVersion);
        this._Editor.commit();
    }

    private void ProductDetailsDownload(String pDownloadProductPath, final String pFileName) {
        File dirFile = new File(String.valueOf(Constants.ALBUM_PATH) + "download/");
        if (!dirFile.exists()) {
            dirFile.mkdirs();
        }
        this.handler = new FinalHttp().download(pDownloadProductPath, String.valueOf(Constants.ALBUM_PATH) + "download/" + pFileName, true, (AjaxCallBack<File>) new AjaxCallBack<File>() {
            public void onLoading(long count, long current) {
            }

            public void onSuccess(File t) {
                super.onSuccess(t);
                if (pFileName.equalsIgnoreCase("drawable_product_details_cms50ew.png")) {
                    FragmentProductActivity.this._put_update_product(Constants.CMS50EW, FragmentProductActivity.this._cms50ew_net);
                } else if (pFileName.equalsIgnoreCase("drawable_product_details_cms50ew_en.png")) {
                    FragmentProductActivity.this._put_update_product("CMS50EW_EN", FragmentProductActivity.this._cms50ew_net);
                } else if (pFileName.equalsIgnoreCase("drawable_product_details_cms50iw.png")) {
                    FragmentProductActivity.this._put_update_product("CMS50IW", FragmentProductActivity.this._cms50iw_net);
                } else if (pFileName.equalsIgnoreCase("drawable_product_details_cms50iw_en.png")) {
                    FragmentProductActivity.this._put_update_product("CMS50IW_EN", FragmentProductActivity.this._cms50iw_net);
                } else if (pFileName.equalsIgnoreCase("drawable_product_details_sp10w.png")) {
                    FragmentProductActivity.this._put_update_product("SP10W", FragmentProductActivity.this._sp10w_net);
                } else if (pFileName.equalsIgnoreCase("drawable_product_details_sp10w_en.png")) {
                    FragmentProductActivity.this._put_update_product("SP10W_EN", FragmentProductActivity.this._sp10w_net);
                } else if (pFileName.equalsIgnoreCase("drawable_product_details_sxt.png")) {
                    FragmentProductActivity.this._put_update_product("SXT", FragmentProductActivity.this._sxt_net);
                } else if (pFileName.equalsIgnoreCase("drawable_product_details_sxt_en.png")) {
                    FragmentProductActivity.this._put_update_product("SXT_EN", FragmentProductActivity.this._sxt_net);
                } else if (pFileName.equalsIgnoreCase("drawable_product_details_abpm50.png")) {
                    FragmentProductActivity.this._put_update_product("ABPM50", FragmentProductActivity.this._abpm50_net);
                } else if (pFileName.equalsIgnoreCase("drawable_product_details_abpm50_en.png")) {
                    FragmentProductActivity.this._put_update_product("ABPM50_EN", FragmentProductActivity.this._abpm50_net);
                } else if (pFileName.equalsIgnoreCase("drawable_product_details_contec08a.png")) {
                    FragmentProductActivity.this._put_update_product("CONTEC08A", FragmentProductActivity.this._contec08a_net);
                } else if (pFileName.equalsIgnoreCase("drawable_product_details_contec08a_en.png")) {
                    FragmentProductActivity.this._put_update_product("CONTEC08A_EN", FragmentProductActivity.this._contec08a_net);
                } else if (pFileName.equalsIgnoreCase("drawable_product_details_wt100.png")) {
                    FragmentProductActivity.this._put_update_product("WT100", FragmentProductActivity.this._wt100_net);
                } else if (pFileName.equalsIgnoreCase("drawable_product_details_wt100_en.png")) {
                    FragmentProductActivity.this._put_update_product("WT100_EN", FragmentProductActivity.this._wt100_net);
                } else if (pFileName.equalsIgnoreCase("drawable_product_details_fhr01.png")) {
                    FragmentProductActivity.this._put_update_product("Sonoline-S", FragmentProductActivity.this._sonoline_net);
                } else if (pFileName.equalsIgnoreCase("drawable_product_details_fhr01_en.png")) {
                    FragmentProductActivity.this._put_update_product("Sonoline-S_EN", FragmentProductActivity.this._sonoline_net);
                } else if (pFileName.equalsIgnoreCase("drawable_product_details_pm85.png")) {
                    FragmentProductActivity.this._put_update_product(Constants.PM85_NAME, FragmentProductActivity.this._pm85_net);
                } else if (pFileName.equalsIgnoreCase("drawable_product_details_pm85_en.png")) {
                    FragmentProductActivity.this._put_update_product("PM85_EN", FragmentProductActivity.this._pm85_net);
                }
                FragmentProductActivity.this.copySDFile(String.valueOf(Constants.ALBUM_PATH) + "download/" + pFileName, String.valueOf(Constants.ALBUM_PATH) + "native/" + pFileName);
            }

            public void onFailure(Throwable t, String strMsg) {
                FragmentProductActivity.RecursionDeleteFile(new File(String.valueOf(Constants.ALBUM_PATH) + "download/" + pFileName));
                FragmentProductActivity.this.mBitmap = BitMapUtil.getBitmap(String.valueOf(Constants.ALBUM_PATH) + "native/" + pFileName, 400, SecExceptionCode.SEC_ERROR_PKG_VALID);
                FragmentProductActivity.this.mDeviceDetailsImage.setImageBitmap(FragmentProductActivity.this.mBitmap);
            }
        });
    }

    private void copySDFile(String pSourceFilePath, String pAimFilePath) {
        File aimPath = new File(pAimFilePath);
        if (!aimPath.exists()) {
            aimPath.mkdirs();
        }
        try {
            InputStream _inputstream = new FileInputStream(pSourceFilePath);
            OutputStream _outputstream = new FileOutputStream(pAimFilePath);
            byte[] buf = new byte[8192];
            while (true) {
                int len = _inputstream.read(buf);
                if (len <= 0) {
                    _inputstream.close();
                    _outputstream.close();
                    RecursionDeleteFile(new File(pSourceFilePath));
                    this.mBitmap = BitmapFactory.decodeFile(pAimFilePath);
                    this.mDeviceDetailsImage.setImageBitmap(this.mBitmap);
                    return;
                }
                _outputstream.write(buf, 0, len);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e2) {
            e2.printStackTrace();
        }
    }

    public static void RecursionDeleteFile(File file) {
        if (file.isFile()) {
            file.delete();
        } else if (file.isDirectory()) {
            File[] childFile = file.listFiles();
            if (childFile == null || childFile.length == 0) {
                file.delete();
                return;
            }
            for (File f : childFile) {
                RecursionDeleteFile(f);
            }
            file.delete();
        }
    }

    protected void onDestroy() {
        super.onDestroy();
        if (this.mBitmap != null && !this.mBitmap.isRecycled()) {
            this.mDeviceDetailsImage = null;
            this.mDeviceImage = null;
            this.mBitmap.recycle();
            System.gc();
        }
    }
}
