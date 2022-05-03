package com.contec.phms.util;

import android.util.Log;
import com.contec.phms.App_phms;

public class SaveHospitalUtils {
    private static final String mCity = "秦皇岛市";
    private static final String mDistrict = "市辖区";
    private static final String mHospitalName = "康泰健康管理医院";
    private static final String mProvince = "河北省";
    public static final String spCity = "city";
    public static final String spDistrict = "district";
    public static final String spHospitalname = "hospitalname";
    public static final String spProvince = "province";

    public static void saveModifiedInfo(String userId, String mProvince2, String mCity2, String mDistrict2, String mHospitalName2) {
        PhmsSharedPreferences sp = PhmsSharedPreferences.getInstance(App_phms.getInstance());
        sp.saveColume(spProvince + userId, mProvince2);
        sp.saveColume(spCity + userId, mCity2);
        sp.saveColume(spDistrict + userId, mDistrict2);
        sp.saveColume(spHospitalname + userId, mHospitalName2);
    }

    public static void saveDeafultHospitalInfo(String userId) {
        Log.e("jxx", "执行testDeafult method");
        PhmsSharedPreferences sp = PhmsSharedPreferences.getInstance(App_phms.getInstance());
        sp.saveColume(spProvince + userId, mProvince);
        sp.saveColume(spCity + userId, mCity);
        sp.saveColume(spDistrict + userId, mDistrict);
        sp.saveColume(spHospitalname + userId, mHospitalName);
    }

    public static String getDefaultProvince() {
        return mProvince;
    }

    public static String getDefaultCity() {
        return mCity;
    }

    public static String getDefaultDistrict() {
        return mDistrict;
    }

    public static String getDefaultHospitalName() {
        return mHospitalName;
    }
}
