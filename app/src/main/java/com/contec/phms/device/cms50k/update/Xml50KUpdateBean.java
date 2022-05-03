package com.contec.phms.device.cms50k.update;

import java.io.Serializable;

public class Xml50KUpdateBean implements Serializable {
    private static final long serialVersionUID = 1;
    public String description;
    public String fname;
    public String md5;
    public String path;
    public String size;
    public String type;
    public String typecode;
    public String uploaddate;
    public String version;
    private String watchVersion;

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description2) {
        this.description = description2;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type2) {
        this.type = type2;
    }

    public String getFname() {
        return this.fname;
    }

    public void setFname(String fname2) {
        this.fname = fname2;
    }

    public String getMd5() {
        return this.md5;
    }

    public void setMd5(String md52) {
        this.md5 = md52;
    }

    public String getPath() {
        return this.path;
    }

    public void setPath(String path2) {
        this.path = path2;
    }

    public String getSize() {
        return this.size;
    }

    public void setSize(String size2) {
        this.size = size2;
    }

    public String getTypecode() {
        return this.typecode;
    }

    public void setTypecode(String typecode2) {
        this.typecode = typecode2;
    }

    public String getVersion() {
        return this.version;
    }

    public void setVersion(String version2) {
        this.version = version2;
    }

    public String getUploaddate() {
        return this.uploaddate;
    }

    public void setUploaddate(String uploaddate2) {
        this.uploaddate = uploaddate2;
    }

    public String getWatchVersion() {
        return this.watchVersion;
    }

    public void setWatchVersion(String watchVersion2) {
        this.watchVersion = watchVersion2;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }
}
