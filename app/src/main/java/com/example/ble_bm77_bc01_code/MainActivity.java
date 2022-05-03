package com.example.ble_bm77_bc01_code;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.Menu;

import com.contec.phms.R;

public class MainActivity extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
}
