package com.contec.phms.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import com.contec.phms.App_phms;
import com.contec.phms.R;
import com.contec.phms.ajaxcallback.AjaxCallBack_submitmodfiyUserinfo;
import com.contec.phms.login.ActivityBase;
import com.contec.phms.login.LoginActivity;
import com.contec.phms.db.LoginUserDao;
import com.contec.phms.db.UserInfoDao;
import com.contec.phms.util.CLog;
import com.contec.phms.util.ScreenAdapter;
import com.contec.phms.widget.CustomChangeUserview;
import com.contec.phms.widget.DialogClass;
import com.contec.phms.widget.TransUserinfo;
import android.widget.GridLayout;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import u.aly.bs;

public class ActivityUserDataEdit extends ActivityBase implements View.OnClickListener {
    private TransUserinfo _transuserinfo;
    private ArrayAdapter<CharSequence> adapter;
    private EditText mAddressEdit;
    private EditText mBirthArea;
    private EditText mBirthEdit;
    private EditText mIdCardEdit;
    private EditText mPhoneEdit;
    private Spinner mSexSpinner;
    private String mUserAddress;
    private String mUserAre;
    private String mUserBirthday;
    private String mUserCardtype;
    private String mUserDiskspace;
    private String mUserHospitalname;
    private String mUserIdcard;
    private String mUserName;
    private EditText mUserNameEdit;
    private String mUserPhone;
    private String mUserSex;
    private String mUserTotal;
    private DialogClass mcommitdialogClass;
    private GridLayout mhand_changeuser_gridlayout;
    private Handler mhandler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int _flag = msg.what;
            if (_flag == 104400) {
                ActivityUserDataEdit.this.mcommitdialogClass.dismiss();
                new DialogClass(ActivityUserDataEdit.this, ActivityUserDataEdit.this.getResources().getString(R.string.user_modifyUserinfosuccess));
            } else if (_flag == 104401) {
                ActivityUserDataEdit.this.mcommitdialogClass.dismiss();
                new DialogClass(ActivityUserDataEdit.this, ActivityUserDataEdit.this.getResources().getString(R.string.user_modifyUserinfofailed));
            }
        }
    };
    DialogInterface.OnKeyListener modifyUserinfoListener = new DialogInterface.OnKeyListener() {
        public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
            if (keyCode != 4 || event.getRepeatCount() == 0) {
            }
            return false;
        }
    };
    private int mresultcode = 1044;
    List<UserInfoDao> popUserArray;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_data_userdata_edit);
        if (getIntent().getSerializableExtra("UserInfo") != null) {
            this._transuserinfo = (TransUserinfo) getIntent().getSerializableExtra("UserInfo");
            this.mUserName = this._transuserinfo.mapuserinfo.get("username");
            this.mUserSex = this._transuserinfo.mapuserinfo.get(LoginUserDao.UserSex);
            this.mUserIdcard = this._transuserinfo.mapuserinfo.get("useridcard");
            this.mUserPhone = this._transuserinfo.mapuserinfo.get("userphone");
            this.mUserBirthday = this._transuserinfo.mapuserinfo.get("userbirthday");
            this.mUserAddress = this._transuserinfo.mapuserinfo.get("useraddress");
            this.mUserAre = this._transuserinfo.mapuserinfo.get("userare");
            this.mUserCardtype = this._transuserinfo.mapuserinfo.get("usercardtype");
            this.mUserDiskspace = this._transuserinfo.mapuserinfo.get("userdiskspace");
            this.mUserTotal = this._transuserinfo.mapuserinfo.get("usertotal");
            this.mUserHospitalname = this._transuserinfo.mapuserinfo.get("userhospitalname");
        }
        init_view();
    }

    private void init_view() {
        this.mUserNameEdit = (EditText) findViewById(R.id.id_username_edit);
        this.mIdCardEdit = (EditText) findViewById(R.id.idcard_edit);
        this.mPhoneEdit = (EditText) findViewById(R.id.phone_edit);
        this.mBirthEdit = (EditText) findViewById(R.id.birth_edit);
        this.mBirthArea = (EditText) findViewById(R.id.birth_area_edit);
        this.mAddressEdit = (EditText) findViewById(R.id.address_edit);
        this.mIdCardEdit.setText(this.mUserIdcard);
        this.mUserNameEdit.setText(this.mUserName);
        this.mPhoneEdit.setText(this.mUserPhone);
        this.mBirthEdit.setText(this.mUserBirthday);
        this.mBirthArea.setText(this.mUserAddress);
        this.mAddressEdit.setText(this.mUserAre);
        ((TextView) findViewById(R.id.cardtype_content)).setText(this.mUserCardtype);
        ((TextView) findViewById(R.id.diskspace_edit)).setText(this.mUserDiskspace);
        ((TextView) findViewById(R.id.total_edit)).setText(this.mUserTotal);
        ((EditText) findViewById(R.id.hospitalname_edit)).setText(this.mUserHospitalname);
        ((Button) findViewById(R.id.back_but)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                ActivityUserDataEdit.this.setResult(ActivityUserDataEdit.this.mresultcode, new Intent());
                ActivityUserDataEdit.this.finish();
                CLog.i("lz", "个人信息编辑已经退出了");
            }
        });
        ((Button) findViewById(R.id.edit_ok_btn)).setOnClickListener(this);
        this.mSexSpinner = (Spinner) findViewById(R.id.sex_spinner);
        this.adapter = ArrayAdapter.createFromResource(this, R.array.sex_array, 17367048);
        this.adapter.setDropDownViewResource(17367049);
        this.mSexSpinner.setAdapter(this.adapter);
        this.mSexSpinner.setSelection(Integer.parseInt(this.mUserSex), true);
        this.mSexSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
            }

            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        this.mhand_changeuser_gridlayout = (GridLayout) findViewById(R.id.hand_changeuser_gridlayout);
        inint_bottomuserlayout();
        ((LinearLayout) findViewById(R.id.changeuserlayout)).setOnClickListener(this);
    }

    private void inint_bottomuserlayout() {
        getallusers();
        for (int i = 0; i < this.popUserArray.size(); i++) {
            GridLayout.LayoutParams mchangeuserparmas = new GridLayout.LayoutParams();
            mchangeuserparmas.rightMargin = ScreenAdapter.dip2px(this, 8.0f);
            mchangeuserparmas.setGravity(17);
            this.mhand_changeuser_gridlayout.addView(new CustomChangeUserview((Context) this, this.popUserArray.get(i)), this.mhand_changeuser_gridlayout.getChildCount(), mchangeuserparmas);
        }
    }

    private void getallusers() {
        this.popUserArray = new ArrayList();
        try {
            this.popUserArray = App_phms.getInstance().mHelper.getUserDao().queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void onClick(View v) {
        if (v.getId() == R.id.changeuserlayout) {
            Intent _intent = new Intent(this, LoginActivity.class);
            Bundle _bundle = new Bundle();
            _bundle.putBoolean("fromaddnewUser", true);
            _bundle.putString("username", bs.b);
            _bundle.putString("userpsw", bs.b);
            _intent.putExtras(_bundle);
            startActivity(_intent);
            App_phms.getInstance().exit(1);
        } else if (v.getId() == R.id.edit_ok_btn) {
            this.mcommitdialogClass = new DialogClass((Context) this, getString(R.string.user_iscommitUserinfo), false, 0, this.modifyUserinfoListener);
            String trim = this.mUserNameEdit.getText().toString().trim();
            String trim2 = this.mIdCardEdit.getText().toString().trim();
            String trim3 = this.mPhoneEdit.getText().toString().trim();
            String sb = new StringBuilder(String.valueOf(this.mSexSpinner.getSelectedItemPosition())).toString();
            String trim4 = this.mBirthEdit.getText().toString().trim();
            String trim5 = this.mAddressEdit.getText().toString().trim();
            new AjaxCallBack_submitmodfiyUserinfo(this, this.mhandler);
        }
    }
}
