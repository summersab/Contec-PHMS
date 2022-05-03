package cn.com.contec.jar.sp10w;

import java.io.Serializable;
import java.util.ArrayList;
import cn.com.contec.jar.cases.ParamInfo;
import cn.com.contec.jar.cases.SerialNumber;
import cn.com.contec.jar.cases.Sp10PatientInfo;
import cn.com.contec.jar.cases.StructData;

public class DeviceDataJar implements Serializable {
    private static final long serialVersionUID = 1;
    public ArrayList<StructData> mDataList;
    public ParamInfo mParamInfo;
    public Sp10PatientInfo mPatientInfo;
    public SerialNumber mSerial;
    public StructData mStructData;
}
