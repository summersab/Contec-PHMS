package com.contec.phms.widget;

import java.io.Serializable;
import java.util.HashMap;

public class TransUserinfo implements Serializable {
    public HashMap<String, String> mapuserinfo = new HashMap<>();

    public HashMap<String, String> getMapuserinfo() {
        return this.mapuserinfo;
    }

    public void setMapuserinfo(HashMap<String, String> mapuserinfo2) {
        this.mapuserinfo = mapuserinfo2;
    }

    public TransUserinfo() {
    }

    public TransUserinfo(HashMap<String, String> _hashmap) {
        this.mapuserinfo = _hashmap;
    }
}
