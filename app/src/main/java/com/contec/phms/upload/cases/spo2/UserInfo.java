package com.contec.phms.upload.cases.spo2;

import java.io.OutputStream;
import java.io.Serializable;
import u.aly.bs;

public class UserInfo implements Serializable {
    private static final long serialVersionUID = 1;
    private long m_age;
    private String m_chComment = bs.b;
    private String m_chName;
    private String m_chNationality = bs.b;
    private long m_height;
    private long m_sex;
    private long m_size = 856;
    private long m_version = serialVersionUID;
    private long m_weight;

    public long Size() {
        return this.m_size;
    }

    public void Size(long v) {
        this.m_size = v;
    }

    public long Version() {
        return this.m_version;
    }

    void Version(long v) {
        this.m_version = v;
    }

    public long Height() {
        return this.m_height;
    }

    public void Height(long v) {
        this.m_height = v;
    }

    public long Weight() {
        return this.m_weight;
    }

    public void Weight(long v) {
        this.m_weight = v;
    }

    public long Age() {
        return this.m_age;
    }

    public void Age(long v) {
        this.m_age = v;
    }

    public long Sex() {
        return this.m_sex;
    }

    public void Sex(long v) {
        this.m_sex = v;
    }

    public String Name() {
        return this.m_chName;
    }

    public void Name(String name) {
        this.m_chName = name;
    }

    public String Comment() {
        return this.m_chComment;
    }

    public void Comment(String comment) {
        this.m_chComment = comment;
    }

    public String Nationa() {
        return this.m_chNationality;
    }

    public void Nationa(String nationa) {
        this.m_chNationality = nationa;
    }

    public boolean writeToFile(OutputStream out) {
        Util.writeLong(out, this.m_size);
        Util.writeLong(out, this.m_version);
        Util.writeLong(out, this.m_height * 100);
        Util.writeLong(out, this.m_weight * 100);
        Util.writeLong(out, this.m_age);
        Util.writeLong(out, this.m_sex);
        Util.writeString(out, this.m_chName, 128);
        Util.writeString(out, this.m_chComment, 256);
        Util.writeString(out, this.m_chNationality, 32);
        return true;
    }
}
