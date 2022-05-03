package com.contec.sp10.code;

public class PatientInfo {
    int bSmoke = 0;
    int nAge = 25;
    int nGender = 0;
    int nHeight = 170;
    int nNation = 0;
    int nStandard = 0;
    int nWeight = 70;

    public void setGender(int n) {
        this.nGender = n;
    }

    public void setAge(int n) {
        this.nAge = n;
    }

    public void setHeight(int n) {
        this.nHeight = n;
    }

    public void setWeight(int n) {
        this.nWeight = n;
    }

    public void setNation(int n) {
        this.nNation = n;
    }

    public void setSmoke(int n) {
        this.bSmoke = n;
    }

    public void setStandard(int n) {
        this.nStandard = n;
    }
}
