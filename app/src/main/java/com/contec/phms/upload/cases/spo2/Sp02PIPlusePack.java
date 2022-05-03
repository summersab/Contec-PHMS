package com.contec.phms.upload.cases.spo2;

import java.io.OutputStream;

public class Sp02PIPlusePack extends SpO2PulsePack {
    private static final long serialVersionUID = 1;

    public boolean writeToFile(OutputStream out) {
        Util.writeShort(out, this.mPI);
        Util.writeChar(out, (int) this.mSpO2);
        Util.writeChar(out, (int) this.mPulse);
        return true;
    }
}
