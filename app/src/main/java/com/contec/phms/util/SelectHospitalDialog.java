package com.contec.phms.util;

import android.app.Dialog;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.alibaba.cchannel.push.receiver.CPushMessageCodec;
import com.contec.phms.App_phms;
import com.contec.phms.R;
import com.contec.phms.activity.HospitalAdapter;
import com.contec.phms.activity.RegisterPhoneCityAdapter;
import com.contec.phms.ajaxcallback.AjaxCallBack_getHospital;
import com.contec.phms.domain.CityListItem;
import com.contec.phms.domain.HospitalBean;
import com.contec.phms.db.RegisterPhoneCityDBManager;
import com.contec.phms.widget.DialogClass;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import cn.com.contec_net_3_android.Meth_android_getHospital;
import u.aly.bs;

public class SelectHospitalDialog extends Dialog {
    private HospitalBean _Bean;
    private String city = null;
    private String district = null;
    private String districtId = null;
    private String hospitalName;
    private List<HospitalBean> listHospital;
    private LinearLayout ll_hospital;
    private LinearLayout ll_linkage;
    private Button mBtn;
    private boolean mCheckHaigang = true;
    private Context mContext;
    private RegisterPhoneCityDBManager mDbManager;
    Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Constants.REQUEST_HOSPITAL_SUCCESS:
                    SelectHospitalDialog.this.listHospital = ParseXmlService.readXML(msg.getData().getString("content"));
                    Log.e("listHospitalSize>>>>", new StringBuilder().append(SelectHospitalDialog.this.listHospital.size()).toString());
                    if (SelectHospitalDialog.this.listHospital != null && SelectHospitalDialog.this.listHospital.size() > 0) {
                        if (SelectHospitalDialog.this.mCheckHaigang) {
                            boolean isHaveContec = false;
                            for (int i = 0; i < SelectHospitalDialog.this.listHospital.size(); i++) {
                                if ("康泰健康管理医院".equals(((HospitalBean) SelectHospitalDialog.this.listHospital.get(i)).getHospitalName().trim())) {
                                    isHaveContec = true;
                                }
                            }
                            if (!isHaveContec) {
                                SelectHospitalDialog.this._Bean = new HospitalBean();
                                SelectHospitalDialog.this._Bean.setHospitalId("H1303230271");
                                SelectHospitalDialog.this._Bean.setHospitalName("康泰健康管理医院");
                                SelectHospitalDialog.this.listHospital.add(SelectHospitalDialog.this._Bean);
                            }
                        }
                        SelectHospitalDialog.this.mSpinnerHospital.setAdapter(new HospitalAdapter(SelectHospitalDialog.this.mContext.getApplicationContext(), SelectHospitalDialog.this.listHospital, true));
                        int k = SelectHospitalDialog.this.listHospital.size();
                        for (int i2 = 0; i2 < k; i2++) {
                            if ("康泰健康管理医院".equals(((HospitalBean) SelectHospitalDialog.this.listHospital.get(i2)).getHospitalName().trim())) {
                                SelectHospitalDialog.this.mSpinnerHospital.setSelection(i2, true);
                                SelectHospitalDialog.this.mSpinnerHospital.setEnabled(true);
                                SelectHospitalDialog.this.mRegistHostipalName.setText("康泰健康管理医院");
                                SelectHospitalDialog.this.mTvContec.setVisibility(View.GONE);
                                SelectHospitalDialog.this.mSpinnerHospital.setVisibility(View.VISIBLE);
                            }
                        }
                        return;
                    } else if (SelectHospitalDialog.this.mCheckHaigang) {
                        SelectHospitalDialog.this._Bean = new HospitalBean();
                        SelectHospitalDialog.this._Bean.setHospitalId("H1303230271");
                        SelectHospitalDialog.this._Bean.setHospitalName("康泰健康管理医院");
                        SelectHospitalDialog.this.listHospital.add(SelectHospitalDialog.this._Bean);
                        SelectHospitalDialog.this.mSpinnerHospital.setAdapter(new HospitalAdapter(SelectHospitalDialog.this.mContext.getApplicationContext(), SelectHospitalDialog.this.listHospital, false));
                        SelectHospitalDialog.this.mSpinnerHospital.setEnabled(false);
                        SelectHospitalDialog.this.mRegistHostipalName.setText(SelectHospitalDialog.this._Bean.getHospitalName());
                        return;
                    } else {
                        Toast toast = Toast.makeText(SelectHospitalDialog.this.mContext, R.string.str_no_response, Toast.LENGTH_LONG);
                        toast.setGravity(17, 0, 0);
                        toast.show();
                        SelectHospitalDialog.this._Bean = new HospitalBean();
                        SelectHospitalDialog.this._Bean.setHospitalId(bs.b);
                        SelectHospitalDialog.this._Bean.setHospitalName("无可选医院");
                        SelectHospitalDialog.this.listHospital.add(SelectHospitalDialog.this._Bean);
                        SelectHospitalDialog.this.mSpinnerHospital.setAdapter(new HospitalAdapter(SelectHospitalDialog.this.mContext.getApplicationContext(), SelectHospitalDialog.this.listHospital, false));
                        SelectHospitalDialog.this.mSpinnerHospital.setEnabled(false);
                        SelectHospitalDialog.this.mRegistHostipalName.setText(bs.b);
                        return;
                    }
                case Constants.REQUEST_HOSPITAL_FAILED:
                    new DialogClass(SelectHospitalDialog.this.mContext, SelectHospitalDialog.this.mContext.getResources().getString(R.string.user_parameter_error));
                    return;
                case Constants.REQUEST_HOSPITAL_DB_FAILED:
                    new DialogClass(SelectHospitalDialog.this.mContext, SelectHospitalDialog.this.mContext.getResources().getString(R.string.user_DB_error));
                    return;
                case Constants.HOSPITAL_FAILED:
                    new DialogClass(SelectHospitalDialog.this.mContext, SelectHospitalDialog.this.mContext.getResources().getString(R.string.user_networkerror));
                    return;
                default:
                    return;
            }
        }
    };
    private TextView mRegistHostipalName;
    private SQLiteDatabase mSdb;
    private Spinner mSpinnerCity;
    private Spinner mSpinnerDistrict;
    public Spinner mSpinnerHospital;
    private Spinner mSpinnerProvince;
    private TextView mTvContec;
    private String province = null;

    public SelectHospitalDialog(Context context, int theme, TextView mRegistHostipalName2) {
        super(context, theme);
        this.mContext = context;
        this.mRegistHostipalName = mRegistHostipalName2;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.selecthosiptaldialog);
        initView();
    }

    private void initView() {
        this.mSpinnerProvince = (Spinner) findViewById(R.id.spinner_province_zc);
        this.mSpinnerCity = (Spinner) findViewById(R.id.spinner_city_zc);
        this.mSpinnerDistrict = (Spinner) findViewById(R.id.spinner_district_zc);
        this.mSpinnerHospital = (Spinner) findViewById(R.id.spinner_hospital_zc);
        this.mTvContec = (TextView) findViewById(R.id.tv_contec);
        this.mTvContec.setVisibility(View.VISIBLE);
        this.mSpinnerHospital.setVisibility(View.GONE);
        this.ll_linkage = (LinearLayout) findViewById(R.id.ll_linkage);
        this.ll_hospital = (LinearLayout) findViewById(R.id.ll_hospital);
        this.mBtn = (Button) findViewById(R.id.comfirm_btn);
        this.mBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                HospitalBean hospital = (HospitalBean) SelectHospitalDialog.this.mSpinnerHospital.getSelectedItem();
                if (hospital != null) {
                    SelectHospitalDialog.this.hospitalName = hospital.getHospitalName();
                    if (!SelectHospitalDialog.this.hospitalName.equalsIgnoreCase("无可选医院")) {
                        SelectHospitalDialog.this.mRegistHostipalName.setText(SelectHospitalDialog.this.hospitalName);
                    }
                }
                SelectHospitalDialog.this.dismiss();
            }
        });
        spinnerProvince();
    }

    private void spinnerProvince() {
        this.mDbManager = new RegisterPhoneCityDBManager(this.mContext);
        this.mDbManager.openDatabase();
        this.mSdb = this.mDbManager.getmDatabase();
        List<CityListItem> list = new ArrayList<>();
        try {
            Cursor cursor = this.mSdb.rawQuery("select * from province", (String[]) null);
            cursor.moveToFirst();
            while (!cursor.isLast()) {
                String code = cursor.getString(cursor.getColumnIndex("code"));
                String name = new String(cursor.getBlob(2), CPushMessageCodec.GBK);
                CityListItem cityListItem = new CityListItem();
                cityListItem.setName(name);
                cityListItem.setPcode(code);
                list.add(cityListItem);
                cursor.moveToNext();
            }
            String code2 = cursor.getString(cursor.getColumnIndex("code"));
            String name2 = new String(cursor.getBlob(2), CPushMessageCodec.GBK);
            CityListItem cityListItem2 = new CityListItem();
            cityListItem2.setName(name2);
            cityListItem2.setPcode(code2);
            list.add(cityListItem2);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        this.mDbManager.closeDatabase();
        this.mSdb.close();
        this.mSpinnerProvince.setOnItemSelectedListener(new SpinnerOnSelectedListener1());
        this.mSpinnerProvince.setAdapter(new RegisterPhoneCityAdapter(this.mContext, list));
        int k = list.size();
        for (int i = 0; i < k; i++) {
            if ("河北省".equals(list.get(i).getName().trim())) {
                this.mSpinnerProvince.setSelection(i, true);
            }
        }
    }

    private void spinnerCity(String pcode) {
        this.mDbManager = new RegisterPhoneCityDBManager(this.mContext);
        this.mDbManager.openDatabase();
        this.mSdb = this.mDbManager.getmDatabase();
        List<CityListItem> list = new ArrayList<>();
        try {
            Cursor cursor = this.mSdb.rawQuery("select * from city where pcode='" + pcode + "'", (String[]) null);
            cursor.moveToFirst();
            while (!cursor.isLast()) {
                String code = cursor.getString(cursor.getColumnIndex("code"));
                String name = new String(cursor.getBlob(2), CPushMessageCodec.GBK);
                CityListItem cityListItem = new CityListItem();
                cityListItem.setName(name);
                cityListItem.setPcode(code);
                list.add(cityListItem);
                cursor.moveToNext();
            }
            String code2 = cursor.getString(cursor.getColumnIndex("code"));
            String name2 = new String(cursor.getBlob(2), CPushMessageCodec.GBK);
            CityListItem cityListItem2 = new CityListItem();
            cityListItem2.setName(name2);
            cityListItem2.setPcode(code2);
            list.add(cityListItem2);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        this.mDbManager.closeDatabase();
        this.mSdb.close();
        this.mSpinnerCity.setOnItemSelectedListener(new SpinnerOnSelectedListener2());
        this.mSpinnerCity.setAdapter(new RegisterPhoneCityAdapter(this.mContext, list));
        int k = list.size();
        for (int i = 0; i < k; i++) {
            if ("秦皇岛市".equals(list.get(i).getName().trim())) {
                this.mSpinnerCity.setSelection(i, true);
            }
        }
    }

    private void spinnerDistrict(String pcode) {
        this.mDbManager = new RegisterPhoneCityDBManager(this.mContext);
        this.mDbManager.openDatabase();
        this.mSdb = this.mDbManager.getmDatabase();
        List<CityListItem> list = new ArrayList<>();
        try {
            Cursor cursor = this.mSdb.rawQuery("select * from district where pcode='" + pcode + "'", (String[]) null);
            cursor.moveToFirst();
            while (!cursor.isLast()) {
                String code = cursor.getString(cursor.getColumnIndex("code"));
                String name = new String(cursor.getBlob(2), CPushMessageCodec.GBK);
                CityListItem cityListItem = new CityListItem();
                cityListItem.setName(name);
                cityListItem.setPcode(code);
                cityListItem.setId(new String(cursor.getBlob(1), CPushMessageCodec.UTF8));
                list.add(cityListItem);
                cursor.moveToNext();
            }
            String code2 = cursor.getString(cursor.getColumnIndex("code"));
            String name2 = new String(cursor.getBlob(2), CPushMessageCodec.GBK);
            CityListItem cityListItem2 = new CityListItem();
            cityListItem2.setName(name2);
            cityListItem2.setPcode(code2);
            cityListItem2.setId(new String(cursor.getBlob(1), CPushMessageCodec.UTF8));
            list.add(cityListItem2);
        } catch (Exception e) {
        }
        this.mDbManager.closeDatabase();
        this.mSdb.close();
        this.mSpinnerDistrict.setOnItemSelectedListener(new SpinnerOnSelectedListener3());
        this.mSpinnerDistrict.setAdapter(new RegisterPhoneCityAdapter(this.mContext, list));
        int k = list.size();
        for (int i = 0; i < k; i++) {
            if ("市辖区".equals(list.get(i).getName().trim())) {
                this.mSpinnerDistrict.setSelection(i, true);
            }
        }
    }

    class SpinnerOnSelectedListener1 implements AdapterView.OnItemSelectedListener {
        SpinnerOnSelectedListener1() {
        }

        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
            SelectHospitalDialog.this.province = ((CityListItem) adapterView.getItemAtPosition(position)).getName();
            String pcode = ((CityListItem) adapterView.getItemAtPosition(position)).getPcode();
            SelectHospitalDialog.this.spinnerCity(pcode);
            SelectHospitalDialog.this.spinnerDistrict(pcode);
        }

        public void onNothingSelected(AdapterView<?> adapterView) {
        }
    }

    class SpinnerOnSelectedListener2 implements AdapterView.OnItemSelectedListener {
        SpinnerOnSelectedListener2() {
        }

        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
            SelectHospitalDialog.this.city = ((CityListItem) adapterView.getItemAtPosition(position)).getName();
            SelectHospitalDialog.this.spinnerDistrict(((CityListItem) adapterView.getItemAtPosition(position)).getPcode());
        }

        public void onNothingSelected(AdapterView<?> adapterView) {
        }
    }

    class SpinnerOnSelectedListener3 implements AdapterView.OnItemSelectedListener {
        SpinnerOnSelectedListener3() {
        }

        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
            SelectHospitalDialog.this.district = ((CityListItem) adapterView.getItemAtPosition(position)).getName();
            SelectHospitalDialog.this.districtId = ((CityListItem) adapterView.getItemAtPosition(position)).getId();
            String s = SelectHospitalDialog.this.districtId.substring(0, 6);
            if (s.equalsIgnoreCase("130302")) {
                SelectHospitalDialog.this.mCheckHaigang = true;
            } else {
                SelectHospitalDialog.this.mCheckHaigang = false;
            }
            SelectHospitalDialog.this.doPostHospital(s);
        }

        public void onNothingSelected(AdapterView<?> adapterView) {
        }
    }

    private void doPostHospital(String districtId2) {
        Meth_android_getHospital.getHospital(districtId2, "00000000000000000000000000000000", new AjaxCallBack_getHospital(this.mContext.getApplicationContext(), this.mHandler), App_phms.getInstance().mFinalHttp, String.valueOf(Constants.URL) + "/main.php");
    }
}
