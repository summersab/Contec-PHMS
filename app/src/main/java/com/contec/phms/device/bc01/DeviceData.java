package com.contec.phms.device.bc01;

import com.example.bm77_bc_code.BC401_Struct;
import java.util.ArrayList;

public class DeviceData extends com.contec.phms.device.template.DeviceData {
    private static final long serialVersionUID = 1;
    public ArrayList<BC401_Struct> mDataList = new ArrayList<>();

    public void initInfo() {
    }

    public void setSaveDate() {
    }
}
