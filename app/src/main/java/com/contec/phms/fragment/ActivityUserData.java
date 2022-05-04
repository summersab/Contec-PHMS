package com.contec.phms.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import com.contec.phms.App_phms;
import com.contec.phms.R;
import com.contec.phms.ajaxcallback.AjaxCallBack_GetUserinfo;
import com.contec.phms.login.ActivityBase;
import com.contec.phms.login.LoginActivity;
import com.contec.phms.db.LoginUserDao;
import com.contec.phms.db.UserInfoDao;
import com.contec.phms.util.Constants;
import com.contec.phms.util.PageUtil;
import com.contec.phms.util.ScreenAdapter;
import com.contec.phms.widget.CustomChangeUserview;
import com.contec.phms.widget.DialogClass;
import com.contec.phms.widget.TransUserinfo;
import android.widget.GridLayout;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import cn.com.contec_net_3_android.Meth_android_getUserinfo;
import u.aly.bs;

public class ActivityUserData extends ActivityBase implements View.OnClickListener {
    private boolean mGoToEditPage = false;
    private UserDataAdapter mUserDataAdapter;
    private ArrayList<UserData> mUserDataArray;
    DialogInterface.OnKeyListener mUserinfoListener = new DialogInterface.OnKeyListener() {
        public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
            if (keyCode == 4 && event.getRepeatCount() == 0) {
                ActivityUserData.this.mquestuserinfodialogClass.dismiss();
            }
            return false;
        }
    };
    private HashMap<String, String> mapUserinfo;
    private GridLayout mhand_changeuser_gridlayout;
    private Handler mhandler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int _flag = msg.what;
            if (_flag == ActivityUserData.this.mqueryuserinfosuccess) {
                if (msg.getData().getSerializable("newuserinfo") != null) {
                    ActivityUserData.this.mGoToEditPage = true;
                    ActivityUserData.this.mapUserinfo.clear();
                    ActivityUserData.this.mUserDataArray.clear();
                    ActivityUserData.this.mapUserinfo = ((TransUserinfo) msg.getData().getSerializable("newuserinfo")).getMapuserinfo();
                    ActivityUserData.this.mUserDataArray.add(new UserData(ActivityUserData.this.getString(R.string.user_username), (String) ActivityUserData.this.mapUserinfo.get("username")));
                    String _newsex = new String();
                    int _intsex = Integer.valueOf((String) ActivityUserData.this.mapUserinfo.get(LoginUserDao.UserSex)).intValue();
                    if (_intsex == 0) {
                        _newsex = "Male";
                    } else if (_intsex == 1) {
                        _newsex = "Female";
                    } else if (_intsex == 2) {
                        _newsex = "No choice";
                    }
                    ActivityUserData.this.mUserDataArray.add(new UserData(ActivityUserData.this.getString(R.string.user_sex), _newsex));
                    ActivityUserData.this.mUserDataArray.add(new UserData(ActivityUserData.this.getString(R.string.user_idcard), (String) ActivityUserData.this.mapUserinfo.get("useridcard")));
                    ActivityUserData.this.mUserDataArray.add(new UserData(ActivityUserData.this.getString(R.string.user_phone), (String) ActivityUserData.this.mapUserinfo.get("userphone")));
                    ActivityUserData.this.mUserDataArray.add(new UserData(ActivityUserData.this.getString(R.string.user_birth), (String) ActivityUserData.this.mapUserinfo.get("userbirthday")));
                    ActivityUserData.this.mUserDataArray.add(new UserData(ActivityUserData.this.getString(R.string.user_address), (String) ActivityUserData.this.mapUserinfo.get("useraddress")));
                    ActivityUserData.this.mUserDataArray.add(new UserData(ActivityUserData.this.getString(R.string.user_are), (String) ActivityUserData.this.mapUserinfo.get("userare")));
                    ActivityUserData.this.mUserDataArray.add(new UserData(ActivityUserData.this.getString(R.string.user_cartype), (String) ActivityUserData.this.mapUserinfo.get("usercardtype")));
                    ActivityUserData.this.mUserDataArray.add(new UserData(ActivityUserData.this.getString(R.string.user_diskspace), (String) ActivityUserData.this.mapUserinfo.get("userdiskspace")));
                    ActivityUserData.this.mUserDataArray.add(new UserData(ActivityUserData.this.getString(R.string.user_hospitalname), (String) ActivityUserData.this.mapUserinfo.get("userhospitalname")));
                    ActivityUserData.this.mUserDataArray.add(new UserData(ActivityUserData.this.getString(R.string.user_used), (String) ActivityUserData.this.mapUserinfo.get("userUsed")));
                    ActivityUserData.this.mUserDataArray.add(new UserData(ActivityUserData.this.getString(R.string.user_total), (String) ActivityUserData.this.mapUserinfo.get("usertotal")));
                    ActivityUserData.this.mUserDataArray.add(new UserData(ActivityUserData.this.getString(R.string.user_createdate), (String) ActivityUserData.this.mapUserinfo.get("usercreatedate")));
                    ActivityUserData.this.mUserDataArray.add(new UserData(ActivityUserData.this.getString(R.string.user_startdate), (String) ActivityUserData.this.mapUserinfo.get("userstartdate")));
                    ActivityUserData.this.mUserDataArray.add(new UserData(ActivityUserData.this.getString(R.string.user_enddate), (String) ActivityUserData.this.mapUserinfo.get("userenddate")));
                    ActivityUserData.this.mUserDataAdapter.notifyDataSetChanged();
                }
                ActivityUserData.this.mquestuserinfodialogClass.dismiss();
            } else if (_flag == 104001) {
                ActivityUserData.this.mquestuserinfodialogClass.dismiss();
                new DialogClass(ActivityUserData.this, ActivityUserData.this.getResources().getString(R.string.user_QueryUserinfoargserror));
            } else if (_flag == 104002) {
                ActivityUserData.this.mquestuserinfodialogClass.dismiss();
                new DialogClass(ActivityUserData.this, ActivityUserData.this.getResources().getString(R.string.user_QueryUserinfodberror));
            } else if (_flag == 104011) {
                ActivityUserData.this.mquestuserinfodialogClass.dismiss();
                new DialogClass(ActivityUserData.this, ActivityUserData.this.getResources().getString(R.string.user_networktimeout));
            } else if (_flag == 104012) {
                ActivityUserData.this.mquestuserinfodialogClass.dismiss();
                new DialogClass(ActivityUserData.this, ActivityUserData.this.getResources().getString(R.string.user_networkerror));
            } else if (_flag == 100007) {
                ActivityUserData.this.mquestuserinfodialogClass.dismiss();
                new DialogClass(ActivityUserData.this, ActivityUserData.this.getResources().getString(R.string.str_login_another_place));
            }
        }
    };
    private final int mloginInAnotherPlace = 100007;
    private final int mqueryuserinfoargserror = 104001;
    private final int mqueryuserinfodberror = 104002;
    private final int mqueryuserinfoneterror = Constants.mqueryuserinfoneterror;
    private int mqueryuserinfosuccess = 104000;
    private final int mqueryuserinfotimeout = Constants.mqueryuserinfotimeout;
    private DialogClass mquestuserinfodialogClass;
    private int mrequestcode = 1040;
    private int mresultcode = 1044;
    List<UserInfoDao> popUserArray;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_data_userdata);
        init_view();
    }

    private void init_view() {
        printUserinfo();
        ((Button) findViewById(R.id.back_but)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                ActivityUserData.this.finish();
            }
        });
        ((Button) findViewById(R.id.edit_btn)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                if (ActivityUserData.this.mGoToEditPage) {
                    Intent _intentSerch = new Intent(ActivityUserData.this, ActivityUserDataEdit.class);
                    _intentSerch.putExtra("UserInfo", new TransUserinfo(ActivityUserData.this.mapUserinfo));
                    ActivityUserData.this.startActivityForResult(_intentSerch, ActivityUserData.this.mrequestcode);
                }
            }
        });
        this.mUserDataArray = new ArrayList<>();
        this.mUserDataAdapter = new UserDataAdapter(this, this.mUserDataArray);
        ((ListView) findViewById(R.id.userinfolist)).setAdapter(this.mUserDataAdapter);
        this.mhand_changeuser_gridlayout = (GridLayout) findViewById(R.id.hand_changeuser_gridlayout);
        inint_bottomuserlayout();
        ((LinearLayout) findViewById(R.id.changeuserlayout)).setOnClickListener(this);
        this.mquestuserinfodialogClass = new DialogClass((Context) this, getString(R.string.user_isQueryUserinfo), false, 0, this.mUserinfoListener);
        Meth_android_getUserinfo.getuserinfo(App_phms.getInstance().mUserInfo.mUserID, App_phms.getInstance().mUserInfo.mPassword, PageUtil.getLoginUserInfo().mSenderId, App_phms.getInstance().mUserInfo.mPHPSession, new AjaxCallBack_GetUserinfo(this, this.mhandler), App_phms.getInstance().mFinalHttp, String.valueOf(Constants.URL) + "/main.php");
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == this.mresultcode) {
            this.mquestuserinfodialogClass = new DialogClass((Context) this, getString(R.string.user_isQueryUserinfo), false, 0, this.mUserinfoListener);
            Meth_android_getUserinfo.getuserinfo(App_phms.getInstance().mUserInfo.mUserID, App_phms.getInstance().mUserInfo.mPassword, PageUtil.getLoginUserInfo().mSenderId, App_phms.getInstance().mUserInfo.mPHPSession, new AjaxCallBack_GetUserinfo(this, this.mhandler), App_phms.getInstance().mFinalHttp, String.valueOf(Constants.URL) + "/main.php");
        }
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

    private void printUserinfo() {
        this.mapUserinfo = new HashMap<>();
        this.mapUserinfo.put("username", PageUtil.getLoginUserInfo().mUserName);
        this.mapUserinfo.put("userphone", PageUtil.getLoginUserInfo().mPhone);
        this.mapUserinfo.put("useridcard", PageUtil.getLoginUserInfo().mPID);
        this.mapUserinfo.put("userbirthday", PageUtil.getLoginUserInfo().mBirthday);
        this.mapUserinfo.put("useraddress", PageUtil.getLoginUserInfo().mAddress);
        this.mapUserinfo.put("userare", PageUtil.getLoginUserInfo().mAre);
        this.mapUserinfo.put("usercardtype", "user card");
        this.mapUserinfo.put("userdiskspace", PageUtil.getLoginUserInfo().mDiskSpace);
        this.mapUserinfo.put("usertotal", PageUtil.getLoginUserInfo().mTotal);
        this.mapUserinfo.put("userhospitalname", PageUtil.getLoginUserInfo().mHospitalName);
        int _intsex = Integer.valueOf(PageUtil.getLoginUserInfo().mSex).intValue();
        if (_intsex == 0) {
            this.mapUserinfo.put(LoginUserDao.UserSex, "Male");
        } else if (_intsex == 1) {
            this.mapUserinfo.put(LoginUserDao.UserSex, "Female");
        } else if (_intsex == 2) {
            this.mapUserinfo.put(LoginUserDao.UserSex, "No choice");
        }
        this.mapUserinfo.put("userUsed", PageUtil.getLoginUserInfo().mUsed);
        this.mapUserinfo.put("usertotal", PageUtil.getLoginUserInfo().mTotal);
        this.mapUserinfo.put("usercreatedate", PageUtil.getLoginUserInfo().mCreateDate);
        this.mapUserinfo.put("userstartdate", PageUtil.getLoginUserInfo().mStartDate);
        this.mapUserinfo.put("userenddate", PageUtil.getLoginUserInfo().mEndDate);
    }

    protected void onResume() {
        super.onResume();
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
        }
    }
}
