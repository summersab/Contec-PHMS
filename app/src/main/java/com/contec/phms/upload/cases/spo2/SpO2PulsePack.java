package com.contec.phms.upload.cases.spo2;

import java.io.OutputStream;
import java.io.Serializable;

public class SpO2PulsePack implements Serializable {
    private static final long serialVersionUID = 1;
    public int mPI;
    public int mPI_Type;
    public byte mPulse;
    public byte mSpO2;

    public SpO2PulsePack() {
    }

    public SpO2PulsePack(byte[] pack) {
        this.mSpO2 = pack[0];
        this.mPulse = pack[1];
    }

    public boolean writeToFile(OutputStream out) {
        if (this.mPI_Type == 0) {
            Util.writeShort(out, this.mPI);
        }
        Util.writeChar(out, (int) this.mSpO2);
        Util.writeChar(out, (int) this.mPulse);
        return true;
    }
}
