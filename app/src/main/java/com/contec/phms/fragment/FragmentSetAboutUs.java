package com.contec.phms.fragment;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import com.contec.phms.App_phms;
import com.contec.phms.R;
import com.contec.phms.login.ActivityBase;
import com.contec.phms.util.Constants;
import com.contec.phms.util.ScalingUtilities;
import com.contec.phms.util.ScreenAdapter;

public class FragmentSetAboutUs extends ActivityBase {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_set_aboutus);
        App_phms.getInstance().addActivity(this);
        init_view();
    }

    private int calculategriditemwidth() {
        return Constants.M_SCREENWEIGH - ScreenAdapter.dip2px(this, 30.0f);
    }

    private int calculategriditemheight() {
        return ScreenAdapter.dip2px(this, 150.0f);
    }

    private void init_view() {
        ((Button) findViewById(R.id.back_but)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                FragmentSetAboutUs.this.finish();
            }
        });
        try {
            PackageInfo packInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        Bitmap unscaledBitmap = ScalingUtilities.decodeResource(getResources(), R.drawable.img_contec, calculategriditemwidth(), calculategriditemheight(), ScalingUtilities.ScalingLogic.FIT);
        Bitmap scaledBitmap = ScalingUtilities.createScaledBitmap(unscaledBitmap, calculategriditemwidth(), calculategriditemheight(), ScalingUtilities.ScalingLogic.FIT);
        unscaledBitmap.recycle();
        ((ImageView) findViewById(R.id.img_contec)).setImageBitmap(scaledBitmap);
    }
}
