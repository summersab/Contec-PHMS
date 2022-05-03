package com.contec.phms.upload.cases.common;

import java.io.Serializable;

public class NEW_CASE implements Serializable {
    public static final int TYPE_CMSSXT = 7;
    public static final int TYPE_ECG = 1;
    public static final int TYPE_NIBP = 2;
    public static final int TYPE_SP10W = 8;
    public static final int TYPE_SPIR = 5;
    public static final int TYPE_SPO2 = 3;
    public static final int TYPE_VESD = 6;
    private static final long serialVersionUID = 1;
    private String caseName;
    private int caseType;
    private int dataPosition;
    private String strBaseInfoFileName;
    private String strBaseInfoPhotoName;
    private String strCasePath;
    private String strId;
    private String strName;
    private String strPaceTime;

    public NEW_CASE(String paceTime, String patientName, String strId2, String strCasePath2, String strBaseInfoFileName2, String strBaseInfoPhotoName2, int dataPosition2) {
        this.strPaceTime = paceTime;
        this.strName = patientName;
        this.strId = strId2;
        this.strCasePath = strCasePath2;
        this.strBaseInfoFileName = strBaseInfoFileName2;
        this.strBaseInfoPhotoName = strBaseInfoPhotoName2;
        this.dataPosition = dataPosition2;
    }

    public String getCaseName() {
        return this.caseName;
    }

    public void setCaseName(String caseName2) {
        this.caseName = caseName2;
    }

    public int getCaseType() {
        return this.caseType;
    }

    public void setCaseType(int caseType2) {
        this.caseType = caseType2;
    }

    public int getDataPosition() {
        return this.dataPosition;
    }

    public void setDataPosition(int dataPosition2) {
        this.dataPosition = dataPosition2;
    }

    public String getStrName() {
        return this.strName;
    }

    public void setStrName(String strName2) {
        this.strName = strName2;
    }

    public String getStrPaceTime() {
        return this.strPaceTime;
    }

    public void setStrPaceTime(String strPaceTime2) {
        this.strPaceTime = strPaceTime2;
    }

    public String getStrId() {
        return this.strId;
    }

    public void setStrId(String strId2) {
        this.strId = strId2;
    }

    public String getStrBaseInfoFileName() {
        return this.strBaseInfoFileName;
    }

    public void setStrBaseInfoFileName(String strBaseInfoFileName2) {
        this.strBaseInfoFileName = strBaseInfoFileName2;
    }

    public String getStrBaseInfoPhotoName() {
        return this.strBaseInfoPhotoName;
    }

    public void setStrBaseInfoPhotoName(String strBaseInfoPhotoName2) {
        this.strBaseInfoPhotoName = strBaseInfoPhotoName2;
    }

    public String getStrCasePath() {
        return this.strCasePath;
    }

    public void setStrCasePath(String strCasePath2) {
        this.strCasePath = strCasePath2;
    }
}
