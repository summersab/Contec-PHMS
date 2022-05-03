package com.contec.phms.SearchDevice;

import com.contec.phms.App_phms;
import com.contec.phms.util.Constants;
import com.contec.phms.util.DeviceNameUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import u.aly.bs;

public class ContecDevices {
    private static final App_phms MAPP_PHMS = App_phms.getInstance();
    private static List<String> mContecDevice;

    private static void init() {
        mContecDevice = new ArrayList();
        mContecDevice.add("WT");
        mContecDevice.add("CMSSXT");
        mContecDevice.add("CMSVESD");
        mContecDevice.add("SP10W");
        mContecDevice.add(Constants.CMS50EW);
        mContecDevice.add("CMS50IW");
        mContecDevice.add("CMS50D");
        mContecDevice.add("ABPM50W");
        mContecDevice.add("PM50");
        mContecDevice.add("CONTEC08AW");
        mContecDevice.add("CONTEC08C");
        mContecDevice.add(Constants.PM85_NAME);
        mContecDevice.add("FHR01");
        mContecDevice.add("BC01");
        mContecDevice.add("TEMP01");
        mContecDevice.add(DeviceNameUtils.TEMP03.toUpperCase(Locale.US));
        mContecDevice.add(Constants.PM10_NAME);
        mContecDevice.add("CMS50F");
        mContecDevice.add(Constants.CMS50K_NAME);
        mContecDevice.add(Constants.CMS50K1_NAME);
        mContecDevice.add(Constants.DEVICE_8000GW_NAME);
        MAPP_PHMS.mContecDevice = mContecDevice;
        mContecDevice = null;
    }

    public static String checkDevice(String deviceName) {
        String _name = transName(deviceName);
        init();
        return MAPP_PHMS.mContecDevice.contains(_name) ? _name : bs.b;
    }

    private static String transName(String deviceName) {
        String _temp = deviceName;
        if (deviceName != null && deviceName.contains(Constants.DEVICE_8000GW_NAME)) {
            _temp = Constants.DEVICE_8000GW_NAME;
        }
        if (deviceName != null && deviceName.contains("SpO201")) {
            _temp = Constants.CMS50EW;
        }
        if (deviceName != null && deviceName.contains("SpO206")) {
            _temp = "CMS50IW";
        }
        if (deviceName != null && deviceName.contains("SpO202")) {
            _temp = "CMS50F";
        }
        if (deviceName != null && deviceName.contains("SpO209")) {
            _temp = Constants.CMS50K_NAME;
        }
        if (deviceName != null && deviceName.contains("SpO210")) {
            _temp = Constants.CMS50K1_NAME;
        }
        if (deviceName != null && deviceName.contains("BG01")) {
            _temp = "CMSSXT";
        }
        if (deviceName != null && deviceName.contains("WT01")) {
            _temp = "WT";
        }
        if ((deviceName != null && deviceName.contains("NIBP03")) || deviceName.contains("NIBP06")) {
            _temp = "CONTEC08AW";
        }
        if (deviceName != null && deviceName.contains("NIBP04")) {
            _temp = "CONTEC08C";
        }
        if (deviceName != null && (deviceName.contains("NIBP01") || deviceName.contains("NIBP02"))) {
            _temp = "ABPM50W";
        }
        if (deviceName != null && deviceName.contains("NIBP05")) {
            _temp = "PM50";
        }
        if (deviceName != null && (deviceName.contains("PULMO01") || deviceName.contains("PULMO02"))) {
            _temp = "SP10W";
        }
        if (deviceName != null && deviceName.contains("PM03")) {
            _temp = Constants.PM85_NAME;
        }
        if (deviceName != null && deviceName.contains("FHR01")) {
            _temp = "FHR01";
        }
        if (deviceName != null && deviceName.contains("SpO208")) {
            _temp = "CMS50D";
        }
        if (deviceName != null && deviceName.contains("BC01")) {
            _temp = "BC01";
        }
        if (deviceName != null && deviceName.contains("TEMP01")) {
            _temp = "TEMP01";
        }
        if (deviceName != null && deviceName.contains(Constants.PM10_NAME)) {
            _temp = Constants.PM10_NAME;
        }
        if (deviceName != null && deviceName.contains(Constants.BLE_50EW)) {
            _temp = Constants.CMS50EW;
        }
        if (deviceName != null && deviceName.contains(Constants.BLE_PM10)) {
            _temp = Constants.PM10_NAME;
        }
        if (deviceName == null || !deviceName.contains(DeviceNameUtils.TEMP03.toUpperCase(Locale.US))) {
            return _temp;
        }
        return DeviceNameUtils.TEMP03.toUpperCase(Locale.US);
    }
}
