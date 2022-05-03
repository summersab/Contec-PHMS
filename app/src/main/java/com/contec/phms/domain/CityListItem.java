package com.contec.phms.domain;

public class CityListItem {
    private String id;
    private String name;
    private String pcode;

    public String getId() {
        return this.id;
    }

    public void setId(String byteId) {
        this.id = byteId;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name2) {
        this.name = name2;
    }

    public String getPcode() {
        return this.pcode;
    }

    public void setPcode(String pcode2) {
        this.pcode = pcode2;
    }
}
