package cn.com.contec.jar.cases;

import java.io.OutputStream;
import java.io.Serializable;

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

    public void initInfo1(byte[] pack) {
        this.info1 = new byte[pack.length];
        this.info1 = pack;
        this.mFVC = ((double) (((pack[2] & 255) | ((pack[3] & 255) << 8)) & 65535)) / 100.0d;
        this.mFEV1 = ((double) (((pack[4] & 255) | ((pack[5] & 255) << 8)) & 65535)) / 100.0d;
        this.mPEF = ((double) (((pack[6] & 255) | ((pack[7] & 255) << 8)) & 65535)) / 100.0d;
        this.mFEV1Per = (this.mFEV1 / this.mFVC) * 100.0d;
        this.mLength = (pack[8] & 255) << 8;
    }

    public void initInfo2(byte[] pack) {
        this.info2 = new byte[pack.length];
        this.info2 = pack;
        this.mFEF25 = ((double) ((pack[2] & 255) | ((pack[3] & 255) << 8))) / 100.0d;
        this.mFEF75 = ((double) ((pack[4] & 255) | ((pack[5] & 255) << 8))) / 100.0d;
        this.mFEF2575 = ((double) ((pack[6] & 255) | ((pack[7] & 255) << 8))) / 100.0d;
        this.mFEV05 = 0.0d;
        this.mLength |= pack[8] & 255;
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
