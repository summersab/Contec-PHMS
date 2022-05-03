package com.contec.scanpickphotocode;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.contec.phms.R;

public class QrcodeActivity extends Activity {
    private String BookCode = null;
    private ImageButton backb;
    private RelativeLayout bookr1;
    private RelativeLayout bookr2;
    private View.OnClickListener buttonlistener = new View.OnClickListener() {
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.backButton:
                    QrcodeActivity.this.finish();
                    return;
                default:
                    return;
            }
        }
    };
    private TextView feildt;
    private TextView text;
    private TextView titletext;
    private RelativeLayout waresr;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(1);
        setContentView(R.layout.activity_codemessage);
        initView();
        this.BookCode = getIntent().getStringExtra("string");
        this.titletext.setText("信息");
        this.bookr1.setVisibility(View.INVISIBLE);
        this.bookr2.setVisibility(View.INVISIBLE);
        this.text.setText("二维码中藏着内容");
        this.feildt.setText(this.BookCode);
    }

    private void initView() {
        this.bookr1 = (RelativeLayout) findViewById(R.id.relativeLayout3);
        this.bookr2 = (RelativeLayout) findViewById(R.id.relativeLayout4);
        this.waresr = (RelativeLayout) findViewById(R.id.relativeLayout2);
        this.feildt = (TextView) findViewById(R.id.filedcode);
        this.titletext = (TextView) findViewById(R.id.titletext);
        this.text = (TextView) findViewById(R.id.textmmm);
        this.backb = (ImageButton) findViewById(R.id.backButton);
        this.backb.setOnClickListener(this.buttonlistener);
    }
}
