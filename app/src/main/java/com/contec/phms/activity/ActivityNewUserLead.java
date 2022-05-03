package com.contec.phms.activity;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.os.Bundle;
import androidx.viewpager.widget.ViewPager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.contec.phms.App_phms;
import com.contec.phms.R;
import com.contec.phms.fragment.ViewPagerAdaper;
import com.contec.phms.login.ActivityBase;
import com.contec.phms.util.CLog;
import com.contec.phms.util.Constants;
import com.contec.phms.widget.DialogClass;
//import com.umeng.analytics.MobclickAgent;
import java.io.InputStream;
import java.util.ArrayList;
import u.aly.bs;

public class ActivityNewUserLead extends ActivityBase {
    private final String TAG = getClass().getSimpleName();
    private DialogClass mDialogClass;
    private ViewPagerAdaper mViewPagerAdaper;
    private ViewPager viewpager;
    private ArrayList<View> views;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_login_lead_pager);
        App_phms.getInstance().addActivity(this);
        initViewPager();
    }

    protected void onResume() {
        PackageInfo packageInfo;
        super.onResume();
        try {
            packageInfo = getPackageManager().getPackageInfo("com.contec", 0);
        } catch (PackageManager.NameNotFoundException e) {
            packageInfo = null;
            e.printStackTrace();
        }
        if (packageInfo == null || packageInfo.equals(bs.b)) {
            if (this.mDialogClass != null && !this.mDialogClass.equals(bs.b)) {
                this.mDialogClass.dismiss();
                CLog.e("jxx", "代码执行到");
            }
        } else if (this.mDialogClass == null || this.mDialogClass.equals(bs.b)) {
            this.mDialogClass = new DialogClass((Context) this, getResources().getString(R.string.uninstall_old_phms), (int) R.drawable.phms_icon, (int) R.drawable.img_phms_icon);
        }
        //MobclickAgent.onResume(this);
    }

    protected void onPause() {
        super.onPause();
        //MobclickAgent.onPause(this);
    }

    private void initViewPager() {
        this.viewpager = (ViewPager) findViewById(R.id.viewpager_lead);
        this.views = new ArrayList<>();
        View v1 = LayoutInflater.from(this).inflate(R.layout.layout1, (ViewGroup) null);
        View v2 = LayoutInflater.from(this).inflate(R.layout.layout2, (ViewGroup) null);
        View v3 = LayoutInflater.from(this).inflate(R.layout.layout3, (ViewGroup) null);
        View v4 = LayoutInflater.from(this).inflate(R.layout.layout4, (ViewGroup) null);
        ImageView img1 = (ImageView) v1.findViewById(R.id.imgview_data_receive_lead);
        ImageView img2 = (ImageView) v2.findViewById(R.id.imgview_device_manager_lead);
        ImageView img3 = (ImageView) v3.findViewById(R.id.imgview_add_device_lead);
        ImageView img4 = (ImageView) v4.findViewById(R.id.imgview_data_report_lead);
        InputStream is = getResources().openRawResource(R.drawable.img_add_device_lead);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = false;
        Bitmap btp = BitmapFactory.decodeStream(is, (Rect) null, options);
        float bmpW = (float) btp.getWidth();
        btp.recycle();
        float scalX = ((float) Constants.SCREEN_WIDTH) / bmpW;
        if (scalX < 1.0f) {
            Matrix matrix = new Matrix();
            matrix.setScale(scalX, scalX);
            img1.setImageMatrix(matrix);
            img2.setImageMatrix(matrix);
            img3.setImageMatrix(matrix);
            img4.setImageMatrix(matrix);
        }
        if ("en".equals(getResources().getConfiguration().locale.getLanguage()) || "en".equals(Constants.Language)) {
            EnglishUserLead(img1, img2, img3, img4);
        }
        this.views.add(v1);
        this.views.add(v2);
        this.views.add(v3);
        this.views.add(v4);
        this.mViewPagerAdaper = new ViewPagerAdaper(this.views);
        this.viewpager.setAdapter(this.mViewPagerAdaper);
        this.viewpager.setCurrentItem(0);
        v4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (ActivityNewUserLead.this.mDialogClass != null && !ActivityNewUserLead.this.mDialogClass.equals(bs.b)) {
                    ActivityNewUserLead.this.mDialogClass.dismiss();
                    ActivityNewUserLead.this.mDialogClass = null;
                }
                ActivityNewUserLead.this.finish();
            }
        });
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode != 4) {
            return super.onKeyDown(keyCode, event);
        }
        finish();
        return false;
    }

    private void EnglishUserLead(ImageView img1, ImageView img2, ImageView img3, ImageView img4) {
        img1.setImageBitmap(readBitMap(getBaseContext(), R.drawable.img_userlead1));
        img2.setImageBitmap(readBitMap(getBaseContext(), R.drawable.img_userlead2));
        img3.setImageBitmap(readBitMap(getBaseContext(), R.drawable.img_userlead3));
        img4.setImageBitmap(readBitMap(getBaseContext(), R.drawable.img_userlead4));
    }

    private Bitmap readBitMap(Context context, int resId) {
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inPreferredConfig = Bitmap.Config.RGB_565;
        opt.inPurgeable = true;
        opt.inInputShareable = true;
        return BitmapFactory.decodeStream(context.getResources().openRawResource(resId), (Rect) null, opt);
    }

    protected void onDestroy() {
        super.onDestroy();
        CLog.i(this.TAG, "导航页 destroy...\t");
        if (this.views != null) {
            for (int i = 0; i < this.views.size(); i++) {
                this.views.get(i).destroyDrawingCache();
                System.gc();
            }
        }
        this.views = null;
        this.mViewPagerAdaper = null;
    }
}
