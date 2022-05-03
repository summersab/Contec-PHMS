package com.contec.phms.activity;

import android.os.Bundle;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import com.contec.phms.App_phms;
import com.contec.phms.R;
import com.contec.phms.login.ActivityBase;
import com.contec.phms.util.Constants;
//import com.umeng.analytics.MobclickAgent;

public class ActivityLoginAnotherPlace extends ActivityBase implements View.OnClickListener {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App_phms.getInstance().addActivity(this);
        setContentView(R.layout.layout_dialog_login_another_place);
        Window dialogWindow = getWindow();
        dialogWindow.setType(2003);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = (Constants.M_SCREENWEIGH * 3) / 4;
        lp.height = (Constants.M_SCREENWEIGH * 1) / 2;
        dialogWindow.setAttributes(lp);
        ((Button) findViewById(R.id.sure)).setOnClickListener(this);
        ((Button) findViewById(R.id.cancle)).setOnClickListener(this);
        ((TextView) findViewById(R.id.show_msg)).setText(getString(R.string.str_login_another_place));
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return false;
    }

    protected void onResume() {
        super.onResume();
        //MobclickAgent.onResume(this);
    }

    protected void onPause() {
        super.onPause();
        //MobclickAgent.onPause(this);
    }

    public void onClick(View v) {
        if (v.getId() == R.id.sure) {
            Message msgs = new Message();
            msgs.what = Constants.Login_In_Another_Place_Sure;
            msgs.arg2 = 1;
            App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(msgs);
            finish();
        } else if (v.getId() == R.id.cancle) {
            Message msgs2 = new Message();
            msgs2.what = 551;
            msgs2.arg2 = 1;
            App_phms.getInstance().mEventBusPostOnBackGround.postInMainThread(msgs2);
            finish();
        }
    }
}
