package com.contec.phms.upload.cases.pm85;

import android.util.Log;
import com.alibaba.cchannel.push.receiver.CPushMessageCodec;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import u.aly.bs;

public class MakeBaseCase {
    public String Address = bs.b;
    public String Age = "0";
    public String EventTime = bs.b;
    public String EvtType = bs.b;
    public String SampleLength = bs.b;
    public String Tel = bs.b;
    public String bloodtype = bs.b;
    public int datatype = 11;
    public String description = bs.b;
    public String height = "0";
    public String personid = bs.b;
    public String sex = "0";
    public String username = bs.b;
    public String weight = "0";

    public int TodoMakeXml(String filename) {
        return 0;
    }

    public int ToDo(String filename) {
        if (filename.trim().length() == 0) {
            return -1;
        }
        try {
            new String();
            String strtemp = String.format("datatype=%d;personid=%s;height=%s;username=%s;weight=%s;bloodtype=%s;sex=%s;age=%s;Tel=%s;Address=%s;description=%s;EventTime=%s;EvtType=%s;SampleLength=%s;", new Object[]{Integer.valueOf(this.datatype), this.personid, this.height, this.username, this.weight, this.bloodtype, this.sex, this.Age, this.Tel, this.Address, this.description, this.EventTime, this.EvtType, this.SampleLength});
            OutputStreamWriter os = new OutputStreamWriter(new FileOutputStream(filename), CPushMessageCodec.GBK);
            os.write(new String(strtemp.getBytes(CPushMessageCodec.GBK), CPushMessageCodec.GBK));
            os.close();
            return 0;
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("MakeBaseCase", filename);
            return -1;
        }
    }
}
