package com.contec.phms.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.alibaba.cchannel.push.receiver.CPushMessageCodec;
import com.contec.phms.App_phms;
import com.contec.phms.R;
import com.contec.phms.ajaxcallback.AjaxCallBack_getHospital;
import com.contec.phms.ajaxcallback.AjaxCallBack_submitmodfiyUserinfo;
import com.contec.phms.domain.CityListItem;
import com.contec.phms.domain.HospitalBean;
import com.contec.phms.db.LoginUserDao;
import com.contec.phms.db.RegisterPhoneCityDBManager;
import com.contec.phms.db.localdata.PluseDataDao;
import com.contec.phms.util.Constants;
import com.contec.phms.util.PageUtil;
import com.contec.phms.util.ParseXmlService;
import com.contec.phms.util.PhmsSharedPreferences;
import com.contec.phms.util.SaveHospitalUtils;
import com.contec.phms.util.SpinerPopWindow;
import com.contec.phms.widget.DialogClass;
import com.zxing.android.CaptureActivity;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import cn.com.contec_net_3_android.Meth_android_getHospital;
import cn.com.contec_net_3_android.Meth_android_modifyUserinfo;
import u.aly.bs;

@SuppressLint({"CutPasteId"})
public class ChangeHospitalActivity extends Activity implements View.OnClickListener, AdapterView.OnItemClickListener {
    private HospitalBean _Bean;
    private HospitalAdapter adapter;
    private String city;
    private View.OnClickListener clickListener = new View.OnClickListener() {
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.tv_value:
                    ChangeHospitalActivity.this.mSpinerPopWindow.setWidth(ChangeHospitalActivity.this.tvValue.getWidth());
                    ChangeHospitalActivity.this.mSpinerPopWindow.showAsDropDown(ChangeHospitalActivity.this.tvValue);
                    ChangeHospitalActivity.this.setTextImage(R.drawable.arrow_btn_gray);
                    return;
                default:
                    return;
            }
        }
    };
    private PopupWindow.OnDismissListener dismissListener = new PopupWindow.OnDismissListener() {
        public void onDismiss() {
            ChangeHospitalActivity.this.setTextImage(R.drawable.arrow_btn);
        }
    };
    private String district;
    private Handler handler_spinner = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    int current_province = ((Integer) msg.obj).intValue();
                    ChangeHospitalActivity.this.mSpinnerProvince.setSelection(current_province, true);
                    ChangeHospitalActivity.this.spinnerCity(((CityListItem) ChangeHospitalActivity.this.list_province.get(current_province)).getPcode());
                    if (ChangeHospitalActivity.this.city != null) {
                        for (int i = 0; i < ChangeHospitalActivity.this.list_city.size(); i++) {
                            if (ChangeHospitalActivity.this.city.equals(((CityListItem) ChangeHospitalActivity.this.list_city.get(i)).getName().trim())) {
                                Message message = Message.obtain();
                                message.what = 2;
                                message.obj = Integer.valueOf(i);
                                ChangeHospitalActivity.this.handler_spinner.sendMessage(message);
                                return;
                            }
                        }
                        return;
                    }
                    return;
                case 2:
                    int current_city = ((Integer) msg.obj).intValue();
                    ChangeHospitalActivity.this.mSpinnerCity.setSelection(current_city, true);
                    ChangeHospitalActivity.this.spinnerDistrict(((CityListItem) ChangeHospitalActivity.this.list_city.get(current_city)).getPcode());
                    if (ChangeHospitalActivity.this.district != null) {
                        for (int i2 = 0; i2 < ChangeHospitalActivity.this.list_district.size(); i2++) {
                            if (ChangeHospitalActivity.this.district.equals(((CityListItem) ChangeHospitalActivity.this.list_district.get(i2)).getName().trim())) {
                                Message message2 = Message.obtain();
                                message2.what = 3;
                                message2.obj = Integer.valueOf(i2);
                                ChangeHospitalActivity.this.handler_spinner.sendMessageDelayed(message2, 500);
                                return;
                            }
                        }
                        return;
                    }
                    return;
                case 3:
                    int current_district = ((Integer) msg.obj).intValue();
                    ChangeHospitalActivity.this.mSpinnerDistrict.setSelection(current_district, true);
                    ChangeHospitalActivity.this.doPostHospital(((CityListItem) ChangeHospitalActivity.this.list_district.get(current_district)).getId().substring(0, 6));
                    return;
                default:
                    return;
            }
        }
    };
    private HospitalBean hospitalBean;
    private AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
            ChangeHospitalActivity.this.mSpinerPopWindow.dismiss();
            ChangeHospitalActivity.this.mHospitalBean = (HospitalBean) ChangeHospitalActivity.this.listHospital.get(position);
            ChangeHospitalActivity.this.tvValue.setText(((HospitalBean) ChangeHospitalActivity.this.listHospital.get(position)).getHospitalName());
        }
    };
    private List<HospitalBean> listHospital;
    private List<HospitalBean> listHospitals;
    private List<CityListItem> list_city;
    private List<CityListItem> list_district;
    private List<CityListItem> list_province;
    private LinearLayout mAllSpinner;
    private Button mCancelSaveInfor;
    private boolean mCheckHaigang = true;
    private String mCity;
    private DialogClass mDialog;
    private String mDistrict;
    private HospitalBean mHospitalBean;
    private String mHospitalId;
    private Spinner mListviewHospitio;
    private LinearLayout mLl_hospital;
    private LoginUserDao mLoginUserDao;
    private TextView mNoSelectHospital;
    private String mProvince;
    private TextView mRegistHostipalName;
    private TextView mScanHospitionColor;
    private TextView mScanSelectHospition;
    private ImageView mScanZxing;
    private TextView mSelcetChangeHopital;
    private TextView mSelecteHospital;
    private PhmsSharedPreferences mSharepreferance;
    private SpinerPopWindow<String> mSpinerPopWindow;
    private Spinner mSpinnerCity;
    private Spinner mSpinnerDistrict;
    private Spinner mSpinnerProvince;
    private Button mSubmitHospital;
    private LinearLayout mZnNeed;
    private DialogClass m_dialogClass;
    private boolean mflage = true;
    Handler mhospitalHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Constants.REQUEST_HOSPITAL_SUCCESS /*110400*/:
                    Log.e("获取当前医院的名字", "--=======hospitalname" + ChangeHospitalActivity.this.mLoginUserDao.mUID);
                    ChangeHospitalActivity.this.listHospital = ParseXmlService.readXML(msg.getData().getString("content"));
                    ChangeHospitalActivity.this.mSpinerPopWindow = new SpinerPopWindow(ChangeHospitalActivity.this, ChangeHospitalActivity.this.listHospital, ChangeHospitalActivity.this.itemClickListener);
                    ChangeHospitalActivity.this.mSpinerPopWindow.setOnDismissListener(ChangeHospitalActivity.this.dismissListener);
                    if (ChangeHospitalActivity.this.listHospital == null || ChangeHospitalActivity.this.listHospital.size() <= 0) {
                        Toast toast = Toast.makeText(ChangeHospitalActivity.this, R.string.str_no_response, Toast.LENGTH_LONG);
                        toast.setGravity(17, 0, 0);
                        toast.show();
                        ChangeHospitalActivity.this._Bean = new HospitalBean();
                        ChangeHospitalActivity.this._Bean.setHospitalId(bs.b);
                        ChangeHospitalActivity.this._Bean.setHospitalName(ChangeHospitalActivity.this.getResources().getString(R.string.nochangehosical));
                        ChangeHospitalActivity.this.tvValue.setText(ChangeHospitalActivity.this.getResources().getString(R.string.nochangehosical));
                        ChangeHospitalActivity.this.listHospital.add(ChangeHospitalActivity.this._Bean);
                        ChangeHospitalActivity.this.mSpinerPopWindow = new SpinerPopWindow(ChangeHospitalActivity.this, ChangeHospitalActivity.this.listHospital, ChangeHospitalActivity.this.itemClickListener);
                        ChangeHospitalActivity.this.mSpinerPopWindow.setOnDismissListener(ChangeHospitalActivity.this.dismissListener);
                        return;
                    } else if (ChangeHospitalActivity.this.sp.getString(SaveHospitalUtils.spProvince + ChangeHospitalActivity.this.mLoginUserDao.mUID) != ChangeHospitalActivity.this.mProvince && !ChangeHospitalActivity.this.sp.getString(SaveHospitalUtils.spProvince + ChangeHospitalActivity.this.mLoginUserDao.mUID).equalsIgnoreCase(ChangeHospitalActivity.this.mProvince)) {
                        ChangeHospitalActivity.this.mHospitalBean = (HospitalBean) ChangeHospitalActivity.this.listHospital.get(0);
                        ChangeHospitalActivity.this.mSpinerPopWindow = new SpinerPopWindow(ChangeHospitalActivity.this, ChangeHospitalActivity.this.listHospital, ChangeHospitalActivity.this.itemClickListener);
                        ChangeHospitalActivity.this.tvValue.setText(((HospitalBean) ChangeHospitalActivity.this.listHospital.get(0)).getHospitalName());
                        return;
                    } else if (ChangeHospitalActivity.this.sp.getString(SaveHospitalUtils.spCity + ChangeHospitalActivity.this.mLoginUserDao.mUID) != ChangeHospitalActivity.this.mCity && !ChangeHospitalActivity.this.sp.getString(SaveHospitalUtils.spCity + ChangeHospitalActivity.this.mLoginUserDao.mUID).equalsIgnoreCase(ChangeHospitalActivity.this.mCity)) {
                        ChangeHospitalActivity.this.mHospitalBean = (HospitalBean) ChangeHospitalActivity.this.listHospital.get(0);
                        ChangeHospitalActivity.this.mSpinerPopWindow = new SpinerPopWindow(ChangeHospitalActivity.this, ChangeHospitalActivity.this.listHospital, ChangeHospitalActivity.this.itemClickListener);
                        ChangeHospitalActivity.this.mSpinerPopWindow.setOnDismissListener(ChangeHospitalActivity.this.dismissListener);
                        ChangeHospitalActivity.this.tvValue.setText(((HospitalBean) ChangeHospitalActivity.this.listHospital.get(0)).getHospitalName());
                        return;
                    } else if (ChangeHospitalActivity.this.sp.getString(SaveHospitalUtils.spDistrict + ChangeHospitalActivity.this.mLoginUserDao.mUID) == ChangeHospitalActivity.this.mDistrict || ChangeHospitalActivity.this.sp.getString(SaveHospitalUtils.spDistrict + ChangeHospitalActivity.this.mLoginUserDao.mUID).equalsIgnoreCase(ChangeHospitalActivity.this.mDistrict)) {
                        int k = ChangeHospitalActivity.this.listHospital.size();
                        for (int i = 0; i < k; i++) {
                            ChangeHospitalActivity.this.mHospitalBean = (HospitalBean) ChangeHospitalActivity.this.listHospital.get(i);
                            if (ChangeHospitalActivity.this.sp.getString(SaveHospitalUtils.spHospitalname + ChangeHospitalActivity.this.mLoginUserDao.mUID) == null || ChangeHospitalActivity.this.sp.getString(SaveHospitalUtils.spHospitalname + ChangeHospitalActivity.this.mLoginUserDao.mUID).equalsIgnoreCase(bs.b)) {
                                ChangeHospitalActivity.this.mHospitalBean = (HospitalBean) ChangeHospitalActivity.this.listHospital.get(i);
                                ChangeHospitalActivity.this.mSpinerPopWindow = new SpinerPopWindow(ChangeHospitalActivity.this, ChangeHospitalActivity.this.listHospital, ChangeHospitalActivity.this.itemClickListener);
                                ChangeHospitalActivity.this.mSpinerPopWindow.setOnDismissListener(ChangeHospitalActivity.this.dismissListener);
                                ChangeHospitalActivity.this.tvValue.setText(((HospitalBean) ChangeHospitalActivity.this.listHospital.get(0)).getHospitalName());
                            } else {
                                Log.e("获取当前的医院名字", ChangeHospitalActivity.this.sp.getString(SaveHospitalUtils.spHospitalname + ChangeHospitalActivity.this.mLoginUserDao.mUID));
                                ChangeHospitalActivity.this.tvValue.setText(ChangeHospitalActivity.this.sp.getString(SaveHospitalUtils.spHospitalname + ChangeHospitalActivity.this.mLoginUserDao.mUID));
                            }
                        }
                        return;
                    } else {
                        ChangeHospitalActivity.this.mHospitalBean = (HospitalBean) ChangeHospitalActivity.this.listHospital.get(0);
                        ChangeHospitalActivity.this.mSpinerPopWindow = new SpinerPopWindow(ChangeHospitalActivity.this, ChangeHospitalActivity.this.listHospital, ChangeHospitalActivity.this.itemClickListener);
                        ChangeHospitalActivity.this.mSpinerPopWindow.setOnDismissListener(ChangeHospitalActivity.this.dismissListener);
                        ChangeHospitalActivity.this.tvValue.setText(((HospitalBean) ChangeHospitalActivity.this.listHospital.get(0)).getHospitalName());
                        return;
                    }
                case Constants.REQUEST_HOSPITAL_FAILED /*110401*/:
                    new DialogClass(ChangeHospitalActivity.this, ChangeHospitalActivity.this.getResources().getString(R.string.user_parameter_error)).show();
                    return;
                case Constants.REQUEST_HOSPITAL_DB_FAILED /*110402*/:
                    new DialogClass(ChangeHospitalActivity.this, ChangeHospitalActivity.this.getResources().getString(R.string.user_DB_error)).show();
                    return;
                case Constants.HOSPITAL_FAILED /*110403*/:
                    new DialogClass(ChangeHospitalActivity.this, ChangeHospitalActivity.this.getResources().getString(R.string.user_networkerror)).show();
                    return;
                case Constants.NETWROK_NOT_GOOD /*900006*/:
                    new DialogClass(ChangeHospitalActivity.this, ChangeHospitalActivity.this.getResources().getString(R.string.user_networknotgood)).show();
                    return;
                default:
                    return;
            }
        }
    };
    private String mhospitalId;
    private String mhospitalName;
    private Handler modifyHandler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int _flag = msg.what;
            if (_flag == 104400) {
                if (ChangeHospitalActivity.this.mDialog != null) {
                    ChangeHospitalActivity.this.mDialog.dismiss();
                }
                new DialogClass(ChangeHospitalActivity.this, ChangeHospitalActivity.this.getResources().getString(R.string.user_modifyUserinfosuccess));
            } else if (_flag == 104403) {
                if (ChangeHospitalActivity.this.mDialog != null) {
                    ChangeHospitalActivity.this.mDialog.dismiss();
                }
                new DialogClass(ChangeHospitalActivity.this, ChangeHospitalActivity.this.getResources().getString(R.string.modofyinfroparaerror));
            } else if (_flag == 104402) {
                if (ChangeHospitalActivity.this.mDialog != null) {
                    ChangeHospitalActivity.this.mDialog.dismiss();
                }
                new DialogClass(ChangeHospitalActivity.this, ChangeHospitalActivity.this.getResources().getString(R.string.modofyinfrodberror));
            }
        }
    };
    private String province;
    private PhmsSharedPreferences sp;
    private TextView tvValue;
    private String userInfoPath;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.modifyhospital);
        initView();
        initData();
    }

    private void initView() {
        this.mSpinnerProvince = (Spinner) findViewById(R.id.spinner_province_zc);
        this.mSpinnerCity = (Spinner) findViewById(R.id.spinner_city_zc);
        this.mSpinnerDistrict = (Spinner) findViewById(R.id.spinner_district_zc);
        this.mSubmitHospital = (Button) findViewById(R.id.modify_hospital_btn);
        this.mZnNeed = (LinearLayout) findViewById(R.id.zn_use);
        this.mCancelSaveInfor = (Button) findViewById(R.id.cancel_save_infor);
        this.mScanZxing = (ImageView) findViewById(R.id.scan_zxing);
        this.mListviewHospitio = (Spinner) findViewById(R.id.spinner_hospital_zc);
        this.mNoSelectHospital = (TextView) findViewById(R.id.no_select_hopital);
        this.mSelecteHospital = (TextView) findViewById(R.id.select_hopital);
        this.mScanSelectHospition = (TextView) findViewById(R.id.scan_select_hopital);
        this.mScanHospitionColor = (TextView) findViewById(R.id.scan_change_hopital_color);
        this.mSelcetChangeHopital = (TextView) findViewById(R.id.selcet_change_hopital);
        this.mAllSpinner = (LinearLayout) findViewById(R.id.ll_all_spinner);
        this.mRegistHostipalName = (TextView) findViewById(R.id.tv_value);
        this.tvValue = (TextView) findViewById(R.id.tv_value);
        this.tvValue.setOnClickListener(this.clickListener);
        this.sp = PhmsSharedPreferences.getInstance(this);
        this.mCancelSaveInfor.setOnClickListener(this);
        this.mScanZxing.setOnClickListener(this);
        this.mSubmitHospital.setOnClickListener(this);
        this.mSharepreferance = PhmsSharedPreferences.getInstance(this);
        this.mLoginUserDao = PageUtil.getLoginUserInfo();
        String _language = Locale.getDefault().getLanguage();
        if (Constants.Language.equalsIgnoreCase("en") || !_language.equalsIgnoreCase("zh")) {
            this.mZnNeed.setVisibility(View.INVISIBLE);
            ViewGroup.LayoutParams params = this.mZnNeed.getLayoutParams();
            params.height = 40;
            this.mZnNeed.setLayoutParams(params);
            this.tvValue.setClickable(false);
            this.tvValue.setCompoundDrawables((Drawable) null, (Drawable) null, (Drawable) null, (Drawable) null);
            if (Constants.Language.equalsIgnoreCase("zh")) {
                this.mZnNeed.setVisibility(View.VISIBLE);
                ViewGroup.LayoutParams zhParams = this.mZnNeed.getLayoutParams();
                zhParams.height = -2;
                this.mZnNeed.setLayoutParams(zhParams);
                this.tvValue.setCompoundDrawables((Drawable) null, (Drawable) null, getResources().getDrawable(R.drawable.arrow_btn), (Drawable) null);
                this.tvValue.setClickable(true);
            }
        }
    }

    private void initData() {
        spinnerProvince();
    }

    private void setTextImage(int resId) {
        Drawable drawable = getResources().getDrawable(resId);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        this.tvValue.setCompoundDrawables((Drawable) null, (Drawable) null, drawable, (Drawable) null);
    }

    private void spinnerProvince() {
        RegisterPhoneCityDBManager mDbManager = new RegisterPhoneCityDBManager(this);
        mDbManager.openDatabase();
        SQLiteDatabase mSdb = mDbManager.getmDatabase();
        List<CityListItem> list = new ArrayList<>();
        try {
            Cursor cursor = mSdb.rawQuery("select * from province", (String[]) null);
            cursor.moveToFirst();
            while (!cursor.isLast()) {
                String code = cursor.getString(cursor.getColumnIndex("code"));
                String name = new String(cursor.getBlob(2), CPushMessageCodec.GBK);
                CityListItem cityListItem = new CityListItem();
                cityListItem.setName(name.trim());
                cityListItem.setPcode(code.trim());
                list.add(cityListItem);
                cursor.moveToNext();
            }
            String code2 = cursor.getString(cursor.getColumnIndex("code"));
            String name2 = new String(cursor.getBlob(2), CPushMessageCodec.GBK);
            CityListItem cityListItem2 = new CityListItem();
            cityListItem2.setName(name2.trim());
            cityListItem2.setPcode(code2.trim());
            list.add(cityListItem2);
            if (list != null) {
                this.list_province = list;
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        mDbManager.closeDatabase();
        mSdb.close();
        this.mSpinnerProvince.setOnItemSelectedListener(new SpinnerOnSelectedListener1());
        this.mSpinnerProvince.setAdapter(new RegisterPhoneCityAdapter(this, list));
        int k = list.size();
        for (int i = 0; i < k; i++) {
            String _province = this.sp.getString(SaveHospitalUtils.spProvince + this.mLoginUserDao.mUID, SaveHospitalUtils.getDefaultProvince());
            Log.e("获取当前的省份", _province);
            if (_province.equals(list.get(i).getName().trim())) {
                this.mSpinnerProvince.setSelection(i, true);
            }
        }
    }

    private void spinnerCity(String pcode) {
        RegisterPhoneCityDBManager mDbManager = new RegisterPhoneCityDBManager(this);
        mDbManager.openDatabase();
        SQLiteDatabase mSdb = mDbManager.getmDatabase();
        List<CityListItem> list = new ArrayList<>();
        try {
            Cursor cursor = mSdb.rawQuery("select * from city where pcode='" + pcode + "'", (String[]) null);
            cursor.moveToFirst();
            while (!cursor.isLast()) {
                String code = cursor.getString(cursor.getColumnIndex("code"));
                String name = new String(cursor.getBlob(2), CPushMessageCodec.GBK);
                CityListItem cityListItem = new CityListItem();
                cityListItem.setName(name.trim());
                cityListItem.setPcode(code.trim());
                list.add(cityListItem);
                cursor.moveToNext();
            }
            String code2 = cursor.getString(cursor.getColumnIndex("code"));
            String name2 = new String(cursor.getBlob(2), CPushMessageCodec.GBK);
            CityListItem cityListItem2 = new CityListItem();
            cityListItem2.setName(name2.trim());
            cityListItem2.setPcode(code2.trim());
            list.add(cityListItem2);
            if (list != null) {
                this.list_city = list;
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        mDbManager.closeDatabase();
        mSdb.close();
        this.mSpinnerCity.setOnItemSelectedListener(new SpinnerOnSelectedListener2());
        this.mSpinnerCity.setAdapter(new RegisterPhoneCityAdapter(this, list));
        int k = list.size();
        for (int i = 0; i < k; i++) {
            String _city = this.sp.getString(SaveHospitalUtils.spCity + this.mLoginUserDao.mUID, SaveHospitalUtils.getDefaultCity());
            Log.e("获取当前的城市", _city);
            if (_city.equals(list.get(i).getName().trim())) {
                this.mSpinnerCity.setSelection(i, true);
            }
        }
    }

    private void spinnerDistrict(String pcode) {
        RegisterPhoneCityDBManager mDbManager = new RegisterPhoneCityDBManager(this);
        mDbManager.openDatabase();
        SQLiteDatabase mSdb = mDbManager.getmDatabase();
        List<CityListItem> list = new ArrayList<>();
        try {
            Cursor cursor = mSdb.rawQuery("select * from district where pcode='" + pcode + "'", (String[]) null);
            cursor.moveToFirst();
            while (!cursor.isLast()) {
                String code = cursor.getString(cursor.getColumnIndex("code"));
                String name = new String(cursor.getBlob(2), CPushMessageCodec.GBK);
                CityListItem cityListItem = new CityListItem();
                cityListItem.setName(name.trim());
                cityListItem.setPcode(code.trim());
                cityListItem.setId(new String(cursor.getBlob(1), CPushMessageCodec.UTF8));
                list.add(cityListItem);
                cursor.moveToNext();
            }
            String code2 = cursor.getString(cursor.getColumnIndex("code"));
            String name2 = new String(cursor.getBlob(2), CPushMessageCodec.GBK);
            CityListItem cityListItem2 = new CityListItem();
            cityListItem2.setName(name2.trim());
            cityListItem2.setPcode(code2.trim());
            cityListItem2.setId(new String(cursor.getBlob(1), CPushMessageCodec.UTF8));
            list.add(cityListItem2);
            if (list != null) {
                this.list_district = list;
            }
        } catch (Exception e) {
        }
        mDbManager.closeDatabase();
        mSdb.close();
        this.mSpinnerDistrict.setOnItemSelectedListener(new SpinnerOnSelectedListener3());
        this.mSpinnerDistrict.setAdapter(new RegisterPhoneCityAdapter(this, list));
        int k = list.size();
        for (int i = 0; i < k; i++) {
            String _district = this.sp.getString(SaveHospitalUtils.spDistrict + this.mLoginUserDao.mUID, SaveHospitalUtils.getDefaultDistrict());
            Log.e("获取当前的地区", _district);
            if (_district.equals(list.get(i).getName().trim())) {
                this.mSpinnerDistrict.setSelection(i, true);
            }
        }
    }

    class SpinnerOnSelectedListener1 implements AdapterView.OnItemSelectedListener {
        SpinnerOnSelectedListener1() {
        }

        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
            ChangeHospitalActivity.this.mProvince = ((CityListItem) adapterView.getItemAtPosition(position)).getName();
            ChangeHospitalActivity.this.spinnerCity(((CityListItem) adapterView.getItemAtPosition(position)).getPcode());
        }

        public void onNothingSelected(AdapterView<?> adapterView) {
        }
    }

    class SpinnerOnSelectedListener2 implements AdapterView.OnItemSelectedListener {
        SpinnerOnSelectedListener2() {
        }

        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
            ChangeHospitalActivity.this.mCity = ((CityListItem) adapterView.getItemAtPosition(position)).getName();
            ChangeHospitalActivity.this.spinnerDistrict(((CityListItem) adapterView.getItemAtPosition(position)).getPcode());
        }

        public void onNothingSelected(AdapterView<?> adapterView) {
        }
    }

    class SpinnerOnSelectedListener3 implements AdapterView.OnItemSelectedListener {
        SpinnerOnSelectedListener3() {
        }

        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
            ChangeHospitalActivity.this.mDistrict = ((CityListItem) adapterView.getItemAtPosition(position)).getName();
            String s = ((CityListItem) adapterView.getItemAtPosition(position)).getId().substring(0, 6);
            if (PageUtil.checkNet(ChangeHospitalActivity.this)) {
                ChangeHospitalActivity.this.doPostHospital(s);
            } else {
                new DialogClass(ChangeHospitalActivity.this, ChangeHospitalActivity.this.getResources().getString(R.string.net_disable));
            }
        }

        public void onNothingSelected(AdapterView<?> adapterView) {
        }
    }

    private void doPostHospital(String districtId) {
        Meth_android_getHospital.getHospital(districtId, "00000000000000000000000000000000", new AjaxCallBack_getHospital(getApplicationContext(), this.mhospitalHandler), App_phms.getInstance().mFinalHttp, String.valueOf(Constants.URL) + "/main.php");
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.scan_zxing:
                startActivityForResult(new Intent(this, CaptureActivity.class), 0);
                return;
            case R.id.cancel_save_infor:
                finish();
                return;
            case R.id.modify_hospital_btn:
                modifyUserinfo();
                return;
            default:
                return;
        }
    }

    private void modifyUserinfo() {
        Log.e("修改医院的sessionID", App_phms.getInstance().mUserInfo.mPHPSession);
        Log.e("修改医院的 user名", App_phms.getInstance().mUserInfo.mUserID);
        Log.e("修改医院的密码", App_phms.getInstance().mUserInfo.mPassword);
        if (this.mHospitalBean == null || this.mHospitalBean.equals(bs.b)) {
            Toast.makeText(this, getResources().getString(R.string.nochangehosical), Toast.LENGTH_LONG).show();
        } else if (this.mHospitalBean.getHospitalId() != null && !this.mHospitalBean.getHospitalId().equalsIgnoreCase(bs.b)) {
            SaveHospitalUtils.saveModifiedInfo(this.mLoginUserDao.mUID, this.mProvince, this.mCity, this.mDistrict, this.mHospitalBean.getHospitalName());
            Log.e("修改医院的id", this.mHospitalBean.getHospitalId());
            Log.e("获取当前的医院名字", this.sp.getString(SaveHospitalUtils.spHospitalname + this.mLoginUserDao.mUID));
            Log.e("获取当前的医院名字", App_phms.getInstance().mUserInfo.mPHPSession);
            if (this.mRegistHostipalName.getText().toString() != null && this.mRegistHostipalName.getText().toString() != bs.b && !this.mRegistHostipalName.getText().toString().equalsIgnoreCase(bs.b)) {
                Log.e("要修改医院的id", this.mhospitalId);
                if (this.mhospitalId == null || this.mhospitalId.equalsIgnoreCase(bs.b)) {
                    this.mhospitalId = this.mHospitalBean.getHospitalId();
                }
                if (this.mRegistHostipalName.getText().toString() == "无可选医院" || this.mRegistHostipalName.getText().toString().equalsIgnoreCase("无可选医院")) {
                    Toast.makeText(this, getResources().getString(R.string.nochangehosical), Toast.LENGTH_LONG).show();
                    return;
                }
                Meth_android_modifyUserinfo.modifyuserinfo(App_phms.getInstance().mUserInfo.mUserID, App_phms.getInstance().mUserInfo.mPassword, App_phms.getInstance().mUserInfo.mPHPSession, bs.b, bs.b, bs.b, bs.b, bs.b, bs.b, bs.b, new AjaxCallBack_submitmodfiyUserinfo(this, this.modifyHandler), App_phms.getInstance().mFinalHttp, String.valueOf(Constants.URL) + "/main.php", this.mhospitalId);
            }
        }
    }

    private void loginfaild(String pstr) {
        this.m_dialogClass.dismiss();
        new DialogClass(this, pstr);
    }

    public List<HospitalBean> getListHospitals() {
        return this.listHospitals;
    }

    private void modifyHospition(String hospitionName, String hospitionID, String filePath) {
        LoginUserDao _lud = new LoginUserDao();
        LoginUserDao mLoginUserDao2 = PageUtil.getLoginUserInfo();
        _lud.mID = mLoginUserDao2.mID;
        _lud.mUID = mLoginUserDao2.mUID;
        _lud.mPsw = mLoginUserDao2.mPsw;
        _lud.mPID = mLoginUserDao2.mPID;
        _lud.mUserName = mLoginUserDao2.mUserName;
        _lud.mSex = mLoginUserDao2.mSex;
        _lud.mPhone = mLoginUserDao2.mPhone;
        _lud.mBirthday = mLoginUserDao2.mBirthday;
        _lud.mAddress = mLoginUserDao2.mAddress;
        _lud.mAre = mLoginUserDao2.mAre;
        _lud.mAreID = mLoginUserDao2.mAreID;
        _lud.mCreateDate = mLoginUserDao2.mCreateDate;
        _lud.mSenderId = mLoginUserDao2.mSenderId;
        _lud.mStartDate = mLoginUserDao2.mStartDate;
        _lud.mEndDate = mLoginUserDao2.mEndDate;
        _lud.mCardType = mLoginUserDao2.mCardType;
        _lud.mDiskSpace = mLoginUserDao2.mDiskSpace;
        _lud.mUsed = mLoginUserDao2.mUsed;
        _lud.mTotal = mLoginUserDao2.mTotal;
        _lud.mState = mLoginUserDao2.mState;
        _lud.mHospitalID = hospitionID;
        _lud.mHospitalName = hospitionName;
        _lud.mEthrID = mLoginUserDao2.mEthrID;
        _lud.mTransType = mLoginUserDao2.mTransType;
        _lud.mHGroupID = mLoginUserDao2.mHGroupID;
        _lud.mHGroupName = mLoginUserDao2.mHGroupName;
        _lud.mSID = mLoginUserDao2.mSID;
        _lud.mAnotherLoginInfo = mLoginUserDao2.mAnotherLoginInfo;
        _lud.mDateTime = mLoginUserDao2.mDateTime;
        _lud.mHeight = mLoginUserDao2.mHeight;
        _lud.mWeight = mLoginUserDao2.mWeight;
        _lud.mSportTargetCal = mLoginUserDao2.mSportTargetCal;
        try {
            App_phms.getInstance().mHelper.getLoginUserDao().deleteBuilder().delete();
            App_phms.getInstance().mHelper.getLoginUserDao().create(_lud);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0 && resultCode == -1) {
            String[] strs = data.getStringExtra(PluseDataDao.RESULT).split("_");
            if (strs.length == 3) {
                this.mhospitalName = strs[2];
                this.mhospitalId = strs[1];
                this.mRegistHostipalName.setText(this.mhospitalName);
                return;
            }
            Toast.makeText(getApplicationContext(), "该二维码不是医院名称，请重新选择二维码进行扫描", Toast.LENGTH_SHORT).show();
        }
    }

    protected void onResume() {
        super.onResume();
    }

    protected void onStop() {
        super.onStop();
        if (this.m_dialogClass != null && !this.m_dialogClass.equals(bs.b)) {
            this.m_dialogClass.dismiss();
        }
    }

    public void onItemClick(AdapterView<?> adapterView, View arg1, int arg2, long arg3) {
    }
}
