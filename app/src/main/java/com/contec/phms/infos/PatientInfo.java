package com.contec.phms.infos;

import u.aly.bs;

public class PatientInfo {
    public int bPace;
    public String dtDateTime;
    public String dtUpload;
    public int nAge;
    public int nHeight;
    public int nSex;
    public int nWeight;
    public String strAgoMedical;
    public String strBedID;
    public String strCaseID;
    public String strID;
    public String strMedical;
    public String strName;
    public String strNowMedical;
    public String strOwnerTell;
    public String strPart;
    public String strPeople;
    public String strPersonId;
    public String strTel;
    public String strText;
    public String strUploader;
    public String strUseMedicineInfo;

    public PatientInfo() {
        initInfo();
    }

    public void initInfo() {
        this.strID = "000000";
        this.strUploader = bs.b;
        this.dtUpload = bs.b;
        this.strName = "kitty";
        this.strPersonId = bs.b;
        this.nSex = 1;
        this.nAge = 3;
        this.nHeight = 3;
        this.nWeight = 2;
        this.bPace = 1;
        this.dtDateTime = bs.b;
        this.strCaseID = "ggg";
        this.strPart = "part";
        this.strBedID = "bedid";
        this.strText = "text";
        this.strPeople = "people";
        this.strTel = "strtel110";
        this.strOwnerTell = "ownertell120";
        this.strAgoMedical = "deadedagomedical";
        this.strMedical = "hellomedical";
        this.strUseMedicineInfo = "useMedicineInfo";
    }
}
