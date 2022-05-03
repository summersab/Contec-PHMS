package com.contec.phms.upload.cases.spo2;

import java.io.OutputStream;

public class SpO2NOPIPulsePack extends SpO2PulsePack {
    private static final long serialVersionUID = 1;

    public boolean writeToFile(OutputStream out) {
        Util.writeChar(out, (int) this.mSpO2);
        Util.writeChar(out, (int) this.mPulse);
        return true;
    }
}
