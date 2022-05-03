package com.contec.phms.upload.cases.BC01;

import com.contec.phms.util.CLog;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import u.aly.bs;

public class MakeBaseCase_BC01 {
    public String age;
    public String birthday;
    public String bloodsugar;
    public String bloodtype;
    public String checktime;
    public String datatype;
    public String description;
    public String device;
    public String height;
    public String language;
    public String mobile;
    public String nation;
    public String personid;
    public String sex;
    public String username;
    public String weight;

    public int TodoMakeXml(String filename) {
        return 0;
    }

    public int ToDo(String filename) {
        if (filename.trim().length() == 0) {
            return -1;
        }
        try {
            File fp = new File(filename);
            if (!fp.exists()) {
                fp.createNewFile();
            }
            new String();
            String strtemp = String.format("personid=%s;height=%s;username=%s;weight=%s;bloodtype=%s;sex=%s;birthday=%s;nation=%s;description=%s;datatype=%s;checktime=%s;mobile=%s;device=%s;age=%s;bloodsugar=%s;language=%s;", new Object[]{this.personid, this.height, this.username, this.weight, this.bloodtype, this.sex, this.birthday, this.nation, this.description, this.datatype, this.checktime, this.mobile, this.device, this.age, this.bloodsugar, this.language});
            OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(fp));
            osw.write(strtemp, 0, strtemp.length());
            osw.flush();
            osw.close();
            return 0;
        } catch (Exception e) {
            e.printStackTrace();
            CLog.i("MakeBaseCase", filename);
            return -1;
        }
    }

    public MakeBaseCase_BC01() {
        this.personid = bs.b;
        this.height = bs.b;
        this.username = bs.b;
        this.weight = bs.b;
        this.bloodsugar = bs.b;
        this.sex = bs.b;
        this.birthday = bs.b;
        this.nation = bs.b;
        this.description = bs.b;
        this.datatype = "1";
        this.checktime = bs.b;
        this.mobile = bs.b;
        this.device = "ECG7";
        this.age = bs.b;
        this.bloodsugar = bs.b;
        this.language = "Chinese";
    }
}
