package com.contec.sp10.code;

import android.util.Log;
import java.io.OutputStream;
import java.io.Serializable;
import u.aly.dp;

public class ParamInfo implements Serializable {
    private static final long serialVersionUID = 1;
    public byte[] info1;
    public byte[] info2;
    public double mFEF25;
    public double mFEF2575;
    public double mFEF50;
    public double mFEF75;
    public double mFEV05;
    public double mFEV1;
    public double mFEV1Per;
    public double mFVC;
    public int mLength;
    public double mPEF;

    public String initInfo1(byte[] pack) {
        this.info1 = new byte[pack.length];
        this.info1 = pack;
        this.mFVC = ((double) ((((((byte) (pack[16] | ((pack[9] & 64) << 1))) & 255) << 8) | (((byte) (pack[15] | ((pack[9] & 32) << 2))) & 255)) & 65535)) / 100.0d;
        this.mFEV1 = ((double) ((((((byte) (pack[19] | ((pack[17] & 2) << 6))) & 255) << 8) | (((byte) (pack[18] | ((pack[17] & 1) << 7))) & 255)) & 65535)) / 100.0d;
        this.mPEF = ((double) ((((((byte) (pack[22] | ((pack[17] & dp.n) << 3))) & 255) << 8) | (((byte) (pack[21] | ((pack[17] & 8) << 4))) & 255)) & 65535)) / 100.0d;
        this.mFEV1Per = (double) (pack[20] | ((pack[17] & 4) << 5));
        this.mLength = (byte) (pack[30] | ((pack[25] & dp.n) << 3));
        Log.e("肺活量信息值====", "值：：FVC:" + this.mFVC + "FEV1:" + this.mFEV1 + "PEF:" + this.mPEF + "FEV1Per:" + this.mFEV1Per);
        return "FVC:" + this.mFVC + "FEV1:" + this.mFEV1 + "PEF:" + this.mPEF + "FEV1Per:" + this.mFEV1Per;
    }

    public String initInfo2(byte[] pack) {
        this.info2 = new byte[pack.length];
        this.info2 = pack;
        this.mFEF25 = ((double) ((((((byte) (pack[24] | ((pack[17] & 64) << 1))) & 255) << 8) | (((byte) (pack[23] | ((pack[17] & 32) << 2))) & 255)) & 65535)) / 100.0d;
        this.mFEF75 = ((double) ((((((byte) (pack[27] | ((pack[25] & 2) << 6))) & 255) << 8) | (((byte) (pack[26] | ((pack[25] & 1) << 7))) & 255)) & 65535)) / 100.0d;
        this.mFEF2575 = ((double) ((((((byte) (pack[29] | ((pack[25] & 8) << 4))) & 255) << 8) | (((byte) (pack[28] | ((pack[25] & 4) << 5))) & 255)) & 65535)) / 100.0d;
        this.mFEV05 = 0.0d;
        this.mLength |= pack[31] | ((pack[25] & 32) << 2);
        Log.e("肺活量信息值====", "值：：FEF25:" + this.mFEF25 + "mFEF75:" + this.mFEF75 + "FEF2575:" + this.mFEF2575);
        return new StringBuilder().append(this.mFEF25).append(this.mFEF75).append(this.mFEF2575).toString();
    }

    public void save(OutputStream out) {
        SaveHelper.saveDouble(out, this.mFVC);
        SaveHelper.saveDouble(out, this.mFEV1);
        SaveHelper.saveDouble(out, this.mPEF);
        SaveHelper.saveDouble(out, this.mFEV1Per);
        SaveHelper.saveDouble(out, this.mFEF25);
        SaveHelper.saveDouble(out, this.mFEF50);
        SaveHelper.saveDouble(out, this.mFEF75);
        SaveHelper.saveDouble(out, this.mFEF2575);
        SaveHelper.saveInt(out, this.mLength);
    }
}
