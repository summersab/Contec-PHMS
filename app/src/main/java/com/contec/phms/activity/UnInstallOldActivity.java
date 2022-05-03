package com.contec.phms.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import com.contec.phms.R;
import com.contec.phms.util.Constants;

public class UnInstallOldActivity extends Activity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.uninstalloldphms);
        initView();
    }

    private void initView() {
        LinearLayout dialogLayout = (LinearLayout) findViewById(R.id.bg_dialog);
        dialogLayout.getLayoutParams().width = (Constants.M_SCREENWEIGH * 4) / 5;
        dialogLayout.getLayoutParams().height = (Constants.M_SCREENHEIGH * 4) / 5;
        Button mbtnText = (Button) findViewById(R.id.btn_uninstall);
        mbtnText.setText(getResources().getString(R.string.uninstall_old_phms));
        mbtnText.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                UnInstallOldActivity.this.startActivity(new Intent("android.intent.action.DELETE", Uri.parse("package:com.contec")));
            }
        });
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == 4) {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}
