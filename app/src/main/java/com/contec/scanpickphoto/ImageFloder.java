package com.contec.scanpickphoto;

import org.apache.commons.httpclient.cookie.CookieSpec;

public class ImageFloder {
    private int count;
    private String dir;
    private String firstImagePath;
    private Boolean flag;
    private String name;

    public String getDir() {
        return this.dir;
    }

    public void setDir(String dir2) {
        this.dir = dir2;
        String s = this.dir.substring(this.dir.lastIndexOf(CookieSpec.PATH_DELIM));
        this.name = s.substring(1, s.length());
    }

    public String getFirstImagePath() {
        return this.firstImagePath;
    }

    public void setFirstImagePath(String firstImagePath2) {
        this.firstImagePath = firstImagePath2;
    }

    public String getName() {
        return this.name;
    }

    public int getCount() {
        return this.count;
    }

    public void setCount(int count2) {
        this.count = count2;
    }

    public Boolean getFlag() {
        return this.flag;
    }

    public void setFlag(Boolean flag2) {
        this.flag = flag2;
    }
}
