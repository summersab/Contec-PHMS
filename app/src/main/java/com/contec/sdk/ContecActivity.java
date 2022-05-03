package com.contec.sdk;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import com.contec.phms.R;

public class ContecActivity extends Activity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_contec);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.contec, menu);
        return true;
    }
}
