package com.contec.phms.widget;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.contec.phms.App_phms;
import com.contec.phms.R;
import com.contec.phms.login.LoginActivity;
import com.contec.phms.db.UserInfoDao;
import com.contec.phms.util.ButtonUnitl;
import u.aly.bs;

public class CustomChangeUserview extends RelativeLayout {
    private Context mContext;
    public UserInfoDao muserdao;
    private View mview;

    public CustomChangeUserview(Context context, UserInfoDao muserdao2) {
        super(context);
        this.mContext = this.mContext;
        this.muserdao = muserdao2;
        init(context);
    }

    public UserInfoDao getMuserdao() {
        return this.muserdao;
    }

    public void setMuserdao(UserInfoDao muserdao2) {
        this.muserdao = muserdao2;
    }

    public CustomChangeUserview(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomChangeUserview(Context context) {
        super(context);
    }

    private void init(Context context) {
        this.mContext = context;
        this.mview = LayoutInflater.from(context).inflate(R.layout.layout_changeuser_item, this, true);
        ((ImageButton) this.mview.findViewById(R.id.changeuserinfobtn)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (!ButtonUnitl.isFastDoubleClick()) {
                    String _currentphonenumber = App_phms.getInstance().mUserInfo.mUserID;
                    String substring = _currentphonenumber.substring(1);
                    if (!CustomChangeUserview.this.muserdao.getmUserId().equals(_currentphonenumber)) {
                        String _nozerowillphonenumber = CustomChangeUserview.this.muserdao.getmUserId().substring(1);
                        Intent _intent = new Intent(CustomChangeUserview.this.mContext, LoginActivity.class);
                        Bundle _bundle = new Bundle();
                        _bundle.putBoolean("fromchangeuser", true);
                        _bundle.putString("username", _nozerowillphonenumber);
                        _bundle.putString("userpsw", CustomChangeUserview.this.muserdao.getPsw());
                        _intent.putExtras(_bundle);
                        CustomChangeUserview.this.mContext.startActivity(_intent);
                        App_phms.getInstance().exit(1);
                    }
                }
            }
        });
        TextView mchangeuserinfo_name_tv = (TextView) this.mview.findViewById(R.id.changeuserinfo_name_tv);
        if (!this.muserdao.getmUserName().equals(bs.b)) {
            mchangeuserinfo_name_tv.setText(new StringBuilder(String.valueOf(this.muserdao.getmUserName())).toString());
        } else {
            mchangeuserinfo_name_tv.setText(new StringBuilder(String.valueOf(this.muserdao.getmUserId().substring(this.muserdao.getmUserId().length() - 4))).toString());
        }
    }
}
