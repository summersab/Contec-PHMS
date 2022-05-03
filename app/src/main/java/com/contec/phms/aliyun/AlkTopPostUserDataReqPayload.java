package com.contec.phms.aliyun;

public class AlkTopPostUserDataReqPayload {
    public AlkTopUserData[] data_list;
    public String device_id;
    public String device_model;
    public String vendor_user_id;

    public String getVendor_user_id() {
        return this.vendor_user_id;
    }

    public void setVendor_user_id(String vendor_user_id2) {
        this.vendor_user_id = vendor_user_id2;
    }

    public String getDevice_model() {
        return this.device_model;
    }

    public void setDevice_model(String device_model2) {
        this.device_model = device_model2;
    }

    public AlkTopUserData[] getData_list() {
        return this.data_list;
    }

    public void setData_list(AlkTopUserData[] data_list2) {
        this.data_list = data_list2;
    }

    public String getDevice_id() {
        return this.device_id;
    }

    public void setDevice_id(String device_id2) {
        this.device_id = device_id2;
    }
}
