package com.contec.sp10.code;

import java.io.Serializable;
import java.util.ArrayList;

public class DeviceDataJar implements Serializable {
    private static final long serialVersionUID = 1;
    public long[] mData;
    public ArrayList<StructData> mDataList;
    public ParamInfo mParamInfo;
    public Sp10PatientInfo mPatientInfo;
    public int mScaler;
    public SerialNumber mSerial;
    public StructData mStructData;
}
