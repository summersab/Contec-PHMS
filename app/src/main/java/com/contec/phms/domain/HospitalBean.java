package com.contec.phms.domain;

public class HospitalBean {
    private String mCity;
    private String mDistrict;
    private String mHospitalId;
    private String mHospitalName;
    private String mProvince;

    public HospitalBean() {
    }

    public String getProvince() {
        return this.mProvince;
    }

    public void setProvince(String Province) {
        this.mProvince = Province;
    }

    public String getCity() {
        return this.mCity;
    }

    public void setCity(String City) {
        this.mCity = City;
    }

    public HospitalBean(String hospitalProvince, String hospitalCity, String hospitalDistrict) {
        this.mProvince = hospitalProvince;
        this.mCity = hospitalCity;
        this.mDistrict = hospitalDistrict;
    }

    public String getDistrict() {
        return this.mDistrict;
    }

    public void setDistrict(String District) {
        this.mDistrict = District;
    }

    public HospitalBean(String hospitalId, String hospitalName) {
        this.mHospitalId = hospitalId;
        this.mHospitalName = hospitalName;
    }

    public String getHospitalId() {
        return this.mHospitalId;
    }

    public void setHospitalId(String hospitalId) {
        this.mHospitalId = hospitalId;
    }

    public String getHospitalName() {
        return this.mHospitalName;
    }

    public void setHospitalName(String hospitalName) {
        this.mHospitalName = hospitalName;
    }
}
