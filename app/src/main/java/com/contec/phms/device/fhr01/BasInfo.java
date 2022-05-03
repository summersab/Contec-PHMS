package com.contec.phms.device.fhr01;

import com.alibaba.cchannel.push.receiver.CPushMessageCodec;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import u.aly.bs;

public class BasInfo {
    public String BirthTimes;
    public String ChildMonths;
    public String Children;
    public String Custom1;
    public String Custom4;
    public String Custom5;
    public String Custom6;
    public String DATATIME;
    public String PregnantTimes;
    public String SID;
    public String TYPE;
    public String Tel;
    public String address;
    public String bCard;
    public String birthday;
    public String bloodtype;
    public String bprintsign;
    public String customtitle;
    public String datatype;
    public String description;
    public String height = "165";
    public String nAge;
    public String nation = "cn";
    public String personid = "015611733173";
    public String phone;
    public String remark;
    public String sex = "0";
    public String smplen;
    public String titletype;
    public String username = "samho";
    public String usertype;
    public String weight = "54";

    public BasInfo() {
        try {
            this.datatype = new String("14".getBytes(CPushMessageCodec.GBK), CPushMessageCodec.GBK);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        this.birthday = "1987-05-14";
        this.usertype = "1";
        this.SID = bs.b;
    }

    public void write(String filePath, String fileName) {
        File _file = new File(filePath);
        if (!_file.exists()) {
            _file.mkdirs();
        }
        String str = "personid=" + this.personid + ";username=" + this.username + ";height=" + this.height + ";weight=" + this.weight + ";bloodtype=" + this.bloodtype + ";sex=" + this.sex + ";birthday=" + this.birthday + ";nation=" + this.nation + ";description=" + this.description + ";datatype=" + this.datatype + ";phone=" + this.phone + ";address=" + this.address + ";usertype=" + this.usertype + ";bprintsign=" + this.bprintsign + ";titletype=" + this.titletype + ";customtitle=" + this.customtitle + ";bCard=" + this.bCard + ";TYPE=" + this.TYPE + ";SID=" + this.SID + ";Tel=" + this.Tel + ";nAge=" + this.nAge + ";Children=" + this.Children + ";PregnantTimes=" + this.PregnantTimes + ";BirthTimes=" + this.BirthTimes + ";ChildMonths=" + this.ChildMonths + ";DATATIME=" + this.DATATIME + ";remark=" + this.remark + ";smplen=" + this.smplen + ";Custom1=" + this.Custom1 + ";Custom4=" + this.Custom4 + ";Custom5=" + this.Custom5 + ";Custom6=" + this.Custom6 + ";";
        try {
            OutputStreamWriter os = new OutputStreamWriter(new FileOutputStream(_file + File.separator + fileName), CPushMessageCodec.GBK);
            os.write(new String(str.getBytes(CPushMessageCodec.GBK), CPushMessageCodec.GBK));
            os.close();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e2) {
            e2.printStackTrace();
        }
    }
}
