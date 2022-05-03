package com.contec.phms.upload.cases.common;

import com.contec.phms.util.CLog;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import u.aly.bs;

public class MakeBaseCase {
    public String TYPE = "Portable";
    public String birthday = bs.b;
    public String bloodtype = bs.b;
    public int datatype = 3;
    public String description = bs.b;
    public int height = 0;
    public String nation = bs.b;
    public String personid = bs.b;
    public int sex = 0;
    public String username = bs.b;
    public int weight = 0;

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
            String strtemp = String.format("datatype=%d;personid=%s;height=%d;username=%s;weight=%d;bloodtype=%s;sex=%d;birthday=%s;nation=%s;description=%s;TYPE=%s;", new Object[]{Integer.valueOf(this.datatype), this.personid, Integer.valueOf(this.height), this.username, Integer.valueOf(this.weight), this.bloodtype, Integer.valueOf(this.sex), this.birthday, this.nation, this.description, this.TYPE});
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
}
